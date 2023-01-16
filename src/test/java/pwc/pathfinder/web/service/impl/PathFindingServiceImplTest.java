package pwc.pathfinder.web.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pwc.pathfinder.common.dto.Country;
import pwc.pathfinder.web.service.impl.finder.PathFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathFindingServiceImplTest {

    @Test
    @DisplayName("Find shortest route")
    public void findShortestRoute() {
        PathFinder pathFinder = mock(PathFinder.class);

        List<Country> routeSet = new ArrayList<>();
        routeSet.add(new Country("ABC", "Europe"));
        routeSet.add(new Country("DEF", "Europe"));
        routeSet.add(new Country("GHI", "Europe"));

        when(pathFinder.findShortestRoute(anyString(), anyString())).thenReturn(routeSet);

        PathFindingServiceImpl pathFindingService = new PathFindingServiceImpl(pathFinder);

        String[] expected = new String[]{"ABC", "DEF", "GHI"};

        Collection<String> shortestRoute = pathFindingService.findShortestRoute("ABC", "GHI");

        assertArrayEquals(expected, shortestRoute.toArray());
    }

    @Test
    @DisplayName("Find no route")
    public void findNoRoute() {
        PathFinder pathFinder = mock(PathFinder.class);

        when(pathFinder.findShortestRoute(anyString(), anyString())).thenReturn(Collections.emptyList());

        PathFindingServiceImpl pathFindingService = new PathFindingServiceImpl(pathFinder);

        Collection<String> shortestRoute = pathFindingService.findShortestRoute("ABC", "GHI");

        assertTrue(shortestRoute.isEmpty());
    }


}
