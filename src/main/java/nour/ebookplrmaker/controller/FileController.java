package nour.ebookplrmaker.controller;

import jakarta.validation.Valid;
import nour.ebookplrmaker.model.RequestDetails;
import nour.ebookplrmaker.model.File;
import nour.ebookplrmaker.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController("/api/files")
public class FileController {
    private FileService fileService;

    @Autowired
    public FileController (FileService fileService){
        this.fileService = fileService;
    }

    @GetMapping("/api/files") // returns all files if no fileId parameter is present
    ResponseEntity<?> getFiles (
            @RequestParam(required = false,name = "fileId") Optional<Integer> fileId
    )
    {
        if(fileId.isPresent()){
            return fileService.getFileByID(fileId.get());
        }
        return fileService.getAllFiles();
    }

    @DeleteMapping("/api/files")
    ResponseEntity<?> deleteFileByID (
            @RequestParam(required = true,name = "fileId") Optional<Integer> fileId
    )
    {
        if(fileId.isEmpty()){
            throw new RuntimeException("Requested Parameter Not found");
        }
        return fileService.deleteFileWithId(fileId.get());
    }

    @PostMapping("/api/files")
    ResponseEntity<?> addFile (
            @RequestBody(required = true) File newFile
    )
    {
        return fileService.addNewFile(newFile);
    }

    @PutMapping("/api/files")
    ResponseEntity<?> updateFile(
            @RequestBody(required = true) File updateFile
    )
    {
        return fileService.updateFile(updateFile);
    }

    @PostMapping("/api/files/generate")
    ResponseEntity<?> generateFiles
    (
    //        @RequestParam(required = false,name = "fileId") Optional<Integer> fileId,
            @RequestBody(required = true) @Valid Optional<RequestDetails> requestDetails
            ) throws IOException {
//        if (fileId.isPresent()){
//                return fileService.generateFiles(fileId.get());
//        }
        /*
        i may need to look at the Jakarta Validation it will make the code much simpler
        but for now i will keep this till i find out more how it works TODO
         */
        if (requestDetails.isEmpty() || requestDetails.get().getFileIds().isEmpty() || requestDetails.get().getContext().isEmpty())
        {
            throw new RuntimeException("empty fileds in the request");
        }
            return fileService.generateFiles(requestDetails.get());
    }


}
