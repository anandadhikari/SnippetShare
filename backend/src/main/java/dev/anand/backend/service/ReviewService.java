package dev.anand.backend.service;

import dev.anand.backend.dto.review.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto addReview(String message, String snippetId, String userId);

    ReviewDto getReviewById(String id);

    List<ReviewDto> getReviewsBySnippetId(String snippetId);

    List<ReviewDto> getReviewsByUserId(String userId);

    ReviewDto updateReview(String message, String id, String userId);

    void deleteReview(String id, String userId);

}
