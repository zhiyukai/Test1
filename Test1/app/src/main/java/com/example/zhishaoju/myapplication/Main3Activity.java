package com.example.zhishaoju.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhishaoju.myapplication.model.Person;

/**
 * Created by zhishaoju on 2018/4/13.
 */

public class Main3Activity extends Activity {

    private TextView mTextTV;
    private Button mButton1BT;
    private Button mButton2BT;
    private Button mButton3BT;
    private Button mButton4BT;
    private Button mButton5BT;
    private Button mButton6BT;

    private Handler mHandler;
    private Handler mHandler2;
    private static Handler mHandler3;

    private Person mPerson;

    private ImageView mImageView;

    private Runnable mRunnable;

    private int mIndex;

    private int mImages[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher_round};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mTextTV = (TextView) findViewById(R.id.tv_text);
        mButton1BT = (Button) findViewById(R.id.bt_button1);
        mButton2BT = (Button) findViewById(R.id.bt_button2);
        mButton3BT = (Button) findViewById(R.id.bt_button3);
        mButton4BT = (Button) findViewById(R.id.bt_button4);
        mButton5BT = (Button) findViewById(R.id.bt_button5);
        mButton6BT = (Button) findViewById(R.id.bt_button6);
        mImageView = (ImageView) findViewById(R.id.iv_img);

        mHandler = new Handler();

        mHandler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mTextTV.setText("Button4");
            }
        };

        mHandler3 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mTextTV.setText("Button5 拦截");
                return true;
            }
        }) {
            @Override
            public void handleMessage(Message msg) {
                mTextTV.setText("Button5 不拦截");
            }
        };

        mPerson = new Person("", 0) {

            @Override
            public int getAge() {
                return 20;
            }

        };

        mRunnable = new Runnable() {

            @Override
            public void run() {
                mIndex ++;

                mIndex = mIndex % 2;

                mImageView.setImageResource(mImages[mIndex]);

                mHandler.postDelayed(mRunnable, 1000);
            }
        };

        setOnClickLienter();

        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    private void setOnClickLienter() {
        mButton1BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextTV.setText("Button1");
                            }
                        });
                    }
                }).start();
            }
        });

        mButton2BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mTextTV.post(new Runnable() {
                            @Override
                            public void run() {
                                mTextTV.setText("Button2");
                            }
                        });
                    }
                }).start();
            }
        });

        mButton3BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTextTV.setText("Button3");
                            }
                        });
                    }
                }).start();
            }
        });

        mButton4BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler2.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

        mButton5BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("zhishaoju", mPerson.getAge() + "");
                        mHandler3.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

        mButton6BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("zhishaoju", mPerson.getAge() + "");
                        mHandler3.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
    }


}
