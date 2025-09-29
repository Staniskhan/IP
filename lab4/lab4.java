public class lab4 {
    public void main(String args[])
    {
        String rest = "1 2 ova 5 1 6 linal 6 1 4";
        StudRecordBook srb = new StudRecordBook("surname", "name", "second_name", 2, 7, 3, rest);
        srb.print();
        System.out.println("\n\n\n");
        System.out.println(srb.getAllExamMarks()[0]);
        System.out.println(srb.getAllExamMarks()[1]);
        // System.out.println(Handler.AVGAllSemsMark(srb));
        // System.out.println(Handler.AVGAllExamsMark(srb));
        
    }
}
