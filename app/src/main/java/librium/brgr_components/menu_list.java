package librium.brgr_components;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.FoodListViewAdapter;


public class menu_list extends ActionBarActivity {
    private List<Map<String, Object>> listItems;
    private ListView menu_list_food;
    private FoodListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        this.menu_list_food = (ListView) findViewById(R.id.foodlist);
        this.listItems = getListItems();
        this.listViewAdapter = new FoodListViewAdapter(this, listItems); //创建适配器
        this.menu_list_food.setAdapter(listViewAdapter);

    }

    private String[] price = {"11", "22",
            "33", "44", "55", "66"};

    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < price.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("price", price[i]); //价格
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
