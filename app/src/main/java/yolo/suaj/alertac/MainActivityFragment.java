package yolo.suaj.alertac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivityFragment extends Fragment {
    private TabHost tabHost;

    private Button bHistorial;
    private ImageButton btnSOS, bSMS, bFoto,  btnShare, bUp;
    private ImageView imageViewPhoto;

    private Historial historial;
    private TextView textResult;

    private static final int LOAD_PICTURE = 2;
    private static final String TAG = "VoiceRecognition";

    Uri imageUri;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);

        //CODIFICACIÓN WEBVIEW VOZ

        imageViewPhoto = (ImageView) view.findViewById(R.id.imageViewPhoto);


        //CODIFICACIÓN WEBVIEW NOTICIAS

        WebView webViewNoticia = (WebView) view.findViewById(R.id.webViewNoticia);
        webViewNoticia.loadUrl("https://news.google.com.pe/news?tab=wn&ei=xQBQVrSFMYj1mAHXqLjoCQ&ved=0CAoQqS4oCg#0");



//////////////////////////BOTON HISTORIAL///////////////////////////////////////

        textResult = (TextView) view.findViewById(R.id.textResult);

     this.createDB();


        bHistorial = (Button) view.findViewById(R.id.bHistorial);
        bHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textResult.setText("");
                Cursor c = listarUsuarios(conexionDB());
                if (c.moveToFirst()) {
                    do {
                        textResult.append(c.getString(0) + " : " + c.getString(1) + " -" + c.getInt(2) + " - " + c.getString(3) + "\n");
                    } while (c.moveToNext());
                }


            }


        });


/////////////////////////BOTON VOZ/////////////////////////////////////
        btnSOS =(ImageButton) view.findViewById(R.id.btnSOS);
        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
                final String a = getPref("pref_contacto", getContext());
                final  String b= getPref("pref_mensaje", getContext());
                EnvioSMS envioSMS = new EnvioSMS(a, a, "AUXILIO");


            }
        });


//////////////////////////BOTON SMS///////////////////////////////////////

        bSMS = (ImageButton) view.findViewById(R.id.bSMS);
        bSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OBTENER VALORES DE PREFERENCIAS
                final String a = getPref("pref_contacto", getContext());
                final  String b= getPref("pref_mensaje", getContext());

               EnvioSMS envioSMS = new EnvioSMS(a, a, b);

                // AÑADIR AL HISTORIAL
                String fecha = obtenerFecha();
                conexionDB().execSQL("INSERT INTO HISTORIAL (fecha, accion, numero, mensaje) VALUES ('"+ fecha +"', 'SMS' ,"+ Integer.parseInt(a) +" ,'" + b + "')");
                conexionDB().close();


            }
        });


//////////////////////////BOTON FOTO///////////////////////////////////////


        bFoto = (ImageButton) view.findViewById(R.id.bFoto);
        bFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getActivity().getPackageManager())!=null) {
                    startActivityForResult(intent, TAKE_PICTURE);
                }
            }


        });




//////////////////////////BOTON COMPARTIR/// :v////////////////////////////////////


                btnShare = (ImageButton) view.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareit();
            }


        });





        //////////////////////////BOTON SUBIR// :v////////////////////////////////////


        bUp= (ImageButton) view.findViewById(R.id.bUp);
        bUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"),LOAD_PICTURE);
            }


        });








////////////////////////// TABHOST ///////////////////////////////////////

        Resources resources = getResources();
        tabHost = (TabHost) view.findViewById(R.id.tabHost);
        tabHost.setup();

        //Primer TAB
        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab1");
        spec1.setContent(R.id.tab1);

        //Custom tab Indicator: Tiene imagen y texto

        View tabIndicator = LayoutInflater.from(this.getContext()).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        ((TextView) tabIndicator.findViewById(R.id.title)).setText("VOZ");
        ((ImageView) tabIndicator.findViewById(R.id.icon)).setImageResource(R.drawable.micro);
        spec1.setIndicator(tabIndicator);

        tabHost.addTab(spec1);

        //Segundo TAB
        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab2");
        spec2.setContent(R.id.tab2);

        //Custom tab Indicator: Tiene imagen y texto

        View tabIndicator2 = LayoutInflater.from(this.getContext()).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        ((TextView) tabIndicator2.findViewById(R.id.title)).setText("SMS");
        ((ImageView) tabIndicator2.findViewById(R.id.icon)).setImageResource(R.drawable.sms);
        spec2.setIndicator(tabIndicator2);

        tabHost.addTab(spec2);

        //Tercer TAB
        TabHost.TabSpec spec3 = tabHost.newTabSpec("Tab3");
        spec3.setContent(R.id.tab3);

        //Custom tab Indicator: Tiene imagen y texto

        View tabIndicator3 = LayoutInflater.from(this.getContext()).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        ((TextView) tabIndicator3.findViewById(R.id.title)).setText("FOTO");
        ((ImageView) tabIndicator3.findViewById(R.id.icon)).setImageResource(R.drawable.foto);
        spec3.setIndicator(tabIndicator3);
        tabHost.addTab(spec3);

        //Cuarto TAB
        TabHost.TabSpec spec4 = tabHost.newTabSpec("Tab4");
        spec4.setContent(R.id.tab4);

        //Custom tab Indicator: Tiene imagen y texto

        View tabIndicator4 = LayoutInflater.from(this.getContext()).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
        ((TextView) tabIndicator4.findViewById(R.id.title)).setText("NOTICIA");
        ((ImageView) tabIndicator4.findViewById(R.id.icon)).setImageResource(R.drawable.noticia);
        spec4.setIndicator(tabIndicator4);
        tabHost.addTab(spec4);





        //INICIALIZACIÓN DE COLORES

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i)
                    .setBackgroundColor(Color.parseColor("#333333")); // un selected
        }

        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                .setBackgroundColor(Color.parseColor("#d5a208")); //selected


        //////////////////////////COLOR DE LOS TABS ///////////////////////////////////////



        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String arg0) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#333333")); // un selected
                }

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#d5a208")); // selected

            }
        });

        return view;
    }




    //////////////////////////METODOS USADOS EN BOTONES ///////////////////////////////////////


    //VOZ

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga:, Auxilio ");
        startActivityForResult(i, VOICE_RECOGNITION_REQUEST_CODE);
    }



    static final int TAKE_PICTURE = 1;


    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle = data.getExtras();
                Bitmap bitmap;
                try {
                    bitmap = (Bitmap) bundle.get("data");

                    imageViewPhoto.setImageBitmap(bitmap);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
                    Log.e("Camera", e.toString());
                }
            }



        if(requestCode == LOAD_PICTURE && resultCode == Activity.RESULT_OK){
            imageUri = data.getData();
            imageViewPhoto.setImageURI(data.getData());
        }
            if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
                ArrayList<String> matches = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                String[] palabras = matches.get(0).toString().split(" ");
                palabras[0] = "Auxilio";
                if (palabras[0].equals("Auxilio")) {

                    // OBTENER VALORES DE PREFERENCIAS
                    final String a = getPref("pref_contacto", getContext());
                    final String b = getPref("pref_mensaje", getContext());

                    //  EnvioSMS envioSMS = new EnvioSMS(a, a, "AUXILIO");

                    // AÑADIR AL HISTORIAL
                    String fecha = obtenerFecha();
                    conexionDB().execSQL("INSERT INTO HISTORIAL (fecha, accion, numero, mensaje) VALUES ('" + fecha + "', 'VOZ' ," + Integer.parseInt(a) + " ,'AUXILIO')");
                    conexionDB().close();

                }
            }

    }


    // OBTENER PREFERENCIAS

    public static String getPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }


    //OBTENER FECHA

    public String obtenerFecha() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDate = df.format(c.getTime());
// Now formattedDate have current date/time
       return formattedDate;

    }



    //Método Foto

    private void shareit(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/png");
        imageViewPhoto.setDrawingCacheEnabled(true);
        Bitmap image=imageViewPhoto.getDrawingCache();
        Uri uri=getLocalBitmapUri(image);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Compartir imagen por..."));
        //ImageView.destroyDrawingCache();
    }
    public Uri getLocalBitmapUri(Bitmap bm) {
        Bitmap bmp = bm;
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;

    }





    // INICIALIZACION DE BD

    private void createDB(){


            Historial userSql = new Historial(this.getContext(), "BDUSER", null, 1);
            SQLiteDatabase conex = userSql.getWritableDatabase();
            conex.close();

    }

    private SQLiteDatabase conexionDB(){
        Historial helper =  new Historial(this.getContext(), "BDUSER", null, 1);
        SQLiteDatabase conex =  helper.getWritableDatabase();
        return conex;
    }

    private Cursor listarUsuarios(SQLiteDatabase access){
        String[] camposDevolver = new String[]{"fecha", "accion", "numero","mensaje"};
        Cursor c = access.query("HISTORIAL", camposDevolver, null, null, null, null, null,"15");

        return c;
    }



}
