package org.staff.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import net.assino.app1msc.Employee;
import net.assino.app1msc.TaskManagerPortType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.staff.utils.Convertor;
import org.staff.utils.WSHandler;

import static org.mockito.Mock.*;

//@RunWith(MockitoJUnitRunner.class)
public class WSHandlerJUnit4Test {


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		 
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAuthenticationIsWorking() {
		String usernameCP1251 = anyString();
		String password = anyString();
		
		//TaskManagerPortType port = WSHandler.getPort(Convertor.getUTF8Representation(usernameCP1251), password);
		TaskManagerPortType port = mock(TaskManagerPortType.class);
		
		//TaskManagerPortType port = WSHandler.getPort(Convertor.getUTF8Representation(usernameCP1251), password);
		when(port.getCurrentUser()).then(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
					return new Employee();
			}
				
		});
		
		//assertEquals(WSHandler.testPortConnection(port), true);
		
		validateMockitoUsage();
	}
	
//	@Mock 
//	private String m;
//	
//	@InjectMocks
//	private LinkedList list = new LinkedList<>();
	
	@Test
	public void testListWithMocks() {
		LinkedList mockedList = mock(LinkedList.class);
		 
		//stub'инг
		when(mockedList.get(0)).thenReturn("first");
		when(mockedList.get(1)).thenThrow(new RuntimeException());
		 
		//получим "first"
		assertEquals(mockedList.get(0), "first");
		 
	}
	
	@Test
	public void testToUTF8RepresentationConversion() throws UnsupportedEncodingException {
		String usernameCP1251 = "Замковый Александр";
		String utf8username = Convertor.getUTF8Representation(usernameCP1251);
		String usernameBackToCP1251 = new String(utf8username.getBytes("CP1251"), "UTF-8");
		assertEquals(usernameCP1251, usernameBackToCP1251);
	}
	
	@Test
	public void testToConvertGregorianCalenderToXMLGregorianCalender(){
		GregorianCalendar gDate = new GregorianCalendar(2014, 10, 30);
		assertTrue(Convertor.xmlGregorianCalenderToGregorianCalender(Convertor.gregorianCalenderToXMLGregorianCalender(gDate)).equals(gDate));
	}
	
	@Test
	public void testToSetAutofillDailyReports(){
		fail("Not implemented yet");
	}
	
	@Test
	public void testToGetProjectById(){
		fail("Not implemented yet");
	}
	
	@Test
	public void testToGetTaskList() {
		//WSHandler.getTaskList
		fail("Not implemented yet");
	}
	
	//getTaskList
}
