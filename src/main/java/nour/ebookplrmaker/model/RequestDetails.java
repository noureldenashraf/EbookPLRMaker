package nour.ebookplrmaker.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@NoArgsConstructor
@ToString
public class RequestDetails {
    @NotEmpty
    private String fileIds;
    @NotEmpty
    private String context;
//    private String needsProcessingContext; // idk about the datatype yet lets leave it like this for now


    public RequestDetails(String fileIds, String context) {
        this.fileIds = fileIds;
        this.context = context;
    }

    public @NotEmpty String getFileIds() {
        return fileIds;
    }

    public void setFileIds(@NotEmpty String fileIds) {
        this.fileIds = fileIds;
    }

    public @NotEmpty String getContext() {
        return context;
    }

    public void setContext(@NotEmpty String context) {
        this.context = context;
    }

}
