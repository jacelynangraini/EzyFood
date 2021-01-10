package com.jacelyn.ezyfood;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    final List<String> nameList = new ArrayList<String>(10);
    final List<Integer> priceList = new ArrayList<Integer>(10);
    final List<Integer> idList = new ArrayList<Integer>(10);
    final List<Integer> qtyList = new ArrayList<Integer>(10);
    final List<Integer> wallet = new ArrayList<Integer>(10);


    private DataHolder(){

    }

    static DataHolder getInstance(){
        if(instance == null){
            instance = new DataHolder();
        }
        return instance;
    }

    private static DataHolder instance;

}
