package ob.openbank.service;

import java.util.List;
import ob.openbank.dto.AccountNumDTO;
import ob.openbank.dto.BankStatementDTO;
import ob.openbank.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

  @Autowired
  private RestTemplate restTemplate;
  public List<BankStatementDTO> getBankTransactionList(TransactionDTO transactionDTO) {
    String bankUrl = "http://localhost:8083/account/transaction_list/acnt_num";
    AccountNumDTO accountNumDTO = new AccountNumDTO(transactionDTO.getAccountNum());
    List<BankStatementDTO> bankResult = List.of(
        restTemplate.postForObject(bankUrl, accountNumDTO, BankStatementDTO[].class));
    return bankResult;
  }
}
