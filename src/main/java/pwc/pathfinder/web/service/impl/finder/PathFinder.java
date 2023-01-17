package pwc.pathfinder.web.service.impl.finder;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import pwc.pathfinder.common.dto.Country;
import pwc.pathfinder.common.dto.Region;
import pwc.pathfinder.common.dto.WayPoint;
import pwc.pathfinder.dataloader.DataLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PathFinder {

    private final DataLoader dataLoader;

    /**
     * Find the shortest route from origin to destination
     * @param origin origin country code
     * @param destination destination country code
     * @return Shortest route, empty {@link List<Country>} when no route found
     */
    public List<Country> findShortestRoute(String origin, String destination) {

        Country originCountry = dataLoader.getCountry(origin);
        Country destinationCountry = dataLoader.getCountry(destination);

        List<Country> foundWay = Collections.emptyList();
        if (ObjectUtils.allNotNull(originCountry, destinationCountry) && routeExists(originCountry, destinationCountry)) {
            Set<String> searchRegions = regionsToSearch(originCountry);

            Optional<WayPoint> route = findRoute(destination, originCountry, removeNotAccessibleCountries(dataLoader.getAllCountries(), searchRegions));

            foundWay = route.map(this::getWholeRoute)
                            .orElseGet(Collections::emptyList);
        }

        return foundWay;
    }

    private List<Country> getWholeRoute(WayPoint destination) {
        List<Country> route = new ArrayList<>();

        // get all countries back to the origin
        WayPoint current = destination;
        while (current != null) {
            route.add(current.getCountry());
            current = current.getPreviousWayPoint();
        }

        Collections.reverse(route);

        return route;
    }

    private Set<String> regionsToSearch(Country origin) {
        Set<String> regionsToSearch = new HashSet<>();
        regionsToSearch.add(origin.getRegion());
        regionsToSearch.addAll(connectedRegions(origin.getRegion()));

        return regionsToSearch;
    }

    private Collection<String> connectedRegions(String region) {
        return dataLoader.getRegion(region).getConnectedRegions().stream()
                .map(Region::getName)
                .collect(Collectors.toSet());
    }

    private boolean isRegionsConnected(Country origin, Country destination) {
        return origin.getRegion().equals(destination.getRegion()) || isRegionsConnected(origin.getRegion(), destination.getRegion());
    }

    private boolean isRegionsConnected(String originRegion, String destinationRegion) {
        return dataLoader.getRegion(originRegion)
                .getConnectedRegions().contains(dataLoader.getRegion(destinationRegion));
    }

    // check whether there is a theoretical route between origin and destination. Eg. neither of them is not an isolated island
    private boolean routeExists(Country origin, Country destination) {
        return CollectionUtils.isNotEmpty(origin.getNeighbours()) &&
                    CollectionUtils.isNotEmpty(destination.getNeighbours()) &&
                        isRegionsConnected(origin, destination);
    }

    private Collection<Country> removeNotAccessibleCountries(Collection<Country> countries, Set<String> regions) {
        return CollectionUtils.emptyIfNull(countries)
                .parallelStream()
                .filter(country -> CollectionUtils.isNotEmpty(country.getNeighbours()) && regions.contains(country.getRegion()))
                .collect(Collectors.toSet());
    }

    // BFS graph search algorithm
    private Optional<WayPoint> findRoute(String destination, Country currentCountry, Collection<Country> notVisited) {
        Map<String, WayPoint> ways = new HashMap<>();
        ways.put(currentCountry.getCountryCode(), new WayPoint(currentCountry, null));

        Set<Country> visited = new HashSet<>(notVisited.size());

        Queue<Country> toVisit = new LinkedList<>();
        toVisit.add(currentCountry);
        visited.add(currentCountry);

        while(!toVisit.isEmpty()) {
            Country countryToVisit = toVisit.poll();

            for (Country neighbour : CollectionUtils.emptyIfNull(countryToVisit.getNeighbours())) {
                if (!visited.contains(neighbour)) {
                    toVisit.add(neighbour);
                    visited.add(neighbour);
                    ways.put(neighbour.getCountryCode(), new WayPoint(neighbour, ways.get(countryToVisit.getCountryCode())));

                    if (destination.equals(neighbour.getCountryCode())) {
                        return Optional.of(ways.get(neighbour.getCountryCode()));
                    }
                }
            }
        }

        return Optional.empty();
    }

}
