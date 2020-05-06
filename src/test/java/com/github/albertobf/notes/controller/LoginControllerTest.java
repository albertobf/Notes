package com.github.albertobf.notes.controller;

import com.github.albertobf.notes.bootstrap.IniUserDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenAccessLoginUnauthenticated_thenReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void whenLoginValidCredentials_thenDoLogin() throws Exception {
        mockMvc.perform(formLogin().user(IniUserDatabase.USERNAME).password(IniUserDatabase.PASSWORD))
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername(IniUserDatabase.USERNAME));

    }

    @Test
    public void whenLoginInvalidCredentials_thenLoginDenied() throws Exception {
        String loginErrorUrl = "/login?error";
        mockMvc.perform(formLogin().password("invalid"))
                .andExpect(redirectedUrl(loginErrorUrl))
                .andExpect(unauthenticated());
    }

}