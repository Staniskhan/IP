package app.src.main.java.org.CollectionRulerJson;

import org.json.JSONArray;
import org.json.JSONObject;

import app.src.main.java.org.StudRecordBookJson.StudRecordBookJson;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.Vector;

public class CollectionRulerJson {
    public static float AVGAllSemsMark(StudRecordBookJson srb) {
        int[] arr = srb.getAllSemesterMarks();
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--) {
            if (arr[i] >= 0) {
                avg += (float)arr[i];
            } else {
                count--;
            }
        }

        avg /= (float)count;
        avg = (float)Math.round(avg * 100) / 100;
        return avg;
    }

    public static float AVGAllExamsMark(StudRecordBookJson srb) {
        int[] arr = srb.getAllExamMarks();
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--) {
            if (arr[i] >= 0) {
                avg += (float)arr[i];
            } else {
                count--;
            }
        }

        avg /= (float)count;
        avg = (float)Math.round(avg * 100) / 100;
        return avg;
    }

    public static float AVGExamMarks(StudRecordBookJson srb, int numOfSes) {
        int arr[] = srb.getExamMarks(numOfSes);
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--) {
            if (arr[i] >= 0) {
                avg += (float)arr[i];
            } else {
                count--;
            }
        }

        avg /= (float)count;
        avg = (float)Math.round(avg * 100) / 100;
        return avg;
    }

    public static float AVGSemMarks(StudRecordBookJson srb, int numOfSem) {
        int arr[] = srb.getSemMarks(numOfSem);
        int count = arr.length;
        float avg = 0;
        for (int i = count - 1; i >= 0; i--) {
            if (arr[i] >= 0) {
                avg += (float)arr[i];
            } else {
                count--;
            }
        }

        avg /= (float)count;
        avg = (float)Math.round(avg * 100) / 100;
        return avg;
    }

    public static float AVG(StudRecordBookJson srb) {
        int[] arr1 = srb.getAllSemesterMarks();
        int[] arr2 = srb.getAllExamMarks();
        int count = arr1.length;
        count += arr2.length;

        int[] arr = new int[count];
        float avg = 0;

        for (int i = 0; i < arr1.length; i++) {
            arr[i] = arr1[i];
        }
        for (int i = 0; i < arr2.length; i++) {
            arr[arr1.length + i] = arr2[i];
        }

        for (int i = count - 1; i >= 0; i--) {
            if (arr[i] >= 0) {
                avg += (float)arr[i];
            } else {
                count--;
            }
        }

        avg /= (float)count;
        avg = (float)Math.round(avg * 100) / 100;
        return avg;
    }

    public int number_of_students;
    public Vector<StudRecordBookJson> students;

    public CollectionRulerJson(String filename) {
        try {
            students = new Vector<>();
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONObject json = new JSONObject(content);
            
            number_of_students = json.getInt("number_of_students");
            JSONArray studentsArray = json.getJSONArray("students");
            
            for (int i = 0; i < studentsArray.length(); i++) {
                StudRecordBookJson student = StudRecordBookJson.fromJson(studentsArray.getJSONObject(i));
                students.addElement(student);
            }
        } catch (Exception e) {
            System.out.println("OOPS! SMTH WENT WRONG. HERE'S THE DISCRIPTION OF THE EXCEPTION:");
            e.printStackTrace();
        }
    }
    
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("number_of_students", number_of_students);
        
        JSONArray studentsArray = new JSONArray();
        for (StudRecordBookJson student : students) {
            studentsArray.put(student.toJson());
        }
        json.put("students", studentsArray);
        
        return json;
    }

    public void fileOutAllInformation(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(toJson().toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOutNames(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            JSONObject json = new JSONObject();
            json.put("number_of_students", number_of_students);
            
            JSONArray namesArray = new JSONArray();
            for (int i = 0; i < number_of_students; i++) {
                JSONObject studentJson = new JSONObject();
                studentJson.put("number", i + 1);
                studentJson.put("surname", students.get(i).surname);
                studentJson.put("name", students.get(i).name);
                studentJson.put("second_name", students.get(i).second_name);
                namesArray.put(studentJson);
            }
            json.put("students", namesArray);
            
            writer.write(json.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOutAVG(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            JSONObject json = new JSONObject();
            json.put("number_of_students", number_of_students);
            
            JSONArray avgArray = new JSONArray();
            for (int i = 0; i < number_of_students; i++) {
                JSONObject studentJson = new JSONObject();
                studentJson.put("number", i + 1);
                studentJson.put("surname", students.get(i).surname);
                studentJson.put("name", students.get(i).name);
                studentJson.put("second_name", students.get(i).second_name);
                studentJson.put("avg", AVG(students.get(i)));
                avgArray.put(studentJson);
            }
            json.put("students", avgArray);
            
            writer.write(json.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOutSemAVG(String filename, int num_of_sem) {
        try (FileWriter writer = new FileWriter(filename)) {
            JSONObject json = new JSONObject();
            json.put("number_of_students", number_of_students);
            json.put("semester_number", num_of_sem);
            
            JSONArray semAvgArray = new JSONArray();
            for (int i = 0; i < number_of_students; i++) {
                JSONObject studentJson = new JSONObject();
                studentJson.put("number", i + 1);
                studentJson.put("surname", students.get(i).surname);
                studentJson.put("name", students.get(i).name);
                studentJson.put("second_name", students.get(i).second_name);
                float semAvg = (AVGSemMarks(students.get(i), num_of_sem) + AVGExamMarks(students.get(i), num_of_sem)) / 2;
                studentJson.put("semester_avg", semAvg);
                semAvgArray.put(studentJson);
            }
            json.put("students", semAvgArray);
            
            writer.write(json.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOutExcellentStudentsAllInf(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            JSONArray excellentArray = new JSONArray();
            
            for (int i = 0; i < number_of_students; i++) {
                if (students.get(i).isExcellentStudent()) {
                    excellentArray.put(students.get(i).toJson());
                }
            }
            
            JSONObject json = new JSONObject();
            json.put("excellent_students_count", excellentArray.length());
            json.put("excellent_students", excellentArray);
            
            writer.write(json.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOutExcellentStudentsNames(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            JSONArray excellentArray = new JSONArray();
            
            for (int i = 0; i < number_of_students; i++) {
                if (students.get(i).isExcellentStudent()) {
                    JSONObject studentJson = new JSONObject();
                    studentJson.put("surname", students.get(i).surname);
                    studentJson.put("name", students.get(i).name);
                    studentJson.put("second_name", students.get(i).second_name);
                    excellentArray.put(studentJson);
                }
            }
            
            JSONObject json = new JSONObject();
            json.put("excellent_students_count", excellentArray.length());
            json.put("excellent_students", excellentArray);
            
            writer.write(json.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileOutExcellentStudentsAVG(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            JSONArray excellentArray = new JSONArray();
            
            for (int i = 0; i < number_of_students; i++) {
                if (students.get(i).isExcellentStudent()) {
                    JSONObject studentJson = new JSONObject();
                    studentJson.put("surname", students.get(i).surname);
                    studentJson.put("name", students.get(i).name);
                    studentJson.put("second_name", students.get(i).second_name);
                    studentJson.put("avg", AVG(students.get(i)));
                    excellentArray.put(studentJson);
                }
            }
            
            JSONObject json = new JSONObject();
            json.put("excellent_students_count", excellentArray.length());
            json.put("excellent_students", excellentArray);
            
            writer.write(json.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortAVGUp() {
        for (int i = 0; i < number_of_students - 1; i++) {
            for (int j = 1; j < number_of_students; j++) {
                if (AVG(students.get(j - 1)) > AVG(students.get(j))) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }

    public void sortAVGDown() {
        for (int i = 0; i < number_of_students - 1; i++) {
            for (int j = 1; j < number_of_students; j++) {
                if (AVG(students.get(j - 1)) < AVG(students.get(j))) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }

    public void sortNameUp() {
        for (int i = 0; i < number_of_students - 1; i++) {
            for (int j = 1; j < number_of_students; j++) {
                String str1 = students.get(j - 1).surname + " " + students.get(j - 1).name + " " + students.get(j - 1).second_name;
                String str2 = students.get(j).surname + " " + students.get(j).name + " " + students.get(j).second_name;
                if (str1.compareTo(str2) > 0) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }

    public void sortNameDown() {
        for (int i = 0; i < number_of_students - 1; i++) {
            for (int j = 1; j < number_of_students; j++) {
                String str1 = students.get(j - 1).surname + " " + students.get(j - 1).name + " " + students.get(j - 1).second_name;
                String str2 = students.get(j).surname + " " + students.get(j).name + " " + students.get(j).second_name;
                if (str2.compareTo(str1) > 0) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }

    public void sortYearGroupUp() {
        for (int i = 0; i < number_of_students - 1; i++) {
            for (int j = 1; j < number_of_students; j++) {
                if (students.get(j - 1).year > students.get(j).year) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                } else if ((students.get(j - 1).year == students.get(j).year) && (students.get(j - 1).group > students.get(j).group)) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }

    public void sortYearGroupDown() {
        for (int i = 0; i < number_of_students - 1; i++) {
            for (int j = 1; j < number_of_students; j++) {
                if (students.get(j - 1).year < students.get(j).year) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                } else if ((students.get(j - 1).year == students.get(j).year) && (students.get(j - 1).group < students.get(j).group)) {
                    StudRecordBookJson srb = students.get(j - 1);
                    students.set(j - 1, students.get(j));
                    students.set(j, srb);
                }
            }
        }
    }
}