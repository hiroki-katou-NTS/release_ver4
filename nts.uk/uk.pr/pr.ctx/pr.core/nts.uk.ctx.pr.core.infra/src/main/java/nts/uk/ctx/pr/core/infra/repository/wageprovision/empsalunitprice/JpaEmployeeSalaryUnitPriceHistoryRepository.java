package nts.uk.ctx.pr.core.infra.repository.wageprovision.empsalunitprice;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.empsalunitprice.EmployeeSalaryUnitPriceHistory;
import nts.uk.ctx.pr.core.dom.wageprovision.empsalunitprice.EmployeeSalaryUnitPriceHistoryRepository;
import nts.uk.ctx.pr.core.dom.wageprovision.empsalunitprice.IndividualEmpSalUnitPrice;
import nts.uk.ctx.pr.core.dom.wageprovision.empsalunitprice.WorkIndividualPrice;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.empsalunitprice.QpbmtEmpSalPriHis;
import nts.uk.ctx.pr.core.infra.entity.wageprovision.empsalunitprice.QpbmtEmpSalPriHisPk;

@Stateless
public class JpaEmployeeSalaryUnitPriceHistoryRepository extends JpaRepository implements EmployeeSalaryUnitPriceHistoryRepository
{

    private static final String SELECT_ALL_QUERY_STRING = "SELECT f FROM QpbmtEmpSalPriHis f";
    private static final String SELECT_BY_KEY_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.empSalPriHisPk.personalUnitPriceCode =:personalUnitPriceCode AND  f.empSalPriHisPk.employeeId IN :employeeId";
    private static final String SELECT_BY_CODE_STRING = SELECT_ALL_QUERY_STRING + " WHERE  f.empSalPriHisPk.personalUnitPriceCode =:personalUnitPriceCode AND  f.empSalPriHisPk.employeeId = :employeeId";
    private static final String UPDATE_ALL_BY_KEY_HIS = "UPDATE QpbmtEmpSalPriHis f SET f.indvidualUnitPrice =:indvidualUnitPrice WHERE f.empSalPriHisPk.historyId =:historyId";

    @Override
    public void updateAllHistory(String historyId, BigDecimal UnitPrice) {
        this.getEntityManager().createQuery(UPDATE_ALL_BY_KEY_HIS,QpbmtEmpSalPriHis.class)
                .setParameter("historyId",historyId)
                .setParameter("indvidualUnitPrice",UnitPrice)
                .executeUpdate();
    }

    @Override
    public List<WorkIndividualPrice> getEmployeeSalaryUnitPriceHistory(String personalUnitPriceCode, List<String> employeeId) {
        if(employeeId==null || !(employeeId.size()>0))
            return Collections.emptyList();
        List<QpbmtEmpSalPriHis> qpbmtEmpSalPriHis = this.queryProxy().query(SELECT_BY_KEY_STRING, QpbmtEmpSalPriHis.class)
                .setParameter("personalUnitPriceCode", personalUnitPriceCode)
                .setParameter("employeeId", employeeId).getList();
        if(qpbmtEmpSalPriHis.size()>0){

            return qpbmtEmpSalPriHis.stream().map(v-> new WorkIndividualPrice(v.empSalPriHisPk.employeeId,v.empSalPriHisPk.historyId,"","",v.startYearMonth,v.endYearMonth,v.indvidualUnitPrice)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public EmployeeSalaryUnitPriceHistory getAllEmployeeSalaryUnitPriceHistory(String perUnitPriceCode, String employeeId) {
        List<QpbmtEmpSalPriHis> qpbmtEmpSalPriHis = this.queryProxy().query(SELECT_BY_KEY_STRING, QpbmtEmpSalPriHis.class)
                .setParameter("personalUnitPriceCode", perUnitPriceCode)
                .setParameter("employeeId", employeeId).getList();
        return QpbmtEmpSalPriHis.toDomain(qpbmtEmpSalPriHis);
    }

    @Override
    public void add(EmployeeSalaryUnitPriceHistory domain){
        this.commandProxy().insertAll(QpbmtEmpSalPriHis.toEntity(domain));
    }

    @Override
    public void update(EmployeeSalaryUnitPriceHistory domain){
        this.commandProxy().updateAll(QpbmtEmpSalPriHis.toEntity(domain));
    }

    @Override
    public void remove(String personalUnitPriceCode, String employeeId, String historyId){
        this.commandProxy().remove(QpbmtEmpSalPriHis.class, new QpbmtEmpSalPriHisPk(personalUnitPriceCode, employeeId, historyId));
    }

    @Override
    public List<IndividualEmpSalUnitPrice> getIndividualEmpSalUnitPrices(String perUnitPriceCode, String employeeId) {
        return this.queryProxy().query(SELECT_BY_CODE_STRING, QpbmtEmpSalPriHis.class)
                .setParameter("personalUnitPriceCode", perUnitPriceCode)
                .setParameter("employeeId", employeeId).getList().stream().map(QpbmtEmpSalPriHis::toDomain).collect(Collectors.toList());
    }

    @Override
    public void add(IndividualEmpSalUnitPrice domain) {
        this.commandProxy().insert(QpbmtEmpSalPriHis.toEntity(domain));
    }

    @Override
    public void update(IndividualEmpSalUnitPrice domain) {
        this.commandProxy().update(QpbmtEmpSalPriHis.toEntity(domain));
    }
}
