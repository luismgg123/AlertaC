package yolo.suaj.alertac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MainActivityFragment mainActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Visualizar el Fragment
        if(savedInstanceState == null) {

            mainActivityFragment = new MainActivityFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainActivityFragment)
                    .commit();

        }else {

            //Set fragment from restored info
            mainActivityFragment = (MainActivityFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);

        }

    }



}
