package wabri.PayRoll;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PayRollTest {

	private PayRoll payRoll;
	
	private EmployeeDB employeeDB;

	@Before
	public void init () {
		payRoll = new PayRoll(employeeDB);
	}
	
	@Test
	public void test() {
		assertNumberOfPayments(0);
	}

	private void assertNumberOfPayments(int expected) {
		int numberOfPayment = payRoll.monthlyPayment();
		assertEquals(expected, numberOfPayment);
	}

}
