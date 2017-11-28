module nts.uk.at.view.kdw007.a.service {

    var paths: any = {
        getAll: "ctx/at/record/workrecord/erroralarm/getall",
        update: "ctx/at/record/workrecord/erroralarm/update",
        getEmploymentByCode: "bs/employee/employment/findByCode/",
        getClassificationByCode: "bs/employee/classification/find/",
        findAllJobTitle: "bs/employee/jobtitle/findAll",
        getBusinessTypeByCode: "at/record/businesstype/findByCode",
        getAllWorkType: "at/share/worktype/findAll",
        getWorkTypeByListCode: "at/share/worktype/getpossibleworktype",
        getAllWorkTime: "at/shared/worktime/findAll",
        getWorkTimeByListCode: "at/shared/worktime/findByCodes"
    }

    export function getAll() {
        return nts.uk.request.ajax(paths.getAll);
    }

    export function update(command) {
        return nts.uk.request.ajax(paths.update, command);
    }

    export function getEmploymentByCode(code) {
        return nts.uk.request.ajax("com", paths.getEmploymentByCode + code);
    }

    export function getClassificationByCode(code) {
        return nts.uk.request.ajax("com", paths.getClassificationByCode + code);
    }

    export function findAllJobTitle() {
        return nts.uk.request.ajax("com", paths.findAllJobTitle, { baseDate: moment().utc().toISOString() });
    }

    export function getBusinessTypeByCode(code) {
        return nts.uk.request.ajax("at", paths.getBusinessTypeByCode, code);
    }

    export function getAllWorkType() {
        return nts.uk.request.ajax("at", paths.getAllWorkType);
    }

    export function getWorkTypeByListCode(lstCode) {
        return nts.uk.request.ajax("at", paths.getWorkTypeByListCode, lstCode);
    }

    export function getAllWorkTime() {
        return nts.uk.request.ajax("at", paths.getAllWorkTime);
    }
    
     export function getWorkTimeByListCode(lstCode) {
        return nts.uk.request.ajax("at", paths.getWorkTimeByListCode, lstCode);
    }

}