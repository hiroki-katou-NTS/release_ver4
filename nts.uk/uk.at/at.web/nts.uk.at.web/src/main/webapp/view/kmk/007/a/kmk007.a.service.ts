module nts.uk.at.view.kmk007.a.service {

    var paths: any = {
        findAll: "at/share/worktype/findAll",
        addWorkType: "at/share/worktype/add",
        removeWorkType: "at/share/worktype/remove",
        updateWorkType: "at/share/worktype/update",
        findWorkTypeSet: "at/share/worktype/find/{0}",
        findByLangId: "at/share/worktype/getByCIdAndLangId",
        getAllAbsenceFrame: "at/share/worktype/absenceframe/findAll",
        getAllSpecialHolidayFrame: "at/share/worktype/specialholidayframe/findAll",
        insertWorkTypeLang: "at/share/worktype/language/insert",
        saveAsExcel: "file/at/worktypereport/saveAsExcel"
    }

    export function loadWorkType(): JQueryPromise<Array<any>> {
        var path = paths.findAll;
        return nts.uk.request.ajax(path);
    }

    export function addWorkType(isCreated, workType: any): JQueryPromise<any> {
        var path = isCreated ? paths.addWorkType : paths.updateWorkType;
        return nts.uk.request.ajax("at", path, workType);
    }

    export function deleteWorkType(workTypeCd): JQueryPromise<any> {
        var path = paths.removeWorkType;
        var obj = {
            workTypeCd: workTypeCd
        };
        return nts.uk.request.ajax("at", path, obj);
    }

    export function findWorkTypeSet(workTypeCd: string): JQueryPromise<any> {
        var path = nts.uk.text.format(paths.findWorkTypeSet, workTypeCd);
        return nts.uk.request.ajax(path);
    }

    export function findByLangId(langId: string): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.findByLangId + '/' + langId);
    }

    /**
 *  Get all Absence Frame
 */
    export function getAllAbsenceFrame(): JQueryPromise<Array<any>> {
        var path = paths.getAllAbsenceFrame;
        return nts.uk.request.ajax("at", path);
    }

    /**
     *  Get all Special Holiday Frame
     */
    export function getAllSpecialHolidayFrame(): JQueryPromise<Array<any>> {
        var path = paths.getAllSpecialHolidayFrame;
        return nts.uk.request.ajax("at", path);
    }

    export function insert(workTypeLanguage: any): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.insertWorkTypeLang, workTypeLanguage);
    }
    
     export function saveAsExcel(command: any): JQueryPromise<any> {
        return nts.uk.request.exportFile('/masterlist/report/print', {domainId: "WorkType", domainType: "test"});
    }
}