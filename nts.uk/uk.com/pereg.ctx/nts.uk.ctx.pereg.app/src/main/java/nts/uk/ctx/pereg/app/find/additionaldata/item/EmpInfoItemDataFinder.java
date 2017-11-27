package nts.uk.ctx.pereg.app.find.additionaldata.item;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.item.EmpInfoItemDataRepository;
import nts.uk.ctx.pereg.app.find.initsetting.item.SettingItemDto;

@Stateless
public class EmpInfoItemDataFinder {

	@Inject
	private EmpInfoItemDataRepository infoItemDataRepo;

	public List<SettingItemDto> loadInfoItemDataList(String categoryCd, String companyId, String employeeId) {
		return this.infoItemDataRepo.getAllInfoItem(categoryCd, companyId, employeeId).stream()
				.map(x -> SettingItemDto.fromInfoDataItem(x)).collect(Collectors.toList());
	}

}
