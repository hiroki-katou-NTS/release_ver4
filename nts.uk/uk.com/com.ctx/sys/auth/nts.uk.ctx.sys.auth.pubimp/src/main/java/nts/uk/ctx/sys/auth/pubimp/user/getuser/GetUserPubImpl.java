package nts.uk.ctx.sys.auth.pubimp.user.getuser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.auth.dom.adapter.person.PersonAdapter;
import nts.uk.ctx.sys.auth.dom.adapter.person.PersonImport;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.ctx.sys.auth.pub.user.getuser.GetUserDto;
import nts.uk.ctx.sys.auth.pub.user.getuser.GetUserPublish;

@Stateless
public class GetUserPubImpl implements GetUserPublish {

	@Inject
	private UserRepository userRepo;

	@Inject
	private PersonAdapter personAdapter;

	@Override
	public List<GetUserDto> getUser(List<String> userIds) {
		List<User> users = userRepo.getByListUser(userIds);
		List<String> associatedPersonIdList = users.stream()
				.filter(x -> x.getAssociatedPersonID().get() != null && !x.getAssociatedPersonID().equals(""))
				.map(x -> x.getAssociatedPersonID().get()).collect(Collectors.toList());
		Map<String, PersonImport> personImportList = personAdapter.findByPersonIds(associatedPersonIdList).stream()
				.collect(Collectors.toMap(x -> x.getPersonId(), x -> x));

		return users.stream().map(user -> {
			GetUserDto dto = createDto(user);

			// change user-name
			PersonImport personImport = personImportList.get(user.getAssociatedPersonID());
			if (personImport != null) {
				dto.setUserName(Optional.of(personImport.getPersonName()));
		
			}
			return dto;
		}).collect(Collectors.toList());

	}

	@Override
	public Optional<GetUserDto> getUserWithPersonId(String personId) {
		Optional<User> _user = userRepo.getByAssociatedPersonId(personId);
		if (!_user.isPresent()) {
			return Optional.empty();
		}
		User user = _user.get();
		return Optional.of(createDto(user));
	}

	private GetUserDto createDto(User user) {
		return new GetUserDto(user.getUserID(),
				user.getLoginID().v(),
				user.getUserName().isPresent() ? user.getUserName().get().v() : Optional.empty().toString(),
				user.getAssociatedPersonID().isPresent() ? user.getUserName().get().toString() : Optional.empty().toString(),
				user.getMailAddress().isPresent() ? user.getMailAddress().get().v() : Optional.empty().toString(),
				user.getPassword().v());
	}

}
