package nts.uk.pr.proto.dom
public interface EmployeeRepository {
	Employee getEmployee(int id);
	void insert(Employee emp);
	void update(Employee emp);
	void delete(Employee emp);
}