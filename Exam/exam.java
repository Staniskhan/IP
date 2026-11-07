import java.io.File;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class exam 
{
    public static void main(String[] args) 
    {
        final String PATH = "Exam/resources";
        final File dir = new File(PATH);

        File[] files = dir.listFiles();
        Thread[] threads = new Thread[5];
        AtomicInteger numOfFiles = new AtomicInteger(0);
        Vector<String> dirs = new Vector<>();
        

        for (int i = 0; i < 5; i++)
        {
            threads[i] = new Thread(() -> {
            if (!dirs.isEmpty())
            {
                String directory = dirs.remove(0);
                numOfFiles.addAndGet(countFiles(directory));
                String[] strdirs = listOfSubdirectories(directory);
                for (int j = 0; j < strdirs.length; j++)
                {
                    dirs.add(strdirs[j]);
                }
            }
            });
            threads[i].setDaemon(true);
        }

        while (!dirs.isEmpty())
        {
            
        }



        for (int i = 0; i < files.length; i++) 
        {
            System.out.println("\n" + files[i].getName());
            if (files[i].isDirectory())
            {
                
            }
        }

        
    }


    private static int countFiles(String directory)
    {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        int count = 0;

        if (files != null) 
        {
            for (int i = 0; i < files.length; i++) 
            {
                File file = files[i];
                {
                    if (file.isFile()) 
                    {
                        count++;
                    }
                }
            }
        
        }
    return count;
    }

    private static String[] listOfSubdirectories(String directory)
    {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        String[] subdirs = new String[files.length];
        int index = 0;

        if (files != null) 
        {
            for (int i = 0; i < files.length; i++) 
            {
                File file = files[i];
                if (file.isDirectory()) 
                {
                    subdirs[index++] = directory + "/" + file.getName();
                }
            }
        }
        
        return subdirs;
    }
}
