package nts.uk.ctx.exio.infra.repository.input.canonicalize.domaindata;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.exio.dom.input.canonicalize.domaindata.DomainDataId;
import nts.uk.ctx.exio.dom.input.canonicalize.domaindata.DomainDataRepository;
import nts.uk.ctx.exio.dom.input.canonicalize.groups.generic.HistoryClass;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.history.History;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaDomainDataRepository extends JpaRepository implements DomainDataRepository {

	@Override
	public boolean exists(DomainDataId id) {
		val statement = createStatement(id, "select 1");
		return statement.getSingle(rec -> 1).isPresent();
	}

	@Override
	public void delete(DomainDataId id) {
		val statement = createStatement(id, "delete");
		statement.execute();
	}
	

	private NtsStatement createStatement(DomainDataId id, String baseCommand) {
		
		StringBuilder sql = new StringBuilder()
				.append(baseCommand)
				.append(" from ")
				.append(id.getTableName())
				.append(" where ");
		
		for (int i = 0; i < id.getKeys().size(); i++) {
			
			if (i > 0) {
				sql.append(" and ");
			}
			
			val column = id.getKeys().get(i).getColumn().getColumnName();
			sql.append(column).append(" = @p").append(i);
		}
		
		val statement = this.jdbcProxy().query(sql.toString());
		
		for (int i = 0; i < id.getKeys().size(); i++) {
			val key = id.getKeys();
			key.get(i).setParam(statement, "p" + i);
		}
		
		return statement;
	}

	@Override
	public History<DateHistoryItem, DatePeriod, GeneralDate> getHistory(DomainDataId id) {
		val statement = createStatement(id, "select *");
		return new HistoryClass(statement.getList(rec ->{
			return new DateHistoryItem(rec.getString("HIST_ID"),
					new DatePeriod(
							rec.getGeneralDate("START_DATE"),
							rec.getGeneralDate("END_DATE")));
		}));
	}

	@Override
	public void update(DomainDataId targetKey, DatePeriod period) {
		StringBuilder sql = new StringBuilder()
				.append(" update ")
				.append(targetKey.getTableName())
				.append(" set ");
				
		//履歴更新=START_ENDの更新だろうという
		// + 開始日＝START_DATE、終了日＝END_DATEって名前だろうという前提
		sql.append("START_DATE").append(" = @startDate");
		sql.append("END_DATE").append(" = @endDate");
				
		
		sql = sql.append(" where ");
		
		//こっちにはTableのkeyが来るので問題無。
		for (int keysInt = 0; keysInt < targetKey.getKeys().size(); keysInt++) {
			
			if (keysInt > 0) {
				sql.append(" and ");
			}
			
			val column = targetKey.getKeys().get(keysInt).getColumn().getColumnName();
			sql.append(column).append(" = @p").append(keysInt);
		}
		
		val statement = this.jdbcProxy().query(sql.toString());

		for (int i = 0; i < targetKey.getKeys().size(); i++) {
			val key = targetKey.getKeys();
			key.get(i).setParam(statement, "p" + i);
		}
		
		statement.paramDate("startDate", period.start())
						.paramDate("endDate", period.end())
						.execute();
	}

}
