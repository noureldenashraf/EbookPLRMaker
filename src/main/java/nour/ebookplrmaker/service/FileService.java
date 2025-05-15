package nour.ebookplrmaker.service;


import nour.ebookplrmaker.model.RequestDetails;
import nour.ebookplrmaker.model.File;
import nour.ebookplrmaker.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service						
public class FileService {
    private FilesRepository filesRepo;
    private OpenAiService openAiService;
    /*
    @FileTypes that are accepted in adding and generating and updating
    Add others if needed,
    HashTable so we can check if the fileType exists and also to get it's normalization O(1)
     */
  //  private final HashSet<String> fileTypes = new HashSet<>(List.of("HTML","TXT","DOCX"));
    private final Hashtable<String,String> fileTypeAndNormalization= new Hashtable<>(Map.of
            ("HTML","Please return only the raw HTML code, without any additional text, explanation, or formatting or Markup language indicating the start and the end of the html code",
             "TXT","You are to respond in raw, unformatted plain text only. Do not use markdown, HTML, bullet points, numbered lists, headings, bold, italics, quotation marks, special symbols, or code blocks. Avoid all forms of formatting. Write as if you are typing in a plain Notepad editor. Do not include any symbols such as *, #, >, -, `, or < >. Your response must be pure plain text with no styling, no tags, no indentation, and no line prefixes. Do not wrap text in quotation marks or other delimiters. This rule applies to all outputs, regardless of content type.",
             "DOCX","Return only the fully structured raw content formatted for direct DOCX generation, including clear headings and subheadings. Do not include any additional text, commentary, or formatting outside the document structure."
            )
    );

    @Autowired	
    public FileService(FilesRepository filesRepository,OpenAiService openAiService) {
        this.filesRepo = filesRepository;
        this.openAiService = openAiService;
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
        if (!fileTypeAndNormalization.contains(newFile.getType())){
            throw new RuntimeException("This fileType is not Supported");
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

    public ResponseEntity<?> generateFiles(RequestDetails requestDetails) {
        if(!requestDetails.getFileIds().contains(",")){ // in case we generate only one file aka: fileIds{23} it will not contain {,}
            Optional<File> file = filesRepo.findById(Integer.valueOf(requestDetails.getFileIds()));
            if(file.isEmpty()){
                throw new RuntimeException("File with id : " + requestDetails.getFileIds() + " Not Found");
            }
            if (!fileTypeAndNormalization.contains(file.get().getType())){
                throw new RuntimeException("File with id : " + file.get().getId() + " Not Found");
            }
                return openAiService.generateContent(requestDetails.getContext(),file.get().getPrompt(),fileTypeAndNormalization.get(file.get().getType()));
        }
        // else means that we will generate multiple ones
//        while(true){
//            for (char c:requestDetails.getFileIds().toCharArray()) {
//                String fileId =
//                if(c != ','){
//                    continue;
//                }
//
//            }
//        }
//
//
//        13,4,6,1


    }
}
// TODO : Normalize based on file type (done)
// TODO : Get the context from docx or  (text is done)