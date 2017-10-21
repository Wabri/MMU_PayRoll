package wabri.PayRoll;

import java.util.List;

public class PayRoll {

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
				System.err.println("Exception when paying " + employee.getName());
				employee.setPaid(false);
			}
		}

		return employees.size();
	}

}
