package com.mit.iot.listener;

import com.mit.iot.config.HkWiFiNettyServer;
import com.mit.iot.config.SensorNettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Description 监听器
 */
@Component
public class ServerListener implements ServletContextListener {

    @Value("${netty.server.sensor.port}")
    private int sensorPort;
    @Value("${netty.server.sensor.enabled:true}")
    private boolean sensorServerEnabled;

    @Value("${netty.server.wifi.port}")
    private int hkWiFiPort;
    @Value("${netty.server.wifi.enabled:true}")
    private boolean hkWiFiServerEnabled;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        if (sensorServerEnabled) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new SensorNettyServer(sensorPort).run();
                }
            }).start();
        }

        if (hkWiFiServerEnabled) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new HkWiFiNettyServer(hkWiFiPort).run();
                }
            }).start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }
}
