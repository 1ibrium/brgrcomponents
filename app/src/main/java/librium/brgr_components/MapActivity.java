package librium.brgr_components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
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
    private final int locationRequestCode = 1500;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageView picker;//�̶�����Ļ���ĵ�picker
    private int touchCounter = 0;//onTouch
    private RelativeLayout thismain;
    private boolean _cameraMoveStatus = false;
    private Handler checkCameraIsStop;
    private TextView addressTextView;
    private boolean isUserInteracting;
    public LocationListener mLocationListener;
    private LocationManager loctionManager;


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
        this.isUserInteracting = false;

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location loc) {

                if (loc != null) {
                    loctionManager.removeUpdates(mLocationListener);
                    LatLng geoPoint = new LatLng(loc.getLatitude(), loc.getLongitude());
                    cameraMoveToMyPosition(geoPoint);
                } else {
                    Toast.makeText(MapActivity.this, R.string.gmslocator_fail,
                            Toast.LENGTH_SHORT).show();
                }
            }

            public void onStatusChanged(final String s, final int i, final Bundle b) {

            }
            // 当系统Setting -> Location & Security -> Use wireless networks勾选，Use GPS satellites勾选时调用
            public void onProviderEnabled(final String s) {

            }

            public void onProviderDisabled(final String s) {

            }
        };

        //papa
        thismain = (RelativeLayout)findViewById(R.id.map_main);

        this.editable_location_container = findViewById(R.id.map_editable_locator);
        this.addressTextView = (TextView)editable_location_container.findViewById(R.id.locator_address);

        setUpMapIfNeeded();
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //this is for debug only
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                Log.d("fuckamd", "地图点击坐标" + point.toString());
            }
        });
        mMap.setMyLocationEnabled(false);
        //屏幕中心locator
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

    public void onAddressTextViewClick(View view){
        Intent intent=new Intent(this,EnterAddressActivity.class);
        startActivityForResult(intent, locationRequestCode);
    }

    //接收当前Activity跳转后，目标Activity关闭后的回传值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //自定义了 就不需要到物理位置了
        loctionManager.removeUpdates(mLocationListener);
        //根据用户选的位置移动摄像机
        switch(resultCode){
            case RESULT_OK:{//接收并显示Activity传过来的值
                Bundle bundle = data.getExtras();
                double latitude = bundle.getDouble("latitude");
                double longitude = bundle.getDouble("longitude");
                cameraMoveToMyPosition(new LatLng(latitude, longitude));
                break;
            }
            default:
                break;
        }

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
            // Try to obtain the map from the SupportMapFragment.��
            mMap = ((com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    getMyLocation();
                }
            });
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void getMyLocation() {
        String contextService = Context.LOCATION_SERVICE;
        //通过系统服务，取得LocationManager对象
        loctionManager = (LocationManager) getSystemService(contextService);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        String provider = loctionManager.getBestProvider(criteria, true);
        Location location = loctionManager.getLastKnownLocation(provider);
        if(null == location){
            Toast.makeText(this, getString(R.string.waiting_locationchange_event),Toast.LENGTH_SHORT).show();
            loctionManager.requestLocationUpdates(provider, 1000, 1, mLocationListener);

        }
        //有巨大问题
    }

    private void cameraMoveToMyPosition(LatLng myloc) {
        mMap.setOnCameraChangeListener(null);
        LatLng latLng = new LatLng(myloc.latitude, myloc.longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mMap.animateCamera(cameraUpdate);
    }


    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        //mMap.setOnCameraChangeListener(mOnCameraChangeListener);
    }

    GoogleMap.OnCameraChangeListener mOnCameraChangeListener = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cp) {
            hideEditableView();
        }
    };

    //停止移动弹出地址栏
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

    //经纬度得到地址
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
            //未成功初始化
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


    //picker所对应的地图坐标
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

    //显示隐藏地址栏
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
                isUserInteracting = true;
                if(touchCounter == 0) {
                    mMap.setOnCameraChangeListener(mOnCameraChangeListener);
                }
                touchCounter ++;
                break;

            case MotionEvent.ACTION_UP:
                touchCounter --;
                if( 0 == touchCounter ) {
                    isUserInteracting = false;
                    checkCameraIsStop.postDelayed(showEditorWhenCameraStop, 1800);
                }
                break;
        }

        return super.dispatchTouchEvent(event);
    }

}
