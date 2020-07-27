/**
 * 
 */
package nts.uk.screen.at.app.ksu001.getinfoofInitstartup;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.schedule.dom.workschedule.displaysetting.WorkScheDisplaySetting;
import nts.uk.ctx.at.schedule.dom.workschedule.displaysetting.WorkScheDisplaySettingRepo;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.TargetOrganizationUnit;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author laitv 
 * ScreenQuery: 初期起動の情報取得 
 * path: UKDesign.UniversalK.就業.KSU_スケジュール.KSU001_個人スケジュール修正(職場別).A：個人スケジュール修正（職場別）.メニュー別OCD.初期起動の情報取得
 */
@Stateless
public class ScreenQueryGetInforOfInitStartup {

	@Inject
	private WorkScheDisplaySettingRepo workScheDisplaySettingRepo;

	public DataScreenQueryGetInforDto getDataInit() {
		// Step 1,2
		String companyID = AppContexts.user().companyId();
		Optional<WorkScheDisplaySetting> workScheDisplaySettingOpt = workScheDisplaySettingRepo.get(companyID);
		if (!workScheDisplaySettingOpt.isPresent()) {
			return new DataScreenQueryGetInforDto(null, null, null, null);
		}

		DatePeriod datePeriod = workScheDisplaySettingOpt.get().calcuInitDisplayPeriod();

		// step 3
		// goi domain service 社員の対象組織識別情報を取得する
		TargetOrgIdenInforDto targetOrgIdenInfor = new TargetOrgIdenInforDto(TargetOrganizationUnit.WORKPLACE.value,
				"dea95de1-a462-4028-ad3a-d68b8f180412", null);

		// step 4
		DisplayInforOrganization displayInforOrganization = new DisplayInforOrganization("designation", "code", "name",
				"targetOrganizationName", "genericTerm");
		
		return new DataScreenQueryGetInforDto(datePeriod.start(), datePeriod.end(), targetOrgIdenInfor,
				displayInforOrganization);
	}

}
