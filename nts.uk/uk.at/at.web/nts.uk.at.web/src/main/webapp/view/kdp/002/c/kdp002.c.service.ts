module nts.uk.at.view.kdp002.c.service {
    let paths: any = {
        startScreen: "screen/at/personalengraving/startCScreen",
        registerDailyIdentify: "screen/at/personalengraving/registerDailyIdentify",
        getStampSetting: "at/record/stamp/management/getStampSetting",
        getStampPage: "at/record/stamp/management/getStampPage",
        deleteStampPage: "at/record/stamp/management/delete",
        getInfo: 'ctx/sys/auth/grant/rolesetperson/getempinfo/',
        NOTIFICATION_STAMP: 'at/record/stamp/notification_by_stamp',
    }

    export function registerDailyIdentify(): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.registerDailyIdentify);
    }

    export function startScreen(data: any): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.startScreen, data);
    }

    export function getStampSetting(): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.getStampSetting);
    }
    
    export function deleteStampPage(command: any) {
       return nts.uk.request.ajax("at", paths.deleteStampPage, command);
    }

    export function getStampPage(pageNo : number): JQueryPromise<any> {
        return nts.uk.request.ajx("at", paths.getStampPage + "/" + pageNo);
    }

    export function getEmpInfo(id: string) {
        return nts.uk.request.ajax("com", paths.getInfo + id);
    }

    export function getNotification(param: any) {
        return nts.uk.request.ajax("at", paths.NOTIFICATION_STAMP, param);
    }
        
}
