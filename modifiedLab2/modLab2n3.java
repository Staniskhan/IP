public class modLab2n3 {
    public static void main(String[] args)
    {
        Matrix.generateMatrixToFile("input_files/matr.txt", 4, 4, -5, 10);
        Matrix matr = new Matrix("input_files/matr.txt");

        System.out.println("\n-----------------------------------------\n3-rd\nmatrix:\n");
        matr.print();

        matr.sumOfPositiveEvenNumbersSortUp();
        System.out.println("\nsorted matrix:\n");
        matr.print();
        System.out.println("\n-----------------------------------------\n");
    }
}
