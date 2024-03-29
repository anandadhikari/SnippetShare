package dev.anand.backend.dto.review;

import dev.anand.backend.dto.snippet.SnippetRequestDto;
import dev.anand.backend.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDto {
    private String id;
    private String userId;
    private String snippetId;
    private String review;
    private UserResponseDto user;
    private SnippetRequestDto snippet;
}
