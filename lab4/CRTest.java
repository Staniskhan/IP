import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

// The format of the input File
// * "-2" means that there is no exam/test/semester mark
// * "0" means incomplete
// * "1" means complete
// * "*" means not already

// 1. number of students                                                                              // for collection ruler
// 2. <surname>                                                                                                \          \
// 3. <name>                                                                                                    |          |
// 4. <second name>                                                                                             |          |
// 5. <year> <group> <semester>                                                                                 |          |
// 6. <number of passed sessions>                                                                               |          |
// 7. <number of disciplines in the 1-st sem>                                      \            \               |          |
// 8. <1-st_discipline_name>                           \            \               |            |              |\student  |
// 9. <1-st discipline semester mark>                   |\discipline |              |            |              |/         |\students
// 10. <1-st discipline test mark>                      |/           |\disciplines   >semester   |              |          |/
// 11. <1-st discipline exam mark>                     /             |/             |             >semesteres   |          |
// 12. <2-nd discipline name>                                        |              |            |              |          |
// .................................                                /              /             |              |          |
// x. <number of disciplines in the 2-nd sem>                                                    |              |          |
// .................................                                                            /              /           |
// y. <surname>                                                                                                            |
// .................................                                                                                      /




public class CRTest {










    public static void generateFile(String filename, int students_amount, int max_passed_sessions_amount, int max_discipline_amount_in_sem, int max_number_of_group)
    {
        String[] surname_array = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", 
        "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", 
        "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", 
        "Nguyen", "Hill", "Flores", "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts", "Gomez", 
        "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy", "Cook", 
        "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", 
        "Richardson", "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes", "Price", "Alvarez", "Castillo", 
        "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez", "Powell", "Jenkins", "Perry", "Russell", "Sullivan", "Bell", "Coleman"};

        String[] name_array = {"James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth", "David", 
        "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen", "Christopher", "Nancy", "Daniel", "Lisa", 
        "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley", "Steven", "Kimberly", "Paul", "Emily", "Andrew", "Donna", 
        "Joshua", "Michelle", "Kenneth", "Dorothy", "Kevin", "Carol", "Brian", "Amanda", "George", "Melissa", "Edward", "Deborah", "Ronald", 
        "Stephanie", "Timothy", "Rebecca", "Jason", "Sharon", "Jeffrey", "Laura", "Ryan", "Cynthia", "Jacob", "Kathleen", "Gary", "Amy", "Nicholas", 
        "Shirley", "Eric", "Angela", "Jonathan", "Helen", "Stephen", "Anna", "Larry", "Brenda", "Justin", "Pamela", "Scott", "Nicole", "Brandon", 
        "Emma", "Benjamin", "Samantha", "Samuel", "Katherine", "Gregory", "Christine", "Frank", "Debra", "Alexander", "Rachel", "Raymond", "Catherine", 
        "Patrick", "Carolyn", "Jack", "Janet", "Dennis", "Ruth", "Jerry", "Maria", "Tyler", "Heather", "Aaron", "Diane", "Jose", "Virginia", "Adam", "Julie"};

        String[] second_name_array = {"Jamesovich", "Johnovich", "Robertych", "Michaelovich", "Williamovich", "Davidovich", "Richardovich", "Thomasovich", 
        "Charlesovich", "Christopherovich", "Danielovich", "Matthewovich", "Anthonyovich", "Donaldovich", "Markovich", "Paulovich", "Stevenovich", "Andrewovich", 
        "Kennethovich", "Joshuaovich", "Kevinovich", "Brianovich", "Georgeovich", "Edwardovich", "Ronaldovich", "Timothyovich", "Jasonovich", "Jeffreyovich", 
        "Ryanovich", "Jacobovich", "Garyovich", "Nicholasovich", "Ericovich", "Jonathanovich", "Stephenovich", "Larryovich", "Justinovich", "Scottovich", 
        "Brandonovich", "Benjaminovich", "Samuelovich", "Gregoryovich", "Frankovich", "Alexanderovich", "Raymondovich", "Patrickovich", "Jackovich", 
        "Dennisovich", "Jerryovich", "Tylerovich", "Aaronovich", "Joseovich", "Adamovich", "Nathanovich", "Henryovich", "Douglasovich", "Zacharyovich", 
        "Peterovich", "Kyleovich", "Ethanovich", "Walterovich", "Noahovich", "Jeremyovich", "Christianovich", "Keithovich", "Rogerovich", "Terryovich", 
        "Austinovich", "Seanovich", "Geraldovich", "Carlovich", "Haroldovich", "Dylanovich", "Arthurovich", "Lawrenceovich", "Jordanovich", "Jesseovich", 
        "Bryanovich", "Billyovich", "Bruceovich", "Gabrielovich", "Joeovich", "Loganovich", "Alanovich", "Juanovich", "Albertovich", "Willieovich", "Elijahovich", 
        "Wayneovich", "Randyovich", "Royovich", "Vincentovich", "Ralphovich", "Eugeneovich", "Russellovich", "Bobbyovich", "Masonovich", "Philipovich", "Louisovich"};

        String[] disciplines_array = {"Algorithms_and_Data_Structures", "Mathematical_Analysis", "Discrete_Mathematics", "Linear_Algebra", "Probability_and_Statistics", 
        "Theory_of_Computation", "Computer_Architecture", "Operating_Systems", "Computer_Networks", "Database_Systems", "Software_Engineering", "Object-Oriented_Programming", 
        "Functional_Programming", "Web_Development", "Mobile_Application_Development", "Artificial_Intelligence", "Machine_Learning", "Deep_Learning", 
        "Natural_Language_Processing", "Computer_Vision", "Cryptography", "Network_Security", "Information_Theory", "Computational_Complexity", "Compiler_Design", 
        "Parallel_and_Distributed_Computing", "Numerical_Methods", "Optimization_Techniques", "Stochastic_Processes", "Graph_Theory", "Combinatorics", 
        "Mathematical_Logic", "Differential_Equations", "Number_Theory", "Abstract_Algebra", "Calculus", "Vector_Analysis", "Theory_of_Algorithms", 
        "Data_Mining", "Big_Data_Analytics", "Cloud_Computing", "Internet_of_Things", "Blockchain_Technology", "Human-Computer_Interaction", "Computer_Graphics", 
        "Embedded_Systems", "System_Programming", "Agile_Methodologies", "Formal_Languages_and_Automata", "Quantum_Computing"};



        Random rand = new Random();
        //number_of_students
        max_passed_sessions_amount = (max_passed_sessions_amount > 10) ? 10 : max_passed_sessions_amount;


        try (FileWriter writer = new FileWriter(filename))                 // +true
        {
            writer.write(students_amount + "\n");
            for (int i = 0; i < students_amount; i++)
            {
                writer.write(surname_array[rand.nextInt(surname_array.length)] + "\n"
                + name_array[rand.nextInt(name_array.length)] + "\n"
                + second_name_array[rand.nextInt(second_name_array.length)] + "\n");
                int num_of_sess = rand.nextInt(max_passed_sessions_amount) + 1;
                int num_of_sem = num_of_sess + 1;
                int year = (num_of_sem + 1) / 2;
                writer.write(year + " " + (rand.nextInt(max_number_of_group) + 1) + " " + num_of_sem + "\n" + num_of_sess + "\n");
                for (int j = 0; j < num_of_sess; j++)
                {
                    int num_of_disc = rand.nextInt(max_discipline_amount_in_sem) + 1;
                    writer.write(num_of_disc + "\n");
                    for (int k = 0; k < num_of_disc; k++)
                    {
                        writer.write(disciplines_array[rand.nextInt(disciplines_array.length)] + "\n");
                        int sem_mark = rand.nextInt(13) - 2;
                        if (sem_mark == -1)
                        {
                            sem_mark--;
                        }
                        int test_mark = rand.nextInt(4) - 2;
                        if (test_mark == -1)
                        {
                            test_mark--;
                        }
                        int exam_mark = rand.nextInt(13) - 2;
                        if (exam_mark == -1)
                        {
                            exam_mark--;
                        }
                        writer.write(sem_mark + "\n" + test_mark + "\n" + exam_mark + "\n");
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
