package pwc.pathfinder.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pwc.pathfinder.common.dto.Country;
import pwc.pathfinder.web.service.PathFindingService;
import pwc.pathfinder.web.service.impl.finder.PathFinder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PathFindingServiceImpl implements PathFindingService {

    private final PathFinder pathFinder;

    /**
     * {@inheritDoc}
     */
    @Cacheable("findShortestRoute")
    @Override
    public Collection<String> findShortestRoute(String origin, String destination) {
        Collection<Country> shortestRoute = pathFinder.findShortestRoute(origin, destination);

        return shortestRoute.stream()
                            .map(Country::getCountryCode)
                            .collect(Collectors.toCollection(LinkedList::new));
    }
}
