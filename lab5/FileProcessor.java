import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class FileProcessor {
    private File inFile;
    private File outFile;
    private int width;

    public FileProcessor() {
        inFile = null;
        outFile = null;
        width = 0;
    };

    public FileProcessor(String in, String out, int w)
    {
        inFile = new File(in);
        outFile = new File(out);
        width = w;
    }

    public void setInputFile(String filename)
    {
        inFile = new File(filename);
    }

    public void setOutputFile(String filename)
    {
        outFile = new File(filename);
    }

    public void setWidth(int w)
    {
        width = w;
    }

    public String inputFile()
    {
        return inFile.toString();
    }

    public String outputFile()
    {
        return outFile.toString();
    }

    public int width()
    {
        return width;
    }

    public void process()
    {
        if (inFile == null)
        {
            System.out.println("Input file is not defined!");
            return;
        }
        if (outFile == null)
        {
            System.out.println("Output file is not defined!");
            return;
        }
        if (width == 0)
        {
            System.out.println("Width is not defined!");
            return;
        }

        Vector<String> text = new Vector<>();
        try (Scanner scanner = new Scanner(new File("input_files/input.txt"))) {
            Vector<String> words = new Vector<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if ((line.startsWith(" ") || line.startsWith("\t")) && !words.isEmpty())
                {
                    text.add(paragraphStr(words));
                    words.clear();
                }
                String[] lineWords = line.trim().split("\\s+");
                for (int i = 0; i < lineWords.length; i++)
                {
                    words.add(lineWords[i]);
                }
            }
            if (!words.isEmpty())
            {
                text.add(paragraphStr(words));
                words.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try(FileWriter writer = new FileWriter("output_files/output.txt"))
        {
            for (int i = 0; i < text.size(); i++)
            {
                writer.write(text.get(i));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String paragraphStr(Vector<String> words)
    {
        int len = words.get(0).length() + 4;                            // +4?
        int number_of_words = 1;
        String par = "\t";

        while(!words.isEmpty())
        {

            while (words.size() > number_of_words && len + words.get(number_of_words).length() + 1 <= width)
            {
                len += 1 + words.get(number_of_words).length();
                number_of_words++;
            }
            int size_of_ws = 0;
            if (number_of_words > 1)
            {
                size_of_ws = (width - len + number_of_words - 1) / (number_of_words - 1);
            }
            String ws = "";
            for (int i = 0; i < size_of_ws; i++)
            {
                ws += " ";
            }

            par += words.get(0);
            words.remove(0);
            for (int i = 1; i < number_of_words && words.size() > 0; i++)
            {
                par += ws + words.get(0);
                words.remove(0);
            }
            par += "\n";
            if (!words.isEmpty())
            {
                len = words.get(0).length();
            }
            number_of_words = 1;
        }
        return par;
    }
}
