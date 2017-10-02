package ua.cn.sandi.jsonview;



import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    ListView grid;

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
                //  Intent backintent = new Intent(GridActivity.this, MainActivity.class);
                //  startActivity(backintent);
                finish();
            }
        });
        bopen = (Button) findViewById(R.id.mybutton_open);
        bopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  code here

                //  request here    WORKING OLD
                //  makeJsonObjectRequest(url);

                openItem("root", "","");

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

    private void openItem(String type, String cat, String num){

     //   String url = getString(R.string.setserver);
     //   if (!TextUtils.isEmpty(getPref("setserver"))) url = getPref("setserver") ;
     //   String url1 = getPref("prefsetserver") + "/" + getPref("prefcategory") + "/" + getPref("prefnum") + "?"+ "ws_key=" + getPref("preapikey") + "&" + getString(R.string.json_par);
        String url1 = "";

        switch (type) {
            case "category": {
                url1 = getPref("server") + "/" + cat + "?" + "ws_key=" + getPref("apikey") + "&" + getString(R.string.json_par);
                makeJsonObjectRequest(url1, type, cat);
                break;
            }
            case "item":{
                url1 = getPref("server") + "/" + cat + "/" + num + "?"+ "ws_key=" + getPref("apikey") + "&" + getString(R.string.json_par);
                makeJsonObjectRequest(url1, type, cat);
                break;
            }
            case "root":{
                url1 = getPref("server") + "/" + "?"+ "ws_key=" + getPref("apikey") + "&" + getString(R.string.json_par);
                type = "root";
                makeJsonArrayRequest(url1);
                break;
            }
        }
    }

    private void makeJsonObjectRequest(String url, final String type, final String cat) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            switch (type) {
                                case "root":   // root case   response = array     dont work
                                    head.setText("Root");
                                    JSONArray orders = response.getJSONArray("");
                                    renderJlist(grid, jsonarrayy2list(orders));
                                    break;
                                case "category":   //  all ok
                                    head.setText(cat);
                                    JSONArray catarray = response.getJSONArray(cat);
                                    renderJlist(grid, jsonarrayy2list(catarray));
                                    break;
                                case "item":
                                    head.setText(cat);
                                    JSONObject orditemobj = response.getJSONObject(response.keys().next().toString());
                                    renderJitem(grid, jsonarrayy2item(orditemobj));
                                    break;
                            }
                        }catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }   //   key set

    private void makeJsonArrayRequest(String url) {

        JsonArrayRequest arrreq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            head.setText("Root");
                            renderJlist(grid, jsonarrayy2root(response));
                        } catch (Exception e) {
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
        ApplicationController.getInstance().addToRequestQueue(arrreq);
    }   // for keyless root

    private void renderJlist(ListView v, ArrayList<Map<String, Object>> data) {

        final String ATTRIBUTE_NAME_TEXT = "id";
        final String ATTRIBUTE_NAME_IMAGE = "image";
        String[] from = {ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE};

        int[] to = {R.id.textView1, R.id.ivImg};

        SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.item_list2, from, to);

        v.setAdapter(adaptor);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  String item = (String) ((ListView) parent).getAdapter().getItem(position).toString();
                //  Toast.makeText(GridActivity.this, item + " выбран", Toast.LENGTH_LONG).show();

                HashMap<String, Object> obj = (HashMap<String, Object>) ((ListView) parent).getAdapter().getItem(position);
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = " + id);


                String idt = obj.get("id").toString();
                Toast.makeText(GridActivity.this, idt + " выбран", Toast.LENGTH_LONG).show();



                if (head.getText().toString() == "Root") {
                    openItem("category", idt, "");
                } else {
                    openItem("item", head.getText().toString(), idt);
                }
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

    private void renderJitem(ListView v,ArrayList<Map<String, Object>> data) {

        final String ATTRIBUTE_NAME_KEY = "key";
        final String ATTRIBUTE_NAME_VALUE = "value";
        final String ATTRIBUTE_NAME_IMAGE = "image";

        String[] from = {ATTRIBUTE_NAME_IMAGE, ATTRIBUTE_NAME_KEY, ATTRIBUTE_NAME_VALUE};
        int[] to = {R.id.ivImg, R.id.textView1, R.id.textView2};
        SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.item_order, from, to);

        //final String ATTRIBUTE_NAME_PICTURE = "picture";
        //final String ATTRIBUTE_NAME_NAME = "name";
        //final String ATTRIBUTE_NAME_PRICE = "price";

        //String[] from = {ATTRIBUTE_NAME_PICTURE, ATTRIBUTE_NAME_NAME, ATTRIBUTE_NAME_PRICE};
        //int[] to = {R.id.picture, R.id.product_name, R.id.price};
        //SimpleAdapter adaptor = new SimpleAdapter(this, data, R.layout.item_product, from, to);


        v.setAdapter(adaptor);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  String item = (String) ((ListView) parent).getAdapter().getItem(position).toString();
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

    private ArrayList<Map<String, Object>> jsonarrayy2item(JSONObject j){

        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> m;
        int img = R.mipmap.ic_launcher;
        final String ATTRIBUTE_NAME_IMAGE = "image";
        final String ATTRIBUTE_NAME_KEY = "key";
        final String ATTRIBUTE_NAME_VALUE = "value";

        Object itemvalue = new Object();  // try init

        int i=0;

        Iterator<String> iter = j.keys();

        while(iter.hasNext()){

            String it = iter.next();

            try {
                itemvalue = j.get(it);

                m = new HashMap<>();
                m.put(ATTRIBUTE_NAME_IMAGE, img);
                m.put(ATTRIBUTE_NAME_KEY, it);
                m.put(ATTRIBUTE_NAME_VALUE, itemvalue);

                list.add(i, m);
                i++;

            }catch(JSONException e){
                Log.d(TAG, e.getMessage());
            }
        }
        return list;
    }

    private ArrayList<Map<String, Object>> jsonarrayy2product(JSONObject j){

        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> m;
        int varpicture = R.mipmap.ic_launcher;

        final String ATTRIBUTE_NAME_PICTURE = "picture";
        final String ATTRIBUTE_NAME_NAME = "name";
        final String ATTRIBUTE_NAME_PRICE = "price";

        String varname = "";
        String varprice = "";

        Object itemvalue = new Object();  // try init

        Iterator<String> iter = j.keys();

        while(iter.hasNext()){

            String it = iter.next();

            if (it == "product_name") {

                m = new HashMap<>();
                m.put(ATTRIBUTE_NAME_PICTURE, varpicture);
                m.put(ATTRIBUTE_NAME_NAME, varname);
                m.put(ATTRIBUTE_NAME_PRICE, varprice);

                list.add(1, m);

                try {
                    itemvalue = j.get(it);
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                }
            }

        }



        return list;
    }

    private ArrayList<Map<String, Object>> jsonarrayy2root(JSONArray j){

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
                    itemtext = j.get(i).toString();
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

    private String getPref (String key) {
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(key, "");
    }

}





