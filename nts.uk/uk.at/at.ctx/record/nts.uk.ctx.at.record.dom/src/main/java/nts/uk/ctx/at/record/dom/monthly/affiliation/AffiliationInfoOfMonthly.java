package nts.uk.ctx.at.record.dom.monthly.affiliation;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * 月別実績の所属情報
 * @author shuichu_ishida
 */
@Getter
public class AffiliationInfoOfMonthly extends AggregateRoot {

	/** 社員ID */
	private final String employeeId;
	/** 年月 */
	private final YearMonth yearMonth;
	/** 締めID */
	private final ClosureId closureId;
	/** 締め日付 */
	private final ClosureDate closureDate;
	
	/** 月初の情報 */
	private AggregateAffiliationInfo firstInfo;
	/** 月末の情報 */
	private AggregateAffiliationInfo lastInfo;

	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 */
	public AffiliationInfoOfMonthly(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate){
		
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		
		this.firstInfo = new AggregateAffiliationInfo();
		this.lastInfo = new AggregateAffiliationInfo();
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param firstInfo 月初の情報
	 * @param lastInfo 月末の情報
	 * @return 月別実績の所属情報
	 */
	public static AffiliationInfoOfMonthly of(
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			AggregateAffiliationInfo firstInfo,
			AggregateAffiliationInfo lastInfo){
		
		AffiliationInfoOfMonthly domain = new AffiliationInfoOfMonthly(
				employeeId, yearMonth, closureId, closureDate);
		domain.firstInfo = firstInfo;
		domain.lastInfo = lastInfo;
		return domain;
	}
}
