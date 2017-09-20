/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.ws.shift.autocalsetting;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.UseClassification;
import nts.uk.ctx.at.schedule.dom.shift.autocalsetting.UseUnitOvertimeSetting;

/**
 * The Class AutomaticCalculationWS.
 */
@Path("ctx/at/schedule/shift/autocal")
@Produces("application/json")
public class AutomaticCalculationWS extends WebService {

	
	/**
	 * Find enum auto cal atr overtime.
	 *
	 * @return the list
	 */
	@POST
    @Path("find/autocalatrovertime")
    public List<EnumConstant> findEnumAutoCalAtrOvertime() {
        return EnumAdaptor.convertToValueNameList(AutoCalAtrOvertime.class);
    }
	

	/**
	 * Find enum use classification.
	 *
	 * @return the list
	 */
	@POST
    @Path("find/autocaluseclassification")
    public List<EnumConstant> findEnumUseClassification() {
        return EnumAdaptor.convertToValueNameList(UseClassification.class);
    }
	
	
	/**
	 * Find enum time limit upper limit setting.
	 *
	 * @return the list
	 */
	@POST
    @Path("find/autocaltimelimitsetting")
    public List<EnumConstant> findEnumTimeLimitUpperLimitSetting() {
        return EnumAdaptor.convertToValueNameList(TimeLimitUpperLimitSetting.class);
    }
	
	/**
	 * Find enum use unit overtime setting.
	 *
	 * @return the list
	 */
	@POST
    @Path("find/autocaluseunitovertimesetting")
    public List<EnumConstant> findEnumUseUnitOvertimeSetting() {
        return EnumAdaptor.convertToValueNameList(UseUnitOvertimeSetting.class);
    }
	
	
	
	
	
}
