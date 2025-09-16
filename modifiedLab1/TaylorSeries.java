public class TaylorSeries
{
    public double sinh(double x, int k)
    {
        double summand, answer, limit;

        summand = x;
        answer = x;
        limit = 1;

        for (int i = 0; i < k; i++)
        {
            limit /= 10;
        }

        int denominatorMultiplier = 1;
        while (summand > limit)
        {
            summand *= x;
            summand *= x;
            denominatorMultiplier++;
            summand /= denominatorMultiplier;
            denominatorMultiplier++;
            summand /= denominatorMultiplier;
            answer += summand;
        }

        answer = (double)Math.round(answer * 1000) / 1000;
        return answer;
    }
}
