package pwc.pathfinder.web.rest;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pwc.pathfinder.web.rest.dto.RouteDto;
import pwc.pathfinder.web.rest.exception.CountryCodeNotFound;
import pwc.pathfinder.web.rest.exception.RouteNotFoundException;
import pwc.pathfinder.web.service.PathFindingService;

import java.util.Collection;

@RestController
@Validated
@RequestMapping(value = Constants.ROUTING_PATH)
public class RoutingRestController {

    @Autowired
    private PathFindingService pathFindingService;

    @GetMapping(path = Constants.FIND_ROUTE_PATH)
    public RouteDto findRoute(@PathVariable @Length(min = 3, max = 3) String origin, @PathVariable @Length(min = 3, max = 3) String destination) {
        String originUpper = origin.toUpperCase();
        String destinationUpper = destination.toUpperCase();

        if (pathFindingService.isNotCountryCodeExists(originUpper)) {
            throw new CountryCodeNotFound(origin);
        }

        if (pathFindingService.isNotCountryCodeExists(destinationUpper)) {
            throw new CountryCodeNotFound(destination);
        }

        Collection<String> route = pathFindingService.findShortestRoute(originUpper, destinationUpper);

        if (route.isEmpty()) {
            throw new RouteNotFoundException();
        }

        return new RouteDto(route);
    }
}
