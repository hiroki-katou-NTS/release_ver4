package nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto;

//import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.childcareschedule.ChildCareAtr;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Getter
@Setter
public class ShortWorkTimeDto {
	/** The employee id. */
	private String employeeId;

	/** The period. */
	private DatePeriod period;

	/** The child care atr. */
	private ChildCareAtr childCareAtr;

	/** The time slot. */
	private java.util.List<ShortChildCareFrameDto> lstTimeSlot;
	
	@Value
	public static class List {
		private final java.util.List<ShortWorkTimeDto> list;
		

		/**
		 * 短時間勤務者を再作成するか判定する
		 * 
		 * @param empId
		 * @param targetDate
		 * @param reShortTermEmp
		 * @return
		 */
		public boolean isReShortTime(String empId, GeneralDate targetDate, Boolean reShortTermEmp) {
			// パラメータ.短時間勤務者を再作成を判定する
			if(!reShortTermEmp){
				return false;
			}
			// 「社員の短時間勤務一覧」からパラメータ.社員ID、対象日をもとに該当する短時間勤務を取得する
			boolean isSuccessProcess = this.acquireShortTimeWorkEmp(empId, targetDate);
			if (!isSuccessProcess) {
				// 取得できない場合
				return false;
			}

			return true;
		}
		
		/**
		 * アルゴリズム「社員の短時間勤務を取得」を実行する
		 * 
		 * @param employeeId
		 * @param targetDate
		 * @return true: success 終了状態：成功 false: fail
		 */
		private boolean acquireShortTimeWorkEmp(String employeeId, GeneralDate targetDate) {
			// EA修正履歴 No2211
			Optional<ShortWorkTimeDto> optionalShortTime = this.list.stream()
					.filter(x -> (x.getEmployeeId().equals(employeeId) && x.getPeriod().contains(targetDate))).findFirst();
			if (!optionalShortTime.isPresent()) {
				return false;
			}

			return true;
		}
	}
}
