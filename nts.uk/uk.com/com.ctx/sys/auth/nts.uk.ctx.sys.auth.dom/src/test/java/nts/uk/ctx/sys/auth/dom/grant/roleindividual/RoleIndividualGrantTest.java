package nts.uk.ctx.sys.auth.dom.grant.roleindividual;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import lombok.val;
import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import nts.arc.testing.assertion.NtsAssert;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.sys.auth.dom.role.RoleType;
@RunWith(JMockit.class)
public class RoleIndividualGrantTest {
	
	@Injectable
	RoleIndividualGrant.Require require;
	
	@Test
	public void getters() {
		
		val roleGrant = new RoleIndividualGrant( "userId"
				,	"companyId", RoleType.PERSONAL_INFO
				,	"roleId"
				,	new DatePeriod( GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd( 9999, 12, 31))
					);
		
		NtsAssert.invokeGetters( roleGrant );
		
	}
	
	/**
	 * target: createFromRole
	 */
	@Test
	public void testCreateFromRole() {
		
		val role = RoleIndividualGrantHelper.createRole( "company-id-of-role", "roleId", RoleType.PERSONAL_INFO );
		val grantTargetUser = "userId";
		val grantTargetCompany = "cid";
		val validPeriod = new DatePeriod( GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd( 9999, 12, 31));
		
		//Act
		RoleIndividualGrant result = NtsAssert.Invoke.staticMethod(	RoleIndividualGrant.class, "createFromRole"
				,	role, grantTargetUser, grantTargetCompany, validPeriod );
		
		//Assert
		assertThat( result.getUserId()).isEqualTo( grantTargetUser );
		assertThat( result.getCompanyId() ).isEqualTo( grantTargetCompany );
		assertThat( result.getRoleType() ).isEqualTo( role.getRoleType() );
		assertThat( result.getRoleId() ).isEqualTo( role.getRoleId() );
		assertThat( result.getValidPeriod().start() ).isEqualTo( validPeriod.start() );
		assertThat( result.getValidPeriod().end() ).isEqualTo( validPeriod.end() );
		
	}

	/**
	 * target : createGrantInfoOfSystemMananger
	 */
	@Test
	public void testCreateGrantInfoOfSystemMananger() {
		val grantTargetUser = "userId";
		val period = new DatePeriod( GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd( 9999, 12, 31));
		val role = RoleIndividualGrantHelper.createRole("cid", "roleId", RoleType.SYSTEM_MANAGER);
		
		new Expectations( ) {
			{
				require.getRoleByRoleType( RoleType.SYSTEM_MANAGER );
				result = role;
			}
		};
		
		//Act
		val result = RoleIndividualGrant.createGrantInfoOfSystemMananger( require, grantTargetUser, period );
		
		//Assert
		assertThat( result.getUserId() ).isEqualTo( grantTargetUser );
		assertThat( result.getCompanyId() ).isEqualTo( role.getCompanyId() );
		assertThat( result.getRoleType() ).isEqualTo( RoleType.SYSTEM_MANAGER );
		assertThat( result.getRoleId() ).isEqualTo( role.getRoleId() );
		assertThat( result.getValidPeriod().start() ).isEqualTo( period.start() );
		assertThat( result.getValidPeriod().end() ).isEqualTo( period.end() );
		
	}
	
	/**
	 * target : createGrantInfoOfCompanyManager
	 */	
	@Test
	public void testCreateGrantInfoOfCompanyManager() {
		val grantCompanyId = "cid";
		val grantTargetUser = "userId";
		val validPeriod = new DatePeriod( GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd( 9999, 12, 31));
		val role = RoleIndividualGrantHelper.createRole("company-id-of-role", "role-id", RoleType.COMPANY_MANAGER);
		
		new Expectations() {
			{
				require.getRoleByRoleType( RoleType.COMPANY_MANAGER );
				result = role ;
			}
		};
		
		//Act
		val result = RoleIndividualGrant.createGrantInfoOfCompanyManager(require, grantTargetUser, grantCompanyId, validPeriod );
		
		//Assert
		assertThat( result.getUserId() ).isEqualTo( grantTargetUser );
		assertThat( result.getCompanyId() ).isEqualTo( grantCompanyId );
		assertThat( result.getRoleType() ).isEqualTo( RoleType.COMPANY_MANAGER );
		assertThat( result.getRoleId() ).isEqualTo( role.getRoleId() );
		assertThat( result.getValidPeriod().start() ).isEqualTo( validPeriod.start() );
		assertThat( result.getValidPeriod().end() ).isEqualTo( validPeriod.end() );
		
	}
	
	/**
	 * target : checkStatusNormal
	 * pattern : ????????????????????? = false
	 * excepted: system error
	 */
	@Test
	public void testCheckStatusNormal_inv_1() {
		
		val validPeriod = new DatePeriod(GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd(9999, 12, 31));
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.PERSONAL_INFO //????????????????????? = false
				,	validPeriod );//dummy
		
		NtsAssert.systemError(() -> {
			
			roleGrant.checkStatusNormal(require);
			
		});
		
	}
	
	/**
	 * target : checkStatusNormal
	 * pattern : ????????????????????? = true, ????????? = empty
	 * excepted: Msg_2210
	 */
	@Test
	public void testCheckStatusNormal_user_empty() {
		
		val validPeriod = new DatePeriod(GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd(9999, 12, 31));
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.SYSTEM_MANAGER //?????????????????????
				,	validPeriod ); //dummy
		
		new Expectations (){
			{
				require.getUser( (String) any );
			}
		};
		
		NtsAssert.businessException( "Msg_2210", () -> {
			
			roleGrant.checkStatusNormal(require);
			
		});
	}
	
	/**
	 * target : checkStatusNormal
	 * pattern : ????????????????????? = true, ????????????????????????????????????
	 * excepted: system error
	 */
	@Test	
	public void testCheckStatusNormal_default_user() {
		
		val user = RoleIndividualGrantHelper.createUser( true);// ????????????????????????
		val validPeriod = new DatePeriod(GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd(9999, 12, 31));
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.SYSTEM_MANAGER //?????????????????????
				,	validPeriod ); //dummy
		
		new Expectations (){
			{
				require.getUser( (String) any );
				result = Optional.of( user );
			}
		};
		
		NtsAssert.systemError( () -> {
			
			roleGrant.checkStatusNormal(require);
			
		});
	}
	
	/**
	 * target : checkStatusNormal
	 * pattern : $?????????.???????????? < @????????????.?????????
	 * excepted: Msg_2211 
	 */	
	@Test
	public void testCheckStatusNormal_user_expired() {
		
		val user = RoleIndividualGrantHelper.createUser( "userId", GeneralDate.ymd(2020, 12, 31) );//????????????= 2020/12/31
		val validPeriod = new DatePeriod(GeneralDate.ymd(2021, 1, 1), GeneralDate.ymd(2020, 12, 31));//????????????.????????? = 2021/01/01
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.SYSTEM_MANAGER //?????????????????????
				,	validPeriod ); //dummy
		
		new Expectations (){
			{
				require.getUser( (String) any );
				result = Optional.of( user );
			}
		};
		
		NtsAssert.businessException( "Msg_2211", () -> {
			
			roleGrant.checkStatusNormal(require);
			
		});
	}
	
	
	/**
	 * target : checkStatusNormal
	 * pattern : ??????????????? = ???????????????, not ????????????????????????
	 * excepted: error ?????????
	 */
	@Test	
	public void testCheckStatusNormal_not_default_user() {
		
		val user = RoleIndividualGrantHelper.createUser( false );// ????????????????????????
		val validPeriod = new DatePeriod(GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd(9999, 12, 31));
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.COMPANY_MANAGER //???????????????
				,	validPeriod ); //dummy
		
		new Expectations (){
			{
				require.getUser( (String) any );
				result = Optional.of( user );
			}
		};
		
		roleGrant.checkStatusNormal(require);
	}
	
	/**
	 * target: getCorrectedValidPeriodByUserInfo
	 * pattern: ????????? = empty
	 * excepted: empty
	 */
	@Test
	public void testGetCorrectedValidPeriodByUserInfo_user_empty() {
		
		val validPeriod = new DatePeriod(GeneralDate.ymd(2000, 1, 1), GeneralDate.ymd(9999, 12, 31));
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.COMPANY_MANAGER //dummy
				,	validPeriod );//dummy
		
		new Expectations (){
			{
				require.getUser( (String) any );
			}
		};
		
		//Act
		val result = roleGrant.getCorrectedValidPeriodByUserInfo( require );
		
		//Assert
		assertThat( result ).isEmpty();
		
	}
	
	/**
	 * target: getCorrectedValidPeriodByUserInfo
	 * pattern: $?????????.???????????? < @????????????.?????????
	 * excepted: empty
	 */
	@Test
	public void testGetCorrectedValidPeriodByUserInfo_expirationDateOfUser_before_startDateOfRole() {
		
		//?????????.???????????? = 2000/12/31
		val user = RoleIndividualGrantHelper.createUser( "userId", GeneralDate.ymd(2000, 12, 31) );
		//????????????.????????? = 2016/01/01
		val validPeriod = new DatePeriod( GeneralDate.ymd(2016, 1, 1), GeneralDate.ymd(9999, 12, 31) );
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.COMPANY_MANAGER //dummy
				,	validPeriod );
		
		new Expectations (){
			{
				require.getUser( (String) any );
				result = Optional.of( user );
			}
		};
		
		//Act
		val result = roleGrant.getCorrectedValidPeriodByUserInfo( require );
		
		//Assert
		assertThat( result ).isEmpty();
		
	}
	
	/**
	 * target: getCorrectedValidPeriodByUserInfo
	 * pattern: $?????????.???????????? < @????????????.?????????
	 * excepted: ????????? = @????????????.?????????, ????????? = ????????????.?????????
	 */
	@Test
	public void testGetCorrectedValidPeriodByUserInfo_endDateOfRole_before_expirationDateOfUser() {
		
		//?????????.???????????? = 9999/12/31
		val user = RoleIndividualGrantHelper.createUser( "userId", GeneralDate.ymd(9999, 12, 31) );
		//????????????.????????? = 2030/12/31
		val validPeriod = new DatePeriod( GeneralDate.ymd(2016, 1, 1), GeneralDate.ymd(2030, 12, 31) );
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.COMPANY_MANAGER //dummy
				,	validPeriod );
		
		new Expectations (){
			{
				require.getUser( (String) any );
				result = Optional.of( user );
			}
		};
		
		//Act
		val result = roleGrant.getCorrectedValidPeriodByUserInfo( require );
		
		//Assert
		assertThat( result.get().start() ).isEqualTo( GeneralDate.ymd(2016, 1, 1) );
		assertThat( result.get().end() ).isEqualTo( GeneralDate.ymd(2030, 12, 31) );
		
	}
	
	/**
	 * target: getCorrectedValidPeriodByUserInfo
	 * pattern: @????????????.????????? > $?????????.????????????  
	 * excepted: ????????? = @????????????.?????????, ????????? = $?????????.????????????  
	 */
	@Test
	public void testGetCorrectedValidPeriodByUserInfo_endDateOfRole_after_expirationDateOfUser() {
		
		//?????????.???????????? = 2030/1/1
		val user = RoleIndividualGrantHelper.createUser( "userId", GeneralDate.ymd(2030, 1, 1) );
		//????????????.????????? = 2030/12/31
		val validPeriod = new DatePeriod( GeneralDate.ymd(2016, 1, 1), GeneralDate.ymd(2030, 12, 31) );
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.COMPANY_MANAGER //dummy
				,	validPeriod );
		
		new Expectations (){
			{
				require.getUser( (String) any );
				result = Optional.of( user );
			}
		};
		
		//Act
		val result = roleGrant.getCorrectedValidPeriodByUserInfo( require );
		
		//Assert
		assertThat( result.get().start() ).isEqualTo( GeneralDate.ymd(2016, 1, 1) );
		assertThat( result.get().end() ).isEqualTo( GeneralDate.ymd(2030, 1, 1) );
		
	}
	
	/**
	 * target: getCorrectedValidPeriodByUserInfo
	 * pattern: @????????????.????????? = $?????????.????????????  
	 * excepted: ????????? = @????????????.?????????, ????????? = $?????????.????????????  
	 */
	@Test
	public void testGetCorrectedValidPeriodByUserInfo_endDateOfRole_equal_expirationDateOfUser() {
		
		//?????????.???????????? = 2030/12/31
		val user = RoleIndividualGrantHelper.createUser( "userId", GeneralDate.ymd(2030, 12, 31) );
		//????????????.????????? = 2030/12/31
		val validPeriod = new DatePeriod( GeneralDate.ymd(2016, 1, 1), GeneralDate.ymd(2030, 12, 31) );
		val roleGrant = RoleIndividualGrantHelper.createRoleIndividualGrant(
					"userId" //dummy
				,	RoleType.COMPANY_MANAGER //dummy
				,	validPeriod );
		
		new Expectations (){
			{
				require.getUser( (String) any );
				result = Optional.of( user );
			}
		};
		
		//Act
		val result = roleGrant.getCorrectedValidPeriodByUserInfo( require );
		
		//Assert
		assertThat( result.get().start() ).isEqualTo( GeneralDate.ymd(2016, 1, 1) );
		assertThat( result.get().end() ).isEqualTo( GeneralDate.ymd(2030, 12, 31) );
		
	}
	
}
