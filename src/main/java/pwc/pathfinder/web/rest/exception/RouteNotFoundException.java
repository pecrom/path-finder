package pwc.pathfinder.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Route not found")
public class RouteNotFoundException extends RuntimeException{
}
