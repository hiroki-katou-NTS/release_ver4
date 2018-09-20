package nts.uk.ctx.pr.core.app.command.wageprovision.statementitem;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.CategoryAtr;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItem;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemDisplaySet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemDisplaySetRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemName;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemNameRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.StatementItemRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.deductionitemset.DeductionItemSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.deductionitemset.DeductionItemSetRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.itemrangeset.ItemRangeSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.itemrangeset.ItemRangeSetRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.paymentitemset.PaymentItemSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.paymentitemset.PaymentItemSetRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.timeitemset.TimeItemSet;
import nts.uk.ctx.pr.core.dom.wageprovision.statementitem.timeitemset.TimeItemSetRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class UpdateStatementItemDataCommandHandler extends CommandHandler<StatementItemDataCommand> {
	@Inject
	private StatementItemRepository statementItemRepository;
	@Inject
	private StatementItemNameRepository statementItemNameRepository;
	@Inject
	private PaymentItemSetRepository paymentItemSetRepository;
	@Inject
	private DeductionItemSetRepository deductionItemSetRepository;
	@Inject
	private TimeItemSetRepository timeItemSetRepository;
	@Inject
	private StatementItemDisplaySetRepository statementItemDisplaySetRepository;
	@Inject
	private ItemRangeSetRepository itemRangeSetRepository;

	@Override
	protected void handle(CommandHandlerContext<StatementItemDataCommand> context) {
		val command = context.getCommand();
		String cid = AppContexts.user().companyId();
		val statementItem = command.getStatementItem();
		if (statementItem == null) {
			return;
		}
		String salaryItemId = statementItem.getSalaryItemId();

		// ドメインモデル「明細書項目」を新規追加する
		statementItemRepository.update(
				new StatementItem(cid, statementItem.getCategoryAtr(), statementItem.getItemNameCd(), salaryItemId,
						statementItem.getDefaultAtr(), statementItem.getValueAtr(), statementItem.getDeprecatedAtr(),
						statementItem.getSocialInsuaEditableAtr(), statementItem.getIntergrateCd()));
		val categoryAtr = EnumAdaptor.valueOf(command.getStatementItem().getCategoryAtr(), CategoryAtr.class);
		switch (categoryAtr) {
		case PAYMENT_ITEM:
			// ドメインモデル「支給項目設定」を新規追加する
			val paymentItem = command.getPaymentItemSet();
			if (paymentItem != null) {
				paymentItemSetRepository.update(new PaymentItemSet(cid, salaryItemId,
						paymentItem.getBreakdownItemUseAtr(), paymentItem.getLaborInsuranceCategory(),
						paymentItem.getSettingAtr(), paymentItem.getEveryoneEqualSet(), paymentItem.getMonthlySalary(),
						paymentItem.getHourlyPay(), paymentItem.getDayPayee(), paymentItem.getMonthlySalaryPerday(),
						paymentItem.getAverageWageAtr(), paymentItem.getSocialInsuranceCategory(),
						paymentItem.getTaxAtr(), paymentItem.getTaxableAmountAtr(), paymentItem.getLimitAmount(),
						paymentItem.getLimitAmountAtr(), paymentItem.getTaxLimitAmountCode(), paymentItem.getNote()));
			}
			break;

		case DEDUCTION_ITEM:
			// ドメインモデル「控除項目設定」を新規追加する
			val deductionItem = command.getDeductionItemSet();
			if (deductionItem != null) {
				deductionItemSetRepository
						.update(new DeductionItemSet(cid, salaryItemId, deductionItem.getDeductionItemAtr(),
								deductionItem.getBreakdownItemUseAtr(), deductionItem.getNote()));
			}
			break;

		case ATTEND_ITEM:
			// ドメインモデル「勤怠項目設定」を新規追加する
			val timeItem = command.getTimeItemSet();
			if (timeItem != null) {
				timeItemSetRepository.update(new TimeItemSet(cid, salaryItemId, timeItem.getAverageWageAtr(),
						timeItem.getWorkingDaysPerYear(), timeItem.getTimeCountAtr(), timeItem.getNote()));
			}
			break;

		case REPORT_ITEM:
			break;
		case OTHER_ITEM:
			break;
		}

		if (categoryAtr == CategoryAtr.PAYMENT_ITEM || categoryAtr == CategoryAtr.DEDUCTION_ITEM
				|| categoryAtr == CategoryAtr.ATTEND_ITEM) {
			// ドメインモデル「項目範囲設定初期値」を新規追加する
			val itemRange = command.getItemRangeSet();
			if (itemRange != null) {
				itemRangeSetRepository.update(new ItemRangeSet(cid, salaryItemId, itemRange.getRangeValueAtr(),
						itemRange.getErrorUpperLimitSettingAtr(), itemRange.getErrorUpperRangeValueAmount(),
						itemRange.getErrorUpperRangeValueTime(), itemRange.getErrorUpperRangeValueNum(),
						itemRange.getErrorLowerLimitSettingAtr(), itemRange.getErrorLowerRangeValueAmount(),
						itemRange.getErrorLowerRangeValueTime(), itemRange.getErrorLowerRangeValueNum(),
						itemRange.getAlarmUpperLimitSettingAtr(), itemRange.getAlarmUpperRangeValueAmount(),
						itemRange.getAlarmUpperRangeValueTime(), itemRange.getAlarmUpperRangeValueNum(),
						itemRange.getAlarmLowerLimitSettingAtr(), itemRange.getAlarmLowerRangeValueAmount(),
						itemRange.getAlarmLowerRangeValueTime(), itemRange.getAlarmLowerRangeValueNum()));
			}
		}

		if (categoryAtr == CategoryAtr.PAYMENT_ITEM || categoryAtr == CategoryAtr.DEDUCTION_ITEM
				|| categoryAtr == CategoryAtr.ATTEND_ITEM || categoryAtr == CategoryAtr.REPORT_ITEM) {
			// ドメインモデル「明細項目の表示設定」を新規追加する
			val statementDisplay = command.getStatementDisplaySet();
			if (statementDisplay != null) {
				statementItemDisplaySetRepository.update(new StatementItemDisplaySet(cid, salaryItemId,
						statementDisplay.getZeroDisplayAtr(), statementDisplay.getItemNameDisplay()));
			}
		}

		// ドメインモデル「明細書項目名称」を新規追加する
		val statementItemName = command.getStatementItemName();
		if (statementItemName != null) {
			statementItemNameRepository.update(new StatementItemName(cid, salaryItemId, statementItemName.getName(),
					statementItemName.getShortName(), statementItemName.getOtherLanguageName(),
					statementItemName.getEnglishName()));
		}

	}
}
