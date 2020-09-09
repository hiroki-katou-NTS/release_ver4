package nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export;

import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.text.IdentifierUtil;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.ConfirmLeavePeriod;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.CalcAnnLeaAttendanceRate;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.DividedDayEachProcess;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.GetAnnLeaRemNumWithinPeriodProc.RequireM3;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggregatePeriodWork;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.ComplileInPeriodOfSpecialLeaveParam;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.InPeriodOfSpecialLeaveResultInfor;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.SpecialLeaveAggregatePeriodWork;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.SpecialLeaveError;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.SpecialLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.SpecialLeaveGrantWork;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.SpecialLeaveInfo;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.export.param.SpecialLeaveLapsedWork;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;
import nts.uk.ctx.at.shared.dom.adapter.employee.AffCompanyHistSharedImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeRecordImport;
import nts.uk.ctx.at.shared.dom.adapter.employee.SClsHistImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.CalcNextAnnualLeaveGrantDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveRemainingHistory;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.AttendanceRate;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.SpecialVacationCD;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.YearDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveAppSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveGrantSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.export.NextSpecialLeaveGrant;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.ElapseYear;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDateTbl;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDateTblRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.AvailabilityPeriod;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.SpecialVacationDeadline;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.TimeLimitSpecification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureStartForEmployee;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.GrantDays;
import nts.uk.ctx.at.shared.dom.yearholidaygrant.export.NextAnnualLeaveGrant;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialHolidayInterimMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InforSpecialLeaveOfEmployeeSevice.RequireM1;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.ErrorFlg;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.GrantDaysInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.GrantDaysInforByDates;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InforSpecialLeaveOfEmployeeSevice;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialHolidayInfor;

/**
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.contexts.勤務実績.残数管理.残数管理.特別休暇管理.Export
 *
 */
public class SpecialLeaveManagementService {
	/**
	 * RequestList273 期間内の特別休暇残を集計する
	 * @return
	 */
	public static InPeriodOfSpecialLeaveResultInfor complileInPeriodOfSpecialLeave(
			RequireM5 require, 
			CacheCarrier cacheCarrier, 
			ComplileInPeriodOfSpecialLeaveParam param) {
		
		// 特別休暇の集計結果情報
		InPeriodOfSpecialLeaveResultInfor outputData = new InPeriodOfSpecialLeaveResultInfor();
		
		// 特別休暇コードから設定を取得（会社設定）
		Optional<SpecialHoliday> specialHolidayOpt
			= require.specialHoliday(param.getCid(), param.getSpecialLeaveCode());
		
		// ドメインモデル「特別休暇基本情報」を取得する
		Optional<SpecialLeaveBasicInfo> optBasicInfor = require.specialLeaveBasicInfo(param.getSid(), param.getSpecialLeaveCode(), UseAtr.USE);
		SpecialLeaveGrantSetting specialLeaveGrantSetting;
		if(!optBasicInfor.isPresent() ){
//				|| optBasicInfor.get().getUsed() == UseAtr.NOT_USE) {
//			RemainDaysOfSpecialHoliday remainDays = new RemainDaysOfSpecialHoliday(new SpecialHolidayRemainInfor(0, 0, 0), 0, Optional.empty(), new ArrayList<>());
//			InPeriodOfSpecialLeave specialLeaveInfor = new InPeriodOfSpecialLeave(new ArrayList<>(), remainDays, new ArrayList<>(), new ArrayList<>());
//			return new InPeriodOfSpecialLeaveResultInfor(specialLeaveInfor, Finally.empty(), Finally.of(param.getComplileDate().end().addDays(1)));
		
			// ドメインモデル「特別休暇付与設定」を取得する
			specialLeaveGrantSetting = optBasicInfor.get().getGrantSetting();
		}
		
		// 社員
		EmployeeImport employee
			= require.employeeInfo(cacheCarrier, param.getSid());
		
		// 「休暇の集計期間から入社前、退職後を除く」を実行する
		DatePeriod aggrPeriod = ConfirmLeavePeriod.sumPeriod(param.getComplileDate(), employee);
				if (aggrPeriod == null) return outputData;
		
		// パラメータ月次モード・その他モード
		InterimRemainMngMode interimRemainMngMode;
		if ( param.isMode() ){ 
			interimRemainMngMode = InterimRemainMngMode.MONTHLY;
		} else {
			interimRemainMngMode = InterimRemainMngMode.OTHER;
		}
		
		// 集計開始日時点の特休情報を作成
		SpecialLeaveInfo specialLeaveInfo = createInfoAsOfPeriodStart(
						require, 
						cacheCarrier, 
						param.getCid(), 
						param.getSid(),
						param.getComplileDate(),
						interimRemainMngMode,
						Optional.of(param.isOverwriteFlg()),
						Optional.of(param.getRemainData()),  // ooooo 要キャスト？Optional<List<SpecialHolidayInterimMngData>>
						param.getOptBeforeResult(),
						param.getSpecialLeaveCode());

		
		// 次回特休付与日を計算
		CalcNextSpecialLeaveGrantDate calcNextSpecialLeaveGrantDate
			= new CalcNextSpecialLeaveGrantDate();
		List<NextSpecialLeaveGrant> nextSpecialLeaveGrantList
			= calcNextSpecialLeaveGrantDate.algorithm(
				require, cacheCarrier, 
				param.getCid(), 
				param.getSid(), 
				param.getSpecialLeaveCode(), 
				Optional.of(param.getComplileDate()));
		
		// 特別休暇集計期間を作成
		List<SpecialLeaveAggregatePeriodWork> aggregateWork
			= createAggregatePeriod(
					require,
					cacheCarrier,
					param.getCid(), 
					param.getSid(),
					param.getSpecialLeaveCode(), 
					nextSpecialLeaveGrantList, 
					aggrPeriod);
		
		
		
		
		
		
		
		
		
		
//		// 暫定年休管理データを取得する
//		val tempAnnualLeaveMngs = getTempAnnualLeaveMngs(require, employeeId, aggrPeriod, mode, 
//															isOverWriteOpt, forOverWriteListOpt);
//
//		for (val aggregatePeriodWork : aggregateWork){
//
//			// 年休の消滅・付与・消化
//			aggrResult = annualLeaveInfo.lapsedGrantDigest(
//					require,
//					companyId, employeeId, aggregatePeriodWork,
//					tempAnnualLeaveMngs, isGetNextMonthData, isCalcAttendanceRate, aggrResult, annualLeaveSet);
//		}
//		
//		// 年休不足分を付与残数データとして作成する
//		aggrResult = createShortRemainingDatas(employeeId, companyId, aggrResult, isOutShortRemainOpt);
//		
//		// 年休不足分として作成した年休付与データを削除する
//		aggrResult = deleteDummyRemainingDatas(aggrResult);
//		
//		// 「年休の集計結果」を返す
//		return Optional.of(aggrResult);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
				
				
				
		
		
//		//管理データを取得する
//		ManagaData grantRemainData = getMngData(require, cacheCarrier, 
//				param.getCid(),
//				param.getSid(),
//				param.getSpecialLeaveCode(),
//				param.getComplileDate(),
//				param.getOptBeforeResult());		
//		
//		SpecialHolidayDataParam speDataParam = new SpecialHolidayDataParam(param.getCid(),
//				param.getSid(),
//				param.getComplileDate(),
//				param.getSpecialLeaveCode(),
//				param.isMode(),
//				param.isOverwriteFlg(),
//				param.getRemainData(),
//				param.getInterimSpecialData());
//		//特別休暇暫定データを取得する
//		SpecialHolidayInterimMngData specialHolidayInterimDataMng = specialHolidayData(require, speDataParam);
//		
//		SpecialLeaveGrantRemainingDataTotal speRemainData = new SpecialLeaveGrantRemainingDataTotal(grantRemainData.getRemainDatas(),
//				grantRemainData.lstGrantDataMemory,
//				grantRemainData.lstGrantDatabase);
//		//特休の使用数を求める
//		RemainDaysOfSpecialHoliday useInfor = getUseDays(param.getCid(),
//				param.getSid(),
//				param.getComplileDate(),
//				speRemainData, 
//				specialHolidayInterimDataMng.getLstSpecialInterimMng(),
//				specialHolidayInterimDataMng.getLstInterimMng());
//		InPeriodOfSpecialLeave getOffsetDay = getInforCurrentProcess(require, param, grantRemainData, specialHolidayInterimDataMng, speRemainData,
//				useInfor);
//		//特別休暇のエラーチェックをする
//		List<SpecialLeaveError> lstError = lstError(getOffsetDay);
//		//「特別休暇の集計結果情報」に集計結果を格納する
//		getOffsetDay.setLstError(lstError);
//		outputData.setAggSpecialLeaveResult(getOffsetDay);
//		InPeriodOfSpecialLeave speTmp = toInPeriodOfSpecialLeave(getOffsetDay);
//		speTmp = getInforNextProcess(require, cacheCarrier, param, grantRemainData, getOffsetDay, speTmp);
//		outputData.setAggSpecialLeaveNextResult(Finally.of(speTmp));
//		//翌月管理データ取得区分をチェックする
//		if(param.isMngAtr()) {
//			outputData.setAggSpecialLeaveResult(speTmp);
//		}
//		//次の集計期間の開始日を計算する
//		outputData.setNextDate(Finally.of(param.getComplileDate().end().addDays(1)));
//		return outputData;
		
		return null;
	}
	
	/**
	 * 集計開始日時点の特休情報を作成
	 * @param require
	 * @param cacheCarrier
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggrPeriod 集計期間（開始日、終了日）
	 * @param mode 実績のみ参照区分
	 * @param isOverWriteOpt 上書きフラグ
	 * @param forOverWriteListOpt　上書き用の暫定管理データ
	 * @param inPeriodOfSpecialLeaveResultInfor 前回の特別休暇の集計結果
	 * @param specialLeaveCode 特別休暇コード
	 * @return 特休情報
	 */
	private static SpecialLeaveInfo createInfoAsOfPeriodStart(
			RequireM5 require, 
			CacheCarrier cacheCarrier, 
			String companyId, 
			String employeeId,
			DatePeriod aggrPeriod,
			InterimRemainMngMode mode,
			Optional<Boolean> isOverWriteOpt,
			Optional<List<SpecialHolidayInterimMngData>> forOverWriteListOpt,
			Optional<InPeriodOfSpecialLeaveResultInfor> inPeriodOfSpecialLeaveResultInfor,
			int specialLeaveCode){
			
		SpecialLeaveInfo emptyInfo = new SpecialLeaveInfo();
		emptyInfo.setYmd(aggrPeriod.start());
		
		// 集計開始日時点の前回の特休の集計結果が存在するかチェック
		// 「前回の特休情報」を確認　（前回の特休の集計結果．特休情報（期間終了日の翌日開始時点））
		SpecialLeaveInfo prevSpecialLeaveInfo 
			= inPeriodOfSpecialLeaveResultInfor.map(c -> c.getAsOfStartNextDayOfPeriodEnd()).orElse(null);
		
		// 「開始日」と「特休情報．年月日」を比較
		boolean isSameInfo = false;
		if (prevSpecialLeaveInfo != null){
			if (aggrPeriod.start().equals(prevSpecialLeaveInfo.getYmd())){
				isSameInfo = true;
			}
		}
		if (isSameInfo){
			// 特別休暇付与残数データをもとに特別休暇情報を作成
			// 「前回の特休情報」を取得　→　取得内容をもとに特休情報を作成
			return createInfoFromRemainingData(
					companyId,
					employeeId,
					aggrPeriod.start(),
					prevSpecialLeaveInfo.getGrantRemainingList());
		}
		
//		// 過去月集計モードの判断
//		if (aggrPastMonthModeOpt.isPresent() && yearMonthOpt.isPresent()) {
//			if (aggrPastMonthModeOpt.get() == true) {
//				List<SpecialLeaveGrantRemaining> remainingDatas = new ArrayList<>();
//				
//				// 「特休付与残数履歴データ」を取得
//				List<SpecialLeaveRemainingHistory> remainHistList = require.SpecialLeaveRemainingHistory(
//						employeeId, yearMonthOpt.get());
//				if (remainHistList.size() > 0) {
//					// 付与日 ASC、期限切れ状態＝「使用可能」　を採用
//					remainHistList.sort((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()));
//					for (SpecialLeaveRemainingHistory remainHist : remainHistList) {
//						if (remainHist.getExpirationStatus() != LeaveExpirationStatus.AVAILABLE) continue;
//						
//						// 取得したドメインを特休付与残数データに変換
//						SpecialLeaveGrantRemaining remainData = new SpecialLeaveGrantRemaining(
//								SpecialLeaveGrantRemainingData.createFromHistory(remainHist));
//						remainingDatas.add(remainData);
//					}
//				}
//				
//				// 特休上限データを取得
//				val annLeaMaxDataOpt = require.SpecialLeaveMaxData(employeeId);
//
//				// 取得内容をもとに特休情報を作成
//				return createInfoFromRemainingData(remainingDatas, annLeaMaxDataOpt, aggrPeriod);
//			}
//		}
//		
//		// 「集計開始日を締め開始日とする」をチェック　（締め開始日としない時、締め開始日を確認する）
//		boolean isAfterClosureStart = false;
//		Optional<GeneralDate> closureStartOpt = Optional.empty();
////		if (!noCheckStartDate){
		
		boolean isAfterClosureStart = false;
		Optional<GeneralDate> closureStartOpt = Optional.empty();

		// 休暇残数を計算する締め開始日を取得する
		GeneralDate closureStart = null;	// 締め開始日
		{
			// 最新の締め終了日翌日を取得する
			Optional<ClosureStatusManagement> sttMng = require.latestClosureStatusManagement(employeeId);
			if (sttMng.isPresent()){
				closureStart = sttMng.get().getPeriod().end();
				if (closureStart.before(GeneralDate.max())){
					closureStart = closureStart.addDays(1);
				}
				closureStartOpt = Optional.of(closureStart);
			}
			else {
				
				//　社員に対応する締め開始日を取得する
				closureStartOpt = GetClosureStartForEmployee.algorithm(require, cacheCarrier, employeeId);
				if (closureStartOpt.isPresent()) closureStart = closureStartOpt.get();
			}
		}
		
		
		
		
		
		
		
		
		// 取得した締め開始日と「集計開始日」を比較
		if (closureStart != null){
			
			// 締め開始日＜集計開始日　か確認する
			if (closureStart.before(aggrPeriod.start())) isAfterClosureStart = true;
		}
			
		if (!closureStartOpt.isPresent()) closureStartOpt = Optional.of(aggrPeriod.start());
		
		if (isAfterClosureStart){
			// 締め開始日<集計開始日　の時
			
			// 開始日までの特休残数を計算　（締め開始日～集計開始日前日） 
			val aggrResultOpt = algorithm(require, cacheCarrier, companyId, employeeId, new DatePeriod(closureStartOpt.get(), 
					aggrPeriod.start().addDays(-1)), mode, aggrPeriod.start().addDays(-1),
					isGetNextMonthData, isCalcAttendanceRate, isOverWriteOpt, forOverWriteListOpt,
					Optional.empty(), Optional.of(true));
			
			if (!aggrResultOpt.isPresent()) return emptyInfo;
			val aggrResult = aggrResultOpt.get();
			
			// 特休情報（期間終了日の翌日開始時点）を取得
			val asOfPeriodEnd = aggrResult.getAsOfPeriodEnd();
			
			// 取得内容をもとに特休情報を作成
			return createInfoFromRemainingData(asOfPeriodEnd.getGrantRemainingList(), 
												Optional.of(asOfPeriodEnd.getMaxData()), aggrPeriod);
		}

		// 締め開始日>=集計開始日　or 締め開始日がnull　の時
		
		// 「特休付与残数データ」を取得
		List<SpecialLeaveGrantRemaining> remainingDatas = new ArrayList<>();
		for (val grantRemainingData : grantRemainingDatas){
			if (grantRemainingData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) continue;
			if (grantRemainingData.getGrantDate().after(closureStartOpt.get())) continue;
			if (grantRemainingData.getDeadline().before(closureStartOpt.get())) continue;
			remainingDatas.add(grantRemainingData);
		}
		
		// 「特休上限データ」を取得
		val annLeaMaxDataOpt = require.SpecialLeaveMaxData(employeeId);

		// 取得内容をもとに特休情報を作成
		return createInfoFromRemainingData(remainingDatas, annLeaMaxDataOpt, aggrPeriod);
	}
	
	/**
	 * 特休集計期間を作成
	 * @param require
	 * @param cacheCarrier
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param nextSpecialLeaveGrantList 次回特休付与リスト
	 * @param aggrPeriod 期間
	 * @return 特休集計期間WORKリスト
	 */
	private static List<SpecialLeaveAggregatePeriodWork> createAggregatePeriod(
			RequireM5 require, 
			CacheCarrier cacheCarrier, 
			String companyId, 
			String employeeId,
			int specialLeaveCode,
			List<NextSpecialLeaveGrant> nextSpecialLeaveGrantList, 
			DatePeriod aggrPeriod){
		
		List<SpecialLeaveAggregatePeriodWork> aggregatePeriodWorks = new ArrayList<>();
		
		// 期間終了日翌日
		GeneralDate nextDayOfPeriodEnd = aggrPeriod.end();
		if (nextDayOfPeriodEnd.before(GeneralDate.max())){
			nextDayOfPeriodEnd = nextDayOfPeriodEnd.addDays(1);
		}
		
		// 消滅日で期間を区切る
		
		// ドメインモデル「特別休暇付与残数データ」を取得
		List<SpecialLeaveGrantRemainingData> lstSpeData 
			= require.specialLeaveGrantRemainingData(
				employeeId, specialLeaveCode,
				aggrPeriod, aggrPeriod.start(), LeaveExpirationStatus.AVAILABLE);
		
		// Comparator作成　期限日、付与日
		Comparator<SpecialLeaveGrantRemainingData> comparator =
		  Comparator.comparing(SpecialLeaveGrantRemainingData::getDeadline).thenComparing(SpecialLeaveGrantRemainingData::getGrantDate);
		
		// ソート処理 　期限日、付与日
		List<SpecialLeaveGrantRemainingData> sortedLstSpeData 
			= lstSpeData.stream().sorted(comparator).collect(Collectors.toList());
		
		// 処理単位分割日リスト
		Map<GeneralDate, SpecialLeaveDividedDayEachProcess> dividedDayMap = new HashMap<>();
		
		sortedLstSpeData.forEach( c->{
			
			// 消滅情報WORKを作成
			SpecialLeaveLapsedWork specialLeaveLapsedWork = new SpecialLeaveLapsedWork();
			// 消滅情報WORK.期間の開始日に消滅するかどうか←true
			specialLeaveLapsedWork.setLapsedAtr(true);
			
			// 期限日
			val deadline = c.getDeadline();
			
			// 期限日>=開始日 && 期限日<=終了日 が処理対象
			if (!aggrPeriod.contains(deadline)) continue;
			
			// 年月日←期限日の翌日
			val nextDayOfDeadLine = deadline;
			if (deadline.before(GeneralDate.max())){
				nextDayOfDeadLine = deadline.addDays(1);
				
			}
			
			
//			// ※既に同じ年月日がある場合は、追加せずに消滅情報WORKのみセット
//			if ( dividedDayMap.
			
					
					
					
					
					
					
					
					
//			// 処理単位分割日
//			SpecialLeaveAggregatePeriodWork specialLeaveAggregatePeriodWork
//				= dividedDayMap
//			
//			
//			
//			
//			specialLeaveAggregatePeriodWork.setLapsedWork(specialLeaveLapsedWork);
			
			
//			dividedDayMap.putIfAbsent(grantDate, new DividedDayEachProcess(grantDate));
//			dividedDayMap.get(grantDate).setGrantAtr(true);
//			dividedDayMap.get(grantDate).setNextAnnualLeaveGrant(Optional.of(nextAnnualLeaveGrant));

			
//			//リストへ追加
//			aggregatePeriodWorks.add(specialLeaveAggregatePeriodWork);
		});

		// 付与日で期間を区切る ----------------------------
		
		// パラメータ「List<次回年休付与>」を取得
		// 【条件】
		// 付与年月日>=パラメータ「開始日」の翌日
		// 付与年月日<=パラメータ「終了日」の翌日
		GeneralDate nextDayStart = aggrPeriod.start().addDays(1);
		GeneralDate nextDayEnd = aggrPeriod.end().addDays(1);
		
		List<NextSpecialLeaveGrant> nextSpecialLeaveGrantList_period
			= nextSpecialLeaveGrantList.stream()
				.filter(c -> c.getGrantDate().afterOrEquals(nextDayStart))
				.filter(c -> c.getGrantDate().beforeOrEquals(nextDayEnd))
				.collect(Collectors.toList());
		
		// 付与情報WORKを作成
		nextSpecialLeaveGrantList_period
			.forEach(c -> {
				SpecialLeaveGrantWork specialLeaveGrantWork
					= new SpecialLeaveGrantWork();
				
				// 付与情報WORK.付与回数←集計期間内での付与回数
				// ooooo
				
				// 付与情報WORK.期間の開始日に付与があるかどうか←true
				specialLeaveGrantWork.setGrantAtr(true);
				// 付与情報WORK.年休付与←次回特別休暇付与
				//specialLeaveGrantWork.setGrantRegular(
				// oooooo
				
				// 年月日←次回特別休暇付与．付与年月日 ooooo
				c.getGrantDate();

				if ( dividedDayMap.containsKey(c.getGrantDate())){ // すでに追加されているとき
					SpecialLeaveDividedDayEachProcess specialLeaveDividedDayEachProcess
						= dividedDayMap.get(c.getGrantDate());
					specialLeaveDividedDayEachProcess.setNextSpecialLeaveGrant(Optional.of(c));

				} else {
					SpecialLeaveDividedDayEachProcess specialLeaveDividedDayEachProcess
						= new SpecialLeaveDividedDayEachProcess(c.getGrantDate()); 
					specialLeaveDividedDayEachProcess.setNextSpecialLeaveGrant(Optional.of(c));
					dividedDayMap.put(c.getGrantDate(), specialLeaveDividedDayEachProcess);
				}
			});
		
//		// 終了日の翌日で期間を区切る  ----------------------------
//		GeneralDate nextEndDate = aggrPeriod.end().addDays(1);
		
		// 終了日の処理単位分割日を取得
		val specialLeaveGrantList1 = new ArrayList<SpecialLeaveDividedDayEachProcess>();
		// 【条件】
		// 終了日の翌日の期間かどうか=true
		dividedDayMap.forEach((key, val)->{
			if ( val.getNextSpecialLeaveGrant().equals(true) ){
				specialLeaveGrantList1.add(val);
			}
		});
		
		// 終了日の期間かどうか　←true
		specialLeaveGrantList1.forEach(c->{
//			c 	ooooo
		});
		
		// 終了日翌日の処理単位分割日を取得
		// 【条件】
		// 年月日=パラメータ「終了日」の翌日
		val specialLeaveGrantList2 = new ArrayList<SpecialLeaveDividedDayEachProcess>();
		dividedDayMap.forEach((key, val)->{
			if ( val.getYmd().equals(nextDayOfPeriodEnd) ){
				specialLeaveGrantList2.add(val);
			}
		});
		
		// 終了日の翌日の期間かどうか←true
		specialLeaveGrantList1.forEach(c->{
			c.setNextDayAfterPeriodEnd(true);
		});
		
		
		// 終了日翌日の期間がない場合作成  ----------------------------
		
		// ○終了日翌日の処理単位分割日を取得
		// 【条件】
		// 終了日の翌日の期間かどうか=true
		val specialLeaveGrantList3 = new ArrayList<SpecialLeaveDividedDayEachProcess>();
		dividedDayMap.forEach((key, val)->{
			if ( val.isNextDayAfterPeriodEnd() ){
				specialLeaveGrantList3.add(val);
			}
		});
		
		// 条件　件数＝０
		// 処理単位分割日に終了日翌日を追加
		// 処理単位分割日.年月日 = 終了日の翌日
		// 終了日の翌日情報WORK.終了日の翌日かどうか = true
		if ( specialLeaveGrantList3.isEmpty() ){
			SpecialLeaveDividedDayEachProcess specialLeaveDividedDayEachProcess
				= new SpecialLeaveDividedDayEachProcess(nextEndDate);
			specialLeaveDividedDayEachProcess.setNextDayAfterPeriodEnd(true);
			dividedDayMap.put(nextEndDate, specialLeaveDividedDayEachProcess);
		}
		
		// 付与前と付与後の期間を区別する   ----------------------------
		
		// List<処理単位分割日>をソート
		// 【ソート】
		// 年月日　昇順
		val specialLeaveGrantList4 = new ArrayList<SpecialLeaveDividedDayEachProcess>();
		dividedDayMap.forEach((key, val)->{
			specialLeaveGrantList4.add(val);
		});
		specialLeaveGrantList4.sort((a,b)->a.getYmd().compareTo(b.getYmd()));
		
		// 付与前か付与後か = 付与前
		boolean isBeforeGrant = false;
		specialLeaveGrantList4.forEach(c->{
			if ( c.())
			
		});
		
		
		
		
		
		
		
		
		
		
//		// 処理単位分割日リスト
//		Map<GeneralDate, DividedDayEachProcess> dividedDayMap = new HashMap<>();
		
		// 期間終了日翌日
		GeneralDate nextDayOfPeriodEnd = aggrPeriod.end();
		if (nextDayOfPeriodEnd.before(GeneralDate.max())) nextDayOfPeriodEnd = nextDayOfPeriodEnd.addDays(1);
		
		// 「年休付与残数データ」を取得　（期限日　昇順、付与日　昇順）
		List<AnnualLeaveGrantRemainingData> remainingDatas = new ArrayList<>();
		remainingDatas.addAll(grantRemainingDatas);
		Collections.sort(remainingDatas, new Comparator<AnnualLeaveGrantRemainingData>() {
			@Override
			public int compare(AnnualLeaveGrantRemainingData o1, AnnualLeaveGrantRemainingData o2) {
				int compDeadline = o1.getDeadline().compareTo(o2.getDeadline());
				if (compDeadline != 0) return compDeadline;
				return o1.getGrantDate().compareTo(o2.getGrantDate());
			}
		});
		
		// 取得した「年休付与残数データ」をすべて「処理単位分割日リスト」に追加
		for (val remainingData : remainingDatas){
			val deadline = remainingData.getDeadline();
			if (!aggrPeriod.contains(deadline)) continue;
			
			val nextDayOfDeadline = deadline.addDays(1);
			dividedDayMap.putIfAbsent(nextDayOfDeadline, new DividedDayEachProcess(nextDayOfDeadline));
			dividedDayMap.get(nextDayOfDeadline).setLapsedAtr(true);
		}
		
		// 「次回年休付与リスト」をすべて「処理単位分割日リスト」に追加
		for (val nextAnnualLeaveGrant : nextAnnualLeaveGrantList){
			val grantDate = nextAnnualLeaveGrant.getGrantDate();
			if (grantDate.beforeOrEquals(aggrPeriod.start().addDays(1))) continue;
			if (grantDate.after(nextDayOfPeriodEnd)) continue;
			
			dividedDayMap.putIfAbsent(grantDate, new DividedDayEachProcess(grantDate));
			dividedDayMap.get(grantDate).setGrantAtr(true);
			dividedDayMap.get(grantDate).setNextAnnualLeaveGrant(Optional.of(nextAnnualLeaveGrant));
		}
		
		// 期間終了日翌日の「処理単位分割日」を取得・追加　→　フラグ設定
		dividedDayMap.putIfAbsent(nextDayOfPeriodEnd, new DividedDayEachProcess(nextDayOfPeriodEnd));
		dividedDayMap.get(nextDayOfPeriodEnd).setNextDayAfterPeriodEnd(true);
		
		// 「処理単位分割日」をソート
		List<DividedDayEachProcess> dividedDayList = new ArrayList<>();
		dividedDayList.addAll(dividedDayMap.values());
		dividedDayList.sort((a, b) -> a.getYmd().compareTo(b.getYmd()));
		
		// 「年休集計期間WORK」を作成
		AggregatePeriodWork startWork = new AggregatePeriodWork();
		val startWorkEnd = dividedDayList.get(0).getYmd().addDays(-1);
		startWork.setPeriod(new DatePeriod(aggrPeriod.start(), startWorkEnd));
		aggregatePeriodWorks.add(startWork);
		
		// 付与後フラグ
		boolean isAfterGrant = false;
		
		for (int index = 0; index < dividedDayList.size(); index++){
			val nowDividedDay = dividedDayList.get(index);
			DividedDayEachProcess nextDividedDay = null;
			if (index + 1 < dividedDayList.size()) nextDividedDay = dividedDayList.get(index + 1);
			
			// 付与フラグをチェック
			if (nowDividedDay.isGrantAtr()) isAfterGrant = true;
			
			// 年休集計期間WORKを作成し、Listに追加
			GeneralDate workPeriodEnd = nextDayOfPeriodEnd;
			if (nextDividedDay != null) workPeriodEnd = nextDividedDay.getYmd().addDays(-1);
			AggregatePeriodWork nowWork = AggregatePeriodWork.of(
					new DatePeriod(nowDividedDay.getYmd(), workPeriodEnd),
					false,
					nowDividedDay.isNextDayAfterPeriodEnd(),
					nowDividedDay.isGrantAtr(),
					isAfterGrant,
					nowDividedDay.isLapsedAtr(),
					nowDividedDay.getNextAnnualLeaveGrant());
			aggregatePeriodWorks.add(nowWork);
		}
		
		// 処理期間内で何回目の付与なのかを保持。（一回目の付与を判断したい）
		int grantNumber = 1;
		for( AggregatePeriodWork nowWork : aggregatePeriodWorks){
			if ( nowWork.isGrantAtr() ) // 付与のとき
			{
				nowWork.setGrantNumber(grantNumber);
				grantNumber++;
			}
		}
		
		return aggregatePeriodWorks;
	}
	
	
	/**
	 * 特休付与残数データから特休情報を作成
	 * @param cId 会社ID
	 * @param employeeId 社員ID 
	 * @param ymd 年月日
	 * @param grantRemainingDataList 付与残数データリスト
	 * @return
	 */
	private static SpecialLeaveInfo createInfoFromRemainingData(
			String cId,
			String employeeId,
			GeneralDate ymd,
			List<SpecialLeaveGrantRemaining> grantRemainingDataList
			){
		
		SpecialLeaveInfo specialLeaveInfo = new SpecialLeaveInfo();
		
		// 特別休暇情報．年月日←パラメータ「年月日」
//		returnInfo.setYmd(aggrPeriod.start());
		specialLeaveInfo.setYmd(ymd);
		
		// 残数．特別休暇(マイナスあり)をクリア。（Listの要素数を０にする）
		specialLeaveInfo.getRemainingNumber().getSpecialLeaveWithMinus().clear();
		
		// 特休情報．特休付与情報　←　パラメータ「付与残数データ」
		List<SpecialLeaveGrantRemaining> targetDatas = new ArrayList<>();
		for (val grantRemainingData : grantRemainingDataList){
			if (grantRemainingData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) continue;
			targetDatas.add(grantRemainingData);
			employeeId = grantRemainingData.getEmployeeId();
		}
		targetDatas.sort((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()));
		specialLeaveInfo.setGrantRemainingList(targetDatas);
		
//		// 特休情報．上限データ　←　パラメータ「上限データ」
//		if (!maxDataOpt.isPresent()) {
//			Integer intNull = null;
//			SpecialLeaveMaxData emptyMaxData = SpecialLeaveMaxData.createFromJavaType(
//					employeeId, intNull, intNull, intNull, intNull);
//			returnInfo.setMaxData(emptyMaxData);
//		}
//		else {
//			returnInfo.setMaxData(maxDataOpt.get());
//		}
		
		// 特休情報残数を更新 
		specialLeaveInfo.updateRemainingNumber(false);
		
		// 特休情報を返す
		return specialLeaveInfo;
	}
	
	
	
	
	
	
//	/**
//	 * RequestList273 期間内の特別休暇残を集計する
//	 * @return
//	 */
//	public static InPeriodOfSpecialLeaveResultInfor complileInPeriodOfSpecialLeave(RequireM5 require, 
//			CacheCarrier cacheCarrier, ComplileInPeriodOfSpecialLeaveParam param) {
//		
//		InPeriodOfSpecialLeaveResultInfor outputData = new InPeriodOfSpecialLeaveResultInfor();
//		//ドメインモデル「特別休暇基本情報」を取得する
//		Optional<SpecialLeaveBasicInfo> optBasicInfor = require.specialLeaveBasicInfo(param.getSid(), param.getSpecialLeaveCode(), UseAtr.USE);
//		if(!optBasicInfor.isPresent()
//				|| optBasicInfor.get().getUsed() == UseAtr.NOT_USE) {
//			RemainDaysOfSpecialHoliday remainDays = new RemainDaysOfSpecialHoliday(new SpecialHolidayRemainInfor(0, 0, 0), 0, Optional.empty(), new ArrayList<>());
//			InPeriodOfSpecialLeave specialLeaveInfor = new InPeriodOfSpecialLeave(new ArrayList<>(), remainDays, new ArrayList<>(), new ArrayList<>());
//			return new InPeriodOfSpecialLeaveResultInfor(specialLeaveInfor, Finally.empty(), Finally.of(param.getComplileDate().end().addDays(1)));
//		}
//		//管理データを取得する
//		ManagaData grantRemainData = getMngData(require, cacheCarrier, 
//				param.getCid(),
//				param.getSid(),
//				param.getSpecialLeaveCode(),
//				param.getComplileDate(),
//				param.getOptBeforeResult());		
//		
//		SpecialHolidayDataParam speDataParam = new SpecialHolidayDataParam(param.getCid(),
//				param.getSid(),
//				param.getComplileDate(),
//				param.getSpecialLeaveCode(),
//				param.isMode(),
//				param.isOverwriteFlg(),
//				param.getRemainData(),
//				param.getInterimSpecialData());
//		//特別休暇暫定データを取得する
//		SpecialHolidayInterimMngData specialHolidayInterimDataMng = specialHolidayData(require, speDataParam);
//		
//		SpecialLeaveGrantRemainingDataTotal speRemainData = new SpecialLeaveGrantRemainingDataTotal(grantRemainData.getRemainDatas(),
//				grantRemainData.lstGrantDataMemory,
//				grantRemainData.lstGrantDatabase);
//		//特休の使用数を求める
//		RemainDaysOfSpecialHoliday useInfor = getUseDays(param.getCid(),
//				param.getSid(),
//				param.getComplileDate(),
//				speRemainData, 
//				specialHolidayInterimDataMng.getLstSpecialInterimMng(),
//				specialHolidayInterimDataMng.getLstInterimMng());
//		InPeriodOfSpecialLeave getOffsetDay = getInforCurrentProcess(require, param, grantRemainData, specialHolidayInterimDataMng, speRemainData,
//				useInfor);
//		//特別休暇のエラーチェックをする
//		List<SpecialLeaveError> lstError = lstError(getOffsetDay);
//		//「特別休暇の集計結果情報」に集計結果を格納する
//		getOffsetDay.setLstError(lstError);
//		outputData.setAggSpecialLeaveResult(getOffsetDay);
//		InPeriodOfSpecialLeave speTmp = toInPeriodOfSpecialLeave(getOffsetDay);
//		speTmp = getInforNextProcess(require, cacheCarrier, param, grantRemainData, getOffsetDay, speTmp);
//		outputData.setAggSpecialLeaveNextResult(Finally.of(speTmp));
//		//翌月管理データ取得区分をチェックする
//		if(param.isMngAtr()) {
//			outputData.setAggSpecialLeaveResult(speTmp);
//		}
//		//次の集計期間の開始日を計算する
//		outputData.setNextDate(Finally.of(param.getComplileDate().end().addDays(1)));
//		return outputData;
//	}
	
	
	
	
	
	/** 管理データを取得する
	 * @param cid
	 * @param sid 
	 * @param specialLeaveCode ・特別休暇コード
	 * @param complileDate ・集計開始日 ・集計終了日
	 * @return 特別休暇付与残数データ
	 */
	private static ManagaData getMngData(RequireM3 require, CacheCarrier cacheCarrier, String cid, String sid, int specialLeaveCode,
			DatePeriod complileDate, Optional<InPeriodOfSpecialLeaveResultInfor> beforeResult) {
		List<SpecialLeaveGrantRemainingData> lstDataBase = new ArrayList<>();
		//「INPUT．特別休暇の集計結果情報」をチェックする
		//INPUT．特別休暇の集計結果情報 = NULL || INPUT．特別休暇の集計結果情報.前回集計期間の翌日 ≠ INPUT．集計開始日
		if(!beforeResult.isPresent()
				|| (beforeResult.get().getNextDate().isPresent()
						&& !beforeResult.get().getNextDate().get().equals(complileDate.start()))) {
			//ドメインモデル「特別休暇付与残数データ」を取得する
			lstDataBase = require.specialLeaveGrantRemainingData(sid,
					specialLeaveCode,
					LeaveExpirationStatus.AVAILABLE,
					complileDate.end(),
					complileDate.start());
		} else {
			Finally<GeneralDate> nextDate = beforeResult.get().getNextDate();
			Finally<InPeriodOfSpecialLeave> aggSpecialLeaveNextResult = beforeResult.get().getAggSpecialLeaveNextResult();
			if(nextDate.isPresent()
					&& nextDate.get().equals(complileDate.start())) {
				if(aggSpecialLeaveNextResult.isPresent()) {
					//「前回の特別休暇の集計結果」を「特別休暇付与残数データ」に置き換える
					SpecialLeaveGrantRemainingData remainDataBefore = new SpecialLeaveGrantRemainingData();
					List<SpecialLeaveGrantDetails> lstSpeLeaveGrantDetails = aggSpecialLeaveNextResult.get().getLstSpeLeaveGrantDetails();
					for (SpecialLeaveGrantDetails x : lstSpeLeaveGrantDetails) {
						if(x.getSid().equals(sid) && x.getCode() == specialLeaveCode) {
							SpecialLeaveNumberInfoService detailBefore = x.getDetails();
							SpecialLeaveNumberInfo details = new SpecialLeaveNumberInfo();
							SpecialLeaveOverNumber limitData = new SpecialLeaveOverNumber();
							detailBefore.getLimitDays().ifPresent(a -> {
								limitData.setNumberOverDays(new DayNumberOver(a.getDays()));
								limitData.setTimeOver(a.getTimes().isPresent() ? Optional.of(new TimeOver(a.getTimes().get())) : Optional.empty());
							});
							details.setGrantNumber(new SpecialLeaveGrantNumber(new DayNumberOfGrant(detailBefore.getGrantDays()),
									detailBefore.getGrantTimes().isPresent() ? Optional.of(new TimeOfGrant(detailBefore.getGrantTimes().get())) : Optional.empty()));
							details.setRemainingNumber(new SpecialLeaveRemainingNumber(new DayNumberOfRemain(detailBefore.getRemainDays()),
									detailBefore.getRemainTimes().isPresent() ? Optional.of(new TimeOfRemain(detailBefore.getRemainTimes().get())) : Optional.empty()));
							
							details.setUsedNumber(new SpecialLeaveUsedNumber(new DayNumberOfUse(detailBefore.getUseDays()),
									detailBefore.getUseTimes().isPresent() ? Optional.of(new TimeOfUse(detailBefore.getUseTimes().get())) : Optional.empty(),
									Optional.empty(),
									Optional.of(limitData)));
							remainDataBefore.setSpecialId(x.getSpecialID());							
							remainDataBefore.setEmployeeId(x.getSid());
							remainDataBefore.setSpecialLeaveCode(new SpecialVacationCD(x.getCode()));
							remainDataBefore.setGrantDate(x.getGrantDate());
							remainDataBefore.setDeadlineDate(x.getDeadlineDate());
							remainDataBefore.setExpirationStatus(x.getExpirationStatus());
							remainDataBefore.setRegisterType(GrantRemainRegisterType.MONTH_CLOSE);
							remainDataBefore.setDetails(details);
							lstDataBase.add(remainDataBefore);
						}
					}
				}
				//ドメインモデル「特別休暇付与残数データ」を取得する
				List<SpecialLeaveGrantRemainingData> lstSpeData = require.specialLeaveGrantRemainingData(sid, specialLeaveCode,
						complileDate, complileDate.start(), LeaveExpirationStatus.AVAILABLE);
				lstDataBase.addAll(lstSpeData);
			}
		}
		
		List<SpecialLeaveGrantRemainingData> lstDataSpeDataBase = lstDataBase.stream().map(c -> {
			SpecialLeaveGrantNumber a = new SpecialLeaveGrantNumber(c.getDetails().getGrantNumber().getDayNumberOfGrant(), c.getDetails().getGrantNumber().getTimeOfGrant());
			SpecialLeaveUsedNumber b = new SpecialLeaveUsedNumber(c.getDetails().getUsedNumber().getDayNumberOfUse(), c.getDetails().getUsedNumber().getTimeOfUse(),
					c.getDetails().getUsedNumber().getUseSavingDays(), c.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber());
			SpecialLeaveRemainingNumber d = new SpecialLeaveRemainingNumber(c.getDetails().getRemainingNumber().getDayNumberOfRemain(), c.getDetails().getRemainingNumber().getTimeOfRemain());
			return new SpecialLeaveGrantRemainingData(c.getSpecialId(),
				c.getCId(),
				c.getEmployeeId(),
				c.getSpecialLeaveCode(),
				c.getGrantDate(),
				c.getDeadlineDate(),
				c.getExpirationStatus(),
				c.getRegisterType(),
				new SpecialLeaveNumberInfo(a, b, d));
		}).collect(Collectors.toList());
		//ドメインモデル「特別休暇」を取得する
		Optional<SpecialHoliday> optSpecialHoliday = require.specialHoliday(cid, specialLeaveCode);
		if(!optSpecialHoliday.isPresent()) {
			return new ManagaData(new ArrayList<>(), Optional.empty(), new ArrayList<>(), new ArrayList<>());
		}
		SpecialHoliday specialHoliday = optSpecialHoliday.get();
		//社員の特別休暇情報を取得する
		DatePeriod compDateEmp = new DatePeriod(complileDate.start().addDays(1), complileDate.end());
		InforSpecialLeaveOfEmployee getSpecialHolidayOfEmp = InforSpecialLeaveOfEmployeeSevice.getInforSpecialLeaveOfEmployee(require, cacheCarrier, cid, sid, specialLeaveCode, compDateEmp,specialHoliday);
		if(getSpecialHolidayOfEmp.getStatus() == InforStatus.GRANTED) {
			//メモリ上「特別休暇付与残数データ」を作る
			List<SpecialLeaveGrantRemainingData> lstDataSpeDataMemory = new ArrayList<>();
			for (SpecialHolidayInfor memoryInfor : getSpecialHolidayOfEmp.getSpeHolidayInfor()) {
				if(memoryInfor.getGrantDaysInfor().getGrantDays() <= 0) {
					continue;
				}
				String mngId = IdentifierUtil.randomUniqueId();
				SpecialLeaveNumberInfo details = new SpecialLeaveNumberInfo();
				SpecialLeaveGrantNumber grantNumber = new SpecialLeaveGrantNumber(new DayNumberOfGrant(memoryInfor.getGrantDaysInfor().getGrantDays()), Optional.empty());
				details.setGrantNumber(grantNumber);
				SpecialLeaveUsedNumber useNumber = new SpecialLeaveUsedNumber();
				useNumber.setDayNumberOfUse(new DayNumberOfUse((double)0));
				useNumber.setSpecialLeaveOverLimitNumber(Optional.empty());
				useNumber.setTimeOfUse(Optional.empty());
				useNumber.setUseSavingDays(Optional.empty());
				details.setUsedNumber(useNumber);
				SpecialLeaveRemainingNumber remainingNumber = new SpecialLeaveRemainingNumber(new DayNumberOfRemain((double) memoryInfor.getGrantDaysInfor().getGrantDays()), Optional.empty());
				details.setRemainingNumber(remainingNumber);
				SpecialLeaveGrantRemainingData grantMemoryData = new SpecialLeaveGrantRemainingData(mngId, 
						cid,
						sid, 
						new SpecialVacationCD(specialLeaveCode),
						memoryInfor.getGrantDaysInfor().getYmd(),
						memoryInfor.getDeadlineDate().isPresent() ? memoryInfor.getDeadlineDate().get() : GeneralDate.max(),
						LeaveExpirationStatus.AVAILABLE,
						GrantRemainRegisterType.MANUAL,
						details);
				lstDataSpeDataMemory.add(grantMemoryData);
			}
			//付与日が同じ管理データを排除する
			lstDataSpeDataBase = adjustGrantData(lstDataBase, lstDataSpeDataMemory);
			//ドメインモデル「期限情報」．期限指定方法をチェックする
			if(specialHoliday.getGrantPeriodic().getTimeSpecifyMethod() == TimeLimitSpecification.AVAILABLE_UNTIL_NEXT_GRANT_DATE
					&& !lstDataSpeDataMemory.isEmpty()
					&& !lstDataBase.isEmpty()) {				
				List<SpecialLeaveGrantRemainingData> lstTmp = new ArrayList<>(lstDataSpeDataBase);
				for (SpecialLeaveGrantRemainingData x : lstTmp) {
					//付与済の「特別休暇付与残数データ」．期限日= (先頭の付与予定の「特別休暇付与残数データ」．付与日).AddDays(-1)
					if (lstDataBase.contains(x)) {
						lstDataSpeDataBase.remove(x);
						GeneralDate kigenBi = lstDataSpeDataMemory.get(0).getGrantDate().addDays(-1);
						x.setDeadlineDate(kigenBi);
						lstDataSpeDataBase.add(x);	
					}
					
				}
				
			}
		}
		//「特別休暇付与残数データ」(output)をソートする
		lstDataSpeDataBase = lstDataSpeDataBase.stream().sorted((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()))
				.collect(Collectors.toList());
		List<SpecialLeaveGrantRemainingData> lstGrantDatabase = new ArrayList<>();
		List<SpecialLeaveGrantRemainingData> lstGrantDataMemory = new ArrayList<>();
		if(!lstDataSpeDataBase.isEmpty()) {
			for (SpecialLeaveGrantRemainingData x : lstDataSpeDataBase) {
				List<SpecialLeaveGrantRemainingData> db = lstDataBase.stream().filter(y -> y.getSpecialId().equals(x.getSpecialId())).collect(Collectors.toList());
				if(db.isEmpty()) {
					lstGrantDataMemory.add(x);
				} else {
					lstGrantDatabase.add(x);
				}
			}
		}
		ManagaData outputData = new ManagaData(lstDataSpeDataBase, getSpecialHolidayOfEmp.getUpLimiDays(), lstGrantDataMemory, lstGrantDatabase);
		return outputData;
	}
	
	/**
	 * 特別休暇暫定データを取得する
	 * @param cid
	 * @param sid
	 * @param dateData
	 * @param mode
	 * @return
	 */

	private static SpecialHolidayInterimMngData specialHolidayData(RequireM2 require, SpecialHolidayDataParam param) {
		List<InterimSpecialHolidayMng> lstOutput = new ArrayList<>();
		List<InterimRemain> lstInterimMng = new ArrayList<>();
		//INPUT．モードをチェックする
		if(param.isMode()) {
			/*//暫定残数管理データを作成する
			Map<GeneralDate, DailyInterimRemainMngData> interimMngData = interimMonthProcess.monthInterimRemainData(param.getCid(),
					param.getSid(), param.getDateData());
			List<DailyInterimRemainMngData> lstDailyInterimRemainMngData = interimMngData.values()
					.stream().collect(Collectors.toList());
			//メモリ上の「特別休暇暫定データ」を取得する
			for(DailyInterimRemainMngData y : lstDailyInterimRemainMngData) {*/
				List<InterimSpecialHolidayMng> specialHolidayData = param.getInterimSpecialData().stream()
						.filter(x -> x.getSpecialHolidayCode() == param.getSpeCode())
						.collect(Collectors.toList());
				for(InterimSpecialHolidayMng specialData : specialHolidayData) {
					lstOutput.add(specialData);
					List<InterimRemain> mngData = param.getRemainData().stream()
							.filter(a -> a.getRemainManaID().equals(specialData.getSpecialHolidayId()))
							.collect(Collectors.toList());
					if(!mngData.isEmpty()) {
						lstInterimMng.add(mngData.get(0));
					}
				}
			//}
		} else {
			//ドメインモデル「特別休暇暫定データ」を取得する
			List<InterimRemain> lstInterimMngTmp = require.interimRemains(param.getSid(),
					param.getDateData(), RemainType.SPECIAL);
			lstInterimMngTmp.stream().forEach(a -> {
				List<InterimSpecialHolidayMng> lstSpecialData = require.interimSpecialHolidayMng(a.getRemainManaID())
						.stream().filter(x -> x.getSpecialHolidayCode() == param.getSpeCode())
						.collect(Collectors.toList());
				if(!lstSpecialData.isEmpty()) {
					lstOutput.addAll(lstSpecialData);
					lstInterimMng.add(a);
				}
			});
		}
		List<InterimRemain> lstInterimMngTmpCreate = new ArrayList<>(lstInterimMng);
		List<InterimSpecialHolidayMng> speHolidayMngTempCreate = new ArrayList<>(lstOutput);
		//INPUT．上書きフラグをチェックする
		if(param.isOverwriteFlg()) {
			for (InterimRemain interimRemain : param.getRemainData()) {
				List<InterimRemain> interimMngChk = lstInterimMngTmpCreate.stream()
						.filter(x -> x.getYmd().equals(interimRemain.getYmd()))
						.collect(Collectors.toList());
				List<InterimSpecialHolidayMng> speMngReplace = param.getInterimSpecialData().stream()
						.filter(y -> y.getSpecialHolidayId().equals(interimRemain.getRemainManaID())
									&& y.getSpecialHolidayCode() == param.getSpeCode())
						.collect(Collectors.toList());
				if(!interimMngChk.isEmpty()) {
					InterimRemain temMng = interimMngChk.get(0);
					lstInterimMng.remove(temMng);
					
					if(!speMngReplace.isEmpty()) {
						List<InterimSpecialHolidayMng> speMngTmp = speHolidayMngTempCreate.stream()
								.filter(z -> z.getSpecialHolidayId().equals(temMng.getRemainManaID()))
								.collect(Collectors.toList());
						if(!speMngTmp.isEmpty()) {
							lstOutput.remove(speMngTmp.get(0));	
						}
					}
				}
				lstInterimMng.add(interimRemain);
				if(!speMngReplace.isEmpty()) {
					lstOutput.add(speMngReplace.get(0));
				}
			}
		}
		return new SpecialHolidayInterimMngData(lstOutput, lstInterimMng);
	}
	
	/**
	 * 特休の使用数を求める
	 * @param cid 会社ID
	 * @param sid 社員ID
	 * @param complileDate 集計開始日 , 集計終了日
	 * @param remainDatas 特別休暇付与残数データ一覧
	 * @param interimDatas 特別休暇暫定データ一覧
	 * @return
	 */
	private static RemainDaysOfSpecialHoliday getUseDays(String cid, String sid, DatePeriod complileDate,
			SpecialLeaveGrantRemainingDataTotal speRemainData, List<InterimSpecialHolidayMng> interimSpeDatas, 
			List<InterimRemain> lstInterimMng) {
		//使用数付与前=0, 使用数付与後=0(初期化)
		double useDaysBefore = 0;
		double useDaysAfter = 0;
		SpecialHolidayRemainInfor grantDetailBefore = new SpecialHolidayRemainInfor(0, 0, 0);
		SpecialHolidayRemainInfor grantDetailAfter = new SpecialHolidayRemainInfor(0, 0, 0);
		//INPUT．特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(speRemainData.getLstGrantDataMemory().isEmpty()) {
			//使用数を求める
			for (InterimSpecialHolidayMng interimData : interimSpeDatas) {
				//使用数付与前=使用数
				useDaysBefore += interimData.getUseDays().isPresent() ? interimData.getUseDays().get().v() : 0;
			}
			//「特別休暇の残数」．付与前明細．使用数 = 使用数付与前
			grantDetailBefore.setUseDays(useDaysBefore);
		} else {
			//使用数付与前
			DatePeriod datePeriodBefore = new DatePeriod(complileDate.start(), speRemainData.getLstGrantDataMemory().get(0).getGrantDate().addDays(-1));
			//指定期間の使用数を求める
			useDaysBefore += useDayFormGrant(interimSpeDatas, lstInterimMng, datePeriodBefore);
			//使用数付与後
			DatePeriod datePeriodAfter = new DatePeriod(speRemainData.getLstGrantDataMemory().get(0).getGrantDate(), complileDate.end());
			//指定期間の使用数を求める
			useDaysAfter += useDayFormGrant(interimSpeDatas, lstInterimMng, datePeriodAfter);
			//「特別休暇の残数」．付与前明細．使用数 = 使用数付与前
			grantDetailBefore.setUseDays(useDaysBefore);
			//「特別休暇の残数」．付与後明細．使用数 = 使用数付与後
			grantDetailAfter.setUseDays(useDaysAfter);
		}
		return new RemainDaysOfSpecialHoliday(grantDetailBefore, 0, 
				grantDetailAfter.getUseDays() == 0 ? Optional.empty() : Optional.of(grantDetailAfter), new ArrayList<>());
	}

	/**
	 * 管理データと暫定データの相殺
	 * @param cid・会社ID
	 * @param sid・社員ID
	 * @param dateData ・集計開始日 ・集計終了日
	 * @param baseDate ・基準日
	 * @param specialCode ・特別休暇コード
	 * @param lstGrantData ・特別休暇付与残数データ一覧
	 * @param interimDataMng ・特別休暇暫定データ一覧
	 * @param accumulationMaxDays ・蓄積上限日数
	 * @return
	 */
	private static InPeriodOfSpecialLeave getOffsetDay1004(RequireM1 require, String cid, String sid, 
			DatePeriod dateData, GeneralDate baseDate, int specialCode, SpecialLeaveGrantRemainingDataTotal lstGrantData,
			SpecialHolidayInterimMngData interimDataMng, double accumulationMaxDays,
			RemainDaysOfSpecialHoliday useInfor, boolean isMode) {
		//未消化数=0(初期化)
		double undigested = 0;
		DataMngOfDeleteExpired expiredData = null;
		SubtractUseDaysFromMngDataOut subtractUseDays = null;
		
		//取得した特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(lstGrantData.getLstGrantDataMemory().isEmpty()) {
			//使用数を管理データから引く speLeaveResult dung o dau
			subtractUseDays = subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDataTotal(), interimDataMng, useInfor);
			//期限切れの管理データを期限切れに変更する
			expiredData = unDigestedDay(subtractUseDays.getLstSpeRemainData(), baseDate, isMode);
			
		} else {
			//付与前用の集計終了日= (先頭の付与予定の「特別休暇付与残数データ」．付与日).AddDays(-1)
			GeneralDate endDate = lstGrantData.getLstGrantDataMemory().get(0).getGrantDate().addDays(-1);			
			//・INPUT．特別休暇暫定データ一覧（対象日 <= 付与前用の集計終了日）
			List<InterimRemain> lstInterimMng = interimDataMng.getLstInterimMng().stream()
					.filter(x -> x.getYmd().beforeOrEquals(endDate))
					.collect(Collectors.toList());
			List<InterimSpecialHolidayMng> lstSpecialInterimMng = new ArrayList<>();
			lstInterimMng.stream().forEach(a -> {
				List<InterimSpecialHolidayMng> speTmp = interimDataMng.getLstSpecialInterimMng()
						.stream().filter(b -> b.getSpecialHolidayId().equals(a.getRemainManaID()))
						.collect(Collectors.toList());
				lstSpecialInterimMng.addAll(speTmp);
			});
			SpecialHolidayInterimMngData interimBefore = new SpecialHolidayInterimMngData(lstSpecialInterimMng, lstInterimMng);
			//使用数を管理データから引く
			subtractUseDays = subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDataTotal(), interimBefore, useInfor);
			//期限切れの管理データを期限切れに変更する //付与前用の集計終了日、INPUT．基準日の古い日付
			expiredData = unDigestedDay(subtractUseDays.getLstSpeRemainData(), endDate.after(baseDate) ? baseDate : endDate, isMode);
			
		}
		//未消化数+=未消化数
		undigested += expiredData.getUnDigestedDay();
		//繰越上限日数まで調整する
		DataMngOfDeleteExpired adjustCarryForward = adjustCarryForward1005(require, expiredData.getLstGrantData(), accumulationMaxDays);
		//未消化数+=未消化数(output)
		undigested += adjustCarryForward.getUnDigestedDay();
		//取得した特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(!lstGrantData.getLstGrantDataMemory().isEmpty()) {
			//付与後用の集計開始日= 先頭の付与予定の「特別休暇付与残数データ」．付与日
			GeneralDate startDate = lstGrantData.getLstGrantDataMemory().get(0).getGrantDate();
			//INPUT．特別休暇暫定データ一覧（対象日 >= 付与後用の集計開始日）
			List<InterimRemain> lstInterimMng = interimDataMng.getLstInterimMng().stream()
					.filter(x -> x.getYmd().afterOrEquals(startDate))
					.collect(Collectors.toList());
			List<InterimSpecialHolidayMng> lstSpecialInterimMng = new ArrayList<>();
			lstInterimMng.stream().forEach(a -> {
				List<InterimSpecialHolidayMng> speTmp = interimDataMng.getLstSpecialInterimMng()
						.stream().filter(b -> b.getSpecialHolidayId().equals(a.getRemainManaID()))
						.collect(Collectors.toList());
				lstSpecialInterimMng.addAll(speTmp);
			});
			SpecialHolidayInterimMngData interimBefore = new SpecialHolidayInterimMngData(lstSpecialInterimMng, lstInterimMng);
			//使用数を管理データから引く
			subtractUseDays = subtractUseDaysFromMngData1004(adjustCarryForward.getLstGrantData(), interimBefore, useInfor);
			//期限切れの管理データを期限切れに変更する
			expiredData = unDigestedDay(subtractUseDays.getLstSpeRemainData(), baseDate, isMode);
			//未消化数+=未消化数(output)
			undigested += expiredData.getUnDigestedDay();
		}
		//パラメータ．特別休暇の残数．未消化数=未消化数
		useInfor.setUnDisgesteDays(undigested);
		//特別休暇付与残数データ一覧を特別休暇パラメータに追加する
		Map<GeneralDate, Double> limitDays = adjustCarryForward.getMapGrantDays();
		List<SpecialLeaveGrantDetails> lstSpeLeaGrant = new ArrayList<>();
		for(SpecialLeaveGrantRemainingData x : expiredData.getLstGrantData()){
			SpecialLeaveGrantDetails grantDetail = new SpecialLeaveGrantDetails();
			grantDetail.setCode(x.getSpecialLeaveCode().v());
			Optional<SpecialLeaveGrantRemainingData> grantDataById = require.specialLeaveGrantRemainingData(x.getSpecialId());
			if(grantDataById.isPresent()) {
				grantDetail.setDataAtr(DataAtr.GRANTED);
			} else {
				grantDetail.setDataAtr(DataAtr.GRANTSCHE);
			}
			grantDetail.setSpecialID(x.getSpecialId());
			grantDetail.setExpirationStatus(x.getExpirationStatus());
			grantDetail.setSid(x.getEmployeeId());
			grantDetail.setDeadlineDate(x.getDeadlineDate());
			grantDetail.setGrantDate(x.getGrantDate());
			SpecialLeaveNumberInfoService inforSevice = new SpecialLeaveNumberInfoService();
			SpecialLeaveGrantNumber grantNumberData = x.getDetails().getGrantNumber();
			SpecialLeaveUsedNumber usedNumberData = x.getDetails().getUsedNumber();
			SpecialLeaveRemainingNumber remainingNumberData = x.getDetails().getRemainingNumber();
			inforSevice.setRemainDays(remainingNumberData != null && remainingNumberData.getDayNumberOfRemain() != null
					? remainingNumberData.getDayNumberOfRemain().v() : 0);
			inforSevice.setUseDays(usedNumberData != null && usedNumberData.getDayNumberOfUse() != null
					? usedNumberData.getDayNumberOfUse().v() : 0);
			inforSevice.setGrantDays(grantNumberData.getDayNumberOfGrant().v());
			inforSevice.setRemainTimes(remainingNumberData != null && remainingNumberData.getTimeOfRemain().isPresent()
					&& remainingNumberData.getTimeOfRemain().get() != null
					? Optional.of( remainingNumberData.getTimeOfRemain().get().v()) : Optional.empty());
			inforSevice.setUseTimes(usedNumberData != null && usedNumberData.getTimeOfUse() != null
					&& usedNumberData.getTimeOfUse().isPresent() && usedNumberData.getTimeOfUse() != null && usedNumberData.getTimeOfUse().get() != null
					? Optional.of(usedNumberData.getTimeOfUse().get().v()) : Optional.empty());
			if(limitDays.containsKey(x.getGrantDate())) {
				LimitTimeAndDays limitInfor = new LimitTimeAndDays(limitDays.get(x.getGrantDate()), Optional.empty());
				inforSevice.setLimitDays(Optional.of(limitInfor));
			} else {
				inforSevice.setLimitDays(Optional.empty());	
			}			
			inforSevice.setGrantTimes(grantNumberData != null && grantNumberData.getTimeOfGrant().isPresent()
					&& grantNumberData.getTimeOfGrant().get() != null
					? Optional.of(grantNumberData.getTimeOfGrant().get().v()) : Optional.empty());
			grantDetail.setDetails(inforSevice);
			lstSpeLeaGrant.add(grantDetail);
		}
		return new InPeriodOfSpecialLeave(lstSpeLeaGrant, useInfor, subtractUseDays.getSpeLeaveResult().getUseOutPeriod(), new ArrayList<>());
	}
	
	/**
	 * 使用数を管理データから引く
	 * @param lstGrantData 特別休暇付与残数データ一覧
	 * @param interimDataMng 特別休暇暫定データ一覧
	 * @return
	 */
	private static SubtractUseDaysFromMngDataOut subtractUseDaysFromMngData1004(List<SpecialLeaveGrantRemainingData> lstGrantData,
			SpecialHolidayInterimMngData interimDataMng, RemainDaysOfSpecialHoliday useInfor) {
		InPeriodOfSpecialLeave output = new InPeriodOfSpecialLeave(new ArrayList<>(), useInfor, new ArrayList<>(), new ArrayList<>());
		List<UseDaysOfPeriodSpeHoliday> lstUseDaysOutOfPeriod = new ArrayList<>();
		List<SpecialLeaveGrantRemainingData> lstOuput = lstGrantData.stream().map(c -> {
			SpecialLeaveGrantNumber a = new SpecialLeaveGrantNumber(c.getDetails().getGrantNumber().getDayNumberOfGrant(), c.getDetails().getGrantNumber().getTimeOfGrant());
			SpecialLeaveUsedNumber b = new SpecialLeaveUsedNumber(c.getDetails().getUsedNumber().getDayNumberOfUse(), c.getDetails().getUsedNumber().getTimeOfUse(),
					c.getDetails().getUsedNumber().getUseSavingDays(), c.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber());
			SpecialLeaveRemainingNumber d = new SpecialLeaveRemainingNumber(c.getDetails().getRemainingNumber().getDayNumberOfRemain(), c.getDetails().getRemainingNumber().getTimeOfRemain());
			return new SpecialLeaveGrantRemainingData(c.getSpecialId(),
				c.getCId(),
				c.getEmployeeId(),
				c.getSpecialLeaveCode(),
				c.getGrantDate(),
				c.getDeadlineDate(),
				c.getExpirationStatus(),
				c.getRegisterType(),
				new SpecialLeaveNumberInfo(a, b, d));
		}).collect(Collectors.toList());		
		for (InterimSpecialHolidayMng speInterimLoop : interimDataMng.getLstSpecialInterimMng()) {
			InterimRemain interimMng = interimDataMng.getLstInterimMng().stream()
					.filter(x -> x.getRemainManaID().equals(speInterimLoop.getSpecialHolidayId()))
					.collect(Collectors.toList()).get(0);
			//相殺できるINPUT．特別休暇付与残数データを取得する
			//・「特別休暇付与残数データ」．付与日<= ループ中の「特別休暇暫定データ」．年月日 <= 「特別休暇付与残数データ」．期限日
			List<SpecialLeaveGrantRemainingData> tmpGrantRemainingData = lstOuput
					.stream()
					.filter(x -> x.getGrantDate().beforeOrEquals(interimMng.getYmd())
							&& interimMng.getYmd().beforeOrEquals(x.getDeadlineDate())
							&& x.getExpirationStatus() == LeaveExpirationStatus.AVAILABLE)
					.collect(Collectors.toList())
					.stream().sorted((a,b) -> a.getGrantDate().compareTo(b.getGrantDate()))
					.collect(Collectors.toList());
			if(tmpGrantRemainingData.isEmpty()) {
				//ループ中の「特別休暇暫定データ」を「特別休暇期間外の使用」に追加する
				UseDaysOfPeriodSpeHoliday useDaysOutPeriod = new UseDaysOfPeriodSpeHoliday(interimMng.getYmd(), 
						speInterimLoop.getUseDays().isPresent() ? Optional.of(speInterimLoop.getUseDays().get().v()) : Optional.empty(),
						speInterimLoop.getUseTimes() != null && speInterimLoop.getUseTimes().isPresent() ? Optional.of(speInterimLoop.getUseTimes().get().v()) : Optional.empty());
				lstUseDaysOutOfPeriod.add(useDaysOutPeriod);
				output.setUseOutPeriod(lstUseDaysOutOfPeriod);
			}
			double speInterimUsedays = speInterimLoop.getUseDays().isPresent() ? speInterimLoop.getUseDays().get().v() : 0;
			//特別休暇付与残数データの有無チェックをする
			if(lstOuput.isEmpty()) {
				//「特別休暇の残数」．付与前明細．残数 -= 特別休暇暫定データ．特休使用				
				output.getRemainDays().getGrantDetailBefore().setRemainDays(output.getRemainDays().getGrantDetailBefore().getRemainDays() - speInterimUsedays);
			} else {
				List<SpecialLeaveGrantRemainingData> specialLeaverDataTmp = new ArrayList<>(tmpGrantRemainingData);
				int count = 0;
				double remainDays = speInterimUsedays;
				for (SpecialLeaveGrantRemainingData grantData : specialLeaverDataTmp) {
					count += 1;
					if(grantData.getDetails().getRemainingNumber().getDayNumberOfRemain().v() == 0
							&& count < specialLeaverDataTmp.size()) {
						continue;
					}					
					lstOuput.remove(grantData);
					//特別休暇暫定データ．特休使用をDBから取得した付与日の古い特別休暇付与残数データから引く
					DayNumberOfRemain remainDaysInfor = grantData.getDetails().getRemainingNumber().getDayNumberOfRemain();
					//「特別休暇付与残数データ」．残数 -= 特別休暇暫定データ．特休使用
					remainDays -= remainDaysInfor.v();
					if(count < specialLeaverDataTmp.size() && remainDays > 0) {
						grantData.getDetails().getRemainingNumber().setDayNumberOfRemain(new DayNumberOfRemain(0.0));
					} else {
						grantData.getDetails().getRemainingNumber().setDayNumberOfRemain(new DayNumberOfRemain(-remainDays));
					}
					//特別休暇暫定データ．特休使用を該当特別休暇付与残数データに特休使用に計上する
					//「特別休暇付与残数データ」．使用数 += 特別休暇暫定データ．特休使用
					double userDay = grantData.getDetails().getUsedNumber().getDayNumberOfUse().v();
					grantData.getDetails().getUsedNumber().setDayNumberOfUse(new DayNumberOfUse(userDay + speInterimUsedays));
					lstOuput.add(grantData);	
					if(remainDays <=0) {
						break;
					}
				}
			}
		}
		return new SubtractUseDaysFromMngDataOut(output, lstOuput);
	}

	/**
	 * 繰越上限日数まで調整する
	 * @param lstGrantData 特別休暇付与残数データ一覧
	 * @param accumulationMaxDays  蓄積上限日数
	 * @param grantDetailBefore 付与前の残数 
	 * @return
	 */
	private static DataMngOfDeleteExpired adjustCarryForward1005(RequireM1 require, 
			List<SpecialLeaveGrantRemainingData> lstGrantData, double accumulationMaxDays) {
		List<SpecialLeaveGrantRemainingData> lstGrantDataMemory = new ArrayList<>();//付与予定
		List<SpecialLeaveGrantRemainingData> lstGrantDatabase = new ArrayList<>();//付与済
		for (SpecialLeaveGrantRemainingData grantData : lstGrantData) {
			Optional<SpecialLeaveGrantRemainingData> grantDataById = require.specialLeaveGrantRemainingData(grantData.getSpecialId());
			if(grantDataById.isPresent()) {
				//DB上取得した「特別休暇付与残数データ」は付与済
				lstGrantDatabase.add(grantData);
			} else {
				//メモリ上の「特別休暇付与残数データ」は付与予定	
				lstGrantDataMemory.add(grantData);
			}
		}
		double unDisgesteDays = 0;
		Map<GeneralDate, Double> limitDays = new HashMap<>();
		//INPUT．蓄積上限日数をチェックする
		if(accumulationMaxDays <= 0) {
			return new DataMngOfDeleteExpired(unDisgesteDays, lstGrantData, limitDays);
		}		
		//INPUT．特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする		
		if(!lstGrantDataMemory.isEmpty()) {			
			double overCarryDays = 0;
			double dayNumberOfGrant = 0;
			for (SpecialLeaveGrantRemainingData grantDataMemory : lstGrantDataMemory) {
				dayNumberOfGrant += grantDataMemory.getDetails().getGrantNumber().getDayNumberOfGrant().v();
			}
			//付与前の残数=付与済の「特別休暇付与残数データ」．「明細」．残数の合計
			double remaintDays = 0;
			for (SpecialLeaveGrantRemainingData grantDatabase : lstGrantDatabase) {				
				remaintDays += grantDatabase.getDetails().getRemainingNumber().getDayNumberOfRemain().v();				
			}
			remaintDays = remaintDays < 0 ? 0 : remaintDays;
			//繰越超えた値 = 付与予定の「特別休暇付与残数データ」．「明細」．付与 + 付与前の残数 -INPUT．蓄積上限日数
			overCarryDays = dayNumberOfGrant + remaintDays - accumulationMaxDays;
			double grantDaysOld = overCarryDays;
			//繰越上限を超えたかチェックする
			if(overCarryDays > 0) {
				List<SpecialLeaveGrantRemainingData> lstGrantDatabaseTmp = new ArrayList<>(lstGrantDatabase);
				lstGrantDatabaseTmp = lstGrantDatabaseTmp.stream().sorted((a,b) -> a.getGrantDate().compareTo(b.getGrantDate()))
						.collect(Collectors.toList());
				int count = 0;
				for (SpecialLeaveGrantRemainingData speLeaverData : lstGrantDatabaseTmp) {
					count += 1;
					//ドメインモデル「特別休暇付与残数データ」．期限切れ状態をチェックする
					if(speLeaverData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) {
						continue;
					}
					double remainLeaverDays = speLeaverData.getDetails().getRemainingNumber().getDayNumberOfRemain().v();
					//古い日付順から、付与済の「特別休暇付与残数データ」．「明細」．残数から繰越超えた値を引く
					grantDaysOld -= remainLeaverDays;
					lstGrantDatabase.remove(speLeaverData);	
					if(grantDaysOld > 0 && count < lstGrantDatabaseTmp.size()) {
						speLeaverData.getDetails().getRemainingNumber().setDayNumberOfRemain(new DayNumberOfRemain((double) 0.0));	
					} else {
						speLeaverData.getDetails().getRemainingNumber().setDayNumberOfRemain(new DayNumberOfRemain((double) -grantDaysOld));	
					}
					//未消化数=繰越超えた値
					unDisgesteDays = overCarryDays;										
					//特別休暇の利用情報．上限超過消滅日数 += 繰越超えた値
					limitDays.put(speLeaverData.getGrantDate(), overCarryDays);					
					//特別休暇の利用情報 set 上限超過消滅日数．日数
					lstGrantDatabase.add(speLeaverData);
				}
			}
		}
		List<SpecialLeaveGrantRemainingData> lstOuput = new ArrayList<>(lstGrantDatabase);
		if(!lstGrantDataMemory.isEmpty()) {
			lstOuput.addAll(lstGrantDataMemory);
		}
		lstOuput = lstOuput.stream().sorted((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()))
				.collect(Collectors.toList());
		return new DataMngOfDeleteExpired(unDisgesteDays, lstOuput, limitDays);
	}
	
	/**
	 * 付与前の残数情報をまとめる
	 * @param lstSpeLeaveGrantDetails
	 * @param grantDetailBefore
	 * @return
	 */
	private static SpecialHolidayRemainInfor grantDetailBefore(List<SpecialLeaveGrantDetails> lstSpeLeaveGrantDetails,
			SpecialHolidayRemainInfor grantDetailBefore) {
		for (SpecialLeaveGrantDetails speGrantDetail : lstSpeLeaveGrantDetails) {
			//ループ中のパラメータ．特別休暇の付与明細．期限切れ区分をチェックする
			if(speGrantDetail.getExpirationStatus() != LeaveExpirationStatus.EXPIRED) {
				//付与前明細．残数 += ループ中のパラメータ．特別休暇の付与明細．明細．残数
				grantDetailBefore.setRemainDays(grantDetailBefore.getRemainDays() + speGrantDetail.getDetails().getRemainDays());
			}
		}
		grantDetailBefore.setGrantDays(0);
		return grantDetailBefore;
	}

	/**
	 * (付与前)管理データと暫定データの相殺
	 * @param cid ・会社ID
	 * @param sid ・社員ID
	 * @param shukeiDate ・集計開始日 ・ 集計終了日
	 * @param lstGrantData・特別休暇付与残数データ一覧
	 * @param interimDataMng ・特別休暇暫定データ一覧
	 * @param baseDate: 基準日
	 * @return
	 */
	private static RemainDaysOfSpecialHoliday remainDaysBefore(String cid, String sid, DatePeriod shukeiDate,
			SpecialLeaveGrantRemainingDataTotal lstGrantData, SpecialHolidayInterimMngData interimDataMng,
			RemainDaysOfSpecialHoliday useInfor, GeneralDate baseDate, boolean isMode) {
		
		//使用数を管理データから引く
		SubtractUseDaysFromMngDataOut subtractUseDays = subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDatabase(), interimDataMng, useInfor);
		//期限切れの管理データを期限切れに変更する //NPUT．集計終了日、INPUT．基準日の古い日付
		DataMngOfDeleteExpired expiredData = unDigestedDay(subtractUseDays.getLstSpeRemainData(), 
				shukeiDate.end().after(baseDate) ? baseDate : shukeiDate.end(), isMode);
		//付与数 = 0, 残数 = 0（初期化）
		double remainDays = 0;
		//上記アルゴリズムのoutput．特別休暇付与残数データ一覧、先頭から最後までループする
		for (SpecialLeaveGrantRemainingData x : expiredData.getLstGrantData()) {
			//特別休暇付与残数データ．期限切れ状態をチェックする
			if(x.getExpirationStatus() != LeaveExpirationStatus.EXPIRED) {
				//残数 += ループ中の「特別休暇数情報」．残数
				remainDays += x.getDetails().getRemainingNumber().getDayNumberOfRemain().v();
			}
		}
		//パラメータ．特別休暇の残数．付与前明細．付与数 = 0
		useInfor.getGrantDetailBefore().setGrantDays(0);
		//パラメータ．特別休暇の残数．付与前明細．残数 = 残数
		useInfor.getGrantDetailBefore().setRemainDays(remainDays);
		if(!subtractUseDays.getSpeLeaveResult().getUseOutPeriod().isEmpty()) {
			useInfor.setUseDaysOutPeriod(subtractUseDays.getSpeLeaveResult().getUseOutPeriod());
		}
		return useInfor;
	}

	/**
	 * 付与後の残数情報をまとめる
	 * @param lstSpeLeaveGrantDetails
	 * @param grantDetailBefore
	 * @return
	 */
	private static InPeriodOfSpecialLeave grantDetailAfter(SpecialHolidayRemainInfor grantDetailAfter, 
			InPeriodOfSpecialLeave getOffsetDay) {
		//付与後明細．残数と付与数=0（初期化）
		double grantDays = 0;
		double remainDays = 0;
		List<SpecialLeaveGrantDetails> lstTmp = new ArrayList<>(getOffsetDay.getLstSpeLeaveGrantDetails());
		//パラメータ．特別休暇の付与明細をループする
		for (SpecialLeaveGrantDetails speDetail : lstTmp) {
			//ループ中のパラメータ．特別休暇の付与明細．データ区分をチェックする
			if(speDetail.getDataAtr() == DataAtr.GRANTED) {
				double remainDaysMinus = speDetail.getDetails().getRemainDays();
				//ループ中のパラメータ．特別休暇の付与明細．明細．残数をチェックする
				if(remainDaysMinus < 0) {
					//ループ中のパラメータ．特別休暇の付与明細．明細．使用数 += ループ中のパラメータ．特別休暇の付与明細．明細．残数
					double useDayMinus = speDetail.getDetails().getUseDays() + remainDaysMinus;
					getOffsetDay.getLstSpeLeaveGrantDetails().remove(speDetail);
					speDetail.getDetails().setUseDays(useDayMinus < 0 ? 0 : useDayMinus);
					//ループ中のパラメータ．特別休暇の付与明細．明細．残数 = 0
					speDetail.getDetails().setRemainDays(0);
					getOffsetDay.getLstSpeLeaveGrantDetails().add(speDetail);
				}
			}
			//ループ中のパラメータ．特別休暇の付与明細．期限切れ区分をチェックする
			if(speDetail.getExpirationStatus() == LeaveExpirationStatus.AVAILABLE) {
				//付与後明細．残数 += ループ中のパラメータ．特別休暇の付与明細．明細．残数
				remainDays += speDetail.getDetails().getRemainDays();
			}
			//ループ中のパラメータ．特別休暇の付与明細．データ区分をチェックする
			if(speDetail.getDataAtr() == DataAtr.GRANTSCHE) {
				//付与後明細．付与数 += ループ中のパラメータ．特別休暇の付与明細．明細．付与数
				grantDays += speDetail.getDetails().getGrantDays();
			}
		}
		grantDetailAfter.setGrantDays(grantDays);
		grantDetailAfter.setRemainDays(remainDays);
		getOffsetDay.getRemainDays().setGrantDetailAfter(Optional.of(grantDetailAfter));
		return getOffsetDay;
	}

	private static InPeriodOfSpecialLeave getInforCurrentProcess(RequireM1 require, ComplileInPeriodOfSpecialLeaveParam param,
			ManagaData grantRemainData, SpecialHolidayInterimMngData specialHolidayInterimDataMng,
			SpecialLeaveGrantRemainingDataTotal speRemainData, RemainDaysOfSpecialHoliday useInfor) {
		InPeriodOfSpecialLeave getOffsetDay;
		//取得した特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(grantRemainData.lstGrantDataMemory.isEmpty()) {
			//管理データと暫定データの相殺
			getOffsetDay = getOffsetDay1004(require, param.getCid(), param.getSid(), param.getComplileDate(), param.getBaseDate(),
					param.getSpecialLeaveCode(), speRemainData, specialHolidayInterimDataMng, 
					grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0, useInfor, param.isMode());
			//付与前の残数情報をまとめる
			SpecialHolidayRemainInfor grantDetailBefore = grantDetailBefore(getOffsetDay.getLstSpeLeaveGrantDetails(), getOffsetDay.getRemainDays().getGrantDetailBefore());
			getOffsetDay.getRemainDays().setGrantDetailBefore(grantDetailBefore);
		} else {
			//付与前用の集計終了日= (先頭の付与予定の「特別休暇付与残数データ」．付与日).AddDays(-1)
			GeneralDate endDate = grantRemainData.lstGrantDataMemory.get(0).getGrantDate().addDays(-1);
			DatePeriod shuukeiDate = new DatePeriod(param.getComplileDate().start(), endDate);
			//取得した特別休暇暫定データ一覧（対象日 <= 付与前用の集計終了日）
			List<InterimRemain> lstInterimMng = specialHolidayInterimDataMng.getLstInterimMng().stream()
					.filter(x -> x.getYmd().beforeOrEquals(endDate))
					.collect(Collectors.toList());
			List<InterimSpecialHolidayMng> lstSpecialInterimMng = new ArrayList<>();
			lstInterimMng.stream().forEach(a -> {
				List<InterimSpecialHolidayMng> speTmp = specialHolidayInterimDataMng.getLstSpecialInterimMng()
						.stream().filter(b -> b.getSpecialHolidayId().equals(a.getRemainManaID()))
						.collect(Collectors.toList());
				lstSpecialInterimMng.addAll(speTmp);
			});
			SpecialHolidayInterimMngData interimBefore = new SpecialHolidayInterimMngData(lstSpecialInterimMng, lstInterimMng);
			//(付与前)管理データと暫定データの相殺
			useInfor = remainDaysBefore(param.getCid(), param.getSid(), shuukeiDate, speRemainData, interimBefore, useInfor, param.getBaseDate(), param.isMode());
			
			//管理データと暫定データの相殺
			getOffsetDay = getOffsetDay1004(require, param.getCid(), param.getSid(), param.getComplileDate(), param.getBaseDate(),
					param.getSpecialLeaveCode(), speRemainData, specialHolidayInterimDataMng, 
					grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0, useInfor, param.isMode());			
			SpecialHolidayRemainInfor grantDetailAfter = getOffsetDay.getRemainDays().getGrantDetailAfter().isPresent() ? getOffsetDay.getRemainDays().getGrantDetailAfter().get() :
				new SpecialHolidayRemainInfor(0, 0, 0);
			//付与後の残数情報をまとめる
			getOffsetDay = grantDetailAfter(grantDetailAfter, getOffsetDay);
			getOffsetDay.getUseOutPeriod().addAll(useInfor.getUseDaysOutPeriod());			
		}
		return getOffsetDay;
	}
	
	private static InPeriodOfSpecialLeave getInforNextProcess(RequireM4 require, CacheCarrier cacheCarrier, ComplileInPeriodOfSpecialLeaveParam param,
			ManagaData grantRemainData, InPeriodOfSpecialLeave getOffsetDay, InPeriodOfSpecialLeave speTmp) {
		DataMngOfDeleteExpired expiredData = new DataMngOfDeleteExpired(0.0, new ArrayList<>(), new HashMap<>());
		//集計終了日の翌日開始時点の未消化数をもとめる
		if(!getOffsetDay.getLstSpeLeaveGrantDetails().isEmpty()) {			
			List<SpecialLeaveGrantDetails> lstSpeLeaveGrantDetails = speTmp.getLstSpeLeaveGrantDetails();
			List<SpecialLeaveGrantRemainingData> lstRemainTmp = lstSpeLeaveGrantDetails.stream().map(speLeaveGrantDetails -> {
				return new SpecialLeaveGrantRemainingData(speLeaveGrantDetails.getSpecialID(),
						param.getCid(),
						param.getSid(),
						new SpecialVacationCD(param.getSpecialLeaveCode()),
						speLeaveGrantDetails.getGrantDate(),
						speLeaveGrantDetails.getDeadlineDate(),
						speLeaveGrantDetails.getExpirationStatus(),
						GrantRemainRegisterType.MONTH_CLOSE,
						new SpecialLeaveNumberInfo(new SpecialLeaveGrantNumber(new DayNumberOfGrant(speLeaveGrantDetails.getDetails().getGrantDays()), Optional.empty()), 
								new SpecialLeaveUsedNumber(new DayNumberOfUse(speLeaveGrantDetails.getDetails().getUseDays()), Optional.empty(), Optional.empty(), Optional.empty()),
								new SpecialLeaveRemainingNumber(new DayNumberOfRemain(speLeaveGrantDetails.getDetails().getRemainDays()), Optional.empty())));
			}).collect(Collectors.toList());
			//期限切れの管理データを期限切れに変更する
			expiredData = unDigestedDay(lstRemainTmp, param.getComplileDate().end().addDays(1), param.isMode());
		}	
		//「特別休暇の残数」．未消化数 += 未消化数(output)
		speTmp.getRemainDays().setUnDisgesteDays(speTmp.getRemainDays().getUnDisgesteDays() + expiredData.getUnDigestedDay());
		//集計終了日の翌日開始時点の残数情報をまとめる
		speTmp = speRemainNextInfor(
				require,
				cacheCarrier,
				param.getCid(),
				param.getSid(),
				param.getSpecialLeaveCode(),
				param.getComplileDate().end().addDays(1),
				expiredData.getLstGrantData(),
				speTmp,
				grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0);
		return speTmp;
	}
	
	private static InPeriodOfSpecialLeave toInPeriodOfSpecialLeave(InPeriodOfSpecialLeave getOffsetDay) {
		List<SpecialLeaveGrantDetails> lstSpeLeaveGrantDetailsAfter = getOffsetDay.getLstSpeLeaveGrantDetails().stream().map(x -> {
			SpecialLeaveNumberInfoService detailsAfer = new SpecialLeaveNumberInfoService(x.getDetails().getRemainDays(),
					x.getDetails().getUseDays(),
					x.getDetails().getGrantDays(),
					x.getDetails().getRemainTimes(),
					x.getDetails().getUseTimes(),
					x.getDetails().getLimitDays(),
					x.getDetails().getGrantTimes());
			return new SpecialLeaveGrantDetails(x.getCode(),
					x.getDataAtr(),
					x.getExpirationStatus(),
					x.getDeadlineDate(),
					x.getSid(),
					x.getGrantDate(),
					detailsAfer,
					x.getSpecialID()); 
		}).collect(Collectors.toList());
		RemainDaysOfSpecialHoliday remainDaysBefore = getOffsetDay.getRemainDays();
		Optional<SpecialHolidayRemainInfor> optgrantDetailAfterOfBefore = Optional.empty();
		if(remainDaysBefore.getGrantDetailAfter().isPresent()) {
			SpecialHolidayRemainInfor tmp = remainDaysBefore.getGrantDetailAfter().get();
			SpecialHolidayRemainInfor tmpAfter = new SpecialHolidayRemainInfor(tmp.getRemainDays(),
					tmp.getUseDays(),
					tmp.getGrantDays());
			optgrantDetailAfterOfBefore = Optional.of(tmpAfter);
		}
		List<UseDaysOfPeriodSpeHoliday> useDaysOutPeriodAfter = remainDaysBefore.getUseDaysOutPeriod().stream().map(y -> {
			return new UseDaysOfPeriodSpeHoliday(y.getYmd(),
					y.getUseDays(),
					y.getUseTimes());
		}).collect(Collectors.toList());
		RemainDaysOfSpecialHoliday remainDaysAfter = new RemainDaysOfSpecialHoliday(new SpecialHolidayRemainInfor(remainDaysBefore.getGrantDetailBefore().getRemainDays(),
				remainDaysBefore.getGrantDetailBefore().getUseDays(),
				remainDaysBefore.getGrantDetailBefore().getGrantDays()),
				remainDaysBefore.getUnDisgesteDays(),
				optgrantDetailAfterOfBefore,
				useDaysOutPeriodAfter);
		List<UseDaysOfPeriodSpeHoliday> useOutPeriodAfter = getOffsetDay.getUseOutPeriod().stream().map(z -> {
			return new UseDaysOfPeriodSpeHoliday(z.getYmd(),
					z.getUseDays(),
					z.getUseTimes());
		}).collect(Collectors.toList());
		InPeriodOfSpecialLeave speTmp = new InPeriodOfSpecialLeave(lstSpeLeaveGrantDetailsAfter, 
				remainDaysAfter,
				useOutPeriodAfter,
				getOffsetDay.getLstError());
		return speTmp;
	}
	
	private static List<SpecialLeaveError> lstError(InPeriodOfSpecialLeave output){
		List<SpecialLeaveError> lstError = new ArrayList<>();
		//「特別休暇期間外の使用」をチェックする
		if(!output.getUseOutPeriod().isEmpty()) {
			lstError.add(SpecialLeaveError.OUTOFUSE);
		}
		//「特別休暇の残数」．付与前明細．残数をチェックする
		if(output.getRemainDays().getGrantDetailBefore().getRemainDays() < 0) {
			lstError.add(SpecialLeaveError.BEFOREGRANT);
		}
		//「特別休暇の残数」．付与後明細．残数をチェックする
		output.getRemainDays().getGrantDetailAfter().ifPresent(x -> {
			if(x.getRemainDays() < 0) {
				lstError.add(SpecialLeaveError.AFTERGRANT);
			}
		});
		
		return lstError;
	}
	
	private static InPeriodOfSpecialLeave speRemainNextInfor(RequireM4 require, CacheCarrier cacheCarrier,String cid, String sid, int speCode, GeneralDate baseDate,
			List<SpecialLeaveGrantRemainingData> lstSpeData, InPeriodOfSpecialLeave speTmp, double limitDays) {
		//[No.373]社員の特別休暇情報を取得する
		InforSpecialLeaveOfEmployee getSpecialHolidayOfEmp = InforSpecialLeaveOfEmployeeSevice.getInforSpecialLeaveOfEmployee(require, cacheCarrier, 
				cid,
				sid,
				speCode,
				new DatePeriod(baseDate, baseDate), null);
		if(getSpecialHolidayOfEmp.getStatus() != InforStatus.GRANTED) {
			return speTmp;
		}		
		List<SpecialHolidayInfor> lstInfor = getSpecialHolidayOfEmp.getSpeHolidayInfor();
		if(lstInfor.isEmpty()) {
			return speTmp;
		}
		SpecialHolidayInfor speInfor = lstInfor.get(0);
		if(speInfor.getGrantDaysInfor().getErrorFlg().isPresent()) {
			return speTmp;
		}
		//取得した特別休暇の付与データを「特別休暇の付与明細」に１行を追加する
		SpecialHolidayRemainInfor afterData = speTmp.getRemainDays().getGrantDetailAfter().isPresent()
				? speTmp.getRemainDays().getGrantDetailAfter().get() : new SpecialHolidayRemainInfor(0, 0, 0);
		double granDay = speInfor.getGrantDaysInfor().getGrantDays();
		//「特別休暇の残数」．付与後明細．付与数 += 「特別休暇の利用情報」．付与数
		afterData.setGrantDays(afterData.getGrantDays() + granDay);
		//繰越上限日数まで調整する
		DataMngOfDeleteExpired adjustCarryForward = adjustCarryForward1005(require, lstSpeData, limitDays);
		//「特別休暇の残数」．未消化数 += 未消化数(output)
		speTmp.getRemainDays().setUnDisgesteDays(speTmp.getRemainDays().getUnDisgesteDays() + adjustCarryForward.getUnDigestedDay());
		//「特別休暇の残数」．付与後明細．残数 += 「特別休暇の利用情報」．残数
		adjustCarryForward.getLstGrantData().stream().forEach(x -> {
			afterData.setRemainDays(afterData.getRemainDays() + x.getDetails().getRemainingNumber().getDayNumberOfRemain().v());
		});
		speTmp.getRemainDays().setGrantDetailAfter(Optional.of(afterData));
		SpecialLeaveNumberInfoService numberInfor = new SpecialLeaveNumberInfoService(granDay,
				0,
				granDay,
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty());
		String mngId = IdentifierUtil.randomUniqueId();
		SpecialLeaveGrantDetails detailAdd = new SpecialLeaveGrantDetails(speCode,
				DataAtr.GRANTSCHE,
				LeaveExpirationStatus.AVAILABLE,
				speInfor.getDeadlineDate().isPresent() ? speInfor.getDeadlineDate().get() : GeneralDate.max(),
				sid,
				speInfor.getGrantDaysInfor().getYmd(),
				numberInfor,
				mngId);
		speTmp.getLstSpeLeaveGrantDetails().add(detailAdd);
		return speTmp;
	}
	
	/**
	 * 付与日が同じ管理データを排除する
	 * @param lstDatabase
	 * @param lstMemoryData
	 * @return
	 */
	private static List<SpecialLeaveGrantRemainingData> adjustGrantData(List<SpecialLeaveGrantRemainingData> lstDatabase, 
			List<SpecialLeaveGrantRemainingData> lstMemoryData){
		//INPUT．「特別休暇付与残数データ」(old)をOUTPUT．「特別休暇付与残数データ」に追加する
		List<SpecialLeaveGrantRemainingData> lstOutputData = new ArrayList<>(lstDatabase);
		//INPUT．「特別休暇付与残数データ」(new)をループする
		for (SpecialLeaveGrantRemainingData memoryData : lstMemoryData) {
			//INPUT．「特別休暇付与残数データ」(old)の中に、ループ中の「特別休暇付与残数データ」(new)の付与日と一致する項目が存在するかチェックする
			List<SpecialLeaveGrantRemainingData> lstTemp = lstDatabase.stream()
					.filter(x -> x.getGrantDate().equals(memoryData.getGrantDate()))
					.collect(Collectors.toList());
			if(lstTemp.isEmpty()) {
				//ループ中の「特別休暇付与残数データ」(new)をOUTPUT．「特別休暇付与残数データ」に追加する
				lstOutputData.add(memoryData);
			}
		}
		
		return lstOutputData;
	}

	/**
	 * 指定期間の使用数を求める
	 * @param lstInterimData
	 * @param dateData
	 * @return
	 */
	private static double useDayFormGrant(List<InterimSpecialHolidayMng> lstInterimData, 
			List<InterimRemain> lstInterimMng, DatePeriod dateData) {
		double outputData = 0;
		for (InterimSpecialHolidayMng interimMng : lstInterimData) {
			List<InterimRemain> optInterimMng = lstInterimMng.stream()
					.filter(z -> z.getRemainManaID().equals(interimMng.getSpecialHolidayId()))
					.collect(Collectors.toList());
			if(!optInterimMng.isEmpty()) {
				InterimRemain interimMngData = optInterimMng.get(0);
				//ループ中のドメインモデル「特別休暇暫定データ」．年月日とINPUT．開始日、終了日を比較する
				if(interimMngData.getYmd().afterOrEquals(dateData.start())
						&& interimMngData.getYmd().beforeOrEquals(dateData.end())) {
					//使用数 += ループ中のドメインモデル「特別休暇暫定データ」．特休使用
					outputData += interimMng.getUseDays().isPresent() ? interimMng.getUseDays().get().v() : 0;
				}
			}
		}
		return outputData;
	}
	
	/**
	 * 期限切れの管理データを期限切れに変更する
	 * @param lstGrantData
	 * @param baseDate
	 * @return
	 */
	private static DataMngOfDeleteExpired unDigestedDay(List<SpecialLeaveGrantRemainingData> lstGrantData, 
			GeneralDate baseDate, boolean isMode) {
		double unDisgesteDays = 0;
		List<SpecialLeaveGrantRemainingData> lstTmp = new ArrayList<>(lstGrantData);
		for (SpecialLeaveGrantRemainingData grantData : lstGrantData) {
			//期限切れかチェックする
			//・モードがその他
			//INPUT．特別休暇付与残数データ．期限日 >= INPUT．集計終了日
			//・モードが月次
			//INPUT．特別休暇付与残数データ．期限日 > INPUT．集計終了日
			if(grantData.getExpirationStatus() != LeaveExpirationStatus.AVAILABLE) {
				continue;
			}
			if( (!isMode && !grantData.getDeadlineDate().afterOrEquals(baseDate))
					|| (isMode && !grantData.getDeadlineDate().after(baseDate))) {
				//未消化数+=「特別休暇数情報」．残数
				unDisgesteDays += grantData.getDetails().getRemainingNumber().getDayNumberOfRemain().v();
				//ループ中の「特別休暇付与残数データ」．期限切れ状態=期限切れ
				lstTmp.remove(grantData);
				grantData.setExpirationStatus(LeaveExpirationStatus.EXPIRED);
				lstTmp.add(grantData);
			}
		}
		//return new DataMngOfDeleteExpired(unDisgesteDays, lstTmp);
		return new DataMngOfDeleteExpired(unDisgesteDays, lstTmp, null);
	}
	
	public static interface RequireM1 {

		Optional<SpecialLeaveGrantRemainingData> specialLeaveGrantRemainingData(String specialId);
		
	}
	
	public static interface RequireM2 { 

		List<InterimRemain> interimRemains(String employeeId, DatePeriod dateData, RemainType remainType);

		List<InterimSpecialHolidayMng> interimSpecialHolidayMng(String mngId);
	}
	
	public static interface RequireM3 extends InforSpecialLeaveOfEmployeeSevice.RequireM4 {

		/** 特別休暇付与残数データ */
		List<SpecialLeaveGrantRemainingData> specialLeaveGrantRemainingData(String sid, int specialLeaveCode, 
				LeaveExpirationStatus expirationStatus,GeneralDate grantDate, GeneralDate deadlineDate);

		/** 特別休暇付与残数データ */
		List<SpecialLeaveGrantRemainingData> specialLeaveGrantRemainingData(String sid, int speCode, 
				DatePeriod datePriod, GeneralDate startDate, LeaveExpirationStatus expirationStatus);
	}
	
	public static interface RequireM4 extends InforSpecialLeaveOfEmployeeSevice.RequireM4, RequireM1 {
		
	}
	
	public static interface RequireM5 extends RequireM2, RequireM3, RequireM4 {

		/** 特別休暇基本情報 */
		Optional<SpecialLeaveBasicInfo> specialLeaveBasicInfo(String sid, int spLeaveCD, UseAtr use);
		
		/** 所属会社履歴 */
		List<AffCompanyHistImport> listAffCompanyHistImport(List listAppId, DatePeriod period);
		
		/** 締め状態管理 */
		Optional<ClosureStatusManagement> latestClosureStatusManagement(String employeeId);
	}

	public static RequireM5 createRequireM5(SpecialLeaveGrantRepository specialLeaveGrantRepo,
			ShareEmploymentAdapter shareEmploymentAdapter, EmpEmployeeAdapter empEmployeeAdapter,
			GrantDateTblRepository grantDateTblRepo, AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo,
			SpecialHolidayRepository specialHolidayRepo, InterimSpecialHolidayMngRepository interimSpecialHolidayMngRepo,
			InterimRemainRepository interimRemainRepo, SpecialLeaveBasicInfoRepository specialLeaveBasicInfoRepo,
			SyCompanyRecordAdapter syCompanyRecordAdapter) {
		
		return new RequireM5Impl(specialLeaveGrantRepo, shareEmploymentAdapter, 
				empEmployeeAdapter, grantDateTblRepo, annLeaEmpBasicInfoRepo, 
				specialHolidayRepo, interimSpecialHolidayMngRepo, interimRemainRepo, 
				specialLeaveBasicInfoRepo, syCompanyRecordAdapter);
	}
	
	@AllArgsConstructor
	public static class RequireM5Impl implements RequireM5 {
		
		protected SpecialLeaveGrantRepository specialLeaveGrantRepo;
		protected ShareEmploymentAdapter shareEmploymentAdapter;
		protected EmpEmployeeAdapter empEmployeeAdapter;
		protected GrantDateTblRepository grantDateTblRepo;
		protected AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo;
		protected SpecialHolidayRepository specialHolidayRepo;
		protected InterimSpecialHolidayMngRepository interimSpecialHolidayMngRepo;
		protected InterimRemainRepository interimRemainRepo;
		protected SpecialLeaveBasicInfoRepository specialLeaveBasicInfoRepo;
		protected SyCompanyRecordAdapter syCompanyRecordAdapter;
		protected ClosureStatusManagementRepository closureStatusManagementRepo;
		
		/** 特別休暇付与残数データ */
		@Override
		public Optional<SpecialLeaveGrantRemainingData> specialLeaveGrantRemainingData(String specialId) {
			return specialLeaveGrantRepo.getBySpecialId(specialId);
		}
		
		@Override
		public Optional<BsEmploymentHistoryImport> employmentHistory(CacheCarrier cacheCarrier, String companyId,
				String employeeId, GeneralDate baseDate) {
			return shareEmploymentAdapter.findEmploymentHistoryRequire(cacheCarrier, companyId, employeeId, baseDate);
		}
		
		@Override
		public EmployeeRecordImport employeeFullInfo(CacheCarrier cacheCarrier, String empId) {
			return empEmployeeAdapter.findByAllInforEmpId(cacheCarrier, empId);
		}
		
		@Override
		public EmployeeImport employeeInfo(CacheCarrier cacheCarrier, String empId) {
			return empEmployeeAdapter.findByEmpIdRequire(cacheCarrier, empId);
		}
		
		@Override
		public List<SClsHistImport> employeeClassificationHistoires(CacheCarrier cacheCarrier, String companyId,
				List<String> employeeIds, DatePeriod datePeriod) {
			return empEmployeeAdapter.lstClassByEmployeeId(cacheCarrier, companyId, employeeIds, datePeriod);
		}
		
		/** 特別休暇付与テーブル */
		@Override
		public Optional<GrantDateTbl> grantDateTbl(String companyId, int specialHolidayCode) {
			return grantDateTblRepo.findByCodeAndIsSpecified(companyId, specialHolidayCode);
		}
		
		/** 経過年数に対する付与日数 */
		@Override
		public List<ElapseYear> elapseYear(String companyId, int specialHolidayCode, String grantDateCode) {
			return grantDateTblRepo.findElapseByGrantDateCd(companyId, specialHolidayCode, grantDateCode);
		}
		
		/** 年休社員基本情報 */
		@Override
		public Optional<AnnualLeaveEmpBasicInfo> employeeAnnualLeaveBasicInfo(String employeeId) {
			return annLeaEmpBasicInfoRepo.get(employeeId);
		}
		
		@Override
		public List<AffCompanyHistSharedImport> employeeAffiliatedCompanyHistories(CacheCarrier cacheCarrier,
				List<String> sids, DatePeriod datePeriod) {
			return empEmployeeAdapter.getAffCompanyHistByEmployee(cacheCarrier, sids, datePeriod);
		}
		
		/** 特別休暇 */
		@Override
		public Optional<SpecialHoliday> specialHoliday(String companyID, int specialHolidayCD) {
			return specialHolidayRepo.findByCode(companyID, specialHolidayCD);
		}
		
		/** 特別休暇付与残数データ */
		@Override
		public List<SpecialLeaveGrantRemainingData> specialLeaveGrantRemainingData(String sid, int speCode,
				DatePeriod datePriod, GeneralDate startDate, LeaveExpirationStatus expirationStatus) {
			return specialLeaveGrantRepo.getByNextDate(sid, speCode, datePriod, startDate, expirationStatus);
		}
		
		/** 特別休暇付与残数データ */
		@Override
		public List<SpecialLeaveGrantRemainingData> specialLeaveGrantRemainingData(String sid, int specialLeaveCode,
				LeaveExpirationStatus expirationStatus, GeneralDate grantDate, GeneralDate deadlineDate) {
			return specialLeaveGrantRepo.getByPeriodStatus(sid, specialLeaveCode, expirationStatus, grantDate, deadlineDate);
		}
		
		/** 特別休暇暫定データ */
		@Override
		public List<InterimSpecialHolidayMng> interimSpecialHolidayMng(String mngId) {
			return interimSpecialHolidayMngRepo.findById(mngId);
		}
		
		/** 暫定残数管理データ */
		@Override
		public List<InterimRemain> interimRemains(String employeeId, DatePeriod dateData, RemainType remainType) {
			return interimRemainRepo.getRemainBySidPriod(employeeId, dateData, remainType);
		}
		
		/** 特別休暇基本情報 */
		@Override
		public Optional<SpecialLeaveBasicInfo> specialLeaveBasicInfo(String sid, int spLeaveCD, UseAtr use) {
			return specialLeaveBasicInfoRepo.getBySidLeaveCdUser(sid, spLeaveCD, use);
		}
		
		/** 所属会社履歴 */
		@Override
		public List<AffCompanyHistImport> listAffCompanyHistImport(
				List sids, DatePeriod period){
			this.syCompanyRecordAdapter.getAffCompanyHistByEmployee(new ArrayList<>(sids), period);
		}

		/** 締め状態管理 */
		@Override
		public Optional<ClosureStatusManagement> latestClosureStatusManagement(String employeeId) {
			return closureStatusManagementRepo.getLatestByEmpId(employeeId);
		}
		
	}
}
