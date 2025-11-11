package app.src.main.java.org.StudRecordBookJson;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.Vector;

public class StudRecordBookJson {
    public static class Discipline {
        public String disciplineName;
        public int semesterMark;
        public int testMark;
        public int examMark;
        
        public Discipline() {}
        
        public Discipline(String name, int semMark, int testMark, int examMark) {
            this.disciplineName = name;
            this.semesterMark = semMark;
            this.testMark = testMark;
            this.examMark = examMark;
        }
        
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("disciplineName", disciplineName);
            json.put("semesterMark", semesterMark);
            json.put("testMark", testMark);
            json.put("examMark", examMark);
            return json;
        }
        
        public static Discipline fromJson(JSONObject json) {
            Discipline disc = new Discipline();
            disc.disciplineName = json.getString("disciplineName");
            disc.semesterMark = json.getInt("semesterMark");
            disc.testMark = json.getInt("testMark");
            disc.examMark = json.getInt("examMark");
            return disc;
        }
    }

    public static class Session {
        public int number;
        public Vector<Discipline> disciplines = new Vector<>();
        public int number_of_disciplines;
        
        public Session() {}
        
        public Session(String args) {
            StringTokenizer strtok = new StringTokenizer(args, "\\s+");
            number_of_disciplines = Integer.parseInt(strtok.nextToken());
            for (int i = 0; i < number_of_disciplines; i++) {
                Discipline cd = new Discipline();
                cd.disciplineName = strtok.nextToken();
                cd.semesterMark = Integer.parseInt(strtok.nextToken());
                cd.testMark = Integer.parseInt(strtok.nextToken());
                cd.examMark = Integer.parseInt(strtok.nextToken());
                disciplines.addElement(cd);
            }
        }
        
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("number", number);
            json.put("number_of_disciplines", number_of_disciplines);
            
            JSONArray disciplinesArray = new JSONArray();
            for (Discipline disc : disciplines) {
                disciplinesArray.put(disc.toJson());
            }
            json.put("disciplines", disciplinesArray);
            
            return json;
        }
        
        public static Session fromJson(JSONObject json) {
            Session session = new Session();
            session.number = json.getInt("number");
            session.number_of_disciplines = json.getInt("number_of_disciplines");
            
            JSONArray disciplinesArray = json.getJSONArray("disciplines");
            for (int i = 0; i < disciplinesArray.length(); i++) {
                Discipline disc = Discipline.fromJson(disciplinesArray.getJSONObject(i));
                session.disciplines.addElement(disc);
            }
            
            return session;
        }
    }

    public String surname;
    public String name;
    public String second_name;
    public int year;
    public int group;
    public int semester;
    public Vector<Session> sessions;
    public int number_of_passed_sessions;

    public StudRecordBookJson() {}
    
    public StudRecordBookJson(String _surname, String _name, String _second_name, int _year, int _group, int _semester, String args) {
        surname = _surname;
        name = _name;
        second_name = _second_name;
        year = _year;
        group = _group;
        semester = _semester;
        StringTokenizer strtok = new StringTokenizer(args, "\\s+");
        String nopestr = strtok.nextToken();
        number_of_passed_sessions = Integer.parseInt(nopestr);
        sessions = new Vector<>(number_of_passed_sessions);
        for (int i = 1; i <= number_of_passed_sessions; i++) {
            String toDisc = strtok.nextToken();
            int number_of_disciplines = Integer.parseInt(toDisc);
            
            for (int j = 0; j < 4 * number_of_disciplines; j++) {
                toDisc += " ";
                toDisc += strtok.nextToken();
            }
            Session cs = new Session(toDisc);
            cs.number = i;
            sessions.addElement(cs);
        }
    }
    
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("surname", surname);
        json.put("name", name);
        json.put("second_name", second_name);
        json.put("year", year);
        json.put("group", group);
        json.put("semester", semester);
        json.put("number_of_passed_sessions", number_of_passed_sessions);
        
        JSONArray sessionsArray = new JSONArray();
        for (Session session : sessions) {
            sessionsArray.put(session.toJson());
        }
        json.put("sessions", sessionsArray);
        
        return json;
    }
    
    public static StudRecordBookJson fromJson(JSONObject json) {
        StudRecordBookJson student = new StudRecordBookJson();
        student.surname = json.getString("surname");
        student.name = json.getString("name");
        student.second_name = json.getString("second_name");
        student.year = json.getInt("year");
        student.group = json.getInt("group");
        student.semester = json.getInt("semester");
        student.number_of_passed_sessions = json.getInt("number_of_passed_sessions");
        
        student.sessions = new Vector<>();
        JSONArray sessionsArray = json.getJSONArray("sessions");
        for (int i = 0; i < sessionsArray.length(); i++) {
            Session session = Session.fromJson(sessionsArray.getJSONObject(i));
            student.sessions.addElement(session);
        }
        
        return student;
    }

    public void print() {
        System.out.println("-------------------------\nsurname: " + surname + "\nname: " + name + "\nsecond name: " + second_name + "\nyear: " + year + "\ngroup: " + group + "\nsemester: " + semester);
        for (int i = 0; i < sessions.size(); i++) {
            System.out.println(sessions.get(i).toJson().toString(2));
        }
        System.out.println("-------------------------");
    }
    
    public void fileOut(String filename) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(toJson().toString(2));
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int[] getAllExamMarks() {
        int count = 0;
        for (int i = 0; i < number_of_passed_sessions; i++) {
            count += sessions.get(i).number_of_disciplines;
        }

        int ret[] = new int[count];
        int k = 0;
        for (int i = 0; i < number_of_passed_sessions; i++) {
            for (int j = 0; j < sessions.get(i).number_of_disciplines; j++) {
                ret[k] = sessions.get(i).disciplines.get(j).examMark;
                k++;
            }
        }
        return ret;
    }

    public int[] getAllSemesterMarks() {
        int count = 0;
        for (int i = 0; i < number_of_passed_sessions; i++) {
            count += sessions.get(i).number_of_disciplines;
        }

        int ret[] = new int[count];
        int k = 0;
        for (int i = 0; i < number_of_passed_sessions; i++) {
            for (int j = 0; j < sessions.get(i).number_of_disciplines; j++) {
                ret[k] = sessions.get(i).disciplines.get(j).semesterMark;
                k++;
            }
        }
        return ret;
    }

    public int[] getExamMarks(int numOfSession) {
        if (numOfSession - 1 > sessions.size()) {
            int ret[] = new int[1];
            return ret;
        }
        int ret[] = new int[sessions.get(numOfSession - 1).number_of_disciplines];
        for (int i = 0; i < sessions.get(numOfSession - 1).number_of_disciplines; i++) {
            ret[i] = sessions.get(numOfSession - 1).disciplines.get(i).examMark;
        }
        return ret;
    }

    public int[] getSemMarks(int numOfSem) {
        if (numOfSem - 1 > sessions.size()) {
            int ret[] = new int[1];
            return ret;
        }
        int ret[] = new int[sessions.get(numOfSem - 1).number_of_disciplines];
        for (int i = 0; i < sessions.get(numOfSem - 1).number_of_disciplines; i++) {
            ret[i] = sessions.get(numOfSem - 1).disciplines.get(i).semesterMark;
        }
        return ret;
    }

    public boolean isExcellentStudent() {
        boolean ret = true;
        for (int i = 0; i < number_of_passed_sessions; i++) {
            for (int j = 0; j < sessions.get(i).number_of_disciplines; j++) {
                if (((sessions.get(i).disciplines.get(j).examMark < 9) && (sessions.get(i).disciplines.get(j).examMark > -2))
                || ((sessions.get(i).disciplines.get(j).semesterMark < 9) && (sessions.get(i).disciplines.get(j).semesterMark > -2))
                || ((sessions.get(i).disciplines.get(j).testMark < 1) && (sessions.get(i).disciplines.get(j).testMark > -2))) {
                    ret = false;
                    break;
                }
            }
            if (!ret) {
                break;
            }
        }
        return ret;
    }
}