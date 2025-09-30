import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class CollectionRuler {
    public static float AVGAllSemsMark(StudRecordBook srb)
    {
        int[] arr = srb.getAllSemesterMarks();
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i] >= 0)
            {
                avg += (float)arr[i];
            }
            else
            {
                count--;
            }
        }

        avg /= (float)count;
        return avg;
    }

    public static float AVGAllExamsMark(StudRecordBook srb)
    {
        int[] arr = srb.getAllExamMarks();
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i] >= 0)
            {
                avg += (float)arr[i];
            }
            else
            {
                count--;
            }
        }

        avg /= (float)count;
        return avg;
    }

    public static float AVGExamMarks(StudRecordBook srb, int numOfSes)
    {
        int arr[] = srb.getExamMarks(numOfSes);
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i] >= 0)
            {
                avg += (float)arr[i];
            }
            else
            {
                count--;
            }
        }

        avg /= (float)count;
        return avg;
    }

    public static float AVGSemMarks(StudRecordBook srb, int numOfSem)
    {
        int arr[] = srb.getSemMarks(numOfSem);
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i] >= 0)
            {
                avg += (float)arr[i];
            }
            else
            {
                count--;
            }
        }

        avg /= (float)count;
        return avg;
    }

    public static float AVG(StudRecordBook srb)
    {
        int[] arr1 = srb.getAllSemesterMarks();
        int[] arr2 = srb.getAllExamMarks();
        int count = arr1.length;
        count += arr2.length;

        int[] arr = new int[count];
        float avg = 0;

        for (int i = 0; i < arr1.length; i++)
        {
            arr[i] = arr1[i];
        }
        for (int i = 0; i < arr2.length; i++)
        {
            arr[arr1.length + i] = arr2[i];
        }

        for (int i = count - 1; i >= 0; i--)
        {
            if (arr[i] >= 0)
            {
                avg += (float)arr[i];
            }
            else
            {
                count--;
            }
        }

        avg /= (float)count;
        return avg;
    }





    int number_of_students;
    Vector<StudRecordBook> students;

    public CollectionRuler (String filename)
    {
        students = new Vector<>();
        try
        {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            number_of_students = Integer.parseInt(fileScanner.nextLine());

            for (int i = 0; i < number_of_students; i++)
            {
                String surname = fileScanner.nextLine();
                String name = fileScanner.nextLine();
                String second_name = fileScanner.nextLine();
                StringTokenizer strtok = new StringTokenizer(fileScanner.nextLine(), "\s+");
                int year = Integer.parseInt(strtok.nextToken());
                int group = Integer.parseInt(strtok.nextToken());
                int semester = Integer.parseInt(strtok.nextToken());
                String args = fileScanner.nextLine();
                int num_of_passed_sessions = Integer.parseInt(args);
                args += " ";
                for (int a = 0; a < num_of_passed_sessions; a++)
                {
                    String num_of_dis_str = fileScanner.nextLine();
                    args += num_of_dis_str + " ";
                    int num_of_disciplines = Integer.parseInt(num_of_dis_str);
                    for (int b = 0; b < 4 * num_of_disciplines; b++)
                    {
                        args += fileScanner.nextLine();
                        args += " ";
                    }
                }
                StudRecordBook srb_curr = new StudRecordBook(surname, name, second_name, year, group, semester, args);
                students.addElement(srb_curr);
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("\nFILE NOT FOUND\n");
            e.printStackTrace();
        }

    }


    public void fileOutAllInformation(String filename)
    {
        try(FileWriter writer = new FileWriter(filename))
        {
            writer.write("number of students: " + number_of_students
            + "\n\n"
            + "* \"no\" means that there is no exam/test/semester mark\n"
            + "* \"-\" means incomplete\n"
            + "* \"+\" means complete\n\n");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        for (int i = 0; i < number_of_students; i++)
        {
            students.get(i).fileOut("output.txt");
        }
    }


    public void fileOutNames(String filename)
    {
        try(FileWriter writer = new FileWriter(filename))
        {
            writer.write("number of students: " + number_of_students + "\n\n");
            for (int i = 0; i < number_of_students; i++)
            {
                writer.write((i + 1) + ". " + students.get(i).surname + " " + students.get(i).name + " " + students.get(i).second_name + "\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    public void fileOutAVG(String filename)
    {
        try(FileWriter writer = new FileWriter(filename))
        {
            writer.write("number of students: " + number_of_students + "\n\n");
            for (int i = 0; i < number_of_students; i++)
            {
                writer.write((i + 1) + ".\t" + students.get(i).surname + " " + students.get(i).name + " " + students.get(i).second_name + "\n\t"  
                + "AVG: " + AVG(students.get(i)) + "\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    public void fileOutSemAVG(String filename, int num_of_sem)
    {
        try(FileWriter writer = new FileWriter(filename))
        {
            writer.write("number of students: " + number_of_students + "\n\nnumber of semester: " + num_of_sem + "\n\n");
            for (int i = 0; i < number_of_students; i++)
            {
                writer.write((i + 1) + ".\t" + students.get(i).surname + " " + students.get(i).name + " " + students.get(i).second_name + "\n\t"  
                + "AVG: " + ((AVGSemMarks(students.get(i), num_of_sem) + AVGExamMarks(students.get(i), num_of_sem)) / 2) + "\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    public void fileOutExcellentStudentsAllInf(String filename)
    {
        for (int i = 0; i < number_of_students; i++)
        {
            if (students.get(i).isExcellentStudent())
            {
                students.get(i).fileOut(filename);
            }
        }
    }


    public void fileOutExcellentStudentsNames(String filename)
    {
        for (int i = 0; i < number_of_students; i++)
        {
            if (students.get(i).isExcellentStudent())
            {
                try(FileWriter writer = new FileWriter(filename))
                {
                    writer.write(students.get(i).surname + " " + students.get(i).name + " " + students.get(i).second_name + "\n");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    public void fileOutExcellentStudentsAVG(String filename)
    {
        for (int i = 0; i < number_of_students; i++)
        {
            if (students.get(i).isExcellentStudent())
            {
                try(FileWriter writer = new FileWriter(filename))
                {
                    writer.write(students.get(i).surname + " " + students.get(i).name + " " + students.get(i).second_name + "  /==/  AVG: " + AVG(students.get(i)) + "\n");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    public void sortAVGUp()
    {
        for (int i = 0; i < number_of_students - 1; i++)
        {
            for (int j = 1; j < number_of_students; j++)
            {
                if (AVG(students.get(j - 1)) > AVG(students.get(j)))
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }


    public void sortAVGDown()
    {
        for (int i = 0; i < number_of_students - 1; i++)
        {
            for (int j = 1; j < number_of_students; j++)
            {
                if (AVG(students.get(j - 1)) < AVG(students.get(j)))
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }


    public void sortNameUp()
    {
        for (int i = 0; i < number_of_students - 1; i++)
        {
            for (int j = 1; j < number_of_students; j++)
            {
                String str1 = students.get(j - 1).surname + " " + students.get(j - 1).name + " " + students.get(j - 1).second_name;
                String str2 = students.get(j).surname + " " + students.get(j).name + " " + students.get(j).second_name;
                if (str1.compareTo(str2) == 1)
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }


    public void sortNameDown()
    {
        for (int i = 0; i < number_of_students - 1; i++)
        {
            for (int j = 1; j < number_of_students; j++)
            {
                String str1 = students.get(j - 1).surname + " " + students.get(j - 1).name + " " + students.get(j - 1).second_name;
                String str2 = students.get(j).surname + " " + students.get(j).name + " " + students.get(j).second_name;
                if (str2.compareTo(str1) == 1)
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }


    public void sortYearGroupUp()
    {
        for (int i = 0; i < number_of_students - 1; i++)
        {
            for (int j = 1; j < number_of_students; j++)
            {
                if (students.get(j - 1).year > students.get(j).year)
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
                else if ((students.get(j - 1).year == students.get(j).year) && (students.get(j - 1).group > students.get(j).group))
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }


    public void sortYearGroupDown()
    {
        for (int i = 0; i < number_of_students - 1; i++)
        {
            for (int j = 1; j < number_of_students; j++)
            {
                if (students.get(j - 1).year < students.get(j).year)
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
                else if ((students.get(j - 1).year == students.get(j).year) && (students.get(j - 1).group < students.get(j).group))
                {
                    StudRecordBook srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }
}
