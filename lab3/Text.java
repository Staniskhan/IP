import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Vector;

public class Text
{
    Vector<String> text = new Vector<>();

    public Text() {}

    public Text(String filename)
    {
        try
        {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            
            while(fileScanner.hasNextLine())
            {
                String line = fileScanner.nextLine();
                text.addElement(line);
            }

            fileScanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("\nFILE NOT FOUND\n");
            e.printStackTrace();
        }
    }

    public void enterTextManually()
    {
        InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
        try
        {
			String line = br.readLine();
            while(!line.isEmpty())
            {
                text.addElement(line);
                line = br.readLine();
            }
        }
		catch (IOException e)
        {
			System.out.println("\nERROR IN READING FROM KEYBOARD!\n");
            e.printStackTrace();
		}
    }

    public void enterTextFromFile(String filename)
    {
        try
        {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            
            while(fileScanner.hasNextLine())
            {
                String line = fileScanner.nextLine();
                text.addElement(line);
            }

            fileScanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("\nFILE NOT FOUND\n");
            e.printStackTrace();
        }
    }

    public void consoleOut()
    {
        for (int i = 0; i < text.size(); i++)
        {
            System.out.println(text.get(i));
        }
    }

    public void fileOut(String filename)
    {
        try(FileWriter writer = new FileWriter(filename))
        {
            for (int i = 0; i < text.size(); i++)
            {
                writer.write(text.get(i) + "\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String get(int index)
    {
        return text.get(index);
    }

    public int size()
    {
        return text.size();
    }
}
