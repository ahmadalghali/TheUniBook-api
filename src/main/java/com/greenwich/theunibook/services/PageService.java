package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Pages;
import com.greenwich.theunibook.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PageService {

    @Autowired
    PageRepository pageRepository;

    public List<Pages> getMostViewedPages(){
        List<Pages> mostViewedPages = pageRepository.getAllPagesByViews();
        return mostViewedPages;
    }

    public HashMap<String, Object> addPageView(int pageId){
        HashMap<String, Object> addViewResponse = new HashMap<>();
        Pages page = pageRepository.findById(pageId).get();
        try {
            if (page != null){
                page.setViews(page.getViews() + 1);
                pageRepository.save(page);
                addViewResponse.put("message", "view added to page " + page.getName());
            }
            else{


            }
        }catch (Exception e){
            e.printStackTrace();
            addViewResponse.put("message", "failed");
        }

        return addViewResponse;
    }
}
