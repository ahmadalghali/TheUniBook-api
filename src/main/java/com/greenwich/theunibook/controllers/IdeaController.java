package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.dto.IdeaDTO;
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

    @GetMapping("/ideas")
    public HashMap<String, Object> getIdeas(@RequestParam int departmentId, @RequestParam int page) {
        return ideaService.getIdeasByDepartmentPaginated(departmentId, page);
    }

    @PostMapping("/ideas")
    public HashMap<String, Object> addIdea(@RequestBody Idea idea) {  //HashMap<Idea, String>
        return ideaService.addIdea(idea);
    }


}
