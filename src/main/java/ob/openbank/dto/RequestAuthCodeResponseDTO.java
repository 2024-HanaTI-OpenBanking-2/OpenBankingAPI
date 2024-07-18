package ob.openbank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestAuthCodeResponseDTO {

  private String authCode;
  private String accessToken;
}
