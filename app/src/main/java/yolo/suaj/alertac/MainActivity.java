package yolo.suaj.alertac;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private MainActivityFragment mainActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffdb2020));
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.barra));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ajustes:

                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_configuracion:

                Intent intentin = new Intent(getApplicationContext(), Prefs.class);
                startActivity(intentin);

                return true;

            case R.id.action_salir:

                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
