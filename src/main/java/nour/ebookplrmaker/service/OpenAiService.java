package nour.ebookplrmaker.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class OpenAiService {
    private final String openAiKey = "example"; // let's take care of the actual key later also create the .env later let's keep it like this till we workout other stuff

    private ChatClient chatClient;


    @Autowired
    public OpenAiService(ChatClient.Builder client) {
        this.chatClient = client.build();
    }

    /*
     Generates Text and Markdown Stuff
     ,normalize parameter is a specific string that contains the instructions for returning the response
     as we want for the specific file type we need,
     */
    public String generateContent(String context, String prompt, String normalize) {
        try {
            /*
            Generation logic as we use the @filePrompt(prompt) and @normalize
            as a system role in open Ai request
            and we use the @Context(the book) as the user message
             */
            String response = chatClient     // genaration logic as we use
                            .prompt(context)
                            .system(prompt+"/n"+normalize)
                    .call()
                    .chatResponse()
                    .getResult()
                    .getOutput()
                    .getText();
            ;
//            if(response.isEmpty()){
//                return ResponseEntity.internalServerError().body("Empty Response");
//            }
            return response;
        }
        catch (RuntimeException e){
            throw new RuntimeException("error Generating the file");
        }

    }
}