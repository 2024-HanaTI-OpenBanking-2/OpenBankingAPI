package ob.openbank.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import ob.openbank.dto.AuthorizeResponseDTO;
import ob.openbank.entity.OpenbankingAuthorizedClient;
import ob.openbank.repository.OpenbankingAuthorizedClientRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  @Autowired
  private OpenbankingAuthorizedClientRepository clientRepository;

  @Value("${coolsms.apikey}")
  private String apiKey;

  @Value("${coolsms.apisecret}")
  private String apiSecret;

  @Value("${coolsms.fromnumber}")
  private String fromNumber;

  private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
  @Transactional
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

  // 인증번호 전송하기
  public String sendSMS(String phoneNumber) {
    Message coolsms = new Message(apiKey, apiSecret);
    String randomNum = createRandomNumber();
    System.out.println("Generated Random Number: " + randomNum);
    HashMap<String, String> params = makeParams(phoneNumber, randomNum);

    try {
      JSONObject obj = (JSONObject) coolsms.send(params);
      System.out.println("Response from CoolSMS: " + obj.toString());
      verificationCodes.put(phoneNumber, randomNum);
    } catch (CoolsmsException e) {
      System.out.println("Error: " + e.getMessage());
      System.out.println("Code: " + e.getCode());
      return "문자 전송 실패: " + e.getMessage();
    }

    return "문자 전송이 완료되었습니다.";
  }

  private String createRandomNumber() {
    Random rand = new Random();
    StringBuilder randomNum = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      String random = Integer.toString(rand.nextInt(10));
      randomNum.append(random);
    }
    return randomNum.toString();
  }

  private HashMap<String, String> makeParams(String to, String randomNum) {
    HashMap<String, String> params = new HashMap<>();
    params.put("from", fromNumber);
    params.put("type", "SMS");
    params.put("app_version", "test app 1.2");
    params.put("to", to);
    params.put("text", "Your verification code is: " + randomNum);
    return params;
  }

  public boolean verifyCode(String phoneNumber, String code) {
    String storedCode = verificationCodes.get(phoneNumber);
    return storedCode != null && storedCode.equals(code);
  }

}
