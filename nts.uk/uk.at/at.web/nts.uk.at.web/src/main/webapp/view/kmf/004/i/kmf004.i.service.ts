module nts.uk.at.view.kmf004.i.service {
    import format = nts.uk.text.format;
    import ajax = nts.uk.request.ajax;
    var basePath = "shared/specialholiday/specialholidayevent/";
    var paths: any = {
        getFrames: basePath + "getFrames",
        changeSpecialEvent: basePath + "changeSpecialEvent/{0}",
        save: basePath + "save",
        remove: basePath + "delete/{0}",
        findEmpByCodes: "bs/employee/employment/findNamesByCodesWithNull",
        findClsByCodes: "bs/employee/classification/getClsNameByCds"
    }

    export function getFrames() {
        return ajax(paths.getFrames);
    }

    export function save(command): JQueryPromise<void> {
        return ajax(paths.save, command);
    }

    export function remove(SheNoCd): JQueryPromise<void> {
        return nts.uk.request.ajax(format(paths.remove, SheNoCd));
    }

    export function changeSpecialEvent(sHENo) {
        return ajax(format(paths.changeSpecialEvent, sHENo));
     }
    
    export function findEmpByCodes(codes): JQueryPromise<any> {
        return ajax("com", paths.findEmpByCodes, codes);
    }
    
    export function findClsByCodes(codes): JQueryPromise<any> {
        return ajax("com", paths.findClsByCodes, codes);
    }

}   