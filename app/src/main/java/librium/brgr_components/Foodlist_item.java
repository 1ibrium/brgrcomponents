package librium.brgr_components;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Librium on 2015/5/2.
 */
public class Foodlist_item extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        //tv = (TextView) findViewById(R.id.tv);

        LayoutInflater inflate = LayoutInflater.from(this);
        View view = inflate.inflate(R.layout.foodmenu_item,null);
        setContentView(view);
    }
}
