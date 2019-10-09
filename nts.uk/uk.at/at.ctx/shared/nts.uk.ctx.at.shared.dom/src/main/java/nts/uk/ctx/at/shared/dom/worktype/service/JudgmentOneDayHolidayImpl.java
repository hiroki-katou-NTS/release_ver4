package nts.uk.ctx.at.shared.dom.worktype.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JudgmentOneDayHolidayImpl implements JudgmentOneDayHoliday {

	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Override
	public boolean judgmentOneDayHoliday(String companyID, String workTypeCD) {
		// 休日フラグ=false(初期化)
		boolean result = false;
		// ドメイン「勤務種類」を取得する
		Optional<WorkType> opWorkType = workTypeRepository.findByPK(companyID, workTypeCD);
		if(!opWorkType.isPresent()){
			return result;
		}
		WorkType workType = opWorkType.get();
		// 勤務区分チェック
		if(workType.getDailyWork().getWorkTypeUnit()!=WorkTypeUnit.OneDay){
			return result;
		}
		// 1日をチェックする
		if(workType.getDailyWork().getOneDay()==WorkTypeClassification.Holiday){
			// 休日フラグ=true
			result = true;
			return result;
		}
		return result;
	}

}
