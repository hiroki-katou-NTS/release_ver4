import kaf002 = nts.uk.at.view.kaf002;
//import kaf009 = nts.uk.at.view.kaf009;
import model = nts.uk.at.view.kaf000.shr.model;

function initScreen(screenModel: any, listAppMeta: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata): void {
    if (currentApp.appType == 7) {
        getProgramName("KAF002", "C");
        screenModel = new kaf002.c.viewmodel.ScreenModel(listAppMeta, currentApp);
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
    screenModel.start(moment.utc().format("YYYY/MM/DD"), true).done(function() {
        __viewContext.bind(screenModel);
        if (currentApp.appType == 10) {
            $("#fixed-table").ntsFixedTable({ width: 100 });
        }
    });
}
    
__viewContext.initScreen = initScreen;
__viewContext.getProgramName = getProgramName;

__viewContext.ready(function() {
    var listAppMeta: Array<model.ApplicationMetadata> = [];
    var currentApp: model.ApplicationMetadata;
    var listValue: Array<string> = __viewContext.transferred.value.listAppMeta;
    var currentValue: string = __viewContext.transferred.value.currentApp;
    var screenModel: any = {};
    nts.uk.at.view.kaf000.b.service.getAppByListID(listValue)
        .done((data) => {
            _.forEach(listValue, (value) => {
                listAppMeta.push(_.find(data, (o) => { return o.appID == value; }));
            });
            currentApp = _.find(listAppMeta, x => {return x.appID == currentValue; });
            initScreen(screenModel, listAppMeta, currentApp);
        }).fail((res) =>{
            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                 model.CommonProcess.callCMM045();
            });
        });
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






