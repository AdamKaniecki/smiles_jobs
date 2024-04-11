package pl.zajavka.integration;


import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

//dzięki tej konfiguracji każdy test który dziedziczy z tej klasy będzie testowany w tych konkretnych warunkach
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//to ustawienie to deklaracja że nie
//chcemy korzystać z bazy H2
@TestPropertySource(locations = "classpath:application-test.yaml")
@ContextConfiguration(initializers = DatabaseContainerInitializer.class)//ta klasa będzie służyła do tego żeby stworzyć
//bazę postgresql na czas trwania testów i będzie importowała konfigurację ustawioną w DatabaseContainerInitializer
public abstract class AbstractJpaIT {
}
