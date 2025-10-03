import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class StudRecordBook 
{    
    private class Session
    {
        //inner class discipline of class Session
        public class discipline
        {
            String disciplineName;
            // -2 if there is no exam or test on that discipline, -1 if there is no mark on this test or exam yet
            int semesterMark;
            int testMark;
            int examMark;
        }
        //------------------------------------------




        // fields of the class Session
        int number;
        Vector<discipline> disciplines = new Vector<>();
        int number_of_disciplines;
        //-------------------------------------------





        public Session(String args)
        {
            StringTokenizer strtok = new StringTokenizer(args, "\s+");
            number_of_disciplines = Integer.parseInt(strtok.nextToken());
            for (int i = 0; i < number_of_disciplines; i++)
            {
                discipline cd = new discipline();
                cd.disciplineName = strtok.nextToken();
                cd.semesterMark = Integer.parseInt(strtok.nextToken());
                cd.testMark = Integer.parseInt(strtok.nextToken());
                cd.examMark = Integer.parseInt(strtok.nextToken());
                disciplines.addElement(cd);
            }
        }


        public String getSes()
        {
            String ret = "";
            ret += "---\nnumber of session: " + number /*+ "\n---\ndisciplines\n"*/
            // + "* \"no\" means that there is no exam/test/semester mark\n"
            // + "* \"-\" means incomplete\n"
            // + "* \"+\" means complete\n"
            + "\ndiscipline_name   :   semester mark/test mark/exam mark:\n";
            for (int i = 0; i < number_of_disciplines; i++)
            {
                ret += disciplines.get(i).disciplineName + "\t:\t";
                if (disciplines.get(i).semesterMark == -2)
                {
                    ret += "no/";
                }
                else if (disciplines.get(i).semesterMark == -1)
                {
                    ret += "*/";
                }
                else 
                {
                    ret += disciplines.get(i).semesterMark + "/";
                }
                if (disciplines.get(i).testMark == -2)
                {
                    ret += "no/";
                }
                else if (disciplines.get(i).testMark == 0)
                {
                    ret += "-/";
                }
                else if (disciplines.get(i).testMark == -1)
                {
                    ret += "*/";
                }
                else
                {
                    ret += "+/";
                }
                if (disciplines.get(i).examMark == -2)
                {
                    ret += "no\n";
                }
                else if (disciplines.get(i).examMark == -1)
                {
                    ret += "*/";
                }
                else 
                {
                    ret += disciplines.get(i).examMark + "\n";
                }
            }
            return ret;
        }


        public void printses()
        {
            System.out.println("---\nnumber of session: " + number /*+ "\n---\ndisciplines\n"*/
            // + "* \"no\" means that there is no exam/test/semester mark\n"
            // + "* \"-\" means incomplete\n"
            // + "* \"+\" means complete\n"
            + "\ndiscipline_name   :   semester mark/test mark/exam mark:");
            for (int i = 0; i < number_of_disciplines; i++)
            {
                System.out.print(disciplines.get(i).disciplineName + "\t:\t");
                if (disciplines.get(i).semesterMark == -2)
                {
                    System.out.print("no/");
                }
                else if (disciplines.get(i).semesterMark == -1)
                {
                    System.out.print("*/");
                }
                else 
                {
                    System.out.print(disciplines.get(i).semesterMark + "/");
                }
                if (disciplines.get(i).testMark == -2)
                {
                    System.out.print("no/");
                }
                else if (disciplines.get(i).testMark == 0)
                {
                    System.out.print("-/");
                }
                else if (disciplines.get(i).testMark == -1)
                {
                    System.out.print("*/");
                }
                else
                {
                    System.out.print("+/");
                }
                if (disciplines.get(i).examMark == -2)
                {
                    System.out.println("no");
                }
                else if (disciplines.get(i).examMark == -1)
                {
                    System.out.print("*/");
                }
                else 
                {
                    System.out.println(disciplines.get(i).examMark);
                }
            }
        }




        public int[] examMarksSes()
        {
            int ret[] = new int[number_of_disciplines];
            for (int i = 0; i < number_of_disciplines; i++)
            {
                ret[i] = disciplines.get(i).examMark;
            }
            return ret;
        }

        public int get_number_of_disciplines()
        {
            return number_of_disciplines;
        }
    }






    String surname;
    String name;
    String second_name;
    int year;
    int group;
    int semester;
    public Vector<Session> sessions;
    int number_of_passed_sessions;

    public StudRecordBook(String _surname, String _name, String _second_name, int _year, int _group, int _semester, String args) 
    // args: <number_of_passed_sessions> <number_of_disciplines_in_1-st_sem> <1-st_discipline_name> <1-st_
    {
        surname = _surname;
        name = _name;
        second_name = _second_name;
        year = _year;
        group = _group;
        semester = _semester;
        StringTokenizer strtok = new StringTokenizer(args, "\s+");
        String nopestr = strtok.nextToken();
        number_of_passed_sessions = Integer.parseInt(nopestr);
        sessions = new Vector<>(number_of_passed_sessions);
        for (int i = 1; i <= number_of_passed_sessions; i++)
        {
            String toDisc = strtok.nextToken();
            int number_of_disciplines = Integer.parseInt(toDisc);
            
            for (int j = 0; j < 4 * number_of_disciplines; j++)
            {
                toDisc += " ";
                toDisc += strtok.nextToken();
            }
            Session cs = new Session(toDisc);
            cs.number = i;
            sessions.addElement(cs);
        }
    }


    public void print()
    {
        System.out.println("-------------------------\nsurname: " + surname + "\nname: " + name + "\nsecond name: " + second_name + "\nyear: " + year + "\ngroup: " + group + "\nsemester: " + semester);
        for (int i = 0; i < sessions.size(); i++)
        {
            sessions.get(i).printses();
        }
        System.out.println("-------------------------");
    }

    public void fileOut(String filename)
    {
        try(FileWriter writer = new FileWriter(filename, true))
        {
            writer.write("-------------------------\nsurname: " + surname + "\nname: " + name + "\nsecond name: " + second_name + "\nyear: " + year + "\ngroup: " + group + "\nsemester: " + semester + "\n");
            for (int i = 0; i < sessions.size(); i++)
            {
                writer.write(sessions.get(i).getSes());
            }
            writer.write("\n-------------------------\n");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }





    }
    

    public int[] getAllExamMarks()
    {
        int count = 0;
        for (int i = 0; i < number_of_passed_sessions; i++)
        {
            count += sessions.get(i).number_of_disciplines;
        }

        int ret[] = new int[count];
        int k = 0;
        for (int i = 0; i < number_of_passed_sessions; i++)
        {
            for (int j = 0; j < sessions.get(i).number_of_disciplines; j++)
            {
                ret[k] = sessions.get(i).disciplines.get(j).examMark;
                k++;
            }
        }
        return ret;
    }


    public int[] getAllSemesterMarks()
    {
        int count = 0;
        for (int i = 0; i < number_of_passed_sessions; i++)
        {
            count += sessions.get(i).number_of_disciplines;
        }

        int ret[] = new int[count];
        int k = 0;
        for (int i = 0; i < number_of_passed_sessions; i++)
        {
            for (int j = 0; j < sessions.get(i).number_of_disciplines; j++)
            {
                ret[k] = sessions.get(i).disciplines.get(j).semesterMark;
                k++;
            }
        }
        return ret;
    }


    public int[] getExamMarks(int numOfSession)
    {
        if (numOfSession - 1 > sessions.size())
        {
            int ret[] = new int[1];
            return ret;
        }
        int ret[] = new int[sessions.get(numOfSession - 1).number_of_disciplines];
        for (int i = 0; i < sessions.get(numOfSession - 1).number_of_disciplines; i++)
        {
            ret[i] = sessions.get(numOfSession - 1).disciplines.get(i).examMark;
        }
        return ret;
    }


    public int[] getSemMarks(int numOfSem)
    {
        if (numOfSem - 1 > sessions.size())
        {
            int ret[] = new int[1];
            return ret;
        }
        int ret[] = new int[sessions.get(numOfSem - 1).number_of_disciplines];
        for (int i = 0; i < sessions.get(numOfSem - 1).number_of_disciplines; i++)
        {
            ret[i] = sessions.get(numOfSem - 1).disciplines.get(i).semesterMark;
        }
        return ret;
    }


    public boolean isExcellentStudent()
    {
        boolean ret = true;
        for (int i = 0; i < number_of_passed_sessions; i++)
        {
            for (int j = 0; j < sessions.get(i).number_of_disciplines; j++)
            {
                if (((sessions.get(i).disciplines.get(j).examMark < 9) && (sessions.get(i).disciplines.get(j).examMark > -2))
                || ((sessions.get(i).disciplines.get(j).semesterMark < 9) && (sessions.get(i).disciplines.get(j).semesterMark > -2))
                || ((sessions.get(i).disciplines.get(j).testMark < 1) && (sessions.get(i).disciplines.get(j).testMark > -2)))
                {
                    ret = false;
                    break;
                }
            }
            if (!ret)
            {
                break;
            }
        }
        return ret;
    }
}