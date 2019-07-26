package nts.uk.ctx.at.request.infra.repository.setting.request.gobackdirectlycommon;

import java.sql.PreparedStatement;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.setting.request.application.comment.CommentContent;
import nts.uk.ctx.at.request.dom.setting.request.application.comment.CommentFontColor;
import nts.uk.ctx.at.request.dom.setting.request.application.comment.FontWeightFlg;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSetting;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.GoBackDirectlyCommonSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.AppDisplayAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.CheckAtr;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.GoBackWorkType;
import nts.uk.ctx.at.request.dom.setting.request.gobackdirectlycommon.primitive.WorkChangeFlg;
import nts.uk.ctx.at.request.infra.entity.setting.request.gobackdirectlycommon.KrqstGoBackDirectSet;
import nts.uk.ctx.at.request.infra.entity.setting.request.gobackdirectlycommon.KrqstGoBackDirectSetPK;

@Stateless
public class JpaGoBackDirectlyCommonSettingRepository extends JpaRepository implements GoBackDirectlyCommonSettingRepository {

	public static final String SELECT_NO_WHERE = "SELECT c FROM KrqstGoBackDirectSet c";

	/**
	 * 
	 */
	public static final String SELECT_WITH_COMPANY_ID = SELECT_NO_WHERE 
			+ " WHERE c.krqstGoBackDirectSetPK.companyID =:companyID";

	/**
	 * @param entity
	 * @return
	 */
	private GoBackDirectlyCommonSetting toDomain(KrqstGoBackDirectSet entity) {
		return new GoBackDirectlyCommonSetting(entity.krqstGoBackDirectSetPK.companyID,
				EnumAdaptor.valueOf(entity.workChangeFlg, WorkChangeFlg.class),
				EnumAdaptor.valueOf(entity.workChangeTimeAtr, UseAtr.class),
				EnumAdaptor.valueOf(entity.perfomanceDisplayAtr, AppDisplayAtr.class),
				EnumAdaptor.valueOf(entity.contraditionCheckAtr, CheckAtr.class),
				EnumAdaptor.valueOf(entity.workType, GoBackWorkType.class),
				EnumAdaptor.valueOf(entity.lateLeaveEarlySettingAtr, CheckAtr.class),
				new CommentContent(entity.commentContent1),
				EnumAdaptor.valueOf(entity.commentFontWeight1, FontWeightFlg.class),
				new CommentFontColor(entity.commentFontColor1), 
				new CommentContent(entity.commentContent2),
				EnumAdaptor.valueOf(entity.commentFontWeight2, FontWeightFlg.class),
				new CommentFontColor(entity.commentFontColor2));
	}

	/**
	 * @param domain
	 * @return
	 */
	private KrqstGoBackDirectSet toEntity(GoBackDirectlyCommonSetting domain) {
		val entity = new KrqstGoBackDirectSet();
		entity.krqstGoBackDirectSetPK = new KrqstGoBackDirectSetPK();
		entity.krqstGoBackDirectSetPK.companyID = domain.getCompanyID();
		entity.workChangeFlg = domain.getWorkChangeFlg().value;
		entity.perfomanceDisplayAtr = domain.getPerformanceDisplayAtr().value;
		entity.contraditionCheckAtr = domain.getContraditionCheckAtr().value;
		entity.workType = domain.getGoBackWorkType().value;
		entity.lateLeaveEarlySettingAtr = domain.getLateLeaveEarlySettingAtr().value;
		entity.commentContent1 = domain.getCommentContent1().v();
		entity.commentFontWeight1 = domain.getCommentFontWeight1().value;
		entity.commentFontColor1 = domain.getCommentFontColor1().v();
		entity.commentContent2 = domain.getCommentContent2().v();
		entity.commentFontWeight2 = domain.getCommentFontWeight2().value;
		entity.commentFontColor2 = domain.getCommentFontColor2().v();
		entity.workChangeTimeAtr = domain.getWorkChangeTimeAtr().value;
		return entity;
	}

	@Override
	public Optional<GoBackDirectlyCommonSetting> findByCompanyID(String companyID) {
		//String ShainID = AppContexts.user().employeeId();
		//String companyID = AppContexts.user().companyId();
		return this.queryProxy().query(SELECT_WITH_COMPANY_ID, KrqstGoBackDirectSet.class)
				.setParameter("companyID", companyID)
				.getSingle(c -> toDomain(c));
	}

	/**
	 * 
	 */
	@Override
	public void insert(GoBackDirectlyCommonSetting goBackDirectlyCommonSettingItem) {
		this.commandProxy().insert(toEntity(goBackDirectlyCommonSettingItem));
	}

	/**
	 * 
	 */
	@Override
	public void update(GoBackDirectlyCommonSetting goBackDirectlyCommonSettingItem) {
		this.commandProxy().update(toEntity(goBackDirectlyCommonSettingItem));
	}

	/**
	 * 
	 */
	@Override
	public void delete(GoBackDirectlyCommonSetting goBackDirectlyCommonSettingItem) {
		this.commandProxy().remove(toEntity(goBackDirectlyCommonSettingItem));
	}

	@Override
	@SneakyThrows
	public Optional<UseAtr> getWorkChangeTimeAtr(String cid) {
		String sql = "SELECT WORK_CHANGE_TIME_ATR"
				+ " FROM KRQST_GO_BACK_DIRECT_SET"
				+ " WHERE CID = ?";
		try (PreparedStatement stmt = this.connection().prepareStatement(sql)){
			stmt.setString(1, cid);
			return new NtsResultSet(stmt.executeQuery())
					.getSingle(x -> EnumAdaptor.valueOf(x.getInt("WORK_CHANGE_TIME_ATR"), UseAtr.class));
		}
	}

}
