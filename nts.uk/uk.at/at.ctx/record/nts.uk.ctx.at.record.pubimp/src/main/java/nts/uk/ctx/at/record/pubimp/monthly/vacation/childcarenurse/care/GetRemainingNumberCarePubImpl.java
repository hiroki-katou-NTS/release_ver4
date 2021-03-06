package nts.uk.ctx.at.record.pubimp.monthly.vacation.childcarenurse.care;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.care.GetRemainingNumberCareService;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.AggrResultOfChildCareNurse;
import nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseRequireImplFactory;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseAggrPeriodDaysInfoExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseAggrPeriodInfoExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseErrorsExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNursePeriodExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseRemainingNumberExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseStartdateDaysInfoExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseStartdateInfoExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.ChildCareNurseUsedNumberExport;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.GetRemainingNumberCarePub;
import nts.uk.ctx.at.record.pub.monthly.vacation.childcarenurse.childcare.TempChildCareNurseManagementExport;
import nts.uk.ctx.at.record.pubimp.monthly.vacation.childcarenurse.ChildCareNurseConverter;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareManagement;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.childcarenurse.ChildCareNurseUsedInfo;

/**
 * ????????????????????????????????????????????????
 * @author yuri_tamakoshi
 */
@Stateless
public class GetRemainingNumberCarePubImpl  implements GetRemainingNumberCarePub {


	@Inject
	private ChildCareNurseRequireImplFactory childCareNurseRequireImplFactory;



	/**
	 * ???????????????????????????????????????
	 * @param companyId ??????ID
	 * @param employeeId ??????ID
	 * @param period ????????????
	 * @param performReferenceAtr ????????????????????????(??????????????? or?????????)
	 * @param criteriaDate ?????????
	 * @param isOverWrite ??????????????????(Optional)
	 * @param tempCareDataforOverWriteList List<????????????????????????????????????>(Optional)
	 * @param prevCareLeave ??????????????????????????????????????????<Optional>
	 * @param createAtr ???????????????(Optional)
	 * @param periodOverWrite ?????????????????????(Optional)
	 * @return ????????????????????????????????????
	 */
	@Override
	public ChildCareNursePeriodExport getCareRemNumWithinPeriod(
			String companyId,
			String employeeId,
			DatePeriod period,
			InterimRemainMngMode performReferenceAtr,
			GeneralDate criteriaDate,
			Optional<Boolean> isOverWrite,
			List<TempChildCareNurseManagementExport> tempCareDataforOverWriteList,
			Optional<ChildCareNursePeriodExport> prevCareLeave,
			Optional<CreateAtr> createAtr,
			Optional<DatePeriod> periodOverWrite
			) {

		val require = childCareNurseRequireImplFactory.createRequireImpl();
		val cacheCarrier = new CacheCarrier();

		List<TempCareManagement>domChildCareNurseManagemenList =
				tempCareDataforOverWriteList.stream().map(c->new TempCareManagement(ChildCareNurseConverter.toDomain(c))).collect(Collectors.toList());

		Optional<AggrResultOfChildCareNurse> domPrevCareLeave = Optional.empty();


		AggrResultOfChildCareNurse result = GetRemainingNumberCareService.getCareRemNumWithinPeriod(
				companyId, employeeId, period, performReferenceAtr, criteriaDate, isOverWrite, domChildCareNurseManagemenList, domPrevCareLeave, createAtr, periodOverWrite, cacheCarrier, require);

		return mapToPub(result);
	}

	// Export????????????
	private ChildCareNursePeriodExport mapToPub(AggrResultOfChildCareNurse c) {

		return new ChildCareNursePeriodExport(

				createError(c.getChildCareNurseErrors()),

				ChildCareNurseUsedNumberExport.of(
						c.getAsOfPeriodEnd().getUsedDay().v(),
						c.getAsOfPeriodEnd().getUsedTimes().map(ny -> ny.v())),

//				ChildCareNurseStartdateDaysInfo.of(
//						mapToPub(c.getStartdateDays().getThisYear()),
//						c.getStartdateDays().getNextYear().map(ny -> mapToPub(ny))),
				ChildCareNurseStartdateDaysInfoExport.of(
						mapToPub(c.getStartdateDays().getThisYear()),
						c.getStartdateDays().getNextYear().map(ny -> mapToPub(ny))),

				c.isStartDateAtr(),

				ChildCareNurseAggrPeriodDaysInfoExport.of(
						mapToPubAggrPeriodInfo(c.getAggrperiodinfo().getThisYear()),
						c.getAggrperiodinfo().getNextYear().map(ny -> mapToPubAggrPeriodInfo(ny)))

				);
	}

	//  ??????????????????????????????
	private ChildCareNurseStartdateInfoExport mapToPub(nts.uk.ctx.at.record.dom.remainingnumber.childcarenurse.childcare.ChildCareNurseStartdateInfo domain) {

		return ChildCareNurseStartdateInfoExport.of(
				ChildCareNurseUsedNumberExport.of(
						domain.getUsedDays().getUsedDay().v(),
						domain.getUsedDays().getUsedTimes().map(t -> t.v())),
					ChildCareNurseRemainingNumberExport.of(
							domain.getRemainingNumber().getRemainDay().v(),
							domain.getRemainingNumber().getRemainTimes().map(t -> t.v())),
					domain.getLimitDays().v());
	}

	// ???????????????????????????
	private ChildCareNurseAggrPeriodInfoExport mapToPubAggrPeriodInfo(ChildCareNurseUsedInfo domain) {
		return ChildCareNurseAggrPeriodInfoExport.of(
							domain.getUsedTimes().v(),
							domain.getUsedDays().v(),
							ChildCareNurseUsedNumberExport.of(
									domain.getUsedNumber().getUsedDay().v(),
									domain.getUsedNumber().getUsedTimes().map(t -> t.v())));
	}

	// ???????????????????????????
	private List<ChildCareNurseErrorsExport> createError(List<nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.children.service.ChildCareNurseErrors> childCareNurseErrors) {

//		return childCareNurseErrors.stream().map(c ->
//																ChildCareNurseErrors.of(
//																		ChildCareNurseUsedNumber.of(
//																				c.getUsedNumber().getUsedDay(),
//																				c.getUsedNumber().getUsedTimes()),
//																		c.getLimitDays().v(),
//																		c.getYmd()))
//														.collect(Collectors.toList());

		return childCareNurseErrors.stream().map(c ->
															ChildCareNurseErrorsExport.of(
																	ChildCareNurseUsedNumberExport.of(
																			c.getUsedNumber().getUsedDay().v(),
																			c.getUsedNumber().getUsedTimes().map(u -> u.v())),
																	c.getLimitDays().v(),
																	c.getYmd()))
														.collect(Collectors.toList());
	}



}
