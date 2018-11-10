package com.halif.apps.mychecklist;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    public ArrayList<ChecklistItem> checklistItems;

    public ListAdapter(ArrayList<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    private onItemClickListener listener;

    public interface onItemClickListener {
//        void onItemClick(int position);

        void onItemCheckStateChange(int position, Boolean isChecked);

        void onItemDelete(int position);
    }

    public void setOnItemClickedListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {

        ChecklistItem checklistItem = checklistItems.get(i);
        EditText itemText = listViewHolder.editText;
        CheckBox itemCheckBox = listViewHolder.checkBox;
        itemText.setText(checklistItem.getItemText());
        itemCheckBox.setChecked(checklistItem.getCheckBox());

        if (itemCheckBox.isChecked()) {
            itemText.setPaintFlags(itemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemText.setTextColor(Color.GRAY);
            itemText.setTextSize(16);
        } else {
            itemText.setPaintFlags(0);
            itemText.setTextColor(Color.BLACK);
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        ListViewHolder myViewHolder = new ListViewHolder(view, listener);
        return myViewHolder;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;
        public EditText editText;
        public ImageButton imageButton;

        public ListViewHolder(@NonNull final View itemView, final ListAdapter.onItemClickListener listener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            editText = itemView.findViewById(R.id.edit_text);
            imageButton = itemView.findViewById(R.id.delete_button);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemDelete(position);
                        }
                    }
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemCheckStateChange(position, checkBox.isChecked());
                        }
                    }
                }
            });

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b)
                        imageButton.setVisibility(View.VISIBLE);
                    else
                        imageButton.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }


}
