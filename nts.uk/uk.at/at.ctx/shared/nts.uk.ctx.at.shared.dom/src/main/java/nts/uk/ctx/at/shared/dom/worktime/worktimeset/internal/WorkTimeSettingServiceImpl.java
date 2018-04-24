/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.schedule.basicschedule.JoggingWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.caltimediff.CalculateTimeDiffService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.difftimecorrection.DiffTimeCorrectionService;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class WorkTimeSettingServiceImpl.
 */
@Stateless
public class WorkTimeSettingServiceImpl implements WorkTimeSettingService {

	/** The work time setting repo. */
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepo;

	/** The predetemine time repo. */
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeRepo;

	/** The fixed work setting repo. */
	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepo;

	/** The flex work setting repo. */
	@Inject
	private FlexWorkSettingRepository flexWorkSettingRepo;

	/** The flow work setting repo. */
	@Inject
	private FlowWorkSettingRepository flowWorkSettingRepo;

	/** The diff time work setting repo. */
	@Inject
	private DiffTimeWorkSettingRepository diffTimeWorkSettingRepo;

	/** The calculate time diff service. */
	@Inject
	private CalculateTimeDiffService calculateTimeDiffService;

	/** The diff time correction service. */
	@Inject
	private DiffTimeCorrectionService diffTimeCorrectionService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService#
	 * getStampReflectTimezone(java.lang.String, java.lang.String)
	 */
	@Override
	public List<StampReflectTimezone> getStampReflectTimezone(String companyId, String workTimeCode, Integer start1,
			Integer start2, Integer end1, Integer end2) {

		Optional<WorkTimeSetting> optWorkTimeSetting = workTimeSettingRepo.findByCode(companyId, workTimeCode);
		if (!optWorkTimeSetting.isPresent()) {
			return Collections.emptyList();
		}
		PredetemineTimeSetting predTime = this.predetemineTimeRepo.findByWorkTimeCode(companyId, workTimeCode).get();

		if (optWorkTimeSetting.get().getWorkTimeDivision().getWorkTimeDailyAtr() == WorkTimeDailyAtr.REGULAR_WORK) {
			switch (optWorkTimeSetting.get().getWorkTimeDivision().getWorkTimeMethodSet()) {
			case FIXED_WORK:
				return this.getFromFixed(companyId, workTimeCode);
			case DIFFTIME_WORK:
				return this.getFromDiffTime(companyId, workTimeCode, predTime, start1);
			case FLOW_WORK:
				return this.getFromFlow(companyId, workTimeCode, predTime, start2);
			default:
				throw new RuntimeException("No such enum value");
			}
		}

		return this.getFromFlex(companyId, workTimeCode);

	}

	/**
	 * Gets the from fixed.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @return the from fixed
	 */
	// 固定勤務設定から、打刻反映時間帯を取得
	private List<StampReflectTimezone> getFromFixed(String companyId, String workTimeCode) {
		FixedWorkSetting fixedWorkSetting = this.fixedWorkSettingRepo.findByKey(companyId, workTimeCode).get();
		return fixedWorkSetting.getLstStampReflectTimezone();
	}

	/**
	 * Gets the from diff time.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @param predtime the predtime
	 * @param start1 the start 1
	 * @return the from diff time
	 */
	// 時差勤務設定から、打刻反映時間帯を取得
	private List<StampReflectTimezone> getFromDiffTime(String companyId, String workTimeCode,
			PredetemineTimeSetting predtime, Integer start1) {
		DiffTimeWorkSetting diffTimeWorkSetting = this.diffTimeWorkSettingRepo.find(companyId, workTimeCode).get();

		// TODO: get dailyWork
		JoggingWorkTime jwt = this.calculateTimeDiffService.caculateJoggingWorkTime(new TimeWithDayAttr(start1),
				new DailyWork(), predtime.getPrescribedTimezoneSetting());

		// 時刻補正
		this.diffTimeCorrectionService.correction(jwt, diffTimeWorkSetting, predtime);

		return diffTimeWorkSetting.getStampReflectTimezone().getStampReflectTimezone();
	}

	/**
	 * Gets the from flow.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @param predTime the pred time
	 * @param start2 the start 2
	 * @return the from flow
	 */
	// 流動勤務設定から、打刻反映時間帯を取得
	private List<StampReflectTimezone> getFromFlow(String companyId, String workTimeCode,
			PredetemineTimeSetting predTime, Integer start2) {
		FlowWorkSetting flowWorkSetting = this.flowWorkSettingRepo.find(companyId, workTimeCode).get();

		// use shift 2
		if (predTime.getPrescribedTimezoneSetting().isUseShiftTwo()) {
			ArrayList<StampReflectTimezone> rs = new ArrayList<StampReflectTimezone>();

			// ２回目勤務の打刻反映時間帯の開始時刻を計算
			int start = start2
					- flowWorkSetting.getStampReflectTimezone().getTwoTimesWorkReflectBasicTime().valueAsMinutes();

			// 打刻反映時間帯でループ
			flowWorkSetting.getStampReflectTimezone().getStampReflectTimezones().forEach(item -> {
				// start = start, end = 2
				StampReflectTimezone v = StampReflectTimezone.builder().workNo(new WorkNo(1))
						.classification(item.getClassification()).startTime(item.getStartTime())
						.endTime(new TimeWithDayAttr(start)).build();

				// start = 2, end = end
				StampReflectTimezone v2 = StampReflectTimezone.builder().workNo(new WorkNo(2))
						.classification(item.getClassification()).startTime(new TimeWithDayAttr(start))
						.endTime(item.getEndTime()).build();
				rs.add(v);
				rs.add(v2);
			});

			return rs;
		}

		// not use shift 2
		return flowWorkSetting.getStampReflectTimezone().getStampReflectTimezones();
	}

	/**
	 * Gets the from flex.
	 *
	 * @param companyId the company id
	 * @param workTimeCode the work time code
	 * @return the from flex
	 */
	// フレックス勤務設定から、打刻反映範囲を取得
	private List<StampReflectTimezone> getFromFlex(String companyId, String workTimeCode) {
		FlexWorkSetting flexWorkSetting = this.flexWorkSettingRepo.find(companyId, workTimeCode).get();
		return flexWorkSetting.getLstStampReflectTimezone();
	}
}
