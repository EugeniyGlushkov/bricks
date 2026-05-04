package ru.briks.service.price;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.briks.dao.ElementInfoDao;
import ru.briks.dao.ElementDao;
import ru.briks.service.web.WebDataService;
import ru.briks.settings.VariantsSettings;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Slf4j
@Service
public class ElementInfoPriceService {
    @Value("${urls.details}")
    private String detailsUrl;
    @Autowired
    private VariantsSettings variants;
    @Autowired
    private WebDataService webDataService;
    @Autowired
    private ElementDao elementDao;
    @Autowired
    private ElementInfoDao elementInfoDao;

    //TODO uncomment and fix elementId
    /*public void downloadPrices() throws InterruptedException {
        for (Map.Entry<String, List<VariantDto>> entry : variants.getVariants().entrySet()) {
            List<VariantDto> variants = entry.getValue();

            for (VariantDto variant : variants) {
                List<BrickProductDto> products = webDataService
                        .getData(detailsUrl, variant.getVariantOf(), variant.getVariantType())
                        .getProducts()
                        .stream()
                        .peek((product) ->
                                product.setPrice(product.getPrice().replace("р.", "").trim()))
                        .toList();
                List<String> models = products.stream()
                        .map(BrickProductDto::getModel)
                        .toList();
                Map<String, Element> elementIdToElements = elementDao.findByElementIds(models).stream()
                        .collect(Collectors.toMap(Element::getElementId, e -> e));

                List<ElementInfo> elementInfos = products.stream()
                        .map(prod -> elementInfoDao.findByElementIdAndState(
                                        elementIdToElements.get(prod.getModel()).getId(),
                                        State.ofCode(prod.getManufacturer()))
                                .orElse(ElementInfo.builder()
                                        .element(elementIdToElements.get(prod.getModel()))
                                        .state(State.ofCode(prod.getManufacturer()))
                                        .build())
                                .setPriceKuboka(BigDecimal.valueOf(Double.parseDouble(prod.getPrice())))
                                .setPriceKubokaUpdated(LocalDateTime.now()))
                        .toList();
                elementInfoDao.saveAll(elementInfos);

                TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 1);
            }
        }
    }*/
}