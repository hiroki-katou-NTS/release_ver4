package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.closure.ClosureMonth;
/**
 * 就業実績確認状態
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
@Builder
public class AppRootConfirm {
	
	/**
	 * 承認ルート中間データID
	 */
	private String rootID;
	
	/**
	 * 会社ID
	 */
	private String companyID;
	
	/**
	 * 対象者
	 */
	private String employeeID;
	
	/**
	 * 対象日
	 */
	private GeneralDate recordDate;
	
	/**
	 * ルート種類
	 */
	private RecordRootType rootType;
	
	/**
	 * 承認済フェーズ
	 */
	@Setter
	private List<AppPhaseConfirm> listAppPhase;
	
	/**
	 * 年月
	 */
	private Optional<YearMonth> yearMonth;
	
	/**
	 * 締めID
	 */
	private Optional<Integer> closureID;
	
	/**
	 * 締め日
	 */
	private Optional<ClosureDate> closureDate;
	
	
	public static AppRootConfirm dummy(String companyID, String employeeID, GeneralDate date, RecordRootType rootType) {
		return new AppRootConfirm(UUID.randomUUID().toString(), companyID, employeeID, date, rootType, new ArrayList<>(),
				Optional.empty(), Optional.empty(), Optional.empty());
	}
	
	public static AppRootConfirm find(List<AppRootConfirm> source, String companyID, String employeeID, GeneralDate date, RecordRootType rootType) {
		return source.stream()
				.filter(s -> s.getCompanyID().equals(companyID)
						&& s.getEmployeeID().equals(employeeID)
						&& s.getRecordDate().equals(date)
						&& s.getRootType().equals(rootType))
				.findFirst()
				.orElseGet(() -> AppRootConfirm.dummy(companyID, employeeID, date, rootType));
	}
	
	public ClosureMonth getClosureMonth() {
		return new ClosureMonth(yearMonth.get(), closureID.get(), closureDate.get());
	}
}
