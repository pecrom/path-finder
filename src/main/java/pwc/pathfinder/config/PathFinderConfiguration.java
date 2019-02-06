package pwc.pathfinder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Configuration
public class PathFinderConfiguration {

    @Value("${pwc.pathfinder.countriesPath}")
    private String countriesDataPath;

    @Bean
    public InputStream getCountriesDataFile() throws FileNotFoundException {
        return new FileInputStream(ResourceUtils.getFile(countriesDataPath));
    }
}
