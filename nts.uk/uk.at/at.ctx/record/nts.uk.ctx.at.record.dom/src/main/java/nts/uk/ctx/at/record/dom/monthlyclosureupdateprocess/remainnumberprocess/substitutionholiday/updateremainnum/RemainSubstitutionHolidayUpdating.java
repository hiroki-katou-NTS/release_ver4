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
	public void updateRemainSubstitutionHoliday(AggrPeriodEachActualClosure period, String empId,
			List<AbsRecDetailPara> lstAbsRecMng) {
		this.updatePayoutMngData(period, empId, lstAbsRecMng);
		this.updateSubstitutionHolidayMngData(period, empId, lstAbsRecMng);
	}

	// 振出管理データの更新
	private void updatePayoutMngData(AggrPeriodEachActualClosure period, String empId,
			List<AbsRecDetailPara> lstAbsRecMng) {
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		lstAbsRecMng = lstAbsRecMng.stream().filter(a -> a.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE)
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		String companyId = AppContexts.user().companyId();
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
	private void updateSubstitutionHolidayMngData(AggrPeriodEachActualClosure period, String empId,
			List<AbsRecDetailPara> lstAbsRecMng) {
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		lstAbsRecMng = lstAbsRecMng.stream().filter(a -> a.getOccurrentClass() == OccurrenceDigClass.DIGESTION)
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(lstAbsRecMng))
			return;
		String companyId = AppContexts.user().companyId();
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
				substitutionData.setRemainsDay(unOffsetOfAb.getUnOffSetDays());
				substitutionMngDataRepo.update(substitutionData);
			} else {
				// insert
				SubstitutionOfHDManagementData substitutionData = new SubstitutionOfHDManagementData(
						unOffsetOfAb.getAbsMngId(), companyId, data.getSid(), data.getYmdData(),
						new ManagementDataDaysAtr(unOffsetOfAb.getRequestDays()),
						new ManagementDataRemainUnit(unOffsetOfAb.getUnOffSetDays()));
				substitutionMngDataRepo.create(substitutionData);
			}
		}
	}

}
