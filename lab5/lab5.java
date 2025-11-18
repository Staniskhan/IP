import java.util.Scanner;

public class lab5 {
    public static void main (String args[])
    {
        Scanner scanner = new Scanner (System.in);
        int w = scanner.nextInt();
        FileProcessor fp = new FileProcessor("input_files/input.txt", "output_files/output.txt", w);
        fp.process();
    }
}
