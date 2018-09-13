package nts.uk.ctx.at.record.infra.repository.monthly.anyitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonAnyItemValueMerge;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * リポジトリ実装：月別実績の任意項目
 * 
 * @author shuichu_ishida
 */
@Stateless
public class JpaAnyItemOfMonthly extends JpaRepository implements AnyItemOfMonthlyRepository {

	private static final String FIND_BY_MONTHLY_AND_CLOSURE = "SELECT a FROM KrcdtMonAnyItemValueMerge a "
			+ "WHERE a.krcdtMonAnyItemValuePk.employeeId = :employeeId "
			+ "AND a.krcdtMonAnyItemValuePk.yearMonth = :yearMonth "
			+ "AND a.krcdtMonAnyItemValuePk.closureId = :closureId "
			+ "AND a.krcdtMonAnyItemValuePk.closureDay = :closureDay "
			+ "AND a.krcdtMonAnyItemValuePk.isLastDay = :isLastDay ";

	private static final String FIND_BY_MONTHLY = "SELECT a FROM KrcdtMonAnyItemValueMerge a "
			+ "WHERE a.krcdtMonAnyItemValuePk.employeeId = :employeeId "
			+ "AND a.krcdtMonAnyItemValuePk.yearMonth = :yearMonth "
			+ "ORDER BY a.krcdtMonAnyItemValuePk.isLastDay DESC, a.krcdtMonAnyItemValuePk.closureDay ";

	private static final String FIND_BY_EMPLOYEES = "SELECT a FROM KrcdtMonAnyItemValueMerge a "
			+ "WHERE a.krcdtMonAnyItemValuePk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonAnyItemValuePk.yearMonth = :yearMonth "
			+ "AND a.krcdtMonAnyItemValuePk.closureId = :closureId "
			+ "AND a.krcdtMonAnyItemValuePk.closureDay = :closureDay "
			+ "AND a.krcdtMonAnyItemValuePk.isLastDay = :isLastDay "
			+ "ORDER BY a.krcdtMonAnyItemValuePk.employeeId ";

	private static final String FIND_BY_SIDS = "SELECT a FROM KrcdtMonAnyItemValueMerge a "
			+ "WHERE a.krcdtMonAnyItemValuePk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonAnyItemValuePk.yearMonth = :yearMonth "
			+ "AND a.krcdtMonAnyItemValuePk.closureId = :closureId "
			+ "AND a.krcdtMonAnyItemValuePk.closureDay = :closureDay "
			+ "AND a.krcdtMonAnyItemValuePk.isLastDay = :isLastDay " + "ORDER BY a.krcdtMonAnyItemValuePk.employeeId ";

	private static final String FIND_BY_SIDS_AND_MONTHS = "SELECT a FROM KrcdtMonAnyItemValueMerge a "
			+ "WHERE a.krcdtMonAnyItemValuePk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonAnyItemValuePk.yearMonth IN :yearMonths "
			+ "ORDER BY a.krcdtMonAnyItemValuePk.employeeId, a.krcdtMonAnyItemValuePk.yearMonth, a.krcdtMonAnyItemValuePk.isLastDay DESC, a.krcdtMonAnyItemValuePk.closureDay";

	private static final String DELETE_BY_MONTHLY_AND_CLOSURE = "DELETE FROM KrcdtMonAnyItemValueMerge a "
			+ "WHERE a.krcdtMonAnyItemValuePk.employeeId = :employeeId "
			+ "AND a.krcdtMonAnyItemValuePk.yearMonth = :yearMonth "
			+ "AND a.krcdtMonAnyItemValuePk.closureId = :closureId "
			+ "AND a.krcdtMonAnyItemValuePk.closureDay = :closureDay "
			+ "AND a.krcdtMonAnyItemValuePk.isLastDay = :isLastDay ";

	private static final String DELETE_BY_MONTHLY = "DELETE FROM KrcdtMonAnyItemValueMerge a "
			+ "WHERE a.krcdtMonAnyItemValuePk.employeeId = :employeeId "
			+ "AND a.krcdtMonAnyItemValuePk.yearMonth = :yearMonth ";

	/** 検索 */
	@Override
	public Optional<AnyItemOfMonthly> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, int anyItemId) {
		Optional<KrcdtMonAnyItemValueMerge> anyEntity = this.queryProxy()
				.find(new KrcdtMonMergePk(employeeId, yearMonth.v(), closureId.value, closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)), KrcdtMonAnyItemValueMerge.class);
		if (anyEntity.isPresent()) {
			return getItem(anyItemId, anyEntity.get());
		}

		return Optional.empty();
	}
	
	@Override
	public List<AnyItemOfMonthly> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, List<Integer> anyItemIds) {
		Optional<KrcdtMonAnyItemValueMerge> anyEntity = this.queryProxy()
				.find(new KrcdtMonMergePk(employeeId, yearMonth.v(), closureId.value, closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)), KrcdtMonAnyItemValueMerge.class);
		if (anyEntity.isPresent()) {
			KrcdtMonAnyItemValueMerge entity = anyEntity.get();
			return anyItemIds.stream().map(id -> getItem(id, entity).orElse(null)).filter(ai -> ai != null).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	private Optional<AnyItemOfMonthly> getItem(int anyItemId, KrcdtMonAnyItemValueMerge entity) {
		switch (anyItemId) {
		case 1:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue1, entity.countValue1, entity.moneyValue1, 1));
		case 2:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue2, entity.countValue2, entity.moneyValue2, 2));
		case 3:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue3, entity.countValue3, entity.moneyValue3, 3));
		case 4:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue4, entity.countValue4, entity.moneyValue4, 4));
		case 5:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue5, entity.countValue5, entity.moneyValue5, 5));
		case 6:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue6, entity.countValue6, entity.moneyValue6, 6));
		case 7:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue7, entity.countValue7, entity.moneyValue7, 7));
		case 8:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue8, entity.countValue8, entity.moneyValue8, 8));
		case 9:
			return Optional.ofNullable(
					entity.toDomainAnyItemOfMonthly(entity.timeValue9, entity.countValue9, entity.moneyValue9, 9));
		case 10:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue10, entity.countValue10,
					entity.moneyValue10, 10));
		case 11:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue11, entity.countValue11,
					entity.moneyValue11, 11));
		case 12:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue12, entity.countValue12,
					entity.moneyValue12, 12));
		case 13:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue13, entity.countValue13,
					entity.moneyValue13, 13));
		case 14:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue14, entity.countValue14,
					entity.moneyValue14, 14));
		case 15:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue15, entity.countValue15,
					entity.moneyValue15, 15));
		case 16:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue16, entity.countValue16,
					entity.moneyValue16, 16));
		case 17:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue17, entity.countValue17,
					entity.moneyValue17, 17));
		case 18:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue18, entity.countValue18,
					entity.moneyValue18, 18));
		case 19:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue19, entity.countValue19,
					entity.moneyValue19, 19));
		case 20:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue20, entity.countValue20,
					entity.moneyValue20, 20));
		case 21:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue21, entity.countValue21,
					entity.moneyValue21, 21));
		case 22:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue22, entity.countValue22,
					entity.moneyValue22, 22));
		case 23:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue23, entity.countValue23,
					entity.moneyValue23, 23));
		case 24:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue24, entity.countValue24,
					entity.moneyValue24, 24));
		case 25:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue25, entity.countValue25,
					entity.moneyValue25, 25));
		case 26:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue26, entity.countValue26,
					entity.moneyValue26, 26));
		case 27:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue27, entity.countValue27,
					entity.moneyValue27, 27));
		case 28:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue28, entity.countValue28,
					entity.moneyValue28, 28));
		case 29:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue29, entity.countValue29,
					entity.moneyValue29, 29));
		case 30:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue30, entity.countValue30,
					entity.moneyValue30, 30));
		case 31:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue31, entity.countValue31,
					entity.moneyValue31, 31));
		case 32:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue32, entity.countValue32,
					entity.moneyValue32, 32));
		case 33:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue33, entity.countValue33,
					entity.moneyValue33, 33));
		case 34:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue34, entity.countValue34,
					entity.moneyValue34, 34));
		case 35:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue35, entity.countValue35,
					entity.moneyValue35, 35));
		case 36:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue36, entity.countValue36,
					entity.moneyValue36, 36));
		case 37:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue37, entity.countValue37,
					entity.moneyValue37, 37));
		case 38:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue38, entity.countValue38,
					entity.moneyValue38, 38));
		case 39:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue39, entity.countValue39,
					entity.moneyValue39, 39));
		case 40:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue40, entity.countValue40,
					entity.moneyValue40, 40));
		case 41:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue41, entity.countValue41,
					entity.moneyValue41, 41));
		case 42:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue42, entity.countValue42,
					entity.moneyValue42, 42));
		case 43:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue43, entity.countValue43,
					entity.moneyValue43, 43));
		case 44:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue44, entity.countValue44,
					entity.moneyValue44, 44));
		case 45:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue45, entity.countValue45,
					entity.moneyValue45, 45));
		case 46:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue46, entity.countValue46,
					entity.moneyValue46, 46));
		case 47:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue47, entity.countValue47,
					entity.moneyValue47, 47));
		case 48:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue48, entity.countValue48,
					entity.moneyValue48, 48));
		case 49:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue49, entity.countValue49,
					entity.moneyValue49, 49));
		case 50:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue50, entity.countValue50,
					entity.moneyValue50, 50));
		case 51:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue51, entity.countValue51,
					entity.moneyValue51, 51));
		case 52:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue52, entity.countValue52,
					entity.moneyValue52, 52));
		case 53:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue53, entity.countValue53,
					entity.moneyValue53, 53));
		case 54:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue54, entity.countValue54,
					entity.moneyValue54, 54));
		case 55:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue55, entity.countValue55,
					entity.moneyValue55, 55));
		case 56:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue56, entity.countValue56,
					entity.moneyValue56, 56));
		case 57:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue57, entity.countValue57,
					entity.moneyValue57, 57));
		case 58:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue58, entity.countValue58,
					entity.moneyValue58, 58));
		case 59:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue59, entity.countValue59,
					entity.moneyValue59, 59));
		case 60:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue60, entity.countValue60,
					entity.moneyValue60, 60));
		case 61:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue61, entity.countValue61,
					entity.moneyValue61, 61));
		case 62:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue62, entity.countValue62,
					entity.moneyValue62, 62));
		case 63:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue63, entity.countValue63,
					entity.moneyValue63, 63));
		case 64:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue64, entity.countValue64,
					entity.moneyValue64, 64));
		case 65:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue65, entity.countValue65,
					entity.moneyValue65, 65));
		case 66:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue66, entity.countValue66,
					entity.moneyValue66, 66));
		case 67:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue67, entity.countValue67,
					entity.moneyValue67, 67));
		case 68:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue68, entity.countValue68,
					entity.moneyValue68, 68));
		case 69:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue69, entity.countValue69,
					entity.moneyValue69, 69));
		case 70:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue70, entity.countValue70,
					entity.moneyValue70, 70));
		case 71:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue71, entity.countValue71,
					entity.moneyValue71, 71));
		case 72:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue72, entity.countValue72,
					entity.moneyValue72, 72));
		case 73:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue73, entity.countValue73,
					entity.moneyValue73, 73));
		case 74:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue74, entity.countValue74,
					entity.moneyValue74, 74));
		case 75:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue75, entity.countValue75,
					entity.moneyValue75, 75));
		case 76:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue76, entity.countValue76,
					entity.moneyValue76, 76));
		case 77:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue77, entity.countValue77,
					entity.moneyValue77, 77));
		case 78:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue78, entity.countValue78,
					entity.moneyValue78, 78));
		case 79:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue79, entity.countValue79,
					entity.moneyValue79, 79));
		case 80:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue80, entity.countValue80,
					entity.moneyValue80, 80));
		case 81:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue81, entity.countValue81,
					entity.moneyValue81, 81));
		case 82:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue82, entity.countValue82,
					entity.moneyValue82, 82));
		case 83:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue83, entity.countValue83,
					entity.moneyValue83, 83));
		case 84:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue84, entity.countValue84,
					entity.moneyValue84, 84));
		case 85:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue85, entity.countValue85,
					entity.moneyValue85, 85));
		case 86:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue86, entity.countValue86,
					entity.moneyValue86, 86));
		case 87:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue87, entity.countValue87,
					entity.moneyValue87, 87));
		case 88:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue88, entity.countValue88,
					entity.moneyValue88, 88));
		case 89:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue89, entity.countValue89,
					entity.moneyValue89, 89));
		case 90:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue90, entity.countValue90,
					entity.moneyValue90, 90));
		case 91:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue91, entity.countValue91,
					entity.moneyValue91, 91));
		case 92:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue92, entity.countValue92,
					entity.moneyValue92, 92));
		case 93:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue93, entity.countValue93,
					entity.moneyValue93, 93));
		case 94:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue94, entity.countValue94,
					entity.moneyValue94, 94));
		case 95:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue95, entity.countValue95,
					entity.moneyValue95, 95));
		case 96:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue96, entity.countValue96,
					entity.moneyValue96, 96));
		case 97:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue97, entity.countValue97,
					entity.moneyValue97, 97));
		case 98:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue98, entity.countValue98,
					entity.moneyValue98, 98));
		case 99:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue99, entity.countValue99,
					entity.moneyValue99, 99));
		case 100:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue100, entity.countValue100,
					entity.moneyValue100, 100));
		case 101:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue101, entity.countValue101,
					entity.moneyValue101, 101));
		case 102:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue102, entity.countValue102,
					entity.moneyValue102, 102));
		case 103:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue103, entity.countValue103,
					entity.moneyValue103, 103));
		case 104:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue104, entity.countValue104,
					entity.moneyValue104, 104));
		case 105:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue105, entity.countValue105,
					entity.moneyValue105, 105));
		case 106:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue106, entity.countValue106,
					entity.moneyValue106, 106));
		case 107:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue107, entity.countValue107,
					entity.moneyValue107, 107));
		case 108:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue108, entity.countValue108,
					entity.moneyValue108, 108));
		case 109:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue109, entity.countValue109,
					entity.moneyValue109, 109));
		case 110:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue110, entity.countValue110,
					entity.moneyValue110, 110));
		case 111:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue111, entity.countValue111,
					entity.moneyValue111, 111));
		case 112:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue112, entity.countValue112,
					entity.moneyValue112, 112));
		case 113:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue113, entity.countValue113,
					entity.moneyValue113, 113));
		case 114:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue114, entity.countValue114,
					entity.moneyValue114, 114));
		case 115:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue115, entity.countValue115,
					entity.moneyValue115, 115));
		case 116:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue116, entity.countValue116,
					entity.moneyValue116, 116));
		case 117:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue117, entity.countValue117,
					entity.moneyValue117, 117));
		case 118:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue118, entity.countValue118,
					entity.moneyValue118, 118));
		case 119:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue119, entity.countValue119,
					entity.moneyValue119, 119));
		case 120:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue120, entity.countValue120,
					entity.moneyValue120, 120));
		case 121:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue121, entity.countValue121,
					entity.moneyValue121, 121));
		case 122:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue122, entity.countValue122,
					entity.moneyValue122, 122));
		case 123:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue123, entity.countValue123,
					entity.moneyValue123, 123));
		case 124:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue124, entity.countValue124,
					entity.moneyValue124, 124));
		case 125:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue125, entity.countValue125,
					entity.moneyValue125, 125));
		case 126:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue126, entity.countValue126,
					entity.moneyValue126, 126));
		case 127:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue127, entity.countValue127,
					entity.moneyValue127, 127));
		case 128:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue128, entity.countValue128,
					entity.moneyValue128, 128));
		case 129:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue129, entity.countValue129,
					entity.moneyValue129, 129));
		case 130:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue130, entity.countValue130,
					entity.moneyValue130, 130));
		case 131:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue131, entity.countValue131,
					entity.moneyValue131, 131));
		case 132:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue132, entity.countValue132,
					entity.moneyValue132, 132));
		case 133:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue133, entity.countValue133,
					entity.moneyValue133, 133));
		case 134:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue134, entity.countValue134,
					entity.moneyValue134, 134));
		case 135:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue135, entity.countValue135,
					entity.moneyValue135, 135));
		case 136:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue136, entity.countValue136,
					entity.moneyValue136, 136));
		case 137:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue137, entity.countValue137,
					entity.moneyValue137, 137));
		case 138:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue138, entity.countValue138,
					entity.moneyValue138, 138));
		case 139:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue139, entity.countValue139,
					entity.moneyValue139, 139));
		case 140:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue140, entity.countValue140,
					entity.moneyValue140, 140));
		case 141:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue141, entity.countValue141,
					entity.moneyValue141, 141));
		case 142:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue142, entity.countValue142,
					entity.moneyValue142, 142));
		case 143:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue143, entity.countValue143,
					entity.moneyValue143, 143));
		case 144:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue144, entity.countValue144,
					entity.moneyValue144, 144));
		case 145:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue145, entity.countValue145,
					entity.moneyValue145, 145));
		case 146:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue146, entity.countValue146,
					entity.moneyValue146, 146));
		case 147:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue147, entity.countValue147,
					entity.moneyValue147, 147));
		case 148:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue148, entity.countValue148,
					entity.moneyValue148, 148));
		case 149:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue149, entity.countValue149,
					entity.moneyValue149, 149));
		case 150:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue150, entity.countValue150,
					entity.moneyValue150, 150));
		case 151:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue151, entity.countValue151,
					entity.moneyValue151, 151));
		case 152:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue152, entity.countValue152,
					entity.moneyValue152, 152));
		case 153:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue153, entity.countValue153,
					entity.moneyValue153, 153));
		case 154:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue154, entity.countValue154,
					entity.moneyValue154, 154));
		case 155:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue155, entity.countValue155,
					entity.moneyValue155, 155));
		case 156:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue156, entity.countValue156,
					entity.moneyValue156, 156));
		case 157:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue157, entity.countValue157,
					entity.moneyValue157, 157));
		case 158:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue158, entity.countValue158,
					entity.moneyValue158, 158));
		case 159:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue159, entity.countValue159,
					entity.moneyValue159, 159));
		case 160:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue160, entity.countValue160,
					entity.moneyValue160, 160));
		case 161:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue161, entity.countValue161,
					entity.moneyValue161, 161));
		case 162:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue162, entity.countValue162,
					entity.moneyValue162, 162));
		case 163:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue163, entity.countValue163,
					entity.moneyValue163, 163));
		case 164:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue164, entity.countValue164,
					entity.moneyValue164, 164));
		case 165:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue165, entity.countValue165,
					entity.moneyValue165, 165));
		case 166:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue166, entity.countValue166,
					entity.moneyValue166, 166));
		case 167:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue167, entity.countValue167,
					entity.moneyValue167, 167));
		case 168:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue168, entity.countValue168,
					entity.moneyValue168, 168));
		case 169:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue169, entity.countValue169,
					entity.moneyValue169, 169));
		case 170:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue170, entity.countValue170,
					entity.moneyValue170, 170));
		case 171:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue171, entity.countValue171,
					entity.moneyValue171, 171));
		case 172:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue172, entity.countValue172,
					entity.moneyValue172, 172));
		case 173:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue173, entity.countValue173,
					entity.moneyValue173, 173));
		case 174:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue174, entity.countValue174,
					entity.moneyValue174, 174));
		case 175:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue175, entity.countValue175,
					entity.moneyValue175, 175));
		case 176:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue176, entity.countValue176,
					entity.moneyValue176, 176));
		case 177:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue177, entity.countValue177,
					entity.moneyValue177, 177));
		case 178:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue178, entity.countValue178,
					entity.moneyValue178, 178));
		case 179:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue179, entity.countValue179,
					entity.moneyValue179, 179));
		case 180:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue180, entity.countValue180,
					entity.moneyValue180, 180));
		case 181:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue181, entity.countValue181,
					entity.moneyValue181, 181));
		case 182:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue182, entity.countValue182,
					entity.moneyValue182, 182));
		case 183:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue183, entity.countValue183,
					entity.moneyValue183, 183));
		case 184:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue184, entity.countValue184,
					entity.moneyValue184, 184));
		case 185:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue185, entity.countValue185,
					entity.moneyValue185, 185));
		case 186:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue186, entity.countValue186,
					entity.moneyValue186, 186));
		case 187:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue187, entity.countValue187,
					entity.moneyValue187, 187));
		case 188:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue188, entity.countValue188,
					entity.moneyValue188, 188));
		case 189:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue189, entity.countValue189,
					entity.moneyValue189, 189));
		case 190:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue190, entity.countValue190,
					entity.moneyValue190, 190));
		case 191:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue191, entity.countValue191,
					entity.moneyValue191, 191));
		case 192:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue192, entity.countValue192,
					entity.moneyValue192, 192));
		case 193:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue193, entity.countValue193,
					entity.moneyValue193, 193));
		case 194:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue194, entity.countValue194,
					entity.moneyValue194, 194));
		case 195:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue195, entity.countValue195,
					entity.moneyValue195, 195));
		case 196:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue196, entity.countValue196,
					entity.moneyValue196, 196));
		case 197:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue197, entity.countValue197,
					entity.moneyValue197, 197));
		case 198:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue198, entity.countValue198,
					entity.moneyValue198, 198));
		case 199:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue199, entity.countValue199,
					entity.moneyValue199, 199));
		case 200:
			return Optional.ofNullable(entity.toDomainAnyItemOfMonthly(entity.timeValue200, entity.countValue200,
					entity.moneyValue200, 200));
		default: return Optional.empty();
		}
		
	}

	/** 検索 （月度と締め） */
	@Override
	public List<AnyItemOfMonthly> findByMonthlyAndClosure(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		Optional<KrcdtMonAnyItemValueMerge> anyItem = this.queryProxy()
				.query(FIND_BY_MONTHLY_AND_CLOSURE, KrcdtMonAnyItemValueMerge.class)
				.setParameter("employeeId", employeeId).setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value).setParameter("closureDay", closureDate.getClosureDay().v())
				.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0)).getSingle();

		return anyItem.isPresent() == true ? anyItem.get().toDomainAnyItemOfMonthly() : new ArrayList<>();
	}

	/** 検索 （月度） */
	@Override
	public List<AnyItemOfMonthly> findByMonthly(String employeeId, YearMonth yearMonth) {
		List<AnyItemOfMonthly> anyItemOfMonthly = new ArrayList<>();
		List<KrcdtMonAnyItemValueMerge> anyItemLst = this.queryProxy()
				.query(FIND_BY_MONTHLY, KrcdtMonAnyItemValueMerge.class).setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v()).getList();
		if (!anyItemLst.isEmpty()) {
			anyItemLst.stream().forEach(c -> {
				anyItemOfMonthly.addAll(c.toDomainAnyItemOfMonthly());
			});

		}

		return anyItemOfMonthly;
	}

	/** 検索 （社員IDリスト） */
	@Override
	public List<AnyItemOfMonthly> findByEmployees(List<String> employeeIds, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, int anyItemId) {

		List<AnyItemOfMonthly> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			List<KrcdtMonAnyItemValueMerge> anyItemLst = this.queryProxy()
					.query(FIND_BY_EMPLOYEES, KrcdtMonAnyItemValueMerge.class).setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v()).setParameter("closureId", closureId.value)
					.setParameter("closureDay", closureDate.getClosureDay().v())
					.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0)).getList();
			anyItemLst.stream().forEach(c -> {
				Optional<AnyItemOfMonthly> anyItem = c.toDomainAnyItemOfMonthly().stream().filter(any -> any.getAnyItemId() == anyItemId).findFirst();
				if (anyItem.isPresent()) {
					results.add(anyItem.get());
				}

			});
		});
		return results;
	}

	/** 検索 （社員IDリスト） */
	@Override
	public List<AnyItemOfMonthly> findByEmployees(List<String> employeeIds, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {

		List<AnyItemOfMonthly> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			List<KrcdtMonAnyItemValueMerge> anyItemLst = this.queryProxy()
					.query(FIND_BY_SIDS, KrcdtMonAnyItemValueMerge.class).setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v()).setParameter("closureId", closureId.value)
					.setParameter("closureDay", closureDate.getClosureDay().v())
					.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0)).getList();
			anyItemLst.stream().forEach(c -> {
				results.addAll(c.toDomainAnyItemOfMonthly());
			});
		});
		return results;
	}

	/** 検索 （社員IDリストと月度リスト） */
	@Override
	public List<AnyItemOfMonthly> findBySidsAndMonths(List<String> employeeIds, List<YearMonth> yearMonths) {

		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());

		List<AnyItemOfMonthly> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			List<KrcdtMonAnyItemValueMerge> anyItemLst = this.queryProxy()
					.query(FIND_BY_SIDS_AND_MONTHS, KrcdtMonAnyItemValueMerge.class)
					.setParameter("employeeIds", splitData).setParameter("yearMonths", yearMonthValues).getList();
			anyItemLst.stream().forEach(c -> {
				results.addAll(c.toDomainAnyItemOfMonthly());
			});
		});
		return results;
	}

	/** 登録および更新 */
	@Override
	public void persistAndUpdate(AnyItemOfMonthly domain) {

		// キー
		val key = new KrcdtMonMergePk(domain.getEmployeeId(), domain.getYearMonth().v(), domain.getClosureId().value,
				domain.getClosureDate().getClosureDay().v(), (domain.getClosureDate().getLastDayOfMonth() ? 1 : 0));

		// 登録・更新
		KrcdtMonAnyItemValueMerge entity = this.getEntityManager().find(KrcdtMonAnyItemValueMerge.class, key);
		if (entity == null) {
			entity = new KrcdtMonAnyItemValueMerge();
			entity.setKrcdtMonAnyItemValuePk(key);
			entity.toEntityAnyItemOfMonthly(domain);
		} else {
			entity.toEntityAnyItemOfMonthly(domain);
		}
		this.getEntityManager().persist(entity);
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(List<AnyItemOfMonthly> domain) {
		if(domain.isEmpty()){
			return;
		}
		// キー
		val key = new KrcdtMonMergePk(domain.get(0).getEmployeeId(), domain.get(0).getYearMonth().v(), domain.get(0).getClosureId().value,
				domain.get(0).getClosureDate().getClosureDay().v(), (domain.get(0).getClosureDate().getLastDayOfMonth() ? 1 : 0));

		// 登録・更新
		KrcdtMonAnyItemValueMerge entity = this.getEntityManager().find(KrcdtMonAnyItemValueMerge.class, key);
		if (entity == null) {
			entity = new KrcdtMonAnyItemValueMerge();
			entity.setKrcdtMonAnyItemValuePk(key);
		}
		for (AnyItemOfMonthly d : domain) {
			entity.toEntityAnyItemOfMonthly(d);
		}
		this.getEntityManager().persist(entity);
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate,
			int anyItemId) {
		Optional<KrcdtMonAnyItemValueMerge> anyItemLst = this.queryProxy()
				.query(FIND_BY_MONTHLY_AND_CLOSURE, KrcdtMonAnyItemValueMerge.class)
				.setParameter("employeeId", employeeId).setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value).setParameter("closureDay", closureDate.getClosureDay().v())
				.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0)).getSingle();
		if (anyItemLst.isPresent()) {
			KrcdtMonAnyItemValueMerge entity = anyItemLst.get();
			switch (anyItemId) {
			   case  1: entity.toDomainAnyItemOfMonthly(null,null,null, 1); break;
			   case  2: entity.toDomainAnyItemOfMonthly(null,null,null, 2); break;
			   case  3: entity.toDomainAnyItemOfMonthly(null,null,null, 3); break;
			   case  4: entity.toDomainAnyItemOfMonthly(null,null,null, 4); break;
			   case  5: entity.toDomainAnyItemOfMonthly(null,null,null, 5); break;
			   case  6: entity.toDomainAnyItemOfMonthly(null,null,null, 6); break;
			   case  7: entity.toDomainAnyItemOfMonthly(null,null,null, 7); break;
			   case  8: entity.toDomainAnyItemOfMonthly(null,null,null, 8); break;
			   case  9: entity.toDomainAnyItemOfMonthly(null,null,null, 9); break;
			   case  10: entity.toDomainAnyItemOfMonthly(null,null,null, 10); break;
			   case  11: entity.toDomainAnyItemOfMonthly(null,null,null, 11); break;
			   case  12: entity.toDomainAnyItemOfMonthly(null,null,null, 12); break;
			   case  13: entity.toDomainAnyItemOfMonthly(null,null,null, 13); break;
			   case  14: entity.toDomainAnyItemOfMonthly(null,null,null, 14); break;
			   case  15: entity.toDomainAnyItemOfMonthly(null,null,null, 15); break;
			   case  16: entity.toDomainAnyItemOfMonthly(null,null,null, 16); break;
			   case  17: entity.toDomainAnyItemOfMonthly(null,null,null, 17); break;
			   case  18: entity.toDomainAnyItemOfMonthly(null,null,null, 18); break;
			   case  19: entity.toDomainAnyItemOfMonthly(null,null,null, 19); break;
			   case  20: entity.toDomainAnyItemOfMonthly(null,null,null, 20); break;
			   case  21: entity.toDomainAnyItemOfMonthly(null,null,null, 21); break;
			   case  22: entity.toDomainAnyItemOfMonthly(null,null,null, 22); break;
			   case  23: entity.toDomainAnyItemOfMonthly(null,null,null, 23); break;
			   case  24: entity.toDomainAnyItemOfMonthly(null,null,null, 24); break;
			   case  25: entity.toDomainAnyItemOfMonthly(null,null,null, 25); break;
			   case  26: entity.toDomainAnyItemOfMonthly(null,null,null, 26); break;
			   case  27: entity.toDomainAnyItemOfMonthly(null,null,null, 27); break;
			   case  28: entity.toDomainAnyItemOfMonthly(null,null,null, 28); break;
			   case  29: entity.toDomainAnyItemOfMonthly(null,null,null, 29); break;
			   case  30: entity.toDomainAnyItemOfMonthly(null,null,null, 30); break;
			   case  31: entity.toDomainAnyItemOfMonthly(null,null,null, 31); break;
			   case  32: entity.toDomainAnyItemOfMonthly(null,null,null, 32); break;
			   case  33: entity.toDomainAnyItemOfMonthly(null,null,null, 33); break;
			   case  34: entity.toDomainAnyItemOfMonthly(null,null,null, 34); break;
			   case  35: entity.toDomainAnyItemOfMonthly(null,null,null, 35); break;
			   case  36: entity.toDomainAnyItemOfMonthly(null,null,null, 36); break;
			   case  37: entity.toDomainAnyItemOfMonthly(null,null,null, 37); break;
			   case  38: entity.toDomainAnyItemOfMonthly(null,null,null, 38); break;
			   case  39: entity.toDomainAnyItemOfMonthly(null,null,null, 39); break;
			   case  40: entity.toDomainAnyItemOfMonthly(null,null,null, 40); break;
			   case  41: entity.toDomainAnyItemOfMonthly(null,null,null, 41); break;
			   case  42: entity.toDomainAnyItemOfMonthly(null,null,null, 42); break;
			   case  43: entity.toDomainAnyItemOfMonthly(null,null,null, 43); break;
			   case  44: entity.toDomainAnyItemOfMonthly(null,null,null, 44); break;
			   case  45: entity.toDomainAnyItemOfMonthly(null,null,null, 45); break;
			   case  46: entity.toDomainAnyItemOfMonthly(null,null,null, 46); break;
			   case  47: entity.toDomainAnyItemOfMonthly(null,null,null, 47); break;
			   case  48: entity.toDomainAnyItemOfMonthly(null,null,null, 48); break;
			   case  49: entity.toDomainAnyItemOfMonthly(null,null,null, 49); break;
			   case  50: entity.toDomainAnyItemOfMonthly(null,null,null, 50); break;
			   case  51: entity.toDomainAnyItemOfMonthly(null,null,null, 51); break;
			   case  52: entity.toDomainAnyItemOfMonthly(null,null,null, 52); break;
			   case  53: entity.toDomainAnyItemOfMonthly(null,null,null, 53); break;
			   case  54: entity.toDomainAnyItemOfMonthly(null,null,null, 54); break;
			   case  55: entity.toDomainAnyItemOfMonthly(null,null,null, 55); break;
			   case  56: entity.toDomainAnyItemOfMonthly(null,null,null, 56); break;
			   case  57: entity.toDomainAnyItemOfMonthly(null,null,null, 57); break;
			   case  58: entity.toDomainAnyItemOfMonthly(null,null,null, 58); break;
			   case  59: entity.toDomainAnyItemOfMonthly(null,null,null, 59); break;
			   case  60: entity.toDomainAnyItemOfMonthly(null,null,null, 60); break;
			   case  61: entity.toDomainAnyItemOfMonthly(null,null,null, 61); break;
			   case  62: entity.toDomainAnyItemOfMonthly(null,null,null, 62); break;
			   case  63: entity.toDomainAnyItemOfMonthly(null,null,null, 63); break;
			   case  64: entity.toDomainAnyItemOfMonthly(null,null,null, 64); break;
			   case  65: entity.toDomainAnyItemOfMonthly(null,null,null, 65); break;
			   case  66: entity.toDomainAnyItemOfMonthly(null,null,null, 66); break;
			   case  67: entity.toDomainAnyItemOfMonthly(null,null,null, 67); break;
			   case  68: entity.toDomainAnyItemOfMonthly(null,null,null, 68); break;
			   case  69: entity.toDomainAnyItemOfMonthly(null,null,null, 69); break;
			   case  70: entity.toDomainAnyItemOfMonthly(null,null,null, 70); break;
			   case  71: entity.toDomainAnyItemOfMonthly(null,null,null, 71); break;
			   case  72: entity.toDomainAnyItemOfMonthly(null,null,null, 72); break;
			   case  73: entity.toDomainAnyItemOfMonthly(null,null,null, 73); break;
			   case  74: entity.toDomainAnyItemOfMonthly(null,null,null, 74); break;
			   case  75: entity.toDomainAnyItemOfMonthly(null,null,null, 75); break;
			   case  76: entity.toDomainAnyItemOfMonthly(null,null,null, 76); break;
			   case  77: entity.toDomainAnyItemOfMonthly(null,null,null, 77); break;
			   case  78: entity.toDomainAnyItemOfMonthly(null,null,null, 78); break;
			   case  79: entity.toDomainAnyItemOfMonthly(null,null,null, 79); break;
			   case  80: entity.toDomainAnyItemOfMonthly(null,null,null, 80); break;
			   case  81: entity.toDomainAnyItemOfMonthly(null,null,null, 81); break;
			   case  82: entity.toDomainAnyItemOfMonthly(null,null,null, 82); break;
			   case  83: entity.toDomainAnyItemOfMonthly(null,null,null, 83); break;
			   case  84: entity.toDomainAnyItemOfMonthly(null,null,null, 84); break;
			   case  85: entity.toDomainAnyItemOfMonthly(null,null,null, 85); break;
			   case  86: entity.toDomainAnyItemOfMonthly(null,null,null, 86); break;
			   case  87: entity.toDomainAnyItemOfMonthly(null,null,null, 87); break;
			   case  88: entity.toDomainAnyItemOfMonthly(null,null,null, 88); break;
			   case  89: entity.toDomainAnyItemOfMonthly(null,null,null, 89); break;
			   case  90: entity.toDomainAnyItemOfMonthly(null,null,null, 90); break;
			   case  91: entity.toDomainAnyItemOfMonthly(null,null,null, 91); break;
			   case  92: entity.toDomainAnyItemOfMonthly(null,null,null, 92); break;
			   case  93: entity.toDomainAnyItemOfMonthly(null,null,null, 93); break;
			   case  94: entity.toDomainAnyItemOfMonthly(null,null,null, 94); break;
			   case  95: entity.toDomainAnyItemOfMonthly(null,null,null, 95); break;
			   case  96: entity.toDomainAnyItemOfMonthly(null,null,null, 96); break;
			   case  97: entity.toDomainAnyItemOfMonthly(null,null,null, 97); break;
			   case  98: entity.toDomainAnyItemOfMonthly(null,null,null, 98); break;
			   case  99: entity.toDomainAnyItemOfMonthly(null,null,null, 99); break;
			   case  100: entity.toDomainAnyItemOfMonthly(null,null,null, 100); break;
			   case  101: entity.toDomainAnyItemOfMonthly(null,null,null, 101); break;
			   case  102: entity.toDomainAnyItemOfMonthly(null,null,null, 102); break;
			   case  103: entity.toDomainAnyItemOfMonthly(null,null,null, 103); break;
			   case  104: entity.toDomainAnyItemOfMonthly(null,null,null, 104); break;
			   case  105: entity.toDomainAnyItemOfMonthly(null,null,null, 105); break;
			   case  106: entity.toDomainAnyItemOfMonthly(null,null,null, 106); break;
			   case  107: entity.toDomainAnyItemOfMonthly(null,null,null, 107); break;
			   case  108: entity.toDomainAnyItemOfMonthly(null,null,null, 108); break;
			   case  109: entity.toDomainAnyItemOfMonthly(null,null,null, 109); break;
			   case  110: entity.toDomainAnyItemOfMonthly(null,null,null, 110); break;
			   case  111: entity.toDomainAnyItemOfMonthly(null,null,null, 111); break;
			   case  112: entity.toDomainAnyItemOfMonthly(null,null,null, 112); break;
			   case  113: entity.toDomainAnyItemOfMonthly(null,null,null, 113); break;
			   case  114: entity.toDomainAnyItemOfMonthly(null,null,null, 114); break;
			   case  115: entity.toDomainAnyItemOfMonthly(null,null,null, 115); break;
			   case  116: entity.toDomainAnyItemOfMonthly(null,null,null, 116); break;
			   case  117: entity.toDomainAnyItemOfMonthly(null,null,null, 117); break;
			   case  118: entity.toDomainAnyItemOfMonthly(null,null,null, 118); break;
			   case  119: entity.toDomainAnyItemOfMonthly(null,null,null, 119); break;
			   case  120: entity.toDomainAnyItemOfMonthly(null,null,null, 120); break;
			   case  121: entity.toDomainAnyItemOfMonthly(null,null,null, 121); break;
			   case  122: entity.toDomainAnyItemOfMonthly(null,null,null, 122); break;
			   case  123: entity.toDomainAnyItemOfMonthly(null,null,null, 123); break;
			   case  124: entity.toDomainAnyItemOfMonthly(null,null,null, 124); break;
			   case  125: entity.toDomainAnyItemOfMonthly(null,null,null, 125); break;
			   case  126: entity.toDomainAnyItemOfMonthly(null,null,null, 126); break;
			   case  127: entity.toDomainAnyItemOfMonthly(null,null,null, 127); break;
			   case  128: entity.toDomainAnyItemOfMonthly(null,null,null, 128); break;
			   case  129: entity.toDomainAnyItemOfMonthly(null,null,null, 129); break;
			   case  130: entity.toDomainAnyItemOfMonthly(null,null,null, 130); break;
			   case  131: entity.toDomainAnyItemOfMonthly(null,null,null, 131); break;
			   case  132: entity.toDomainAnyItemOfMonthly(null,null,null, 132); break;
			   case  133: entity.toDomainAnyItemOfMonthly(null,null,null, 133); break;
			   case  134: entity.toDomainAnyItemOfMonthly(null,null,null, 134); break;
			   case  135: entity.toDomainAnyItemOfMonthly(null,null,null, 135); break;
			   case  136: entity.toDomainAnyItemOfMonthly(null,null,null, 136); break;
			   case  137: entity.toDomainAnyItemOfMonthly(null,null,null, 137); break;
			   case  138: entity.toDomainAnyItemOfMonthly(null,null,null, 138); break;
			   case  139: entity.toDomainAnyItemOfMonthly(null,null,null, 139); break;
			   case  140: entity.toDomainAnyItemOfMonthly(null,null,null, 140); break;
			   case  141: entity.toDomainAnyItemOfMonthly(null,null,null, 141); break;
			   case  142: entity.toDomainAnyItemOfMonthly(null,null,null, 142); break;
			   case  143: entity.toDomainAnyItemOfMonthly(null,null,null, 143); break;
			   case  144: entity.toDomainAnyItemOfMonthly(null,null,null, 144); break;
			   case  145: entity.toDomainAnyItemOfMonthly(null,null,null, 145); break;
			   case  146: entity.toDomainAnyItemOfMonthly(null,null,null, 146); break;
			   case  147: entity.toDomainAnyItemOfMonthly(null,null,null, 147); break;
			   case  148: entity.toDomainAnyItemOfMonthly(null,null,null, 148); break;
			   case  149: entity.toDomainAnyItemOfMonthly(null,null,null, 149); break;
			   case  150: entity.toDomainAnyItemOfMonthly(null,null,null, 150); break;
			   case  151: entity.toDomainAnyItemOfMonthly(null,null,null, 151); break;
			   case  152: entity.toDomainAnyItemOfMonthly(null,null,null, 152); break;
			   case  153: entity.toDomainAnyItemOfMonthly(null,null,null, 153); break;
			   case  154: entity.toDomainAnyItemOfMonthly(null,null,null, 154); break;
			   case  155: entity.toDomainAnyItemOfMonthly(null,null,null, 155); break;
			   case  156: entity.toDomainAnyItemOfMonthly(null,null,null, 156); break;
			   case  157: entity.toDomainAnyItemOfMonthly(null,null,null, 157); break;
			   case  158: entity.toDomainAnyItemOfMonthly(null,null,null, 158); break;
			   case  159: entity.toDomainAnyItemOfMonthly(null,null,null, 159); break;
			   case  160: entity.toDomainAnyItemOfMonthly(null,null,null, 160); break;
			   case  161: entity.toDomainAnyItemOfMonthly(null,null,null, 161); break;
			   case  162: entity.toDomainAnyItemOfMonthly(null,null,null, 162); break;
			   case  163: entity.toDomainAnyItemOfMonthly(null,null,null, 163); break;
			   case  164: entity.toDomainAnyItemOfMonthly(null,null,null, 164); break;
			   case  165: entity.toDomainAnyItemOfMonthly(null,null,null, 165); break;
			   case  166: entity.toDomainAnyItemOfMonthly(null,null,null, 166); break;
			   case  167: entity.toDomainAnyItemOfMonthly(null,null,null, 167); break;
			   case  168: entity.toDomainAnyItemOfMonthly(null,null,null, 168); break;
			   case  169: entity.toDomainAnyItemOfMonthly(null,null,null, 169); break;
			   case  170: entity.toDomainAnyItemOfMonthly(null,null,null, 170); break;
			   case  171: entity.toDomainAnyItemOfMonthly(null,null,null, 171); break;
			   case  172: entity.toDomainAnyItemOfMonthly(null,null,null, 172); break;
			   case  173: entity.toDomainAnyItemOfMonthly(null,null,null, 173); break;
			   case  174: entity.toDomainAnyItemOfMonthly(null,null,null, 174); break;
			   case  175: entity.toDomainAnyItemOfMonthly(null,null,null, 175); break;
			   case  176: entity.toDomainAnyItemOfMonthly(null,null,null, 176); break;
			   case  177: entity.toDomainAnyItemOfMonthly(null,null,null, 177); break;
			   case  178: entity.toDomainAnyItemOfMonthly(null,null,null, 178); break;
			   case  179: entity.toDomainAnyItemOfMonthly(null,null,null, 179); break;
			   case  180: entity.toDomainAnyItemOfMonthly(null,null,null, 180); break;
			   case  181: entity.toDomainAnyItemOfMonthly(null,null,null, 181); break;
			   case  182: entity.toDomainAnyItemOfMonthly(null,null,null, 182); break;
			   case  183: entity.toDomainAnyItemOfMonthly(null,null,null, 183); break;
			   case  184: entity.toDomainAnyItemOfMonthly(null,null,null, 184); break;
			   case  185: entity.toDomainAnyItemOfMonthly(null,null,null, 185); break;
			   case  186: entity.toDomainAnyItemOfMonthly(null,null,null, 186); break;
			   case  187: entity.toDomainAnyItemOfMonthly(null,null,null, 187); break;
			   case  188: entity.toDomainAnyItemOfMonthly(null,null,null, 188); break;
			   case  189: entity.toDomainAnyItemOfMonthly(null,null,null, 189); break;
			   case  190: entity.toDomainAnyItemOfMonthly(null,null,null, 190); break;
			   case  191: entity.toDomainAnyItemOfMonthly(null,null,null, 191); break;
			   case  192: entity.toDomainAnyItemOfMonthly(null,null,null, 192); break;
			   case  193: entity.toDomainAnyItemOfMonthly(null,null,null, 193); break;
			   case  194: entity.toDomainAnyItemOfMonthly(null,null,null, 194); break;
			   case  195: entity.toDomainAnyItemOfMonthly(null,null,null, 195); break;
			   case  196: entity.toDomainAnyItemOfMonthly(null,null,null, 196); break;
			   case  197: entity.toDomainAnyItemOfMonthly(null,null,null, 197); break;
			   case  198: entity.toDomainAnyItemOfMonthly(null,null,null, 198); break;
			   case  199: entity.toDomainAnyItemOfMonthly(null,null,null, 199); break;
			   case  200: entity.toDomainAnyItemOfMonthly(null,null,null, 200); break;
			}
			this.commandProxy().update(entity);
		}
	}

	/** 削除 （月度と締め） */
	@Override
	public void removeByMonthlyAndClosure(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {

		this.getEntityManager().createQuery(DELETE_BY_MONTHLY_AND_CLOSURE).setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v()).setParameter("closureId", closureId.value)
				.setParameter("closureDay", closureDate.getClosureDay().v())
				.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0)).executeUpdate();
	}

	/** 削除 （月度） */
	@Override
	public void removeByMonthly(String employeeId, YearMonth yearMonth) {

		this.getEntityManager().createQuery(DELETE_BY_MONTHLY).setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v()).executeUpdate();
	}

}
