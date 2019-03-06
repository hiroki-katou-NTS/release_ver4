package nts.uk.ctx.at.record.infra.repository.standardtime;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.record.dom.standardtime.AgreementTimeOfWorkPlace;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceRepository;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeWorkPlace;
import nts.uk.ctx.at.record.infra.entity.standardtime.KmkmtAgeementTimeWorkPlacePK;

@Stateless
public class JpaAgreementTimeOfWorkPlaceRepository extends JpaRepository implements AgreementTimeOfWorkPlaceRepository {

	private static final String DELETE_BY_ONE_KEY;

//	private static final String FIND_BY_KEY;

	private static final String FIND_WORKPLACE_SETTING;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KmkmtAgeementTimeWorkPlace a ");
		builderString.append("WHERE a.kmkmtAgeementTimeWorkPlacePK.workPlaceId = :workPlaceId ");
		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
		DELETE_BY_ONE_KEY = builderString.toString();

//		builderString = new StringBuilder();
//		builderString.append("SELECT a ");
//		builderString.append("FROM KmkmtAgeementTimeWorkPlace a ");
//		builderString.append("WHERE a.kmkmtAgeementTimeWorkPlacePK.workPlaceId = :workPlaceId ");
//		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
//		FIND_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KmkmtAgeementTimeWorkPlace a ");
		builderString.append("WHERE a.kmkmtAgeementTimeWorkPlacePK.basicSettingId != NULL ");
		builderString.append("AND a.laborSystemAtr = :laborSystemAtr ");
		FIND_WORKPLACE_SETTING = builderString.toString();
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
	public List<String> findWorkPlaceSetting(LaborSystemtAtr laborSystemAtr) {
		return this.queryProxy().query(FIND_WORKPLACE_SETTING, KmkmtAgeementTimeWorkPlace.class)
				.setParameter("laborSystemAtr", laborSystemAtr.value)
				.getList(f -> f.kmkmtAgeementTimeWorkPlacePK.workPlaceId);
	}

	@Override
	@SneakyThrows
	public Optional<String> find(String workplaceId, LaborSystemtAtr laborSystemAtr) {

		try (PreparedStatement statement = this.connection().prepareStatement(
				" select * from KMKMT_AGREEMENTTIME_WPL " + " where WKPCD = ? " + " and LABOR_SYSTEM_ATR = ? ")) {

			statement.setString(1, workplaceId);
			statement.setBigDecimal(2, new BigDecimal(laborSystemAtr.value));

			return new NtsResultSet(statement.executeQuery()).getSingle(rec -> {
				return rec.getString("BASIC_SETTING_ID");
			});
		}
		// return this.queryProxy().query(FIND_BY_KEY,
		// KmkmtAgeementTimeWorkPlace.class)
		// .setParameter("workPlaceId", workplaceId)
		// .setParameter("laborSystemAtr", laborSystemAtr.value)
		// .getSingle(f -> f.kmkmtAgeementTimeWorkPlacePK.basicSettingId);
	}
	
	@Override
	@SneakyThrows
	public List<AgreementTimeOfWorkPlace> findWorkPlaceSetting(List<String> workplaceId) {
		String query = "select WKPCD, BASIC_SETTING_ID, LABOR_SYSTEM_ATR from KMKMT_AGREEMENTTIME_WPL where WKPCD IN (" + workplaceId.stream().map(s -> "?").collect(Collectors.joining(",")) +" )";
		try (PreparedStatement statement = this.connection().prepareStatement(query)) {
			for(int i = 0; i < workplaceId.size(); i++){
				statement.setString(i + 1, workplaceId.get(i));
			}

			return new NtsResultSet(statement.executeQuery()).getList(rec -> {
				return new AgreementTimeOfWorkPlace(rec.getString(1), rec.getString(2), 
													EnumAdaptor.valueOf(rec.getInt(3), LaborSystemtAtr.class));
			});
		}
	}

	private KmkmtAgeementTimeWorkPlace toEntity(AgreementTimeOfWorkPlace agreementTimeOfWorkPlace) {
		val entity = new KmkmtAgeementTimeWorkPlace();

		entity.kmkmtAgeementTimeWorkPlacePK = new KmkmtAgeementTimeWorkPlacePK();
		entity.kmkmtAgeementTimeWorkPlacePK.basicSettingId = agreementTimeOfWorkPlace.getBasicSettingId();
		entity.kmkmtAgeementTimeWorkPlacePK.workPlaceId = agreementTimeOfWorkPlace.getWorkplaceId();
		entity.laborSystemAtr = new BigDecimal(agreementTimeOfWorkPlace.getLaborSystemAtr().value);

		return entity;
	}

//	private static AgreementTimeOfWorkPlace toDomain(KmkmtAgeementTimeWorkPlace kmkmtAgeementTimeWorkPlace) {
//		AgreementTimeOfWorkPlace agreementTimeOfWorkPlace = AgreementTimeOfWorkPlace.createJavaType(
//				kmkmtAgeementTimeWorkPlace.kmkmtAgeementTimeWorkPlacePK.workPlaceId,
//				kmkmtAgeementTimeWorkPlace.kmkmtAgeementTimeWorkPlacePK.basicSettingId,
//				kmkmtAgeementTimeWorkPlace.laborSystemAtr);
//		return agreementTimeOfWorkPlace;
//	}
}
