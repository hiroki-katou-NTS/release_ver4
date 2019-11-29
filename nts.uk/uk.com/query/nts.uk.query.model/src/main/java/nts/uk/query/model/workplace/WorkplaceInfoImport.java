package nts.uk.query.model.workplace;

import lombok.Value;

@Value
public class WorkplaceInfoImport {

    private String workplaceId;

    private String hierarchyCode;

    private String workplaceCode;

    private String workplaceName;

    private String workplaceDisplayName;

    private String workplaceGenericName;

    private String workplaceExternalCode;

}
