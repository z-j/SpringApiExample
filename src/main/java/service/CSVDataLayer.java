package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import Utility.Constants;
import bean.Transaction;

public class CSVDataLayer implements DataLayer {

	Logger log = Logger.getLogger(CSVDataLayer.class);
	
	
	private void createFile(String filePath) throws Exception {
		
		File f = new File(filePath);
		if(f.exists() && !f.isDirectory()) { 
		   return; 
		} else {
			log.info("file does not exists, create one");
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath), true))) {

				StringBuilder sb = new StringBuilder();

				sb.append("Id");
				sb.append(',');
				sb.append("Number of Items");
				sb.append(',');
				sb.append("Total Price");
				sb.append(System.getProperty("line.separator"));
				
				bw.write(sb.toString());
			
			} catch(Exception e) {
				log.error(e.getMessage(), e);
				throw new Exception("Some exception occurred while writing to file. Please try later.");
			}
		}
	}
	
	@Override
	public void writeData(Transaction t) throws Exception {

		//No need to close Buffered Writer as using try-with-resource feature
		
		if(Constants.csv_file_name == null) {
			Constants.setFileNames();
		}
		
		this.createFile(Constants.csv_file_name);
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Constants.csv_file_name), true))) {

			StringBuilder sb = new StringBuilder();

			sb.append(t.getId());
			sb.append(',');
			sb.append(t.getNumberOfItems());
			sb.append(',');
			sb.append(t.getPrice());
			sb.append(System.getProperty("line.separator"));
			
			bw.write(sb.toString());
		
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception("Some exception occurred while writing to file. Please try later.");
		}
	}

	@Override
	public List<Transaction> readData() throws Exception{
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		if(Constants.csv_file_name == null) {
			Constants.setFileNames();
		}
		
		this.createFile(Constants.csv_file_name);
		
		try(BufferedReader br = new BufferedReader(new FileReader(new File(Constants.csv_file_name)))) {
			
			String record = "";
			
			while ((record = br.readLine()) != null) {
				Transaction trans = this.getTransactionObject(record);
				if(trans!= null) {
					transactions.add(trans);
				}
			}
			
			log.info("returning transactions:"+transactions.size());
			log.info("returning transactions:"+transactions);
			return transactions;
			
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception("Some exception occurred while writing to file. Please try later.");
		}
		
	}
	
	public Transaction getTransactionObject(String line) {
		
		try{
			String [] tokens = line.split(",");
			return new Transaction(Integer.valueOf(tokens[0].trim()), Integer.valueOf(tokens[1].trim())
					, Integer.valueOf(tokens[2].trim()));
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}



}
