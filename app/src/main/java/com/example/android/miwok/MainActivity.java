/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.miwok.PnrStatus.mainapp.PnrMainActivity;
import com.example.android.miwok.SeatAval.mainapp2.ReservedTrainDataEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    static SharedPreferences sd;
    private FirebaseAnalytics mFirebaseAnalytics;
    Boolean gotthekey = false;

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "2.1.4");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "http://play.google.com/store/apps/details?id=com.SahuAppsPvtLtd.myTrainEnquiryApp");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        sd = this.getSharedPreferences("com.example.android.miwok", Context.MODE_APPEND);


       System.out.println("sd lastcall is empty!!!");
            sd.edit().putString("lastcall", "0").apply();

        LinearLayout seatAvailabilityLayout= (LinearLayout) findViewById(R.id.seatAvailabilityLayout);
        seatAvailabilityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ReservedTrainDataEntry.class);
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "9");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Seat Availability");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });


        LinearLayout pnrStatustransLayout = (LinearLayout) findViewById(R.id.pnrStatustransLayout);
        pnrStatustransLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PnrMainActivity.class);
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "8");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PNR Status");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        LinearLayout canceledTrnsLayout = (LinearLayout) findViewById(R.id.canceledTrnsLayout);
        canceledTrnsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CanceledTrains.class);
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "7");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Canceled Trains");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        LinearLayout DivertedTrnsLayout = (LinearLayout) findViewById(R.id.DivertedTrnsLayout);
        DivertedTrnsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DivertedTrains.class);
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "6");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Diverted  Trains");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        LinearLayout RescheduledTrainsLayout = (LinearLayout) findViewById(R.id.RescheduledTrainsLayout);
        RescheduledTrainsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, com.example.android.miwok.RescheduledTrains.class);
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "5");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Reschedduled Trains");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

   
        LinearLayout stn_stsLayout = (LinearLayout) findViewById(R.id.stn_stsLayout);
        stn_stsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Select_Station.class);
                i.putExtra("origin", "main_act_stn_sts");
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Station Status");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });


        LinearLayout train_schLayout = (LinearLayout) findViewById(R.id.train_schLayout);
        train_schLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Select_Train.class);
                i.putExtra("origin", "main_act_trn_schedule");
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "3");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Train Schedule");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });


        LinearLayout live_trainLayout = (LinearLayout) findViewById(R.id.live_trainLayout);
        live_trainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Select_Train.class);
                i.putExtra("origin", "main_act_live_train_options");
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Live Train Status");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });

        LinearLayout trn_bw2_stLayout = (LinearLayout) findViewById(R.id.trn_bw2_stLayout);
        trn_bw2_stLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Select_2Stations.class);
                sd.edit().putString("src_code","").apply();
                sd.edit().putString("dstn_code","").apply();
                sd.edit().putString("src_name","").apply();
                sd.edit().putString("dstn_name","").apply();
                sd.edit().putBoolean("swap_clked",false).apply();
                sd.edit().putString("temp_toStn_code","").apply();
                sd.edit().putString("temp_toStn_name","").apply();
                sd.edit().putString("temp_fromStn_name","").apply();
                sd.edit().putString("temp_fromStn_code","").apply();

                i.putExtra("origin", "main_act_src_stn");
                startActivity(i);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Train bw Stations");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }
        });



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "http://play.google.com/store/apps/details?id=com.SahuAppsPvtLtd.myTrainEnquiryApp";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Share Google Play Link"));

                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "11");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Share App");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                return true;

            case R.id.rate:
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=com.SahuAppsPvtLtd.myTrainEnquiryApp"));
                startActivity(intent);

                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "12");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Rate App");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    finish();
                                }catch (Exception e){
                                    e.fillInStackTrace();
                                    System.out.println("error in the no thanks ,dont update click...");
                                }
                            }
                        }).create();
        dialog.show();
    }


    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
