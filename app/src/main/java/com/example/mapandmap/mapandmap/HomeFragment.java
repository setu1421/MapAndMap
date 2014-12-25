package com.example.mapandmap.mapandmap;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener{
    // ...
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;
    EditText et;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_map_activity, null, false);

        MapFragment mapFrag = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFrag.getMap();

        Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
                .title("Hamburg"));
        Marker kiel = map.addMarker(new MarkerOptions()
                .position(KIEL)
                .title("Kiel")
                .snippet("Kiel is cool")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_launcher)));
        //
        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

         et = (EditText) v.findViewById(R.id.editText1);
       //added the go button listener
       Button go = (Button) v.findViewById(R.id.go);
        go.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go:
                try {
                    geoLocate(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        map.moveCamera(update);
    }

    public void geoLocate(View v) throws IOException {
        hideSoftKeyboard(v);


        String location = et.getText().toString();

        Geocoder gc = new Geocoder(getActivity());
        List<Address> list = gc.getFromLocationName(location, 1);
        Address add = list.get(0);
        String locality = add.getLocality();
        Toast.makeText(getActivity(), locality, Toast.LENGTH_LONG).show();

        double lat = add.getLatitude();
        double lng = add.getLongitude();

        gotoLocation(lat, lng, 15);

    }
  // to hide keyboard must use getActivity() and Context

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}