public class Student
{
    private Integer studentId;
    String name;

    public Student(Integer ID, String name)
    {
        this.studentId = ID;
        this.name = name;
    }

    public Student()
    {
        this.studentId = 0;
        this.name = "";
    }

    public Integer getID()
    {
        return studentId;
    }

    public void setID(Integer ID)
    {
        studentId = ID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}