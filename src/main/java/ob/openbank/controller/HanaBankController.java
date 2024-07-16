package ob.openbank.controller;

import java.util.List;
import ob.openbank.dto.AccountNumDTO;
import ob.openbank.dto.BankStatementDTO;
import ob.openbank.dto.TransactionDTO;
import ob.openbank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/transaction_list")
public class HanaBankController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping("/acnt_num")
  public ResponseEntity<List<BankStatementDTO>> getBankTransaction(@RequestBody TransactionDTO transactionDTO) {
    return ResponseEntity.ok(transactionService.getBankTransactionList(transactionDTO));
  }
}
