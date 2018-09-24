package com.weather;

import com.weather.collector.MinMaxValues;
import com.weather.collector.MultiThreadCollector;
import com.weather.collector.SingleThreadCollector;
import com.weather.httpserver.MinMaxHttpServer;
import com.weather.openweather.CityReaderFromJson;
import com.weather.openweather.json.city.City;

import java.util.List;

/**
 * Created by Szabolcs Filep in 22 October 2017.
 */
class MinMaxWeatherApplication {

	private final MinMaxValues minMaxValues = new MinMaxValues();

	public static void main(String[] args) {

		new MinMaxWeatherApplication().start();
	}

	private void start() {
		System.out.println("Start reading cities from file...");
		List<City> cities = new CityReaderFromJson().readCitiesForCountry("hu");
		System.out.println("Number of hungarian cities: " + cities.size());
		//collectInSingleThread(cities);
		collectInMultiThread(cities);
	}

	private void collectInSingleThread(List<City> cities) {
		System.out.println("\n----------------------------------------------");
		System.out.println(" Start requesting temperatures one by one...");
		System.out.println("----------------------------------------------");
		long messaureTime = System.currentTimeMillis();

		new SingleThreadCollector().checkAndCollectTemperatures(cities, minMaxValues);
		//TODO
		System.out.println("RESULT: \n" + minMaxValues);

		System.out.println("Finished in " + (System.currentTimeMillis() - messaureTime) + "ms.");
	}

	public void collectInMultiThread(List<City> cities){
		System.out.println("\n\n----------------------------------------------");
		System.out.println(" Start requesting temperatures parallely...");
		System.out.println("----------------------------------------------");
		long messaureTime = System.currentTimeMillis();

		new MultiThreadCollector().checkAndCollectTemperatures(cities, minMaxValues);

		System.out.println("Finished in " + (System.currentTimeMillis() - messaureTime) + "ms.");

		startHttpServer();
	}

	private void startHttpServer() {
		new MinMaxHttpServer().start(minMaxValues);
	}

}