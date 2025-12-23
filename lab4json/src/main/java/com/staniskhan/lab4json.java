package com.staniskhan;

public class lab4json {
    public static void main(String[] args) {
        CRTestjson.generateFile("input_files/lab4_input.json", 10, 5, 4, 7);
        CollectionRulerjson clctrlr = new CollectionRulerjson("input_files/lab4_input.json", true);
        clctrlr.saveToTxt("output_files/allInf.txt");


        // CRTestjson.generateFile("input_files/lab4_input.json", 10, 5, 4, 7);
        // CollectionRulerjson clctrlr = new CollectionRulerjson("input_files/lab4_input.txt", false);
        // clctrlr.fileOutAllInformation("output_files/allInf.json");
    }
}