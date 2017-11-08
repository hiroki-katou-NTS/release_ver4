package nts.uk.ctx.at.request.dom.application.common.adapter.schedule.shift.businesscalendar.specificdate;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.shift.businesscalendar.specificdate.dto.WpSpecificDateSettingImport;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface WpSpecificDateSettingAdapter {
	
	public WpSpecificDateSettingImport workplaceSpecificDateSettingService(String companyID, String workPlaceID, GeneralDate date);
	
}
