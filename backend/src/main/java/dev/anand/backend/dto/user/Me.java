package dev.anand.backend.dto.user;

import dev.anand.backend.dto.review.ReviewDto;
import dev.anand.backend.dto.snippet.SnippetDto;
import dev.anand.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Me {
    private User me;
    private List<SnippetDto> mySnippets;
    private List<ReviewDto> myReviews;
}
