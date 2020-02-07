package nts.uk.ctx.at.schedule.infra.repository.shift.management;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.schedule.dom.shift.management.ShiftPalletsOrg;
import nts.uk.ctx.at.schedule.dom.shift.management.ShiftPalletsOrgRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.management.KscmtPaletteOrg;
import nts.uk.ctx.at.schedule.infra.entity.shift.management.KscmtPaletteOrgCombi;
import nts.uk.ctx.at.schedule.infra.entity.shift.management.KscmtPaletteOrgCombiDtl;
import nts.uk.ctx.at.schedule.infra.entity.shift.management.KscmtPaletteOrgCombiDtlPk;
import nts.uk.ctx.at.schedule.infra.entity.shift.management.KscmtPaletteOrgCombiPk;
import nts.uk.ctx.at.schedule.infra.entity.shift.management.KscmtPaletteOrgPk;

/**
 * 
 * @author phongtq
 *
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaShiftPalletOrgRepository extends JpaRepository implements ShiftPalletsOrgRepository {

	private static final String SELECT;

	private static final String FIND_BY_PAGE;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a.CID, a.TARGET_UNIT, a.TARGET_ID, a.PAGE, a.PAGE_NAME, a.USE_ATR, a.NOTE,");
		builderString.append("b.POSITION, b.POSITION_NAME,");
		builderString.append("c.POSITION_ORDER, c.SHIFT_MASTER_CD");
		builderString.append(
				"FROM KSCMT_PALETTE_ORG a LEFT JOIN KSCMT_PALETTE_ORG_COMBI b ON a.TARGET_UNIT = b.TARGET_UNIT AND a.TARGET_ID = b.TARGET_ID AND a.PAGE = b.PAGE ");
		builderString.append("LEFT JOIN KSCMT_PALETTE_ORG_COMBI_DTL c ON a.TARGET_UNIT = c.TARGET_UNIT AND a.TARGET_ID = c.TARGET_ID AND a.PAGE = c.PAGE ");
		SELECT = builderString.toString();

		builderString = new StringBuilder();
		builderString.append(SELECT);
		builderString.append("WHERE a.TARGET_UNIT = 'targetUnit' AND a.TARGET_ID = targetId AND a.PAGE = page");
		FIND_BY_PAGE = builderString.toString();

	}
	
	@AllArgsConstructor
	@Getter
	private class FullShiftPallets {
		public String companyId;
		public int targetUnit;
		public String targetId;
		public int page;
		public String pageName;
		public boolean useAtr;
		public String note;
		public int position;
		public String positionName;
		public int positionOrder;
		public String shiftMasterCd;
	}

	@SneakyThrows
	private List<FullShiftPallets> createShiftPallets(ResultSet rs) {
		List<FullShiftPallets> listFullData = new ArrayList<>();
		while (rs.next()) {
			listFullData.add(new FullShiftPallets(rs.getString("CID"),Integer.valueOf(rs.getString("TARGET_UNIT")), rs.getString("TARGET_ID"),Integer.valueOf(rs.getString("PAGE")),
					rs.getString("PAGE_NAME"), Integer.valueOf(rs.getString("USE_ATR")) == 1 ? true : false,
					rs.getString("NOTE"), Integer.valueOf(rs.getString("POSITION")), rs.getString("POSITION_NAME"),
					Integer.valueOf(rs.getString("POSITION_ORDER")), rs.getString("SHIFT_MASTER_CD")));
		}
		return listFullData;
	}

	private List<KscmtPaletteOrg> toEntity(List<FullShiftPallets> listFullJoin) {
		return listFullJoin.stream().collect(Collectors.groupingBy(FullShiftPallets::getPage)).entrySet().stream()
				.map(x -> {
					FullShiftPallets shiftPallets = x.getValue().get(0);
					KscmtPaletteOrgPk pk = new KscmtPaletteOrgPk(shiftPallets.getCompanyId(),shiftPallets.getTargetUnit(),shiftPallets.getTargetId() , shiftPallets.getPage());
					String pageName = shiftPallets.getPageName();
					boolean useAtr = shiftPallets.isUseAtr();
					String note = shiftPallets.getNote();
					List<KscmtPaletteOrgCombi> cmpCombis = x.getValue().stream()
							.collect(Collectors.groupingBy(FullShiftPallets::getPosition)).entrySet().stream()
							.map(y -> {
								KscmtPaletteOrgCombiPk combiPk = new KscmtPaletteOrgCombiPk(
										y.getValue().get(0).getCompanyId(), y.getValue().get(0).getTargetUnit(), y.getValue().get(0).getTargetId(), y.getValue().get(0).getPage(),
										y.getValue().get(0).getPosition());
								String positionName = y.getValue().get(0).getPositionName();
								List<KscmtPaletteOrgCombiDtl> cmpCombiDtls = y.getValue().stream()
										.collect(Collectors.groupingBy(FullShiftPallets::getPositionOrder)).entrySet()
										.stream().map(z -> {
											KscmtPaletteOrgCombiDtlPk cmpCombiDtlPk = new KscmtPaletteOrgCombiDtlPk(
													z.getValue().get(0).getCompanyId(),z.getValue().get(0).getTargetUnit(),z.getValue().get(0).getTargetId(), z.getValue().get(0).getPage(),
													z.getValue().get(0).getPosition(),
													z.getValue().get(0).getPositionOrder());
											String shiftMasterCd = z.getValue().get(0).getShiftMasterCd();
											return new KscmtPaletteOrgCombiDtl(cmpCombiDtlPk, shiftMasterCd);
										}).collect(Collectors.toList());
								return new KscmtPaletteOrgCombi(combiPk, positionName);
							}).collect(Collectors.toList());
					return new KscmtPaletteOrg(pk, pageName, useAtr ? 1 : 0, note);
				}).collect(Collectors.toList());
	}

	@Override
	public void add(ShiftPalletsOrg shiftPalletsOrg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ShiftPalletsOrg shiftPalletsOrg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ShiftPalletsOrg shiftPalletsOrg) {
		// TODO Auto-generated method stub

	}

	@Override
	public ShiftPalletsOrg findShiftPalletOrg(int targetUnit, String targetId, int page) {
		// TODO Auto-generated method stub
		return null;
	}

}
