package nts.uk.shr.com.system.config;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import lombok.Getter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * システム構成
 */
@ApplicationScoped
@Getter
@Slf4j
public class SystemConfiguration implements InitializeWhenDeploy {
	
	@Inject
	private SystemConfigurationRepository repository;

	/** システム名称 */
	private String systemName;
	
	/** インストール形式 */
	private InstallationType installationType;
	
	/** インストール製品情報 */
	private List<InstalledProduct> installedProducts;
	
	/** マニュアル置き場 */
	private String pathToManual;
	
	@Override
	public void initialize() {

		log.info("[INIT START] nts.uk.shr.com.system.config.SystemConfiguration");
		
		this.systemName = this.getValue("SystemName").asString().orElse("");
		this.installationType = this.getValue("InstallationType").asEnum(InstallationType.class).get();
		this.installedProducts = this.loadInstalledProducts();
		this.pathToManual = this.getValue("PathToManual").asString().orElse("");
		
		log.info("[INIT END] nts.uk.shr.com.system.config.SystemConfiguration");
	}
	
	public boolean isCloud() {
		return this.installationType == InstallationType.CLOUD;
	}
	
	public boolean isOnPremise() {
		return this.installationType == InstallationType.ON_PREMISES;
	}
	
	public boolean isInstalled(ProductType productType) {
		return this.installedProducts.stream().anyMatch(p -> p.getProductType().equals(productType));
	}
	
	public boolean isEnabled(ProductType productType) {
		return this.isInstalled(productType);
	}
	
	private List<InstalledProduct> loadInstalledProducts() {
		
		val list = new ArrayList<InstalledProduct>();
		
		this.getValue(ProductType.ATTENDANCE.systemConfigVersionKey).asString()
				.map(version -> new InstalledProduct(ProductType.ATTENDANCE, version))
				.ifPresent(p -> list.add(p));
		
		this.getValue(ProductType.PAYROLL.systemConfigVersionKey).asString()
				.map(version -> new InstalledProduct(ProductType.PAYROLL, version))
				.ifPresent(p -> list.add(p));
		
		this.getValue(ProductType.PERSONNEL.systemConfigVersionKey).asString()
				.map(version -> new InstalledProduct(ProductType.PERSONNEL, version))
				.ifPresent(p -> list.add(p));
		
		return list;
	}
	
	private SystemConfigurationValue getValue(String name) {
		val value = this.repository.get(name);
		
		log.info(name + " : " + value.asString().orElse("null"));
		
		return value;
	}
}
