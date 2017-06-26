package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.util.List;
import java.util.Optional;

public interface SpecificationDepartmentRepository {
	
	void add(SpecificationDepartment specificationDepartment);
	
	void update(SpecificationDepartment specificationDepartment);
	
	void remove(String companyCode, SpecificationDepartment  specificationDepartment, String historyId);
	
	Optional<SpecificationDepartment> findSingle(String companyCode, SpecificationDepartment specificationDepartment, String historyId);
	
	List<SpecificationDepartment> findAll(String companyCode);
}
