package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.Event;
import model.Person;

public class MapsFragment extends Fragment {

    private DataCache cache = DataCache.getInstance();
    private GoogleMap map;
    private TextView name;
    private TextView eventText;
    private ImageView genderImageView;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        // When map is loaded.
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            makeMarkers(googleMap);

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        setHasOptionsMenu(true);
        Iconify.with(new FontAwesomeModule());
        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).
                colorRes(R.color.android_icon).sizeDp(50);

        genderImageView = view.findViewById(R.id.gender_image_view);
        genderImageView.setImageDrawable(genderIcon);
        name = view.findViewById(R.id.person_name);
        eventText = view.findViewById(R.id.event_text);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void makeMarkers(GoogleMap googleMap) {
        for (Event e : cache.getEvents()) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(e.getLatitude(), e.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(getEventColor(e))));
            marker.setTag(e);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    Drawable maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(50);
                    Drawable femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(50);
                    Event event = (Event) marker.getTag();
                    Person currPerson = cache.findPerson(cache.getPeople(), event.getPersonID());
                    name.setText(formatPersonText(currPerson));
                    eventText.setText(formatEventText(event));

                    // EVENT lines
                    eventLines(event);
                    for (PolylineOptions line : cache.getEventLines()) {
                        map.addPolyline(line);
                    }

                    // FAMILY lines
                    parentLines(currPerson, 10);
                    for (PolylineOptions line : cache.getFamilyLines()) {
                        map.addPolyline(line);
                    }

                    if (currPerson.getGender().equals("m") ||
                            (currPerson.getGender().equals("male"))) {
                        genderImageView.setImageDrawable(maleIcon);
                    } else {
                        genderImageView.setImageDrawable(femaleIcon);
                    }
                    return false;
                }
            });
        }
    }

    public String formatPersonText(Person p) {
        return p.getFirstName() + " " + p.getLastName();
    }

    public String formatEventText(Event e) {
        return e.getEventType().toUpperCase() + ": " + e.getCity() + ", " + e.getCountry() + " ("
                + e.getYear() + ")";
    }


    public void eventLines(Event event) {
        Map<Person, ArrayList<Event>> map = cache.getEventsMap();
        Person currPerson = null;
        ArrayList<PolylineOptions> polyLineEvents = new ArrayList<>();
        for (Person p : map.keySet()) {
            if (p.equals(event.getPersonID())) {
                currPerson = p;
            }
        }

        // Event Lines
        if (map.get(currPerson).size() > 0) {
            for (int i = 0; i < map.get(currPerson).size() - 1; i++) { // For every event in the Person
                ArrayList<Event> arrayList = map.get(currPerson); // Get the event list for that Person

                Event e = arrayList.get(i);
                LatLng startPoint;
                LatLng endPoint;

                PolylineOptions options = new PolylineOptions().color(20).width(3);
                    startPoint = new LatLng(e.getLatitude(), e.getLongitude());
                    endPoint = new LatLng(arrayList.get(i + 1).getLatitude(), arrayList.get(i + 1).getLongitude());
                    options.add(startPoint, endPoint);
                    polyLineEvents.add(options);
            }
            cache.setEventLines(polyLineEvents);




        }
    }

    public void parentLines(Person currPerson, int width) {
        Map<Person, ArrayList<Event>> map = cache.getEventsMap();
        ArrayList<PolylineOptions> parentLines = new ArrayList<>();
        ArrayList<Event> startEventList = map.get(currPerson);
        Person father = cache.findPerson(cache.getPeople(), currPerson.getFatherID());
        Person mother = cache.findPerson(cache.getPeople(), currPerson.getMotherID());
        float fatherLatitude = map.get(father).get(0).getLatitude();
        float fatherLongitude = map.get(father).get(0).getLongitude();
        float motherLatitude = map.get(mother).get(0).getLatitude();
        float motherLongitude = map.get(mother).get(0).getLongitude();
        float startLatitude = startEventList.get(0).getLatitude();
        float startLongitude = startEventList.get(0).getLongitude();

        if (width > 2) {
            if (currPerson.getFatherID() != null) {
                LatLng startPoint = new LatLng(startLatitude, startLongitude);
                LatLng endPoint = new LatLng(fatherLatitude, fatherLongitude);
                PolylineOptions newLine = new PolylineOptions().color(200);
                newLine.add(startPoint, endPoint);
                parentLines.add(newLine);
                parentLines(father, width - 2);
            }

            if (currPerson.getMotherID() != null) {
                LatLng startPoint = new LatLng(startLatitude, startLongitude);
                LatLng endPoint = new LatLng(motherLatitude, motherLongitude);
                PolylineOptions newLine = new PolylineOptions().color(200);
                newLine.add(startPoint, endPoint);
                parentLines.add(newLine);
                parentLines(mother, width - 2);
            }
        }

    }

    public float getEventColor(Event e) {
        float colorCode = 0;
        Map<Event, Float> colorMap = randomColorMap();
        switch (e.getEventType()) {
            case "Marriage":
            case "marriage":
                colorCode = BitmapDescriptorFactory.HUE_ROSE;
                break;
            case "Death":
            case "death":
                colorCode = BitmapDescriptorFactory.HUE_VIOLET;
                break;
            case "Birth":
            case "birth":
                colorCode = BitmapDescriptorFactory.HUE_CYAN;
                break;
            default:
                if (colorMap.isEmpty()) {
                    colorCode = BitmapDescriptorFactory.HUE_YELLOW;
                } else {
                    for (Event event : colorMap.keySet()) {
                        if (e.getEventType().equals(event.getEventType())) {
                            colorCode = colorMap.get(event);
                        }
                    }
                }
                break;
        }
        return colorCode;
    }

    public Map<Event, Float> randomColorMap() {
        Map<Event, Float> colorMap = new HashMap<>();
        float currColor = 0;
        for (Event currEvent : cache.getEvents()) {
            boolean colorFound = false;
            if (currEvent.getEventType().equals("Marriage") || currEvent.getEventType().equals("Death")
                || currEvent.getEventType().equals("Birth") || currEvent.getEventType().equals("marriage")
                || currEvent.getEventType().equals("death") || currEvent.getEventType().equals("birth")) {
                continue; // Skip the value
            } else if (colorMap.isEmpty()) {
                colorMap.put(currEvent, currColor);
            } else { // The colorMap is not empty
                for (Event event : colorMap.keySet()) { // For each event in the colorMap
                    if (currEvent.getEventType().toLowerCase(Locale.ROOT).equals(event.getEventType().toLowerCase())) { // If the current Event's type is the same as an Event's type in the colorMap
                        float color = colorMap.get(event); // Take that color
                        colorMap.put(currEvent, color); // Give it the same color as that event and put it into the database.
                        colorFound = true;
                    }
                }
                if (!colorFound) {
                    currColor += currColor + 15;
                    if (currColor > 255) {
                        currColor = 5;
                    }
                    colorMap.put(currEvent, currColor);
                }
            }
        }
        return colorMap;
    }
}