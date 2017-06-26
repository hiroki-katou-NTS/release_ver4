package nts.uk.ctx.pr.core.app.find.rule.employment.allot;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.EmployeeAllotSettingRepository;

@Stateless
public class EmployeeAllotSettingFinder {
	@Inject
	private EmployeeAllotSettingRepository EmpAllotSettingRepo;

	/**
	 * 
	 * @param companyCode
	 * @param historyId
	 * @return
	 */
	public List<EmployeeAllotSettingDto> getAllEmployeeAllotDetailSetting(String companyCode, String historyId) {
		return this.EmpAllotSettingRepo.findAll(companyCode, historyId).stream()
				.map(m -> EmployeeAllotSettingDto.fromDomain(m)).collect(Collectors.toList());
	}

	public List<EmployeeAllSettingDto> getAllEmployeeAllotSettingList(String companyCode, String historyID) {
		return this.EmpAllotSettingRepo.findAllEm(companyCode, historyID).stream()
				.map(e -> EmployeeAllSettingDto.fromDomain(e)).collect(Collectors.toList());

	}
	public List<EmployeeAllotSettingDto> getEmpDetail(String companyCode, String historyID){
		List<EmployeeAllotSettingDto> lstDto = this.EmpAllotSettingRepo.findEmpDetail(companyCode, historyID).stream()
				.map(a -> EmployeeAllotSettingDto.fromDomain(a)).collect(Collectors.toList());
		return lstDto;
	}
}
