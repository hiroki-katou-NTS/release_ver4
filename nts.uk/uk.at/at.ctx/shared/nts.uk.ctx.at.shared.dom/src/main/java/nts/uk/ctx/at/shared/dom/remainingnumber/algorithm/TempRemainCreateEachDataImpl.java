package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.InforFormerRemainData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.OccurrenceUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.TranferTimeInfor;
import nts.uk.ctx.at.shared.dom.vacation.service.UseDateDeadlineFromDatePeriod;
import nts.uk.ctx.at.shared.dom.vacation.setting.ExpirationTime;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.SubstVacationSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;

@Stateless
public class TempRemainCreateEachDataImpl implements TempRemainCreateEachData{
	@Inject
	private UseDateDeadlineFromDatePeriod useDateService;
	@Override
	public DailyInterimRemainMngData createInterimAnnualHoliday(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		if(!occUseDetail.isPresent()) {
			return mngData;
		}
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		OccurrenceUseDetail useDetail = occUseDetail.get();
		String mngId = IdentifierUtil.randomUniqueId();
		InterimRemain ramainData = new InterimRemain(mngId, 
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemain().get().getCreateData(), 
				RemainType.ANNUAL,
				RemainAtr.SINGLE);
		recAbsData.add(ramainData);
		TmpAnnualHolidayMng annualMng = new TmpAnnualHolidayMng(mngId, 
				inforData.getWorkTypeRemain().get().getWorkTypeCode(), 
				new UseDay(useDetail.getDays()));
		mngData.setRecAbsData(recAbsData);
		mngData.setAnnualHolidayData(Optional.of(annualMng));
		return mngData;
	}

	@Override
	public DailyInterimRemainMngData createInterimReserveHoliday(InforFormerRemainData inforData,
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
				inforData.getWorkTypeRemain().get().getCreateData(), 
				RemainType.FUNDINGANNUAL,
				RemainAtr.SINGLE);
		recAbsData.add(ramainData);
		TmpResereLeaveMng resereData = new TmpResereLeaveMng(mngId, new UseDay(occUseDetail.get().getDays()));
		mngData.setResereData(Optional.of(resereData));
		mngData.setRecAbsData(recAbsData);
		return mngData;
	}
	@Override
	public DailyInterimRemainMngData createInterimAbsData(InforFormerRemainData inforData, WorkTypeClassification workTypeClass,
			DailyInterimRemainMngData mngData) {
		//残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		if(occUseDetail.isPresent()) {
			String mngId = IdentifierUtil.randomUniqueId();
			InterimRemain mngDataRemain = new InterimRemain(mngId,
					inforData.getSid(),
					inforData.getYmd(),
					inforData.getWorkTypeRemain().get().getCreateData(),
					RemainType.PAUSE,
					RemainAtr.SINGLE);
			InterimAbsMng absData = new InterimAbsMng(mngId,
					new RequiredDay(occUseDetail.get().getDays()),
					new UnOffsetDay(occUseDetail.get().getDays()));
			mngData.setInterimAbsData(Optional.of(absData));
			recAbsData.add(mngDataRemain);
			mngData.setRecAbsData(recAbsData);
		}
		return mngData;
	}

	@Override
	public DailyInterimRemainMngData createInterimDayOffData(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		if(occUseDetail.isPresent()) {
			if(!occUseDetail.get().isUseAtr()) {
				String mngId = IdentifierUtil.randomUniqueId();
				InterimRemain mngDataRemain = new InterimRemain(mngId, inforData.getSid(), inforData.getYmd(), 
						inforData.getWorkTypeRemain().get().getCreateData(), RemainType.SUBHOLIDAY, RemainAtr.SINGLE);
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

	@Override
	public DailyInterimRemainMngData createInterimRecData(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		// 残数作成元情報のアルゴリズム「分類を指定して発生使用明細を取得する」を実行する
		Optional<OccurrenceUseDetail> occUseDetail = inforData.getOccurrenceUseDetail(workTypeClass);
		if(!occUseDetail.isPresent()) {
			return mngData;
		}
		//アルゴリズム「振休使用期限日の算出」を実行する
		GeneralDate useDate = this.getUseDays(inforData);
		String mngId = IdentifierUtil.randomUniqueId();
		InterimRemain remainMng = new InterimRemain(mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemain().get().getCreateData(), 
				RemainType.PICKINGUP, 
				RemainAtr.SINGLE);
		List<OccurrenceUseDetail> occurrenceDetailData =  inforData.getWorkTypeRemain().get().getOccurrenceDetailData()
				.stream().filter(x -> x.getWorkTypeAtr() == workTypeClass)
				.collect(Collectors.toList());
		
		InterimRecMng recMng = new InterimRecMng(mngId,
				useDate,
				new OccurrenceDay(occurrenceDetailData.isEmpty() ? 0 : occurrenceDetailData.get(0).getDays()),
				StatutoryAtr.NONSTATURORY,
				new UnUsedDay(occurrenceDetailData.isEmpty() ? 0 : occurrenceDetailData.get(0).getDays()));
		mngData.setRecData(Optional.of(recMng));
		List<InterimRemain> recAbsData = new ArrayList<>(mngData.getRecAbsData());
		recAbsData.add(remainMng);
		mngData.setRecAbsData(recAbsData);
		return mngData;
	}
	/**
	 * 振休使用期限日を取得する
	 * @return
	 */
	private GeneralDate getUseDays(InforFormerRemainData inforData) {
		//雇用別休暇管理設定の振休をチェックする
		EmploymentHolidayMngSetting employmentHolidaySetting = inforData.getEmploymentHolidaySetting();
		SubstVacationSetting subSetting = null;
		if(employmentHolidaySetting != null && employmentHolidaySetting.getAbsSetting().isPresent()) {
			subSetting = employmentHolidaySetting.getAbsSetting().get().getSetting();
		} else {
			if (inforData.getCompanyHolidaySetting().getAbsSetting().isPresent()) {
				ComSubstVacation companyHolidaySetting = inforData.getCompanyHolidaySetting().getAbsSetting().get();
				subSetting = companyHolidaySetting.getSetting();
			}
		}
		if(subSetting == null) {
			return null;
		}
		//アルゴリズム「休暇使用期限から使用期限日を算出する」を実行する
		if(subSetting.getExpirationDate() == ExpirationTime.END_OF_YEAR) {
			//TODO 
		} else if (subSetting.getExpirationDate() == ExpirationTime.UNLIMITED) {
			return GeneralDate.max();
		} else {
			//期限指定のある使用期限日を作成する
			if(inforData.getEmploymentHolidaySetting() == null) {
				return null;
			}
			return useDateService.useDateDeadline(inforData.getEmploymentHolidaySetting().getEmploymentCode(),subSetting.getExpirationDate(), inforData.getYmd());
		}
		return null;
	}

	@Override
	public DailyInterimRemainMngData createInterimBreak(InforFormerRemainData inforData,
			WorkTypeClassification workTypeClass, DailyInterimRemainMngData mngData) {
		//代休振替情報をチェックする
		if(!inforData.getDayOffTranfer().isPresent()) {
			return mngData;
		}
		//代休振替情報のアルゴリズム「振替時間情報を取得する」を実行する
		TranferTimeInfor dataChange = inforData.getDayOffTranfer().get().getTranferTimeInfor();
		//代休使用期限日を取得する
		GeneralDate useDate = this.getUseDays(inforData);
		String mngId = IdentifierUtil.randomUniqueId();
		InterimRemain recAbsData = new InterimRemain(mngId,
				inforData.getSid(),
				inforData.getYmd(),
				inforData.getWorkTypeRemain().get().getCreateData(),
				RemainType.BREAK,
				RemainAtr.SINGLE);
		List<InterimRemain> lstRecAbsData = new ArrayList<>(mngData.getRecAbsData());
		lstRecAbsData.add(recAbsData);
		mngData.setRecAbsData(lstRecAbsData);
		//時間代休を利用するかチェックする		
		if(inforData.isDayOffTimeIsUse()) {
			//振替時間をチェックする
			InterimBreakMng breakMng = new InterimBreakMng(mngId,
					new AttendanceTime(0),
					useDate,
					new OccurrenceTime(dataChange.getTranferTime()),
					new OccurrenceDay(0.0),
					new AttendanceTime(0),
					new UnUsedTime(dataChange.getTranferTime()), 
					new UnUsedDay(0.0));
			mngData.setBreakData(Optional.of(breakMng));
		} else {
			InterimBreakMng breakMng = new InterimBreakMng(mngId,
					new AttendanceTime(0),
					useDate,
					new OccurrenceTime(0),
					new OccurrenceDay(dataChange.getDays().isPresent() ? dataChange.getDays().get() : 0.0),
					new AttendanceTime(0),
					new UnUsedTime(0), 
					new UnUsedDay(dataChange.getDays().isPresent() ? dataChange.getDays().get() : 0.0));
			mngData.setBreakData(Optional.of(breakMng));
		}
		return mngData;
	}
	
	
}
