package pwc.pathfinder.dataloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.stereotype.Component;
import pwc.pathfinder.dataloader.common.Country;
import pwc.pathfinder.dataloader.common.JsonCountry;
import pwc.pathfinder.dataloader.common.ShortestRoute;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class PathFinder {

    private Map<String, Country> countryMap;

    @Setter
    @Resource
    private InputStream countriesDataStream;

    @PostConstruct
    public void initPaths() throws IOException {
        loadPaths(countriesDataStream);
    }

    private void loadPaths(InputStream dataStream) throws IOException {
        List<JsonCountry> jsonCountries = parseJson(dataStream);
        initCountryMap(jsonCountries);
    }

    /**
     * Check existence of country code
     * @param countryCode country code
     * @return true / false
     */
    public boolean isNotCountryCodeExists(String countryCode) {
        return !countryMap.containsKey(countryCode);
    }

    /**
     * Find shortest route from origin to destination
     * @param origin origin country code
     * @param destination destination country code
     * @return Shortest route, empty {@link Set<Country>} when no route found
     */
    public Set<Country> findShortestRoute(String origin, String destination) {
        Set<Country> currentRoute = new LinkedHashSet<>();
        ShortestRoute shortestRoute = new ShortestRoute();

        findRoute(destination, countryMap.get(origin), currentRoute, shortestRoute);

        // get the shortest route
        return SetUtils.emptyIfNull(shortestRoute.getRoute());
    }

    private void findRoute(String destination, Country currentCountry, Set<Country> route, ShortestRoute shortestRoute) {
        Set<Country> currentRoute = new LinkedHashSet<>(route);
        currentRoute.add(currentCountry);

        if (!currentCountry.getCountryCode().equals(destination) && shortestRoute.isShorter(currentRoute)) {

            CollectionUtils.subtract(currentCountry.getNeighbours(), currentRoute)// get rid of already visited countries
                    .parallelStream()
                    .forEach(neighbour -> findRoute(destination, neighbour, currentRoute, shortestRoute));
        } else {
            // route to the destination was found
            shortestRoute.setRoute(currentRoute);
        }
    }

    private List<JsonCountry> parseJson(InputStream dataStream) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        CollectionType javaType = jsonMapper.getTypeFactory().constructCollectionType(List.class, JsonCountry.class);

        return jsonMapper.readValue(dataStream, javaType);
    }

    private void initCountryMap(List<JsonCountry> jsonCountries) {
        countryMap = new HashMap<>(jsonCountries.size());

        for(JsonCountry jsonCountry : jsonCountries) {
            Country country = countryMap.computeIfAbsent(jsonCountry.getCountryCode(), this::createCountry);

            // fill neighbours
            for(String border : jsonCountry.getBorders()) {
                country.addNeighbour(countryMap.computeIfAbsent(border, this::createCountry));
            }
        }
    }

    private Country createCountry(String countryCode) {
        return new Country(countryCode);
    }


}
