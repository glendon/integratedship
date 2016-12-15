package com.integratedship;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

public class JpaModule extends AbstractModule {
  private final boolean initialize;

  public JpaModule() {
    this.initialize = true;
  }

  @Override
  protected void configure() {
    Config config = ConfigFactory.load(System.getProperty("ENVIRONMENT", "dev"));
    Map<String, String> properties = new HashMap<>();
    properties.put("javax.persistence.jdbc.user",
        config.getString("postgres.user"));
    properties.put("javax.persistence.jdbc.password",
        config.getString("postgres.password"));

    JpaPersistModule jpa = new JpaPersistModule("integrated");
    jpa.properties(properties);
    install(jpa);
    if (initialize) {
      bind(Initializer.class).asEagerSingleton();
    }
  }

  static class Initializer {
    @Inject
    Initializer(PersistService persistService) {
      persistService.start();
    }
  }
}


