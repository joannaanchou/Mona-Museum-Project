package util;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.xddf.usermodel.chart.*;


public class CreateExcel {
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFSheet sheet;

    public void create(String outputFile, String sheetName, String[] titleName) {
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            sheet = workbook.createSheet(sheetName);
            XSSFRow row = sheet.createRow(0);
            for (int i = 0; i < titleName.length; i++) {
                row.createCell(i).setCellValue(titleName[i]);
            }
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // 匯出：會員單筆訂單 excel
    public void exportReceipt(String filePath, String sheetName, String text) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet(sheetName == null ? "Sheet1" : sheetName);

            // 欄寬：設寬一點
            sheet.setColumnWidth(0, 70 * 256);
       
            sheet.setDisplayGridlines(false);

           
            // 一般內容樣式
            XSSFCellStyle normal = wb.createCellStyle();
            normal.setWrapText(true);
            normal.setVerticalAlignment(VerticalAlignment.TOP);
            Font normalFont = wb.createFont();
            normalFont.setFontHeightInPoints((short)12);
            normalFont.setFontName("PingFang TC");
            normal.setFont(normalFont);

            // 段落標題樣式
            XSSFCellStyle section = wb.createCellStyle();
            section.cloneStyleFrom(normal);
            Font sectionFont = wb.createFont();
            sectionFont.setBold(true);
            sectionFont.setFontHeightInPoints((short)12);
            sectionFont.setFontName("PingFang TC");
            section.setFont(sectionFont);
            section.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            section.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setThinBorder(section);

            // 分隔線（=====）
            XSSFCellStyle separator = wb.createCellStyle();
            separator.cloneStyleFrom(normal);
            separator.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            separator.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setThinBorder(separator);

            // 一般行加薄邊框
            setThinBorder(normal);

            // 逐列輸出內容
           
            String safe = (text == null) ? "" : text;
            safe = safe.replace("\r\n", "\n").replace("\r", "\n");

            //-1 代表保留最後空行
            String[] lines = safe.split("\n", -1);

            // 逐行寫入 Excel
            for (int r = 0; r < lines.length; r++) {
                Row row = sheet.createRow(r);
                Cell cell = row.createCell(0);
                String line = lines[r];
                cell.setCellValue(line);

                // 判斷樣式
                String trimmed = line.trim();
                boolean isSectionTitle = trimmed.startsWith("【") && trimmed.endsWith("】");
                boolean isSeparator    = trimmed.startsWith("=") && trimmed.length() >= 5; 

                if (isSectionTitle) {
                    cell.setCellStyle(section);   // 例如【會員資料】
                } else if (isSeparator) {
                    cell.setCellStyle(separator); // 例如 ========
                } else {
                    cell.setCellStyle(normal);    // 一般內容
                }
            }

            // 輸出檔案
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                wb.write(fos);
            }
        }
    }

    //幫 cell style 加上薄邊框 
    private static void setThinBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
    
    
    // 匯出：所有訂單（findallporder jtable）
    public void exportTable(String filePath, String sheetName, String[] headers, java.util.List<Object[]> rows)
            throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet(sheetName == null ? "Sheet1" : sheetName);

            // 樣式：表頭
            XSSFCellStyle header = wb.createCellStyle();
            header.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            header.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(header);
            Font hfont = wb.createFont();
            hfont.setBold(true);
            hfont.setFontHeightInPoints((short)12);
            hfont.setFontName("PingFang TC");
            header.setFont(hfont);

            // 樣式：一般儲存格
            XSSFCellStyle body = wb.createCellStyle();
            body.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(body);
            Font bfont = wb.createFont();
            bfont.setFontHeightInPoints((short)11);
            bfont.setFontName("PingFang TC");
            body.setFont(bfont);

            int r = 0;

            //表頭
            XSSFRow headRow = sheet.createRow(r++);
            for (int c = 0; c < headers.length; c++) {
                Cell cell = headRow.createCell(c);
                cell.setCellValue(headers[c]);
                cell.setCellStyle(header);
            }

            //資料列
            for (Object[] row : rows) {
                XSSFRow xrow = sheet.createRow(r++);
                for (int c = 0; c < row.length; c++) {
                    Cell cell = xrow.createCell(c);
                    Object v = (c < row.length) ? row[c] : null;
                    if (v == null) {
                        cell.setCellValue("");
                    } else if (v instanceof Number) {
                        cell.setCellValue(((Number) v).doubleValue());
                    } else {
                        cell.setCellValue(String.valueOf(v));
                    }
                    cell.setCellStyle(body);
                }
            }

            // 自動欄寬
            for (int c = 0; c < headers.length; c++) {
                sheet.autoSizeColumn(c);
                // 用途：讓中文字不會太窄
                int w = sheet.getColumnWidth(c);
                sheet.setColumnWidth(c, Math.min(Math.max(w + 512, 3000), 12000));
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                wb.write(fos);
            }
        }
    }
    

    // 匯出：票種銷售報表
    public void exportTicketTypeSalesReport(Object[][] data, String fileName, String sheetName) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(sheetName == null ? "票種銷售報表" : sheetName);

            // 樣式：表頭
            XSSFCellStyle header = workbook.createCellStyle();
            header.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            header.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(header);
            Font hfont = workbook.createFont();
            hfont.setBold(true);
            hfont.setFontHeightInPoints((short)12);
            hfont.setFontName("PingFang TC");
            header.setFont(hfont);

            // 樣式：一般儲存格
            XSSFCellStyle body = workbook.createCellStyle();
            body.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(body);
            Font bfont = workbook.createFont();
            bfont.setFontHeightInPoints((short)11);
            bfont.setFontName("PingFang TC");
            body.setFont(bfont);

            // 1. 欄位標題（A1:B1）
            String[] headers = {"票種", "銷量"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(header);
            }

            // 2. 寫入資料列（從第2列開始），並累計加總
            
            double volumeSum = 0; //銷量加總
            
            
            for (int i = 0; i < data.length; i++) {
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue((String) data[i][0]); // 票種
                double v = ((Number) data[i][1]).doubleValue();       // 銷量 
                row.createCell(1).setCellValue(v);
              

                for (int c = 0; c < 2; c++) {
                    row.getCell(c).setCellStyle(body);
                }
                volumeSum += v;
                
            }
            
            // 總計列（放在所有資料列後的下一列）
            int totalRowIndex = data.length + 1; // header=0, data=1..n, total=n+1
            XSSFRow totalRow = sheet.createRow(totalRowIndex);
            totalRow.createCell(0).setCellValue("總計");
            totalRow.createCell(1).setCellValue(volumeSum);

            for (int c = 0; c < 2; c++) {
                totalRow.getCell(c).setCellStyle(body);
            }

            
            
            // 欄寬
            sheet.setColumnWidth(0, 12 * 256);
            sheet.setColumnWidth(1, 10 * 256);
          

            // 3. 建圖
            int n = data.length;                 // 例如 3 筆：全票/學生票/愛心票
            int firstRow = 1;                    // 資料從第2列開始（index 1）
            int lastRow  = n;                    // 到第 n+1 列（index n）

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            // anchor: 左上 (col,row) 到 右下 (col,row)——可自行調整尺寸
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, n + 3, 8, n + 25);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("票種銷量");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("票種");
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("銷量");
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            // 資料範圍
            XDDFDataSource<String> category = XDDFDataSourcesFactory.fromStringCellRange(
                    sheet, new CellRangeAddress(firstRow, lastRow, 0, 0)); // A2:A(1+n)
            XDDFNumericalDataSource<Double> volume = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(firstRow, lastRow, 1, 1)); // B2:B(1+n)
            

            // 柱狀圖（銷量）
            XDDFBarChartData bar = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            XDDFBarChartData.Series barSeries = (XDDFBarChartData.Series) bar.addSeries(category, volume);
            barSeries.setTitle("銷量", null);
            bar.setBarDirection(BarDirection.COL);
            chart.plot(bar);


            // 寫檔
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
            }
        }
    }
    
    
    // 匯出：票期銷售報表
    public void exportTicketPeriodSalesReport(Object[][] data, String fileName, String sheetName) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(sheetName == null ? "票期銷售報表" : sheetName);

            // 樣式：表頭
            XSSFCellStyle header = workbook.createCellStyle();
            header.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            header.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(header);
            Font hfont = workbook.createFont();
            hfont.setBold(true);
            hfont.setFontHeightInPoints((short)12);
            hfont.setFontName("PingFang TC");
            header.setFont(hfont);

            // 樣式：一般儲存格
            XSSFCellStyle body = workbook.createCellStyle();
            body.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(body);
            Font bfont = workbook.createFont();
            bfont.setFontHeightInPoints((short)11);
            bfont.setFontName("PingFang TC");
            body.setFont(bfont);

            // 1. 欄位標題（A1:B1）
            String[] headers = {"票期", "銷量"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(header);
            }

            // 2. 寫入資料列（從第2列開始），並累計加總
            double volumeSum = 0; // 銷量加總
         

            for (int i = 0; i < data.length; i++) {
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue((String) data[i][0]); // 票期
                double v = ((Number) data[i][1]).doubleValue();       // 銷量   
                row.createCell(1).setCellValue(v);
               

                for (int c = 0; c < 2; c++) {
                    row.getCell(c).setCellStyle(body);
                }
                volumeSum += v;
            }

            // 2-1. 總計列（放在所有資料列後的下一列）
            int totalRowIndex = data.length + 1; // header=0, data=1..n, total=n+1
            XSSFRow totalRow = sheet.createRow(totalRowIndex);
            totalRow.createCell(0).setCellValue("總計");
            totalRow.createCell(1).setCellValue(volumeSum);
            for (int c = 0; c < 2; c++) {
                totalRow.getCell(c).setCellStyle(body);
            }

            // 欄寬
            sheet.setColumnWidth(0, 12 * 256);
            sheet.setColumnWidth(1, 10 * 256);
   

            // 建圖
            int n = data.length;   // 例如 3 筆：單日票/季票/年票
            int firstRow = 1;      // 資料從第2列開始（index 1）
            int lastRow  = n;      // 到第 n+1 列（index n）

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            // anchor: 左上 (col,row) 到 右下 (col,row)——可自行調整尺寸
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, totalRowIndex + 2, 8, totalRowIndex + 24);
            XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("票期銷量");
            chart.setTitleOverlay(false);

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("票期");
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("銷量");
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            // 資料範圍
            XDDFDataSource<String> category = XDDFDataSourcesFactory.fromStringCellRange(
                    sheet, new CellRangeAddress(firstRow, lastRow, 0, 0)); // A2:A(1+n)
            XDDFNumericalDataSource<Double> volume = XDDFDataSourcesFactory.fromNumericCellRange(
                    sheet, new CellRangeAddress(firstRow, lastRow, 1, 1)); // B2:B(1+n)

            // 柱狀圖（銷量）
            XDDFBarChartData bar = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            XDDFBarChartData.Series barSeries = (XDDFBarChartData.Series) bar.addSeries(category, volume);
            barSeries.setTitle("銷量", null);
            bar.setBarDirection(BarDirection.COL);
            chart.plot(bar);

            // 寫檔
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
            }
        }
    }
    
    // 匯出：票種×票期逐列銷售報表（含總計）
    public void exportTicketTypePeriodSalesReport(Object[][] data, String fileName, String sheetName) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(sheetName == null ? "票種x票期 銷售報表" : sheetName);

            // 表頭
            XSSFCellStyle header = workbook.createCellStyle();
            header.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            header.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(header);
            Font hfont = workbook.createFont();
            hfont.setBold(true);
            hfont.setFontHeightInPoints((short)12);
            hfont.setFontName("PingFang TC");
            header.setFont(hfont);

            // 一般儲存格樣式
            XSSFCellStyle body = workbook.createCellStyle();
            body.setVerticalAlignment(VerticalAlignment.CENTER);
            setThinBorder(body);
            Font bfont = workbook.createFont();
            bfont.setFontHeightInPoints((short)11);
            bfont.setFontName("PingFang TC");
            body.setFont(bfont);

            // 總計列
            XSSFCellStyle total = workbook.createCellStyle();
            total.cloneStyleFrom(body);
            Font tfont = workbook.createFont();
            tfont.setBold(true);
            tfont.setFontHeightInPoints((short)11);
            tfont.setFontName("PingFang TC");
            total.setFont(tfont);

            // 標題列
            String[] headers = {"票種", "票期", "銷量", "銷售總額"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(header);
            }

            // 拿資料 + 累計合計
            double volumeSum = 0; // 銷量加總
            double amountSum = 0; // 銷售總額加總

            for (int i = 0; i < data.length; i++) {
                XSSFRow row = sheet.createRow(i + 1);
                // A:票種、B:票期
                row.createCell(0).setCellValue((String) data[i][0]);
                row.createCell(1).setCellValue((String) data[i][1]);

                // C:銷量、D:銷售總額（轉成 double 寫入）
                double v = ((Number) data[i][2]).doubleValue();
                double a = ((Number) data[i][3]).doubleValue();
                row.createCell(2).setCellValue(v);
                row.createCell(3).setCellValue(a);

                for (int c = 0; c < 4; c++) {
                    row.getCell(c).setCellStyle(body);
                }
                volumeSum += v;
                amountSum += a;
            }

            // 總計列（放在所有資料列後下一列）
            int totalRowIndex = data.length + 1; // header=0, data=1..n, total=n+1
            XSSFRow totalRow = sheet.createRow(totalRowIndex);
            totalRow.createCell(0).setCellValue("總計");     // 第一欄顯示「總計」
            totalRow.createCell(1).setCellValue("");         // 票期欄位留白
            totalRow.createCell(2).setCellValue(volumeSum);  // 銷量合計（
            totalRow.createCell(3).setCellValue(amountSum);  // 銷售總額合計（必填）

            for (int c = 0; c < 4; c++) {
                totalRow.getCell(c).setCellStyle(total);
            }

            // 4) 欄寬
            sheet.setColumnWidth(0, 12 * 256); // 票種
            sheet.setColumnWidth(1, 12 * 256); // 票期
            sheet.setColumnWidth(2, 10 * 256); // 銷量
            sheet.setColumnWidth(3, 14 * 256); // 銷售總額

            // 5) 輸出檔案
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
            }
        }
    }




}
  
