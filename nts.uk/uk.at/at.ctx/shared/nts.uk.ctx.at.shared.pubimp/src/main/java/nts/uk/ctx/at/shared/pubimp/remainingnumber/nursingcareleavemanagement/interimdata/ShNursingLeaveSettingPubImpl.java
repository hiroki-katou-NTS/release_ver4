/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.pubimp.remainingnumber.nursingcareleavemanagement.interimdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareData;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.ChildTempCareData;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.childcare.interimdata.ChildTempCareDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.ChildCareLeaveRemaiDataRepo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.ChildCareLeaveRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.LeaveForCareData;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.data.LeaveForCareDataRepo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.ChildCareLeaveRemInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.ChildCareLeaveRemainingInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.LeaveForCareInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.LeaveForCareInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.info.UpperLimitSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingCategory;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSettingRepository;
import nts.uk.ctx.at.shared.pub.remainingnumber.nursingcareleavemanagement.interimdata.ChildNursingRemainExport;
import nts.uk.ctx.at.shared.pub.remainingnumber.nursingcareleavemanagement.interimdata.ChildNursingRemainInforExport;
import nts.uk.ctx.at.shared.pub.remainingnumber.nursingcareleavemanagement.interimdata.NursingMode;
import nts.uk.ctx.at.shared.pub.remainingnumber.nursingcareleavemanagement.interimdata.ShNursingLeaveSettingPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class ShNursingLeaveSettingPubImpl.
 */
@Stateless
public class ShNursingLeaveSettingPubImpl implements ShNursingLeaveSettingPub {

	/** The nursing leave setting repository. */
	@Inject
	private NursingLeaveSettingRepository nursingLeaveSettingRepository;

	@Inject
	private LeaveForCareDataRepo leaveForCareDataRepo;

	@Inject
	private LeaveForCareInfoRepository leaveForCareInfoRepository;

	@Inject
	private ChildCareLeaveRemaiDataRepo childCareLeaveRemaiDataRepo;
	
	@Inject
	private ChildCareLeaveRemInfoRepository childCareLeaveRemInfoRepo;

	@Inject
	private TempCareDataRepository tempCareDataRepository;
	
	@Inject
	private ChildTempCareDataRepository childTempCareDataRepository;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ChildNursingRemainExport aggrChildNursingRemainPeriod(String companyId, String employeeId, DatePeriod period,
			NursingMode mode) {
		
		ChildNursingRemainExport childNursingRemainExport = ChildNursingRemainExport.builder().build();
		// get nursingLeaveSetting by companyId,nursingCategory = NURSING_CARE
		Optional<NursingLeaveSetting> _nursingLeaveSetting = this.findNursingLeaveSetting(companyId,
				NursingCategory.Nursing);
		if (!_nursingLeaveSetting.isPresent() || (_nursingLeaveSetting.isPresent() && !_nursingLeaveSetting.get().isManaged()) || (_nursingLeaveSetting.isPresent() && _nursingLeaveSetting.get().isManaged() && _nursingLeaveSetting.get().getStartMonthDay() == 0)) {
			childNursingRemainExport.setIsManage(false);
			return childNursingRemainExport;
		} else {
			// do something
			return getAggrChildNursingRemainPeriod(companyId, employeeId, period.start(), period.end(), mode, _nursingLeaveSetting.get());
		}
		
	}

	private ChildNursingRemainExport getAggrChildNursingRemainPeriod(String companyId, String employeeId,
			GeneralDate startDate, GeneralDate endDate, NursingMode mode, NursingLeaveSetting nursingLeaveSetting) {
		ChildNursingRemainExport childNursingRemainExport = ChildNursingRemainExport.builder().build();
		childNursingRemainExport.setGrantPeriodFlag(false);
		Output1 out1 = this.calculationUsagePeriod(nursingLeaveSetting.getStartMonthDay(), startDate, endDate);
		childNursingRemainExport.setGrantPeriodFlag(out1.isPeriodGrantFlag());
		
		// algorithm 個人情報の上限と使用の取得, once in params: useEndDateBeforeGrant, endDate
		Output2 out2 = this.getPersonalChildInfor(employeeId, startDate, endDate, out1.getUseEndDateBefore(),
				out1.getUseStartDateAfter(), nursingLeaveSetting, childNursingRemainExport);
		if(out2 == null)
			return childNursingRemainExport;
		
		PeriodOfOverlap out3 = this.getPeriodOfOverlap(startDate, endDate, out1.getUseStartDateBefore(),
				out1.getUseEndDateBefore());
		
		if(out3 == null)
			return childNursingRemainExport;
		
		double useNumberNursing = this.getChildNursingUsedNumber(companyId, employeeId, out3.getStartDateOverlap(),
				out3.getEndDateOverlap(), mode);
		
		ChildNursingRemainInforExport preGrantStatement = ChildNursingRemainInforExport.builder().build();
		preGrantStatement.setNumberOfUse(useNumberNursing);

		double residual = out2.getGrantedNumberThisTime() - useNumberNursing;
		preGrantStatement.setResidual(residual);

		childNursingRemainExport.setPreGrantStatement(preGrantStatement);
		
		if (out1.isPeriodGrantFlag()) {
			PeriodOfOverlap out4 = this.getPeriodOfOverlap(startDate, endDate, out1.getUseStartDateAfter().get(),
					out1.getUseEndDateAfter().get());
			if(out4 == null)
				return childNursingRemainExport;
			
			double useNumberNursing2 = this.getChildNursingUsedNumber(companyId, employeeId, out4.getStartDateOverlap(),
					out4.getEndDateOverlap(), mode);
			
			ChildNursingRemainInforExport afterGrantStatement = ChildNursingRemainInforExport.builder().build();
			afterGrantStatement.setNumberOfUse(useNumberNursing2);

			double residualafter = out2.getGrantedNumberThisTime() - useNumberNursing2;
			afterGrantStatement.setResidual(residualafter);

			childNursingRemainExport.setAfterGrantStatement(Optional.of(afterGrantStatement));
		}else {
			childNursingRemainExport.setAfterGrantStatement(Optional.empty());
		}
		
		return childNursingRemainExport;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ChildNursingRemainExport aggrNursingRemainPeriod(String companyId, String employeeId, GeneralDate startDate,
			GeneralDate endDate, NursingMode mode) {
		ChildNursingRemainExport childNursingRemainExport = ChildNursingRemainExport.builder().build();
		// get nursingLeaveSetting by companyId,nursingCategory = NURSING_CARE
		Optional<NursingLeaveSetting> _nursingLeaveSetting = this.findNursingLeaveSetting(companyId,
				NursingCategory.Nursing);
		if (!_nursingLeaveSetting.isPresent() || (_nursingLeaveSetting.isPresent() && !_nursingLeaveSetting.get().isManaged())  || (_nursingLeaveSetting.isPresent() && _nursingLeaveSetting.get().isManaged() && _nursingLeaveSetting.get().getStartMonthDay() == 0)) {
			childNursingRemainExport.setIsManage(false);
			return childNursingRemainExport;
		} else {
			// do something
			return getAggrNursingRemainPeriod(companyId, employeeId, startDate, endDate, mode, _nursingLeaveSetting.get());
		}
	}

	/**
	 * Find nursing leave setting.
	 *
	 * @param companyId
	 * @param nursingCategory
	 * 
	 * @return the nursing leave setting
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private Optional<NursingLeaveSetting> findNursingLeaveSetting(String companyId, NursingCategory nursingCategory) {
		List<NursingLeaveSetting> nursingLeaveSettings = this.nursingLeaveSettingRepository.findByCompanyId(companyId);
		return nursingLeaveSettings.stream().filter(e -> e.getNursingCategory().equals(nursingCategory)).findFirst();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private ChildNursingRemainExport getAggrNursingRemainPeriod(String companyId, String employeeId, GeneralDate startDate,
			GeneralDate endDate, NursingMode mode, NursingLeaveSetting nursingLeaveSetting) {
		ChildNursingRemainExport childNursingRemainExport = ChildNursingRemainExport.builder().build();
		// algorithm 利用期間の算出
		childNursingRemainExport.setGrantPeriodFlag(false);
		Output1 out1 = this.calculationUsagePeriod(nursingLeaveSetting.getStartMonthDay(), startDate, endDate);
		childNursingRemainExport.setGrantPeriodFlag(out1.isPeriodGrantFlag());

		// algorithm 個人情報の上限と使用の取得, once in params: useEndDateBeforeGrant, endDate
		Output2 out2 = this.getPersonalInfor(employeeId, startDate, endDate, out1.getUseEndDateBefore(),
				out1.getUseStartDateAfter(), nursingLeaveSetting, childNursingRemainExport);

		if(out2 == null)
			return childNursingRemainExport;
		
		PeriodOfOverlap out3 = this.getPeriodOfOverlap(startDate, endDate, out1.getUseStartDateBefore(),
				out1.getUseEndDateBefore());
		
		if(out3 == null)
			return childNursingRemainExport;

		double useNumberNursing = this.getNursingUsedNumber(companyId, employeeId, out3.getStartDateOverlap(),
				out3.getEndDateOverlap(), mode);

		ChildNursingRemainInforExport preGrantStatement = ChildNursingRemainInforExport.builder().build();
		preGrantStatement.setNumberOfUse(useNumberNursing);

		double residual = out2.getGrantedNumberThisTime() - useNumberNursing;
		preGrantStatement.setResidual(residual);

		childNursingRemainExport.setPreGrantStatement(preGrantStatement);

		if (out1.isPeriodGrantFlag()) {
			PeriodOfOverlap out4 = this.getPeriodOfOverlap(startDate, endDate, out1.getUseStartDateAfter().get(),
					out1.getUseEndDateAfter().get());
			
			if(out4 == null)
				return childNursingRemainExport;
			
			double useNumberNursing2 = this.getNursingUsedNumber(companyId, employeeId, out4.getStartDateOverlap(),
					out4.getEndDateOverlap(), mode);
			
			ChildNursingRemainInforExport afterGrantStatement = ChildNursingRemainInforExport.builder().build();
			afterGrantStatement.setNumberOfUse(useNumberNursing2);

			double residualafter = out2.getGrantedNumberThisTime() - useNumberNursing2;
			afterGrantStatement.setResidual(residualafter);

			childNursingRemainExport.setAfterGrantStatement(Optional.of(afterGrantStatement));
		}else {
			childNursingRemainExport.setAfterGrantStatement(Optional.empty());
		}
		 
		return childNursingRemainExport;
	}

	private Output1 calculationUsagePeriod(Integer startMonthDay, GeneralDate startDate, GeneralDate endDate) {
		Output1 output = new Output1();
		// startMonthDay không thể null. nếu null. sai data. gửi cho anh Thành newways
		int dayStartMonthDay = startMonthDay % 100;
		int monthStartMonthDay = startMonthDay / 100;
		GeneralDate commencementDate = GeneralDate.ymd(startDate.year(), monthStartMonthDay, dayStartMonthDay);
		GeneralDate useStartDateBeforeGrant;

		if (commencementDate.after(startDate)) {
			useStartDateBeforeGrant = commencementDate.addYears(-1);
		} else {
			useStartDateBeforeGrant = commencementDate;
		}
		// set to output
		output.setUseStartDateBefore(useStartDateBeforeGrant);
		output.setUseEndDateBefore(useStartDateBeforeGrant.addYears(1).addDays(-1));

		GeneralDate checkDate = useStartDateBeforeGrant.addYears(1);
		if (checkDate.beforeOrEquals(endDate)) {
			// change flag
			output.setPeriodGrantFlag(true);
			GeneralDate useStartDateAfterGrant = useStartDateBeforeGrant.addYears(1);

			// set to input
			output.setUseStartDateAfter(Optional.of(useStartDateAfterGrant));
			output.setUseEndDateAfter(Optional.of(useStartDateAfterGrant.addYears(1).addDays(-1)));
		}

		return output;
	}
	
	private Output2 getPersonalChildInfor(String employeeId, GeneralDate startDate, GeneralDate endDate,
			GeneralDate useEndDateBeforeGrant, Optional<GeneralDate> useStartDateAfterGrant,
			NursingLeaveSetting nursingLeaveSetting, ChildNursingRemainExport childNursingRemainExport) {
		Output2 ouput = new Output2();
		Optional<ChildCareLeaveRemainingInfo> optionalNursingChildInfo = this.childCareLeaveRemInfoRepo.getChildCareByEmpId(employeeId);
		if(optionalNursingChildInfo.isPresent()) {
		ChildCareLeaveRemainingInfo nursingChildInfo = optionalNursingChildInfo.get();
		ouput.setUseClassification(nursingChildInfo.isUseClassification());
		if (nursingChildInfo.getUpperlimitSetting().equals(UpperLimitSetting.FAMILY_INFO)) {
			int fakeTargetNumberFamily = 13; // (gọi request 440 và 441 ra,
												// param: useEndDateBeforeGrant)
			int vacationDays = 0;
			if (fakeTargetNumberFamily >= nursingLeaveSetting.getMaxPersonSetting().getNursingNumberPerson().v()) {
				vacationDays = nursingLeaveSetting.getMaxPersonSetting().getNursingNumberLeaveDay().v();
			}
			ouput.setGrantedNumberThisTime(vacationDays);

			if (childNursingRemainExport.getGrantPeriodFlag()) {
				int fakeTargetNumberFamily2 = 14; // (440,441 - param: endDate)
				if (nursingLeaveSetting != null && fakeTargetNumberFamily2 >= nursingLeaveSetting.getMaxPersonSetting()
						.getNursingNumberPerson().v()) {
					vacationDays = nursingLeaveSetting.getMaxPersonSetting().getNursingNumberLeaveDay().v();
				}
				ouput.setGrantedNumberNextTime(vacationDays);
			}
		} else {
			ouput.setGrantedNumberThisTime(nursingChildInfo.getMaxDayForThisFiscalYear().get().v());
			if (childNursingRemainExport.getGrantPeriodFlag())
				ouput.setGrantedNumberNextTime(nursingChildInfo.getMaxDayForThisFiscalYear().get().v());
		}
		// setManage
		childNursingRemainExport.setIsManage(nursingChildInfo.isUseClassification());
		// calculate overlap time
		Optional<ChildCareLeaveRemainingData> optionalNursingChildData = this.childCareLeaveRemaiDataRepo.getChildCareByEmpId(employeeId);
		ouput.setUseNumberPersonInfo(optionalNursingChildData.get().getNumOfUsedDay().v());

		ouput.setStartDateOverlapBeforeGrant(startDate);
		ouput.setEndDateOverlapBeforeGrant(useEndDateBeforeGrant);
		ouput.setStartDateOverlapAfterGrant(useStartDateAfterGrant.isPresent() ? useStartDateAfterGrant.get() : null);
		ouput.setEndDateOverlapAfterGrant(endDate);

		return ouput;
		}else {
			return null;
		}
	}

	private Output2 getPersonalInfor(String employeeId, GeneralDate startDate, GeneralDate endDate,
			GeneralDate useEndDateBeforeGrant, Optional<GeneralDate> useStartDateAfterGrant,
			NursingLeaveSetting nursingLeaveSetting, ChildNursingRemainExport childNursingRemainExport) {
		Output2 ouput = new Output2();
		Optional<LeaveForCareInfo> optionalNursingInfo = this.leaveForCareInfoRepository.getCareByEmpId(employeeId);
		if(optionalNursingInfo.isPresent()) {
			LeaveForCareInfo nursingInfo = optionalNursingInfo.get();
			ouput.setUseClassification(nursingInfo.isUseClassification());
			if (nursingInfo.getUpperlimitSetting().equals(UpperLimitSetting.FAMILY_INFO)) {
				int fakeTargetNumberFamily = 13; // (gọi request 440 và 441 ra,
													// param: useEndDateBeforeGrant)
				int vacationDays = 0;
				if (fakeTargetNumberFamily >= nursingLeaveSetting.getMaxPersonSetting().getNursingNumberPerson().v()) {
					vacationDays = nursingLeaveSetting.getMaxPersonSetting().getNursingNumberLeaveDay().v();
				}
				ouput.setGrantedNumberThisTime(vacationDays);

				if (childNursingRemainExport.getGrantPeriodFlag()) {
					int fakeTargetNumberFamily2 = 14; // (440,441 - param: endDate)
					if (nursingLeaveSetting != null && fakeTargetNumberFamily2 >= nursingLeaveSetting.getMaxPersonSetting()
							.getNursingNumberPerson().v()) {
						vacationDays = nursingLeaveSetting.getMaxPersonSetting().getNursingNumberLeaveDay().v();
					}
					ouput.setGrantedNumberNextTime(vacationDays);
				}
			} else {
				ouput.setGrantedNumberThisTime(nursingInfo.getMaxDayForThisFiscalYear().get().v());
				if (childNursingRemainExport.getGrantPeriodFlag())
					ouput.setGrantedNumberNextTime(nursingInfo.getMaxDayForThisFiscalYear().get().v());
			}
			// setManage
			childNursingRemainExport.setIsManage(nursingInfo.isUseClassification());
			// calculate overlap time
			Optional<LeaveForCareData> optionalNursingData = this.leaveForCareDataRepo.getCareByEmpId(employeeId);
			ouput.setUseNumberPersonInfo(optionalNursingData.get().getNumOfUsedDay().v());

			ouput.setStartDateOverlapBeforeGrant(startDate);
			ouput.setEndDateOverlapBeforeGrant(useEndDateBeforeGrant);
			ouput.setStartDateOverlapAfterGrant(useStartDateAfterGrant.isPresent() ? useStartDateAfterGrant.get() : null);
			ouput.setEndDateOverlapAfterGrant(endDate);

			return ouput;
		}
		return null;
		
	}

	private PeriodOfOverlap getPeriodOfOverlap(GeneralDate startDate, GeneralDate endDate, GeneralDate useStartDate,
			GeneralDate useEndDate  ) {
		PeriodOfOverlap out3 = new PeriodOfOverlap();
		if (startDate.afterOrEquals(useStartDate)) {
			out3.setStartDateOverlap(startDate);
		} else {
			out3.setStartDateOverlap(useStartDate);
		}

		if (endDate.beforeOrEquals(useEndDate)) {
			out3.setEndDateOverlap(endDate);
		} else {
			out3.setEndDateOverlap(useEndDate);
		}
		
		if(out3.getStartDateOverlap().beforeOrEquals(out3.getEndDateOverlap())) {
			return out3;
		}
		return null;
	}

	private double getNursingUsedNumber(String companyId, String employeeId, GeneralDate startDate, GeneralDate endDate,
			NursingMode mode) {
		double usedNumber = 0;
		List<TempCareData> tempCareDataList = new ArrayList<>();
		if (mode == NursingMode.Monthly) {
			// TODO
			//　対象外
		} else {
			tempCareDataList = tempCareDataRepository.findByEmpIdInPeriod(employeeId,
					startDate, endDate);
		}
		
		for (TempCareData domain : tempCareDataList) {
			usedNumber += domain.getUsedDays().v();
		}

		return usedNumber;
	}
	
	private double getChildNursingUsedNumber(String companyId, String employeeId, GeneralDate startDate, GeneralDate endDate,
			NursingMode mode) {
		double usedNumber = 0;
		List<ChildTempCareData> childTempCareDataList = new ArrayList<>();
		if (mode == NursingMode.Monthly) {
			// TODO
			//　対象外
		} else {
			childTempCareDataList = childTempCareDataRepository.findByEmpIdInPeriod(employeeId,
					startDate, endDate);
		}
		
		for (ChildTempCareData domain : childTempCareDataList) {
			usedNumber += domain.getUsedDays().v();
		}

		return usedNumber;
	}

}
