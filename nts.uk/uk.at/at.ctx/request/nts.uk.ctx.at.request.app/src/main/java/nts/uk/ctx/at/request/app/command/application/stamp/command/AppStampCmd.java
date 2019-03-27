package nts.uk.ctx.at.request.app.command.application.stamp.command;

import java.util.List;

import lombok.Data;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
public class AppStampCmd {
	
	// version
	private Long version;
	
	// appID
	private String appID;
    
    // ngày xin
    private String applicationDate;
    
    // reason from combo box
    private String titleReason;
    
    // reason from text area
    private String detailReason;
    
    // id người xin
    private String employeeID;
	
    // loại xin của đơn
	private Integer stampRequestMode;
	
	// thông tin của đơn xin
	private List<AppStampGoOutPermitCmd> appStampGoOutPermitCmds;
	private List<AppStampWorkCmd> appStampWorkCmds;
	private List<AppStampCancelCmd> appStampCancelCmds;
	private AppStampOnlineRecordCmd appStampOnlineRecordCmd;
	private boolean checkOver1Year;
}

