package pwc.pathfinder.dataloader.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonCountry {

    private String countryCode;

    private List<String> borders;

    private String region;

    @JsonCreator
    public JsonCountry(@JsonProperty(value = "cca3", required = true) String countryCode) {
        this.countryCode = countryCode;
    }
}
