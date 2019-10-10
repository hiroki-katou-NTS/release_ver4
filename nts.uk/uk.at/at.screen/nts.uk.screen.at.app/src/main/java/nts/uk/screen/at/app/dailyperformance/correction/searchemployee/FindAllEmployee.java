package nts.uk.screen.at.app.dailyperformance.correction.searchemployee;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.query.model.employee.EmployeeInformation;
import nts.uk.query.model.employee.EmployeeInformationQuery;
import nts.uk.query.model.employee.EmployeeInformationRepository;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceEmployeeDto;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class FindAllEmployee {

	@Inject
	private EmployeeInformationRepository employeeInformationPub;

	public List<DailyPerformanceEmployeeDto> findAllEmployee(List<String> employeeIds, GeneralDate referenceDate) {
		
		List<EmployeeInformation> employeeInfo = employeeInformationPub.find(createQuery(employeeIds, referenceDate));
		
		return employeeInfo.stream().map(x -> {
			val workplace = x.getWorkplace().orElse(null);
			return DailyPerformanceEmployeeDto.builder().id(x.getEmployeeId()).code(x.getEmployeeCode())
					.businessName(x.getBusinessName())
					.workplaceName(workplace != null ? workplace.getWorkplaceName() : "")
					.workplaceId(workplace != null ? workplace.getWorkplaceCode() : "").build();
		}).collect(Collectors.toList());
	}

	private EmployeeInformationQuery createQuery(List<String> employeeIds, GeneralDate referenceDate) {
		return EmployeeInformationQuery.builder().employeeIds(employeeIds).referenceDate(referenceDate)
				.toGetClassification(false).toGetDepartment(false).toGetEmployment(true).toGetEmploymentCls(false)
				.toGetPosition(false).toGetWorkplace(true).build();
	}
}
