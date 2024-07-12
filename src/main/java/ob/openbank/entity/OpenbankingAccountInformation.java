package ob.openbank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ob_account_informations")
public class OpenbankingAccountInformation {

  @Id
  @Column(name = "account_num", nullable = false, length = 255)
  private String accountNum;

  @Column(name = "ci", nullable = false, length = 255)
  private String ci;

  @Column(name = "client_id", length = 255)
  private String clientId;
}
