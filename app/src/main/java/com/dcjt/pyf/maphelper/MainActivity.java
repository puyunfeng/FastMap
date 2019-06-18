package com.dcjt.pyf.maphelper;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.amap.api.services.core.LatLonPoint;
import com.dcjt.pyf.fastmap.map.MapContainer;
import com.dcjt.pyf.fastmap.map.MyMapView;


public class MainActivity extends AppCompatActivity {

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
       // LatLng latLng = new LatLng(30.6662659463, 104.1723632813);

        mapView.initMap(savedInstanceState, getLifecycle())
                .openLocation(true)
                .moveCamera()
        ;
//                .moveCamera(latLng)
//                .addMarkersToMap(latLng);

        mapView.setOperatePosListener(new MyMapView.OperatePosListener() {
            @Override
            public void getAddress(String addressName, LatLonPoint point) {

            }
        });
    }

    public void openPos(View view) {
       if(!isShow){
           mapView.operatePos(this,true);
       }else{
           mapView.operatePos(this,false);
       }
        isShow=!isShow;
    }

    public void openActivity(View view) {
        mapView.moveCamera();
    }
}
