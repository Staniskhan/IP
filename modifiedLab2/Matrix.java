import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;

public class Matrix {
    int rows = 0, columns = 0, minVal = 0, maxVal = 0;
    int[][] matrix;

    public Matrix(){}

    public Matrix(int _rows, int _columns) 
    {
        rows = _rows;
        columns = _columns;
        matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = 0;
            }
        }
    }

    public Matrix(int _rows, int _columns, int _minVal, int _maxVal) {
        rows = _rows;
        columns = _columns;
        minVal = _minVal;
        maxVal = _maxVal;
        matrix = new int[rows][columns];
        Random rand = new Random();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i][j] = rand.nextInt(maxVal - minVal + 1) + minVal;
            }
        }
    }

    public Matrix(String filename)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.isEmpty()) continue;
                if (line.split("\\s+").length > columns)
                {
                    columns = line.split("\\s+").length;
                }
                rows++;
            }

            matrix = new int[rows][columns];
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < columns; j++)
                {
                    matrix[i][j] = 0;
                }
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null)
            {
                if (line.isEmpty()) continue;
                String[] elements = line.split("\\s+");
                for (int j = 0; j < columns; j++)
                {
                    matrix[i][j] = Integer.parseInt(elements[j]);
                    if (matrix[i][j] > maxVal) maxVal = matrix[i][j];
                    if (matrix[i][j] < minVal) minVal = matrix[i][j];
                }
                i++;
            }

            reader.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public int get(int _row, int _column)
    {
        return matrix[_row][_column];
    }

    public void set(int value, int _row, int _column)
    {
        matrix[_row][_column] = value;
    }


    public static void generateMatrixToFile(String filename, int _rows, int _columns, int _minVal, int _maxVal)
    {
        try(FileWriter writer = new FileWriter(filename))
        {
            Random rand = new Random();
            for (int i = 0; i < _rows; i++)
            {
                for (int j = 0; j < _columns; j++)
                {
                    writer.write((rand.nextInt(_maxVal - _minVal + 1) + _minVal) + " ");
                }
                writer.write("\n");
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void print()
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }
    }


    public int MaxRepeatingElement() throws NoSuchElementException
    {
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
                        return matrix[i][j];
                    }
                }
            }
        }
        throw new NoSuchElementException("THERE ARE NO REPEATING ELEMENTS IN THE MATRIX!");
    }


    public int numberOfUniqueSetsOfRows()
    {
        int[][] sortedMatrix = matrix;
        // for (int i = 0; i < rows; i++)
        // {
        //     for (int j = 0; j < columns; j++)
        //     {
        //         sortedMatrix[i][j] = matrix[i][j];
        //     }
        // }
        // sortedMatrix = matrix;

        int answer = 1;
        boolean indicator = false;
        for (int i = 0; i < rows; i++)
        {
            for (int k = 0; k < columns - 1; k++) {
                for (int j = 1; j < columns; j++) {
                    if (sortedMatrix[i][j - 1] > sortedMatrix[i][j]) {
                        sortedMatrix[i][j - 1] += sortedMatrix[i][j];
                        sortedMatrix[i][j] = sortedMatrix[i][j - 1] - sortedMatrix[i][j];
                        sortedMatrix[i][j - 1] = sortedMatrix[i][j - 1] - sortedMatrix[i][j];
                    }
                }
            }

            for (int k = i - 1; k >= 0; k--)
            {
                for (int j = 0; j < columns; j++)
                {
                    if (sortedMatrix[i][j] != sortedMatrix[k][j])
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
                indicator = false;
                answer++;
            }

        }
        return answer;
    }



    public void sumOfPositiveEvenNumbersSortUp()
    {
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
    }
}