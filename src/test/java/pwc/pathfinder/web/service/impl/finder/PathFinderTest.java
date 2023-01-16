package pwc.pathfinder.web.service.impl.finder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import pwc.pathfinder.common.dto.Country;
import pwc.pathfinder.dataloader.DataLoader;
import pwc.pathfinder.dataloader.impl.JsonDataLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathFinderTest {

    private static PathFinder pathFinder;


    @BeforeAll
    public static void setup() throws IOException {
        pathFinder = new PathFinder(getDataLoader());
    }

    @Test
    @DisplayName("Finding shortest path")
    public void findValidShortestPath() {
        List<Country> shortestPath = pathFinder.findShortestRoute("ABW","AGO");

        Country[] expected = new Country[]{new Country("ABW", "Asia"), new Country("AFG", "Asia"), new Country("AGO", "Africa")};

        assertArrayEquals(expected, shortestPath.toArray());
    }

    @Test
    @DisplayName("Finding no path")
    public void findNoPath() {
        List<Country> shortestPath = pathFinder.findShortestRoute("ABW","AIA");

        List<Country> expected = Collections.emptyList();
        assertEquals(expected, shortestPath);

        assertArrayEquals(expected.toArray(), shortestPath.toArray());
    }

    private static DataLoader getDataLoader() throws IOException {
        JsonDataLoader jsonDataLoader = new JsonDataLoader(new ObjectMapper());
        jsonDataLoader.setCountriesData(new ClassPathResource("countries/test-countries.json"));
        jsonDataLoader.init();

        return jsonDataLoader;
    }
}
