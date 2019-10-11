package nts.uk.screen.at.app.ktgwidget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.management.RuntimeErrorException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.app.query.workrule.closure.ClosureResultModel;
import nts.uk.ctx.at.shared.app.query.workrule.closure.WorkClosureQueryProcessor;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.DailyPerformanceAdapter;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.shr.com.context.AppContexts;
/**
 * @author thanhpv
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class KTG002QueryProcessor {
	
	@Inject
	private DailyPerformanceAdapter dailyPerformanceAdapter;
	
	@Inject
	private WorkClosureQueryProcessor workClosureQueryProcessor;
	
	@Inject
	private ShClosurePub shClosurePub;

	
	public boolean checkDataApprove() {
		
		String cid = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		List<GeneralDate> listDate = new ArrayList<>();
		//アルゴリズム「会社の締めを取得する」を実行する
		// Lấy RQ 140
		List<ClosureResultModel> rq140 = workClosureQueryProcessor.findClosureByReferenceDate(GeneralDate.today());
		List<Integer> listClosureID = rq140.stream().map(c ->c.getClosureId()).collect(Collectors.toList());
		//取得した締めIDのリストでループする
		for (Integer integer : listClosureID) {
			// アルゴリズム「処理年月と締め期間を取得する」を実行する
			// (Thực thi xử lý lấy thời gian quyết toán và tháng năm xử lý)
			Optional<PresentClosingPeriodExport> presentClosingPeriod = shClosurePub.find(cid, integer);
			if (presentClosingPeriod.isPresent()) {
				listDate.add(presentClosingPeriod.get().getClosureStartDate());
			}
		}
		Optional<GeneralDate> startDate = listDate.stream().min(Comparator.comparing(GeneralDate:: date));
		if(startDate.isPresent()){
			//・年月日（開始日）　　＜＝　締め開始日（取得した一番小さい締め開始日）
			//・年月日（終了日）　　＜＝　締め開始日（取得した一番小さい締め開始日） + ２年 - １日
			GeneralDate endDate = startDate.get().addYears(2).addDays(-1);
				boolean checkDateApproved = dailyPerformanceAdapter.checkDataApproveed(startDate.get(), endDate, employeeID, 0, cid);
			return checkDateApproved;
		}else{
			throw new RuntimeErrorException(new Error(),"Can't get Start Date");
		}
	}
}
