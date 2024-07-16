package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountInfoResponseDTO {
  private String accountNum;
  private String customerId;
  private String accountName;
  private String customerPassword;
  private double balance;
  private String accountIssueDate;
  private String accountStatus;
  private String bankCodeStd;
  private String activityType;
  private String accountType;
  private String maturityDate;
  private String productId;
  private String dormancyYn;
  private String balanceAmt;
}
