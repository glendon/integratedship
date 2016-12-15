package com.integratedship.fixture;

import com.integratedship.model.Ship;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class ShipFixture implements TemplateLoader {

  @Override
  public void load() {
    Fixture.of(Ship.class).addTemplate("valid", new Rule(){
      {
        add("name", regex("w{10}"));
      }
    });
  }
}
