package nts.uk.screen.com.ws;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.screen.com.app.command.company.Cmm001AddCommand;
import nts.uk.screen.com.app.command.company.Cmm001AddCommandHandler;
import nts.uk.screen.com.app.command.company.Cmm001DeleteCommand;
import nts.uk.screen.com.app.command.company.Cmm001DeleteCommandHandler;
import nts.uk.screen.com.app.command.company.Cmm001UpdateCommand;
import nts.uk.screen.com.app.command.company.Cmm001UpdateCommandHandler;
import nts.uk.screen.com.app.repository.company.CompanyQueryDto;
import nts.uk.screen.com.app.repository.company.CompanyQueryRepository;
/**
 * 
 * @author yennth
 *
 */
@Path("screen/com/cmm001")
@Produces("application/json")
public class Cmm001WebService extends WebService{
	@Inject
	private Cmm001UpdateCommandHandler update;
	
	@Inject
	private Cmm001AddCommandHandler add;
	
	@Inject
	private Cmm001DeleteCommandHandler delete;
	
	@Inject
	private CompanyQueryRepository companyQueryRepo;
	
	/**
	 * update cmm001
	 * @param cm
	 */
	@POST
	@Path("update")
	public void update(Cmm001UpdateCommand cm){
		this.update.handle(cm);
	}
	
	/**
	 * insert cmm001
	 * @param cm
	 */
	@POST
	@Path("add")
	public void add(Cmm001AddCommand cm){
		this.add.handle(cm);
	}
	
	/**
	 * delete cmm001  
	 * @param cm
	 */
	@POST
	@Path("del")
	public void del(Cmm001DeleteCommand cm){
		this.delete.handle(cm);
	}
	
	/**
	 * delete cmm001  
	 * @param cm
	 */
	@POST
	@Path("findAll")
	public List<CompanyQueryDto> findAll() {
		return this.companyQueryRepo.findAll();
	}
}
