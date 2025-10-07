import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StudCollection {

    Set<Student> studset;

    
    private static String processID(long num)
    {
        String ret = "";
        long check = 1000000000000000000L;
        while (num < check)
        {
            ret += "0";
            check /= 10;
        }
        ret += Long.toString(num);
        return ret;
    }


    public StudCollection(Set<Student> inset)
    {
        studset = new HashSet<>(inset);
    }


    public StudCollection()
    {
        studset = new HashSet<>();
    };


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


    public void fileOut(String filename, boolean append)
    {
        try (FileWriter writer = new FileWriter(filename, append))
        {
            int i = 1;
            for (Student stud: studset)
            {
                writer.write
                (
                "-<number " + i + ">-\n"
                + "ID: " + processID(stud.num) + "\n"
                + "Name: " + stud.name + "\n"
                + "Group: " + stud.group + "\n"
                + "AVG: " + stud.grade + "\n"
                + "_________________________\n"
                );
                i++;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void add(Student currstud)
    {
        studset.add(currstud);
    }
}
