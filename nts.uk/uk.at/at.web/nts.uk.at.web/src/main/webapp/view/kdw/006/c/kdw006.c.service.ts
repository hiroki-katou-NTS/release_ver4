module nts.uk.at.view.kdw006.c.service {
    let servicePath = {
        // C2_2, C2_3, C3_3
        getIdentity: 'at/record/workrecord/operationsetting/getIdentity',
        // C2_4, C2_5, C3_4
        getApproval: 'at/record/workrecord/operationsetting/getApproval',
        // C2_6 -> C2_12
        getDaily: 'at/record/workrecord/operationsetting/getdaily',
        // C3_2
        getMonthly: 'at/record/workrecord/operationsetting/getMonthy',

        getAppType: 'at/function/dailyfix/find',

        getRestrictConfirmEmp: 'at/record/workrecord/operationsetting/getRestrictConfirmEmp',

        updateMonthly: 'at/record/workrecord/operationsetting/updateMonthly',

        updateAppType: 'at/function/dailyfix/update',

        updateRestrictConfirmEmp: 'at/record/workrecord/operationsetting/updateRestrictConfirmEmp',

        updateDaiFuncControl: 'at/record/workrecord/operationsetting/updateDaiFuncControl',
        
        getApplicationType: 'at/record/workrecord/operationsetting/findApplicationType',
    };

    export function updateAppType(cm: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateAppType, cm);
    }

    export function updateMonthly(dispRestric: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateMonthly, dispRestric);
    }

    export function updateRestrictConfirmEmp(cm: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateRestrictConfirmEmp, cm);
    }

    export function updateDaiFuncControl(cm: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateDaiFuncControl, cm);
    }

    export function getAppType(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getAppType);
    }

    export function getIdentity(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getIdentity);
    }

    export function getApproval(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getApproval);
    }

    export function getDaily(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getDaily);
    }

    export function getMonthly(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getMonthly);
    }

    export function getRestrictConfirmEmp(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getRestrictConfirmEmp);
    }
        
    export function getApplicationType(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getApplicationType);
    } 

}
