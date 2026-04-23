package ru.briks.controllers.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.briks.dto.ElementOfferDto;
import ru.briks.dto.PartAdminDto;
import ru.briks.entity.State;
import ru.briks.service.ElementService;
import ru.briks.service.PartService;

/**
 * @author EGlushkov
 * Date: 17.04.2026
 * Time: 13:21
 */

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PartService partService;
    private final ElementService elementService;

    public AdminController(PartService partService, ElementService elementService) {
        this.partService = partService;
        this.elementService = elementService;
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
}
