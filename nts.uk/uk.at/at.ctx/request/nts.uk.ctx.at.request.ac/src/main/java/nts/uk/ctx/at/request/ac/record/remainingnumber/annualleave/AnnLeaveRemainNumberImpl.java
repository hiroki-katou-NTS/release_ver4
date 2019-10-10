package nts.uk.ctx.at.request.ac.record.remainingnumber.annualleave;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.AnnLeaveRemainNumberPub;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.export.AnnualLeaveGrantExport;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.export.AnnualLeaveManageInforExport;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.export.AnnualLeaveRemainingNumberExport;
import nts.uk.ctx.at.record.pub.remainnumber.annualleave.export.ReNumAnnLeaReferenceDateExport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.AnnLeaveRemainNumberAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.AnnualLeaveGrantImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.AnnualLeaveManageInforImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.AnnualLeaveRemainingNumberImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.ReNumAnnLeaReferenceDateImport;

@Stateless
public class AnnLeaveRemainNumberImpl implements AnnLeaveRemainNumberAdapter {

	@Inject
	AnnLeaveRemainNumberPub annLeavePub;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public ReNumAnnLeaReferenceDateImport getReferDateAnnualLeaveRemainNumber(String employeeID, GeneralDate date) {
		ReNumAnnLeaReferenceDateExport reNum = this.annLeavePub.getReferDateAnnualLeaveRemainNumber(employeeID, date);
		return new ReNumAnnLeaReferenceDateImport(
											mapNumber(reNum.getAnnualLeaveRemainNumberExport()),
											mapLeaveGrants(reNum.getAnnualLeaveGrantExports()),
											mapManageInfors(reNum.getAnnualLeaveManageInforExports()));
	}

	private List<AnnualLeaveManageInforImport> mapManageInfors(
			List<AnnualLeaveManageInforExport> infors) {
		if(infors == null){
			return	Collections.emptyList();
		}
	return	infors.stream().map(x->  new AnnualLeaveManageInforImport(
								x.getYmd(),
								x.getDaysUsedNo(),
								x.getUsedMinutes(),
								x.getScheduleRecordAtr())).collect(Collectors.toList());
	}

	private List<AnnualLeaveGrantImport> mapLeaveGrants(List<AnnualLeaveGrantExport> grants) {
		if(grants== null){
			return	Collections.emptyList();
		}
		return grants.stream().map(x-> new AnnualLeaveGrantImport(
											x.getGrantDate(), 
											x.getGrantNumber(), 
											x.getDaysUsedNo(), 
											x.getUsedMinutes(), 
											x.getRemainDays(), 
											x.getRemainMinutes(), 
											x.getDeadline())).collect(Collectors.toList());
	}

	private AnnualLeaveRemainingNumberImport mapNumber(AnnualLeaveRemainingNumberExport number) {
		if (number == null) {
			return null;
		}
		return new AnnualLeaveRemainingNumberImport(
											number.getAnnualLeaveGrantPreDay(), 
											number.getAnnualLeaveGrantPreTime(),
											number.getNumberOfRemainGrantPre(), 
											number.getTimeAnnualLeaveWithMinusGrantPre(), 
											number.getAnnualLeaveGrantPostDay(),
											number.getAnnualLeaveGrantPostTime(), 
											number.getNumberOfRemainGrantPost(), 
											number.getTimeAnnualLeaveWithMinusGrantPost(),
											number.getAnnualLeaveGrantDay(),
											number.getAnnualLeaveGrantTime(),
											number.getNumberOfRemainGrant(),
											number.getTimeAnnualLeaveWithMinusGrant(),
											number.getAttendanceRate(),
											number.getWorkingDays());
	}

}
