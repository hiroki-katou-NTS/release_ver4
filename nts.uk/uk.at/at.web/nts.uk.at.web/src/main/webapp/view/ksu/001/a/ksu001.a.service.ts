 module ksu001.a.service {
    var paths: any = {
        getDataBasicSchedule: "screen/at/schedule/basicschedule/getData"
    }

    export function getDataBasicSchedule(obj): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.getDataBasicSchedule, obj);
    }
}