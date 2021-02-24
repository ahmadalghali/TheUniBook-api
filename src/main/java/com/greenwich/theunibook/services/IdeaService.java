package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IdeaService {

    @Autowired
    IdeaRepository ideaRepository;


    public List<Idea> getIdeas() {

        List<Idea> ideas = new ArrayList<>();
        ideaRepository.findAll().forEach(ideas::add);

        return ideas;
    }

}
