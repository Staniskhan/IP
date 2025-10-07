import java.io.FileWriter;
import java.io.IOException;

public class SCRuler {
    public static void fileOutUnion(StudCollection coll1, StudCollection coll2, String filename)
    {
        try (FileWriter writer = new FileWriter(filename))
        {
            for (int i = 0; i < coll1.studset.size(); i++)
            {
                writer.write(coll1.studset.get)
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
