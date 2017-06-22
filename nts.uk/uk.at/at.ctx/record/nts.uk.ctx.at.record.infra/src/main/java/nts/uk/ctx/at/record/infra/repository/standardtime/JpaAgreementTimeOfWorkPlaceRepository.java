package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfWorkPlace;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceRepository;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeWorkPlace;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeWorkPlacePK;

@Stateless
public class JpaAgreementTimeOfWorkPlaceRepository extends JpaRepository implements AgreementTimeOfWorkPlaceRepository {

	private static final String DELETE_BY_ONE_KEY;

	private static final String FIND_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KmkmtAgeementTimeWorkPlace a ");
		builderString.append("WHERE a.kmkmtAgeementTimeWorkPlacePK.workPlaceId = :workPlaceId ");
		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
		DELETE_BY_ONE_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementTimeWorkPlace a ");
		builderString.append("WHERE a.kmkmtAgeementTimeWorkPlacePK.workPlaceId = :workPlaceId ");
		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
		FIND_BY_KEY = builderString.toString();
	}

	@Override
	public void remove(String workplaceId, LaborSystemtAtr laborSystemAtr) {
		this.getEntityManager().createQuery(DELETE_BY_ONE_KEY).setParameter("workPlaceId", workplaceId)
				.setParameter("laborSystemAtr", laborSystemAtr.value).executeUpdate();
	}

	@Override
	public void add(AgreementTimeOfWorkPlace agreementTimeOfWorkPlace) {
		this.commandProxy().insert(toEntity(agreementTimeOfWorkPlace));
	}

	@Override
	public List<AgreementTimeOfWorkPlace> find(String workplaceId, LaborSystemtAtr laborSystemAtr) {
		return this.queryProxy().query(FIND_BY_KEY, KmkmtAgeementTimeWorkPlace.class)
				.setParameter("workPlaceId", workplaceId)
				.setParameter("laborSystemAtr", laborSystemAtr.value)
				.getList(f -> toDomain(f));
	}

	private KmkmtAgeementTimeWorkPlace toEntity(AgreementTimeOfWorkPlace agreementTimeOfWorkPlace) {
		val entity = new KmkmtAgeementTimeWorkPlace();

		entity.kmkmtAgeementTimeWorkPlacePK = new KmkmtAgeementTimeWorkPlacePK();
		entity.kmkmtAgeementTimeWorkPlacePK.basicSettingId = agreementTimeOfWorkPlace.getBasicSettingId();
		entity.kmkmtAgeementTimeWorkPlacePK.workPlaceId = agreementTimeOfWorkPlace.getWorkplaceId();
		entity.laborSystemAtr = new BigDecimal(agreementTimeOfWorkPlace.getLaborSystemAtr().value);

		return entity;
	}

	private static AgreementTimeOfWorkPlace toDomain(KmkmtAgeementTimeWorkPlace kmkmtAgeementTimeWorkPlace) {
		AgreementTimeOfWorkPlace agreementTimeOfWorkPlace = AgreementTimeOfWorkPlace.createJavaType(
				kmkmtAgeementTimeWorkPlace.kmkmtAgeementTimeWorkPlacePK.workPlaceId,
				kmkmtAgeementTimeWorkPlace.kmkmtAgeementTimeWorkPlacePK.basicSettingId,
				kmkmtAgeementTimeWorkPlace.laborSystemAtr);
		return agreementTimeOfWorkPlace;
	}

}
