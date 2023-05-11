package org.example.controller;

import com.atlassian.connect.spring.AtlassianHostUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Controller
public class HelloController {
    private final ProjectService projectService;

    @Autowired
    public HelloController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(value = "/hello-world")
    public ModelAndView greetPerson(@AuthenticationPrincipal AtlassianHostUser hostUser) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(hostUser.getHost().getDescription());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        modelAndView.setViewName("hello");
        return modelAndView;
    }

    @GetMapping(path = "/demo")
    public String getData(@AuthenticationPrincipal Object principal, Model model) {
        model.addAttribute("principal", Optional.ofNullable(principal).orElse("anonymous"));
        model.addAttribute("getProjectsAsAddon", projectService.getProjectsAsAddon());
        model.addAttribute("getFirstProjectAsAddon", projectService.getFirstProjectAsAddon());
        return "demo";
    }
}
