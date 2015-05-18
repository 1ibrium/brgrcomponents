package view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import librium.brgr_components.R;

/**
 * Created by Librium on 2015/5/18.
 */
public class CustomBorderTextView  extends TextView {
    private Paint mPaint;

    protected boolean showFullSizeBottomBorder;
    protected boolean showChoppingBottomBorder;
    protected boolean showLeftBorder;
    protected int color;
    protected int sroke_width = 1;
    protected float leftChopping;

    public CustomBorderTextView(Context context) {
        super(context);
    }
    public CustomBorderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomBorderTextView);

        int paintColor = array.getColor(R.styleable.CustomBorderTextView_borderColor, Color.BLACK );

        this.showFullSizeBottomBorder = array.getBoolean(R.styleable.CustomBorderTextView_showFullSizeBottomBorder, false);
        this.showChoppingBottomBorder = array.getBoolean(R.styleable.CustomBorderTextView_showChoppingBottomBorder, false);
        this.showLeftBorder = array.getBoolean(R.styleable.CustomBorderTextView_showLeftBorder, false);
        this.sroke_width = array.getDimensionPixelSize(R.styleable.CustomBorderTextView_borderWidth, 1);
        this.leftChopping = array.getFloat(R.styleable.CustomBorderTextView_leftChopping, 0.2f);
        if(this.leftChopping > 1)
            this.leftChopping = 1f;

        this.mPaint.setColor(paintColor);
        mPaint.setStrokeWidth(sroke_width);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //  将边框设为黑色
        //  画TextView的4个边
//        canvas.drawLine(0, 0, this.getWidth() - sroke_width, 0, mPaint);

//        canvas.drawLine(0, 0, 0, this.getHeight() - sroke_width, mPaint);

        if (showLeftBorder) {
            canvas.drawLine(0, 0, 0, this.getHeight() - sroke_width, mPaint);
        }

        if (showFullSizeBottomBorder) {
            canvas.drawLine(0, this.getHeight() - sroke_width, this.getWidth() - sroke_width, this.getHeight() - sroke_width, mPaint);
        } else if(showChoppingBottomBorder){
            canvas.drawLine((this.getWidth() - sroke_width)*leftChopping, this.getHeight() - sroke_width, this.getWidth() - sroke_width, this.getHeight() - sroke_width, mPaint);
        }

        super.onDraw(canvas);
    }


}
