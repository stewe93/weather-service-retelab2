package com.weather.httpserver;

import com.sun.net.httpserver.HttpServer;
import com.weather.collector.MinMaxValues;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MinMaxHttpServer {

    public void start(MinMaxValues minMaxValues) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(41414), 0);
            server.createContext("/", new RestHttpHandler(server, minMaxValues));
            server.start();

            System.out.println("Server started. Stop it by URL: 'localhost:41414/stop'.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
