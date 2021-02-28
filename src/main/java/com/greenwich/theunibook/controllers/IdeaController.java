package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.dto.IdeaDTO;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.services.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IdeaController {

    @Autowired
    IdeaService ideaService;

    @GetMapping("/ideas")
    public HashMap<String, Object> getIdeas(@RequestParam int departmentId, @RequestParam int page, @RequestParam(required=false) Integer categoryId) {
        if (categoryId == null){
            return ideaService.getIdeasByDepartmentPaginated(departmentId, page);
        }
        else{
            return ideaService.sortIdeasByCategoryPaginated(departmentId, page, categoryId);
        }
    }

//    @PostMapping("/ideas")
//    public HashMap<String, Object> addIdea(@RequestBody Idea idea) {  //HashMap<Idea, String>
//        return ideaService.addIdea(idea);
//    }

    @PostMapping("/ideas")
    public HashMap<String, Object> addIdea(@ModelAttribute Idea idea) {  //HashMap<Idea, String>
        return ideaService.addIdea(idea);
    }

//    @PostMapping("/fileUpload")
//    public String uploadFile(@RequestParam MultipartFile file) {  //HashMap<Idea, String>
//        return ideaService.uploadFile(file);
//    }

    @GetMapping("/ideas/downloadFile")
    public Resource downloadFile(@RequestParam String documentPath) {
        return ideaService.downloadFile(documentPath);
    }
}
