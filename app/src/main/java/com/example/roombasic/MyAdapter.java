package com.example.roombasic;

import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Word> allWords = new ArrayList<>();
    boolean is_cardView;
    DataBaseViewModel viewModel;

    public MyAdapter(boolean is_cardView, DataBaseViewModel viewModel) {
        this.is_cardView = is_cardView;
        this.viewModel = viewModel; //获取viewModel操作数据库
    }

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView;
        if(is_cardView)
            itemView = layoutInflater.inflate(R.layout.cell_card_switch,parent,false);
        else
            itemView = layoutInflater.inflate(R.layout.cell_normal_switch,parent,false);

        final MyViewHolder holder = new MyViewHolder(itemView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.youdao.com/w/eng/" + holder.textViewEnglish.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.aSwitchChiniseInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Word word = (Word) itemView.getTag();
                if (b) {
                    holder.textViewChinese.setVisibility(View.GONE);
                    word.setChineseInvisible(true);
                    viewModel.updateWords(word);
                } else {
                    holder.textViewChinese.setVisibility(View.VISIBLE);
                    word.setChineseInvisible(false);
                    viewModel.updateWords(word);
                }
            }
        });
        return new MyViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //填充item的数据
        final Word word = allWords.get(position);
        holder.textViewNum.setText(String.valueOf(position+1));
        holder.textViewChinese.setText(word.getMeaning());
        holder.textViewEnglish.setText(word.getWord());
        holder.itemView.setTag(word);

        //holder.aSwitchChiniseInvisible.setOnCheckedChangeListener(null);//刚开始移除监听器，防止二次触发
        if (word.isChineseInvisible()) {
            holder.textViewChinese.setVisibility(View.GONE);
            holder.aSwitchChiniseInvisible.setChecked(true);
        } else {
            holder.textViewChinese.setVisibility(View.VISIBLE);
            holder.aSwitchChiniseInvisible.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNum,textViewEnglish,textViewChinese;
        Switch aSwitchChiniseInvisible;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewNum = itemView.findViewById(R.id.textViewNum);
            this.textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            this.textViewChinese = itemView.findViewById(R.id.textViewChinese);
            this.aSwitchChiniseInvisible = itemView.findViewById(R.id.switchChineseInvisible);
        }
    }
}
