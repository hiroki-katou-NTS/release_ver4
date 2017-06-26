package nts.uk.ctx.pr.core.app.command.paymentdata;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1Repository;
import nts.uk.ctx.pr.core.dom.paymentdata.Payment;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDataRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDateMasterRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * Command Handler: add payment data.
 * 
 * @author vunv
 *
 */
@Stateless
@Transactional
public class InsertPaymentDataCommandHandler extends CommandHandler<InsertPaymentDataCommand> {

	/** CompanyRepository */
	@Inject
	private PaymentDataRepository paymentDataRepository;

	@Inject
	private PaymentDateMasterRepository payDateMasterRepository;

	@Inject
	private ItemMasterV1Repository itemMasterRepository;

	@Override
	protected void handle(CommandHandlerContext<InsertPaymentDataCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		Payment payment = context.getCommand().toDomain(companyCode);
		GeneralDate mPayDate = this.payDateMasterRepository
				.find(companyCode, payment.getPayBonusAtr().value, payment.getProcessingYM().v(),
						payment.getSparePayAtr().value, payment.getProcessingNo().v())
				.map(d -> d.getStandardDate()).orElse(GeneralDate.today());

		payment.setStandardDate(mPayDate);
		payment.validate();

		this.paymentDataRepository.insertHeader(payment);
		// update detail
		this.registerDetail(payment, payment.getDetailPaymentItems());
		this.registerDetail(payment, payment.getDetailArticleItems());
		this.registerDetail(payment, payment.getDetailDeductionItems());
		this.registerDetail(payment, payment.getDetailPersonalTimeItems());

	}

	/**
	 * Insert of update item data by status
	 * 
	 * @param items
	 * @param payment
	 */
	private void registerDetail(Payment payment, List<DetailItem> details) {
		for (DetailItem item : details) {
			ItemMasterV1 mItem = this.itemMasterRepository
					.find(payment.getCompanyCode().v(), item.getCategoryAtr().value, item.getItemCode().v()).get();
			item.additionalInfo(mItem.getLimitMoney().v(), mItem.getFixedPaidAtr().value, mItem.getAvgPaidAtr().value,
					mItem.getItemAtr().value);
			
			item.additionalInfo(item.getCorrectFlag(), mItem.getSocialInsuranceAtr().value, mItem.getLaborInsuranceAtr().value, mItem.getDeductAttribute());
			this.paymentDataRepository.insertDetail(payment, item);
		}
	}
}
