package librium.brgr_components.controller.customobjects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import librium.brgr_components.R;

/**
 * Created by Librium on 2015/5/5.
 */

public class FoodlistPicker  implements View.OnClickListener {
    private Context context;
    private Button btn_minus;
    private Button btn_plus;
    private TextView quantityText;
    private int quantity = 0;
    private LinearLayout self;
    private int pickermax;

    private LinearLayout soldout;
    private LinearLayout timeout;

    private ViewGroup papa;

    public FoodlistPicker(Context context,ViewGroup papa) {
        this.papa = papa;
        this.context = context;
        this.self = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.customobject_foodpicker, papa, false);

        //customobjectsXML
        this.self = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.customobject_foodpicker, papa, false);
        this.soldout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.customobject_foodsoldout, papa, false);
        this.timeout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.customobject_timeout, papa, false);


        this.btn_minus = (Button)self.findViewById(R.id.button_minus);
        this.btn_plus = (Button)self.findViewById(R.id.button_plus);
        this.quantityText = (TextView)self.findViewById(R.id.foodquantity);
        this.pickermax = context.getResources().getInteger(R.integer.max_food_quantity);

        this.btn_minus.setOnClickListener(this);
        this.btn_plus.setOnClickListener(this);

    }

    public LinearLayout getPicker(){
        return this.self;
    }

    public LinearLayout getSoldout(){
        return this.soldout;
    }

    public LinearLayout getTimeout(){
        return this.timeout;
    }

    public void setPickerMax(int i){
        pickermax = i;
    }

    public int getPickerMax(int i){
        return pickermax;
    }

    public void setTimeoutText(String i){
        TextView tv = (TextView)timeout.findViewById(R.id.foodstatus);
        tv.setText(i);
    }

    @Override
    public void onClick(View v) {
        Log.v("fuckamd", "picker click");
        switch (v.getId()){
            case R.id.button_minus:
                if(quantity>0){
                    quantity--;
                    quantityText.setText(String.valueOf(quantity));
                }
                break;
            case R.id.button_plus:
                if(quantity < pickermax){
                    quantity++;
                    quantityText.setText(String.valueOf(quantity));
                }else{
                    Toast.makeText(context, R.string.so_many_alerttext, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        papa.performClick();

    }

    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int i){
        this.quantity = i;
        quantityText.setText(String.valueOf(quantity));
    }

    public void resetquantity(){
        this.quantity = 0;
        this.quantityText.setText(0);
    }
}
