package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.Category;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.Report;
import com.greenwich.theunibook.repository.IdeaRepository;
import com.greenwich.theunibook.repository.ReportRepository;
import com.greenwich.theunibook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    public HashMap<String, Object> reportIdea(int userId, int ideaId, int reportId) {
        HashMap<String, Object> reportIdeaResponse = new HashMap<>();
        Report reportedIdeas = reportRepository.exists(userId, ideaId);
        try{
            if(reportedIdeas != null){
                if (reportId == 5){
                    removeIdeaReport(userId, ideaId);
                    reportIdeaResponse.put("message", "Report has been removed");
                }
                else{
                reportRepository.updateReportedIdea(reportId, userId, ideaId);
                reportIdeaResponse.put("message", "Idea report has been updated");
                }
            }
            else {
                reportRepository.save(new Report(userId, ideaId, reportId));
                reportIdeaResponse.put("message", "idea has been reported");
            }
        }catch (Exception e){
            e.printStackTrace();
            reportIdeaResponse.put("message", "failed");
        }

        return reportIdeaResponse;
    }

    public void removeIdeaReport(int userId, int ideaId){
        reportRepository.removeIdeaReport(userId, ideaId);
    }

}
