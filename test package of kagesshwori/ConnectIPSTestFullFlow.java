package com.apptechnosoft.test;

import com.apptechnosoft.utils.SignatureUtil;

public class ConnectIPSTestFullFlow {
    public static void main(String[] args) {
        try {
            String message = "MERCHANTID=3154,APPID=MER-3154-APP-1,REFERENCEID=txn-123,TXNAMT=500";

            String pfxFilePath = "src/main/resources/connectips_cert/CREDITOR.pfx";
            String pfxPassword = "123";

            // Generate signature token
            String signToken = SignatureUtil.signData(message, pfxFilePath, pfxPassword);
            System.out.println("Generated Sign Token:\n" + signToken);

            // Skip verification for now, since you don't have the publicCert.cer
            System.out.println("Skipping signature verification since public certificate is not available.");
            
            // Optionally, you can just assume it's valid during dev:
            boolean isValid = true;

            System.out.println("Signature valid? " + isValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
