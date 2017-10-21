package wabri.PayRoll;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class PayRollTest {

	private PayRoll payRoll;
	private EmployeeDB employeeDB;
	private BankService bankService;
	private List<Employee> employees;

	@Before
	public void init() {
		employeeDB = mock(EmployeeDB.class);
		bankService = mock(BankService.class);
		employees = new ArrayList<Employee>();

		when(employeeDB.getAllEmployees()).thenReturn(employees);

		payRoll = new PayRoll(employeeDB, bankService);
	}

	@Test
	public void testNoEmployees() {
		assertNumberOfPayments(0);
	}

	@Test
	public void testSingleEmployeed() {
		employees.add(createTestEmployee("Test Employee", "ID1", 1000));

		assertNumberOfPayments(1);
	}

	@Test
	public void testEmployeeIsPaid() {
		String employeeID = "ID1";
		int salary = 1000;

		employees.add(createTestEmployee("Test Employee", "ID1", 1000));

		assertNumberOfPayments(1);

		verify(bankService, times(1)).makePayment(employeeID, salary);
	}
	
	@Test
	public void testAllEmployeesArePaid () {
		employees.add(createTestEmployee("employee1", "ID1", 200));
		employees.add(createTestEmployee("employee2", "ID2", 400));
		
		assertNumberOfPayments(2);
		
		ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> salaryCaptor = ArgumentCaptor.forClass(Integer.class);
		
		verify(bankService,times(2)).makePayment(idCaptor.capture(), salaryCaptor.capture());
		
		assertEquals("ID1", idCaptor.getAllValues().get(0));
		assertEquals("ID2", idCaptor.getAllValues().get(1));
		assertEquals(200, salaryCaptor.getAllValues().get(0).intValue());
		assertEquals(400, salaryCaptor.getAllValues().get(1).intValue());
	}

	private void assertNumberOfPayments(int expected) {
		int numberOfPayment = payRoll.monthlyPayment();
		assertEquals(expected, numberOfPayment);
	}

	private Employee createTestEmployee(String nameEmployee, String bankId, int salary) {
		return new Employee(nameEmployee, bankId, salary);
	}

}
