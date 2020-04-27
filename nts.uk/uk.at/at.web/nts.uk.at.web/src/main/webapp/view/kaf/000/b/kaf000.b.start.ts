import model = nts.uk.at.view.kaf000.shr.model;

function initScreen(screenModel: any, listAppMeta: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata): void {
    if (currentApp.appType == 7) {
        getProgramName("KAF002", "C");
        screenModel = new nts.uk.at.view.kaf002.c.viewmodel.ScreenModel(listAppMeta, currentApp);
    } else if (currentApp.appType == 9) {
        getProgramName("KAF004", "E");
        screenModel = new nts.uk.at.view.kaf004.e.viewmodel.ScreenModel(listAppMeta, currentApp);
    } else if (currentApp.appType == 4) {
        getProgramName("KAF009", "B");
        screenModel = new nts.uk.at.view.kaf009.b.viewmodel.ScreenModel(listAppMeta, currentApp);
    } else if (currentApp.appType == 0) {
//        getProgramName("KAF005", "B");
        screenModel = new nts.uk.at.view.kaf005.b.viewmodel.ScreenModel(listAppMeta, currentApp);
    } else if (currentApp.appType == 2) {
        getProgramName("KAF007", "B");
        screenModel = new nts.uk.at.view.kaf007.b.viewmodel.ScreenModel(listAppMeta, currentApp);
    } else if (currentApp.appType == 6) {
        getProgramName("KAF010", "B");
        screenModel = new nts.uk.at.view.kaf010.b.viewmodel.ScreenModel(listAppMeta, currentApp);
    } else if (currentApp.appType == 1) {
        getProgramName("KAF006", "B");
        screenModel = new nts.uk.at.view.kaf006.b.viewmodel.ScreenModel(listAppMeta, currentApp);
    } else if (currentApp.appType == 10) {
        getProgramName("KAF011", "B");
        screenModel = new nts.uk.at.view.kaf011.b.viewmodel.ScreenModel(listAppMeta, currentApp);
    }
    __viewContext['viewModel'] = screenModel;
    if(currentApp.appType == 1 || currentApp.appType == 2 || currentApp.appType == 6 || currentApp.appType == 10) {
        bindScreen(screenModel, currentApp.appType)
//        __viewContext.bind(screenModel);
//        if (currentApp.appType == 10) {
//            $("#fixed-table").ntsFixedTable({ width: 100 });
//        }
//        nts.uk.ui._viewModel.errors.isEmpty.subscribe((values) => {
//            screenModel.errorEmpty(values);
//        });
//        $.get('/nts.uk.at.web/view/kaf/000/b/index2.xhtml').done(html => {
//            let htmlN = html.replace(/\<\?xml version='1\.0' encoding='UTF\-8' \?\>/, "");
//            let htmlF = htmlN.replace("<!DOCTYPE html>", "");
//            __viewContext.html = htmlF;
//        });     
    } else {
        screenModel.start(moment.utc().format("YYYY/MM/DD"), true).done(function() {
            bindScreen(screenModel, appType)
//            __viewContext.bind(screenModel);
//            if (currentApp.appType == 10) {
//                $("#fixed-table").ntsFixedTable({ width: 100 });
//            }
//            nts.uk.ui._viewModel.errors.isEmpty.subscribe((values) => {
//                screenModel.errorEmpty(values);
//            });
//            $.get('/nts.uk.at.web/view/kaf/000/b/index2.xhtml').done(html => {
//                let htmlN = html.replace(/\<\?xml version='1\.0' encoding='UTF\-8' \?\>/, "");
//                let htmlF = htmlN.replace("<!DOCTYPE html>", "");
//                __viewContext.html = htmlF;
//            });
        });
    }
}

function bindScreen(screenModel, appType) {
    __viewContext.bind(screenModel);
    if (appType == 10) {
        $("#fixed-table").ntsFixedTable({ width: 100 });
    }
    nts.uk.ui._viewModel.errors.isEmpty.subscribe((values) => {
        screenModel.errorEmpty(values);
    });
    $.get('/nts.uk.at.web/view/kaf/000/b/index2.xhtml').done(html => {
        let htmlN = html.replace(/\<\?xml version='1\.0' encoding='UTF\-8' \?\>/, "");
        let htmlF = htmlN.replace("<!DOCTYPE html>", "");
        __viewContext.html = htmlF;
    });      
}
    
__viewContext.initScreen = initScreen;
__viewContext.getProgramName = getProgramName;

__viewContext.ready(function() {
    var listAppMeta: Array<model.ApplicationMetadata> = [];
    var currentApp: model.ApplicationMetadata;
    var listValue: Array<string> = [];
    var currentValue: string = '';
    var screenModel: any = {};
    if (__viewContext.transferred.value == null || __viewContext.transferred.value === undefined) {
        nts.uk.characteristics.restore('paramInitKAF000').done((paramCache) => {
            if (paramCache != null && paramCache !== undefined) {
                listValue = paramCache.listAppMeta;
                currentValue = paramCache.currentApp;
                __viewContext.transferred.value = {'listAppMeta': listValue, 'currentApp': currentValue};
                nts.uk.at.view.kaf000.b.service.getAppByListID(listValue).done((data) => {
                    _.forEach(listValue, (value) => {
                        let appMeta = _.find(data, (o) => { return o.appID == value; });
                        if(!_.isUndefined(appMeta)) {
                            listAppMeta.push(appMeta);       
                        }
                    });
                    currentApp = _.find(listAppMeta, x => { return x.appID == currentValue; });
                    if(_.isUndefined(currentApp)) {
                        nts.uk.ui.dialog.alertError({ messageId: 'Msg_198' }).then(function() {
                            model.CommonProcess.callCMM045();
                        });        
                    } else {
                        initScreen(screenModel, listAppMeta, currentApp);    
                    }
                }).fail((res) => {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                        model.CommonProcess.callCMM045();
                    });
                });
            }
        });
    } else {
        listValue = __viewContext.transferred.value.listAppMeta;
        currentValue = __viewContext.transferred.value.currentApp;
        nts.uk.characteristics.save('paramInitKAF000', { 'listAppMeta': listValue, 'currentApp': currentValue });
        nts.uk.at.view.kaf000.b.service.getAppByListID(listValue).done((data) => {
            _.forEach(listValue, (value) => {
                let appMeta = _.find(data, (o) => { return o.appID == value; });
                if(!_.isUndefined(appMeta)) {
                    listAppMeta.push(appMeta);       
                }
            });
            currentApp = _.find(listAppMeta, x => { return x.appID == currentValue; });
            if(_.isUndefined(currentApp)) {
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_198' }).then(function() {
                    model.CommonProcess.callCMM045();
                });        
            } else {
                initScreen(screenModel, listAppMeta, currentApp);    
            }
        }).fail((res) => {
            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                model.CommonProcess.callCMM045();
            });
        });
    }
}

);

function getProgramName(programID: string, screenID: string){
    var namePath = nts.uk.text.format("sys/portal/standardmenu/findProgramName/{0}/{1}", programID, screenID);
    nts.uk.request.ajax("com", namePath).done((value) => {
        if(!nts.uk.util.isNullOrEmpty(value)){
            $("#pg-name").text(value);
        }else{
            $("#pg-name").text('');
        }   
    });           
}






