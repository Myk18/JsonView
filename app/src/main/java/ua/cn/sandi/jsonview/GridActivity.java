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

    TextView head;

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
        head = (TextView) findViewById(R.id.mytext_head);

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

            //    String url = getString(R.string.setserver);

            //    if (!TextUtils.isEmpty(getPref("setserver"))) url = getPref("setserver") ;

                //  request here    WORKING
            //    makeJsonObjectRequest(url);

                openItem("orders","0");

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

    private void openItem(String cat, String num){

     //   String url = getString(R.string.setserver);
     //   if (!TextUtils.isEmpty(getPref("setserver"))) url = getPref("setserver") ;
     //   String url1 = getPref("prefsetserver") + "/" + getPref("prefcategory") + "/" + getPref("prefnum") + "?"+ "ws_key=" + getPref("preapikey") + "&" + getString(R.string.json_par);
        String url1 = "";

        switch (cat) {
            case "orders": {
                url1 = getPref("server") + "/" + "orders" + "?" + "ws_key=" + getPref("apikey") + "&" + getString(R.string.json_par);
                break;
            }
            case "order":{
                url1 = getPref("server") + "/" + "orders" + "/" + num + "?"+ "ws_key=" + getPref("apikey") + "&" + getString(R.string.json_par);
                break;
            }
        }

        makeJsonObjectRequest(url1);

    }

    private void str2render(String str) {

        WebService ws = new WebService();
        ws.inputstr = str;
        ws.parseStr(str);

        //String text1 = ws.getorders();
        text2 = ws.getordersids();

        renderSlist(grid, text2);

    }

    private void renderSlist(ListView v,List<Integer> l) {

        final String ATTRIBUTE_NAME_TEXT = "text";
        final String ATTRIBUTE_NAME_IMAGE = "image";
        int img = R.mipmap.ic_launcher;

        int[] array1 = new int[l.size()];
        for(int i = 0; i < l.size(); i++) { array1[i] = l.get(i); }

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(array1.length);
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

        SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.item_list2, from, to);

        v.setAdapter(adaptor);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) ((ListView) parent).getAdapter().getItem(position).toString();

                //  Toast.makeText(GridActivity.this, item + " выбран", Toast.LENGTH_LONG).show();

                HashMap<String, Object> obj = (HashMap<String, Object>) ((ListView) parent).getAdapter().getItem(position);

                String text = obj.get("text").toString();

                Toast.makeText(GridActivity.this, text + " выбран", Toast.LENGTH_LONG).show();

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

    private void renderJlist(ListView v,ArrayList<Map<String, Object>> data) {

        final String ATTRIBUTE_NAME_TEXT = "id";
        final String ATTRIBUTE_NAME_IMAGE = "image";
        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE};

        int[] to = {R.id.textView1, R.id.ivImg};

        SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.item_list2, from, to);

        v.setAdapter(adaptor);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) ((ListView) parent).getAdapter().getItem(position).toString();

                //  Toast.makeText(GridActivity.this, item + " выбран", Toast.LENGTH_LONG).show();

                HashMap<String, Object> obj = (HashMap<String, Object>) ((ListView) parent).getAdapter().getItem(position);

                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);
                String text = obj.get("id").toString();

                Toast.makeText(GridActivity.this, text + " выбран", Toast.LENGTH_LONG).show();
                // TODO: go to order
                openItem("order", text);

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

    private void renderJorder(ListView v,ArrayList<Map<String, Object>> data) {

        final String ATTRIBUTE_NAME_KEY = "key";
        final String ATTRIBUTE_NAME_VALUE = "value";
        final String ATTRIBUTE_NAME_IMAGE = "image";
        String[] from = {ATTRIBUTE_NAME_IMAGE, ATTRIBUTE_NAME_KEY, ATTRIBUTE_NAME_VALUE};

        int[] to = {R.id.ivImg, R.id.textView1, R.id.textView2};

        SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.item_order, from, to);

        v.setAdapter(adaptor);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) ((ListView) parent).getAdapter().getItem(position).toString();

                //  Toast.makeText(GridActivity.this, item + " выбран", Toast.LENGTH_LONG).show();

                HashMap<String, Object> obj = (HashMap<String, Object>) ((ListView) parent).getAdapter().getItem(position);

                String text = obj.get("key").toString();

                Toast.makeText(GridActivity.this, text + " выбран", Toast.LENGTH_LONG).show();

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
                        str2render(response);

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

    private void makeJsonObjectRequest(String url) {

    //    showDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            if (response.has("orders")) {
                                head.setText("Orders");
                                JSONArray orders = response.getJSONArray("orders");
                                renderJlist(grid, jsonarrayy2list(orders));

                            } else if(response.has("order")) {
                                head.setText("Order");
                                JSONObject order = response.getJSONObject("order");
                                renderJorder(grid, jsonarrayy2order(order));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        // hidepDialog();
                    }},
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        // hide the progress dialog
                        // hidepDialog();
                    }
                });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private String getPref (String key) {
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(key, "");
    }

    private ArrayList<Map<String, Object>> jsonarrayy2list(JSONArray j){

        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> m;
        int img = R.mipmap.ic_launcher;
        final String ATTRIBUTE_NAME_IMAGE = "image";
        final String ATTRIBUTE_NAME_TEXT = "id";

        if (j != null) {
            int len = j.length();
            for (int i=0;i<len;i++){

                Object itemtext = new Object();
                try {
                    itemtext = j.getJSONObject(i).get("id");
                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                m = new HashMap<String, Object>();
                m.put(ATTRIBUTE_NAME_TEXT, itemtext);
                m.put(ATTRIBUTE_NAME_IMAGE, img);

                list.add(i, m);
            }
        }

        return list;

    }

    private ArrayList<Map<String, Object>> jsonarrayy2order(JSONObject j){

        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> m;
        int img = R.mipmap.ic_launcher;
        final String ATTRIBUTE_NAME_IMAGE = "image";
        final String ATTRIBUTE_NAME_KEY = "key";
        final String ATTRIBUTE_NAME_VALUE = "value";

        Object itemvalue = new Object();

        String[] arr = {"id","reference","id_cart","id_customer","shipping_number","payment","total_paid","total_shipping","secure_key","delivery_date","mobile_theme","id_carrier","id_currency"};

        int i=0;

        for (String it: arr) {

                try {
                    itemvalue = j.get(it);
                }catch(JSONException e){
                    Log.d(TAG, e.getMessage());
                }

                m = new HashMap<String, Object>();
                m.put(ATTRIBUTE_NAME_IMAGE, img);
                m.put(ATTRIBUTE_NAME_KEY, it);
                m.put(ATTRIBUTE_NAME_VALUE, itemvalue);

                list.add(i, m);
                i++;

        }

        return list;

    }

    private void makeJsonObjectRequestfunc(String url) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray orders = response.getJSONArray("orders");
                            renderJlist(grid, jsonarrayy2list(orders));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);

    }

}





