package yolo.suaj.alertac;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;


public class MainActivityFragment extends Fragment {
    private TabHost tabHost;

    private Button bmaps;

    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);

        //Codificación

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


}
