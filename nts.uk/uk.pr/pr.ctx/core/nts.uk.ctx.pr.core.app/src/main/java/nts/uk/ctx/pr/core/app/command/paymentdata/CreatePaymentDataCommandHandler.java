package nts.uk.ctx.pr.core.app.command.paymentdata;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.allot.CompanyAllotSettingRepository;
import nts.uk.ctx.pr.core.dom.allot.PersonalAllotSetting;
import nts.uk.ctx.pr.core.dom.allot.PersonalAllotSettingRepository;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.enums.SparePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterV1Repository;
import nts.uk.ctx.pr.core.dom.itemmaster.TaxAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.BonusTaxRate;
import nts.uk.ctx.pr.core.dom.paymentdata.CalcFlag;
import nts.uk.ctx.pr.core.dom.paymentdata.Comment;
import nts.uk.ctx.pr.core.dom.paymentdata.DependentNumber;
import nts.uk.ctx.pr.core.dom.paymentdata.MakeMethodFlag;
import nts.uk.ctx.pr.core.dom.paymentdata.Payment;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentCalculationBasicInformation;
import nts.uk.ctx.pr.core.dom.paymentdata.PersonName;
import nts.uk.ctx.pr.core.dom.paymentdata.SpecificationCode;
import nts.uk.ctx.pr.core.dom.paymentdata.SpecificationName;
import nts.uk.ctx.pr.core.dom.paymentdata.TenureAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.CorrectFlag;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.DetailItem;
import nts.uk.ctx.pr.core.dom.paymentdata.dataitem.position.PrintPositionCategory;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.AgeContinuationInsureAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.EmploymentInsuranceAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.HealthInsuranceAverageEarn;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.HealthInsuranceGrade;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.InsuredAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.PensionAverageEarn;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.PensionInsuranceGrade;
import nts.uk.ctx.pr.core.dom.paymentdata.insure.WorkInsuranceCalculateAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster.PaymentDateMaster;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentCalculationBasicInformationRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDataRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDateMasterRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.residence.ResidenceName;
import nts.uk.ctx.pr.core.dom.paymentdata.service.PaymentDataCheckService;
import nts.uk.ctx.pr.core.dom.paymentdata.service.PaymentDetailParam;
import nts.uk.ctx.pr.core.dom.paymentdata.service.PaymentDetailService;
import nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract.PersonalEmploymentContract;
import nts.uk.ctx.pr.core.dom.personalinfo.employmentcontract.PersonalEmploymentContractRepository;
import nts.uk.ctx.pr.core.dom.personalinfo.holiday.HolidayPaid;
import nts.uk.ctx.pr.core.dom.personalinfo.holiday.HolidayPaidRepository;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.PersonalWage;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.PersonalWageRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMasterRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategoryRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetailRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLineRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LineDispAtr;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.primitive.PersonId;
import nts.uk.shr.com.primitive.sample.ProcessingNo;
import nts.uk.shr.com.primitive.sample.ResidenceCode;

/**
 * Command Handler: add payment data.
 * 
 * @author chinhbv
 *
 */
@Stateless
@Transactional
public class CreatePaymentDataCommandHandler extends CommandHandler<CreatePaymentDataCommand> {

	@Inject
	private PaymentDetailService paymentDetailService;
	@Inject
	private PaymentDataCheckService paymentDataCheckService;
	@Inject
	private PersonalAllotSettingRepository personalAllotSettingRepo;
	//@Inject
	//private CompanyAllotSettingRepository companyAllotSettingRepo;
	@Inject
	private HolidayPaidRepository holidayPaidRepo;
	@Inject
	private PersonalEmploymentContractRepository personalEmploymentContractRepo;
	@Inject
	private PaymentCalculationBasicInformationRepository payCalBasicInfoRepo;
	@Inject
	private PaymentDateMasterRepository payDateMasterRepo;
	@Inject
	private LayoutMasterRepository layoutMasterRepo;
	@Inject
	private LayoutMasterDetailRepository layoutDetailMasterRepo;
	@Inject
	private PaymentDataRepository paymentDataRepo;
	@Inject
	private LayoutMasterLineRepository layoutMasterLineRepo;
	@Inject
	private LayoutMasterCategoryRepository layoutMasterCtgRepo;
	@Inject
	private ItemMasterV1Repository itemMasterRepo;
	@Inject
	private PersonalWageRepository personalWageRepo;

	@Override
	protected void handle(CommandHandlerContext<CreatePaymentDataCommand> context) {
		// get context
		LoginUserContext loginInfo = AppContexts.user();

		// get command
		CreatePaymentDataCommand command = context.getCommand();

		// get base date
		GeneralDate currentDate = GeneralDate.today();
		YearMonth baseYearMonth = currentDate.yearMonth();
				
		// get basic calculate
		PaymentCalculationBasicInformation payCalBasicInfo = payCalBasicInfoRepo.find(loginInfo.companyCode())
				.orElseThrow(() -> new BusinessException("エラーでは？")); //PaymentCalculationBasicInformation not found
		
		// get pay day
		PaymentDateMaster payDay = payDateMasterRepo.find(loginInfo.companyCode(), PayBonusAtr.SALARY.value,
				command.getProcessingYearMonth(), SparePayAtr.NORMAL.value, command.getProcessingNo())
					.orElseThrow(() -> new BusinessException("対象データがありません。"));
			
		// calculate personal
		calculatePersonalPayment(
				loginInfo,
				YearMonth.of(command.getProcessingYearMonth()),
				command.getProcessingNo(),
				baseYearMonth,
				payCalBasicInfo,
				payDay,
				command.getPersonId(),
				command.getPersonName()
				);
	}

	/**
	 * Calculate personal payment. 
	 */
	private void calculatePersonalPayment(
			LoginUserContext loginInfo,
			YearMonth processingYearMonth,
			int processingNo,
			YearMonth baseYearMonth,
			PaymentCalculationBasicInformation payCalBasicInfo,
			PaymentDateMaster payDay,
			String personId, 
			String personName) {
		// check exists
		boolean isExists = paymentDataCheckService.isExists(loginInfo.companyCode(), personId, PayBonusAtr.SALARY, processingYearMonth.v());
		if (isExists) {
			throw new BusinessException("既にデータが存在します。");
		}
		
		//
		// Start calculate payment
		//
		PersonalEmploymentContract employmentContract = personalEmploymentContractRepo.findActive(
				loginInfo.companyCode(), personId, payDay.getStandardDate())
					.orElseThrow(() -> new BusinessException("エラーでは？")); // PersonalEmploymentContract not found
		HolidayPaid holiday = holidayPaidRepo.find(loginInfo.companyCode(), personId)
					.orElseThrow(() -> new BusinessException("エラーでは？")); // HolidayPaid not found
		
		// get allot personal setting
		PersonalAllotSetting personalAllotSetting = getPersonalAllotSetting(loginInfo.companyCode(), personId,
				processingYearMonth.v());

		// get personal allot setting
		String stmtCode = personalAllotSetting.getPaymentDetailCode().v();
		
		// get layout master 
		List<LayoutMaster> layoutMasterList = layoutMasterRepo.findAll(loginInfo.companyCode(), stmtCode, processingYearMonth.v());
		if (layoutMasterList.isEmpty()) {
			throw new BusinessException("対象データがありません。");
		}
		LayoutMaster layoutMaster = layoutMasterList.get(0);
		
		//LamVT: fix tam gia tri startYm = 0 vi dang update do thay đổi bảng dữ liệu
		// get layout detail master
		List<LayoutMasterDetail> layoutMasterDetailList = layoutDetailMasterRepo.getDetails(loginInfo.companyCode(),
						stmtCode, 0);
		
		// get lay master category
		List<LayoutMasterCategory> categoryList = layoutMasterCtgRepo.getCategories(loginInfo.companyCode(), stmtCode, 0);
		
		// get layout master line
		List<LayoutMasterLine> lineList = layoutMasterLineRepo.getLines(loginInfo.companyCode(), stmtCode, 0);
		
		// get personal wage list
		List<PersonalWage> personWageList = personalWageRepo.findAll(loginInfo.companyCode(), personId, baseYearMonth.v());
		
		PaymentDetailParam param = new PaymentDetailParam(
				loginInfo.companyCode(),
				new PersonId(personId),
				baseYearMonth,
				processingYearMonth,
				holiday,
				employmentContract,
				payCalBasicInfo,
				payDay,
				personalAllotSetting,
				layoutMasterDetailList,
				lineList,
				personWageList);

		// calculate payment detail
		Map<CategoryAtr, List<DetailItem>> payDetail = paymentDetailService.calculatePayValue(param);
		
		// convert to domain
		List<DetailItem> detailPaymentList = Helper.createDetailsOfPayment(payDetail, layoutMasterDetailList);
		List<DetailItem> detailDeductionList = Helper.createDetailsOfDeduction(payDetail, layoutMasterDetailList);
		List<PrintPositionCategory> positionCategoryList = Helper.createPositionCategory(categoryList, lineList);
		List<DetailItem> detailPersonTimeList = payDetail.get(CategoryAtr.PERSONAL_TIME);
		List<DetailItem> detailArticleList = payDetail.get(CategoryAtr.ARTICLES);
		
		Payment paymentHead = this.toDomain(
				loginInfo.companyCode(), personId, personName, processingNo, processingYearMonth, stmtCode, layoutMaster.getStmtName().v(),
				detailPaymentList, detailDeductionList, detailPersonTimeList, detailArticleList, positionCategoryList, payDay);
		
		// Save detail payment with calculate payment
		double totalPayment = Payment.calculateTotalPayment(Helper.detailItemByTotalPayment(payDetail, layoutMasterDetailList));
		DetailItem detailPaymentTotal = this.createDataDetailItem(loginInfo.companyCode(), "F003", totalPayment, CategoryAtr.PAYMENT, lineList, layoutMasterDetailList);
		double deductionTotalPayment = Payment.calculateDeductionTotalPayment(Helper.layoutMasterByDeductionTotalPayment(payDetail, layoutMasterDetailList)); 
		DetailItem detailDeductionTotal = this.createDataDetailItem(loginInfo.companyCode(), "F114", deductionTotalPayment, CategoryAtr.DEDUCTION, lineList, layoutMasterDetailList);
		double amount = Payment.amountOfPay(totalPayment, deductionTotalPayment);
		DetailItem detailAmount = this.createDataDetailItem(loginInfo.companyCode(), "F309", amount, CategoryAtr.ARTICLES, lineList, layoutMasterDetailList);
		
		detailPaymentList = updateDetailItem(paymentHead.existsDetailPaymentItem(CategoryAtr.PAYMENT, new ItemCode("F003")), detailPaymentList, detailPaymentTotal);
		detailDeductionList = updateDetailItem(paymentHead.existsDetailDeductionItem(CategoryAtr.DEDUCTION, new ItemCode("F114")), detailDeductionList, detailDeductionTotal);
		detailArticleList = updateDetailItem(paymentHead.existsDetailArticleItem(CategoryAtr.ARTICLES, new ItemCode("F309")), detailArticleList, detailAmount);
		
		paymentHead = this.toDomain(paymentHead, detailPaymentList, detailDeductionList, detailArticleList);
		
		// create data
		paymentDataRepo.add(paymentHead);
	}
	
	/**
	 * Convert to domain object.
	 * 
	 * @return domain
	 */
	private Payment toDomain(String companyCode, String personId, String personName, int processingNo, YearMonth processingYearMonth, String stmtCode, String stmtName,
			List<DetailItem> detailPaymentList, List<DetailItem> detailDeductionList, List<DetailItem> detailPersonTimeList, List<DetailItem> detailArticleList,
			List<PrintPositionCategory> positionCategoryList, PaymentDateMaster payDay) {
		Payment payment = new Payment(
				new CompanyCode(companyCode), 
				new PersonId(personId),
				new PersonName(personName),
				new ProcessingNo(processingNo), 
				PayBonusAtr.SALARY, 
				processingYearMonth, 
				SparePayAtr.NORMAL,
				payDay.getStandardDate(),
				new SpecificationCode(stmtCode), 
				new SpecificationName(stmtName),
				new ResidenceCode("000001"), 
				new ResidenceName("住民税納付先"), 
				new HealthInsuranceGrade(5), 
				new HealthInsuranceAverageEarn(98000), 
				AgeContinuationInsureAtr.NOT_TARGET, 
				TenureAtr.TENURE,
				TaxAtr.TAXATION,
				new PensionInsuranceGrade(1), 
				new PensionAverageEarn(98000), 
				EmploymentInsuranceAtr.A, 
				new DependentNumber(0),
				WorkInsuranceCalculateAtr.FULL_TIME_EMPLOYEE, 
				InsuredAtr.GENERAL_INSURED_PERSON,
				new BonusTaxRate(0),
				CalcFlag.CALCULATED, 
				MakeMethodFlag.INITIAL_DATA, 
				new Comment(""));

		payment.setDetailPaymentItems(detailPaymentList);
		payment.setDetailDeductionItems(detailDeductionList);
		payment.setDetailPersonalTimeItems(detailPersonTimeList);
		payment.setDetailArticleItems(detailArticleList);
		payment.setPositionCategoryItems(positionCategoryList);
				
		return payment;
	}
	
	private Payment toDomain(Payment payment, List<DetailItem> detailPaymentList, List<DetailItem> detailDeductionList, List<DetailItem> detailArticleList) {
		payment.setDetailPaymentItems(detailPaymentList);
		payment.setDetailDeductionItems(detailDeductionList);
		payment.setDetailArticleItems(detailArticleList);
		
		return payment;
	}

	/**
	 * Get allot setting by personal
	 * 
	 * @return
	 */
	private PersonalAllotSetting getPersonalAllotSetting(String companyCode, String personId, int baseYearMonth) {
		PersonalAllotSetting result = null;

		Optional<PersonalAllotSetting> personalAllotSettingOp = personalAllotSettingRepo.find(companyCode, personId,
				baseYearMonth);

		if (!personalAllotSettingOp.isPresent()) {
//			// get allot company setting
//			CompanyAllotSetting companyAllotSetting = companyAllotSettingRepo.find(companyCode)
//					.orElseThrow(() -> new RuntimeException("エラーでは？")); // Company Allot Setting Not Found
//
//			result = new PersonalAllotSetting(new CompanyCode(companyCode), new PersonId(personId),
//					companyAllotSetting.getStartDate(), companyAllotSetting.getEndDate(),
//					companyAllotSetting.getBonusDetailCode(), companyAllotSetting.getPaymentDetailCode());
		} else {
			result = personalAllotSettingOp.get();
		}

		return result;
	}
	
	/** Create data payment detail for case total payment **/
	private DetailItem createDataDetailItem(String companyCode, String itemCode, double value, CategoryAtr categoryAtr, List<LayoutMasterLine> lineList, List<LayoutMasterDetail> layoutMasterDetailList) {
		ItemMasterV1 itemMaster = itemMasterRepo.getItemMaster(companyCode, categoryAtr.value, itemCode).orElseThrow(() -> new BusinessException("対象データがありません。"));
		LayoutMasterDetail layoutMasterDetail = layoutMasterDetailList.stream().filter(x -> x.getCategoryAtr() == categoryAtr && itemCode.equals(x.getItemCode().v())).findFirst()
				.orElseThrow(() -> new BusinessException("対象データがありません。"));
		String autoLineId = layoutMasterDetail.getAutoLineId().v();
		int itemPositionColumn = layoutMasterDetail.getItemPosColumn().v();
		
		LayoutMasterLine line = lineList.stream()
				.filter(x -> categoryAtr == x.getCategoryAtr() && x.getAutoLineId().v().equals(autoLineId))
				.findFirst().orElseThrow(() -> new BusinessException("対象データがありません。"));
		
		int linePosition;
		if (line.getLineDisplayAttribute() == LineDispAtr.DISABLE) {
			linePosition = -1;
		} else {
			linePosition = line.getLinePosition().v();
		}
		
		val detailItem = DetailItem.createDataDetailItem(itemMaster.getItemCode(), value, categoryAtr);
		detailItem.additionalInfo(CorrectFlag.NO_MODIFY, itemMaster.getSocialInsuranceAtr().value, itemMaster.getLaborInsuranceAtr().value, itemMaster.getDeductAttribute());
		detailItem.additionalInfo(itemMaster.getLimitMoney().v(), itemMaster.getFixedPaidAtr().value, itemMaster.getAvgPaidAtr().value, itemMaster.getItemAtr().value);
		detailItem.additionalInfo(linePosition, itemPositionColumn);
		detailItem.additionalInfo(itemMaster.getTaxAtr());
		return detailItem;
	}
	
	/**
	 * Update again list detail item.
	 * @param exists
	 * @param detailItemList
	 * @param detailItem
	 * @return
	 */
	private List<DetailItem> updateDetailItem(boolean exists, List<DetailItem> detailItemList, DetailItem detailItem) {
		if (exists) {
			for (DetailItem item : detailItemList) {
				if (detailItem.getItemCode().equals(item.getItemCode())) {
					detailItemList.remove(item);
					detailItemList.add(detailItem);
					return detailItemList;
				}
			}
		} else {
			detailItemList.add(detailItem);
		}
		
		return detailItemList;
	}
}
