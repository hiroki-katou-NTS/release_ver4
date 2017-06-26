package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import java.time.LocalDate;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class SpecificationHeaderDepartment extends AggregateRoot {
	
	/** code */
	@Getter
	private CompanyCode companyCode;
	/** history Id */
	@Getter
	private String historyId;

	/** 開始年月 */
	@Getter
	@Setter
	private YearMonth startYM;

	/** 終了年月 */
	@Getter
	@Setter
	private YearMonth endYm;
	
	/** 部門基準日*/
	@Getter
	private LocalDate secBaseDate;

	public SpecificationHeaderDepartment(CompanyCode companyCode, String historyId, YearMonth startYM, YearMonth endYm,
			LocalDate secBaseDate) {
		super();
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.startYM = startYM;
		this.endYm = endYm;
		this.secBaseDate = secBaseDate;
	}
	/**
	 * Create from Java Type
	 * @return SpecificationHeaderDepartment
	 */
	public static SpecificationHeaderDepartment createFromJavaType(String companyCode, String historyId, Integer startYM, Integer endYm,
			LocalDate secBaseDate) {
		if (historyId.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}
		
		return new SpecificationHeaderDepartment(new CompanyCode(companyCode),
				historyId, 
				new YearMonth(startYM),
				new YearMonth(endYm),
				secBaseDate
				);
	}
}
