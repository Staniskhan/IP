
package app.src.main.java.org.example;

import app.src.main.java.org.CRTestJson.CRTestJson;
import app.src.main.java.org.CollectionRulerJson.CollectionRulerJson;


public class lab4json {
    public static void main(String args[]) {
        // Генерируем JSON файл с тестовыми данными
        CRTestJson.generateFile("input_files/lab4_input.json", 10, 5, 4, 7);
        
        // Создаем коллекцию из JSON файла
        CollectionRulerJson clctrlr = new CollectionRulerJson("input_files/lab4_input.json");
        
        // Выводим различную информацию в JSON файлы
        clctrlr.fileOutAllInformation("output_files/allInf.json");
        clctrlr.fileOutNames("output_files/names.json");
        clctrlr.fileOutAVG("output_files/AVG.json");
        
        clctrlr.sortAVGUp();
        clctrlr.fileOutAVG("output_files/sortUpAVG.json");
        
        clctrlr.sortAVGDown();
        clctrlr.fileOutAVG("output_files/sortDownAVG.json");
        
        clctrlr.sortNameUp();
        clctrlr.fileOutNames("output_files/sortUpName.json");
        
        clctrlr.sortNameDown();
        clctrlr.fileOutNames("output_files/sortDownName.json");
        
        clctrlr.sortYearGroupUp();
        clctrlr.fileOutAllInformation("output_files/sortUpYearGroup.json");
        
        clctrlr.sortYearGroupDown();
        clctrlr.fileOutAllInformation("output_files/sortDownYearGroup.json");
        
        clctrlr.fileOutExcellentStudentsAllInf("output_files/ExcellentStudentsAllInf.json");
        clctrlr.fileOutExcellentStudentsNames("output_files/ExcellentStudentsNames.json");
        clctrlr.fileOutExcellentStudentsAVG("output_files/ExcellentStudentsAVG.json");
    }
}