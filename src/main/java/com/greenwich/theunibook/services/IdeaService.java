package com.greenwich.theunibook.services;

import com.greenwich.theunibook.dto.AnonymousIdeaDTO;
import com.greenwich.theunibook.dto.IdeaDTO;
import com.greenwich.theunibook.dto.UserDTO;
import com.greenwich.theunibook.models.Department;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class IdeaService {

    @Autowired
    IdeaRepository ideaRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserService userService;

    @Autowired
    private JavaMailSender sender;


    private ModelMapper modelMapper = new ModelMapper();


    public List<IdeaDTO> getAllIdeas() {

        return ideaRepository.getIdeas().stream()
                .map(this::convertToIdeaDTO)
                .collect(Collectors.toList());
    }

    public HashMap<String, Object> addIdea(Idea idea) {

        HashMap<String, Object> addIdeaResponse = new HashMap();

        try {
            User ideaAuthor = userRepository.findById(idea.getUserId()).get();

            if (!ideaAuthor.isEnabled()) {
                addIdeaResponse.put("message", "user account is disabled");
                return addIdeaResponse;
            }

            idea.setStatusId(1);
            idea.setDepartmentId(ideaAuthor.getDepartmentId());
            idea.setDate(LocalDateTime.now());

            //Save document if it exists
            if (idea.getDocument() != null) {
                String documentPath = uploadFile(idea.getDocument());
                if (documentPath != null) {
                    idea.setDocumentPath(documentPath);
                }
            }


            Idea savedIdea = ideaRepository.save(idea);

            //Email the QA Coordinator of the same department

            notifyQACoordinatorByEmail(idea);

            addIdeaResponse.put("idea", convertToIdeaDTO(savedIdea));
            addIdeaResponse.put("message", "added");
//            addIdeaResponse.put("ideaAuthor", ideaAuthor);

        } catch (Exception e) {

            e.printStackTrace();
            addIdeaResponse.put("data", idea);
            addIdeaResponse.put("message", "failed");
        }
        return addIdeaResponse;
    }


    public HashMap<String, Object> getIdeas(Integer departmentId, Integer page, Integer loggedInUser, String categoryId, String sortBy) {
        HashMap<String, Object> getIdeasResponse = new HashMap<>();
        List<Idea> ideas = new ArrayList<>();
        //test1

        if (categoryId.equals("any")) {
            categoryId = "%";
        }
        ideas = ideaRepository.getIdeas(departmentId, page, sortBy, categoryId);

        List<IdeaDTO> ideaDTOS = ideas
                .stream()
                .map(this::convertToIdeaDTO)
                .collect(Collectors.toList());

        if (sortBy.equals("most_popular")) {
            List<IdeaDTO> mostPopularIdeas = sortMostPopular(ideaDTOS);
            getIdeasResponse.put("ideas", mostPopularIdeas);

        } else {
            getIdeasResponse.put("ideas", ideaDTOS);
        }

        getIdeasResponse.put("pageCount", calculateNumberOfPagesBasedOnListSize(ideaRepository.countIdeasByDepartmentId(departmentId)));
        getIdeasResponse.put("likedIdeasByUser", ratingRepository.getLikedIdeasByUser(loggedInUser));
        getIdeasResponse.put("dislikedIdeasByUser", ratingRepository.getDislikedIdeasByUser(loggedInUser));

        return getIdeasResponse;

    }

    private List<IdeaDTO> sortMostPopular(List<IdeaDTO> ideas) {

        //Collections.sort(ideas, Comparator.comparingInt(IdeaDTO ::getScore));

        Collections.sort(ideas, (IdeaDTO idea1, IdeaDTO idea2) -> idea1.getScore() - idea2.getScore());
        Collections.reverse(ideas);
        return ideas;
    }


    public HashMap<String, Object> notifyQACoordinatorByEmail(Idea idea) {

        HashMap<String, Object> emailResponse = new HashMap<>();

        try {


            int QACoordinatorId = ideaRepository.getQACoordinatorId(idea.getDepartmentId());

            //Check if the commenter is the same author of the idea so you don't send an email to them
            if (idea.getUserId() == QACoordinatorId) {
                emailResponse.put("message", "email not sent - poster is the QA Coordinator himself");
                return emailResponse;
            }

            //Get the Email of the QA Coordinator
            String QACoordiantorEmail = userRepository.getQACoordinatorEmail(idea.getDepartmentId());
            String departmentName = departmentRepository.getDepartmentNameById(idea.getDepartmentId());
            String QACoordinatorName = userRepository.getQACoordinatorName(QACoordinatorId);
            String ideaTitle = ideaRepository.getIdeaTitle(idea.getId());

            EmailValidator emailValidator = EmailValidator.getInstance();
            if (emailValidator.isValid(QACoordiantorEmail)) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setFrom("theunibook1@gmail.com");
                mail.setTo(QACoordiantorEmail);
                mail.setSubject("Idea Added!");

                mail.setText("\n\n Hi, " + QACoordinatorName + "\n\nAn idea " + '"' + ideaTitle + '"' + "has been posted in the " + departmentName + " department. \n \n Click here to see it \nhttps://theunibook.netlify.app\n\n\nThanks,\nTheUniBook Team");
                this.sender.send(mail);

                emailResponse.put("message", "email sent");
            } else {
                emailResponse.put("message", "email invalid");
            }


        } catch (Exception e) {
            e.printStackTrace();
            emailResponse.put("message", "failed to send email");

        }

        return emailResponse;
    }

    private IdeaDTO convertToIdeaDTO(Idea idea) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        IdeaDTO ideaDTO = modelMapper.map(idea, IdeaDTO.class);


        User ideaAuthor = userRepository.findById(idea.getUserId()).get();
        if (idea.isAnonymous()) {
            ideaDTO.setAuthorName("Anonymous");
            ideaDTO.setAuthorPhoto(null);
        } else {
            ideaDTO.setAuthorName(ideaAuthor.getFirstname() + " " + ideaAuthor.getLastname());
            ideaDTO.setAuthorPhoto(ideaAuthor.getProfileImageUrl());

        }


        ideaDTO.setLikes(ratingRepository.getIdeaLikes(idea.getId()));
        ideaDTO.setDislikes(ratingRepository.getIdeaDislikes(idea.getId()));

        int likeCount = ideaDTO.getLikes();
        int dislikeCount = ideaDTO.getDislikes();

        int score = likeCount - dislikeCount;
        ideaDTO.setScore(score);


        return ideaDTO;
    }

    private AnonymousIdeaDTO convertToAnonymousIdeaDTO(Idea idea) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        AnonymousIdeaDTO anonymousIdeaDTO = modelMapper.map(idea, AnonymousIdeaDTO.class);


        User ideaAuthor = userRepository.findById(idea.getUserId()).get();

        anonymousIdeaDTO.setAuthorName(ideaAuthor.getFirstname() + " " + ideaAuthor.getLastname());

        anonymousIdeaDTO.setLikes(ratingRepository.getIdeaLikes(idea.getId()));
        anonymousIdeaDTO.setDislikes(ratingRepository.getIdeaDislikes(idea.getId()));
        anonymousIdeaDTO.setEmail(ideaAuthor.getEmail());
        anonymousIdeaDTO.setAuthorPhoto(ideaAuthor.getProfileImageUrl());

        return anonymousIdeaDTO;
    }

    public int calculateNumberOfPagesBasedOnListSize(int itemCount) {


        int numberOfPages = itemCount / 5;

        if (itemCount % 5 > 0) {
            numberOfPages += 1;
        }

        return numberOfPages;
    }

    public String uploadFile(MultipartFile file) {

        String destinationFilename = "./uploads/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            Path path = Paths.get(destinationFilename);
            Files.copy(file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING);

            return destinationFilename;

        } catch (IOException e) {

            return null;
        }


    }

    public ResponseEntity<Object> downloadFile(String documentPath) throws FileNotFoundException {


        File file = new File(documentPath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                String.format("attachment; filename=\"%s\"", file.getName()));
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/txt")).body(resource);

        return responseEntity;
    }


    public IdeaDTO getIdea(int ideaId) {
        return convertToIdeaDTO(ideaRepository.findById(ideaId).get());
    }

    public void incrementViews(int ideaId) {
        Idea idea = ideaRepository.findById(ideaId).get();
        idea.setViews(idea.getViews() + 1);
        ideaRepository.save(idea);
    }


    public HashMap<String, Object> getStatistics() {

        HashMap<String, Object> statistics = new HashMap<>();

        List<Department> departmentsList = departmentRepository.getAll();

//        HashMap<String, Object> departments = new HashMap<>();

        List<Object> departments = new ArrayList<>();


        for (Department department : departmentsList) {
            HashMap<String, Object> _department = new HashMap<>();

            _department.put("id", department.getId());
            _department.put("name", department.getName());
            _department.put("ideaCount", ideaRepository.countIdeasByDepartmentId(department.getId()));
            _department.put("contributors", ideaRepository.getContributorsPerDepartment(department.getId()));

            departments.add(_department);
        }

        statistics.put("departments", departments);
        statistics.put("numOfIdeasWithNoComments", ideaRepository.getNumberOfIdeasWithNoComments());
        statistics.put("numOfAnonymousIdeas", ideaRepository.countAllByAnonymousTrue());
        statistics.put("totalNumOfComments", commentRepository.numOfComments());


        return statistics;
    }


    public void downloadAllIdeasCSV(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        String fileName = "ideas.csv";

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;

        response.setHeader(headerKey, headerValue);

        List<Idea> ideasList = ideaRepository.getIdeas();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"idea ID", "Idea Title", "Idea Description", "Date", "Author ID", "Category ID", "Idea Status ID", "Department ID", "Anonymity", "Idea Document Path", "Number of Views"};
        String[] nameMapping = {"id", "title", "description", "date", "userId", "categoryId", "statusId", "departmentId", "anonymous", "documentPath", "views"};

        csvWriter.writeHeader(csvHeader);

        for (Idea idea : ideasList) {
            csvWriter.write(idea, nameMapping);
        }

        csvWriter.close();


    }

    public void downloadAllDocuments(HttpServletResponse response2) throws IOException {

        //setting headers
        response2.setStatus(HttpServletResponse.SC_OK);
        response2.addHeader("Content-Disposition", "attachment; filename=\"allDocuments.zip\"");

        ZipOutputStream zipOutputStream = new ZipOutputStream(response2.getOutputStream());

        String directory = "./uploads";
        File fileDir = new File(directory);
        File[] listOfDocuments = fileDir.listFiles();

        // package files
        for (File file : listOfDocuments) {
            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);
            IOUtils.copy(fileInputStream, zipOutputStream);
            fileInputStream.close();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }

    public List<AnonymousIdeaDTO> getAnonymousIdeas() {

        return convertListToUserْDTO(ideaRepository.getAnonymousIdeas());
    }

    private List<AnonymousIdeaDTO> convertListToUserْDTO(List<Idea> ideas) {

        List<AnonymousIdeaDTO> anonymousIdeaDTOS = new ArrayList<>();
        for (Idea idea : ideas) {
            anonymousIdeaDTOS.add(convertToAnonymousIdeaDTO(idea));
        }

        return anonymousIdeaDTOS;
    }

//    public Resource downloadFile(String documentPath) {
//
//        Path path = Paths.get(documentPath);
//
//        UrlResource resource = null;
//        try {
//            resource = new UrlResource(path.toUri());
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        return resource;
//    }
}
