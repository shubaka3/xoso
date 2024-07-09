package hutech.com.demo;

import hutech.com.demo.model.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Helper {

    public static String[] HEADERS ={
            "id",
            "description",
            "price",
            "category_id"
    };

    public static String SHEET_NAME = "PRODUCTS";
    private static Workbook workBook;
    private static ByteArrayOutputStream outputStream;
    public static ByteArrayInputStream dataToExel(List<Product> list) throws IOException {
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
            for(Product p : list){
                Row dataRow = sheet.createRow(rowIndex);
                rowIndex++;
                dataRow.createCell(0).setCellValue(p.getId());
                dataRow.createCell(1).setCellValue(p.getDescription());
                dataRow.createCell(2).setCellValue(p.getPrice());
                dataRow.createCell(3).setCellValue(p.getCategory().getId());
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
