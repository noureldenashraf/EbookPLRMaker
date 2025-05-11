package nour.ebookplrmaker.controller;

import nour.ebookplrmaker.model.Profile;
import nour.ebookplrmaker.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController("/api/profiles")
public class ProfileController {
    private ProfileService profileService;

@Autowired
public ProfileController(ProfileService profileService) {
    this.profileService = profileService;

}

    @GetMapping("/api/profiles")
    public ResponseEntity<?> getProfile
    (
            @RequestParam(required = false,name = "profileId") Optional<Integer> ProfileId
    )
    {
        if (ProfileId.isPresent()){
            return profileService.getProfileById(ProfileId.get());
        }
        return profileService.getAllProfiles();

    }

    @PostMapping("/api/profiles") //files should be for example (13,55,1,4)
    ResponseEntity<?> addProfile
    (
            @RequestBody(required = true) Profile newProfile
    )
    {
        return profileService.AddProfile(newProfile);
    }

    @DeleteMapping("/api/profiles")
    ResponseEntity<?> deleteProfileById
     (
            @RequestParam(required = true,name = "profileId") Optional<Integer> profileId
     )
    {
        if(profileId.isEmpty()){
            throw new RuntimeException("Requested Parameter not found");
        }
        return profileService.deleteProfileWithId(profileId.get());
    }

    @PutMapping("/api/profiles")
    ResponseEntity<?> updateProfile
    (
           @RequestBody(required = true) Profile updateProfile
    )
    {
        return profileService.updateProfile(updateProfile);
    }
}
