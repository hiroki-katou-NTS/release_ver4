package nts.uk.ctx.at.shared.app.query.workrule.closure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
/**
 * 基準日の会社の締めをすべて取得する
 * @author tutk
 *
 */
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class GetCloseOnKeyDate {
	@Inject
	
	private ClosureRepository closureRepo;
	public List<Closure> getCloseOnKeyDate(GeneralDate baseDate){
		//ドメインモデル「締め」を全て取得する
		List<Closure> closures = this.closureRepo.findAllActive(AppContexts.user().companyId(),
				UseClassification.UseClass_Use);
		//取得したドメインモデル「締め」の基準日時点のドメインモデル「締め変更履歴」を取得する
		List<Closure> resultList = new ArrayList<>();
		closures.forEach(clo -> {
			Optional<ClosureHistory> history = clo.getClosureHistories().stream().filter(his -> his.getClosureYMD().beforeOrEquals(baseDate)).findFirst();
			if(history.isPresent()) {
				resultList.add(clo);
			}
		});
		//取得した「締め」をリストで返す
		return resultList;
	}

}
