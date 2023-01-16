package pwc.pathfinder.web.rest;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pwc.pathfinder.dataloader.DataLoader;
import pwc.pathfinder.web.rest.dto.RouteDto;
import pwc.pathfinder.web.rest.exception.CountryCodeNotFound;
import pwc.pathfinder.web.rest.exception.RouteNotFoundException;
import pwc.pathfinder.web.service.PathFindingService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = Constants.ROUTING_PATH)
public class RoutingRestController {

    private final PathFindingService pathFindingService;
    private final DataLoader dataLoader;

    @GetMapping(path = Constants.FIND_ROUTE_PATH)
    public RouteDto findRoute(@PathVariable @Size(min = 3, max = 3) String origin, @PathVariable @Size(min = 3, max = 3) String destination) {
        String originUpper = origin.toUpperCase();
        String destinationUpper = destination.toUpperCase();

        if (dataLoader.isNotCountryCodeExists(originUpper)) {
            throw new CountryCodeNotFound(origin);
        }

        if (dataLoader.isNotCountryCodeExists(destinationUpper)) {
            throw new CountryCodeNotFound(destination);
        }

        Collection<String> route = originUpper.equals(destinationUpper) ? List.of(originUpper) : pathFindingService.findShortestRoute(originUpper, destinationUpper);

        if (CollectionUtils.isEmpty(route)) {
            throw new RouteNotFoundException();
        }

        return new RouteDto(route);
    }
}
