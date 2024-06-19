package com.itsthenikolai.servicemonitor.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.itsthenikolai.servicemonitor.DatabaseAccessor;
import com.itsthenikolai.servicemonitor.db.Device.Device;
import com.itsthenikolai.servicemonitor.MainActivity;
import com.itsthenikolai.servicemonitor.db.Service.Service;
import com.itsthenikolai.servicemonitor.ServiceState;
import com.itsthenikolai.servicemonitor.databinding.ServiceTabBinding;
import com.itsthenikolai.servicemonitor.db.Logs.StatusLog;
import com.itsthenikolai.servicemonitor.db.Logs.StatusLogDao;

import java.io.IOException;
import java.util.List;

import okhttp3.*;
public class ServiceTab extends Fragment {

    ServiceTabBinding binding;
    private Service attachedService;
    private Device attachedDevice;

    StatusLogDao sld;

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
        updateState(ServiceState.RUNNING);
        OkHttpClient client = new OkHttpClient();
        String endpoint = attachedService.endpoint.startsWith("/") ? attachedService.endpoint : "/"+attachedService.endpoint;
        String url = attachedDevice.ip+":"+attachedService.port+endpoint;
        if(!(url.startsWith("http://")||url.startsWith("https://"))) url = "http://" + url;
        Request request = new Request.Builder().url(url).build();



        client.newCall(request).enqueue(new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                sld.insertAll(new StatusLog(ServiceState.FAILED, attachedService.uid));

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
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
                sld.insertAll(new StatusLog(response.code(), attachedService.uid));


            }
        });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ServiceTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    private void loadState()
    {
        // fetch the logs
        List<StatusLog> logs = sld.getByServiceId(attachedService.uid);

        // if there are no logs, mark the tab as unrun
        if(logs.isEmpty()) {
            updateState(ServiceState.UNRUN);
        }
        // otherwise, use the state of the top log
        else
        {

            if(logs.get(0).code == null) {
                updateState(logs.get(0).status);
            }else {
                updateState(Integer.parseInt(logs.get(0).code));
            }
        }
    }


    void delete()
    {
        DatabaseAccessor.db.serviceDao().deleteById(attachedService.uid);
        MainActivity act = (MainActivity) getActivity();
        act.navigateToDevice(attachedDevice.name, attachedDevice.uid);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the status log dao
        MainActivity act = (MainActivity) getActivity();
        sld = DatabaseAccessor.db.statusLogDao();


        loadState();


        binding.txtServiceName.setText(attachedService.name);
        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });


    }
}
