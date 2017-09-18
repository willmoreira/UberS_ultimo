package com.example.williammoreira.ubers_ultimo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LatLng SJC = new LatLng(-23.1946027,-45.853515);

    private Connection con = new Connection();
    private List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //obter o mapa da view
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //set o callback
        mapFragment.getMapAsync(this);

    }


    //metodo callback
    @Override
    public void onMapReady(GoogleMap map) {

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SJC,11.4f));


        try {
            this.markers = con.getData();

            for (Marker marker : this.markers) {
                if (marker.getDesc().equals("upa")) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(marker.getLat(), marker.getLon()))
                            .title(marker.getDesc())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pronto)));
                }
                if (marker.getDesc().equals("ubs")) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(marker.getLat(), marker.getLon()))
                            .title(marker.getDesc())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ubs)));
                }
                if (marker.getDesc().equals("hosp")) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(marker.getLat(), marker.getLon()))
                            .title(marker.getDesc())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.hosp)));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener gps = new GPS(map, this.markers);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gps);


    }
}