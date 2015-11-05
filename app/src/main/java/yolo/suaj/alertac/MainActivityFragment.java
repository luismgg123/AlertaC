package yolo.suaj.alertac;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TabHost;

import java.util.ArrayList;


public class MainActivityFragment extends Fragment {
    private TabHost tabHost;

    private WebView parrafo_1, parrafo_2;
    private Button bmaps, btnSOS;

    private static final String TAG = "VoiceRecognition";

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);

        //Codificación

        parrafo_1=(WebView) view.findViewById(R.id.parrafo_1);
      //  parrafo_2=(WebView) view.findViewById(R.id.parrafo_2);

        String txt_paragraph_1 = "<html><body><div style=text-align:justify;font-size:12px;color:#000000;>"+
                "<ul>"+
                "<li>Partida de Nacimiento original</li>"+
                "<li>Certificado de Estudios Secundarios Original</li>"+
                "<li>Dos fotografías tamaño carnet</li>"+
                "<li>Copia de D.N.I</li>"+
                "<li>Pago por el Derecho de examen de admisión incluido prospecto. El pago se realiza en la Cuenta en soles del Banco de la Nación a la Cta. Cte. N° 0000284475</li>" +
                "</ul></div></body></html>";
       parrafo_1.loadData(txt_paragraph_1, "text/html; charset=utf-8","UTF-8");

        btnSOS =(Button) view.findViewById(R.id.btnSOS);
        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();


            }
        });
        bmaps= (Button) view.findViewById(R.id.bmaps);
        bmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MapsActivity.class);
                startActivity(i);
            }
        });


                //TabHost
        Resources resources = getResources();
        tabHost = (TabHost) view.findViewById(R.id.tabHost);
        tabHost.setup();

        //Primer TAB
        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Voz");
        tabHost.addTab(spec1);

        //Segundo TAB
        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("SMS");
        tabHost.addTab(spec2);

        //Tercer TAB
        TabHost.TabSpec spec3 = tabHost.newTabSpec("Tab3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Foto");
        tabHost.addTab(spec3);

        //Cuarto TAB
        TabHost.TabSpec spec4 = tabHost.newTabSpec("Tab4");
        spec4.setContent(R.id.tab4);
        spec4.setIndicator("Noticia");
        tabHost.addTab(spec4);

        return view;
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga:, Auxilio ");
        startActivityForResult(i, VOICE_RECOGNITION_REQUEST_CODE);
    }
    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra
                    (RecognizerIntent.EXTRA_RESULTS);
            String[] palabras = matches.get(0).toString().split(" ");
            palabras[0] = "Auxilio";
            if (palabras[0].equals("Auxilio")) {
                EnvioSMS envioSMS = new EnvioSMS("999956103", "999956103", "AUXILIO");
            }
        }
    }


}
