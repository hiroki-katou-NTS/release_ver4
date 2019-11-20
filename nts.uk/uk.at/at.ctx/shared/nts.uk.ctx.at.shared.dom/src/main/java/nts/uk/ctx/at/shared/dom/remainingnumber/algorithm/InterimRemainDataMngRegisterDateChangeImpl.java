package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

//import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbasMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByApplicationData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByRecordData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByScheData;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class InterimRemainDataMngRegisterDateChangeImpl implements InterimRemainDataMngRegisterDateChange{
	@Inject
	private RemainCreateInforByScheData remainScheData;
	@Inject
	private RemainCreateInforByRecordData remainRecordData;
	@Inject
	private RemainCreateInforByApplicationData remainAppData;
	@Inject
	private InterimRemainDataMngRegister mngRegister;
	@Inject
	private ComSubstVacationRepository subRepos;
	@Inject
	private CompensLeaveComSetRepository leaveSetRepos;
	@Inject
	private InterimRemainRepository inRemainData;
	@Inject
	private TmpAnnualHolidayMngRepository annualHolidayMngRepos;
	@Inject
	private TmpResereLeaveMngRepository resereLeave;
	@Inject
	private InterimRecAbasMngRepository recAbsRepos;
	@Inject
	private InterimBreakDayOffMngRepository breakDayOffRepos;
	@Inject
	private InterimSpecialHolidayMngRepository specialHoliday;

	@Override
	public void registerDateChange(String cid, String sid, List<GeneralDate> lstDate) {
		//「残数作成元情報(実績)」を取得する
		List<RecordRemainCreateInfor> lstRecordData = remainRecordData.lstRecordRemainData(cid, sid, lstDate);
		//「残数作成元の勤務予定を取得する」
		List<ScheRemainCreateInfor> lstScheData = remainScheData.createRemainInfor(cid, sid, lstDate);
		//雇用履歴と休暇管理設定を取得する
		Optional<ComSubstVacation> comSetting = subRepos.findById(cid);
		CompensatoryLeaveComSetting leaveComSetting = leaveSetRepos.find(cid);
		CompanyHolidayMngSetting comHolidaySetting = new CompanyHolidayMngSetting(cid, comSetting, leaveComSetting);
		for(GeneralDate loopDate : lstDate){
			DatePeriod datePeriod = new DatePeriod(loopDate, loopDate);
			//「残数作成元の申請を取得する」
			List<AppRemainCreateInfor> lstAppData = remainAppData.lstRemainDataFromApp(cid, sid, datePeriod);
			clearInterimWhenRecordScheAppNotData(sid, lstDate, lstRecordData, lstScheData, lstAppData);
			//指定期間の暫定残数管理データを作成する
			InterimRemainCreateDataInputPara inputData = new InterimRemainCreateDataInputPara(cid, 
					sid, 
					datePeriod, 
					lstRecordData, 
					lstScheData,
					lstAppData,
					false);
			mngRegister.registryInterimDataMng(inputData, comHolidaySetting);
		}
	}
	/**
	 * スケジュールのデータがないし実績データがないし、申請を削除の場合暫定データがあったら削除します。
	 * @param sid
	 * @param lstDate
	 * @param lstRecordData
	 * @param lstScheData
	 * @param lstAppData
	 */
	private void clearInterimWhenRecordScheAppNotData(String sid, List<GeneralDate> lstDate,
			List<RecordRemainCreateInfor> lstRecordData, List<ScheRemainCreateInfor> lstScheData,
			List<AppRemainCreateInfor> lstAppData) {
		if(lstRecordData.isEmpty()
				&& lstScheData.isEmpty()
				&& (lstAppData.isEmpty() || lstAppData.size() < lstDate.size())) {
			List<GeneralDate> lstDelete = new ArrayList<>();
			lstDate.stream().forEach(x -> {
				List<AppRemainCreateInfor> lstAppDateNotDelete = lstAppData.stream().filter(a -> a.getAppDate().equals(x)
							|| (a.getStartDate().isPresent() && a.getEndDate().isPresent() 
								&& a.getStartDate().get().beforeOrEquals(x) && a.getEndDate().get().afterOrEquals(x)))
						.collect(Collectors.toList());
				if(lstAppDateNotDelete.isEmpty()) {
					lstDelete.add(x);
				}
			});
			//スケジュールのデータがないし実績データがないし、申請を削除の場合暫定データがあったら削除します。
			List<InterimRemain> getDataBySidDates = inRemainData.getDataBySidDates(sid, !lstDelete.isEmpty() ? lstDelete : lstDate);
			getDataBySidDates.stream().forEach(x -> {
				
				inRemainData.deleteById(x.getRemainManaID());
				//Delete
				switch (x.getRemainType()) {
				case ANNUAL:
					annualHolidayMngRepos.deleteById(x.getRemainManaID());
					break;
				case FUNDINGANNUAL:
					resereLeave.deleteById(x.getRemainManaID());
					break;
				case PAUSE:
					recAbsRepos.deleteInterimAbsMng(x.getRemainManaID());
					break;
				case PICKINGUP:
					recAbsRepos.deleteInterimRecMng(x.getRemainManaID());
					break;
				case SUBHOLIDAY:
					breakDayOffRepos.deleteInterimDayOffMng(x.getRemainManaID());
					break;
				case BREAK:
					breakDayOffRepos.deleteInterimBreakMng(x.getRemainManaID());
					break;
				case SPECIAL:
					specialHoliday.deleteSpecialHoliday(x.getRemainManaID());
					break;
				default:
					break;
				}
			});
			if(lstAppData.isEmpty()) {
				return;	
			}
		}
	}

}
