package pwc.pathfinder.dataloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import pwc.pathfinder.common.dto.Country;
import pwc.pathfinder.common.dto.Region;
import pwc.pathfinder.dataloader.impl.JsonDataLoader;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JsonDataLoaderTest {

    private static final String NON_EXISTING_COUNTRY_CODE = "XXX";
    private static final String EXISTING_COUNTRY_CODE = "AFG";

    private static JsonDataLoader jsonDataLoader;

    @BeforeAll
    public static void setup() throws IOException {
        jsonDataLoader = new JsonDataLoader(new ObjectMapper());
        jsonDataLoader.setCountriesData(new ClassPathResource("countries/test-countries.json"));
        jsonDataLoader.init();
    }

    @Test
    @DisplayName("No country code exists")
    public void noCountryCodeExists() {
        assertTrue(jsonDataLoader.isNotCountryCodeExists(NON_EXISTING_COUNTRY_CODE));
    }

    @Test
    @DisplayName("Country code exists")
    public void countryCodeExists() {
        assertFalse(jsonDataLoader.isNotCountryCodeExists(EXISTING_COUNTRY_CODE));
    }

    @Test
    @DisplayName("Get all countries")
    public void getAllCountries() {
        Collection<Country> countries = jsonDataLoader.getAllCountries();
        Collection<Country> expectedCountries = Set.of(
                new Country("ABW", "Asia"),
                new Country("AFG", "Asia"),
                new Country("AGO", "Africa")
        );

        assertEquals(expectedCountries.size(), countries.size());
        assertTrue(countries.containsAll(expectedCountries));
    }

    @Test
    @DisplayName("Check region and connected ones")
    public void regionAndConnected() {
        Region region = jsonDataLoader.getRegion("Asia");
        Collection<Region> connectedRegions = region.getConnectedRegions();
        Collection<Region> expectedConnectedRegions = Set.of(jsonDataLoader.getRegion("Africa"));

        assertEquals(expectedConnectedRegions.size(), connectedRegions.size());
        assertTrue(connectedRegions.containsAll(expectedConnectedRegions));
    }
}
