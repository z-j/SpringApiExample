package testPackage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import Utility.Constants;
import bean.Transaction;
import controller.Controller;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springrest-servlet.xml" })
public class ControllerIntegrationTest {

	Logger log = Logger.getLogger(ControllerIntegrationTest.class);

	@InjectMocks
	private Controller controller;

	private MockMvc mockMvc;

	@Test
	public void testTransactionsExcel() throws Exception {

		Transaction t1 = new Transaction(1, 10, 100);
		Transaction t2 = new Transaction(2, 20, 200);

		mockMvc.perform(post("/transactions/{format}", "excel")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(t1))
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(content().string(Constants.MSG_TRANSACTION_SAVED));

		mockMvc.perform(post("/transactions/{format}", "excel")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(t2))
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(content().string(Constants.MSG_TRANSACTION_SAVED));


		MvcResult res = mockMvc.perform(get("/transactions/{format}", "excel"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)).andReturn();

		log.info(res.getResponse().getContentAsString());

		ObjectMapper mapper = new ObjectMapper();
		Transaction[] transactions = mapper.readValue(res.getResponse().getContentAsString(), Transaction[].class);

		log.info(transactions.length);
		for(Transaction t : transactions) {
			log.info(t.toString());
		}

		Assert.assertEquals(2, transactions.length);
		Assert.assertTrue(isTransactionAdded(t1, transactions));
		Assert.assertTrue(isTransactionAdded(t2, transactions));

	}

	@Test
	public void testTransactionsCSV() throws Exception {

		Transaction t1 = new Transaction(1, 10, 100);
		Transaction t2 = new Transaction(2, 20, 200);

		mockMvc.perform(post("/transactions/{format}", "csv")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(t1))
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(content().string(Constants.MSG_TRANSACTION_SAVED));

		mockMvc.perform(post("/transactions/{format}", "csv")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(t2))
				)
		.andExpect(status().isOk())
		.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(content().string(Constants.MSG_TRANSACTION_SAVED));


		MvcResult res = mockMvc.perform(get("/transactions/{format}", "csv"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)).andReturn();

		log.info(res.getResponse().getContentAsString());

		ObjectMapper mapper = new ObjectMapper();
		Transaction[] transactions = mapper.readValue(res.getResponse().getContentAsString(), Transaction[].class);

		log.info(transactions.length);
		for(Transaction t : transactions) {
			log.info(t.toString());
		}

		Assert.assertEquals(2, transactions.length);
		Assert.assertTrue(isTransactionAdded(t1, transactions));
		Assert.assertTrue(isTransactionAdded(t2, transactions));

	}

	@Test
	public void testWrongFormat() throws Exception {

		mockMvc.perform(get("/transactions/{format}", "txt")
				)
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(content().string(Constants.ERROR_MESSAGE_WRONG_FORMAT));
		
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		deleteFiles();
	}

	@After
	public void teardown() {
		deleteFiles();
	}

	
	
	/**
	 * 
	 * @param t
	 * @param arr
	 * @return
	 * Only a Util function, keeping it in a Test file since only two util function so far,
	 * should create another class TestUtil if more then 2-3 utils functions are needed. 
	 */
	private boolean isTransactionAdded(Transaction t, Transaction [] arr) {

		for(Transaction t1 : arr) {
			if(t.getId() == t1.getId() &&
					t.getNumberOfItems() == t1.getNumberOfItems() &&
					t.getPrice() == t1.getPrice()) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * delete the csv/excel files before testing the post/get api calls
	 */

	private void deleteFiles() {

		Constants.setFileNames();

		try{
			File csvFile = new File(Constants.csv_file_name);
			log.info(Constants.csv_file_name +", deleted: "+ csvFile.delete());

			File excelFile = new File(Constants.excel_file_name);
			log.info(Constants.excel_file_name +", deleted: "+ excelFile.delete());
		} catch(Exception e) {
			//we can log but ignore exception here and continue with execution of tests
			log.error(e.getMessage(), e);
		}

	}

}