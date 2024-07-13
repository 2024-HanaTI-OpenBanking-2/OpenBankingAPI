package ob.openbank.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ob_authentications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenbankingAuthentication {

  @Id
  @Column(name = "ci", nullable = false, length = 255)
  private String ci;

  @Column(name = "access_token_id", length = 255)
  private String accessTokenId;

  @Column(name = "expires_in")
  private Integer expiresIn;

  @Column(name = "refresh_token", length = 255)
  private String refreshToken;

  @Column(name = "scope", length = 255)
  private String scope;

//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_no_generator")
//  @SequenceGenerator(name = "seq_no_generator", sequenceName = "seq_no_seq", allocationSize = 1)
  @Column(name = "seq_no")
  private Long seqNo;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Column(name = "client_id", nullable = false, length = 255)
  private String clientId;

  @Column(name = "auth_code", length = 255)
  private String authCode;

  @Column(name = "authorization_code", length = 255)
  private String authorizationCode;
}
