package ob.openbank.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import ob.openbank.dto.AccountCiResponseDTO;
import ob.openbank.dto.AccountInfoDTO;
import ob.openbank.dto.AccountInfoResponseDTO;
import ob.openbank.dto.CombinedAccountInfo;
import ob.openbank.dto.InsuranceDTO;
import ob.openbank.dto.InsuranceResponseDTO;
import ob.openbank.dto.SecuritiesAccountInfoDto;
import ob.openbank.entity.OpenbankingAuthentication;
import ob.openbank.repository.OpenbankingAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InsuranceService {
  @Autowired
  private OpenbankingAuthenticationRepository openbankingAuthenticationRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${insurance.server.url}")
  private String InsURL;

  public List<InsuranceResponseDTO> getInsuranceInfoList(InsuranceDTO insuranceDTO) throws ExecutionException, InterruptedException {
    OpenbankingAuthentication ciObject = openbankingAuthenticationRepository.findByAccessTokenId(insuranceDTO.getAccess_token())
        .orElseThrow(() -> new RuntimeException("Invalid access token"));
    AccountCiResponseDTO accountCiResponseDTO = new AccountCiResponseDTO(ciObject.getCi()); // ci 담는 dto
    System.out.println("Service -> Sending request with CI: " + accountCiResponseDTO.getCi());

    String InsUrl = InsURL + "/api/insurance/list";

    // 비동기적으로 보험 서버에 POST 요청
    CompletableFuture<List<InsuranceResponseDTO>> insFuture = CompletableFuture.supplyAsync(() -> {
      InsuranceResponseDTO[] insResult = restTemplate.postForObject(InsUrl, accountCiResponseDTO, InsuranceResponseDTO[].class);
      return Arrays.asList(insResult);  // 배열을 리스트로 변환
    });

    // 보험 정보의 결과를 기다림
    List<InsuranceResponseDTO> insuranceResponse = insFuture.get();  // ExecutionException or InterruptedException could be thrown here

    return insuranceResponse;
  }

  public String getAccessTokenByCi(String ci) {
    OpenbankingAuthentication auth = openbankingAuthenticationRepository.findByCi(ci);
    if (auth != null) {
      return auth.getAccessTokenId();
    }
    return null;
  }

  public String getAuthCodeByCi(String ci) {
    OpenbankingAuthentication auth = openbankingAuthenticationRepository.findByCi(ci);
    if (auth != null) {
      return auth.getAuthCode();
    }
    return null;
  }
}
