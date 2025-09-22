public class modLab2n3 {
    public static void main(String[] args)
    {
        Matrix.generateMatrixToFile("matr.txt", 7, 6, 3, 10);
        Matrix matr = new Matrix("matr.txt");

        System.out.println("\n-----------------------------------------\n3-rd\nmatrix:\n");
        matr.print();

        matr.sumOfPositiveEvenNumbersSortUp();
        System.out.println("\nsorted matrix:\n");
        matr.print();
        System.out.println("\n-----------------------------------------\n");
    }
}
