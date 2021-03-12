package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.compensatoryholiday.updateremainnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.task.tran.AtomTask;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.param.UnbalanceCompensation;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.ManagementDataDaysAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.ManagementDataHours;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.ManagementDataRemainUnit;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.UnbalanceVacation;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.CompensatoryDayOffManaData;
import nts.uk.ctx.at.shared.dom.remainingnumber.subhdmana.LeaveManagementData;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author HungTT - <<Work>> 代休残数更新
 *
 */
public class RemainCompensatoryHolidayUpdating {

	// 代休残数更新
	public static AtomTask updateRemainCompensatoryHoliday(RequireM5 require,  
			List<AccumulationAbsenceDetail> lstDetailData, 
			AggrPeriodEachActualClosure period, String empId) {
		
		String companyId = AppContexts.user().companyId();
		
		return AtomTask.of(() -> {})
				.then(deleteLeaveManaData(require, period.getPeriod(), empId))
				.then(updateLeaveMngData(require, companyId, lstDetailData))
				.then(deleteDayOffManaData(require, period.getPeriod(), empId))
				.then(updateCompensatoryDayData(require, companyId, lstDetailData));
	}
	
	// ドメインモデル 「休出管理データ」 を削除する
	public static AtomTask deleteLeaveManaData(RequireM4 require, DatePeriod period, String empId){
		AtomTask atomTask = AtomTask.of(() -> {});
		
		List<LeaveManagementData> managementDatas = require.leaveManagementData(empId, false, period);
		
		if (CollectionUtil.isEmpty(managementDatas))
			return atomTask;
		
		List<String> leaveManaId = managementDatas.stream().map(r -> r.getID()).collect(Collectors.toList());
		
		return atomTask.then(() -> require.deleteLeaveManagementData(leaveManaId));
	}
	
	// ドメインモデル 「代休管理データ」 を削除する
	public static AtomTask deleteDayOffManaData(RequireM3 require, DatePeriod period, String empId){
		AtomTask atomTask = AtomTask.of(() -> {});
		
		List<CompensatoryDayOffManaData> dayOffDatas = require.compensatoryDayOffManaData(empId, false, period);
		
		if (CollectionUtil.isEmpty(dayOffDatas))
			return atomTask;
		
		List<String> dayOffId = dayOffDatas.stream().map(r -> r.getComDayOffID()).collect(Collectors.toList());
		
		return atomTask.then(() -> require.deleteCompensatoryDayOffManaData(dayOffId));
	}

	// 休出管理データの更新
	private static AtomTask updateLeaveMngData(RequireM2 require,
			String companyId, List<AccumulationAbsenceDetail> lstDetailData) {
		
		List<AtomTask> atomTask = new ArrayList<>();
		
		if (CollectionUtil.isEmpty(lstDetailData))
			return AtomTask.bundle(atomTask);
		
		lstDetailData = lstDetailData.stream()
				.filter(a -> a.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE)
				.collect(Collectors.toList());
		
		if (CollectionUtil.isEmpty(lstDetailData))
			return AtomTask.bundle(atomTask);
		
		for (AccumulationAbsenceDetail data : lstDetailData) {
			//BreakDayOffDetail
			UnbalanceVacation unUserOfBreak = (UnbalanceVacation)data;
			Optional<LeaveManagementData> optLeaveMngData = require.leaveManagementData(unUserOfBreak.getManageId());
			if (optLeaveMngData.isPresent()) {
				// update
				LeaveManagementData leaveMng = new LeaveManagementData(optLeaveMngData.get().getID(),
						optLeaveMngData.get().getCID(), optLeaveMngData.get().getSID(),
						optLeaveMngData.get().getComDayOffDate(), unUserOfBreak.getDeadline(),
						new ManagementDataDaysAtr(unUserOfBreak.getNumberOccurren().getDay().v()),
						new ManagementDataHours(unUserOfBreak.getNumberOccurren().getTime().map(x -> x.v()).orElse(0)),
						new ManagementDataRemainUnit(unUserOfBreak.getUnbalanceNumber().getDay().v()),
						new ManagementDataHours(unUserOfBreak.getUnbalanceNumber().getTime().map(x -> x.v()).orElse(0)), 
						unUserOfBreak.getDigestionCate(),
						new AttendanceTime(unUserOfBreak.getTimeOneDay().v()),
						new AttendanceTime(unUserOfBreak.getTimeHalfDay().v()), 
						unUserOfBreak.getExtinctionDate());
				atomTask.add(AtomTask.of(() -> require.updateLeaveManagementData(leaveMng)));
			} else {
				// insert
				LeaveManagementData leaveMng = new LeaveManagementData(unUserOfBreak.getManageId(), companyId,
						data.getEmployeeId(), data.getDateOccur(), unUserOfBreak.getDeadline(),
						new ManagementDataDaysAtr(unUserOfBreak.getNumberOccurren().getDay().v()),
						new ManagementDataHours(unUserOfBreak.getNumberOccurren().getTime().map(x -> x.v()).orElse(0)),
						new ManagementDataRemainUnit(unUserOfBreak.getUnbalanceNumber().getDay().v()),
						new ManagementDataHours(unUserOfBreak.getUnbalanceNumber().getTime().map(x -> x.v()).orElse(0)), 
						unUserOfBreak.getDigestionCate(),
						new AttendanceTime(unUserOfBreak.getTimeOneDay().v()),
						new AttendanceTime(unUserOfBreak.getTimeHalfDay().v()), 
						unUserOfBreak.getExtinctionDate());
				atomTask.add(AtomTask.of(() -> require.createLeaveManagementData(leaveMng)));
			}
		}
		
		return AtomTask.bundle(atomTask);
	}

	// 代休管理データの更新
	private static AtomTask updateCompensatoryDayData(RequireM1 require, 
			String companyId, List<AccumulationAbsenceDetail> lstDetailData) {
		List<AtomTask> atomTask = new ArrayList<>();
		
		if (CollectionUtil.isEmpty(lstDetailData))
			return AtomTask.bundle(atomTask);
		
		lstDetailData = lstDetailData.stream()
				.filter(a -> a.getOccurrentClass() == OccurrenceDigClass.DIGESTION)
				.collect(Collectors.toList());
		
		if (CollectionUtil.isEmpty(lstDetailData))
			return AtomTask.bundle(atomTask);
		for (AccumulationAbsenceDetail data : lstDetailData) {
			UnbalanceCompensation unOffsetOfDayoff = (UnbalanceCompensation)data;
			Optional<CompensatoryDayOffManaData> optCompensatoryData = require
					.compensatoryDayOffManaData(unOffsetOfDayoff.getManageId());
			
			if (optCompensatoryData.isPresent()) {
				// update
				CompensatoryDayOffManaData compDayOffData = new CompensatoryDayOffManaData(
						optCompensatoryData.get().getComDayOffID(), optCompensatoryData.get().getSID(),
						optCompensatoryData.get().getCID(), optCompensatoryData.get().getDayOffDate(),
						new ManagementDataDaysAtr(unOffsetOfDayoff.getNumberOccurren().getDay().v()),
						new ManagementDataHours(unOffsetOfDayoff.getNumberOccurren().getTime().map(x -> x.v()).orElse(0)),
						new ManagementDataRemainUnit(unOffsetOfDayoff.getUnbalanceNumber().getDay().v()),
						new ManagementDataHours(unOffsetOfDayoff.getUnbalanceNumber().getTime().map(x -> x.v()).orElse(0)));
				atomTask.add(AtomTask.of(() -> require.updateCompensatoryDayOffManaData(compDayOffData)));
			} else {
				// insert
				CompensatoryDayOffManaData compDayOffData = new CompensatoryDayOffManaData(
						unOffsetOfDayoff.getManageId(), unOffsetOfDayoff.getEmployeeId(), companyId, unOffsetOfDayoff.getDateOccur(),
						new ManagementDataDaysAtr(unOffsetOfDayoff.getNumberOccurren().getDay().v()),
						new ManagementDataHours(unOffsetOfDayoff.getNumberOccurren().getTime().map(x -> x.v()).orElse(0)),
						new ManagementDataRemainUnit(unOffsetOfDayoff.getUnbalanceNumber().getDay().v()),
						new ManagementDataHours(unOffsetOfDayoff.getUnbalanceNumber().getTime().map(x -> x.v()).orElse(0)));
				atomTask.add(AtomTask.of(() -> require.createCompensatoryDayOffManaData(compDayOffData)));
			}
		}
		
		return AtomTask.bundle(atomTask);
	}

	public static interface RequireM5 extends RequireM1, RequireM2, RequireM3, RequireM4 {}
	
	public static interface RequireM4 {
		
		List<LeaveManagementData> leaveManagementData(String sid, Boolean unknownDate, DatePeriod dayOff);
		
		void deleteLeaveManagementData(List<String> leaveManaId);
	}
	
	public static interface RequireM3 {
		
		List<CompensatoryDayOffManaData> compensatoryDayOffManaData(String sid, Boolean unknownDate, DatePeriod dayOff);
		
		void deleteCompensatoryDayOffManaData(List<String> dayOffId);
	}
	
	public static interface RequireM2 {
		
		Optional<LeaveManagementData> leaveManagementData(String comDayOffId);
		
		void updateLeaveManagementData(LeaveManagementData leaveMng);
		
		void createLeaveManagementData(LeaveManagementData leaveMng);
	}
	
	public static interface RequireM1 {
		
		Optional<CompensatoryDayOffManaData> compensatoryDayOffManaData(String comDayOffId);
		
		void updateCompensatoryDayOffManaData(CompensatoryDayOffManaData domain);
		
		void createCompensatoryDayOffManaData(CompensatoryDayOffManaData domain);
	}
}
