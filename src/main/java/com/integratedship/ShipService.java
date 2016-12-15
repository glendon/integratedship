package com.integratedship;

import com.google.inject.persist.Transactional;

import com.integratedship.dao.ShipDao;
import com.integratedship.model.Ship;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class ShipService {

  private final ShipDao shipDao;

  @Inject
  public ShipService(ShipDao shipDao) {
    this.shipDao = shipDao;
  }

  @Transactional
  public Ship launching(String shipName) {
    Ship ship = new Ship();
    ship.setName(shipName);

    shipDao.save(ship);

    return ship;
  }

  @Transactional
  public void update(Ship ship) {
    shipDao.save(ship);
  }

  public Optional<Ship> get(Long id) {
    return shipDao.get(id);
  }

  public List<Ship> all() {
    return shipDao.listAll();
  }

  @Transactional
  public void remove(Long id) {
    shipDao.get(id).ifPresent(shipDao::delete);
  }

  @Transactional
  public void removeAll() {
    all().forEach(ship -> remove(ship.getId()));
  }

}
