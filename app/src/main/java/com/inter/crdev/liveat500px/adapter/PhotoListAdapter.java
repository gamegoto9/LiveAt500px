package com.inter.crdev.liveat500px.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inter.crdev.liveat500px.R;
import com.inter.crdev.liveat500px.dao.PhotoItemCollectionDao;
import com.inter.crdev.liveat500px.dao.PhotoItemDao;
import com.inter.crdev.liveat500px.datatype.Mutableinteger;
import com.inter.crdev.liveat500px.manager.PhotoListManager;
import com.inter.crdev.liveat500px.view.PhotoListItem;

/**
 * Created by CRRU0001 on 04/03/2559.
 */
public class PhotoListAdapter extends BaseAdapter {

    PhotoItemCollectionDao dao;                 // กรณีไม่ใช้ ซิงเกวตั้น PhotoListManager.class

    Mutableinteger lastPositionInteger;


    public PhotoListAdapter(Mutableinteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
    }

    // กรณีไม่ใช้ ซิงเกวตั้น   PhotoListManager.class //////////////////////////////
    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }


    ///////////////////////////////////////////////////

    // นับข้อมูลทั้งหมด
    @Override
    public int getCount() {
        // กรณี่ใช้ ซิงเกวตั้น  //////////////////////////////////////////////////
        /*
        if(PhotoListManager.getInstance().getDao() == null)
            return 0;
        if(PhotoListManager.getInstance().getDao().getData() == null)
            return 0;
        return PhotoListManager.getInstance().getDao().getData().size();
        */
        //////////////////////////////////////////////////////////////////////


        // กรณไม่ใช้ ซิงเกวตั้น  PhotoListManager.class //////////////////////////////////////////////////
        if(dao == null)
            return 0;
        if(dao == null)
            return 1;
        return dao.getData().size() + 1;

        //////////////////////////////////////////////////////////////////////
    }

    @Override
    public Object getItem(int position) {

        // กรณี่ใช้ ซิงเกวตั้น  //////////////////////////////////////////////////
        /*
        return  PhotoListManager.getInstance().getDao().getData().get(position);
        */
        /////////////////////////////////////////////////////////////////////


        // กรณไม่ใช้ ซิงเกวตั้น  PhotoListManager.class//////////////////////////////////////////////////
        return  dao.getData().get(position);
        //////////////////////////////////////////////////////////////////////

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    /*
    // กรณีมี view มากกว่า 1 ชนิด
    @Override
    public int getViewTypeCount() {
        return 2;                                   // จำนวนประเภทของ view
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;           // return เลขกำกับของ view ประเภทนั้นๆ
    }
    */

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() -1 ? 1 : 0;
    }

    //ขอตำแหน่ง
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        // กรณีมี view มากกว่า 1 ชนิด

        if (getItemViewType(position) == 0 ) {
            PhotoListItem item;

            // recycle listview
            if (convertView != null) {
                item = (PhotoListItem) convertView;
            } else {
                item = new PhotoListItem(parent.getContext());
            }
            return item;
        } else {
            TextView item;
            // recycle listview
            if (convertView != null) {
                item = (TextView) convertView;
            } else {
                item = new TextView(parent.getContext());
                item.setText("Position" + position);
            }
            return item;
        }
        */


        if(position == getCount() -1){
            //Progress Bar
            ProgressBar item;
            if(convertView != null){
                item = (ProgressBar) convertView;
            }else{
                item = new ProgressBar(parent.getContext());
            }
            return item;
        }

        // recycle listview
        PhotoListItem item;
        if (convertView != null) {
            item = (PhotoListItem) convertView;
        } else {
            item = new PhotoListItem(parent.getContext());
        }

        PhotoItemDao dao = (PhotoItemDao) getItem(position);

        item.setNameText(dao.getCapTion());
        item.setDescriptionText(dao.getUserName() + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());

        if(position > lastPositionInteger.getValue()){
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(),
                    R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPositionInteger.setValue(position);

        }
        return item;
    }

    public void increaseLastPosition(int amount){
        lastPositionInteger.setValue(lastPositionInteger.getValue()+amount);
    }
}
