package nts.uk.ctx.at.shared.app.find.bonuspay;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.WorkplaceId;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.WPBonusPaySettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.WorkplaceBonusPaySetting;

@Stateless
@Transactional
public class WPBonusPaySettingFinder {
	@Inject
	private WPBonusPaySettingRepository repo;

	public List<WPBonusPaySettingDto> getListSetting(List<String> lstWorkplace) {
		List<WorkplaceBonusPaySetting> domains = this.repo
				.getListSetting(lstWorkplace.stream().map(c -> new WorkplaceId(c)).collect(Collectors.toList()));

		return domains.stream().map(c -> toWPBonusPaySettingDto(c)).collect(Collectors.toList());
	}

	public WPBonusPaySettingDto getWPBPSetting(String WorkplaceId) {

		Optional<WorkplaceBonusPaySetting> domain = this.repo.getWPBPSetting(new WorkplaceId(WorkplaceId));

		if (domain.isPresent()) {
			return this.toWPBonusPaySettingDto(domain.get());
		}

		return null;
	}

	private WPBonusPaySettingDto toWPBonusPaySettingDto(WorkplaceBonusPaySetting workplaceBonusPaySetting) {
		return new WPBonusPaySettingDto(workplaceBonusPaySetting.getWorkplaceId().toString(),
				workplaceBonusPaySetting.getBonusPaySettingCode().toString());
	}

}
