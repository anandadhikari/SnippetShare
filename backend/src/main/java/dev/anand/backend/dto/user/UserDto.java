package dev.anand.backend.dto.user;

import dev.anand.backend.dto.review.ReviewDto;
import dev.anand.backend.entity.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private String id;
    private String firstname;
    private String lastname;
    private List<Snippet> snippets;
    private List<ReviewDto> reviews;
}
