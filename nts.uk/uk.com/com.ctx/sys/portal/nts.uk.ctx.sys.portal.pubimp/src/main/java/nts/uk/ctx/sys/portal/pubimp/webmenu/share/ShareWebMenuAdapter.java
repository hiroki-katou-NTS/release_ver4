package nts.uk.ctx.sys.portal.pubimp.webmenu.share;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.scoped.request.RequestContextProvider;
import nts.uk.ctx.sys.portal.dom.adapter.person.PersonInfoAdapter;
import nts.uk.ctx.sys.portal.dom.adapter.role.LoginResponsibleDto;
import nts.uk.ctx.sys.portal.dom.adapter.role.RoleGrantAdapter;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.AppContextsConfig;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.menu.adapter.ProgramNameDto;
import nts.uk.shr.com.menu.adapter.ShareMenuAdapter;

@Stateless
public class ShareWebMenuAdapter implements ShareMenuAdapter {

	@Inject
	private StandardMenuRepository standardMenuRepository;
	
	@Inject
	private PersonInfoAdapter personInfoAdapter;
	
	@Inject
	private RoleGrantAdapter roleGrantAdapter;
	
	@Override
	public List<ProgramNameDto> getProgramName(String pgId, String companyId) {
//		String companyId = AppContexts.user().companyId();
//		String pgId = RequestContextProvider.get().get(AppContextsConfig.KEY_PROGRAM_ID);
		if (pgId == null) return new ArrayList<>();
		String programId = pgId, screenId = null;
		if (pgId.length() > 6) {
			 programId = pgId.substring(0, 6);
			 screenId = pgId.substring(6);
		}
		return standardMenuRepository.getProgram(companyId, programId, screenId).stream()
				.map(m -> new ProgramNameDto(m.getQueryString(), pgId + " " + m.getDisplayName()))
				.collect(Collectors.toList());
	}

	@Override
	public String userName() {
		LoginUserContext userCtx = AppContexts.user();
		return personInfoAdapter.getBusinessName(userCtx.employeeId()); 
	}

	@Override
	public boolean showManual() {
		LoginUserContext userCtx = AppContexts.user();
		if (userCtx.roles().forSystemAdmin() != null || userCtx.roles().forCompanyAdmin() != null) 
			return true;
		
		LoginResponsibleDto responsible = roleGrantAdapter.getLoginResponsible();
		if (responsible.isWork() || responsible.isSalary() || responsible.isPersonalInfo())
			return true;
		
		return false;
	}
}
