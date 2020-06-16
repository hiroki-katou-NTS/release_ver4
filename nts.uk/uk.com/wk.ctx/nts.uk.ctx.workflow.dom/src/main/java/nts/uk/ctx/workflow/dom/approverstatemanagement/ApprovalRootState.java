package nts.uk.ctx.workflow.dom.approverstatemanagement;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
/**
 * 承認ルートインスタンス
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ApprovalRootState extends AggregateRoot {
	
	private String CompanyID;
	
	private String rootStateID;
	
	private String employeeID;
	
	private GeneralDate approvalRecordDate;
	
//	private RootType rootType;
	
	@Setter
	private List<ApprovalPhaseState> listApprovalPhaseState;
	
	public static ApprovalRootState createFromFirst(String companyID, String appID, String employeeID, GeneralDate date, ApprovalRootState approvalRootState){
		if(Strings.isBlank(approvalRootState.getRootStateID())){
			return ApprovalRootState.builder()
					.rootStateID(appID)
					.approvalRecordDate(date)
					.employeeID(employeeID)
					.listApprovalPhaseState(approvalRootState.getListApprovalPhaseState().stream()
							.map(x -> ApprovalPhaseState.createFromFirst(companyID, date, appID, x)).collect(Collectors.toList()))
					.build();
		}
		return approvalRootState;
	}
	
	// もう使っていない
//	public String getHistoryID() {
//		return null;
//	}
}
