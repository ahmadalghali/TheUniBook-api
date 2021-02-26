package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.IdeaRepository;
import com.greenwich.theunibook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IdeaService {

    @Autowired
    IdeaRepository ideaRepository;

    @Autowired
    UserRepository userRepository;


    public List<Idea> getIdeas() {

        return ideaRepository.getIdeas();
    }

    public HashMap<String, Object> addIdea(Idea idea) {

        HashMap<String, Object> addIdeaResponse = new HashMap();

        try {


            User ideaAuthor = userRepository.findById(idea.getUserId()).get();

            idea.setDate(new Date(System.currentTimeMillis()));
            idea.setStatusId(1);
            idea.setDepartmentId(ideaAuthor.getDepartmentId());

            Idea savedIdea = ideaRepository.save(idea);

            addIdeaResponse.put("idea", savedIdea);
            addIdeaResponse.put("message", "added");
//            addIdeaResponse.put("ideaAuthor", ideaAuthor);


        } catch (Exception e) {

            e.printStackTrace();
            addIdeaResponse.put("data", idea);
            addIdeaResponse.put("message", "failed");
        }
        return addIdeaResponse;

    }

    public List<Idea> getIdeasByDepartment(int departmentId) {

        return ideaRepository.getIdeasByDepartment(departmentId);
    }

    public List<Idea> getIdeasPaginated(int page) {
        PageRequest pageWithFiveIdeas = PageRequest.of(page - 1, 5);

        Page<Idea> fiveIdeasPage = ideaRepository.findAll(pageWithFiveIdeas);

        List<Idea> fiveIdeas = fiveIdeasPage.getContent();

        return fiveIdeas;
    }

    public int calculateNumberOfPagesForIdeas() {

        int totalIdeasCount = getIdeas().size();

        int numberOfPages = totalIdeasCount / 5;

        if (totalIdeasCount % 5 > 0) {
            numberOfPages += 1;
        }

        return numberOfPages;

    }
}
