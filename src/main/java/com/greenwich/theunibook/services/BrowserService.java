package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Browser;
import com.greenwich.theunibook.repository.BrowserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
public class BrowserService {

    @Autowired
    BrowserRepository browserRepository;


    public HashMap<String, Object> addBrowser(String browserName) {
        HashMap<String, Object> addBrowserResponse = new HashMap<>();

        Browser browser = browserRepository.findByName(browserName);

        try{
            if (browser != null){
                browser.setTimes_used(browser.getTimes_used() + 1);
                browserRepository.save(browser);
                addBrowserResponse.put("message", "added use count");
                addBrowserResponse.put("browser name", browser);

            }
            else{
                Browser newBrowser = new Browser(browserName, 1);
                browserRepository.save(newBrowser);
                addBrowserResponse.put("message", "browser has been added");
                addBrowserResponse.put("browser", newBrowser);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return addBrowserResponse;
    }

    public List<Browser> getMostUsedBrowsers(){
        List<Browser> mostUsedBrowsers = browserRepository.getAllBrowsers();
        return mostUsedBrowsers;
    }

}
