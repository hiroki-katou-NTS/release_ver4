package nts.uk.ctx.sys.log.app.find.reference.record;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.log.app.find.reference.LogOutputItemDto;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;

/*
 * author: thuongtv
 */

@Getter
@Setter
@AllArgsConstructor
public class LogBasicInfoDto {


	/** operationId */
	private String operationId;
	/** userName login */
	private String userNameLogin; // no 2
	/** employeeCode Login */
	private String employeeCodeLogin; // no 3
	/** userId */
	private String userIdTaget;
	/** userName */
	private String userNameTaget; // no 20
	/** employeeId */
	private String employeeIdTaget; 
	/** employeeCode */
	private String employeeCodeTaget;// no 21
	/** ipAdress */
	private String ipAdress;
	/** modifyDateTime */
	private String modifyDateTime;
	/** processAttr */
	private String processAttr;
	
	/** List data  log */
	private List<LogDataCorrectRecordRefeDto> lstLogDataCorrectRecordRefeDto;
	
	/** List persion corect log */
	private List<LogPerCateCorrectRecordDto> lstLogPerCateCorrectRecordDto;
	
	/** List sub header  */
	private List<LogOutputItemDto> lstLogOutputItemDto;
	// log startPage
	private String menuName;
	private String note;
	//log login
	private String methodName;
	private String loginStatus;
	
	/** modifyDateConvert */
	private GeneralDateTime modifyDateConvert;
	
	public static LogBasicInfoDto fromDomain(LogBasicInformation domain) {
		return new LogBasicInfoDto(
				domain.getOperationId(),domain.getUserInfo().getUserName(),null,null,null,null,null,null,
				null,null,null,null,null,null,null,null,null,domain.getModifiedDateTime());
	}


}