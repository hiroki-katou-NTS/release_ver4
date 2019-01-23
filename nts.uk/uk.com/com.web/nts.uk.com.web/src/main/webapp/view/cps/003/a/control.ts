module cps003 {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;
    import ajax = nts.uk.request.ajax;
    import writeConstraint = nts.uk.ui.validation.writeConstraint;
    import parseTime = nts.uk.time.parseTime;
    import parseTimeWithDay = nts.uk.time.minutesBased.clock.dayattr.create;
    let ITEM_SINGLE_TYPE = (cps003.a || cps003.c).vm.ITEM_SINGLE_TYPE;
    let ITEM_STRING_TYPE = (cps003.a || cps003.c).vm.ITEM_STRING_TYPE;
    let ITEM_SELECT_TYPE = (cps003.a || cps003.c).vm.ITEM_SELECT_TYPE;
    let ITEM_STRING_DTYPE = (cps003.a || cps003.c).vm.ITEM_STRING_DTYPE;
    
    export module control {
        const selectGroups: Array<IGroupControl> = [
            {  
                ctgCode: 'CS00017',
                workplace: 'IS00084',
                startDate: 'IS00082'
            },
            { 
                ctgCode: 'CS00017',
                workplace: 'IS00085',
                startDate: 'IS00082'
            },
            {
                ctgCode: 'CS00019',
                firstTimes: {
                    start: 'IS00106',
                    end: 'IS00107'
                },
                secondTimes: {
                    start: 'IS00109',
                    end: 'IS00110'
                }
            },
            {
                ctgCode: 'CS00020',
                workType: 'IS00128'
            },
            {
                ctgCode: 'CS00020',
                workType: 'IS00130',
                workTime: 'IS00131',
                firstTimes: {
                    start: 'IS00133',
                    end: 'IS00134'
                },
                secondTimes: {
                    start: 'IS00136',
                    end: 'IS00137'
                }
            },
            {
                ctgCode: 'CS00020',
                workType: 'IS00139',
                workTime: 'IS00140',
                firstTimes: {
                    start: 'IS00142',
                    end: 'IS00143'
                },
                secondTimes: {
                    start: 'IS00145',
                    end: 'IS00146'
                }
            },
            {
                ctgCode: 'CS00020',
                workType: 'IS00148',
                workTime: 'IS00149',
                firstTimes: {
                    start: 'IS00151',
                    end: 'IS00152'
                },
                secondTimes: {
                    start: 'IS00154',
                    end: 'IS00155'
                }
            },
            {
                ctgCode: 'CS00020',
                workType: 'IS00157',
                workTime: 'IS00158',
                firstTimes: {
                    start: 'IS00160',
                    end: 'IS00161'
                },
                secondTimes: {
                    start: 'IS00163',
                    end: 'IS00164'
                }
            },
            {
                ctgCode: 'CS00020',
                workType: 'IS00166',
                workTime: 'IS00167',
                firstTimes: {
                    start: 'IS00169',
                    end: 'IS00170'
                },
                secondTimes: {
                    start: 'IS00172',
                    end: 'IS00173'
                }
            },
            {
                ctgCode: 'CS00020',
                workType: 'IS00175',
                workTime: 'IS00176',
                firstTimes: {
                    start: 'IS00178',
                    end: 'IS00179'
                },
                secondTimes: {
                    start: 'IS00181',
                    end: 'IS00182'
                }
            },
            {
                ctgCode: 'CS00070',
                workType: 'IS00193',
                workTime: 'IS00194',
                firstTimes: {
                    start: 'IS00196',
                    end: 'IS00197'
                },
                secondTimes: {
                    start: 'IS00199',
                    end: 'IS00200'
                }
            },
            {
                ctgCode: 'CS00070',
                workType: 'IS00202',
                workTime: 'IS00203',
                firstTimes: {
                    start: 'IS00205',
                    end: 'IS00206'
                },
                secondTimes: {
                    start: 'IS00208',
                    end: 'IS00209'
                }
            },
            {
                ctgCode: 'CS00070',
                workType: 'IS00211',
                workTime: 'IS00212',
                firstTimes: {
                    start: 'IS00214',
                    end: 'IS00215'
                },
                secondTimes: {
                    start: 'IS00217',
                    end: 'IS00218'
                }
            },
            {
                ctgCode: 'CS00070',
                workType: 'IS00220',
                workTime: 'IS00221',
                firstTimes: {
                    start: 'IS00223',
                    end: 'IS00224'
                },
                secondTimes: {
                    start: 'IS00226',
                    end: 'IS00227'
                }
            },
            {
                ctgCode: 'CS00070',
                workType: 'IS00229',
                workTime: 'IS00230',
                firstTimes: {
                    start: 'IS00232',
                    end: 'IS00233'
                },
                secondTimes: {
                    start: 'IS00235',
                    end: 'IS00236'
                }
            },
            {
                ctgCode: 'CS00070',
                workType: 'IS00238',
                workTime: 'IS00239',
                firstTimes: {
                    start: 'IS00241',
                    end: 'IS00242'
                },
                secondTimes: {
                    start: 'IS00244',
                    end: 'IS00245'
                }
            },
            {
                ctgCode: 'CS00070',
                workType: 'IS00184',
                workTime: 'IS00185',
                firstTimes: {
                    start: 'IS00187',
                    end: 'IS00188'
                },
                secondTimes: {
                    start: 'IS00190',
                    end: 'IS00191'
                }
            }
        ];
        
        const relateButtonGroups : Array<IRelateButton> = [{
                ctgCode: 'CS00024',
                btnCode: 'IS00276',
                lblCode: 'IS00277',
                dialogId: 'g'
            }, {
                    ctgCode: 'CS00024',
                    btnCode: 'IS00294',
                    dialogId: 'h'
                }, {
                    ctgCode: 'CS00025',
                    btnCode: 'IS00301',
                    dialogId: 'i',
                    specialCd: 1
                }, {
                    ctgCode: 'CS00026',
                    btnCode: 'IS00308',
                    dialogId: 'i',
                    specialCd: 2
                }, {
                    ctgCode: 'CS00027',
                    btnCode: 'IS00315',
                    dialogId: 'i',
                    specialCd: 3
                }, {
                    ctgCode: 'CS00028',
                    btnCode: 'IS00322',
                    dialogId: 'i',
                    specialCd: 4
                }, {
                    ctgCode: 'CS00029',
                    btnCode: 'IS00329',
                    dialogId: 'i',
                    specialCd: 5
                }, {
                    ctgCode: 'CS00030',
                    btnCode: 'IS00336',
                    dialogId: 'i',
                    specialCd: 6
                }, {
                    ctgCode: 'CS00031',
                    btnCode: 'IS00343',
                    dialogId: 'i',
                    specialCd: 7
                }, {
                    ctgCode: 'CS00032',
                    btnCode: 'IS00350',
                    dialogId: 'i',
                    specialCd: 8
                }, {
                    ctgCode: 'CS00033',
                    btnCode: 'IS00357',
                    dialogId: 'i',
                    specialCd: 9
                }, {
                    ctgCode: 'CS00034',
                    btnCode: 'IS00364',
                    dialogId: 'i',
                    specialCd: 10
                }, {
                    ctgCode: 'CS00049',
                    btnCode: 'IS00565',
                    dialogId: 'i',
                    specialCd: 11
                }, {
                    ctgCode: 'CS00050',
                    btnCode: 'IS00572',
                    dialogId: 'i',
                    specialCd: 12
                }, {
                    ctgCode: 'CS00051',
                    btnCode: 'IS00579',
                    dialogId: 'i',
                    specialCd: 13
                }, {
                    ctgCode: 'CS00052',
                    btnCode: 'IS00586',
                    dialogId: 'i',
                    specialCd: 14
                }, {
                    ctgCode: 'CS00053',
                    btnCode: 'IS00593',
                    dialogId: 'i',
                    specialCd: 15
                }, {
                    ctgCode: 'CS00054',
                    btnCode: 'IS00600',
                    dialogId: 'i',
                    specialCd: 16
                }, {
                    ctgCode: 'CS00055',
                    btnCode: 'IS00607',
                    dialogId: 'i',
                    specialCd: 17
                }, {
                    ctgCode: 'CS00056',
                    btnCode: 'IS00614',
                    dialogId: 'i',
                    specialCd: 18
                }, {
                    ctgCode: 'CS00057',
                    btnCode: 'IS00621',
                    dialogId: 'i',
                    specialCd: 19
                }, {
                    ctgCode: 'CS00058',
                    btnCode: 'IS00628',
                    dialogId: 'i',
                    specialCd: 20
                }];
        
        export const fetch = {
            get_stc_setting: () => ajax('at', `record/stamp/stampcardedit/find`),
            get_annLeaNumber: (sid: string) => ajax('at', `at/record/remainnumber/annlea/getAnnLeaNumber/${sid}`),
            get_resvLeaNumber: (sid: string) => ajax('com', `ctx/pereg/layout/getResvLeaNumber/${sid}`),
            get_calDayTime: (sid: string, specialCd: number) => ajax('com', `ctx/pereg/layout/calDayTime/${sid}/${specialCd}`),
            checkFunctionNo: () => ajax(`ctx/pereg/functions/auth/find-with-role-person-info`),
            check_mt_se: (param: any) => ajax(`ctx/pereg/person/common/checkStartEndMultiTime`, param),
            check_start_end: (param: ICheckParam) => ajax(`ctx/pereg/person/common/checkStartEnd`, param),
            check_multi_time: (param: ICheckParam) => ajax(`ctx/pereg/person/common/checkMultiTime`, param),
            get_cb_data: (param: IComboParam) => ajax(`ctx/pereg/person/common/getFlexComboBox`, param),
            get_ro_data: (param: INextTimeParam) => ajax('com', `at/record/remainnumber/annlea/event/nextTime`, param),
            get_sphd_nextGrantDate: (param: ISpecialParam) => ajax('com', `ctx/pereg/layout/getSPHolidayGrantDate`, param),
            check_remain_days: (sid: string) => ajax('com', `ctx/pereg/person/common/checkEnableRemainDays/${sid}`),
            check_remain_left: (sid: string) => ajax('com', `ctx/pereg/person/common/checkEnableRemainLeft/${sid}`)
        };
        
        export let SELECT_BUTTON = {}, RELATE_BUTTON = {}, WORK_TIME = {}, 
        HALF_INT = { 
            CS00035_IS00366: true,
            CS00035_IS00368: true,
            CS00035_IS00369: true,
            CS00036_IS00377: true,
            CS00036_IS00378: true,
            CS00036_IS00379: true,
            CS00036_IS00382: true,
            CS00036_IS00383: true,
            CS00036_IS00384: true
        },
        COMBOBOX = {
            CS00020_IS00123: (v, id) => { 
                let $grid = $("#grid");   
                switch (v) {
                    case "0":
                        $grid.mGrid("enableNtsControlAt", id, "IS00124");
                        $grid.mGrid("enableNtsControlAt", id, "IS00125");
                        $grid.mGrid("enableNtsControlAt", id, "IS00126");
                        $grid.mGrid("disableNtsControlAt", id, "IS00127");
                        break;
                    case "1":
                        $grid.mGrid("disableNtsControlAt", id, "IS00124");
                        $grid.mGrid("disableNtsControlAt", id, "IS00125");
                        $grid.mGrid("enableNtsControlAt", id, "IS00126");
                        $grid.mGrid("enableNtsControlAt", id, "IS00127");
                        break;
                    case "2":
                        $grid.mGrid("disableNtsControlAt", id, "IS00124");
                        $grid.mGrid("disableNtsControlAt", id, "IS00125");
                        $grid.mGrid("disableNtsControlAt", id, "IS00126");
                        $grid.mGrid("disableNtsControlAt", id, "IS00127");
                        break;
                }
            },
            CS00020_IS00248: (v, id) => {
                let $grid = $("#grid");
                $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, "IS00249");
                $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, "IS00250");
                $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, "IS00251");
            },
            CS00020_IS00121: (v, id, o) => {
                let $grid = $("#grid");
                $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, "IS00123");
                $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, "IS00124");
                $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, "IS00125");
                $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, "IS00126");
                if (v === "0" || o.IS00123 === "0" || o.IS00123 === "2") {
                    $grid.mGrid("disableNtsControlAt", id, "IS00127");
                } else {
                    $grid.mGrid("enableNtsControlAt", id, "IS00127");
                }
            },
            CS00025_IS00296: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00297', 'IS00298', 'IS00299', 'IS00300', 'IS00301'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00026_IS00303: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00304', 'IS00305', 'IS00306', 'IS00307', 'IS00308'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00027_IS00310: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00311', 'IS00312', 'IS00313', 'IS00314', 'IS00315'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00028_IS00317: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00318', 'IS00319', 'IS00320', 'IS00321', 'IS00322'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00029_IS00324: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00325', 'IS00326', 'IS00327', 'IS00328', 'IS00329'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00030_IS00331: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00332', 'IS00333', 'IS00334', 'IS00335', 'IS00336'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00031_IS00338: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00339', 'IS00340', 'IS00341', 'IS00342', 'IS00343'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00032_IS00345: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00346', 'IS00347', 'IS00348', 'IS00349', 'IS00350'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00033_IS00352: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00353', 'IS00354', 'IS00355', 'IS00356', 'IS00357'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00034_IS00359: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00360', 'IS00361', 'IS00362', 'IS00363', 'IS00364'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00035_IS00370: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00371', 'IS00372', 'IS00374'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00036_IS00375: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00376', 'IS00377', 'IS00378', 'IS00379'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00036_IS00380: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00381', 'IS00382', 'IS00383', 'IS00384'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00049_IS00560: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00561', 'IS00562', 'IS00563', 'IS00564', 'IS00565'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00050_IS00567: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00568', 'IS00569', 'IS00570', 'IS00571', 'IS00572'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00051_IS00574: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00575', 'IS00576', 'IS00577', 'IS00578', 'IS00579'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00052_IS00581: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00582', 'IS00583', 'IS00584', 'IS00585', 'IS00586'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00053_IS00588: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00589', 'IS00590', 'IS00591', 'IS00592', 'IS00593'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00054_IS00595: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00596', 'IS00597', 'IS00598', 'IS00599', 'IS00600'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00055_IS00602: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00603', 'IS00604', 'IS00605', 'IS00606', 'IS00607'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00056_IS00609: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00610', 'IS00611', 'IS00612', 'IS00613', 'IS00614'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00057_IS00616: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00617', 'IS00618', 'IS00619', 'IS00620', 'IS00621'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            },
            CS00058_IS00623: (v, id) => {
                let $grid = $("#grid");
                _.forEach(['IS00624', 'IS00625', 'IS00626', 'IS00627', 'IS00628'], code => {
                    $grid.mGrid(v === "1" ? "enableNtsControlAt" : "disableNtsControlAt", id, code);
                });
            }
        },
        DATE_TIME = {
            CS00016_IS00077: (v, o) => {
                dateSubscribeCombo("IS00079", v, o);
            },
            CS00017_IS00082: (v, o) => {
                // Replace new item list but not passed when opening dialog
                dateSubscribeCombo("IS00084", v, o);
                dateSubscribeCombo("IS00085", v, o);
            },
            CS00020_IS00119: (v, o) => {
                if (_.isNil(v) || v === "" || _.isNil(o.employeeId)) return;
                _.forEach([
                    { workType: "IS00130", workTime: "IS00131" },
                    { workType: "IS00139", workTime: "IS00140" },
                    { workType: "IS00157", workTime: "IS00158" },
                    { workType: "IS00166", workTime: "IS00167" },
                    { workType: "IS00175", workTime: "IS00176" },
                    { workType: "IS00148", workTime: "IS00149" }
                ], cate => {
                    let types = ((__viewContext || {}).viewModel || {}).dataTypes || {}, comboData = types[cate.workType],
                        catId = (((__viewContext || {}).viewModel || {}).category || {}).catId;
                    if (comboData) {
                        fetch.get_cb_data({
                            comboBoxType: comboData.cls.referenceType,
                            categoryId: catId(),
                            required: comboData.cls.required,
                            standardDate: moment.utc(v, "YYYY/MM/DD").toDate(),
                            typeCode: undefined,
                            masterType: comboData.cls.masterType,
                            employeeId: o.employeeId,
                            cps002: false,
                            workplaceId: undefined,
                            baseDate: undefined
                        }).done(data => {
                            $("#grid").mGrid("updateCell", o.id, cate.workType, data, null, null, true);
                        });
                    }
                    
                    comboData = types[cate.workTime];
                    if (comboData) {
                        fetch.get_cb_data({
                            comboBoxType: comboData.cls.referenceType,
                            categoryId: catId(),
                            required: comboData.cls.required,
                            standardDate: moment.utc(v, "YYYY/MM/DD").toDate(),
                            typeCode: undefined,
                            masterType: comboData.cls.masterType,
                            employeeId: o.employeeId,
                            cps002: false,
                            workplaceId: undefined,
                            baseDate: undefined
                        }).done(data => {
                            $("#grid").mGrid("updateCell", o.id, cate.workTime, data, null, null, true);
                        });
                    }
                });
            },
            CS00070_IS00781: (v, o) => {
                if (_.isNil(v) || v === "" || _.isNil(o.employeeId)) return;
                _.forEach([
                    { workType: "IS00193", workTime: "IS00194" },
                    { workType: "IS00202", workTime: "IS00203" },
                    { workType: "IS00211", workTime: "IS00212" },
                    { workType: "IS00220", workTime: "IS00221" },
                    { workType: "IS00229", workTime: "IS00230" },
                    { workType: "IS00238", workTime: "IS00239" },
                    { workType: "IS00184", workTime: "IS00185" }
                ], cate => {
                    let types = ((__viewContext || {}).viewModel || {}).dataTypes || {}, comboData = types[cate.workType],
                        catId = (((__viewContext || {}).viewModel || {}).category || {}).catId;
                    if (comboData) {
                        fetch.get_cb_data({
                            comboBoxType: comboData.cls.referenceType,
                            categoryId: catId(),
                            required: comboData.cls.required,
                            standardDate: moment.utc(v, "YYYY/MM/DD").toDate(),
                            typeCode: undefined,
                            masterType: comboData.cls.masterType,
                            employeeId: o.employeeId,
                            cps002: false,
                            workplaceId: undefined,
                            baseDate: undefined
                        }).done(data => {
                            $("#grid").mGrid("updateCell", o.id, cate.workType, data, null, null, true);
                        });
                    }
                    
                    comboData = types[cate.workTime];
                    if (comboData) {
                        fetch.get_cb_data({
                            comboBoxType: comboData.cls.referenceType,
                            categoryId: catId(),
                            required: comboData.cls.required,
                            standardDate: moment.utc(v, "YYYY/MM/DD").toDate(),
                            typeCode: undefined,
                            masterType: comboData.cls.masterType,
                            employeeId: o.employeeId,
                            cps002: false,
                            workplaceId: undefined,
                            baseDate: undefined
                        }).done(data => {
                            $("#grid").mGrid("updateCell", o.id, cate.workTime, data, null, null, true);
                        });
                    }
                });
            },
            CS00024_IS00279: (v, o) => {
                if (!o) return;
                let empId = o.employeeId, standardDate = o.IS00279, grantTable = o.IS00280, hireDate, startWork, endWork, conTime, $grid = $("#grid");
                if (!v || !grantTable) {
                    $grid.mGrid("updateCell", o.id, "IS00281", "");
                    $grid.mGrid("updateCell", o.id, "IS00282", "");
                    $grid.mGrid("updateCell", o.id, "IS00283", "");
                    return;
                }
                
                fetch.get_ro_data({
                    employeeId: empId,
                    standardDate: moment(standardDate).format('YYYY/MM/DD'),
                    grantTable: grantTable,
                    entryDate: hireDate,
                    startWorkCond: startWork,
                    endWorkCond: endWork,
                    contactTime: conTime
                }).done(result => {
                    $grid.mGrid("updateCell", o.id, "IS00281", result.nextTimeGrantDate);
                    $grid.mGrid("updateCell", o.id, "IS00282", result.nextTimeGrantDays);
                    $grid.mGrid("updateCell", o.id, "IS00283", result.nextTimeMaxTime);
                });
            },
            CS00025_IS00295: (v, o) => {
                specialOff(v, o, "IS00297", "IS00298", "IS00296", "IS00299", "IS00300", 1);
            },
            CS00026_IS00302: (v, o) => {
                specialOff(v, o, "IS00304", "IS00305", "IS00303", "IS00306", "IS00307", 2);
            },
            CS00027_IS00309: (v, o) => {
                specialOff(v, o, "IS00311", "IS00312", "IS00310", "IS00313", "IS00314", 3);
            },
            CS00028_IS00316: (v, o) => {
                specialOff(v, o, "IS00318", "IS00319", "IS00317", "IS00320", "IS00321", 4);
            },
            CS00029_IS00323: (v, o) => {
                specialOff(v, o, "IS00325", "IS00326", "IS00324", "IS00327", "IS00328", 5);
            },
            CS00030_IS00330: (v, o) => {
                specialOff(v, o, "IS00332", "IS00333", "IS00331", "IS00334", "IS00335", 6);
            },
            CS00031_IS00337: (v, o) => {
                specialOff(v, o, "IS00339", "IS00340", "IS00338", "IS00341", "IS00342", 7);
            },
            CS00032_IS00344: (v, o) => {
                specialOff(v, o, "IS00346", "IS00347", "IS00345", "IS00348", "IS00349", 8);
            },
            CS00033_IS00351: (v, o) => {
                specialOff(v, o, "IS00353", "IS00354", "IS00352", "IS00355", "IS00356", 9);
            },
            CS00034_IS00358: (v, o) => {
                specialOff(v, o, "IS00360", "IS00361", "IS00359", "IS00362", "IS00363", 10);
            },
            CS00049_IS00559: (v, o) => {
                specialOff(v, o, "IS00561", "IS00562", "IS00560", "IS00563", "IS00564", 11);
            },
            CS00050_IS00566: (v, o) => {
                specialOff(v, o, "IS00568", "IS00569", "IS00567", "IS00570", "IS00571", 12);
            },
            CS00051_IS00573: (v, o) => {
                specialOff(v, o, "IS00575", "IS00576", "IS00574", "IS00577", "IS00578", 13);
            },
            CS00052_IS00580: (v, o) => {
                specialOff(v, o, "IS00582", "IS00583", "IS00581", "IS00584", "IS00585", 14);
            },
            CS00053_IS00587: (v, o) => {
                specialOff(v, o, "IS00589", "IS00590", "IS00588", "IS00591", "IS00592", 15);
            },
            CS00054_IS00594: (v, o) => {
                specialOff(v, o, "IS00596", "IS00597", "IS00595", "IS00598", "IS00599", 16);
            },
            CS00055_IS00601: (v, o) => {
                specialOff(v, o, "IS00603", "IS00604", "IS00602", "IS00605", "IS00606", 17);
            },
            CS00056_IS00608: (v, o) => {
                specialOff(v, o, "IS00610", "IS00611", "IS00609", "IS00612", "IS00613", 18);
            },
            CS00057_IS00615: (v, o) => {
                specialOff(v, o, "IS00617", "IS00618", "IS00616", "IS00619", "IS00620", 19);
            },
            CS00058_IS00622: (v, o) => {
                specialOff(v, o, "IS00624", "IS00625", "IS00623", "IS00626", "IS00627", 20);
            }
        },
        NUMBER = {
            CS00024_IS00287: (i, k, v, o) => {
                let dfd = $.Deferred(),
                    result = timeNumber(i, k, v, o, "IS00287", "IS00288", "IS00289");
                dfd.resolve(result);
                return dfd.promise();
            },
            CS00024_IS00288: (i, k, v, o) => {
                let dfd = $.Deferred(),
                    result = timeNumber(i, k, v, o, "IS00287", "IS00288", "IS00289");
                dfd.resolve(result);
                return dfd.promise();
            },
            CS00024_IS00291: (i, k, v, o) => {
                let dfd = $.Deferred(),
                    result = timeNumber(i, k, v, o, "IS00291", "IS00292", "IS00293");
                dfd.resolve(result);
                return dfd.promise();
            },
            CS00024_IS00292: (i, k, v, o) => {
                let dfd = $.Deferred(),
                    result = timeNumber(i, k, v, o, "IS00291", "IS00292", "IS00293");
                dfd.resolve(result);
                return dfd.promise();
            }
        };
        
        function timeNumber(i, k, v, o, firstCode, secondCode, resultCode) {
            let dt = __viewContext.viewModel.dataTypes[firstCode], result = [];
            if (!dt) {
                return;    
            }
            
            let first = o[firstCode], second = o[secondCode];
            if ((_.isNil(first) || first === "") && (_.isNil(second) || second === "")) {
                result.push({ id: i, item: resultCode, value: "" });
                return result;
            }
            
            if (dt.cls.dataTypeValue === ITEM_SINGLE_TYPE.TIME) {    
                first = nts.uk.time.parseTime(first);
                second = nts.uk.time.parseTime(second);
                if (first.success && second.success) {
                    result.push({ id: i, item: resultCode, value: nts.uk.time.parseTime(first.toValue() - second.toValue(), true).format() });
                } else if (first.success && !second.success) {
                    result.push({ id: i, item: resultCode, value: nts.uk.time.parseTime(first.toValue(), true).format() });
                } else if (!first.success && second.success) {
                    result.push({ id: i, item: resultCode, value: nts.uk.time.parseTime(-1 * second.toValue(), true).format() });
                } else {
                    result.push({ id: i, item: resultCode, value: "" });
                }
            } else if (dt.cls.dataTypeValue === ITEM_SINGLE_TYPE.TIMEPOINT) {
                first = nts.uk.time.parseTime(first);
                second = nts.uk.time.parseTime(second);
                if (first.success && second.success) {
                    result.push({ id: i, item: resultCode, value: nts.uk.time.minutesBased.clock.dayattr.create(first.toValue() - second.toValue()).shortText });
                } else if (first.success && !second.success) {
                    result.push({ id: i, item: resultCode, value: nts.uk.time.minutesBased.clock.dayattr.create(first.toValue()).shortText });
                } else if (!first.success && second.success) {
                    result.push({ id: i, item: resultCode, value: nts.uk.time.minutesBased.clock.dayattr.create(-1 * second.toValue()).shortText });
                } else {
                    result.push({ id: i, item: resultCode, value: "" });
                }
            } else if (dt.cls.dataTypeValue === ITEM_SINGLE_TYPE.NUMERIC) {
                result.push({ id: i, item: resultCode, value: Number(first) - Number(second) });
            } else {
                result.push({ id: i, item: resultCode, value: "" });
            }
            
            return result;
        }   
        
        function specialOff(v, o, cbxCode, grantDayCode, manageCode, grantTblCode, resultCode, specialCd) {
            if (_.isNil(cbxCode)) return;
            let sid = o.employeeId, hireDate, retireDate, yearRefDate,
                cbx = o[cbxCode], grantDay = o[grantDayCode], manage = o[manageCode], grantTbl = o[grantTblCode], 
                result = o[resultCode], $grid = $("#grid");
            if (!v || !cbx || !manage || manage === "0") { 
                $grid.mGrid("updateCell", o.id, resultCode, "");
                return;
            }
            
            let consGrantDays;
            if (grantDayCode) {
                let grantDaysConstr = _.find(__viewContext.viewModel.gridOptions.columns, c => {
                    return c.key === grantDayCode;
                });
                if (grantDaysConstr) {
                    consGrantDays = __viewContext.primitiveValueConstraints[grantDaysConstr.itemId.replace(/[-_]/g, "")];
                }
            }
            
            let inputDate = moment.utc(v);
            if (!inputDate.isValid() || inputDate.diff(moment.utc("1900/01/01"), "days", true) < 0
                || inputDate.diff(moment.utc("9999/12/31"), "days", true) > 0
                || _.isNaN(grantDay) || (consGrantDays && !_.isNaN(grantDay) && (grantDay < consGrantDays.min || grantDay > consGrantDays.max))) {
                return;
            }
            
            fetch.get_sphd_nextGrantDate({
                sid: sid,
                grantDate: moment.utc(v).toDate(),
                spLeaveCD: specialCd,
                appSet: cbx,
                grantDays: grantDay,
                grantTable: grantTbl,
                entryDate: moment.utc(hireDate).toDate(),
                yearRefDate: moment.utc(yearRefDate).toDate()
            }).done(res => {
                if (!resultCode) return;
                let x;
                if (res && (x = moment.utc(ko.toJS(res))) && x.isValid()) {
                    $grid.mGrid("updateCell", o.id, resultCode, x.format('YYYY/MM/DD'));
                } else {
                    $grid.mGrid("updateCell", o.id, resultCode, "");
                }
            });
        }
        
        function dateSubscribeCombo(code, v, o) {
            let empId = o.employeeId, comboData = (((__viewContext || {}).viewModel || {}).dataTypes || {})[code], date = moment.utc(v, "YYYY/MM/DD"),
                catId = (((__viewContext || {}).viewModel || {}).category || {}).catId; 
            if (_.isNil(empId) || empId === "" || _.isNil(comboData) || _.isNil(catId()) 
                || date.diff(moment.utc("1900/01/01", "YYYY/MM/DD"), "days", true) < 0 
                || date.diff(moment.utc("9999/12/31", "YYYY/MM/DD"), "days", true) > 0) {
                return;
            }
            
            fetch.get_cb_data({
                comboBoxType: comboData.cls.referenceType,
                categoryId: catId(),
                required: comboData.cls.required,
                standardDate: date.toDate(),
                typeCode: comboData.cls.typeCode,
                masterType: comboData.cls.masterType,
                employeeId: empId,
                cps002: false,
                workplaceId: undefined,
                baseDate: undefined
            }).done((cbx: Array<IComboboxItem>) => {
//                if (code === "IS00079") {
                    $("#grid").mGrid("updateCell", o.id, code, cbx, null, null, true);
//                }
            });
        }
        
        export function selectButton() {
            _.forEach(selectGroups, (g: IGroupControl) => {
                if (!g.workType) {
                    if (g.workplace) {
                        SELECT_BUTTON[g.ctgCode + "_" + g.workplace] = (required, data) => {
                            let startDate = data.rowValue[g.startDate];
                            if (startDate && moment(startDate).isValid()) {
                                fetch.checkFunctionNo().done(role => {
                                    setShared('inputCDL008', {
                                        selectedCodes: [data.value],
                                        baseDate: startDate,
                                        isMultiple: false,
                                        selectedSystemType: 1, // 1 : 個人情報 , 2 : 就業 , 3 :給与 , 4 :人事 ,  5 : 管理者
                                        isrestrictionOfReferenceRange: role.available,
                                        showNoSelection: !required,
                                        isShowBaseDate: false
                                    }, true);
                                    
                                    modal('com', '/view/cdl/008/a/index.xhtml').onClosed(() => {
                                        if (getShared('CDL008Cancel')) {
                                            return;
                                        }
    
                                        let output = getShared('outputCDL008');
                                        if (!_.isNil(output)) {
                                            $("#grid").mGrid("updateCell", data.rowId, g.workplace, output);
                                        }
                                    });
                                });
                            }
                        };    
                    }
                    
                    return;
                }
                
                if (!g.workTime) {
                    SELECT_BUTTON[g.ctgCode + "_" + g.workType] = (required, data) => {
                        setShared("KDL002_isShowNoSelectRow", !required);
                        setShared("KDL002_Multiple", false, true);
                        setShared('kdl002isSelection', false, true);
                        setShared("KDL002_SelectedItemId", _.isNil(data.value) ? [] : [ data.value ], true);
                        // lstComboBoxValue
                        setShared("KDL002_AllItemObj", _.map(data.itemList, x => x.optionValue), true);
    
                        modal('at', '/view/kdl/002/a/index.xhtml').onClosed(() => {
                            let childData: Array<any> = getShared('KDL002_SelectedNewItem');
    
                            if (childData[0]) {
                                $("#grid").mGrid("updateCell", data.rowId, g.workType, childData[0].code);
                            }
                        });
                    }
                } else {
                    WORK_TIME[g.workTime] = { firstTimes: g.firstTimes, secondTimes: g.secondTimes };
                    SELECT_BUTTON[g.ctgCode + "_" + g.workType] = (required, data) => {
                        if (['IS00130', 'IS00139'].indexOf(g.workType) > - 1) {
                            setShared('parentCodes', {
                                workTypeCodes: g.workType && _.map(data.itemList, x => x.optionValue),
                                selectedWorkTypeCode: g.workType && data.value,
                                // getRelatedItemList: getItemList of column
                                workTimeCodes: g.workTime && _.map(data.relatedItemList(g.workTime), x => x.optionValue),
                                selectedWorkTimeCode: g.workTime && data.rowValue[g.workTime]
                            }, true);
        
                            modal('at', '/view/kdl/003/a/index.xhtml').onClosed(() => {
                                let childData: IChildData = getShared('childData'), $grid = $("#grid");
        
                                if (childData) {
                                    $grid.mGrid("updateCell", data.rowId, g.workType, childData.selectedWorkTypeCode);
                                    $grid.mGrid("updateCell", data.rowId, g.workTime, childData.selectedWorkTimeCode);
                                    
                                    if (g.firstTimes) {
                                        updateTime($grid, data.rowId, g.firstTimes.start, childData.first && childData.first.start);
                                        updateTime($grid, data.rowId, g.firstTimes.end, childData.first && childData.first.end);
                                    }
                                    
                                    if (g.secondTimes) {
                                        updateTime($grid, data.rowId, g.secondTimes.start, childData.second && childData.second.start);
                                        updateTime($grid, data.rowId, g.secondTimes.end, childData.second && childData.second.end);
                                    }
                                    
                                    setEnable(childData.selectedWorkTimeCode, data, g);
                                }
                            });
                        } else {
                            setShared("KDL002_isShowNoSelectRow", !required);
                            setShared("KDL002_Multiple", false, true);
                            setShared('kdl002isSelection', true, true);
                            setShared("KDL002_SelectedItemId", _.isNil(data.value) ? [] : [data.value], true);
                            setShared("KDL002_AllItemObj", _.map(data.itemList, x => x.optionValue), true);
        
                            modal('at', '/view/kdl/002/a/index.xhtml').onClosed(() => {
                                let childData: Array<any> = getShared('KDL002_SelectedNewItem');
        
                                if (childData.length > 0) {
                                    $("#grid").mGrid("updateCell", data.rowId, g.workType, childData[0].code); 
                                }
                            });
                        }
                    };
                        
                    SELECT_BUTTON[g.ctgCode + "_" + g.workTime] = (required, data) => {

                        if (['IS00131', 'IS00140'].indexOf(g.workTime) > - 1) {
                            setShared('parentCodes', {
                                workTypeCodes: g.workType && _.map(data.relatedItemList(g.workType), x => x.optionValue),
                                selectedWorkTypeCode: g.workType && data.rowValue[g.workType],
                                workTimeCodes: g.workTime && _.map(data.itemList, x => x.optionValue),
                                selectedWorkTimeCode: g.workTime && data.value
                            }, true);

                            modal('at', '/view/kdl/003/a/index.xhtml').onClosed(() => {
                                let childData: IChildData = getShared('childData'), $grid = $("#grid");

                                if (childData) {
                                    $grid.mGrid("updateCell", data.rowId, g.workType, childData.selectedWorkTypeCode);
                                    $grid.mGrid("updateCell", data.rowId, g.workTime, childData.selectedWorkTimeCode);
                                    
                                    if (g.firstTimes) {
                                        updateTime($grid, data.rowId, g.firstTimes.start, childData.first && childData.first.start);
                                        updateTime($grid, data.rowId, g.firstTimes.end, childData.first && childData.first.end);
                                    }
                                    
                                    if (g.secondTimes) {
                                        updateTime($grid, data.rowId, g.secondTimes.start, childData.second && childData.second.start);
                                        updateTime($grid, data.rowId, g.secondTimes.end, childData.second && childData.second.end);
                                    }
                                    
                                    setEnable(childData.selectedWorkTimeCode, data, g);
                                }
                            });
                        } else {
                            setShared("kml001multiSelectMode", false);
                            setShared("kml001selectedCodeList", _.isNil(data.value) ? [] : [data.value]);
                            setShared("kml001isSelection", true);
                            setShared("kml001selectAbleCodeList", _.map(data.itemList, x => x.optionValue), true);

                            modal('at', '/view/kdl/001/a/index.xhtml').onClosed(() => {
                                let childData: Array<IChildData> = getShared('kml001selectedTimes'), $grid = $("#grid");
                                if (childData) {
                                    if (childData.length > 0) {
                                        let oData: IChildData = childData[0];
                                        $grid.mGrid("updateCell", data.rowId, g.workTime, oData.selectedWorkTimeCode);
                                        
                                        if (g.firstTimes) {
                                            updateTime($grid, data.rowId, g.firstTimes.start, oData.first && oData.first.start);
                                            updateTime($grid, data.rowId, g.firstTimes.end, oData.first && oData.first.end);
                                        }
                                        
                                        if (g.secondTimes) {
                                            updateTime($grid, data.rowId, g.secondTimes.start, oData.second && oData.second.start);
                                            updateTime($grid, data.rowId, g.secondTimes.end, oData.second && oData.second.end);
                                        }
                                        
                                        setEnable(oData.selectedWorkTimeCode, data, g);
                                    }
                                }
                            });
                        }
                    };  
                };
            });
        }
        
        function updateTime($grid: any, id: any, time: any, value: any) {
            if (!_.isNil(value)) {
                value = nts.uk.time.minutesBased.clock.dayattr.create(value).shortText;
            }
            
            $grid.mGrid("updateCell", id, time, value, null, true);
        }
        
        export function setEnable(workTimeCode: any, data: any, group: IGroupControl) {
            let $grid = $("#grid");
            if (workTimeCode) {
                let command = { workTimeCode: workTimeCode };
                fetch.check_start_end(command).done(first => {
                    if (group.firstTimes) {
                        if (!first) {
                            $grid.mGrid("disableNtsControlAt", data.rowId, group.firstTimes.start); 
                            $grid.mGrid("disableNtsControlAt", data.rowId, group.firstTimes.end);
                        } else {
                            $grid.mGrid("enableNtsControlAt", data.rowId, group.firstTimes.start);
                            $grid.mGrid("enableNtsControlAt", data.rowId, group.firstTimes.end);
                        }
                    }
                    
                    if (group.secondTimes) {
                        fetch.check_multi_time(command).done(second => {
                            if (first && second) {
                                $grid.mGrid("enableNtsControlAt", data.rowId, group.secondTimes.start);
                                $grid.mGrid("enableNtsControlAt", data.rowId, group.secondTimes.end);
                            } else {
                                $grid.mGrid("disableNtsControlAt", data.rowId, group.secondTimes.start);
                                $grid.mGrid("disableNtsControlAt", data.rowId, group.secondTimes.end);
                            }
                        });
                    }
                });
            } else {
                if (group.firstTimes) {
                    $grid.mGrid("disableNtsControlAt", data.rowId, group.firstTimes.start);
                    $grid.mGrid("disableNtsControlAt", data.rowId, group.firstTimes.end);
                }
                
                if (group.secondTimes) {
                    $grid.mGrid("disableNtsControlAt", data.rowId, group.secondTimes.start);
                    $grid.mGrid("disableNtsControlAt", data.rowId, group.secondTimes.end);
                }
            }
        }
        
        export function relateButton() {
            _.forEach(relateButtonGroups, (g: IRelateButton) => {
                RELATE_BUTTON[g.ctgCode + "_" + g.btnCode] = (v) => {
                    let sid = v.rowValue.employeeId;
                    setShared('CPS001GHI_VALUES', {
                        ctgCode: g.ctgCode,
                        sid: sid
                    });
        
                    modal('com', `/view/cps/001/${g.dialogId}/index.xhtml`).onClosed(() => {    
                        if (!sid) {
                            return;
                        }
        
                        let $grid = $("#grid");
                        switch (g.dialogId) {
                            case "g":
                                fetch.get_annLeaNumber(sid).done(data => {
                                    $grid.mGrid("updateCell", v.rowId, g.btnCode, data.annualLeaveNumber);
                                    if (g.lblCode) {
                                        $grid.mGrid("updateCell", v.rowId, g.lblCode, data.lastGrantDate);
                                    }
                                });
                                break;
                            case "h":
                                fetch.get_resvLeaNumber(sid).done(data => {
                                    $grid.mGrid("updateCell", v.rowId, g.btnCode, data);
                                });
                                break;
                            case "i":
                                fetch.get_calDayTime(sid, g.specialCd).done(data => {
                                    $grid.mGrid("updateCell", v.rowId, g.btnCode, data);
                                });
                        }
                    });
                };
            });
        }
        
        export function primitiveConst(x: any) {
            let dts = x.itemTypeState.dataTypeState,
                constraint: any = {
                    itemName: x.itemName,
                    itemCode: x.itemId.replace(/[-_]/g, ""),
                    required: x.required
                };
    
            if (dts) {
                switch (dts.dataTypeValue) {
                    default:
                    case ITEM_SINGLE_TYPE.STRING:
                        constraint.valueType = "String";
                        constraint.maxLength = dts.stringItemLength || dts.maxLength;
                        constraint.stringExpression = /(?:)/;
    
                        switch (dts.stringItemType) {
                            default:
                            case ITEM_STRING_TYPE.ANY:
                                constraint.charType = 'Any';
                                break;
                            case ITEM_STRING_TYPE.CARDNO:
                                constraint.itemCode = 'StampNumber';
                                constraint.charType = 'AnyHalfWidth';
                                constraint.stringExpression = /^[a-zA-Z0-9\s"#$%&(~|{}\[\]@:`*+?;\\/_\-><)]{1,20}$/;
                                break;
                            case ITEM_STRING_TYPE.EMPLOYEE_CODE:
                                constraint.itemCode = 'EmployeeCode';
                                constraint.charType = 'AnyHalfWidth';
                                break;
                            case ITEM_STRING_TYPE.ANYHALFWIDTH:
                                constraint.charType = 'AnyHalfWidth';
                                break;
                            case ITEM_STRING_TYPE.ALPHANUMERIC:
                                constraint.charType = 'AlphaNumeric';
                                break;
                            case ITEM_STRING_TYPE.NUMERIC:
                                constraint.charType = 'Numeric';
                                break;
                            case ITEM_STRING_TYPE.KANA:
                                constraint.charType = 'Kana';
                                break;
                        }
                        break;
                    case ITEM_SINGLE_TYPE.NUMERIC:
                    case ITEM_SINGLE_TYPE.NUMBERIC_BUTTON:
                        constraint.charType = 'Numeric';
                        if (dts.decimalPart == 0) {
                            constraint.valueType = "Integer";
                        } else {
                            constraint.valueType = "Decimal";
                            constraint.mantissaMaxLength = dts.decimalPart;
                        }
    
                        let max = (Math.pow(10, dts.integerPart) - Math.pow(10, -(dts.decimalPart || 0)));
                        constraint.min = dts.numericItemMin || 0;
                        constraint.max = dts.numericItemMax || max;
                        break;
                    case ITEM_SINGLE_TYPE.DATE:
                        constraint.valueType = "Date";
                        constraint.max = parseTime(dts.max, true).format() || '';
                        constraint.min = parseTime(dts.min, true).format() || '';
                        break;
                    case ITEM_SINGLE_TYPE.TIME:
                        constraint.valueType = "Time";
                        constraint.max = parseTime(dts.max, true).format();
                        constraint.min = parseTime(dts.min, true).format();
                        break;
                    case ITEM_SINGLE_TYPE.TIMEPOINT:
                        constraint.valueType = "TimeWithDay";
                        constraint.max = parseTimeWithDay(dts.timePointItemMax).shortText;
                        constraint.min = parseTimeWithDay(dts.timePointItemMin).shortText;
                        break;
                    case ITEM_SINGLE_TYPE.SELECTION:
                        constraint.valueType = "Selection";
                        break;
                    case ITEM_SINGLE_TYPE.SEL_RADIO:
                        constraint.valueType = "Radio";
                        break;
                    case ITEM_SINGLE_TYPE.SEL_BUTTON:
                        constraint.valueType = "Button";
                        break;
                    case ITEM_SINGLE_TYPE.READONLY:
                        constraint.valueType = "READONLY";
                        break;
                    case ITEM_SINGLE_TYPE.RELATE_CATEGORY:
                        constraint.valueType = "RELATE_CATEGORY";
                        break;
                    case ITEM_SINGLE_TYPE.READONLY_BUTTON:
                        constraint.valueType = "READONLY_BUTTON";
                        break;
                }
                
                let cateCode = __viewContext.viewModel.category.catCode();
                if (HALF_INT[cateCode + "_" + x.itemCode]) {
                    constraint.valueType = "HalfInt";
                }
            }
            return constraint;
        }
        
        export function writePrimitiveConstraint(data: any) {
            let constraint = primitiveConst(data);
            if (constraint) {
                writeConstraint(constraint.itemCode, constraint);
            }
        }
        
        interface IGroupControl {
            ctgCode: string;
            workType?: string;
            workTime?: string;
            firstTimes?: ITimeRange;
            secondTimes?: ITimeRange;
            workplace: string;
            startDate: string;
        }
    
        interface ITimeRange {
            start: string;
            end: string;
        }
        
        interface IChildData {
            selectedWorkTypeCode: string;
            selectedWorkTypeName: string;
            selectedWorkTimeCode: string;
            selectedWorkTimeName: string;
            first: IDateRange;
            second: IDateRange;
        }
        
        interface IDateRange {
            start: number;
            end: number;
        }
        
        interface ICheckParam {
            workTimeCode?: string;
        }
        
        interface IRelateButton {
            ctgCode: string;
            btnCode: string;
            dialogId: string;
            specialCd?: number;
            lblCode?: string;
        }
        
        interface IComboboxItem {
            optionText: string;
            optionValue: string;
        }
        
        interface INextTimeParam {
            employeeId: string;
            standardDate: Date;
            grantTable: string;
            entryDate: Date;
            startWorkCond: Date;
            endWorkCond: Date;
            contactTime: number;
        }
        
        interface ISpecialParam {
            sid: string;
            grantDate: Date;
            spLeaveCD: number;
            appSet: number;
            grantDays?: number;
            grantTable?: string;
            entryDate: Date;
            yearRefDate: Date;
        }
    }
}