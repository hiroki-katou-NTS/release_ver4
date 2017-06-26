package nts.uk.ctx.pr.core.app.command.paymentdata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.pr.core.app.command.paymentdata.base.CategoryCommandBase;
import nts.uk.ctx.pr.core.app.command.paymentdata.base.LineCommandBase;
import nts.uk.ctx.pr.core.app.command.paymentdata.base.PaymentDataCommandBase;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.Payment;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;

/**
 * Command: insert payment data
 * 
 * @author vunv
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class InsertPaymentDataCommand extends PaymentDataCommandBase {
	/**
	 * Convert to domain object.
	 * 
	 * @return domain
	 */
	public Payment toDomain(String companyCode) {
		Payment domain = super.getPaymentHeader().toDomain(companyCode);
		
		for (CategoryCommandBase cate : super.getCategories()) {
			toDomainByCategory(domain, cate);
		}
		
		return domain;
	}

	/**
	 * convert data from command to value object detail item
	 * 
	 * @param items
	 * @return
	 */
	private static void toDomainByCategory(Payment domain, CategoryCommandBase categoryCmd) {

		CategoryAtr cateAtr = EnumAdaptor.valueOf(categoryCmd.getCategoryAttribute(), CategoryAtr.class);
		switch (cateAtr) {
		case PAYMENT:
			domain.setDetailPaymentItems(toDomainDetails(categoryCmd.getLines()));
			break;
		case DEDUCTION:
			domain.setDetailDeductionItems(toDomainDetails(categoryCmd.getLines()));
			break;
		case ARTICLES:
			domain.setDetailArticleItems(toDomainDetails(categoryCmd.getLines()));
			break;
		case PERSONAL_TIME:
			domain.setDetailPersonalTimeItems(toDomainDetails(categoryCmd.getLines()));
		default:
			break;
		}
	}

	private static List<DetailItem> toDomainDetails(List<LineCommandBase> lines) {
		List<DetailItem> details = new ArrayList<>();
		try {
			for (LineCommandBase line : lines) {
				List<DetailItem> items = line.getDetails().stream().filter(d-> !d.getItemCode().equals("")).map(d -> d.toDomain(line.getLinePosition())).collect(Collectors.toList());
				details.addAll(items);
			}
		} catch (Exception e) {
			throw e;
		}
		
		
		return details;
	}
}
