import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class testExam
{    
    public static void main(String args[])
    {
        Map<Integer, Student> students = new HashMap<>();
        List<Grade> grades = new ArrayList<>();


        try (Scanner scanner = new Scanner(System.in)) {
            int choice = 0;
            
            while (choice != 5)
            {
                choice = askPositiveNZeroInt(
                    "1. Add a student.\n" +
                    "2. Add a student's grade for a subject.\n" +
                    "3. Get a student's GPA.\n" + 
                    "4. Get a list of students with the GPA is higher than a set value\n" +
                    "5. Exit.\n" +
                    "Please, enter your choice (1-5): ", 
                    scanner
                );
                
                switch(choice)
                {
                    case 1 ->
                    {
                        cleanConsole();
                        System.out.println("Enter student name: ");
                        String name = scanner.nextLine();
                        Integer id = students.size() + 1;
                        Student student = new Student(id, name);
                        students.put(id, student);
                        cleanConsole();
                    }
                    case 2 ->
                    {
                        cleanConsole();
                        Integer id = askPositiveNZeroInt("Enter student ID: ", scanner);
                        if (!students.containsKey(id))
                        {
                            cleanConsole();
                            System.out.println("Student ID not found. Try again.");
                            continue;
                        }
                        System.out.println("Enter subject name: ");
                        String subjectName = scanner.nextLine();
                        int gradeValue = askPositiveNZeroInt("Enter grade: ", scanner);
                        boolean found = false;
                        for (int i = 0; i < grades.size(); i++)
                        {
                            if (grades.get(i).getID() == id && grades.get(i).getName().equals(subjectName))
                            {
                                grades.set(i, new Grade(id, subjectName, gradeValue));
                                found = true;
                                break;
                            }
                        }
                        if (!found)
                        {
                            grades.add(new Grade(id, subjectName, gradeValue));
                        }
                        cleanConsole();
                    }
                    case 3 ->
                    {
                        cleanConsole();
                        Integer id = askPositiveNZeroInt("Enter student ID: ", scanner);
                        if (!students.containsKey(id))
                        {
                            cleanConsole();
                            System.out.println("Student ID not found. Try again.");
                            continue;
                        }
                        int result = calculateGPA(id, grades, students, scanner);
                        if (result == -1)
                        {
                            cleanConsole();
                            System.out.println("No grades found for this student.");
                            scanner.nextLine();
                            cleanConsole();
                            continue;
                        }
                        cleanConsole();
                        System.out.println("GPA: " + result);
                        scanner.nextLine();
                        cleanConsole();
                    }
                    case 4 ->
                    {
                        cleanConsole();
                        int minGPA = askPositiveNZeroInt("Enter minimum GPA: ", scanner);
                        cleanConsole();
                        System.out.println("Students with GPA higher than " + minGPA + ":");
                        for (int i = 0; i < students.size(); i++)
                        {
                            int gpa = calculateGPA(i + 1, grades, students, scanner);
                            if (gpa > minGPA)
                            {
                                System.out.println("ID: " + students.get(i + 1).getID() + ", Name: " + students.get(i + 1).getName() + ", GPA: " + gpa);
                            }
                        }
                        scanner.nextLine();
                        cleanConsole();
                    }
                    case 5 -> {
                        cleanConsole();
                        System.out.println("Exiting the program. Goodbye!");
                    }
                    default -> {
                        cleanConsole();
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
                
            }
        }
    }

    private static void cleanConsole()
    {
        System.out.print("\n\n\n\n\n\n--------------------------------------------------------");
        System.out.print("\033[H\033[2J");
    }


    private static Integer calculateGPA(Integer studentId, List<Grade> grades, Map<Integer, Student> students, Scanner scanner)
    {
        int result = 0;
        int numGrades = 0;
        
        
        for (int i = 0; i < grades.size(); i++)
        {
            if (grades.get(i).getID() == studentId)
            {
                result += grades.get(i).getGrade();
                numGrades++;
            }
        }
        if (numGrades == 0)
        {
            return -1;
        }
        return result / numGrades;
    }

    private static int askPositiveNZeroInt(String message, Scanner scanner)
    {
        int value = -1;
        while (value < 0)
        {
            try{
                System.out.println(message);
                value = scanner.nextInt();
                scanner.nextLine();
                if (value <= 0)
                {
                    cleanConsole();
                    System.out.println("Invalid input. Please try again.");
                }
            } catch (Exception e){
                cleanConsole();
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine();
            }
        }
        return value;
    }
}
