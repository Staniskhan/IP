import java.util.InputMismatchException;
import java.util.Scanner;

public class setMain {

    private static void waitForKeyPress()
    {
        System.out.println("To complete the program press enter...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }


    private static void cleanConsole()
    {
        System.out.print("\033[H\033[2J");
    }

    private static void printStartSelectionMenu()
    {
        System.out.print("You have 2 collections of students.\n"
        + "Select the action to do with collections:\n"
        + "1. Generate input file of students.\n"
        + "2. Input data to collection.\n"
        + "3. Append data.\n"
        + "4. Output.\n"
        + "5. Exit program\n");
    }



    private static int askActionNumber()
    {
        Scanner scanner;
        int action = 0;
        while(action < 1 || action > 5)
        {
            try
            {
                scanner = new Scanner(System.in);
                System.out.print("\nSelect: ");
                action = scanner.nextInt();
                if (action < 1 || action > 4)
                {
                    cleanConsole();
                    System.out.println("TRY AGAIN!");
                    printStartSelectionMenu();
                }
            }
            catch (InputMismatchException e)
            {
                action = 0;
                cleanConsole();
                System.out.println("TRY AGAIN!");
                printStartSelectionMenu();
            }
        }
        return action;
    }


    private static boolean checkFileName(String filename)
    {
        if (filename.matches(".*[><:\"/\\\\|*?].*")
            || filename.matches("^CON\\.*[^.]*")
            || filename.matches("^AUX\\.*[^.]*")
            || filename.matches("^PRN\\.*[^.]*")
            || filename.matches("^NUL\\.*[^.]*")
            || filename.matches("^COM[1-9]\\.*[^.]*")
            || filename.matches("^LPT[1-9]\\.*[^.]*")
            || filename.matches("^.*\\.$"))
        {
            return false;
        }
        return true;
    }


    private static String addExtension(String filename)
    {
        if (!filename.matches(".*[.]txt")) filename += ".txt";
        return filename;
    }


    private static String askFileName()
    {
        System.out.println("Write the name of generating file: ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();
        while(!checkFileName(filename))
        {
            cleanConsole();
            System.out.println("TRY AGAIN!\nWrite the name of generating file: ");
            scanner = new Scanner(System.in);
            filename = scanner.nextLine();
        }
        filename = addExtension(filename);
        return filename;
    }


    private static int askNumOfStudents()
    {
        int num = -1;
        while(num < 1)
        {
            System.out.println("Enter the number of students: ");
            Scanner scanner = new Scanner(System.in);
            try
            {
                num = scanner.nextInt();
            }
            catch (InputMismatchException e)
            {
                cleanConsole();
                System.out.println("TRY AGAIN!");
                System.out.println("Enter the number of students: ");
                scanner = new Scanner(System.in);
            }
            if (num < 1)
            {
                cleanConsole();
                System.out.println("TRY AGAIN!");
            }
        }
        return num;
    }


    private static int askNumberOfGroups()
    {
        int num = -1;
        while(num < 1)
        {
            System.out.println("Enter the number of groups: ");
            Scanner scanner = new Scanner(System.in);
            try
            {
                num = scanner.nextInt();
            }
            catch (InputMismatchException e)
            {
                cleanConsole();
                System.out.println("TRY AGAIN!");
                System.out.println("Enter the number of groups: ");
                scanner = new Scanner(System.in);
            }
            if (num < 1)
            {
                cleanConsole();
                System.out.println("TRY AGAIN!");
            }
        }
        return num;
    }


    private static void GenerateFile()
    {
        cleanConsole();
        String filename = askFileName();
        cleanConsole();
        int num_of_students = askNumOfStudents();
        cleanConsole();
        int num_of_groups = askNumberOfGroups();
        FileGenerator.GenerateFile(filename, num_of_students, num_of_groups);
    }


    public static void main(String args[])
    {
        // FileGenerator.GenerateFile("input_files/input1.txt", 5, 15);
        // FileGenerator.GenerateFile("input_files/input2.txt", 5, 15);
        // StudCollection coll1 = new StudCollection("input_files/input1.txt");
        // StudCollection coll2 = new StudCollection("input_files/input2.txt");
        // SCRuler.Union(coll1, coll2).fileOut("output_files/output_union.txt", false);
        // SCRuler.Intersection(coll1, coll2).fileOut("output_files/output_intersection.txt", false);
        // SCRuler.Difference(coll1, coll2).fileOut("output_files/output_difference.txt", false);


        cleanConsole();
        printStartSelectionMenu();
        int action = askActionNumber();

        if (action == 1)
        {
            GenerateFile();
        }
        else if (action == 2)
        {

        }
        else if (action == 3)
        {

        }
        else if (action == 4)
        {

        }
        else if (action == 5)
        {
            return;
        }



        waitForKeyPress();
    }
}
