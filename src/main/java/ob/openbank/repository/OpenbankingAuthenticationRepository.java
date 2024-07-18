package ob.openbank.repository;

import ob.openbank.entity.OpenbankingAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OpenbankingAuthenticationRepository extends
    JpaRepository<OpenbankingAuthentication, String> {

  Optional<OpenbankingAuthentication> findByAuthorizationCode(String authorizationCode);

  Optional<OpenbankingAuthentication> findByAccessTokenId(String accessTokenId);

  OpenbankingAuthentication findByAuthCode(String authCode);

  @Query("SELECT COUNT(o) FROM OpenbankingAuthentication o")
  Long countAllCIs();

  @Query("SELECT o.ci FROM OpenbankingAuthentication o WHERE o.accessTokenId = :accessTokenId")
  Optional<String> findCiByAccessToken(@Param("accessTokenId") String accessTokenId);
}