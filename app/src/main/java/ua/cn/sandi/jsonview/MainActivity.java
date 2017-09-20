package ua.cn.sandi.jsonview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import static android.content.ContentValues.TAG;

/**
 * Created by mikni on 23.08.2017.
 */


public class MainActivity extends Activity {

    Button bexit;
    Button bopen;
    Button bconfig;

    ImageView main;

    String logourl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = (ImageView)findViewById(R.id.myimage_main);

        bexit = (Button) findViewById(R.id.mybutton_exit);
        bexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Exit function");

                finish();
                moveTaskToBack(true);

            }
        });
        bopen = (Button) findViewById(R.id.mybutton_grid);
        bopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openintent = new Intent(MainActivity.this, GridActivity.class);
                startActivity(openintent);
            }
        });
        bconfig = (Button) findViewById(R.id.mybutton_config);
        bconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent configintent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(configintent);
            }
        });

        logourl = "http://mag.sandi.cn.ua/img/magsandi-logo-1503421528.jpg";

        loadimage(logourl, main);

    }

    private void loadimage(String url,final ImageView i){

        ImageLoader imageLoader = ApplicationController.getInstance().getImageLoader();

        // If you are using normal ImageView
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    i.setImageBitmap(response.getBitmap());
                }
            }
        });

    }
}
