/**
 * 9:53:14 AM Jun 6, 2017
 */
package nts.uk.ctx.at.record.dom.bonuspay.repository;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.record.dom.bonuspay.primitives.WorkplaceId;
import nts.uk.ctx.at.record.dom.bonuspay.setting.WorkplaceBonusPaySetting;


/**
 * @author hungnm
 *
 */
public interface WPBonusPaySettingRepository {

	List<WorkplaceBonusPaySetting> getListSetting(List<WorkplaceId> lstWorkplace);

	void addWPBPSetting(WorkplaceBonusPaySetting workplaceBonusPaySetting);

	void updateWPBPSetting(WorkplaceBonusPaySetting workplaceBonusPaySetting);

	void removeWPBPSetting(WorkplaceBonusPaySetting workplaceBonusPaySetting);
	
	Optional<WorkplaceBonusPaySetting> getWPBPSetting(WorkplaceId WorkplaceId);

}
