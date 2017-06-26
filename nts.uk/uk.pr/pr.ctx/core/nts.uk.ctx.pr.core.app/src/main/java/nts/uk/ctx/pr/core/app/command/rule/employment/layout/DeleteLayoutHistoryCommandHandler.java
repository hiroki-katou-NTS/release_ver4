package nts.uk.ctx.pr.core.app.command.rule.employment.layout;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMaster;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutMasterRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategory;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.category.LayoutMasterCategoryRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetail;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.detail.LayoutMasterDetailRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLine;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.line.LayoutMasterLineRepository;

@Stateless
@Transactional
public class DeleteLayoutHistoryCommandHandler extends CommandHandler<DeleteLayoutHistoryCommand> {

	
	@Inject
	private LayoutHistRepository layoutHistRepository;
	@Inject
	private LayoutMasterRepository layoutRepository;
	@Inject
	private LayoutMasterCategoryRepository categoryRepository;
	@Inject
	private LayoutMasterLineRepository lineRepository;
	@Inject
	private LayoutMasterDetailRepository detailRepository;
	@Override
	protected void handle(CommandHandlerContext<DeleteLayoutHistoryCommand> context) {
		DeleteLayoutHistoryCommand command = context.getCommand();
		//データベース削除
		deleteObject(command);
		//データベース更新
		updateObject(command);
	}
	
	private void deleteObject(DeleteLayoutHistoryCommand command){
		//データベース削除[明細書マスタ.DEL-1]を実施する
//		this.layoutRepository.remove(command.getCompanyCode(),
//				command.getStmtCode(),
//				command.getHistoryId());
		this.layoutRepository.remove(command.getCompanyCode(),
				command.getStmtCode());
		this.layoutHistRepository.remove(command.getCompanyCode(), command.getStmtCode(), command.getHistoryId());
		//1件成功した場合以外の場合
		//bo sung them
		
		//データベース削除[明細書マスタカテゴリ.DEL-2] を実施する
//		List<LayoutMasterCategory> lstCategories = this.categoryRepository.getCategories(command.getCompanyCode(),
//				command.getStmtCode(), 
//				command.getStartYm());
		List<LayoutMasterCategory> lstCategories = this.categoryRepository.getCategories(command.getCompanyCode(),
				command.getStmtCode(), 
				command.getHistoryId());
		this.categoryRepository.removeAllCategory(lstCategories);
		//1件成功した場合以外の場合
		//bo sung them
		
		//データベース削除[明細書マスタ行.DEL-2] を実施する
		List<LayoutMasterLine> lstLineOfCategory = this.lineRepository.getLines(command.getCompanyCode(), 
				command.getStmtCode(), 
				command.getHistoryId());
		this.lineRepository.remove(lstLineOfCategory);
		
		
		//データベース削除[明細書マスタ明細.DEｌ-1] を実施する
		List<LayoutMasterDetail> lstDetails = this.detailRepository.getDetails(command.getCompanyCode(),
				command.getStmtCode(),
				command.getStartYm());				
		this.detailRepository.remove(lstDetails);
	}
	
	private void updateObject(DeleteLayoutHistoryCommand command){
		//データベース更新[明細書マスタ.UPD-2] を実施する
		// anh Lam chua thay doi DB
		// Optional<LayoutMaster> layoutBefore =
		// layoutRepository.getHistoryBefore(command.getCompanyCode(),
		// command.getStmtCode());
		// if (layoutBefore != null
		// && layoutBefore.isPresent()) {
		// LayoutMaster layout = layoutBefore.get();
		// layout.setEndYm(new YearMonth(command.getEndYm()));
		// layoutRepository.update(layout);
		// }
		Optional<LayoutHistory> layoutHistBefore = layoutHistRepository.getHistoryBefore(command.getCompanyCode(),
				command.getStmtCode(),
				command.getStartYm() - 1);
		if (layoutHistBefore != null
			&& layoutHistBefore.isPresent()) {
			LayoutHistory layoutHist = layoutHistBefore.get();
			layoutHist.setEndYm(new YearMonth(command.getEndYm()));
			layoutHistRepository.update(layoutHist);
		}
		
		//データベース更新[明細書マスタカテゴリ.UPD-2] を実施する
		List<LayoutMasterCategory> lstCategoryBefore = this.categoryRepository.getCategoriesBefore(command.getCompanyCode(),
				command.getStmtCode(), command.getHistoryId());
		if(!lstCategoryBefore.isEmpty()){
			List<LayoutMasterCategory> lstCategoryUP = lstCategoryBefore.stream().map(
					category -> {
						return LayoutMasterCategory.createFromDomain(category.getCompanyCode(),
								category.getStmtCode(),
								category.getCtAtr(),
								category.getCtgPos(),
								category.getHistoryId());
					}).collect(Collectors.toList());
			this.categoryRepository.update(lstCategoryUP);
		}
		
		
		//データベース更新[明細書マスタ行.UPD-2] を実施する
//		List<LayoutMasterLine> lstLineBefore = this.lineRepository.getLinesBefore(command.getCompanyCode(),
//				command.getStmtCode(),
//				command.getStartYm() - 1);
		List<LayoutMasterLine> lstLineBefore = this.lineRepository.getLinesBefore(command.getCompanyCode(),
				command.getStmtCode(),
				command.getHistoryId());
		if(!lstLineBefore.isEmpty()){
			List<LayoutMasterLine> lstLineUp = lstLineBefore.stream().map(
					line ->{
						return LayoutMasterLine.createFromDomain(line.getCompanyCode(),
								line.getStmtCode(), 
								line.getAutoLineId(), 
								line.getCategoryAtr(), 
								line.getLineDisplayAttribute(), 
								line.getLinePosition(),
								line.getHistoryId());
					}).collect(Collectors.toList());
			this.lineRepository.update(lstLineUp);	
		}
		
		//データベース更新[明細書マスタ明細.UPD-2] を実施する
//		List<LayoutMasterDetail> detailsBefore = detailRepository.getDetailsBefore(command.getCompanyCode(), 
//				command.getStmtCode(), 
//				command.getStartYm() - 1);
		List<LayoutMasterDetail> detailsBefore = detailRepository.getDetailsBefore(command.getCompanyCode(), 
				command.getStmtCode(), command.getHistoryId());
		if(!detailsBefore.isEmpty()){
			List<LayoutMasterDetail> detailsUpdate = detailsBefore.stream().map(
					org -> {
						return LayoutMasterDetail.createFromDomain(
								org.getCompanyCode(),
								org.getStmtCode(),
								org.getCategoryAtr(),
								org.getItemCode(),
								org.getAutoLineId(), 
								org.getItemPosColumn(), 
								org.getError(),
								org.getCalculationMethod(),
								org.getDistribute(),
								org.getDisplayAtr(),
								org.getAlarm(),
								org.getSumScopeAtr(),
								org.getSetOffItemCode(),
								org.getCommuteAtr(),
								org.getFormulaCode(),
								org.getPersonalWageCode(),
								org.getWageTableCode(),
								org.getCommonAmount(), 
								org.getHistoryId());
					}).collect(Collectors.toList());
			this.detailRepository.update(detailsUpdate);
		}
		
	}
	
}
