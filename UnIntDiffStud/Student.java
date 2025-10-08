public class Student {
    long num;
    String name;
    int group;
    float grade;

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Student stud = (Student)obj;
        return 
        (
            num == stud.num 
            && name.equals(stud.name)
            && group == stud.group
            && grade == stud.grade
        );
    }

    @Override
    public int hashCode()
    {
        int id = (int)(grade / 1000000000);
        return id;
    }
    
}
