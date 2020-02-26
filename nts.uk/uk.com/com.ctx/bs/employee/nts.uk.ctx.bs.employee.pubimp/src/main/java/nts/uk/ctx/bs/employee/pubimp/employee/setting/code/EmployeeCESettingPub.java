package nts.uk.ctx.bs.employee.pubimp.employee.setting.code;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.setting.code.EmployeeCESetting;
import nts.uk.ctx.bs.employee.dom.setting.code.IEmployeeCESettingRepository;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.setting.code.EmployeeCodeEditSettingExport;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.setting.code.IEmployeeCESettingPub;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.employee.setting.EmployeeCodeSettingAdapter;
import nts.uk.shr.com.employee.setting.EmployeeCodeSettingDto;

@Stateless
public class EmployeeCESettingPub implements IEmployeeCESettingPub, EmployeeCodeSettingAdapter {

	@Inject
	IEmployeeCESettingRepository repo;

	@Override
	public Optional<EmployeeCodeEditSettingExport> getByComId(String companyId) {
		Optional<EmployeeCESetting> domain = repo.getByComId(companyId);

		if (!domain.isPresent()) {
			return Optional.empty();
		}

		EmployeeCESetting _domain = domain.get();

		return Optional.of(new EmployeeCodeEditSettingExport(_domain.getCompanyId(), _domain.getCeMethodAtr().value,
				_domain.getDigitNumb().v()));
	}

	@Override
	public Optional<EmployeeCodeSettingDto> find() {
		return repo.getByComId(AppContexts.user().companyId())
				.map(c -> new EmployeeCodeSettingDto(c.getCompanyId(), c.getCeMethodAtr().value, c.getDigitNumb().v()));
	}
}
