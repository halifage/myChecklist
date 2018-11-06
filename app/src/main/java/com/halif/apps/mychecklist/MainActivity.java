package com.halif.apps.mychecklist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;


//TODO add logic to handle checkbox and delete clicks


public class MainActivity extends AppCompatActivity {
    ArrayList<ChecklistItem> listItems;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listItems = new ArrayList();
        listItems.add(new ChecklistItem(false, "one"));
        listItems.add(new ChecklistItem(false, "two"));
        listItems.add(new ChecklistItem(false, "three"));

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ListAdapter(listItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickedListener(new ListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listItems.get(position);
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteIconClicked(int position) {
                if(position == listItems.size()-1){
                    removeItem(position);
                }else {
                    removeItem(position);
                    addItemChecked();
                }
            }
        });

    }

    public void addItemUnchecked() {
        listItems.add(0, new ChecklistItem(false, "added"));
        adapter.notifyItemInserted(0);
    }

    public void addItemChecked() {
        listItems.add(new ChecklistItem(true, "added"));
        adapter.notifyItemInserted(listItems.size()-1);
    }

    public void removeItem(int position) {
        listItems.remove(position);
        adapter.notifyItemRemoved(position);
    }

}

