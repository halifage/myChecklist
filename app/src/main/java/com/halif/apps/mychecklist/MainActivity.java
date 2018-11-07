package com.halif.apps.mychecklist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


//TODO add logic to handle checkbox and delete clicks


public class MainActivity extends AppCompatActivity {
    ArrayList<ChecklistItem> listItems;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText title;
    private EditText newListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildAndInitialize();


    }

    private void buildAndInitialize(){
        listItems = new ArrayList();
        listItems.add(new ChecklistItem("one"));
        listItems.add(new ChecklistItem("two"));
        listItems.add(new ChecklistItem("three"));

        title = findViewById(R.id.title);
        newListItem = findViewById(R.id.list_item);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 1);
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
            public void onItemCheckStateChange(int position, Boolean isChecked) {
                if(isChecked){
                    itemChecked(position);
                }else{
                    itemUnChecked(position);}
            }

            @Override
            public void onItemDelete(int position) {
                removeItem(position);
            }
        });
    }

    public void itemUnChecked(int position) {
        listItems.add(0, new ChecklistItem(removeItem(position)));
        adapter.notifyItemInserted(0);
    }

    public void itemChecked(int position) {
        ChecklistItem item = new ChecklistItem(removeItem(position));
        item.setCheckBox(true);
        listItems.add(item);
        adapter.notifyItemInserted(listItems.size()-1);
    }

    public String removeItem(int position) {
        String itemText = listItems.get(position).getItemText();
        listItems.remove(position);
        adapter.notifyItemRemoved(position);
        return itemText;
    }

}

