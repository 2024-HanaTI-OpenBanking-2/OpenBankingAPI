package ob.openbank.controller;

import lombok.RequiredArgsConstructor;
import ob.openbank.dto.AuthorizeResponseDTO;
import ob.openbank.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @GetMapping("/authorize")
  public ResponseEntity<AuthorizeResponseDTO> authorize(
      @RequestParam("response_type") String responseType,
      @RequestParam("client_id") String clientId,
      @RequestParam("redirect_uri") String redirectUri,
      @RequestParam("scope") String scope) {

    try {
      AuthorizeResponseDTO response = authService.authorize(responseType, clientId, redirectUri, scope);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      // 여기서 필요한 에러 처리 로직을 추가할 수 있습니다.
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }
}
