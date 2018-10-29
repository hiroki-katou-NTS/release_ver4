package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.SpecialVacationCD;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveNumberInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.grantnumber.DayNumberOfGrant;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.grantnumber.SpecialLeaveGrantNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.DayNumberOfRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.SpecialLeaveRemainingNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.DayNumberOfUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.SpecialLeaveUsedNumber;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.TimeLimitSpecification;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class SpecialLeaveManagementServiceImpl implements SpecialLeaveManagementService{
	@Inject
	private SpecialLeaveGrantRepository speLeaveRepo;
	@Inject
	private InforSpecialLeaveOfEmployeeSevice inforSpeLeaveEmpService;
	@Inject
	private InterimRemainRepository interimMngRepo;
	@Inject
	private InterimSpecialHolidayMngRepository interimSpecialRepos;
	@Inject
	private SpecialHolidayRepository holidayRepo;
	@Inject
	private SpecialLeaveBasicInfoRepository leaveBasicInfoRepo;

	@Override
	public InPeriodOfSpecialLeave complileInPeriodOfSpecialLeave(ComplileInPeriodOfSpecialLeaveParam param) {
		//ドメインモデル「特別休暇基本情報」を取得する
		Optional<SpecialLeaveBasicInfo> optBasicInfor = leaveBasicInfoRepo.getBySidLeaveCdUser(param.getSid(), param.getSpecialLeaveCode(), UseAtr.USE);
		if(!optBasicInfor.isPresent()) {
			RemainDaysOfSpecialHoliday remainDays = new RemainDaysOfSpecialHoliday(new SpecialHolidayRemainInfor(0, 0, 0), 0, Optional.empty(), new ArrayList<>());
			return new InPeriodOfSpecialLeave(Collections.emptyList(), remainDays, Collections.emptyList(), Collections.emptyList());
		}
		//管理データを取得する
		ManagaData grantRemainData = this.getMngData(param.getCid(),
				param.getSid(),
				param.getSpecialLeaveCode(),
				param.getComplileDate());		
		//特別休暇暫定データを取得する
		SpecialHolidayDataParam speDataParam = new SpecialHolidayDataParam(param.getCid(),
				param.getSid(),
				param.getComplileDate(),
				param.getSpecialLeaveCode(),
				param.isMode(),
				param.isOverwriteFlg(),
				param.getRemainData(),
				param.getInterimSpecialData());
		SpecialHolidayInterimMngData specialHolidayInterimDataMng = this.specialHolidayData(speDataParam);
		List<SpecialLeaveGrantRemainingData> lstGrantDataMemory = new ArrayList<>();//付与予定
		List<SpecialLeaveGrantRemainingData> lstGrantDatabase = new ArrayList<>();//付与済
		for (SpecialLeaveGrantRemainingData grantData : grantRemainData.getRemainDatas()) {
			Optional<SpecialLeaveGrantRemainingData> grantDataById = speLeaveRepo.getBySpecialId(grantData.getSpecialId());
			if(grantDataById.isPresent()) {
				//DB上取得した「特別休暇付与残数データ」は付与済
				lstGrantDatabase.add(grantData);
			} else {
				//メモリ上の「特別休暇付与残数データ」は付与予定	
				lstGrantDataMemory.add(grantData);
			}
		}
		SpecialLeaveGrantRemainingDataTotal speRemainData = new SpecialLeaveGrantRemainingDataTotal(grantRemainData.getRemainDatas(), lstGrantDataMemory, lstGrantDatabase);
		//特休の使用数を求める
		RemainDaysOfSpecialHoliday useInfor = this.getUseDays(param.getCid(), param.getSid(), param.getComplileDate(), speRemainData, 
				specialHolidayInterimDataMng.getLstSpecialInterimMng(), specialHolidayInterimDataMng.getLstInterimMng());
		
		InPeriodOfSpecialLeave getOffsetDay = null;
		//取得した特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(lstGrantDataMemory.isEmpty()) {
			//管理データと暫定データの相殺
			getOffsetDay = this.getOffsetDay1004(param.getCid(), param.getSid(), param.getComplileDate(), param.getBaseDate(),
					param.getSpecialLeaveCode(), speRemainData, specialHolidayInterimDataMng, 
					grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0, useInfor);
			//付与前の残数情報をまとめる
			SpecialHolidayRemainInfor grantDetailBefore = this.grantDetailBefore(getOffsetDay.getLstSpeLeaveGrantDetails(), getOffsetDay.getRemainDays().getGrantDetailBefore());
			getOffsetDay.getRemainDays().setGrantDetailBefore(grantDetailBefore);
		} else {
			//付与前用の集計終了日= (先頭の付与予定の「特別休暇付与残数データ」．付与日).AddDays(-1)
			GeneralDate endDate = lstGrantDataMemory.get(0).getGrantDate().addDays(-1);
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
			useInfor = this.remainDaysBefore(param.getCid(), param.getSid(), shuukeiDate, speRemainData, interimBefore, useInfor, param.getBaseDate());
			
			//管理データと暫定データの相殺
			getOffsetDay = this.getOffsetDay1004(param.getCid(), param.getSid(), param.getComplileDate(), param.getBaseDate(),
					param.getSpecialLeaveCode(), speRemainData, specialHolidayInterimDataMng, 
					grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0, useInfor);			
			SpecialHolidayRemainInfor grantDetailAfter = getOffsetDay.getRemainDays().getGrantDetailAfter().isPresent() ? getOffsetDay.getRemainDays().getGrantDetailAfter().get() :
				new SpecialHolidayRemainInfor(0, 0, 0);
			//付与後の残数情報をまとめる
			grantDetailAfter = this.grantDetailAfter(getOffsetDay.getLstSpeLeaveGrantDetails(), grantDetailAfter);
			getOffsetDay.getRemainDays().setGrantDetailAfter(Optional.of(grantDetailAfter));
			getOffsetDay.setUseOutPeriod(useInfor.getUseDaysOutPeriod());
		}
		if(param.isMngAtr()) {
			//社員の特別休暇情報を取得する
			InforSpecialLeaveOfEmployee getSpecialHolidayOfEmp = inforSpeLeaveEmpService.getInforSpecialLeaveOfEmployee(param.getCid(),
					param.getSid(),
					param.getSpecialLeaveCode(),
					new DatePeriod(param.getComplileDate().end().addDays(1),
							param.getComplileDate().end().addDays(1)), null);
			if(getSpecialHolidayOfEmp.getStatus() != InforStatus.GRANTED) {
				//取得した特別休暇の付与データを「特別休暇の付与明細」に１行を追加する
				List<SpecialHolidayInfor> lstInfor = getSpecialHolidayOfEmp.getSpeHolidayInfor();
				if(lstInfor.isEmpty()) {
					return  this.lstError(getOffsetDay);
				}
				SpecialHolidayInfor speInfor = lstInfor.get(0);
				SpecialLeaveNumberInfoService numberInfor = new SpecialLeaveNumberInfoService(speInfor.getGrantDaysInfor().getGrantDays(),
						0,
						speInfor.getGrantDaysInfor().getGrantDays(),
						Optional.empty(),
						Optional.empty(),
						Optional.empty(),
						Optional.empty());
				SpecialLeaveGrantDetails detailAdd = new SpecialLeaveGrantDetails(param.getSpecialLeaveCode(),
						DataAtr.GRANTSCHE,
						LeaveExpirationStatus.AVAILABLE,
						speInfor.getDeadlineDate().isPresent() ? speInfor.getDeadlineDate().get() : GeneralDate.max(),
						param.getSid(),
						speInfor.getGrantDaysInfor().getYmd(),
						numberInfor);
				
				SpecialHolidayRemainInfor afterData = getOffsetDay.getRemainDays().getGrantDetailAfter().isPresent()
						? getOffsetDay.getRemainDays().getGrantDetailAfter().get() : null;
				if(afterData != null) {
					//「特別休暇の残数」．付与後明細．付与数 += 「特別休暇の利用情報」．付与数
					afterData.setGrantDays(afterData.getGrantDays() + numberInfor.getGrantDays());
					//「特別休暇の残数」．付与後明細．残数 += 「特別休暇の利用情報」．残数
					afterData.setGrantDays(afterData.getRemainDays() + numberInfor.getRemainDays());
					getOffsetDay.getRemainDays().setGrantDetailAfter(Optional.of(afterData));
					
				}
				//期限切れの管理データを期限切れに変更する
				DataMngOfDeleteExpired expiredData = this.unDigestedDay(grantRemainData.getRemainDatas(), param.getBaseDate());
				//「特別休暇の残数」．未消化数 += 未消化数(output)
				getOffsetDay.getRemainDays().setUnDisgesteDays(getOffsetDay.getRemainDays().getUnDisgesteDays() + expiredData.getUnDigestedDay());
				//繰越上限日数まで調整する
				DataMngOfDeleteExpired adjustCarryForward = this.adjustCarryForward1005(expiredData.getLstGrantData(), 
						grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0);
				//「特別休暇の残数」．未消化数 += 未消化数(output)
				getOffsetDay.getRemainDays().setUnDisgesteDays(getOffsetDay.getRemainDays().getUnDisgesteDays() + adjustCarryForward.getUnDigestedDay());
				
				getOffsetDay.getLstSpeLeaveGrantDetails().add(detailAdd);
			}
			
		}
		
		return this.lstError(getOffsetDay);
	}
	private InPeriodOfSpecialLeave lstError(InPeriodOfSpecialLeave output){
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
		output.setLstError(lstError);
		return output;
	}
	
	@Override
	public ManagaData getMngData(String cid, String sid, int specialLeaveCode,
			DatePeriod complileDate) {
		
		//ドメインモデル「特別休暇付与残数データ」を取得する
		List<SpecialLeaveGrantRemainingData> lstDataBase = speLeaveRepo.getByPeriodStatus(sid, specialLeaveCode, LeaveExpirationStatus.AVAILABLE,
				complileDate.start());
		List<SpecialLeaveGrantRemainingData> lstDataSpeDataBase = new ArrayList<>();
		if(!lstDataBase.isEmpty()) {
			for (SpecialLeaveGrantRemainingData c : lstDataBase) {
				SpecialLeaveGrantNumber a = new SpecialLeaveGrantNumber(c.getDetails().getGrantNumber().getDayNumberOfGrant(), c.getDetails().getGrantNumber().getTimeOfGrant());
				SpecialLeaveUsedNumber b = new SpecialLeaveUsedNumber(c.getDetails().getUsedNumber().getDayNumberOfUse(), c.getDetails().getUsedNumber().getTimeOfUse(),c.getDetails().getUsedNumber().getUseSavingDays(), c.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber());
				SpecialLeaveRemainingNumber d = new SpecialLeaveRemainingNumber(c.getDetails().getRemainingNumber().getDayNumberOfRemain(), c.getDetails().getRemainingNumber().getTimeOfRemain());
				SpecialLeaveGrantRemainingData tmp = new SpecialLeaveGrantRemainingData(c.getSpecialId(),
					c.getCId(),
					c.getEmployeeId(),
					c.getSpecialLeaveCode(),
					c.getGrantDate(),
					c.getDeadlineDate(),
					c.getExpirationStatus(),
					c.getRegisterType(),
					new SpecialLeaveNumberInfo(a, b, d));
				lstDataSpeDataBase.add(tmp);
			}	
		}
		
		//ドメインモデル「特別休暇」を取得する
		Optional<SpecialHoliday> optSpecialHoliday = holidayRepo.findBySingleCD(cid, specialLeaveCode);
		if(!optSpecialHoliday.isPresent()) {
			return new ManagaData(Collections.emptyList(), Optional.empty());
		}
		SpecialHoliday specialHoliday = optSpecialHoliday.get();
		//社員の特別休暇情報を取得する
		InforSpecialLeaveOfEmployee getSpecialHolidayOfEmp = inforSpeLeaveEmpService.getInforSpecialLeaveOfEmployee(cid, sid, specialLeaveCode, complileDate,specialHoliday);
		if(getSpecialHolidayOfEmp.getStatus() != InforStatus.NOTGRANT
				&& getSpecialHolidayOfEmp.getStatus() != InforStatus.NOTUSE
				&& getSpecialHolidayOfEmp.getStatus() != InforStatus.OUTOFSERVICE) {
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
			lstDataSpeDataBase = this.adjustGrantData(lstDataBase, lstDataSpeDataMemory);
			//ドメインモデル「期限情報」．期限指定方法をチェックする
			if(specialHoliday.getGrantPeriodic().getTimeSpecifyMethod() == TimeLimitSpecification.AVAILABLE_UNTIL_NEXT_GRANT_DATE
					&& !lstDataSpeDataMemory.isEmpty()) {				
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
		ManagaData outputData = new ManagaData(lstDataSpeDataBase, getSpecialHolidayOfEmp.getUpLimiDays());
		return outputData;
	}
	/**
	 * 付与日が同じ管理データを排除する
	 * @param lstDatabase
	 * @param lstMemoryData
	 * @return
	 */
	private List<SpecialLeaveGrantRemainingData> adjustGrantData(List<SpecialLeaveGrantRemainingData> lstDatabase, 
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

	@Override
	public SpecialHolidayInterimMngData specialHolidayData(SpecialHolidayDataParam param) {
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
			List<InterimRemain> lstInterimMngTmp = interimMngRepo.getRemainBySidPriod(param.getSid(),
					param.getDateData(), RemainType.SPECIAL);
			lstInterimMngTmp.stream().forEach(a -> {
				List<InterimSpecialHolidayMng> lstSpecialData = interimSpecialRepos.findById(a.getRemainManaID())
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
				List<InterimRemain> interimMngChk = lstInterimMngTmpCreate.stream().filter(x -> x.getYmd().equals(interimRemain.getYmd()))
						.collect(Collectors.toList());
				List<InterimSpecialHolidayMng> speMngReplace = param.getInterimSpecialData().stream()
						.filter(y -> y.getSpecialHolidayId().equals(interimRemain.getRemainManaID()) && y.getSpecialHolidayCode() == param.getSpeCode())
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
	 * 指定期間の使用数を求める
	 * @param lstInterimData
	 * @param dateData
	 * @return
	 */
	private double useDayFormGrant(List<InterimSpecialHolidayMng> lstInterimData, List<InterimRemain> lstInterimMng, DatePeriod dateData) {
		double outputData = 0;
		for (InterimSpecialHolidayMng interimMng : lstInterimData) {
			List<InterimRemain> optInterimMng = lstInterimMng.stream().filter(z -> z.getRemainManaID().equals(interimMng.getSpecialHolidayId()))
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
	private DataMngOfDeleteExpired unDigestedDay(List<SpecialLeaveGrantRemainingData> lstGrantData, GeneralDate baseDate) {
		double unDisgesteDays = 0;
		List<SpecialLeaveGrantRemainingData> lstTmp = new ArrayList<>(lstGrantData);
		for (SpecialLeaveGrantRemainingData grantData : lstGrantData) {
			//期限切れかチェックする
			if(!grantData.getDeadlineDate().afterOrEquals(baseDate)) {
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
	

	@Override
	public RemainDaysOfSpecialHoliday getUseDays(String cid, String sid, DatePeriod complileDate,
			SpecialLeaveGrantRemainingDataTotal speRemainData, List<InterimSpecialHolidayMng> interimSpeDatas, List<InterimRemain> lstInterimMng) {
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
			useDaysBefore += this.useDayFormGrant(interimSpeDatas, lstInterimMng, datePeriodBefore);
			//使用数付与後
			DatePeriod datePeriodAfter = new DatePeriod(speRemainData.getLstGrantDataMemory().get(0).getGrantDate(), complileDate.end());
			//指定期間の使用数を求める
			useDaysAfter += this.useDayFormGrant(interimSpeDatas, lstInterimMng, datePeriodAfter);
			//「特別休暇の残数」．付与前明細．使用数 = 使用数付与前
			grantDetailBefore.setUseDays(useDaysBefore);
			//「特別休暇の残数」．付与後明細．使用数 = 使用数付与後
			grantDetailAfter.setUseDays(useDaysAfter);
		}
		return new RemainDaysOfSpecialHoliday(grantDetailBefore, 0, 
				grantDetailAfter.getUseDays() == 0 ? Optional.empty() : Optional.of(grantDetailAfter), new ArrayList<>());
	}

	@Override
	public InPeriodOfSpecialLeave getOffsetDay1004(String cid, String sid, DatePeriod dateData, GeneralDate baseDate,
			int specialCode, SpecialLeaveGrantRemainingDataTotal lstGrantData,
			SpecialHolidayInterimMngData interimDataMng, double accumulationMaxDays,RemainDaysOfSpecialHoliday useInfor) {
		//未消化数=0(初期化)
		double undigested = 0;
		DataMngOfDeleteExpired expiredData = null;
		SubtractUseDaysFromMngDataOut subtractUseDays = null;
		
		//取得した特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(lstGrantData.getLstGrantDataMemory().isEmpty()) {
			//使用数を管理データから引く speLeaveResult dung o dau
			subtractUseDays = this.subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDataTotal(), interimDataMng, useInfor);
			//期限切れの管理データを期限切れに変更する
			expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), baseDate);
			
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
			subtractUseDays = this.subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDataTotal(), interimBefore, useInfor);
			//期限切れの管理データを期限切れに変更する //付与前用の集計終了日、INPUT．基準日の古い日付
			expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), endDate.after(baseDate) ? baseDate : endDate);
			
		}
		//未消化数+=未消化数
		undigested += expiredData.getUnDigestedDay();
		//繰越上限日数まで調整する
		DataMngOfDeleteExpired adjustCarryForward = this.adjustCarryForward1005(expiredData.getLstGrantData(), accumulationMaxDays);
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
			subtractUseDays = this.subtractUseDaysFromMngData1004(adjustCarryForward.getLstGrantData(), interimBefore, useInfor);
			//期限切れの管理データを期限切れに変更する
			expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), baseDate);
			//未消化数+=未消化数(output)
			undigested += expiredData.getUnDigestedDay();
		}
		//パラメータ．特別休暇の残数．未消化数=未消化数
		useInfor.setUnDisgesteDays(undigested);
		//特別休暇付与残数データ一覧を特別休暇パラメータに追加する
		List<SpecialLeaveGrantDetails> lstSpecialLeaveGrantDetails = new ArrayList<>();
		Map<GeneralDate, Double> limitDays = adjustCarryForward.getMapGrantDays();
		expiredData.getLstGrantData().forEach(x -> {
			SpecialLeaveGrantDetails grantDetail = new SpecialLeaveGrantDetails();
			grantDetail.setCode(x.getSpecialLeaveCode().v());
			Optional<SpecialLeaveGrantRemainingData> grantDataById = speLeaveRepo.getBySpecialId(x.getSpecialId());
			if(grantDataById.isPresent()) {
				grantDetail.setDataAtr(DataAtr.GRANTED);
			} else {
				grantDetail.setDataAtr(DataAtr.GRANTSCHE);
			}
			
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
			lstSpecialLeaveGrantDetails.add(grantDetail);
		});
		return new InPeriodOfSpecialLeave(lstSpecialLeaveGrantDetails, useInfor, subtractUseDays.getSpeLeaveResult().getUseOutPeriod(), Collections.emptyList());
	}

	@Override
	public SubtractUseDaysFromMngDataOut subtractUseDaysFromMngData1004(List<SpecialLeaveGrantRemainingData> lstGrantData,
			SpecialHolidayInterimMngData interimDataMng, RemainDaysOfSpecialHoliday useInfor) {
		InPeriodOfSpecialLeave output = new InPeriodOfSpecialLeave(Collections.emptyList(), useInfor, Collections.emptyList(), Collections.emptyList());
		List<UseDaysOfPeriodSpeHoliday> lstUseDaysOutOfPeriod = new ArrayList<>();
		List<SpecialLeaveGrantRemainingData> lstOuput = new ArrayList<>();
		for (SpecialLeaveGrantRemainingData c : lstGrantData) {
			SpecialLeaveGrantNumber a = new SpecialLeaveGrantNumber(c.getDetails().getGrantNumber().getDayNumberOfGrant(), c.getDetails().getGrantNumber().getTimeOfGrant());
			SpecialLeaveUsedNumber b = new SpecialLeaveUsedNumber(c.getDetails().getUsedNumber().getDayNumberOfUse(), c.getDetails().getUsedNumber().getTimeOfUse(),c.getDetails().getUsedNumber().getUseSavingDays(), c.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber());
			SpecialLeaveRemainingNumber d = new SpecialLeaveRemainingNumber(c.getDetails().getRemainingNumber().getDayNumberOfRemain(), c.getDetails().getRemainingNumber().getTimeOfRemain());
			SpecialLeaveGrantRemainingData tmp = new SpecialLeaveGrantRemainingData(c.getSpecialId(),
				c.getCId(),
				c.getEmployeeId(),
				c.getSpecialLeaveCode(),
				c.getGrantDate(),
				c.getDeadlineDate(),
				c.getExpirationStatus(),
				c.getRegisterType(),
				new SpecialLeaveNumberInfo(a, b, d));
			lstOuput.add(tmp);
		}
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
					.collect(Collectors.toList());
			if(tmpGrantRemainingData.isEmpty()) {
				//ループ中の「特別休暇暫定データ」を「特別休暇期間外の使用」に追加する
				UseDaysOfPeriodSpeHoliday useDaysOutPeriod = new UseDaysOfPeriodSpeHoliday(interimMng.getYmd(), 
						speInterimLoop.getUseDays().isPresent() ? Optional.of(speInterimLoop.getUseDays().get().v()) : Optional.empty(), 
								speInterimLoop.getUseTimes() != null && speInterimLoop.getUseTimes().isPresent() 
								? Optional.of(speInterimLoop.getUseTimes().get().v()) : Optional.empty());
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

	@Override
	public DataMngOfDeleteExpired adjustCarryForward1005(List<SpecialLeaveGrantRemainingData> lstGrantData,
			double accumulationMaxDays) {
		List<SpecialLeaveGrantRemainingData> lstGrantDataMemory = new ArrayList<>();//付与予定
		List<SpecialLeaveGrantRemainingData> lstGrantDatabase = new ArrayList<>();//付与済
		for (SpecialLeaveGrantRemainingData grantData : lstGrantData) {
			Optional<SpecialLeaveGrantRemainingData> grantDataById = speLeaveRepo.getBySpecialId(grantData.getSpecialId());
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
			double grantDaysOld = 0;
			//繰越上限を超えたかチェックする
			if(overCarryDays > 0) {
				List<SpecialLeaveGrantRemainingData> lstGrantDatabaseTmp = new ArrayList<>(lstGrantDatabase);
				for (SpecialLeaveGrantRemainingData speLeaverData : lstGrantDatabaseTmp) {
					//ドメインモデル「特別休暇付与残数データ」．期限切れ状態をチェックする
					if(speLeaverData.getExpirationStatus() == LeaveExpirationStatus.EXPIRED) {
						continue;
					}
					double remainLeaverDays = speLeaverData.getDetails().getRemainingNumber().getDayNumberOfRemain().v();
					//古い日付順から、付与済の「特別休暇付与残数データ」．「明細」．残数から繰越超えた値を引く
					grantDaysOld -= remainLeaverDays;
					//未消化数=繰越超えた値
					unDisgesteDays = overCarryDays;										
					//特別休暇の利用情報．上限超過消滅日数 += 繰越超えた値
					limitDays.put(speLeaverData.getGrantDate(), overCarryDays);					
					//特別休暇の利用情報 set 上限超過消滅日数．日数
					lstGrantDatabase.remove(speLeaverData);
					speLeaverData.getDetails().getRemainingNumber().setDayNumberOfRemain(new DayNumberOfRemain((double) grantDaysOld));
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

	@Override
	public SpecialHolidayRemainInfor grantDetailBefore(List<SpecialLeaveGrantDetails> lstSpeLeaveGrantDetails,
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

	@Override
	public RemainDaysOfSpecialHoliday remainDaysBefore(String cid, String sid, DatePeriod shukeiDate,
			SpecialLeaveGrantRemainingDataTotal lstGrantData, SpecialHolidayInterimMngData interimDataMng,
			RemainDaysOfSpecialHoliday useInfor, GeneralDate baseDate) {
		//使用数を管理データから引く
		SubtractUseDaysFromMngDataOut subtractUseDays = this.subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDatabase(), interimDataMng, useInfor);
		//期限切れの管理データを期限切れに変更する //NPUT．集計終了日、INPUT．基準日の古い日付
		DataMngOfDeleteExpired expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), 
				shukeiDate.end().after(baseDate) ? baseDate : shukeiDate.end());
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

	@Override
	public SpecialHolidayRemainInfor grantDetailAfter(List<SpecialLeaveGrantDetails> lstSpeLeaveGrantDetails,
			SpecialHolidayRemainInfor grantDetailAfter) {
		//付与後明細．残数と付与数=0（初期化）
		double grantDays = 0;
		double remainDays = 0;
		//パラメータ．特別休暇の付与明細をループする
		for (SpecialLeaveGrantDetails speDetail : lstSpeLeaveGrantDetails) {
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
		return grantDetailAfter;
	}
}
