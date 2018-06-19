package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.updateremainnum;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnLeaGrantRemDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveRemainHistRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveRemainingHistory;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveTimeRemainHistRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveTimeRemainingHistory;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnLeaMaxDataRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxHistRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.AnnualLeaveMaxHistoryData;

/**
 * 
 * @author HungTT - <<Work>> 年休残数更新
 *
 */

@Stateless
public class RemainAnnualLeaveUpdating {

	@Inject
	private AnnLeaGrantRemDataRepository annLeaveRemainRepo;

	@Inject
	private AnnLeaMaxDataRepository annLeaveMaxRepo;

	@Inject
	private AnnualLeaveMaxHistRepository annLeaveMaxHistRepo;

	@Inject
	private AnnualLeaveRemainHistRepository annLeaveRemainHistRepo;

	@Inject
	private AnnualLeaveTimeRemainHistRepository annLeaveTimeRemainHistRepo;

	/**
	 * 年休残数更新
	 * 
	 * @param output
	 * @param period
	 * @param empId
	 */
	public void updateRemainAnnualLeave(AggrResultOfAnnualLeave output, AggrPeriodEachActualClosure period,
			String empId) {
		updateRemainAnnualLeaveNumber(output, period, empId);
		updateMaxAnnualLeaveNumber(output, period, empId);
	}

	/**
	 * 年休付与残数データの更新
	 * 
	 * @param output
	 * @param period
	 * @param empId
	 */
	private void updateRemainAnnualLeaveNumber(AggrResultOfAnnualLeave output, AggrPeriodEachActualClosure period,
			String empId) {
		List<AnnualLeaveGrantRemainingData> listRemainData = annLeaveRemainRepo.findNotExp(empId);
		for (AnnualLeaveGrantRemainingData data : listRemainData) {
			AnnualLeaveRemainingHistory hist = new AnnualLeaveRemainingHistory(data, period.getYearMonth(),
					period.getClosureId(), period.getClosureDate());
			annLeaveRemainHistRepo.addOrUpdate(hist);
		}
		updateAnnualLeaveRemainProcess(output.getAsOfPeriodEnd());
		updateAnnualLeaveTimeRemainProcess(
				output.getAsOfGrant().isPresent() ? output.getAsOfGrant().get() : Collections.emptyList());
	}

	/**
	 * 年休上限データの更新
	 * 
	 * @param output
	 * @param period
	 * @param empId
	 */
	private void updateMaxAnnualLeaveNumber(AggrResultOfAnnualLeave output, AggrPeriodEachActualClosure period,
			String empId) {
		Optional<AnnualLeaveMaxData> optMaxData = annLeaveMaxRepo.get(empId);
		if (optMaxData.isPresent()) {
			AnnualLeaveMaxData maxData = optMaxData.get();
			AnnualLeaveMaxHistoryData maxDataHist = new AnnualLeaveMaxHistoryData(maxData, period.getYearMonth(),
					period.getClosureId(), period.getClosureDate());
			annLeaveMaxHistRepo.addOrUpdate(maxDataHist);
			AnnualLeaveMaxData maxDataOutput = output.getAsOfPeriodEnd().getMaxData();
			maxData = AnnualLeaveMaxData.createFromJavaType(empId,
					maxDataOutput.getHalfdayAnnualLeaveMax() != null
							&& maxDataOutput.getHalfdayAnnualLeaveMax().isPresent()
									? maxDataOutput.getHalfdayAnnualLeaveMax().get().getMaxTimes().v() : null,
					maxDataOutput.getHalfdayAnnualLeaveMax() != null
							&& maxDataOutput.getHalfdayAnnualLeaveMax().isPresent()
									? maxDataOutput.getHalfdayAnnualLeaveMax().get().getUsedTimes().v() : null,
					maxDataOutput.getTimeAnnualLeaveMax() != null && maxDataOutput.getTimeAnnualLeaveMax().isPresent()
							? maxDataOutput.getTimeAnnualLeaveMax().get().getMaxMinutes().v() : null,
					maxDataOutput.getTimeAnnualLeaveMax() != null && maxDataOutput.getTimeAnnualLeaveMax().isPresent()
							? maxDataOutput.getTimeAnnualLeaveMax().get().getUsedMinutes().v() : null);
			annLeaveMaxRepo.update(maxData);
		} else {
			AnnualLeaveMaxData maxDataOutput = output.getAsOfPeriodEnd().getMaxData();
			AnnualLeaveMaxData maxData = AnnualLeaveMaxData.createFromJavaType(empId,
					maxDataOutput.getHalfdayAnnualLeaveMax() != null
							&& maxDataOutput.getHalfdayAnnualLeaveMax().isPresent()
									? maxDataOutput.getHalfdayAnnualLeaveMax().get().getMaxTimes().v() : null,
					maxDataOutput.getHalfdayAnnualLeaveMax() != null
							&& maxDataOutput.getHalfdayAnnualLeaveMax().isPresent()
									? maxDataOutput.getHalfdayAnnualLeaveMax().get().getUsedTimes().v() : null,
					maxDataOutput.getTimeAnnualLeaveMax() != null && maxDataOutput.getTimeAnnualLeaveMax().isPresent()
							? maxDataOutput.getTimeAnnualLeaveMax().get().getMaxMinutes().v() : null,
					maxDataOutput.getTimeAnnualLeaveMax() != null && maxDataOutput.getTimeAnnualLeaveMax().isPresent()
							? maxDataOutput.getTimeAnnualLeaveMax().get().getUsedMinutes().v() : null);
			annLeaveMaxRepo.add(maxData);
		}
	}

	/**
	 * 年休付与残数データ更新処理
	 * 
	 * @param info
	 */
	private void updateAnnualLeaveRemainProcess(AnnualLeaveInfo info) {
		List<AnnualLeaveGrantRemainingData> listData = info.getGrantRemainingNumberList();
		for (AnnualLeaveGrantRemainingData data : listData) {
			Optional<AnnualLeaveGrantRemainingData> optDomain = annLeaveRemainRepo.find(data.getEmployeeId(),
					data.getGrantDate(), data.getDeadline());
			if (optDomain.isPresent()) {
				AnnualLeaveGrantRemainingData domain = optDomain.get();
				AnnualLeaveGrantRemainingData updateDomain = AnnualLeaveGrantRemainingData.createFromJavaType(
						domain.getAnnLeavID(), domain.getCid(), domain.getEmployeeId(), domain.getGrantDate(),
						domain.getDeadline(), data.getExpirationStatus().value, data.getRegisterType().value,
						data.getDetails().getGrantNumber().getDays().v(),
						data.getDetails().getGrantNumber().getMinutes().isPresent()
								? data.getDetails().getGrantNumber().getMinutes().get().v() : null,
						data.getDetails().getUsedNumber().getDays().v(),
						data.getDetails().getUsedNumber().getMinutes().isPresent()
								? data.getDetails().getUsedNumber().getMinutes().get().v() : null,
						data.getDetails().getUsedNumber().getStowageDays().isPresent()
								? data.getDetails().getUsedNumber().getStowageDays().get().v() : null,
						data.getDetails().getRemainingNumber().getDays().v(),
						data.getDetails().getRemainingNumber().getMinutes().isPresent()
								? data.getDetails().getRemainingNumber().getMinutes().get().v() : null,
						data.getDetails().getUsedPercent().v().doubleValue(),
						data.getAnnualLeaveConditionInfo().isPresent()
								? data.getAnnualLeaveConditionInfo().get().getPrescribedDays().v() : null,
						data.getAnnualLeaveConditionInfo().isPresent()
								? data.getAnnualLeaveConditionInfo().get().getDeductedDays().v() : null,
						data.getAnnualLeaveConditionInfo().isPresent()
								? data.getAnnualLeaveConditionInfo().get().getWorkingDays().v() : null);
				annLeaveRemainRepo.update(updateDomain);
			} else {
				AnnualLeaveGrantRemainingData addDomain = AnnualLeaveGrantRemainingData.createFromJavaType(
						IdentifierUtil.randomUniqueId(), data.getCid(), data.getEmployeeId(), data.getGrantDate(),
						data.getDeadline(), data.getExpirationStatus().value, data.getRegisterType().value,
						data.getDetails().getGrantNumber().getDays().v(),
						data.getDetails().getGrantNumber().getMinutes().isPresent()
								? data.getDetails().getGrantNumber().getMinutes().get().v() : null,
						data.getDetails().getUsedNumber().getDays().v(),
						data.getDetails().getUsedNumber().getMinutes().isPresent()
								? data.getDetails().getUsedNumber().getMinutes().get().v() : null,
						data.getDetails().getUsedNumber().getStowageDays().isPresent()
								? data.getDetails().getUsedNumber().getStowageDays().get().v() : null,
						data.getDetails().getRemainingNumber().getDays().v(),
						data.getDetails().getRemainingNumber().getMinutes().isPresent()
								? data.getDetails().getRemainingNumber().getMinutes().get().v() : null,
						data.getDetails().getUsedPercent().v().doubleValue(),
						data.getAnnualLeaveConditionInfo().isPresent()
								? data.getAnnualLeaveConditionInfo().get().getPrescribedDays().v() : null,
						data.getAnnualLeaveConditionInfo().isPresent()
								? data.getAnnualLeaveConditionInfo().get().getDeductedDays().v() : null,
						data.getAnnualLeaveConditionInfo().isPresent()
								? data.getAnnualLeaveConditionInfo().get().getWorkingDays().v() : null);
				annLeaveRemainRepo.add(addDomain);
			}
		}
	}

	/**
	 * 年休付与時点残数履歴データ更新処理
	 */
	private void updateAnnualLeaveTimeRemainProcess(List<AnnualLeaveInfo> listInfor) {
		for (AnnualLeaveInfo infor : listInfor) {
			for (AnnualLeaveGrantRemainingData data : infor.getGrantRemainingNumberList()) {
				AnnualLeaveTimeRemainingHistory hist = new AnnualLeaveTimeRemainingHistory(data, infor.getYmd());
				annLeaveTimeRemainHistRepo.add(hist);
			}
		}
	}
}
