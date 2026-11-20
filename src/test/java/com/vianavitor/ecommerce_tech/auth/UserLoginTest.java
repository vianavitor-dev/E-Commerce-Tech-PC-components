package com.vianavitor.ecommerce_tech.auth;

import com.vianavitor.ecommerce_tech.aux.TestcontainerClass;
import com.vianavitor.ecommerce_tech.auth.dtos.request.UserRegisterFormsDTO;
import com.vianavitor.ecommerce_tech.auth.entities.User;
import com.vianavitor.ecommerce_tech.auth.services.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserLoginTest extends TestcontainerClass {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserService service;

    private Integer userId;

    @BeforeEach
    void setUp() {
        var userForms = new UserRegisterFormsDTO("test1", "test1@email.com", "123");
        service.createNew(userForms);
        userId = service.findByEmail(userForms.email()).getId();
    }

    @AfterEach
    void tearDown() {
        if (userId == null) {
            return;
        }

        transactionTemplate.executeWithoutResult(status -> {
            var result = entityManager.find(User.class, userId);

            if (result != null) {
                entityManager.remove(result);
                entityManager.flush();
            }
        });
    }

    @RepeatedTest(3)
    public void testLogin__whenSuccessful() throws Exception {
        mockMvc.perform(
                post("http://localhost:8081/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"email\": \"test1@email.com\",\n" +
                                "    \"password\": \"123\"\n" + "}") )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testLogin__whenInvalidEmailOrPassword() throws Exception {
        mockMvc.perform(
                post("http://localhost:8081/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"email\": \"test1@email.com\",\n" +
                                "    \"password\": \"123322\"\n" +
                                "}") )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid e-mail or password"));
    }
}

