package com.example.android.miwok;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class trn_bw_2_stn extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    String origin = null;
    SharedPreferences sd = null;
    static ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_trn_bw2_stn);
//        dialog = ProgressDialog.show(trn_bw_2_stn.this, "",
//                "Loading. Please wait...", true);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        sd = this.getSharedPreferences("com.example.android.miwok", Context.MODE_PRIVATE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Log.i("current tab", String.valueOf(tabLayout.getSelectedTabPosition()));

        TextView src_stn = (TextView) findViewById(R.id.src_stn);
        src_stn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trn_bw_2_stn.this, Select_Station.class);
                i.putExtra("origin", "src_stn");
                startActivity(i);
                trn_bw_2_stn.this.finish();
            }
        });
        TextView dstn_stn = (TextView) findViewById(R.id.dstn_stn);
        dstn_stn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trn_bw_2_stn.this, Select_Station.class);
                i.putExtra("origin", "dstn_stn");
                startActivity(i);
                trn_bw_2_stn.this.finish();

            }
        });

        origin = this.getIntent().getStringExtra("origin");
        if (origin.equals("main_act_src_stn")) {
            sd.edit().putString("src_name", this.getIntent().getStringExtra("src_name")).apply();
            sd.edit().putString("src_code", this.getIntent().getStringExtra("src_code")).apply();
            Log.i("src_name", sd.getString("src_name", ""));
            src_stn.setText(this.getIntent().getStringExtra("src_name"));

//            if (sd.getString("dstn_code", "") != "") {
//                dstn_stn.setText(sd.getString("dstn_name", ""));
//            } else {
                Intent i = new Intent(trn_bw_2_stn.this, Select_Station.class);
                i.putExtra("origin", "dstn_stn");
                startActivity(i);
                trn_bw_2_stn.this.finish();
           // }

        }
        else if (origin.equals("src_stn")) {
            sd.edit().putString("src_name", this.getIntent().getStringExtra("src_name")).apply();
            sd.edit().putString("src_code", this.getIntent().getStringExtra("src_code")).apply();
            Log.i("src_name", sd.getString("src_name", ""));
            src_stn.setText(this.getIntent().getStringExtra("src_name"));

            if (sd.getString("dstn_code", "") != "") {
                dstn_stn.setText(sd.getString("dstn_name", ""));
            } else {
                Intent i = new Intent(trn_bw_2_stn.this, Select_Station.class);
                i.putExtra("origin", "dstn_stn");
                startActivity(i);
                trn_bw_2_stn.this.finish();
            }

        } else if (origin.equals("dstn_stn")) {
            sd.edit().putString("dstn_name", this.getIntent().getStringExtra("dstn_name")).apply();
            sd.edit().putString("dstn_code", this.getIntent().getStringExtra("dstn_code")).apply();
            Log.i("dstn_name", sd.getString("dstn_name", ""));
            dstn_stn.setText(this.getIntent().getStringExtra("dstn_name"));

            if (sd.getString("src_code", "") != "") {
                src_stn.setText(sd.getString("src_name", ""));
            }


        } else if (origin.equals("main_activity")) {
            sd.edit().putString("src_name", "").apply();
            sd.edit().putString("src_code", "").apply();
            Log.i("src_name", sd.getString("src_name", ""));
            sd.edit().putString("dstn_name", "").apply();
            sd.edit().putString("dstn_code", "").apply();
            Log.i("dstn_name", sd.getString("dstn_name", ""));
            src_stn.setText("Source");
            dstn_stn.setText("Destination");
        }


    }


    public static class PlaceholderFragment extends Fragment {


        // String filter="all";
        String receiveddata = null;
        RelativeLayout datepickerlayout;
        LinearLayout tablelayout;
        String value;
        String key;
        String origin = null;
        SharedPreferences sd = null;
        ListView listView1;
        DatePicker simpleDatePicker;
        Button submit;
        ArrayList<trn_bw_2_stn_Items_Class> words;
        private static final String ARG_SECTION_NUMBER = "section_number";



        @Override
        public void onPause() {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                tablelayout.setVisibility(View.INVISIBLE);
                datepickerlayout.setVisibility(View.VISIBLE);

            } else {
                Log.i(" on Resume function", "else part working");
            }
            super.onPause();
        }

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sd = getActivity().getSharedPreferences("com.example.android.miwok", Context.MODE_PRIVATE);
        final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.getSelectedTabPosition();
        View rootView = inflater.inflate(R.layout.fragment_sub_page01, container, false);


        key = sd.getString("key", "");
        value = sd.getString("pass", "");


        if (sd.getString("src_code", "") != "" & sd.getString("dstn_code", "") != "") {
            System.out.println("here is the data  :" + sd.getString("src_name", "") + "\n" + sd.getString("dstn_name", ""));

            listView1 = (ListView) rootView.findViewById(R.id.listview1);


            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                getTrn_bw2_stn(sd.getString("src_code", ""), sd.getString("dstn_code", ""), "all", "");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                getTrn_bw2_stn(sd.getString("src_code", ""), sd.getString("dstn_code", ""), "today", "");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                getTrn_bw2_stn(sd.getString("src_code", ""), sd.getString("dstn_code", ""), "tomorrow", "");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                rootView = null;
                rootView = inflater.inflate(R.layout.fragment_sub_page02, container, false);
                listView1 = (ListView) rootView.findViewById(R.id.listview1);
                simpleDatePicker = (DatePicker) rootView.findViewById(R.id.simpleDatePicker);

                datepickerlayout = (RelativeLayout) rootView.findViewById(R.id.datepickerlayout);

                tablelayout = (LinearLayout) rootView.findViewById(R.id.tablelayout);

                submit = (Button) rootView.findViewById(R.id.submitButton);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get the values for day of month , month and year from a date picker
                        String day = "" + simpleDatePicker.getDayOfMonth();
                        String month = "" + (simpleDatePicker.getMonth() + 1);
                        String year = "" + simpleDatePicker.getYear();
                        datepickerlayout.setVisibility(View.INVISIBLE);
                        tablelayout.setVisibility(View.VISIBLE);
                        // display the values by using a toast
                        simpleDatePicker.setCalendarViewShown(false);

                        getTrn_bw2_stn(sd.getString("src_code", ""), sd.getString("dstn_code", ""), "byDate", "" + year + "," + month + "," + day);

                        Toast.makeText(getActivity().getApplicationContext(), day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.i(" Error in onCreateView", "error");
            }

        }

        return rootView;
    }

    void getTrn_bw2_stn(String src_code, String dstn_code, String fltr, String dateobj) {
        try {
            key_pass_generator key_pass_generator=new key_pass_generator();
            key_pass_generator.start();
            try {
                key_pass_generator.join();
                System.out.println("joined the thread :"+key_pass_generator.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            key = sd.getString("key","");
            value = sd.getString("pass","");
            DownloadTask1 task1 = new DownloadTask1();
            // task.execute("http://enquiry.indianrail.gov.in/ntes/NTES?action=showAllCancelledTrains&"+key+"="+value);
            if(receiveddata== null) {
                receiveddata = task1.execute("http://enquiry.indianrail.gov.in/ntes/NTES?action=getTrnBwStns&stn1=" + src_code + "&stn2=" + dstn_code + "&trainType=ALL&" + key + "=" + value).get();
                System.out.println("Calling request for"+src_code+" to "+dstn_code);


                data_filter_task data_filter_task=new data_filter_task(receiveddata,fltr,dateobj,listView1,getActivity(),words);
                data_filter_task.execute();
                Log.i("receiveddata",receiveddata);
            }  else{
                data_filter_task data_filter_task=new data_filter_task(receiveddata,fltr,dateobj,listView1,getActivity(),words);
                data_filter_task.execute();
            }

        } catch (Exception e) {
            Log.e("in getTrai_bw2_stn", e.toString());
        }


    }



    public class DownloadTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;

            try {
                HttpURLConnection E = null;
                url = new URL(urls[0]);
                E = (HttpURLConnection) url.openConnection();
                String str2 = sd.getString("cookie", "");
                str2 = str2.replaceAll("\\s", "").split("\\[", 2)[1].split("\\]", 2)[0];
                E.setRequestProperty("Cookie", str2.split(",", 2)[0] + ";" + str2.split(",")[1]);
                E.setRequestProperty("Referer", "http://enquiry.indianrail.gov.in/ntes/");
                E.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
                E.setRequestProperty("Host", "enquiry.indianrail.gov.in");
                E.setRequestProperty("Method", "GET");
                E.setConnectTimeout(20000);
                E.setReadTimeout(30000);
                E.setDoInput(true);
                E.connect();

                if (E.getResponseCode() != 200) {
                    System.out.println("respose code is not 200");
                } else {
                    System.out.println("Jai hind : " + E.getResponseCode());
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(E.getInputStream()));


                String inputLine = null;

                while ((inputLine = in.readLine()) != null) {
                    result += inputLine;
                }

                return result;
            } catch (Exception e) {
                Log.e("error http get:", e.toString());
            }


            return null;
        }


    }


//
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return PlaceholderFragment.newInstance(i + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:

                    return "All";
                case 1:

                    return "today";
                case 2:

                    return "tomorrow";
                case 3:

                    return "Date";
            }
            return null;
        }
    }

    
    
    
}
