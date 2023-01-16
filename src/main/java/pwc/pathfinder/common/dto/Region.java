package pwc.pathfinder.common.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class Region {

    private final String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final Set<Region> connectedRegions = new HashSet<>();

    public void addConnectedRegion(Region region) {
        connectedRegions.add(region);
    }

}
