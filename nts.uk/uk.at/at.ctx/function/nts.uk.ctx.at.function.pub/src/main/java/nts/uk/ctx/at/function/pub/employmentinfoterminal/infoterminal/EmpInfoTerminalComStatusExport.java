package nts.uk.ctx.at.function.pub.employmentinfoterminal.infoterminal;

import lombok.Value;
import nts.arc.time.GeneralDateTime;
/**
 * 
 * @author dungbn
 *
 */
@Value
public class EmpInfoTerminalComStatusExport {

	private String contractCode;
	
	private String empInfoTerCode;
	
	private GeneralDateTime signalLastTime;
}
