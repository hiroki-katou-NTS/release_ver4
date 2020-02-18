package nts.uk.ctx.hr.develop.app.sysoperationset.businessrecognition.command;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import nts.uk.ctx.hr.develop.app.sysoperationset.businessrecognition.dto.MenuApprovalSettingsDto;
import nts.uk.ctx.hr.develop.dom.sysoperationset.businessrecognition.MenuApprovalSettingsRepository;

public class MenuApprovalSettingsCommand {

	@Inject
	private MenuApprovalSettingsRepository repo;
	
	public void update(List<MenuApprovalSettingsDto> dto) {
		repo.update(dto.stream().map(c->c.toDomain()).collect(Collectors.toList()));
	}
}
