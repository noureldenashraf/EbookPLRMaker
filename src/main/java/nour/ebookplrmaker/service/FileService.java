package nour.ebookplrmaker.service;


import nour.ebookplrmaker.model.RequestDetails;
import nour.ebookplrmaker.model.File;
import nour.ebookplrmaker.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service						
public class FileService {
    private FilesRepository filesRepo;
    private OpenAiService openAiService;
    private DocxService docxService;
    /*
    @FileTypes that are accepted in adding and generating and updating
    Add others if needed,
    HashTable so we can check if the fileType exists and also to get it's normalization O(1)
     */
  //  private final HashSet<String> fileTypes = new HashSet<>(List.of("HTML","TXT","DOCX"));
    private final Hashtable<String,String> fileTypeAndNormalization= new Hashtable<>(Map.of
            ("HTML","Please return only the raw HTML code, without any additional text, explanation, or formatting or Markup language indicating the start and the end of the html code",
             "TXT","You are to respond in raw, unformatted plain text only. Do not use markdown, HTML, bullet points, numbered lists, headings, bold, italics, quotation marks, special symbols, or code blocks. Avoid all forms of formatting. Write as if you are typing in a plain Notepad editor. Do not include any symbols such as *, #, >, -, `, or < >. Your response must be pure plain text with no styling, no tags, no indentation, and no line prefixes. Do not wrap text in quotation marks or other delimiters. This rule applies to all outputs, regardless of content type.",
             "DOCX","Return only the fully structured raw content formatted in valid, semantic HTML. Include clear headings (<h1>–<h3>), subheadings, paragraphs (<p>), and lists (<ul>, <ol>). Avoid CSS, JavaScript, or unsupported HTML features. Ensure all tags are well-formed and XHTML-compatible to ensure smooth DOCX conversion using tools like docx4j. Do not include any additional commentary or text outside the HTML content"
            )
    );

    @Autowired	
    public FileService(FilesRepository filesRepository,OpenAiService openAiService,DocxService docxService) {
        this.filesRepo = filesRepository;
        this.openAiService = openAiService;
        this.docxService = docxService;
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
        if (!fileTypeAndNormalization.containsKey(newFile.getType())){
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

    public ResponseEntity<?> generateFiles(RequestDetails requestDetails) throws IOException {
        if(!requestDetails.getFileIds().contains(",")) { // in case we generate only one file aka: fileIds{23} it will not contain {,}
            Optional<File> file = filesRepo.findById(Integer.valueOf(requestDetails.getFileIds()));
            if (file.isEmpty()) {
                throw new RuntimeException("File with id : " + requestDetails.getFileIds() + " Not Found");
            }
            if (!fileTypeAndNormalization.containsKey(file.get().getType())) { //TODO
                // already took care of it in adding but just in case
                throw new RuntimeException("File type " + file.get().getType() + " Not supported");
            }
            try {

                java.io.File savedFile = new java.io.File
                        ("src/main/resources/static/"
                                + new Date().getTime() // to make sure files have unique identifiers
                                + file.get().getName()
                                + requestDetails.getFileIds()
                                + "." + file.get().getType().toLowerCase()); // .FileType
                FileWriter fileWriter = new FileWriter(savedFile, true);
                String generatedContent = openAiService
                        .generateContent
                                (requestDetails.getContext()
                                        , file.get().getPrompt()
                                        , fileTypeAndNormalization.get(file.get().getType())
                                );
                if (generatedContent.isEmpty()) {
                    throw new RuntimeException("Content Couldn't be generated");
                }
                /*
                After Generating the content based on the file type we will handle it to save
                the content to file,
                however all the following cases will work great with no extra work but the DOCX
                will need more work on it
                 */
                switch (file.get().getType()) { // file Type
                    case "DOCX":
                        try {
                            fileWriter.close(); // we won't use it so why wasting memory
                            savedFile = docxService.convertToDocx(savedFile,generatedContent); // non complex HTML ---> DOCX
                            break;
                        } catch (Exception e){
                            throw new RuntimeException(e);
                        }
                    case "HTML", "TXT":
                        fileWriter.write(generatedContent);
                        fileWriter.close();
                        break;

                    case null, default:
                        throw new RuntimeException("File Type Not Supported");

                }
                return ResponseEntity.accepted().body(savedFile);
            }
            catch(RuntimeException e){
                throw new RuntimeException(e.getMessage());
            }
            catch (IOException e){
                throw new IOException(e.getMessage());
            }
        }

 //        else means that we will generate multiple ones
        //  using @hashTable to carry all the filesIds and there Generated content
        Hashtable<String,ResponseEntity<?>> fileIdAndGeneratedContent = new Hashtable<>();
        StringBuilder id = new StringBuilder();
        for (char c:requestDetails.getFileIds().toCharArray()) {

            if(c == ','){
                     /*
                    instead of making a making duplicated code alone for the case of multiple FileIds
                    i thought why not using recursion and through it a new RequestDetails entity that have the invidual fileId
                    of the stripped fileIds String and the recursion will see it as a one fileId
                     */
                fileIdAndGeneratedContent.put(id.toString(),generateFiles(new RequestDetails(id.toString(),requestDetails.getContext())));
                id.delete(0,id.length()); // clearing the FileId for another entry
            }
            else {
                id.append(c);
            }
            }
        /*
        We can remove the extra one here by optimizing
        the forEach loop and converting it for .charAt(i)
        and if == .length() do it
        i will do it later
        TODO
         */
        fileIdAndGeneratedContent.put(id.toString(),generateFiles(new RequestDetails(id.toString(),requestDetails.getContext())));
//        for (char c:requestDetails.getFileIds().toCharArray()) {
//            StringBuilder id = new StringBuilder();
//                if (c != ','){
//                    id.append(c);
//                }
//                else {
//                    Optional<File> file = filesRepo.findById(Integer.valueOf(requestDetails.getFileIds()));
//                    if(file.isEmpty()){
//                        throw new RuntimeException("File with id : " + requestDetails.getFileIds() + " Not Found");
//                    }
//                    if (!fileTypeAndNormalization.containsKey(file.get().getType())){ // already took care of it in adding but just in case
//                        throw new RuntimeException("File type "+file.get().getType()+" Not supported");
//                    }
//                    try {
//
//                        fileIdAndGeneratedContent.put(id.toString(), openAiService.generateContent(requestDetails.getContext(), file.get().getPrompt(), fileTypeAndNormalization.get(file.get().getType())));
//                    }
//                    catch (RuntimeException e){
//                        throw new RuntimeException("Error in transaction");
//                    }
//                    }
//            }

                                return ResponseEntity.accepted().body(fileIdAndGeneratedContent);
    }
}
// TODO : Normalize based on file type (done)
// TODO : Get the context from docx or  (text is done)
// TODO : look at the File creating logic