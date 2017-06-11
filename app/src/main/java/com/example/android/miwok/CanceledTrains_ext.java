package com.example.android.miwok;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class CanceledTrains_ext {
    public CanceledTrains_ext(String dnld_data, Handler info_ext_handler) {
        ArrayList<CanceledTrainClass> words = new ArrayList<CanceledTrainClass>();
        try {

            String[] rs = dnld_data.split("=", 2);
            dnld_data = rs[1].trim();
            Matcher localObject1;

            localObject1 = Pattern.compile("trnName:function().*?\\\"\\},").matcher((CharSequence) dnld_data);

            while (localObject1.find()) {
                dnld_data = dnld_data.replace(localObject1.group(0), "");
            }
            JSONObject jsonObject = new JSONObject(dnld_data);

            JSONArray arr = jsonObject.getJSONArray("allCancelledTrains");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonpart = arr.getJSONObject(i);
                String   trainNo = jsonpart.getString("trainNo");
                String   trainName = jsonpart.getString("trainName");
                String   trainSrc =jsonpart.getString("trainSrc");
                String   trainDstn =jsonpart.getString("trainDstn");
                String   startDate =jsonpart.getString("startDate");
                String  trainType =jsonpart.getString("trainType");

                CanceledTrainClass w = new CanceledTrainClass(trainNo,trainName,trainSrc,trainDstn,startDate,trainType);
                words.add(w);
            }

            Message message =Message.obtain();
            customObject obj =new customObject("info_ext_handler","success","");
            obj.setCnsTrnList(words);
            message.obj=obj;
            info_ext_handler.sendMessage(message);
        }catch (Exception e){
            System.out.println("error inside info extraction works....");
            Message message =Message.obtain();
            message.obj =new customObject("info_ext_handler","error",e.toString());
            info_ext_handler.sendMessage(message);
        }
    }
}