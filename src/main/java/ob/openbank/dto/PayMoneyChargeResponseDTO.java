package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayMoneyChargeResponseDTO {
    private String bankCodeStd; // 어느 은행인지
    private String balanceAmt; // 충전 완료 후 해당 계좌에 얼마가 남았는지
    private double amount; // 얼마의 금액을 충전했는지
    private String accountNum; // 계좌번호
}
