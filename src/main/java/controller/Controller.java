package controller;


import java.util.ArrayList;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Utility.Constants;
import bean.Transaction;
import service.Service;  

@RestController  
public class Controller {

	Logger log = Logger.getLogger(Controller.class.getName());
	Service service = new Service();

	@RequestMapping(value = "/transactions/{format}", method = RequestMethod.POST, headers = "Accept=application/json")  
	public ResponseEntity<?> createTransaction(@PathVariable String format, @RequestBody Transaction transaction) {

		log.info("format:"+format);
		log.info(transaction.toString());
		
		try {
			service.saveTransaction(transaction, format);
			return new ResponseEntity<>(Constants.MSG_TRANSACTION_SAVED, HttpStatus.CREATED);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			//with assumption that all checked exceptions are caught and handled before re-throwing, with custom error message
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}  

	@RequestMapping(value = "/transactions/{format}", method = RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<?> getTransactions(@PathVariable String format)  
	{  
		log.info("format:"+format);
		List<Transaction> transactions = new ArrayList<Transaction>();  

		try {
			transactions=service.getTransactions(format);
			return new ResponseEntity<>(transactions, HttpStatus.OK);
		} catch(Exception e) {
			//with assumption that all checked exceptions are caught and handled before re-throwing
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}  

}  

