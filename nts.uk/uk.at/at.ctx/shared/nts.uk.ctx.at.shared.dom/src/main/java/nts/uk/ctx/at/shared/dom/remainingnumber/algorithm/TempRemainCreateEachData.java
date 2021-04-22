package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyDto;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.holidaymanagement.interim.InterimHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TempAnnualLeaveMngs;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveUsedNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.holidayover60h.interim.TmpHolidayOver60hMng;
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
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.ChildCareNurseUsedNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.TempChildCareManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.ManagermentAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.DayNumberOfUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.usenumber.TimeOfUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CareUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.DayoffTranferInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.DigestionHourlyTimeType;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.InforFormerRemainData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.OccurrenceUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.SpecialHolidayUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.VacationTimeInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.VacationUsageTimeDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.WorkTypeRemainInfor;
import nts.uk.ctx.at.shared.dom.vacation.service.UseDateDeadlineFromDatePeriod;
import nts.uk.ctx.at.shared.dom.vacation.setting.ExpirationTime;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.SubstVacationSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;

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

		String mngId = IdentifierUtil.randomUniqueId();
		TempAnnualLeaveMngs annualMng = new TempAnnualLeaveMngs(
				mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				RemainType.ANNUAL,
				new WorkTypeCode(inforData.getWorkTypeRemainInfor(workTypeClass).get().getWorkTypeCode()),
				new LeaveUsedNumber(occUseDetail.get().getDays(), null, null),
				Optional.empty());
		mngData.getRecAbsData().add(annualMng);

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

		String mngId = IdentifierUtil.randomUniqueId();

		TmpResereLeaveMng resereData = new TmpResereLeaveMng(mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				RemainType.FUNDINGANNUAL, new UseDay(occUseDetail.get().getDays()));
		mngData.setResereData(Optional.of(resereData));
		mngData.getRecAbsData().add(resereData);
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
		if(occUseDetail.isPresent()) {
			String mngId = IdentifierUtil.randomUniqueId();
			
			WorkTypeRemainInfor Rinfor = inforData.getWorkTypeRemainInfor(workTypeClass).map(ri -> ri)
					.orElse(inforData.getWorkTypeRemainInforByOd(workTypeClass));
			
			InterimAbsMng absDataMng = new InterimAbsMng(mngId,inforData.getSid(),
					inforData.getYmd(),
					Rinfor.getCreateData(),
					RemainType.PAUSE,
					new RequiredDay(occUseDetail.get().getDays()),
					new UnOffsetDay(occUseDetail.get().getDays()));
			mngData.setInterimAbsData(Optional.of(absDataMng));
			mngData.getRecAbsData().add(absDataMng);
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

		if(occUseDetail.isPresent()) {
				String mngId = IdentifierUtil.randomUniqueId();

				InterimDayOffMng dayoffMng = new InterimDayOffMng(
						mngId,
						inforData.getSid(),
						inforData.getYmd(),
						inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
						RemainType.SUBHOLIDAY,
						new RequiredTime(occUseDetail.get().getSubstituteHolidayTime().map(x -> x.v()).orElse(0)),
						new RequiredDay(occUseDetail.get().getDays()),
						new UnOffsetTime(occUseDetail.get().getSubstituteHolidayTime().map(x -> x.v()).orElse(0)),
						new UnOffsetDay(occUseDetail.get().getDays()),
						Optional.ofNullable(DigestionHourlyTimeType.of(true, Optional.empty()))
						);
				mngData.setDayOffData(Optional.of(dayoffMng));
				mngData.getRecAbsData().add(dayoffMng);
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
		
		WorkTypeRemainInfor Rinfor = inforData.getWorkTypeRemainInfor(workTypeClass).map(ri -> ri)
				.orElse(inforData.getWorkTypeRemainInforByOd(workTypeClass));

		InterimRecMng recMng = new InterimRecMng(mngId,inforData.getSid(),
				inforData.getYmd(),
				Rinfor.getCreateData(),
				RemainType.PICKINGUP,
				useDate,
				new OccurrenceDay(occUseDetail.get().getDays()),
				new UnUsedDay(occUseDetail.get().getDays()));
		mngData.setRecData(Optional.of(recMng));
		mngData.getRecAbsData().add(recMng);
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

		InterimBreakMng breakMng = new InterimBreakMng();
		//時間代休を利用するかチェックする
		if(inforData.isDayOffTimeIsUse()) {
			if(tranferTime == 0) {
				return mngData;
			}
			//振替時間をチェックする
			breakMng = new InterimBreakMng(
					mngId,
					inforData.getSid(),
					inforData.getYmd(),
					inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
					RemainType.BREAK,
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
			breakMng = new InterimBreakMng(
					mngId,
					inforData.getSid(),
					inforData.getYmd(),
					inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
					RemainType.BREAK,
					new AttendanceTime(0),
					useDate,
					new OccurrenceTime(0),
					new OccurrenceDay(tranferDay),
					new AttendanceTime(0),
					new UnUsedTime(0),
					new UnUsedDay(tranferDay));
			mngData.setBreakData(Optional.of(breakMng));
		}
		mngData.getRecAbsData().add(breakMng);
		mngData.setRecAbsData(mngData.getRecAbsData());
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

		for (SpecialHolidayUseDetail speHolidayDetail : inforData.getWorkTypeRemainInfor(workTypeClass).get().getSpeHolidayDetailData()) {
			InterimSpecialHolidayMng holidayMng = new InterimSpecialHolidayMng(mngId,
					inforData.getSid(),
					inforData.getYmd(),
					inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
					RemainType.SPECIAL,
					speHolidayDetail.getSpecialHolidayCode(),
					ManagermentAtr.DAYS, Optional.of(new UseTime(0)),
					Optional.of(new UseDay(speHolidayDetail.getDays())),
					Optional.ofNullable(DigestionHourlyTimeType.of(true, Optional.empty()))
					);
			specialHolidayData.add(holidayMng);
		}
		if (!specialHolidayData.isEmpty()) {
			mngData.getRecAbsData().add(specialHolidayData.get(0));
		}
		mngData.setSpecialHolidayData(specialHolidayData);
		return mngData;
	}

	/**
	 * 残数作成元情報から時間消化休暇の暫定データを作成する
	 *
	 * @param inforData
	 * @param yearlyreserved
	 * @param outputData
	 * @return
	 */
		public static DailyInterimRemainMngData createInterimDigestVacation(InforFormerRemainData inforData,
				WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		// 残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetails = inforData.getOccurrenceUseDetail(workTypeClass);

		if (!occUseDetails.isPresent()) {
			return mngData;
		}

		// 暫定年休管理データを作成する

		occUseDetails.get().getVacationUsageTimeDetails().stream()
				.filter(x -> x.getHolidayType().equals(HolidayType.ANNUAL) && x.getTimes() > 0).findFirst().
				ifPresent(usageTimeDetail -> {
					TempAnnualLeaveMngs annualMng = createInterimAnnualHolidayFromDigestVacation(inforData,
							workTypeClass, usageTimeDetail);
					mngData.getRecAbsData().add(annualMng);
				});

		// 暫定代休管理データ作成する
		occUseDetails.get().getVacationUsageTimeDetails().stream()
				.filter(x -> x.getHolidayType().equals(HolidayType.SUBSTITUTE) && x.getTimes() > 0).findFirst()
				.ifPresent(usageTimeDetail -> {
					InterimDayOffMng dayOffMng = createInterimDayOffFromDigestVacation(inforData, workTypeClass,
							usageTimeDetail);
					mngData.getRecAbsData().add(dayOffMng);
				});

		// 暫定60H超休管理データを作成する
		occUseDetails.get().getVacationUsageTimeDetails().stream()
				.filter(x -> x.getHolidayType().equals(HolidayType.SIXTYHOUR) && x.getTimes() > 0).findFirst()
				.ifPresent(usageTimeDetail -> {
					if (usageTimeDetail.getTimes() > 0) {
						TmpHolidayOver60hMng holidayOver60hMng = createInterimHolidayOver60hMngFromDigestVacation(
								inforData, workTypeClass, usageTimeDetail);
						mngData.getRecAbsData().add(holidayOver60hMng);
					}
				});

		return mngData;
		}

	/**
	 * 時間消化休暇から暫定60H超休管理データを作成する
	 *
	 * @param inforData
	 * @param workTypeClass
	 * @param usageTimeDetail
	 * @return
	 */
	private static TmpHolidayOver60hMng createInterimHolidayOver60hMngFromDigestVacation(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, VacationUsageTimeDetail usageTimeDetail) {

		return new TmpHolidayOver60hMng(
				IdentifierUtil.randomUniqueId(),
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				RemainType.SIXTYHOUR,
				Optional.ofNullable(new UseTime(usageTimeDetail.getTimes())),
				Optional.ofNullable(DigestionHourlyTimeType.of(true, Optional.empty())));
	}

	/**
	 * 時間消化休暇から暫定代休管理データを作成する
	 *
	 * @param inforData
	 * @param workTypeClass
	 * @param usageTimeDetail
	 * @return
	 */
	private static InterimDayOffMng createInterimDayOffFromDigestVacation(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, VacationUsageTimeDetail usageTimeDetail) {

		Integer times = usageTimeDetail.getTimes();
		return new InterimDayOffMng(
				IdentifierUtil.randomUniqueId(),
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				RemainType.SUBHOLIDAY,
				new RequiredTime(times),
				new RequiredDay(0d) ,
				new UnOffsetTime(times),
				new UnOffsetDay(0d),
				Optional.ofNullable(DigestionHourlyTimeType.of(true, Optional.empty()))
				);
	}

	/**
	 * 時間消化休暇から暫定年休管理データを作成する
	 *
	 * @param usageTimeDetail
	 * @param workTypeClass
	 * @param inforData
	 * @return
	 */
	private static TempAnnualLeaveMngs createInterimAnnualHolidayFromDigestVacation(InforFormerRemainData inforData, WorkTypeClassification workTypeClass, VacationUsageTimeDetail usageTimeDetail) {

		WorkTypeRemainInfor WorkTypeRemainInfor = inforData.getWorkTypeRemainInfor(workTypeClass).map(x -> x)
				.orElse(null);

		return new TempAnnualLeaveMngs(IdentifierUtil.randomUniqueId(),
				inforData.getSid(),
				inforData.getYmd(),
				WorkTypeRemainInfor.getCreateData(),
				RemainType.ANNUAL,
				new WorkTypeCode(WorkTypeRemainInfor.getWorkTypeCode()),
				new LeaveUsedNumber(0d, usageTimeDetail.getTimes(), 0d),
				Optional.of(DigestionHourlyTimeType.of(true, Optional.empty())));

	}

	/**
	 * 残数作成元情報から暫定公休管理データを作成する
	 *
	 * @param inforData
	 * @param holiday
	 * @param outputData
	 * @return
	 */
	public static DailyInterimRemainMngData createInterimHolidayData(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		// 残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);

		occUseDetail.ifPresent(x -> {
			//発生使用明細＝設定あり
			
			WorkTypeRemainInfor Rinfor = inforData.getWorkTypeRemainInfor(workTypeClass).map(ri -> ri)
					.orElse(inforData.getWorkTypeRemainInforByOd(workTypeClass));
			
			InterimHolidayMng holidayMng = new InterimHolidayMng(IdentifierUtil.randomUniqueId(), inforData.getSid(),
					inforData.getYmd(), Rinfor.getCreateData(),
					RemainType.PUBLICHOLIDAY, x.getDays());
			mngData.getRecAbsData().add(holidayMng);
		});

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
		//取得・使用方法を決定する
		EmploymentHolidayMngSetting employmentHolidaySetting = inforData.getEmploymentHolidaySetting();
		ExpirationTime expriTime = ExpirationTime.UNLIMITED;
		if(employmentHolidaySetting != null
				&& employmentHolidaySetting.getDayOffSetting() != null) {
		/*	expriTime = employmentHolidaySetting.getDayOffSetting().getCompensatoryAcquisitionUse().getExpirationTime();*/
		} else {
			expriTime = inforData.getCompanyHolidaySetting().getDayOffSetting().getCompensatoryAcquisitionUse().getExpirationTime();
		}
		//アルゴリズム「休暇使用期限から使用期限日を算出する」を実行する
		return commonDate(require, expriTime, inforData.getEmploymentHolidaySetting().getEmploymentCode(), inforData.getYmd());
	}

	private static GeneralDate commonDate(RequireM1 require, ExpirationTime expriTime, String employmentCode, GeneralDate dateInfor) {
		//アルゴリズム「休暇使用期限から使用期限日を算出する」を実行する
		if(expriTime == ExpirationTime.END_OF_YEAR) {
			// 使用期限日を作成する
			return createExpDate(require, AppContexts.user().companyId(), dateInfor);
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

	/**
	 * 年度末クリアの使用期限日を作成する
	 * @param require
	 */
	private static GeneralDate createExpDate(RequireM1 require, String companyId, GeneralDate date) {
		// 会社の期首月を取得
		CompanyDto companyDto = require.getFirstMonth(companyId);

		int  startMonth = companyDto.getStartMonth();

		GeneralDate nextPeriod = GeneralDate.fromString(
				String.valueOf(date.year()) + "/" + startMonth + "/" + String.valueOf(date.day()), "yyyy/MM/dd");
		//月　＝　次の期首月 －１
		String endPeriodMonth = String.valueOf(nextPeriod.addMonths(-1).month());
		//日　＝　次の期首月－１の月末の日
		String endPeriodDay = String.valueOf(nextPeriod.addMonths(-1).lastDateInMonth());

		String endPeriodYear = "";

		//年月日．月　＜　次の期首月
		if(date.month() < startMonth){
			//年　＝　年月日．年
			endPeriodYear = String.valueOf(date.year());
		}

		//年月日．月　＞＝　次の期首月
		if(date.month() >= startMonth){
			//年　＝　年月日．年　＋　１
			endPeriodYear = String.valueOf(date.addYears(1).year());
		}

		//但し１月が期首月の場合
		if(startMonth == 1){
			//年 = 年 - 1
			endPeriodYear = String.valueOf(date.addYears(-1).year());
		}

		return GeneralDate.fromString(endPeriodYear + "/" + endPeriodMonth + "/" + endPeriodDay, "yyyy/MM/dd");
	}


	public static interface RequireM1 extends UseDateDeadlineFromDatePeriod.RequireM1 {

		CompanyDto getFirstMonth(String companyId);

	}

	/**
	 * 残数作成元情報から暫定代休管理データ（時間）を作成する
	 *
	 * @param inforData
	 *            残数作成元情報
	 * @return List<暫定代休管理データ>
	 */
	public static List<InterimDayOffMng> createSubstituteHolidayTime(InforFormerRemainData inforData) {

		List<InterimDayOffMng> dayOffs = new ArrayList<InterimDayOffMng>();

		// 休暇種類を指定して時間休暇使用時間を取得する
		List<VacationTimeInfor> vacations = getTimeByHolidayType(HolidayType.SUBSTITUTE, inforData);


		//時間休暇使用時間の件数分ループ
		vacations.forEach(vac -> {
			String mngId = IdentifierUtil.randomUniqueId();
			//暫定代休管理データを作成する
			dayOffs.add(
					new InterimDayOffMng(
							mngId,
							inforData.getSid(),
							inforData.getYmd(),
							vac.getCreateData(),
							RemainType.SUBHOLIDAY,
							new RequiredTime(vac.getTotalTimes()),
							new RequiredDay(0d),
							new UnOffsetTime(vac.getTotalTimes()),
							new UnOffsetDay(0d),
							Optional.ofNullable(DigestionHourlyTimeType.of(false, Optional.ofNullable(vac.getTimeType())))
							)
					);
		});

		return dayOffs;
	}

	/**
	 * 残数作成元情報から暫定年休管理データ（時間）を作成する
	 *
	 * @param inforData
	 *            残数作成元情報
	 * @return List<暫定年休管理データ>
	 */
	public static List<TempAnnualLeaveMngs> createAnnualHolidayTime(InforFormerRemainData inforData) {

		List<TempAnnualLeaveMngs> annuals = new ArrayList<TempAnnualLeaveMngs>();

		//休暇種類を指定して時間休暇使用時間を取得する
		List<VacationTimeInfor> vacations = getTimeByHolidayType(HolidayType.ANNUAL, inforData);


		//時間休暇使用時間の件数分ループ

		vacations.forEach(vac -> {
			String mngId = IdentifierUtil.randomUniqueId();
			//暫定年休管理データを作成する
			annuals.add(new TempAnnualLeaveMngs(
					mngId,
					inforData.getSid(),
					inforData.getYmd(),
					vac.getCreateData(),
					RemainType.ANNUAL,
					new WorkTypeCode(vac.getWorkTypeCode()),
					new LeaveUsedNumber(0d, vac.getTotalTimes(), null),
					Optional.ofNullable(DigestionHourlyTimeType.of(false, Optional.ofNullable(vac.getTimeType()))))
					);
		});

		return annuals;
	}

	/**
	 * 休暇種類を指定して時間休暇使用時間を取得する
	 *
	 * @param holidayType
	 *            休暇種類
	 * @param inforData
	 *            残数作成元情報
	 */
	private static List<VacationTimeInfor> getTimeByHolidayType(HolidayType holidayType,
			InforFormerRemainData inforData) {

		List<VacationTimeInfor> result = new ArrayList<VacationTimeInfor>();

		// 時間休暇使用時間をチェック
		if (CollectionUtil.isEmpty(inforData.getVactionTime())) {
			return result;
		}

		// 時間休暇使用時間の件数ループ
		inforData.getVactionTime().forEach(vacTime -> {
			if (!CollectionUtil.isEmpty(vacTime.getVacationUsageTimeDetails())) {
				// 休暇種類のデータを抽出する
				// 【条件】
				// 時間．休暇種類 = INPUT．休暇種類
				// 時間．時間 > 0
				if (vacTime.getVacationUsageTimeDetails().stream()
						.filter(x -> (x.getHolidayType().equals(holidayType) && x.getTimes() > 0)).findFirst()
						.isPresent()) {
					result.add(vacTime);
				}
			}
		});

		return result;
	}

	/**
	 * 残数作成元情報から暫定特別休暇管理データ（時間）を作成する
	 *
	 * @param inforData
	 *            残数作成元情報
	 * @return List<暫定特別休暇データ>
	 */
	public static List<InterimSpecialHolidayMng> createSpecialHolidayTime(InforFormerRemainData inforData) {

		List<InterimSpecialHolidayMng> specials = new ArrayList<InterimSpecialHolidayMng>();
		//休暇種類を指定して時間休暇使用時間を取得する

		List<VacationTimeInfor> vacations = getTimeByHolidayType(HolidayType.SPECIAL, inforData);

		vacations.forEach(vac -> {
			String mngId = IdentifierUtil.randomUniqueId();

			//暫定特休管理データを作成する

			specials.add(new InterimSpecialHolidayMng(
					mngId,
					inforData.getSid(),
					inforData.getYmd(),
					vac.getCreateData(),
					RemainType.SPECIAL,
					vac.getVacationUsageTimeDetails().get(0).getSpecialHolidayCode().map(x -> x).orElse(0),
					ManagermentAtr.TIMES,
					Optional.ofNullable(new UseTime(vac.getTotalTimes())),
					Optional.ofNullable(new UseDay(0d)),
					Optional.ofNullable(DigestionHourlyTimeType.of(false, Optional.ofNullable(vac.getTimeType()))))
					);

		});

		return specials;
	}

	/**
	 * 残数作成元情報から暫定60H超休管理データを作成する
	 *
	 * @param inforData
	 *            残数作成元情報
	 * @return List<暫定60H超休管理データ>
	 */
	public static List<TmpHolidayOver60hMng> createHolidayOver60hTime(InforFormerRemainData inforData) {

		List<TmpHolidayOver60hMng> holidayOver60hs = new ArrayList<TmpHolidayOver60hMng>();

		// 休暇種類を指定して時間休暇使用時間を取得する

		List<VacationTimeInfor> vacations = getTimeByHolidayType(HolidayType.SIXTYHOUR, inforData);

		vacations.forEach(vac -> {
			String mngId = IdentifierUtil.randomUniqueId();

			//暫定60H超休管理データを作成する

			holidayOver60hs.add(
					new TmpHolidayOver60hMng(
							mngId,
							inforData.getSid(),
							inforData.getYmd(),
							vac.getCreateData(),
							RemainType.SIXTYHOUR,
							Optional.ofNullable(new UseTime(vac.getTotalTimes())),
							Optional.ofNullable(DigestionHourlyTimeType.of(false, Optional.ofNullable(vac.getTimeType()))))
					);
		});

		return holidayOver60hs;
	}

	/**
	 * 残数作成元情報から暫定子の看護管理データ（時間）を作成する
	 *
	 * @param inforData
	 *            残数作成元情報
	 * @return List<暫定子の看護管理データ>
	 */
	public static List<TempChildCareManagement> createChildCareTime(InforFormerRemainData inforData) {

		List<TempChildCareManagement> childcares = new ArrayList<TempChildCareManagement>();

		// 休暇種類を指定して時間休暇使用時間を取得する

		List<VacationTimeInfor> vacations = getTimeByHolidayType(HolidayType.CHILDCARE, inforData);

		vacations.forEach(vac -> {
			String mngId = IdentifierUtil.randomUniqueId();

			//暫定子の看護管理データを作成す

			childcares.add(
					new TempChildCareManagement(
							mngId,
							inforData.getSid(),
							inforData.getYmd(),
							vac.getCreateData(),
							ChildCareNurseUsedNumber.of(new DayNumberOfUse(0d),
									Optional.ofNullable(new TimeOfUse(vac.getTotalTimes()))),
							Optional.ofNullable(DigestionHourlyTimeType.of(false, Optional.ofNullable(vac.getTimeType()))))
					);
		});

		return childcares;
	}

	/**
	 * 残数作成元情報から暫定介護管理データ（時間）を作成する
	 * 
	 * @param inforData
	 * @return
	 */
	public static Collection<? extends InterimRemain> createCareTime(InforFormerRemainData inforData) {
		List<TempCareManagement> cares = new ArrayList<TempCareManagement>();

		// 休暇種類を指定して時間休暇使用時間を取得する

		List<VacationTimeInfor> vacations = getTimeByHolidayType(HolidayType.CARE, inforData);

		vacations.forEach(vac -> {
			String mngId = IdentifierUtil.randomUniqueId();

			//暫定介護管理データを作成する

			cares.add(
					new TempCareManagement(
							mngId,
							inforData.getSid(),
							inforData.getYmd(),
							vac.getCreateData(),
							ChildCareNurseUsedNumber.of(new DayNumberOfUse(0d),
									Optional.ofNullable(new TimeOfUse(vac.getTotalTimes()))),
							Optional.ofNullable(DigestionHourlyTimeType.of(false, Optional.ofNullable(vac.getTimeType()))))
					);
		});

		return cares;
	}

	public static DailyInterimRemainMngData createInterimChildNursing(InforFormerRemainData inforData, CareUseDetail care,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//残数作成元情報を取得
		String mngId = IdentifierUtil.randomUniqueId();

		TempChildCareManagement childData =  new TempChildCareManagement(
				mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				ChildCareNurseUsedNumber.of(new DayNumberOfUse(care.getDays()), Optional.empty()),
				Optional.ofNullable(DigestionHourlyTimeType.of(true, Optional.empty())));

		mngData.getRecAbsData().add(childData);
		return mngData;


	}

	public static DailyInterimRemainMngData createInterimNursing(InforFormerRemainData inforData, CareUseDetail care,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		// 残数作成元情報を取得
		String mngId = IdentifierUtil.randomUniqueId();

		TempCareManagement careData = new TempCareManagement(mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemainInfor(workTypeClass).get().getCreateData(),
				ChildCareNurseUsedNumber.of(new DayNumberOfUse(care.getDays()), Optional.empty()),
				Optional.ofNullable(DigestionHourlyTimeType.of(true, Optional.empty())));

		mngData.getRecAbsData().add(careData);
		return mngData;
	}



}
