package com.example.android.miwok;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class live_train_status_selected_item extends AppCompatActivity {
    TextView[] day=new TextView[7];
    SharedPreferences sd=null;
    String value; String key;
    TextView src_stn,dstn_stn,trnName;
    String result;
    String startDate;
    String origin;
    String journeyDate;
    String fromStn;
    Handler handler,pre_handler;
    LinearLayout disp_content,loading;
    ProgressBar progressbar;
    TextView disp_msg;
    ListView listView1;
    Button retryButton;
    stnName_to_stnCode codeToName;
   String trainNo;
    live_train_selected_Item_Adaptor Adapter;
    ArrayList<live_train_selected_Item_Class> words = new ArrayList<live_train_selected_Item_Class>();
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_train_status_seleted_item);
        codeToName = new stnName_to_stnCode(getApplicationContext());
        listView1 = (ListView) findViewById(R.id.listview);
        loading = (LinearLayout)findViewById(R.id.loading);
        disp_content = (LinearLayout)findViewById(R.id.disp_content);
        progressbar  =(ProgressBar)findViewById(R.id.progressBar);
        disp_msg= (TextView) findViewById(R.id.disp_msg);
        retryButton =(Button)findViewById(R.id.retryButton);
        sd = this.getSharedPreferences("com.example.android.miwok", Context.MODE_PRIVATE);
        src_stn=(TextView)findViewById(R.id.src_stn);
        src_stn=(TextView)findViewById(R.id.src_stn);
        dstn_stn=(TextView)findViewById(R.id.dstn_stn);
         trnName=(TextView)findViewById(R.id.selectTrain);
        day[0] = (TextView) findViewById(R.id.sun);
        day[1] = (TextView) findViewById(R.id.mon);
        day[2] = (TextView) findViewById(R.id.tue);
        day[3] = (TextView) findViewById(R.id.wed);
        day[4] = (TextView) findViewById(R.id.thr);
        day[5] = (TextView) findViewById(R.id.fri);
        day[6] = (TextView) findViewById(R.id.sat);
        disp_content.setVisibility(View.GONE);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("under main handler......");
                customObject myobj =(customObject)msg.obj;
                if(myobj.getResult().equals("success")) {
                    words = (ArrayList<live_train_selected_Item_Class>) myobj.getLiveTrnSeleted();
                    Adapter = new live_train_selected_Item_Adaptor(live_train_status_selected_item.this, words);
                    loading.setVisibility(View.GONE);
                    disp_content.setVisibility(View.VISIBLE);
                    listView1.setAdapter(Adapter);
                    String[] runDayInt=myobj.getRunDaysInt();
                    try {
                        for (int k = 1; k < 8; k++) {
                            if(Integer.parseInt(runDayInt[k])==1){
                                day[k-1].setTextColor(Color.parseColor("#388E3C"));
                                day[k-1].setTextSize(15);

                            }else{
                                day[k-1].setTextColor(Color.parseColor("#BDBDBD"));


                            }

                        }

                        trnName.setText(myobj.getTrainNo()+" : "+myobj.getTrainName());
                        src_stn.setText(myobj.getSrcStn());
                        dstn_stn.setText(myobj.getDstnStn());
                    }catch(Exception e){
                        e.fillInStackTrace();
                        System.out.println("error in loop or array!!"+e);
                    }
                }else if(myobj.getResult().equals("error")){
                    progressbar.setVisibility(View.GONE);
                    disp_msg.setVisibility(View.VISIBLE);
                    retryButton.setVisibility(View.VISIBLE);
                    disp_msg.setText(myobj.getErrorMsg());
                    Log.e("error",myobj.getErrorMsg());
                }

            }
        };

        pre_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("under pre main handler......");
                customObject myobj =(customObject)msg.obj;
                result = myobj.getResult();
                startDate = getIntent().getStringExtra("startDate");
                System.out.println("pre handler startDate:"+startDate);
                System.out.println("pre handler result :"+result);

                Thread thread =new Thread(new Info_extractor("live_trn_sltd_item",handler,result,codeToName,startDate));
                thread.start();

            }
        };

        origin =getIntent().getStringExtra("origin");
        if(origin.equals("live_train_options")) {
            startDate = getIntent().getStringExtra("startDate");
            System.out.println("live se startDate:"+startDate);
            result = getIntent().getStringExtra("result");
            Log.i("startDate", getIntent().getStringExtra("startDate"));
            System.out.println("live train options :"+result);

            Thread thread =new Thread(new Info_extractor("live_trn_sltd_item",handler,result,codeToName,startDate));
            thread.start();
        }
        else
            if(origin.equals("train_bw_2_stn")){
                startDate=null;
                journeyDate = getIntent().getStringExtra("journeyDate");
                trainNo = getIntent().getStringExtra("trainNo");
                fromStn = getIntent().getStringExtra("fromStn");
                System.out.println("journeyDate :"+journeyDate);
                System.out.println("trainNo :"+trainNo);
                System.out.println("fromStn :"+fromStn);





            }else if(origin.equals("stn_sts")){
                startDate = getIntent().getStringExtra("startDate");
                trainNo = getIntent().getStringExtra("trainNo");
                System.out.println("stn sts se startDate:"+startDate);

                Worker worker =new Worker("stn_sts_trn_clk");
                worker.Input_Details(sd,pre_handler,Integer.parseInt(trainNo),null);
                Thread thread =new Thread(worker);
                thread.start();
            }

    }




}
