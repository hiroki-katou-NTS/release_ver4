module nts.uk.at.view.kal001.c {
    import ajax = nts.uk.request.ajax;

    export module service {

        var paths = {
            getEmployeeSendEmail: "at/function/alarm/kal/001/get/employee/sendEmail/",
            getAllMailSet: "at/function/alarm/mailsetting/getinformailseting",
            alarmListSendEmail: "at/function/alarm/kal/001/send-email"
        }


        export function getEmployeeSendEmail(processId: string): JQueryPromise<any> {
            return ajax("at", paths.getEmployeeSendEmail + processId);
        };

        export function getAllMailSet(): JQueryPromise<any> {
            return ajax("at", paths.getAllMailSet);
        };

        export function alarmListSendEmail(params: any): JQueryPromise<any> {
            return ajax("at", paths.alarmListSendEmail, params);
        };

    }
}
