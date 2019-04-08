package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MPDataDto {
    private String id;
	private String state;
    private String error;
    private String employeeCode;
    private String employeeName;
    private String employeeId;
    private String typeGroup;
    private boolean identify;
    private boolean approval;
    private String dailyConfirm;
    private String dailyCorrectPerformance;
    private Set<MPCellDataDto> cellDatas;
    
    public MPDataDto(String id, String state, String error, String employeeCode, String employeeName, String employeeId, String typeGroup, boolean identify, boolean approval,
			String dailyConfirm, String dailyCorrectPerformance) {
    	
		this.id = id;
		this.state = state;
		this.error = error;
		this.employeeCode = employeeCode;
		this.employeeName = employeeName;
		this.employeeId = employeeId;
		this.typeGroup = typeGroup;
		this.identify = identify;
		this.approval = approval;
		this.dailyConfirm = dailyConfirm;
		this.dailyCorrectPerformance = dailyCorrectPerformance;
		this.cellDatas = new HashSet<MPCellDataDto>(); 
	}	
	
	public void setCellDatas(Set<MPCellDataDto> lstCellData) {
		this.cellDatas = lstCellData;
	}
	
	public void addCellData(MPCellDataDto cellData) {
		this.cellDatas.add(cellData);
	}

	
}
