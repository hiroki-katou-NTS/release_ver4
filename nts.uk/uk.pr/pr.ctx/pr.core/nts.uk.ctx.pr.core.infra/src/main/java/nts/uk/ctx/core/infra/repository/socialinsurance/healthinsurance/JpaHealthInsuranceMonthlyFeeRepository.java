package nts.uk.ctx.core.infra.repository.socialinsurance.healthinsurance;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.core.dom.socialinsurance.healthinsurance.HealthInsuranceMonthlyFee;
import nts.uk.ctx.core.dom.socialinsurance.healthinsurance.HealthInsuranceMonthlyFeeRepository;
import nts.uk.ctx.core.dom.socialinsurance.healthinsurance.HealthInsurancePerGradeFee;
import nts.uk.ctx.core.dom.socialinsurance.healthinsurance.ScreenMode;
import nts.uk.ctx.core.infra.entity.socialinsurance.healthinsurance.QpbmtHealthInsuranceMonthlyFee;
import nts.uk.ctx.core.infra.entity.socialinsurance.healthinsurance.QpbmtHealthInsurancePerGradeFee;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class JpaHealthInsuranceMonthlyFeeRepository extends JpaRepository implements HealthInsuranceMonthlyFeeRepository {

    private static final String GET_HEALTH_INSURANCE_PER_GRADE_FEE_BY_HISTORY_ID = "SELECT a FROM QpbmtHealthInsurancePerGradeFee a WHERE a.healthMonPerGraPk.historyId=:historyId";

    @Override
    public Optional<HealthInsuranceMonthlyFee> getHealthInsuranceMonthlyFeeById(String historyId) {
        val entity = this.queryProxy().find(historyId, QpbmtHealthInsuranceMonthlyFee.class);

        if (!entity.isPresent())
            return Optional.empty();

        val details = this.queryProxy().query(GET_HEALTH_INSURANCE_PER_GRADE_FEE_BY_HISTORY_ID, QpbmtHealthInsurancePerGradeFee.class).setParameter("historyId", historyId).getList();

        return Optional.of(toDomain(entity.get(), details));
    }

    @Override
    public void addOrUpdate(HealthInsuranceMonthlyFee domain, ScreenMode screenMode) {
        if (ScreenMode.ADD.equals(screenMode)) {
            //ドメインモデル「健康保険月額保険料額」を追加する
            this.commandProxy().insert(QpbmtHealthInsuranceMonthlyFee.toEntity(domain));
            this.commandProxy().insertAll(QpbmtHealthInsurancePerGradeFee.toEntity(domain));
            return;
        }
        //ドメインモデル「健康保険月額保険料額」を更新する
        this.commandProxy().update(QpbmtHealthInsuranceMonthlyFee.toEntity(domain));
        this.commandProxy().updateAll(QpbmtHealthInsurancePerGradeFee.toEntity(domain));
    }

    /**
     * Convert entity to domain
     *
     * @param entity QpbmtHealthInsuranceMonthlyFee
     * @return HealthInsuranceMonthlyFee
     */
    private HealthInsuranceMonthlyFee toDomain(QpbmtHealthInsuranceMonthlyFee entity, List<QpbmtHealthInsurancePerGradeFee> details) {
        return new HealthInsuranceMonthlyFee(entity.historyId, entity.employeeShareAmountMethod,
                entity.individualLongCareInsuranceRate, entity.individualBasicInsuranceRate, entity.individualHealthInsuranceRate, entity.individualFractionCls, entity.individualSpecialInsuranceRate,
                entity.employeeLongCareInsuranceRate, entity.employeeBasicInsuranceRate, entity.employeeHealthInsuranceRate, entity.employeeFractionCls, entity.employeeSpecialInsuranceRate,
                entity.autoCalculationCls,
                details.stream().map(x -> new HealthInsurancePerGradeFee(
                        x.healthMonPerGraPk.healthInsuranceGrade,
                        x.employeeHealthInsuranceFee,
                        x.employeeNursingCareInsuranceFee,
                        x.employeeSpecificInsuranceFee,
                        x.employeeBasicInsuranceFee,
                        x.insuredHealthInsuranceFee,
                        x.insuredNursingCareInsuranceFee,
                        x.insuredSpecificInsuranceFee,
                        x.insuredBasicInsuranceFee
                )).collect(Collectors.toList())
        );
    }

    @Override
    public void deleteByHistoryIds(List<String> historyIds) {
        this.commandProxy().removeAll(QpbmtHealthInsuranceMonthlyFee.class, historyIds);
    }

    @Override
	public void add(HealthInsuranceMonthlyFee domain) {
		this.commandProxy().insert(QpbmtHealthInsuranceMonthlyFee.toEntity(domain));
		this.commandProxy().insertAll(QpbmtHealthInsurancePerGradeFee.toEntity(domain));
	}

	@Override
	public void update(HealthInsuranceMonthlyFee domain) {
		this.commandProxy().update(QpbmtHealthInsuranceMonthlyFee.toEntity(domain));
		this.commandProxy().updateAll(QpbmtHealthInsurancePerGradeFee.toEntity(domain));
		
	}

    @Override
    public void delete(HealthInsuranceMonthlyFee domain) {
        this.commandProxy().remove(QpbmtHealthInsurancePerGradeFee.toEntity(domain));
        this.commandProxy().removeAll(QpbmtHealthInsurancePerGradeFee.toEntity(domain));
    }

	@Override
	public void updateGraFee(HealthInsuranceMonthlyFee domain) {
		this.commandProxy().updateAll(QpbmtHealthInsurancePerGradeFee.toEntity(domain));
	}
}
