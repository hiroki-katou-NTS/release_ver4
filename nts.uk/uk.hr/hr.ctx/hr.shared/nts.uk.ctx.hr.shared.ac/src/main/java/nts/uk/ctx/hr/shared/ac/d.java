package nts.uk.ctx.hr.shared.ac;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.query.pub.classification.ClassificationExport;
import nts.uk.query.pub.department.DepartmentExport;
import nts.uk.query.pub.employee.EmployeeInformationExport;
import nts.uk.query.pub.employee.EmployeeInformationPub;
import nts.uk.query.pub.employee.EmployeeInformationQueryDto;
import nts.uk.query.pub.employement.EmploymentExport;
import nts.uk.query.pub.position.PositionExport;
import nts.uk.query.pub.workplace.WorkplaceExport;

@Stateless
public class d implements EmployeeInforAdapter {

	@Inject
	private EmployeeInformationPub employeeInformationPub;
	
	
	private EmployeeInformationImport toImport (EmployeeInformationExport ex){
		
		return new EmployeeInformationImport(
				ex.getEmployeeId(),
				ex.getEmployeeCode(),
				ex.getBusinessName(),
				converToWorkplaceExport( ex.getWorkplace()),
				converToClassificationExport(ex.getClassification()),
				converToDepartmentExport(ex.getDepartment()),
				converToPositionExport(ex.getPosition()),
				converToEmploymentExport(ex.getEmployment()),
				ex.getEmploymentCls());
		
	}
	private WorkplaceImport converToWorkplaceExport (WorkplaceExport ex){
		return new WorkplaceImport
				(ex.getWorkplaceId(),
			     ex.getWorkplaceCode(),
			     ex.getWorkplaceGenericName(),
			     ex.getWorkplaceName());
	}
	
	private ClassificationImport converToClassificationExport (ClassificationExport ex){
		return new ClassificationImport(
				ex.getClassificationCode(),
				ex.getClassificationName());
	}
	
	private DepartmentImport converToDepartmentExport (DepartmentExport ex){
		return new DepartmentImport(
				ex.getDepartmentCode(),
				ex.getDepartmentName(),
				ex.getDepartmentGenericName());
	}
	private PositionImport converToPositionExport(PositionExport ex){
		return new PositionImport(
				ex.getPositionId(),
				ex.getPositionCode(),
				ex.getPositionName());
	}
	private EmploymentImport converToEmploymentExport (EmploymentExport ex){
		return new EmploymentImport(
				ex.getEmploymentCode(),
				ex.getEmploymentName());
	}
	@Override
	public List<EmployeeInformationImport> find(EmployeeInfoQueryImport param ) {		
		List<EmployeeInformationImport> result = employeeInformationPub.find(convertQuery(param)).stream().map(c -> this.toImport(c)).collect(Collectors.toList());	

		return result;
	}
	private EmployeeInformationQueryDto convertQuery(EmployeeInfoQueryImport query){
		return EmployeeInformationQueryDto.builder()
				.employeeIds(query.getEmployeeIds())
				.referenceDate(query.getReferenceDate())
				.toGetWorkplace(query.isToGetWorkplace())
				.toGetDepartment(query.isToGetDepartment())
				.toGetPosition(query.isToGetPosition())
				.toGetEmployment(query.isToGetEmployment())
				.toGetClassification(query.isToGetClassification())
				.toGetEmploymentCls(query.isToGetEmploymentCls()).build();
	}
}
