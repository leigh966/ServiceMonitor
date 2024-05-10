package com.itsthenikolai.servicemonitor;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.itsthenikolai.servicemonitor.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public AppDatabase db;

    Menu sidebar;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "service-db")
                    .allowMainThreadQueries()
                    .build();


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
    }

    public void navigateToDevice(String name){
        Bundle bundle = new Bundle();
        bundle.putString("device_name", name);
        navController.navigate(R.id.nav_device, bundle);
    }

    public void addNavOption(Device d)
    {
        sidebar.add(d.name);
        sidebar.getItem(sidebar.size()-1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                navigateToDevice(item.getTitle().toString());
                return true;
            }
        });
    }

    public void onAddDeviceClicked()
    {
        navController.navigate(R.id.nav_add_device);
    }

    public void initNavBar()
    {
        // Get the devices
        List<Device> devices = db.deviceDao().getAll();
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