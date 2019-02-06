package pwc.pathfinder.web.service.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import pwc.pathfinder.dataloader.PathFinder;
import pwc.pathfinder.dataloader.common.Country;
import pwc.pathfinder.web.service.PathFindingService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Setter
@Service
public class PathFindingServiceImpl implements PathFindingService {

    @Autowired
    private PathFinder pathFinder;


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

    /**
     * {@inheritDoc}
     */
    @Cacheable("isNotCountryCodeExists")
    @Override
    public boolean isNotCountryCodeExists(String countryCode) {
        return pathFinder.isNotCountryCodeExists(countryCode);
    }


}
