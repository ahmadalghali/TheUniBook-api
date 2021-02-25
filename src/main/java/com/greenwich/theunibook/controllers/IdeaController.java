package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.services.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IdeaController {

    @Autowired
    IdeaService ideaService;

    @GetMapping("/ideas")
    public List<Idea> getIdeas() {
        return ideaService.getIdeas();
    }


    @PostMapping("/ideas")
    public HashMap<String, Object> addIdea(@RequestBody Idea idea) {  //HashMap<Idea, String>
        return ideaService.addIdea(idea);
    }

}
