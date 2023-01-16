package pwc.pathfinder.web.service;

import java.util.Collection;

public interface PathFindingService {

    /**
     * Find the shortest route
     *
     * @param origin country code of origin
     * @param destination country code of destination
     * @return country code of states which are between origin and destination
     */
    Collection<String> findShortestRoute(String origin, String destination);


}
