public class Handler {
    public static double AVGAllSemsMark(StudRecordBook srb)
    {
        int[] arr = srb.getAllSemesterMarks();
        int count = arr.length;
        double avg = 0;
        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i] >= 0)
            {
                avg += (double)arr[i];
            }
            else
            {
                count--;
            }
        }

        avg /= (double)count;
        return avg;
    }

    public static double AVGAllExamsMark(StudRecordBook srb)
    {
        int[] arr = srb.getAllExamMarks();
        int count = arr.length;
        double avg = 0;
        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i] >= 0)
            {
                avg += (double)arr[i];
            }
            else
            {
                count--;
            }
        }

        avg /= (double)count;
        return avg;
    }
}
