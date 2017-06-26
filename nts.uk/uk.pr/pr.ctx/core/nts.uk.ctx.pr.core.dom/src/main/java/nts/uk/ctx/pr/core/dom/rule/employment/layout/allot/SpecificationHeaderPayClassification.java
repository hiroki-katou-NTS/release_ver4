package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;
/**
 * 
 */
import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;

public class SpecificationHeaderPayClassification extends AggregateRoot {
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

	public SpecificationHeaderPayClassification(CompanyCode companyCode, String historyId, YearMonth startYM,
			YearMonth endYm) {
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.startYM = startYM;
		this.endYm = endYm;
	}
	
	/**
	 * Create from Java Type
	 * @return SpecificationHeaderDepartment
	 */
	public static SpecificationHeaderPayClassification createFromJavaType(String companyCode, String historyId, Integer startYM, 
			Integer endYm) {
		if (historyId.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("明細書名が入力されていません。"));
		}
		
		return new SpecificationHeaderPayClassification(new CompanyCode(companyCode),
				historyId, 
				new YearMonth(startYM),
				new YearMonth(endYm)
				);
	}

	
}
