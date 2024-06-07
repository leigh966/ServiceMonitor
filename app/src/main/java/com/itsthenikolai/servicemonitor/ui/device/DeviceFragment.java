package com.itsthenikolai.servicemonitor.ui.device;

import static android.view.View.*;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.R;
import com.itsthenikolai.servicemonitor.Service;
import com.itsthenikolai.servicemonitor.ServiceDao;
import com.itsthenikolai.servicemonitor.databinding.FragmentDeviceBinding;

import java.util.List;
import com.itsthenikolai.servicemonitor.ui.ServiceTab;

public class DeviceFragment extends Fragment {

    private FragmentDeviceBinding binding;
    ServiceDao serviceDao;
    public List<Service> relatedServices;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DeviceViewModel deviceViewModel =
                new ViewModelProvider(this).get(DeviceViewModel.class);

        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDeviceName;
        textView.setText(getArguments().getString("device_name"));
        deviceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        MainActivity act = (MainActivity) getActivity();
        act.setButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putInt("device_id", getArguments().getInt("device_id"));
                act.navController.navigate(R.id.nav_add_service, b);
            }
        });

        return root;
    }

    private void createServiceTabs()
    {

        for(Service s : relatedServices)
        {
            // add service tab
            binding.serviceTabsLayout.addView(new ServiceTab(getContext()));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        // show button
        MainActivity act = (MainActivity) getActivity();
        act.setShowButton(true);

        serviceDao = act.db.serviceDao();

        relatedServices = serviceDao.getAllForDevice(getArguments().getInt("device_id"));

        createServiceTabs();
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