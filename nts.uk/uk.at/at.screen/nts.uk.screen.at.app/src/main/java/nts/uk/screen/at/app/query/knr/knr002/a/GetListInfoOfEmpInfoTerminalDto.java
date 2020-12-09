package nts.uk.screen.at.app.query.knr.knr002.a;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author dungbn
 *
 */
@AllArgsConstructor
@Getter
@Setter
public class GetListInfoOfEmpInfoTerminalDto {
	
	private int numOfRegTerminals;
	
	private int numNormalState;
	
	private int numAbnormalState;
	
	private int numUntransmitted;
	
	private List<EmpInfoTerminalDto> listEmpInfoTerminalDto;
}
