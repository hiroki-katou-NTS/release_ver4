package nts.uk.ctx.pr.core.dom.wageprovision.companyuniformamount;


import java.util.Optional;
import java.util.List;

/**
* 給与会社単価
*/
public interface PayrollUnitPriceRepository
{

    List<Object[]> getAllPayrollUnitPriceSetByCID(String cid);

    List<PayrollUnitPrice> getAllPayrollUnitPriceByCID(String cid);

    List<PayrollUnitPrice> getPayrollUnitPriceByYearMonth(int yearMonth);

    Optional<PayrollUnitPrice> getPayrollUnitPriceById(String code, String cid);

    void add(PayrollUnitPrice domain);

    void update(PayrollUnitPrice domain);

    void remove(String code, String cid);

}
