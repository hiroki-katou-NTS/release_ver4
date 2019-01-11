package nts.uk.file.at.infra.export.attendanceitemprepare;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.Query;

import lombok.SneakyThrows;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApprovalProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFun;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.FormatPerformance;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.IdentityProcess;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.MonPerformanceFun;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.YourselfConfirmError;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtDaiPerformEdFun;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtFormatPerformance;
import nts.uk.ctx.at.record.infra.entity.workrecord.operationsetting.KrcmtMonPerformanceFun;
import nts.uk.file.at.app.export.attendanceitemprepare.ApplicationCallExport;
import nts.uk.file.at.app.export.attendanceitemprepare.ApplicationTypeExport;
import nts.uk.file.at.app.export.attendanceitemprepare.OperationExcelRepo;
import nts.uk.file.at.app.export.attendanceitemprepare.RoleExport;
/**
 * 
 * @author Hoidd 2
 *
 */
@Stateless
public class JpaOperationRepository extends JpaRepository implements OperationExcelRepo
{
	int test = 0 ;
    private static final String SELECT_ALL_QUERY_STRING_DAILY = "SELECT f FROM KrcmtDaiPerformEdFun f";
    private static final String SELECT_BY_KEY_STRING_DAILY = SELECT_ALL_QUERY_STRING_DAILY + " WHERE  f.daiPerformanceFunPk.cid =:cid ";

    //Monthly
    private static final String SELECT_ALL_QUERY_STRING_MONTHLY = "SELECT a FROM KrcmtMonPerformanceFun a";
    private static final String SELECT_BY_KEY_STRING_MONTHLY = SELECT_ALL_QUERY_STRING_MONTHLY + " WHERE  a.monPerformanceFunPk.cid =:cid ";
    //format
    private static final String SELECT_ALL_QUERY_STRING_FORMAT = "SELECT b FROM KrcmtFormatPerformance b";
    private static final String SELECT_BY_KEY_STRING_FORMAT = SELECT_ALL_QUERY_STRING_FORMAT + " WHERE  b.formatPerformancePk.cid =:cid ";
    //IDENTITY
//    private static final String SELECT_ALL_QUERY_STRING_IDENTITY= "SELECT c FROM KrcmtIdentityProcess c";
//    private static final String SELECT_BY_KEY_STRING_IDENTITY = SELECT_ALL_QUERY_STRING_IDENTITY + " WHERE  c.identityProcessPk.cid =:cid ";
    //APPLICATIONCALL
    private static final String SELECT_BY_COM_APPLICATIONCALL = "SELECT c.APPLICATION_TYPE FROM KFNMT_APPLICATION_CALL c where c.CID=?cid";
	//Rold
    private static final String SELECT_ALL_ROLD = "SELECT a.ROLE_CD as codeRole,a.ROLE_NAME as nameRole,c.DESCRIPTION_OF_FUNCTION as description ,c.DISPLAY_NAME_OF_FUNCTION as displayName, b.AVAILABILITY as availability "
    											+ "from SACMT_ROLE  a left join KRCMT_DAI_PERFORMANCE_AUT b "
    											+ "on a.ROLE_ID=b.ROLE_ID and a.CID = b.CID "
    											+ "left join KRCMT_DAI_PERFORMANCE_FUN c "
    											+ "on b.FUNCTION_NO=c.FUNCTION_NO "
    											+ "where a.CID=?cid ORDER BY a.ROLE_CD ";
	
    @Override
    public Optional<FormatPerformance> getFormatPerformanceById(String companyId){
        return this.queryProxy().query(SELECT_BY_KEY_STRING_FORMAT, KrcmtFormatPerformance.class)
        .setParameter("cid", companyId)
        .getSingle(c->c.toDomain());
    }
  
    @Override
    public Optional<DaiPerformanceFun> getDaiPerformanceFunById(String companyId){
        return this.queryProxy().query(SELECT_BY_KEY_STRING_DAILY, KrcmtDaiPerformEdFun.class)
        .setParameter("cid", companyId)
        .getSingle(c->c.toDomain());
    }


    @Override
    public Optional<MonPerformanceFun> getMonPerformanceFunById(String companyId){
        return this.queryProxy().query(SELECT_BY_KEY_STRING_MONTHLY, KrcmtMonPerformanceFun.class)
        .setParameter("cid", companyId)
        .getSingle(c->c.toDomain());
    }
    
    @Override
    @SneakyThrows
    public Optional<IdentityProcess> getIdentityProcessById(String companyId){
    	try (PreparedStatement statement = this.connection().prepareStatement("SELECT * from KRCMT_SELF_CHECK_SET h WHERE h.CID = ?")) {
			statement.setString(1, companyId);
			return new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				return new IdentityProcess(companyId, rec.getInt("USE_DAILY_SELF_CHECK"), 
		        		rec.getInt("USE_MONTHLY_SELF_CHECK"),
		        		rec.getInt("YOURSELF_CONFIRM_ERROR") == null ? null : EnumAdaptor.valueOf(rec.getInt("YOURSELF_CONFIRM_ERROR"), YourselfConfirmError.class));
			});
    	}
    }
    @Override
    @SneakyThrows
    public Optional<ApprovalProcess> getApprovalProcessById(String companyId){
    	try (PreparedStatement statement = this.connection().prepareStatement("SELECT * from KRCMT_BOSS_CHECK_SET h WHERE h.CID = ?")) {
			statement.setString(1, companyId);
			return new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				return new ApprovalProcess(companyId, rec.getString("JOB_TITLE_NOT_BOSS_CHECK"), 
		        		rec.getInt("USE_DAILY_BOSS_CHECK"), rec.getInt("USE_MONTHLY_BOSS_CHECK"), 
		        		rec.getInt("SUPERVISOR_CONFIRM_ERROR") == null ? null : EnumAdaptor.valueOf(rec.getInt("SUPERVISOR_CONFIRM_ERROR"), YourselfConfirmError.class));
			});
    	}
    }
	
	@Override
	public List<ApplicationCallExport> findByCom(String companyId) {
		List<?> data = this.getEntityManager().createNativeQuery(SELECT_BY_COM_APPLICATIONCALL)
				.setParameter("cid", companyId).getResultList();
		List<ApplicationCallExport> result = new ArrayList<>();
		data.stream().forEach(x -> {
			if(x !=null){
				int	valueEnum = ((BigDecimal) x).intValue();
				result.add(new ApplicationCallExport(companyId,EnumAdaptor.valueOf(valueEnum, ApplicationTypeExport.class)));
			}
		});
		
		return result;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<RoleExport> findRole(String companyId) {
		List<Object[]> rawResultList;
		Query query = this.getEntityManager().createNativeQuery(SELECT_ALL_ROLD)
				.setParameter("cid", companyId);
		rawResultList = query.getResultList();
		List<RoleExport> result = new ArrayList<>();
		
		rawResultList.stream().forEach(x -> {
			convertToRoleExport(result, (Object[])x);
		});
		return result;
	}

	private void convertToRoleExport(List<RoleExport> result, Object[] x) {
		
		try{
			test++;
		RoleExport reExport = 
				new RoleExport((String) x[0],(String) x[1],(String) x[2],(String) x[3],x[4]!=null?((BigDecimal) x[4]).intValue():null);
		result.add(reExport);
		}catch (Exception e) {
			System.out.println(test);
		}
	}

}
