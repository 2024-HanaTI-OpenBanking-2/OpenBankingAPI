package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CombinedAccountInfo {

  private List<AccountInfoResponseDTO> bankAccounts;
  private List<SecuritiesAccountInfoDto> stockAccounts;
}
