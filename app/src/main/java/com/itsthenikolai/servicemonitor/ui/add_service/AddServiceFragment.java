package com.itsthenikolai.servicemonitor.ui.add_service;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itsthenikolai.servicemonitor.DeviceDao;
import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.Service;
import com.itsthenikolai.servicemonitor.ServiceDao;
import com.itsthenikolai.servicemonitor.databinding.FragmentAddServiceBinding;

public class AddServiceFragment extends Fragment {

    private FragmentAddServiceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddServiceViewModel addServiceViewModel =
                new ViewModelProvider(this).get(AddServiceViewModel.class);

        binding = FragmentAddServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.txt;
        //addServiceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        return root;
    }

    public void onSubmit()
    {
        String name = binding.txtName.getText().toString();
        String endpoint = binding.txtEndpoint.getText().toString();
        int port = Integer.parseInt(binding.txtPort.getText().toString());
        Service newService = new Service(
                name,
                endpoint,
                port,
                getArguments().getInt("device_id"));
        MainActivity act = (MainActivity) getActivity();
        ServiceDao dao = act.db.serviceDao();
        dao.insertAll(newService);
        Log.w("test", act.db.deviceDao().getDeviceWithServices().toString());
        Log.w("test", act.db.serviceDao().getAll().toString());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}