package br.com.fdrtec.repair_tips_api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fdrtec.repair_tips_api.dto.EquipamentRequest;
import br.com.fdrtec.repair_tips_api.dto.PartRequest;
import br.com.fdrtec.repair_tips_api.repository.EquipamentRepository;
import br.com.fdrtec.repair_tips_api.repository.PartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class EquipamentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EquipamentRepository equipamentRepository;

    @Autowired
    private PartRepository partRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        equipamentRepository.deleteAll();
        partRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAndRetrieveEquipamentWithParts() throws Exception {
        var partResult = mockMvc.perform(post("/api/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PartRequest("Black toner", "HP-56A"))))
            .andExpect(status().isCreated())
            .andReturn();

        var partId = objectMapper.readTree(partResult.getResponse().getContentAsString()).get("id").asLong();
        var request = new EquipamentRequest(
            "HP LaserJet Pro M404dn",
            "HP",
            "PRINTER",
            "LASER",
            List.of(partId)
        );

        var modelResult = mockMvc.perform(post("/api/equipaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("HP LaserJet Pro M404dn")))
            .andExpect(jsonPath("$.brand", is("HP")))
            .andExpect(jsonPath("$.category", is("PRINTER")))
            .andExpect(jsonPath("$.type", is("LASER")))
            .andExpect(jsonPath("$.parts", hasSize(1)))
            .andExpect(jsonPath("$.parts[0].number", is("HP-56A")))
            .andReturn();

        var modelId = objectMapper.readTree(modelResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/equipaments/{id}", modelId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is((int) modelId)))
            .andExpect(jsonPath("$.parts", hasSize(1)));
    }
}