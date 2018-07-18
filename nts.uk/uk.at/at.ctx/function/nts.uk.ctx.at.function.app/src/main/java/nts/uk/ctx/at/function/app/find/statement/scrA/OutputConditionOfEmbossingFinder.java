/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.statement.scrA;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.holidaysremaining.PermissionOfEmploymentForm;
import nts.uk.ctx.at.function.dom.holidaysremaining.repository.PermissionOfEmploymentFormRepository;
import nts.uk.ctx.at.function.dom.statement.StampingOutputItemSetRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.CurrentMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class OutputConditionOfEmbossingFinder.
 */
@Stateless
public class OutputConditionOfEmbossingFinder {
	
	/** The permission of employment form repository. */
	@Inject
	private PermissionOfEmploymentFormRepository permissionOfEmploymentFormRepository;
	
	@Inject
	private ClosureRepository closureRepository;
	
	@Inject
	private ClosureService closureService;
	
	@Inject
	private StampingOutputItemSetRepository stampingOutputItemSetRepository; 
	
	/** The Constant FUNCTION_NO. */
	private static final Integer FUNCTION_NO = 5;
	
	private static final Integer CLOSURE_ID = 1;
	
	/**
	 * Start scr.
	 *
	 * @return the output condition of embossing dto
	 */
	// 起動する(khởi động)
	public OutputConditionOfEmbossingDto startScr() {
		
		OutputConditionOfEmbossingDto dto = new OutputConditionOfEmbossingDto();
		
		String companyID = AppContexts.user().companyId();
		String roleId = AppContexts.user().roles().forAttendance();
		
		// ドメインモデル「就業帳票の権限」を取得する(get domain model 「就業帳票の権限」)
		Optional<PermissionOfEmploymentForm> optPermissionOfEmploymentForm = permissionOfEmploymentFormRepository.find(companyID, roleId, FUNCTION_NO);
		
		
		// ユーザ固有情報「打刻一覧の出力条件」を取得する(get thông tin riêng biệt user 「打刻一覧の出力条件」)
		// xu li o frontend: characteristic
		
		// ドメインモデル「締め」を取得する(get domain model「締め」)
		Optional<Closure> optclosure = closureRepository.findById(companyID, CLOSURE_ID);
		CurrentMonth curMonthClosure;
		if (optclosure.isPresent()) {
			curMonthClosure = optclosure.get().getClosureMonth();
			// アルゴリズム「当月の期間を算出する」を実行する(thực hiện thuật toán 「当月の期間を算出する」)
			
			Closure closure = optclosure.get();
			DatePeriod datePeriod = closureService.getClosurePeriodNws(CLOSURE_ID, closure.getClosureMonth().getProcessingYm());
			dto.setStartDate(datePeriod.start());
			dto.setEndDate(datePeriod.end());
		} 
		
		// ドメインモデル「打刻一覧出力項目設定」を取得する(get domain model 「打刻一覧出力項目設定」)
		dto.setLstStampingOutputItemSetDto(stampingOutputItemSetRepository.getByCid(companyID).stream().map(domain -> {
																				StampingOutputItemSetDto outputItemSetDto = new StampingOutputItemSetDto();
																				outputItemSetDto.setStampOutputSetCode(domain.getStampOutputSetCode().v());
																				outputItemSetDto.setStampOutputSetName(domain.getStampOutputSetName().v());
																				return outputItemSetDto;
																			}).collect(Collectors.toList()));
		return dto;
	}
}
