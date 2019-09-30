package nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.emphealinsurassocinfor;

import java.util.List;
import java.util.Optional;

/**
* 健保組合情報
*/
public interface HealthCarePortInforRepository
{

    List<HealthCarePortInfor> getAllHealthCarePortInfor();

    Optional<HealthCarePortInfor> getHealthCarePortInforById(String hisId);

    void add(HealthCarePortInfor domain);

    void update(HealthCarePortInfor domain);

    void remove(String hisId,String employeeId);

}
