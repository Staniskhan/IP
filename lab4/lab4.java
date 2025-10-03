
public class lab4 {
    public void main(String args[])
    {
        CRTest.generateFile("lab4.txt", 10, 5, 4, 7);
        CollectionRuler clctrlr = new CollectionRuler("lab4.txt");
        clctrlr.fileOutAllInformation("allInf.txt");
        clctrlr.fileOutNames("names.txt");
        clctrlr.fileOutAVG("AVG.txt");
        clctrlr.sortAVGUp();
        clctrlr.fileOutAVG("sortUpAVG.txt");
        clctrlr.sortAVGDown();
        clctrlr.fileOutAVG("sortDownAVG.txt");
        clctrlr.sortNameUp();
        clctrlr.fileOutNames("sortUpName.txt");
        clctrlr.sortNameDown();
        clctrlr.fileOutNames("sortDownName.txt");
        clctrlr.sortYearGroupUp();
        clctrlr.fileOutAllInformation("sortUpYearGroup.txt");
        clctrlr.sortYearGroupDown();
        clctrlr.fileOutAllInformation("sortDownYearGroup.txt");
        clctrlr.fileOutExcellentStudentsAllInf("ExcellentStudentsAllInf.txt");
        clctrlr.fileOutExcellentStudentsNames("ExcellentStudentsNames.txt");
        clctrlr.fileOutExcellentStudentsAVG("ExcellentStudentsAVG.txt");
    }
}
