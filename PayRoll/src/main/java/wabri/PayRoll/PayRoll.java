package wabri.PayRoll;

import java.util.List;

import org.apache.log4j.Logger;

public class PayRoll {

	final static Logger LOGGER = Logger.getLogger(PayRoll.class);

	private EmployeeDB employeeDB;
	private BankService bankService;

	public PayRoll(EmployeeDB employeeDB, BankService bankService) {
		super();
		this.employeeDB = employeeDB;
		this.bankService = bankService;
	}

	public int monthlyPayment() {
		List<Employee> employees = employeeDB.getAllEmployees();

		for (Employee employee : employees) {
			try {
				bankService.makePayment(employee.getBankId(), employee.getSalary());
				employee.setPaid(true);
			} catch (RuntimeException re) {
				LOGGER.error("Bank can not pay employee: " + employee.getName());
				employee.setPaid(false);
			}
		}

		return employees.size();
	}
	
	public static void main(String[] args) {
		
	}

}
