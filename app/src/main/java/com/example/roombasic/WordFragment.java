package com.example.roombasic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class WordFragment extends Fragment {

    private DataBaseViewModel wordViewModel;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter_cardView,myAdapter_normalView;
    private LiveData<List<Word>> filteredWords;
    private static final String view_type = "view_type";
    private static final String view_type_key = "type";

    public WordFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredWords.removeObservers(requireActivity()); //先移除原来的观察，下面重新观察
                filteredWords = wordViewModel.filterWords(newText.trim());
                filteredWords.observe(requireActivity(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = myAdapter_normalView.getItemCount();
                        myAdapter_normalView.setAllWords(words);
                        myAdapter_cardView.setAllWords(words);
                        if (temp != words.size()) {
                            /*只有数据数量发生改变的时候采取刷新界面，
                            switchChineseInvisible的改变不要触发界面刷新，否则不平滑*/
                            myAdapter_normalView.notifyDataSetChanged();
                            myAdapter_cardView.notifyDataSetChanged();
                        }
                    }
                });
                return true; //不需要继续传递
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = requireActivity().findViewById(R.id.recyclerview);
        FloatingActionButton floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        wordViewModel = new ViewModelProvider(this,new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(DataBaseViewModel.class);
        myAdapter_normalView = new MyAdapter(false,wordViewModel);
        myAdapter_cardView = new MyAdapter(true,wordViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        SharedPreferences shp = requireActivity().getSharedPreferences(view_type, Context.MODE_PRIVATE);
        boolean viewType = shp.getBoolean(view_type_key,false);
        if (viewType)
            recyclerView.setAdapter(myAdapter_cardView);
        else
            recyclerView.setAdapter(myAdapter_normalView);

        filteredWords = wordViewModel.getAllWords();
        filteredWords.observe(requireActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter_normalView.getItemCount();
                myAdapter_normalView.setAllWords(words);
                myAdapter_cardView.setAllWords(words);
                if (temp != words.size()) {
                    /*只有数据数量发生改变的时候采取刷新界面，
                    switchChineseInvisible的改变不要触发界面刷新，否则不平滑*/
                    myAdapter_normalView.notifyDataSetChanged();
                    myAdapter_cardView.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearData:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Are you sure to clear all words?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        wordViewModel.deleteAllWords();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.switchView:
                SharedPreferences shp = requireActivity().getSharedPreferences(view_type, Context.MODE_PRIVATE);
                boolean viewType = shp.getBoolean(view_type_key,false);
                SharedPreferences.Editor editor = shp.edit();
                if(viewType) { //卡片视图
                    recyclerView.setAdapter(myAdapter_normalView);
                    editor.putBoolean("type",false);
                } else {
                    recyclerView.setAdapter(myAdapter_cardView);
                    editor.putBoolean("type",true);
                }
                editor.apply();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}