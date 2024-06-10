package com.itsthenikolai.servicemonitor.ui;

import static android.os.Looper.getMainLooper;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itsthenikolai.servicemonitor.db.Device;
import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.db.Service;
import com.itsthenikolai.servicemonitor.ServiceState;
import com.itsthenikolai.servicemonitor.databinding.ServiceTabBinding;

import java.io.IOException;

import okhttp3.*;
public class ServiceTab extends Fragment {

    ServiceTabBinding binding;
    private Service attachedService;
    private Device attachedDevice;


    private ServiceState currentState = ServiceState.UNRUN;

    private void updateState(ServiceState newState)
    {
        if(newState == ServiceState.UNRUN)
        {
            binding.txtStatus.setText("?");
            binding.txtStatus.setTextColor(Color.YELLOW);
        }
        else if(newState == ServiceState.FAILED)
        {
            binding.txtStatus.setText("!");
            binding.txtStatus.setTextColor(Color.RED);
        }
        else if(newState == ServiceState.RUNNING)
        {
            binding.txtStatus.setText("...");
            binding.txtStatus.setTextColor(Color.BLUE);
        }
        else if(newState == ServiceState.RUN)
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
            updateState(ServiceState.RUN);
        }
        else
        {
            updateState(ServiceState.FAILED);
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
        updateState(ServiceState.RUNNING);
        OkHttpClient client = new OkHttpClient();
        String endpoint = attachedService.endpoint.startsWith("/") ? attachedService.endpoint : "/"+attachedService.endpoint;
        String url = attachedDevice.ip+":"+attachedService.port+endpoint;
        if(!(url.startsWith("http://")||url.startsWith("https://"))) url = "http://" + url;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("Request","Request failed");

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        updateState(ServiceState.FAILED);
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

        updateState(ServiceState.UNRUN);

        binding.txtServiceName.setText(attachedService.name);
        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
            }
        });
    }
}
