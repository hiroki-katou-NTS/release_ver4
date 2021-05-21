/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.employmentinfoterminal.infoterminal.EmpInfoTerminalCode;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.support.SupportCardNumber;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkLocationCD;

/**
 * @author laitv
 *
 */
public class WorkInformationStampHelper {
	
	public static WorkInformationStamp getStampDefault() {
		return new WorkInformationStamp(
				Optional.of("workplaceID"), 
				Optional.of(new EmpInfoTerminalCode("emCD")), 
				Optional.of(new WorkLocationCD("wkCD")), 
				Optional.of(new SupportCardNumber(9999)));
	}
	
	public static WorkInformationStamp getStampDefault3() {
		return new WorkInformationStamp(
				Optional.empty(), 
				Optional.of(new EmpInfoTerminalCode("emCD")), 
				Optional.empty(), 
				Optional.empty());
	}
	
	public static WorkInformationStamp getStampDefault4() {
		return new WorkInformationStamp(
				Optional.empty(), 
				Optional.of(new EmpInfoTerminalCode("emCD")), 
				Optional.of(new WorkLocationCD("workLocationCD")), 
				Optional.empty());
	}
	
	public static WorkInformationStamp getStampDefault5() {
		return new WorkInformationStamp(
				Optional.of("workPlaceId"), 
				Optional.of(new EmpInfoTerminalCode("emCD")), 
				Optional.of(new WorkLocationCD("workLocationCD")), 
				Optional.empty());
	}
	
	public static WorkInformationStamp getStampDefault6() {
		return new WorkInformationStamp(
				Optional.empty(), 
				Optional.empty(), 
				Optional.empty(), 
				Optional.empty());
	}
	
}
