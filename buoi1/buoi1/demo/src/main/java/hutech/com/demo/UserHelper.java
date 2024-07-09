package hutech.com.demo;

import hutech.com.demo.model.Product;
import hutech.com.demo.model.User;
import hutech.com.demo.service.UserService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class UserHelper {

    public static String[] HEADERS ={
            "id",
            "email",
            "phone",
            "provider",
            "username"

    };

    public static String SHEET_NAME = "USERS";
    private static Workbook workBook;
    private static ByteArrayOutputStream outputStream;


    private static final UserService userService = new UserService();
    public static ByteArrayInputStream dataToExel(List<User> list) throws IOException {
        try{
            workBook = new XSSFWorkbook();
            outputStream = new ByteArrayOutputStream();

            //Create sheet
            Sheet sheet = workBook.createSheet(SHEET_NAME);

            //Create row: header row
            Row row = sheet.createRow(0);

            for(int i = 0; i < HEADERS.length; i++){
                Cell cell =row.createCell(i);
                cell.setCellValue(HEADERS[i]);
            }
            int rowIndex = 1;
            for(User p : list){
                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;
                dataRow.createCell(0).setCellValue(p.getId());
                dataRow.createCell(1).setCellValue(p.getEmail());
                dataRow.createCell(2).setCellValue(p.getPhone());
                dataRow.createCell(3).setCellValue(p.getProvider());
                dataRow.createCell(4).setCellValue(p.getUsername());
                //dataRow.createCell(5).setCellValue(userService.getUserRoles(p.getId()).toString());
            }

            workBook.write(outputStream);

            return new ByteArrayInputStream(outputStream.toByteArray());
        }catch(Exception e){e.printStackTrace();}
        finally {
            workBook.close();
            outputStream.close();
        }
        return null;
    }
}
