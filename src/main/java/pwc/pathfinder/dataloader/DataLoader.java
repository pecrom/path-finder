package pwc.pathfinder.dataloader;

import pwc.pathfinder.common.dto.Country;
import pwc.pathfinder.common.dto.Region;

import java.util.Collection;

public interface DataLoader {

    /**
     * Get country specified by country code
     *
     * @param countryCode code of the country
     * @return {@link Country} found country
     */
    Country getCountry(String countryCode);

    /**
     * Get all countries
     *
     * @return {@link Collection<Country>} all countries
     */
    Collection<Country> getAllCountries();

    /**
     * Get region specified by region name
     *
     * @param region region name
     * @return {@link Region} found region
     */
    Region getRegion(String region);


    /**
     * Check existence of country code
     *
     * @param countryCode country code
     * @return true / false
     */
    boolean isNotCountryCodeExists(String countryCode);
}
