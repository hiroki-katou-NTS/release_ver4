/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ws.workrule.shiftmaster;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.CopyShiftMasterOrgCommand;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.CopyShiftMasterOrgCommandHandler;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.CopyShiftMasterResultDto;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.CopyShiftWplGrpCommandHandler;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.CopyShiftWplGrpResultDto;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.DeleteShiftMasterCommand;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.DeleteShiftMasterCommandHandler;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.DeleteShiftMasterOrgCommand;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.DeleteShiftMasterOrgCommandHandler;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.RegisterShiftMasterCommand;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.RegisterShiftMasterCommandHandler;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.RegisterShiftMasterOrgCommand;
import nts.uk.ctx.at.shared.app.command.workrule.shiftmaster.RegisterShiftMasterOrgCommandHandler;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.AlreadySettingWorkplaceDto;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.Ksm015StartPageDto;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.OutPutShiftMasterDto;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.ShiftMasterFinder;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.ShiftMasterOrgFinder;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.ShiftMasterOrganizationDto;
import nts.uk.ctx.at.shared.app.find.workrule.shiftmaster.WorkInfoDto;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.dto.ShiftMasterDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class ShiftMasterWs.
 */
/**
 * @author anhdt
 *
 */
@Path("ctx/at/shared/workrule/shiftmaster")
@Produces("application/json")
public class ShiftMasterWs {
	
	@Inject
	private ShiftMasterFinder finder;
	
	@Inject
	private ShiftMasterOrgFinder orgFinder;
	
	@Inject
	private RegisterShiftMasterCommandHandler registerCmd;
	
	@Inject
	private DeleteShiftMasterCommandHandler deleteCmd;
	
	@Inject
	private RegisterShiftMasterOrgCommandHandler registerOrgCmd;
	
	@Inject
	private CopyShiftMasterOrgCommandHandler copyOrgCmd;
	
	@Inject
	private CopyShiftWplGrpCommandHandler wplGrpCommandHandler;
	
	@Inject
	private DeleteShiftMasterOrgCommandHandler deleteOrgCmd; 
	
	@POST
	@Path("startCPage/{unit}")
	public Ksm015StartPageDto isForAttendent(@PathParam("unit") int unit){
		AlreadySettingWorkplaceDto configWorkplace = this.orgFinder.getAlreadySetting(unit);
		return Ksm015StartPageDto.builder()
				.forAttendent(AppContexts.user().roles().isInChargeAttendance())
				.alreadyConfigWorkplaces(configWorkplace.getWorkplaceIds())
				.build() ;
	}
	
	@POST
	@Path("startDPage/{unit}")
	public Ksm015StartPageDto getDStart(@PathParam("unit") int unit){
		AlreadySettingWorkplaceDto configWorkplace = this.orgFinder.getAlreadySettingWplGr(unit);
		return Ksm015StartPageDto.builder()
				.forAttendent(AppContexts.user().roles().isInChargeAttendance())
				.alreadyConfigWorkplaces(unit == 0 ? configWorkplace.getWorkplaceIds() : configWorkplace.getWorkplaceGrpIds())
				.build() ;
	}
	
	@POST
	@Path("startPage")
	public Ksm015StartPageDto findAll(){
		return this.finder.startScreen();
	}
	
	@POST
	@Path("getlist")
	public List<ShiftMasterDto> getlist(){
		return this.finder.getShiftMasters();
	}
	
	@POST
	@Path("getlistByWorkPlace")
	public List<ShiftMasterDto> getlist(FindShiftMasterDto dto){
		String id = null;
		if(dto.getTargetUnit() != null) {
		if(dto.getTargetUnit() == 0){
			id = dto.getWorkplaceId();
		} else {
			id = dto.getWorkplaceGroupId();
		}
		}
		return this.orgFinder.optainShiftMastersByWorkPlace(id, dto.getTargetUnit());
	}
	
	@POST
	@Path("getShiftMasterWplGroup")
	public OutPutShiftMasterDto getShiftMasterWplGroup(FindShiftMasterDto dto){
		return this.orgFinder.getWorkgroupShiftInfo(dto.getWorkplaceGroupId());
	}
	
	@POST
	@Path("getShiftMasterByWplGroup")
	public List<ShiftMasterDto> getShiftMasterByWplGroup(FindShiftMasterDto dto){
		return this.orgFinder.getShiftMastersByWorkPlace(dto.getWorkplaceGroupId(), dto.getTargetUnit());
	}
	
	@POST
	@Path("optainlistByWorkPlace")
	public List<ShiftMasterDto> optainlistByWorkPlace(FindShiftMasterDto dto){
		return this.orgFinder.getShiftMastersByWorkPlace(dto.getWorkplaceId(), dto.getTargetUnit());
	}
	
	@POST
	@Path("getAlreadyConfigOrg/{unit}")
	public AlreadySettingWorkplaceDto getAlreadyConfigOrg(@PathParam("unit") int unit){
		return this.orgFinder.getAlreadySetting(unit);
	}
	
	@POST
	@Path("getAlreadySettingWplGr/{unit}")
	public AlreadySettingWorkplaceDto getAlreadySettingWplGr(@PathParam("unit") int unit){
		return this.orgFinder.getAlreadySettingWplGr(unit);
	}
	
	@POST
	@Path("register/shiftmaster/org")
	public void registerShiftMasterOrg(RegisterShiftMasterOrgCommand dto){
		this.registerOrgCmd.handle(dto);
	}
	
	@POST
	@Path("copy/shiftmaster/org")
	public List<CopyShiftMasterResultDto> copyShiftMasterOrg(CopyShiftMasterOrgCommand dto){
		return this.copyOrgCmd.handle(dto);
	}
	
	@POST
	@Path("copy/shiftmaster/org/wpg")
	public CopyShiftWplGrpResultDto copyShiftMasterOrgWpg(CopyShiftMasterOrgCommand dto){
		return this.wplGrpCommandHandler.handle(dto);
	}
	
	
	@POST
	@Path("register")
	public void register(RegisterShiftMasterCommand command){
		this.registerCmd.handle(command);
	}
	
	@POST
	@Path("delete")
	public void delete(DeleteShiftMasterCommand command){
		this.deleteCmd.handle(command);
	}
	
	@POST
	@Path("delete/org")
	public void deleteOrg(DeleteShiftMasterOrgCommand command){
		this.deleteOrgCmd.handle(command);
	}
	
	@POST
	@Path("workinfo/get")
	public WorkInfoDto getWorkInfo(FindWorkInfoDto dto){
		return this.finder.getWorkInfo(dto.getWorkTypeCd(), dto.getWorkTimeCd());
	}
}
