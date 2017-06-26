/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.pr.core.infra.entity.rule.law.tax.residential.output;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 *
 * @author chinhbv
 */
@AllArgsConstructor
@Entity
@Table(name = "QCPMT_REGAL_DOC_COM")
public class QcpmtRegalDocCom extends UkJpaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public QcpmtRegalDocComPK qcpmtRegalDocComPK;
    
    @Basic(optional = false)
    @Column(name = "REGAL_DOC_CNAME")
    public String regalDocCname;
    @Column(name = "REGAL_DOC_CNAME_SJIS")
    public String regalDocCnameSjis;
    @Column(name = "REGAL_DOC_AB_CNAME")
    public String regalDocAbCname;
    @Column(name = "TEL_NO")
    public Long telNo;
    @Column(name = "PRESIDENT_NAME")
    public String presidentName;
    @Column(name = "PRESIDENT_TITLE")
    public String presidentTitle;
    @Basic(optional = false)
    @Column(name = "LINK_DEPCD")
    public String linkDepcd;
    @Column(name = "PAYER_POSTAL")
    public String payerPostal;
    @Basic(optional = false)
    @Column(name = "PAYER_ADDRESS1")
    public String payerAddress1;
    @Column(name = "PAYER_ADDRESS2")
    public String payerAddress2;
    @Column(name = "PAYER_KN_ADDRESS1")
    public String payerKnAddress1;
    @Column(name = "PAYER_KN_ADDRESS2")
    public String payerKnAddress2;
    @Column(name = "PAYER_TEL_NO")
    public String payerTelNo;
    @Column(name = "ACCOUNTING_OFFICER")
    public String accountingOfficer;
    @Column(name = "LIAISON_DEP")
    public String liaisonDep;
    @Column(name = "LIAISON_NAME")
    public String liaisonName;
    @Column(name = "LIAISON_TEL_NO")
    public String liaisonTelNo;
    @Column(name = "ACCOUNTING_FIRM")
    public String accountingFirm;
    @Column(name = "ACCOUNTING_FIRM_TEL_NO")
    public String accountingFirmTelNo;
    @Column(name = "PAYMENT_METHOD1")
    public String paymentMethod1;
    @Column(name = "PAYMENT_METHOD2")
    public String paymentMethod2;
    @Column(name = "PAYMENT_METHOD3")
    public String paymentMethod3;
    @Column(name = "BIZ_TYPE1")
    public String bizType1;
    @Column(name = "BIZ_TYPE2")
    public String bizType2;
    @Column(name = "BIZ_TYPE3")
    public String bizType3;
    @Column(name = "TAX_OFFICE")
    public String taxOffice;
    @Column(name = "BANK_NAME")
    public String bankName;
    @Column(name = "BANK_ADDRESS")
    public String bankAddress;
    @Column(name = "PAYER_RESI_TAX_REPORT_CD")
    public String payerResiTaxReportCd;
    @Column(name = "MEMO")
    public String memo;

    public QcpmtRegalDocCom() {
    }

    public QcpmtRegalDocCom(QcpmtRegalDocComPK qcpmtRegalDocComPK) {
        this.qcpmtRegalDocComPK = qcpmtRegalDocComPK;
    }

    public QcpmtRegalDocCom(QcpmtRegalDocComPK qcpmtRegalDocComPK, String regalDocCname, String linkDepcd, String payerAddress1) {
        this.qcpmtRegalDocComPK = qcpmtRegalDocComPK;
        this.regalDocCname = regalDocCname;
        this.linkDepcd = linkDepcd;
        this.payerAddress1 = payerAddress1;
    }

    public QcpmtRegalDocCom(String ccd, String regalDocCcd) {
        this.qcpmtRegalDocComPK = new QcpmtRegalDocComPK(ccd, regalDocCcd);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qcpmtRegalDocComPK != null ? qcpmtRegalDocComPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QcpmtRegalDocCom)) {
            return false;
        }
        QcpmtRegalDocCom other = (QcpmtRegalDocCom) object;
        if ((this.qcpmtRegalDocComPK == null && other.qcpmtRegalDocComPK != null) || (this.qcpmtRegalDocComPK != null && !this.qcpmtRegalDocComPK.equals(other.qcpmtRegalDocComPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.QcpmtRegalDocCom[ qcpmtRegalDocComPK=" + qcpmtRegalDocComPK + " ]";
    }

	@Override
	protected Object getKey() {
		return qcpmtRegalDocComPK;
	}
    
}
