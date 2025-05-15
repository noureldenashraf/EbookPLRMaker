package nour.ebookplrmaker.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestDetails {
    @NotEmpty
    private String fileIds;
    @NotEmpty
    private String context;
//    private String needsProcessingContext; // idk about the datatype yet lets leave it like this for now
}
