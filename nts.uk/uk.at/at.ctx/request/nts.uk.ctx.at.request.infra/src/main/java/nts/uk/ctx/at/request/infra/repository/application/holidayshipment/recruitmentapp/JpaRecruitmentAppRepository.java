package nts.uk.ctx.at.request.infra.repository.application.holidayshipment.recruitmentapp;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.ManagementDataAtr;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.SubTargetDigestion;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.WorkTime;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentWorkingHour;
import nts.uk.ctx.at.request.infra.entity.application.holidayshipment.recruitmentapp.KrqdtRecruitmentApp;
import nts.uk.ctx.at.request.infra.entity.application.holidayshipment.subtargetdigestion.KrqdtSubTargetDigestion;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 
 * @author sonnlb
 */
@Stateless
public class JpaRecruitmentAppRepository extends JpaRepository implements RecruitmentAppRepository {

	String FIND_SUB_TAG_DIG_BY_REC_ID = "SELECT d FROM KrqdtSubTargetDigestion d WHERE d.pk.recAppID=:recAppID";

	@Override
	public void insert(RecruitmentApp recApp) {
		this.commandProxy().insert(toEntity(recApp));
	}

	@Override
	public Optional<RecruitmentApp> findByID(String applicationID) {
		return this.queryProxy().find(applicationID, KrqdtRecruitmentApp.class).map(x -> toDomain(x));
	}

	private RecruitmentApp toDomain(KrqdtRecruitmentApp entity) {
		RecruitmentWorkingHour workTime1 = new RecruitmentWorkingHour(new WorkTime(entity.getStartWorkTime1()),
				EnumAdaptor.valueOf(entity.getStartUseAtr1(), NotUseAtr.class), new WorkTime(entity.getEndWorkTime1()),
				EnumAdaptor.valueOf(entity.getEndUseAtr1(), NotUseAtr.class));
		RecruitmentWorkingHour workTime2 = new RecruitmentWorkingHour(new WorkTime(entity.getStartWorkTime2()),
				EnumAdaptor.valueOf(entity.getStartUseAtr2(), NotUseAtr.class), new WorkTime(entity.getEndWorkTime2()),
				EnumAdaptor.valueOf(entity.getEndUseAtr2(), NotUseAtr.class));

		List<SubTargetDigestion> subTargetDigestions = this.queryProxy()
				.query(FIND_SUB_TAG_DIG_BY_REC_ID, KrqdtSubTargetDigestion.class)
				.setParameter("recAppID", entity.getAppID()).getList(x -> toSubTagDigestion(x));

		return new RecruitmentApp(entity.getAppID(), entity.getWorkTypeCD(), new WorkTimeCode(entity.getWorkTimeCD()),
				workTime1, workTime2, subTargetDigestions);
	}

	private SubTargetDigestion toSubTagDigestion(KrqdtSubTargetDigestion entity) {
		return new SubTargetDigestion(entity.getAppID(), entity.getHoursUsed(), entity.getLeaveMngDataID(),
				entity.getBreakOutDate(), EnumAdaptor.valueOf(entity.getRestState(), ManagementDataAtr.class),
				entity.getUnknownDate());
	}

	private KrqdtRecruitmentApp toEntity(RecruitmentApp recApp) {
		KrqdtRecruitmentApp entity = new KrqdtRecruitmentApp();
		entity.setAppID(recApp.getAppID());
		entity.setWorkTypeCD(recApp.getWorkTypeCD());
		entity.setWorkTimeCD(recApp.getWorkTimeCD().v());

		RecruitmentWorkingHour wkTime1 = recApp.getWorkTime1();
		if (wkTime1 != null) {
			entity.setStartWorkTime1(wkTime1.getStartTime().v());
			entity.setStartUseAtr1(wkTime1.getStartUseAtr().value);
			entity.setEndWorkTime1(wkTime1.getEndTime().v());
			entity.setEndUseAtr1(wkTime1.getEndUseAtr().value);
		}
		RecruitmentWorkingHour wkTime2 = recApp.getWorkTime2();
		if (wkTime2 != null) {
			entity.setStartWorkTime2(wkTime2.getStartTime().v());
			entity.setStartUseAtr2(wkTime2.getStartUseAtr().value);
			entity.setEndWorkTime2(wkTime2.getEndTime().v());
			entity.setEndUseAtr2(wkTime2.getEndUseAtr().value);
		}

		return entity;

	}

	@Override
	public void update(RecruitmentApp recApp) {
		this.commandProxy().update(toEntity(recApp));

	}

	@Override
	public void remove(String appID) {
		Optional<KrqdtRecruitmentApp> entityOpt = this.queryProxy().find(appID, KrqdtRecruitmentApp.class);
		if (entityOpt.isPresent()) {
			this.commandProxy().remove(KrqdtRecruitmentApp.class, appID);
		}

	}

}
