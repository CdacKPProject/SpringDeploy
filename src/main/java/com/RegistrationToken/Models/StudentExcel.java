package com.RegistrationToken.Models;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class StudentExcel {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<RegisterdStudent> list;
	
	public StudentExcel(List<RegisterdStudent> list) {
		this.list = list;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("StudentsReg");
	}
	private void writeHeaderData() {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		
		Row row = sheet.createRow(0);
		Cell cell =row.createCell(0);
		cell.setCellValue("PRN Number");
		cell.setCellStyle(style);
		
		cell =row.createCell(1);
		cell.setCellValue("Student Name");
		cell.setCellStyle(style);
		
		cell =row.createCell(2);
		cell.setCellValue("Course");
		cell.setCellStyle(style);
		
		
	}
	private void writeStudentData() {
		int r =1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		for(RegisterdStudent s:list) {
			
			Row row = sheet.createRow(r++);
			Cell cell =row.createCell(0);
			cell.setCellValue(s.getPrnNumber());
			cell.setCellStyle(style);
			
			cell =row.createCell(1);
			cell.setCellValue(s.getName());
			cell.setCellStyle(style);
			
			cell =row.createCell(2);
			cell.setCellValue(s.getCourse());
			cell.setCellStyle(style);
			
		}
		for(int i=0;i<list.size();i++) {
			sheet.autoSizeColumn(i);
		}
		
	}
	public void exportDataExcel(HttpServletResponse res) throws IOException {
		writeHeaderData();
		writeStudentData();
		ServletOutputStream sos = res.getOutputStream();
		workbook.write(sos);
		workbook.close();
		sos.close();
	}
}
