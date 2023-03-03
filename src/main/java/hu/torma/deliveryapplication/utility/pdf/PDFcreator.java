package hu.torma.deliveryapplication.utility.pdf;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.SerializableSupplier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.logging.Logger;

@Service
public class PDFcreator {
    File file;
    ClassPathResource res;
    private Workbook workbook;
    private Sheet sheet;
    Logger log = Logger.getLogger("excel");

    @PostConstruct
    private void createWorkBook() {
        try {
            res = new ClassPathResource("form.xlsx", this.getClass().getClassLoader());
            file = new File(res.getURI());
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRowCol(int row, int cell, String s) {
        if (sheet.getRow(row) == null) sheet.createRow(row);
        if (sheet.getRow(row).getCell(cell) == null) sheet.getRow(row).createCell(cell);
        sheet.getRow(row).getCell(cell).setCellValue(s);
    }

    private void populateExcel(PurchaseDTO pur) {
        createRowCol(6, 5, pur.getVendor().getVendorName());
    }

    public StreamedContent createDownload(PurchaseDTO pur) {
        populateExcel(pur);

        try {
            // create a new file with a modified name
            String modifiedFileName = "merlegelesi_jegy_" + pur.getId() + ".xlsx";
            File modifiedFile = new File(modifiedFileName);

            // write the workbook to the modified file
            FileOutputStream fos = new FileOutputStream(modifiedFile);
            workbook.write(fos);
            fos.close();

            // create the StreamedContent object
            InputStream stream = new FileInputStream(modifiedFile);
            StreamedContent file = DefaultStreamedContent.builder()
                    .stream(() -> {
                        try {
                            return new FileInputStream(modifiedFile);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .contentType("application/vnd.ms-excel")
                    .name(modifiedFileName)
                    .build();

            // delete the modified file after it has been downloaded
            modifiedFile.delete();

            // close the workbook and return the StreamedContent object

            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
