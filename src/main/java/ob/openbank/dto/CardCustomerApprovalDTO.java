package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardCustomerApprovalDTO {

  private String approvalNumber;
  private Instant approvalDate;
  private Double approvalAmount;
  private String merchantId;
  private Double benefitAmount;
  private String approvalStatusCode;
  private String paymentCategory;
  private String customerCardId;
}