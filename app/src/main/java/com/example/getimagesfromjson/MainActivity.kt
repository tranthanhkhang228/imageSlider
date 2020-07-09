package com.example.getimagesfromjson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kaopiz.kprogresshud.KProgressHUD
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder


const val EXTRA_DATA = "com.example.viewpage.DATA"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getAlbumBtn = findViewById<Button>(R.id.getAlbumBtn);
        val urlT = findViewById<EditText>(R.id.urlT);

        getAlbumBtn.setOnClickListener {



            getData(urlT.text.toString()) };
    }

    fun showData(dataInString: String) {
        val intent = Intent(this, ViewPage::class.java).apply {
            putExtra(EXTRA_DATA, dataInString)
        }
        startActivity(intent)
    }

    fun getData(url: String) {
        if (url.isNotEmpty()) {
            val queue = Volley.newRequestQueue(this);
            val url: String = "https://" + url;

            val stringReq =
                StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->

                    var strResp = "";

                    try {
                        strResp = URLDecoder.decode(URLEncoder.encode(response.toString(), "iso8859-1"),"UTF-8");

                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                    showData(strResp)
                },
                    Response.ErrorListener { Log.i("MyActivity", "error") }
                )




            queue.add(stringReq);

            KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter url!", Toast.LENGTH_LONG).show();
        }
    }
}
