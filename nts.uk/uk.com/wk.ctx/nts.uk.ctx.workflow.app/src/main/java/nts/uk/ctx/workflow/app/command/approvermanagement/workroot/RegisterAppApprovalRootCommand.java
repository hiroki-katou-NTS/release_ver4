package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import java.util.List;

import lombok.Value;

@Value
public class RegisterAppApprovalRootCommand {

	/**就業ルート区分: 会社(0)　－　職場(1)　－　社員(2)*/
	private int rootType;
	/**work place id*/
	private String workpplaceId;
	/** employee id*/
	private String employeeId;
	/**check add history*/
	private boolean checkAddHist;
	/**list right*/
	private List<CompanyAppRootADto> root;
	/**ls is selected*/
	private AddHistoryDto addHist;
	//list db
	private List<AppType> lstAppType;
	private String startDate;
	private String endDate;
}
