import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {
    public static void GenerateFile(String filename, int num_of_students, int num_of_group)
    {
        String[] name_array = {"James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth", "David", 
        "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen", "Christopher", "Nancy", "Daniel", "Lisa", 
        "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley", "Steven", "Kimberly", "Paul", "Emily", "Andrew", "Donna", 
        "Joshua", "Michelle", "Kenneth", "Dorothy", "Kevin", "Carol", "Brian", "Amanda", "George", "Melissa", "Edward", "Deborah", "Ronald", 
        "Stephanie", "Timothy", "Rebecca", "Jason", "Sharon", "Jeffrey", "Laura", "Ryan", "Cynthia", "Jacob", "Kathleen", "Gary", "Amy", "Nicholas", 
        "Shirley", "Eric", "Angela", "Jonathan", "Helen", "Stephen", "Anna", "Larry", "Brenda", "Justin", "Pamela", "Scott", "Nicole", "Brandon", 
        "Emma", "Benjamin", "Samantha", "Samuel", "Katherine", "Gregory", "Christine", "Frank", "Debra", "Alexander", "Rachel", "Raymond", "Catherine", 
        "Patrick", "Carolyn", "Jack", "Janet", "Dennis", "Ruth", "Jerry", "Maria", "Tyler", "Heather", "Aaron", "Diane", "Jose", "Virginia", "Adam", "Julie"};



        try(FileWriter writer = new FileWriter(filename))
        {
            Random rand = new Random();
            writer.write(num_of_students + "\n");
            for (int i = 0; i < num_of_students; i++)
            {
                float avg = (float)Math.round(10*rand.nextFloat() * 100) / 100;
                writer.write(rand.nextLong(Long.MAX_VALUE) + "\n"
                + name_array[rand.nextInt(name_array.length)] + "\n"
                + (rand.nextInt(num_of_group) + 1) + "\n"
                + avg + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
