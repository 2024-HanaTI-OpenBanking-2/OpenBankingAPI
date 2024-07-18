package ob.openbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// AccessTokenDTO 클래스에 적절한 생성자나 @JsonProperty 어노테이션이 적용된 세터가 없기 때문에, Jackson 라이브러리가 JSON 데이터를 해당 객체로 변환할 수 없어서 발생하는 문제
public class AccessTokenDTO {

  private String access_token;
}
