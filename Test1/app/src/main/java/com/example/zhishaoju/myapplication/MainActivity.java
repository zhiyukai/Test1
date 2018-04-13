package com.example.zhishaoju.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhishaoju.myapplication.model.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mTestLayoutTV;
    private TextView mRightTV;
    private LinearLayout mRootLayoutLL;
    private int lastX;
    private int lastY;
    private int mRightWidth;

    private TextView mSendHanderTV;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String a = null;
        boolean flag = "B".equals(a);
        Log.e("zhishaoju", "\"B\".equals(a) =  " + String.valueOf(flag));

//        startActivity(new Intent(this, Main2Activity.class));
        mTestLayoutTV = (TextView) findViewById(R.id.tv_test);
        mRootLayoutLL = (LinearLayout) findViewById(R.id.ll_root);
        mRightTV = (TextView) findViewById(R.id.tv_right);

        mSendHanderTV = (TextView) findViewById(R.id.tv_send_handle);

        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        mRightTV.measure(width, height);
        mRightWidth = mRightTV.getMeasuredWidth(); // 获取宽度
        mRightTV.getMeasuredHeight(); // 获取高度


//        mTestLayoutTV.offsetLeftAndRight(400);
//        mTestLayoutTV.layout(-300, mTestLayoutTV.getTop() + 200,
//                mTestLayoutTV.getWidth() - 400, mTestLayoutTV.getHeight());
        mTestLayoutTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //获取到手指处的横坐标和纵坐标
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算移动的距离
                        int offX = x - lastX;
                        int offY = y - lastY;
                        //调用layout方法来重新放置它的位置
                        mTestLayoutTV.layout(mTestLayoutTV.getLeft() + offX,
                                mTestLayoutTV.getTop() + offY,
                                mTestLayoutTV.getRight() + offX, mTestLayoutTV.getBottom() +
 offY);
                        mRightTV.layout(mTestLayoutTV.getRight() + offX,
                                mTestLayoutTV.getTop() + offY,
                                mTestLayoutTV.getRight() + mRightWidth
                                        + offX,
                                mTestLayoutTV.getBottom() + offY);
                        Log.e("zhishaoju",
                                "mRightTV.getMeasuredWidth() = " + mRightTV.getMeasuredWidth());
                        Log.e("zhishaoju", "mRightTV.getRight() = " + mRightTV.getRight());
//                        mRootLayoutLL.postInvalidate();
                        break;
                }
                return true;
            }
        });
        test();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper looper = Looper.getMainLooper();
//                Looper.myLooper();
//                Looper.prepare();
//            }
//        }).start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        test(null);
        testSparseArray();
        setRes(R.string.app_name);

//        String url = "http://img1.dzwww.com:8080/tupian_pl/20150813/16/7858995348613407436.jpg";
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        Glide.with(this)
//                .load(url)
//                .into(imageView);


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    SystemClock.sleep(45000);
                }
            }
        };


        mSendHanderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mHandler.sendEmptyMessage(1);
                Log.e("zhishaoju", "start time = " + System.currentTimeMillis());
                SystemClock.sleep(5000);
                for (int i = 0; i < 1000000; i++) {
                    Log.v("zhishaoju", "kskkdkd");
                }
                Log.e("zhishaoju", "end time = " + System.currentTimeMillis());
            }
        });
//        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void testSparseArray() {
        SparseArray sparseArray = new SparseArray();
        sparseArray.put(1, "1");
        sparseArray.put(2, "2");
        sparseArray.put(3, "3");
        sparseArray.put(1, "4");

        int size = sparseArray.size();

        for (int i = 0; i < size; i++) {
            Log.e("zhishaoju", "" + sparseArray.keyAt(i));
            Log.e("zhishaoju", "" + sparseArray.valueAt(i));
        }
    }

    private void test() {
        ArrayList<Person> list = new ArrayList<>();
        Person p1 = new Person("张三", 19);
        Person p2 = new Person("李四", 23);
        list.add(p1);
        list.add(p2);

        JSONObject j1 = new JSONObject();
        JSONObject j2 = new JSONObject();
        try {
            j1.put("j_name", p1.name);
            j1.put("j_age", p1.age);
            j2.put("j_name", p2.name);
            j2.put("j_age", p2.age);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(j1);
        jsonArray.put(j2);
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("personList", list);
            jsonObject.put("personList", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject2 = new JSONObject(jsonObject.toString());
//            ArrayList<Person> arrayList = (ArrayList<Person>) jsonObject2.get("personList");
//
//            if (arrayList != null) {
//                int len = arrayList.size();
//                for (int i = 0; i < len; i++) {
//                    Person person = (Person) arrayList.get(i);
//                    Log.e("zhishaoju",
//                            "person" + i + "= [person.name = " + person.name + ",person.age = "
//                                    + person.age + "]");
//                }
//            }

            JSONArray jsonArray1 = jsonObject2.getJSONArray("personList");

            if (jsonArray1 != null) {
                int len = jsonArray1.length();
                for (int i = 0; i < len; i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    Log.e("zhishaoju",
                            "person" + i + " = [person.name = " + jsonObject1.optString("j_name")
                                    + ", person.age = "
                                    + jsonObject1.optString("j_age") + "]");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void test(@NonNull String t) {

    }

    private void setRes(@StringRes int id) {

    }
}
