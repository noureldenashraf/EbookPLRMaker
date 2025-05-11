package nour.ebookplrmaker.repository;

import nour.ebookplrmaker.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface ProfilesRepository extends JpaRepository<Profile,Integer> {

//    @Query(value = "INSERT INTO profiles (name,files) values (?name,?files)",nativeQuery = true);
//    Profile addProfile;
}
