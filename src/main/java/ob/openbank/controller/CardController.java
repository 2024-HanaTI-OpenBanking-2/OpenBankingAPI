package ob.openbank.controller;

import ob.openbank.dto.*;
import ob.openbank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/card-list")
    public ResponseEntity<List<CustomerCardInfoDTO>> getCardList(@RequestBody AccessTokenDTO accessToken) {
        try {
            List<CustomerCardInfoDTO> customerCardInfoDTOList = cardService.getCustomerCardInfo(accessToken.getAccess_token());
            return ResponseEntity.ok(customerCardInfoDTOList);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 상태 코드와 메시지를 적절히 반환
        }
    }

    @PostMapping("/card-approval")
    public ResponseEntity<List<CardCustomerApprovalDTO>> getCardApprovalList(@RequestBody CardApprovalRequestDTO cardApprovalRequestDTO) {
        try {
            List<CardCustomerApprovalDTO> result = cardService.getCardApprovalList(cardApprovalRequestDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/account-list")
    public ResponseEntity<List<AccountInfoResponseDTO>> getAccountList(@RequestBody CiDTO ciDTO) {
        List<AccountInfoResponseDTO> result = cardService.getAccountList(ciDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/paymoney-charge")
    private ResponseEntity<PayMoneyChargeResponseDTO> getPayMoneyCharge(@RequestBody PayMoneyChargeRequestDTO requestDTO) {
        PayMoneyChargeResponseDTO responseDTO = cardService.getPayMoneyCharge(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


}
