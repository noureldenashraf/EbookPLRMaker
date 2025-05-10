package nour.ebookplrmaker.repository;

import nour.ebookplrmaker.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepository extends JpaRepository<Profile,Integer> {
}
