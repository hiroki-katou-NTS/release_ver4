package nts.uk.ctx.core.infra.entity.socialinsurance.socialinsuranceoffice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.core.dom.socialinsurance.socialinsuranceoffice.SocialInsurancePrefectureInformation;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 社会保険用都道府県情報
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QPBMT_SOCIAL_INS_PRE_INFO")
public class QpbmtSocialInsuracePrefectureInfomation extends UkJpaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @EmbeddedId
    public QpbmtSocialInsPreInfoPk socialInsPreInfoPk;

    /**
     * 都道府県コード
     */
    @Basic(optional = false)
    @Column(name = "PREFECTURE_CODE")
    public String prefectureCode;

    /**
     * 都道府県名称
     */
    @Basic(optional = false)
    @Column(name = "PREFECTURE_NAME")
    public String prefectureName;

    @Override
    protected Object getKey() {
        return socialInsPreInfoPk;
    }

    /**
     * Entity to domain
     *
     * @param entity QpbmtSocialInsuranceOffice
     * @return SocialInsuranceOffice
     */
    public SocialInsurancePrefectureInformation toDomain(QpbmtSocialInsuracePrefectureInfomation entity) {
        return new SocialInsurancePrefectureInformation(entity.socialInsPreInfoPk.no, entity.prefectureCode, entity.prefectureName, entity.socialInsPreInfoPk.historyId);
    }

    /**
     * Domain to Entity
     *
     * @param domain SocialInsuranceOffice
     * @return QpbmtSocialInsuranceOffice
     */
    public static QpbmtSocialInsuracePrefectureInfomation toEntity(SocialInsurancePrefectureInformation domain) {
        return new QpbmtSocialInsuracePrefectureInfomation(new QpbmtSocialInsPreInfoPk(domain.getHistoryID(), domain.getNo()), domain.getPrefectureCode().v(), domain.getPrefectureName().v());
    }
}
