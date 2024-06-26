package com.itsthenikolai.servicemonitor.ui.add_device;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itsthenikolai.servicemonitor.DatabaseAccessor;
import com.itsthenikolai.servicemonitor.db.Device.Device;
import com.itsthenikolai.servicemonitor.db.Device.DeviceDao;
import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.databinding.FragmentAddDeviceBinding;

import java.util.List;

public class AddDeviceFragment extends Fragment{

    private FragmentAddDeviceBinding binding;
    DeviceDao deviceDao;

    private void onSubmit()
    {
        Device newDevice = new Device(binding.nameInput.getText().toString(), binding.ipInput.getText().toString());
        // Add new device to database
        deviceDao.insertAll(newDevice);

        // Update nav bar
        MainActivity act = (MainActivity) getActivity();
        act.refreshNavBar();

        act.navigateToLatestDevice();
    }

    private void startListeningForSubmit()
    {
        Button submitButton = binding.btnSubmit;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddDeviceViewModel addDeviceViewModel =
                new ViewModelProvider(this).get(AddDeviceViewModel.class);

        binding = FragmentAddDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        addDeviceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        MainActivity act = (MainActivity) getActivity();
        deviceDao = DatabaseAccessor.db.deviceDao();
        startListeningForSubmit();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}