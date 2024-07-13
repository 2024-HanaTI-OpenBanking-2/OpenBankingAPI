package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccessTokenResponseDTO {

  private String access_token;
//  private String token_type;
  private int expires_in;
  private String refresh_token;
  private String seq_no; // 사용자 일련번호

}
