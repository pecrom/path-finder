package pwc.pathfinder.dataloader.common;

import lombok.Getter;
import lombok.Synchronized;

import java.util.Set;

@Getter
public class ShortestRoute {
    private Set<Country> route;

    /**
     * When the currentRoute is shorter than the route, then set the currentRoute as the shortest route, otherwise do not set it.
     * @param currentRoute route to be set
     */
    @Synchronized
    public void setRoute(Set<Country> currentRoute) {
        if (isShorter(currentRoute)) {
            this.route = currentRoute;
        }
    }

    /**
     * Check whether currentRoute is shorter than the route
     * @param currentRoute route to be checked
     * @return if the currentRoute is shorter or not
     */
    public boolean isShorter(Set<Country> currentRoute) {
        return this.route == null || this.route.size() > currentRoute.size();
    }
}
