package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.OptimisticLockException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.i18n.I18NText;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ApprovalStatusActualResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ConfirmStatusActualResult;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalConfirmCache implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String sId;
	
	private List<String> employeeIds;
	
	private DatePeriod period;
	
	private Integer mode;
	
	private List<ConfirmStatusActualResult> lstConfirm;
	
	private List<ApprovalStatusActualResult> lstApproval;
	
	public void checkVer(ApprovalConfirmCache cacheNew) {
		
		List<ConfirmStatusActualResult> lstConfirmNew = cacheNew.getLstConfirm();
		if (!lstConfirmNew.equals(lstConfirm)) {
			throw new OptimisticLockException(I18NText.getText("Msg_1528"));
		}

		List<ApprovalStatusActualResult> lstApprovalNew = cacheNew.getLstApproval();

		if (!lstApprovalNew.equals(lstApproval)) {
			throw new OptimisticLockException(I18NText.getText("Msg_1528"));
		}
		
		
	}
}
