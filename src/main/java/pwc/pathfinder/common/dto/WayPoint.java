package pwc.pathfinder.common.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WayPoint {

    private final Country country;

    private final WayPoint previousWayPoint;
}
