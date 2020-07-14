package com.example.roombasic;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class WordFragment extends Fragment {

    private DataBaseViewModel wordViewModel;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter_cardView,myAdapter_normalView;
    private FloatingActionButton floatingActionButton;

    public WordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = requireActivity().findViewById(R.id.recyclerview);
        floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        wordViewModel = new ViewModelProvider(this,new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(DataBaseViewModel.class);
        myAdapter_normalView = new MyAdapter(false,wordViewModel);
        myAdapter_cardView = new MyAdapter(true,wordViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        //aSwitch = findViewById(R.id.switch_cardview);
        recyclerView.setAdapter(myAdapter_normalView);
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b) {
//                    recyclerView.setAdapter(myAdapter_cardView);
//                } else {
//                    recyclerView.setAdapter(myAdapter_normalView);
//                }
//            }
//        });

        //textView = requireActivity().findViewById(R.id.textViewEnglish);

        wordViewModel.getAllWords().observe(requireActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter_normalView.getItemCount();
                myAdapter_cardView.setAllWords(words);
                myAdapter_cardView.notifyDataSetChanged();
                if (temp != words.size()) {
                    /*只有数据数量发生改变的时候采取刷新界面，
                    switchChineseInvisible的改变不要触发界面刷新，否则不平滑*/
                    myAdapter_normalView.setAllWords(words);
                    myAdapter_normalView.notifyDataSetChanged();
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(),R.id.fragment);
                navController.navigate(R.id.action_wordFragment_to_addFragment);
            }
        });
    }
}