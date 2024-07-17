package ob.openbank.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsuranceResponseDTO {
  private String insuranceId; // 증권번호
  private String insuranceName; // 상품명
  private String insuranceType; // 보험종류
  private String totalFee; // 보험상태
  private String insuranceStatus; // 보��상태
  private Timestamp startDate; // 시작일
  private Timestamp expiryDate; // 만료일
}
