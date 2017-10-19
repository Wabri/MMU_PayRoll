package wabri.PayRoll;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PayRollTest {

	private PayRoll payRoll;
	
	private EmployeeDB employeeDB;

	@Before
	public void init () {
		employeeDB = mock(EmployeeDB.class);
		payRoll = new PayRoll(employeeDB);
	}
	
	@Test
	public void testNoEmployees() {
		assertNumberOfPayments(0);
	}
	
	@Test
	public void testSingleEmployeed() {
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(new Employee());
		
		when(employeeDB.getAllEmployees()).thenReturn(employees);
		
		assertNumberOfPayments(1);
	}

	private void assertNumberOfPayments(int expected) {
		int numberOfPayment = payRoll.monthlyPayment();
		assertEquals(expected, numberOfPayment);
	}

}
