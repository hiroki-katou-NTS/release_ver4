module nts.layout {
    import ajax = nts.uk.request.ajax;
    import modal = nts.uk.ui.windows.sub.modal;
    import nou = nts.uk.util.isNullOrUndefined;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    import parseTime = nts.uk.time.parseTime;

    let rmError = nts.uk.ui.errors["removeByCode"],
        getError = nts.uk.ui.errors["getErrorByElement"],
        getErrorList = nts.uk.ui.errors["getErrorList"],
        removeErrorByElement = window['nts']['uk']['ui']['errors']["removeByElement"],
        clearError = window['nts']['uk']['ui']['errors']['clearAll'],
        parseTimeWidthDay = window['nts']['uk']['time']['minutesBased']['clock']['dayattr']['create'];

    export const validate = {
        removeDoubleLine: (items: Array<any>) => {
            let maps = _(items)
                .map((x, i) => (x.layoutItemType == IT_CLA_TYPE.SPER) ? i : -1)
                .filter(x => x != -1)
                .value(),
                dupl = _(maps)
                    .filter((x, i) => maps[i + 1] == x + 1)
                    .value();

            _.remove(items, (m: any, k: number) => dupl.indexOf(k) > -1);
        },
        initCheckError: (items: Array<any>) => {
            // validate button, radio button
            _(items)
                .filter(x => _.has(x, "items") && !!x.items)
                .map(x => x.items)
                .flatten()
                .flatten()
                .filter((x: IItemData) => x.required && x.type != ITEM_TYPE.SET)
                .each((x: IItemData) => {
                    let v: any = ko.toJS(x),
                        id = v.itemDefId.replace(/[-_]/g, ''),
                        element = document.getElementById(id),
                        $element = $(element);

                    if (element && (element.tagName.toUpperCase() == "BUTTON" || $element.hasClass('radio-wrapper'))) {
                        x.value.subscribe(d => {
                            !nou(d) && rmError($element, "FND_E_REQ_SELECT");
                        });
                    }
                });
        },
        checkError: (items: Array<any>) => {
            _(items)
                .filter(x => _.has(x, "items") && !!x.items)
                .map(x => x.items)
                .flatten()
                .flatten()
                .filter((x: any) => x.type != ITEM_TYPE.SET)
                .map(x => ko.toJS(x))
                .each(x => {
                    let id = x.itemDefId.replace(/[-_]/g, ''),
                        element = document.getElementById(id),
                        $element = $(element);

                    if (element && !!x.editable) {
                        if (element.tagName.toUpperCase() == "INPUT") {
                            $element
                                .trigger('blur')
                                .trigger('change');
                        } else if ((element.tagName.toUpperCase() == "BUTTON" || $element.hasClass('radio-wrapper'))) {
                            if (nou(x.value) && x.required) {
                                if (!getError($element).length) {
                                    $element.ntsError('set', {
                                        messageId: "FND_E_REQ_SELECT",
                                        messageParams: [x.itemName]
                                    });
                                }
                            }
                        }
                        else {
                            $element
                                .trigger('validate')
                                .find('.nts-input')
                                .trigger('blur')
                                .trigger('change');
                        }
                    }
                });
        }
    }

    class constraint {
        lstCls: Array<any> = [];

        constructor(lstCls: Array<any>) {
            let self = this;

            self.lstCls = lstCls;
        }

        find = (categoryCode: string, subscribeCode: string): IFindData => {
            let self = this,
                controls: Array<any> = _(self.lstCls).filter(x => _.has(x, "items") && !!x.items).map(x => x.items).flatten().flatten().value(),
                subscribe: any = _.find(controls, (x: any) => x.categoryCode.indexOf(categoryCode) > -1 && x.itemCode == subscribeCode);

            if (subscribe) {
                return <IFindData>{
                    id: `#${subscribe.itemDefId.replace(/[-_]/g, '')}`,
                    data: subscribe,
                    ctrl: $('#' + subscribe.itemDefId.replace(/[-_]/g, ''))
                };
            }

            return null;
        };

        finds = (categoryCode: string, subscribesCode: Array<string>): Array<IFindData> => {
            if (!_.isArray(subscribesCode)) {
                throw "[subscribesCode] isn't an array!";
            }

            let self = this,
                controls: Array<any> = _(self.lstCls).filter(x => _.has(x, "items") && !!x.items).map(x => x.items).flatten().flatten().value(),
                subscribes: Array<any> = _.filter(controls, (x: any) => x.categoryCode.indexOf(categoryCode) > -1 && (subscribesCode || []).indexOf(x.itemCode) > -1);

            return subscribes.map(x => {
                return <IFindData>{
                    id: `#${x.itemDefId.replace(/[-_]/g, '')}`,
                    data: x,
                    ctrl: $('#' + x.itemDefId.replace(/[-_]/g, ''))
                };
            });
        };

        findChilds = (categoryCode: string, parentCode: string): Array<IFindData> => {
            let self = this,
                controls: Array<any> = _(self.lstCls).filter(x => _.has(x, "items") && !!x.items).map(x => x.items).flatten().flatten().value(),
                subscribes: Array<any> = _.filter(controls, (x: any) => x.categoryCode.indexOf(categoryCode) > -1 && x.itemParentCode == parentCode),
                childset: Array<string> = _(subscribes).filter(x => [ITEM_TYPE.SET, ITEM_TYPE.SET_TABLE].indexOf(x.type) > -1).map(x => x.itemCode).value();

            _.each(childset, code => {
                let child = _.filter(controls, (x: any) => x.categoryCode.indexOf(categoryCode) > -1 && x.itemParentCode == code);
                subscribes = _.concat(subscribes, child);
            });

            return subscribes.map(x => {
                return <IFindData>{
                    id: `#${x.itemDefId.replace(/[-_]/g, '')}`,
                    data: x,
                    ctrl: $('#' + x.itemDefId.replace(/[-_]/g, ''))
                };
            });
        };
    }

    const fetch = {
        get_cb_data: (param: IComboParam) => ajax(`ctx/pereg/person/common/getFlexComboBox`, param),
        check_start_end: (param: ICheckParam) => ajax(`ctx/pereg/person/common/checkStartEnd`, param),
        check_multi_time: (param: ICheckParam) => ajax(`ctx/pereg/person/common/checkMultiTime`, param),
        get_ro_data: (param: INextTimeParam) => ajax('at', `at/record/remainnumber/annlea/event/nextTime`, param),
        get_annLeaNumber: (sid: string) => ajax('at', `at/record/remainnumber/annlea/getAnnLeaNumber/${sid}`),
        get_resvLeaNumber: (sid: string) => ajax('com', `ctx/pereg/layout/getResvLeaNumber/${sid}`),
        get_calDayTime: (sid: string, specialCd: number) => ajax('com', `ctx/pereg/layout/calDayTime/${sid}/${specialCd}`),
        check_remain_days: (sid: string) => ajax('com', `ctx/pereg/person/common/checkEnableRemainDays/${sid}`),
        check_remain_left: (sid: string) => ajax('com', `ctx/pereg/person/common/checkEnableRemainLeft/${sid}`)
    }

    export class validation {
        finder: IFinder = undefined;
        constructor(private lstCls: Array<any>) {
            let self = this;
            self.finder = new constraint(lstCls);

            setTimeout(() => {
                self.textBox();
                self.radio();
                self.button();
                self.combobox();
                self.grand_radio();
                //self.relate_radio();
                self.relate_button();

                self.remain_day();

                self.dateTime();
                self.setTable();
                self.grantInformation();
                self.specialLeaveInformation();

                self.time_range();

                self.haft_int();

                validate.initCheckError(lstCls);
            }, 50);
        }

        textBox = () => {
            let self = this,
                finder = self.finder,
                CS00002_IS00003: IFindData = finder.find('CS00002', 'IS00003'),
                CS00002_IS00004: IFindData = finder.find('CS00002', 'IS00004'),
                CS00002_IS00015: IFindData = finder.find('CS00002', 'IS00015'),
                CS00002_IS00016: IFindData = finder.find('CS00002', 'IS00016'),
                validateName = (item: IFindData) => {
                    $(item.id).on('blur', () => {
                        let value: string = ko.toJS(item.data.value),
                            index: number = value.indexOf('　'),
                            lindex: number = value.lastIndexOf('　'),
                            dom = $(item.id);

                        if (!value || (index > 0 && lindex < value.length - 1)) {
                            rmError(dom, "Msg_924");
                        } else if (!dom.is(':disabled') && !dom.ntsError('hasError')) {
                            dom.ntsError('set', {
                                messageId: "Msg_924",
                                messageParams: [item.data.itemName]
                            });
                        }
                    });
                };

            CS00002_IS00003 && validateName(CS00002_IS00003);

            CS00002_IS00004 && validateName(CS00002_IS00004);

            CS00002_IS00015 && validateName(CS00002_IS00015);

            CS00002_IS00016 && validateName(CS00002_IS00016);
        }

        radio = () => {
            let self = this,
                finder = self.finder,
                CS00020_IS00248: IFindData = finder.find('CS00020', 'IS00248'),
                CS00020_IS00121: IFindData = finder.find('CS00020', 'IS00121'),
                CS00020_IS00123: IFindData = finder.find("CS00020", "IS00123");


            if (CS00020_IS00248) {
                CS00020_IS00248.data.value.subscribe(x => {
                    let ctrls: Array<IFindData> = finder.findChilds(CS00020_IS00248.data.categoryCode, CS00020_IS00248.data.itemParentCode);

                    _.each(ctrls, c => {
                        if (c.data.itemCode != CS00020_IS00248.data.itemCode) {
                            c.data.editable(x == 1);
                        }
                    });
                });

                setTimeout(() => {
                    CS00020_IS00248.data.value.valueHasMutated();
                }, 0);
            }

            if (CS00020_IS00121) {
                CS00020_IS00121.data.value.subscribe(x => {
                    let ctrls: Array<IFindData> = finder.findChilds(CS00020_IS00121.data.categoryCode, CS00020_IS00121.data.itemParentCode);

                    _.each(ctrls, c => {
                        if (c.data.itemCode != CS00020_IS00121.data.itemCode) {
                            c.data.editable(x == 1);
                            if (x == 1 && CS00020_IS00123) {
                                CS00020_IS00123.data.value.valueHasMutated();
                            }
                        }
                    });
                });

                setTimeout(() => {
                    CS00020_IS00121.data.value.valueHasMutated();
                }, 0);
            }
        }

        remain_day = () => {
            let self = this,
                finder = self.finder,
                empId = ko.toJS((((__viewContext || {}).viewModel || {}).employee || {}).employeeId),
                CS00035_IS00366: IFindData = finder.find('CS00035', 'IS00366'),
                CS00035_IS00368: IFindData = finder.find('CS00035', 'IS00368');

            if (!empId) {
                return;
            }

            if (CS00035_IS00366) {
                fetch.check_remain_days(empId).done(x => {
                    CS00035_IS00366.data.numberedit(x);
                });
            }

            if (CS00035_IS00368) {
                fetch.check_remain_left(empId).done(x => {
                    CS00035_IS00368.data.numberedit(x);
                });
            }
        }

        grand_radio = () => {
            let self = this,
                finder = self.finder,
                radios: Array<IGrandRadio> = [{
                    ctgCode: 'CS00025',
                    radioCode: 'IS00296',
                    relateCode: ['IS00297', 'IS00298', 'IS00299', 'IS00300', 'IS00301']
                }, {
                        ctgCode: 'CS00026',
                        radioCode: 'IS00303',
                        relateCode: ['IS00304', 'IS00305', 'IS00306', 'IS00307', 'IS00308']
                    }, {
                        ctgCode: 'CS00027',
                        radioCode: 'IS00310',
                        relateCode: ['IS00311', 'IS00312', 'IS00313', 'IS00314', 'IS00315']
                    }, {
                        ctgCode: 'CS00028',
                        radioCode: 'IS00317',
                        relateCode: ['IS00318', 'IS00319', 'IS00320', 'IS00321', 'IS00322']
                    }, {
                        ctgCode: 'CS00029',
                        radioCode: 'IS00324',
                        relateCode: ['IS00325', 'IS00326', 'IS00327', 'IS00328', 'IS00329']
                    }, {
                        ctgCode: 'CS00030',
                        radioCode: 'IS00331',
                        relateCode: ['IS00332', 'IS00333', 'IS00334', 'IS00335', 'IS00336']
                    }, {
                        ctgCode: 'CS00031',
                        radioCode: 'IS00338',
                        relateCode: ['IS00339', 'IS00340', 'IS00341', 'IS00342', 'IS00343']
                    }, {
                        ctgCode: 'CS00032',
                        radioCode: 'IS00345',
                        relateCode: ['IS00346', 'IS00347', 'IS00348', 'IS00349', 'IS00350']
                    }, {
                        ctgCode: 'CS00033',
                        radioCode: 'IS00352',
                        relateCode: ['IS00353', 'IS00354', 'IS00355', 'IS00356', 'IS00357']
                    }, {
                        ctgCode: 'CS00034',
                        radioCode: 'IS00359',
                        relateCode: ['IS00360', 'IS00361', 'IS00362', 'IS00363', 'IS00364']
                    }, {
                        ctgCode: 'CS00035',
                        radioCode: 'IS00370',
                        relateCode: ['IS00371', 'IS00372', 'IS00374']
                    }, {
                        ctgCode: 'CS00036',
                        radioCode: 'IS00375',
                        relateCode: ['IS00376', 'IS00377', 'IS00378', 'IS00379']
                    }, {
                        ctgCode: 'CS00036',
                        radioCode: 'IS00380',
                        relateCode: ['IS00381', 'IS00382', 'IS00383', 'IS00384']
                    }, {
                        ctgCode: 'CS00049',
                        radioCode: 'IS00560',
                        relateCode: ['IS00561', 'IS00562', 'IS00563', 'IS00564', 'IS00565']
                    }, {
                        ctgCode: 'CS00050',
                        radioCode: 'IS00567',
                        relateCode: ['IS00568', 'IS00569', 'IS00570', 'IS00571', 'IS00572']
                    }, {
                        ctgCode: 'CS00051',
                        radioCode: 'IS00574',
                        relateCode: ['IS00575', 'IS00576', 'IS00577', 'IS00578', 'IS00579']
                    }, {
                        ctgCode: 'CS00052',
                        radioCode: 'IS00581',
                        relateCode: ['IS00582', 'IS00583', 'IS00584', 'IS00585', 'IS00586']
                    }, {
                        ctgCode: 'CS00053',
                        radioCode: 'IS00588',
                        relateCode: ['IS00589', 'IS00590', 'IS00591', 'IS00592', 'IS00593']
                    }, {
                        ctgCode: 'CS00054',
                        radioCode: 'IS00595',
                        relateCode: ['IS00596', 'IS00597', 'IS00598', 'IS00599', 'IS00600']
                    }, {
                        ctgCode: 'CS00055',
                        radioCode: 'IS00602',
                        relateCode: ['IS00603', 'IS00604', 'IS00605', 'IS00606', 'IS00607']
                    }, {
                        ctgCode: 'CS00056',
                        radioCode: 'IS00609',
                        relateCode: ['IS00610', 'IS00611', 'IS00612', 'IS00613', 'IS00614']
                    }, {
                        ctgCode: 'CS00057',
                        radioCode: 'IS00616',
                        relateCode: ['IS00617', 'IS00618', 'IS00619', 'IS00620', 'IS00621']
                    }, {
                        ctgCode: 'CS00058',
                        radioCode: 'IS00623',
                        relateCode: ['IS00624', 'IS00625', 'IS00626', 'IS00627', 'IS00628']
                    }, {
                        ctgCode: '',
                        radioCode: '',
                        relateCode: []
                    }],
                validation = (radio: IGrandRadio) => {
                    let rd: IFindData = finder.find(radio.rdctCode || radio.ctgCode, radio.radioCode),
                        ctrls: Array<IFindData> = _.map(radio.relateCode, x => finder.find(radio.ctgCode, x));

                    if (rd) {
                        rd.data.value.subscribe(v => {
                            _.each(ctrls, c => {
                                if (c && c.data) {
                                    c.data.editable(v == 1);
                                }
                            });
                        });
                        rd.data.value.valueHasMutated();
                    }
                };

            _(radios).each(radio => validation(radio));
        }

        relate_radio = () => {
            let self = this,
                finder = self.finder,
                radios: Array<IRelateRadio> = [
                    {
                        ctgCode: 'CS00024',
                        radioCode: 'IS00387',
                        setParentCode: 'IS00388'
                    },
                    {
                        ctgCode: 'CS00024',
                        radioCode: 'IS00400',
                        setParentCode: 'IS00401'
                    },
                    {
                        ctgCode: 'CS00025',
                        radioCode: 'IS00411',
                        setParentCode: 'IS00412'
                    },
                    {
                        ctgCode: 'CS00026',
                        radioCode: 'IS00426',
                        setParentCode: 'IS00427'
                    }, {
                        ctgCode: 'CS00027',
                        radioCode: 'IS00441',
                        setParentCode: 'IS00442'
                    }, {
                        ctgCode: 'CS00028',
                        radioCode: 'IS00456',
                        setParentCode: 'IS00457'
                    }, {
                        ctgCode: 'CS00027',
                        radioCode: 'IS00441',
                        setParentCode: 'IS00442'
                    }, {
                        ctgCode: 'CS00029',
                        radioCode: 'IS00471',
                        setParentCode: 'IS00472'
                    }, {
                        ctgCode: 'CS00030',
                        radioCode: 'IS00486',
                        setParentCode: 'IS00487'
                    }, {
                        ctgCode: 'CS00031',
                        radioCode: 'IS00501',
                        setParentCode: 'IS00502'
                    }, {
                        ctgCode: 'CS00032',
                        radioCode: 'IS00516',
                        setParentCode: 'IS00517'
                    }, {
                        ctgCode: 'CS00033',
                        radioCode: 'IS00531',
                        setParentCode: 'IS00532'
                    }, {
                        ctgCode: 'CS00034',
                        radioCode: 'IS00546',
                        setParentCode: 'IS00547'
                    }, {
                        ctgCode: 'CS00049',
                        radioCode: 'IS00631',
                        setParentCode: 'IS00632'
                    }, {
                        ctgCode: 'CS00049',
                        radioCode: 'IS00646',
                        setParentCode: 'IS00647'
                    }, {
                        ctgCode: 'CS00050',
                        radioCode: 'IS00646',
                        setParentCode: 'IS00647'
                    }, {
                        ctgCode: 'CS00051',
                        radioCode: 'IS00661',
                        setParentCode: 'IS00662'
                    }, {
                        ctgCode: 'CS00052',
                        radioCode: 'IS00676',
                        setParentCode: 'IS00677'
                    }, {
                        ctgCode: 'CS00053',
                        radioCode: 'IS00691',
                        setParentCode: 'IS00692'
                    }, {
                        ctgCode: 'CS00055',
                        radioCode: 'IS00721',
                        setParentCode: 'IS00722'
                    }, {
                        ctgCode: '',
                        radioCode: '',
                        setParentCode: ''
                    }
                ],
                validation = (radio: IRelateRadio) => {
                    let rd: IFindData = finder.find(radio.ctgCode, radio.radioCode),
                        ctrls: Array<IFindData> = finder.findChilds(radio.ctgCode, radio.setParentCode);

                    if (rd) {
                        rd.data.value.subscribe(x => {
                            _.each(ctrls, c => {
                                c.data.editable(x == 1);
                                removeErrorByElement($(c.id));
                            });
                        });

                        setTimeout(() => {
                            rd.data.value.valueHasMutated();
                        }, 0);
                    }
                };

            _(radios).each(radio => validation(radio));
        }

        button = () => {
            let self = this,
                finder = self.finder,
                groups: Array<IGroupControl> = [
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
                        ctgCode: 'CS00020',
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
                        ctgCode: 'CS00020',
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
                        ctgCode: 'CS00020',
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
                        ctgCode: 'CS00020',
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
                        ctgCode: 'CS00020',
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
                        ctgCode: 'CS00020',
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
                        ctgCode: 'CS00020',
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
                ],
                setData = (ctrl: IFindData, value?: any) => {
                    if (ctrl) {
                        ctrl.data.value(value);
                    }
                },
                setEditAble = (ctrl: IFindData, editable?: boolean) => {
                    ctrl && ctrl.data.editable(editable || false);
                },
                validateGroup = (group: IGroupControl) => {
                    let firstTimes: ITimeFindData = group.firstTimes && {
                        start: finder.find(group.ctgCode, group.firstTimes.start),
                        end: finder.find(group.ctgCode, group.firstTimes.end)
                    },
                        secondTimes: ITimeFindData = group.secondTimes && {
                            start: finder.find(group.ctgCode, group.secondTimes.start),
                            end: finder.find(group.ctgCode, group.secondTimes.end)
                        };

                    if (firstTimes && secondTimes) {
                        if (firstTimes.end && secondTimes.start) {
                            $(`${firstTimes.end.id}, ${secondTimes.start.id}`).on('blur', () => {
                                setTimeout(() => {
                                    let dom1 = $(firstTimes.end.id),
                                        dom2 = $(secondTimes.start.id),
                                        pv = ko.toJS(firstTimes.end.data.value),
                                        nv = ko.toJS(secondTimes.start.data.value),
                                        tpt = typeof pv == 'number',
                                        tnt = typeof nv == 'number';

                                    if (tpt && tnt && pv > nv) {
                                        if (!dom1.ntsError('hasError')) {
                                            dom1.ntsError('set', {
                                                messageId: "Msg_859",
                                                messageParams: [
                                                    firstTimes.end.data.itemName,
                                                    secondTimes.start.data.itemName
                                                ]
                                            });
                                        }

                                        if (!dom2.ntsError('hasError')) {
                                            dom2.parent().addClass('error');
                                        }
                                    }

                                    if ((tpt && tnt && !(pv > nv)) || (!tpt || !tnt)) {
                                        rmError(dom1, "Msg_859");

                                        if (!dom2.ntsError('hasError')) {
                                            dom2.parent().removeClass('error');
                                        }
                                    }
                                }, 50);
                            });
                        }
                    }
                },
                validateEditable = (group: IGroupControl, wtc?: any) => {
                    let command: ICheckParam = {
                        workTimeCode: ko.toJS(wtc || undefined)
                    },
                        firstTimes: ITimeFindData = group.firstTimes && {
                            start: finder.find(group.ctgCode, group.firstTimes.start),
                            end: finder.find(group.ctgCode, group.firstTimes.end)
                        },
                        secondTimes: ITimeFindData = group.secondTimes && {
                            start: finder.find(group.ctgCode, group.secondTimes.start),
                            end: finder.find(group.ctgCode, group.secondTimes.end)
                        };


                    if (command.workTimeCode) {
                        fetch.check_start_end(command).done(first => {
                            firstTimes && setEditAble(firstTimes.start, !!first);
                            firstTimes && setEditAble(firstTimes.end, !!first);

                            fetch.check_multi_time(command).done(second => {
                                secondTimes && setEditAble(secondTimes.start, !!first && !!second);
                                secondTimes && setEditAble(secondTimes.end, !!first && !!second);
                            });
                        });
                    } else {
                        firstTimes && setEditAble(firstTimes.start, false);
                        firstTimes && setEditAble(firstTimes.end, false);

                        secondTimes && setEditAble(secondTimes.start, false);
                        secondTimes && setEditAble(secondTimes.end, false);
                    }
                };

            _.each(groups, (group: IGroupControl) => {
                let workType: IFindData = group.workType && finder.find(group.ctgCode, group.workType),
                    workTime: IFindData = group.workTime && finder.find(group.ctgCode, group.workTime),
                    firstTimes: ITimeFindData = group.firstTimes && {
                        start: finder.find(group.ctgCode, group.firstTimes.start),
                        end: finder.find(group.ctgCode, group.firstTimes.end)
                    },
                    secondTimes: ITimeFindData = group.secondTimes && {
                        start: finder.find(group.ctgCode, group.secondTimes.start),
                        end: finder.find(group.ctgCode, group.secondTimes.end)
                    };

                if (firstTimes && secondTimes) {
                    validateGroup(group);
                }

                if (!workType) {
                    return;
                }

                if (!workTime) {
                    workType.ctrl.on('click', () => {
                        setShared("KDL002_Multiple", false, true);
                        setShared("KDL002_SelectedItemId", workType.data.value(), true);
                        setShared("KDL002_AllItemObj", _.map(ko.toJS(workType.data).lstComboBoxValue, x => x.optionValue), true);

                        modal('at', '/view/kdl/002/a/index.xhtml').onClosed(() => {
                            let childData: Array<any> = getShared('KDL002_SelectedNewItem');

                            if (childData[0]) {
                                setData(workType, childData[0].code);
                            }
                        });
                    });
                } else {
                    validateEditable(group, workTime.data.value);

                    workType.ctrl.on('click', () => {
                        setShared('parentCodes', {
                            workTypeCodes: workType && _.map(ko.toJS(workType.data).lstComboBoxValue, x => x.optionValue),
                            selectedWorkTypeCode: workType && ko.toJS(workType.data).value,
                            workTimeCodes: workTime && _.map(ko.toJS(workTime.data).lstComboBoxValue, x => x.optionValue),
                            selectedWorkTimeCode: workTime && ko.toJS(workTime.data).value
                        }, true);

                        modal('at', '/view/kdl/003/a/index.xhtml').onClosed(() => {
                            let childData: IChildData = getShared('childData');

                            if (childData) {
                                setData(workType, childData.selectedWorkTypeCode);

                                setData(workTime, childData.selectedWorkTimeCode);

                                firstTimes && setData(firstTimes.start, childData.first && childData.first.start);
                                firstTimes && setData(firstTimes.end, childData.first && childData.first.end);

                                secondTimes && setData(secondTimes.start, childData.second && childData.second.start);
                                secondTimes && setData(secondTimes.end, childData.second && childData.second.end);

                                validateEditable(group, workTime.data.value);
                            }
                        });
                    });

                    // handle click event of workType
                    workTime.ctrl.on('click', () => workType.ctrl.trigger('click'));
                }
            });
        }

        relate_button = () => {
            let self = this,
                finder: IFinder = self.finder,
                buttons: Array<IRelateButton> = [{
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
                    }
                ],

                validation = (btn: IRelateButton) => {
                    let button: IFindData = finder.find(btn.ctgCode, btn.btnCode),
                        label: IFindData = finder.find(btn.ctgCode, btn.lblCode);

                    if (button) {
                        $(button.id).on('click', (evt) => {
                            let sid = ko.toJS((((__viewContext || {}).viewModel || {}).employee || {}).employeeId);
                            setShared('CPS001GHI_VALUES', {
                                ctgCode: button.data.categoryCode,
                                sid: sid
                            });

                            modal('com', `/view/cps/001/${btn.dialogId}/index.xhtml`).onClosed(() => {
                                // load lai du lieu

                                if (!sid) {
                                    return;
                                }

                                switch (btn.dialogId) {
                                    case "g":
                                        fetch.get_annLeaNumber(sid).done(data => {
                                            button.data.value(data.annualLeaveNumber);
                                            if (label) {
                                                label.data.value(data.lastGrantDate);
                                            }
                                        });
                                        break;
                                    case "h":
                                        fetch.get_resvLeaNumber(sid).done(data => {
                                            button.data.value(data);
                                        });
                                        break;
                                    case "i":
                                        fetch.get_calDayTime(sid, btn.specialCd).done(data => {
                                            button.data.value(data);
                                        });
                                }
                            });
                        });
                    }
                };

            _(buttons).each(btn => validation(btn));
        }

        combobox = () => {
            let self = this,
                finder: IFinder = self.finder,
                CS00020_IS00123: IFindData = finder.find("CS00020", "IS00123"),
                CS00020_IS00124: IFindData = finder.find("CS00020", "IS00124"),
                CS00020_IS00125: IFindData = finder.find("CS00020", "IS00125"),
                CS00020_IS00126: IFindData = finder.find("CS00020", "IS00126"),
                CS00020_IS00127: IFindData = finder.find("CS00020", "IS00127");

            if (CS00020_IS00123) {
                CS00020_IS00123.data.value.subscribe(v => {
                    switch (v) {
                        case "0":
                            CS00020_IS00124 && CS00020_IS00124.data.editable(true);
                            CS00020_IS00125 && CS00020_IS00125.data.editable(true);
                            CS00020_IS00126 && CS00020_IS00126.data.editable(true);
                            CS00020_IS00127 && CS00020_IS00127.data.editable(false);
                            break;
                        case "1":
                            CS00020_IS00124 && CS00020_IS00124.data.editable(false);
                            CS00020_IS00125 && CS00020_IS00125.data.editable(false);
                            CS00020_IS00126 && CS00020_IS00126.data.editable(true);
                            CS00020_IS00127 && CS00020_IS00127.data.editable(true);
                            break;
                        case "2":
                            CS00020_IS00124 && CS00020_IS00124.data.editable(false);
                            CS00020_IS00125 && CS00020_IS00125.data.editable(false);
                            CS00020_IS00126 && CS00020_IS00126.data.editable(false);
                            CS00020_IS00127 && CS00020_IS00127.data.editable(false);
                            break;
                    }
                });
                CS00020_IS00123.data.value.valueHasMutated();
            }
        }

        // validate set table control
        setTable = () => {
            let self = this,
                finder: IFinder = self.finder,
                times: Array<ITimeTable> = [{
                    ctgCode: 'CS00024',
                    firstCode: 'IS00287',
                    secondCode: 'IS00288',
                    resultCode: 'IS00289'
                }, {
                        ctgCode: 'CS00024',
                        firstCode: 'IS00291',
                        secondCode: 'IS00292',
                        resultCode: 'IS00293'
                    }],
                calc = (time: ITimeTable) => {
                    let first: IFindData = finder.find(time.ctgCode, time.firstCode),
                        second: IFindData = finder.find(time.ctgCode, time.secondCode),
                        result: IFindData = finder.find(time.ctgCode, time.resultCode);

                    if (first && second && result) {
                        first.data.value.subscribe(x => {
                            let vnb1 = ko.toJS(first.data.value),
                                vnb2 = ko.toJS(second.data.value),
                                nb1 = typeof vnb1 == 'number',
                                nb2 = typeof vnb2 == 'number';

                            if (ITEM_SINGLE_TYPE.TIME == first.data.item.dataTypeValue) {
                                if (nb1 && nb2) {
                                    result.data.value(parseTime(vnb1 - vnb2, true).format());
                                } else if (nb1) {
                                    result.data.value(parseTime(vnb1, true).format());
                                } else if (nb2) {
                                    result.data.value(parseTime(-vnb2, true).format());
                                } else {
                                    result.data.value(undefined);
                                }
                            } else if (ITEM_SINGLE_TYPE.TIMEPOINT == first.data.item.dataTypeValue) {
                                if (nb1 && nb2) {
                                    result.data.value(parseTimeWidthDay(vnb1 - vnb2).shortText);
                                } else if (nb1) {
                                    result.data.value(parseTimeWidthDay(vnb1).shortText);
                                } else if (nb2) {
                                    result.data.value(parseTimeWidthDay(-vnb2).shortText);
                                } else {
                                    result.data.value(undefined);
                                }
                            }
                            else if (ITEM_SINGLE_TYPE.NUMERIC == first.data.item.dataTypeValue) {
                                result.data.value(Number(vnb1) - Number(vnb2));
                            } else {
                                result.data.value(undefined);
                            }
                        });

                        second.data.value.subscribe(x => first.data.value.valueHasMutated());
                        second.data.value.valueHasMutated();
                    }
                };

            _(times).each(time => calc(time));
        }

        // validate datetime control
        dateTime = () => {
            let self = this,
                finder: IFinder = self.finder,
                CS00016_IS00077: IFindData = finder.find('CS00016', 'IS00077'),
                CS00016_IS00079: IFindData = finder.find('CS00016', 'IS00079'),
                CS00017_IS00082: IFindData = finder.find('CS00017', 'IS00082'),
                CS00017_IS00084: IFindData = finder.find('CS00017', 'IS00084'),
                CS00020_IS00130: IFindData = finder.find('CS00020', 'IS00130'),
                CS00020_IS00131: IFindData = finder.find('CS00020', 'IS00131');

            if (CS00016_IS00077 && CS00016_IS00079) {
                CS00016_IS00077.data.value.subscribe(_date => {
                    let empId = ko.toJS((((__viewContext || {}).viewModel || {}).employee || {}).employeeId),
                        data = ko.toJS(CS00016_IS00077.data),
                        comboData = ko.toJS(CS00016_IS00079.data);

                    if (!empId) {
                        return;
                    }

                    fetch.get_cb_data({
                        comboBoxType: comboData.item.referenceType,
                        categoryId: comboData.categoryId,
                        required: comboData.required,
                        standardDate: moment.utc(_date).toDate(),
                        typeCode: comboData.item.typeCode,
                        masterType: comboData.item.masterType,
                        employeeId: empId,
                        cps002: false,
                        workplaceId: undefined
                    }).done((cbx: Array<IComboboxItem>) => {
                        CS00016_IS00079.data.lstComboBoxValue(cbx);
                    });
                });
            }

            if (CS00017_IS00082 && CS00017_IS00084) {
                CS00017_IS00082.data.value.subscribe(_date => {
                    let empId = ko.toJS((((__viewContext || {}).viewModel || {}).employee || {}).employeeId),
                        data = ko.toJS(CS00017_IS00082.data),
                        comboData = ko.toJS(CS00017_IS00084.data);

                    if (!empId) {
                        return;
                    }

                    fetch.get_cb_data({
                        comboBoxType: comboData.item.referenceType,
                        categoryId: comboData.categoryId,
                        required: comboData.required,
                        standardDate: moment.utc(_date).toDate(),
                        typeCode: comboData.item.typeCode,
                        masterType: comboData.item.masterType,
                        employeeId: empId,
                        cps002: false,
                        workplaceId: undefined
                    }).done((cbx: Array<IComboboxItem>) => {
                        CS00017_IS00084.data.lstComboBoxValue(cbx);
                    });
                });
            }

            if (CS00017_IS00084 && (CS00020_IS00130 || CS00020_IS00131)) {
                CS00017_IS00084.data.value.subscribe(wc => {
                    if (CS00020_IS00130) {
                        let comboData = ko.toJS(CS00020_IS00130.data);

                        fetch.get_cb_data({
                            comboBoxType: comboData.item.referenceType,
                            categoryId: comboData.categoryId,
                            required: comboData.required,
                            standardDate: undefined,
                            typeCode: undefined,
                            masterType: comboData.item.masterType,
                            employeeId: undefined,
                            cps002: true,
                            workplaceId: CS00017_IS00084.data.value()
                        }).done(data => {
                            CS00020_IS00130.data.lstComboBoxValue(data);
                        });;
                    }
                    if (CS00020_IS00131) {
                        let comboData = ko.toJS(CS00020_IS00131.data);

                        fetch.get_cb_data({
                            comboBoxType: comboData.item.referenceType,
                            categoryId: comboData.categoryId,
                            required: comboData.required,
                            standardDate: undefined,
                            typeCode: undefined,
                            masterType: comboData.item.masterType,
                            employeeId: undefined,
                            cps002: true,
                            workplaceId: CS00017_IS00084.data.value()
                        }).done(data => {
                            CS00020_IS00131.data.lstComboBoxValue(data);
                        });;
                    }
                });
            }
        }

        // 次回年休付与情報を取得する
        grantInformation = () => {
            let self = this,
                finder: IFinder = self.finder,
                CS00024_IS00279: IFindData = finder.find('CS00024', 'IS00279'),
                CS00024_IS00280: IFindData = finder.find('CS00024', 'IS00280'),
                CS00024_IS00281: IFindData = finder.find('CS00024', 'IS00281'),
                CS00024_IS00282: IFindData = finder.find('CS00024', 'IS00282'),
                CS00024_IS00283: IFindData = finder.find('CS00024', 'IS00283');

            if (CS00024_IS00279 &&
                CS00024_IS00280 &&
                CS00024_IS00281 &&
                CS00024_IS00282 &&
                CS00024_IS00283) {
                CS00024_IS00279.data.value.subscribe(x => {
                    let employeeId = ko.toJS((((__viewContext || {}).viewModel || {}).employee || {}).employeeId),
                        standardDate = ko.toJS(CS00024_IS00279.data.value),
                        grantTable = ko.toJS(CS00024_IS00280.data.value);

                    if (!employeeId) {
                        return;
                    }

                    fetch.get_ro_data({
                        employeeId: employeeId,
                        standardDate: standardDate,
                        grantTable: grantTable
                    }).done(result => {
                        CS00024_IS00281.data.value(result.nextTimeGrantDate);
                        CS00024_IS00282.data.value(result.nextTimeGrantDays);
                        CS00024_IS00283.data.value(result.nextTimeMaxTime);
                    });
                });

                CS00024_IS00280.data.value.subscribe(x => CS00024_IS00279.data.value.valueHasMutated());
                CS00024_IS00280.data.value.valueHasMutated();
            }
        }

        specialLeaveInformation = () => {
            let self = this,
                finder: IFinder = self.finder,
                specialLeaInfos: Array<ISpeacialLeaInfo> = [{
                    ctgCode: 'CS00025',
                    inpCode: 'IS00295',
                    comboboxCode: 'IS00297',
                    result: 'IS00300',
                    specialCd: 1
                }, {
                        ctgCode: 'CS00026',
                        inpCode: 'IS00302',
                        comboboxCode: 'IS00304',
                        result: 'IS00307',
                        specialCd: 2
                    }, {
                        ctgCode: 'CS00027',
                        inpCode: 'IS00309',
                        comboboxCode: 'IS00311',
                        result: 'IS00314',
                        specialCd: 3
                    }, {
                        ctgCode: 'CS00028',
                        inpCode: 'IS00316',
                        comboboxCode: 'IS00318',
                        result: 'IS00321',
                        specialCd: 4
                    }, {
                        ctgCode: 'CS00029',
                        inpCode: 'IS00323',
                        comboboxCode: 'IS00325',
                        result: 'IS00328',
                        specialCd: 5
                    }, {
                        ctgCode: 'CS00030',
                        inpCode: 'IS00330',
                        comboboxCode: 'IS00332',
                        result: 'IS00335',
                        specialCd: 6
                    }, {
                        ctgCode: 'CS00031',
                        inpCode: 'IS00337',
                        comboboxCode: 'IS00339',
                        result: 'IS00342',
                        specialCd: 7
                    }, {
                        ctgCode: 'CS00032',
                        inpCode: 'IS00344',
                        comboboxCode: 'IS00346',
                        result: 'IS00349',
                        specialCd: 8
                    }, {
                        ctgCode: 'CS00033',
                        inpCode: 'IS00351',
                        comboboxCode: 'IS00353',
                        result: 'IS00356',
                        specialCd: 9
                    }, {
                        ctgCode: 'CS00034',
                        inpCode: 'IS00358',
                        comboboxCode: 'IS00360',
                        result: 'IS00363',
                        specialCd: 10
                    }, {
                        ctgCode: 'CS00049',
                        inpCode: 'IS00559',
                        comboboxCode: 'IS00561',
                        result: 'IS00564',
                        specialCd: 11
                    }, {
                        ctgCode: 'CS00050',
                        inpCode: 'IS00566',
                        comboboxCode: 'IS00568',
                        result: 'IS00571',
                        specialCd: 12
                    }, {
                        ctgCode: 'CS00051',
                        inpCode: 'IS00573',
                        comboboxCode: 'IS00575',
                        result: 'IS00578',
                        specialCd: 13
                    }, {
                        ctgCode: 'CS00052',
                        inpCode: 'IS00580',
                        comboboxCode: 'IS00582',
                        result: 'IS00585',
                        specialCd: 14
                    }, {
                        ctgCode: 'CS00053',
                        inpCode: 'IS00587',
                        comboboxCode: 'IS00589',
                        result: 'IS00592',
                        specialCd: 15
                    }, {
                        ctgCode: 'CS00054',
                        inpCode: 'IS00594',
                        comboboxCode: 'IS00596',
                        result: 'IS00599',
                        specialCd: 16
                    }, {
                        ctgCode: 'CS00055',
                        inpCode: 'IS00601',
                        comboboxCode: 'IS00603',
                        result: 'IS00606',
                        specialCd: 17
                    }, {
                        ctgCode: 'CS00056',
                        inpCode: 'IS00608',
                        comboboxCode: 'IS00610',
                        result: 'IS00613',
                        specialCd: 18
                    }, {
                        ctgCode: 'CS00057',
                        inpCode: 'IS00615',
                        comboboxCode: 'IS00617',
                        result: 'IS00620',
                        specialCd: 19
                    }, {
                        ctgCode: 'CS00058',
                        inpCode: 'IS00622',
                        comboboxCode: 'IS00624',
                        result: 'IS00627',
                        specialCd: 20
                    }
                ],

                validation = (specialLeaInfo: ISpeacialLeaInfo) => {
                    let inp: IFindData = finder.find(specialLeaInfo.ctgCode, specialLeaInfo.inpCode),
                        cbx: IFindData = finder.find(specialLeaInfo.ctgCode, specialLeaInfo.comboboxCode),
                        result: IFindData = finder.find(specialLeaInfo.ctgCode, specialLeaInfo.result);

                    if (inp && cbx) {
                        inp.data.value.subscribe(x => {
                            // obj để get dữ liệu
                            let sid = ko.toJS((((__viewContext || {}).viewModel || {}).employee || {}).employeeId),
                                grantDate = ko.toJS(inp.data.value),
                                appSet = ko.toJS(cbx.data.value),
                                specialLeaveCD = specialLeaInfo.specialCd;
                            // 

                            if (!sid) {
                                return;
                            }

                            let x = moment.utc(ko.toJS(inp.data.value));
                            if (x._isValid)
                                result.data.value(x.format('YYYY/MM/DD'));
                            else
                                result.data.value('');

                        });

                        cbx.data.value.subscribe(x => inp.data.value.valueHasMutated());
                        cbx.data.value.valueHasMutated();
                    }
                };

            _(specialLeaInfos).each(specialLeaInfo => validation(specialLeaInfo));
        }

        time_range = () => {
            let self = this;

            _(self.lstCls)
                .filter(c => c.items && _.isFunction(c.items))
                .filter(c => {
                    let items = _.filter(ko.toJS(c.items), t => t.item && t.item.dataTypeValue);

                    return _.filter(items, t => t.item.dataTypeValue == ITEM_SINGLE_TYPE.SEL_BUTTON).length == 2
                        && [2, 4].indexOf(_.filter(items, t => [ITEM_SINGLE_TYPE.TIME, ITEM_SINGLE_TYPE.TIMEPOINT].indexOf(t.item.dataTypeValue) > -1).length) > -1;
                })
                .map(c => c.items())
                .map(t => ({
                    btns: _.filter(t, m => m.item && m.item.dataTypeValue == ITEM_SINGLE_TYPE.SEL_BUTTON),
                    inps: _(t).filter(m => m.item && [ITEM_SINGLE_TYPE.TIME, ITEM_SINGLE_TYPE.TIMEPOINT].indexOf(m.item.dataTypeValue) > -1)
                        .groupBy(m => m.itemParentCode)
                        .map(g => g)
                        .value()
                }))
                .each(c => {
                    _.each(c.inps, group => {
                        if (c.btns.length == 2 && group.length == 2) {
                            let id1 = `#${group[0].itemDefId.replace(/[-_]/gi, '')}`,
                                id2 = `#${group[1].itemDefId.replace(/[-_]/gi, '')}`;

                            $(`${id1}, ${id2}`).on('blur', () => {
                                let ctrl1 = $(id1),
                                    ctrl2 = $(id2),
                                    hvl = _.filter(c.btns, b => !!b.value()),
                                    vl1 = ko.toJS(group[0].value),
                                    vl2 = ko.toJS(group[1].value),
                                    nnb = !_.isNumber(vl1) && !_.isNumber(vl2);

                                setTimeout(() => {
                                    if (hvl && nnb) {
                                        if (!ctrl1.is(':disabled') && !ctrl1.ntsError('hasError')) {
                                            ctrl1.ntsError('set', {
                                                messageId: "Msg_998",
                                                messageParams: [
                                                    group[0].itemName,
                                                    group[1].itemName
                                                ]
                                            });
                                        }

                                        if (!ctrl2.ntsError('hasError')) {
                                            ctrl2.parent().addClass('error');
                                        }
                                    } else {
                                        rmError(ctrl1, "Msg_435");
                                        if (!ctrl2.ntsError('hasError')) {
                                            ctrl2.parent().removeClass('error');
                                        }
                                    }
                                }, 50);
                            });
                        }
                    });
                });
        }

        haft_int = () => {
            let self = this,
                finder: IFinder = self.finder,
                haft_int: Array<IHaftInt> = [
                    //{
                    //'ctgCode': 'CS00035',
                    //'inpCode': 'IS00369'
                    //},
                    {
                        'ctgCode': 'CS00036',
                        'inpCode': 'IS00377'
                    },
                    {
                        'ctgCode': 'CS00036',
                        'inpCode': 'IS00378'
                    },
                    {
                        'ctgCode': 'CS00036',
                        'inpCode': 'IS00379'
                    },
                    {
                        'ctgCode': 'CS00036',
                        'inpCode': 'IS00382'
                    },
                    {
                        'ctgCode': 'CS00036',
                        'inpCode': 'IS00383'
                    },
                    {
                        'ctgCode': 'CS00036',
                        'inpCode': 'IS00384'
                    }
                ],
                validation = (haft: IHaftInt) => {
                    let ctrl: IFindData = finder.find(haft.ctgCode, haft.inpCode);

                    if (ctrl) {
                        (((__viewContext || {}).primitiveValueConstraints || {})[ctrl.id.replace(/#/g, '')] || {}).valueType = "HalfInt";
                    }
                };

            _.each(haft_int, h => validation(h));
        }
    }

    enum ITEM_SINGLE_TYPE {
        STRING = 1,
        NUMERIC = 2,
        DATE = 3,
        TIME = 4,
        TIMEPOINT = 5,
        SELECTION = 6,
        SEL_RADIO = 7,
        SEL_BUTTON = 8,
        READONLY = 9,
        RELATE_CATEGORY = 10,
        NUMBERIC_BUTTON = 11,
        READONLY_BUTTON = 12
    }

    // define ITEM_CLASSIFICATION_TYPE
    enum IT_CLA_TYPE {
        ITEM = <any>"ITEM", // single item
        LIST = <any>"LIST", // list item
        SPER = <any>"SeparatorLine" // line item
    }

    enum ITEM_TYPE {
        SET = 1,
        SINGLE = 2,
        SET_TABLE = 3
    }

    interface IValidation {
        radio: () => void;
        button: () => void;
        combobox: () => void;
    }

    interface IFinder {
        find: (categoryCode: string, subscribeCode: string) => IFindData;
        finds: (categoryCode: string, subscribesCode: Array<string>) => Array<IFindData>;
        findChilds: (categoryCode: string, parentCode: string) => Array<IFindData>;
    }

    interface IFindData {
        id: string;
        ctrl: JQuery;
        data: IItemData;
    }

    interface IItemData {
        'type': ITEM_TYPE;
        required: boolean;
        value: KnockoutObservable<any>;
        defValue: any;
        textValue: KnockoutObservable<any>;
        item: any;
        editable: KnockoutObservable<boolean>;
        numberedit: KnockoutObservable<boolean>;
        readonly: KnockoutObservable<boolean>;
        categoryCode: string;
        itemCode: string;
        lstComboBoxValue: KnockoutObservableArray<any>;
        itemParentCode?: string;
        itemName?: string;
    }

    interface IComboboxItem {
        optionText: string;
        optionValue: string;
    }

    interface IParentCodes {
        workTypeCodes: string;
        selectedWorkTypeCode: string;
        workTimeCodes: string;
        selectedWorkTimeCode: string
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

    interface IComboParam {
        comboBoxType: string;
        categoryId: string;
        required: boolean;
        standardDate: Date;
        typeCode: String;
        masterType: String;
        employeeId: string;
        cps002?: boolean;
        workplaceId: string;
    }

    interface INextTimeParam {
        employeeId: string;
        standardDate: Date;
        grantTable: string;
    }

    interface IGroupControl {
        ctgCode: string;
        workType?: string;
        workTime?: string;
        firstTimes?: ITimeRange;
        secondTimes?: ITimeRange;
    }

    interface ITimeRange {
        start: string;
        end: string;
    }

    interface ITimeFindData {
        start: IFindData;
        end: IFindData;
    }

    interface IGrandRadio {
        ctgCode: string;
        rdctCode?: string;
        radioCode: string;
        relateCode: Array<string>
    }

    interface IRelateRadio {
        ctgCode: string;
        radioCode: string;
        setParentCode: string;
    }

    interface IRelateButton {
        ctgCode: string;
        btnCode: string;
        dialogId: string;
        specialCd?: number;
        lblCode?: string;
    }

    interface ITimeTable {
        ctgCode: string;
        firstCode: string;
        secondCode: string;
        resultCode: string;
    }

    interface ISpeacialLeaInfo {
        ctgCode: string;
        inpCode: string;
        comboboxCode: string;
        result: string;
        specialCd: number;
    }

    interface IHaftInt {
        ctgCode: string;
        inpCode: string;
    }

    interface ISpeacialParam {
        sid: string;
        grantDate: Date;
        specialLeaveCD: number;
        appSet: number;
    }
}
