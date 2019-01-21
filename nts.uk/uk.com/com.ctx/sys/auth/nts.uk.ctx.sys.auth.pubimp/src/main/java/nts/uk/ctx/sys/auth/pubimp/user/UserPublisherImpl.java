package nts.uk.ctx.sys.auth.pubimp.user;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.pub.person.IPersonInfoPub;
import nts.uk.ctx.bs.employee.pub.person.PersonInfoExport;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.ctx.sys.auth.pub.user.UserDto;
import nts.uk.ctx.sys.auth.pub.user.UserExport;
import nts.uk.ctx.sys.auth.pub.user.UserInforEx;
import nts.uk.ctx.sys.auth.pub.user.UserPublisher;

@Stateless
public class UserPublisherImpl implements UserPublisher {

	@Inject
	private UserRepository userRepo;
	
	@Inject
	private IPersonInfoPub iPersonInfoPub;
	
	@Override
	public Optional<UserDto> getUserInfo(String userId) {
		return Optional.ofNullable(toDto(userRepo.getByUserID(userId).orElse(null)));
	}
	
	private UserDto toDto(User user) {
		return user != null 
				? new UserDto(user.getUserID(),
						user.getUserName().isPresent() ? user.getUserName().get().v() : "",
						user.getAssociatedPersonID().isPresent() ? user.getAssociatedPersonID().get() : "" ) 
				: null;
	}

	@Override
	public Optional<UserExport> getUserByContractAndLoginId(String contractCode, String loginId) {
		Optional<User> optUser = userRepo.getByContractAndLoginId(contractCode, loginId);
		if (optUser.isPresent())
			return Optional.of(fromDomain(optUser.get()));
		return Optional.empty();
	}

	@Override
	public Optional<UserExport> getUserByAssociateId(String associatePersonId) {
		Optional<User> optUser = userRepo.getByAssociatedPersonId(associatePersonId);
		if (optUser.isPresent())
			return Optional.of(fromDomain(optUser.get()));
		return Optional.empty();
	}

	private UserExport fromDomain(User domain) {
		return new UserExport(domain.getUserID(), domain.getLoginID().v(), domain.getContractCode().v(),
				domain.getUserName().isPresent() ? domain.getUserName().get().v() : "",
				domain.getPassword().v(),
				domain.getMailAddress().isPresent() ? domain.getMailAddress().get().v() : "",
				domain.getAssociatedPersonID().isPresent() ? domain.getAssociatedPersonID().get() : "",
				domain.getExpirationDate(),
				domain.getPassStatus().value);
	}

	@Override
	public List<UserExport> getListUserByListAsId(List<String> listAssociatePersonId) {
		List<UserExport> listUser = userRepo.getListUserByListAsID(listAssociatePersonId).stream()
				.map(c->fromDomain(c)).collect(Collectors.toList());
		if(listUser.isEmpty())
			return Collections.emptyList();
		return listUser;
	}

	@Override
	public Optional<UserExport> getByUserId(String userId) {
		Optional<User> optUser = userRepo.getByUserID(userId);
		if (optUser.isPresent()) {
			return Optional.of(this.fromDomain(optUser.get()));
		}
		return Optional.empty();
	}


	@Override
	public Optional<UserInforEx> getByEmpID(String empID) {
		// Lay RequestList No.1
		PersonInfoExport exportData = iPersonInfoPub.getPersonInfo(empID);
		if(exportData == null){
			return Optional.empty();
		}
		else{
	      Optional<User> user = userRepo.getByAssociatedPersonId(exportData.getPid());
	      if(!user.isPresent()){
	    	  return Optional.empty();
	      }
	      return Optional.of( new UserInforEx(
	    		  user.get().getUserID(),
	    		  user.get().getLoginID().v(),
	    		  exportData.getEmployeeId(),
	    		  exportData.getEmployeeCode()));
		}
		
	}

	@Override
	public Optional<UserExport> getByUserIDandDate(String userID, GeneralDate systemDate) {
		Optional<User> optUser = userRepo.getByUserIDAndDate(userID, systemDate);
		if (!optUser.isPresent()) {
			return Optional.empty();
		} else{
			return Optional.of(fromDomain(optUser.get()));
		}
	}

	@Override
	public Optional<UserExport> getBySid(String sid) {
		// Lay RequestList No.1
		PersonInfoExport exportData = iPersonInfoPub.getPersonInfo(sid);
		if (exportData == null) {
			return Optional.empty();
		} else {
			Optional<User> opUser = userRepo.getByAssociatedPersonId(exportData.getPid());
			if (!opUser.isPresent()) {
				return Optional.empty();
			}
			User user = opUser.get();
			return Optional.of(this.fromDomain(user));
		}
	}
}
