package in.www.myapplication3;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Location curentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private GoogleMap mMap;
    private Canvas canvas;
    TextView b1, b2, v1, v2, km, vv;
    String kisho1, kisho2, lik1, lik2;
    private boolean shadow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        Intent intent = getIntent();
        final String fullname = intent.getStringExtra("name");
        TextView tx = findViewById(R.id.from);

        tx.setText(fullname);
        TextView goplace=findViewById(R.id.goplcae);
        goplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MapsActivity.this,Sreach.class);
                startActivity(i);
            }
        });


      /*  Toast.makeText(getApplicationContext(), fullname, Toast.LENGTH_SHORT).show();*/
        Button bt = findViewById(R.id.sub);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerOptions mo = new MarkerOptions();
                List<Address> addressList = null;

                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try {
                    addressList = geocoder.getFromLocationName(fullname, 5);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < addressList.size(); i++) {
                    final Address myaddress = addressList.get(i);
                    LatLng latLng = new LatLng(myaddress.getLatitude(), myaddress.getLongitude());
                    Toast.makeText(getApplicationContext(), myaddress.getLatitude() + "" + myaddress.getLongitude(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), myaddress.getLatitude() + "" + myaddress.getLongitude(), Toast.LENGTH_SHORT).show();
                    v1 = findViewById(R.id.vinay1);
                    v1.setText(String.valueOf(myaddress.getLatitude()));
                    v2 = findViewById(R.id.vinay2);
                    lik1 = String.valueOf(myaddress.getLatitude()).trim();
                    lik2 = String.valueOf(myaddress.getLongitude()).trim();


                    v2.setText(String.valueOf(myaddress.getLongitude()));
                    mo.position(latLng);
                    mo.title("your sreac result");
                    mo.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin));

                    mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }


               int Radius = 6371;// radius of earth in Km

               double lat1=Double.valueOf(kisho1);
                double lat2 = Double.valueOf(lik1);
                double lon1 = Double.valueOf(kisho2);
                double lon2 = Double.valueOf(lik2);

                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(lat1,lon1), new LatLng(lat2,lon2))
                        .width(5)
                        .color(Color.BLUE));
                double dLat = Math.toRadians(lat2 - lat1);
                double dLon = Math.toRadians(lon2 - lon1);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                        * Math.sin(dLon / 2);
                double c = 2 * Math.asin(Math.sqrt(a));
                double valueResult = Radius * c;
                double kmm = valueResult / 1;
                DecimalFormat newFormat = new DecimalFormat("####");
                int kmInDec = Integer.valueOf(newFormat.format(kmm));
                double meter = valueResult % 1000;
                Toast.makeText(getApplicationContext(),"km1 "+ kmInDec,Toast.LENGTH_SHORT).show();
               TextView  km1 =findViewById(R.id.km);
               km1.setText(String.valueOf(kmInDec+"disatnce"));





            }
        });
    }


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);


            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    curentLocation = location;
                    Toast.makeText(getApplicationContext(), curentLocation.getLatitude() + "" + curentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);

                    mapFragment.getMapAsync(MapsActivity.this);
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        TextView   from1 = findViewById(R.id.to);
        LatLng latLng = new LatLng(curentLocation.getLatitude(), curentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        b1 = findViewById(R.id.balu1);
        b2 = findViewById(R.id.balu2);
        b1.setText(String.valueOf(curentLocation.getLatitude()));
        b2.setText(String.valueOf(curentLocation.getLongitude()));
        kisho1 = String.valueOf(curentLocation.getLatitude()).trim();
        kisho2 = String.valueOf(curentLocation.getLongitude()).trim();
        Toast.makeText(getApplicationContext(), "kishoe" + kisho1, Toast.LENGTH_SHORT).show();

        markerOptions.position(latLng);
        markerOptions.title("current location");
        markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin2));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));
        mMap.addMarker(markerOptions);
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> address1 = null;
        try {
            address1 = geocoder.getFromLocation(curentLocation.getLatitude(), curentLocation.getLongitude(), 1);
            Toast.makeText(getApplicationContext(), String.valueOf(address1), Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
        for (int i = 0; i < address1.size(); i++) {
            /* final Address myaddress12=address1.get(i);*/
            String ast = address1.get(i).getAdminArea() + "" + address1.get(i).getAddressLine(i);
            from1.setText(ast);
            Toast.makeText(getApplicationContext(), ast, Toast.LENGTH_SHORT).show();


        }
    }


        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            switch (requestCode) {
                case REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        fetchLocation();

                    }
            }


        }
        public BitmapDescriptor bitmapDescriptorFromVector (Context context,int vectorResId)
        {
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.draw(canvas);
            return BitmapDescriptorFactory.fromBitmap(bitmap);

        }
    }



