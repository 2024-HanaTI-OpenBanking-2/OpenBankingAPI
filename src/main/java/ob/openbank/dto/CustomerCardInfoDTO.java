package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCardInfoDTO {

  private String customerCardId;
  private String cardProductId;
  private String customerId;
  private Date expirationDate;
  private Double lastMonthPerformance;
  private String customerPerformanceSegment;
  private String cardTypeCode;
  private String cardStatusCode;
  private byte[] cardImageUrl;
  private String cardName;
}
