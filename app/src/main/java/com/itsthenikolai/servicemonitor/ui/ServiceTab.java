package com.itsthenikolai.servicemonitor.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itsthenikolai.servicemonitor.Device;
import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.R;
import com.itsthenikolai.servicemonitor.Service;
import com.itsthenikolai.servicemonitor.databinding.FragmentDeviceBinding;
import com.itsthenikolai.servicemonitor.databinding.ServiceTabBinding;
import com.itsthenikolai.servicemonitor.ui.device.DeviceViewModel;

import java.io.IOException;

import okhttp3.*;
public class ServiceTab extends Fragment {

    ServiceTabBinding binding;
    private Service attachedService;
    private Device attachedDevice;

    public ServiceTab(Device deviceToAttach, Service serviceToAttach)
    {
        attachedService = serviceToAttach;
        attachedDevice = deviceToAttach;
    }

    public void run()
    {
        OkHttpClient client = new OkHttpClient();
        String url = attachedDevice.ip+":"+attachedService.port+attachedService.endpoint;
        if(!(url.startsWith("http://")||url.startsWith("https://"))) url = "http://" + url;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("Request","Request failed");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("Request","Request succeeded - "+response.code());
            }
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ServiceTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        MainActivity act = (MainActivity) getActivity();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.txtServiceName.setText(attachedService.name);
        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
            }
        });
    }
}
