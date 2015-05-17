package librium.brgr_components.controller;

import android.content.Context;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import librium.brgr_components.R;
import librium.brgr_components.controller.customobjects.FoodlistPicker;

/**
 * Created by Librium on 2015/5/14.
 */
public class AddressSearchAdapter extends BaseAdapter {
    private Context context;                        //all

    private List<Address> listItems;    //商品信息集合
    private LayoutInflater listContainer;           //视图容器

    public final class ListItemView{                //自定义控件集合
        public TextView address;
    }


    public AddressSearchAdapter(Context context, List<Address> listItems) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
    }


    @Override
    public int getCount() {
       return listItems.size();
    }

    @Override
    public Address getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView;

        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = listContainer.inflate(R.layout.textonly_item, parent, false);
            listItemView.address = (TextView)convertView.findViewById(R.id.textonly_item_text1);
            convertView.setTag(listItemView);
        }else{
            listItemView = (ListItemView)convertView.getTag();
        }



            Address address = listItems.get(position);
            String result = MapUtils.fromAddressToString(address);

            //我想不可能是空的 不过谁知道呢 google那么操蛋
            if(!result.isEmpty())
            listItemView.address.setText(result);
            else
            listItemView.address.setText(context.getString(R.string.not_readable_addressStr));


        return convertView;

    }
}
