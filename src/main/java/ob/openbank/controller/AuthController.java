package ob.openbank.controller;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import ob.openbank.dto.AccessTokenResponseDTO;

import ob.openbank.dto.RequestAuthCodeResponseDTO;
import ob.openbank.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/authorize") // 오픈뱅킹을 사용하시겠습니까??
    public void authorize(
            @RequestParam String response_type,
            @RequestParam String client_id,
            @RequestParam String redirect_uri,
            @RequestParam String scope,
            @RequestParam String state,
            @RequestParam String ci,
            @RequestParam String customer_id,
            HttpServletResponse response) throws IOException {
        try {
            String redirectUrl = authService.authorize2(response_type, client_id, redirect_uri, scope, state, ci, customer_id);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
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

        AccessTokenResponseDTO accessTokenResponseDTO = authService.generateAccessToken(
                grant_type, code, redirect_uri, client_id, client_secret);

        return ResponseEntity.ok(accessTokenResponseDTO);
    }
    @GetMapping("/show-send-form")
    public void showSendForm(@RequestParam("client_id") String clientId, HttpServletResponse response) throws IOException {
        authService.validateAndSendForm(clientId, response);
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

    @GetMapping("/request-auth-code")
    public ResponseEntity<?> requestAuthCode(@RequestParam String access_token) {
        try {
            RequestAuthCodeResponseDTO responseDTO = authService.generateAuthCode(access_token);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
