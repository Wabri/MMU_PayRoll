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

	private List<Employee> employees;

	@Before
	public void init () {
		employeeDB = mock(EmployeeDB.class);
		employees = new ArrayList<Employee>();
		
		when(employeeDB.getAllEmployees()).thenReturn(employees);

		payRoll = new PayRoll(employeeDB);
	}
	
	@Test
	public void testNoEmployees() {
		assertNumberOfPayments(0);
	}
	
	@Test
	public void testSingleEmployeed() {
		employees.add(new Employee());
		
		assertNumberOfPayments(1);
	}
	
	@Test
	public void testOnlyOneInteractionWithDB () {
		payRoll.monthlyPayment();
		verify(employeeDB).getAllEmployees();
	}

	private void assertNumberOfPayments(int expected) {
		int numberOfPayment = payRoll.monthlyPayment();
		assertEquals(expected, numberOfPayment);
	}

}
