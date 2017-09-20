package ua.cn.sandi.jsonview;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by mikni on 19.09.2017.
 */

public class JsonObjectRequest {

    public JSONObject getResponsefull() {
        return responsefull;
    }

    private JSONObject responsefull;

    public void setUrl(String url) {

        com.android.volley.toolbox.JsonObjectRequest jsonObjReq = new com.android.volley.toolbox.JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            responsefull = response;
                        }catch(Exception e){
                            VolleyLog.d(TAG, "Error: " + e.getMessage());
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
