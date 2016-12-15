import com.google.inject.Provider;

import com.typesafe.config.Config;

import org.flywaydb.core.Flyway;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.typesafe.config.ConfigFactory;

public class LibraryTest {

    private static Config config = ConfigFactory.load("dev");
    static EntityManager entityManager;
    static Provider<EntityManager> entityManagerProvider;

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
        properties.put("hibernate.show_sql", config.getString("postgres.showSql"));
        properties.put("hibernate.format_sql", "true");
        return Persistence.createEntityManagerFactory("integrated", properties).createEntityManager();
    }

    @Test
    public void testeQualquer(){
        System.out.println("Test?");
    }
}
