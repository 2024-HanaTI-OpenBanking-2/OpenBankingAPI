package ob.openbank.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import ob.openbank.dto.AccountInfoDTO;
import ob.openbank.dto.AccountInfoInsDTO;
import ob.openbank.dto.CombinedAccountInfo;
import ob.openbank.dto.InsuranceDTO;
import ob.openbank.dto.InsuranceResponseDTO;
import ob.openbank.service.AccountService;
import ob.openbank.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/insurance")

public class InsuranceController {

  @Autowired
  private InsuranceService insuranceService;
  @Autowired
  private AccountService accountService;

  @PostMapping("/list")
  public ResponseEntity<List<InsuranceResponseDTO>> getInsuranceInfoList(
      @RequestBody InsuranceDTO insuranceDTO) throws ExecutionException, InterruptedException {
    // DTO에서 access_token과 auth_code를 사용하여 로직을 수행
    List<InsuranceResponseDTO> InsuranceList = insuranceService.getInsuranceInfoList(insuranceDTO);
    return ResponseEntity.ok(InsuranceList);
  }

  @PostMapping("/accountinfo/list")
  public ResponseEntity<CombinedAccountInfo> getAccountInfoList(
      @RequestBody AccountInfoInsDTO accountInfoInsDTO) throws ExecutionException, InterruptedException {
    String ci = accountInfoInsDTO.getCi();
    System.out.println("in /account/list");
    // Access token과 Auth code를 조회하는 로직을 추가합니다.
    String accessToken = insuranceService.getAccessTokenByCi(ci);
    String authCode = insuranceService.getAuthCodeByCi(ci);

    System.out.println(accessToken);
    System.out.println(authCode);
    AccountInfoDTO accountInfoDTO = new AccountInfoDTO(accessToken, authCode);

    // Account 서비스 로직 재사용
    System.out.println(accountInfoDTO);
    CombinedAccountInfo accountList = accountService.getAccountInfoList(accountInfoDTO);
    return ResponseEntity.ok(accountList);
  }
}
