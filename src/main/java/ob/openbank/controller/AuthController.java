package ob.openbank.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import ob.openbank.dto.AccessTokenResponseDTO;
import ob.openbank.service.AuthService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @GetMapping("/authorize") // 오픈뱅킹을 사용하시겠습니까?
  public void authorize(
      @RequestParam String response_type,
      @RequestParam String client_id,
      @RequestParam String redirect_uri,
      @RequestParam String scope,
      @RequestParam String state,
      HttpServletResponse response) throws IOException {
    try{
      String authorizationCode = UUID.randomUUID().toString();
      response.sendRedirect(redirect_uri + "?code=" + authorizationCode + "&state=" + state);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  @GetMapping("/token")
  public ResponseEntity<AccessTokenResponseDTO> token(
      @RequestParam String grant_type,
      @RequestParam String code,
      @RequestParam String redirect_uri,
      @RequestParam String client_id,
      @RequestParam String client_secret) {

    AccessTokenResponseDTO accessTokenResponseDTO = new AccessTokenResponseDTO(
        UUID.randomUUID().toString(), "Bearer", 3600, UUID.randomUUID().toString());

    return ResponseEntity.ok(accessTokenResponseDTO);
  }
  @GetMapping("/show-send-form")
  public void showSendForm(HttpServletResponse response) throws IOException {
    ClassPathResource htmlFile = new ClassPathResource("templates/send.html");
    response.setContentType("text/html; charset=UTF-8");
    FileCopyUtils.copy(htmlFile.getInputStream(), response.getOutputStream());
  }

  @PostMapping("/send")
  @ResponseBody
  public Map<String, String> sendSMS(@RequestParam String phoneNumber) {
    String response = authService.sendSMS(phoneNumber);
    Map<String, String> result = new HashMap<>();
    result.put("message", response);
    return result;
  }

  @PostMapping("/verify")
  @ResponseBody
  public Map<String, Object> verifyCode(@RequestParam String phoneNumber, @RequestParam String code) {
    boolean success = authService.verifyCode(phoneNumber, code);
    Map<String, Object> result = new HashMap<>();
    result.put("success", success);
    return result;
  }

}
