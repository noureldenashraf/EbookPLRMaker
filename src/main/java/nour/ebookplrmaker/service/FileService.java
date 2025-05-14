package nour.ebookplrmaker.service;


import nour.ebookplrmaker.model.File;
import nour.ebookplrmaker.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service						
public class FileService {
    private FilesRepository filesRepo;

    @Autowired	
    public FileService(FilesRepository filesRepository) {
        this.filesRepo = filesRepository;
    }

    public ResponseEntity<?> getAllFiles() {
        try {
            return ResponseEntity.accepted().body(filesRepo.findAll());
        } catch (RuntimeException e) {
            throw new RuntimeException("Couldn't Retrieve Files");
        }
    }

    public ResponseEntity<?> deleteFileWithId(Integer fileId) {

//        Optional<File> tempFile = filesRepo.findById(fileId);
//        if (tempFile.isEmpty()) {
//            throw new RuntimeException("File with id : " + fileId + " Not Found");
//        }
        if (!filesRepo.existsById(fileId)) { // much better than the above one i believe and less complicated
            throw new RuntimeException("File with id : " + fileId + " Not Found");
        }
        filesRepo.deleteById(fileId);
        return ResponseEntity.accepted().body("File with id : " + fileId + " Deleted successfully");

    }

    public ResponseEntity<?> getFileByID(Integer fileId) {
        Optional<File> searchedFile = filesRepo.findById(fileId);
        if (searchedFile.isEmpty()) {
            throw new RuntimeException("File with id : " + fileId + " Not Found");
        }
        return ResponseEntity.accepted().body(searchedFile.get());
    }


    public ResponseEntity<?> addNewFile(File newFile) {
        if (newFile.getId() != 0) { // in case if the body is sent with ID
            newFile.setId(0);
        }
        try {
            return ResponseEntity.accepted().body(filesRepo.save(newFile));
        } catch (RuntimeException e) {
            throw new RuntimeException("Can't save File");
        }
    }

    public ResponseEntity<?> updateFile(File updateFile) {
        if (!filesRepo.existsById(updateFile.getId())) {
            throw new RuntimeException("File with id " + updateFile.getId() + " Doesn't exist");
        }
        try {
            return ResponseEntity.accepted().body(filesRepo.save(updateFile));
        }
        catch (RuntimeException e){
            throw new RuntimeException("Couldn't process the Transaction");
        }

    }
}
