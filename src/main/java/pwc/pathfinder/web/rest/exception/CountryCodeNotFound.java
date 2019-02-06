package pwc.pathfinder.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CountryCodeNotFound extends RuntimeException {
    private static final String REASON = "Country code not found: ";

    public CountryCodeNotFound(String countryCode) {
        super(REASON + countryCode);
    }
}
