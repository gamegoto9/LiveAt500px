package com.inter.crdev.liveat500px.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.inter.crdev.liveat500px.R;
import com.inter.crdev.liveat500px.activity.MoreInfoActivity;
import com.inter.crdev.liveat500px.adapter.PhotoListAdapter;
import com.inter.crdev.liveat500px.dao.PhotoItemCollectionDao;
import com.inter.crdev.liveat500px.dao.PhotoItemDao;
import com.inter.crdev.liveat500px.datatype.Mutableinteger;
import com.inter.crdev.liveat500px.manager.HttpManager;
import com.inter.crdev.liveat500px.manager.PhotoListManager;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class MainFragment extends Fragment {

    // Variables
    public interface FragmentListener {
        void onPhotoItemClicked(PhotoItemDao dao);
    }

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    PhotoListManager photoListManager;
    Button btnNewPhotos;
    Mutableinteger lastPositionInteger;

    /************************
     * Functions
     ***********************/

    public MainFragment() {
        super();
    }

    PhotoListAdapter listAdapter;

    @SuppressWarnings("unused")
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            //คืนค่า
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        photoListManager = new PhotoListManager();
        lastPositionInteger = new Mutableinteger(-1);

        /***********************************************
         * สร้าง file internal storage
         ***********************************************/
        /*
        File dir = getContext().getDir("Hello", Context.MODE_PRIVATE);                  //  get path ของ internal storage
        Log.d("Storage",String.valueOf(dir));
        File file = new File(dir,"testfile.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("hello".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /***********************************************
         * ปิด file internal storage
         ***********************************************/

        /***********************************************
         * การเข้าถึง sharedperference
         ***********************************************/
        //SharedPreferences prefs = getContext().getSharedPreferences("dummy",Context.MODE_PRIVATE);      //การเข้า sharedperference ชื่อว่า dummy

        /*
        SharedPreferences.Editor editor = prefs.edit();                     // การเขียนไฟล์ที่เปิดมา
        // add/edit/delete
        editor.putString("Hello","World");
        editor.apply();                     //ยืนยันการแก้ไข
        */

        //String value = prefs.getString("Hello",null);                       // การดึงข้อมูล


        /***********************************************
         * ปิด การเข้าถึง sharedperference
         ***********************************************/

    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {


        // Init 'View' instance(s) with rootView.findViewById here
        btnNewPhotos = (Button) rootView.findViewById(R.id.btnNewPhotos);
        btnNewPhotos.setOnClickListener(buttonClickListener);




        listView = (ListView) rootView.findViewById(R.id.listView);
        listAdapter = new PhotoListAdapter(lastPositionInteger);
        listAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(listViewItemClickListener);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(pullToRefreshListener);

        listView.setOnScrollListener(listViewScrollListener);
        //////////////////////////////////////////////////////////////
        if (savedInstanceState == null) {
            refreshData();
        }
    }

    private void refreshData() {
        if (photoListManager.getCount() == 0)
            reLoadData();
        else
            reloadDataNewer();
    }

    private void reloadDataNewer() {
        int maxId = photoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListAfterId(maxId);
        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_RELOAD_NEWER));

    }

    private void reLoadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();


        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_RELOAD));
    }

    boolean isLoadingMore = false;

    private void loadMoreData() {

        if (isLoadingMore)
            return;
        isLoadingMore = true;

        int minId = photoListManager.getMinimumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListBeforeId(minId);
        call.enqueue(
                new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_LOAD_MORE));

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        // Todo : save PhotoListManatger()
        outState.putBundle("photoListManager"
                , photoListManager.onSaveInstanceState());
        outState.putBundle("lastPositionInteger"
                , lastPositionInteger.onSaveInstanceState());

    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
        photoListManager.onRestoreInstanceState(
                savedInstanceState.getBundle("photoListManager"));

        lastPositionInteger.onRestoreInstanceState(
                savedInstanceState.getBundle("lastPositionInteger"));
    }

    private void showButtonNewPhotos() {
        btnNewPhotos.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_in
        );

        btnNewPhotos.startAnimation(anim);
    }

    private void hideButtonNewPhotos() {
        btnNewPhotos.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_out

        );
        btnNewPhotos.startAnimation(anim);
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT)
                .show();
    }

    /******************************
     * Listener Zone
     ******************************/

    final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnNewPhotos) {
                listView.smoothScrollToPosition(0);
                hideButtonNewPhotos();
            }
        }
    };

    final SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();

        }
    };

    // แก้ bug listview กับ pull to refresh  /////////////////////////
    final AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view,
                                         int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view,
                             int firstVisibleItem,                  //  ตำแหน่งแรกที่ถูกแสดงผล
                             int visibleItemCount,                  //  ตอนนี้หน้าจอโชว์ไอเทมอยู่กี่อัน
                             int totalItemCount) {                  //  จำนวนไอเทมทั้งหมดที่มีอยู่ใน listview

            if (view == listView) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);

                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (photoListManager.getCount() > 0) {
                        //Load More
                        //Log.d("ListView","Loade More Trigged");
                        loadMoreData();
                    }


                }
            }
        }
    };


    final AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position < photoListManager.getCount()) {
                PhotoItemDao dao = photoListManager.getDao().getData().get(position);
                FragmentListener listener = (FragmentListener) getActivity();
                listener.onPhotoItemClicked(dao);
            }
        }
    };

    /***********************************************************
     * Inner Class
     ***********************************************************/

    class PhotoListLoadCallBack implements Callback<PhotoItemCollectionDao> {

        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;

        int mode;

        public PhotoListLoadCallBack(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {

            swipeRefreshLayout.setRefreshing(false);
            if (response.isSuccess()) {
                PhotoItemCollectionDao dao = response.body();

                int firstVisiblePosition = listView.getFirstVisiblePosition();      // ตำแหน่งแรกที่แสดงผล
                View c = listView.getChildAt(0);                                    // View ตัวแรกที่ถูกแสดงผล
                int top = c == null ? 0 : c.getTop();                               // top หน้าจอ if แบบย่อๆ

                if (mode == MODE_RELOAD_NEWER)
                    photoListManager.insertDaoAtTopPosition(dao);
                else if (mode == MODE_LOAD_MORE) {
                    photoListManager.appendDaoAtButtomPosition(dao);
                } else {
                    photoListManager.setDao(dao);
                }
                clearLoadingMoreFlagIfCopable(mode);
                showToast("Load Completed");


                // กรณีใช้ ซิงเกวตั้น PhotoListManager.class  //////////////////////////
                    /*
                    PhotoListManager.getInstance().setDao(dao);
                    */
                ///////////////////////////////////////////////////////////////////

                // กรณีไม่ใช้ ซิงเกวตั้น PhotoListManager.class  //////////////////////////
                //listAdapter.setDao(dao);
                ///////////////////////////////////////////////////////////////////

                listAdapter.setDao(photoListManager.getDao());                     // ปรับปรุงหน้าตา listview
                listAdapter.notifyDataSetChanged();

                if (mode == MODE_RELOAD_NEWER) {
                    //Maintain Scroll Position
                    int additionnalSize =
                            (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    listAdapter.increaseLastPosition(additionnalSize);
                    listView.setSelectionFromTop(firstVisiblePosition + additionnalSize,
                            top);

                    if (additionnalSize > 0) {
                        showButtonNewPhotos();
                    }
                }

            } else {
                // Hendle
                clearLoadingMoreFlagIfCopable(mode);
                try {

                    showToast(response.errorBody().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {

            //ติดต่อ server ไม่ได้
            clearLoadingMoreFlagIfCopable(mode);
            swipeRefreshLayout.setRefreshing(false);
            showToast(t.toString());
        }

        private void clearLoadingMoreFlagIfCopable(int mode) {
            if (mode == MODE_LOAD_MORE) {
                isLoadingMore = false;
            }
        }
    }

}
