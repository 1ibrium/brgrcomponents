package controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import librium.brgr_components.MainActivity;
import librium.brgr_components.R;

/**
 * Created by Librium on 2015/4/30.
 */
public class FoodListViewAdapter  extends BaseAdapter {

    private Context context;                        //运行上下文
    private List<Map<String, Object>> listItems;    //商品信息集合
    private LayoutInflater listContainer;           //视图容器

    public final class ListItemView{                //自定义控件集合
        public ImageView image;
        public TextView price;
        public View texts;
    }

    public FoodListViewAdapter(Context context) {
        this.context = context;
    }

    public FoodListViewAdapter(Context context, List<Map<String, Object>> listItems) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.foodmenu_item, parent);

            //获取控件对象
            listItemView.image = (ImageView)convertView.findViewById(R.id.menulist_foodimg);
            listItemView.price = (TextView)convertView.findViewById(R.id.foodprice);
            listItemView.texts = convertView.findViewById(R.id.foodlist_text);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        }else {
            listItemView = (ListItemView)convertView.getTag();
        }

        listItemView.texts.  setY(listItemView.texts.getHeight());
        ((TextView)listItemView.texts.findViewById(R.id.foodlist_title)).setText("fuckamd");

        Log.e("get X" ,listItemView.texts.getX()+"") ;
        Log.e("get lY" ,listItemView.texts.getY()+"") ;
        Log.e("get Height" ,listItemView.texts.getHeight()+"") ;


//        listItemView.price.setText((String) listItems.get(position)
//                .get("title"));
        listItemView.price.setText((String) listItems.get(position)
                .get("price"));
        return convertView;
    }

    public int Dp2Px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


}

