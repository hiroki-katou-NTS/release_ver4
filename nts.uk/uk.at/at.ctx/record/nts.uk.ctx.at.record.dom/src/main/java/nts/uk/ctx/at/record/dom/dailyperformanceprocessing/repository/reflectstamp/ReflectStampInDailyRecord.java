package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.reflectstamp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyresultcreationprocess.creationprocess.creationclass.dailywork.ReflectStampDailyAttdOutput;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.ContractCode;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.ReflectDataStampDailyService;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.ReflectDateAndEmpID;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.StampDataReflectResult;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * @author thanh_nx
 *
 *         打刻を日別実績へ反映する
 */
public class ReflectStampInDailyRecord {

	public static Optional<StampDataReflectResult> reflect(Require require, String cid, String contractCode,
			Optional<Stamp> stamp) {
		// if(!打刻.isPresent())
		if (!stamp.isPresent())
			return Optional.empty();
		// $反映する日と社員ID
		Optional<ReflectDateAndEmpID> reflectDateAndEmpID = ReflectDataStampDailyService.getJudgment(require, cid,
				new ContractCode(contractCode), stamp.get());
		// <>$反映する日と社員ID.isPresent()
		if (!reflectDateAndEmpID.isPresent())
			return Optional.empty();
		Optional<ReflectStampDailyAttdOutput> stampDailyAttdOutput = require.createDailyDomAndReflectStamp(cid,
				reflectDateAndEmpID.get().getSid(), reflectDateAndEmpID.get().getDate(), stamp.get());

		if (!stampDailyAttdOutput.isPresent())
			return Optional.empty();

		IntegrationOfDaily domainDaily = stampDailyAttdOutput.get().getIntegrationOfDaily();
		// $手修正項目
		List<Integer> attendanceItemIdList = domainDaily.getEditState().stream()
				.map(editState -> editState.getAttendanceItemId()).distinct().collect(Collectors.toList());
		DailyRecordToAttendanceItemConverter converter = require.createDailyConverter().setData(domainDaily)
				.completed();
		List<ItemValue> listItemValue = converter.convert(attendanceItemIdList);

		if (!attendanceItemIdList.isEmpty()) {
			// 手修正項目のデータを元に戻す
			domainDaily = require.restoreData(converter, domainDaily, listItemValue);
		}

		// DS_日別勤怠を補正する.補正する
		domainDaily = require.process(domainDaily, stampDailyAttdOutput.get().getChangeDailyAttendance());

		// $計算後の日別実績
		List<IntegrationOfDaily> domainDailys = require.calculatePassCompanySetting(cid, Arrays.asList(domainDaily),
				ExecutionType.NORMAL_EXECUTION);

		if (domainDailys.isEmpty())
			return Optional.empty();
		IntegrationOfDaily domainDailyResult = domainDailys.get(0);
		AtomTask task = AtomTask.of(() -> {
			//日別実績を更新する
			require.addAllDomain(domainDailyResult);
			//暫定データの登録
			require.registerDateChange(cid, reflectDateAndEmpID.get().getSid(),
					Arrays.asList(reflectDateAndEmpID.get().getDate()));

		});
		return Optional.of(new StampDataReflectResult(Optional.of(reflectDateAndEmpID.get().getDate()), task));
	}

	public static interface Require extends ReflectDataStampDailyService.Require {

		DailyRecordToAttendanceItemConverter createDailyConverter();

		// [R-1] 手修正項目のデータを元に戻す
		IntegrationOfDaily restoreData(DailyRecordToAttendanceItemConverter converter,
				IntegrationOfDaily integrationOfDaily, List<ItemValue> listItemValue);

		// ICorrectionAttendanceRule
		public IntegrationOfDaily process(IntegrationOfDaily domainDaily, ChangeDailyAttendance changeAtt);

		// [R-2] 計算処理
		// CalculateDailyRecordServiceCenter
		List<IntegrationOfDaily> calculatePassCompanySetting(String cid, List<IntegrationOfDaily> integrationOfDaily,
				ExecutionType reCalcAtr);

		// [R-3] 暫定データの登録
		// InterimRemainDataMngRegisterDateChange
		public void registerDateChange(String cid, String sid, List<GeneralDate> lstDate);

		// [R-7] 日別実績を更新する
		// DailyRecordAdUpService - 日別実績を登録する
		void addAllDomain(IntegrationOfDaily domain);
	}

}
