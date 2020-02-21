package com.fq.flutter_0220_install.receiver;

/**
 * @projectName: flutter_0220_install
 * @packageName: com.fq.flutter_0220_install.receiver
 * @author: catherine
 * @time: 2020-02-20
 * @description:
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by aiyang on 2018/3/8.
 * <p>
 * 监听应用安装
 */
public class PackageReceiver extends BroadcastReceiver {

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


}

