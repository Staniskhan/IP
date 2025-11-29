package com.staniskhan;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class fileGeneratorForTable {
     public static void generateFile(String filename, int students_amount, int max_passed_sessions_amount, int max_number_of_group)
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



        Random rand = new Random();
        //number_of_students
        max_passed_sessions_amount = (max_passed_sessions_amount > 10) ? 10 : max_passed_sessions_amount;


        try (FileWriter writer = new FileWriter(filename))                 // +true
        {
            writer.write(students_amount + "\n");
            for (int i = 0; i < students_amount; i++)
            {
                writer.write(surname_array[rand.nextInt(surname_array.length)] + ' '
                + name_array[rand.nextInt(name_array.length)] + ' '
                + second_name_array[rand.nextInt(second_name_array.length)] + "\n");
                int num_of_sem = rand.nextInt(max_passed_sessions_amount) + 2;
                int year = (num_of_sem + 1) / 2;
                writer.write(year + "\n" + num_of_sem + "\n" + (rand.nextInt(max_number_of_group) + 1) + "\n" + (double)Math.round(rand.nextDouble() * 100) / 10 + '\n');
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
