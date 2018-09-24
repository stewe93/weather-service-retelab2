package com.weather.collector;

import com.weather.openweather.WeatherApiCaller;
import com.weather.openweather.json.city.City;
import com.weather.openweather.json.temperature.Weather;

public class TemperatureCollectorTask implements Runnable {

	private WeatherApiCaller weatherApiCaller = new WeatherApiCaller();

	private final City city;
	private final MinMaxValues minMax;

	public TemperatureCollectorTask(City city, MinMaxValues minMax) {
		this.city = city;
		this.minMax = minMax;
	}

	@Override
	public void run() {

		Weather cityWeather = weatherApiCaller.callWeatherApi(city);

		setMinAndMax(cityWeather);
	}

	private void setMinAndMax(Weather w) {
		Float thisCityTemp = w.getMain().getTemp();

		synchronized (minMax) {
			if (minMax.getMinTempWeather() == null) {
				minMax.setMinTempWeather(w);
			}
			if (minMax.getMaxTempWeather() == null) {
				minMax.setMaxTempWeather(w);
			}

			if(thisCityTemp.compareTo(minMax.getMaxTempWeather().getMain().getTemp()) >  0){
				minMax.setMaxTempWeather(w);
			}
			if(thisCityTemp.compareTo(minMax.getMinTempWeather().getMain().getTemp()) < 0){
				minMax.setMinTempWeather(w);
			}
		}

	}

}
