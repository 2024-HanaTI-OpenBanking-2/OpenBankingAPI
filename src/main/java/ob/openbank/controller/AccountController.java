package ob.openbank.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import ob.openbank.dto.AccountInfoDTO;
import ob.openbank.dto.AccountInfoResponseDTO;
import ob.openbank.dto.CombinedAccountInfo;
import ob.openbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/accountinfo")
public class AccountController {

  @Autowired
  private AccountService accountService;

  @PostMapping("/list")
  public ResponseEntity<CombinedAccountInfo> getAccountInfoList(
      @RequestBody AccountInfoDTO accountInfoDTO) throws ExecutionException, InterruptedException {
    // DTO에서 access_token과 auth_code를 사용하여 로직을 수행
    CombinedAccountInfo accountList = accountService.getAccountInfoList(accountInfoDTO);
    return ResponseEntity.ok(accountList);
  }

}
