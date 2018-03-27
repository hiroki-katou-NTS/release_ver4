module nts.uk.at.view.kmk011.i {

    export module service {
        /**
         * define path to service
         */
        var path: any = {
            getAllDivTime: "at/record/divergence/time/message/divergenceTimeErrAlarmMsg/getAllDivTime",
            saveDivTimeErrAlarmMsg: "at/record/divergence/time/message/divergenceTimeErrAlarmMsg/save",
            findByDivergenceTimeNo: "at/record/divergence/time/message/divergenceTimeErrAlarmMsg/findByDivergenceTimeNo/",

            saveWorkTypeDivTimeErrAlarmMsg: "at/record/divergence/time/message/workTypeDivergenceTimeErrAlarmMsg/save",
            findByWorkTypeDivergenceTimeNo: "at/record/divergence/time/message/workTypeDivergenceTimeErrAlarmMsg/findByWorkTypeDivergenceTimeNo/"
        };

        export function saveDivTimeErrAlarmMsg(data: model.DivergenceTimeErrAlarmMsg): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.saveDivTimeErrAlarmMsg, data);
        }

        export function getAllDivTime(): JQueryPromise<Array<model.DivergenceTimeErrAlarmMsg>> {
            return nts.uk.request.ajax("at", path.getAllDivTime);
        }

        export function findByDivergenceTimeNo(divergenceTimeNo: number): JQueryPromise<model.DivergenceTimeErrAlarmMsg> {
            return nts.uk.request.ajax("at", path.findByDivergenceTimeNo + divergenceTimeNo);
        }

        export function saveWorkTypeDivTimeErrAlarmMsg(data: model.DivergenceTimeErrAlarmMsg): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.saveWorkTypeDivTimeErrAlarmMsg, data);
        }

        export function findByWorkTypeDivergenceTimeNo(divergenceTimeNo: number, workTypeCode: string): JQueryPromise<model.DivergenceTimeErrAlarmMsg> {
            return nts.uk.request.ajax("at", path.findByWorkTypeDivergenceTimeNo + divergenceTimeNo + "/" + workTypeCode);
        }
    }

    export module model {
        export class DivergenceTimeErrAlarmMsg {
            divergenceTimeNo: number;
            workTypeCode: string;
            divergenceTimeName: string;
            alarmMessage: string;
            errorMessage: string;

            constructor(divergenceTimeNo: number, workTypeCode: string, divergenceTimeName: string, alarmMessage: string, errorMessage: string) {
                this.divergenceTimeNo = divergenceTimeNo;
                this.workTypeCode = workTypeCode;
                this.divergenceTimeName = divergenceTimeName;
                this.alarmMessage = alarmMessage;
                this.errorMessage = errorMessage;
            }
        }
    }
}