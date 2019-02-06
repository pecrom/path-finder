package pwc.pathfinder.web.rest;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pwc.pathfinder.web.service.PathFindingService;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RoutingRestController.class)
public class RoutingRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PathFindingService pathFindingService;

    @Test
    @DisplayName("Check not found origin")
    public void notFoundOrigin() throws Exception {
        when(pathFindingService.isNotCountryCodeExists("ABC")).thenReturn(true);

        mvc.perform(get(Constants.ROUTING_PATH + "/ABC/DEF")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Check not found destination")
    public void notFoundDestination() throws Exception {

        when(pathFindingService.isNotCountryCodeExists("ABC")).thenReturn(false);
        when(pathFindingService.isNotCountryCodeExists("DEF")).thenReturn(true);

        mvc.perform(get(Constants.ROUTING_PATH + "/ABC/DEF")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Check not found route")
    public void notFoundRoute() throws Exception {

        when(pathFindingService.isNotCountryCodeExists(anyString())).thenReturn(false);
        when(pathFindingService.findShortestRoute(anyString(), anyString())).thenReturn(Collections.emptyList());

        mvc.perform(get(Constants.ROUTING_PATH + "/ABC/DEF")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Check found route")
    public void foundRoute() throws Exception {

        LinkedList<String> returnedRoute = new LinkedList<>();
        returnedRoute.add("ABC");
        returnedRoute.add("XYZ");
        returnedRoute.add("DEF");

        when(pathFindingService.isNotCountryCodeExists(anyString())).thenReturn(false);
        when(pathFindingService.findShortestRoute(anyString(), anyString())).thenReturn(returnedRoute);

        mvc.perform(get(Constants.ROUTING_PATH + "/ABC/DEF")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("route", hasSize(3)))
                .andExpect(jsonPath("route", equalTo(returnedRoute)));
    }

}
