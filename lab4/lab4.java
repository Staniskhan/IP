
public class lab4 {
    public static void main(String args[])
    {
        CRTest.generateFile("input_files/lab4_input.txt", 10, 5, 4, 7);
        CollectionRuler clctrlr = new CollectionRuler("input_files/lab4_input.txt");
        clctrlr.fileOutAllInformation("output_files/allInf.txt");
        clctrlr.fileOutNames("output_files/names.txt");
        clctrlr.fileOutAVG("output_files/AVG.txt");
        clctrlr.sortAVGUp();
        clctrlr.fileOutAVG("output_files/sortUpAVG.txt");
        clctrlr.sortAVGDown();
        clctrlr.fileOutAVG("output_files/sortDownAVG.txt");
        clctrlr.sortNameUp();
        clctrlr.fileOutNames("output_files/sortUpName.txt");
        clctrlr.sortNameDown();
        clctrlr.fileOutNames("output_files/sortDownName.txt");
        clctrlr.sortYearGroupUp();
        clctrlr.fileOutAllInformation("output_files/sortUpYearGroup.txt");
        clctrlr.sortYearGroupDown();
        clctrlr.fileOutAllInformation("output_files/sortDownYearGroup.txt");
        clctrlr.fileOutExcellentStudentsAllInf("output_files/ExcellentStudentsAllInf.txt");
        clctrlr.fileOutExcellentStudentsNames("output_files/ExcellentStudentsNames.txt");
        clctrlr.fileOutExcellentStudentsAVG("output_files/ExcellentStudentsAVG.txt");
    }
}
