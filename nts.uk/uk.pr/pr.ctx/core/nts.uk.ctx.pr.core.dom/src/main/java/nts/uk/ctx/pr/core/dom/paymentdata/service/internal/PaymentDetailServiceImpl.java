package nts.uk.ctx.pr.core.dom.paymentdata.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1Repository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LineDispAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentCalculationBasicInformation;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.CorrectFlag;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;
import nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster.PaymentDateMaster;
import nts.uk.ctx.pr.core.dom.paymentdata.service.PaymentDetailParam;
import nts.uk.ctx.pr.core.dom.paymentdata.service.PaymentDetailService;
import nts.uk.ctx.pr.core.dom.personalinfo.commute.PersonalCommuteFee;
import nts.uk.ctx.pr.core.dom.personalinfo.commute.PersonalCommuteFeeRepository;
import nts.uk.ctx.pr.core.dom.personalinfo.holiday.HolidayPaid;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.PersonalWage;

@Stateless
public class PaymentDetailServiceImpl implements PaymentDetailService {

	@Inject
	private ItemMasterV1Repository itemMasterRepo;
	@Inject
	private PersonalCommuteFeeRepository personalCommuteRepo;

	@Override
	public Map<CategoryAtr, List<DetailItem>> calculatePayValue(PaymentDetailParam param) {
		// layout master detail list
		List<LayoutMasterDetail> layoutMasterDetailList = param.getLayoutMasterDetailList();
						
		// get personal commute
		List<PersonalCommuteFee> commuteList = personalCommuteRepo.findAll(
				param.getCompanyCode(), param.getPersonId().v(), param.getCurrentProcessingYearMonth().v());
		if (commuteList.isEmpty()) {
			throw new BusinessException("エラーでは？"); // Personal commute not found: personId=" + param.getPersonId().v()
		}
		PersonalCommuteFee commute = commuteList.get(0);
		
		// LAYOUT_DETAIL with CTR_ATR = 0
		List<DetailItem> detailsOfCategoryPayment = new ArrayList<>(); 
		layoutMasterDetailList.stream()
			.filter(l -> l.isCategoryPayment())
			.forEach(l -> {
				DetailItem item = createDetailOfCategoryPayment(param, l, commute);
				if (item != null) {
					detailsOfCategoryPayment.add(item);
				}
			});
		
		// LAYOUT_DETAIL with CTR_ATR = 1
		List<DetailItem> detailsOfCategoryDeduction = new ArrayList<>(); 
		layoutMasterDetailList.stream()
				.filter(l -> l.isCategoryDeduction())
				.forEach(l -> {
					DetailItem item = createDetailOfCategoryDeduction(param, l);
					if (item != null) {
						detailsOfCategoryDeduction.add(item);
					}
				});
		
		// LAYOUT_DETAIL with CTR_ATR = 2
		List<DetailItem> detailsOfCategoryPersonalTime = new ArrayList<>();
		
		layoutMasterDetailList.stream()
				.filter(l -> l.isCategoryPersonalTime())
				.forEach(l -> {
					DetailItem item = createDetailOfCategoryPersonalTime(param, l);
					if (item != null) {
						detailsOfCategoryPersonalTime.add(item);
					}
				});
		
		// LAYOUT_DETAIL with CTR_ATR = 3
		val detailsOfCategoryArticles = layoutMasterDetailList.stream()
				.filter(l -> l.isCategoryArticles())
				.map(l -> createDetailOfCategoryArticlesOrOther(param, l))
				.collect(Collectors.toList());
		
		// LAYOUT_DETAIL with CTR_ATR = 9
		val detailsOfCategoryOther = layoutMasterDetailList.stream()
				.filter(l -> l.isCategoryOther())
				.map(l -> createDetailOfCategoryArticlesOrOther(param, l))
				.collect(Collectors.toList());
		
		Map<CategoryAtr, List<DetailItem>> payDetail = new HashMap<>();
		payDetail.put(CategoryAtr.PAYMENT, detailsOfCategoryPayment);
		payDetail.put(CategoryAtr.DEDUCTION, detailsOfCategoryDeduction);
		payDetail.put(CategoryAtr.PERSONAL_TIME, detailsOfCategoryPersonalTime);
		payDetail.put(CategoryAtr.ARTICLES, detailsOfCategoryArticles);
		payDetail.put(CategoryAtr.OTHER, detailsOfCategoryOther);
		
		return payDetail;
	}
	
	/** Create data detail with Layout.Category = 0 **/
	private DetailItem createDetailOfCategoryPayment(
			PaymentDetailParam param,
			LayoutMasterDetail layout,
			PersonalCommuteFee commute) {
		
		ItemMasterV1 itemMaster = getItemMaster(param, layout);
		
		// get calculate method
		if (layout.isCalMethodManualOrFormulaOrWageOrCommon()) {
			return this.createDataDetailItem(itemMaster, 0.0, layout.getCategoryAtr(), param.getLineList(), layout.getAutoLineId().v(), layout.getItemPosColumn().v(), 0);
		} else if (layout.isCalMethodPesonalInfomation()) {
			// calculate pay value by tax
			return getPayValueByTax(param, layout, layout.getItemCode().v(),
					itemMaster, commute);
		}
		
		return null;
	}

	/** Create data detail with Layout.Category = 1 **/
	private DetailItem createDetailOfCategoryDeduction(
			PaymentDetailParam param,
			LayoutMasterDetail layout) {
		ItemMasterV1 itemMaster = getItemMaster(param, layout);
		
		if (layout.isCalMethodPesonalInfomation()) {
			// get personal wage
			Optional<PersonalWage> personalWage = param.getPersonalWageList().stream()
					.filter(x -> x.getCategoryAtr() == itemMaster.getCategoryAtr() && x.getWageCode().equals(layout.getPersonalWageCode())).findFirst();
					//.orElseThrow(() -> new RuntimeException("システムエラー PersonalWage: itemCode="+ layout.getItemCode() + "& category=" + itemMaster.getCategoryAtr() + " & personId=" + param.getPersonId().v())); 
			if (!personalWage.isPresent()) {
				return null;
			}
			
			return this.createDataDetailItem(itemMaster, personalWage.get().getWageValue().doubleValue(), layout.getCategoryAtr(), param.getLineList(), layout.getAutoLineId().v(), layout.getItemPosColumn().v(), 0);
		} else if (layout.isCalMethodManualOrFormulaOrWageOrCommonOrPaymentCanceled()) {
			return this.createDataDetailItem(itemMaster, 0.0, layout.getCategoryAtr(), param.getLineList(), layout.getAutoLineId().v(), layout.getItemPosColumn().v(), 0);
		}
		
		return null;
	}
	
	/** Create data detail with Layout.Category = 2 **/
	private DetailItem createDetailOfCategoryPersonalTime(
			PaymentDetailParam param,
			LayoutMasterDetail layout) {
		
		double value;
		
		ItemMasterV1 itemMaster = getItemMaster(param, layout);
		
		// PayrollSystem == 2 || 3
		if (param.getEmploymentContract().isPayrollSystemDailyOrDay()) {
			value = getPayValueByPayrollDayHours(layout.getItemCode(), param.getHoliday());
			
		} else if (param.getEmploymentContract().isPayrollSystemDailyOrMonthly()) {
			// PayrollSystem == 0 || 1
			value = getPayValueByMonthlyDaily(layout.getItemCode(), param.getHoliday(), param.getPaymentDateMaster(),
					param.getPayCalBasicInfo());
		} else {
			throw new BusinessException("エラーでは？"); // システムエラー: itemCode="+ layout.getItemCode() + "& category=" + itemMaster.getCategoryAtr() + " & personId=" + param.getPersonId().v()
		}
		
		return this.createDataDetailItem(itemMaster, value, layout.getCategoryAtr(), param.getLineList(), layout.getAutoLineId().v(), layout.getItemPosColumn().v(), 0);
	}
	
	/** Create data detail with Layout.Category = 3 || 9 **/
	private DetailItem createDetailOfCategoryArticlesOrOther(PaymentDetailParam param, LayoutMasterDetail layout) {
		ItemMasterV1 itemMaster = getItemMaster(param, layout);
		
		return this.createDataDetailItem(itemMaster, 0.0, layout.getCategoryAtr(), param.getLineList(), layout.getAutoLineId().v(), layout.getItemPosColumn().v(), 0);
	}

	/** Get item master **/
	private ItemMasterV1 getItemMaster(PaymentDetailParam param, LayoutMasterDetail layout) {
		ItemMasterV1 itemMaster = itemMasterRepo
				.find(param.getCompanyCode(), layout.getCategoryAtr().value, layout.getItemCode().v())
				.orElseThrow(() -> new BusinessException("対象データがありません。"));
		return itemMaster;
	}
		
	/**
	 * Calculate value by tax
	 * @param param
	 * @param startYearMonth
	 * @param itemLayoutMasterDetail
	 * @param detail
	 * @param itemCode
	 * @param itemMaster
	 * @return pay detail
	 */
	private DetailItem getPayValueByTax(PaymentDetailParam param,
			LayoutMasterDetail itemLayoutMasterDetail, String itemCode, ItemMasterV1 itemMaster, PersonalCommuteFee commute) {
		
		// check tax attribute
		if (itemMaster.isTaxTaxationOrTaxFreeLimitOrTaxFreeUnLimit()) { // tax_atr = 0 || 1 || 2
			// get personal wage
			Optional<PersonalWage> personalWage = param.getPersonalWageList().stream()
					.filter(x -> x.getCategoryAtr() == itemMaster.getCategoryAtr() && x.getWageCode().equals(itemLayoutMasterDetail.getPersonalWageCode())).findFirst();
					//.orElseThrow(() -> new RuntimeException("システムエラー PersonalWage: itemCode="+ itemCode + "& category=" + itemMaster.getCategoryAtr() + " & personId=" + param.getPersonId().v())); 
			if (!personalWage.isPresent()) {
				return null;
			}
			
			return this.createDataDetailItem(
					itemMaster, personalWage.get().getWageValue().doubleValue(), itemLayoutMasterDetail.getCategoryAtr(), 
					param.getLineList(), itemLayoutMasterDetail.getAutoLineId().v(), itemLayoutMasterDetail.getItemPosColumn().v(), 0);
		} else if (itemMaster.isTaxCommutingoCostOrCommutingExpense()) { // tax_atr = 3 || 4
			
			double commuteAllowMonth = commute.sumCommuteAllowance(param.getCurrentProcessingYearMonth().v());
			
			return this.createDataDetailItem(itemMaster,
					commute.sumCommuteAllowance(
							itemLayoutMasterDetail.getCommuteAtr(),
							param.getCurrentProcessingYearMonth().v()),
					itemLayoutMasterDetail.getCategoryAtr(), param.getLineList(), itemLayoutMasterDetail.getAutoLineId().v(), itemLayoutMasterDetail.getItemPosColumn().v(), commuteAllowMonth);
			
		} else {
			throw new BusinessException("エラーでは？"); // システムエラー: itemCode="+ itemCode + "& category=" + itemMaster.getCategoryAtr() + " & personId=" + param.getPersonId().v()
		}
	}

	/**
	 * PayrollSystem == 2 || 3
	 * 
	 * @param itemCode
	 * @param holiday
	 * @return QSTDT_PAYMENT_DETAIL.VAL
	 */
	private double getPayValueByPayrollDayHours(ItemCode itemCode, HolidayPaid holiday) {
		if (itemCode.isRemainDaysOfHoliday()) {
			return holiday.getRemainDays().doubleValue();
		} else if (itemCode.isRemainTimeOfHoliday()) {
			return holiday.getRemainTime().doubleValue();
		} else {
			return 0;
		}
	}

	/**
	 * PayrollSystem == 0 || 1
	 * 
	 * @param itemCode
	 * @param holiday
	 * @param payDay
	 * @param payCalBasic
	 * @return QSTDT_PAYMENT_DETAIL.VAL
	 */
	private double getPayValueByMonthlyDaily(ItemCode itemCode, HolidayPaid holiday, PaymentDateMaster payDay,
			PaymentCalculationBasicInformation payCalBasic) {
		
		if (itemCode.isRemainDaysOfHoliday()) {
			return holiday.getRemainDays().doubleValue();
		} else if (itemCode.isRemainTimeOfHoliday()) {
			return holiday.getRemainTime().doubleValue();
		} else if (itemCode.isNeededWorkDays()) {
			return payDay.getNeededWorkDay().doubleValue();
		} else if (itemCode.isNeededWorkTime()) {
			return payDay.getNeededWorkDay().doubleValue() * payCalBasic.getBaseHours().doubleValue();
		} else {
			return 0;
		}
	}
		
	/**
	 * Create data for detail item
	 * @param itemMaster
	 * @param value
	 * @param categoryAtr
	 * @return
	 */
	private DetailItem createDataDetailItem(ItemMasterV1 itemMaster, double value, CategoryAtr categoryAtr, 
			List<LayoutMasterLine> lineList, String autoLineId, int itemPositionColumn, double commuteAllowMonth) {
		LayoutMasterLine line = lineList.stream()
				.filter(x -> categoryAtr == x.getCategoryAtr() && x.getAutoLineId().v().equals(autoLineId))
				.findFirst().orElseThrow(() -> new BusinessException("対象データがありません。"));
		
		int linePosition;
		if (line.getLineDisplayAttribute() == LineDispAtr.DISABLE) {
			linePosition = 0;
		} else {
			linePosition = line.getLinePosition().v();
		}
		
		val detailItem = DetailItem.createDataDetailItem(itemMaster.getItemCode(), value, categoryAtr);
		detailItem.additionalInfo(CorrectFlag.NO_MODIFY, itemMaster.getSocialInsuranceAtr().value, itemMaster.getLaborInsuranceAtr().value, itemMaster.getDeductAttribute());
		detailItem.additionalInfo(itemMaster.getLimitMoney().v(), itemMaster.getFixedPaidAtr().value, itemMaster.getAvgPaidAtr().value, itemMaster.getItemAtr().value);
		detailItem.additionalInfo(linePosition, itemPositionColumn);
		detailItem.additionalInfo(commuteAllowMonth);
		detailItem.additionalInfo(itemMaster.getTaxAtr());
		return detailItem;
	}
}
