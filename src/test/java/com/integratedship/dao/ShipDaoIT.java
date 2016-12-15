package com.integratedship.dao;

import com.google.inject.Provider;

import com.integratedship.fixture.ShipFixture;
import com.integratedship.fixture.ShipProcessor;
import com.integratedship.model.Ship;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ShipDaoIT {

  private static Config config = ConfigFactory.load("dev");
  private static EntityManager entityManager;
  private static Provider<EntityManager> entityManagerProvider;
  private static ShipProcessor shipProcessor;

  private ShipDao shipDao;

  @Before
  public void setup() {
    FixtureFactoryLoader.loadTemplates(ShipFixture.class.getPackage().getName());
    shipDao = new ShipDao(entityManagerProvider);
    shipProcessor = new ShipProcessor(entityManager);
    entityManager.getTransaction().begin();
  }

  @After
  public void tearDownTransaction() {
    entityManager.getTransaction().rollback();
  }

  @BeforeClass
  public static void beforeClass() {
    Flyway migration = new Flyway();
    migration.setDataSource(
        config.getString("postgres.url"),
        config.getString("postgres.user"),
        config.getString("postgres.password")
    );
    migration.clean();
    migration.migrate();

    entityManager = getEntityManager();
    entityManagerProvider = () -> entityManager;
  }

  public static EntityManager getEntityManager() {
    Map<String, String> properties = new HashMap<>();
    properties.put("javax.persistence.jdbc.url", config.getString("postgres.url"));
    properties.put("javax.persistence.jdbc.user", config.getString("postgres.user"));
    properties.put("javax.persistence.jdbc.password", config.getString("postgres.password"));
    return Persistence.createEntityManagerFactory("integrated", properties).createEntityManager();
  }

  @Test
  public void shouldCreateNewShip() {

    Ship ship = new Ship();
    ship.setName("Should be saved");

    shipDao.save(ship);

    assertNotNull(ship.getId());
  }

  @Test
  public void shouldDeleteShip() {
    Ship ship = Fixture.from(Ship.class).uses(shipProcessor).gimme("valid");

    shipDao.delete(ship);

    Optional<Ship> deleted = shipDao.get(ship.getId());
    assertFalse(deleted.isPresent());
  }

  @Test
  public void shouldUpdateName() {
    Ship ship = Fixture.from(Ship.class).uses(shipProcessor).gimme("valid");
    String newName = "New name";

    ship.setName(newName);
    shipDao.save(ship);

    Optional<Ship> fromDb = shipDao.get(ship.getId());
    assertTrue(fromDb.isPresent());
    assertEquals(newName, fromDb.get().getName());
  }

  @Test
  public void shouldFindShipById() {
    Ship ship = Fixture.from(Ship.class).uses(shipProcessor).gimme("valid");

    Optional<Ship> recovered = shipDao.get(ship.getId());

    assertTrue(recovered.isPresent());
    assertEquals(ship.getId(), recovered.get().getId());
  }

  @Test
  public void shouldListAll() {
    List<Ship> shipList = Fixture.from(Ship.class).uses(shipProcessor).gimme(10, "valid");

    assertEquals(shipList.size(), shipDao.listAll().size());
  }


}
