package ru.briks.services.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.briks.dto.BrickAllProductsDto;

/**
 * @author EGlushkov
 * Date: 28.03.2026
 * Time: 0:24
 */

@Service
public class WebDataService {
    private final RestClient restClient;

    public WebDataService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public BrickAllProductsDto getData(String url, String variantOf, String variantType) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("variant_of", variantOf);
        formData.add("variant_type", variantType);

        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(BrickAllProductsDto.class);
    }
}
