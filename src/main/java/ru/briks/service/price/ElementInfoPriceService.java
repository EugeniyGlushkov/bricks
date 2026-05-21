package ru.briks.service.price;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.briks.dao.ElementExternalIdDao;
import ru.briks.dao.ElementInfoDao;
import ru.briks.dao.ElementDao;
import ru.briks.dao.PartDao;
import ru.briks.dto.BrickProductDto;
import ru.briks.dto.VariantDto;
import ru.briks.entity.Element;
import ru.briks.entity.ElementExternalId;
import ru.briks.entity.ElementInfo;
import ru.briks.entity.State;
import ru.briks.service.web.WebDataService;
import ru.briks.settings.VariantsSettings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private ElementExternalIdDao elementExternalIdDao;
    @Autowired
    private PartDao partDao;

    @Transactional
    public void downloadPrices(VariantDto variant) throws InterruptedException {
        List<BrickProductDto> products = webDataService
                .getData(detailsUrl, variant.getVariantOf(), variant.getVariantType())
                .getProducts()
                .stream()
                .peek((product) ->
                        product.setPrice(product.getPrice().replace("р.", "").trim()))
                .toList();

        List<String> models = products.stream()
                .map(BrickProductDto::getModel)
                .filter(StringUtils::isNotBlank)
                .toList();

        Map<String, Element> elementIdToElements =
                elementExternalIdDao.findAllByExternalIds(models).stream()
                        .collect(Collectors.toMap(ElementExternalId::getExternalId, ElementExternalId::getElement));

        LocalDateTime startUpdating = LocalDateTime.now();

        if (!elementIdToElements.isEmpty()) {
            List<ElementInfo> elementInfos = products.stream()
                    .filter(prod -> elementIdToElements.containsKey(prod.getModel()))
                    .map(prod -> elementInfoDao.findByElementIdAndState(
                                    elementIdToElements.get(prod.getModel()).getId(),
                                    State.ofCode(prod.getManufacturer()))
                            .orElse(ElementInfo.builder()
                                    .element(elementIdToElements.get(prod.getModel()))
                                    .state(State.ofCode(prod.getManufacturer()))
                                    .build())
                            .setPriceKuboka(BigDecimal.valueOf(Double.parseDouble(prod.getPrice().replace(" ", ""))))
                            .setPriceKubokaUpdated(LocalDateTime.now()))
                    .toList();
            elementInfoDao.saveAll(elementInfos);
        }

        Map<String, List<Element>> colorNameToElements = elementDao.findAllByPartNum(variant.getVariantOf())
                .stream()
                .collect(Collectors.toMap(el -> el.getColor().getName(),
                        el -> new ArrayList<>(List.of(el)),
                        (oldVal, newVal) -> {
                            oldVal.addAll(newVal);
                            return oldVal;
                        }));

        if (!colorNameToElements.isEmpty()) {
            Map<String, List<Element>> finalColorNameToElements = colorNameToElements;
            List<ElementInfo> elementInfos = new ArrayList<>();

            for (BrickProductDto prod : products) {
                if (!finalColorNameToElements.containsKey(prod.getColorEn())) {
                    continue;
                }
                for (Element el : finalColorNameToElements.get(prod.getColorEn())) {
                    Optional<ElementInfo> elementInfoOpt = elementInfoDao.findByElementIdAndState(
                            el.getId(),
                            State.ofCode(prod.getManufacturer()));
                    if (elementInfoOpt.isEmpty() ||
                            elementInfoOpt.get().getPriceKubokaUpdated().isBefore(startUpdating)) {
                        ElementInfo elementInfo = elementInfoOpt
                                .orElse(ElementInfo.builder()
                                        .element(el)
                                        .state(State.ofCode(prod.getManufacturer()))
                                        .build())
                                .setPriceKuboka(BigDecimal.valueOf(Double.parseDouble(prod.getPrice())))
                                .setPriceKubokaUpdated(LocalDateTime.now());
                        elementInfos.add(elementInfo);
                    }
                }
            }

            /*List<ElementInfo> elementInfos = products.stream()
                    .filter(prod -> finalColorNameToElements.containsKey(prod.getColorEn()))
                    .flatMap(prod -> finalColorNameToElements.get(prod.getColorEn()).stream()
                            .map(el -> {
                                        Optional<ElementInfo> elementInfo = elementInfoDao.findByElementIdAndState(
                                                el.getId(),
                                                State.ofCode(prod.getManufacturer()));
                                        if (elementInfo.isEmpty() ||
                                                elementInfo.get().getPriceKubokaUpdated().isBefore(startUpdating)) {
                                            return elementInfo
                                                    .orElse(ElementInfo.builder()
                                                            .element(el)
                                                            .state(State.ofCode(prod.getManufacturer()))
                                                            .build())
                                                    .setPriceKuboka(BigDecimal.valueOf(Double.parseDouble(prod.getPrice())))
                                                    .setPriceKubokaUpdated(LocalDateTime.now());
                                        } else {
                                            return null;
                                        }
                                    }
                            )
                    )
                    .toList();*/
            elementInfoDao.saveAll(elementInfos);
        }

        colorNameToElements = elementDao.findAllByBricklinkNum(variant.getVariantOf()).stream()
                .collect(Collectors.toMap(el -> el.getColor().getName(),
                        el -> new ArrayList<>(List.of(el)),
                        (oldVal, newVal) -> {
                            oldVal.addAll(newVal);
                            return oldVal;
                        }));

        if (!colorNameToElements.isEmpty()) {
            Map<String, List<Element>> finalColorNameToElements = colorNameToElements;
            List<ElementInfo> elementInfos = new ArrayList<>();

            for (BrickProductDto prod : products) {
                if (!finalColorNameToElements.containsKey(prod.getColorEn())) {
                    continue;
                }
                for (Element el : finalColorNameToElements.get(prod.getColorEn())) {
                    Optional<ElementInfo> elementInfoOpt = elementInfoDao.findByElementIdAndState(
                            el.getId(),
                            State.ofCode(prod.getManufacturer()));
                    if (elementInfoOpt.isEmpty() ||
                            elementInfoOpt.get().getPriceKubokaUpdated().isBefore(startUpdating)) {
                        ElementInfo elementInfo = elementInfoOpt
                                .orElse(ElementInfo.builder()
                                        .element(el)
                                        .state(State.ofCode(prod.getManufacturer()))
                                        .build())
                                .setPriceKuboka(BigDecimal.valueOf(Double.parseDouble(prod.getPrice())))
                                .setPriceKubokaUpdated(LocalDateTime.now());
                        elementInfos.add(elementInfo);
                    }
                }
            }

            /*Map<String, List<Element>> finalColorNameToElements = colorNameToElements;
            List<ElementInfo> elementInfos = products.stream()
                    .filter(prod -> finalColorNameToElements.containsKey(prod.getColorEn()))
                    .flatMap(prod -> finalColorNameToElements.get(prod.getColorEn()).stream()
                            .map(el -> elementInfoDao.findByElementIdAndState(
                                            el.getId(),
                                            State.ofCode(prod.getManufacturer()))
                                    .orElse(ElementInfo.builder()
                                            .element(el)
                                            .state(State.ofCode(prod.getManufacturer()))
                                            .build())
                                    .setPriceKuboka(BigDecimal.valueOf(Double.parseDouble(prod.getPrice())))
                                    .setPriceKubokaUpdated(LocalDateTime.now())))

                    .toList();*/
            elementInfoDao.saveAll(elementInfos);
        }
    }
}