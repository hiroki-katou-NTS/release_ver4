/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.command.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.Address1;
import nts.uk.ctx.pr.core.dom.insurance.Address2;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana1;
import nts.uk.ctx.pr.core.dom.insurance.AddressKana2;
import nts.uk.ctx.pr.core.dom.insurance.CitySign;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.OfficeMark;
import nts.uk.ctx.pr.core.dom.insurance.OfficeName;
import nts.uk.ctx.pr.core.dom.insurance.OfficeNoA;
import nts.uk.ctx.pr.core.dom.insurance.OfficeNoB;
import nts.uk.ctx.pr.core.dom.insurance.OfficeNoC;
import nts.uk.ctx.pr.core.dom.insurance.PhoneNumber;
import nts.uk.ctx.pr.core.dom.insurance.PicName;
import nts.uk.ctx.pr.core.dom.insurance.PicPosition;
import nts.uk.ctx.pr.core.dom.insurance.PotalCode;
import nts.uk.ctx.pr.core.dom.insurance.ShortName;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class LaborInsuranceOfficeDto.
 */
@Getter
@Setter
public class LaborInsuranceOfficeDto {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The short name. */
	private String shortName;

	/** The pic name. */
	private String picName;

	/** The pic position. */
	private String picPosition;

	/** The potal code. */
	private String potalCode;

	/** The address 1 st. */
	private String address1st;

	/** The address 2 nd. */
	private String address2nd;

	/** The kana address 1 st. */
	private String kanaAddress1st;

	/** The kana address 2 nd. */
	private String kanaAddress2nd;

	/** The phone number. */
	private String phoneNumber;

	/** The city sign. */
	private String citySign;

	/** The office mark. */
	private String officeMark;

	/** The office no A. */
	private String officeNoA;

	/** The office no B. */
	private String officeNoB;

	/** The office no C. */
	private String officeNoC;

	/** The memo. */
	private String memo;

	/** The version. */
	private long version;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the labor insurance office
	 */
	public LaborInsuranceOffice toDomain(String companyCode) {
		return new LaborInsuranceOffice(new LaborInsuranceOfficeGetMementoImpl(companyCode, this));
	}

	/**
	 * The Class LaborInsuranceOfficeGetMementoImpl.
	 */
	public class LaborInsuranceOfficeGetMementoImpl implements LaborInsuranceOfficeGetMemento {

		/** The dto. */
		private LaborInsuranceOfficeDto dto;

		/** The company code. */
		private String companyCode;

		/**
		 * Instantiates a new labor insurance office get memento impl.
		 *
		 * @param companyCode
		 *            the company code
		 * @param dto
		 *            the dto
		 */
		public LaborInsuranceOfficeGetMementoImpl(String companyCode, LaborInsuranceOfficeDto dto) {
			super();
			this.dto = dto;
			this.companyCode = companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getShortName()
		 */
		@Override
		public ShortName getShortName() {
			return new ShortName(dto.shortName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getPotalCode()
		 */
		@Override
		public PotalCode getPotalCode() {
			return new PotalCode(dto.potalCode);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getPicPosition()
		 */
		@Override
		public PicPosition getPicPosition() {
			return new PicPosition(dto.picPosition);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getPicName()
		 */
		@Override
		public PicName getPicName() {
			return new PicName(dto.picName);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getPhoneNumber()
		 */
		@Override
		public PhoneNumber getPhoneNumber() {
			return new PhoneNumber(dto.phoneNumber);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getOfficeNoC()
		 */
		@Override
		public OfficeNoC getOfficeNoC() {
			return new OfficeNoC(dto.officeNoC);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getOfficeNoB()
		 */
		@Override
		public OfficeNoB getOfficeNoB() {
			return new OfficeNoB(dto.officeNoB);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getOfficeNoA()
		 */
		@Override
		public OfficeNoA getOfficeNoA() {
			return new OfficeNoA(dto.officeNoA);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getOfficeMark()
		 */
		@Override
		public OfficeMark getOfficeMark() {
			return new OfficeMark(dto.officeMark);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getName()
		 */
		@Override
		public OfficeName getName() {
			return new OfficeName(dto.name);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getMemo()
		 */
		@Override
		public Memo getMemo() {
			return new Memo(dto.memo);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getKanaAddress2nd()
		 */
		@Override
		public AddressKana2 getKanaAddress2nd() {
			return new AddressKana2(dto.kanaAddress2nd);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getKanaAddress1st()
		 */
		@Override
		public AddressKana1 getKanaAddress1st() {
			return new AddressKana1(dto.kanaAddress1st);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getCompanyCode()
		 */
		@Override
		public String getCompanyCode() {
			return companyCode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getCode()
		 */
		@Override
		public OfficeCode getCode() {
			return new OfficeCode(dto.code);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getCitySign()
		 */
		@Override
		public CitySign getCitySign() {
			return new CitySign(dto.citySign);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getAddress2nd()
		 */
		@Override
		public Address2 getAddress2nd() {
			return new Address2(dto.address2nd);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeGetMemento
		 * #getAddress1st()
		 */
		@Override
		public Address1 getAddress1st() {
			return new Address1(dto.address1st);
		}

	}

}
