module nts.uk.at.view.kdw006.h.service {
    let servicePath = {
        getApplicationType: 'at/record/workrecord/operationsetting/findApplicationType',
    
    }

    export function getApplicationType(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getApplicationType);
    } 
}
