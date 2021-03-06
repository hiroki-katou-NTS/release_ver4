package nts.uk.ctx.pr.core.dom.wageprovision.companyuniformamount;


import java.util.Optional;
import java.util.List;

/**
* 給与会社単価設定
*/
public interface PayrollUnitPriceSettingRepository
{

    List<PayrollUnitPriceSetting> getAllpayrollUnitPriceSetting();

    Optional<PayrollUnitPriceSetting> getpayrollUnitPriceSettingById(String hisId);

    void add(PayrollUnitPriceSetting domain);

    void update(PayrollUnitPriceSetting domain);

    void remove(String hisId);

}
