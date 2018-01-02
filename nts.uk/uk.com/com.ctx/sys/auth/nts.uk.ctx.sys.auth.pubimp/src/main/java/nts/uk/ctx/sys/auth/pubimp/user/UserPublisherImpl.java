package nts.uk.ctx.sys.auth.pubimp.user;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.ctx.sys.auth.pub.user.UserDto;
import nts.uk.ctx.sys.auth.pub.user.UserExport;
import nts.uk.ctx.sys.auth.pub.user.UserPublisher;

@Stateless
public class UserPublisherImpl implements UserPublisher {

	@Inject
	private UserRepository userRepo;
	
	@Override
	public Optional<UserDto> getUserInfo(String userId) {
		return Optional.ofNullable(toDto(userRepo.getByUserID(userId).orElse(null)));
	}
	
	private UserDto toDto(User user) {
		return user != null 
				? new UserDto(user.getUserID(), user.getUserName().v(), user.getAssociatedPersonID()) 
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
				domain.getUserName().v(), domain.getPassword().v(), domain.getMailAddress().v(),
				domain.getAssociatedPersonID(), domain.getExpirationDate());
	}
	
}
