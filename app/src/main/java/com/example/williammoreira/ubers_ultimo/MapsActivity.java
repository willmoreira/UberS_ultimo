package com.example.williammoreira.ubers_ultimo;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;

        import android.view.Gravity;
        import android.view.View;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;

        import org.json.JSONException;

        import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LatLng SJC = new LatLng(-23.2146027, -45.8854213);
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

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SJC, 12));
        try {
            this.markers = con.getData();
            map.setInfoWindowAdapter(new InfoWindowAdapter() {
                @Override
                public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {

                    LinearLayout info = new LinearLayout(getApplicationContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getApplicationContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getApplicationContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
            for (Marker marker : this.markers) {
                if (marker.getDesc().equals("upa")) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(marker.getLat(), marker.getLon()))
                            .title(marker.getName())
                            .snippet(marker.getEndereco()+"\n"+marker.getHorarioFunc()+"\n"+marker.getTel())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pronto)));
                }
                if (marker.getDesc().equals("ubs")) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(marker.getLat(), marker.getLon()))
                            .title(marker.getName())
                            .snippet(marker.getEndereco()+"\n"+marker.getHorarioFunc()+"\n"+marker.getTel())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ubs)));
                }
                if (marker.getDesc().equals("hosp")) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(marker.getLat(), marker.getLon()))
                            .title(marker.getName())
                            .snippet(marker.getEndereco()+"\n"+marker.getHorarioFunc()+"\n"+marker.getTel())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.hosp)));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener gps = new Model(map, this.markers);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.



        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 500 , gps);
        // tempo em milissegundos para atualização da localização e distância mínima entre as atualizações de localização..
    }
}
