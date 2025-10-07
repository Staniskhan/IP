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
        System.out.print("\n\n\n\n\n\n--------------------------------------------------------");
        System.out.print("\033[H\033[2J");
    }

    private static void printStartSelectionMenu(int number_of_menu)
    {
        if (number_of_menu == 0)
        {
            System.out.print("You have 2 collections of students.\n"
            + "Select the action to do with collections:\n"
            + "1. Generate input file of students.\n"
            + "2. Input data to collection.\n"
            + "3. Append data.\n"
            + "4. Output.\n"
            + "5. Show the tree of actions.\n"
            + "6. Exit program\n");
        }
        else if (number_of_menu == 2 
        || number_of_menu == 31 
        || number_of_menu == 32)
        {
            System.out.print("1. Manually.\n2. From the file.\n");
        }
        else if (number_of_menu == 21 
        || number_of_menu == 22 
        || number_of_menu == 321
        || number_of_menu == 322)
        {
            System.out.print("1. Collection 1.\n2. Collection 2.\n");
        }
        else if (number_of_menu == 3)
        {
            System.out.print("1. To file.\n2. To collection.\n");
        }
        else if (number_of_menu == 4)
        {
            System.out.print("1. To file.\n2. To console.\n");
        }
        else if (number_of_menu == 41
        || number_of_menu == 42)
        {
            System.out.print("1. Collection 1.\n2. Collection 2.\n3. Union.\n4. Intersection.\n5.Difference.");
        }


    }



    private static int askActionNumber(int number_of_menu)
    {
        cleanConsole();
        printStartSelectionMenu(number_of_menu);
        int maj = 0;
        if (number_of_menu == 0)
        {
            maj = 6;
        }
        else if (number_of_menu == 2
        || number_of_menu == 31 
        || number_of_menu == 32
        || number_of_menu == 21 
        || number_of_menu == 22 
        || number_of_menu == 311
        || number_of_menu == 312
        || number_of_menu == 321
        || number_of_menu == 322
        || number_of_menu == 3
        || number_of_menu == 31
        || number_of_menu == 32
        || number_of_menu == 4)
        {
            maj = 2;
        }
        else if (number_of_menu == 41
        || number_of_menu == 42)
        {
            maj = 5;
        }
        //--------------------
        Scanner scanner;
        int action = 0;
        while(action < 1 || action > maj)
        {
            try
            {
                scanner = new Scanner(System.in);
                System.out.print("Select: ");
                action = scanner.nextInt();
                if (action < 1 || action > 2)
                {
                    cleanConsole();
                    System.out.println("TRY AGAIN!");
                    printStartSelectionMenu(number_of_menu);
                }
            }
            catch (InputMismatchException e)
            {
                action = 0;
                cleanConsole();
                System.out.println("TRY AGAIN!");
                printStartSelectionMenu(number_of_menu);
            }
        }
        number_of_menu *= 10;
        number_of_menu += action;
        return number_of_menu;
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
        System.out.println();
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


    private static void GenerateFile()
    {
        String filename = askFileName();
        int num_of_students = askPositiveNZeroInt("Enter the number of students: ");
        int num_of_groups = askPositiveNZeroInt("Enter the number of groups: ");
        FileGenerator.GenerateFile(filename, num_of_students, num_of_groups);
    }


    private static void showTheTreeOfActions()
    {
        cleanConsole();
        System.out.print(
            "1. Generate input file of students.\n"
            + "2. Input data to collection.\n"
            + "  2.1. Manually.\n"
            + "  2.2. From the file.\n"
            + "3. Append data.\n"
            + "  3.1. To file.\n"
            + "    3.1.1. Manually.\n"
            + "    3.1.2. From the file.\n"
            + "  3.2. To collection.\n"
            + "    3.2.1. Manually.\n"
            + "    3.2.2. From the file.\n"
            + "4. Output.\n"
            + "  4.1. To file.\n"
            + "    4.1.1. Collection 1.\n"
            + "    4.1.2. Collection 2.\n"
            + "    4.1.3. Union.\n"
            + "    4.1.4. Intersection.\n"
            + "    4.1.5. Difference.\n"
            + "  4.2. To console.\n"
            + "    4.2.1. Collection 1.\n"
            + "    4.2.2. Collection 2.\n"
            + "    4.2.3. Union.\n"
            + "    4.2.4. Intersection.\n"
            + "    4.2.5. Difference.\n"
            + "5. Show the tree of actions.\n"
            + "6. Exit program\n"
        );
        waitForKeyPress();
    }

//=============
    private static long askPositiveNZeroLong(String msg)
    {
        cleanConsole();
        long num = -1;
        Scanner scanner = new Scanner(System.in);
                boolean ind = false;
                while (ind == false)
                {
                    System.out.println(msg);
                    try
                    {
                        num = scanner.nextLong();
                        if (num < 0)
                        {
                            cleanConsole();
                            System.out.println("TRY AGAIN!");
                            scanner = new Scanner(System.in);
                        }
                        else
                        {
                            ind = true;
                        }
                    }
                    catch (InputMismatchException e)
                    {
                        cleanConsole();
                        System.out.println("TRY AGAIN!");
                        scanner = new Scanner(System.in);
                    }
                }
                return num;
    }

    private static String askString(String msg)
    {
        cleanConsole();
        String name = "";
        System.out.println(msg);
        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();

        return name;
    }



    private static float askPositiveNZeroFloat(String msg)
    {
        cleanConsole();
        float num = -1;
        Scanner scanner = new Scanner(System.in);
                boolean ind = false;
                while (ind == false)
                {
                    System.out.println(msg);
                    try
                    {
                        num = scanner.nextFloat();
                        if (num < 0)
                        {
                            cleanConsole();
                            System.out.println("TRY AGAIN!");
                            scanner = new Scanner(System.in);
                        }
                        else
                        {
                            ind = true;
                        }
                    }
                    catch (InputMismatchException e)
                    {
                        cleanConsole();
                        System.out.println("TRY AGAIN!");
                        scanner = new Scanner(System.in);
                    }
                }
                return num;
    }


    private static int askPositiveNZeroInt(String msg)
    {
        cleanConsole();
        int num = -1;
        Scanner scanner = new Scanner(System.in);
                boolean ind = false;
                while (ind == false)
                {
                    System.out.println(msg);
                    try
                    {
                        num = scanner.nextInt();
                        if (num < 0)
                        {
                            cleanConsole();
                            System.out.println("TRY AGAIN!");
                            scanner = new Scanner(System.in);
                        }
                        else
                        {
                            ind = true;
                        }
                    }
                    catch (InputMismatchException e)
                    {
                        cleanConsole();
                        System.out.println("TRY AGAIN!");
                        scanner = new Scanner(System.in);
                    }
                }
                return num;
    }


    private static Student askStudInf()
    {
        Student currstud = new Student();
        currstud.num = askPositiveNZeroLong("Enter the ID of the student: ");
        currstud.name = askString("Enter the name of the student: ");
        currstud.group = askPositiveNZeroInt("Enter the group of the student: ");
        currstud.grade = askPositiveNZeroFloat("Enter the AVG mark of the student: ");
        return currstud;
    }


    private static StudCollection inputDataToCollectionManually()
    {
        StudCollection coll = new StudCollection();
        int numOfStud = askPositiveNZeroInt("Enter the number of students: ");
        for (int i = 0; i < numOfStud; i++)
        {
            coll.add(askStudInf());
        }
        return coll;
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

        
        StudCollection coll1 = new StudCollection();
        StudCollection coll2 = new StudCollection();
        while(true)
        {
            int action = 0;
            action = askActionNumber(action);

            if (action == 1) // generate file
            {
                GenerateFile();
            }
            else if (action == 2) // input data to collection
            {
                action = askActionNumber(action);
                if (action == 21) // input data to collection manually
                {
                    action = askActionNumber(action);
                    if (action == 211) // input data to collection 1 manually
                    {
                        coll1 = inputDataToCollectionManually();
                    }
                    else if (action == 212) // input data to collection 2 manually
                    {
                        coll2 = inputDataToCollectionManually();
                    }
                }
                else if (action == 22) // input data to collection from the file
                {
                    action = askActionNumber(action);
                    if (action == 221) // input data to collection 1 from the file
                    {
                        cleanConsole();
                        coll1 = new StudCollection(askFileName());
                    }
                    else if (action == 222) // input data to collection 2 from the file
                    {
                        cleanConsole();
                        coll2 = new StudCollection(askFileName());
                    }
                }
            }
            else if (action == 3) // add data
            {
                action = askActionNumber(action);
                if (action == 31) // add data to file
                {

                }
                else if (action == 32) // add data to collection
                {
                    
                }

            }
            else if (action == 4) // output data
            {

            }
            else if (action == 5) // show the tree of actions
            {
                showTheTreeOfActions();
            }
            else if (action == 6) // exit program
            {
                return;
            }
        }
        
    }
}
