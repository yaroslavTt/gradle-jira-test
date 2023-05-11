package org.example.controller;

import com.atlassian.connect.spring.AtlassianHostRestClients;
import com.atlassian.connect.spring.AddonAuthenticationType;

import com.atlassian.connect.spring.AtlassianHostUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class ProjectService {

    private static final String JIRA_GET_PROJECTS = "/rest/api/3/project";
    private final AtlassianHostRestClients atlassianHostRestClients;
    private Project[] projects;

    @Autowired
    public ProjectService(AtlassianHostRestClients atlassianHostRestClients) {
        this.atlassianHostRestClients = atlassianHostRestClients;
    }

    public Project[] getProjectsAsAddon() {
        if (isOAuth2ClientCredentialAvailable()) {
            return getProjectsAsAddon(AddonAuthenticationType.OAUTH2);
        }

        return getProjectsAsAddon(AddonAuthenticationType.JWT);
    }

    public Project[] getProjectsAsAddon(AddonAuthenticationType auth) {
        this.projects = atlassianHostRestClients.authenticatedAsAddon(auth).getForObject(JIRA_GET_PROJECTS, Project[].class);
        return projects;
    }

    public Project getFirstProjectAsAddon() {
        if (this.projects == null) {
            return null;
        }
        return projects[0];
    }

    private boolean isOAuth2ClientCredentialAvailable() {

        Optional<AtlassianHostUser> optionalHostUser = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(AtlassianHostUser.class::isInstance)
                .map(AtlassianHostUser.class::cast);

        if (optionalHostUser.isPresent() ) {
            AtlassianHostUser hostUser = optionalHostUser.get();
            return this.atlassianHostRestClients.isClientCredentialsAvailable(hostUser.getHost());
        }

        return false;
    }
}

