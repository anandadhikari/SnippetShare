package dev.anand.backend.repository;

import dev.anand.backend.entity.Snippet;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnippetRepository extends MongoRepository<Snippet, String> {
    //    Randomized list of SNIPPETS to show on dashboard
    @Aggregation(pipeline = {
            "{ $sample: { size: 15 } }"
    })
    List<Snippet> findRandomSnippets();

    List<Snippet> findByUserId(String userId);

    List<Snippet> findAllByTagsContainingIgnoreCase(String tag);

    List<Snippet> findAllByTitleContainingIgnoreCase(String title);

    List<Snippet> findAllByLanguageContainingIgnoreCase(String language);
}

