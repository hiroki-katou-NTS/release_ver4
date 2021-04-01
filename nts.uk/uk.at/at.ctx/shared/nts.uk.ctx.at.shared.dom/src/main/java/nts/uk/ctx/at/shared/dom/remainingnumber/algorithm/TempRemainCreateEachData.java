package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber.AnnualLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.HolidayAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.ManagermentAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.DayoffTranferInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.DigestionHourlyTimeType;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.InforFormerRemainData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.OccurrenceUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.SpecialHolidayUseDetail;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnualLeaveUsedNumber;
import nts.uk.ctx.at.shared.dom.vacation.service.UseDateDeadlineFromDatePeriod;
import nts.uk.ctx.at.shared.dom.vacation.setting.ExpirationTime;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.SubstVacationSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

public class TempRemainCreateEachData {
	/**
	 * 残数作成元情報から暫定年休管理データを作成する
	 * @param inforData
	 * @param workTypeClass
	 * @param mngData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimAnnualHoliday(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		if(!occUseDetail.isPresent()) {
			return mngData;
		}
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		OccurrenceUseDetail useDetail = occUseDetail.get();
		String mngId = IdentifierUtil.randomUniqueId();
		TmpAnnualHolidayMng annualMng = new TmpAnnualHolidayMng(mngId, inforData.getSid(), inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(), 
				new DigestionHourlyTimeType(), 
				new WorkTypeCode(inforData.getWorkTypeRemainInfor(workTypeClass).get().getWorkTypeCode()), 
				AnnualLeaveUsedNumber.of(Optional.of(new AnnualLeaveUsedDayNumber(useDetail.getDays())), 
						Optional.empty()));
		recAbsData.add(annualMng);
		mngData.setRecAbsData(recAbsData);
		mngData.setAnnualHolidayData(Optional.of(annualMng));
		return mngData;
	}

	/**
	 * 残数作成元情報から暫定積立年休管理データを作成する
	 * @param inforData
	 * @param workTypeClass
	 * @param mngData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimReserveHoliday(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		if(!occUseDetail.isPresent()) {
			return mngData;
		}
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		String mngId = IdentifierUtil.randomUniqueId();
		InterimRemain ramainData = new InterimRemain(mngId, 
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(), 
				RemainType.FUNDINGANNUAL);
		recAbsData.add(ramainData);
		TmpResereLeaveMng resereData = new TmpResereLeaveMng(mngId, new UseDay(occUseDetail.get().getDays()));
		mngData.setResereData(Optional.of(resereData));
		mngData.setRecAbsData(recAbsData);
		return mngData;
	}

	/**
	 * 残数作成元情報から暫定振休管理データを作成する
	 * @param inforData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimAbsData(InforFormerRemainData inforData, 
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		if(occUseDetail.isPresent()) {
			String mngId = IdentifierUtil.randomUniqueId();
			InterimRemain mngDataRemain = new InterimRemain(mngId,
					inforData.getSid(),
					inforData.getYmd(),
					inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
					RemainType.PAUSE);
			InterimAbsMng absData = new InterimAbsMng(mngId,
					new RequiredDay(occUseDetail.get().getDays()),
					new UnOffsetDay(occUseDetail.get().getDays()));
			mngData.setInterimAbsData(Optional.of(absData));
			recAbsData.add(mngDataRemain);
			mngData.setRecAbsData(recAbsData);
		}
		return mngData;
	}

	/**
	 * 残数作成元情報から暫定代休管理データを作成する
	 * @param inforData
	 * @param workTypeClass
	 * @param mngData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimDayOffData(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		if(occUseDetail.isPresent()) {
			if(!inforData.isDayOffTimeIsUse()) {
				String mngId = IdentifierUtil.randomUniqueId();
				InterimRemain mngDataRemain = new InterimRemain(mngId, inforData.getSid(), inforData.getYmd(), 
						inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(), RemainType.SUBHOLIDAY);
				InterimDayOffMng dayoffMng = new InterimDayOffMng(mngId, 
						new RequiredTime(0),
						new RequiredDay(occUseDetail.get().getDays()),
						new UnOffsetTime(0), 
						new UnOffsetDay(occUseDetail.get().getDays()));
				mngData.setDayOffData(Optional.of(dayoffMng));
				recAbsData.add(mngDataRemain);
			} else {
				//TODO 2018.06.20 chua lam trong giai doan nay
			}
			mngData.setRecAbsData(recAbsData);
		}
		return mngData;
	}
	
	/**
	 * 残数作成元情報から暫定振出管理データを作成する
	 * @param inforData
	 * @param workTypeClass
	 * @param mngData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimRecData(RequireM1 require, InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		// 残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		if(!occUseDetail.isPresent()) {
			return mngData;
		}
		//アルゴリズム「振休使用期限日の算出」を実行する
		GeneralDate useDate = getUseDays(require, inforData);
		String mngId = IdentifierUtil.randomUniqueId();
		InterimRemain remainMng = new InterimRemain(mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(), 
				RemainType.PICKINGUP);
		List<OccurrenceUseDetail> occurrenceDetailData =  inforData.getWorkTypeRemainInfor(workTypeClass).get().getOccurrenceDetailData()
				.stream().filter(x -> x.getWorkTypeAtr() == workTypeClass)
				.collect(Collectors.toList());
		
		InterimRecMng recMng = new InterimRecMng(mngId,
				useDate,
				new OccurrenceDay(occurrenceDetailData.isEmpty() ? 0 : occurrenceDetailData.get(0).getDays()),
				HolidayAtr.NON_STATUTORYHOLIDAY,
				new UnUsedDay(occurrenceDetailData.isEmpty() ? 0 : occurrenceDetailData.get(0).getDays()));
		mngData.setRecData(Optional.of(recMng));
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		recAbsData.add(remainMng);
		mngData.setRecAbsData(recAbsData);
		return mngData;
	}
	
	/**
	 * 残数作成元情報から暫定休出管理データを作成する
	 * @param inforData
	 * @param workTypeClass
	 * @param mngData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimBreak(
			RequireM1 require, InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		Integer tranferTime = 0;
		double tranferDay = 0;
		//代休振替情報のアルゴリズム「振替時間情報を取得する」を実行する
		for (DayoffTranferInfor x : inforData.getDayOffTranfer()) {
			tranferTime += x.getTranferTimeInfor().getTranferTime();
			if(x.getTranferTimeInfor().getDays().isPresent()) {
				tranferDay += x.getTranferTimeInfor().getDays().get();
			}
		}
		//代休振替情報をチェックする
		if(!inforData.getWorkTypeRemainInfor(workTypeClass).isPresent()
				|| (tranferTime == 0 && (tranferDay == 0))) {
			return mngData;
		}
		//代休使用期限日を取得する
		GeneralDate useDate = getDayDaikyu(require, inforData);
		String mngId = IdentifierUtil.randomUniqueId();
		InterimRemain recAbsData = new InterimRemain(mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				RemainType.BREAK);
		List<InterimRemain> lstRecAbsData = new ArrayList<>(mngData.getRecAbsData());
		lstRecAbsData.add(recAbsData);
		
		//時間代休を利用するかチェックする		
		if(inforData.isDayOffTimeIsUse()) {
			if(tranferTime == 0) {
				return mngData;
			}
			//振替時間をチェックする
			InterimBreakMng breakMng = new InterimBreakMng(mngId,
					new AttendanceTime(0),
					useDate,
					new OccurrenceTime(tranferTime),
					new OccurrenceDay(0.0),
					new AttendanceTime(0),
					new UnUsedTime(tranferTime), 
					new UnUsedDay(0.0));
			mngData.setBreakData(Optional.of(breakMng));
		} else {
			if(tranferDay == 0) {
				return mngData;
			}
			InterimBreakMng breakMng = new InterimBreakMng(mngId,
					new AttendanceTime(0),
					useDate,
					new OccurrenceTime(0),
					new OccurrenceDay(tranferDay),
					new AttendanceTime(0),
					new UnUsedTime(0), 
					new UnUsedDay(tranferDay));
			mngData.setBreakData(Optional.of(breakMng));
		}
		mngData.setRecAbsData(lstRecAbsData);
		return mngData;
	}

	/**
	 * 残数作成元情報から暫定特別休暇管理データを作成する
	 * @param inforData
	 * @param workTypeClass
	 * @param mngData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimSpecialHoliday(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		List<InterimSpecialHolidayMng> specialHolidayData = new ArrayList<>(mngData.getSpecialHolidayData()); 
		if(inforData.getWorkTypeRemainInfor(workTypeClass).get().getSpeHolidayDetailData().isEmpty()) {
			return mngData;
		}
		String mngId = IdentifierUtil.randomUniqueId();
		InterimRemain recAbsData = new InterimRemain(mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				RemainType.SPECIAL);
		List<InterimRemain> lstRecAbsData = new ArrayList<>(mngData.getRecAbsData());
		lstRecAbsData.add(recAbsData);
		mngData.setRecAbsData(lstRecAbsData);
		for (SpecialHolidayUseDetail speHolidayDetail : inforData.getWorkTypeRemainInfor(workTypeClass).get().getSpeHolidayDetailData()) {
			InterimSpecialHolidayMng holidayMng = new InterimSpecialHolidayMng();
			holidayMng.setSpecialHolidayId(mngId);
			holidayMng.setSpecialHolidayCode(speHolidayDetail.getSpecialHolidayCode());
			holidayMng.setMngAtr(ManagermentAtr.DAYS);
			holidayMng.setUseDays(Optional.of(new UseDay(speHolidayDetail.getDays())));
			holidayMng.setUseTimes(Optional.of(new UseTime(0)));
			specialHolidayData.add(holidayMng);
		}
		mngData.setSpecialHolidayData(specialHolidayData);
		return mngData;
	}
	

	/**
	 * 振休使用期限日を取得する, 
	 * @return
	 */
	private static GeneralDate getUseDays(RequireM1 require, InforFormerRemainData inforData) {
		//雇用別休暇管理設定の振休をチェックする
		EmploymentHolidayMngSetting employmentHolidaySetting = inforData.getEmploymentHolidaySetting();
		SubstVacationSetting subSetting = null;
		if(employmentHolidaySetting != null
				&& employmentHolidaySetting.getAbsSetting().isPresent()) {
		//	subSetting = employmentHolidaySetting.getAbsSetting().get().getSetting();
			
		} else {
			Optional<ComSubstVacation> companyHolidaySettingOpt = inforData.getCompanyHolidaySetting().getAbsSetting();
			if(companyHolidaySettingOpt.isPresent()) {
				subSetting = companyHolidaySettingOpt.get().getSetting();
			}
		}
		if (subSetting == null) {
			return GeneralDate.max();
		} 
		
		return commonDate(require, subSetting.getExpirationDate(), inforData.getEmploymentHolidaySetting().getEmploymentCode(), inforData.getYmd());
	}
	
	/**
	 * 代休使用期限日を取得する
	 * @param inforData
	 * @return
	 */
	private static GeneralDate getDayDaikyu(RequireM1 require, InforFormerRemainData inforData) {
		//雇用別休暇管理設定の振休をチェックする
		EmploymentHolidayMngSetting employmentHolidaySetting = inforData.getEmploymentHolidaySetting();
		ExpirationTime expriTime = ExpirationTime.UNLIMITED;
		if(employmentHolidaySetting != null 
				&& employmentHolidaySetting.getDayOffSetting() != null) {
		/*	expriTime = employmentHolidaySetting.getDayOffSetting().getCompensatoryAcquisitionUse().getExpirationTime();*/
		} else {
			expriTime = inforData.getCompanyHolidaySetting().getDayOffSetting().getCompensatoryAcquisitionUse().getExpirationTime();
		}
		
		return commonDate(require, expriTime, inforData.getEmploymentHolidaySetting().getEmploymentCode(), inforData.getYmd());
	}
	
	private static GeneralDate commonDate(RequireM1 require, ExpirationTime expriTime, String employmentCode, GeneralDate dateInfor) {
		//アルゴリズム「休暇使用期限から使用期限日を算出する」を実行する
		if(expriTime == ExpirationTime.END_OF_YEAR) {
			//TODO 
		} else if (expriTime == ExpirationTime.UNLIMITED) {
			return GeneralDate.max();
		} else {
			//期限指定のある使用期限日を作成する
			if(expriTime != null) {
				return UseDateDeadlineFromDatePeriod.useDateDeadline(require, employmentCode, expriTime, dateInfor);
			}
		}
		return GeneralDate.max();
	}

	
	public static interface RequireM1 extends UseDateDeadlineFromDatePeriod.RequireM1 {
		
	}
			
}
