package ob.openbank.repository;

import ob.openbank.entity.OpenbankingAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenbankingAuthenticationRepository extends
    JpaRepository<OpenbankingAuthentication, String> {

}