package Utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Constants {

	static Logger log = Logger.getLogger(Constants.class);
	
	public static final String ERROR_MESSAGE = "error message";
	public static final String MSG_TRANSACTION_SAVED = "transaction saved";
	public static final String ERROR_MESSAGE_WRONG_FORMAT = "Wrong format. Please enter 'csv' or 'excel'.";
	public static final String ERROR_MESSAGE_INVALID_TRANSACTION = "invalid transaction object";
	
	public static String excel_file_name = null;
	public static String csv_file_name = null;
	
	
	public static void setFileNames(){
		Properties prop = new Properties();

		//using try with resource, no need to close resource
		InputStream input = null;
		try {

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			input = classLoader.getResourceAsStream("config.properties");
			
			//input = new FileInputStream("")
			prop.load(input);
			csv_file_name = prop.getProperty("csv_path");
			excel_file_name = prop.getProperty("excel_path");
			log.info(csv_file_name +", "+ excel_file_name);

		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			//since IO Exception occured while reading the files, setting some default location
			csv_file_name = "C:\\temp\\transactions.csv";
			csv_file_name = "C:\\temp\\transactions.xls";
		}
	}
	
}
