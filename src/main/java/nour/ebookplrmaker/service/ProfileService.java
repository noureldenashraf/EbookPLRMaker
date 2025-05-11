package nour.ebookplrmaker.service;

import nour.ebookplrmaker.model.Profile;
import nour.ebookplrmaker.repository.ProfilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    private ProfilesRepository profilesRepo;

    @Autowired
    public ProfileService (ProfilesRepository profilesRepo){
        this.profilesRepo = profilesRepo;
    }

    public ResponseEntity<?> getAllProfiles() {
        try {
            return ResponseEntity.accepted().body(profilesRepo.findAll());
        }
        catch (RuntimeException e) {
            throw new RuntimeException("Couldn't Retrieve Profiles");
        }
    }
    public ResponseEntity<?> getProfileById(Integer profileId){
        Optional<Profile> searchedProfile = profilesRepo.findById(profileId);
        if(searchedProfile.isEmpty()){
            throw new RuntimeException("Profile with Id " +profileId+" Not Found");
        }
        return ResponseEntity.accepted().body(searchedProfile.get());
    }

    public ResponseEntity<?> AddProfile(Profile newProfile){ // files should be for example (13,55,1,4)
        if(newProfile.getId() != 0){ // if the request body contains id in it so we make it into 0
            newProfile.setId(0);
        }
        try {
            return ResponseEntity.accepted().body(profilesRepo.save(newProfile));
        } catch (RuntimeException e) {
            throw new RuntimeException("Can't save Profile");
        }

    }

    public ResponseEntity<?> deleteProfileWithId(Integer profileId) {
        if(!profilesRepo.existsById(profileId)){
            throw new RuntimeException("Profile with Id "+profileId+" Not Found");
        }
        try {
            profilesRepo.deleteById(profileId);
            return ResponseEntity.accepted().body("Profile with Id "+profileId+" Deleted Succesfully");
        }
        catch (RuntimeException e){
            throw new RuntimeException("Couldn't Delete the profile with Id "+profileId);
        }
    }

    public ResponseEntity<?> updateProfile (Profile updateProfile){
        if(!profilesRepo.existsById(updateProfile.getId())){
            throw new RuntimeException("Profile with Id "+updateProfile.getId()+" Not Found");
        }
        try {
            return ResponseEntity.accepted().body(profilesRepo.save(updateProfile));
        }
        catch (RuntimeException e){
            throw new RuntimeException("Couldn't process the Transaction");
        }
    }


}
