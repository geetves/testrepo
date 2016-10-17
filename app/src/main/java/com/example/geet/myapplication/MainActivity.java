package com.example.geet.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    Switch switchWifi;
    ProgressBar progressSdcard;
    WifiManager wifiManager;
    LinearLayout layoutSdcard;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        switchWifi= (Switch) findViewById(R.id.wifi_indicator);
        layoutSdcard=(LinearLayout)findViewById(R.id.layout_sdcard);
        File[] fs = getExternalFilesDirs(null);
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    wifiManager.setWifiEnabled(true);
                }else{
                    wifiManager.setWifiEnabled(false);
                }
                int i=1;
            }
        });
        List<StorageInfo> info = new ArrayList<>(getStorageList());
        for (int i = 0; i < info.size(); i++) {
            /*Log.e("Path", "" + info.get(i).path);
            Log.e("Total size", "  " + totasize(new File(info.get(i).path)));
            Log.e("Free size", "  " + freesize(new File(info.get(i).path)));*/
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                long total = totasize(new File(info.get(i).path).getAbsolutePath());
                long free =  freesize(new File(info.get(i).path).getAbsolutePath());
                set_progressbar(total, free);
            }else{
                long total = totasize(fs[i].getAbsolutePath());
                long free =  freesize(fs[i].getAbsolutePath());
                set_progressbar(total, free);
            }

        }

    }

    private void set_progressbar(long total1, long free1) {
        String totals=formatSize(total1);
        String frees=formatSize(free1);
        TextView txtSpace=new TextView(this);
        txtSpace.setText(frees+"/"+totals);
        if(totals.contains("GB")){
            totals=totals.replace("GB","");
        } else if(totals.contains("MB")){
            totals=totals.replace("MB","");
        }else{
            totals=totals.replace("KB","");
        }
        if(frees.contains("GB")){
            frees=frees.replace("GB","");
        } else if(frees.contains("MB")){
            frees=frees.replace("MB","");
        }else{
            frees=frees.replace("KB","");
        }

        //int total=Integer.parseInt(totals.replace(",",""));
        //int free=Integer.parseInt(frees.replace(",",""));
        ProgressBar progressSdcard=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        progressSdcard.setMax(100);

        int available = 0;
        try {
            available = (int) ((free1 * 100) / total1);
        } catch (Exception e) {
        }
        progressSdcard.setProgress(available);
        layoutSdcard.addView(progressSdcard);
        layoutSdcard.addView(txtSpace);
    }

    /*public static String freesize(File path) {
        //File externalStorageDir = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getAbsolutePath());
        //StatFs stat = new StatFs(externalStorageDir.getAbsolutePath());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
            // return availableBlocks;
        } else {
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
            //return availableBlocks;
        }
    }*/
    public static long freesize(String path) {
        //File externalStorageDir = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path);
        //StatFs stat = new StatFs(externalStorageDir.getAbsolutePath());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            //return formatSize(availableBlocks * blockSize);
             return availableBlocks*blockSize;
        } else {
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            //return formatSize(availableBlocks * blockSize);
            return availableBlocks * blockSize;
        }
    }
    /*public static String totasize(File path) {
        //File externalStorageDir = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(path.getAbsolutePath());
        //StatFs statFs = new StatFs(externalStorageDir.getAbsolutePath());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockSize = statFs.getBlockSize();
            long totalSize = statFs.getBlockCount() * blockSize;
            //return totalSize;
            return formatSize(totalSize);
        } else {
            long blockSize = statFs.getBlockSizeLong();
            long totalSize = statFs.getBlockCountLong() * blockSize;
            //return totalSize;
            return formatSize(totalSize);
        }
    }*/

    public static long totasize(String path) {
        //File externalStorageDir = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(path);
        //StatFs statFs = new StatFs(externalStorageDir.getAbsolutePath());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            long blockSize = statFs.getBlockSize();
            long totalSize = statFs.getBlockCount() * blockSize;
            return totalSize;
            //return formatSize(totalSize);
        } else {
            long blockSize = statFs.getBlockSizeLong();
            long totalSize = statFs.getBlockCountLong() * blockSize;
            return totalSize;
            //return formatSize(totalSize);
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "GB";
                    size /= 1024;
                }
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static List<StorageInfo> getStorageList() {

        List<StorageInfo> list = new ArrayList<StorageInfo>();
        String def_path = Environment.getExternalStorageDirectory().getPath();
        boolean def_path_internal = !Environment.isExternalStorageRemovable();
        String def_path_state = Environment.getExternalStorageState();
        boolean def_path_available = def_path_state.equals(Environment.MEDIA_MOUNTED)
                || def_path_state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean def_path_readonly = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        BufferedReader buf_reader = null;
        try {
            HashSet<String> paths = new HashSet<String>();
            buf_reader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            int cur_display_number = 1;
            Log.e("starageinfo", "/proc/mounts");
            while ((line = buf_reader.readLine()) != null) {
                Log.e("starageinfo", line);
                if (line.contains("vfat") || line.contains("/mnt")) {
                    StringTokenizer tokens = new StringTokenizer(line, " ");
                    String unused = tokens.nextToken(); //device
                    String mount_point = tokens.nextToken(); //mount point
                    if (paths.contains(mount_point)) {
                        continue;
                    }
                    unused = tokens.nextToken(); //file system
                    List<String> flags = Arrays.asList(tokens.nextToken().split(",")); //flags
                    boolean readonly = flags.contains("ro");

                    if (mount_point.equals(def_path)) {
                        paths.add(def_path);
                        list.add(0, new StorageInfo(def_path, def_path_internal, readonly, -1));
                    } else if (line.contains("/dev/block/vold")) {
                        if (!line.contains("/mnt/secure")
                                && !line.contains("/mnt/asec")
                                && !line.contains("/mnt/obb")
                                && !line.contains("/dev/mapper")
                                && !line.contains("tmpfs")) {
                            paths.add(mount_point);
                            list.add(new StorageInfo(mount_point, false, readonly, cur_display_number++));
                        }
                    }
                }
            }

            if (!paths.contains(def_path) && def_path_available) {
                list.add(0, new StorageInfo(def_path, def_path_internal, def_path_readonly, -1));
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (buf_reader != null) {
                try {
                    buf_reader.close();
                } catch (IOException ex) {
                }
            }
        }
        return list;
    }
}
