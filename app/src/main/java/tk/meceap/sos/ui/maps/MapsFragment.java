package tk.meceap.sos.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import tk.meceap.sos.R;
import tk.meceap.sos.constants.Constants;
import tk.meceap.sos.directionhelpers.CalculateDistanceTime;

public class MapsFragment extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    private MarkerOptions place1, place2;
    Button getDirection;
    GoogleMap gMap;
    private Polyline currentPolyline;

    Location myLocation = null;
    LatLng destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;
    private List<Polyline> polylines = null;
    TextView time, distance;

    ImageView routingMode;
    CheckBox changeMode;
    CalculateDistanceTime distance_task;
    AbstractRouting.TravelMode travelMode = AbstractRouting.TravelMode.WALKING;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            gMap = googleMap;
            getMyLocation();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        time = getActivity().findViewById(R.id.duration);
        distance = getActivity().findViewById(R.id.distance);
        changeMode = getActivity().findViewById(R.id.changeMode);
        routingMode = getActivity().findViewById(R.id.routingMode);
        distance_task = new CalculateDistanceTime(getActivity(), "walking");

        changeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (changeMode.isChecked()) {
                    routingMode.setImageResource(R.drawable.driving_mode_on);
                    travelMode = AbstractRouting.TravelMode.DRIVING;
                    distance_task.travelMode = "driving";
                } else {
                    routingMode.setImageResource(R.drawable.walking_mode_on);
                    travelMode = AbstractRouting.TravelMode.WALKING;
                    distance_task.travelMode = "walking";
                }
            }
        });
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    //to get user location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);

        gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                myLocation = location;
                LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 16f);
                gMap.animateCamera(cameraUpdate);
            }
        });

        //get destination location when user click on map
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                end = latLng;
                gMap.clear();
                destinationLocation = latLng;

                start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                //start route finding
                Findroutes(start, end, travelMode);
                distance_task.getDirectionsUrl(start, end);
                distance_task.setLoadListener(new CalculateDistanceTime.taskCompleteListener() {
                    @Override
                    public void taskCompleted(String[] time_distance) {
                        time.setText("" + time_distance[1]);
                        distance.setText("" + time_distance[0]);
                    }
                });
            }
        });
    }

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End, AbstractRouting.TravelMode travelMode) {
        if (Start == null || End == null) {
            Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_LONG).show();
        } else {
            Location loc = new Location("Final");
            loc.setLongitude(End.longitude);
            loc.setLatitude(End.latitude);
            System.out.println(myLocation.distanceTo(loc));

            Routing routing = new Routing.Builder()
                    .travelMode(travelMode)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(Constants.googleMapKey)  //also define your api key here.
                    .build();
            routing.execute();
        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(getContext(), "Finding routes...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.purple_500));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = gMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);
            } else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        gMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        gMap.addMarker(endMarker);

        if (destinationLocation.longitude != polylineEndLatLng.longitude || destinationLocation.latitude != polylineEndLatLng.latitude) {
            MarkerOptions ends = new MarkerOptions();
            ends.position(end);
            ends.title("Final Destination");
            gMap.addMarker(endMarker);
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}