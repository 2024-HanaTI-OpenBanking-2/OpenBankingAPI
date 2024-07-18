package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountInfoDTO {

  private String access_token;
  private String auth_code;
}
