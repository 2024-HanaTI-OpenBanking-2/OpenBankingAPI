package ob.openbank.repository;

import java.util.Optional;
import ob.openbank.entity.OpenbankingAuthorizedClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenbankingAuthorizedClientRepository extends
    JpaRepository<OpenbankingAuthorizedClient, String> {

  // clientId로 OpenbankingAuthorizedClient를 찾기 위한 메서드 추가
  Optional<OpenbankingAuthorizedClient> findByClientId(String clientId);

}
