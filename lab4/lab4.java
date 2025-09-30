
public class lab4 {
    public void main(String args[])
    {
        // String rest = "1 2 ova 5 1 6 linal 6 1 4";
        // StudRecordBook srb = new StudRecordBook("surname", "name", "second_name", 2, 7, 3, rest);
        // srb.print();
        // System.out.println("\n\n\n");
        // System.out.println(srb.getAllExamMarks()[0]);
        // System.out.println(srb.getAllExamMarks()[1]);
        // System.out.println(CollectionRuler.AVGAllSemsMark(srb));
        // System.out.println(CollectionRuler.AVGAllExamsMark(srb));
        // System.out.println(CollectionRuler.AVG(srb));
        // srb.fileOut("output.txt");
        CollectionRuler clctrlr = new CollectionRuler("input.txt");
        clctrlr.fileOutAVG("output.txt");
        clctrlr.sortAVGUp();
        clctrlr.fileOutAVG("output.txt");
        clctrlr.sortAVGDown();
        clctrlr.fileOutAVG("output.txt");
        clctrlr.sortNameUp();
        clctrlr.fileOutAVG("output.txt");
        clctrlr.sortNameDown();
        clctrlr.fileOutAVG("output.txt");
        clctrlr.sortYearGroupUp();
        clctrlr.fileOutAllInformation("output.txt");
        clctrlr.sortYearGroupDown();
        clctrlr.fileOutAllInformation("output.txt");
        clctrlr.fileOutExcellentStudentsAllInf("output.txt");
        clctrlr.fileOutExcellentStudentsNames("output.txt");
        clctrlr.fileOutExcellentStudentsAVG("output.txt");
    }
}
