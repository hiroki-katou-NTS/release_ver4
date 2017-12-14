/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.app.find.singlesignon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.gateway.dom.adapter.employee.EmployeeInfoAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.employee.EmployeeInfoDtoImport;
import nts.uk.ctx.sys.gateway.dom.adapter.person.PersonInfoAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.person.PersonInfoImport;
import nts.uk.ctx.sys.gateway.dom.login.User;
import nts.uk.ctx.sys.gateway.dom.login.UserRepository;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.OtherSysAccountRepository;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccount;
import nts.uk.ctx.sys.gateway.dom.singlesignon.WindowAccountRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class UserFinder.
 */
@Stateless
public class UserInfoFinder {

	/** The person info adapter. */
	@Inject
	private PersonInfoAdapter personInfoAdapter;

	/** The employee info adapter. */
	@Inject
	private EmployeeInfoAdapter employeeInfoAdapter;

	/** The user repo. */
	@Inject
	private UserRepository userRepo;

	/** The window account repository. */
	@Inject
	private WindowAccountRepository windowAccountRepository;
	
	/** The other sys account repository. */
	@Inject
	private OtherSysAccountRepository otherSysAccountRepository;

	/**
	 * Find list user info.
	 *
	 * @param baseDate
	 *            the base date
	 * @return the list
	 */
	public List<UserDto> findListUserInfo(GeneralDate baseDate) {

		// Get Company Id
		String companyId = AppContexts.user().companyId();

		// get employee code
		List<EmployeeInfoDtoImport> listEmpl = this.employeeInfoAdapter.getEmployeesAtWorkByBaseDate(companyId,
				baseDate);
		
		List<String> listPersonId = new ArrayList<>();
		List<UserDto> listUserMap = new ArrayList<>();

		// Set<UserDto> listUserDto = new HashSet<>();

		List<User> listUser = new ArrayList<>();
		List<UserDto> listUserAccount = new ArrayList<>();
		
		Set<String> list = new HashSet<>();

		// Step 1 - add employee info
		listEmpl.forEach(employee -> {
			UserDto userDto = new UserDto();
			userDto.setEmployeeCode(employee.getEmployeeCode());
			userDto.setPersonId(employee.getPersonId());
			userDto.setEmployeeId(employee.getEmployeeId());
			listUserAccount.add(userDto);
			list.add(employee.getPersonId());
		});
		
		// reject duplicate element
		listPersonId.addAll(list);

		// Step 2 - add person info
		

		List<PersonInfoImport> listPerson = this.personInfoAdapter.getListPersonInfo(listPersonId);
		Map<String, PersonInfoImport> mapPerson = listPerson.stream()
				.collect(Collectors.toMap(PersonInfoImport::getPersonId, Function.identity()));

		listUserAccount.forEach(item -> {
			PersonInfoImport personInfoImport = mapPerson.get(item.getPersonId());
			if (personInfoImport != null) {
				item.setPersonName(personInfoImport.getPersonName());
			}

		});

		// Step 3 - add user info
		for (String personId : listPersonId) {
			Optional<User> user = userRepo.getByAssociatedPersonId(personId);
			if (user.isPresent()) {
				listUser.add(user.get());
			}
		}

		Map<String, User> mapUser = listUser.stream()
				.collect(Collectors.toMap(User::getAssociatedPersonId, Function.identity()));

		listUserAccount.forEach(item -> {
			User user = mapUser.get(item.getPersonId());
			if (user != null) {
				item.setLoginId(user.getLoginId().toString());
				item.setUserId(user.getUserId());
			}

		});

		listUserAccount.forEach(item -> {
			if (mapUser.get(item.getPersonId()) != null && mapPerson.get(item.getPersonId()) != null) {
				listUserMap.add(item);
			}
		});

		return loadUserSetting(listUserMap);
				
	}
	
	public List<UserDto> loadUserSetting(List<UserDto> listUserMap){
				
		listUserMap.forEach(w -> {
			
			List<WindowAccount> winAcc = this.windowAccountRepository.findByUserId(w.getUserId());
			Optional<OtherSysAccount> opOtherSysAcc = otherSysAccountRepository.findByUserId(w.getUserId());
			if(winAcc.isEmpty() && !opOtherSysAcc.isPresent()){
				w.setIsSetting(false);
			}			
			else{
				w.setIsSetting(true);
			}
		});	
		
		return listUserMap;
	}
	
	
	
	
}
