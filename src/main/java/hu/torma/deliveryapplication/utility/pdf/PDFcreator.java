package hu.torma.deliveryapplication.utility.pdf;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.SerializableSupplier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PDFcreator {

    CellStyle style;
    File file;
    ClassPathResource res;
    private Workbook workbook;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private Sheet sheet;
    Logger log = Logger.getLogger("excel");

    @PostConstruct
    private void createWorkBook() {
        try {
            res = new ClassPathResource("form.xlsx", this.getClass().getClassLoader());
            file = new File(res.getURI());
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt(0);
            style = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            style.setDataFormat(format.getFormat("0"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createRowCol(int row, int cell, String s) {
        if (sheet.getRow(row) == null) sheet.createRow(row);
        if (sheet.getRow(row).getCell(cell) == null) sheet.getRow(row).createCell(cell);
        sheet.getRow(row).getCell(cell).removeFormula();
        sheet.getRow(row).getCell(cell).removeCellComment();
        sheet.getRow(row).getCell(cell).removeHyperlink();
        sheet.getRow(row).getCell(cell).setCellValue(s);


    }
    private void createRowCol(int row, int cell, Integer s) {
        if (sheet.getRow(row) == null) sheet.createRow(row);
        if (sheet.getRow(row).getCell(cell) == null) sheet.getRow(row).createCell(cell);
        sheet.getRow(row).getCell(cell).removeFormula();
        sheet.getRow(row).getCell(cell).removeCellComment();
        sheet.getRow(row).getCell(cell).removeHyperlink();
        sheet.getRow(row).getCell(cell).setCellValue(s);


    }
    private void createRowCol(int row, int cell, Double s) {
        if (sheet.getRow(row) == null) sheet.createRow(row);
        if (sheet.getRow(row).getCell(cell) == null) sheet.getRow(row).createCell(cell);
        sheet.getRow(row).getCell(cell).removeFormula();
        sheet.getRow(row).getCell(cell).removeCellComment();
        sheet.getRow(row).getCell(cell).removeHyperlink();
        sheet.getRow(row).getCell(cell).setCellValue(s);


    }

    private void populateExcel(PurchaseDTO pur) {
        createRowCol(6, 5, pur.getVendor().getVendorName());
        createRowCol(7, 5, pur.getVendor().getBirthName());
        createRowCol(8, 5, pur.getVendor().getAddress());
        createRowCol(9, 5, pur.getVendor().getFelir());
        createRowCol(10, 5, pur.getVendor().getTaxId());
        createRowCol(11, 5, pur.getVendor().getTaxNumber());
        createRowCol(12, 5, pur.getVendor().getBirthPlace());
        createRowCol(13, 5, dateFormat.format(pur.getVendor().getBirthDate()));
        createRowCol(14, 5, pur.getVendor().getNameOfMother());
        createRowCol(15, 5, pur.getVendor().getTaj());
        createRowCol(16, 5, pur.getVendor().getFileNumber());
        createRowCol(17, 5, pur.getVendor().getGgn());
        createRowCol(18, 5, pur.getVendor().getPhone());
        createRowCol(19, 5, pur.getVendor().getAccountNumber());
        createRowCol(20, 5, pur.getVendor().getContract());
        //prices

        createRowCol(24, 5, pur.getProductList().get(0).getTotalPrice().intValue());
        createRowCol(25, 5, pur.getProductList().get(1).getTotalPrice().intValue());
        createRowCol(26, 5, pur.getProductList().get(2).getTotalPrice().intValue());
        createRowCol(27, 5, pur.getProductList().get(3).getTotalPrice().intValue());
        createRowCol(28, 5, pur.getProductList().get(4).getTotalPrice().intValue());
        createRowCol(29, 5, pur.getProductList().get(5).getTotalPrice().intValue());
        //unitprics
        createRowCol(24, 1, pur.getProductList().get(0).getUnitPrice().intValue());
        createRowCol(25, 1, pur.getProductList().get(1).getUnitPrice().intValue());
        createRowCol(26, 1, pur.getProductList().get(2).getUnitPrice().intValue());
        createRowCol(27, 1, pur.getProductList().get(3).getUnitPrice().intValue());
        createRowCol(28, 1, pur.getProductList().get(4).getUnitPrice().intValue());
        createRowCol(29, 1, pur.getProductList().get(5).getUnitPrice().intValue());
        //gross weigt
        createRowCol(24, 2, pur.getProductList().get(0).getQuantity().intValue());
        createRowCol(25, 2, pur.getProductList().get(1).getQuantity().intValue());
        createRowCol(26, 2, pur.getProductList().get(2).getQuantity().intValue());
        createRowCol(27, 2, pur.getProductList().get(3).getQuantity().intValue());
        createRowCol(28, 2, pur.getProductList().get(4).getQuantity().intValue());
        createRowCol(29, 2, pur.getProductList().get(5).getQuantity().intValue());
        //correction
        createRowCol(24, 3, pur.getProductList().get(0).getCorrPercent().intValue() +"%");
        createRowCol(25, 3, pur.getProductList().get(1).getCorrPercent().intValue() +"%");
        createRowCol(26, 3, pur.getProductList().get(2).getCorrPercent().intValue() +"%");
        createRowCol(27, 3, pur.getProductList().get(3).getCorrPercent().intValue() +"%");
        createRowCol(28, 3, pur.getProductList().get(4).getCorrPercent().intValue() +"%");
        createRowCol(29, 3, pur.getProductList().get(5).getCorrPercent().intValue() +"%");
        //net w
        createRowCol(24, 4, pur.getProductList().get(0).getQuantity2().intValue());
        createRowCol(25, 4, pur.getProductList().get(1).getQuantity2().intValue());
        createRowCol(26, 4, pur.getProductList().get(2).getQuantity2().intValue());
        createRowCol(27, 4, pur.getProductList().get(3).getQuantity2().intValue());
        createRowCol(28, 4, pur.getProductList().get(4).getQuantity2().intValue());
        createRowCol(29, 4, pur.getProductList().get(5).getQuantity2().intValue());
        //total
        var totalp = pur.getTotalPrice().intValue();
        createRowCol(30, 5, totalp);
        //totalW
        var totalw = pur.getProductList().stream().map(c->c.getQuantity2().intValue()).collect(Collectors.summingInt(Integer::intValue));
        createRowCol(30, 4, totalw);
        //fee
        createRowCol(31, 5, "Töltse ki");
        //avg price e40

        Double avgprice = (double)(Math.round((totalp / (double)totalw) *100) /100);
        log.info(avgprice+"");
        createRowCol(39, 4, avgprice);
        //f40
        var f40 =(int) Math.round(totalw * avgprice);
        createRowCol(39, 5, f40);
        //c40
        int c40 = (int) Math.round(f40 / 1.12);
        createRowCol(39, 2, c40);
        //d40
        int d40 = f40 - c40;
        createRowCol(39, 3, d40);
        //e41
        var e41 = (double) Math.round((avgprice / 1.12)*100) /100;
        createRowCol(40, 4, e41);
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
