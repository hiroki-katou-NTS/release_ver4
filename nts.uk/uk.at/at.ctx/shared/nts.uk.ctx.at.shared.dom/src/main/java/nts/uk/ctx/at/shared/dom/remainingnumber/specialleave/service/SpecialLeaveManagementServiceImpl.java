package nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.gul.util.value.Finally;
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
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.grantnumber.TimeOfGrant;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.DayNumberOfRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.SpecialLeaveRemainingNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.TimeOfRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.DayNumberOfUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.DayNumberOver;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.SpecialLeaveOverNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.SpecialLeaveUsedNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.TimeOfUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.TimeOver;
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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public InPeriodOfSpecialLeaveResultInfor complileInPeriodOfSpecialLeave(ComplileInPeriodOfSpecialLeaveParam param) {
		InPeriodOfSpecialLeaveResultInfor outputData = new InPeriodOfSpecialLeaveResultInfor();
		//ドメインモデル「特別休暇基本情報」を取得する
		Optional<SpecialLeaveBasicInfo> optBasicInfor = leaveBasicInfoRepo.getBySidLeaveCdUser(param.getSid(), param.getSpecialLeaveCode(), UseAtr.USE);
		if(!optBasicInfor.isPresent()
				|| optBasicInfor.get().getUsed() == UseAtr.NOT_USE) {
			RemainDaysOfSpecialHoliday remainDays = new RemainDaysOfSpecialHoliday(new SpecialHolidayRemainInfor(0, 0, 0), 0, Optional.empty(), new ArrayList<>());
			InPeriodOfSpecialLeave specialLeaveInfor = new InPeriodOfSpecialLeave(new ArrayList<>(), remainDays, new ArrayList<>(), new ArrayList<>());
			return new InPeriodOfSpecialLeaveResultInfor(specialLeaveInfor, Finally.empty(), Finally.of(param.getComplileDate().end().addDays(1)));
		}
		//管理データを取得する
		ManagaData grantRemainData = this.getMngData(param.getCid(),
				param.getSid(),
				param.getSpecialLeaveCode(),
				param.getComplileDate(),
				param.getOptBeforeResult());		
		
		SpecialHolidayDataParam speDataParam = new SpecialHolidayDataParam(param.getCid(),
				param.getSid(),
				param.getComplileDate(),
				param.getSpecialLeaveCode(),
				param.isMode(),
				param.isOverwriteFlg(),
				param.getRemainData(),
				param.getInterimSpecialData());
		//特別休暇暫定データを取得する
		SpecialHolidayInterimMngData specialHolidayInterimDataMng = this.specialHolidayData(speDataParam);
		
		SpecialLeaveGrantRemainingDataTotal speRemainData = new SpecialLeaveGrantRemainingDataTotal(grantRemainData.getRemainDatas(),
				grantRemainData.lstGrantDataMemory,
				grantRemainData.lstGrantDatabase);
		//特休の使用数を求める
		RemainDaysOfSpecialHoliday useInfor = this.getUseDays(param.getCid(),
				param.getSid(),
				param.getComplileDate(),
				speRemainData, 
				specialHolidayInterimDataMng.getLstSpecialInterimMng(),
				specialHolidayInterimDataMng.getLstInterimMng());
		
		InPeriodOfSpecialLeave getOffsetDay = getInforCurrentProcess(param, grantRemainData, specialHolidayInterimDataMng, speRemainData,
				useInfor);
		//特別休暇のエラーチェックをする
		List<SpecialLeaveError> lstError = this.lstError(getOffsetDay);
		//「特別休暇の集計結果情報」に集計結果を格納する
		getOffsetDay.setLstError(lstError);
		outputData.setAggSpecialLeaveResult(getOffsetDay);
		InPeriodOfSpecialLeave speTmp = toInPeriodOfSpecialLeave(getOffsetDay);
		speTmp = getInforNextProcess(param, grantRemainData, getOffsetDay, speTmp);
		
		outputData.setAggSpecialLeaveNextResult(Finally.of(speTmp));
		//翌月管理データ取得区分をチェックする
		if(param.isMngAtr()) {
			outputData.setAggSpecialLeaveResult(speTmp);
		}
		//次の集計期間の開始日を計算する
		outputData.setNextDate(Finally.of(param.getComplileDate().end().addDays(1)));
		return outputData;
	}
	private InPeriodOfSpecialLeave getInforCurrentProcess(ComplileInPeriodOfSpecialLeaveParam param,
			ManagaData grantRemainData, SpecialHolidayInterimMngData specialHolidayInterimDataMng,
			SpecialLeaveGrantRemainingDataTotal speRemainData, RemainDaysOfSpecialHoliday useInfor) {
		InPeriodOfSpecialLeave getOffsetDay;
		//取得した特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(grantRemainData.lstGrantDataMemory.isEmpty()) {
			//管理データと暫定データの相殺
			getOffsetDay = this.getOffsetDay1004(param.getCid(), param.getSid(), param.getComplileDate(), param.getBaseDate(),
					param.getSpecialLeaveCode(), speRemainData, specialHolidayInterimDataMng, 
					grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0, useInfor, param.isMode());
			//付与前の残数情報をまとめる
			SpecialHolidayRemainInfor grantDetailBefore = this.grantDetailBefore(getOffsetDay.getLstSpeLeaveGrantDetails(), getOffsetDay.getRemainDays().getGrantDetailBefore());
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
			useInfor = this.remainDaysBefore(param.getCid(), param.getSid(), shuukeiDate, speRemainData, interimBefore, useInfor, param.getBaseDate(), param.isMode());
			
			//管理データと暫定データの相殺
			getOffsetDay = this.getOffsetDay1004(param.getCid(), param.getSid(), param.getComplileDate(), param.getBaseDate(),
					param.getSpecialLeaveCode(), speRemainData, specialHolidayInterimDataMng, 
					grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0, useInfor, param.isMode());			
			SpecialHolidayRemainInfor grantDetailAfter = getOffsetDay.getRemainDays().getGrantDetailAfter().isPresent() ? getOffsetDay.getRemainDays().getGrantDetailAfter().get() :
				new SpecialHolidayRemainInfor(0, 0, 0);
			//付与後の残数情報をまとめる
			getOffsetDay = this.grantDetailAfter(grantDetailAfter, getOffsetDay);
			getOffsetDay.getUseOutPeriod().addAll(useInfor.getUseDaysOutPeriod());			
		}
		return getOffsetDay;
	}
	private InPeriodOfSpecialLeave getInforNextProcess(ComplileInPeriodOfSpecialLeaveParam param,
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
			expiredData = this.unDigestedDay(lstRemainTmp, param.getComplileDate().end().addDays(1), param.isMode());
		}	
		//「特別休暇の残数」．未消化数 += 未消化数(output)
		speTmp.getRemainDays().setUnDisgesteDays(speTmp.getRemainDays().getUnDisgesteDays() + expiredData.getUnDigestedDay());
		//集計終了日の翌日開始時点の残数情報をまとめる
		speTmp = this.speRemainNextInfor(param.getCid(),
				param.getSid(),
				param.getSpecialLeaveCode(),
				param.getComplileDate().end().addDays(1),
				expiredData.getLstGrantData(),
				speTmp,
				grantRemainData.limitDays.isPresent() ? grantRemainData.limitDays.get() : 0);
		return speTmp;
	}
	private InPeriodOfSpecialLeave toInPeriodOfSpecialLeave(InPeriodOfSpecialLeave getOffsetDay) {
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
	private List<SpecialLeaveError> lstError(InPeriodOfSpecialLeave output){
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
	
	private InPeriodOfSpecialLeave speRemainNextInfor(String cid, String sid, int speCode, GeneralDate baseDate,
			List<SpecialLeaveGrantRemainingData> lstSpeData, InPeriodOfSpecialLeave speTmp, double limitDays) {
		//[No.373]社員の特別休暇情報を取得する
		InforSpecialLeaveOfEmployee getSpecialHolidayOfEmp = inforSpeLeaveEmpService.getInforSpecialLeaveOfEmployee(cid,
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
		DataMngOfDeleteExpired adjustCarryForward = this.adjustCarryForward1005(lstSpeData, limitDays);
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
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ManagaData getMngData(String cid, String sid, int specialLeaveCode,
			DatePeriod complileDate, Optional<InPeriodOfSpecialLeaveResultInfor> beforeResult) {
		List<SpecialLeaveGrantRemainingData> lstDataBase = new ArrayList<>();
		//「INPUT．特別休暇の集計結果情報」をチェックする
		//INPUT．特別休暇の集計結果情報 = NULL || INPUT．特別休暇の集計結果情報.前回集計期間の翌日 ≠ INPUT．集計開始日
		if(!beforeResult.isPresent()
				|| (beforeResult.get().getNextDate().isPresent()
						&& !beforeResult.get().getNextDate().get().equals(complileDate.start()))) {
			//ドメインモデル「特別休暇付与残数データ」を取得する
			lstDataBase = speLeaveRepo.getByPeriodStatus(sid,
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
				List<SpecialLeaveGrantRemainingData> lstSpeData = speLeaveRepo.getByNextDate(sid, specialLeaveCode,
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
		Optional<SpecialHoliday> optSpecialHoliday = holidayRepo.findBySingleCD(cid, specialLeaveCode);
		if(!optSpecialHoliday.isPresent()) {
			return new ManagaData(new ArrayList<>(), Optional.empty(), new ArrayList<>(), new ArrayList<>());
		}
		SpecialHoliday specialHoliday = optSpecialHoliday.get();
		//社員の特別休暇情報を取得する
		DatePeriod compDateEmp = new DatePeriod(complileDate.start().addDays(1), complileDate.end());
		InforSpecialLeaveOfEmployee getSpecialHolidayOfEmp = inforSpeLeaveEmpService.getInforSpecialLeaveOfEmployee(cid, sid, specialLeaveCode, compDateEmp,specialHoliday);
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
			lstDataSpeDataBase = this.adjustGrantData(lstDataBase, lstDataSpeDataMemory);
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
	private DataMngOfDeleteExpired unDigestedDay(List<SpecialLeaveGrantRemainingData> lstGrantData, GeneralDate baseDate, boolean isMode) {
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
			SpecialHolidayInterimMngData interimDataMng, double accumulationMaxDays,RemainDaysOfSpecialHoliday useInfor, boolean isMode) {
		//未消化数=0(初期化)
		double undigested = 0;
		DataMngOfDeleteExpired expiredData = null;
		SubtractUseDaysFromMngDataOut subtractUseDays = null;
		
		//取得した特別休暇付与残数データ一覧に付与予定のデータがあるかチェックする
		if(lstGrantData.getLstGrantDataMemory().isEmpty()) {
			//使用数を管理データから引く speLeaveResult dung o dau
			subtractUseDays = this.subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDataTotal(), interimDataMng, useInfor);
			//期限切れの管理データを期限切れに変更する
			expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), baseDate, isMode);
			
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
			expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), endDate.after(baseDate) ? baseDate : endDate, isMode);
			
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
			expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), baseDate, isMode);
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
			Optional<SpecialLeaveGrantRemainingData> grantDataById = speLeaveRepo.getBySpecialId(x.getSpecialId());
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

	@Override
	public SubtractUseDaysFromMngDataOut subtractUseDaysFromMngData1004(List<SpecialLeaveGrantRemainingData> lstGrantData,
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
			RemainDaysOfSpecialHoliday useInfor, GeneralDate baseDate, boolean isMode) {
		//使用数を管理データから引く
		SubtractUseDaysFromMngDataOut subtractUseDays = this.subtractUseDaysFromMngData1004(lstGrantData.getLstGrantDatabase(), interimDataMng, useInfor);
		//期限切れの管理データを期限切れに変更する //NPUT．集計終了日、INPUT．基準日の古い日付
		DataMngOfDeleteExpired expiredData = this.unDigestedDay(subtractUseDays.getLstSpeRemainData(), 
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

	@Override
	public InPeriodOfSpecialLeave grantDetailAfter(SpecialHolidayRemainInfor grantDetailAfter, InPeriodOfSpecialLeave getOffsetDay) {
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
}
