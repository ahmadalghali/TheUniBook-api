package com.greenwich.theunibook.repository;

import com.greenwich.theunibook.models.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    List<Comment> getAllByIdeaIdOrderByDateDesc(int ideaId);
}
