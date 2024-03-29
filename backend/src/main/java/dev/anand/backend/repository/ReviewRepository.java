package dev.anand.backend.repository;

import dev.anand.backend.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findAllBySnippetId(String snippetId);

    List<Review> findAllByUserId(String userId);

    void deleteAllBySnippetId(String snippetId);

    void deleteAllByUserId(String userId);
}
