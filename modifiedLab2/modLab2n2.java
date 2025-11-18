public class modLab2n2 
{
    public static void main(String[] args)
    {
        Matrix.generateMatrixToFile("input_files/matr.txt", 4, 4, 2, 4);
            Matrix matrix = new Matrix("input_files/matr.txt");


            System.out.println("----------------------------------------\n2-nd\n\nmatrix:");
            matrix.print();

            System.out.println("number of rows in max set of unique sets of elements of rows: " + matrix.numberOfUniqueSetsOfRows() + "\n----------------------------------------");
    }
}
