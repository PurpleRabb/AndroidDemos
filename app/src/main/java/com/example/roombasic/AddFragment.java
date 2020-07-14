package com.example.roombasic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


public class AddFragment extends Fragment {
    private TextView textViewEnglish;
    private TextView textViewChinese;
    private DataBaseViewModel wordViewModel;
    private Button buttonSubmit;

    public AddFragment() {
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
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = requireActivity();
        textViewChinese = activity.findViewById(R.id.editTextChinese);
        textViewEnglish = activity.findViewById(R.id.editTextEnglish);
        buttonSubmit = activity.findViewById(R.id.submit);
        wordViewModel = new ViewModelProvider(this,new SavedStateViewModelFactory(requireActivity().getApplication(),this))
                .get(DataBaseViewModel.class);
        buttonSubmit.setEnabled(false);
        textViewEnglish.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textViewEnglish,0);

        textViewEnglish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonSubmit.setEnabled(!textViewEnglish.getText().toString().trim().isEmpty() &&
                        !textViewChinese.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textViewChinese.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonSubmit.setEnabled(!textViewEnglish.getText().toString().trim().isEmpty() &&
                        !textViewChinese.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word = new Word(textViewEnglish.getText().toString().trim(),textViewChinese.getText().toString().trim());
                wordViewModel.insertWords(word);
            }
        });
    }
}