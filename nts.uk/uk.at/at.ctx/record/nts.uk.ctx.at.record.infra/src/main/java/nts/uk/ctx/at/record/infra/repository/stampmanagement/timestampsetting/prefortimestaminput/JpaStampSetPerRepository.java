package nts.uk.ctx.at.record.infra.repository.stampmanagement.timestampsetting.prefortimestaminput;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonDisSet;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonNameSet;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonSettings;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.ButtonType;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampPageLayout;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampSetPerRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampSettingPerson;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampType;
import nts.uk.ctx.at.record.infra.entity.stamp.management.KrcmtStampPerson;
import nts.uk.ctx.at.record.infra.entity.workrecord.stampmanagement.stamp.timestampsetting.prefortimestaminput.KrcmtStampLayoutDetail;
import nts.uk.ctx.at.record.infra.entity.workrecord.stampmanagement.stamp.timestampsetting.prefortimestaminput.KrcmtStampPageLayout;
import nts.uk.ctx.at.record.infra.entity.workrecord.stampmanagement.stamp.timestampsetting.prefortimestaminput.KrcmtStampPageLayoutPk;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author phongtq
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaStampSetPerRepository extends JpaRepository implements StampSetPerRepository {

	private static final String SELECT_ALL = "SELECT c FROM KrcmtStampPerson c ";

	private static final String SELECT_BY_CID = SELECT_ALL + " WHERE c.companyId = :companyId";

	private static final String SELECT_BY_CID_METHOD = SELECT_BY_CID ;

	private static final String SELECT_ALL_PAGE = "SELECT c FROM KrcmtStampPageLayout c ";

	private static final String SELECT_BY_CID_PAGE = SELECT_ALL_PAGE + " WHERE c.pk.companyId = :companyId";
	
	private static final String SELECT_BY_CID_PAGE_METHOD = SELECT_BY_CID_PAGE + " AND c.pk.stampMeans = :operationMethod";
	
	private static final String SELECT_BY_CID_PAGENO = SELECT_BY_CID_PAGE 
			+ " AND c.pk.stampMeans = :operationMethod"
			+ " AND c.pk.pageNo = :pageNo";

	/**
	 * 打刻の前準備(個人)を登録する
	 * insert KrcctStampDisplay
	 */
	@Override
	public void insert(StampSettingPerson stampSettingPerson) {
		commandProxy().insert(KrcmtStampPerson.toEntity(stampSettingPerson));
	}

	/**
	 * 打刻の前準備(個人)の設定を取得する
	 * update KrcctStampDisplay
	 */
	@Override
	public void update(StampSettingPerson stampSettingPerson) {
		
		Optional<KrcmtStampPerson> oldData = this.queryProxy().query(SELECT_BY_CID_METHOD, KrcmtStampPerson.class)
				.setParameter("companyId", stampSettingPerson.getCompanyId())
				.getSingle();
		if(oldData.isPresent()){
			KrcmtStampPerson newData = KrcmtStampPerson.toEntity(stampSettingPerson);
			oldData.get().correctionInterval = newData.correctionInterval;
			oldData.get().histDisplayMethod = newData.histDisplayMethod;
			oldData.get().resultDisplayTime = newData.resultDisplayTime;
			oldData.get().textColor = newData.textColor;
			oldData.get().buttonEmphasisArt = newData.buttonEmphasisArt;
		}

		this.commandProxy().update(oldData.get());
	}

	/**
	 * 打刻の前準備(個人)を表示する
	 * get Stamp Setting Person
	 */
	@Override
	public Optional<StampSettingPerson> getStampSet(String companyId) {
		return this.queryProxy().query(SELECT_BY_CID, KrcmtStampPerson.class)
				.setParameter("companyId", companyId).getSingle(c -> c.toDomain());
	}
	
	/**
	 * 打刻の前準備(個人)を表示する
	 * get Stamp Setting Person
	 */
	@Override
	public Optional<StampSettingPerson> getStampSetting(String companyId) {
		List<StampPageLayout> layouts = getAllStampSetPage(companyId);
		return this.queryProxy().query(SELECT_BY_CID, KrcmtStampPerson.class)
				.setParameter("companyId", companyId).getSingle(c -> c.toDomain(layouts));
	}

	/**
	 * 打刻レイアウトの設定内容を追加する
	 * insert KrcctStampPageLayout
	 */
	@Override
	public void insertPage(StampPageLayout layout) {
		String companyId = AppContexts.user().companyId();
		commandProxy().insert(KrcmtStampPageLayout.toEntity(layout, companyId, 1));
	}

	/**
	 * 打刻レイアウトの設定内容を更新する
	 * update KrcmtStampPageLayout
	 */
	@Override
	public void updatePage(StampPageLayout layout) {
		String companyId = AppContexts.user().companyId();
		
		this.commandProxy().update(KrcmtStampPageLayout.toEntity(layout, companyId, 1));
		
	}

	/**
	 * 打刻レイアウトの設定内容を取得する & 打刻レイアウトのページ変更時の表示をする
	 * get Stamp Page Layout
	 */
	@Override
	public Optional<StampPageLayout> getStampSetPage(String companyId, int pageNo) {
		return this.queryProxy().query(SELECT_BY_CID_PAGENO, KrcmtStampPageLayout.class)
				.setParameter("companyId", companyId)
				.setParameter("operationMethod", 1)
				.setParameter("pageNo", pageNo)
				.getSingle(c -> c.toDomain());
	}
	
	/**
	 * get Stamp Page Layout
	 */
	@Override
	public Optional<StampPageLayout> getStampSetPageByCid(String companyId) {
		return this.queryProxy().query(SELECT_BY_CID_PAGE_METHOD, KrcmtStampPageLayout.class)
				.setParameter("companyId", companyId)
				.setParameter("operationMethod", 1)
				.getSingle(c -> c.toDomain());
	}

	/**
	 * get All Stamp Page Layout
	 */
	@Override
	public List<StampPageLayout> getAllStampSetPage(String companyId) {
		List<StampPageLayout> data = this.queryProxy().query(SELECT_BY_CID_PAGE_METHOD, KrcmtStampPageLayout.class)
				.setParameter("companyId", companyId)
				.setParameter("operationMethod", 1)
				.getList(c -> c.toDomain());
		if (data.isEmpty())
			return Collections.emptyList();

		return data.stream().collect(Collectors.toList());
	}
	
	/**
	 * 打刻レイアウトの設定内容を削除する
	 * delete Stamp Page Layout
	 */
	@Override
	public void delete(String companyId, int pageNo) {
		Optional<StampPageLayout> newEntity = this.queryProxy().query(SELECT_BY_CID_PAGENO,KrcmtStampPageLayout.class)
				.setParameter("companyId", companyId)
				.setParameter("operationMethod", 1)
				.setParameter("pageNo", pageNo)
				.getSingle(c -> c.toDomain());
		if (newEntity.isPresent()) {
			this.commandProxy().remove(KrcmtStampPageLayout.class, new KrcmtStampPageLayoutPk(companyId,1, pageNo));
		}

	}

}
