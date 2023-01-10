package pwc.pathfinder.web.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pwc.pathfinder.dataloader.PathFinder;
import pwc.pathfinder.dataloader.common.Country;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathFindingServiceImplTest {

    @Test
    @DisplayName("Find shortest route")
    public void findShortestRoute() {
        PathFinder pathFinder = mock(PathFinder.class);

        Set<Country> routeSet = new LinkedHashSet<>();
        routeSet.add(new Country("ABC", "Europe"));
        routeSet.add(new Country("DEF", "Europe"));
        routeSet.add(new Country("GHI", "Europe"));

        when(pathFinder.findShortestRoute(anyString(), anyString())).thenReturn(routeSet);

        PathFindingServiceImpl pathFindingService = new PathFindingServiceImpl();
        pathFindingService.setPathFinder(pathFinder);

        String[] expected = new String[]{"ABC", "DEF", "GHI"};

        Collection<String> shortestRoute = pathFindingService.findShortestRoute("ABC", "GHI");

        assertArrayEquals(expected, shortestRoute.toArray());
    }

    @Test
    @DisplayName("Find no route")
    public void findNoRoute() {
        PathFinder pathFinder = mock(PathFinder.class);

        when(pathFinder.findShortestRoute(anyString(), anyString())).thenReturn(Collections.emptySet());

        PathFindingServiceImpl pathFindingService = new PathFindingServiceImpl();
        pathFindingService.setPathFinder(pathFinder);

        Collection<String> shortestRoute = pathFindingService.findShortestRoute("ABC", "GHI");

        assertTrue(shortestRoute.isEmpty());
    }

    @Test
    @DisplayName("Check no country code exists")
    public void noCountryCodeExists() {
        PathFinder pathFinder = mock(PathFinder.class);

        when(pathFinder.isNotCountryCodeExists(anyString())).thenReturn(true);

        PathFindingServiceImpl pathFindingService = new PathFindingServiceImpl();
        pathFindingService.setPathFinder(pathFinder);

        assertTrue(pathFindingService.isNotCountryCodeExists("ABC"));
    }

    @Test
    @DisplayName("Check country code exists")
    public void countryCodeExists() {
        PathFinder pathFinder = mock(PathFinder.class);

        when(pathFinder.isNotCountryCodeExists(anyString())).thenReturn(false);

        PathFindingServiceImpl pathFindingService = new PathFindingServiceImpl();
        pathFindingService.setPathFinder(pathFinder);

        assertFalse(pathFindingService.isNotCountryCodeExists("ABC"));
    }
}
