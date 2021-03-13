package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.dto.IdeaDTO;
import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.services.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class IdeaController {

    @Autowired
    IdeaService ideaService;


//    @GetMapping("/ideas")
//    public HashMap<String, Object> getIdeas(@RequestParam int departmentId, @RequestParam int page,
//                                            @RequestParam(required = false) Integer categoryId,
//                                            @RequestParam(required = false) int loggedInUser) {
//        if (categoryId == null) {
//            return ideaService.getIdeasByDepartmentPaginated(departmentId, page, loggedInUser);
//        } else {
//            return ideaService.sortIdeasByCategoryPaginated(departmentId, page, categoryId, loggedInUser);
//        }
//    }

    @GetMapping("/ideas/{ideaId}")
    public IdeaDTO getIdea(@PathVariable("ideaId") int ideaId) {
        return ideaService.getIdea(ideaId);
    }

    @PutMapping("/ideas/{ideaId}/incrementViews")
    public void incrementIdeaViews(@PathVariable("ideaId") int ideaId) {
        ideaService.incrementViews(ideaId);
    }

    @GetMapping("/ideas")
    public HashMap<String, Object> getIdeas(@RequestParam int departmentId, @RequestParam int page,
                                            @RequestParam(required = false) String categoryId,
                                            @RequestParam(required = false) Integer loggedInUser,
                                            @RequestParam(required = false) String sortBy) {

        return ideaService.getIdeas(departmentId, page, loggedInUser, categoryId, sortBy);
    }


    @PostMapping("/ideas")
    public HashMap<String, Object> addIdea(@ModelAttribute Idea idea) {  //HashMap<Idea, String>
        return ideaService.addIdea(idea);
    }

    @GetMapping("/ideas/downloadFile")
    public ResponseEntity<Object> downloadFile(@RequestParam String documentPath) throws FileNotFoundException {
        return ideaService.downloadFile(documentPath);

    }


    @GetMapping("/ideas/statistics")
    public HashMap<String, Object> getStatistics() {
        return ideaService.getStatistics();
    }




    @GetMapping("/ideas/downloadAllIdeas")
    public void downloadAllIdeasCSV(HttpServletResponse response) throws IOException {
        ideaService.downloadAllIdeasCSV(response);

    }

    @GetMapping(value="//ideas/downloadAllDocuments")
    public void zipFiles(HttpServletResponse response) throws IOException {
        ideaService.downloadAllDocuments(response);

    }

    }
