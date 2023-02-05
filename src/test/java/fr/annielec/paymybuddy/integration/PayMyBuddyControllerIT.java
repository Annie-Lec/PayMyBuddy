package fr.annielec.paymybuddy.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@SpringBootTest
@AutoConfigureMockMvc
class PayMyBuddyControllerIT {
	
	@Autowired
	private MockMvc mockMvc;


	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	@WithMockUser("minie@yahoo.fr")
	void test_addNewContact_OK() throws Exception {
		
		mockMvc.perform(post("/addnewContact")
			.contentType(MediaType.parseMediaType("application/x-www-form-urlencoded"))
			.param("emailContact", "buddy2@email.fr")
			.with(csrf()))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print())
			.andExpect(redirectedUrl("/contacts"))
			.andExpect(status().isFound());
		
	}
	
	@Test
	@WithMockUser("minie@yahoo.fr")
	void test_addNewContact_ALREADY() throws Exception {
		
		mockMvc.perform(post("/addnewContact")
			.contentType(MediaType.parseMediaType("application/x-www-form-urlencoded"))
			.param("emailContact", "buddy2@email.fr")
			.with(csrf()))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("newContact"))
			.andExpect(content().string(containsString("You have already added this contact in your buddies !")));
		
	}
	
	@Test
	@WithMockUser("minie@yahoo.fr")
	void test_addNewContact_NOTBDD() throws Exception {
		
		mockMvc.perform(post("/addnewContact")
			.contentType(MediaType.parseMediaType("application/x-www-form-urlencoded"))
			.param("emailContact", "buddy11111111@email.fr")
			.with(csrf()))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("newContact"))
			.andExpect(content().string(containsString("This contact doesn't figure in our database : please verify your tape!")));
		
	}
	
	
	
	@Test
	void test_listTransfertsU_OK() throws Exception {
		
		mockMvc.perform(get("/transfers").with(user("minie@yahoo.fr"))
		.contentType(MediaType.parseMediaType("application/x-www-form-urlencoded"))
		.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("Transfert"))
			.andExpect(content().string(containsString("Send Money")))
			.andExpect(content().string(containsString("10 pour Roger")))
			;
		
	}
	
	@Test
		void test_newTransfert_OK() throws Exception {
		
		mockMvc.perform(get("/newTransfer").with(user("minie@yahoo.fr"))
		.contentType(MediaType.parseMediaType("application/x-www-form-urlencoded"))
		.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("newTransfert"))
			.andExpect(content().string(containsString("Transfer money to your buddies !")))
			;
		
	}
	
	@Test
	@WithMockUser("minie@yahoo.fr")
	void test_addnewTransfert_OK_amountPositiv_balanceSufficient() throws Exception {
		
		mockMvc.perform(post("/addnewTransfer")
			.contentType(MediaType.parseMediaType("application/x-www-form-urlencoded"))
			.param("pseudoContact", "buddy2@email.fr")
			//.param("pseudoContact", "roger@gmail.com")
			.param("amount", "50")
			.param("description", "50 pour Buddy")
			.with(csrf()))
			.andDo(MockMvcResultHandlers.print())
			.andDo(print())
			.andExpect(redirectedUrl("/transfers"))
			.andExpect(status().isFound());
		
	}



}
