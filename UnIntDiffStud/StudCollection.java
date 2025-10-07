import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class StudCollection {

    HashSet<Student> studset;


    public StudCollection(String filename)
    {
        studset = new HashSet<>();

        try
        {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            int number_of_students = Integer.parseInt(fileScanner.nextLine());

            for (int i = 0; i < number_of_students; i++)
            {
                Student currstud = new Student();
                currstud.num = Long.parseLong(fileScanner.nextLine());
                currstud.name = fileScanner.nextLine();
                currstud.group = Integer.parseInt(fileScanner.nextLine());
                currstud.grade = Float.parseFloat(fileScanner.nextLine());
                studset.add(currstud);
            }

        }
        catch(FileNotFoundException e)
        {
            System.out.println("\nFILE NOT FOUND\n");
            e.printStackTrace();
        }
    }
}
