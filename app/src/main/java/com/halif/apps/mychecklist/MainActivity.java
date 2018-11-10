package com.halif.apps.mychecklist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


//TODO add logic to handle checkbox and delete clicks


public class MainActivity extends AppCompatActivity {
    ArrayList<ChecklistItem> listItems;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText title;
    private EditText newListItem;
    private int startOfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newListItem = findViewById(R.id.list_item);
        title = findViewById(R.id.title);

        retrieveData();

        buildAndInitialize();


    }

    private void buildAndInitialize() {
        startOfList = 0;
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ListAdapter(listItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickedListener(new ListAdapter.onItemClickListener() {

            @Override
            public void onItemCheckStateChange(int position, Boolean isChecked) {
                if (isChecked) {
                    itemChecked(position);
                } else {
                    itemUnChecked(position);
                }
            }

            @Override
            public void onItemDelete(int position) {
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        newListItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String itemText = textView.getText().toString().trim();

                    if (itemText.length() > 0) {
                        listItems.add(startOfList, new ChecklistItem(itemText, false));
                        textView.setText("");
                        textView.requestFocus();
                        adapter.notifyItemInserted(startOfList);
                        return true;
                    }
                    return false;
                }
                return false;
            }
        });

        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String titleText = textView.getText().toString().trim();
                    if (titleText.length() > 0) {
                        setTitle(titleText);
                        title.setText(titleText);
//                        newListItem.setVisibility(View.VISIBLE);
                        newListItem.requestFocus();
                    } else {
                        title.setText("");
                        Toast.makeText(getApplicationContext(), "We really need this Title :-)",
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public void itemUnChecked(int position) {
        ChecklistItem currentItem = listItems.get(position);
        int endOfList = listItems.size() - 1;
        if (position == startOfList) {
            currentItem.setCheckBox(false);
            adapter.notifyItemChanged(endOfList);
            newListItem.requestFocus();
        } else {
            ChecklistItem itemAboveCurrent = listItems.get(position - 1);

            if (itemAboveCurrent.getCheckBox()) {
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
                listItems.add(startOfList, new ChecklistItem(currentItem.getItemText(), false));
                adapter.notifyItemInserted(startOfList);
                newListItem.requestFocus();
            } else {
                currentItem.setCheckBox(false);
                adapter.notifyItemChanged(position);
                newListItem.requestFocus();
            }
        }

    }

    public void itemChecked(int position) {

        ChecklistItem currentItem = listItems.get(position);
        int endOfList = listItems.size() - 1;

        if (position == endOfList) {
            currentItem.setCheckBox(true);
            adapter.notifyItemChanged(endOfList);
        } else {       // move to bottom only if the item below it is not checked
            ChecklistItem itemBelowCurrent = listItems.get(position + 1);
            if (!itemBelowCurrent.getCheckBox()) {
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
                listItems.add(endOfList, new ChecklistItem(currentItem.getItemText(), true));
                adapter.notifyItemInserted(endOfList);
            } else {
                currentItem.setCheckBox(true);
                adapter.notifyItemChanged(position);
            }
        }
    }

    public void saveData() {
        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences("Lists & Notes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String listJson = gson.toJson(listItems);
        editor.putString("Checklist", listJson);
        editor.putString("title", title.getText().toString());
        editor.apply();
    }

    public void retrieveData() {
        SharedPreferences sp = getSharedPreferences("Lists & Notes", MODE_PRIVATE);
        Gson gson = new Gson();
        title.setText(sp.getString("title", ""));
        setTitle(title.getText().toString());
        newListItem.requestFocus();
        String listJson = sp.getString("Checklist", null);
        Type type = new TypeToken<ArrayList<ChecklistItem>>() {
        }.getType();
        listItems = gson.fromJson(listJson, type);

        if (listItems == null)
            listItems = new ArrayList<>();
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }
}

