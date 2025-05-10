package nour.ebookplrmaker.controller;

import nour.ebookplrmaker.model.File;
import nour.ebookplrmaker.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<?> deleteFileByID( @RequestParam(required = true,name = "fileId") Integer fileId){
        return fileService.deleteFileWithId(fileId);
    }

    @PostMapping("/api/files")
    ResponseEntity<?> addFile (
            @RequestBody(required = true) File newFile
    ){
        return fileService.addNewFile(newFile);
    }

}
