package nts.uk.ctx.at.record.ws.log;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.log.EmpCalAndSumExeLogFinder;
import nts.uk.ctx.at.record.app.find.log.dto.EmpCalAndSumExeLogDto;
import nts.uk.ctx.at.record.app.find.log.dto.InputEmpCalAndSum;
import nts.uk.ctx.at.record.app.find.log.dto.InputEmpCalAndSumByDate;

@Path("at/record/log")
@Produces("application/json")
public class EmpCalAndSumExeLogWebService extends WebService {
	
	@Inject
	private EmpCalAndSumExeLogFinder empCalAndSumExeLogFinder;
	/**
	 * get all EmpCalAndSumExeLog
	 * @return
	 */
	@POST
	@Path("getall")
	public List<EmpCalAndSumExeLogDto> getAllEmpCalAndSumExeLog(){
		return this.empCalAndSumExeLogFinder.getAllEmpCalAndSumExeLog();
	}
	/**
	 * get EmpCalAndSumExeLog by code
	 * @param inputEmpCalAndSum
	 * @return
	 */
	@POST
	@Path("getbycode")
	public EmpCalAndSumExeLogDto getEmpCalAndSumExeLogByID(InputEmpCalAndSum inputEmpCalAndSum){
		return this.empCalAndSumExeLogFinder.getEmpCalAndSumExeLogById(inputEmpCalAndSum);
		
	}
	/**
	 * get list EmpCalAndSumExeLog by date 
	 * @param inputEmpCalAndSumByDate
	 * @return
	 */
	@POST
	@Path("getallbydate")
	public List<EmpCalAndSumExeLogDto> getAllEmpCalAndSumExeLog(InputEmpCalAndSumByDate inputEmpCalAndSumByDate){
		return this.empCalAndSumExeLogFinder.getEmpCalAndSumExeLogByDate(inputEmpCalAndSumByDate);	
		
	}
	
	/**
	 * get list EmpCalAndSumExeLog by  getByEmpCalAndSumExecLogID
	 * @param empCalAndSumExecLogID
	 * @return
	 */
	@POST
	@Path("getallbyempid/{empCalAndSumExecLogID}")
	public List<EmpCalAndSumExeLogDto> getByEmpCalAndSumExecLogID(@PathParam("empCalAndSumExecLogID")String empCalAndSumExecLogID){
		return this.empCalAndSumExeLogFinder.getByEmpCalAndSumExecLogID(empCalAndSumExecLogID);	
		
	}
		
}
