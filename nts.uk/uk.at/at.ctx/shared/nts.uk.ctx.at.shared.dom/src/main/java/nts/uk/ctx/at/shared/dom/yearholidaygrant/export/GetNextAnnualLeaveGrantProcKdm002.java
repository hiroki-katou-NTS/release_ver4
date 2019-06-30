package nts.uk.ctx.at.shared.dom.yearholidaygrant.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantHdTblSet;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantNum;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantReferenceDate;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantSimultaneity;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantYearHolidayRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceRepository;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.LengthServiceTbl;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.UseSimultaneousGrant;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.YearHolidayRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * Improve Performance for KDM002
 * 
 * @author HungTT
 */
@Stateless
public class GetNextAnnualLeaveGrantProcKdm002 {

	/** 年休付与テーブル設定 */
	@Inject
	private YearHolidayRepository yearHolidayRepo;
	/** 勤続年数テーブル */
	@Inject
	private LengthServiceRepository lengthServiceRepo;
	/** 年休付与テーブル */
	@Inject
	private GrantYearHolidayRepository grantYearHolidayRepo;

	/**
	 * 次回年休付与を取得する
	 * @param companyId 会社ID
	 * @param employeeIds 社員ID
	 * @param annualLeaveEmpBasicInfoMap 年休社員基本情報マップ
	 * @param entryDateMap 入社年月日マップ
	 * @param period 期間
	 * @param isSingleDay 単一日フラグ
	 * @return 次回年休付与マップ
	 */
	public Map<String, List<NextAnnualLeaveGrant>> algorithm(String companyId, List<String> employeeIds,
			Map<String, AnnualLeaveEmpBasicInfo> annualLeaveEmpBasicInfoMap, Map<String, GeneralDate> entryDateMap,
			DatePeriod period, boolean isSingleDay) {
		Map<String, List<NextAnnualLeaveGrant>> result = new HashMap<>();
		Set<String> grantTableCodeSet = annualLeaveEmpBasicInfoMap.values().stream()
				.map(i -> i.getGrantRule().getGrantTableCode().v()).collect(Collectors.toSet());
		Map<String, List<LengthServiceTbl>> lengthServiceTblMap = lengthServiceRepo.findByCode(companyId,
				new ArrayList<>(grantTableCodeSet));
		Map<String, GrantHdTblSet> grantHdTblSetMap = yearHolidayRepo.findAll(companyId).stream()
				.filter(i -> grantTableCodeSet.contains(i.getYearHolidayCode().v()))
				.collect(Collectors.toMap(a -> a.getYearHolidayCode().v(), a -> a));
		for (String empId : employeeIds) {
			List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList = new ArrayList<>();
			AnnualLeaveEmpBasicInfo annualLeaveEmpBasicInfo = annualLeaveEmpBasicInfoMap.get(empId);
			if (annualLeaveEmpBasicInfo == null) {
				result.put(empId, nextAnnualLeaveGrantList);
				continue;
			}
			GeneralDate entryDate = entryDateMap.get(empId);
			GeneralDate criteriaDate = annualLeaveEmpBasicInfo.getGrantRule().getGrantStandardDate();
			String grantTableCode = annualLeaveEmpBasicInfo.getGrantRule().getGrantTableCode().v();
			// 「年休付与テーブル設定」を取得する
			GrantHdTblSet grantHdTblSet = grantHdTblSetMap.get(grantTableCode);
			if (grantHdTblSet == null) {
				result.put(empId, nextAnnualLeaveGrantList);
				continue;
			}
			// 一斉付与日 確認
			Optional<Integer> simultaneousGrantMDOpt = Optional.empty();
			if (grantHdTblSet.getUseSimultaneousGrant() == UseSimultaneousGrant.USE) {
				simultaneousGrantMDOpt = Optional.of(grantHdTblSet.getSimultaneousGrandMonthDays());
			}
			// 「勤続年数テーブル」を取得する
			List<LengthServiceTbl> lengthServiceTbls = lengthServiceTblMap.get(grantTableCode);
			if (lengthServiceTbls.size() <= 0) {
				result.put(empId, nextAnnualLeaveGrantList);
				continue;
			}
			// 年休付与年月日を計算
			this.calcAnnualLeaveGrantDate(entryDate, criteriaDate, simultaneousGrantMDOpt, lengthServiceTbls, period,
					isSingleDay, nextAnnualLeaveGrantList);
			for (val nextAnnualLeaveGrant : nextAnnualLeaveGrantList) {
				// 付与回数をもとに年休付与テーブルを取得
				val grantTimes = nextAnnualLeaveGrant.getTimes().v();
				val grantHdTblOpt = this.grantYearHolidayRepo.find(companyId, 1, grantTableCode, grantTimes);

				// 次回年休付与に付与日数・半日年休上限回数・時間年休上限日数をセット
				if (!grantHdTblOpt.isPresent())
					continue;
				val grantHdTbl = grantHdTblOpt.get();
				nextAnnualLeaveGrant.setGrantDays(grantHdTbl.getGrantDays());
				nextAnnualLeaveGrant.setHalfDayAnnualLeaveMaxTimes(grantHdTbl.getLimitDayYear());
				nextAnnualLeaveGrant.setTimeAnnualLeaveMaxDays(grantHdTbl.getLimitTimeHd());
			}
			// 次回年休付与を返す
			result.put(empId, nextAnnualLeaveGrantList);
		}
		return result;
	}

	/**
	 * 年休付与年月日を計算
	 * @param entryDate 入社年月日
	 * @param criteriaDate 年休付与基準日
	 * @param simultaneousGrantMDOpt 一斉付与年月
	 * @param lengthServiceTbls 勤続年数テーブルリスト
	 * @param period 期間
	 * @param isSingleDay 単一日フラグ
	 * @param nextAnnualLeaveGrantList 次回年休付与リスト
	 */
	public void calcAnnualLeaveGrantDate(GeneralDate entryDate, GeneralDate criteriaDate,
			Optional<Integer> simultaneousGrantMDOpt, List<LengthServiceTbl> lengthServiceTbls, DatePeriod period,
			boolean isSingleDay, List<NextAnnualLeaveGrant> nextAnnualLeaveGrantList) {

		GeneralDate previousDate = null;
		for (val lengthServiceTbl : lengthServiceTbls) {

			// 付与日を計算
			val nextAnnualLeaveGrant = this.calcGrantDate(lengthServiceTbl, Optional.empty(),
					entryDate, criteriaDate, simultaneousGrantMDOpt, previousDate);

			// 次回付与日が前回付与日を超えているかどうかチェック
			boolean isExceed = true;
			if (previousDate != null) {
				if (nextAnnualLeaveGrant.getGrantDate().beforeOrEquals(previousDate)) {
					isExceed = false;
				}
			}
			if (isExceed == true) {
				
				// 期間中の年休付与かチェック
				val grantDate = nextAnnualLeaveGrant.getGrantDate();
				if (grantDate.after(period.end())) {
					// 期間より後
					return;
				}
				else if (period.contains(grantDate)) {
					// 期間内

					// 「次回年休付与リスト」に追加
					nextAnnualLeaveGrantList.add(nextAnnualLeaveGrant);

					// 「単一日フラグ」をチェック
					if (isSingleDay) return;
				}

				// 前回付与日 ← 次回年休付与．付与年月日
				if (previousDate == null) {
					previousDate = nextAnnualLeaveGrant.getGrantDate();
				}
				else if (previousDate.before(nextAnnualLeaveGrant.getGrantDate())) {
					previousDate = nextAnnualLeaveGrant.getGrantDate();
				}
			}
		}

		// 勤続年数データの最終データの年数に+1しながら、付与日を求める （上限 99 年）
		val lastLengthServiceTbl = lengthServiceTbls.get(lengthServiceTbls.size() - 1);
		Integer calcYears = lastLengthServiceTbl.getYear().v();
		while (calcYears < 100) {

			// 付与日を計算
			val nextAnnualLeaveGrant = this.calcGrantDate(lastLengthServiceTbl, Optional.of(calcYears),
					entryDate, criteriaDate, simultaneousGrantMDOpt, previousDate);

			// 次回付与日が前回付与日を超えているかどうかチェック
			boolean isExceed = true;
			if (previousDate != null) {
				if (nextAnnualLeaveGrant.getGrantDate().beforeOrEquals(previousDate)) {
					isExceed = false;
				}
			}
			if (isExceed == true) {

				// 期間中の年休付与かチェック
				val grantDate = nextAnnualLeaveGrant.getGrantDate();
				if (grantDate.after(period.end())) {
					// 期間より後
					return;
				}
				else if (period.contains(grantDate)) {
					// 期間内

					// 「次回年休付与リスト」に追加
					nextAnnualLeaveGrantList.add(nextAnnualLeaveGrant);

					// 「単一日フラグ」をチェック
					if (isSingleDay) return;
				}

				// 前回付与日 ← 次回年休付与．付与年月日
				if (previousDate == null) {
					previousDate = nextAnnualLeaveGrant.getGrantDate();
				}
				else if (previousDate.before(nextAnnualLeaveGrant.getGrantDate())) {
					previousDate = nextAnnualLeaveGrant.getGrantDate();
				}
			}

			// 処理中の年数を+1して再度ループ
			calcYears++;
		}
	}

	/**
	 * 付与日を計算
	 * @param lengthServiceTbl 勤続年数
	 * @param calcYears 計算年数
	 * @param entryDate 入社年月日
	 * @param criteriaDate 基準年月日
	 * @param simultaneousGrantMDOpt 一斉付与年月
	 * @param previousDate 前回付与日
	 * @return 次回年休付与
	 */
	private NextAnnualLeaveGrant calcGrantDate(LengthServiceTbl lengthServiceTbl,
			Optional<Integer> calcYears, GeneralDate entryDate, GeneralDate criteriaDate,
			Optional<Integer> simultaneousGrantMDOpt, GeneralDate previousDate) {

		NextAnnualLeaveGrant nextAnnualLeaveGrant = new NextAnnualLeaveGrant();

		// 勤続年数から付与日を計算
		NextAnnualLeaveGrant calcResult = this.calcGrantDateFromLengthService(
				entryDate, lengthServiceTbl.getStandGrantDay(), criteriaDate, lengthServiceTbl, calcYears);
		GeneralDate grantDate = calcResult.getGrantDate();
		GrantNum grantNum = new GrantNum(calcResult.getTimes().v());

		// 「一斉付与する」をチェック および 「一斉付与日」が存在するかチェック
		if (lengthServiceTbl.getAllowStatus() == GrantSimultaneity.USE && simultaneousGrantMDOpt.isPresent()) {

			// 一斉付与の計算
			NextAnnualLeaveGrant simulResult = this.calcSimultaneousGrant(
					previousDate, entryDate, grantDate, lengthServiceTbl, simultaneousGrantMDOpt);
			grantDate = simulResult.getGrantDate();
			grantNum = new GrantNum(simulResult.getTimes().v());
		}

		// 次回年休付与を返す
		nextAnnualLeaveGrant.setGrantDate(grantDate);
		nextAnnualLeaveGrant.setTimes(grantNum);
		return nextAnnualLeaveGrant;
	}
	
	/**
	 * 勤続年数から付与日を計算
	 * @param entryDate 入社年月日
	 * @param grantReferenceDate 付与基準日（年休付与テーブル）
	 * @param criteriaDate 基準年月日
	 * @param lengthServiceTbl 勤続年数
	 * @param calcYears 計算年数
	 * @return 次回年休付与
	 */
	private NextAnnualLeaveGrant calcGrantDateFromLengthService(
			GeneralDate entryDate,
			GrantReferenceDate grantReferenceDate,
			GeneralDate criteriaDate,
			LengthServiceTbl lengthServiceTbl,
			Optional<Integer> calcYears){
		
		NextAnnualLeaveGrant result = new NextAnnualLeaveGrant();
		
		// 「付与基準日」をチェック → 付与計算基準日
		GeneralDate calcCriteria = entryDate;
		if (grantReferenceDate == GrantReferenceDate.YEAR_HD_REFERENCE_DATE) {
			calcCriteria = criteriaDate;
		}

		// 年休付与日を計算
		GeneralDate grantDate = calcCriteria.addYears(lengthServiceTbl.getYear().v());
		if (calcYears.isPresent()) grantDate = calcCriteria.addYears(calcYears.get());
		grantDate = grantDate.addMonths(lengthServiceTbl.getMonth().v());
		result.setGrantDate(grantDate);
		result.setTimes(lengthServiceTbl.getGrantNum());
		
		return result;
	}
	
	/**
	 * 一斉付与の計算
	 * @param previousDate 前回付与日
	 * @param entryDate 入社年月日
	 * @param lengthGrantDate 勤続年月の年休付与日
	 * @param lengthServiceTbl 勤続年数
	 * @param simultaneousGrantMDOpt 一斉付与年月
	 * @return 次回年休付与
	 */
	private NextAnnualLeaveGrant calcSimultaneousGrant(
			GeneralDate previousDate,
			GeneralDate entryDate,
			GeneralDate lengthGrantDate,
			LengthServiceTbl lengthServiceTbl,
			Optional<Integer> simultaneousGrantMDOpt){
		
		NextAnnualLeaveGrant result = new NextAnnualLeaveGrant();
		
		// 「前回付与日」が存在するかチェック → 計算開始日を計算
		GeneralDate calcStart = entryDate.addDays(1);
		if (previousDate != null) {
			calcStart = previousDate.addDays(1);
		}

		// 一斉付与年月を取得
		val simultaneousGrantMD = simultaneousGrantMDOpt.get();
		Integer simulMonth = simultaneousGrantMD / 100;
		Integer simulDay = simultaneousGrantMD % 100;

		// 勤続年月の年の、当年と前年の一斉付与日の計算
		GeneralDate currentSimul = GeneralDate.ymd(lengthGrantDate.year(), simulMonth, 1).addMonths(1);
		GeneralDate previousSimul = currentSimul.addYears(-1);
		currentSimul = currentSimul.addDays(-1);
		previousSimul = previousSimul.addDays(-1);
		if (currentSimul.day() > simulDay) {
			currentSimul = GeneralDate.ymd(currentSimul.year(), currentSimul.month(), simulDay);
		}
		if (previousSimul.day() > simulDay) {
			previousSimul = GeneralDate.ymd(previousSimul.year(), previousSimul.month(), simulDay);
		}

		// 一斉付与日<=勤続年月の年休付与日 となる一番遅い年月日
		GeneralDate grantDate = currentSimul;
		if (grantDate.after(lengthGrantDate)) {
			grantDate = previousSimul;
		}
		result.setGrantDate(grantDate);
		result.setTimes(lengthServiceTbl.getGrantNum());
		
		return result;
	}
}
