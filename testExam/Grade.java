public class Grade
{
    private Integer studentId;
    private String subjectName;
    private int grade;

    public Grade(Integer studentId, String subjectName, int grade)
    {
        this.studentId = studentId;
        this.subjectName = subjectName;
        this.grade = grade;
    }

    public Grade()
    {
        this.studentId = 0;
        this.subjectName = "";
        this.grade = 0;
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
        return subjectName;
    }

    public void setName(String name)
    {
        this.subjectName = name;
    }

    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        this.grade = grade;
    }
}
