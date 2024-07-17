package ob.openbank.service;

import ob.openbank.dto.*;
import ob.openbank.repository.OpenbankingAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.*;

@Service
public class CardService {
    @Autowired
    private OpenbankingAuthenticationRepository authenticationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${card.server.url}")
    private String cardServerUrl;

    public List<CustomerCardInfoDTO> getCustomerCardInfo(String accessToken) {
        String ci = authenticationRepository.findCiByAccessToken(accessToken)
                .orElseThrow(() -> new RuntimeException("Invalid access token: " + accessToken));
        CiDTO ciDTO = new CiDTO(ci);
        System.out.println(ciDTO);
        String cardUrl = "http://localhost:8082/api/card-list-ci";

        // REST 템플릿을 사용하여 다른 서비스로 CI를 기반으로 하는 요청을 보냅니다.
        try {
            ResponseEntity<CustomerCardInfoDTO[]> response = restTemplate.postForEntity(cardUrl, ciDTO, CustomerCardInfoDTO[].class);
            System.out.println(response);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Arrays.asList(response.getBody());
            } else {
                throw new RuntimeException("Failed to fetch card info from card service");
            }
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("HTTP error occurred: " + ex.getStatusCode());
        }
    }

    public List<CardCustomerApprovalDTO> getCardApprovalList(CardApprovalRequestDTO cardApprovalRequestDTO) {
        String url = cardServerUrl + "/card-approval-list";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CardApprovalRequestDTO> requestEntity = new HttpEntity<>(cardApprovalRequestDTO, headers);

        ResponseEntity<CardCustomerApprovalDTO[]> response = restTemplate.postForEntity(url, requestEntity, CardCustomerApprovalDTO[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            if(response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }else{
                throw new NullPointerException("Response is null in getCardApprovalList");
            }
        } else {
            throw new RuntimeException("Failed to get approval list: " + response.getStatusCode());
        }
    }
}
