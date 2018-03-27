/**
 * 9:37:57 AM Jul 21, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ErrorAlarmClassification;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordName;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;

/**
 * @author hungnm 勤務実績のエラーアラーム
 */
@Getter
public class ErrorAlarmWorkRecord extends AggregateRoot {

	/* 会社ID */
	private String companyId;

	/* コード */
	private ErrorAlarmWorkRecordCode code;

	/* 名称 */
	private ErrorAlarmWorkRecordName name;

	/* システム固定とする */
	private Boolean fixedAtr;

	/* 使用する */
	private Boolean useAtr;

	/* 区分 */
	private ErrorAlarmClassification typeAtr;

	/* メッセージ */
	private ErrorAlarmMessage message;

	/* エラーアラームを解除できる */
	private Boolean cancelableAtr;

	/* エラー表示項目 */
	private BigDecimal errorDisplayItem;

	/* エラー解除ロールID */
	private String cancelRoleId;

	private List<Integer> lstApplication;

	private String errorAlarmCheckID;
	
	@Setter
	private ErrorAlarmCondition errorAlarmCondition;

	/* Constructor */
	private ErrorAlarmWorkRecord() {
		super();
	}

	/* Constructor */
	private ErrorAlarmWorkRecord(String companyId, ErrorAlarmWorkRecordCode code, ErrorAlarmWorkRecordName name,
			boolean fixedAtr, boolean useAtr, ErrorAlarmClassification typeAtr, ErrorAlarmMessage message,
			boolean cancelableAtr, BigDecimal errorDisplayItem, List<Integer> lstApplication,
			String errorAlarmCheckID) {
		super();
		this.companyId = companyId;
		this.code = code;
		this.name = name;
		this.fixedAtr = fixedAtr;
		this.useAtr = useAtr;
		this.typeAtr = typeAtr;
		this.message = message;
		this.cancelableAtr = cancelableAtr;
		this.errorDisplayItem = errorDisplayItem;
		this.lstApplication = lstApplication;
		this.errorAlarmCheckID = errorAlarmCheckID;
	}

	/**
	 * Create ErrorAlarmWorkRecord from Java type
	 * 
	 * @param
	 * @return ErrorAlarmWorkRecord
	 **/
	public static ErrorAlarmWorkRecord createFromJavaType(String companyId, String code, String name, boolean fixedAtr,
			boolean useAtr, int typeAtr, boolean boldAtr, String messageColor, boolean cancelableAtr,
			BigDecimal errorDisplayItem, List<Integer> lstApplication, String errorAlarmCheckID) {
		ErrorAlarmWorkRecord errorAlarmWorkRecord = new ErrorAlarmWorkRecord(companyId,
				new ErrorAlarmWorkRecordCode(code), new ErrorAlarmWorkRecordName(name), fixedAtr, useAtr,
				EnumAdaptor.valueOf(typeAtr, ErrorAlarmClassification.class),
				ErrorAlarmMessage.createFromJavaType(boldAtr, messageColor), cancelableAtr, errorDisplayItem,
				lstApplication, errorAlarmCheckID);
		return errorAlarmWorkRecord;
	}

	/**
	 * Init
	 * 
	 * @param
	 * @return ErrorAlarmWorkRecord
	 **/
	public static ErrorAlarmWorkRecord init(String companyId, String code, String name, boolean fixedAtr,
			boolean useAtr, int typeAtr, boolean boldAtr, String messageColor, boolean cancelableAtr,
			BigDecimal errorDisplayItem, List<Integer> lstApplication) {
		ErrorAlarmWorkRecord errorAlarmWorkRecord = new ErrorAlarmWorkRecord(companyId,
				code.length() < 4 ? new ErrorAlarmWorkRecordCode("U" + code) : new ErrorAlarmWorkRecordCode(code),
				new ErrorAlarmWorkRecordName(name), fixedAtr, useAtr,
				EnumAdaptor.valueOf(typeAtr, ErrorAlarmClassification.class),
				ErrorAlarmMessage.createFromJavaType(boldAtr, messageColor), cancelableAtr, errorDisplayItem,
				lstApplication, IdentifierUtil.randomUniqueId());
		return errorAlarmWorkRecord;
	}

	public void setCheckId(String errorAlarmCheckID) {
		this.errorAlarmCheckID = errorAlarmCheckID;
	}
	
//	/**
//	 * システム固定エラーチェック
//	 * @return
//	 */
//	public EmployeeDailyPerError systemErrorCheck(IntegrationOfDaily integrationOfDaily) {
//		
//		SystemFixedErrorAlarm fixedErrorAlarmCode = SystemFixedErrorAlarm.valueOf(this.code.toString());
//		switch(fixedErrorAlarmCode) {
//			//遅刻
//			case LATE:
//				break;
//			//早退
//			case LEAVE_EARLY:
//				break;
//			//事前残業申請超過
//			case PRE_OVERTIME_APP_EXCESS:
//				break;
//			//事前休出申請超過
//			case PRE_HOLIDAYWORK_APP_EXCESS:
//				break;
//			//事前フレックス申請超過
//			case PRE_FLEX_APP_EXCESS:
//				break;
//			//事前深夜申請超過
//			case PRE_MIDNIGHT_EXCESS:
//				break;
//			//残業時間実績超過
//			case OVER_TIME_EXCESS:
//				checkOverTimeExcess(integrationOfDaily);
//				break;
//			//休出時間実績超過
//			case REST_TIME_EXCESS:
//				break;
//			//フレックス時間実績超過
//			case FLEX_OVER_TIME:
//				break;
//			//深夜時間実績超過
//			case MIDNIGHT_EXCESS:
//				break;
//			
//			//乖離時間のエラー	
//			case ERROR_OF_DIVERGENCE_TIME:
//				break;
//			//乖離時間のアラーム
//			case ALARM_OF_DIVERGENCE_TIME:
//				break;
//			//それ以外ルート
//			default:
//				
//		}
//	}
//
//	/**
//	 * 残業時間実装超過のチェック
//	 * @param integrationOfDaily
//	 */
//	private void checkOverTimeExcess(IntegrationOfDaily integrationOfDaily) {
//		if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
//			if(integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
//				integrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get();
//			}
//		}
//	}
}

