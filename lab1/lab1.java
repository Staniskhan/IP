import java.util.Scanner;

public class lab1{

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

        System.out.println("\nx = " + x + "\nk = " + k);

        double summand, answer, limit;

        summand = x;
        answer = x;
        limit = 1;
        for (int i = 0; i < k; i++)
        {
            limit /= 10;
        }

        int counter = 1;
        while (summand > limit)
        {
            summand *= x;
            summand *= x;
            counter++;
            summand /= counter;
            counter++;
            summand /= counter;
            answer += summand;
        }

        answer = (double)Math.round(answer * 1000) / 1000;
        System.out.print("\nsinh(x) = " + answer);
    }
}