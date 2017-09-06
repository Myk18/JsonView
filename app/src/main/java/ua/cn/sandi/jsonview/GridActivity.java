package ua.cn.sandi.jsonview;



import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.android.volley.VolleyLog.TAG;


/**
 * Created by mikni on 23.08.2017.
 */

public class GridActivity extends Activity {

    Button bopen;
    Button bback;
    Button bexit;

    TextView responsegrid;

    public String serverresponse;

    String jsonstr;

    ListView grid;

    List<Integer> text2;

    String LOG_TAG = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        responsegrid = (TextView) findViewById(R.id.mytext_responsegrid);

        grid = (ListView) findViewById(R.id.mylist_grid);

        bback = (Button) findViewById(R.id.mybutton_back);
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent backintent = new Intent(GridActivity.this, MainActivity.class);
                //startActivity(backintent);
                finish();
            }
        });
        bopen = (Button) findViewById(R.id.mybutton_open);
        bopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code here

                String url = getString(R.string.setserver);

                if (!TextUtils.isEmpty(getPref("setserver"))) url = getPref("setserver") ;

                //  request here
                makeJsonStringRequest(url, responsegrid);

                Log.d(TAG, "ok");
            }
        });
        bexit = (Button) findViewById(R.id.mybutton_exit);
        bexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Exit function");

                finish();
                moveTaskToBack(true);
            }
        });
    }


    private void makeJsonStringRequest(String url,final TextView t) {

        String  tag_string_req = "string_req";

        StringRequest strReq = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response);
                        // code here
                        serverresponse = response;

                        WebService ws = new WebService();
                        ws.inputstr = serverresponse;
                        ws.parseStr(serverresponse);

                        String text1 = ws.getorders();
                        text2 = ws.getordersids();

                        // render text
                        t.setText(text1);

                        renderlist(grid,text2);

                        Log.d(TAG, "1");
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.d(TAG, "Error: " + error.getMessage());}
                });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void renderlist(ListView v,List<Integer> l) {

                final String ATTRIBUTE_NAME_TEXT = "text";
                final String ATTRIBUTE_NAME_IMAGE = "image";
                int img = R.mipmap.ic_launcher;

                int[] array1 = new int[l.size()];
                for(int i = 0; i < l.size(); i++) { array1[i] = l.get(i); }

                ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(
                        array1.length);
                Map<String, Object> m;

                for (int i = 0; i < array1.length; i++) {
                    m = new HashMap<String, Object>();
                    m.put(ATTRIBUTE_NAME_TEXT, array1[i]);
                    m.put(ATTRIBUTE_NAME_IMAGE, img);
                    data.add(m);
                }

                // массив имен атрибутов, из которых будут читаться данные
                String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE};
                // массив ID View-компонентов, в которые будут вставлять данные
                int[] to = {R.id.textView1, R.id.ivImg};

                SimpleAdapter adap = new SimpleAdapter(this, data, R.layout.item_list2, from, to);

        v.setAdapter(adap);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
            }
        });

        v.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "itemSelect: position = " + position + ", id = " + id);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(LOG_TAG, "itemSelect: nothing");
            }
        });

        v.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Log.d(LOG_TAG, "scrollState = " + scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                Log.d(LOG_TAG, "scroll: firstVisibleItem = " + firstVisibleItem
                        + ", visibleItemCount" + visibleItemCount
                        + ", totalItemCount" + totalItemCount);
            }
        });

    }

    private void makeJsonArrayRequest(String url, final TextView t) {

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object



                            String jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);
                                String id = person.getString("id");
                                jsonResponse += "id: " + id + "\n\n";
                            }

                            t.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private void makeJsonObjectRequest(String url, final TextView t) {

    //    showDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    String name = response.getString("orders");
                    String jsonResponse = "";
                    jsonResponse += "orders: " + name + "\n\n";
                    t.setText(jsonResponse);

                    JSONAdapter adap = new JSONAdapter(GridActivity.this, response.getJSONArray("orders") );

                    grid.setAdapter(adap);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
    //            hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
     //           hidepDialog();
            }
        });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private String getPref (String key) {
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(key, "");
    }
}





