package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.services.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IdeaController {

    @Autowired
    IdeaService ideaService;

    @GetMapping("/ideas")
    public List<Idea> getIdeas() {
        return ideaService.getIdeas();
    }
}
