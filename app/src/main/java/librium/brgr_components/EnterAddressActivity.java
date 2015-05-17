package librium.brgr_components;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import librium.brgr_components.controller.AddressSearchAdapter;


public class EnterAddressActivity extends Activity implements SearchView.OnQueryTextListener{
    private SearchView mSearchView;
    private ListView list;
    private boolean isSearching = false;
    private AddressSearchAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_address);

        final Intent cominData = getIntent();


        this.mSearchView = (SearchView)findViewById(R.id.searchView);
        this.list = (ListView)findViewById(R.id.listView);
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view,int arg2, long arg3)
            {
                int selectedPosition = adapterView.getSelectedItemPosition();
                Address data = listViewAdapter.getItem(arg2);
                Double latitude = data.getLatitude();
                Double longitude = data.getLongitude();
                Log.e("fuckamd",latitude+" ; "+ longitude);
                //新建一个Bundle，Bundle主要放值类型
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude",latitude);
                bundle.putDouble("longitude",longitude);

                //将Bundle赋给Intent
                cominData.putExtras(bundle);
                //跳转回MainActivity
                //注意下面的RESULT_OK常量要与回传接收的Activity中onActivityResult（）方法一致
                EnterAddressActivity.this.setResult(RESULT_OK, cominData);
                //关闭当前activity
                EnterAddressActivity.this.finish();

            }
        });

        this.mSearchView.requestFocus();
        this.mSearchView.setIconified(false);
        this.mSearchView.requestFocusFromTouch();

        this.mSearchView.setOnQueryTextListener(this);

    }

    public void fillUpList(List<Address> listItems){
        this.listViewAdapter = new AddressSearchAdapter(this, listItems); //创建适配器
        this.list.setAdapter(listViewAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.isEmpty())return false;
        getLocationAddress(query);
        return false;
    }


    private void getLocationAddress(String inputAddress){
        Geocoder geoCode=new Geocoder(this, Locale.getDefault());
        List<Address> addresses= null;
        try {
            addresses = geoCode.getFromLocationName(inputAddress, 5);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.map_ioexception), Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            //未成功初始化
            e.printStackTrace();
        }

        if(null == addresses){
            Toast.makeText(this, getString(R.string.invalid_address_input), Toast.LENGTH_SHORT).show();

//            if( 0 < addresses.size()){
//                Address address=addresses.get(0);
//                for(int i=0;i<address.getMaxAddressLineIndex();i++){
//                    sb.append(address.getAddressLine(i)).append(", ");
//                }
//                if(null != address.getLocality())
//                    sb.append(address.getLocality()).append(", ");
//
//                if(null != address.getPostalCode())
//                    sb.append(address.getPostalCode()).append(", ");
//
//                if(null != address.getCountryName())
//                    sb.append(address.getCountryName()).append(",");
//                addressTextView.setText(sb.toString());
//                // Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        }else{
            //填充list
            fillUpList(addresses);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_address, menu);

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


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
