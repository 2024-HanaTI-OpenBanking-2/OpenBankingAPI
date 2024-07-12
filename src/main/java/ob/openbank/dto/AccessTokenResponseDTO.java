package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccessTokenResponseDTO {

  private String access_token;
  private String token_type;
  private int expires_in;
  private String refresh_token;

}
