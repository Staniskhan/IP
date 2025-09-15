import java.util.Random;

public class lab2n3 {
    public static void main(String[] args) {

        //----------------start parameters------------------

        int columns = 5, rows = 5, maxVal = 4, minVal = 1;


        //------matrix declaration and initialization-------

        int[][] matrix = new int[rows][columns];
        Random rand = new Random();
        System.out.println("matrix:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = rand.nextInt(maxVal - minVal + 1) + minVal;
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }


        //----------------counting sums--------------------------

        int[] array = new int[rows];
        int sum;

        for (int i = 0; i < rows; i++)
        {
            sum = 0;
            for (int j = 0; j < columns; j++)
            {
                if (matrix[i][j] > 0 && matrix[i][j] % 2 == 0)
                {
                    sum += matrix[i][j];
                }
            }
            array[i] = sum;
        }


        //-------------------sorting matrix--------------------------

        for (int k = 0; k < rows - 1; k++)
        {
            for (int i = 1; i < rows; i++)
            {
                if (array[i - 1] > array[i])
                {
                    array[i - 1] += array[i];
                    array[i] = array[i - 1] - array[i];
                    array[i - 1] = array[i - 1] - array[i];

                    for (int j = 0; j < columns; j++)
                    {
                        matrix[i - 1][j] += matrix[i][j];
                        matrix[i][j] = matrix[i - 1][j] - matrix[i][j];
                        matrix[i - 1][j] = matrix[i - 1][j] - matrix[i][j];
                    }
                }
            }
        }


        //-----------------displaying result-----------------------

        System.out.print("\nsorted matrix:\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }

    }
}