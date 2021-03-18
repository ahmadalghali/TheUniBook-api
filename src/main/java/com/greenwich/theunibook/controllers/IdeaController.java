package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.dto.IdeaDTO;
import com.greenwich.theunibook.enums.UserRole;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.services.IdeaService;
import com.greenwich.theunibook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@RestController
public class IdeaController {

    @Autowired
    IdeaService ideaService;

    @Autowired
    UserService userService;

    @GetMapping("/ideas/anonymous")
    public HashMap<String, Object> getAnonymousIdeas(@RequestParam String email, @RequestParam String password) {

        HashMap<String, Object> response = new HashMap<>();

        if (userService.isAuthorized(email, password, UserRole.ADMINISTRATOR)) {

            response.put("anonymousIdeas", ideaService.getAnonymousIdeas());
            response.put("authorized", true);

        } else {
            response.put("message", "User has no access to this data");
            response.put("authorized", false);
        }

        return response;
    }


    @GetMapping("/ideas/{ideaId}")
    public IdeaDTO getIdea(@PathVariable("ideaId") int ideaId) {
        return ideaService.getIdea(ideaId);
    }

    @PutMapping("/ideas/{ideaId}/incrementViews")
    public void incrementIdeaViews(@PathVariable("ideaId") int ideaId) {
        ideaService.incrementViews(ideaId);
    }

    @GetMapping("/ideas")
    public HashMap<String, Object> getIdeas(@RequestParam int page,
                                            @RequestParam(required = false) String categoryId,
                                            @RequestParam String email,
                                            @RequestParam String password,
                                            @RequestParam(required = false) String sortBy) {

        return ideaService.getIdeas(page, email, password, categoryId, sortBy);
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

    @GetMapping(value = "/ideas/downloadAllDocuments")
    public void zipFiles(HttpServletResponse response) throws IOException {
        ideaService.downloadAllDocuments(response);

    }

    @PostMapping("/ideas/setClosureDate")
    public String setIdeaClosureDate(@RequestParam String email, @RequestParam String password, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate closureDate) {
        if (userService.isAuthorized(email, password, UserRole.ADMINISTRATOR)) {
            return ideaService.setIdeaClosureDate(closureDate);
        }
        return "unauthorised access";
    }

    @GetMapping("/ideas/closureDate")
    public String getClosureDate() {
        return ideaService.getClosureDate();
    }

//    @PostMapping("/ideas/setIdeaClosureDate")
////    public String setIdeaClosureDate(@RequestParam(value="fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
////                                     @RequestParam(value="toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
////
////        return ideaService.setIdeaClosureDate(fromDate, toDate);
////    }




}
