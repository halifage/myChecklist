package com.halif.apps.mychecklist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


//TODO add logic to handle checkbox and delete clicks


public class MainActivity extends AppCompatActivity {
    ArrayList<ChecklistItem> listItems;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText title;
    private EditText newListItem;
    private int endOfList;
    private int startOfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildAndInitialize();


    }

    private void buildAndInitialize() {
        listItems = new ArrayList<>();
        listItems.add(new ChecklistItem("one", false));
        listItems.add(new ChecklistItem("two", false));
        listItems.add(new ChecklistItem("three", false));
        startOfList = 0;
        endOfList = listItems.size() - 1;

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
            public void onItemCheckStateChange(int position, Boolean isChecked) {
                if (isChecked) {
                    itemChecked(position);
                } else {
                    itemUnChecked(position);
                }
            }

            @Override
            public void onItemDelete(int position) {
                removeItem(position);
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
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public void itemUnChecked(int position) {
        ChecklistItem currentItem = listItems.get(position);

        if (position == startOfList) {
            currentItem.setCheckBox(false);
            adapter.notifyItemChanged(endOfList);
        } else {
            ChecklistItem itemAboveCurrent = listItems.get(position - 1);

            if (itemAboveCurrent.getCheckBox()) {
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
                listItems.add(startOfList, new ChecklistItem(currentItem.getItemText(), false));
                adapter.notifyItemInserted(startOfList);
            } else {
                currentItem.setCheckBox(false);
                adapter.notifyItemChanged(position);
            }
        }

    }

    public void itemChecked(int position) {
        ChecklistItem currentItem = listItems.get(position);

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

    public String removeItem(int position) {
        String itemText = listItems.get(position).getItemText();
        listItems.remove(position);
        adapter.notifyItemRemoved(position);
        return itemText;
    }

}

