package ob.openbank.repository;

import ob.openbank.entity.OpenbankingRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenbankingRequestLogRepository extends
    JpaRepository<OpenbankingRequestLog, String> {

}