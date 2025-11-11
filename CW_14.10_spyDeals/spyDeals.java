import java.util.Scanner;


public class spyDeals {
    private static int dimension(int num)
    {
        int check = 100000000;
        int i = 9;

        while (check >= 0)
        {
            if (num >= check)
            {
                return i;
            }
            i--;
            check /= 10;
        }
        return -1;
    }



    public static void main(String args[])
    {
        Scanner scanner = new Scanner(System.in);
        int password = scanner.nextInt();


        int currPassword = password;
        if (password % 9 == 0)
        {
            System.out.println(currPassword + "\npassword is true!");
            return;
        }

        int dim = dimension(password);

        int currdim = 10;
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                if ((i != dim - 1) || j != 0)
                {
                    int num1 = password / currdim;
                    int num2 = password % (currdim / 10);
                    currPassword = num1 * currdim + j * (currdim / 10) + num2;
                    if (currPassword % 9 == 0)
                    {
                        System.out.print(currPassword);
                    }
                }

                
            }
            currdim *= 10;
        }
    }
}
