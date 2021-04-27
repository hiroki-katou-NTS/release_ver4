package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.layer.app.cache.CacheCarrier;
//import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.holidaymanagement.interim.InterimHolidayMng;
import nts.uk.ctx.at.shared.dom.holidaymanagement.interim.InterimHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbasMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.require.RemainNumberTempRequireService;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TempAnnualLeaveMngs;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.holidayover60h.interim.TmpHolidayOver60hMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.holidayover60h.interim.TmpHolidayOver60hMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareManagementRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.TempChildCareManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.TempChildCareManagementRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByApplicationData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByRecordData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByScheData;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;

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
	private TmpAnnualHolidayMngRepository tmpAnnual;
	@Inject
	private TmpResereLeaveMngRepository tmpResereLeave;
	@Inject
	private InterimRecAbasMngRepository recAbsRepos;
	@Inject
	private InterimBreakDayOffMngRepository breakDayOffRepos;
	@Inject
	private InterimSpecialHolidayMngRepository specialHoliday;
	@Inject
	private RemainNumberTempRequireService requireService;
	@Inject
	private TmpHolidayOver60hMngRepository over60hMngRepository;
	@Inject
	private InterimHolidayMngRepository holidayMngRepository;
	@Inject
	private TempChildCareManagementRepository  childCareManagementRepo;
	@Inject
	private TempCareManagementRepository careManagementRepo;
	@Inject
	private CompensLeaveComSetRepository leaveSetRepos;
	@Inject
	private ComSubstVacationRepository subRepos;

	@Override
	public void registerDateChange(String cid, String sid, List<GeneralDate> lstDate) {

		//暫定データを作成する為の勤務予定を取得する
		List<ScheRemainCreateInfor> lstScheData = this.remainScheData.createRemainInforNew(sid, lstDate);

		//暫定データを作成する為の日別実績を取得する

		List<RecordRemainCreateInfor> lstRecordData = this.remainRecordData.lstRecordRemainData(sid, lstDate);

		List<InterimRemain> interimRemains  = new ArrayList<InterimRemain>();

		for (GeneralDate loopDate : lstDate) {

			DatePeriod datePeriod = new DatePeriod(loopDate, loopDate);

			// (Imported)「残数作成元の申請を取得する」
			List<AppRemainCreateInfor> lstAppData = this.remainAppData.lstRemainDataFromApp(new CacheCarrier(), cid,
					sid, datePeriod);

			// 指定期間の暫定残数管理データを作成する
			InterimRemainCreateDataInputPara inputParam = new InterimRemainCreateDataInputPara(cid,
					sid,
					datePeriod,
					lstRecordData,
					lstScheData,
					lstAppData,
					false);

				Map<GeneralDate, DailyInterimRemainMngData> dailyMap = InterimRemainOffPeriodCreateData.createInterimRemainDataMng(requireService.createRequire(), new CacheCarrier(), inputParam, new CompanyHolidayMngSetting(cid , subRepos.findById(cid), leaveSetRepos.find(cid)));

			// もらった暫定残数管理データを受け取る

			interimRemains.addAll(dailyMap.entrySet().stream().map(x -> x.getValue().getRecAbsData())
					.flatMap(List::stream).collect(Collectors.toList()));

		}
		
		

		//暫定データの登録処理
		regisInterimDataProcess(cid, sid, interimRemains, lstDate);

	}


	/**
	 * 暫定データの登録処理
	 *
	 * @param interimRemains
	 * @param sid
	 * @param cid
	 * @param lstDate
	 */

	private void regisInterimDataProcess(String cid, String sid,
			List<InterimRemain> interimRemains, List<GeneralDate> lstDate) {

		//対象年月日の「暫定残数管理データ」を全て削除
		deleteAllData(cid, sid, lstDate);

		interimRemains.forEach(x -> {
			updateInterimData(cid, sid, x);
		});
	}

	/**
	 *
	 * @param cid
	 * @param sid
	 * @param interimRemain
	 */
	private void updateInterimData(String cid, String sid, InterimRemain interimRemain) {


		switch (interimRemain.getRemainType()) {
		case ANNUAL:
			// 暫定年休データの登録
			TempAnnualLeaveMngs annual = (TempAnnualLeaveMngs) interimRemain;
			this.tmpAnnual.persistAndUpdate(annual);
			break;
		case FUNDINGANNUAL:
			// 暫定積立年休データの登録
			TmpResereLeaveMng resere = (TmpResereLeaveMng) interimRemain;
			this.tmpResereLeave.persistAndUpdate(resere);
			break;
		case SPECIAL:
			// 暫定特休データの登録
			InterimSpecialHolidayMng special = (InterimSpecialHolidayMng) interimRemain;
			this.specialHoliday.persistAndUpdateInterimSpecialHoliday(special);
			break;
		case PAUSE:
			//暫定振休データの登録
			InterimAbsMng abs = (InterimAbsMng) interimRemain;
			this.recAbsRepos.persistAndUpdateInterimAbsMng(abs);
			break;
		case PICKINGUP:
			//暫定振出データの登録
			InterimRecMng rec = (InterimRecMng) interimRemain;
			this.recAbsRepos.persistAndUpdateInterimRecMng(rec);
			break;
		case SUBHOLIDAY:
			//暫定代休データの登録
			InterimDayOffMng dayOff = (InterimDayOffMng) interimRemain;
			this.breakDayOffRepos.persistAndUpdateInterimDayOffMng(dayOff);
			break;
		case BREAK:
			// 暫定休出データの登録
			InterimBreakMng breakMng = (InterimBreakMng) interimRemain;
			this.breakDayOffRepos.persistAndUpdateInterimBreakMng(breakMng);
			break;
		case SIXTYHOUR:
			// 暫定60H超休データの登録
			TmpHolidayOver60hMng dataMng = (TmpHolidayOver60hMng) interimRemain;
			this.over60hMngRepository.persistAndUpdate(dataMng);
			break;
		case PUBLICHOLIDAY:
			// 暫定公休データの登録
			InterimHolidayMng holidayMng = (InterimHolidayMng) interimRemain;
			this.holidayMngRepository.persistAndUpdate(holidayMng);
			break;
		case CHILDCARE:
			// 暫定子の看護管理データの登録
			TempChildCareManagement tempChildCareManagement = (TempChildCareManagement) interimRemain;
			this.childCareManagementRepo.persistAndUpdate(tempChildCareManagement);
			break;
		case CARE:
			// 暫定介護データの登録
			TempCareManagement tempCareManagement = (TempCareManagement) interimRemain;
			this.careManagementRepo.persistAndUpdate(tempCareManagement);
			break;
			}
		}

	/**
	 * 暫定残数管理データ
	 * @param lstDate
	 * @param sid
	 * @param cid
	 */
	private void deleteAllData(String cid, String sid, List<GeneralDate> lstDate) {

		lstDate.forEach(day -> {
			// 暫定年休管理データをDelete
			this.tmpAnnual.deleteSidAndYmd(sid, day);
			// 暫定積立年休管理データを削除する
			this.tmpResereLeave.deleteSidPeriod(sid, new DatePeriod(day, day));
			// 特別休暇暫定データをDeleteする
			this.specialHoliday.deleteSpecialHolidayBySidAndYmd(sid, day);
			// 暫定振休管理データをDeleteする
			this.recAbsRepos.deleteInterimAbsMngBySidAndYmd(sid, day);
			// 暫定振出管理データをDeleteする
			this.recAbsRepos.deleteInterimRecMngBySidAndYmd(sid, day);
			// 暫定代休管理データをDeleteする
			this.breakDayOffRepos.deleteInterimDayOffMngBySidAndYmd(sid, day);
			// 暫定休出管理データをDeleteする
			this.breakDayOffRepos.deleteInterimBreakMngBySidAndYmd(sid, day);
			// 暫定60H超休管理データをDelete
			this.over60hMngRepository.deleteBySidAndYmd(sid, day);
			// 暫定公休管理データをDeleteする
			this.holidayMngRepository.deleteBySidAndYmd(sid, day);
			// 暫定子の看護管理データをDeleteする
			this.childCareManagementRepo.removeBySidAndYmd(sid, day);
			// 暫定介護管理データをDeleteする
			this.careManagementRepo.deleteBySidAndYmd(sid, day);

		});
	}

	/*private void clearInterimWhenRecordScheAppNotData(String sid, List<GeneralDate> lstDate,
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
					resereLeave.deleteById(x.getRemainManaID());;
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


	}*/



}
