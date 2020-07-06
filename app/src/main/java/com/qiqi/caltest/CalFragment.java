package com.qiqi.caltest;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiqi.caltest.databinding.FragmentCalBinding;
import com.qiqi.caltest.databinding.FragmentTitleBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalFragment extends Fragment {
    public CalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final MyViewModel myViewModel;
        PlaySound.initSoundPool(getContext());
        myViewModel = new ViewModelProvider(getActivity(),new SavedStateViewModelFactory(getActivity().getApplication(),this)).get(MyViewModel.class);
        myViewModel.generator();
        myViewModel.getCurScore().setValue(0);
        final FragmentCalBinding binding;
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cal,container,false);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(getActivity());
        final StringBuilder answer = new StringBuilder();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button0:
                        answer.append("0");
                        break;
                    case R.id.button1:
                        answer.append("1");
                        break;
                    case R.id.button2:
                        answer.append("2");
                        break;
                    case R.id.button3:
                        answer.append("3");
                        break;
                    case R.id.button4:
                        answer.append("4");
                        break;
                    case R.id.button5:
                        answer.append("5");
                        break;
                    case R.id.button6:
                        answer.append("6");
                        break;
                    case R.id.button7:
                        answer.append("7");
                        break;
                    case R.id.button8:
                        answer.append("8");
                        break;
                    case R.id.button9:
                        answer.append("9");
                        break;
                    case R.id.btn_clear:
                        answer.setLength(0);
                        break;
                }
                if(answer.length() == 0) {
                    binding.inputIndicator.setText(R.string.input_indicator);
                } else {
                    binding.inputIndicator.setText(answer.toString());
                }
            }
        };

        binding.button0.setOnClickListener(listener);
        binding.button1.setOnClickListener(listener);
        binding.button2.setOnClickListener(listener);
        binding.button3.setOnClickListener(listener);
        binding.button4.setOnClickListener(listener);
        binding.button5.setOnClickListener(listener);
        binding.button6.setOnClickListener(listener);
        binding.button7.setOnClickListener(listener);
        binding.button8.setOnClickListener(listener);
        binding.button9.setOnClickListener(listener);
        binding.btnClear.setOnClickListener(listener);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.length() == 0) {
                    return;
                }
                if (Integer.valueOf(answer.toString()).intValue() == myViewModel.getAnswer().getValue().intValue()) {
                    PlaySound.playSound(getContext(),1,0);
                    myViewModel.answerCorrect();
                    answer.setLength(0);
                    binding.inputIndicator.setText(R.string.input_indicator);
                } else {
                    PlaySound.playSound(getContext(),2,0);
                    NavController controller = Navigation.findNavController(v);
                    if(myViewModel.new_record) {
                        controller.navigate(R.id.action_calFragment_to_winFragment);
                        myViewModel.new_record = false;
                        myViewModel.save();
                    } else {
                        controller.navigate(R.id.action_calFragment_to_loseFragment);
                    }
                }
            }
        });

        return binding.getRoot();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_cal, container, false);
    }
}
