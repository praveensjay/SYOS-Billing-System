package com.syos.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api") // All REST endpoints will start with /api
public class AppConfig extends Application {
    public AppConfig() {
        System.out.println("✅ AppConfig initialized!");
    }
}
