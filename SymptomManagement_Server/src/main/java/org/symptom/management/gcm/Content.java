package org.symptom.management.gcm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the content of the POST request send to
 * Google Cloud Messaging 
 * 
 * @author stephane
 */
public class Content implements Serializable {

    private ArrayList<String> registration_ids;
    private Map<String,String> data;

    public void addRegId(String regId){
        if(registration_ids == null)
            registration_ids = new ArrayList<String>();
        registration_ids.add(regId);
    }

    public void createData(String title, String message){
        if(data == null)
            data = new HashMap<String,String>();

        data.put("title", title);
        data.put("message", message);
    }
    
    public ArrayList<String> getRegistration_ids(){
    	return this.registration_ids;
    }
    
    public void setRegistration_ids(ArrayList<String> list){
    	registration_ids = list;
    }
    
    public Map<String, String> getData(){
    	return this.data;
    }
    
    public void setData(Map<String, String> map){
    	data = map;
    }
}
