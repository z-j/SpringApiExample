package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import Utility.Constants;
import bean.Transaction;

public class ExcelDataLayer implements DataLayer {

	Logger log = Logger.getLogger(ExcelDataLayer.class);

	private void createFile(String filePath) throws Exception {

		File f = new File(filePath);
		if(f.exists() && !f.isDirectory()) {
			log.info("file already exists");
			return; 
		} else {
			log.info("file does not exists, create one");

			try {
				Workbook wb = new HSSFWorkbook();
				Sheet sheet = wb.createSheet();
				Row row = sheet.getRow(sheet.getLastRowNum()+1);

				if(row == null) {
					log.info("Last Row Number: "+sheet.getLastRowNum());
					row = sheet.createRow(sheet.getLastRowNum());
				}
				Cell cell1 = row.getCell(0);
				Cell cell2 = row.getCell(1);
				Cell cell3 = row.getCell(2);

				if (cell1 == null)
					cell1 = row.createCell(0);
				if (cell2 == null)
					cell2 = row.createCell(1);
				if (cell3 == null)
					cell3 = row.createCell(2);

				cell1.setCellType(Cell.CELL_TYPE_STRING);
				cell1.setCellValue("Id");

				cell1.setCellType(Cell.CELL_TYPE_STRING);
				cell2.setCellValue("Number of Items");

				cell3.setCellType(Cell.CELL_TYPE_STRING);
				cell3.setCellValue("Total Price");

				log.info(cell1.getStringCellValue()+","+cell2.getStringCellValue());
				// Write the output to a file
				FileOutputStream fileOut = new FileOutputStream(filePath);
				wb.write(fileOut);
				fileOut.close();

			} catch(Exception e) {
				log.error(e.getMessage(), e);
				throw new Exception("Some exception occurred while writing to file. Please try later.");
			}
		}
	}

	@Override
	public void writeData(Transaction t) throws Exception {
		// TODO Auto-generated method stub

		if(Constants.excel_file_name == null) {
			Constants.setFileNames();
		}
		
		this.createFile(Constants.excel_file_name);
		
		try {
			Workbook wb = WorkbookFactory.create(new FileInputStream(Constants.excel_file_name));
			Sheet sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(sheet.getLastRowNum()+1);

			if(row == null) {
				log.info("Last Row Number: "+sheet.getLastRowNum());
				row = sheet.createRow(sheet.getLastRowNum()+1);
			}
			Cell cell1 = row.getCell(0);
			Cell cell2 = row.getCell(1);
			Cell cell3 = row.getCell(2);

			if (cell1 == null)
				cell1 = row.createCell(0);
			if (cell2 == null)
				cell2 = row.createCell(1);
			if (cell3 == null)
				cell3 = row.createCell(2);

			cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell1.setCellValue(t.getId());

			cell2.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell2.setCellValue(t.getNumberOfItems());
			
			cell3.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell3.setCellValue(t.getPrice());

			log.info(t.getNumberOfItems()+","+t.getPrice());

			//log.info(cell1.getStringCellValue()+","+cell2.getStringCellValue()+", "+cell3.getStringCellValue());
			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(Constants.excel_file_name);
			wb.write(fileOut);
			fileOut.close();

		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<Transaction> readData()  throws Exception {

		if(Constants.excel_file_name == null) {
			Constants.setFileNames();
		}
		
		this.createFile(Constants.excel_file_name);
		
		InputStream inp = new FileInputStream(Constants.excel_file_name);

		List<Transaction> transactions = new ArrayList<Transaction>();

		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(0);

		Iterator<Row> rowIterator = sheet.iterator();

		while(rowIterator.hasNext()) {
			Row r = rowIterator.next();
			Cell cell0 = r.getCell(0);
			Cell cell1 = r.getCell(1);
			Cell cell2 = r.getCell(2);

			try {
				transactions.add(new Transaction((int)(cell0.getNumericCellValue()), 
						(int)(cell1.getNumericCellValue()), 
						(int)cell2.getNumericCellValue()));
			} catch(Exception e) {
				log.error("ignore here");
				log.error(e.getMessage(), e);
			}
		}

		inp.close();

		return transactions;
	}


}
