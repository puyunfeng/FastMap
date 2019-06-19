package com.dcjt.pyf.fastmap.map;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.dcjt.pyf.fastmap.R;


public class MyMapView extends MapView implements LifecycleObserver, GeocodeSearch.OnGeocodeSearchListener, LocationSource, AMapLocationListener {
    private Boolean MyLocationButtonEnabled = false;
    private Boolean ZoomGesturesEnabled = true;
    private static Boolean IsDebug = true;
    private static String TAG = "MyMapView";
    private MarkerOptions markerOptions;
    private Marker mPositionMark;
    private OnLocationChangedListener onLocationChangedListener;
    private AMapLocationClient mlocationClient;
    private Context context;
    private float zoom = 0;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    IPOSTION ipostion = null;
    private AMapLocation aMapLocation;

    interface IPOSTION {
        void getPostion(LatLng latlng);
    }

    public MyMapView(Context context) {
        super(context);
        this.context = context;
    }

    public MyMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    public MyMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = context;
    }

    public MyMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
        this.context = context;
    }

    public MyMapView initMap(Bundle savedInstanceState, Lifecycle lifecycle) {
        lifecycle.addObserver(this);
        onCreate(savedInstanceState);
        final AMap aMap = getMap();
        aMap.setMapLanguage(AMap.CHINESE);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(MyLocationButtonEnabled);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(ZoomGesturesEnabled);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                DBLog.d("地图加载完成！");
            }
        });
        return this;
    }

    public MyMapView openLocation(Boolean open) {
        openLocation(open, 16f);
        return this;
    }

    public MyMapView openLocation(Boolean open, float zoom) {
        this.zoom = zoom;
        openLocation(open, R.drawable.location_marker);
        return this;
    }

    public void openLocation(Boolean open, int resourse) {
        if (open) {
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
            myLocationStyle.strokeColor(STROKE_COLOR);
            //自定义精度范围的圆形边框宽度
            myLocationStyle.strokeWidth(5);
            // 设置圆形的填充颜色
            myLocationStyle.radiusFillColor(FILL_COLOR);
            myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
            getMap().setMyLocationStyle(myLocationStyle);
            getMap().setLocationSource(this);// 设置定位监听
            getMap().getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            getMap().setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        }
    }

    public MyMapView setMyLocationButtonEnabled(Boolean isEnabled) {
        MyLocationButtonEnabled = isEnabled;
        return this;
    }

    public MyMapView setZoomGesturesEnabled(Boolean isEnabled) {
        ZoomGesturesEnabled = isEnabled;
        return this;
    }

    public MyMapView moveCamera() {
        if (aMapLocation == null) {
            ipostion = new IPOSTION() {
                @Override
                public void getPostion(LatLng latlng) {
                    if (zoom == 0)
                        moveCamera(latlng);
                    else
                        moveCamera(latlng, zoom);
                }
            };
        } else {
            moveCamera(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        }
        return this;
    }

    public MyMapView moveCamera(LatLng latlng) {
        return moveCamera(latlng, 16f);
    }

    public MyMapView moveCamera(LatLng latlng, float zoom) {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
        return this;
    }

    public MyMapView clear() {
        getMap().clear();
        return this;
    }

    /**
     * 在地图上添加marker
     */
    public MyMapView addMarkersToMap(LatLng latlng, AMap.OnMarkerClickListener listener) {
        getMap().setOnMarkerClickListener(listener);
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromResource(R.drawable.map_pin))
                .position(latlng)
                .draggable(true);
        getMap().addMarker(markerOption);
        return this;
    }

    public MyMapView openNav2(LatLng latLng) {
        if (latLng != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.parse("androidamap://navi?sourceApplication=appname&poiname=fangheng&lat="+latLng.latitude+"&lon="+latLng.longitude+"&dev=1&style=2");
            intent.setData(uri);
            context.startActivity(intent);
        }
        return this;
    }

    /**
     * 在地图上添加marker
     */
    public MyMapView addMarkersToMap(LatLng latlng) {
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromResource(R.drawable.map_pin))
                .position(latlng)
                .draggable(true);
        getMap().addMarker(markerOption);
        return this;
    }

    public void go2Location() {

    }

    public MyMapView setOperatePosListener(OperatePosListener operatePosListener) {
        if (operatePosListener != null) {
            IOperatePosListener = operatePosListener;
        }
        return this;
    }

    OperatePosListener IOperatePosListener = null;

    public interface OperatePosListener {
        void getAddress(String addressName, LatLonPoint point);
    }

    /**
     * 开启手动定位
     *
     * @return
     */
    public MyMapView operatePos(Context context, Boolean open) {
        if (open) {
            final GeocodeSearch geocoderSearch = new GeocodeSearch(context);
            geocoderSearch.setOnGeocodeSearchListener(this);
            markerOptions = new MarkerOptions();
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.position(new LatLng(0, 0));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sc_center_marker_icon)));
            getMap().setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {

                    LatLng mLatlng = cameraPosition.target;
                    if (mLatlng != null) {
                        DBLog.d("当前的维度坐标=" + (mLatlng.latitude));
                        DBLog.d("当前的精度坐标=" + (mLatlng.longitude));
                        double[] doubles = com.dcjt.pyf.maphelper.map.CoordinateTransformUtil.gcj02towgs84(mLatlng.longitude, mLatlng.latitude);
                        DBLog.d("转换后的坐标为===" + "(" + doubles[1] + "," + doubles[0] + ")");
                        LatLonPoint point = new LatLonPoint(mLatlng.latitude, mLatlng.longitude);
                        RegeocodeQuery query = new RegeocodeQuery(point, 1,
                                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
                        if (mPositionMark != null) {
//                        mPositionMark.setPositionByPixels(MyMapView.this.getWidth() / 2, MyMapView.this.getHeight() / 2);
//                        jumpPoint(mPositionMark);
//                        mPositionMark.setPositionByPixels(MyMapView.this.getWidth() / 2, MyMapView.this.getHeight() / 2);
                        }
                    }

                }
            });
            mPositionMark = getMap().addMarker(markerOptions);
            mPositionMark.showInfoWindow();//主动显示indowindow
            mPositionMark.setPositionByPixels(this.getWidth() / 2, this.getHeight() / 2);
        } else {
            mPositionMark.remove();
        }

        return this;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress();
                if (IOperatePosListener != null) {
                    IOperatePosListener.getAddress(addressName, result.getRegeocodeQuery().getPoint());
                }
                DBLog.d(addressName + "附近");
            } else {
                DBLog.d("暂时没有获取到相应的结果！");
            }
        } else {
            DBLog.d(rCode + "");
        }
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = getMap().getProjection();

        Point markerPoint = new Point(MyMapView.this.getWidth() / 2, MyMapView.this.getWidth() / 2);
        markerPoint.offset(0, -100);
        //获取当前屏幕坐标的地理坐标
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        Point markerPoint2 = new Point(MyMapView.this.getWidth() / 2, MyMapView.this.getWidth() / 2);
        final LatLng endLatLng = proj.fromScreenLocation(markerPoint2);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * endLatLng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * endLatLng.latitude + (1 - t)
                        * startLatLng.latitude;
                // marker.setPosition(new LatLng(lat, lng));
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void setMapGesturesEnabled(boolean enable) {
        AMap map = getMap();
        if (map == null) return;
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setScrollGesturesEnabled(enable);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void create() {
        DBLog.d("create: ");

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void start() {
        DBLog.d("start: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        DBLog.d("resume: ");
        onResume();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pasue() {
        DBLog.d("pasue: ");
        onPause();
        deactivate();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {
        DBLog.d("stop: ");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destory() {
        DBLog.d("destory: ");
        onDestroy();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // mLocationOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        onLocationChangedListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (onLocationChangedListener != null && aMapLocation != null) {
            StringBuffer sb = new StringBuffer();
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                addDes(aMapLocation, sb);
                this.aMapLocation = aMapLocation;
                ipostion.getPostion(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void addDes(AMapLocation aMapLocation, StringBuffer sb) {
        sb.append("定位成功" + "\n");
        sb.append("定位类型: " + aMapLocation.getLocationType() + "\n");
        sb.append("经    度    : " + aMapLocation.getLongitude() + "\n");
        sb.append("纬    度    : " + aMapLocation.getLatitude() + "\n");
        sb.append("精    度    : " + aMapLocation.getAccuracy() + "米" + "\n");
        sb.append("提供者    : " + aMapLocation.getProvider() + "\n");

        sb.append("速    度    : " + aMapLocation.getSpeed() + "米/秒" + "\n");
        sb.append("角    度    : " + aMapLocation.getBearing() + "\n");
        // 获取当前提供定位服务的卫星个数
        sb.append("星    数    : " + aMapLocation.getSatellites() + "\n");
        sb.append("国    家    : " + aMapLocation.getCountry() + "\n");
        sb.append("省            : " + aMapLocation.getProvince() + "\n");
        sb.append("市            : " + aMapLocation.getCity() + "\n");
        sb.append("城市编码 : " + aMapLocation.getCityCode() + "\n");
        sb.append("区            : " + aMapLocation.getDistrict() + "\n");
        sb.append("区域 码   : " + aMapLocation.getAdCode() + "\n");
        sb.append("地    址    : " + aMapLocation.getAddress() + "\n");
        sb.append("兴趣点    : " + aMapLocation.getPoiName() + "\n");
        //定位完成的时间
        sb.append("定位时间: " + com.dcjt.pyf.maphelper.map.Utils.formatUTC(aMapLocation.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        onLocationChangedListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
        DBLog.d("获取到定位信息=>" + sb.toString());
    }

    static class DBLog {
        public static void d(String msg) {
            if (IsDebug) {
                Log.d(TAG, msg);
            }
        }
    }

}
