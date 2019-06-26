package com.dcjt.pyf.fastmap.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.amap.api.maps2d.model.LatLng;
import com.dcjt.pyf.fastmap.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2019/6/20.
 * desc:
 */
public class NavigationUtil {


    private static String BAIDU_PACKAGE_NAME = "com.baidu.BaiduMap";
    private static String GAODE_PACKAGE_NAME = "com.autonavi.minimap";

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    private static boolean isInstalled(Context mContext, String packageName) {
        PackageManager manager = mContext.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }


    private static List<NavigationTips> getInstalledMapApp(Context context) {
        ArrayList<NavigationTips> navigationTips = new ArrayList<>();

        if (isInstalled(context, BAIDU_PACKAGE_NAME)) {
            navigationTips.add(new NavigationTips(0, "百度导航"));
        }

        if (isInstalled(context, GAODE_PACKAGE_NAME)) {
            navigationTips.add(new NavigationTips(1, "高德导航"));
        }

        return navigationTips;
    }


    /**
     * 跳转高德地图
     */
    private static void goToGaodeMap(Activity activity, LatLng point, String mAddress) {
        StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=").append("amap");
        stringBuffer.append("&lat=").append(point.latitude)
                .append("&lon=").append(point.longitude).append("&keywords=" + mAddress)
                .append("&dev=").append(0)
                .append("&style=").append(2);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        activity.startActivity(intent);
    }


    /**
     * 跳转百度地图
     */
    private static void goToBaiduMap(Activity activity, LatLng point, String mAddress) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction?destination=latlng:"
                + point.latitude + ","
                + point.longitude + "|name:" + mAddress + // 终点
                "&mode=driving" + // 导航路线方式
                "&src=" + activity.getPackageName()));
        activity.startActivity(intent); // 启动调用
    }


    public static void startNavigation(Activity activity, LatLng point, String mAddress) {
        List<NavigationTips> mapApps = getInstalledMapApp(activity);
        if (mapApps.size() == 0) {
            new AlertDialog.Builder(activity).setMessage(R.string.tips)
                    .setMessage(R.string.navigationTips)
                    .show();
        } else if (mapApps.size() == 1) {
            NavigationTips navigationTips = mapApps.get(0);
            startNavigation(navigationTips.getType(), activity, point, mAddress);
        } else if (mapApps.size() == 2) {
//            new AlertDialog.Builder(activity).setMessage("请选择导航软件")
//                    .setItems(R.array.chooseMaps, (dialog, which) -> {
//                        startNavigation(which, activity, point, mAddress);
//                        dialog.dismiss();
//                    })
//                    .show();


//
//            new AlertDialog.Builder(activity).setMessage("请选择导航软件")
//                    .setItems(R.array.chooseMaps,new DialogInterface.OnClickListener)
//                    .show();
        }

    }


    private static void startNavigation(int type, Activity activity, LatLng point, String mAddress) {
        if (type == 0) {
            goToBaiduMap(activity, point, mAddress);
        } else if (type == 1) {
            goToGaodeMap(activity, point, mAddress);
        }
    }


}
