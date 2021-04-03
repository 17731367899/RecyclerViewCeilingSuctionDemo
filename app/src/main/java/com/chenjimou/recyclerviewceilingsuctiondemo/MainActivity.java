package com.chenjimou.recyclerviewceilingsuctiondemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        List<Model> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 20; j++) {
                if (i % 2 == 0) {
                    list.add(new Model("何炅" + j, "快乐家族" + i));
                } else {
                    list.add(new Model("汪涵" + j, "天天兄弟" + i));
                }
            }
        }
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyDecoration(this));
        recyclerView.setAdapter(new MyAdapter(this, list));
    }
}