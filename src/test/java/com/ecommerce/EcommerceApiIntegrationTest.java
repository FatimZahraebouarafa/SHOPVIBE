package com.ecommerce;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.model.Article;
import com.ecommerce.model.User;
import com.ecommerce.repository.ArticleRepository;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EcommerceApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
        articleRepository.save(new Article("Test Smartphone", "Samsung", 499.99, 10));

        User user = new User("test-user", passwordEncoder.encode("password123"));
        userRepository.save(user);
    }

    @Test
    void catalogueApiReturnsAvailableArticles() throws Exception {
        mockMvc.perform(get("/articles").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Test Smartphone")));
    }

    @Test
    void articleDetailApiReturnsArticle() throws Exception {
        Integer articleId = articleRepository.findAll().get(0).getIdArticle();

        mockMvc.perform(get("/articles/" + articleId))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Test Smartphone")));
    }

    @Test
    void protectedCartApiRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/cart"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    void loginApiAuthenticatesValidUser() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "test-user")
                .param("password", "password123")
                .with(csrf()))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    void loginApiRejectsRequestWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "test-user")
                .param("password", "password123"))
            .andExpect(status().isForbidden());
    }
}
