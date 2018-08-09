package nts.uk.ctx.sys.auth.pubimp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.auth.dom.adapter.employee.employeeinfo.EmpInfoImport;
import nts.uk.ctx.sys.auth.dom.user.User;
import nts.uk.ctx.sys.auth.dom.user.UserRepository;
import nts.uk.ctx.sys.auth.dom.wkpmanager.EmpInfoAdapter;
import nts.uk.ctx.sys.auth.pub.user.GetUserByEmpPublisher;
import nts.uk.ctx.sys.auth.pub.user.UserAuthDto;
@Stateless
public class GetUserByEmpPublisherImpl implements GetUserByEmpPublisher{

	
	@Inject
	private EmpInfoAdapter empInfoAdapter;
	
	@Inject
	private UserRepository userRepo;
	@Override
	public List<UserAuthDto> getByListEmp(List<String> listEmpID) {
		// Lay request 61
		List<EmpInfoImport> empInforLst = empInfoAdapter.getEmpInfo(listEmpID);
		List<String> listPID = empInforLst.stream().map(c -> c.getPersonId()).collect(Collectors.toList());

		if(empInforLst.isEmpty()){
			return new ArrayList<>();
		}else{
		List<User> listUser  = 	userRepo.getListUserByListAsID(listPID);
		List<UserAuthDto> result = new ArrayList<>();

		for (EmpInfoImport em : empInforLst) {
			User user = listUser.stream().filter(c -> c.getAssociatedPersonID().equals(em.getPersonId())).findFirst().get();
					
			UserAuthDto u = new UserAuthDto(user.getUserID(),
					user.getUserName().get().v(),
					user.getLoginID().v(),
					em.getEmployeeId(),
					em.getEmployeeCode(),
					em.getPerName());
			result.add(u);
		}
		return result;
		}
	}

}
