package com.example.moivememoir.ui.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moivememoir.R;
import com.example.moivememoir.entities.Person;
import com.example.moivememoir.helpers.RestHelper;
import com.example.moivememoir.ui.activities.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap map;
    Geocoder coder;
    Person user;
    List<LatLng> cinemaCoordination;
    List<String> cinemaAddress;

    RestHelper restHelper = new RestHelper();

    public MapViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        coder = new Geocoder(getContext());
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();

        return view;
    }

    public LatLng getUserLocation() {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(user.getPersonAddress() + ", " + user.getPersonState(), 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng userLocation = getUserLocation();
        map = googleMap;
        map.addMarker(new MarkerOptions().position(userLocation));

        float zoomLevel = (float) 10.0;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
                zoomLevel));
        getCinemaLocations();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });


    }



    public void getCinemaLocations() {
        GetCinemaAddress getCinemaAddress = new GetCinemaAddress();
        getCinemaAddress.execute();

    }

    private class GetCinemaAddress extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {


            String result = restHelper.getCinemas();
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            cinemaAddress = new ArrayList<>();
            if (!result.equals("failed")) {

                JSONArray array = null;
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < array.length(); i++) {
                    //String[] colHEAD = new String[]{"NAME", "RELEASE YEAR", "RATING"};
                    JSONObject obj = null;
                    try {
                        Geocoder coder = new Geocoder(getContext());
                        List<Address> address;
                        LatLng p1 = null;
                        obj = array.getJSONObject(i);
                        String addressStr = obj.getString("cinemaName") + ", VIC" + obj.getString("cinemaPostcode");
                        cinemaAddress.add(addressStr);
                        address = coder.getFromLocationName(cinemaAddress.get(i), 5);
                        if (address != null) {


                            Address location = address.get(0);
                            p1 = new LatLng(location.getLatitude(), location.getLongitude());
                            map.addMarker(new MarkerOptions()
                                    .position(p1)
                                    .title(obj.getString("cinemaName"))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }


                        // May throw an IOException



                }

            }
        }

}