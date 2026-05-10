package ru.briks.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.briks.dto.ElementInfoFormDto;
import ru.briks.dto.ElementOfferDto;
import ru.briks.dto.PartAdminDto;
import ru.briks.entity.State;
import ru.briks.service.ElementInfoService;
import ru.briks.service.ElementService;
import ru.briks.service.PartCategoryService;
import ru.briks.service.PartService;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author EGlushkov
 * Date: 17.04.2026
 * Time: 13:21
 */

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final String FLASH_MSG_KEY = "flashMessage";
    private static final String SUCCESS_SAVE_MSG = "✅ Предложение успешно сохранено.";
    private static final String SUCCESS_DELETE_MSG = "🗑️ Предложение удалено.";
    private static final String ALL_STATES_CREATED_MSG = "✅ Все возможные состояния для этого элемента уже созданы.";

    private final PartService partService;
    private final ElementService elementService;
    private final ElementInfoService elementInfoService;
    private final PartCategoryService partCategoryService;

    public AdminController(PartService partService,
                           ElementService elementService,
                           ElementInfoService elementInfoService,
                           PartCategoryService partCategoryService) {
        this.partService = partService;
        this.elementService = elementService;
        this.elementInfoService = elementInfoService;
        this.partCategoryService = partCategoryService;
    }

    @GetMapping
    public String dashboard() {
        return "admin/index";
    }

    @GetMapping("/parts")
    public String listParts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        var pageable = PageRequest.of(page, size, Sort.by("partNum").ascending());
        Page<PartAdminDto> parts = partService.findPartsForAdmin(search, pageable);

        model.addAttribute("parts", parts);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", parts.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/parts";
    }

    @GetMapping("/parts/{partId}/offers")
    public String listOffers(
            @PathVariable Long partId,
            @RequestParam(required = false) State state,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        var pageable = PageRequest.of(page, size, Sort.by("colorName", "state").ascending());
        Page<ElementOfferDto> offers = elementService.findOffersByPart(partId, state, inStock, pageable);

        model.addAttribute("partId", partId);
        model.addAttribute("offers", offers);
        model.addAttribute("states", State.values());
        model.addAttribute("selectedState", state);
        model.addAttribute("inStock", inStock);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", offers.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/part-offers";
    }

    @GetMapping("/offers/{id}/edit")
    public String editOffer(@PathVariable Long id, Model model) {
        var info = elementInfoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + id));

        var form = new ElementInfoFormDto();
        form.setId(info.getId());
        form.setElementId(info.getElement().getId());
        form.setPartId(info.getElement().getPart().getId());
        form.setState(info.getState());
        form.setCount(info.getCount());
        form.setPrice(info.getPrice());
        form.setPriceKuboka(info.getPriceKuboka());

        model.addAttribute("form", form);
        model.addAttribute("isEdit", true);
        return "admin/offer-form";
    }

    @GetMapping("/offers/new")
    public String newOffer(@RequestParam Long elementId,
                           @RequestParam Long partId,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        Set<State> existing = elementInfoService.getExistingStates(elementId);
        Set<State> available = EnumSet.allOf(State.class);
        available.removeAll(existing);

        if (available.isEmpty()) {
            redirectAttributes.addFlashAttribute(FLASH_MSG_KEY, ALL_STATES_CREATED_MSG);
            return "redirect:/admin/parts/%d/offers".formatted(partId);
        }

        var form = new ElementInfoFormDto();
        form.setElementId(elementId);
        form.setPartId(partId);
        form.setCount(0L);

        model.addAttribute("form", form);
        model.addAttribute("isEdit", false);
        model.addAttribute("availableStates", available);
        return "admin/offer-form";
    }

    @PostMapping("/offers/save")
    public String saveOffer(@Valid @ModelAttribute("form") ElementInfoFormDto form,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", form.getId() != null);
            // При ошибке валидации нужно восстановить список доступных состояний
            if (form.getId() == null) {
                Set<State> existing = elementInfoService.getExistingStates(form.getElementId());
                Set<State> available = EnumSet.allOf(State.class);
                available.removeAll(existing);
                model.addAttribute("availableStates", available);
            }
            return "admin/offer-form";
        }

        elementInfoService.saveFromForm(form);
        redirectAttributes.addFlashAttribute(FLASH_MSG_KEY, SUCCESS_SAVE_MSG);
        return "redirect:/admin/parts/" + form.getPartId() + "/offers";
    }

    @PostMapping("/offers/{id}/delete")
    public String deleteOffer(@PathVariable Long id, @RequestParam Long partId, RedirectAttributes redirectAttributes) {
        elementInfoService.deleteById(id);
        redirectAttributes.addFlashAttribute(FLASH_MSG_KEY, SUCCESS_DELETE_MSG);
        return "redirect:/admin/parts/" + partId + "/offers";
    }

    @GetMapping("/reports")
    public String reportsPage(Model model) {
        model.addAttribute("categories", partCategoryService.getAllSortedByName()); // Вернёт List<Category>
        model.addAttribute("states", State.values());
        return "admin/reports";
    }
}