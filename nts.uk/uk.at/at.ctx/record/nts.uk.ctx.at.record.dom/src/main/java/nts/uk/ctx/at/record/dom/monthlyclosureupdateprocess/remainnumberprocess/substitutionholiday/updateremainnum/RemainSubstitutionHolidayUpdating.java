package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.substitutionholiday.updateremainnum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecDetailPara;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.UnOffsetOfAbs;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.UnUseOfRec;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.HolidayAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.ManagementDataDaysAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.ManagementDataRemainUnit;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementData;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.PayoutManagementDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManaDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author HungTT - <<Work>> 振休残数更新
 *
 */

@Stateless
public class RemainSubstitutionHolidayUpdating {

	@Inject
	private PayoutManagementDataRepository payoutMngDataRepo;

	@Inject
	private SubstitutionOfHDManaDataRepository substitutionMngDataRepo;
	

	// 振休残数更新
	public void updateRemainSubstitutionHoliday(List<AbsRecDetailPara> lstAbsRecMng,AggrPeriodEachActualClosure period, String empId) {
		String companyId = AppContexts.user().companyId();
		this.deletePayoutManaData(period.getPeriod(), empId);
		this.updatePayoutMngData(companyId, lstAbsRecMng);
		this.deleteSubManaData(period.getPeriod(), empId);
		this.updateSubstitutionHolidayMngData(companyId, lstAbsRecMng);
	}
	
	//ドメインモデル 「振出管理データ」 を削除する
	public void deletePayoutManaData(DatePeriod period, String empId){
		List<PayoutManagementData> managementDatas = payoutMngDataRepo.getByHoliday(empId, false, period);
		if (CollectionUtil.isEmpty(managementDatas))
			return;
		List<String> payoutId = managementDatas.stream().map(r -> r.getPayoutId()).collect(Collectors.toList());
		payoutMngDataRepo.deleteById(payoutId);
	}
	
	//ドメインモデル 「振休管理データ」 を削除する
	public void deleteSubManaData(DatePeriod period, String empId){
		List<SubstitutionOfHDManagementData> ofHDManagementDatas = substitutionMngDataRepo.getByHoliday(empId, false, period);
		if (CollectionUtil.isEmpty(ofHDManagementDatas))
			return;
		List<String> subOfHDID = ofHDManagementDatas.stream().map(r -> r.getSubOfHDID()).collect(Collectors.toList());
		substitutionMngDataRepo.deleteById(subOfHDID);
	}

	// 振出管理データの更新
	private void updatePayoutMngData(String companyId, List<AbsRecDetailPara> lstAbsRecMng) {
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		lstAbsRecMng = lstAbsRecMng.stream().filter(a -> a.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE)
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		for (AbsRecDetailPara data : lstAbsRecMng) {
			Optional<UnUseOfRec> optUnUseOfRec = data.getUnUseOfRec();
			if (!optUnUseOfRec.isPresent())
				continue;
			UnUseOfRec unUseOfRec = optUnUseOfRec.get();
			Optional<PayoutManagementData> optPayout = payoutMngDataRepo.findByID(unUseOfRec.getRecMngId());
			if (optPayout.isPresent()) {
				// update
				PayoutManagementData payoutDataOld = optPayout.get();
				PayoutManagementData payoutData = new PayoutManagementData(payoutDataOld.getPayoutId(),
						payoutDataOld.getCID(), payoutDataOld.getSID(), payoutDataOld.getPayoutDate(),
						unUseOfRec.getExpirationDate(),
						EnumAdaptor.valueOf(unUseOfRec.getStatutoryAtr().value, HolidayAtr.class),
						new ManagementDataDaysAtr(unUseOfRec.getOccurrenceDays()),
						new ManagementDataRemainUnit(unUseOfRec.getUnUseDays()), unUseOfRec.getDigestionAtr(),
						unUseOfRec.getDisappearanceDate());
				payoutMngDataRepo.update(payoutData);
			} else {
				// insert
				PayoutManagementData payoutData = new PayoutManagementData(unUseOfRec.getRecMngId(), companyId,
						data.getSid(), data.getYmdData(), unUseOfRec.getExpirationDate(),
						EnumAdaptor.valueOf(unUseOfRec.getStatutoryAtr().value, HolidayAtr.class),
						new ManagementDataDaysAtr(unUseOfRec.getOccurrenceDays()),
						new ManagementDataRemainUnit(unUseOfRec.getUnUseDays()), unUseOfRec.getDigestionAtr(),
						unUseOfRec.getDisappearanceDate());
				payoutMngDataRepo.create(payoutData);
			}
		}
	}

	// 振休管理データの更新
	private void updateSubstitutionHolidayMngData(String companyId, List<AbsRecDetailPara> lstAbsRecMng) {
		//大塚カスタマイズ　振休残数がマイナスの場合でも、常にクリアする。
		boolean isOotsuka = true;
		
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		lstAbsRecMng = lstAbsRecMng.stream().filter(a -> a.getOccurrentClass() == OccurrenceDigClass.DIGESTION)
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		for (AbsRecDetailPara data : lstAbsRecMng) {
			Optional<UnOffsetOfAbs> optUnOffsetOfAb = data.getUnOffsetOfAb();
			if (!optUnOffsetOfAb.isPresent())
				continue;
			UnOffsetOfAbs unOffsetOfAb = optUnOffsetOfAb.get();
			Optional<SubstitutionOfHDManagementData> optSubData = substitutionMngDataRepo
					.findByID(unOffsetOfAb.getAbsMngId());
			if (optSubData.isPresent()) {
				// update
				SubstitutionOfHDManagementData substitutionData = optSubData.get();
				substitutionData.setRequiredDays(new ManagementDataDaysAtr(unOffsetOfAb.getRequestDays()));
				substitutionData.setRemainsDay(isOotsuka ? 0 : unOffsetOfAb.getUnOffSetDays());
				substitutionMngDataRepo.update(substitutionData);
			} else {
				// insert
				SubstitutionOfHDManagementData substitutionData = new SubstitutionOfHDManagementData(
						unOffsetOfAb.getAbsMngId(), companyId, data.getSid(), data.getYmdData(),
						new ManagementDataDaysAtr(unOffsetOfAb.getRequestDays()),
						new ManagementDataRemainUnit(isOotsuka ? 0 : unOffsetOfAb.getUnOffSetDays()));
				substitutionMngDataRepo.create(substitutionData);
			}
		}
	}

}
