package com.itsthenikolai.servicemonitor.ui;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

    private enum serviceState
    {
        UNRUN, FAILED, RUN, RUNNING
    }

    private serviceState currentState = serviceState.UNRUN;

    private void updateState(serviceState newState)
    {
        if(newState == serviceState.UNRUN)
        {
            binding.txtStatus.setText("?");
            binding.txtStatus.setTextColor(Color.YELLOW);
        }
        else if(newState == serviceState.FAILED)
        {
            binding.txtStatus.setText("!");
            binding.txtStatus.setTextColor(Color.RED);
        }
        else if(newState == serviceState.RUNNING)
        {
            binding.txtStatus.setText("...");
            binding.txtStatus.setTextColor(Color.BLUE);
        }
        else if(newState == serviceState.RUN)
        {
            binding.txtStatus.setText("GOOD");
            binding.txtStatus.setTextColor(Color.GREEN);
        }

        currentState = newState;
    }

    private void updateState(int code)
    {
        if(code >= 200 && code < 300)
        {
            updateState(serviceState.RUN);
        }
        else
        {
            updateState(serviceState.FAILED);
        }
        binding.txtStatus.setText(Integer.toString(code));
    }

    public ServiceTab(Device deviceToAttach, Service serviceToAttach)
    {
        attachedService = serviceToAttach;
        attachedDevice = deviceToAttach;
    }

    public void run()
    {
        Handler mainHandler = new Handler(getMainLooper());
        updateState(serviceState.RUNNING);
        OkHttpClient client = new OkHttpClient();
        String url = attachedDevice.ip+":"+attachedService.port+attachedService.endpoint;
        if(!(url.startsWith("http://")||url.startsWith("https://"))) url = "http://" + url;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("Request","Request failed");

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        updateState(serviceState.FAILED);
                    }
                };
                //mainHandler.post(r);
                getActivity().runOnUiThread(r);

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("Request","Request succeeded - "+response.code());
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        updateState(response.code());
                    }
                };
                //mainHandler.post(r);
                getActivity().runOnUiThread(r);


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

        updateState(serviceState.UNRUN);

        binding.txtServiceName.setText(attachedService.name);
        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
            }
        });
    }
}
