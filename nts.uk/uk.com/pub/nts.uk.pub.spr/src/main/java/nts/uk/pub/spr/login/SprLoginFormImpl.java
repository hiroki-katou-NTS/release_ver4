package nts.uk.pub.spr.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.pub.employee.SyEmployeePub;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmpInfoExport;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.EmployeeInfoPub;
import nts.uk.ctx.bs.employee.pub.spr.EmployeeSprPub;
import nts.uk.ctx.bs.employee.pub.spr.export.EmpSprExport;
import nts.uk.ctx.sys.auth.pub.spr.UserSprExport;
import nts.uk.ctx.sys.auth.pub.spr.UserSprPub;
import nts.uk.pub.spr.login.output.LoginUserContextSpr;
import nts.uk.pub.spr.login.output.RoleInfoSpr;
import nts.uk.pub.spr.login.output.RoleTypeSpr;
import nts.uk.pub.spr.login.paramcheck.LoginParamCheck;
import nts.uk.shr.com.context.loginuser.LoginUserContextManager;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class SprLoginFormImpl implements SprLoginFormService {
	
	@Inject
	private UserSprPub userSprPub;
	
	@Inject
	private EmployeeSprPub employeeSprPub;
	
	@Inject
	private LoginParamCheck loginParamCheck;
	
	@Inject
	private EmployeeInfoPub employeeInfoPub;
	
	@Inject
	private SyEmployeePub syEmployeePub;
	
	@Inject
	private LoginUserContextManager loginUserContextManager;
	
	@Override
	public LoginUserContextSpr loginFromSpr(String menuCD, String loginEmployeeCD, String employeeCD, String startTime, 
			String endTime, String date, String selectType, String appID, String reason, String stampFlg){
		// 契約コード固定：　000000000000
		// 会社コード固定：　0001
		// 会社ID固定：　000000000000-0001
		String companyID = "000000000000-0001";
		
		// アルゴリズム「パラメータチェック」を実行する
		String employeeID = this.paramCheck(menuCD, loginEmployeeCD, employeeCD, startTime, endTime, date, selectType, appID, reason, stampFlg);
		
		// （基幹・社員Export）アルゴリズム「「会社ID」「社員コード」より社員基本情報を取得」を実行する　RequestList No.18
		Optional<EmpSprExport> opEmployeeSpr = employeeSprPub.getEmployeeID(companyID, loginEmployeeCD.trim());
		if(!opEmployeeSpr.isPresent()){
			throw new BusinessException("Msg_301", "Com_User");
		}
		// アルゴリズム「指定の個人IDから在籍社員を取得」を実行する
		List<EmpInfoExport> empInfoExportLst = employeeInfoPub.getEmpInfoByPid(opEmployeeSpr.get().getPersonID());
		if(CollectionUtil.isEmpty(empInfoExportLst)){
			throw new BusinessException("Msg_301", "Com_User");
		}
		// アルゴリズム「社員が削除されたかを取得」を実行する
		boolean isEmpDelete = syEmployeePub.isEmployeeDelete(opEmployeeSpr.get().getEmployeeID());
		if(isEmpDelete){
			throw new BusinessException("Msg_301", "Com_User");
		}
		return this.generateSession(loginEmployeeCD.trim(), opEmployeeSpr.get().getEmployeeID(), opEmployeeSpr.get().getPersonID(), employeeID);
	}

	@Override
	public String paramCheck(String menuCD, String loginEmployeeCD, String employeeCD, String startTime, 
			String endTime, String date, String selectType, String appID, String reason, String stampFlg) {
		// アルゴリズム「パラメータチェック（共通）」を実行する
		this.paramCheckBasic(menuCD, loginEmployeeCD);
		Integer menuValue = Integer.valueOf(menuCD);
		String employeeID = null;
		// 遷移先画面をチェックする
		switch (menuValue) {
		case 1:
			// アルゴリズム「パラメータチェック（事前早出申請）」を実行する
			employeeID = loginParamCheck.checkParamPreApp(employeeCD, startTime, date, reason);
			break;
		case 2:
			// アルゴリズム「パラメータチェック（事前残業申請）」を実行する
			employeeID = loginParamCheck.checkParamOvertime(employeeCD, endTime, date, reason);
			break;
		case 3: 
			// アルゴリズム「パラメータチェック（日別実績の修正）」を実行する
			employeeID = loginParamCheck.checkParamAdjustDaily(employeeCD, startTime, endTime, date, reason, stampFlg);
			break;
		case 4: 
			// アルゴリズム「パラメータチェック（承認一覧）」を実行する
			loginParamCheck.checkParamApprovalList(date, selectType);
			break;
		case 5: 
			// アルゴリズム「パラメータチェック（日別実績の確認）」を実行する
			loginParamCheck.checkParamConfirmDaily(date);
			break;
		case 6:
			// アルゴリズム「パラメータチェック（残業申請確認）」を実行する
			loginParamCheck.checkParamConfirmOvertime(appID);
			break;
		default:
			break;
		}
		return employeeID;
	}
	
	@Override
	public void paramCheckBasic(String menuCD, String loginEmployeeCD) {
		// フォームデータ「ログイン社員コード(loginemployeeCode)」を取得する
		if(Strings.isBlank(loginEmployeeCD)){
			throw new BusinessException("Msg_999", "Msg_1026");
		}
		// ログイン社員コード(loginemployeeCode)をチェックする
		employeeSprPub.validateEmpCodeSpr(loginEmployeeCD.trim());
		// フォームデータ「遷移先画面(menu)」を取得する
		Integer menuValue = null;
		try {
			menuValue = Integer.valueOf(menuCD);
		} catch (NumberFormatException e) {
			throw new BusinessException("Msg_1011", "Msg_1026");
		}
		// 遷移先画面(menu)をチェックする
		if(menuValue > 7 | menuValue < 0){
			throw new BusinessException("Msg_1011", menuCD);
		}
	}

	@Override
	public LoginUserContextSpr generateSession(String loginEmployeeCD, String loginEmployeeID, String personID, String employeeID) {
		// （権限管理Export）アルゴリズム「紐付け先個人IDからユーザを取得する」を実行する(thuc hien thuat toan 「紐付け先個人IDからユーザを取得する」 )
		Optional<UserSprExport> opUserSpr = userSprPub.getUserSpr(personID);
		if(!opUserSpr.isPresent()){
			throw new BusinessException("Msg_301");
		}
		UserSprExport userSpr = opUserSpr.get();
		loginUserContextManager.loggedInAsEmployee(
				userSpr.getUserID(), 
				personID, 
				"000000000000", 
				"000000000000-0001", 
				"0001", 
				loginEmployeeID, 
				loginEmployeeCD);
		// 権限（ロール）情報を取得、設定する(lay thong tin quuyen han (role) roi setting)
		List<RoleInfoSpr> roleList = this.getRoleInfo(userSpr.getUserID());
		return new LoginUserContextSpr(
				userSpr.getUserID(), 
				"000000000000", // 固定
				"000000000000-0001", // 固定
				"0001", // 固定
				personID, 
				loginEmployeeID, 
				loginEmployeeCD, 
				roleList,
				employeeID);
		
	}

	@Override
	public List<RoleInfoSpr> getRoleInfo(String userID) {
		// 契約コード固定：　000000000000
		// 会社コード固定：　0001
		// 会社ID固定：　000000000000-0001
		String companyID = "000000000000-0001";
		List<RoleInfoSpr> roleList = new ArrayList<>();
		Optional<String> resultRole = Optional.empty();
		// ロール種類＝就業
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 3);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.EMPLOYMENT));
		}
		
		// ロール種類＝給与
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 4);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.SALARY));
		}
		
		// ロール種類＝人事
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 5);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.HUMAN_RESOURCE));
		}
		
		// ロール種類＝オフィスヘルパー
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 6);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.OFFICE_HELPER));
		}
		
		/*
		// ロール種類＝会計
		resultRole = userSprInterface.getRoleFromUserId(userID, roleType, date);
		if(Strings.isNotBlank(resultRole)){
			role = resultRole;
		}
		*/
		
		// ロール種類＝マイナンバー
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 7);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.MY_NUMBER));
		}
		
		// ロール種類＝グループ会社管理
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 2);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.GROUP_COMAPNY_MANAGER));
		}
		
		// ロール種類＝会社管理者
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 1);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.COMPANY_MANAGER));
		}
		
		// ロール種類＝システム管理者
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 0);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.SYSTEM_MANAGER));
		}
		
		// ロール種類＝個人情報
		resultRole = userSprPub.getRoleFromUserId(companyID, userID, 8);
		if(resultRole.isPresent()){
			roleList.add(new RoleInfoSpr(resultRole.get(), RoleTypeSpr.PERSONAL_INFO));
		}
		
		return roleList;
	}
}
