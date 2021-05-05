__viewContext.ready(function() {
    nts.uk.request.login.keepUsedLoginPage("com", "/view/ccg/007/d/index.xhtml");
	var paramSPR = JSON.parse(window.sessionStorage.getItem("paramSPR"));
	window.sessionStorage.removeItem("paramSPR");
    var menuCD = parseInt(paramSPR.menu);
    var loginemployeeCD = paramSPR.loginemployeeCode;
    var employeeCode = paramSPR.employeeCode;
    var starttime = parseInt(paramSPR.starttime);
    var endtime = parseInt(paramSPR.endtime);
    var date = paramSPR.date;
    var selecttype = parseInt(paramSPR.selecttype);
    var applicationID = paramSPR.applicationID;
    var reason = paramSPR.reason;
    var stampProtection = parseInt(paramSPR.stampProtection);
    var userID = paramSPR.userID;
    var contractCD = paramSPR.contractCD;
    var companyID = paramSPR.companyID;
    var companyCD = paramSPR.companyCD;
    var personID = paramSPR.personID;
    var loginEmployeeID = paramSPR.loginEmployeeID;
    var roleID = paramSPR.roleID;
    var employeeID = nts.uk.util.isNullOrEmpty(paramSPR.employeeID) ? null : paramSPR.employeeID;
	switch(menuCD) {
	case 1:
        nts.uk.request.jump("at", "/view/kaf/005/a/index.xhtml?a=0", 
            {
                uiType: 2,
                appDate: date,
                startTime: starttime,
                endTime: endtime,
                applicationReason: reason,
                employeeID: nts.uk.util.isNullOrEmpty(employeeID) ? null : employeeID
            }
        );
		break;
	case 2:
        nts.uk.request.jump("at", "/view/kaf/005/a/index.xhtml?a=1", 
            {
                uiType: 2,
                appDate: date,
                startTime: starttime,
                endTime: endtime,
                applicationReason: reason,
                employeeID: nts.uk.util.isNullOrEmpty(employeeID) ? null : employeeID
            }
        );
		break;
	case 3:
		var initParam = {
			//画面モード
			screenMode: 0,
			//社員一覧
			lstEmployee: nts.uk.util.isNullOrEmpty(employeeID) ? [] : [employeeID],
			//エラー参照を起動する
			errorRefStartAtr: true,
			//期間を変更する
			changePeriodAtr: true,
			//処理締め
			targetClosue: null,
			//Optional
			//打刻初期値
			initClock: {
				dateSpr: date,
				canEdit: stampProtection == 0 ? false : true,
				employeeId: nts.uk.util.isNullOrEmpty(employeeID) ? null : employeeID, 
				liveTime: endtime,	//退勤打刻
				goOut: starttime,  //出勤打刻
			},
			//遷移先の画面
			transitionDesScreen: null,
		}

		var extractionParam = {
			//表示形式
			displayFormat: 0, // DPCorrectionDisplayFormat.INDIVIDUAl(個人別)
			//期間
			startDate: date,
			endDate: date,
			//抽出した社員一覧
			lstExtractedEmployee: nts.uk.util.isNullOrEmpty(employeeID) ? [] : [employeeID],
			//Optional
			//日付別で起動
			dateTarget: date,
			individualTarget: nts.uk.util.isNullOrEmpty(employeeID) ? null : employeeID,
		}
		nts.uk.request.jump("at", "/view/kdw/003/a/index.xhtml", {initParam: initParam, extractionParam: extractionParam});
		break;
	case 4:
        nts.uk.localStorage.setItem('UKProgramParam', 'a=1');
        nts.uk.request.jump("at", "/view/cmm/045/a/index.xhtml", 
            { 'PARAM_SPR_CMM045':
                {
                    mode: 1 ,//1=承認一覧
                    startDate: date,//yyyy/mm/dd //期間（開始日）
                    endDate: date,//yyyy/mm/dd //期間（終了日）
                    extractCondition: selecttype,//０＝全て、１＝早出・普通残業のみ
                    agreementTime36: 0,//０＝表示しない、1＝表示する
                }
            }
        );
		break;
	case 5:
        var initParam = { 
            //画面モード
            screenMode: 1,
            //社員一覧
            lstEmployee: nts.uk.util.isNullOrEmpty(employeeID) ? [] : [employeeID],
            //エラー参照を起動する
            errorRefStartAtr: true,
            //期間を変更する
            changePeriodAtr: true,
            //処理締め
            targetClosue: null,
            //Optional
            //打刻初期値
            initClock: {
                dateSpr: date,
                canEdit: stampProtection == 0 ? false : true,
                employeeId: nts.uk.util.isNullOrEmpty(employeeID) ? null : employeeID, 
                liveTime: endtime,  //退勤打刻
                goOut: starttime,  //出勤打刻
            },
            //遷移先の画面
            transitionDesScreen: null,
        }

        var extractionParam = {
            //表示形式
            displayFormat: 1, // DPCorrectionDisplayFormat.DATE(日付別)
            //期間
            startDate: date,
            endDate: date,
            //抽出した社員一覧
            lstExtractedEmployee: nts.uk.util.isNullOrEmpty(employeeID) ? [] : [employeeID],
            //Optional
            //日付別で起動
            dateTarget: date,
            individualTarget: nts.uk.util.isNullOrEmpty(employeeID) ? null : employeeID,
        }
        nts.uk.request.jump("at", "/view/kdw/003/a/index.xhtml", {initParam: initParam, extractionParam: extractionParam});
		break;
	case 6:
//        let paramSave = {
//            startDate: '',
//            /**期間終了日付*/
//            endDate: '',
//            /**申請一覧区分*/
//            appListAtr: 1,
//            /**申請種類*/
//            appType: -1,
//            /**承認状況＿未承認*/
//            unapprovalStatus: true,
//            /**承認状況＿承認済*/
//            approvalStatus: true,
//            /**承認状況＿否認*/
//            denialStatus: true,
//            /**承認状況＿代行承認済*/
//            agentApprovalStatus: true,
//            /**承認状況＿差戻*/
//            remandStatus: true,
//            /**承認状況＿取消*/
//            cancelStatus: true,
//            /**申請表示対象*/
//            appDisplayAtr: 0,
//            /**社員IDリスト*/
//            listEmployeeId: [],
//            /**社員絞込条件*/
//            empRefineCondition: ""        
//        }
//        nts.uk.characteristics.save('AppListExtractCondition', paramSave);
        nts.uk.request.jump("at", "/view/kaf/000/b/index.xhtml", { 'listAppMeta': [applicationID], 'currentApp': applicationID });
		break;
	case 7:
        nts.uk.request.jump("at", "/view/kaf/005/a/index.xhtml?a=2", 
            {
                uiType: 2,
                appDate: date,
                startTime: starttime,
                endTime: endtime,
                applicationReason: reason,
                employeeID: nts.uk.util.isNullOrEmpty(employeeID) ? null : employeeID
            }
        );
		break;
	default:
        nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
	}
})