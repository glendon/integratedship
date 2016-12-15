package com.integratedship.dao;

import com.google.inject.Provider;

import com.integratedship.model.Ship;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class ShipDao {

  private Provider<EntityManager> entityManagerProvider;

  @Inject
  public ShipDao(Provider<EntityManager> entityManagerProvider) {
    this.entityManagerProvider = entityManagerProvider;
  }

  public Optional<Ship> get(Long id) {
    return Optional.ofNullable(entityManagerProvider.get().find(Ship.class, id));
  }

  public void save(Ship entity) {
    entityManagerProvider.get().persist(entity);
    entityManagerProvider.get().flush();
  }

  public void delete(Ship entity) {
    entityManagerProvider.get().remove(entity);
  }

  public List<Ship> listAll() {
    String sql = "FROM Ship s "
        + "ORDER BY s.name";
    return entityManagerProvider.get()
        .createQuery(sql, Ship.class)
        .getResultList();
  }
}
