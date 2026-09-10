package br.com.fdrtec.repair_tips_api;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class OpenApiDocumentationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldExposeDocumentedApiContract() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.info.title", is("Repair Tips API")))
            .andExpect(jsonPath("$.info.version", is("1.0.0")))
            .andExpect(jsonPath("$.paths", hasKey("/api/parts")))
            .andExpect(jsonPath("$.paths", hasKey("/api/parts/{id}")))
            .andExpect(jsonPath("$.paths", hasKey("/api/equipaments")))
            .andExpect(jsonPath("$.paths", hasKey("/api/equipaments/{id}")))
            .andExpect(jsonPath("$.paths['/api/parts'].post.operationId", is("createPart")))
            .andExpect(jsonPath("$.paths['/api/parts'].post.responses", hasKey("201")))
            .andExpect(jsonPath("$.paths['/api/parts'].post.responses['201'].headers", hasKey("Location")))
            .andExpect(jsonPath("$.paths['/api/parts'].get.parameters", hasSize(3)))
            .andExpect(jsonPath("$.components.schemas", hasKey("Part")))
            .andExpect(jsonPath("$.components.schemas.Part.required", hasItem("name")))
            .andExpect(jsonPath("$.components.schemas.Part.required", hasItem("number")))
            .andExpect(jsonPath("$.components.schemas", hasKey("Equipament")));
    }
}
