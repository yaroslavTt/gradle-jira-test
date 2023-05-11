package org.example.controller;

import com.atlassian.connect.spring.AtlassianHostRestClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    AtlassianHostRestClients clients;

    @Autowired
    public TestService(AtlassianHostRestClients clients) {
        this.clients = clients;
    }

    public void doSmth() {
        clients.authenticatedAsAddon();
    }
}
