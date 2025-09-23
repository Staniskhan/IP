import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class lab2n1 {
    public static void main(String[] args) {

        //----------------start parameters------------------
        int columns = 4, rows = 4;
        double maxVal = 2.2, minVal = 1.5;
        //--------------------------------------------------
        double[][] matrix = new double[rows][columns];
        Random rand = new Random();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = rand.nextDouble(maxVal - minVal) + minVal;
                matrix[i][j] = (double)Math.round(matrix[i][j] * 10) / 10;
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.print("\n");
        }

        Map<Double, Integer> map = new HashMap<>();

        double maxRepEl = minVal - 1;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (map.containsKey(matrix[i][j]) && map.get(matrix[i][j]) == 1)
                {
                    if (matrix[i][j] > maxRepEl)
                    {
                        maxRepEl = matrix[i][j];
                    }
                }
                else 
                {
                    map.put(matrix[i][j], 1);
                }
            }
        }

        if (maxRepEl == minVal - 1)
        {
            System.out.println("\nThere are no repeating numbers in the matrix\n");
        }
        else
        {
            System.out.println("\nmax repeating element in matrix: " + maxRepEl + "\n");
        }
        //-----------------------------------------------
        // boolean indicator = false;
        // int count;
        // for (int k = maxVal; k >= minVal; k--)
        // {
        //     count = 0;
        //     for(int i = 0; i < rows; i++)
        //     {
        //         for (int j = 0; j < columns; j++)
        //         {
        //             if (matrix[i][j] == k)
        //             {
        //                 count++;
        //             }
        //             if (count == 2)
        //             {
        //                 System.out.println("\n" + matrix[i][j] + "\n");
        //                 indicator = true;
        //                 break;
        //             }
        //         }
        //         if (indicator)
        //         {
        //             break;
        //         }
        //     }
        //     if (indicator)
        //     {

        //         break;
        //     }
        // }

        // if (!indicator)
        // {
        //     System.out.println("\nThere are no repeating numbers in the matrix\n");
        // }

    }
}