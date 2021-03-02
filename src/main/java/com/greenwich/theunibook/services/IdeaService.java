package com.greenwich.theunibook.services;

import com.greenwich.theunibook.dto.IdeaDTO;
import com.greenwich.theunibook.models.Comment;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.IdeaRepository;
import com.greenwich.theunibook.repository.RatingRepository;
import com.greenwich.theunibook.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IdeaService {

    @Autowired
    IdeaRepository ideaRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RatingRepository ratingRepository;


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

            idea.setStatusId(1);
            idea.setDepartmentId(ideaAuthor.getDepartmentId());

            //Save document if it exists
            if (idea.getDocument() != null) {
                String documentPath = uploadFile(idea.getDocument());
                if (documentPath != null) {
                    idea.setDocumentPath(documentPath);
                }
            }

            Idea savedIdea = ideaRepository.save(idea);

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

    public HashMap<String, Object> getIdeasByDepartment(int departmentId, int page) {

        HashMap<String, Object> getIdeasByDepartmentResponse = new HashMap<>();

        Pageable pageRequestWithFiveIdeas = PageRequest.of(page - 1, 5);

        Page<Idea> fiveIdeasByDepartmentPage = ideaRepository.findAllByDepartmentId(departmentId, pageRequestWithFiveIdeas);

//        ideaRepository.findAll()
        List<Idea> fiveIdeasByDepartment = fiveIdeasByDepartmentPage.getContent();

        List<IdeaDTO> ideaDTOS = fiveIdeasByDepartment
                .stream()
                .map(this::convertToIdeaDTO)
                .collect(Collectors.toList());


        getIdeasByDepartmentResponse.put("ideas", ideaDTOS);
        getIdeasByDepartmentResponse.put("page-count", calculateNumberOfPagesBasedOnListSize(ideaDTOS.size()));

        return getIdeasByDepartmentResponse;
    }

    public HashMap<String, Object> getIdeasByDepartmentPaginated(int departmentId, int page) {

        HashMap<String, Object> getIdeasByDepartmentResponse = new HashMap<>();

        List<Idea> fiveIdeasByDepartmentPaginated = ideaRepository.getIdeasByDepartmentIdPaginated(departmentId, page);


        List<IdeaDTO> ideaDTOS = fiveIdeasByDepartmentPaginated
                .stream()
                .map(this::convertToIdeaDTO)
                .collect(Collectors.toList());


        getIdeasByDepartmentResponse.put("ideas", ideaDTOS);
        getIdeasByDepartmentResponse.put("pageCount", calculateNumberOfPagesBasedOnListSize(ideaRepository.countIdeasByDepartmentId(departmentId)));

        return getIdeasByDepartmentResponse;
    }

    public HashMap<String, Object> sortIdeasByCategoryPaginated(int departmentId, int page, int categoryId) {

        HashMap<String, Object> getIdeasByDepartmentResponse = new HashMap<>();

        List<Idea> fiveIdeasByDepartmentPaginated = ideaRepository.sortIdeasByCategoryPaginated(departmentId, page, categoryId);


        List<IdeaDTO> ideaDTOS = fiveIdeasByDepartmentPaginated
                .stream()
                .map(this::convertToIdeaDTO)
                .collect(Collectors.toList());


        getIdeasByDepartmentResponse.put("ideas", ideaDTOS);
        getIdeasByDepartmentResponse.put("pageCount", calculateNumberOfPagesBasedOnListSize(ideaRepository.countIdeasByDepartmentId(departmentId)));

        return getIdeasByDepartmentResponse;
    }


    private IdeaDTO convertToIdeaDTO(Idea idea) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        IdeaDTO ideaDTO = modelMapper.map(idea, IdeaDTO.class);


        User ideaAuthor = userRepository.findById(idea.getUserId()).get();
        if (idea.isAnonymous()) {
            ideaDTO.setAuthorName("Anonymous");
        } else {
            ideaDTO.setAuthorName(ideaAuthor.getFirstname() + " " + ideaAuthor.getLastname());
        }

        ideaDTO.setLikes(ratingRepository.getIdeaLikes(idea.getId()));
        ideaDTO.setDislikes(ratingRepository.getIdeaDislikes(idea.getId()));

        return ideaDTO;
    }


    public int calculateNumberOfPagesBasedOnListSize(int itemCount) {


        int numberOfPages = itemCount / 5;

        if (itemCount % 5 > 0) {
            numberOfPages += 1;
        }

        return numberOfPages;
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
