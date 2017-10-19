package wabri.PayRoll;

import java.util.List;

public class PayRoll {
	
	private EmployeeDB employeeDB;
	
	public PayRoll(EmployeeDB employeeDB) {
		super();
		this.employeeDB = employeeDB;
	}

	public int monthlyPayment() {
		List<Employee> employees = employeeDB.getAllEmployees();
		return employees.size();
	}

}
