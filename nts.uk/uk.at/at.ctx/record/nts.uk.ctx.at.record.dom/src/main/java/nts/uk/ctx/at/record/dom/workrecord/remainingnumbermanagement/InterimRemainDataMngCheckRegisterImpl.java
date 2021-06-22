package nts.uk.ctx.at.record.dom.workrecord.remainingnumbermanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.AggrResultOfChildCareNurse;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseRequireImplFactory;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.GetRemainingNumberChildCareService;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.NumberCompensatoryLeavePeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.param.AbsRecMngInPeriodRefactParamInput;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.algorithm.param.CompenLeaveAggrResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.EarchInterimRemainCheck;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimEachData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainCheckInputParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainCreateDataInputPara;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffPeriodCreateData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.require.RemainNumberTempRequireService;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TempAnnualLeaveMngs;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.NumberRemainVacationLeaveRangeProcess;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.BreakDayOffRemainMngRefactParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.TempChildCareManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.AnnualLeaveErrorSharedImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.GetAnnLeaRemNumWithinPeriodSharedImport;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.ReserveLeaveErrorImport;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.breakinfo.FixedManagementDataMonth;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
//import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.ComplileInPeriodOfSpecialLeaveParam;

@Stateless
public class InterimRemainDataMngCheckRegisterImpl implements InterimRemainDataMngCheckRegister {

	@Inject
	private ComSubstVacationRepository subRepos;
	@Inject
	private CompensLeaveComSetRepository leaveSetRepos;
	@Inject
	private GetAnnLeaRemNumWithinPeriodSharedImport annualService;
//	@Inject
//	private ComplileInPeriodOfSpecialLeaveSharedImport specialLeaveService;

	/** REQUIRE対応 */
	@Inject
	private RemainNumberTempRequireService requireService;

	@Inject
	private NumberRemainVacationLeaveRangeProcess numberRemainVacationLeaveRangeProcess;
	
	@Inject
    private GetRemainingNumberChildCareService getRemainingNumberChildCareService;
	
	@Inject
    private ChildCareNurseRequireImplFactory childCareNurseRequireImplFactory;

	@Override
	public EarchInterimRemainCheck checkRegister(InterimRemainCheckInputParam inputParam) {
		// 代休不足区分、振休不足区分、年休不足区分、積休不足区分、特休不足区分、公休不足区分、超休不足区分、子の看護不足区分、介護不足区分をfalseにする(初期化) 
		EarchInterimRemainCheck outputData = new EarchInterimRemainCheck(false, false, false, false, false, false,
				false, false, false);
		Optional<ComSubstVacation> comSetting = subRepos.findById(inputParam.getCid());
		CompensatoryLeaveComSetting leaveComSetting = leaveSetRepos.find(inputParam.getCid());
		CompanyHolidayMngSetting comHolidaySetting = new CompanyHolidayMngSetting(inputParam.getCid(), comSetting,
				leaveComSetting);
		// 暫定管理データをメモリ上で作成する
		Map<GeneralDate, DailyInterimRemainMngData> mapDataOutput = new HashMap<>();

		val require = requireService.createRequire();
		CacheCarrier cacheCarrier = new CacheCarrier();

		inputParam.getAppData().stream().forEach(x -> {
			DatePeriod dateData = inputParam.getRegisterDate();
			if (x.getAppType() == ApplicationType.COMPLEMENT_LEAVE_APPLICATION) {
				dateData = new DatePeriod(x.getAppDate(), x.getAppDate());
			}
			InterimRemainCreateDataInputPara dataCreate = new InterimRemainCreateDataInputPara(inputParam.getCid(),
					inputParam.getSid(), dateData, inputParam.getRecordData(), inputParam.getScheData(),
					inputParam.getAppData(), false);
			Map<GeneralDate, DailyInterimRemainMngData> mapDataOutputTmp = InterimRemainOffPeriodCreateData
					.createInterimRemainDataMng(require, cacheCarrier, dataCreate, comHolidaySetting);
			// 振休申請は取り消しになる時を対応します。
			if (x.getAppType() == ApplicationType.COMPLEMENT_LEAVE_APPLICATION
					&& mapDataOutputTmp.containsKey(x.getAppDate())) {
				DailyInterimRemainMngData data = mapDataOutputTmp.get(x.getAppDate());
				if (data.getRecAbsData().isEmpty()) {
					String mngId = IdentifierUtil.randomUniqueId();
					InterimAbsMng furikyuSinsei = new InterimAbsMng(mngId, inputParam.getSid(), x.getAppDate(),
							CreateAtr.APPBEFORE, RemainType.PAUSE, new RequiredDay(0.0), new UnOffsetDay(0.0));
					data.getRecAbsData().add(furikyuSinsei);
					data.setInterimAbsData(Optional.of(furikyuSinsei));
					mapDataOutputTmp.remove(x.getAppDate());
					mapDataOutputTmp.put(x.getAppDate(), data);
				}
			}
			mapDataOutputTmp.forEach((z, y) -> {
				if (!mapDataOutput.containsKey(z)) {
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
		List<TempAnnualLeaveMngs> annualHolidayData = eachData.getAnnualHolidayData();
		List<TmpResereLeaveMng> resereLeaveData = eachData.getResereLeaveData();

		// 代休チェック区分をチェックする
		if (inputParam.isChkSubHoliday()) {

			// 期間内の休出代休残数を取得する
			BreakDayOffRemainMngRefactParam inputParamBreak =  new BreakDayOffRemainMngRefactParam(
					inputParam.getCid(), inputParam.getSid(),
					inputParam.getDatePeriod(),
					inputParam.isMode(),
					inputParam.getBaseDate(),
					true,
					interimMngBreakDayOff,
					Optional.of(CreateAtr.APPBEFORE),
					Optional.of(inputParam.getRegisterDate()),
					breakMng,
					dayOffMng,
					Optional.empty(), new FixedManagementDataMonth());
			val remainMng = numberRemainVacationLeaveRangeProcess.getBreakDayOffMngInPeriod(inputParamBreak);
			if (!remainMng.getDayOffErrors().isEmpty()) {
				outputData.setChkSubHoliday(true);
			}
		}
		// 振休不足区分をチェックする
		if (inputParam.isChkPause()) {
			// 振出振休残数を取得する
			val mngParam = new AbsRecMngInPeriodRefactParamInput(inputParam.getCid(),
					inputParam.getSid(), inputParam.getDatePeriod(), inputParam.getBaseDate(), inputParam.isMode(),
					true,
					useAbsMng, interimMngAbsRec, useRecMng,
					Optional.empty(),
					Optional.of(CreateAtr.APPBEFORE),
					Optional.of(inputParam.getRegisterDate()),
					new FixedManagementDataMonth());
			CompenLeaveAggrResult remainMng = NumberCompensatoryLeavePeriodQuery.process(require, mngParam);
			if (!remainMng.getPError().isEmpty()) {
				outputData.setChkPause(true);
			}
		}
		// 特休チェック区分をチェックする
		if (inputParam.isChkSpecial() && !specialHolidayData.isEmpty()) {
			// 暫定残数管理データ(output)に「特別休暇暫定データ」が存在するかチェックする
			for (InterimSpecialHolidayMng a : specialHolidayData) {
				List<InterimRemain> interimSpecialChk = interimSpecial.stream()
						.filter(c -> c.getRemainManaID().equals(a.getRemainManaID())).collect(Collectors.toList());

//				List<SpecialLeaveError> specialErros = specialLeaveService.complileInPeriodOfSpecialLeave(
//						require, cacheCarrier, inputParam.getCid(), inputParam.getSid(), inputParam.getDatePeriod(), inputParam.isMode(),
//						!interimSpecialChk.isEmpty() ? interimSpecialChk.get(0).getYmd() : inputParam.getBaseDate(),
//						a.getSpecialHolidayCode(), false, true, interimSpecial, specialHolidayData);
//
//				if (!specialErros.isEmpty()) {
//					outputData.setChkSpecial(true);
//					break;
//				}
			}
		}
		// 年休チェック区分をチェックする
		List<TempAnnualLeaveMngs> mngWork = new ArrayList<>();
		if (inputParam.isChkAnnual()) {
			mngWork = annualHolidayData;
			List<AnnualLeaveErrorSharedImport> lstError = annualService.annualLeaveErrors(inputParam.getCid(),
					inputParam.getSid(), inputParam.getDatePeriod(), inputParam.isMode(), inputParam.getBaseDate(),
					false, false, Optional.of(true), Optional.of(mngWork), Optional.empty());
			for (AnnualLeaveErrorSharedImport errorcheck : lstError) {
				if (errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_AL_OF_UNIT_DAY_AFT_GRANT
						|| errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_AL_OF_UNIT_DAY_BFR_GRANT
						|| errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_TIMEAL_AFTER_GRANT
						|| errorcheck == AnnualLeaveErrorSharedImport.SHORTAGE_TIMEAL_BEFORE_GRANT) {
					outputData.setChkAnnual(true);
					break;
				}
			}
		}
		// 期間中の年休積休残数を取得
		if (inputParam.isChkFundingAnnual()) {
			List<TmpResereLeaveMng> lstReserve = resereLeaveData;
			List<ReserveLeaveErrorImport> reserveLeaveErrors = annualService.reserveLeaveErrors(inputParam.getCid(),
					inputParam.getSid(), inputParam.getDatePeriod(), inputParam.isMode(), inputParam.getBaseDate(),
					false, false, Optional.of(true), Optional.of(mngWork), Optional.of(lstReserve), Optional.empty(),
					Optional.empty());
			for (ReserveLeaveErrorImport errorCheck : reserveLeaveErrors) {
				if (errorCheck == ReserveLeaveErrorImport.SHORTAGE_RSVLEA_AFTER_GRANT
						|| errorCheck == ReserveLeaveErrorImport.SHORTAGE_RSVLEA_BEFORE_GRANT) {
					outputData.setChkFundingAnnual(true);
					break;
				}
			}
		}
		// 子の看護チェック区分をチェックする
		if (inputParam.isChkChildNursing()) {
		    // [NO.206]期間中の子の看護休暇残数を取得
		    AggrResultOfChildCareNurse result =
	                getRemainingNumberChildCareService.getChildCareRemNumWithinPeriod(
	                        inputParam.getCid(), 
	                        inputParam.getSid(), 
	                        inputParam.getDatePeriod(), 
	                        InterimRemainMngMode.of(inputParam.isMode()), 
	                        inputParam.getBaseDate(),
	                        Optional.of(true), 
	                        new ArrayList<TempChildCareManagement>(), // ????
	                        Optional.empty(), 
	                        Optional.empty(),  // confirm???
	                        Optional.of(inputParam.getRegisterDate()),
	                        cacheCarrier, 
	                        childCareNurseRequireImplFactory.createRequireImpl());
		}
		// 介護チェック区分をチェックする
		if (inputParam.isChkLongTermCare()) {
		    // TODO: Call RQ 207: [NO.207]期間中の介護休暇残数を取得
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
		List<TempAnnualLeaveMngs> annualHolidayData = new ArrayList<>();
		/**
		 * 積立年休の暫定残数管理
		 */
		List<InterimRemain> resereMng = new ArrayList<>();
		List<TmpResereLeaveMng> resereLeaveData = new ArrayList<>();
		mapDataOutput.forEach((x, y) -> {
			// 積立年休
			y.getResereData().ifPresent(z -> {
				resereLeaveData.add(z);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream()
						.filter(w -> w.getRemainManaID().equals(z.getRemainManaID()) && w.getRemainType() == RemainType.FUNDINGANNUAL)
						.collect(Collectors.toList());
				for (InterimRemain mngData : lstTmp) {
					resereMng.add(mngData);
				}

			});
			// 年休
			y.getAnnualHolidayData().forEach(c->annualMng.add(c));
			// 特別休暇
			y.getSpecialHolidayData().forEach(c->interimSpecial.add(c));
			// 休出
			y.getBreakData().ifPresent(z -> {
				breakMng.add(z);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream()
						.filter(a -> z.getRemainManaID().equals(a.getRemainManaID())).collect(Collectors.toList());
				interimMngBreakDayOff.addAll(lstTmp);
			});
			//代休
			y.getDayOffData().forEach(c->interimMngBreakDayOff.add(c));
			// 振出
			y.getRecData().ifPresent(b -> {
				useRecMng.add(b);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream()
						.filter(a -> b.getRemainManaID().equals(a.getRemainManaID())).collect(Collectors.toList());
				interimMngAbsRec.addAll(lstTmp);
			});
			//振休
			y.getInterimAbsData().ifPresent(c -> {
				useAbsMng.add(c);
				List<InterimRemain> lstTmp = y.getRecAbsData().stream()
						.filter(a -> c.getRemainManaID().equals(a.getRemainManaID())).collect(Collectors.toList());
				interimMngAbsRec.addAll(lstTmp);
			});
		});
		return new InterimEachData(interimMngAbsRec, useAbsMng, useRecMng, interimMngBreakDayOff, breakMng, dayOffMng,
				interimSpecial, specialHolidayData, annualMng, annualHolidayData, resereMng, resereLeaveData);
	}

}
