package pwc.pathfinder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class PathFinderConfiguration {

    @Value("${pwc.pathfinder.countriesPath}")
    private Resource countriesData;

    @Bean
    public InputStream getCountriesDataFile() throws IOException {
        return countriesData.getInputStream();
    }
}
