package ob.openbank.repository;

import java.util.Optional;
import ob.openbank.entity.OpenbankingAuthorizedClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpenbankingAuthorizedClientRepository extends
    JpaRepository<OpenbankingAuthorizedClient, String> {

  // clientId로 OpenbankingAuthorizedClient를 찾기 위한 메서드 추가
  Optional<OpenbankingAuthorizedClient> findByClientId(String clientId);
  @Query(value = "SELECT COUNT(*) FROM (SELECT 1 FROM ob_authorized_clients oac WHERE oac.client_id = :clientId AND ROWNUM = 1)", nativeQuery = true)
  Integer existsByClientIdCustom(@Param("clientId") String clientId);

}
