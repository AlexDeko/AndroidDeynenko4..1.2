package com.homework1_3.androiddeynenko42;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final static String TITLE = "title";
    private final static String SUBTITLE = "subtitle";
    private final static String TEXT = "text";
    private SharedPreferences sharedPref;
    private ListView list;
    private String result;
    private String[] content ;
    private SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateList();
        content = prepareContent();
        final BaseAdapter listContentAdapter = createAdapter(content);
        list.setAdapter(listContentAdapter);
        listContentAdapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final View item = (TextView) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.removeView(item);
                                listContentAdapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
                listContentAdapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // Будет вызван, когда пользователь потянет список вниз
            @Override public void onRefresh() {
                updateList();
                swipeLayout.setRefreshing(false);
            }
        });
        }

        private void updateList(){
            sharedPref = getSharedPreferences(getString(R.string.large_text), Context.MODE_PRIVATE);
            myEditor = sharedPref.edit();
        if (!getString(R.string.large_text).contentEquals(sharedPref.toString())) {
            myEditor.putString(TEXT, getString(R.string.large_text));
            myEditor.apply();
            result = sharedPref.getString(TEXT, getString(R.string.large_text));
        }
            content = prepareContent();
        }

    @NonNull
    private BaseAdapter createAdapter(String[] values) {
        list = findViewById(R.id.list);
        List<Map<String, String>> simpleAdapterContent = new ArrayList<>();

        for (String value : values) {
            Map<String, String> row = new HashMap<>();
            row.put(TITLE, value);
            row.put(SUBTITLE, String.valueOf(value.length()));
            simpleAdapterContent.add(row);
        }
        return new SimpleAdapter(
                this,
                simpleAdapterContent,
                R.layout.lists,
                new String[]{TITLE, SUBTITLE},
                new int[]{R.id.textItem1, R.id.textItem2}
        );
    }

    @NonNull
    private String[] prepareContent() {
        return result.split("\n\n");
    }

    @Override
    public String toString() {
        return
                " " + sharedPref;
    }
}