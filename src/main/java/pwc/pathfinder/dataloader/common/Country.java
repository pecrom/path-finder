package pwc.pathfinder.dataloader.common;

import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Country {

    private final String countryCode;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Country> neighbours = new HashSet<>();

    /**
     * Add neighbour country
     * @param neighbour neighbour country
     */
    public void addNeighbour(Country neighbour) {
        neighbours.add(neighbour);
    }

}
