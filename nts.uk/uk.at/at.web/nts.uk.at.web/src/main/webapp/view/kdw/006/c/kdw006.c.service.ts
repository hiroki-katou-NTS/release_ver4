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

        updateIdentity: 'at/record/workrecord/operationsetting/updateIdentity',

        updateApproval: 'at/record/workrecord/operationsetting/updateApproval',

        updateDaily: 'at/record/workrecord/operationsetting/updateDaily',

        updateMonthly: 'at/record/workrecord/operationsetting/updateMonthly',

        updateAppType: 'at/function/dailyfix/update',
        
        getApplicationType: 'at/record/workrecord/operationsetting/findApplicationType',
    };

    export function updateAppType(cm: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateAppType, cm);
    }

    export function updateIdentity(dispRestric: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateIdentity, dispRestric);
    }

    export function updateApproval(dispRestric: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateApproval, dispRestric);
    }

    export function updateDaily(dispRestric: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateDaily, dispRestric);
    }

    export function updateMonthly(dispRestric: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.updateMonthly, dispRestric);
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
        
    export function getApplicationType(): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.getApplicationType);
    } 

}
