package com.inter.crdev.liveat500px.datatype;

import android.os.Bundle;

/**
 * Created by CRRU0001 on 08/03/2559.
 */
public class Mutableinteger {
    private int value;

    public Mutableinteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Bundle onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putInt("value",value);
        return bundle;
    }
    public void onRestoreInstanceState(Bundle savedInstanceState){
        value = savedInstanceState.getInt("value");
    }
}
