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
    private  static  final int REQUEST_CODE=101;
    EditText editText1;
    private GoogleMap mMap;
    private Canvas canvas;
    TextView b1,b2,v1,v2,km;
    String kisho1,kisho2,lik1,lik2;
    private boolean shadow;

    public MapsActivity() {
    }



/*    private GoogleMap mMap;
    private  static final  int REQUEST_CODE=101;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private  LocationListener locationListener;
    double latitude,logitude;
    private  Location location,currentLocation123;

    protected LocationManager locationManager;


    double end_latitude,end_logitude;
    double start_latitude,start_logitude;
    private Marker currentLocationMarker;
    public  static  final  int REQUEST_LOCATION_CODE =99;
    FusedLocationProviderClient fusedLocationProviderClient;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient =LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        Intent intent=getIntent();
        String fullname=intent.getStringExtra("text");
        Toast.makeText(getApplicationContext(),fullname,Toast.LENGTH_SHORT).show();

        /*ImageView go=findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),PlacesActivity.class);
                startActivity(i);
            }
        });*/
        Button bt=findViewById(R.id.sub);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
     Toast.makeText(getApplicationContext(),"balu"+kisho1,Toast.LENGTH_SHORT).show();


                MarkerOptions mo = new MarkerOptions();
                List<Address> addressList=null;
                editText1=findViewById(R.id.from);
                String to=editText1.getText().toString().trim();
                Geocoder geocoder= new Geocoder(MapsActivity.this);
                try {
                    addressList=geocoder.getFromLocationName(to,5);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<addressList.size();i++)
                {
                    final Address myaddress=addressList.get(i);
                    LatLng latLng = new LatLng(myaddress.getLatitude(),myaddress.getLongitude());
                   /* Toast.makeText(getApplicationContext(),myaddress.getLatitude()+""+myaddress.getLongitude(),Toast.LENGTH_SHORT).show();*/
                  /*  Toast.makeText(getApplicationContext(),"Long: " + myaddress.getLongitude() ,Toast.LENGTH_SHORT).show();*/
                   v1=findViewById(R.id.vinay1);
                    v1.setText(String.valueOf(myaddress.getLatitude()));
                    v2=findViewById(R.id.vinay2);
                    lik1=String.valueOf(myaddress.getLatitude()).trim();
                    lik2=String.valueOf(myaddress.getLongitude()).trim();



                    v2.setText(String.valueOf(myaddress.getLongitude()));
                    mo.position(latLng);
                    mo.title("your sreac result");
                    mo.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_pin));

                   mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }


                int Radius = 6371;// radius of earth in Km

                double lat1=Double.valueOf(kisho1);
                double lat2 = Double.valueOf(lik1);
                double lon1 = Double.valueOf(kisho2);
                double lon2 = Double.valueOf(lik2);
               /* double lat1 = 17.4390375;
                double lat2 = 38.9072;
                double lon1 = 78.3983702;
                double lon2 = 77.0369;*/
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

      /* fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fetchLastLpcation();
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            checkLocationPremission();
        }

        //code for getting current location

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
        Button bt=findViewById(R.id.bt_sreach);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txt=findViewById(R.id.TF_location);
                String location = txt.getText().toString();
                mMap.clear();
                List<Address> addressList=null;
                MarkerOptions mo = new MarkerOptions();
                if(!location.equals(""))
                {
                    Geocoder geocoder= new Geocoder(MapsActivity.this);
                    try {
                        addressList=geocoder.getFromLocationName(location,5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for(int i=0;i<addressList.size();i++)
                    {
                       final Address myaddress=addressList.get(i);
                        LatLng latLng = new LatLng(myaddress.getLatitude(),myaddress.getLongitude());

                        mo.position(latLng);
                        mo.title("your sreac result");
                        mMap.addMarker(mo);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }

                }
            }
        });
        Button bt1=findViewById(R.id.bt_to);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Toast.makeText(getApplicationContext(),"shfgdssh",Toast.LENGTH_SHORT).show();

              *//* mMap.clear();
                MarkerOptions markerOptions =new MarkerOptions();
                markerOptions.position(new LatLng(end_latitude,end_logitude));
                markerOptions.title("Destination ");
                float results[] =new float[20];
                markerOptions.draggable(true);
                Location.distanceBetween(latitude,logitude,end_latitude,end_logitude,results);
                markerOptions.snippet("Distance ="+results[0]);
                mMap.addMarker(markerOptions);*//*

            }
        });

*/



    }



    private void fetchLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);


            return;
        }
        Task<Location>task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    curentLocation=location;
                /*    Toast.makeText(getApplicationContext(),curentLocation.getLatitude()+""+curentLocation.getLongitude(),Toast.LENGTH_SHORT).show();*/
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);

                    mapFragment.getMapAsync(MapsActivity.this);
                }

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        TextView from1 =findViewById(R.id.to);
        mMap=googleMap;
        LatLng latLng = new LatLng(curentLocation.getLatitude(),curentLocation.getLongitude());
        MarkerOptions markerOptions =new MarkerOptions();
     /*  Toast.makeText(getApplicationContext(),"Long"+curentLocation.getLatitude(),Toast.LENGTH_SHORT).show();*/
       b1=findViewById(R.id.balu1);
        b2=findViewById(R.id.balu2);
        b1.setText(String.valueOf(curentLocation.getLatitude()));
        b2.setText(String.valueOf(curentLocation.getLongitude()));
        kisho1=String.valueOf(curentLocation.getLatitude()).trim();
        kisho2=String.valueOf(curentLocation.getLongitude()).trim();
        Toast.makeText(getApplicationContext(),"kishoe"+kisho1,Toast.LENGTH_SHORT).show();

        markerOptions.position(latLng);
        markerOptions.title("current location");
        markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_pin2));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,30));
        mMap.addMarker(markerOptions);
        Geocoder geocoder= new Geocoder(MapsActivity.this);
        List<Address> address1=null;
        try{
            address1 = geocoder.getFromLocation(curentLocation.getLatitude(),curentLocation.getLongitude(), 1);
           /* Toast.makeText(getApplicationContext(),String.valueOf(address1),Toast.LENGTH_LONG).show();*/
        }catch (Exception e)
        {

        }
        for(int i=0;i<address1.size();i++)
        {
            final Address myaddress12=address1.get(i);
            String ast=address1.get(i).getAdminArea()+""+address1.get(i).getAddressLine(i);

            /*ast.append(address1.getLocality()).append(",");
            sb.append(address.getPostalCode()).append(",");
            sb.append(address.getCountryName());
            String strAddress = sb.toString();*/

          /*  Toast.makeText(getApplicationContext(),ast,Toast.LENGTH_LONG).show();*/
            from1.setText(ast);



        }





       /* MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Iam here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
        googleMap.addMarker(markerOptions);*/
       /* MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("current location");

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(1));*/



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case REQUEST_CODE:
               if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                   fetchLocation();

               }
       }


    }
    /*  private void fetchLastLpcation() {
        Task<Location>task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                 if(location!=null)
                 {
                     currentLocation123 =location;
                     Toast.makeText(getApplicationContext(),currentLocation123.getLatitude()+""+currentLocation123.getAltitude(),Toast.LENGTH_SHORT);

                 }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
       if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
           buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }


      if(currentLocationMarker!=null)
      {
          Toast.makeText(getApplicationContext(),"djdbf",Toast.LENGTH_SHORT).show();
      }
      else {
          Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
      }

    }
    protected  synchronized void buildGoogleApiClient()
    {
        client =new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(),"hdsbjdc",Toast.LENGTH_SHORT).show();
        lastLocation=location;
        if(currentLocationMarker!= null){
            currentLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("current location");

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(1));
        if(client!=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, (com.google.android.gms.location.LocationListener) this);
        }

    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

     if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, (com.google.android.gms.location.LocationListener) this);}

    }


    public boolean checkLocationPremission()
    {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return false;
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"dinned",Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d("System out", "onMarkerDragStart..."+marker.getPosition().latitude+"..."+marker.getPosition().longitude);

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        end_latitude =marker.getPosition().latitude;
        end_logitude=marker.getPosition().longitude;

    }

    @Override
    protected void onStart() {
        super.onStart();
        buildGoogleApiClient();

    }*/
    public BitmapDescriptor bitmapDescriptorFromVector(Context context,int vectorResId)
    {
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }
}