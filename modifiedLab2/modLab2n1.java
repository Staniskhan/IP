
public class modLab2n1 {
    public static void main(String[] args) {
        {
            // Matrix.generateMatrixToFile("matr.txt", 4, 5, 2, 10);
            Matrix matrix = new Matrix("matr.txt");
            // Matrix matrix1 = new Matrix(3, 4, 2 ,6);
            matrix.print();
        }
    }
}
