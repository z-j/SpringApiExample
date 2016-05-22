package service;

import java.util.List;

import bean.Transaction;

public interface DataLayer {

	public void writeData(Transaction t) throws Exception;
	public List<Transaction> readData() throws Exception;
	
	
}
