package com.halif.apps.mychecklist;

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

    private ArrayList<ChecklistItem> checklistItems;

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

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;
        public EditText editText;
        public ImageButton imageButton;

        public ListViewHolder(@NonNull final View itemView, final onItemClickListener listener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_checked);
            editText = itemView.findViewById(R.id.edit_text_checked);
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

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        ListViewHolder myViewHolder = new ListViewHolder(view, listener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {

        ChecklistItem checklistItem = checklistItems.get(i);
        listViewHolder.editText.setText(checklistItem.getItemText());
        listViewHolder.checkBox.setChecked(checklistItem.getCheckBox());
    }

    @Override
    public int getItemCount() {
        return checklistItems.size();
    }


}
