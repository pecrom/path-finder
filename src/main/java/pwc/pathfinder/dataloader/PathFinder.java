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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PathFinder {

    private Map<String, Country> countryMap;
    private Map<String, Set<String>> regions;

    @Setter
    @Resource
    private InputStream countriesDataStream;

    @PostConstruct
    public void init() throws IOException {
        load(countriesDataStream);
    }

    private void load(InputStream dataStream) throws IOException {
        List<JsonCountry> jsonCountries = parseJson(dataStream);
        initCountryAndRegionsMap(jsonCountries);
    }

    /**
     * Check existence of country code
     *
     * @param countryCode country code
     * @return true / false
     */
    public boolean isNotCountryCodeExists(String countryCode) {
        return !countryMap.containsKey(countryCode);
    }

    /**
     * Find the shortest route from origin to destination
     * @param origin origin country code
     * @param destination destination country code
     * @return Shortest route, empty {@link Set<Country>} when no route found
     */
    public Set<Country> findShortestRoute(String origin, String destination) {
        Set<Country> currentRoute = new LinkedHashSet<>();
        ShortestRoute shortestRoute = new ShortestRoute();

        Country originCountry = countryMap.get(origin);
        Country destinationCountry = countryMap.get(destination);

        if (routeExists(originCountry, destinationCountry)) {
            Set<String> searchRegions = regionsToSearch(originCountry);
            findRoute(destination, countryMap.get(origin), currentRoute, shortestRoute, removeNotAccessible(countryMap.values(), searchRegions));
        }

        // get the shortest route
        return SetUtils.emptyIfNull(shortestRoute.getRoute());
    }

    private Set<String> regionsToSearch(Country origin) {
        Set<String> regionsToSearch = new HashSet<>();
        regionsToSearch.add(origin.getRegion());
        regionsToSearch.addAll(regions.getOrDefault(origin.getRegion(), SetUtils.emptySet()));

        return regionsToSearch;
    }

    private boolean regionsConnected(Country origin, Country destination) {
        return origin.getRegion().equals(destination.getRegion()) || regions.computeIfAbsent(origin.getRegion(), ignore -> new HashSet<>())
                        .contains(destination.getRegion());
    }

    // check whether there is a theoretical route between origin and destination. Eg. neither of them is not an isolated island
    private boolean routeExists(Country origin, Country destination) {
        return CollectionUtils.isNotEmpty(origin.getNeighbours()) &&
                    CollectionUtils.isNotEmpty(destination.getNeighbours()) &&
                        regionsConnected(origin, destination);
    }

    private Collection<Country> removeNotAccessible(Collection<Country> countries, Set<String> regions) {
        return CollectionUtils.emptyIfNull(countries)
                .parallelStream()
                .filter(country -> CollectionUtils.isNotEmpty(country.getNeighbours()) && regions.contains(country.getRegion()))
                .collect(Collectors.toSet());
    }

    private void findRoute(String destination, Country currentCountry, Set<Country> route, ShortestRoute shortestRoute, Collection<Country> notVisited) {
        Set<Country> currentRoute = new LinkedHashSet<>(route);
        currentRoute.add(currentCountry);
        notVisited.remove(currentCountry);

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

    private void initCountryAndRegionsMap(List<JsonCountry> jsonCountries) {
        countryMap = CollectionUtils.emptyIfNull(jsonCountries).parallelStream()
                                    .map(this::createCountry)
                                    .collect(Collectors.toMap(Country::getCountryCode, Function.identity()));

        regions = new HashMap<>();

        Country country;
        Country neighbour;
        for(JsonCountry jsonCountry : jsonCountries) {
            country = countryMap.get(jsonCountry.getCountryCode());

            // fill neighbours and regions
            for(String border : jsonCountry.getBorders()) {
                neighbour = countryMap.get(border);
                country.addNeighbour(neighbour);

                addRegionConnection(country, neighbour);
            }

        }
    }

    private void addRegionConnection(Country country, Country neighbour) {
        if (isNotSameRegion(country, neighbour)) {
            regions.computeIfAbsent(country.getRegion(), ignore -> new HashSet<>())
                    .add(neighbour.getRegion());
        }
    }

    private boolean isNotSameRegion(Country country, Country neighbour) {
        return !country.getRegion().equals(neighbour.getRegion());
    }

    private Country createCountry(JsonCountry country) {
        return new Country(country.getCountryCode(), country.getRegion());
    }

}
