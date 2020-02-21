package com.fq.flutter_0220_install;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerSDCardListener();

    }

    static  String CHANNEL_INSTALLED ="gameInstalledChannel";


    @Override
    public void configureFlutterEngine(FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL_INSTALLED)
                .setMethodCallHandler(
                        (call, result) -> {
                            String uriSchema;
                            switch (call.method) {
                                case "1":
                                    uriSchema = call.argument("uri").toString();
                                    this.checkAvailability( uriSchema, result );
                                    break;
                                case "installedApp":
                                    result.success(getInstalledApps());
                                    break;
                                default:
                                    result.notImplemented();
                            }
                        }
                );
    }

    private void checkAvailability(String uri, MethodChannel.Result result) {
        PackageInfo info = getAppPackageInfo(uri);

        if(info != null) {
            result.success(this.convertPackageInfoToJson(info));
            return;
        }
        result.error("", "App not found " + uri, null);
    }

    private List<Map<String, Object>> getInstalledApps() {
        PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> apps = packageManager.getInstalledPackages(0);
        List<Map<String, Object>> installedApps = new ArrayList<>(apps.size());
        int systemAppMask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;

        for (PackageInfo pInfo : apps) {
            if ((pInfo.applicationInfo.flags & systemAppMask) != 0) {
                continue;
            }

            Map<String, Object> map = this.convertPackageInfoToJson(pInfo);
            installedApps.add(map);
        }

        return installedApps;
    }

    private PackageInfo getAppPackageInfo(String uri) {
        Context ctx = this.getApplicationContext();
        final PackageManager pm = ctx.getPackageManager();

        try {
            return pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
        }
        catch(PackageManager.NameNotFoundException e) {

        }

        return null;
    }

    private Map<String, Object> convertPackageInfoToJson(PackageInfo info) {
        Map<String, Object> map = new HashMap<>();
        map.put("app_name", info.applicationInfo.loadLabel(this.getPackageManager()).toString());
        map.put("package_name", info.packageName);
        map.put("version_code", String.valueOf(info.versionCode));
        map.put("version_name", info.versionName);
        return map;
    }


//2.代码实现添加

    private final BroadcastReceiver apkInstallListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getDataString();

            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                System.out.println(packageName + "有应用被添加");
                Toast.makeText(context, "有应用被添加", Toast.LENGTH_LONG).show();
            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
                System.out.println(packageName + "应用被删除");
                Toast.makeText(context, "有应用被删除", Toast.LENGTH_LONG).show();
            } else if (Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())) {
                Toast.makeText(context, "有应用被改变", Toast.LENGTH_LONG).show();
            } else if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
                System.out.println(packageName + "应用被替换");
                Toast.makeText(context, "有应用被替换", Toast.LENGTH_LONG).show();
            } else if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())) {
                Toast.makeText(context, packageName + "有应用被重启", Toast.LENGTH_LONG).show();
            } else if (Intent.ACTION_PACKAGE_INSTALL.equals(intent.getAction())) {
                Toast.makeText(context, packageName + "有应用被安装", Toast.LENGTH_LONG).show();
            }

        }
    };

    // 注册监听
    private void registerSDCardListener() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        registerReceiver(apkInstallListener, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(apkInstallListener);
    }
}
