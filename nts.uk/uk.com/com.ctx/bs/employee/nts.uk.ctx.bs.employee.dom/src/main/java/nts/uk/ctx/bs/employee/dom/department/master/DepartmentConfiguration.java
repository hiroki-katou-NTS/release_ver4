//package nts.uk.ctx.bs.employee.dom.department.master;
//
//import java.util.List;
//
//import lombok.AllArgsConstructor;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import nts.arc.layer.dom.AggregateRoot;
//import nts.arc.time.GeneralDate;
//import nts.uk.shr.com.history.DateHistoryItem;
//import nts.uk.shr.com.history.strategic.ContinuousHistory;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;
//
///**
// * 
// * @author HungTT - 部門構成
// *
// */
//
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = false)
//public class DepartmentConfiguration extends AggregateRoot
//		implements ContinuousHistory<DateHistoryItem, DatePeriod, GeneralDate> {
//
//	/**
//	 * 会社ID
//	 */
//	@Getter
//	private String companyId;
//
//	/**
//	 * 部門履歴
//	 */
//	private List<DateHistoryItem> histories;
//
//	@Override
//	public List<DateHistoryItem> items() {
//		return this.histories;
//	}
//
//}
