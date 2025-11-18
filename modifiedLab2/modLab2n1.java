
import java.util.NoSuchElementException;


public class modLab2n1 {
    public static void main(String[] args) {
        {
            Matrix.generateMatrixToFile("input_files/matr.txt", 4, 5, 2, 10);
            Matrix matrix = new Matrix("input_files/matr.txt");


            System.out.println("----------------------------------------\n1-st\n\nmatrix:");
            matrix.print();
            try
            {
                System.out.println("\nmax repeating element of the matrix: " + matrix.MaxRepeatingElement() + "\n----------------------------------------");
            }
            catch(NoSuchElementException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
