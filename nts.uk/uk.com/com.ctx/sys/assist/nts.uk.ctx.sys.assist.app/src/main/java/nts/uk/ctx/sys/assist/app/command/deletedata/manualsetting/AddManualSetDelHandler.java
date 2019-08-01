/**
 * 
 */
package nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.sys.assist.dom.deletedata.CategoryDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeeDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeesDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletion;
import nts.uk.ctx.sys.assist.dom.deletedata.ManualSetDeletionRepository;
import nts.uk.ctx.sys.assist.dom.deletedata.SysEmployeeDelAdapter;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * @author hiep.th
 *
 */
@Stateless
public class AddManualSetDelHandler extends CommandHandlerWithResult<ManualSetDelCommand, String> {
	@Inject
	private ManualSetDeletionRepository repo;
	@Inject
	private EmployeesDeletionRepository repoEmp;
	@Inject
	private CategoryDeletionRepository repoCate;
	@Inject
	private ManualSetDeletionService manualSetDeletionService;
	@Inject
	private SysEmployeeDelAdapter syEmployeeAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected String handle(CommandHandlerContext<ManualSetDelCommand> context) {
		ManualSetDelCommand manualSetCmd = context.getCommand();
		String delId = IdentifierUtil.randomUniqueId();
		
		 // get login info
        LoginUserContext loginUserContext = AppContexts.user();
         // get company id
        String cid = loginUserContext .companyId();
        String sid = loginUserContext.userId();
        manualSetCmd.setExecutionDateAndTime(GeneralDateTime.now());
        
		ManualSetDeletion domain = manualSetCmd.toDomain(delId, cid, sid);
		
		// 画面の保存対象社員から「社員指定の有無」を判定する ( check radio button )presenceOfEmployee
		if (manualSetCmd.getHaveEmployeeSpecifiedFlg() == 1) {
			// 指定社員の有無＝「する」
			repoEmp.addAll(manualSetCmd.getEmployees(delId));
		} else if (manualSetCmd.getHaveEmployeeSpecifiedFlg() == 0) {
			// 指定社員の有無＝「しない」の場合」
			List<EmployeeDeletion>  lstEmpDelAll = syEmployeeAdapter.getListEmployeeByCompanyId(cid);
			lstEmpDelAll.stream().map(x -> {
				x.setDelId(delId);
				return x;
			}).collect(Collectors.toList());
			repoEmp.addAll(lstEmpDelAll);
		}
		
		repoCate.addAll(manualSetCmd.getCategories(delId));
		repo.addManualSetting(domain);
		
		try {
			manualSetDeletionService.start(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return delId;
	}
}
