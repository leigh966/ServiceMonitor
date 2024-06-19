package com.itsthenikolai.servicemonitor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import com.itsthenikolai.servicemonitor.databinding.ActivityMainBinding;
import com.itsthenikolai.servicemonitor.db.Device.Device;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    Menu sidebar;
    public NavController navController;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "services";
            String description = "Notifies the state of all services";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("services", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                0);

        DatabaseAccessor.init(getApplicationContext());


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sidebar = binding.navView.getMenu();
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add_device, R.id.nav_device)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        setShowButton(false);

        initNavBar();
        createNotificationChannel();
        startService(new Intent(getBaseContext(), RunServicesService.class));
    }

    public void navigateToDevice(String name, int id){
        Bundle bundle = new Bundle();
        bundle.putString("device_name", name);
        bundle.putInt("device_id", id);
        navController.navigate(R.id.nav_device, bundle);
    }

    public void navigateToLatestDevice()
    {
        List<Device> devices = DatabaseAccessor.db.deviceDao().getAll();
        Device lastDevice = devices.get(devices.size()-1);
        navigateToDevice(lastDevice.name, lastDevice.uid);
    }

    public void addNavOption(Device d)
    {
        sidebar.add(d.name);
        sidebar.getItem(sidebar.size()-1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                navigateToDevice(d.name, d.uid);
                return true;
            }
        });
    }

    public void onAddDeviceClicked()
    {
        navController.navigate(R.id.nav_add_device);
    }

    private void clearNavBar()
    {
        // this is scary but will do the job
        while(sidebar.size()>1) {
            sidebar.removeItem(0);
        }
    }

    public void refreshNavBar()
    {
        clearNavBar();
        initNavBar();
    }

    public void initNavBar()
    {
        // Get the devices
        List<Device> devices = DatabaseAccessor.db.deviceDao().getAll();
        // Add the devices
        for (Device d : devices
             ) {
            addNavOption(d);
        }

        sidebar.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                onAddDeviceClicked();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setShowButton(Boolean shown)
    {
        if(shown)
        {
            binding.appBarMain.fab.show();
            return;
        }
        binding.appBarMain.fab.hide();
    }

    public void setButtonOnClick(View.OnClickListener listener)
    {
        binding.appBarMain.fab.setOnClickListener(listener);
    }

}