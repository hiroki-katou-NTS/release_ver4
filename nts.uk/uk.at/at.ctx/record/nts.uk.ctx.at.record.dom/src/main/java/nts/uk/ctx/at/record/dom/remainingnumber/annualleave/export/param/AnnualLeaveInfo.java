package nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.GrantPeriodAtr;
import nts.uk.ctx.at.shared.dom.common.days.MonthlyDays;
import nts.uk.ctx.at.shared.dom.common.days.YearlyDays;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveGrantDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedDayNumber;
//import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.UsedMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.RemNumShiftListWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.LeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveNumberInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveRemainingNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveUndigestDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveUndigestTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveUsedNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.erroralarm.AnnualLeaveError;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnualLeaveGrant;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnualLeaveUsedNumber;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AttendanceRate;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPriority;

/**
 * 年休情報
 * @author shuichi_ishida
 */
@Getter
@Setter
public class AnnualLeaveInfo implements Cloneable {

	/** 年月日 */
	private GeneralDate ymd;
	/** 残数 */
	private AnnualLeaveRemaining remainingNumber;
	/** 付与残数データ */
	private List<AnnualLeaveGrantRemainingData> grantRemainingDataList;
	/** 上限データ */
	private AnnualLeaveMaxData maxData;
	/** 付与情報 */
	private Optional<AnnualLeaveGrant> grantInfo;
	/** 使用日数 */
	private AnnualLeaveUsedDayNumber usedDays;
	/** 使用時間 */
	private UsedMinutes usedTime;
//	/** 付与後フラグ */
//	private boolean afterGrantAtr;

	/** 年休設定 */
	private AnnualPaidLeaveSetting annualPaidLeaveSet;

	/**
	 * コンストラクタ
	 */
	public AnnualLeaveInfo(){

		this.ymd = GeneralDate.min();
		this.remainingNumber = new AnnualLeaveRemaining();
		this.grantRemainingDataList = new ArrayList<>();
		this.maxData = new AnnualLeaveMaxData();
		this.grantInfo = Optional.empty();
		this.usedDays = new AnnualLeaveUsedDayNumber(0.0);
		this.usedTime = new UsedMinutes(0);
//		this.afterGrantAtr = false;

		this.annualPaidLeaveSet = null;
	}

	/**
	 * ファクトリー
	 * @param ymd 年月日
	 * @param remainingNumber 残数
	 * @param grantRemainingNumberList 付与残数データ
	 * @param maxData 上限データ
	 * @param grantInfo 付与情報
	 * @param usedDays 使用日数
	 * @param usedTime 使用時間
	 * @param afterGrantAtr 付与後フラグ
	 * @return 年休情報
	 */
	public static AnnualLeaveInfo of(
			GeneralDate ymd,
			AnnualLeaveRemaining remainingNumber,
			List<AnnualLeaveGrantRemainingData> grantRemainingNumberList,
			AnnualLeaveMaxData maxData,
			Optional<AnnualLeaveGrant> grantInfo,
			AnnualLeaveUsedDayNumber usedDays,
			UsedMinutes usedTime,
			boolean afterGrantAtr){

		AnnualLeaveInfo domain = new AnnualLeaveInfo();
		domain.ymd = ymd;
		domain.remainingNumber = remainingNumber;
		domain.grantRemainingDataList = grantRemainingNumberList;
		domain.maxData = maxData;
		domain.grantInfo = grantInfo;
		domain.usedDays = usedDays;
		domain.usedTime = usedTime;
//		domain.afterGrantAtr = afterGrantAtr;
		return domain;
	}

	@Override
	public AnnualLeaveInfo clone() {
		AnnualLeaveInfo cloned = new AnnualLeaveInfo();
		try {
			cloned.ymd = this.ymd;
			cloned.remainingNumber = this.remainingNumber.clone();
			for (val grantRemainingNumber : this.grantRemainingDataList){
				val detail = grantRemainingNumber.getDetails();
				Integer grantMinutes = null;
				if (detail.getGrantNumber().getMinutes().isPresent()){
					grantMinutes = detail.getGrantNumber().getMinutes().get().v();
				}
				Integer usedMinutes = null;
				if (detail.getUsedNumber().getMinutes().isPresent()){
					usedMinutes = detail.getUsedNumber().getMinutes().get().v();
				}
				Double stowageDays = null;
				if (detail.getUsedNumber().getStowageDays().isPresent()){
					stowageDays = detail.getUsedNumber().getStowageDays().get().v();
				}
				Integer remainMinutes = null;
				if (detail.getRemainingNumber().getMinutes().isPresent()){
					remainMinutes = detail.getRemainingNumber().getMinutes().get().v();
				}
				Double prescribedDays = null;
				Double deductedDays = null;
				Double workingDays = null;
				if (grantRemainingNumber.getAnnualLeaveConditionInfo().isPresent()){
					val annualLeaveCond = grantRemainingNumber.getAnnualLeaveConditionInfo().get();
					prescribedDays = annualLeaveCond.getPrescribedDays().v();
					deductedDays = annualLeaveCond.getDeductedDays().v();
					workingDays = annualLeaveCond.getWorkingDays().v();
				}
				AnnualLeaveGrantRemainingData newRemainData
						= AnnualLeaveGrantRemainingData.createFromJavaType(
								grantRemainingNumber.getLeaveID(),
								grantRemainingNumber.getEmployeeId(),
								grantRemainingNumber.getGrantDate(),
								grantRemainingNumber.getDeadline(),
								grantRemainingNumber.getExpirationStatus().value,
								grantRemainingNumber.getRegisterType().value,
								detail.getGrantNumber().getDays().v(),
								grantMinutes,
								detail.getUsedNumber().getDays().v(),
								usedMinutes,
								stowageDays,
								detail.getRemainingNumber().getDays().v(),
								remainMinutes,
								0.0,
								prescribedDays,
								deductedDays,
								workingDays);
				cloned.grantRemainingDataList.add(newRemainData);
			}
			if (this.grantInfo.isPresent()){
				cloned.grantInfo = Optional.of(this.grantInfo.get().clone());
			}
			cloned.usedDays = new AnnualLeaveUsedDayNumber(this.usedDays.v());
			cloned.usedTime = new UsedMinutes(this.usedTime.v());
//			cloned.afterGrantAtr = this.afterGrantAtr;

			// 以下は、cloneしなくてよい。
			cloned.maxData = this.maxData;
			cloned.annualPaidLeaveSet = this.annualPaidLeaveSet;
		}
		catch (Exception e){
			throw new RuntimeException("AnnualLeaveInfo clone error.");
		}
		return cloned;
	}

	public List<AnnualLeaveGrantRemainingData> getGrantRemainingNumberList(){
		return this.grantRemainingDataList.stream().map(c -> (AnnualLeaveGrantRemainingData)c)
				.collect(Collectors.toList());
	}

	/**
	 * 年休付与残数を更新
	 */
	public void updateRemainingNumber(boolean afterGrant){
		this.remainingNumber.updateRemainingNumber(this.grantRemainingDataList, afterGrant);
	}

	/**
	 * 年休の消滅・付与・消化
	 * @param require
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggregatePeriodWork 年休集計期間WORK
	 * @param tempAnnualLeaveMngs 暫定年休管理データリスト
	 * @param isGetNextMonthData 翌月管理データ取得フラグ
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 * @param aggrResult 年休の集計結果
	 * @param annualPaidLeaveSet 年休設定
	 * @return 年休の集計結果
	 */
	public AggrResultOfAnnualLeave lapsedGrantDigest(
			LeaveRemainingNumber.RequireM3 require,
			String companyId,
			String employeeId,
			AggregatePeriodWork aggregatePeriodWork,
			List<TmpAnnualLeaveMngWork> tempAnnualLeaveMngs,
			AggrResultOfAnnualLeave aggrResult,
			AnnualPaidLeaveSetting annualPaidLeaveSet){

		this.annualPaidLeaveSet = annualPaidLeaveSet;

		//○付与前退避処理
		this.saveStateBeforeGrant(aggregatePeriodWork);

		//○年休情報．年月日を開始日に更新
		this.ymd = aggregatePeriodWork.getPeriod().end();

		//○消滅処理
		aggrResult = this.lapsedProcess(aggregatePeriodWork, aggrResult);

		//○付与処理
		aggrResult = this.grantProcess(companyId, employeeId,
				aggregatePeriodWork, aggrResult);

		//○消化処理
		aggrResult = this.digestProcess(
				require, companyId, employeeId,
				aggregatePeriodWork, tempAnnualLeaveMngs, aggrResult);

		// ○エラーをチェックする
		List<AnnualLeaveError> lstError = checkError(aggregatePeriodWork);

		//期間終了退避処理
		periodEndSaveProcess(companyId, employeeId, aggregatePeriodWork, aggrResult);

		//終了時点更新処理
		this.updateProcessEnd(companyId, employeeId, aggregatePeriodWork, aggrResult, lstError);

		//○「年休の集計結果」を返す
		return aggrResult;

	}

	/**
	 * 付与前退避処理
	 * @param aggregatePeriodWork 処理中の年休集計期間WORK
	 */
	private void saveStateBeforeGrant(AggregatePeriodWork aggregatePeriodWork){

		// 「年休集計期間WORK.付与フラグ」をチェック
		if (!aggregatePeriodWork.getGrantWork().isGrantAtr()) return;

		// 初回付与かチェックする
		if (!check(aggregatePeriodWork)){
			return;
		}

		// 現在の年休（マイナスあり）の残数を付与前として退避する
		val withMinus = this.remainingNumber.getAnnualLeaveWithMinus();
		withMinus.saveStateBeforeGrant();

		// 現在の年休（マイナスなし）の残数を付与前として退避する
		val noMinus = this.remainingNumber.getAnnualLeaveNoMinus();
		noMinus.saveStateAfterGrant();
	}

	//初回付与かチェックする
	public boolean check(AggregatePeriodWork aggregatePeriodWork) {

		//期間開始日に付与があるか
		if(!aggregatePeriodWork.getGrantWork().isGrantAtr()) {
			return false;
		}

		//初回付与か
		return aggregatePeriodWork.getGrantWork().getGrantNumber() == 1;
	}


	/**
	 * 消滅処理
	 * @param aggregatePeriodWork 処理中の年休集計期間WORK
	 * @param aggrResult 年休の集計結果
	 * @return 年休の集計結果
	 */
	private AggrResultOfAnnualLeave lapsedProcess(
			AggregatePeriodWork aggregatePeriodWork,
			AggrResultOfAnnualLeave aggrResult){

		// 消滅フラグを取得
		if (!aggregatePeriodWork.getLapsedAtr().isLapsedAtr()) return aggrResult;

		// 「付与残数データ」を取得
		extinguishAnnualLeave(aggregatePeriodWork);

		// 年休情報残数を更新
		this.updateRemainingNumber(aggregatePeriodWork.getGrantPeriodAtr() == GrantPeriodAtr.AFTER_GRANT);

		// 年休情報を「年休の集計結果．年休情報（消滅）」に追加
		if (!aggrResult.getLapsed().isPresent()) aggrResult.setLapsed(Optional.of(new ArrayList<>()));
		aggrResult.getLapsed().get().add(this.clone());

		// 「年休の集計結果」を返す
		return aggrResult;
	}

	//年休を消滅させる
	public void extinguishAnnualLeave(AggregatePeriodWork aggregatePeriodWork) {
		// 「付与残数データ」を取得
				val itrGrantRemainingNumber = this.grantRemainingDataList.listIterator();
				while (itrGrantRemainingNumber.hasNext()){
					val grantRemainingNumber = itrGrantRemainingNumber.next();

					// 期限日が年休集計期間WORK.期間.開始日の前日でなければ、消滅処理しない
					if (!grantRemainingNumber.getDeadline().equals(aggregatePeriodWork.getPeriod().start().addDays(-1))){
						continue;
					}

					// 年休不足ダミーフラグがtrueなら、消滅処理しない
					if (grantRemainingNumber.isShortageRemain() == true) continue;

					// 処理中の付与残数データを期限切れにする
					grantRemainingNumber.setExpirationStatus(LeaveExpirationStatus.EXPIRED);

					// 未消化数を更新
			this.remainingNumber.getAnnualLeaveUndigestNumber().ifPresent(x -> {
				x.setDays(new LeaveUndigestDayNumber(
						x.getDays().v() + grantRemainingNumber.getDetails().getUsedNumber().getDays().v()));
				x.setMinutes(Optional.of(new LeaveUndigestTime(x.getMinutes().map(y -> y.v()).orElse(0)
						+ grantRemainingNumber.getDetails().getUsedNumber().getMinutes().map(y -> y.v()).orElse(0))));
			});
				}
	}

	/**
	 * 付与処理
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggregatePeriodWork 処理中の年休集計期間WORK
	 * @param aggrResult 年休の集計結果
	 * @return 年休の集計結果
	 */
	private AggrResultOfAnnualLeave grantProcess(
			String companyId,
			String employeeId,
			AggregatePeriodWork aggregatePeriodWork,
			AggrResultOfAnnualLeave aggrResult){

		// 「付与フラグ」をチェック
		if (!aggregatePeriodWork.getGrantWork().isGrantAtr()) return aggrResult;

		// 付与日から期限日を計算
		if (!aggregatePeriodWork.getGrantWork().getSpecialLeaveGrant().isPresent()) return aggrResult;
		val annualLeaveGrant = aggregatePeriodWork.getGrantWork().getSpecialLeaveGrant().get();
		val grantDate = annualLeaveGrant.getGrantDate();
		//val deadline = this.annualPaidLeaveSet.calcDeadline(grantDate);
		val deadline = annualLeaveGrant.getDeadLine();

		// 付与日数を確認する
		double grantDays = 0.0;
		if (aggregatePeriodWork.getGrantWork().getSpecialLeaveGrant().isPresent()){
			grantDays = aggregatePeriodWork.getGrantWork().getSpecialLeaveGrant().get().getGrantDays().v().doubleValue();
		}

		// 次回年休付与を確認する
		Double prescribedDays = null;
		Double deductedDays = null;
		Double workingDays = null;
		Double attendanceRate = 0.0;
//		if (aggregatePeriodWork.getGrantWork().getSpecialLeaveGrant().isPresent()){
//			val nextAnnLeaGrant = aggregatePeriodWork.getGrantWork().getSpecialLeaveGrant().get();
//			if (nextAnnLeaGrant.getPrescribedDays().isPresent()){
//				prescribedDays = nextAnnLeaGrant.getPrescribedDays().get().v();
//			}
//			if (nextAnnLeaGrant.getDeductedDays().isPresent()){
//				deductedDays = nextAnnLeaGrant.getDeductedDays().get().v();
//			}
//			if (nextAnnLeaGrant.getWorkingDays().isPresent()){
//				workingDays = nextAnnLeaGrant.getWorkingDays().get().v();
//			}
//			if (nextAnnLeaGrant.getAttendanceRate().isPresent()){
//				attendanceRate = nextAnnLeaGrant.getAttendanceRate().get().v().doubleValue();
//			}
//		}

		// 「年休付与残数データ」を作成する
		val newRemainData = AnnualLeaveGrantRemainingData.createFromJavaType(
				"", employeeId, grantDate, deadline,
				LeaveExpirationStatus.AVAILABLE.value, GrantRemainRegisterType.MONTH_CLOSE.value,
				grantDays, null,
				0.0, null, null,
				grantDays, null,
				0.0,
				prescribedDays,
				deductedDays,
				workingDays);

		// 作成した「年休付与残数データ」を付与残数データリストに追加
		this.grantRemainingDataList.add(newRemainData);

//		// 付与後フラグ　←　true
//		this.afterGrantAtr = true;

		// 年休情報．付与情報に付与時の情報をセット
		double oldGrantDays = 0.0;
		if (this.grantInfo.isPresent()){
			oldGrantDays = this.grantInfo.get().getGrantDays().v().doubleValue();
		}
		this.grantInfo = Optional.of(AnnualLeaveGrant.of(
				new AnnualLeaveGrantDayNumber(oldGrantDays + grantDays),
				new YearlyDays(workingDays == null ? 0.0 : workingDays),
				new YearlyDays(prescribedDays == null ? 0.0 : prescribedDays),
				new YearlyDays(deductedDays == null ? 0.0 : deductedDays),
				new MonthlyDays(0.0),
				new MonthlyDays(0.0),
				new AttendanceRate(attendanceRate)));

		// 年休情報残数を更新
		this.updateRemainingNumber(aggregatePeriodWork.getGrantPeriodAtr() == GrantPeriodAtr.AFTER_GRANT);

		// 「年休情報（付与時点）」に「年休情報」を追加
		if (!aggrResult.getAsOfGrant().isPresent()) aggrResult.setAsOfGrant(Optional.of(new ArrayList<>()));
		aggrResult.getAsOfGrant().get().add(this.clone());

		// 「年休の集計結果」を返す
		return aggrResult;
	}

	/**
	 * 消化処理
	 * @param repositoriesRequiredByRemNum 残数処理 キャッシュクラス
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param aggregatePeriodWork 処理中の年休集計期間WORK
	 * @param tempAnnualLeaveMngs 暫定年休管理データリスト
	 * @param aggrResult 年休の集計結果
	 * @return 年休の集計結果
	 */
	private AggrResultOfAnnualLeave digestProcess(
			LeaveRemainingNumber.RequireM3 require,
			String companyId,
			String employeeId,
			AggregatePeriodWork aggregatePeriodWork,
			List<TmpAnnualLeaveMngWork> tempAnnualLeaveMngs,
			AggrResultOfAnnualLeave aggrResult){

		// 年休使用数WORK
		AnnualLeaveUsedNumber usedNumber = new AnnualLeaveUsedNumber();

		// 「暫定年休管理データリスト」を取得する
		tempAnnualLeaveMngs.sort((a, b) -> a.getYmd().compareTo(b.getYmd()));
		for (val tempAnnualLeaveMng : tempAnnualLeaveMngs){
			if (!aggregatePeriodWork.getPeriod().contains(tempAnnualLeaveMng.getYmd())) continue;

			// 年休を消化する
			{
				// 年休付与残数を取得
				List<LeaveGrantRemainingData> targetRemainingDatas
					= new ArrayList<LeaveGrantRemainingData>();

				for (val remainingData : this.grantRemainingDataList){
					if (tempAnnualLeaveMng.getYmd().before(remainingData.getGrantDate())) continue;
					if (tempAnnualLeaveMng.getYmd().after(remainingData.getDeadline())) continue;
					targetRemainingDatas.add(remainingData);
				}

				// 取得設定をチェック
				if (this.annualPaidLeaveSet.getAcquisitionSetting().annualPriority == AnnualPriority.FIFO){

					// 当年付与分から消化する　（付与日　降順(DESC)）
					targetRemainingDatas.sort((a, b) -> -a.getGrantDate().compareTo(b.getGrantDate()));
				}
				else {

					// 繰越分から消化する　（付与日　昇順(ASC)）
					targetRemainingDatas.sort((a, b) -> a.getGrantDate().compareTo(b.getGrantDate()));
				}

				// 使用数変数作成
				LeaveUsedNumber leaveUsedNumber = new LeaveUsedNumber();
				leaveUsedNumber.setDays(new LeaveUsedDayNumber(tempAnnualLeaveMng.getUseDays().v()));
				// ooooo 要時間　leaveUsedNumber.setMinutes(minutes);


				//使用数に加算する
				Optional<AnnualLeaveUsedDayNumber> days = Optional.of(new AnnualLeaveUsedDayNumber(leaveUsedNumber.getDays().v()));
				Optional<UsedMinutes>times = Optional.of(new UsedMinutes(leaveUsedNumber.getMinutesOrZero().valueAsMinutes()));

				AnnualLeaveUsedNumber addNumber = AnnualLeaveUsedNumber.of(days,times);
				usedNumber.addUsedNumber(addNumber);

				// 「休暇残数シフトリストWORK」一時変数を作成
				RemNumShiftListWork remNumShiftListWork = new RemNumShiftListWork();

				// 休暇残数を指定使用数消化する
				LeaveGrantRemainingData.digest(
						require,
						targetRemainingDatas,
						remNumShiftListWork,
						leaveUsedNumber,
						companyId,
						employeeId,
						aggregatePeriodWork.getPeriod().start());

				// 残数不足で一部消化できなかったとき
				if ( !remNumShiftListWork.getUnusedNumber().isZero() ){

					// 消化できなかった休暇使用数をもとに、付与残数ダミーデータを作成する
					// 「年休付与残数データ」を作成する
					val dummyRemainData
							= AnnualLeaveGrantRemainingData.createFromJavaType(
							"", employeeId, tempAnnualLeaveMng.getYmd(), tempAnnualLeaveMng.getYmd(),
							LeaveExpirationStatus.AVAILABLE.value, GrantRemainRegisterType.MONTH_CLOSE.value,
							0.0, null,
							0.0, null, null,
							0.0, null,
							0.0,
							null, null, null);

//					// 年休を指定日数消化する
//					remainDaysWork = new ManagementDays(dummyRemainData.digest(remainDaysWork.v(), true));

					// 付与残数データに追加
					this.grantRemainingDataList.add(dummyRemainData);
				}
			}
		}
		// 実年休（年休（マイナスあり））に使用数を加算する
		this.getRemainingNumber().getAnnualLeaveWithMinus().addUsedNumber(
				usedNumber, aggregatePeriodWork.getGrantPeriodAtr() == GrantPeriodAtr.AFTER_GRANT);


		// 年休情報残数を更新
		this.updateRemainingNumber(aggregatePeriodWork.getGrantPeriodAtr() == GrantPeriodAtr.AFTER_GRANT);

		// 「年休の集計結果」を返す
		return aggrResult;
	}

	//エラーをチェックする
	public List<AnnualLeaveError> checkError(AggregatePeriodWork aggregatePeriodWork){
		List<AnnualLeaveError> annualLeaveErrors = new ArrayList<AnnualLeaveError>();
		// 年休残数がマイナスかチェック
		val withMinus = this.remainingNumber.getAnnualLeaveWithMinus();
		if (withMinus.getRemainingNumberInfo().getRemainingNumber().isMinus()){
			if (aggregatePeriodWork.getGrantPeriodAtr() == GrantPeriodAtr.AFTER_GRANT){
				// 「日単位年休不足エラー（付与後）」を追加
				annualLeaveErrors.add(AnnualLeaveError.SHORTAGE_AL_OF_UNIT_DAY_AFT_GRANT);
			}
			else {
				// 「日単位年休不足エラー（付与前）」を追加
				annualLeaveErrors.add(AnnualLeaveError.SHORTAGE_AL_OF_UNIT_DAY_BFR_GRANT);
			}
		}
		return annualLeaveErrors;
	}

	//期間終了退避処理
	public void periodEndSaveProcess(String cid, String sid, AggregatePeriodWork aggregatePeriodWork,
			AggrResultOfAnnualLeave aggResult) {

		//○期間終了日時点の年休情報を付与消滅前に退避するかチェック
		if(aggregatePeriodWork.getEndWork().isPeriodEndAtr()) {
			//○「年休の集計結果．年休情報(期間終了日時点)」←処理中の「年休情報」
			aggResult.setAsOfPeriodEnd(this);
		}

		//○期間終了日翌日時点の期間かチェック
		if(aggregatePeriodWork.getEndWork().isNextPeriodEndAtr()) {
		//○「年休の集計結果．年休情報(期間終了日の翌日開始時点)」←処理中の「年休情報」
			aggResult.setAsOfStartNextDayOfPeriodEnd(this);
		}

		return;
	}

	//終了時点更新処理
	public void updateProcessEnd(String cid, String sid, AggregatePeriodWork aggregatePeriodWork,
			AggrResultOfAnnualLeave aggResult, List<AnnualLeaveError> lstError) {

		//○期間終了日翌日時点の期間かチェック
		if(aggregatePeriodWork.getEndWork().isNextPeriodEndAtr())
		 return;

		//○「年休の集計結果．年休エラー情報」に受け取った年休エラーを全て追加
		aggResult.getAnnualLeaveErrors().addAll(lstError);
		aggResult.setAnnualLeaveErrors(aggResult.getAnnualLeaveErrors().stream().distinct().collect(Collectors.toList()));

		//○年休情報．年月日を終了日に更新
		aggResult.getAsOfPeriodEnd().setYmd(aggregatePeriodWork.getPeriod().end());

	}

	/**
	 * マイナス分の年休付与残数を1レコードにまとめる
	 * @return 年休付与残数データ
	 */
	public Optional<AnnualLeaveGrantRemainingData>
			createLeaveGrantRemainingShortageData(){

		// 残数不足（ダミー）として作成した「年休付与残数(List)」を取得
		List<AnnualLeaveGrantRemainingData> remainingList
			= this.getGrantRemainingDataList();
		List<AnnualLeaveGrantRemainingData> dummyRemainingList
			= remainingList.stream()
				.filter(c -> c.isShortageRemain())
				.collect(Collectors.toList());

		if ( dummyRemainingList.size()==0 ) {
			return Optional.empty();
		}

		// 取得した年休付与残数の「年休使用数」、「年休残数」をそれぞれ合計
		LeaveRemainingNumber leaveRemainingNumberTotal = new LeaveRemainingNumber();
		LeaveUsedNumber leaveUsedNumberTotal = new LeaveUsedNumber();
		dummyRemainingList.forEach(c->{
			leaveRemainingNumberTotal.add(c.getDetails().getRemainingNumber());
			leaveUsedNumberTotal.add(c.getDetails().getUsedNumber());
		});

		// 合計した「年休使用数」「年休残数」から年休付与残数を作成
		AnnualLeaveGrantRemainingData annualLeaveGrantRemainingData
			= new AnnualLeaveGrantRemainingData();
		LeaveNumberInfo leaveNumberInfo = new LeaveNumberInfo();
		// 明細．残数　←　合計した「年休残数」
		leaveNumberInfo.setRemainingNumber(leaveRemainingNumberTotal);
		// 明細．使用数　←　合計した「年休使用数」
		leaveNumberInfo.setUsedNumber(leaveUsedNumberTotal);
		annualLeaveGrantRemainingData.setDetails(leaveNumberInfo);

		return Optional.of(annualLeaveGrantRemainingData);
	}

	/**
	 * 付与残数データから年休不足分の年休付与残数を削除
	 */
	public void deleteDummy(){
		//　年休付与残数が残数不足の年休付与残数をListから削除
		List<AnnualLeaveGrantRemainingData> noDummyList
			= this.getGrantRemainingDataList().stream()
				.filter(c->!c.isShortageRemain())
				.collect(Collectors.toList());
		this.setGrantRemainingDataList(noDummyList);
	}

}


