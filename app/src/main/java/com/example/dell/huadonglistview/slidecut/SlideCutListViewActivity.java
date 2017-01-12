package com.example.dell.huadonglistview.slidecut;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.dell.huadonglistview.R;

import java.util.ArrayList;
import java.util.List;

public class SlideCutListViewActivity extends Activity implements SlideCutListView.RemoveListener {
    private SlideCutListView slideCutListView ;
    private ArrayAdapter<String> adapter;
    private List<String> dataSourceList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidecutlistview);
        init();
    }

    private void init() {
        slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
        slideCutListView.setRemoveListener(this);

        for(int i=0; i<20; i++){
            dataSourceList.add("listview" + i);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.list_item, dataSourceList);
        slideCutListView.setAdapter(adapter);

        slideCutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(SlideCutListViewActivity.this, dataSourceList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void removeItem(SlideCutListView.RemoveDirection direction, int position) {
        adapter.remove(adapter.getItem(position));

        switch (direction) {
            case RIGHT:
                Toast.makeText(this, "右划----"+ position, Toast.LENGTH_SHORT).show();
                break;
            case LEFT:
                Toast.makeText(this, "左划----"+ position, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
