package yolo.suaj.alertac;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    private LocationManager locManager;
    private LocationListener locListener;
    private double latitud;
    private double longitud;
    private double presicion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        comienzaLocalizacion();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng sydney = new LatLng(latitud,longitud);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Ubicacion Actual"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));

    }




    //////////////////////////////////////////////////////////////////

    private void comienzaLocalizacion() {
        //Obtenemos una referencia al servicio de localizacion del sistema
        locManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        try{
        Location ubicacion =
                locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mostrarUbicacion(ubicacion);
        }catch (Exception e){

        }

        locListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mostrarUbicacion(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onProviderEnabled(String provider) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onProviderDisabled(String provider) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        try {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    30000, 0, locListener);
        }catch(Exception e){

        }
    }





    private void mostrarUbicacion(Location location) {
        if (location != null){
            longitud=location.getLongitude();
            latitud=location.getLatitude();
            presicion=location.getAccuracy();
            Log.e("1LATITUD - 1LONGITUD:", longitud + "--" + latitud);
        }
    }
}
