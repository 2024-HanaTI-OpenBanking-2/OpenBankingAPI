package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor  // 기본 생성자 추가
public class AccountCiResponseDTO {
  private String ci;
}
