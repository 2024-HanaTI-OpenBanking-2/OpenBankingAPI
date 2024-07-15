package ob.openbank.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import ob.openbank.dto.AccessTokenResponseDTO;
import ob.openbank.dto.AuthorizeResponseDTO;
import ob.openbank.dto.RequestAuthCodeResponseDTO;
import ob.openbank.entity.OpenbankingAuthentication;
import ob.openbank.entity.OpenbankingAuthorizedClient;
import ob.openbank.repository.OpenbankingAuthenticationRepository;
import ob.openbank.repository.OpenbankingAuthorizedClientRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Autowired
    private OpenbankingAuthorizedClientRepository clientRepository;

    @Autowired
    private OpenbankingAuthenticationRepository authenticationRepository;

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @PersistenceContext
    private EntityManager entityManager;


    public void validateAndSendForm(String clientId, HttpServletResponse response) throws IOException {
        Integer count = clientRepository.existsByClientIdCustom(clientId);
        if (count == null || count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client ID not found");
        }

        ClassPathResource htmlFile = new ClassPathResource("templates/send.html");
        response.setContentType("text/html; charset=UTF-8");
        FileCopyUtils.copy(htmlFile.getInputStream(), response.getOutputStream());
    }

    public Long getNextSequenceValue() {
        // 시퀀스의 다음 값 가져오기
        return ((BigDecimal) entityManager.createNativeQuery("SELECT seq_no_seq.NEXTVAL FROM dual").getSingleResult()).longValue();
    }

    @Transactional
    public AccessTokenResponseDTO generateAccessToken(String grantType, String code, String redirectUri, String clientId, String clientSecret) {
        OpenbankingAuthentication auth = authenticationRepository.findByAuthorizationCode(code)
                .orElseThrow(() -> new RuntimeException("Authorization code not found"));

        // 계산된 seq_no 만들기
        Long nextSeqNo = getNextSequenceValue(); // 본인인증을 2번 진행하면 user_seq_no가 update 되는 문제가 있음
        String formattedSeqNo = String.format("%010d", nextSeqNo);

        // 새 토큰 정보 생성
        String newAccessToken = UUID.randomUUID().toString();
        String newRefreshToken = UUID.randomUUID().toString();

        // 테이블 업데이트
        auth.setAccessTokenId(newAccessToken);
//        auth.setTokenType("Bearer");
        auth.setExpiresIn(7776000); // 90 days in seconds
        auth.setRefreshToken(newRefreshToken);
        auth.setSeqNo(Long.valueOf(formattedSeqNo));
        auth.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        authenticationRepository.save(auth);

        // AccessTokenResponseDTO 생성
        return AccessTokenResponseDTO.builder()
                .access_token(newAccessToken)
//                .token_type("Bearer")
                .expires_in(7776000)
                .refresh_token(newRefreshToken)
//                .seq_no(formattedSeqNo)
                .build();
    }


    @Transactional(rollbackFor = Exception.class)
    public String authorize2(String responseType, String clientId, String redirectUri, String scope, String state, String ci, String customerId) throws Exception {
        try {
            OpenbankingAuthentication auth = authenticationRepository.findById(ci).orElse(new OpenbankingAuthentication());
            String authorizationCode = UUID.randomUUID().toString();

            if (auth.getCi() == null) { // New CI, setting values
                auth.setCi(ci);
                auth.setClientId(clientId);
                auth.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            }
            // Update or set new values
            auth.setAuthorizationCode(authorizationCode);
            auth.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            authenticationRepository.save(auth);

            return redirectUri + "?code=" + authorizationCode + "&state=" + state + "&customer_id=" + customerId;
        } catch (Exception e) {
            throw new Exception("Authorization failed", e);
        }
    }


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


    @Transactional
    public RequestAuthCodeResponseDTO generateAuthCode(String accessToken) {
        // Generate auth code logic
        String authCode = UUID.randomUUID().toString();  // Placeholder for actual auth code generation logic

        // Save auth code with associated access token in the database
        OpenbankingAuthentication auth = authenticationRepository.findByAccessTokenId(accessToken)
            .orElseThrow(() -> new RuntimeException("Invalid access token: " + accessToken));

        auth.setAuthCode(authCode);
        auth.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        authenticationRepository.save(auth);

        return RequestAuthCodeResponseDTO.builder()
            .authCode(authCode)
            .accessToken(accessToken)
            .build();
    }
}
