package com.itsthenikolai.servicemonitor.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.R;
import com.itsthenikolai.servicemonitor.databinding.FragmentDeviceBinding;
import com.itsthenikolai.servicemonitor.databinding.ServiceTabBinding;
import com.itsthenikolai.servicemonitor.ui.device.DeviceViewModel;

public class ServiceTab extends Fragment {

    ServiceTabBinding binding;

    public ServiceTab()
    {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ServiceTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        MainActivity act = (MainActivity) getActivity();

        return root;
    }
}
