package nts.uk.ctx.pereg.app.find.deleteemployee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfo;
import nts.uk.ctx.bs.employee.dom.employee.mgndata.EmployeeDataMngInfoRepository;
import nts.uk.ctx.bs.person.dom.person.info.Person;
import nts.uk.ctx.bs.person.dom.person.info.PersonRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class EmployeeDeleteFinder {

	@Inject
	private EmployeeDataMngInfoRepository empDataMngRepo;

	@Inject
	private PersonRepository personRepo;

	public EmployeeToDeleteDto getEmployeeInfo(String employeeId) {

		List<EmployeeDataMngInfo> listEmpData = empDataMngRepo.findByEmployeeId(employeeId);
		if (!listEmpData.isEmpty()) {
			EmployeeDataMngInfo empInfo = listEmpData.get(0);
			return EmployeeToDeleteDto.fromDomain(empInfo.getEmployeeCode().v(), empInfo.getRemoveReason().v());
		} else {
			return null;
		}
	}

	public List<EmployeeToDeleteDto> getAllEmployeeInfoToDelete() {

		String loginCID = AppContexts.user().companyId();
		List<EmployeeToDeleteDto> listResult = new ArrayList<>();
		List<EmployeeDataMngInfo> listEmpData = empDataMngRepo.getListEmpToDelete(loginCID);
		if (!listEmpData.isEmpty()) {
			for (EmployeeDataMngInfo employeeDataMngInfo : listEmpData) {
				Optional<Person> person = personRepo.getByPersonId(employeeDataMngInfo.getPersonId());
				if (!person.isPresent()){
					continue;
				}
				listResult.add(EmployeeToDeleteDto.fromDomain(employeeDataMngInfo.getEmployeeCode().v()+" "+ person.get().getPersonNameGroup().getBusinessName().v().trim(), "",
						person.get().getPersonNameGroup().getBusinessName().v().trim(),
						employeeDataMngInfo.getEmployeeId().toString()));
			}
		} else {
			return null;
		}
		return listResult;
	}

	public EmployeeToDeleteDetailDto getDetailEmployeeInfoToDelete(String employeeId) {

//		EmployeeToDeleteDetailDto result = null;
		List<EmployeeDataMngInfo> listEmpData = empDataMngRepo.findByEmployeeId(employeeId);
		if (!listEmpData.isEmpty()) {
			EmployeeDataMngInfo empInfo = listEmpData.get(0);
			Person person = personRepo.getByPersonId(empInfo.getPersonId()).get();

			return EmployeeToDeleteDetailDto.fromDomain(empInfo.getEmployeeCode().v(),
					person.getPersonNameGroup().getBusinessName().v().trim(), 
					empInfo.getRemoveReason() == null ? "" : empInfo.getRemoveReason().toString(),
					empInfo.getDeleteDateTemporary() == null ? "" : empInfo.getDeleteDateTemporary().toString("yyyy/MM/dd HH:mm:ss"));
		} else {
			return null;
		}
	}

	public boolean checkExit(String empCode) {
		Optional<EmployeeDataMngInfo> emp = empDataMngRepo.findByEmployeCD(empCode, AppContexts.user().companyId());
		if (emp.isPresent())
			return true;

		return false;

	}
}
