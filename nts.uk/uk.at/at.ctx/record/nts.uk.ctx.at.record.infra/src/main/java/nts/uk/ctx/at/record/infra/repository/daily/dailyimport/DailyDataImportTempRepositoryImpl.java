package nts.uk.ctx.at.record.infra.repository.daily.dailyimport;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.dailyimport.DailyDataImportTemp;
import nts.uk.ctx.at.record.dom.dailyimport.DailyDataImportTempRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DailyDataImportTempRepositoryImpl extends JpaRepository implements DailyDataImportTempRepository {

	@Override
	@SneakyThrows
	public List<DailyDataImportTemp> getDataImport(DatePeriod period, String companyCode) {
		String sql = "SELECT SCD, YMD, WORKTYPE, WORKTIME, STARTTIME, ENDTIME, BREAKSTARTTIME1, BREAKENDTIME1, BREAKSTARTTIME2, "
				+ " BREAKENDTIME2 FROM KRCDT_TEMP_DAI WHERE CCD = ? AND YMD <= ? AND YMD >= ?"
				+ " ORDER BY SCD, YMD";
		
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
			stmt.setInt(1, Integer.valueOf(companyCode));
			stmt.setDate(2, Date.valueOf(period.end().localDate()));
			stmt.setDate(3, Date.valueOf(period.start().localDate()));
			
			return new NtsResultSet(stmt.executeQuery())
					.getList(r -> {
						return new DailyDataImportTemp(companyCode, r.getString(1),  r.getGeneralDate(2), 
														r.getString(3), Optional.ofNullable(r.getString(4)), 
														Optional.ofNullable(r.getString(5)), Optional.ofNullable(r.getString(6)), 
														Optional.ofNullable(r.getString(7)), Optional.ofNullable(r.getString(8)), 
														Optional.ofNullable(r.getString(9)), Optional.ofNullable(r.getString(10)));
					});
		}
	}

	@Override
	@SneakyThrows
	public List<String> getTargetEmpCode(DatePeriod period, String companyCode) {
		String sql = "SELECT DISTINCT SCD FROM KRCDT_TEMP_DAI WHERE CCD = ? AND YMD <= ? AND YMD >= ?";
		
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
			stmt.setInt(1, Integer.valueOf(companyCode));
			stmt.setDate(2, Date.valueOf(period.end().localDate()));
			stmt.setDate(3, Date.valueOf(period.start().localDate()));
			
			return new NtsResultSet(stmt.executeQuery()).getList(r ->  r.getString(1));
		}
	}

	@Override
	@SneakyThrows
	public List<DailyDataImportTemp> getDataImport(DatePeriod period, String companyCode, Collection<String> empCode) {
		List<DailyDataImportTemp> result = new ArrayList<>();
		CollectionUtil.split(new ArrayList<>(empCode), DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, emps -> {
			result.addAll(getInternal(period, companyCode, emps));
		});
		
		return result;
	}

	@SneakyThrows
	private List<DailyDataImportTemp> getInternal(DatePeriod period, String companyCode, List<String> empCode) {
		String sql = "SELECT SCD, YMD, WORKTYPE, WORKTIME, STARTTIME, ENDTIME, BREAKSTARTTIME1, BREAKENDTIME1, BREAKSTARTTIME2, "
				+ " BREAKENDTIME2 FROM KRCDT_TEMP_DAI WHERE CCD = ? AND YMD <= ? AND YMD >= ? AND SCD IN ("
				+ empCode.stream().map(c -> "?").collect(Collectors.joining(", "))
				+ ")";
		
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
			stmt.setInt(1, Integer.valueOf(companyCode));
			stmt.setDate(2, Date.valueOf(period.end().localDate()));
			stmt.setDate(3, Date.valueOf(period.start().localDate()));
			for(int i = 0; i < empCode.size(); i++){
				stmt.setString(4 + i, empCode.get(i));
			}
			
			return new NtsResultSet(stmt.executeQuery())
					.getList(r -> {
						return new DailyDataImportTemp(companyCode, r.getString(1),  r.getGeneralDate(2), 
														r.getString(3), Optional.ofNullable(r.getString(4)), 
														Optional.ofNullable(r.getString(5)), Optional.ofNullable(r.getString(6)), 
														Optional.ofNullable(r.getString(7)), Optional.ofNullable(r.getString(8)), 
														Optional.ofNullable(r.getString(9)), Optional.ofNullable(r.getString(10)));
					});
		}
	}
}
