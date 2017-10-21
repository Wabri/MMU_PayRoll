package wabri.PayRoll;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PayRollTest {

	private PayRoll payRoll;
	@Mock private EmployeeDB employeeDB;
	@Mock private BankService bankService;
	private List<Employee> employees;

	@Before
	public void init() {
		employees = new ArrayList<Employee>();

		when(employeeDB.getAllEmployees()).thenReturn(employees);

		payRoll = new PayRoll(employeeDB, bankService);
	}

	@Test
	public void testNoEmployees() {
		assertNumberOfProcessedEmployees(0);
	}

	@Test
	public void testSingleEmployeed() {
		employees.add(createTestEmployee("Test Employee", "ID1", 1000));

		assertNumberOfProcessedEmployees(1);
	}

	@Test
	public void testEmployeeIsPaid() {
		String employeeID = "ID1";
		int salary = 1000;

		employees.add(createTestEmployee("Test Employee", "ID1", 1000));

		assertNumberOfProcessedEmployees(1);

		verify(bankService, times(1)).makePayment(employeeID, salary);
	}

	@Test
	public void testAllEmployeesArePaid() {
		employees.add(createTestEmployee("employee1", "ID1", 200));
		employees.add(createTestEmployee("employee2", "ID2", 400));

		assertNumberOfProcessedEmployees(2);

		ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> salaryCaptor = ArgumentCaptor.forClass(Integer.class);

		verify(bankService, times(2)).makePayment(idCaptor.capture(), salaryCaptor.capture());

		assertEquals("ID1", idCaptor.getAllValues().get(0));
		assertEquals("ID2", idCaptor.getAllValues().get(1));
		assertEquals(200, salaryCaptor.getAllValues().get(0).intValue());
		assertEquals(400, salaryCaptor.getAllValues().get(1).intValue());
	}
	
	@Test
	public void testInteractionOrder() {
		String employeeId = "ID1";
		int salary = 1000;
		
		employees.add(createTestEmployee("Test Employee", employeeId, salary));
		
		assertNumberOfProcessedEmployees(1);
		
		InOrder inOrder = inOrder(employeeDB, bankService);
		inOrder.verify(employeeDB).getAllEmployees();
		inOrder.verify(bankService).makePayment(employeeId, salary);
	}

	@Test
	public void testEmployeePaidIsUpdate() {
		String employeeId = "ID1";
		int salary = 1000;

		Employee testEmployee = spy(createTestEmployee("Test Employee", employeeId, salary));
		employees.add(testEmployee);

		assertNumberOfProcessedEmployees(1);

		verify(bankService, times(1)).makePayment(employeeId, salary);
		verify(testEmployee).setPaid(true);
	}
	
	@Test
	public void testOnlyOneInteractionWithDB () {
		payRoll.monthlyPayment();
		verify(employeeDB).getAllEmployees();
	}

	@Test
	public void testEmployeeIsNotPaidWhenBankThrowsException() {
		String employeeID = "ID1";
		int salary = 1000;
		Employee testEmployee = spy(createTestEmployee("Test Employee", employeeID, salary));
		employees.add(testEmployee);

		doThrow(new RuntimeException()).when(bankService).makePayment(anyString(), anyInt());

		assertNumberOfProcessedEmployees(1);

		verify(bankService, times(1)).makePayment(employeeID, salary);
		verify(testEmployee, times(1)).setPaid(false);
	}

	@Test
	public void testOtherEmployeesArePaidInCaseOfASingleException() {
		Employee firstEmployee = spy(createTestEmployee("Test Employee1", "ID1", 100));
		employees.add(firstEmployee);
		Employee secondEmployee = spy(createTestEmployee("Test Employee2", "ID2", 200));
		employees.add(secondEmployee);

		doThrow(new RuntimeException()).doNothing().when(bankService).makePayment(anyString(), anyInt());
		
		assertNumberOfProcessedEmployees(2);
		
		verify(bankService,times(2)).makePayment(anyString(), anyInt());
		verify(firstEmployee, times(1)).setPaid(false);
		verify(secondEmployee, times(1)).setPaid(true);
	}

	private void assertNumberOfProcessedEmployees(int expected) {
		int numberOfPayment = payRoll.monthlyPayment();
		assertEquals(expected, numberOfPayment);
	}

	private Employee createTestEmployee(String nameEmployee, String bankId, int salary) {
		return new Employee(nameEmployee, bankId, salary);
	}

}
