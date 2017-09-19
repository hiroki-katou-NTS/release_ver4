/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.app.outsideot;

import nts.arc.layer.infra.file.export.FileGeneratorContext;

/**
 * The Interface OutsideOTSettingExportGenerator.
 */
public interface OutsideOTSettingExportGenerator {

	 /**
 	 * Generate.
 	 *
 	 * @param fileContext the file context
 	 * @param data the data
 	 */
    void generate(FileGeneratorContext fileContext, OutsideOTSettingData data);
}

