import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class Hotel
{
    
    Vector<hotelstr> arr = new Vector<>();

    public Hotel()
    {
        try 
        {
            File file = new File("hotel.txt");
            Scanner filescanner = new Scanner(file);

            while(filescanner.hasNextLine())
            {
                String line = filescanner.nextLine();
                StringTokenizer strtok = new StringTokenizer(line);
                hotelstr currh = new hotelstr();
                currh.city = strtok.nextToken();
                currh.name = strtok.nextToken();
                currh.stars = Integer.parseInt(strtok.nextToken());
                arr.add(currh);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("FILE NOT FOUND!");
            e.printStackTrace();
        }
    }


    public void sort()
    {
        Collections.sort(arr, new Comparator<hotelstr>(){
            @Override
            public int compare(hotelstr h1, hotelstr h2)
            {
                if (h1.city.compareTo(h2.city) > 0)
                {
                    return 1;
                }
                else if ((h1.city.compareTo(h2.city) == 0) && h1.stars < h2.stars)
                {
                    return 1;
                }
                else if ((h1.city.compareTo(h2.city) == 0) && h1.stars == h2.stars)
                {
                    return 0;
                }
                else if (h1.city.compareTo(h2.city) < 0)
                {
                    return -1;
                }
                else if ((h1.city.compareTo(h2.city) == 0) && h1.stars > h2.stars)
                {
                    return -1;
                }
                return 0;
            }
        });
    }


    public void fileOut()
    {
        try(FileWriter writer = new FileWriter("HotelOut.txt"))
        {
            for (int i = 0; i < arr.size(); i++)
            {
                writer.write(arr.get(i).city + " " + arr.get(i).name + " " + arr.get(i).stars + "\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void fileOutHotelsInTheCity(String city)
    {
        try(FileWriter writer = new FileWriter("HotelOut.txt"))
        {
            for (int i = 0; i < arr.size(); i++)
            {
                if (city.compareTo(arr.get(i).city) == 0)
                {
                    writer.write(arr.get(i).city + " " + arr.get(i).name + " " + arr.get(i).stars + "\n");
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    public void fileOutCitiesWithCurrHotel(String name)
    {
        try(FileWriter writer = new FileWriter("HotelOut.txt"))
        {
            for (int i = 0; i < arr.size(); i++)
            {
                if (name.compareTo(arr.get(i).name) == 0)
                {
                    writer.write(arr.get(i).city + "\n");
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}