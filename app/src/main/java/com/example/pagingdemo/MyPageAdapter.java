package com.example.pagingdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MyPageAdapter extends PagedListAdapter<Student, MyPageAdapter.MyPageHoler> {

    protected MyPageAdapter() {
        super(new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getNum() == newItem.getNum();
            }
        });
    }

    @NonNull
    @Override
    public MyPageHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_cell,parent,false);
        return new MyPageHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageHoler holder, int position) {
        Student student = (Student) getItem(position);
        if (student == null) {
            //分批加载，数据可能少于容器所以student可能为空
            holder.textView.setText("Loading");
        } else {
            holder.textView.setText(String.valueOf(student.getNum()));
        }
    }

    static class MyPageHoler extends RecyclerView.ViewHolder {
        TextView textView;
        public MyPageHoler(View itemview) {
            super(itemview);
            textView = itemview.findViewById(R.id.textView);
        }
    }
}
