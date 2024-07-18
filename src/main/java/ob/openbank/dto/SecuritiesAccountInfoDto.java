package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SecuritiesAccountInfoDto {

  private String accountId;
  private String financialCompany;
  private String accountNumber;
  private BigDecimal cash;
  private BigDecimal totalAssets;

}