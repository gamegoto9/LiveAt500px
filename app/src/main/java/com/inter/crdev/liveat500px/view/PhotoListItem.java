package com.inter.crdev.liveat500px.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inter.crdev.liveat500px.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class PhotoListItem extends BaseCustomViewGroup {

    TextView tvName,tvDescription;
    ImageView ivImg;



    public PhotoListItem(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public PhotoListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public PhotoListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public PhotoListItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_item_photo, this);
    }

    private void initInstances() {
        // findViewById here
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        ivImg = (ImageView) findViewById(R.id.ivImg);



    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    //ปรับขนาดของลูกใน layout
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec); // width in px
        int hight = width * 2 /3;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                hight,
                MeasureSpec.EXACTLY
        );
        // child views
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
        // self
        setMeasuredDimension(width,hight);
    }

    public void setNameText(String text){
        tvName.setText(text);
    }

    public void setDescriptionText(String text){
        tvDescription.setText(text);
    }
    public void setImageUrl(String url){
        // TODO : Load Image
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.loading)                                    //ภาาพระหว่างรอโหลด
                //.error()                                          //แสดงเมื่อโหลดรุปไม่ได้ error
                //.transform()                                      //สร้าง tranformation
                //.diskCacheStrategy(DiskCacheStrategy.ALL)         //  การแคลชนรูปกรณีนำไปใช้หลายที่

                .into(ivImg);

    }
}
