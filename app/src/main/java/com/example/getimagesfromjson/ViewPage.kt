package com.example.getimagesfromjson


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View.OnTouchListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import org.json.JSONArray
import org.json.JSONObject


lateinit var albumNameTextView: TextView;
lateinit var imageSlider: ViewPager;
lateinit var descriptionTextView: TextView;
var mScaleFactor = 1.0f
var cap = false;

class ViewPage : AppCompatActivity() {
    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var mDetector2: GestureDetectorCompat
    private lateinit var sDetector: ScaleGestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_page)

        albumNameTextView = findViewById<TextView>(R.id.albumNameTV);
        imageSlider = findViewById<ViewPager>(R.id.imageSlider)
        descriptionTextView = findViewById<TextView>(R.id.descriptionTV);

        mDetector = GestureDetectorCompat(this, MyGestureListener());
        mDetector2 = GestureDetectorCompat(this, MyGestureListener2());
        sDetector = ScaleGestureDetector(this, ScaleListener());


        setData();


        albumNameTextView.setOnTouchListener(touchListener);
        descriptionTextView.setOnTouchListener(touchListener2);
    }

    fun setData() {
        val data = intent.getStringExtra(EXTRA_DATA);
        val jsonObj: JSONObject = JSONObject(data)
        albumNameTextView.text = jsonObj.getString("name");
        val jsonArray: JSONArray = jsonObj.getJSONArray("images")

        val urls = Array<String>(jsonArray.length()) { "" };
        val descriptions = Array<String>(jsonArray.length()) { "" };

        for (i in 0 until jsonArray.length()) {
            var jsonInner: JSONObject = jsonArray.getJSONObject(i)
            urls[i] = jsonInner.getString("name")
            descriptions[i] = jsonInner.getString("description")
        }

        descriptionTextView.text = descriptions[0];

        val imageSliderAdapter = ImageSliderAdapter(urls)
        imageSlider.adapter = imageSliderAdapter

        imageSlider.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                descriptionTextView.text = descriptions[position];
                ;
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    var touchListener = OnTouchListener { v, event -> // pass the events to the gesture detector
        // a return value of true means the detector is handling it
        // a return value of false means the detector didn't
        // recognize the event
        mDetector.onTouchEvent(event)
    }

    var touchListener2 = OnTouchListener { v, event -> // pass the events to the gesture detector
        // a return value of true means the detector is handling it
        // a return value of false means the detector didn't
        // recognize the event
        mDetector2.onTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // re-route the Touch Events to the ScaleListener class
        sDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private class MyGestureListener2 : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean {
            Log.i("MyActivity", "onDown")
            return true
        }


        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.i("MyActivity", "abc: $e?.source");
            cap = !cap;
            descriptionTextView.isAllCaps = cap;
            return super.onDoubleTap(e)
        }
    }

    private class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent): Boolean {
            Log.i("MyActivity", "onDown")
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            Log.i("MyActivity", "abc: ${e?.source}");

            var num = (0..8).random();
            var listOfColor = arrayOf(
                "#f24b3f",
                "#24eb21",
                "#2552f5",
                "#f5f52f",
                "#eb1abd",
                "#f0ac1a",
                "#c817e3",
                "#4d4a46",
                "#ebe6df"
            );
            albumNameTextView.setTextColor(Color.parseColor(listOfColor[num]));
            super.onLongPress(e)
        }
    }

    private class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            Log.i("MyActivity", "onScale");

            mScaleFactor *= detector!!.scaleFactor;
            mScaleFactor = Math.max(
                0.1f,
                Math.min(mScaleFactor, 2.0f)
            );

            imageSlider.setScaleX(mScaleFactor);
            imageSlider.setScaleY(mScaleFactor);

            return super.onScale(detector)
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            Log.i("MyActivity", "onScaleBegin");
            return super.onScaleBegin(detector)
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            Log.i("MyActivity", "onScaleEnd");
            super.onScaleEnd(detector)
        }
    }

}
