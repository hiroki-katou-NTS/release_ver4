﻿package nts.uk.ctx.at.request.infra.entity.application.workchange;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
/**
* 勤務変更申請: 主キー情報
*/
public class KrqdtAppWorkChangePk implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 会社ID
     */
    @Basic(optional = false)
    @Column(name = "CID")
    private String cid;
    
    /**
     * 申請ID
     */
    @Basic(optional = false)
    @Column(name = "APP_ID")
    private String appId;
}
