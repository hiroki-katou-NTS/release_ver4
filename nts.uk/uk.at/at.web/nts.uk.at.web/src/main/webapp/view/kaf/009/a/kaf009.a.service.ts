module nts.uk.at.view.kaf009.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    import common = nts.uk.at.view.kaf009.share.common;
    var paths = {
        getAllWorkLocation: "at/record/worklocation/findall",
        getGoBackDirectly: "/at/request/application/gobackdirectly/getGoBackDirectlyByAppID",
        getGoBackDirectlySetting: "/at/request/application/gobackdirectly/getGoBackCommonSetting",
        insertGoBackDirectly: "/at/request/application/gobackdirectly/insertGoBackDirectly",
        checkInsertGoBackDirectly: "/at/request/application/gobackdirectly/checkBeforeChangeGoBackDirectly",
        updateGoBackDirectly: "/at/request/application/gobackdirectly/updateGoBackDirectly",
        confirmInconsistency: "/at/request/application/gobackdirectly/confirmInconsistency"
    }
    /**
     * get all Work Location  
     */
    export function getAllLocation(): JQueryPromise<any> {
        return ajax("at", paths.getAllWorkLocation, {});
    }

    /**
     * get GoBackDirectly
     */
    export function getGoBackDirectly(): JQueryPromise<any> {
        return ajax("at", paths.getGoBackDirectly, {});
    }

    /**
     * get GoBackSetting
     */
    export function getGoBackSetting(param : any): JQueryPromise<any> {
        return ajax("at", paths.getGoBackDirectlySetting, param);
    }

    /**
     * 
     */
    export function insertGoBackDirect(currentGoBack: common.GoBackCommand): JQueryPromise<Array<any>> {
        return ajax("at", paths.insertGoBackDirectly, currentGoBack);
    }
    
    /**
     * 
     */
    export function checkInsertGoBackDirect(currentGoBack: common.GoBackCommand): JQueryPromise<Array<any>> {
        return ajax("at", paths.checkInsertGoBackDirectly, currentGoBack);
    }
    /**
     * 
     */
    export function updateGoBackDirect(currentGoBack: common.GoBackCommand): JQueryPromise<Array<any>> {
        return ajax("at", paths.updateGoBackDirectly, currentGoBack);
    }
    
    export function confirmInconsistency(currentGoBack: common.GoBackCommand): JQueryPromise<Array<any>> {
        return ajax("at", paths.confirmInconsistency, currentGoBack);
    }
}