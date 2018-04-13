package com.example.zhishaoju.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private HorizontalScrollView mHorizontalScrollView;
    private TextView mDeleteTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        mHorizontalScrollView = findViewById(R.id.scrollView);
        mDeleteTV = (TextView) findViewById(R.id.browse_delete);

        mHorizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 手抬起的时候判断滑动距离
                        int slideDistance =
                                mHorizontalScrollView.getScrollX();// 获取向左滑动的距离，是一个非负数，px
                        int delete = mDeleteTV.getWidth();// 删除按钮的宽度，单位px
                        // 当滑动距离大于删除按钮宽度的一半时，就自动滑动到最左边，完全显示删除按钮，所谓的最左边就是滑动距离等于删除按钮宽度
                        // 当滑动距离小于删除按钮宽度的一半时，就隐藏删除按钮，即滑动距离等于0的位置
                        if (slideDistance >= delete / 2) {
                            mHorizontalScrollView.scrollTo(delete, 0);
//                            currentSlideView = holder.scrollView;// 保存当前滑开的item
                        } else {
                            mHorizontalScrollView.scrollTo(0, 0);
//                            currentSlideView = null;// 清空
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 滑动的时候判断当前有没有其它滑开的item，有的话就隐藏
//                        if (currentSlideView != holder.scrollView) {
//                            autoHide();
//                        }
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }
}
