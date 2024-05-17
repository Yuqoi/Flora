package com.example.aplikacja.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacja.R;
import com.example.aplikacja.helpers.StringSelectListener;
import com.example.aplikacja.models.SearchItem;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {

    private List<SearchItem> searchList;
    private Context context;

    private StringSelectListener listener;

    public SearchHistoryAdapter(Context context, List<SearchItem> searchList) {
        this.context = context;
        this.searchList = searchList;
    }
    public void setSelectedListener(StringSelectListener listener){this.listener = listener;}

    @NonNull
    @Override
    public SearchHistoryAdapter.SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchHistoryAdapter.SearchHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.search_history_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.SearchHistoryViewHolder holder, int position) {
        holder.bind(searchList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.setSelectedItem(searchList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }


    public class SearchHistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.search_history_textview);
        }
        public void bind(SearchItem searchItem) {
            textView.setText(searchItem.getText());
        }
    }
}
