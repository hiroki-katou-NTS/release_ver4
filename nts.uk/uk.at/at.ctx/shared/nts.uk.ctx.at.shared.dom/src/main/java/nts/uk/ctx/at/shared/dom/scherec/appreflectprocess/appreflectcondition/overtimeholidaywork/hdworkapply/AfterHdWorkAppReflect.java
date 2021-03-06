package nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.hdworkapply;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.scherec.application.holidayworktime.AppHolidayWorkShare;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.ReflectAppDestination;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.BreakApplication;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.OthersReflect;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.algorithm.reflectbreak.ReflectApplicationTime;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.overtimeholidaywork.algorithm.subtransfer.TranferHdWorkCompensatoryApp;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.DailyRecordOfApplication;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.condition.DailyAfterAppReflectResult;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.condition.ReflectAttendance;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.condition.ReflectDirectBounceClassifi;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.condition.ReflectWorkInformation;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.workchangeapp.ReflectWorkChangeApp.WorkInfoDto;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.TimeChangeMeans;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * refactor 4
 * 
 *  ?????????????????????????????????
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AfterHdWorkAppReflect extends DomainObject {
	/**
	 * ??????????????????????????????
	 */
	private OthersReflect othersReflect;

	/**
	 * ????????????????????????????????????
	 */
	private BreakApplication breakLeaveApplication;

	/**
	 * ????????????????????????
	 */
	private NotUseAtr workReflect;

	public static AfterHdWorkAppReflect create(int workReflect, int reflectPaytime, int reflectDivergence,
			int reflectBreakOuting) {
		return new AfterHdWorkAppReflect(
				new OthersReflect(EnumAdaptor.valueOf(reflectDivergence, NotUseAtr.class),
						EnumAdaptor.valueOf(reflectPaytime, NotUseAtr.class)),
				new BreakApplication(EnumAdaptor.valueOf(reflectBreakOuting, NotUseAtr.class)),
				EnumAdaptor.valueOf(workReflect, NotUseAtr.class));
	}

	/**
	 * @author thanh_nx
	 *
	 *         ?????????????????????????????????
	 */

	public DailyAfterAppReflectResult process(Require require, String cid, AppHolidayWorkShare holidayApp,
			DailyRecordOfApplication dailyApp) {
		List<Integer> lstId = new ArrayList<Integer>();
		// ???????????????????????????DTO????????????
		WorkInfoDto workInfoDto = new WorkInfoDto(
				Optional.ofNullable(holidayApp.getWorkInformation().getWorkTypeCode()),
				holidayApp.getWorkInformation().getWorkTimeCodeNotNull());

		// ???????????????????????????????????????????????????????????????
		String workTypeApp = holidayApp.getWorkInformation().getWorkTypeCode().v();
		String workTimeApp = workInfoDto.getWorkTimeCode().map(x -> x.v()).orElse(null);
		String workTypeDomain = dailyApp.getWorkInformation().getRecordInfo().getWorkTypeCode().v();
		String workTimeDomain = dailyApp.getWorkInformation().getRecordInfo().getWorkTimeCodeNotNull().map(x -> x.v())
				.orElse(null);
		if (!(workTypeApp.equals(workTypeDomain)
				&& (workTimeApp == null ? workTimeDomain == null : workTimeApp.equals(workTimeDomain)))) {
			// ?????????????????????
			lstId.addAll(ReflectWorkInformation.reflectInfo(require, cid, workInfoDto, dailyApp, Optional.of(true),
					Optional.of(true)));
		}

		// ???????????????????????????
		lstId.addAll(
				ReflectDirectBounceClassifi.reflect(dailyApp, holidayApp.getBackHomeAtr(), holidayApp.getGoWorkAtr()));

		// [????????????????????????]???????????????
		if (this.getWorkReflect() == NotUseAtr.USE) {
			// ??????????????????
			lstId.addAll(ReflectAttendance.reflect(require, cid, holidayApp.getWorkingTimeList(), ScheduleRecordClassifi.RECORD,
					dailyApp, Optional.of(holidayApp.getGoWorkAtr() == NotUseAtr.NOT_USE),
					Optional.of(holidayApp.getBackHomeAtr() == NotUseAtr.NOT_USE), Optional.of(TimeChangeMeans.APPLICATION)));
		}

		// ?????????????????????
		ReflectApplicationTime.process(holidayApp.getApplicationTime().getApplicationTime(), dailyApp,
				Optional.of(ReflectAppDestination.RECORD));

		// ????????????????????????
		this.othersReflect.process(holidayApp.getApplicationTime(), dailyApp);

		// ??????????????????????????????
		this.getBreakLeaveApplication().process(holidayApp.getBreakTimeList(), dailyApp);

		// ?????????????????????????????????
		TranferHdWorkCompensatoryApp.process(require, cid, dailyApp.getDomain());
		return new DailyAfterAppReflectResult(dailyApp, lstId);
	}

	public static interface Require extends ReflectWorkInformation.Require, TranferHdWorkCompensatoryApp.Require, ReflectAttendance.Require {

	}
}
