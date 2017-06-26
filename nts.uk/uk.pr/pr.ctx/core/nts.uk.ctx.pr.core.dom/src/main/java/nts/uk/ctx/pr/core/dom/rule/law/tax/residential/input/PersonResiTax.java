package nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.paymentdata.residence.ResidenceCode;
import nts.uk.shr.com.primitive.PersonId;
/**
 * 
 * @author sonnh1
 *
 */
public class PersonResiTax extends AggregateRoot {
	@Getter
	private CompanyCode companyCode;
	@Getter
	private PersonId personId;
	@Getter
	private YearKey yearKey;
	@Getter
	private ResidenceCode residenceCode;
	@Getter
	private ResidenceTaxBn residenceTaxBn;
	@Getter
	private ResidenceTaxAve residenceTaxAve;
	@Getter
	private List<ResidenceTax> residenceTax;
	@Getter
	private ResidenceTaxLumpAtr residenceTaxLumpAtr;
	@Getter
	private ResidenceTaxLumpYm residenceTaxLumpYm;
	@Getter
	private ResidenceTaxLevyAtr residenceTaxLevyAtr;

	@Override
	public void validate() {
		super.validate();
		// custom validate
		// throw new BusinessException("");
	}

	public PersonResiTax(CompanyCode companyCode, PersonId personId, YearKey yearKey, ResidenceCode residenceCode,
			ResidenceTaxBn residenceTaxBn, ResidenceTaxAve residenceTaxAve, ResidenceTaxLumpAtr residenceTaxLumpAtr,
			ResidenceTaxLumpYm residenceTaxLumpYm, ResidenceTaxLevyAtr residenceTaxLevyAtr) {
		super();
		this.companyCode = companyCode;
		this.personId = personId;
		this.yearKey = yearKey;
		this.residenceCode = residenceCode;
		this.residenceTaxBn = residenceTaxBn;
		this.residenceTaxAve = residenceTaxAve;
		this.residenceTaxLumpAtr = residenceTaxLumpAtr;
		this.residenceTaxLumpYm = residenceTaxLumpYm;
		this.residenceTaxLevyAtr = residenceTaxLevyAtr;
	}

	public static PersonResiTax createFromJavaType(String companyCode, String personId, int yearKey,
			String residenceCode, BigDecimal residenceTaxBn, BigDecimal residenceTaxAve, int residenceTaxLumpAtr,
			int residenceTaxLumpYm, int residenceTaxLevyAtr) {
		return new PersonResiTax(new CompanyCode(companyCode), new PersonId(personId), new YearKey(yearKey),
				new ResidenceCode(residenceCode), new ResidenceTaxBn(residenceTaxBn),
				new ResidenceTaxAve(residenceTaxAve),
				EnumAdaptor.valueOf(residenceTaxLumpAtr, ResidenceTaxLumpAtr.class),
				new ResidenceTaxLumpYm(residenceTaxLumpYm),
				EnumAdaptor.valueOf(residenceTaxLevyAtr, ResidenceTaxLevyAtr.class));
	}

	public void createResidenceTaxFromJavaType(int month1, BigDecimal value1, int month2, BigDecimal value2, int month3,
			BigDecimal value3, int month4, BigDecimal value4, int month5, BigDecimal value5, int month6,
			BigDecimal value6, int month7, BigDecimal value7, int month8, BigDecimal value8, int month9,
			BigDecimal value9, int month10, BigDecimal value10, int month11, BigDecimal value11, int month12,
			BigDecimal value12) {
		this.residenceTax = Arrays.asList(new ResidenceTax(month1, new MonthlyResidenceTax(value1)),
				new ResidenceTax(month2, new MonthlyResidenceTax(value2)),
				new ResidenceTax(month3, new MonthlyResidenceTax(value3)),
				new ResidenceTax(month4, new MonthlyResidenceTax(value4)),
				new ResidenceTax(month5, new MonthlyResidenceTax(value5)),
				new ResidenceTax(month6, new MonthlyResidenceTax(value6)),
				new ResidenceTax(month7, new MonthlyResidenceTax(value7)),
				new ResidenceTax(month8, new MonthlyResidenceTax(value8)),
				new ResidenceTax(month9, new MonthlyResidenceTax(value9)),
				new ResidenceTax(month10, new MonthlyResidenceTax(value10)),
				new ResidenceTax(month11, new MonthlyResidenceTax(value11)),
				new ResidenceTax(month12, new MonthlyResidenceTax(value12)));
	}
}
