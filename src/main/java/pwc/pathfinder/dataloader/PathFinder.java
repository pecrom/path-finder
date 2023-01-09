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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        Country originCountry = countryMap.get(origin);
        Country destinationCountry = countryMap.get(destination);

        if (CollectionUtils.isNotEmpty(originCountry.getNeighbours()) && CollectionUtils.isNotEmpty(destinationCountry.getNeighbours())) {
            findRoute(destination, countryMap.get(origin), currentRoute, shortestRoute, removeWithoutBorders(countryMap.values()));
        }

        // get the shortest route
        return SetUtils.emptyIfNull(shortestRoute.getRoute());
    }

    private Collection<Country> removeWithoutBorders(Collection<Country> countries) {
        return CollectionUtils.emptyIfNull(countries)
                .parallelStream()
                .filter(country -> CollectionUtils.isNotEmpty(country.getNeighbours()))
                .collect(Collectors.toSet());
    }

    private void findRoute(String destination, Country currentCountry, Set<Country> route, ShortestRoute shortestRoute, Collection<Country> notVisited) {
        Set<Country> currentRoute = new LinkedHashSet<>(route);
        currentRoute.add(currentCountry);
        notVisited.remove(currentCountry);

        System.out.println("checking: " + currentCountry);
        if (!currentCountry.getCountryCode().equals(destination) && shortestRoute.isShorter(currentRoute)) {

            CollectionUtils.emptyIfNull(currentCountry.getNeighbours())
                            .parallelStream()
                            .filter(notVisited::contains)
                            .forEach(neighbour -> findRoute(destination, neighbour, currentRoute, shortestRoute, new HashSet<>(notVisited)));

        } else if (currentCountry.getCountryCode().equals(destination)) {
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
