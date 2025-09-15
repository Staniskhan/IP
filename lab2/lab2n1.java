import java.util.Random;

public class lab2n1 {
    public static void main(String[] args) {

        //----------------start parameters------------------
        int columns = 5, rows = 10, maxVal = 45, minVal = 2;
        //--------------------------------------------------
        int[][] matrix = new int[rows][columns];
        Random rand = new Random();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = rand.nextInt(maxVal - minVal + 1) + minVal;
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }

        //-----------------------------------------------
        boolean indicator = false;
        int count;
        for (int k = maxVal; k >= minVal; k--)
        {
            count = 0;
            for(int i = 0; i < rows; i++)
            {
                for (int j = 0; j < columns; j++)
                {
                    if (matrix[i][j] == k)
                    {
                        count++;
                    }
                    if (count == 2)
                    {
                        System.out.println("\n" + matrix[i][j] + "\n");
                        indicator = true;
                        break;
                    }
                }
                if (indicator)
                {
                    break;
                }
            }
            if (indicator)
            {
                break;
            }
        }

        if (!indicator)
        {
            System.out.println("\nThere are no repeating numbers in the matrix\n");
        }

    }
}