
public class HotelMain {
    public static void main(String[] args) 
    {
        Hotel h = new Hotel();
        h.sort();
        h.fileOut();
        h.fileOutHotelsInTheCity("moskva");
        h.fileOutCitiesWithCurrHotel("moskvaHotel");
    }
}
