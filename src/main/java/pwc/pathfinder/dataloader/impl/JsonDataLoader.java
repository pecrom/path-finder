package pwc.pathfinder.dataloader.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pwc.pathfinder.common.dto.Country;
import pwc.pathfinder.common.dto.Region;
import pwc.pathfinder.dataloader.DataLoader;
import pwc.pathfinder.dataloader.impl.dto.JsonCountry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JsonDataLoader implements DataLoader {

    private final ObjectMapper jsonMapper;

    @Setter
    @Value("${pwc.pathfinder.countriesPath}")
    private Resource countriesData;

    private Map<String, Country> countries;

    private Map<String, Region> regions;

    @PostConstruct
    public void init() throws IOException {
        try (InputStream dataStream = countriesData.getInputStream()) {
            List<JsonCountry> jsonCountries = parseJson(dataStream);
            initCountryAndRegionsMap(jsonCountries);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Country getCountry(String countryCode) {
        return countries.get(countryCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Country> getAllCountries() {
        return countries.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Region getRegion(String region) {
        return regions.get(region);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNotCountryCodeExists(String countryCode) {
        return !countries.containsKey(countryCode);
    }


    private void initCountryAndRegionsMap(List<JsonCountry> jsonCountries) {
        countries = CollectionUtils.emptyIfNull(jsonCountries).parallelStream()
                .map(this::createCountry)
                .collect(Collectors.toMap(Country::getCountryCode, Function.identity()));

        regions = new HashMap<>();

        Country country;
        Country neighbour;
        for(JsonCountry jsonCountry : jsonCountries) {
            country = countries.get(jsonCountry.getCountryCode());

            // fill neighbours and regions
            for(String border : jsonCountry.getBorders()) {
                neighbour = countries.get(border);
                country.addNeighbour(neighbour);

                addRegionConnection(country, neighbour);
            }

        }
    }

    private void addRegionConnection(Country country, Country neighbour) {
        if (isNotSameRegion(country, neighbour)) {
            getOrCreateRegion(country.getRegion())
                    .addConnectedRegion(getOrCreateRegion(neighbour.getRegion()));
        }
    }

    private Region getOrCreateRegion(String region) {
        return regions.computeIfAbsent(region, Region::new);
    }

    private boolean isNotSameRegion(Country country, Country neighbour) {
        return !country.getRegion().equals(neighbour.getRegion());
    }

    private Country createCountry(JsonCountry country) {
        return new Country(country.getCountryCode(), country.getRegion());
    }

    private List<JsonCountry> parseJson(InputStream dataStream) throws IOException {
        CollectionType javaType = jsonMapper.getTypeFactory().constructCollectionType(List.class, JsonCountry.class);

        return jsonMapper.readValue(dataStream, javaType);
    }

}
