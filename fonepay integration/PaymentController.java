package com.apptechnosoft.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import com.apptechnosoft.configuration.FonepayConfig;
import com.apptechnosoft.utils.HashUtil;

@RestController
public class PaymentController {
    @Autowired private FonepayConfig fonepayConfig;

    @PostMapping("/initiatePayment")
    public RedirectView initiatePayment(@RequestParam("amount") String amountStr) {
        String pid = fonepayConfig.getPid();             // Merchant PID from config
        String secret = fonepayConfig.getSecret();       // Shared secret
        String baseUrl = fonepayConfig.getBaseUrl();     // e.g. dev-clientapi.fonepay.com/api
        String returnUrl = fonepayConfig.getReturnUrl(); // e.g. https://your-domain.com/payment/verify

        // Prepare fields
        String prn = java.util.UUID.randomUUID().toString();        // Unique PRN
        String md = "P";                                           // Mode = P (payment)
        String crn = "NPR";                                        // Currency = NPR
        String dt = java.time.LocalDate.now()
                         .format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String r1 = "Test Payment";                                // e.g. description
        String r2 = "Invoice #1234";                              // optional
        // Concatenate fields for DV (per spec: PID,MD,PRN,AMT,CRN,DT,R1,R2,RU)
        String message = String.join(",",
            pid, md, prn, amountStr, crn, dt, r1, r2, returnUrl
        );
        String dv = HashUtil.hmacSha512(secret, message);

        // Build Fonepay redirect URL with all params
        String redirectUrl = UriComponentsBuilder.fromHttpUrl(baseUrl + "/merchantRequest")
            .queryParam("PID", pid)
            .queryParam("MD", md)
            .queryParam("PRN", prn)
            .queryParam("AMT", amountStr)
            .queryParam("CRN", crn)
            .queryParam("DT", dt)
            .queryParam("R1", r1)
            .queryParam("R2", r2)
            .queryParam("DV", dv)
            .queryParam("RU", returnUrl)
            .build().toUriString();

        return new RedirectView(redirectUrl);
    }
    
    @GetMapping("/payment/verify")
    public ResponseEntity<String> handleFonepayResponse(
        @RequestParam("PRN") String prn,
        @RequestParam("PID") String pid,
        @RequestParam("PS")  String ps,
        @RequestParam("RC")  String rc,
        @RequestParam("UID") String uid,
        @RequestParam("BC")  String bc,
        @RequestParam("INI") String ini,
        @RequestParam("P_AMT") String paidAmt,
        @RequestParam("R_AMT") String reqAmt,
        @RequestParam("DV")  String dv
    ) {
        String secret = fonepayConfig.getSecret();

        // Reconstruct the message for DV = PRN,PID,PS,RC,UID,BC,INI,P_AMT,R_AMT
        String responseData = String.join(",", 
            prn, pid, ps, rc, uid, bc, ini, paidAmt, reqAmt
        );
        String calculatedDV = HashUtil.hmacSha512(secret, responseData);

        // Compare calculated DV with Fonepay's DV
        if (!calculatedDV.equalsIgnoreCase(dv)) {
            // DV mismatch – possible tampering
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid DV");
        }

        // DV matched – safe to check payment status
        if ("true".equalsIgnoreCase(ps)) {
            // Payment was successful
            return ResponseEntity.ok("Payment successful (PRN: " + prn + ")");
        } else {
            // Payment failed or cancelled
            return ResponseEntity.ok("Payment failed or canceled (PRN: " + prn + ")");
        }
    }

    
    
}
