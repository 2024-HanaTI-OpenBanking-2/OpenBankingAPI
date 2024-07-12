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
@Table(name = "ob_request_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenbankingRequestLog {

  @Id
  @Column(name = "log_id", nullable = false, length = 255)
  private String logId;

  @Column(name = "request_url", nullable = false, length = 255)
  private String requestUrl;

  @Column(name = "http_method", nullable = false, length = 255)
  private String httpMethod;

  @Column(name = "request_headers", nullable = false, length = 255)
  private String requestHeaders;

  @Column(name = "request_body", nullable = false, length = 255)
  private String requestBody;

  @Column(name = "response_code", nullable = false)
  private Integer responseCode;

  @Column(name = "response_body", nullable = false, length = 255)
  private String responseBody;

  @Column(name = "request_time", nullable = false)
  private Timestamp requestTime;

  @Column(name = "response_time", nullable = false)
  private Timestamp responseTime;

  @Column(name = "client_ip", nullable = false, length = 255)
  private String clientIp;
}