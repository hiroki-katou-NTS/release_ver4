package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecMngInPeriodParamInput;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsenceReruitmentMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpReserveLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.ComplileInPeriodOfSpecialLeaveParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.AnnualLeaveErrorSharedImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.GetAnnLeaRemNumWithinPeriodSharedImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.ReserveLeaveErrorImport;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class InterimRemainDataMngCheckRegisterImpl implements InterimRemainDataMngCheckRegister{
	@Inject
	private BreakDayOffMngInPeriodQuery breakDayOffMngService;
	@Inject
	private AbsenceReruitmentMngInPeriodQuery absRecMngService;
	@Inject
	private InterimRemainOffPeriodCreateData interimCreateData;
	@Inject
	private ComSubstVacationRepository subRepos;
	@Inject
	private CompensLeaveComSetRepository leaveSetRepos;
	@Inject
	private SpecialLeaveManagementService speLeaveSevice;
	@Inject
	private GetAnnLeaRemNumWithinPeriodSharedImport annualService;
	@Override
	public EarchInterimRemainCheck checkRegister(InterimRemainCheckInputParam inputParam) {
		//代休不足区分、振休不足区分、年休不足区分、積休不足区分、特休不足区分、公休不足区分、超休不足区分をfalseにする(初期化)
		EarchInterimRemainCheck outputData = new EarchInterimRemainCheck(false, false, false, false, false, false, false);
		Optional<ComSubstVacation> comSetting = subRepos.findById(inputParam.getCid());
		CompensatoryLeaveComSetting leaveComSetting = leaveSetRepos.find(inputParam.getCid());
		CompanyHolidayMngSetting comHolidaySetting = new CompanyHolidayMngSetting(inputParam.getCid(), comSetting, leaveComSetting);
		//暫定管理データをメモリ上で作成する
		Map<GeneralDate, DailyInterimRemainMngData> mapDataOutput = new HashMap<>();
		inputParam.getAppData().stream().forEach(x -> {
			DatePeriod dateData = inputParam.getRegisterDate();
			if(x.getAppType() == ApplicationType.COMPLEMENT_LEAVE_APPLICATION) {
				dateData = new DatePeriod(x.getAppDate(), x.getAppDate());
			}
			InterimRemainCreateDataInputPara dataCreate = new InterimRemainCreateDataInputPara(inputParam.getCid(),
					inputParam.getSid(),
					dateData,
					inputParam.getRecordData(),
					inputParam.getScheData(),
					inputParam.getAppData(),
					false);
			Map<GeneralDate, DailyInterimRemainMngData> mapDataOutputTmp = interimCreateData.createInterimRemainDataMng(dataCreate, comHolidaySetting);	
			//振休申請は取り消しになる時を対応します。
			if(x.getAppType() == ApplicationType.COMPLEMENT_LEAVE_APPLICATION && mapDataOutputTmp.containsKey(x.getAppDate())){
				DailyInterimRemainMngData data = mapDataOutputTmp.get(x.getAppDate());
				if(data.getRecAbsData().isEmpty()) {
					String mngId = IdentifierUtil.randomUniqueId();
					InterimRemain furikyu = new InterimRemain(mngId, 
							inputParam.getSid(),
							x.getAppDate(),
							CreateAtr.APPBEFORE,
							RemainType.PAUSE,
							RemainAtr.SINGLE);
					InterimAbsMng furikyuSinsei = new InterimAbsMng(mngId, new RequiredDay(0.0), new UnOffsetDay(0.0));
					data.getRecAbsData().add(furikyu);
					data.setInterimAbsData(Optional.of(furikyuSinsei));
					mapDataOutputTmp.remove(x.getAppDate());
					mapDataOutputTmp.put(x.getAppDate(), data);
				}
			}
			mapDataOutputTmp.forEach((z,y) -> {
				if(!mapDataOutput.containsKey(z)) {
					mapDataOutput.put(z, y);	
				}				
			});
			
		});
		InterimEachData eachData = this.interimInfor(mapDataOutput);
		List<InterimRemain> interimMngAbsRec = eachData.getInterimMngAbsRec();
		List<InterimAbsMng> useAbsMng = eachData.getUseAbsMng();
		List<InterimRecMng> useRecMng = eachData.getUseRecMng();
		List<InterimRemain> interimMngBreakDayOff = eachData.getInterimMngBreakDayOff();
		List<InterimBreakMng> breakMng = eachData.getBreakMng();
		List<InterimDayOffMng> dayOffMng = eachData.getDayOffMng();
		List<InterimSpecialHolidayMng> specialHolidayData = eachData.getSpecialHolidayData();
		List<InterimRemain> interimSpecial = eachData.getInterimSpecial();
		List<TmpAnnualHolidayMng> annualHolidayData = eachData.getAnnualHolidayData();
		List<InterimRemain> annualMng = eachData.getAnnualMng();
		List<TmpResereLeaveMng> resereLeaveData = eachData.getResereLeaveData();
		List<InterimRemain> resereMng = eachData.getResereMng();
		
		//代休チェック区分をチェックする
		if(inputParam.isChkSubHoliday()) {
			
			//期間内の休出代休残数を取得する
			BreakDayOffRemainMngParam mngParam = new BreakDayOffRemainMngParam(inputParam.getCid(),
					inputParam.getSid(),
					inputParam.getDatePeriod(),
					inputParam.isMode(),
					inputParam.getBaseDate(),
					true,
					interimMngBreakDayOff, 
					breakMng, 
					dayOffMng,
					Optional.empty(),
					Optional.of(CreateAtr.APPBEFORE),
					Optional.of(inputParam.getRegisterDate()));
			BreakDayOffRemainMngOfInPeriod remainMng = breakDayOffMngService.getBreakDayOffMngInPeriod(mngParam);
			if(!remainMng.getLstError().isEmpty()) {
				outputData.setChkSubHoliday(true);
			}
		}
		//振休不足区分をチェックする
		if(inputParam.isChkPause()) {
			//振出振休残数を取得する
			AbsRecMngInPeriodParamInput mngParam = new AbsRecMngInPeriodParamInput(inputParam.getCid(),
					inputParam.getSid(),
					inputParam.getDatePeriod(),
					inputParam.getBaseDate(),
					inputParam.isMode(),
					true,
					useAbsMng,
					interimMngAbsRec,
					useRecMng,
					Optional.empty(),
					Optional.of(CreateAtr.APPBEFORE),
					Optional.of(inputParam.getRegisterDate()));
			AbsRecRemainMngOfInPeriod remainMng = absRecMngService.getAbsRecMngInPeriod(mngParam);
			if(!remainMng.getPError().isEmpty()) {
				outputData.setChkPause(true);
			}
		}
		//特休チェック区分をチェックする
		if(inputParam.isChkSpecial() && !specialHolidayData.isEmpty()) {
			//暫定残数管理データ(output)に「特別休暇暫定データ」が存在するかチェックする
			for (InterimSpecialHolidayMng a : specialHolidayData) {
				List<InterimRemain> interimSpecialChk = interimSpecial.stream()
						.filter(c -> c.getRemainManaID().equals(a.getSpecialHolidayId())).collect(Collectors.toList());
				ComplileInPeriodOfSpecialLeaveParam speParam = new ComplileInPeriodOfSpecialLeaveParam(inputParam.getCid(),
						inputParam.getSid(),
						inputParam.getDatePeriod(),
						inputParam.isMode(),
						!interimSpecialChk.isEmpty() ? interimSpecialChk.get(0).getYmd() : inputParam.getBaseDate(),
						a.getSpecialHolidayCode(),
						false,
						true,
						interimSpecial,
						specialHolidayData,
						Optional.empty());
				InPeriodOfSpecialLeave speOutCheck = speLeaveSevice.complileInPeriodOfSpecialLeave(speParam).getAggSpecialLeaveResult();
				if(!speOutCheck.getLstError().isEmpty()) {
					outputData.setChkSpecial(true);
					break;
				}				
			}
		}
		//年休チェック区分をチェックする
		List<TmpAnnualLeaveMngWork> mngWork = new ArrayList<>();
		if(inputParam.isChkAnnual()) {
			mngWork = annualHolidayData.stream()
					.map(o -> {
						InterimRemain annualInterim = annualMng.stream().filter(a -> a.getRemainManaID() == o.getAnnualId())
								.collect(Collectors.toList()).get(0);
						return TmpAnnualLeaveMngWork.of(annualInterim, o);
					}).collect(Collectors.toList());
			List<AnnualLeaveErrorSharedImport> lstError = annualService.annualLeaveErrors(inputParam.getCid(),
					inputParam.getSid(),
					inputParam.getDatePeriod(),
					inputParam.isMode(),
					inputParam.getBaseDate(),
					false,
					false, Optional.of(true),
					Optional.of(mngWork),
					Optional.empty());
			for (AnnualLeaveErrorSharedImport errorcheck : lstError) {
				if(errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_AL_OF_UNIT_DAY_AFT_GRANT
						|| errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_AL_OF_UNIT_DAY_BFR_GRANT
						|| errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_TIMEAL_AFTER_GRANT
						|| errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_TIMEAL_BEFORE_GRANT) {
					outputData.setChkAnnual(true);
					break;
				}
			}
		}
		//期間中の年休積休残数を取得
		if(inputParam.isChkFundingAnnual()) {
			List<TmpReserveLeaveMngWork> lstReserve = resereLeaveData.stream()
					.map(l -> {
						InterimRemain reserveInterim = resereMng.stream().filter(a -> a.getRemainManaID() == l.getResereId())
								.collect(Collectors.toList()).get(0);
						return TmpReserveLeaveMngWork.of(reserveInterim, l);
					}).collect(Collectors.toList());
			List<ReserveLeaveErrorImport> reserveLeaveErrors = annualService.reserveLeaveErrors(inputParam.getCid(),
					inputParam.getSid(),
					inputParam.getDatePeriod(), 
					inputParam.isMode(), 
					inputParam.getBaseDate(), 
					false, false, Optional.of(true), 
					Optional.of(mngWork), 
					Optional.of(lstReserve), Optional.empty(),
					Optional.empty());
			for (ReserveLeaveErrorImport errorCheck : reserveLeaveErrors) {
				if(errorCheck == ReserveLeaveErrorImport.SHORTAGE_RSVLEA_AFTER_GRANT
						|| errorCheck == ReserveLeaveErrorImport.SHORTAGE_RSVLEA_BEFORE_GRANT) {
					outputData.setChkFundingAnnual(true);
					break;
				}
			}
		}

		return outputData;
	}
	@Override
	public InterimEachData interimInfor(Map<GeneralDate, DailyInterimRemainMngData> mapDataOutput) {
		/**
		 * 振休か振出の暫定残数管理
		 */
		List<InterimRemain> interimMngAbsRec = new ArrayList<>();
		List<InterimAbsMng> useAbsMng = new ArrayList<>();
		List<InterimRecMng> useRecMng = new ArrayList<>();
		/**
		 * 休出か代休の暫定残数管理
		 */
		List<InterimRemain> interimMngBreakDayOff = new ArrayList<>();
		List<InterimBreakMng> breakMng = new ArrayList<>();
		List<InterimDayOffMng> dayOffMng = new ArrayList<>();
		/**
		 * 特別休暇の暫定残数管理
		 */
		List<InterimRemain> interimSpecial = new ArrayList<>();
		List<InterimSpecialHolidayMng> specialHolidayData = new ArrayList<>();
		/**
		 * 年休の暫定残数管理
		 */
		List<InterimRemain> annualMng = new ArrayList<>();
		List<TmpAnnualHolidayMng> annualHolidayData = new ArrayList<>();
		/**
		 * 積立年休の暫定残数管理
		 */
		List<InterimRemain> resereMng = new ArrayList<>();
		List<TmpResereLeaveMng> resereLeaveData = new ArrayList<>();
		mapDataOutput.forEach((x,y) -> {
			//積立年休
			y.getResereData().ifPresent(z -> {
				resereLeaveData.add(z);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream().filter(w -> w.getRemainManaID().equals(z.getResereId()))
						.collect(Collectors.toList());
				for (InterimRemain mngData : lstTmp) {
					resereMng.add(mngData);
				}
				
			});
			//年休
			y.getAnnualHolidayData().ifPresent(z -> {
				annualHolidayData.add(z);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream().filter(w -> w.getRemainManaID().equals(z.getAnnualId()))
						.collect(Collectors.toList());
				for (InterimRemain mngData : lstTmp) {
					annualMng.add(mngData);
				}
			});
			//特別休暇
			y.getSpecialHolidayData().stream().forEach(a -> {
				specialHolidayData.add(a);
				List<InterimRemain> interimMngSpe = y.getRecAbsData().stream()
						.filter(b -> b.getRemainManaID().equals(a.getSpecialHolidayId()))
						.collect(Collectors.toList());
				if(!interimMngSpe.isEmpty()) {
					interimSpecial.addAll(y.getRecAbsData());
				}
			});
			
			//休出代休
			y.getBreakData().ifPresent(z -> {
				breakMng.add(z);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream().filter(a -> z.getBreakMngId().equals(a.getRemainManaID()))
				.collect(Collectors.toList());
				interimMngBreakDayOff.addAll(lstTmp);
			});
			y.getDayOffData().ifPresent(a -> {
				dayOffMng.add(a);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream().filter(b -> b.getRemainManaID().equals(a.getDayOffManaId()))
						.collect(Collectors.toList());
				interimMngBreakDayOff.addAll(lstTmp);
			});
			//振出振休
			y.getRecData().ifPresent(b -> {
				useRecMng.add(b);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream().filter(a -> b.getRecruitmentMngId().equals(a.getRemainManaID()))
						.collect(Collectors.toList());
				interimMngAbsRec.addAll(lstTmp);
			});
			y.getInterimAbsData().ifPresent(c -> {
				useAbsMng.add(c);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream().filter(a -> c.getAbsenceMngId().equals(a.getRemainManaID()))
						.collect(Collectors.toList());
				interimMngAbsRec.addAll(lstTmp);
			});
		});
		return new InterimEachData(interimMngAbsRec,
				useAbsMng,
				useRecMng,
				interimMngBreakDayOff,
				breakMng, dayOffMng,
				interimSpecial,
				specialHolidayData,
				annualMng,
				annualHolidayData,
				resereMng,
				resereLeaveData);
	}

}
