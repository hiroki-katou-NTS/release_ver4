package nts.uk.ctx.at.request.app.find.application.common.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalFrameImport_New;
@Value
@AllArgsConstructor
public class ApprovalFrameForAppDto {
private Integer phaseOrder;
	
	private Integer frameOrder;
	
	private Integer approvalAtrValue;
	
	private String approvalAtrName;
	
	private List<ApproverStateForAppDto> listApprover;
	
	private String approverID;
	
	private String approverName;
	
	private String representerID;
	
	private String representerName;
	
	private String approvalReason;
	
	public static ApprovalFrameForAppDto fromApprovalFrameImport(ApprovalFrameImport_New approvalFrameImport){
		return new ApprovalFrameForAppDto(
				approvalFrameImport.getPhaseOrder(), 
				approvalFrameImport.getFrameOrder(), 
				approvalFrameImport.getApprovalAtr().value,
				approvalFrameImport.getApprovalAtr().name, 
				approvalFrameImport.getListApprover().stream()
					.map(x -> new ApproverStateForAppDto(
							x.getApproverID(), 
							x.getApproverName(),
							x.getRepresenterID(),
							x.getRepresenterName()))
					.collect(Collectors.toList()), 
				approvalFrameImport.getApproverID(),
				approvalFrameImport.getApproverName(),
				approvalFrameImport.getRepresenterID(),
				approvalFrameImport.getRepresenterName(),
				approvalFrameImport.getApprovalReason());
	}
}
