package com.example.dell.huadonglistview.everyview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.huadonglistview.R;

import java.util.ArrayList;

/**
 * Created by dell on 2017/1/11.
 */
public class EveryViewActivity extends Activity{
    private float max = 300;//你想滑动的极限长度默认  本demo以删除布局宽度为max
    private ArrayList<String> data = new ArrayList<String>() {{
        add("str01");
        add("str02");
        add("str03");
        add("str04");
        add("str05");
        add("str06");
        add("str07");
        add("str08");
    }};
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everyview);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new IAdapter());
    }

    class IAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(EveryViewActivity.this, R.layout.every_item, null);
            }
            ParentOnTouchChildClickLinearLayout root = (ParentOnTouchChildClickLinearLayout) view.findViewById(R.id.root);
            TextView tvTestClick = (TextView) view.findViewById(R.id.tvTestClick);
            final LinearLayout llContext = (LinearLayout) view.findViewById(R.id.llContext);
            final LinearLayout llDelete = (LinearLayout) view.findViewById(R.id.llDelete);

            tvTestClick.setText(data.get(position));
            ViewTreeObserver vto = llDelete.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    //监听一次马上结束

                    if (Build.VERSION.SDK_INT < 16) {
                        llDelete.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        llDelete.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    max = llDelete.getWidth();
                    //得到删除按钮长度 得到最大拖动限定
                    Log.i("rex", "max--" + max);

                }
            });


            llContext.setTranslationX(0);
            llDelete.setTranslationX(0);
            view.setScaleY(1);
            view.setTranslationY(0);

            final View finalView = view;
            llDelete.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    //删除
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(finalView, "scaleY", 1, 0);
                    scaleY.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            data.remove(position);
                            IAdapter.this.notifyDataSetChanged();
                        }
                    });
                    scaleY.setDuration(800).start();
                    for (int i = 1; i < lv.getChildCount() - position; i++) {
                        ObjectAnimator.ofFloat(lv.getChildAt(i + position), "translationY", 0, -finalView.getMeasuredHeight()).setDuration(800).start();
                    }


                }
            });
            tvTestClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(EveryViewActivity.this, "测试按钮被调用！", Toast.LENGTH_SHORT).show();
                }
            });


            llContext.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(EveryViewActivity.this, "item 长按被调用！", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


            //点击内容让item回到最初的位置
            llContext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击归位
                    ObjectAnimator.ofFloat(llContext, "translationX", llContext.getTranslationX(), 0).setDuration(600).start();
                    ObjectAnimator.ofFloat(llDelete, "translationX", llDelete.getTranslationX(), 0).setDuration(600).start();
                }
            });


            root.setOnTouchListener(new View.OnTouchListener() {

                private float diff;
                float x = -1;
                float mx;
                boolean isMove;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (max == 0) {
                        return false;
                    }
                    //当按下时处理
                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        //由于父onInterceptTouchEvent 为false所以down无效 且不需要 以-1作为初始X
                        //这里类似一般写法的ACTION_DOWN初始化
                        if (x == -1)
                            x = event.getRawX();

                        mx = event.getRawX();
                        isMove = true;
                        diff = mx - x;

                        if (diff < -max)
                            diff = -max;

                        if (llContext.getTranslationX() > 0 && diff > llContext.getTranslationX())
                            diff = llContext.getTranslationX();

                        llContext.setTranslationX(diff);
                        llDelete.setTranslationX(diff);

                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        x = -1;
                        if (isMove) {
                            //自动归位  过半则全部显示删除布局  反之则回收为正常
                            if (diff < -max / 2.0f) {
                                ObjectAnimator.ofFloat(llContext, "translationX", diff, -max).setDuration(600).start();
                                ObjectAnimator.ofFloat(llDelete, "translationX", diff, -max).setDuration(600).start();
                            } else {
                                ObjectAnimator.ofFloat(llContext, "translationX", diff, 0).setDuration(600).start();
                                ObjectAnimator.ofFloat(llDelete, "translationX", diff, 0).setDuration(600).start();
                            }
                            return true;
                        } else {
                            return false;
                        }

                    } else {//其他模式
                        //设置背景为未选中正常状态
                        //v.setBackgroundResource(R.drawable.mm_listitem_simple);

                    }
                    return true;
                }
            });


            return view;
        }
    }
}
