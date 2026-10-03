package com.deve.restaurantreviews.restaurant;

import com.deve.restaurantreviews.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RestaurantControllerTest {

    private static final String VALID_REQUEST = """
            {
              "name": "Trinity",
              "description": "Burgeradiko",
              "priceRange": 2,
              "address": "Navarinou",
              "city": "Thessaloniki",
              "neighborhood": "Kentro"
            }
            """;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        restaurantRepository.deleteAll();
    }

    @Test
    void createReturns201AndRestaurantCanBeRetrieved() throws Exception{
        MvcResult result = mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Trinity"))
                .andExpect(jsonPath("$.ratingCount").value(0))
                .andReturn();
        String location = result.getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Trinity"))
                .andExpect(jsonPath("$.neighborhood").value("Kentro"));
    }

    @Test
    void createWithBlankNameReturns400WithFieldError() throws Exception {
        String invalidRequest = """
                {
                  "name": "   ",
                  "priceRange": 2,
                  "address": "Main Street 14",
                  "city": "Test City",
                  "neighborhood": "Center"
                }
                """;

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("name"));

        assertThat(restaurantRepository.count()).isZero();
    }

    @Test
    void getUnknownRestaurantReturns404() throws Exception {
        mockMvc.perform(get("/api/restaurants/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(containsString("999999")));
    }

    @Test
    void updatePersistsChangesInDatabase() throws Exception {
        Restaurant existing = restaurantRepository.save(
                new Restaurant("Old name", 1, "Old Street 1", "Test City", "Port"));

        mockMvc.perform(put("/api/restaurants/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Trinity"));

        Restaurant reloaded = restaurantRepository.findById(existing.getId()).orElseThrow();
        assertThat(reloaded.getName()).isEqualTo("Trinity");
        assertThat(reloaded.getPriceRange()).isEqualTo(2);
        assertThat(reloaded.getNeighborhood()).isEqualTo("Kentro");
        assertThat(reloaded.getVersion()).isEqualTo(1L);
    }

    @Test
    void listReturnsPagedResults() throws Exception {
        restaurantRepository.save(new Restaurant("First", 1, "Street 1", "Test City", "Center"));
        restaurantRepository.save(new Restaurant("Second", 2, "Street 2", "Test City", "Center"));

        mockMvc.perform(get("/api/restaurants").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(2));
    }
}
