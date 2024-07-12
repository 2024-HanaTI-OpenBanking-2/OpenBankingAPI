package ob.openbank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ob_authorized_clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenbankingAuthorizedClient {

  @Id
  @Column(name = "client_id", nullable = false, length = 255)
  private String clientId;

  @Column(name = "client_secret", nullable = false, length = 255)
  private String clientSecret;

  @Column(name = "organization_name", nullable = false, length = 255)
  private String organizationName;

  @Column(name = "redirect_uri", length = 255)
  private String redirectUri;

  @Column(name = "created_time", nullable = false)
  private Timestamp createdTime;

  @Column(name = "updated_time", nullable = false)
  private Timestamp updatedTime;
}
