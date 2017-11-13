package com.czy.seed.mvc.charge.config.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/7/17.
 */
public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static void downloadExcelPerson(List<Map<String, Object>> data, OutputStream out) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("assist product person sell detail");
        //设置单元格宽度
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();

        HSSFCellStyle styleMoney = wb.createCellStyle();
        HSSFDataFormat df = wb.createDataFormat();//数据格式
        styleMoney.setDataFormat(df.getFormat("#,#0.00"));//千分位+两位小数的金额

        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setDataFormat((short) 3);



        HSSFCell cell = row.createCell(0);
        cell.setCellValue("order No.");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("flight No.");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("name");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("ID");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("Product No.");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("Amount");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("payment type");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("Staff ID.");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("date");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("status");
        cell.setCellStyle(style);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Map<String, Object> map = null;
        for (int i = 0; i < data.size(); i++) {
            map = data.get(i);
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue((String) map.get("orderNum"));
            row.createCell(1).setCellValue((String) map.get("fi"));
            row.createCell(2).setCellValue((String) map.get("name"));
            row.createCell(3).setCellValue((String) map.get("licenseNo"));
            row.createCell(4).setCellValue(((Long) map.get("productNum")).doubleValue());
            double tMoney = ((BigDecimal) map.get("totalMoney")).doubleValue();
            cell = row.createCell(5);
            cell.setCellStyle(styleMoney);
            cell.setCellValue(tMoney);

            String paymentType = "";
            if ("1".equals((String) map.get("paymentType"))) {
                paymentType = "CASH";
            } else if ("2".equals((String) map.get("paymentType"))) {
                paymentType = "POS";
            } else if ("3".equals((String) map.get("paymentType"))) {
                paymentType = "POS(G)";
            }
            row.createCell(6).setCellValue(paymentType);

            row.createCell(7).setCellValue((String) map.get("createdBy"));
            row.createCell(8).setCellValue(sdf.format((Date) map.get("createdDt")));

            String status = "";
            if ("Y".equals(map.get("type"))) {
                status = "Y";
            } else if ("N".equals(map.get("type"))) {
                status = "N";
            } else if ("Refund".equals(map.get("type"))) {
                status = "Refund";
            } else if ("C".equals(map.get("type"))) {
                status = "C";
            }
            row.createCell(9).setCellValue(status);
        }

        try {
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            logger.error("--export excel error!");

        } finally {
            try {
                out.close();
            } catch (IOException e1) {
                logger.error("--shutdown outputStream error!");
            }
            try {
                wb.close();
            } catch (IOException e1) {
                logger.error("--shutdown outputStream error!");
            }

        }

    }

    public static void downloadExcelCollect(List<Map<String, Object>> data, OutputStream out, boolean showCreatedby) {

        int a = 0, b = 1, c = 2, d = 3;
        //按产品分组时不展示工号
        if (!showCreatedby) {
            a = -1;
            b = 0;
            c = 1;
            d = 2;
        }

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("assist product sell collect");
        //设置单元格宽度
        if (a == 0) {
            sheet.setColumnWidth(a, 20 * 256);
        }
        sheet.setColumnWidth(b, 20 * 256);
        sheet.setColumnWidth(c, 20 * 256);
        sheet.setColumnWidth(d, 20 * 256);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle styleMoney = wb.createCellStyle();
        HSSFDataFormat df = wb.createDataFormat();//数据格式
        styleMoney.setDataFormat(df.getFormat("#,#0.00"));//千分位+两位小数的金额

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setDataFormat((short)3);

        HSSFCell cell = null;

        if (a == 0) {
            cell = row.createCell(a);
            cell.setCellValue("staff id.");
            cell.setCellStyle(style);
        }

        cell = row.createCell(b);
        cell.setCellValue("product type");
        cell.setCellStyle(style);
        cell = row.createCell(c);
        cell.setCellValue("product No.");
        cell.setCellStyle(style);
        cell = row.createCell(d);
        cell.setCellValue("Amount");
        cell.setCellStyle(style);

        int num = 0;
        double totalMoney = 0.0;
        Map<String, Object> map = null;
        for (int i = 0; i < data.size(); i++) {
            map = data.get(i);
            row = sheet.createRow(i + 1);

            if (a == 0) {
                row.createCell(a).setCellValue((String) map.get("createBy"));
            }

            row.createCell(b).setCellValue((String) map.get("productType"));

            int productNum = ((Long) map.get("productNum")).intValue();
            row.createCell(c).setCellValue(productNum);
            double groupMoney = ((BigDecimal) map.get("groupMoney")).doubleValue();
            cell = row.createCell(d);
            cell.setCellStyle(styleMoney);
            cell.setCellValue(groupMoney);
            num += productNum;
            totalMoney += groupMoney;
        }

        row = sheet.createRow(data.size() + 1);
        row.createCell(b).setCellValue("total");
        row.createCell(c).setCellValue(num);
        cell = row.createCell(d);
        cell.setCellStyle(styleMoney);
        cell.setCellValue(totalMoney);

        try {
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            logger.error("--export excel error!");

        } finally {
            try {
                out.close();
            } catch (IOException e1) {
                logger.error("--shutdown outputStream error!");
            }
            try {
                wb.close();
            } catch (IOException e1) {
                logger.error("--shutdown outputStream error!");
            }
        }
    }

    //辅收产品航班销售明细
    public static void downloadExcelFlight(Map<String, Object> result, OutputStream out) {

        List<Map<String, Object>> flightInfo = (List<Map<String, Object>>) result.get("flightInfo");
        List<Map<String, String>> flightSubtotal = (List<Map<String, String>>) result.get("flightSubtotal");
        List<Map<String, String>> flightTotal = (List<Map<String, String>>) result.get("flightTotal");

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("assist product flight sell detail");
        //设置单元格宽度
        sheet.setColumnWidth(0, 40 * 256);
        sheet.setColumnWidth(1, 40 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 400);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();

        HSSFCellStyle styleMoney = wb.createCellStyle();
        HSSFDataFormat df = wb.createDataFormat();//数据格式
        styleMoney.setDataFormat(df.getFormat("#,#0.00"));//千分位+两位小数的金额
        styleMoney.setVerticalAlignment(VerticalAlignment.CENTER);

//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setDataFormat((short)3);
        HSSFCellStyle preStyle = wb.createCellStyle();
        preStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("order No.");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("Product type+Information");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("order status");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("Amount");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("payment type");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("total");
        cell.setCellStyle(style);

        if (flightInfo != null && flightInfo.size() != 0) {
            CellRangeAddress craType = null;
            CellRangeAddress craMoney = null;

            for (int i = 0; i < flightInfo.size(); i++) {
                Map<String, Object> info = flightInfo.get(i);

                Integer rowNum = (Integer) info.get("rowNum");
                if (rowNum != null && rowNum > 1) {
                    craType = new CellRangeAddress(i + 1, i + rowNum, 4, 4);
                    sheet.addMergedRegion(craType);//合并付款类型的单元格

                    craMoney = new CellRangeAddress(i + 1, i + rowNum, 5, 5);
                    sheet.addMergedRegion(craMoney);//合并金额的单元格

                }

                row = sheet.createRow(i + 1);
                row.setHeight((short) 400);

                cell = row.createCell(0);
                cell.setCellValue((String) info.get("orderNum"));
                cell.setCellStyle(preStyle);

                cell = row.createCell(1);
                cell.setCellValue((String) info.get("typeDetail"));
                cell.setCellStyle(preStyle);

                String status = "";
                if ("Y".equals(info.get("status"))) {
                    status = "Y";
                } else if ("N".equals(info.get("status"))) {
                    status = "N";
                } else if ("Refund".equals(info.get("status"))) {
                    status = "Refund";
                } else if ("C".equals(info.get("status"))) {
                    status = "C";
                }
                cell = row.createCell(2);
                cell.setCellValue(status);
                cell.setCellStyle(style);

                cell = row.createCell(3);
                cell.setCellValue(((BigDecimal) info.get("money")).doubleValue());
                cell.setCellStyle(styleMoney);


                String paymentType = "";
                if ("1".equals((String) info.get("paymentType"))) {
                    paymentType = "CASH";
                } else if ("2".equals((String) info.get("paymentType"))) {
                    paymentType = "POS";
                } else if ("3".equals((String) info.get("paymentType"))) {
                    paymentType = "POS(G)";
                }
                if (rowNum != null && rowNum == 1) {
                    cell = row.createCell(4);
                    cell.setCellValue(paymentType);
                    cell.setCellStyle(style);

                    cell = row.createCell(5);
                    cell.setCellValue(((BigDecimal) info.get("totalMoney")).doubleValue());
                    cell.setCellStyle(styleMoney);

                } else if (rowNum != null && rowNum > 1) {
                    cell = row.createCell(4);
                    cell.setCellValue(paymentType);
                    cell.setCellStyle(style);

                    cell = row.createCell(5);
                    cell.setCellValue(((BigDecimal) info.get("totalMoney")).doubleValue());
                    cell.setCellStyle(styleMoney);
                }
            }


            //小计
            int fsize = flightInfo.size() + 1;
            for (int i = 0; i < flightSubtotal.size(); i++) {
                Map<String, String> map = flightSubtotal.get(i);

                if (i == 0) {
                    row = sheet.createRow(fsize);
                    row.setHeight((short) 400);

                    cell = row.createCell(0);
                    cell.setCellValue("subtotal");
                    cell.setCellStyle(preStyle);

                } else {
                    row = sheet.createRow(fsize + i);
                    row.setHeight((short) 400);
                }

                cell = row.createCell(1);
                cell.setCellValue(map.get("productType"));
                cell.setCellStyle(preStyle);

                String paymentType = "";
                if ("1".equals(map.get("paymentType"))) {
                    paymentType = "CASH";
                } else if ("2".equals(map.get("paymentType"))) {
                    paymentType = "POS";
                } else if ("3".equals(map.get("paymentType"))) {
                    paymentType = "POS(G)";
                }
                cell = row.createCell(4);
                cell.setCellValue(paymentType);
                cell.setCellStyle(style);

                cell = row.createCell(5);
                cell.setCellValue(Long.valueOf(map.get("totalMoney")));
                cell.setCellStyle(styleMoney);
            }

            //合计
            int preSize = fsize + flightSubtotal.size();
            for (int i = 0; i < flightTotal.size(); i++) {
                Map<String, String> map = flightTotal.get(i);

                if (i == 0) {
                    row = sheet.createRow(preSize);
                    row.setHeight((short) 400);

                    cell = row.createCell(0);
                    cell.setCellValue("total");
                    cell.setCellStyle(preStyle);

                } else {
                    row = sheet.createRow(preSize + i);
                    row.setHeight((short) 400);
                }

                cell = row.createCell(1);
                cell.setCellValue(map.get("productType"));
                cell.setCellStyle(preStyle);

                cell = row.createCell(5);
                cell.setCellValue(Long.valueOf(map.get("totalMoney")));
                cell.setCellStyle(styleMoney);

            }

        }

        try {
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            logger.error("--export excel error!");

        } finally {
            try {
                out.close();
            } catch (IOException e1) {
                logger.error("--shutdown outputStream error!");
            }
            try {
                wb.close();
            } catch (IOException e1) {
                logger.error("--shutdown outputStream error!");
            }
        }

    }


//    public static String formatMoney (double money) {
//
//        NumberFormat numberFormat =NumberFormat.getNumberInstance();
//        numberFormat.setMaximumFractionDigits(2);
//        numberFormat.setMinimumFractionDigits(2);
//
//        return numberFormat.format(money);
//    }

}
