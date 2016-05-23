package testPackage;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import Utility.Constants;
import bean.Transaction;
import controller.Controller;
import service.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springrest-servlet.xml" })
public class ControllerTest {

	Logger log = Logger.getLogger(ControllerTest.class.getName());

	private MockMvc mockMvc;

	@Mock
	private Service service;
	
	@InjectMocks
	private Controller controller;

	@Test
	public void testCreateTransaction() throws Exception {
		Transaction trans = new Transaction(1, 23,23);
		
		doNothing().when(service).saveTransaction(any(Transaction.class), any(String.class));

		mockMvc.perform(post("/transactions/csv")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trans))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(content().string(Constants.MSG_TRANSACTION_SAVED));
	}

	/**
	 * Since these are only Unit tests and no real data will be populated in 
	 * csv/excel files, so no need to delete files before/after tests
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
}
