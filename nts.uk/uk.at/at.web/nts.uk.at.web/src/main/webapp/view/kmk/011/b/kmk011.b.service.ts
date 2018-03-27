module nts.uk.at.view.kmk011.b {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            findDivergenceTime: "at/record/divergencetime/setting/getdivtimeinfo/",
            getAllDivergenceTime: "at/record/divergencetime/setting/getalldivtime",
            updateDivergenceTime: "at/record/divergencetime/setting/updatedivtime",
            getAllAttendanceItem: "at/record/divergencetime/setting/getAttendanceDivergenceItem",
            getAllNameAttendance: "at/record/divergencetime/setting/AttendanceDivergenceName",
            getAllDivReason: "at/record/divergencetime/reason/getalldivreason/"

        };

        /**
        * get name(item da duoc chon)
        */
        export function getNameItemSelected(lstItemId: Array<number>): JQueryPromise<Array<viewmodel.model.DivergenceItem>> {
            return nts.uk.request.ajax("at", path.getAllNameAttendance, lstItemId);
        }

        /**
        * get all divergence time
        */
        export function getAllDivergenceTime(): JQueryPromise<Array<viewmodel.model.DivergenceTime>> {
            var result = nts.uk.request.ajax("at", path.getAllDivergenceTime);
            return result;
        }

        export function findDivergenceTime(divTimeId: number): JQueryPromise<viewmodel.model.DivergenceTime> {
            return nts.uk.request.ajax("at", path.findDivergenceTime + divTimeId);
        }

        /**
          * get all attendance item id(id co the chon)
        */
        export function getAllAttendanceItem(divergenceType: number): JQueryPromise<Array<viewmodel.model.AttendanceType>> {
            return nts.uk.request.ajax("at", path.getAllAttendanceItem);
        }

        /**
            * update divergence time
       */
        export function updateDivTime(divergenceTime: viewmodel.model.DivergenceTime): JQueryPromise<Array<viewmodel.model.DivergenceTimeItem>> {
            return nts.uk.request.ajax("at", path.updateDivergenceTime, divergenceTime);
        }


        /**
            * get all divergence reason
        */
        export function getAllDivReason(divTimeId: number): JQueryPromise<Array<viewmodel.model.DivergenceReason>> {
            return nts.uk.request.ajax("at", path.getAllDivReason + divTimeId);
        }


    }

}