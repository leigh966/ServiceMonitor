package com.itsthenikolai.servicemonitor.ui.add_service;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.db.Service;
import com.itsthenikolai.servicemonitor.db.ServiceDao;
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

    private Boolean isEmpty(EditText et)
    {
        return et.getText().toString().isEmpty();
    }


    private int getNumber(EditText et)
    {
        return Integer.parseInt(et.getText().toString());
    }

    Boolean isGoodPortNumber(int portNumber)
    {
        return portNumber >= 0 && portNumber <= 65535;
    }

    private void validate()
    {
        Boolean somethingEmpty = isEmpty(binding.txtName) || isEmpty(binding.txtEndpoint);

        Boolean portNumberValid = !isEmpty(binding.txtPort) && isGoodPortNumber(getNumber(binding.txtPort));
        binding.btnSave.setEnabled(!somethingEmpty && portNumberValid);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnSave.setEnabled(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validate();
            }
        };

        binding.txtEndpoint.addTextChangedListener(watcher);
        binding.txtName.addTextChangedListener(watcher);
        binding.txtPort.addTextChangedListener(watcher);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}