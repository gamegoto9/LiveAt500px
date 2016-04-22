package com.inter.crdev.liveat500px.manager;

import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Contacts;

import com.google.gson.Gson;
import com.inter.crdev.liveat500px.dao.PhotoItemCollectionDao;
import com.inter.crdev.liveat500px.dao.PhotoItemDao;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class PhotoListManager {

    // กรณีใช้ ซิงเกวตั้น ///////////////////////////////////////////////////////////////////////////////
    /*
    private static PhotoListManager instance;

    public static PhotoListManager getInstance() {
        if (instance == null)
            instance = new PhotoListManager();
        return instance;
    }
    */
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private Context mContext;
    private PhotoItemCollectionDao dao;

    public PhotoListManager() {                     // กรณีใช้ ซิงเกวตั้น  ให้ใช้ private PhotoListManager()
        mContext = Contextor.getInstance().getContext();
        // Load data from Persistent Storage
        loadCache();
    }

    public PhotoItemCollectionDao getDao() {
        return dao;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
        // save to persistent storage
        saveCache();
    }

    public void insertDaoAtTopPosition(PhotoItemCollectionDao newDao) {
        if (dao == null)                                    //ถ้า dao เป็น null สร้าง dao ใหม่
            dao = new PhotoItemCollectionDao();
        if (dao.getData() == null)                          //ถ้า data ของ dao เป็น null
            dao.setData(new ArrayList<PhotoItemDao>());     //ทำให้เป็นเป็น null
        dao.getData().addAll(0, newDao.getData());          //ใส่ newDao มาใว้ตำแหน่งบนสุดของ dao ตัวเดิม(ตำแหน่งที่ 0)
        saveCache();
    }

    public void appendDaoAtButtomPosition(PhotoItemCollectionDao newDao) {
        if (dao == null)                                    //ถ้า dao เป็น null สร้าง dao ใหม่
            dao = new PhotoItemCollectionDao();
        if (dao.getData() == null)                          //ถ้า data ของ dao เป็น null
            dao.setData(new ArrayList<PhotoItemDao>());     //ทำให้เป็นเป็น null
        dao.getData().addAll(dao.getData().size(), newDao.getData());          //ใส่ newDao มาใว้ตำแหน่งบนสุดของ dao ตัวเดิม(ตำแหน่งที่ 0)
        saveCache();
    }


    public int getMinimumId() {
        if (dao == null)
            return 0;
        if (dao.getData() == null)
            return 0;
        if (dao.getData().size() == 0)
            return 0;
        int minId = dao.getData().get(0).getId();
        for (int i = 1; i < dao.getData().size(); i++)
            minId = Math.min(minId, dao.getData().get(i).getId());

        return minId;
    }

    public int getMaximumId() {
        if (dao == null)
            return 0;
        if (dao.getData() == null)
            return 0;
        if (dao.getData().size() == 0)
            return 0;
        int maxId = dao.getData().get(0).getId();
        for (int i = 1; i < dao.getData().size(); i++)
            maxId = Math.max(maxId, dao.getData().get(i).getId());

        return maxId;
    }

    public int getCount() {
        if (dao == null)
            return 0;
        if (dao.getData() == null)
            return 0;
        return dao.getData().size();
    }

    public Bundle onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("dao",dao);
        return bundle;
    }
    public void  onRestoreInstanceState(Bundle savaInstanceState){

        dao = savaInstanceState.getParcelable("dao");

    }

    private void saveCache(){
        PhotoItemCollectionDao cacheDao = new PhotoItemCollectionDao();
        if(dao != null && dao.getData() != null)
            cacheDao.setData(dao.getData().subList(0,Math.min(20,dao.getData().size())));

        String json = new Gson().toJson(cacheDao);
        SharedPreferences prefs = mContext.getSharedPreferences("photos",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        // Add/Edit/Delete
        editor.putString("json", json);

        editor.apply();
    }
    private void loadCache(){
        SharedPreferences prefs = mContext.getSharedPreferences("photos",
                Context.MODE_PRIVATE);


        String json = prefs.getString("json",null);
        if(json == null){
            return;
        }
        dao = new Gson().fromJson(json,PhotoItemCollectionDao.class);
    }

}
