import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class testExam
{
    private static String mainMenu = 
    "1. Add a student.\n" +
    "2. Add a student's grade for a subject." +
    "3. Get a student's GPA." + 
    "4. Get a list of students with the GPA is higher than a set value" +
    "5. Exit";

    Map<Integer, Student> students = new HashMap<>();
    List<Grade> grades = new ArrayList<>();
    public static void main(String args[])
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println(mainMenu);
        // add the exception catch
        scanner.nextInt();



        scanner.close();
    }
}
