package nour.ebookplrmaker.repository;

import nour.ebookplrmaker.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<File,Integer> {

}
