package controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import librium.brgr_components.R;
import librium.brgr_components.controller.customobjects.FoodlistPicker;

public class FoodListViewAdapter  extends BaseAdapter  {
    @SuppressWarnings("all")
    private Context context;                        //all

    private List<Map<String, Object>> listItems;    //商品信息集合
    private LayoutInflater listContainer;           //视图容器
    private boolean[] _isFoodInfoShow;              //点击文字说明
    private Integer[] foodQuantity;                     //食物数量

    public final class ListItemView{                //自定义控件集合
        public ImageView image;
        public TextView price;
        public TextView foodInfoTitle;
        public View texts;
        public int position;

        public LinearLayout statusLayout;  //显示食物状态的容器
        public FoodlistPicker picker;      //获取食物picker控件的逻辑

    }

    public FoodListViewAdapter(Context context, List<Map<String, Object>> listItems) {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);
        this.listItems = listItems;
        this._isFoodInfoShow = new boolean[getCount()];
        this.foodQuantity = new Integer[getCount()];
        for(int i = 0;i < getCount();i++){
            this.foodQuantity[i] = 0; //数量都是0哦~~·
        }

    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private void setIsFoodInfoShow(int checkedID) {
        _isFoodInfoShow[checkedID] = !_isFoodInfoShow[checkedID];
    }
    public boolean getIsFoodInfoShow(int checkedID) {
        return _isFoodInfoShow[checkedID];
    }

    public int getFoodQuantity(int checkedID){return foodQuantity[checkedID]; }
    public void setFoodQuantity(int quantity,int checkedID){ foodQuantity[checkedID] = quantity; }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemView listItemView;
        if (convertView == null) {
            listItemView = new ListItemView();
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(R.layout.foodmenu_item, parent, false);
            //获取控件对象
            listItemView.image = (ImageView)convertView.findViewById(R.id.menulist_foodimg);
            listItemView.price = (TextView)convertView.findViewById(R.id.foodprice);
            listItemView.texts = convertView.findViewById(R.id.foodlist_text);
            listItemView.position = position;
            listItemView.foodInfoTitle = (TextView)listItemView.texts.findViewById(R.id.foodlist_title);
            listItemView.statusLayout = (LinearLayout)convertView.findViewById(R.id.selector_status);
            listItemView.picker = new FoodlistPicker(context,listItemView.statusLayout);

            //设置控件集到convertView
            convertView.setTag(listItemView);




            //事件访问修饰
            final View ref = convertView;
            final ListItemView refTag = listItemView;


            //无法获得viewlist的viewgroup 所以onAttach后设置正确的高度 不知道是否是最优方案
            convertView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    ref.removeOnLayoutChangeListener(this);
                    ImageView image = (ImageView)ref.findViewById(R.id.menulist_foodimg);
                    View texts = ref.findViewById(R.id.foodlist_text);
                    TextView title = (TextView)texts.findViewById(R.id.foodlist_title);

                 //   texts.setY(image.getHeight()-texts.getHeight());
                    if (getIsFoodInfoShow(position)) {
                        texts.setY(image.getHeight() - texts.getHeight());
                    } else {
                        texts.setY(image.getHeight() - title.getHeight());
                    }


                }
            });
        }else {
            listItemView = (ListItemView)convertView.getTag();
        }
        //添加picker

        final View ref = convertView;
        final ListItemView refTag = listItemView;

        //添加picker状态
        LinearLayout picker;
        View existsPicker = listItemView.statusLayout.getChildAt(0);
        Enum.foodStatus foodstatus = (Enum.foodStatus)listItems.get(position).get("status");
        String foodstatusData = (String)listItems.get(position).get("statusData");
        switch (foodstatus){

            case available:
                picker = listItemView.picker.getPicker();
                if(!picker.equals(existsPicker)){
                    listItemView.picker.setPickerMax(Integer.parseInt(foodstatusData));
                    listItemView.statusLayout.removeAllViews();
                    listItemView.statusLayout.addView(picker);
                }
                break;
            case timeout:
                picker = listItemView.picker.getTimeout();
                if(!picker.equals(existsPicker)){
                    listItemView.picker.setTimeoutText(foodstatusData);
                    listItemView.statusLayout.removeAllViews();
                    listItemView.statusLayout.addView(picker);
                }

                break;
            case soldout:
                picker = listItemView.picker.getSoldout();
                if(!picker.equals(existsPicker)){
                    listItemView.statusLayout.removeAllViews();
                    listItemView.statusLayout.addView(picker);
                }

                break;
        }

        //点击获得食物数量
        listItemView.statusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Log.v("fuckamd", "parent click");
                setFoodQuantity(refTag.picker.getQuantity(), position);

            }
        });

        //点击获得信息
        listItemView.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = refTag.image;
                View texts = refTag.texts;
                TextView body = (TextView)texts.findViewById(R.id.foodlist_body);
                if(texts.getAnimation() != null)return;
                //点击动画
                if(getIsFoodInfoShow(position)){
                    texts.animate().translationY(body.getHeight()).setDuration(300).setInterpolator(new LinearInterpolator()).start();
                  //  slideview(texts, 0, body.getHeight());

                    //   texts.setY(texts.getY() + texts.getHeight() - Dp2Px(30));
                }else {
                    texts.animate().translationY(0).setDuration(300).setInterpolator(new LinearInterpolator()).start();

                //    slideview(texts,0, - body.getHeight());
                    //       texts.setY(image.getHeight() - texts.getHeight());
                }
                setIsFoodInfoShow(position);

                //开始动画
            }


        });


        //设置正确高度
        if (getIsFoodInfoShow(position)) {
            listItemView.texts.setY(listItemView.image.getHeight() - listItemView.texts.getHeight());
        } else {
            listItemView.texts.setY(listItemView.image.getHeight() - listItemView.foodInfoTitle.getHeight());
        }
        //根据数据重新设置view状态
        listItemView.foodInfoTitle.setText("fuckamd");
        listItemView.price.setText((String) listItems.get(position)
                .get("price"));
        listItemView.picker.setQuantity(getFoodQuantity(position));


        return convertView;
    }



//
//    public int Dp2Px(float dp) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dp * scale + 0.5f);
//    }


}

