import java.util.Scanner;

public class modLab1
{
    public static void main (String args[]){
        String text = "\nProgram is used to count sinh using a Taylor series.\n"+
                "Enter the value of number, which sinh you would like to count (x)\n"+
                "and degree of accuracy (k). The counting will last untill a summand will be bigger than 10^(-k)";
        System.out.print(text);

        Scanner in = new Scanner(System.in);
        System.out.print("\n\nEnter x: ");
        double x = in.nextDouble();
        System.out.print("Enter k: ");
        int k = in.nextInt();
        in.close();

        System.out.println("\nx = " +x + "\nk = " + k);

        TaylorSeries ts = new TaylorSeries();
        System.out.print("\nsinh(x) = " + ts.sinh(x, k));
        System.out.println("\nMath.sinh(x) = " + Math.sinh(x));
    }

}

