package com.itsthenikolai.servicemonitor;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.itsthenikolai.servicemonitor.db.Device.DeviceWithServices;
import com.itsthenikolai.servicemonitor.db.Logs.StatusLog;

import java.util.List;

public class RunServicesService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void createNotification()
    {
        String group = "com.android.itsthenikolai.servicemonitor.DEVICE_NOTIF";
        List<DeviceWithServices> dws =  DatabaseAccessor.db.deviceDao().getDeviceWithServices();
        for(DeviceWithServices rec : dws)
        {
            addNotifications(rec, group);
        }
    }

    private void addNotifications(DeviceWithServices dws, String group)
    {
        for(com.itsthenikolai.servicemonitor.db.Service.Service s : dws.services)
        {
            List<StatusLog> logs = DatabaseAccessor.db.statusLogDao().getByServiceId(s.uid);
            StatusLog log;
            if(logs.isEmpty()) {
                log=new StatusLog(ServiceState.UNRUN, s.uid);
            }else {
                log = logs.get(0);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "services")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Service Monitor")
                    .setContentText(s.name + " - " + log.getDescriptor())
                    .setGroup(group)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notManager.notify(0, builder.build());
        }
    }

/*
    private String buildContent()
    {
        String output = "";
        // get all the services
        List<com.itsthenikolai.servicemonitor.db.Service.Service> services = DatabaseAccessor.db.serviceDao().getAll();

        // for each service
        for(com.itsthenikolai.servicemonitor.db.Service.Service s : services)
        {
            // get latest log for service
            List<StatusLog> logs = DatabaseAccessor.db.statusLogDao().getByServiceId(s.uid);
            StatusLog log;
            if(logs.isEmpty()) {
            log=new StatusLog(ServiceState.UNRUN, s.uid);
            }else {
                log = logs.get(0);
            }

            // output += `${service_name} - ${status_descriptor}\n`
            output += s.name + " - " + log.getDescriptor() + "\n";
        }

        return output;
    }
*/
/*
    private void createNotification()
    {
        String body = buildContent();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "services")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Service Monitor")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notManager.notify(0, builder.build());
    }

*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        createNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

}
