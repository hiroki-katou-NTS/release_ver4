package nts.uk.ctx.sys.auth.app.find.person.role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.auth.app.find.person.role.dto.RoleDto;
import nts.uk.ctx.sys.auth.dom.role.Role;
import nts.uk.ctx.sys.auth.dom.role.RoleRepository;
import nts.uk.ctx.sys.auth.dom.role.RoleType;
import nts.uk.ctx.sys.auth.dom.role.personrole.PersonRole;
import nts.uk.ctx.sys.auth.dom.role.personrole.PersonRoleRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class PersonInformationRoleFinder {
	@Inject
	private RoleRepository roleRepo;

	@Inject
	private PersonRoleRepository personRoleRepo;

	public List<PersonInformationRole> find() {
		List<PersonInformationRole> result = new ArrayList<PersonInformationRole>();
		
		String companyId = AppContexts.user().companyId();
		// get domain role
		List<Role> roles = roleRepo.findByType(companyId, RoleType.PERSONAL_INFO.value);
		if (roles !=null && !roles.isEmpty()) {
			List<String> roleIds = roles.stream().map(x ->x.getRoleId()).collect(Collectors.toList());
			Map<String, PersonRole> mapPerson = personRoleRepo.find(roleIds).stream().collect(Collectors.toMap(PersonRole::getRoleId,  Function.identity()));
			PersonInformationRole personInfoRole = new PersonInformationRole();
			roles.forEach(role ->{
				personInfoRole.setRoleId(role.getRoleId());
				personInfoRole.setAssignAtr(role.getAssignAtr());
				personInfoRole.setEmployeeReferenceRange(role.getEmployeeReferenceRange());
				personInfoRole.setRoleType(role.getRoleType());
				personInfoRole.setRoleCode(role.getRoleCode().toString());
				
				// get domain PersonRole
				PersonRole personRole = mapPerson.get(role.getRoleId());				
				personInfoRole.setReferFutureDate(personRole.getReferFutureDate());
				result.add(personInfoRole);
			});
		}	
		return result;
	}
	
	
	public List<RoleDto> getListRoleByRoleType(int roleType ){
		String companyId = AppContexts.user().companyId();
		List<RoleDto> data =  roleRepo
				.findByType(companyId,roleType)
				.stream().map( c ->RoleDto.fromDomain(c) ).collect(Collectors.toList());
		if(data.isEmpty()) {
			return Collections.emptyList();
		}
		return data;
	}
}
