import java.util.Random;

public class lab2n2
{
    public static void main(String[] args)
    {
        //----------------start parameters------------------
        int columns = 5, rows = 4, maxVal = 4, minVal = 2;
        //------matrix declaration and initialization-------
        int[][] matrix = new int[rows][columns];
        Random rand = new Random();
        System.out.println("matrix:");
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = rand.nextInt(maxVal - minVal + 1) + minVal;
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }

        //---------------sorting matrix------------------

        System.out.print("\n");

        int answer = 1;
        boolean indicator = false;
        for (int i = 0; i < rows; i++)
        {
            for (int k = 0; k < columns - 1; k++) {
                for (int j = 1; j < columns; j++) {
                    if (matrix[i][j - 1] > matrix[i][j]) {
                        matrix[i][j - 1] += matrix[i][j];
                        matrix[i][j] = matrix[i][j - 1] - matrix[i][j];
                        matrix[i][j - 1] = matrix[i][j - 1] - matrix[i][j];
                    }
                }
            }

            for (int k = i - 1; k >= 0; k--)
            {
                for (int j = 0; j < columns; j++)
                {
                    if (matrix[i][j] != matrix[k][j])
                    {
                        indicator = true;
                        break;
                    }
                }
                if (indicator && k != 0)
                {
                    indicator = false;
                }
                else
                {
                    break;
                }
            }
            if (indicator)
            {
                answer++;
            }

        }


        //---------------displaying sorted matrix-------------------

        System.out.println("sorted matrix:");
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }


        //---------------------displaying answer---------------------

        System.out.println("\nanswer = " + answer);


    }
}