package hu.torma.deliveryapplication.utility.pdf;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Objects;
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



            //file = new File(this.getClass().getClassLoader().getResource("form.xlsx"));
            //file = new File(res.getURI());
            workbook = new XSSFWorkbook(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("form.xlsx")));
           // workbook = new XSSFWorkbook(file);





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
    private void createRowCol(int row, int cell, Double s, Boolean b) {
        if (sheet.getRow(row) == null) sheet.createRow(row);
        if (sheet.getRow(row).getCell(cell) == null) sheet.getRow(row).createCell(cell);
        sheet.getRow(row).getCell(cell).removeFormula();
        sheet.getRow(row).getCell(cell).removeCellComment();
        sheet.getRow(row).getCell(cell).removeHyperlink();
        sheet.getRow(row).getCell(cell).setCellValue(s);
        var st = sheet.getRow(row).getCell(cell).getCellStyle();
        st.setWrapText(true);
        sheet.getRow(row).getCell(cell).setCellStyle(st);
    }
    private void createRowCol(int row, int cell, String s, Boolean b) {
        if (sheet.getRow(row) == null) sheet.createRow(row);
        if (sheet.getRow(row).getCell(cell) == null) sheet.getRow(row).createCell(cell);
        sheet.getRow(row).getCell(cell).removeFormula();
        sheet.getRow(row).getCell(cell).removeCellComment();
        sheet.getRow(row).getCell(cell).removeHyperlink();
        sheet.getRow(row).getCell(cell).setCellValue(s);
        var st = sheet.getRow(row).getCell(cell).getCellStyle();
        st.setWrapText(true);
        sheet.getRow(row).getCell(cell).setCellStyle(st);
    }
    public void setNullToEmpty(PurchaseDTO pur) {

    }
    private void populateExcel(PurchaseDTO pur) {
        setNullToEmpty(pur);
        createRowCol(6, 5, pur.getVendor().getVendorName(),true);
        createRowCol(7, 5, pur.getVendor().getBirthName(),true);
        createRowCol(8, 5, pur.getVendor().getCity()+", "+pur.getVendor().getAddress(),true);
        createRowCol(9, 5, pur.getVendor().getFelir(),true);
        createRowCol(10, 5, pur.getVendor().getTaxId(),true);
        createRowCol(11, 5, pur.getVendor().getTaxNumber(),true);
        createRowCol(12, 5, pur.getVendor().getBirthPlace(),true);
        createRowCol(13, 5, dateFormat.format(pur.getVendor().getBirthDate()),true);
        createRowCol(14, 5, pur.getVendor().getNameOfMother(),true);
        createRowCol(15, 5, pur.getVendor().getTaj(),true);
        createRowCol(16, 5, pur.getVendor().getFileNumber(),true);
        createRowCol(17, 5, pur.getVendor().getActivity(),true);
        createRowCol(18, 5, pur.getVendor().getPhone(),true);
        createRowCol(19, 5, pur.getVendor().getAccountNumber(),true);
        createRowCol(20, 5, pur.getVendor().getContract(),true);

        //date
        String datee = dateFormat.format(pur.getReceiptDate());
        createRowCol(5, 1, datee);
        //prices

        createRowCol(24, 5, pur.getProductList().get(0).getTotalPrice());
        createRowCol(25, 5, pur.getProductList().get(1).getTotalPrice());
        createRowCol(26, 5, pur.getProductList().get(2).getTotalPrice());
        createRowCol(27, 5, pur.getProductList().get(3).getTotalPrice());
        createRowCol(28, 5, pur.getProductList().get(4).getTotalPrice());
        createRowCol(29, 5, pur.getProductList().get(5).getTotalPrice());
        //unitprics
        createRowCol(24, 1, pur.getProductList().get(0).getUnitPrice());
        createRowCol(25, 1, pur.getProductList().get(1).getUnitPrice());
        createRowCol(26, 1, pur.getProductList().get(2).getUnitPrice());
        createRowCol(27, 1, pur.getProductList().get(3).getUnitPrice());
        createRowCol(28, 1, pur.getProductList().get(4).getUnitPrice());
        createRowCol(29, 1, pur.getProductList().get(5).getUnitPrice());
        //gross weigt
        createRowCol(24, 2, pur.getProductList().get(0).getQuantity());
        createRowCol(25, 2, pur.getProductList().get(1).getQuantity());
        createRowCol(26, 2, pur.getProductList().get(2).getQuantity());
        createRowCol(27, 2, pur.getProductList().get(3).getQuantity());
        createRowCol(28, 2, pur.getProductList().get(4).getQuantity());
        createRowCol(29, 2, pur.getProductList().get(5).getQuantity());
        //correction
        createRowCol(24, 3, pur.getProductList().get(0).getCorrPercent() + "%");
        createRowCol(25, 3, pur.getProductList().get(1).getCorrPercent() + "%");
        createRowCol(26, 3, pur.getProductList().get(2).getCorrPercent() + "%");
        createRowCol(27, 3, pur.getProductList().get(3).getCorrPercent() + "%");
        createRowCol(28, 3, pur.getProductList().get(4).getCorrPercent() + "%");
        createRowCol(29, 3, pur.getProductList().get(5).getCorrPercent() + "%");
        //net w
        createRowCol(24, 4, pur.getProductList().get(0).getQuantity2());
        createRowCol(25, 4, pur.getProductList().get(1).getQuantity2());
        createRowCol(26, 4, pur.getProductList().get(2).getQuantity2());
        createRowCol(27, 4, pur.getProductList().get(3).getQuantity2());
        createRowCol(28, 4, pur.getProductList().get(4).getQuantity2());
        createRowCol(29, 4, pur.getProductList().get(5).getQuantity2());
        //total
        var totalp = pur.getTotalPrice().intValue();
        createRowCol(30, 5, totalp);
        //totalW
        var totalw = (Integer) pur.getProductList().stream().map(PurchasedProductDTO::getQuantity2).mapToInt(Integer::intValue).sum();
        createRowCol(30, 4, totalw);

        //avg price e40

        Double avgprice = (double)Math.round((totalp / (double) totalw * 100)) /100 ;
        log.info(avgprice + "");
        createRowCol(39, 4, avgprice);
        //f40
        var f40 = (int) Math.round(totalw * avgprice);
        createRowCol(39, 5, f40);
        //c40
        int c40 = (int) Math.round(f40 / 1.12);
        createRowCol(39, 2, c40);
        //d40
        int d40 = f40 - c40;
        createRowCol(39, 3, d40);
        //e41
        var e41 = (double) Math.round((avgprice / 1.12) * 100) / 100;
        createRowCol(40, 4, e41);
    }

    public StreamedContent createDownload(PurchaseDTO pur) {
        populateExcel(pur);

        try {
           /*
            String modifiedFileName = "merlegelesi_jegy_" + pur.getId() + ".xlsx";
            var modifiedPath = new ClassPathResource(modifiedFileName, this.getClass().getClassLoader());
            File modifiedFile = new File(modifiedPath.getPath());
*/

            String currentDirectory = System.getProperty("user.dir");
            String modifiedFileName = "merlegelesi_jegy_" + pur.getId() + ".xlsx";
            String filePath = currentDirectory + File.separator + modifiedFileName;

            // Create the file object
            File modifiedFile = new File(filePath);
            log.info("Filepaths of excel are: \n" + filePath+"\n"+currentDirectory );
            //FileOutputStream fos = new FileOutputStream(modifiedFile);
            FileOutputStream fos = new FileOutputStream(modifiedFile);
            workbook.write(fos);

            fos.close();

            // create the StreamedContent object
            InputStream stream = new FileInputStream(modifiedFile);
            StreamedContent file = DefaultStreamedContent.builder()
                    .stream(()-> stream)
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
