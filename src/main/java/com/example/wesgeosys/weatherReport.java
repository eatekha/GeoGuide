package com.example.wesgeosys;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;


public class weatherReport {


    static private final String error = "NA";
    static private final String lat = "42.9849";
    static private final String lon = "-81.2453";
    static private String tempCurrent = error;
    static private String tempMin = error;
    static private String tempMax = error;
    static private String tempFeelsLike = error;
    static private String humidity = error;
    static private String precipitationStatus = error;
    static private String precipitationDescription = error;
    static private JSONObject jsonData = null;
    public static String GetTempCurrent() {
        return tempCurrent;
    }
    public static String GetTempMin() {
        return tempMin;
    }
    public static String GetTempMax() {
        return tempMax;
    }
    public static String GetTempFeelsLike() {
        return tempFeelsLike;
    }
    public static String GetHumidity() {
        return humidity;
    }
    public static String GetPrecipitationStatus() {
        return precipitationStatus;
    }
    public static String GetPrecipitationDescription() {
        return precipitationDescription;
    }




    private static void SetCurrentWeatherData(){
        try {
            String apiKey = "6ae73b11767febcd9a4c5b850246e0f1";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + ""))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            jsonData = new JSONObject(response.body());
        }
        catch (Exception e) {
            PrintError(e);
            jsonData = null;
        }
    }


    private static void SetPrecipitation() {
        try {
            JSONArray weatherArray = jsonData.getJSONArray("weather");
            JSONObject inner = weatherArray.getJSONObject(0);
            var precipitationStatus = inner.getString("main");
            var precipitationDescription = inner.getString("description");
            weatherReport.precipitationStatus = precipitationStatus;
            weatherReport.precipitationDescription = precipitationDescription;
        }
        catch (Exception e) {
            PrintError(e);
            weatherReport.precipitationStatus = error;
            weatherReport.precipitationDescription = error;
        }
    }


    private static void SetTemperature(){
        try {
            var main = jsonData.getJSONObject("main");
            float temp = main.getFloat("temp") - 273.15f;
            tempCurrent = String.valueOf(Math.round(temp));
        }
        catch (Exception e) {
            PrintError(e);
            tempCurrent = error;
        }
    }


    private static void SetTemperatureMin(){
        try {
            var main = jsonData.getJSONObject("main");
            float temp = main.getFloat("temp_min") - 273.15f;
            tempMin = String.valueOf(Math.round(temp));
        }
        catch (Exception e) {
            PrintError(e);
            tempMin = error;
        }
    }


    private static void SetTemperatureMax(){
        try {
            var main = jsonData.getJSONObject("main");
            float temp = main.getFloat("temp_max") - 273.15f;
            tempMax = String.valueOf(Math.round(temp));
        }
        catch (Exception e) {
            PrintError(e);
            tempMax = error;
        }
    }

    private static void SetFeelsLikeTemperature(){
        try {
            var innerJSONData = jsonData.getJSONObject("main");
            float temp = innerJSONData.getFloat("feels_like") - 273.15f;
            tempFeelsLike = String.valueOf(Math.round(temp));
        }
        catch (Exception e) {
            PrintError(e);
            tempFeelsLike = error;
        }
    }


    private static void SetHumidity() {
        try {
            var innerJSONData = jsonData.getJSONObject("main");
            float humidity = innerJSONData.getFloat("humidity");
            weatherReport.humidity = String.valueOf(humidity);
        }
        catch (Exception e) {
            PrintError(e);
            humidity = error;
        }
    }


    private static void PrintError(Exception e) {
        System.out.println("Did not succeed");
        System.out.println("Error: " + e);
    }


    public static void SetAllWeatherData() {
        SetCurrentWeatherData();
        SetTemperature();
        SetTemperatureMax();
        SetTemperatureMin();
        SetFeelsLikeTemperature();
        SetHumidity();
        SetPrecipitation();
    }

    public static void main(String[] args) {
    }
}