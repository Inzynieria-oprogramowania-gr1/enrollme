package com.company.project.fileGenerators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.company.project.dto.enrollment.EnrollmentResultsDto;
import com.company.project.dto.timetable.SpecifiedTimeslotDto;
import com.company.project.entity.Weekday;
import com.company.project.service.StudentService;

import lombok.Getter;


@Component
public class XlsxGenerator {
    private final StudentService studentService;
    private @Getter Workbook workbook;
    private boolean generated;
    private ByteArrayOutputStream outputStream;

    public XlsxGenerator(StudentService studentService){
        this.studentService = studentService;
        this.workbook = new XSSFWorkbook();
        this.generated = false;
        this.outputStream = new ByteArrayOutputStream();
    }
    public void clear(){
        this.generated = false;
        this.workbook = new XSSFWorkbook();
        this.outputStream = new ByteArrayOutputStream();
    }
    public void generate() throws IOException{
        if(this.generated){
            return;
        }
        this.generated = true;
        List<EnrollmentResultsDto> results = studentService.getResults();

        HashMap<Weekday, List<EnrollmentResultsDto>> weekdaySlotMap = new HashMap<>(){{
            for(Weekday day: Weekday.values()){
                put(day, new ArrayList<>());
            }
        }};
        
        for(EnrollmentResultsDto enroll: results){
            weekdaySlotMap.get(enroll.timeslotDto().weekday()).add(enroll);
        }
        Sheet sheet = this.workbook.createSheet("Rezultaty");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        int rowNum = 0;
        for(var weekdayEnum: Weekday.values()){

            String weekdayTmp = "";
            switch (weekdayEnum) {
                case Monday:
                    weekdayTmp = "Poniedziałek";
                    break;
                case Tuesday:
                    weekdayTmp = "Wtorek";
                    break;
                case Wednesday:
                    weekdayTmp = "Środa";
                    break;
                case Thursday:
                    weekdayTmp = "Czwartek";
                    break;
                case Friday:
                    weekdayTmp = "Piątek";
                    break;
            }
            for(EnrollmentResultsDto enroll: results){
                var l = weekdaySlotMap.get(enroll.timeslotDto().weekday())
                .stream().sorted((a,b)->a.timeslotDto().startTime().compareTo(b.timeslotDto().startTime()))
                .toList();
                weekdaySlotMap.put(enroll.timeslotDto().weekday(), l);
            }
            List<EnrollmentResultsDto> enrollList = weekdaySlotMap.get(weekdayEnum);
            List<String> rowList = new ArrayList<>();
            rowList.add(weekdayTmp);
            
            if(enrollList.size() == 0){
                continue;
            }
            for(EnrollmentResultsDto resultsDto: enrollList){
                String start = formatter.format(resultsDto.timeslotDto().startTime());
                String end = formatter.format(resultsDto.timeslotDto().endTime());
                String resultTime = start+"-"+end;
                rowList.add(resultTime);
            }

            addRow(rowList, sheet, rowNum);
            rowNum++;



            int maxStudents = 0;
            for(var en: enrollList){
                maxStudents = Math.max(maxStudents, en.studentDto().size());
            }
            for(int i=0;i<maxStudents;i++){
                rowList = new ArrayList<>(){{
                    add(null);
                }};
                for(var en: enrollList){
                    String email;
                    try {
                        email = en.studentDto().get(i).email();
                    } catch (IndexOutOfBoundsException e) {
                        email = null;
                    }
                    rowList.add(email);
                }
                addRow(rowList, sheet, rowNum);
                rowNum++;
            }
            rowNum+=2;
        }
        this.workbook.write(outputStream);
        this.workbook.close();
    }
    public byte[] getWorkbookAsByteArray(){
        return outputStream.toByteArray();
    }
    
    private void addRow(List<String> values, Sheet sheet, int rowNumber){
        Row row = sheet.createRow(rowNumber);
        for(int i=0;i<values.size();i++){
            row.createCell(i).setCellValue(values.get(i));
        }
    }
}
