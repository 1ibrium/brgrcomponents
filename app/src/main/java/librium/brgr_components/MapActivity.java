package librium.brgr_components;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.os.Handler;


public class MapActivity extends AppCompatActivity{
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageView picker;//固定在屏幕中心的picker
    private int touchCounter = 0;//onTouch
    private RelativeLayout thismain;
    private boolean _cameraMoveStatus = false;
    private Handler checkCameraIsStop;
    private TextView addressTextView;

    private int displayWidth;
    private int displayHeight;


    private View editable_location_container;

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

        this.checkCameraIsStop = new Handler();

        //papa的长宽
        thismain = (RelativeLayout)findViewById(R.id.map_main);
        thismain.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                thismain.removeOnLayoutChangeListener(this);
                displayWidth = thismain.getWidth();
                displayHeight = thismain.getHeight();
            }
        });

        this.editable_location_container = findViewById(R.id.map_editable_locator);
        this.addressTextView = (TextView)editable_location_container.findViewById(R.id.locator_address);

        setUpMapIfNeeded();
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //this is for debug only
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

    Runnable showEditorWhenCameraStop = new Runnable(){
        public void run(){
            showEditableView();
            getLocationAddress(fromStaticLocatorToGeo());
        }
    };

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

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.、
            mMap = ((com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
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
            hideEditableView();
        }
    };

    //移动结束显示地址输入框
    private void cameraMoveStatus(boolean i){
        this._cameraMoveStatus = i;
    }

    private boolean cameraMoveStatus(){  //true:stoped
        boolean returnValue = !_cameraMoveStatus && (0 == touchCounter);
        if(returnValue) {
            showEditableView();
        }
        return !returnValue;
    }

    //根据经纬度获得地址
    private String getLocationAddress(LatLng point){
        StringBuilder sb=new StringBuilder();
        Geocoder geoCode=new Geocoder(this,Locale.getDefault());
        List<Address> addresses= null;
        try {
            addresses = geoCode.getFromLocation(point.latitude, point.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.map_ioexception), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            //地址不存在 //地图未成功初始化
            e.printStackTrace();
        }

        if(null != addresses){
            if( 0 < addresses.size()){
                Address address=addresses.get(0);
                for(int i=0;i<address.getMaxAddressLineIndex();i++){
                    sb.append(address.getAddressLine(i)).append(", ");
                }
                if(null != address.getLocality())
                sb.append(address.getLocality()).append(", ");

                if(null != address.getPostalCode())
                    sb.append(address.getPostalCode()).append(", ");

                if(null != address.getCountryName())
                    sb.append(address.getCountryName()).append(",");
                addressTextView.setText(sb.toString());
               // Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return sb.toString();
    }


    // 屏幕中心picker所对应的地理位置
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

    //地址输入框的淡入淡出效果
    private boolean isEditableViewHiding = false;
    public void hideEditableView(){
        if(isEditableViewHiding)return;
        isEditableViewHiding = true;
        editable_location_container.animate().
                translationY(editable_location_container.getHeight()).alpha(0).
                setDuration(500).setInterpolator(new LinearInterpolator()).start();
    }

    public void showEditableView(){
        if(!isEditableViewHiding)return;
        isEditableViewHiding = false;
        editable_location_container.animate().
                translationY(0).alpha(1).
                setDuration(500).setInterpolator(new LinearInterpolator()).start();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchCounter ++;
                break;

            case MotionEvent.ACTION_UP:
                touchCounter --;
                if( 0 == touchCounter )
                    checkCameraIsStop.postDelayed(showEditorWhenCameraStop, 1800);
                break;
        }

        return super.dispatchTouchEvent(event);
    }

}
