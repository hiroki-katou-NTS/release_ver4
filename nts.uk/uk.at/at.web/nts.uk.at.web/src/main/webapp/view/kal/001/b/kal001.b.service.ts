module nts.uk.at.view.kal001.b {
    export module service {
        var paths = {
            getExtractAlarmData : "",
            exportAlarmData: "at/function/alarm/kal/001/export-alarm-data/"
        }
 
        /**
         * save file excel
         */
        export function exportAlarmData(processId): JQueryPromise<any> {
            return nts.uk.request.exportFile(paths.exportAlarmData + processId);
        };
    }
}
