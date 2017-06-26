package nts.uk.ctx.pr.core.app.command.rule.employment.layout;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.gul.text.IdentifierUtil;
import nts.gul.util.Range;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.CommuteAtr;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;
import nts.uk.ctx.pr.core.dom.enums.UseOrNot;
import nts.uk.ctx.pr.core.dom.fomula.FormulaCode;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemCode;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename.PersonalWageCode;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutCode;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMasterRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.CategoryPosition;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategoryRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.CalculationMethod;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.CommonAmount;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.ItemPosColumn;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetailRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.RangeChecker;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.SumScopeAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.distribute.Distribute;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.distribute.DistributeSet;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.distribute.DistributeWay;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.AutoLineId;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLineRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LineDispAtr;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LinePosition;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * CreateLayoutCommandHandler
 * 
 * @author lamvt
 *
 */
@Stateless
@Transactional
public class CreateLayoutCommandHandler extends CommandHandler<CreateLayoutCommand> {

	@Inject
	private LayoutMasterRepository layoutRepo;

	@Inject
	private LayoutHistRepository layoutHistRepo;

	@Inject
	private LayoutMasterCategoryRepository categoryRepo;
	@Inject
	private LayoutMasterLineRepository lineRepo;
	@Inject
	private LayoutMasterDetailRepository detailRepo;

	@Override
	protected void handle(CommandHandlerContext<CreateLayoutCommand> context) {
		try {
			CreateLayoutCommand command = context.getCommand();
			String companyCode = AppContexts.user().companyCode();
			if (layoutRepo.isExist(companyCode, command.getStmtCode())) {
				throw new BusinessException(new RawErrorMessage("入力した明細書コードは既に存在しています。\r\n明細書コードを確認してください。"));
			}

			// LayoutMaster layout =
			// command.toDomain(IdentifierUtil.randomUniqueId());
			LayoutMaster layoutHead = command.toDomain();
			LayoutHistory layoutHistory = command.toDomain(IdentifierUtil.randomUniqueId());
			layoutHead.validate();
			this.layoutRepo.add(layoutHead);
			this.layoutHistRepo.add(layoutHistory);
			// 作成方法で「既存のレイアウトをコピーして作成」を選択した場合
			if (command.isCheckCopy()) {
				copyNewData(command, companyCode, layoutHead, layoutHistory);
			}
			// 作成方法で「新規に作成」を選択した場合
			else {

				createNewData(layoutHead, layoutHistory, companyCode);
			}

		} catch (Exception ex) {
			throw ex;
		}
	}

	public void createNewData(LayoutMaster layout, LayoutHistory layoutHistory, String companyCode) {
		// データベース登録[明細書マスタカテゴリ.INS-1] を実施する
		LayoutMasterCategory category = new LayoutMasterCategory(new CompanyCode(companyCode), layout.getStmtCode(),
				CategoryAtr.PAYMENT, new CategoryPosition(1), layoutHistory.getHistoryId());
		this.categoryRepo.add(category);
		category = new LayoutMasterCategory(new CompanyCode(companyCode), layout.getStmtCode(), CategoryAtr.DEDUCTION,
				new CategoryPosition(2), layoutHistory.getHistoryId());
		this.categoryRepo.add(category);
		category = new LayoutMasterCategory(new CompanyCode(companyCode), layout.getStmtCode(), CategoryAtr.ARTICLES,
				new CategoryPosition(3), layoutHistory.getHistoryId());
		this.categoryRepo.add(category);
		// データベース登録[明細書マスタ行.INS-1] を実施する
		String autoLineId1 = IdentifierUtil.randomUniqueId();
		String autoLineId2 = IdentifierUtil.randomUniqueId();
		String autoLineId3 = IdentifierUtil.randomUniqueId();
		String autoLineId4 = IdentifierUtil.randomUniqueId();
		String autoLineId5 = IdentifierUtil.randomUniqueId();
		String autoLineId6 = IdentifierUtil.randomUniqueId();
		createLineDefault(layout, layoutHistory, companyCode, CategoryAtr.PAYMENT, autoLineId1, 1);
		createLineDefault(layout, layoutHistory, companyCode, CategoryAtr.PAYMENT, autoLineId2, 2);
		createLineDefault(layout, layoutHistory, companyCode, CategoryAtr.PAYMENT, autoLineId3, 3);
		createLineDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, autoLineId4, 1);
		createLineDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, autoLineId5, 2);
		createLineDefault(layout, layoutHistory, companyCode, CategoryAtr.ARTICLES, autoLineId6, 1);
		// データベース登録[明細書マスタ明細.INS-1] を実施する
		// 支給3項目
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.PAYMENT, "F001", autoLineId1, 9,
				SumScopeAtr.EXCLUDED, CalculationMethod.SYSTEM_CALCULATION);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.PAYMENT, "F002", autoLineId2, 9,
				SumScopeAtr.EXCLUDED, CalculationMethod.SYSTEM_CALCULATION);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.PAYMENT, "F003", autoLineId3, 9,
				SumScopeAtr.EXCLUDED, CalculationMethod.SYSTEM_CALCULATION);

		// 控除9項目
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F101", autoLineId4, 1,
				SumScopeAtr.INCLUDED, CalculationMethod.MANUAL_ENTRY);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F102", autoLineId4, 2,
				SumScopeAtr.INCLUDED, CalculationMethod.MANUAL_ENTRY);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F103", autoLineId4, 3,
				SumScopeAtr.INCLUDED, CalculationMethod.MANUAL_ENTRY);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F104", autoLineId4, 4,
				SumScopeAtr.INCLUDED, CalculationMethod.MANUAL_ENTRY);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F105", autoLineId4, 5,
				SumScopeAtr.EXCLUDED, CalculationMethod.SYSTEM_CALCULATION);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F106", autoLineId4, 6,
				SumScopeAtr.EXCLUDED, CalculationMethod.SYSTEM_CALCULATION);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F107", autoLineId4, 7,
				SumScopeAtr.INCLUDED, CalculationMethod.MANUAL_ENTRY);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F108", autoLineId4, 8,
				SumScopeAtr.INCLUDED, CalculationMethod.MANUAL_ENTRY);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.DEDUCTION, "F114", autoLineId5, 9,
				SumScopeAtr.EXCLUDED, CalculationMethod.SYSTEM_CALCULATION);
		createDetailDefault(layout, layoutHistory, companyCode, CategoryAtr.ARTICLES, "F309", autoLineId6, 9,
				SumScopeAtr.EXCLUDED, CalculationMethod.SYSTEM_CALCULATION);
	}

	private void createDetailDefault(LayoutMaster layout, LayoutHistory layoutHistory, String companyCode,
			CategoryAtr cateAtr, String itemCode, String autoLineId, int itemPosColumn, SumScopeAtr sumAtr,
			CalculationMethod calMethod) {
		RangeChecker alarm = new RangeChecker(UseOrNot.DO_NOT_USE, UseOrNot.DO_NOT_USE,
				Range.between(BigDecimal.ZERO, BigDecimal.ZERO));
		RangeChecker error = new RangeChecker(UseOrNot.DO_NOT_USE, UseOrNot.DO_NOT_USE,
				Range.between(BigDecimal.ZERO, BigDecimal.ZERO));
		Distribute distribute = new Distribute(DistributeWay.CALCULATED_PERCENTAGE, DistributeSet.NOT_PROPORTIONAL);
		LayoutMasterDetail detailData = new LayoutMasterDetail(new CompanyCode(companyCode), layout.getStmtCode(),
				layoutHistory.getHistoryId(), cateAtr, new ItemCode(itemCode), new AutoLineId(autoLineId),
				new ItemPosColumn(itemPosColumn), alarm, calMethod, distribute, DisplayAtr.DISPLAY, error, sumAtr,
				new FormulaCode("00"), new PersonalWageCode("00"), new WtCode("00"),
				new CommonAmount(new BigDecimal("0")), new ItemCode("0000"), CommuteAtr.TRANSPORTATION_EQUIPMENT);

		this.detailRepo.add(detailData);
	}

	private void createLineDefault(LayoutMaster layout, LayoutHistory layoutHistory, String companyCode,
			CategoryAtr cateAtr, String autoLine, int linePos) {
		LayoutMasterLine line = new LayoutMasterLine(new CompanyCode(companyCode), layout.getStmtCode(),
				new AutoLineId(autoLine), cateAtr, LineDispAtr.ENABLE, new LinePosition(linePos),
				layoutHistory.getHistoryId());
		this.lineRepo.add(line);
	}

	private void copyNewData(CreateLayoutCommand command, String companyCode, LayoutMaster layout,
			LayoutHistory layoutHistory) {
		// List<LayoutMasterCategory> categoriesOrigin =
		// categoryRepo.getCategories(companyCode, command.getStmtCodeCopied(),
		// command.getStartYmCopied());
		// [明細書マスタカテゴリ]の明細書コード = G_SEL_002で選択している項目の明細書コード AND [明細書マスタカテゴリ]の終了年月
		// = 999912
		// List<LayoutMasterCategory> categoriesOrigin =
		// categoryRepo.getCategoriesBefore(companyCode,
		// command.getStmtCodeCopied(), layoutHistory.getHistoryId());
		List<LayoutMasterCategory> categoriesOrigin = categoryRepo.getCategoriesBefore(companyCode,
				command.getStmtCodeCopied());
		if (!categoriesOrigin.isEmpty()) {
			List<LayoutMasterCategory> categoriesNew = categoriesOrigin.stream().map(org -> {
				return LayoutMasterCategory.createFromDomain(org.getCompanyCode(),
						new LayoutCode(command.getStmtCode()), org.getCtAtr(), org.getCtgPos(),
						layoutHistory.getHistoryId());
			}).collect(Collectors.toList());
			categoryRepo.add(categoriesNew);
		}

		// [明細書マスタ行]の明細書コード = G_SEL_002で選択している項目の明細書コード AND [明細書マスタ行]の終了年月 =
		// 999912
		// anh lam truoc khi thay doi DB
		//List<LayoutMasterLine> linesOrigin = lineRepo.getLinesBefore(companyCode, command.getStmtCodeCopied(),layoutHistory.getHistoryId());
		// Lanlt 16.3.2017
		List<LayoutMasterLine> linesOrigin = lineRepo.getLinesBefore(companyCode, command.getStmtCodeCopied());
		if (!linesOrigin.isEmpty()) {
			List<LayoutMasterLine> linesNew = linesOrigin.stream().map(org -> {
				return LayoutMasterLine.createFromDomain(org.getCompanyCode(), org.getStmtCode(), org.getAutoLineId(),
						org.getCategoryAtr(), org.getLineDisplayAttribute(), org.getLinePosition(),
						layoutHistory.getHistoryId());
			}).collect(Collectors.toList());
			lineRepo.add(linesNew);
		}

		// [明細書マスタ明細]の明細書コード = G_SEL_002で選択している項目の明細書コードAND [明細書マスタ明細]の終了年月 =
		// 999912
		// anh Lam 16.3
		// List<LayoutMasterDetail> detailsOrigin =
		// detailRepo.getDetailsBefore(companyCode, command.getStmtCodeCopied(),
		// layoutHistory.getHistoryId());
		List<LayoutMasterDetail> detailsOrigin = detailRepo.getDetailsBefore(companyCode, command.getStmtCodeCopied());
		if (!detailsOrigin.isEmpty()) {
			List<LayoutMasterDetail> detailsNew = detailsOrigin.stream().map(org -> {
				return LayoutMasterDetail.createFromDomain(org.getCompanyCode(), new LayoutCode(command.getStmtCode()),
						org.getCategoryAtr(), org.getItemCode(), org.getAutoLineId(), org.getItemPosColumn(),
						org.getError(), org.getCalculationMethod(), org.getDistribute(), org.getDisplayAtr(),
						org.getAlarm(), org.getSumScopeAtr(), org.getSetOffItemCode(), org.getCommuteAtr(),
						org.getFormulaCode(), org.getPersonalWageCode(), org.getWageTableCode(), org.getCommonAmount(),
						layoutHistory.getHistoryId());
			}).collect(Collectors.toList());

			detailRepo.add(detailsNew);
			System.out.println(detailsNew.get(0));
		}
	}

}
