package com.powernode.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;

public class HSSFUtils {
    private HSSFUtils(){};
    public static String getCellValueForStr(HSSFCell cell){
        if(cell.getCellType()== CellType.STRING){
            return cell.getStringCellValue();
        }else if(cell.getCellType()==CellType.NUMERIC){
            return String.valueOf(cell.getNumericCellValue());
        }else if(cell.getCellType()==CellType.BOOLEAN){
            return String.valueOf(cell.getBooleanCellValue());
        }else if(cell.getCellType()==CellType.FORMULA){
            return String.valueOf(cell.getCellFormula());
        }else{
            return "null";
        }
    }
}
