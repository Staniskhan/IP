import java.util.StringTokenizer;

public class lab3 {
    public static void main(String[] args)
    {
        int count = 0;
        Text text = new Text();
        text.enterTextFromFile("input.txt");
        for (int i = 0; i < text.size(); i++)
        {
            StringTokenizer strtok = new StringTokenizer(text.get(i), "\s+,.!?/\\|()[]{}<>:;\"\'");
            while(strtok.hasMoreTokens())
            {
                String tok = strtok.nextToken();
                if (tok.matches("^[eEuUiIoOaAyYаАуУоОыЫиИэЭяЯюЮеЕёЁ].*[eEuUiIoOaAyYаАуУоОыЫиИэЭяЯюЮеЕёЁ]$"))
                {
                    System.out.println(tok);
                    count++;
                }
            }
        }
        System.out.println("\nAnswer: " + count);
    }
}
