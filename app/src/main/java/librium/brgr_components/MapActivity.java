package librium.brgr_components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapActivity extends AppCompatActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageView picker;//固定在屏幕中心的picker
    private FrameLayout mapContainer;

    public Point get_pickerPointTo() {
        return _pickerPointTo;
    }

    public void set_pickerPointTo(Point _pickerPointTo) {
        this._pickerPointTo = _pickerPointTo;
    }
    private Point _pickerPointTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        this.mapContainer = (FrameLayout)findViewById(R.id.map_container);

        setUpMapIfNeeded();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            public void onMapClick(LatLng point) {

                Log.d("fuckamd", "    点击的位置" + point.toString());

            }
        });
        mMap.setMyLocationEnabled(false);
        //设置中心locator
        final ImageView picker = (ImageView)findViewById(R.id.map_centerlocator);
        FrameLayout mapPapa = (FrameLayout)picker.getParent();

        mapPapa.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                picker.setX(v.getWidth() / 2 - picker.getWidth() / 2);
                picker.setY(v.getHeight() / 2 - picker.getHeight());
                set_pickerPointTo(new Point(v.getWidth() / 2,v.getHeight()/2));
            }
        });
        createArena();

    }
    public void createArena(){
        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(-20.85194905943511, -31.875013187527657),
                        new LatLng(-18.646229258784658, 16.406245082616806),
                        new LatLng(-55.11189037134487, 17.343744188547134),
                        new LatLng(-54.57205699120097, -41.71876586973667),
                        new LatLng(-20.85194905943511, -31.875013187527657)).strokeColor(Color.RED)
                .fillColor(Color.BLUE).strokeWidth(2);

// Get back the mutable Polygon
        Polygon polygon = mMap.addPolygon(rectOptions);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);
        Log.e("fuckamd", "super.onTouchEvent: " + value+ " event: " + event.getAction());
        return value;
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.、
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraChangeListener(mOnCameraChangeListener);
    }

    GoogleMap.OnCameraChangeListener mOnCameraChangeListener = new GoogleMap.OnCameraChangeListener() {

        @Override
        public void onCameraChange(CameraPosition cp) {

            LatLng pickerGeo = fromStaticLocatorToGeo();
            Log.v("fuckamd", "camera moved!");
            Log.v("fuckamd", "Picker gpoint:"+pickerGeo.latitude+" "+pickerGeo.longitude);
            Log.v("fuckamd", "Camera gpoint:"+cp.target.toString());
      //      Log.v("fuckamd", "Camera address:"+getLocationAddress(pickerGeo));

        }
    };

    private String getLocationAddress(LatLng point){
        StringBuilder sb=new StringBuilder();
        Geocoder geoCode=new Geocoder(this,Locale.getDefault());
        List<Address> addresses= null;
        try {
            addresses = geoCode.getFromLocation(point.latitude, point.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            //地址不存在
            e.printStackTrace();
        }

        if(null != addresses){
            if( 0 < addresses.size()){
                Address address=addresses.get(0);
                for(int i=0;i<address.getMaxAddressLineIndex();i++){
                    sb.append(address.getAddressLine(i)+", ");
                }
                sb.append(address.getLocality()+", ");
                sb.append(address.getPostalCode()+", ");
                sb.append(address.getCountryName()+",");

                Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return sb.toString();
    }


    //屏幕中心picker所对应的地理位置
    public LatLng fromStaticLocatorToGeo(){
        Projection projection=mMap.getProjection();
        LatLng gpoint=projection.fromScreenLocation(get_pickerPointTo());
        return gpoint;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
