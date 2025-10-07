import java.util.HashSet;
import java.util.Set;

public class SCRuler {
    public static StudCollection Union(StudCollection coll1, StudCollection coll2)
    {
        Set<Student> coll = new HashSet<>(coll1.studset);
        for(Student currstud : coll2.studset)
        {
            coll.add(currstud);
        }
        StudCollection studcol = new StudCollection(coll);
        return studcol;
    }

    public static StudCollection Intersection(StudCollection coll1, StudCollection coll2)
    {
        Set<Student> coll = new HashSet<>();
        for(Student currstud : coll2.studset)
        {
            if (coll1.studset.contains(currstud))
            {
                coll.add(currstud);
            }
        }
        StudCollection ret = new StudCollection(coll);
        return ret;
    }

    public static StudCollection Difference(StudCollection reduced_coll, StudCollection deductible_coll)
    {
        Set<Student> coll = new HashSet<>(reduced_coll.studset);
        for(Student currstud : deductible_coll.studset)
        {
            if (coll.contains(currstud))
            {
                coll.remove(currstud);
            }
        }
        StudCollection ret = new StudCollection(coll);
        return ret;
    }
}
