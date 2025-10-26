package com.example.hello_spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfiguration
class MessageIntegrationTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRecentMessagesEndpoint() throws Exception {
        mockMvc.perform(get("/chat/messages/recent"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].author").value("System"))
               .andExpect(jsonPath("$[0].content").value("Добро пожаловать в игру Крестики-Нолики!"));
    }

    @Test
    void createMessageEndpoint() throws Exception {
        mockMvc.perform(post("/chat/messages")
               .param("author", "TestUser")
               .param("content", "Test message"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value("TestUser"))
               .andExpect(jsonPath("$.content").value("Test message"));
    }

    @Test
    void createMessageWithoutAuthor() throws Exception {
        mockMvc.perform(post("/chat/messages")
               .param("content", "Anonymous message"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value("Аноним"))
               .andExpect(jsonPath("$.content").value("Anonymous message"));
    }

    @Test
    void createEmptyMessage() throws Exception {
        mockMvc.perform(post("/chat/messages")
               .param("author", "TestUser")
               .param("content", ""))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").value("Сообщение не может быть пустым"));
    }
}
