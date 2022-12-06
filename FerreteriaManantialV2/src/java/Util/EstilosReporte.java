/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
/**
 *
 * @author chejo
 */
public class EstilosReporte {
    public void insertImage(Workbook workbook, Sheet sheet,String direccion) throws FileNotFoundException, IOException {

            //fila1
            final FileInputStream stream = new FileInputStream(direccion);
            final CreationHelper helper = workbook.getCreationHelper();
            final Drawing drawing = sheet.createDrawingPatriarch();

            final ClientAnchor anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

            final int pictureIndex = workbook.addPicture(IOUtils.toByteArray(stream), Workbook.PICTURE_TYPE_PNG);

            anchor.setCol1(0);
            anchor.setRow1(0); // same row is okay
            anchor.setRow2(0);
            anchor.setCol2(0);
            final Picture pict = drawing.createPicture(anchor, pictureIndex);
            pict.resize();
    }

    //estilo Encabezado
    public CellStyle Estilo1(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial");
        font.setItalic(true);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }
    //estilo Encabezado

    public CellStyle EstiloEnteros1(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);

        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial");
        font.setItalic(true);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    //estilo cuerpo del documento
    public CellStyle Estilo2(Workbook workbook) {
        CellStyle style2 = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        style2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

        font2.setFontHeightInPoints((short) 10);
        font2.setFontName("Arial");
        font2.setItalic(true);
        style2.setFont(font2);
        return style2;
    }

     public CellStyle EstiloEnteros2(Workbook workbook) {
        CellStyle style2 = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        style2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);

        font2.setFontHeightInPoints((short) 10);
        font2.setFontName("Arial");
        font2.setItalic(true);
        style2.setFont(font2);
        return style2;
    }
    
        public CellStyle EstiloSinBorde(Workbook workbook) {
        CellStyle style2 = workbook.createCellStyle();
        Font font2 = workbook.createFont();
        font2.setFontHeightInPoints((short) 10);
        font2.setFontName("Arial");
        font2.setItalic(true);
        style2.setFont(font2);
        return style2;
    }
    
    public CellStyle Estilo3(Workbook workbook) {
        CellStyle style3 = workbook.createCellStyle();
        Font font3 = workbook.createFont();
        style3.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style3.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style3.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style3.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        //style3.getWrapText();
        //style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

        font3.setFontHeightInPoints((short) 10);
        font3.setFontName("Arial");
        font3.setItalic(true);
        font3.setBoldweight ( Font.BOLDWEIGHT_BOLD );
        style3.setFont(font3);
        return style3;
    }
    
    
        public CellStyle Estilo4(Workbook workbook) {
        CellStyle style4 = workbook.createCellStyle();
        Font font4 = workbook.createFont();
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style4.setWrapText(true);
        style4.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style4.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style4.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style4.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        //style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));

        font4.setFontHeightInPoints((short) 10);
        font4.setFontName("Arial");
        font4.setItalic(true);
        font4.setBoldweight ( Font.BOLDWEIGHT_BOLD );
        style4.setFont(font4);
        return style4;
    }
    

    //funcion para autoajustar las columnas segun su contenido
    public void autoSizeColumns(Workbook workbook, int noFila) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(noFila);
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                }
            }
        }
    }
    
}
