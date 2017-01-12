package com.example.dell.huadonglistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dell.huadonglistview.everyview.EveryViewActivity;
import com.example.dell.huadonglistview.slidecut.SlideCutListViewActivity;

/**
 * Created by dell on 2017/1/11.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private TextView slidecutTv,everyviewTv;

    private Intent itt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inintView();
    }

    public void inintView(){
        slidecutTv=(TextView)findViewById(R.id.slidecut);
        everyviewTv=(TextView)findViewById(R.id.everyview);

        slidecutTv.setOnClickListener(this);
        everyviewTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.slidecut:
                itt=new Intent(this,SlideCutListViewActivity.class);
                startActivity(itt);
                break;
            case R.id.everyview:
                itt=new Intent(this,EveryViewActivity.class);
                startActivity(itt);
                break;
            default:break;
        }
    }
}
