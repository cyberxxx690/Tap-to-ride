package com.traderoot.server.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.traderoot.server.model.Charge;
import com.traderoot.server.repository.BatchRepository;
import com.traderoot.server.repository.ChargeRepository;
import com.traderoot.server.repository.TripRepository;
import com.traderoot.server.service.SettlementService;

@Controller
public class ConsoleController {

    private final SettlementService settlementService;
    private final TripRepository tripRepository;
    private final BatchRepository batchRepository;
    private final ChargeRepository chargeRepository;

    public ConsoleController(SettlementService settlementService, TripRepository tripRepository, 
                             BatchRepository batchRepository, ChargeRepository chargeRepository) {
        this.settlementService = settlementService;
        this.tripRepository = tripRepository;
        this.batchRepository = batchRepository;
        this.chargeRepository = chargeRepository;
    }

    @GetMapping("/console")
    public String showConsole(Model model) {
        // Fetch stats from the database
        long totalTrips = tripRepository.count();
        long unsettledTrips = tripRepository.countByStatus("RECEIVED");
        long settledCharges = chargeRepository.count();

        // Pack them into the Model to ship to the HTML
        model.addAttribute("totalTrips", totalTrips);
        model.addAttribute("unsettledTrips", unsettledTrips);
        model.addAttribute("settledCharges", settledCharges);
        
        return "console"; // src/main/resources/templates/console.html
    }

    @GetMapping("/charges")
    public String showCharges(Model model) {
        // TODO [Phase 7] Show today's settled charges
        // 1. Fetch charges for today using settlementService.getChargesForDate(...)
        // 2. Add to model



        List<Charge> charges = settlementService.getChargesForDate(LocalDate.now());
        model.addAttribute("Charges", charges);
        
        return "charges"; // src/main/resources/templates/charges.html
    }

    @PostMapping("/settle")
    public String runSettlement(RedirectAttributes redirectAttributes) {
        // TODO [Phase 7] Trigger the settlement engine
        // 1. Call settlementService.settleTrips(LocalDate.now())
        // 2. Add a flash attribute message

        List<Charge> charges = settlementService.settleTrips(LocalDate.now());
        redirectAttributes.addFlashAttribute("successMessage", charges.size() + " charges created successfully");
        
        return "redirect:/console";
    }
}
