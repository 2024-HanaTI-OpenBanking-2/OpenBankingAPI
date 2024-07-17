package ob.openbank.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ob.openbank.dto.*;
import ob.openbank.entity.OpenbankingAuthentication;
import ob.openbank.repository.OpenbankingAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AccountService {
  @Autowired
  private OpenbankingAuthenticationRepository openbankingAuthenticationRepository;

  @Autowired
  private RestTemplate restTemplate;


  public CombinedAccountInfo getAccountInfoList(AccountInfoDTO accountInfoDTO) throws ExecutionException, InterruptedException {
    OpenbankingAuthentication ciObject = openbankingAuthenticationRepository.findByAuthCode(accountInfoDTO.getAuth_code());
    AccountCiResponseDTO accountCiResponseDTO = new AccountCiResponseDTO(ciObject.getCi());
    System.out.println("Sending request with CI: " + accountCiResponseDTO.getCi());

    String bankUrl = "http://localhost:8083/accountinfo/list";
    String stockUrl = "http://localhost:8084/accountinfo/list";

    // 비동기적으로 각 서버에 POST 요청
    CompletableFuture<List<AccountInfoResponseDTO>> bankFuture = CompletableFuture.supplyAsync(() -> {
      AccountInfoResponseDTO[] bankResult = restTemplate.postForObject(bankUrl, accountCiResponseDTO, AccountInfoResponseDTO[].class);
      return Arrays.asList(bankResult);  // 배열을 리스트로 변환
    });

    CompletableFuture<List<SecuritiesAccountInfoDto>> stockFuture = CompletableFuture.supplyAsync(() -> {
      SecuritiesAccountInfoDto[] stockResult = restTemplate.postForObject(stockUrl, accountCiResponseDTO, SecuritiesAccountInfoDto[].class);
      return Arrays.asList(stockResult);  // 배열을 리스트로 변환
    });

    // 모든 비동기 요청의 결과를 기다림
    CompletableFuture<Void> allFutures = CompletableFuture.allOf(bankFuture, stockFuture);

    // 결과를 하나의 CombinedAccountInfo 객체로 결합
    CombinedAccountInfo combinedAccountInfo = allFutures.thenApply(v -> {
      List<AccountInfoResponseDTO> bankAccounts = bankFuture.join();
      List<SecuritiesAccountInfoDto> stockAccounts = stockFuture.join();
      return new CombinedAccountInfo(bankAccounts, stockAccounts);
    }).get();  // ExecutionException or InterruptedException could be thrown here

    System.out.println("returning");
    return combinedAccountInfo;
  }



}
