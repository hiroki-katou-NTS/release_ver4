module nts.uk.at.view.ksm002.b {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    import getText = nts.uk.resource.getText;
    export module service {
        var paths: any = {
            getAllSpecDate: "at/schedule/shift/businesscalendar/specificdate/getallspecificdate",
            getSpecDateByIsUse: "at/schedule/shift/businesscalendar/specificdate/getspecificdatebyuse/{0}",
            getCompanyStartDay: "at/schedule/shift/businesscalendar/businesscalendar/getcompanystartday",
            getCalendarWorkPlaceByCode: "at/schedule/shift/specificdayset/workplace/getworkplacespecificdate",
            insertCalendarWorkPlace: "at/schedule/shift/specificdayset/workplace/insertworkplacespecificdate",
            updateCalendarWorkPlace: "at/schedule/shift/specificdayset/workplace/updateworkplacespecificdate",
            deleteCalendarWorkPlace: "at/schedule/shift/specificdayset/workplace/deleteworkplacespecificdate"
        }
        
        export function getAllSpecDate(): JQueryPromise<any> {
            return ajax("at", paths.getAllSpecDate);
        }
        
        export function getSpecificDateByIsUse(useAtr: number): JQueryPromise<any> {
            return ajax("at", format(paths.getSpecDateByIsUse,useAtr));
        }
        
        export function getCompanyStartDay(): JQueryPromise<any> {
            return ajax("at", paths.getCompanyStartDay);
        }
        
        export function getCalendarWorkPlaceByCode(workplaceParam:any): JQueryPromise<any> {
            return ajax(paths.getCalendarWorkPlaceByCode, workplaceParam);
        }
        
        export function insertCalendarWorkPlace(command): JQueryPromise<any> {
            return ajax(paths.insertCalendarWorkPlace, command);
        }
        
        export function updateCalendarWorkPlace(command): JQueryPromise<any> {
            return ajax(paths.updateCalendarWorkPlace, command);
        }
        
        export function deleteCalendarWorkPlace(command): JQueryPromise<any> {
            return ajax(paths.deleteCalendarWorkPlace, command);
        }
               
        export function saveAsExcel(mode: string, startDate: string, endDate: string): JQueryPromise<any> {
            let program = nts.uk.ui._viewModel.kiban.programName().split(" ");
            let domainType = "KSM002";
            if (program.length > 1) {
                program.shift();
                domainType = domainType + program.join(" ");
            }
            return nts.uk.request.exportFile('/masterlist/report/print', {
                domainId: 'SpecificdaySet',
                domainType: domainType,
                languageId: 'ja', reportType: 0, mode: mode,
                startDate: moment.utc(startDate).format(),
                endDate: moment.utc(endDate).format()
            });
        }
    }
}