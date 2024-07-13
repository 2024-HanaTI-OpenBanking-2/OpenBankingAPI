package ob.openbank.repository;

import ob.openbank.entity.OpenbankingAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OpenbankingAuthenticationRepository extends
    JpaRepository<OpenbankingAuthentication, String> {
    Optional<OpenbankingAuthentication> findByAuthorizationCode(String authorizationCode);

    @Query("SELECT COUNT(o) FROM OpenbankingAuthentication o")
    Long countAllCIs();
}