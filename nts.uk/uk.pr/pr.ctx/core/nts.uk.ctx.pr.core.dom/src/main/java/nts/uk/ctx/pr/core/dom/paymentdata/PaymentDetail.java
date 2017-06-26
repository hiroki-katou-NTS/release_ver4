package nts.uk.ctx.pr.core.dom.paymentdata;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.enums.SparePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.shr.com.primitive.PersonId;
import nts.uk.shr.com.primitive.sample.ProcessingNo;

public class PaymentDetail extends AggregateRoot {
	@Getter
	private CompanyCode companyCode;

	@Getter
	private PersonId personId;

	@Getter
	private ProcessingNo processingNo;

	@Getter
	private YearMonth processingYM;

	@Getter
	private PayBonusAtr payBonusAtr;

	@Getter
	private SparePayAtr sparePayAtr;

	@Getter
	private CategoryAtr categoryAtr;

	@Getter
	private ItemCode itemCode;

	@Getter
	private BigDecimal value;

	public PaymentDetail(CompanyCode companyCode, PersonId personId, ProcessingNo processingNo, YearMonth processingYM,
			PayBonusAtr payBonusAtr, SparePayAtr sparePayAtr, CategoryAtr categoryAtr, ItemCode itemCode,
			BigDecimal value) {
		super();
		this.companyCode = companyCode;
		this.personId = personId;
		this.processingNo = processingNo;
		this.processingYM = processingYM;
		this.payBonusAtr = payBonusAtr;
		this.sparePayAtr = sparePayAtr;
		this.categoryAtr = categoryAtr;
		this.itemCode = itemCode;
		this.value = value;
	}

	public static PaymentDetail createFromJavaType(String companyCode, String personId, int processingNo,
			int payBonusAtr, int processingYM, int sparePayAtr, int categoryAtr, String itemCode,
			BigDecimal value) {

		return new PaymentDetail(new CompanyCode(companyCode), new PersonId(personId),
				new ProcessingNo(processingNo),
				new YearMonth(processingYM), EnumAdaptor.valueOf(payBonusAtr, PayBonusAtr.class), EnumAdaptor.valueOf(sparePayAtr, SparePayAtr.class),
				EnumAdaptor.valueOf(categoryAtr, CategoryAtr.class), new ItemCode(itemCode), value);
	}
}
