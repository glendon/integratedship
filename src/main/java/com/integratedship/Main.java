package com.integratedship;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.integratedship.model.Ship;

public class Main {

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new JpaModule());

    ShipService shipService = injector.getInstance(ShipService.class);

    Ship ship1 = shipService.launching("My #1 boat");
    Ship ship2 = shipService.launching("My #2 boat");

    shipService.get(ship1.getId()).ifPresent(ship -> {
      ship.setName("#1 boat");
      shipService.update(ship);
    });

    System.out.println("My fleet has: ");
    shipService.all().stream().map(Ship::getName).forEach(System.out::println);

    shipService.remove(ship2.getId());

    System.out.println("After remove one, my fleet has: ");
    shipService.all().stream().map(Ship::getName).forEach(System.out::println);

    //Because it is a very very very simple test, I can do it:
    shipService.removeAll();

    System.out.println("That's all folks.");
  }
}
