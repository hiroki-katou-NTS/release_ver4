package nts.uk.ctx.at.record.pubimp.remainnumber.annualleave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoDomService;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.basicinfo.CalcNextAnnualLeaveGrantDate;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.basicinfo.valueobject.AnnLeaRemNumValueObject;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.GetAnnLeaRemNumWithinPeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.TempAnnualLeaveMngMode;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.CheckShortageFlex;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.AggrResultOfAnnualLeaveEachMonth;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.AnnLeaveOfThisMonth;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.AnnLeaveRemainNumberPub;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.ClosurePeriodEachYear;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.NextAnnualLeaveGrant;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AnnLeaveRemainNumberPubImpl implements AnnLeaveRemainNumberPub {

	@Inject
	private AnnLeaEmpBasicInfoDomService annLeaService;

	@Inject
	private GetClosureStartForEmployee closureStartService;

	@Inject
	private CheckShortageFlex checkShortageFlex;

	@Inject
	private GetAnnLeaRemNumWithinPeriod getAnnLeaRemNumWithinPeriod;

	@Inject
	private AnnLeaEmpBasicInfoRepository annLeaBasicInfoRepo;

	@Inject
	private CalcNextAnnualLeaveGrantDate calcNextAnnualLeaveGrantDate;

	@Inject
	private Closure closureService;

	@Inject
	private GetClosurePeriod getClosurePeriod;

	@Override
	public AnnLeaveOfThisMonth getAnnLeaveOfThisMonth(String employeeId) {
		String companyId = AppContexts.user().companyId();
		// 月初の年休残数を取得
		AnnLeaRemNumValueObject remainNumber = annLeaService.getAnnLeaveNumber(companyId, employeeId);
		// 計算した年休残数を出力用クラスにコピー
		GeneralDate startDate = closureStartService.algorithm(employeeId).get();
		// 社員に対応する締め期間を取得する
		DatePeriod datePeriod = checkShortageFlex.findClosurePeriod(employeeId, startDate);
		// 期間中の年休残数を取得
		Optional<AggrResultOfAnnualLeave> aggrResult = getAnnLeaRemNumWithinPeriod.algorithm(companyId, employeeId,
				datePeriod, TempAnnualLeaveMngMode.OTHER, datePeriod.end(), false, false, Optional.empty(),
				Optional.empty(), Optional.empty());
		AggrResultOfAnnualLeave annualLeave = new AggrResultOfAnnualLeave();
		// ドメインモデル「年休社員基本情報」を取得
		Optional<AnnualLeaveEmpBasicInfo> basicInfo = annLeaBasicInfoRepo.get(employeeId);
		// 次回年休付与を計算
		List<NextAnnualLeaveGrant> annualLeaveGrant = calcNextAnnualLeaveGrantDate.algorithm(companyId, employeeId,
				Optional.empty());
		// 当月年休を返す
		AnnLeaveOfThisMonth result = new AnnLeaveOfThisMonth(annualLeaveGrant.get(0).getGrantDate(),
				annualLeaveGrant.get(0).getGrantDays(), remainNumber.getDays(), remainNumber.getMinutes(),
				annualLeave.getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus().getUsedNumber()
						.getUsedDays().getUsedDays(),
				annualLeave.getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus().getUsedNumber()
						.getUsedTime(),
				annualLeave.getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus().getRemainingNumber()
						.getTotalRemainingDays(),
				annualLeave.getAsOfPeriodEnd().getRemainingNumber().getAnnualLeaveWithMinus().getRemainingNumber()
						.getTotalRemainingTime());
		return result;
	}

	@Override
	public List<AggrResultOfAnnualLeaveEachMonth> getAnnLeaveRemainAfterThisMonth(String employeeId,
			DatePeriod datePeriod) {
		String companyId = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.today();
		// 社員に対応する処理締めを取得する
		Optional<Closure> closure = checkShortageFlex.findClosureByEmployee(employeeId, baseDate);
		// 指定した年月の期間をすべて取得する
		List<DatePeriod> periodByYearMonth = closureService.getPeriodByYearMonth(datePeriod.end().yearMonth());
		if (periodByYearMonth == null || periodByYearMonth.size() == 0)
			return null;
		// 集計期間を計算する
		List<ClosurePeriod> listClosurePeriod = getClosurePeriod.get(companyId, employeeId,
				periodByYearMonth.get(periodByYearMonth.size() - 1).end(), Optional.empty(), Optional.empty(),
				Optional.empty());
		// 締め処理期間のうち、同じ年月の期間をまとめる
		Map<YearMonth, List<ClosurePeriod>> listMap = listClosurePeriod.stream()
				.collect(Collectors.groupingBy(ClosurePeriod::getYearMonth));

		List<ClosurePeriodEachYear> listClosurePeriodEachYear = new ArrayList<ClosurePeriodEachYear>();

		for (Map.Entry<YearMonth, List<ClosurePeriod>> item : listMap.entrySet()) {
			GeneralDate start = null, end = null;
			for (ClosurePeriod closurePeriodItem : item.getValue()) {
				for (AggrPeriodEachActualClosure actualClosureItem : closurePeriodItem.getAggrPeriods()) {
					if (start == null || start.compareTo(actualClosureItem.getPeriod().start()) > 0) {
						start = actualClosureItem.getPeriod().start();
					}
					if (end == null || end.compareTo(actualClosureItem.getPeriod().end()) < 0) {
						end = actualClosureItem.getPeriod().end();
					}
				}			
			}

			listClosurePeriodEachYear.add(new ClosurePeriodEachYear(item.getKey(), new DatePeriod(start, end)));
		}

		List<AggrResultOfAnnualLeaveEachMonth> result = new ArrayList<AggrResultOfAnnualLeaveEachMonth>();

		Optional<AggrResultOfAnnualLeave> aggrResultOfAnnualLeave = Optional.empty();
		for (ClosurePeriodEachYear item : listClosurePeriodEachYear) {
			// 期間中の年休残数を取得
			aggrResultOfAnnualLeave = getAnnLeaRemNumWithinPeriod.algorithm(companyId, employeeId, item.getDatePeriod(),
					TempAnnualLeaveMngMode.OTHER, item.getDatePeriod().end(), false, false, Optional.empty(),
					Optional.empty(), aggrResultOfAnnualLeave);
			// 結果をListに追加
			if (aggrResultOfAnnualLeave.isPresent()) {
				result.add(new AggrResultOfAnnualLeaveEachMonth(item.getYearMonth(), aggrResultOfAnnualLeave.get()));
			}
		}
		return result;
	}
}
