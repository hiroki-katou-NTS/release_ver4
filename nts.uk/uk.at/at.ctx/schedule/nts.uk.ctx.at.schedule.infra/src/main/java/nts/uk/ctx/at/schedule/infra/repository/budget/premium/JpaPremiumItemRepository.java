package nts.uk.ctx.at.schedule.infra.repository.budget.premium;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumItem;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumItemRepository;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumName;
import nts.uk.ctx.at.schedule.dom.budget.premium.UseAttribute;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmnmpPremiumItemPK;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmnmtPremiumItem;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaPremiumItemRepository extends JpaRepository implements PremiumItemRepository{

	private static final String FIND_ALL = "SELECT a FROM KmnmtPremiumItem a WHERE a.kmnmpPremiumItemPK.companyID = :CID";
	
	private static final String FIND_BY_LIST_DISPLAY_NUMBER = FIND_ALL + " AND a.kmnmpPremiumItemPK.displayNumber IN :displayNumbers";
	
	private static final String FIND_BY_LIST_PREMIUM_NO_IS_USE = FIND_ALL +  " AND a.kmnmpPremiumItemPK.displayNumber NOT IN :displayNumbers AND a.useAtr = :useAtr";
	
	private static final String FIND_ALL_IS_USE = FIND_ALL + " AND a.useAtr = :useAtr";
	
	@Override
	public void update(PremiumItem premiumItem) {
		KmnmtPremiumItem item = this.queryProxy().find(new KmnmpPremiumItemPK(premiumItem.getCompanyID(),
				premiumItem.getDisplayNumber()), KmnmtPremiumItem.class).get();
		
		if(premiumItem.getUseAtr() == UseAttribute.Use){
			item.setUseAtr(premiumItem.getUseAtr().value);
			item.setName(premiumItem.getName().v());
		} else {
			item.setUseAtr(premiumItem.getUseAtr().value);
		}
		this.commandProxy().update(item);
	}
	
	@Override
	public List<PremiumItem> findByCompanyID(String companyID) {
		return this.queryProxy().query(FIND_ALL, KmnmtPremiumItem.class).setParameter("CID", companyID)
				.getList(x -> convertToDomain(x));
	}
	
	@Override
	public List<PremiumItem> findByCompanyIDAndDisplayNumber(String companyID, List<Integer> displayNumbers) {
		return this.queryProxy().query(FIND_BY_LIST_DISPLAY_NUMBER, KmnmtPremiumItem.class)
				.setParameter("CID", companyID)
				.setParameter("displayNumbers", displayNumbers)
				.getList(x -> convertToDomain(x));
	}
	
	@Override
	public List<PremiumItem> findByCompanyIDAndListPremiumNo (String companyID, List<Integer> premiumNo) {
		if (CollectionUtil.isEmpty(premiumNo)) {
			return this.findAllIsUse(companyID);
		}
		return this.queryProxy().query(FIND_BY_LIST_PREMIUM_NO_IS_USE, KmnmtPremiumItem.class)
				.setParameter("CID", companyID)
				.setParameter("displayNumbers", premiumNo)
				.setParameter("useAtr", UseAttribute.Use.value)
				.getList(x -> convertToDomain(x));
	}
	
	@Override
	public List<PremiumItem> findAllIsUse (String companyID) {
//		return this.queryProxy().query(FIND_ALL_IS_USE, KmnmtPremiumItem.class)
//				.setParameter("CID", companyID)
//				.setParameter("useAtr", UseAttribute.Use.value)
//				.getList(x -> convertToDomain(x));
		
		try {val statement = this.connection().prepareStatement("select * FROM KMNMT_PREMIUM_ITEM where CID = ? and USE_ATR = ?");
			statement.setString(1, companyID);
			statement.setInt(2, UseAttribute.Use.value);
			return new NtsResultSet(statement.executeQuery()).getList(rec -> {
				val entity = new KmnmtPremiumItem();
				entity.kmnmpPremiumItemPK = new KmnmpPremiumItemPK();
				entity.kmnmpPremiumItemPK.companyID = companyID;
				entity.kmnmpPremiumItemPK.displayNumber = rec.getInt("PREMIUM_NO");
				entity.useAtr = UseAttribute.Use.value;
				entity.name = rec.getString("PREMIUM_NAME");
				return entity;
			}).stream().map(e -> convertToDomain(e)).collect(Collectors.toList());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	/**
	 * convert PremiumItem Entity Object to PremiumItem Domain Object
	 * @param kmnmtPremiumItem PremiumItem Entity Object
	 * @return PremiumItem Domain Object
	 */ 
	private PremiumItem convertToDomain(KmnmtPremiumItem kmnmtPremiumItem){
		return new PremiumItem(
				kmnmtPremiumItem.kmnmpPremiumItemPK.companyID, 
				kmnmtPremiumItem.kmnmpPremiumItemPK.displayNumber,
				new PremiumName(kmnmtPremiumItem.name), 
				EnumAdaptor.valueOf(kmnmtPremiumItem.useAtr, UseAttribute.class));
	}

	
}
