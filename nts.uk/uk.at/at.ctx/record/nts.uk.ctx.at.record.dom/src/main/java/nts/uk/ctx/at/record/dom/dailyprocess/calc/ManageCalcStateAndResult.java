package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.attendanceitem.util.AttendanceItemConvertFactory;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;

/**
 * 計算内部で使用する処理計算完了か、未完了かと実績データを保持するクラス
 * @author keisuke_hoshina
 *
 */
@Getter
public class ManageCalcStateAndResult {

	//計算処理が実行された
	boolean isCalc;
	
	@Setter
	IntegrationOfDaily integrationOfDaily;
	
	/**
	 * @param isCalc 計算したかどうかのboolean true = 計算できた。
	 * Constructor 
	 */
	private ManageCalcStateAndResult(boolean isCalc, IntegrationOfDaily integrationOfDaily) {
		super();
		this.isCalc = isCalc;
		this.integrationOfDaily = integrationOfDaily;
	}
	
	/**
	 * 計算処理を実行した時のConstructor
	 * @param integrationOfDaily
	 * @return
	 */
	public static ManageCalcStateAndResult successCalc(IntegrationOfDaily integrationOfDaily) {
		return new ManageCalcStateAndResult(true, integrationOfDaily);
	}
	
	/**
	 * 計算処理をしなかったときのConstructor
	 * @param integrationOfDaily
	 * @return
	 */
	public static ManageCalcStateAndResult failCalc(IntegrationOfDaily integrationOfDaily, AttendanceItemConvertFactory attendanceItemConvertFactory) {
		return ManageCalcStateAndResult.noCalcProc(integrationOfDaily, attendanceItemConvertFactory);
	}	
	
	/**
	 * 計算処理をしなかったときのConstructor
	 * @param integrationOfDaily 日別実績work
	 * @param attendanceItemConvertFactory 勤怠項目コンバータ作成I/F
	 * @return 計算結果管理
	 */
	public static ManageCalcStateAndResult noCalcProc(IntegrationOfDaily integrationOfDaily, AttendanceItemConvertFactory attendanceItemConvertFactory) {
		if (integrationOfDaily.getWorkInformation().getEmployeeId() == null
				|| integrationOfDaily.getWorkInformation().getYmd() == null)
			return new ManageCalcStateAndResult(false, integrationOfDaily);
		// // 編集状態を取得（日別実績の編集状態が持つ勤怠項目IDのみのList作成）
		List<Integer> attendanceItemIdList = integrationOfDaily.getEditState().stream()
				.map(editState -> editState.getAttendanceItemId()).distinct().collect(Collectors.toList());

		Optional<AttendanceTimeOfDailyPerformance> peform = Optional.of(AttendanceTimeOfDailyPerformance
				.allZeroValue(integrationOfDaily.getWorkInformation().getEmployeeId(),
						integrationOfDaily.getWorkInformation().getYmd()));

		DailyRecordToAttendanceItemConverter converterForAllZero = attendanceItemConvertFactory
				.createDailyConverter();
		DailyRecordToAttendanceItemConverter beforDailyRecordDto = converterForAllZero.setData(integrationOfDaily);
		// 複製に対してsetしないと引数：integrationOfDailyが書き換わる(※参照型)
		val recordAllZeroValueIntegration = beforDailyRecordDto.toDomain();
		recordAllZeroValueIntegration.setAttendanceTimeOfDailyPerformance(peform);
		List<ItemValue> itemValueList = Collections.emptyList();
		if (!attendanceItemIdList.isEmpty()) {

			itemValueList = beforDailyRecordDto.convert(attendanceItemIdList);
			DailyRecordToAttendanceItemConverter afterDailyRecordDto = converterForAllZero
					.setData(recordAllZeroValueIntegration);
			afterDailyRecordDto.merge(itemValueList);

			// 手修正された項目の値を計算前に戻す
			integrationOfDaily = afterDailyRecordDto.toDomain();
		} else {
			integrationOfDaily = recordAllZeroValueIntegration;
		}
		
		return new ManageCalcStateAndResult(false, integrationOfDaily);
	}	
}
