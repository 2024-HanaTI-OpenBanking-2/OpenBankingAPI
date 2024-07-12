package ob.openbank.service;

import ob.openbank.dto.AuthorizeResponseDTO;
import ob.openbank.entity.OpenbankingAuthorizedClient;
import ob.openbank.repository.OpenbankingAuthorizedClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

  @Autowired
  private OpenbankingAuthorizedClientRepository clientRepository;

  public AuthorizeResponseDTO authorize(String responseType, String clientId, String redirectUri,
      String scope) {
    // DB에서 clientId가 있는지 확인
    Optional<OpenbankingAuthorizedClient> clientOpt = clientRepository.findById(clientId);

    if (clientOpt.isPresent()) {
      // authorization_code를 랜덤 문자열로 생성
      String authorizationCode = UUID.randomUUID().toString();

      // AuthorizeResponseDTO 객체 생성 및 값 설정
      AuthorizeResponseDTO responseDTO = AuthorizeResponseDTO.builder()
          .code(authorizationCode)
          .scope("all")
          .build();

      return responseDTO;
    } else {
      // clientId가 유효하지 않은 경우 예외 발생
      throw new RuntimeException("Invalid client_id");
    }
  }
}
