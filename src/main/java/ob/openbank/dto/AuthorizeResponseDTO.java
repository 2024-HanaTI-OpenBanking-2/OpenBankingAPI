package ob.openbank.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorizeResponseDTO {

  private String code;
  //  private String state;
  private String scope;
}
