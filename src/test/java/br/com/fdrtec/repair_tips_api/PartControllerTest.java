package br.com.fdrtec.repair_tips_api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fdrtec.repair_tips_api.dto.PartRequest;
import br.com.fdrtec.repair_tips_api.repository.PartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class PartControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PartRepository repository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAndRetrievePart() throws Exception {
        var request = new PartRequest("Air filter", "12345");

        var createResult = mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/parts/")))
            .andExpect(jsonPath("$.name", is("Air filter")))
            .andExpect(jsonPath("$.number", is("12345")));

        var location = createResult.andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Air filter")))
            .andExpect(jsonPath("$.number", is("12345")));
    }

    @Test
    void shouldListPartsWithPagination() throws Exception {
        mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PartRequest("Part 1", "1"))))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PartRequest("Part 2", "2"))))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/parts").param("page", "0").param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void shouldUpdateAndDeletePart() throws Exception {
        var created = mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PartRequest("Original", "001"))))
            .andExpect(status().isCreated())
            .andReturn();

        var location = created.getResponse().getHeader("Location");

        mockMvc.perform(put(location)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PartRequest("Updated", "999"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Updated")))
            .andExpect(jsonPath("$.number", is("999")));

        mockMvc.perform(delete(location))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnProblemDetailsForNotFoundAndValidation() throws Exception {
        mockMvc.perform(get("/api/parts/999999"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("application/problem+json")))
            .andExpect(jsonPath("$.title", is("Not Found")));

        mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.valueOf("application/problem+json")))
            .andExpect(jsonPath("$.title", is("Validation failed")));
    }
}
