package librium.brgr_components;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import controller.*;
import controller.Enum;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class menu_list extends ActionBarActivity  {
    private List<Map<String, Object>> listItems;
    private ListView menu_list_food;
    private FoodListViewAdapter listViewAdapter;

    private PtrClassicFrameLayout mPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);


        this.menu_list_food = (ListView) findViewById(R.id.foodlist);
        this.listItems = getListItems();
        this.listViewAdapter = new FoodListViewAdapter(this, listItems); //创建适配器
        this.menu_list_food.setAdapter(listViewAdapter);
        menu_list_food.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                listViewAdapter.notifyDataSetInvalidated();

            }
        });

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check $mListView instead of $content
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, menu_list_food, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }
        });

    }

    protected void updateData() {
        Log.e("fuckamd", "ListView updateData");
        mPtrFrame.refreshComplete();
        TimerTask task = new TimerTask(){
                 public void run(){
                     mPtrFrame.refreshComplete();
                 }
             };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }


    private String[] price = {"11", "22",
            "33", "44", "55", "66"};
    private String[] statusData = {
            "11",//最大数量
            "",
            "11:00 - 12:00",//需要确定一个数据类型
            "",
            "12:11 - 11:00",
            "15"
    };

    private Enum.foodStatus[] status = {
            Enum.foodStatus.available,
            Enum.foodStatus.soldout,
            Enum.foodStatus.timeout,
            Enum.foodStatus.soldout,
            Enum.foodStatus.timeout,
            Enum.foodStatus.available
    };

    public EnumMap<Enum.foodStatus, String>  times;
    private List<Map<String, Object>> getListItems() {

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < price.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("price", price[i]); //价格
            map.put("status", status[i]); //状态
            map.put("statusData", statusData[i]); //状态

            listItems.add(map);
        }
        return listItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_list, menu);
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
