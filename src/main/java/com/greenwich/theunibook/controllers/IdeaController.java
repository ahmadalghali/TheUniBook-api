package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.services.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IdeaController {

    @Autowired
    IdeaService ideaService;

    @GetMapping("/ideas/page-count")
    public int calculateNumberOfPagesForIdeas() {
        return ideaService.calculateNumberOfPagesForIdeas();
    }


    @GetMapping("/ideas")
    public List<Idea> getIdeas() {
        return ideaService.getIdeas();
    }

    @GetMapping("/ideas/{departmentId}")
    public List<Idea> getIdeasByDepartment(@PathVariable("departmentId") int departmentId) {
        return ideaService.getIdeasByDepartment(departmentId);
    }

    @GetMapping("/ideas/page={page}")
    public List<Idea> getIdeasPaginated(@PathVariable("page") int page) {
        return ideaService.getIdeasPaginated(page);
    }


    @PostMapping("/ideas")
    public HashMap<String, Object> addIdea(@RequestBody Idea idea) {  //HashMap<Idea, String>
        return ideaService.addIdea(idea);
    }


}
