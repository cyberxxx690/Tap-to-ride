package com.traderoot.terminal.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.traderoot.terminal.service.FareService;

/*
 * This controller handles the web traffic for the driver's UI.
 * It serves the HTML page and catches the form submission when the driver taps a card.
 */
@Controller //tells spring boot that this class is a webserver it listens for web traffic
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    /*
     * Loads the main fare screen.
     * We pass the number of queued (unsent) trips to the HTML so the driver knows
     * if they have data waiting to be synced.
     */
    @GetMapping("/") //when you open localhost:8081 in your browser, it sends a GET request. This method catches it.
    public String showFareScreen(Model model) {

        long queuedCount = fareService.getQueuedCount(); //asks the service the number of trips waiting in queue in the local sqlite 

        model.addAttribute("queuedCount", queuedCount); //captures the count and puts its in the queueCount which will be used in the html 
        return "fare";



    }

    //this allows the queuecount to be updated every 3s using javascript since html does reload
    @GetMapping("/api/queue-count")
    @ResponseBody
    public long getQueueCount() {
        return fareService.getQueuedCount();
    }

    /*
     * Catches the POST request when the driver clicks "Tap Card".
     * We grab the riderId and amount from the HTML form, tell the service to save it,
     * and then reload the page with a success message.
     */
    @PostMapping("/tap")
    public String captureFare(@RequestParam String riderId, 
                              @RequestParam String amountInput, 
                              RedirectAttributes redirectAttributes) {
        
        // TODO [Phase 1] Handle the incoming fare request
        // 1. Hardcode a driverId (e.g., "DRIVER_01")
        // 2. Call fareService.captureFare(...)
        // 3. Add a success message to redirectAttributes (using addFlashAttribute)
        // 4. Return "redirect:/" to reload the page
        

        //uses regular expression to ensure the right amount format is typed
        if (!amountInput.matches("\\d+\\.\\d{2}")) {
            redirectAttributes.addFlashAttribute(
                "errorMessage", "Enter a valid amount, e.g. 12.00");
            return "redirect:/";
        }

        BigDecimal amount = new BigDecimal(amountInput);
        String driverId = "Driver_01";

        fareService.captureFare(riderId, driverId, amount);

        redirectAttributes.addFlashAttribute("successMessage", "Fare captured successfully");

        
        return "redirect:/";
    }
}
