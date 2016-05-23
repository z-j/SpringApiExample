package service;

import java.util.List;

import org.apache.log4j.Logger;

import Utility.Constants;
import bean.Transaction;

public class Service {

	Logger log = Logger.getLogger(Service.class);

	public void saveTransaction(Transaction transaction, String format) throws Exception {

		DataLayer dataLayer = this.getInstance(format);
		if(dataLayer != null) {
			if(this.isValid(transaction)) {
				dataLayer.writeData(transaction);
			} else {
				throw new Exception(Constants.ERROR_MESSAGE_INVALID_TRANSACTION);
			}
		} else {
			throw new Exception(Constants.ERROR_MESSAGE_WRONG_FORMAT);
		}

	}

	public List<Transaction> getTransactions(String format) throws Exception {

		DataLayer dataLayer = this.getInstance(format);
		if(dataLayer != null) {
			return dataLayer.readData();
		} else {
			throw new Exception(Constants.ERROR_MESSAGE_WRONG_FORMAT);
		}

	}

	public DataLayer getInstance(String format) {

		if("csv".equalsIgnoreCase(format)) {
			return new CSVDataLayer();
		} else if ("excel".equalsIgnoreCase(format)) {
			return new ExcelDataLayer();
		}

		return null;
	}

	private boolean isValid(Transaction trans) {
		if (trans != null 
				&& trans.getId() != 0
				&& trans.getNumberOfItems() != 0
				&& trans.getPrice() != 0) {
			return true;
		} else {
			return false;
		}
	}
}
