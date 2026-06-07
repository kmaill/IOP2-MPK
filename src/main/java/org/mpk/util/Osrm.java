package org.mpk.util;

import com.formdev.flatlaf.json.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jxmapviewer.viewer.GeoPosition;
import org.mpk.GPSCoordinates;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

//Klasa w util bo nie wiedziałem gdzie ją dać xd
public class Osrm {
    private static JSONArray routePoints = null;
    private static final List<GeoPosition> routePointsGeo = new ArrayList<>();

    private static JSONArray routeSpeeds = null;
    private static double routeDuration = -1.f;

//    public static void updateRoute(GeoPosition startPoint, GeoPosition endPoint) throws IOException {
//        String url = String.format(Locale.US, "http://router.project-osrm.org/route/v1/driving/%.6f,%.6f;%.6f,%.6f?geometries=geojson&overview=full&annotations=true",
//                startPoint.getLongitude(), startPoint.getLatitude(),
//                endPoint.getLongitude(), endPoint.getLatitude());
//        System.out.println(url);
//        HttpURLConnection conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
//        conn.setRequestProperty("User-Agent", "Mozilla/5.0 JXMapViewer2");
//        conn.setConnectTimeout(5000);
//        conn.setReadTimeout(5000);
//
//        JSONObject tempJson = new JSONObject(new String(conn.getInputStream().readAllBytes()));
//
//        JSONArray points = tempJson
//                .getJSONArray("routes")
//                .getJSONObject(0)
//                .getJSONObject("geometry")
//                .getJSONArray("coordinates");
//
//        double duration = tempJson
//                .getJSONArray("routes")
//                .getJSONObject(0)
//                .getJSONArray("legs")
//                .getJSONObject(0)
//                .getDouble("duration");
//
//        routePoints = points;
//        routeDuration = duration;
//
//        routePointsGeo.clear();
//
//        //return points;
//    }

    public static void updateRoute(List<GeoPosition> points) throws IOException {
        String url = "http://router.project-osrm.org/route/v1/driving/" +
                points.stream()
                        .map(p -> String.format(Locale.US, "%.6f,%.6f", p.getLongitude(), p.getLatitude()))
                        .collect(Collectors.joining(";"))
                + "?geometries=geojson&overview=full&annotations=true";
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) URI.create(url).toURL().openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 JXMapViewer2");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        JSONObject tempJson = new JSONObject(new String(conn.getInputStream().readAllBytes()));

        JSONArray resPoints = tempJson
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONArray("coordinates");

        JSONArray routes = tempJson.getJSONArray("routes");
        JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
        
        double duration = routes.getJSONObject(0).getDouble("duration");

        JSONArray allSpeeds = new JSONArray();
        for (int i=0; i<legs.length(); i++) {
            JSONArray legSpeeds = legs.getJSONObject(i).getJSONObject("annotation").getJSONArray("speed");
            for (int j=0; j<legSpeeds.length(); j++) {
                allSpeeds.put(legSpeeds.getDouble(j));
            }
        }

        routePoints = resPoints;
        routeDuration = duration;
        routeSpeeds = allSpeeds;

        routePointsGeo.clear();

        //return points;
    }

//    public static JSONArray getRoutePointsJSON(GeoPosition startPoint, GeoPosition endPoint) throws IOException {
//        if(routePoints == null ||
//                !new GeoPosition(routePoints.getJSONArray(0).getDouble(1),
//                        routePoints.getJSONArray(0).getDouble(0)).equals(startPoint) ||
//                !new GeoPosition(routePoints.getJSONArray( routePoints.length()-1 ).getDouble(1),
//                        routePoints.getJSONArray( routePoints.length()-1 ).getDouble(0)).equals(endPoint)) {
//            updateRoute(startPoint, endPoint);
//        }
//        return routePoints;
//    }

    public static JSONArray getRoutePointsJSON(List<GeoPosition> inputPoints) throws IOException {
        // Tu było więcej rzeczy przy dwóch punktach dlatego funkcja pusta xd
        updateRoute(inputPoints);
        return routePoints;
    }


    // --------------------------------------------------------------
    // -- DO PRZEROBIENIA LUB USUNIĘCIA JEŚLI NIE BEDZIE POTRZEBNA --
    // --------------------------------------------------------------


//    public static List<GeoPosition> getRoutePointsGeo(GeoPosition startPoint, GeoPosition endPoint) throws IOException {
//        updateRoute(startPoint, endPoint);
//        if(routePointsGeo.isEmpty()) {
//            for(int i = 1; i < routePoints.length(); i++) {
//                routePointsGeo.add(new GeoPosition(
//                        routePoints.getJSONArray(i).getDouble(1),
//                        routePoints.getJSONArray(i).getDouble(0))
//                );
//            }
//        }
//        return routePointsGeo;
//    }

    public static double getRouteDuration(List<GeoPosition> inputPoints) throws IOException {
        if(routeDuration < 0.f) {
            updateRoute(inputPoints);
        }
        return routeDuration;
    }

    public static JSONArray getRouteSpeedsArray() {
        return routeSpeeds;
    }
}
