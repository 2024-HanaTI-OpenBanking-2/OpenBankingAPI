package ob.openbank.repository;

import ob.openbank.entity.OpenbankingAccountInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenbankingAccountInformationRepository extends
    JpaRepository<OpenbankingAccountInformation, String> {

}
