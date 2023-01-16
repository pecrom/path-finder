package pwc.pathfinder.common.dto;

import lombok.*;

import java.util.Collection;
import java.util.HashSet;


@Data
@RequiredArgsConstructor
public class Country {

    private final String countryCode;

    private final String region;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final Collection<Country> neighbours = new HashSet<>();

    /**
     * Add neighbour country
     * @param neighbour neighbour country
     */
    public void addNeighbour(Country neighbour) {
        neighbours.add(neighbour);
    }

}
