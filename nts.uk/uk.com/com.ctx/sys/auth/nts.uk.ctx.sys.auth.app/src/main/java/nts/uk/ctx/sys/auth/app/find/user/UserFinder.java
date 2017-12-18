package nts.uk.ctx.sys.auth.app.find.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmployeeInfoAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmployeeInfoImport;
import nts.uk.ctx.sys.auth.dom.user.DisabledSegment;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserName;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class UserFinder {

	@Inject
	private UserRepository userRepo;
	
	@Inject
	private EmployeeInfoAdapter employeeInfoAdapter;
	
	public List<UserDto> searchUser(String userNameID ){
		GeneralDate date = GeneralDate.today();
		if(userNameID == null){
			throw new BusinessException("Msg_438");
		}
		List<UserDto> listUserDto = userRepo.searchUser(userNameID, date).stream().map(c -> UserDto.fromDomain(c)).collect(Collectors.toList());
		//Sort
		listUserDto = listUserDto.stream().sorted(Comparator.comparing(UserDto::getUserID)).collect(Collectors.toList());
		return listUserDto;
	
		
	}
	public List<UserDto> getAllUser(){
		return userRepo.getAllUser().stream().map(c -> UserDto.fromDomain(c)).collect(Collectors.toList());
	}
	
	public List<UserDto> findByKey(UserKeyDto userKeyDto) {
		String companyId = AppContexts.user().companyId();
		
		List<UserDto> result =  new ArrayList<UserDto>();
		
		DisabledSegment specialUser = EnumAdaptor.valueOf(userKeyDto.isSpecial() ? 1 : 0, DisabledSegment.class);
		DisabledSegment multiCompanyConcurrent = EnumAdaptor.valueOf(userKeyDto.isMulti() ? 1 : 0, DisabledSegment.class);
		List<User> listUser = userRepo.searchBySpecialAndMulti(GeneralDate.today(), specialUser.value, multiCompanyConcurrent.value);		
		
		if (!userKeyDto.isMulti() && !userKeyDto.isSpecial()) {
			List<EmployeeInfoImport> listEmployeeInfo = employeeInfoAdapter.getEmployeesAtWorkByBaseDate(companyId, GeneralDate.today());
			if (listEmployeeInfo.isEmpty())
				return result;
			
			for (User user : listUser) {
				Optional<EmployeeInfoImport> associatedEmployee = listEmployeeInfo.stream().filter(c -> c.getPersonId().equals(user.getAssociatedPersonID())).findFirst();
				if (associatedEmployee.isPresent()) {
					if (user.getUserName().v().toLowerCase().contains(userKeyDto.getKey().toLowerCase()) ||
						associatedEmployee.get().getEmployeeName().toLowerCase().contains(userKeyDto.getKey().toLowerCase())) {
						user.setUserName(new UserName(associatedEmployee.get().getEmployeeName()));
						result.add(UserDto.fromDomain(user));
					}
				}
			}
			return result;
		}
		
		result = listUser.stream().filter(c -> c.getUserName().v().toLowerCase().contains(userKeyDto.getKey().toLowerCase())).
				map(c -> UserDto.fromDomain(c)).collect(Collectors.toList());
		return result;
	}
	
 	
}
