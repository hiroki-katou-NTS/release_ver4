package nts.uk.ctx.pr.core.app.find.wageprovision.statementitem;

import java.util.List;

import lombok.Value;

@Value
public class StatementItemDataDto {
	private StatementItemDto statementItem;
	private StatementItemNameDto statementItemName;
	private PaymentItemSetDto paymentItemSet;
	private DeductionItemSetDto deductionItemSet;
	private TimeItemSetDto timeItemSet;
	private StatementItemDisplaySetDto statementDisplaySet;
	private ItemRangeSetDto itemRangeSet;
	private ValidityPeriodAndCycleSetDto validityPeriodAndCycleSet;
	private List<BreakdownItemSetDto> breakdownItemSet;
	private IntegratedItemDto integratedItem;
	/**
	 * カテゴリ区分
	 */
	private int categoryAtr;

	/**
	 * 項目名コード
	 */
	private String itemNameCd;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 廃止区分
	 */
	private int deprecatedAtr;
}
