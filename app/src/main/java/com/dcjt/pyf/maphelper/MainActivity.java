package com.dcjt.pyf.maphelper;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.dcjt.pyf.fastmap.map.MapContainer;
import com.dcjt.pyf.fastmap.map.MyMapView;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    float zoom=9f;
    private MyMapView mapView;
    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MyMapView) findViewById(R.id.map_view);
        NestedScrollView nsScrollview = (NestedScrollView) findViewById(R.id.ns_scrollview);
        MapContainer mapContainer = (MapContainer) findViewById(R.id.map_Container);
        mapContainer.setScrollView(nsScrollview);
        LatLng latLng = new LatLng(30.6662659463, 104.1723632813);

        mapView.initMap(savedInstanceState, getLifecycle())
                .setMarkersdrawable(R.drawable.position)
                .setLocationMarker(R.drawable.location)
                .addMarkersToMap(latLng)
                .openLocation(true)
                .moveCamera(latLng,1)

        ;


//        mapView.initMap(savedInstanceState, getLifecycle())
//                .openLocation(true)
//                .setOperatePosListener(new MyMapView.OperatePosListener() {
//                    @Override
//                    public void getAddress(String addressName, LatLonPoint point) {
//                        Log.d("getAddress", addressName);
//                    }
//                })
//                .operatePos(true)
//                .moveCamera();


//                .moveCamera(latLng)
//                .addMarkersToMap(latLng);
//
//        mapView.setOperatePosListener(new MyMapView.OperatePosListener() {
//            @Override
//            public void getAddress(String getAddress, LatLonPoint point) {
//                Log.d("getAddress", getAddress);
//            }
//        });
//        mapView.operatePos(this, true);
    }

    public void openPos(View view) {
        LatLng latLng = new LatLng(30.6662659463, 104.1723632813);
        mapView.moveCamera(latLng,zoom++);
    }

    public void openActivity(View view) {
        mapView.moveCamera();
    }

    public void openNav(View view) {
        LatLng latLng = new LatLng(30.6662659463, 104.1723632813);
        mapView.openNav2(latLng);
    }

    public void openSelected(View view) {
        mapView.setOperatePosListener(new MyMapView.OperatePosListener() {

            @Override
            public void getAddress(String addressName, LatLonPoint point, RegeocodeAddress allBean) {
                Log.d("getAddress", "getAddress: "+allBean.getCityCode()+allBean.getCity());
            }
        });
        mapView.operatePos(true);
    }
}
