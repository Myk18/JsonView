package ua.cn.sandi.jsonview;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;


import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.android.volley.VolleyLog.TAG;


/**
 * Created by mikni on 23.08.2017.
 */

public class ConfigActivity extends Activity {

    Button bback;
    Button bsave;
    Button bcheck;

    EditText server;
    EditText setserver;
    EditText apikey;
    EditText startitem;
    EditText startnum;
    TextView responseTV;

    String serverurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        responseTV = (TextView)findViewById(R.id.mytext_response);


        server = (EditText)findViewById(R.id.mytext_server);
        apikey = (EditText)findViewById(R.id.mytext_apikey);
        startitem = (EditText)findViewById(R.id.mytext_startitem);
        startnum = (EditText)findViewById(R.id.mytext_startnum);
        setserver = (EditText)findViewById(R.id.mytext_setserver);

        server.setText(getString(R.string.server));

        apikey.setText(getString(R.string.ws_key));

        startitem.setText(getString(R.string.start_item));

        startnum.setText(getString(R.string.start_num));

        if ( !TextUtils.isEmpty(getPref("setserver")) ) setserver.setText(getPref("setserver"));


        bback = (Button) findViewById(R.id.mybutton_back);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent backintent = new Intent(ConfigActivity.this, MainActivity.class);
                //startActivity(backintent);
                finish();


            }
        });




        bsave = (Button) findViewById(R.id.mybutton_save);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String prefserver = server.getText().toString();
                setPref("server", prefserver);

                String prefapikey = apikey.getText().toString();
                setPref("apikey", prefapikey);

                String prefcategory = startitem.getText().toString();
                setPref("category", prefcategory);

                String prefnum = startnum.getText().toString();
                setPref("num", prefnum);


                serverurl = server.getText() +"/"+ startitem.getText() + "/" + startnum.getText() + "?"+ "ws_key=" + apikey.getText() + "&" + getString(R.string.json_par);
                setPref("setserver", serverurl);

                if (getPref("setserver") != serverurl) { System.out.println("ok");};
                System.out.println("Saved");
                //Intent openintent = new Intent(ConfigActivity.this, MainActivity.class);
                //startActivity(openintent);

                finish();

            }
        });
        bcheck = (Button) findViewById(R.id.mybutton_check);
        bcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Checked");

                serverurl = server.getText() +"/"+ startitem.getText()+ "/" + startnum.getText() + "?"+ "ws_key=" + apikey.getText() + "&" + getString(R.string.json_par);

                strFromUrlToTV(serverurl, responseTV);

            }
        });
    }

    private void strFromUrlToTV(String url,final TextView t) {

        // Tag used to cancel the request
        String  tag_string_req = "string_req";

        StringRequest strReq = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        // code here
                        t.setText(response);}},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.d(TAG, "Error: " + error.getMessage());}
                });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setPref (String Key, String value) {
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putString(Key, value).apply();
    }

    private String getPref (String key) {
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(key, "none");
    }

}



