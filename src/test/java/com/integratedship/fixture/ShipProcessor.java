package com.integratedship.fixture;

import com.integratedship.model.Ship;

import javax.persistence.EntityManager;
import br.com.six2six.fixturefactory.processor.Processor;

public class ShipProcessor implements Processor {

  private final EntityManager entityManager;

  public ShipProcessor(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void execute(Object result) {
    if(result.getClass() != Ship.class) return;

    entityManager.persist(result);
    entityManager.flush();
  }
}
