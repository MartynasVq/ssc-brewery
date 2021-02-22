package guru.sfg.brewery.repositories.seurity;

import guru.sfg.brewery.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {


}
