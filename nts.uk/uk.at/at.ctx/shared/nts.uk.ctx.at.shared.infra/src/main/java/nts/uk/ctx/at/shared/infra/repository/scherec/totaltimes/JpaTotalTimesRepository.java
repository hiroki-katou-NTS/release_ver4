/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.scherec.totaltimes;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository;
import nts.uk.ctx.at.shared.infra.entity.scherec.totaltimes.KshstTotalCondition;
import nts.uk.ctx.at.shared.infra.entity.scherec.totaltimes.KshstTotalConditionPK;
import nts.uk.ctx.at.shared.infra.entity.scherec.totaltimes.KshstTotalSubjects;
import nts.uk.ctx.at.shared.infra.entity.scherec.totaltimes.KshstTotalSubjectsPK;
import nts.uk.ctx.at.shared.infra.entity.scherec.totaltimes.KshstTotalTimes;
import nts.uk.ctx.at.shared.infra.entity.scherec.totaltimes.KshstTotalTimesPK;

/**
 * The Class JpaTotalTimesRepository.
 */
@Stateless
public class JpaTotalTimesRepository extends JpaRepository implements TotalTimesRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository#
	 * getAllTotalTimes(java.lang.String)
	 */
	@SneakyThrows
	@Override
	public List<TotalTimes> getAllTotalTimes(String companyId) {
		String sqlJdbc = "SELECT * " + "FROM KSHST_TOTAL_SUBJECTS KTS "
				+ "WHERE KTS.CID = ? ORDER BY KTS.TOTAL_TIMES_NO ASC";

		try (PreparedStatement stmt1 = this.connection().prepareStatement(sqlJdbc)) {

			stmt1.setString(1, companyId);

			List<KshstTotalSubjects> listTotalSubjects = new NtsResultSet(stmt1.executeQuery())
					.getList(rec -> {
						KshstTotalSubjectsPK kshstTotalSubjectsPK = new KshstTotalSubjectsPK();
						kshstTotalSubjectsPK.setCid(rec.getString("CID"));
						kshstTotalSubjectsPK.setTotalTimesNo(rec.getInt("TOTAL_TIMES_NO"));
						kshstTotalSubjectsPK.setWorkTypeAtr(rec.getInt("WORK_TYPE_ATR"));
						kshstTotalSubjectsPK.setWorkTypeCd(rec.getString("WORK_TYPE_CD"));

						KshstTotalSubjects entity = new KshstTotalSubjects();
						entity.setKshstTotalSubjectsPK(kshstTotalSubjectsPK);

						return entity;
					});

			Map<Integer, List<KshstTotalSubjects>> listTotalSubjectsMap = listTotalSubjects.stream()
					.collect(Collectors
							.groupingBy(item -> item.getKshstTotalSubjectsPK().getTotalTimesNo()));

			sqlJdbc = "SELECT * " + "FROM KSHST_TOTAL_TIMES KTT "
					+ "LEFT JOIN KSHST_TOTAL_CONDITION KTC ON KTT.CID = KTC.CID AND KTT.TOTAL_TIMES_NO = KTC.TOTAL_TIMES_NO "
					+ "WHERE KTT.CID = ? ORDER BY KTT.TOTAL_TIMES_NO ASC";

			try (PreparedStatement stmt2 = this.connection().prepareStatement(sqlJdbc)) {

				stmt2.setString(1, companyId);

				List<KshstTotalTimes> result = new NtsResultSet(stmt2.executeQuery())
						.getList(rec -> {

							KshstTotalConditionPK kshstTotalConditionPK = new KshstTotalConditionPK();
							kshstTotalConditionPK.setCid(rec.getString("CID"));
							kshstTotalConditionPK.setTotalTimesNo(rec.getInt("TOTAL_TIMES_NO"));

							KshstTotalCondition totalCondition = new KshstTotalCondition();
							totalCondition.setKshstTotalConditionPK(kshstTotalConditionPK);
							totalCondition.setUpperLimitSetAtr(rec.getInt("UPPER_LIMIT_SET_ATR"));
							totalCondition.setLowerLimitSetAtr(rec.getInt("LOWER_LIMIT_SET_ATR"));
							totalCondition
									.setThresoldUpperLimit(rec.getInt("THRESOLD_UPPER_LIMIT"));
							totalCondition
									.setThresoldLowerLimit(rec.getInt("THRESOLD_LOWER_LIMIT"));
							totalCondition.setAttendanceItemId(rec.getInt("ATD_ITEM_ID"));

							KshstTotalTimesPK kshstTotalTimesPK = new KshstTotalTimesPK();
							kshstTotalTimesPK.setCid(rec.getString("CID"));
							kshstTotalTimesPK.setTotalTimesNo(rec.getInt("TOTAL_TIMES_NO"));

							KshstTotalTimes entity = new KshstTotalTimes();
							entity.setKshstTotalTimesPK(kshstTotalTimesPK);
							entity.setUseAtr(rec.getInt("USE_ATR"));
							entity.setCountAtr(rec.getInt("COUNT_ATR"));
							entity.setTotalTimesName(rec.getString("TOTAL_TIMES_NAME"));
							entity.setTotalTimesAbname(rec.getString("TOTAL_TIMES_ABNAME"));
							entity.setSummaryAtr(rec.getInt("SUMMARY_ATR"));
							entity.setListTotalSubjects(listTotalSubjectsMap
									.getOrDefault(kshstTotalConditionPK.getTotalTimesNo(), Collections.emptyList()));
							entity.setTotalCondition(totalCondition);

							return entity;
						});

				if (result.isEmpty()) {
					return Collections.emptyList();
				}

				return result.stream()
						.map(entity -> new TotalTimes(new JpaTotalTimesGetMemento(entity)))
						.collect(Collectors.toList());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository#
	 * getTotalTimesDetail(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Optional<TotalTimes> getTotalTimesDetail(String companyId, Integer totalCountNo) {
		KshstTotalTimesPK kshstTotalTimesPK = new KshstTotalTimesPK(companyId, totalCountNo);

		Optional<KshstTotalTimes> optKshstTotalTimes = this.queryProxy().find(kshstTotalTimesPK, KshstTotalTimes.class);

		if (!optKshstTotalTimes.isPresent()) {
			return Optional.empty();
		}

		return Optional.of(new TotalTimes(new JpaTotalTimesGetMemento(optKshstTotalTimes.get())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository#update(
	 * nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes)
	 */
	@Override
	public void update(TotalTimes totalTimes) {
		Optional<KshstTotalTimes> optional = this.queryProxy().find(
				new KshstTotalTimesPK(totalTimes.getCompanyId().v(), totalTimes.getTotalCountNo()),
				KshstTotalTimes.class);

		if (!optional.isPresent()) {
			throw new RuntimeException("Total times not existed.");
		}

		KshstTotalTimes entity = optional.get();
		totalTimes.saveToMemento(new JpaTotalTimesSetMemento(entity));
		this.commandProxy().update(entity);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<TotalTimes> getTotalTimesDetailByListNo(String companyId, List<Integer> totalCountNos) {
		if(totalCountNos.isEmpty())
			return Collections.emptyList();
		List<TotalTimes> resultList = new ArrayList<>();
		CollectionUtil.split(totalCountNos, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			String sql = 
					"SELECT a.CID, a.TOTAL_TIMES_NO, a.USE_ATR, a.COUNT_ATR, a.TOTAL_TIMES_NAME, a.TOTAL_TIMES_ABNAME, a.SUMMARY_ATR, " +
					"b.WORK_TYPE_ATR, b.WORK_TYPE_CD, " +
					"c.UPPER_LIMIT_SET_ATR, c.LOWER_LIMIT_SET_ATR, c.THRESOLD_UPPER_LIMIT, c.THRESOLD_LOWER_LIMIT, c.ATD_ITEM_ID " +
					"FROM KSHST_TOTAL_TIMES a " +
					"LEFT JOIN KSHST_TOTAL_SUBJECTS b ON a.CID = b.CID AND a.TOTAL_TIMES_NO = b.TOTAL_TIMES_NO " +
					"JOIN KSHST_TOTAL_CONDITION c ON a.CID = c.CID AND a.TOTAL_TIMES_NO = c.TOTAL_TIMES_NO " +
					"WHERE a.CID = @companyId " +
					"AND a.TOTAL_TIMES_NO IN @totalCountNos";
			
			List<Object[]> objectLst = new NtsStatement(sql, this.jdbcProxy())
										.paramString("companyId", companyId)
										.paramInt("totalCountNos", subList)
										.getList(rec -> {
											Object[] object = new Object[100];
											object[0] = rec.getString("CID");
											object[1] = rec.getInt("TOTAL_TIMES_NO");
											object[2] = rec.getInt("USE_ATR");
											object[3] = rec.getInt("COUNT_ATR");
											object[4] = rec.getString("TOTAL_TIMES_NAME");
											object[5] = rec.getString("TOTAL_TIMES_ABNAME");
											object[6] = rec.getInt("SUMMARY_ATR");
											object[7] = rec.getInt("WORK_TYPE_ATR");
											object[8] = rec.getString("WORK_TYPE_CD");
											object[9] = rec.getInt("UPPER_LIMIT_SET_ATR");
											object[10] = rec.getInt("LOWER_LIMIT_SET_ATR");
											object[11] = rec.getInt("THRESOLD_UPPER_LIMIT");
											object[12] = rec.getInt("THRESOLD_LOWER_LIMIT");
											object[13] = rec.getInt("ATD_ITEM_ID");
											return object;
										});
			
			List<TotalTimes> totalTimesLst = objectLst.stream().collect(Collectors.groupingBy(x -> (Integer) x[1])).entrySet()
				.stream().map(x -> {
					KshstTotalTimes kshstTotalTimes = new KshstTotalTimes(new KshstTotalTimesPK(
							(String) x.getValue().get(0)[0], 
							(Integer) x.getValue().get(0)[1]));
					kshstTotalTimes.setUseAtr((Integer) x.getValue().get(0)[2]);
					kshstTotalTimes.setCountAtr((Integer) x.getValue().get(0)[3]);
					kshstTotalTimes.setTotalTimesName((String) x.getValue().get(0)[4]);
					kshstTotalTimes.setTotalTimesAbname((String) x.getValue().get(0)[5]);
					kshstTotalTimes.setSummaryAtr((Integer) x.getValue().get(0)[6]);
					
					KshstTotalCondition kshstTotalCondition = new KshstTotalCondition(new KshstTotalConditionPK(
							(String) x.getValue().get(0)[0], 
							(Integer) x.getValue().get(0)[1]));
					kshstTotalCondition.setUpperLimitSetAtr((Integer) x.getValue().get(0)[9]);
					kshstTotalCondition.setLowerLimitSetAtr((Integer) x.getValue().get(0)[10]);
					kshstTotalCondition.setThresoldUpperLimit((Integer) x.getValue().get(0)[11]);
					kshstTotalCondition.setThresoldLowerLimit((Integer) x.getValue().get(0)[12]);
					kshstTotalCondition.setAttendanceItemId((Integer) x.getValue().get(0)[13]);
					kshstTotalTimes.totalCondition = kshstTotalCondition;
					
					List<KshstTotalSubjects> kshstTotalSubjectsLst = x.getValue().stream()
							.map(y -> new KshstTotalSubjects(new KshstTotalSubjectsPK(
									(String) y[0], 
									(Integer) y[1], 
									(Integer) y[7], 
									(String) y[8])))
							.collect(Collectors.toList());
					kshstTotalTimes.listTotalSubjects = kshstTotalSubjectsLst;
					
					return new TotalTimes(new JpaTotalTimesGetMemento(kshstTotalTimes));
					
				}).collect(Collectors.toList());
			
			resultList.addAll(totalTimesLst);
		});
		return resultList;
	}

}
