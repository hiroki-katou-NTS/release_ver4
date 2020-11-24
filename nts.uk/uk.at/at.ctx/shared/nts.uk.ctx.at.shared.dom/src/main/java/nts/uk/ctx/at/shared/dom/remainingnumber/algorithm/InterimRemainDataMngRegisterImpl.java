package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbasMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakDayOffMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;

@Stateless
public class InterimRemainDataMngRegisterImpl implements InterimRemainDataMngRegister{
	@Inject
	private InterimRemainOffPeriodCreateData periodCreateData;
	@Inject
	private InterimRemainRepository interimRemainRepos;
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
	public void registryInterimDataMng(InterimRemainCreateDataInputPara inputData, CompanyHolidayMngSetting comHolidaySetting) {
		//指定期間の暫定残数管理データを作成する
		Map<GeneralDate, DailyInterimRemainMngData> interimDataMng = periodCreateData.createInterimRemainDataMng(inputData, comHolidaySetting);
		List<GeneralDate> lstInterimDate = new ArrayList<>();
		interimDataMng.forEach((x, y) -> {
			lstInterimDate.add(x);
		});
		if(lstInterimDate.isEmpty()) {
			return;
		}
//		List<InterimRemain> lstBeforInterimDataAlls = interimRemainRepos.getDataBySidDates(inputData.getSid(), lstInterimDate);
		interimDataMng.forEach((x, y) -> {
			//ドメインモデル「暫定残数管理データ」を取得する
//			List<InterimRemain> lstBeforInterimData = lstBeforInterimDataAlls.stream()
//					.filter(z -> z.getYmd().equals(x)).collect(Collectors.toList());
			List<InterimRemain> lstBeforInterimData = new ArrayList<>();
			List<InterimRemain> lstInterimData = y.getRecAbsData();
			RegistryInterimResereLeaveDataInput dataInput = new RegistryInterimResereLeaveDataInput();
			dataInput.setCid(inputData.getCid());
			dataInput.setBaseDate(x);
			dataInput.setEarchData(y);
			dataInput.setLstBeforeData(lstBeforInterimData);
			dataInput.setLstCreateData(lstInterimData);
			dataInput.setSid(inputData.getSid());
			//暫定年休データの登録
			dataInput.setRemainType(RemainType.ANNUAL);
			this.registryInterimResereLeave(dataInput);
			//暫定積立年休データの登録
			dataInput.setRemainType(RemainType.FUNDINGANNUAL);
			this.registryInterimResereLeave(dataInput);
			//暫定特休データの登録
			dataInput.setRemainType(RemainType.SPECIAL);
			this.registryInterimResereLeave(dataInput);
			//暫定振休データの登録
			dataInput.setRemainType(RemainType.PAUSE);
			this.registryInterimResereLeave(dataInput);
			//暫定振出データの登録
			dataInput.setRemainType(RemainType.PICKINGUP);
			this.registryInterimResereLeave(dataInput);
			//暫定代休データの登録
			dataInput.setRemainType(RemainType.SUBHOLIDAY);
			this.registryInterimResereLeave(dataInput);
			//暫定休出データの登録
			dataInput.setRemainType(RemainType.BREAK);
			this.registryInterimResereLeave(dataInput);
		});

	}

	private void registryInterimResereLeave(RegistryInterimResereLeaveDataInput dataInput) {
		List<InterimRemain> lstInterimDataBefore = dataInput.getLstBeforeData().stream()
				.filter(x -> x.getRemainType() == dataInput.getRemainType())
				.collect(Collectors.toList());
		List<InterimRemain> lstAnnualHolidayCreate = dataInput.getLstCreateData().stream()
				.filter(x -> x.getRemainType() == dataInput.getRemainType())
				.collect(Collectors.toList());
		//INPUT．暫定残数管理データ(新作成)がない、INPUT．暫定残数管理データ(DB既存)がない
//		if(lstInterimDataBefore.isEmpty()
//				&& lstAnnualHolidayCreate.isEmpty()) {
//			return;
//		}
		if(lstAnnualHolidayCreate.isEmpty()) {
			return;
		}
		DailyInterimRemainMngData earchData = dataInput.getEarchData();


//		//INPUT．暫定残数管理データ(新作成)がある、INPUT．暫定残数管理データ(DB既存)がある
//		if(!lstAnnualHolidayCreate.isEmpty()
//				&& !lstInterimDataBefore.isEmpty()) {
//			//Updateする
//			InterimRemain interimDataBefore = lstInterimDataBefore.get(0);
//			InterimRemain interimDataCreate = lstAnnualHolidayCreate.get(0);
//			String mngId = interimDataBefore.getRemainManaID();
//			interimDataCreate.setRemainManaID(mngId);
//			interimRemainRepos.persistAndUpdateInterimRemain(interimDataCreate);
//			switch (dataInput.getRemainType()) {
//			case ANNUAL:
//				TmpAnnualHolidayMng annualHolidayDataCreate = earchData.getAnnualHolidayData().get();
//				annualHolidayDataCreate.setAnnualId(mngId);
//				annualHolidayMngRepos.persistAndUpdate(annualHolidayDataCreate);
//				break;
//			case FUNDINGANNUAL:
//				TmpResereLeaveMng resereDataCreate = earchData.getResereData().get();
//				resereDataCreate.setResereId(mngId);
//				resereLeave.persistAndUpdate(resereDataCreate);
//				break;
//			case PAUSE:
//				InterimAbsMng absDataCreate = earchData.getInterimAbsData().get();
//				absDataCreate.setAbsenceMngId(mngId);
//				recAbsRepos.persistAndUpdateInterimAbsMng(absDataCreate);
//				break;
//			case PICKINGUP:
//				InterimRecMng recDataCreate = earchData.getRecData().get();
//				recDataCreate.setRecruitmentMngId(mngId);
//				recAbsRepos.persistAndUpdateInterimRecMng(recDataCreate);
//				break;
//			case SUBHOLIDAY:
//				InterimDayOffMng dayOffDataCreate = earchData.getDayOffData().get();
//				dayOffDataCreate.setDayOffManaId(mngId);
//				breakDayOffRepos.persistAndUpdateInterimDayOffMng(dayOffDataCreate);
//				break;
//			case BREAK:
//				InterimBreakMng breakDataCreate = earchData.getBreakData().get();
//				breakDataCreate.setBreakMngId(mngId);
//				breakDayOffRepos.persistAndUpdateInterimBreakMng(breakDataCreate);
//				break;
//			case SPECIAL:
//				earchData.getSpecialHolidayData().forEach(x -> {
//					x.setSpecialHolidayId(mngId);
//					specialHoliday.persistAndUpdateInterimSpecialHoliday(x);
//				});
//				break;
//			default:
//				break;
//			}
//		}




		//暫定残数管理データ(新作成)がある、INPUT．暫定残数管理データ(DB既存)がない
//		if(!lstAnnualHolidayCreate.isEmpty()
//				&& lstInterimDataBefore.isEmpty()) {
			//Insertする
			InterimRemain interimCreate = lstAnnualHolidayCreate.get(0);
			interimRemainRepos.persistAndUpdateInterimRemain(interimCreate);
			switch (dataInput.getRemainType()) {
			case ANNUAL:
				TmpAnnualHolidayMng annualHolidayDataCreate = earchData.getAnnualHolidayData().get();
				annualHolidayMngRepos.persistAndUpdate(annualHolidayDataCreate);
				break;
			case FUNDINGANNUAL:
				TmpResereLeaveMng resereDataCreate = earchData.getResereData().get();
				resereLeave.persistAndUpdate(resereDataCreate);
				break;
			case PAUSE:
				InterimAbsMng absDataCreate = earchData.getInterimAbsData().get();
				recAbsRepos.persistAndUpdateInterimAbsMng(absDataCreate);
				break;
			case PICKINGUP:
				InterimRecMng recDataCreate = earchData.getRecData().get();
				recAbsRepos.persistAndUpdateInterimRecMng(recDataCreate);
				break;
			case SUBHOLIDAY:
				InterimDayOffMng dayOffDataCreate = earchData.getDayOffData().get();
				breakDayOffRepos.persistAndUpdateInterimDayOffMng(dayOffDataCreate);
				break;
			case BREAK:
				InterimBreakMng breakDataCreate = earchData.getBreakData().get();
				breakDayOffRepos.persistAndUpdateInterimBreakMng(breakDataCreate);
				break;
			case SPECIAL:
				earchData.getSpecialHolidayData().forEach(x -> {
					specialHoliday.persistAndUpdateInterimSpecialHoliday(x);
				});
				break;
			default:
				break;
			}
//		}


//		//暫定残数管理データ(新作成)がない、INPUT．暫定残数管理データ(DB既存)がある
//		if(lstAnnualHolidayCreate.isEmpty()
//				&& !lstInterimDataBefore.isEmpty()) {
//			InterimRemain annualHolidayRemainBefore = lstInterimDataBefore.get(0);
//			String mngId = annualHolidayRemainBefore.getRemainManaID();
//			interimRemainRepos.deleteById(mngId);
//			//Delete
//			switch (dataInput.getRemainType()) {
//			case ANNUAL:
//				annualHolidayMngRepos.deleteById(mngId);
//				break;
//			case FUNDINGANNUAL:
//				resereLeave.deleteById(mngId);
//				break;
//			case PAUSE:
//				recAbsRepos.deleteInterimAbsMng(mngId);
//				break;
//			case PICKINGUP:
//				recAbsRepos.deleteInterimRecMng(mngId);
//				break;
//			case SUBHOLIDAY:
//				breakDayOffRepos.deleteInterimDayOffMng(mngId);
//				break;
//			case BREAK:
//				breakDayOffRepos.deleteInterimBreakMng(mngId);
//				break;
//			case SPECIAL:
//				specialHoliday.deleteSpecialHoliday(mngId);
//				break;
//			default:
//				break;
//			}
//		}
	}

}
