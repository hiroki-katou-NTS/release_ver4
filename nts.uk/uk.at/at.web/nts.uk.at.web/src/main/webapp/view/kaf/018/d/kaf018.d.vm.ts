module nts.uk.at.view.kaf018.d.viewmodel {
    import text = nts.uk.resource.getText;
    import getShared = nts.uk.ui.windows.getShared;
    import formatDate = nts.uk.time.formatDate;
    import shareModel = kaf018.share.model.REFLECTEDSTATUS;
    import shared = kaf018.share.model;
    import block = nts.uk.ui.block;

    export class ScreenModel {
        selectedEmpId: KnockoutObservable<string> = ko.observable('');
        listStatusEmp: Array<ApprovalStatusEmployee> = [];

        columns: KnockoutObservableArray<NtsGridListColumn>;
        listDataDisp: KnockoutObservableArray<ContentDisp> = ko.observableArray([]);
        dispEmpName: string = "";
        lstAppCompltLeaveSync: Array<AppCompltLeaveSync> = [];
        lstContent: Array<Content> = [];
        displayPrePostFlg: boolean;
        constructor() {
            var self = this;
        }

        /**
         * 起動する
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            block.invisible();
            let params = getShared("KAF018D_VALUE");
            if (params) {
                self.dispEmpName = params.empName;
                _.each(params.listStatusEmp, function(item) {
                    self.listStatusEmp.push(new ApprovalStatusEmployee(item.sid, new Date(item.startDate), new Date(item.endDate)));
                });
            }
            let paramsTranfer = {
                listStatusEmp: self.listStatusEmp,
                selectedEmpId: params.selectedEmpId
            };
            service.initApprovalSttRequestContentDis(paramsTranfer).done(function(data: ApplicationList) {
                self.lstContent = data.listAppDetail;
                self.lstAppCompltLeaveSync = data.lstAppCompltLeaveSync;
                self.displayPrePostFlg = data.displayPrePostFlg;
                self.initExTable(self.lstContent, self.lstAppCompltLeaveSync, self.displayPrePostFlg);
                block.clear();
                dfd.resolve();
            }).fail(function(data: any) {
                block.clear();
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Create exTable
         */
        initExTable(listData: Array<Content>, lstAppComplt: Array<AppCompltLeaveSync>, displayPrePostFlg: boolean): void {
            var self = this;
            let index = 0;
            _.each(listData, function(data: Content) {
                let appStartDate: string = data.appStartDate.toString();
                let appEndDate: string = '';
                let contentDisp: ContentDisp;
                if (data.appType == shared.ApplicationType.COMPLEMENT_LEAVE_APPLICATION) {
                    let complt: AppCompltLeaveSync = self.findCompltLeave(data.applicationID, lstAppComplt);
                    console.log(complt);
                    if(!complt) return; 
                    if (complt.sync==true) {
                        contentDisp = self.formatCompltSync(data, complt);
                    } else {
                        contentDisp = self.formatCompltLeave(data, complt);
                    }
                     self.listDataDisp.push(contentDisp);
                } else {
                    if (data.appType == shared.ApplicationType.ABSENCE_APPLICATION || data.appType == shared.ApplicationType.WORK_CHANGE_APPLICATION || data.appType == shared.ApplicationType.BUSINESS_TRIP_APPLICATION) {
                        appEndDate = data.appEndDate;
                    } else {
                        appEndDate == "";
                    }
                    let dateRange = self.appDateRangeColor(self.convertDateMDW(data.appStartDate), self.convertDateMDW(appEndDate));
                    let reflectStateContent = self.disReflectionStatus(data.reflectState);
                    self.listDataDisp.push(new ContentDisp(data.applicationID, data.appName, data.prePostAtr == 1 ? "事後" : "事前", dateRange, data.appContent, data.reflectState, reflectStateContent, self.disApprovalStatus(data.approvalStatus), data.phase1, data.phase2, data.phase3, data.phase4, data.phase5));
                }
            });
            console.log(self.listDataDisp());
            let colorBackGr = self.fillColorbackGr();
            self.reloadGridApplicaion(colorBackGr, displayPrePostFlg);
        }

        /**
         * find application holiday work by id
         */
        findCompltLeave(appId: string, lstCompltLeave: Array<AppCompltLeaveSync>): AppCompltLeaveSync {
            return _.find(lstCompltLeave, function(complt) {
                return complt.appMain.appID == appId;
            });
        }
        reloadGridApplicaion(colorBackGr: any, displayPrePostFlg: boolean) {
            var self = this;
            $("#grid1").ntsGrid({
                width: '1120px',
                height: '450px',
                dataSource: self.listDataDisp(),
                primaryKey: 'appId',
                virtualization: true,
                rows: 20,
                hidePrimaryKey: true,
                rowVirtualization: true,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: "", key: 'appId', dataType: 'string', width: '0px', hidden: true },
                    { headerText: text("KAF018_36"), key: 'appName', dataType: 'string', width: 160 },
                    { headerText: text("KAF018_37"), key: 'prePostAtr', dataType: 'string',hidden: displayPrePostFlg== false ? true: false, width: 120 },
                    { headerText: text("KAF018_38"), key: 'appDate', dataType: 'string', width: 150 },
                    { headerText: text("KAF018_39"), key: 'appContent', dataType: 'string', width: 450 },
                    { headerText: text("KAF018_40"), key: 'reflectStateContent', dataType: 'string', width: 100 },
                    { headerText: text("KAF018_41"), key: 'approvalStatus', dataType: 'string', width: 150 },
                    { headerText: text("KAF018_42"), key: 'phase1', dataType: 'string', width: 150 },
                    { headerText: text("KAF018_43"), key: 'phase2', dataType: 'string', width: 150 },
                    { headerText: text("KAF018_44"), key: 'phase3', dataType: 'string', width: 150 },
                    { headerText: text("KAF018_45"), key: 'phase4', dataType: 'string', width: 150 },
                    { headerText: text("KAF018_46"), key: 'phase5', dataType: 'string', width: 150 }
                ],
                features: [
                    {
                        name: 'Selection',
                        mode: 'row',
                        multipleSelection: true
                    }
                ],
                ntsFeatures: [
                    {
                        name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: colorBackGr
                    }
                ],
                ntsControls: [
                ]
            });
        }

        fillColorbackGr(): Array<shared.CellState> {
            let self = this;
            let result = [];
            _.each(self.listDataDisp(), function(item) {
                let rowId = item.appId;
                //fill color in 承認状況
                if (item.reflectState == shareModel.NOTREFLECTED) {//0 下書き保存/未反映　=　未
                    result.push(new shared.CellState(rowId, 'reflectStateContent', ['unapprovalCell']));
                }
                if (item.reflectState == shareModel.WAITREFLECTION) {//1 反映待ち　＝　承認済み
                    result.push(new shared.CellState(rowId, 'reflectStateContent', ['approvalCell']));
                }
                if (item.reflectState == shareModel.REFLECTED) {//2 反映済　＝　反映済み
                    result.push(new shared.CellState(rowId, 'reflectStateContent', ['reflectCell']));
                }
                if (item.reflectState == shareModel.WAITCANCEL || item.reflectState == shareModel.CANCELED) {//3,4 取消待ち/取消済　＝　取消
                    result.push(new shared.CellState(rowId, 'reflectStateContent', ['cancelCell']));
                }
                if (item.reflectState == shareModel.REMAND) {//5 差し戻し　＝　差戻
                    result.push(new shared.CellState(rowId, 'reflectStateContent', ['remandCell']));
                }
                if (item.reflectState == shareModel.DENIAL) {//6 否認　=　否
                    result.push(new shared.CellState(rowId, 'reflectStateContent', ['denialCell']));
                }
            });
            return result;
        }
        //反映状況
        disReflectionStatus(status: number): string {
            switch (status) {
                case shareModel.NOTREFLECTED:
                    return text('KAF018_93');
                case shareModel.WAITREFLECTION:
                    return text('KAF018_94');
                case shareModel.REFLECTED:
                    return text('KAF018_95');
                case shareModel.DENIAL:
                    return text('KAF018_96');
                case shareModel.REMAND:
                    return text('KAF018_97');
                case shareModel.WAITCANCEL:
                    return text('KAF018_98');
                case shareModel.CANCELED:
                    return text('KAF018_98');
            }
        }

        //承認状況
        disApprovalStatus(listAppStatus: Array<number>): string {
            var disAppStt: string = "";
            _.each(listAppStatus, function(appStt) {
                if (appStt == 0) {
                    disAppStt += '<span class = "unapprovaled-remand">' + "－" + '</span>';
                } else if (appStt == 1) {
                    disAppStt += '<span class = "approvaled">' + "〇" + '</span>';
                } else if (appStt == 2) {
                    disAppStt += '<span class = "denied">' + "×" + '</span>';
                }
            });
            return disAppStt;
        }

        /**
        * 振休振出申請
        * kaf011 - appType = 10
        * TO DO
        */
        formatCompltLeave(app: Content, complt: AppCompltLeaveSync): ContentDisp {
            let self = this;
            //振出 rec typeApp = 1
            //振休 abs typeApp = 0
            let prePost: string = app.prePostAtr == 0 ? '事前' : '事後';
            let content010: string = '';
            //振出 rec typeApp = 1
            //振休 abs typeApp = 0
            if (complt.typeApp == 0) {
                content010 = self.convertB(complt.appMain, app.applicationDate);
            } else {
                content010 = self.convertA(complt.appMain, app.applicationDate);
            }
            let appDate = self.appDateColor(self.convertDateMDW(app.applicationDate), '', '');
            let reflectStateContent = self.disReflectionStatus(app.reflectState);
            let a: ContentDisp = new ContentDisp(app.applicationID, app.appName, prePost, appDate, content010, app.reflectState,
                reflectStateContent,self.disApprovalStatus(app.approvalStatus), app.phase1, app.phase2, app.phase3, app.phase4, app.phase5);
            return a;
        }
        /**
         * 振休振出申請
         * 同期
         * kaf011 - appType = 10
         * TO DO
         */
        formatCompltSync(app: Content, complt: AppCompltLeaveSync): ContentDisp {
            let self = this;
            let prePost = app.prePostAtr == 0 ? '事前' : '事後';
            let content010 = '';
            content010 = self.convertC(app, complt);

            let appDateAbs = '';
            let appDateRec = '';
            let inputDateAbs = '';
            let inputDateRec = '';
            //振出 rec typeApp = 1
            //振休 abs typeApp = 0
            if (complt.typeApp == 0) {
                appDateAbs = self.appDateColor(self.convertDateMDW(app.applicationDate), 'abs', '');
                appDateRec = self.appDateColor(self.convertDateMDW(complt.appDateSub), 'rec', '');
            } else {
                appDateRec = self.appDateColor(self.convertDateMDW(app.applicationDate), 'rec', '');
                appDateAbs = self.appDateColor(self.convertDateMDW(complt.appDateSub), 'abs', '');
            }
            let reflectStateContent = self.disReflectionStatus(app.reflectState);
            let replectStateFull: string = '<div class = "rec" >' + reflectStateContent + '</div>' + '<br/>' + '<div class = "abs" >' + reflectStateContent + '</div>';
            let appDateFull: string = appDateRec + '<br/>' + appDateAbs;
            let a: ContentDisp = new ContentDisp(app.applicationID, app.appName, prePost, appDateFull,
                content010, app.reflectState, replectStateFull,self.disApprovalStatus(app.approvalStatus), app.phase1, app.phase2, app.phase3, app.phase4, app.phase5);
            return a;
        }

        //※振出申請のみ同期なし・紐付けなし
        //申請/承認モード
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)の表示はない（１段）
        convertA(compltLeave: AppCompltLeaveFull, date: string) {
            let self = this;
            let time = compltLeave.startTime + text('KAF018_220') + compltLeave.endTime;
            return text('KAF018_262') + self.convertDateShort_MD(date) + text('KAF018_230', [compltLeave.workTypeName]) + time;
        }
        //※振休申請のみ同期なし・紐付けなし
        //申請/承認モード
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)の表示はない（１段）
        convertB(compltLeave: AppCompltLeaveFull, date: string) {
            let self = this;
            let eTime = compltLeave.endTime == '' ? '' : text('KAF018_220') + compltLeave.endTime;
            let time = compltLeave.startTime + eTime;
            return text('KAF018_263') + self.convertDateShort_MD(date) + text('KAF018_230', [compltLeave.workTypeName]) + time;
        }
        //※振休振出申請　同期（あり/なし）・紐付けあり
        //申請モード/承認モード merge convert C + D
        //申請日付(A6_C2_6)、入力日(A6_C2_8)、承認状況(A6_C2_9)表示（２段）
        //振出(rec) -> 振休(abs)
        convertC(app: Content, compltSync: AppCompltLeaveSync) {
            let self = this;
            let abs = null;
            let rec = null;
            let recContent = '';
            let absContent = '';
            if (compltSync.typeApp == 0) {
                abs = compltSync.appMain;
                rec = compltSync.appSub;
                recContent = self.convertA(rec, compltSync.appDateSub);
                absContent = self.convertB(abs, app.applicationDate);

            } else {
                rec = compltSync.appMain;
                abs = compltSync.appSub;
                absContent = self.convertB(abs, compltSync.appDateSub);
                recContent = self.convertA(rec, app.applicationDate);
            }
            return '<div class = "rec" >' + recContent + '</div>' + '<div class = "abs" >' + absContent + '</div>';
        }

        //Short_MD
        convertDateShort_MD(date: string) {
            let a: number = moment(date, 'YYYY/MM/DD').isoWeekday();
            let toDate = moment(date, 'YYYY/MM/DD').toDate();
            let dateMDW = (toDate.getMonth() + 1) + '/' + toDate.getDate();
            return dateMDW;
        }
   
        inputDateColor(input: string, classApp: string): string {
            let inputDate = '<div class = "' + classApp + '" >' + input + '</div>';
            //fill color text input date
            let colorIn = input.substring(11, 12);
            if (colorIn == '土') {//土
                inputDate = '<div class = "saturdayCell ' + classApp + '" >' + input + '</div>';
            }
            if (colorIn == '日') {//日
                inputDate = '<div class = "sundayCell ' + classApp + '" >' + input + '</div>';
            }
            return inputDate;
        }
        appDateColor(date: string, classApp: string, priod: string): string {
            let appDate = '<div class = "' + classApp + '" >' + date + priod + '</div>';;
            //color text appDate
            let a = date.split("(")[1];
            let color = a.substring(0, 1);
            if (color == '土') {//土
                appDate = '<div class = "saturdayCell  ' + classApp + '" >' + date + priod + '</div>';
            }
            if (color == '日') {//日 
                appDate = '<div class = "sundayCell  ' + classApp + '" >' + date + priod + '</div>';
            }
            return appDate;
        }

        closeUp() {
            //画面を閉じる
            nts.uk.ui.windows.close();
        }
        //MM/dd ver24
        convertDateMDW(date: string) {
            if (date == "") return "";
            let a: number = moment(date, 'YYYY/MM/DD').isoWeekday();
            let toDate = moment(date, 'YYYY/MM/DD').toDate();
            let dateMDW = (toDate.getMonth() + 1) + '/' + toDate.getDate();
            switch (a) {
                case 1://Mon
                    return dateMDW + '(月)';
                case 2://Tue
                    return dateMDW + '(火)';
                case 3://Wed
                    return dateMDW + '(水)';
                case 4://Thu
                    return dateMDW + '(木)';
                case 5://Fri
                    return dateMDW + '(金)';
                case 6://Sat
                    return dateMDW + '(土)';
                default://Sun
                    return dateMDW + '(日)';
            }
        }

        appDateRangeColor(startDate: string, endDate: string): string {
            let sDate = '<div class = "dateRange" >' + startDate + '</div>';
            //color text appDate
            let a = startDate.split("(")[1];
            let color1 = a.substring(0, 1);
            if (color1 == '土') {//土
                sDate = '<div class = "saturdayCell  dateRange" >' + startDate + '</div>';
            }
            if (color1 == '日') {//日 
                sDate = '<div class = "sundayCell  dateRange" >' + startDate + '</div>';
            }

            if (endDate != '') {
                let eDate = '<div class = "dateRange" >' + endDate + '</div>';
                let b = endDate.split("(")[1];
                let color2 = b.substring(0, 1);
                if (color2 == '土') {//土
                    eDate = '<div class = "saturdayCell  dateRange" >' + endDate + '</div>';
                }
                if (color2 == '日') {//日 
                    eDate = '<div class = "sundayCell  dateRange" >' + endDate + '</div>';
                }
                return sDate + '<div class = "dateRange" >' + '－' + '</div>' + eDate;
            }
            return sDate;
        }
    }

    class ApprovalStatusEmployee {
        sid: string;
        startDate: Date;
        endDate: Date;
        constructor(sid: string, startDate: Date, endDate: Date) {
            this.sid = sid;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    class NtsGridListColumn {
        headerText: string;
        key: string;
        width: number;
    }
    class Content {
        // 申請ID
        applicationID: string;
        appType: shared.ApplicationType;
        applicationDate: string;
        appName: string;
        prePostAtr: number;
        appStartDate: string;
        appEndDate: string;
        appContent: string;
        reflectState: number;
        approvalStatus: Array<number>;
        phase1: string;
        phase2: string;
        phase3: string;
        phase4: string;
        phase5: string;
        constructor(applicationID: string, applicationDate: string, appType: number, appName: string, prePostAtr: number, appStartDate: string, appEndDate: string, appContent: string, reflectState: number,
            approvalStatus: Array<number>, phase1: string, phase2: string, phase3: string, phase4: string, phase5: string) {
            this.applicationID = applicationID;
            this.appType = appType;
            this.appName = appName;
            this.applicationDate = applicationDate;
            this.prePostAtr = prePostAtr;
            this.appStartDate = appStartDate;
            this.appEndDate = appEndDate;
            this.appContent = appContent;
            this.reflectState = reflectState;
            this.approvalStatus = approvalStatus;
            this.phase1 = phase1;
            this.phase2 = phase2;
            this.phase3 = phase3;
            this.phase4 = phase4;
            this.phase5 = phase5;
        }
    }

    class ContentDisp {
        appId: string;
        appName: string;
        prePostAtr: string;
        appDate: string;
        appContent: string;
        reflectState: number;
        reflectStateContent: string;
        approvalStatus: string;
        phase1: string;
        phase2: string;
        phase3: string;
        phase4: string;
        phase5: string;
        constructor(appId: string, appName: string, prePostAtr: string, appDate: string, appContent: string, reflectState: number, reflectStateContent: string,
            approvalStatus: string, phase1: string, phase2: string, phase3: string, phase4: string, phase5: string) {
            this.appId = appId;
            this.appName = appName;
            this.prePostAtr = prePostAtr;
            this.appDate = appDate;
            this.appContent = appContent;
            this.reflectState = reflectState;
            this.reflectStateContent = reflectStateContent;
            this.approvalStatus = approvalStatus;
            this.phase1 = phase1;
            this.phase2 = phase2;
            this.phase3 = phase3;
            this.phase4 = phase4;
            this.phase5 = phase5;
        }
    }

    export class AppCompltLeaveFull {
        /**申請ID*/
        appID: string;
        /**勤務種類Name*/
        workTypeName: string;
        /**勤務時間1.開始時刻*/
        startTime: string;
        /**勤務時間1.終了時刻*/
        endTime: string;
        constructor(appID: string, workTypeName: string, startTime: string, endTime: string) {
            this.appID = appID;
            this.workTypeName = workTypeName;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    export class AppCompltLeaveSync {
        //0 - abs
        //1 - rec
        typeApp: number;
        sync: boolean;
        appMain: AppCompltLeaveFull;
        appSub: AppCompltLeaveFull;
        appDateSub: string;
        appInputSub: string;
        constructor(typeApp: number, sync: boolean, appMain: AppCompltLeaveFull,
            appSub: AppCompltLeaveFull, appDateSub: string, appInputSub: string) {
            this.typeApp = typeApp;
            this.sync = sync;
            this.appMain = appMain;
            this.appSub = appSub;
            this.appDateSub = appDateSub;
            this.appInputSub = appInputSub;
        }
    }

    export class ApplicationList {
        listAppDetail: Array<Content>;
        lstAppCompltLeaveSync: Array<AppCompltLeaveSync>;
    }
}