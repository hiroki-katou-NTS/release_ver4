package nts.uk.pub.spr.appstatus.output;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.GeneralDate;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class AppStatusSpr {
	
	private GeneralDate date;
	/**
	 * 早出残業
	 */
	private Integer status1;
	/**
	 * 通常残業
	 */
	private Integer status2;
	/**
	 * 早出残業・通常残業
	 */
	private Integer status3;
	
	private Optional<String> applicationID1;
	
	private Optional<String> applicationID2;
	
	private Optional<String> applicationID3;
	
}
