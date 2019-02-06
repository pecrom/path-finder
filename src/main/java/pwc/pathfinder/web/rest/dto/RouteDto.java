package pwc.pathfinder.web.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class RouteDto implements Serializable {

    /**
     * Route from origin to destination
     */
    private Collection<String> route;
}
