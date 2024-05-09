package com.itsthenikolai.servicemonitor.ui.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.databinding.FragmentDeviceBinding;

public class DeviceFragment extends Fragment {

    private FragmentDeviceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DeviceViewModel deviceViewModel =
                new ViewModelProvider(this).get(DeviceViewModel.class);

        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        textView.setText(getArguments().getString("device_name"));
        deviceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        // show button
        MainActivity act = (MainActivity) getActivity();
        act.setShowButton(true);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

    @Override
    public void onStop()
    {
        // hide button
        MainActivity act = (MainActivity) getActivity();
        act.setShowButton(false);
        super.onStop();
    }




}