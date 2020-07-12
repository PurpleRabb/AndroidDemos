package com.example.roombasic;

import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Word> allWords = new ArrayList<>();
    boolean is_cardView;

    public MyAdapter(boolean is_cardView) {
        this.is_cardView = is_cardView;
    }

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if(is_cardView)
            itemView = layoutInflater.inflate(R.layout.cell_card,parent,false);
        else
            itemView = layoutInflater.inflate(R.layout.cell_normal,parent,false);
        return new MyViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //填充item的数据
        Word word = allWords.get(position);
        holder.textViewNum.setText(String.valueOf(position));
        holder.textViewChinese.setText(word.getMeaning());
        holder.textViewEnglish.setText(word.getWord());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.youdao.com/w/eng/" + holder.textViewEnglish.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNum,textViewEnglish,textViewChinese;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewNum = itemView.findViewById(R.id.textViewNum);
            this.textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            this.textViewChinese = itemView.findViewById(R.id.textViewChinese);
        }
    }
}
