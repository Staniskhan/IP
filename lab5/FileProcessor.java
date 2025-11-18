import java.io.File;
import java.io.FileNotFoundException;
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

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            Vector<String> words = new Vector<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("\t"))
                {
                    String[] lineWords = line.split("\\s+");
                    for (int i = 0; i < lineWords.length; i++)
                    {
                        words.add(lineWords[i]);
                    }
                }
                else
                {
                    //displayParagraph(words)
                    words.clear();
                    String[] lineWords = line.split("\\s+");
                    for (int i = 0; i < lineWords.length; i++)
                    {
                        words.add(lineWords[i]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
