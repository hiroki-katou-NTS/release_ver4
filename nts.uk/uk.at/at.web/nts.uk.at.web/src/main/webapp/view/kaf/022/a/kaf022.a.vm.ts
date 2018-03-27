module nts.uk.at.view.kmf022 {
    import getText = nts.uk.resource.getText;
    import alert = nts.uk.ui.dialog.alert;
    import confirm = nts.uk.ui.dialog.confirm;
    import href = nts.uk.request.jump;
    import modal = nts.uk.ui.windows.sub.modal;

    let __viewContext: any = window["__viewContext"] || {};

    export module viewmodel {
        export class TabScreenModel {
            title: KnockoutObservable<string> = ko.observable('');
            removeAble: KnockoutObservable<boolean> = ko.observable(true);
            tabs: KnockoutObservableArray<TabModel> = ko.observableArray([
                new TabModel({ id: 'a', name: getText('Com_Company'), active: true }),
                new TabModel({ id: 'l', name: getText('Com_Employment') }),
                new TabModel({ id: 'm', name: getText('Com_Workplace') })

            ]);
            currentTab: KnockoutObservable<string> = ko.observable('a');

            //radio     

            constructor() {
                let self = this;
                //get use setting 
                self.tabs().map((t) => {
                    // set title for tab

                    if (t.active() == true) {
                        self.title(t.name);
                        self.changeTab(t);
                    }
                });
            }

            changeTab(tab: TabModel) {
                let self = this,
                    view: any = __viewContext.viewModel,
                    oldtab: TabModel = _.find(self.tabs(), t => t.active());

                // cancel action if tab self click
                if (oldtab.id == tab.id) {
                    return;
                }
                //set not display remove button first when change tab
                //__viewContext.viewModel.tabView.removeAble(false);
                tab.active(true);
                self.title(tab.name);

                self.tabs().map(t => (t.id != tab.id) && t.active(false));

                // call start function on view at here
                switch (tab.id) {
                    case 'a':
                        self.currentTab('a');
                        if (!!view.viewmodelA && typeof view.viewmodelA.start == 'function') {
                            view.viewmodelA.start();
                        }
                        break;
                    case 'l':
                        self.currentTab('l');
                        if (!!view.viewmodelL && typeof view.viewmodelL.start == 'function') {
                            view.viewmodelL.start();
                        }
                        break;
                    case 'm':
                        self.currentTab('m');
                        if (!!view.viewmodelM && typeof view.viewmodelM.start == 'function') {
                            view.viewmodelM.start();
                        }
                        break;
                }
            }
        }

        interface ITabModel {
            id: string;
            name: string;
            active?: boolean;
            display?: boolean;
        }

        class TabModel {
            id: string;
            name: string;
            active: KnockoutObservable<boolean> = ko.observable(false);
            display: KnockoutObservable<boolean> = ko.observable(true);

            constructor(param: ITabModel) {
                this.id = param.id;
                this.name = param.name;
                this.active(param.active || false);
                this.display(param.display || true);
            }

            changeTab() {
                // call parent view action
                __viewContext.viewModel.tabView.changeTab(this);
            }
        }
    }

    export module a.viewmodel {
        export class ScreenModel {
            companyId: KnockoutObservable<string>;
            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;
            enableA4_6: KnockoutObservable<boolean>;
            itemListA4_7: KnockoutObservableArray<ItemModel>;
            itemListA4_8: KnockoutObservableArray<ItemModel>;
            selectedCodeA4_7: KnockoutObservable<number>;
            selectedCodeA4_8: KnockoutObservable<number>;
            isEnable: KnockoutObservable<boolean>;
            isEditable: KnockoutObservable<boolean>;
            dataA4Display: KnockoutObservableArray<ItemA4>;
            sizeArrayA4: KnockoutObservable<number>;

            //a5
            itemListA5_14: KnockoutObservableArray<any>;
            selectedIdA5_14: KnockoutObservable<number>;
            enableA5_14: KnockoutObservable<boolean>;
            itemListA5_16: KnockoutObservableArray<any>;
            selectedCodeA5_16: KnockoutObservable<number>;
            selectedIdA5_18: KnockoutObservable<number>;
            selectedIdA5_19: KnockoutObservable<number>;
            selectedIdA5_20: KnockoutObservable<number>;
            selectedIdA5_21: KnockoutObservable<number>;
            selectedIdA5_22: KnockoutObservable<number>;
            selectedIdA5_23: KnockoutObservable<number>;
            itemListA5_24: KnockoutObservableArray<any>;
            selectedIdA5_24: KnockoutObservable<number>;
            selectedIdA5_25: KnockoutObservable<number>;
            itemListA17_4: KnockoutObservableArray<any>;
            itemListA17_5: KnockoutObservableArray<any>;
            selectedIdA17_4: KnockoutObservable<number>;
            selectedIdA17_5: KnockoutObservable<number>;
            //a6
            listDataA6: KnockoutObservableArray<any>;
            //a7
            listDataA7: KnockoutObservableArray<any>;
            enableA7_17: KnockoutObservable<boolean>;
            enableA7_26: KnockoutObservable<boolean>;
            enableA7_31: KnockoutObservable<boolean>;
            enableA7_32: KnockoutObservable<boolean>;
            enableA7_33: KnockoutObservable<boolean>;
            enableA7_34: KnockoutObservable<boolean>;
            enableA7_35: KnockoutObservable<boolean>;
            enableA7_36: KnockoutObservable<boolean>;
            enableA7_37: KnockoutObservable<boolean>;
            enableA7_38: KnockoutObservable<boolean>;
            enableA7_39: KnockoutObservable<boolean>;
            enableA7_40: KnockoutObservable<boolean>;
            enableA7_41: KnockoutObservable<boolean>;
            selectedValueA7_18: KnockoutObservable<number>;
            itemListA7_20: KnockoutObservableArray<any>;
            selectedCodeA7_20: KnockoutObservable<number>;
            timeeditor: any;
            enableA7_25: KnockoutObservable<boolean>;
            selectedCodeA7_28: KnockoutObservable<number>;
            enableA7_30: KnockoutObservable<boolean>;
            enableA7_30_1: KnockoutObservable<boolean>;
            enableA7_30_2: KnockoutObservable<boolean>;
            enableA7_30_3: KnockoutObservable<boolean>;
            enableA7_30_4: KnockoutObservable<boolean>;
            enableA7_30_5: KnockoutObservable<boolean>;
            enableA7_30_6: KnockoutObservable<boolean>;
            enableA7_30_7: KnockoutObservable<boolean>;
            enableA7_30_8: KnockoutObservable<boolean>;
            enableA7_30_9: KnockoutObservable<boolean>;
            enableA7_30_10: KnockoutObservable<boolean>;
            enableA7_30_11: KnockoutObservable<boolean>;
            selectedCodeA7_31: KnockoutObservable<number>;
            selectedCodeA7_32: KnockoutObservable<number>;
            selectedCodeA7_33: KnockoutObservable<number>;
            selectedCodeA7_34: KnockoutObservable<number>;
            selectedCodeA7_35: KnockoutObservable<number>;
            selectedCodeA7_36: KnockoutObservable<number>;
            selectedCodeA7_37: KnockoutObservable<number>;
            selectedCodeA7_38: KnockoutObservable<number>;
            selectedCodeA7_39: KnockoutObservable<number>;
            selectedCodeA7_40: KnockoutObservable<number>;
            selectedCodeA7_41: KnockoutObservable<number>;
            //a8
            listDataA8: KnockoutObservableArray<any>;
            enableA8_29: KnockoutObservable<boolean>;
            enableA8_35: KnockoutObservable<boolean>;
            enableA8_36: KnockoutObservable<boolean>;
            enableA8_37: KnockoutObservable<boolean>;
            enableA8_38: KnockoutObservable<boolean>;
            enableA8_39: KnockoutObservable<boolean>;
            enableA8_40: KnockoutObservable<boolean>;
            enableA8_41: KnockoutObservable<boolean>;
            enableA8_42: KnockoutObservable<boolean>;
            enableA8_43: KnockoutObservable<boolean>;
            enableA8_44: KnockoutObservable<boolean>;
            enableA8_45: KnockoutObservable<boolean>;
            enableA8_46: KnockoutObservable<boolean>;
            enableA8_47: KnockoutObservable<boolean>;
            enableA8_48: KnockoutObservable<boolean>;
            enableA8_49: KnockoutObservable<boolean>;
            enableA8_50: KnockoutObservable<boolean>;
            enableA8_51: KnockoutObservable<boolean>;
            enableA8_52: KnockoutObservable<boolean>;
            enableA8_53: KnockoutObservable<boolean>;
            enableA8_54: KnockoutObservable<boolean>;

            enableA8_30: KnockoutObservable<boolean>;
            enableA8_30_1: KnockoutObservable<boolean>;
            enableA8_30_2: KnockoutObservable<boolean>;
            enableA8_30_3: KnockoutObservable<boolean>;
            enableA8_30_4: KnockoutObservable<boolean>;
            enableA8_30_5: KnockoutObservable<boolean>;
            enableA8_30_6: KnockoutObservable<boolean>;
            enableA8_30_7: KnockoutObservable<boolean>;
            enableA8_30_8: KnockoutObservable<boolean>;
            enableA8_30_9: KnockoutObservable<boolean>;
            enableA8_30_10: KnockoutObservable<boolean>;
            enableA8_30_11: KnockoutObservable<boolean>;
            enableA8_30_12: KnockoutObservable<boolean>;
            enableA8_30_13: KnockoutObservable<boolean>;
            enableA8_30_14: KnockoutObservable<boolean>;
            enableA8_30_15: KnockoutObservable<boolean>;
            enableA8_30_16: KnockoutObservable<boolean>;
            enableA8_30_17: KnockoutObservable<boolean>;
            enableA8_30_18: KnockoutObservable<boolean>;
            enableA8_30_19: KnockoutObservable<boolean>;
            enableA8_30_20: KnockoutObservable<boolean>;

            enableA8_31: KnockoutObservable<boolean>;
            enableA8_31_1: KnockoutObservable<boolean>;
            enableA8_31_2: KnockoutObservable<boolean>;
            enableA8_31_3: KnockoutObservable<boolean>;
            enableA8_31_4: KnockoutObservable<boolean>;
            enableA8_31_5: KnockoutObservable<boolean>;
            enableA8_31_6: KnockoutObservable<boolean>;
            enableA8_31_7: KnockoutObservable<boolean>;
            enableA8_31_8: KnockoutObservable<boolean>;
            enableA8_31_9: KnockoutObservable<boolean>;
            enableA8_31_10: KnockoutObservable<boolean>;
            enableA8_31_11: KnockoutObservable<boolean>;
            enableA8_31_12: KnockoutObservable<boolean>;
            enableA8_31_13: KnockoutObservable<boolean>;
            enableA8_31_14: KnockoutObservable<boolean>;
            enableA8_31_15: KnockoutObservable<boolean>;
            enableA8_31_16: KnockoutObservable<boolean>;
            enableA8_31_17: KnockoutObservable<boolean>;
            enableA8_31_18: KnockoutObservable<boolean>;
            enableA8_31_19: KnockoutObservable<boolean>;
            enableA8_31_20: KnockoutObservable<boolean>;

            enableA8_32: KnockoutObservable<boolean>;
            enableA8_32_1: KnockoutObservable<boolean>;
            enableA8_32_2: KnockoutObservable<boolean>;
            enableA8_32_3: KnockoutObservable<boolean>;
            enableA8_32_4: KnockoutObservable<boolean>;
            enableA8_32_5: KnockoutObservable<boolean>;
            enableA8_32_6: KnockoutObservable<boolean>;
            enableA8_32_7: KnockoutObservable<boolean>;
            enableA8_32_8: KnockoutObservable<boolean>;
            enableA8_32_9: KnockoutObservable<boolean>;
            enableA8_32_10: KnockoutObservable<boolean>;
            enableA8_32_11: KnockoutObservable<boolean>;
            enableA8_32_12: KnockoutObservable<boolean>;
            enableA8_32_13: KnockoutObservable<boolean>;
            enableA8_32_14: KnockoutObservable<boolean>;
            enableA8_32_15: KnockoutObservable<boolean>;
            enableA8_32_16: KnockoutObservable<boolean>;
            enableA8_32_17: KnockoutObservable<boolean>;
            enableA8_32_18: KnockoutObservable<boolean>;
            enableA8_32_19: KnockoutObservable<boolean>;
            enableA8_32_20: KnockoutObservable<boolean>;

            enableA8_34: KnockoutObservable<boolean>;
            enableA8_34_1: KnockoutObservable<boolean>;
            enableA8_34_2: KnockoutObservable<boolean>;
            enableA8_34_3: KnockoutObservable<boolean>;
            enableA8_34_4: KnockoutObservable<boolean>;
            enableA8_34_5: KnockoutObservable<boolean>;
            enableA8_34_6: KnockoutObservable<boolean>;
            enableA8_34_7: KnockoutObservable<boolean>;
            enableA8_34_8: KnockoutObservable<boolean>;
            enableA8_34_9: KnockoutObservable<boolean>;
            enableA8_34_10: KnockoutObservable<boolean>;
            enableA8_34_11: KnockoutObservable<boolean>;
            enableA8_34_12: KnockoutObservable<boolean>;
            enableA8_34_13: KnockoutObservable<boolean>;
            enableA8_34_14: KnockoutObservable<boolean>;
            enableA8_34_15: KnockoutObservable<boolean>;
            enableA8_34_16: KnockoutObservable<boolean>;
            enableA8_34_17: KnockoutObservable<boolean>;
            enableA8_34_18: KnockoutObservable<boolean>;
            enableA8_34_19: KnockoutObservable<boolean>;
            enableA8_34_20: KnockoutObservable<boolean>;

            itemListA8_33: KnockoutObservableArray<ItemModel>;
            selectedCodeA8_33: KnockoutObservable<number>;
            selectedCodeA8_33_1: KnockoutObservable<number>;
            selectedCodeA8_33_2: KnockoutObservable<number>;
            selectedCodeA8_33_3: KnockoutObservable<number>;
            selectedCodeA8_33_4: KnockoutObservable<number>;
            selectedCodeA8_33_5: KnockoutObservable<number>;
            selectedCodeA8_33_6: KnockoutObservable<number>;
            selectedCodeA8_33_7: KnockoutObservable<number>;
            selectedCodeA8_33_8: KnockoutObservable<number>;
            selectedCodeA8_33_9: KnockoutObservable<number>;
            selectedCodeA8_33_10: KnockoutObservable<number>;
            selectedCodeA8_33_11: KnockoutObservable<number>;
            selectedCodeA8_33_12: KnockoutObservable<number>;
            selectedCodeA8_33_13: KnockoutObservable<number>;
            selectedCodeA8_33_14: KnockoutObservable<number>;
            selectedCodeA8_33_15: KnockoutObservable<number>;
            selectedCodeA8_33_16: KnockoutObservable<number>;
            selectedCodeA8_33_17: KnockoutObservable<number>;
            selectedCodeA8_33_18: KnockoutObservable<number>;
            selectedCodeA8_33_19: KnockoutObservable<number>;
            selectedCodeA8_33_20: KnockoutObservable<number>;

            //a9
            selectedIdA9_5: KnockoutObservable<number>;
            selectedIdA9_8: KnockoutObservable<number>;
            selectedIdA9_9: KnockoutObservable<number>;
            itemListA9_5: KnockoutObservableArray<ItemModel>;
            itemListA9_9: KnockoutObservableArray<ItemModel>;

            //a10
            itemListA10_3: KnockoutObservableArray<ItemModel>;
            selectedIdA10_3: KnockoutObservable<number>;

            //a11
            itemListA11_8: KnockoutObservableArray<ItemModel>;
            selectedIdA11_8: KnockoutObservable<number>;
            selectedIdA11_9: KnockoutObservable<number>;
            selectedIdA11_10: KnockoutObservable<number>;
            selectedIdA11_11: KnockoutObservable<number>;
            selectedIdA11_12: KnockoutObservable<number>;
            selectedIdA11_13: KnockoutObservable<number>;

            //a12
            itemListA12_5: KnockoutObservableArray<ItemModel>;
            selectedIdA12_5: KnockoutObservable<number>;
            itemListA12_6: KnockoutObservableArray<ItemModel>;
            selectedIdA12_6: KnockoutObservable<number>;
            itemListA12_7: KnockoutObservableArray<ItemModel>;
            selectedIdA12_7: KnockoutObservable<number>;

            //a13
            textEditorA13_4: KnockoutObservable<any>;
            listDataA13: KnockoutObservableArray<number>;

            //a14
            itemListA14_3: KnockoutObservableArray<ItemModel>;
            selectedIdA14_3: KnockoutObservable<number>;
            //a15
            itemListA15_4: KnockoutObservableArray<ItemModel>;
            listDataA15: KnockoutObservableArray<ItemA15>;

            //a16
            texteditorA16_7: any;
            texteditorA16_8: any;
            texteditorA16_9: any;
            texteditorA16_10: any;
            texteditorA16_11: any;

            //b
            itemListB18: KnockoutObservableArray<ItemModel>;
            selectedIdB18: KnockoutObservable<number>;
            itemListB19: KnockoutObservableArray<ItemModel>;
            selectedIdB19: KnockoutObservable<number>;
            itemListB21: KnockoutObservableArray<ItemModel>;
            selectedIdB21: KnockoutObservable<number>;
            selectedIdB23: KnockoutObservable<number>;
            selectedIdB25: KnockoutObservable<number>;
            selectedIdB27: KnockoutObservable<number>;
            selectedIdB29: KnockoutObservable<number>;
            selectedIdB31: KnockoutObservable<number>;
            selectedIdB32: KnockoutObservable<number>;
            selectedIdB34: KnockoutObservable<number>;
            selectedIdB35: KnockoutObservable<number>;
            selectedIdB36: KnockoutObservable<number>;
            itemListB30: KnockoutObservableArray<ItemModel>;
            selectedCodeB30: KnockoutObservable<number>;
            itemListB33: KnockoutObservableArray<ItemModel>;
            selectedCodeB33: KnockoutObservable<number>;
            //i
            selectedIdI4: KnockoutObservable<number>;
            itemListI4: KnockoutObservableArray<ItemModel>;
            //c
            itemListC27: KnockoutObservableArray<ItemModel>;
            selectedIdC27: KnockoutObservable<number>;
            itemListC28: KnockoutObservableArray<ItemModel>;
            selectedIdC28: KnockoutObservable<number>;
            itemListC29: KnockoutObservableArray<ItemModel>;
            selectedIdC29: KnockoutObservable<number>;
            itemListC30: KnockoutObservableArray<ItemModel>;
            selectedIdC30: KnockoutObservable<number>;
            selectedIdC31: KnockoutObservable<number>;
            selectedIdC32: KnockoutObservable<number>;
            selectedIdC33: KnockoutObservable<number>;
            selectedIdC34: KnockoutObservable<number>;
            selectedIdC35: KnockoutObservable<number>;
            selectedIdC36: KnockoutObservable<number>;
            selectedIdC37: KnockoutObservable<number>;
            selectedIdC38: KnockoutObservable<number>;
            selectedIdC39: KnockoutObservable<number>;
            selectedIdC40: KnockoutObservable<number>;
            selectedIdC48: KnockoutObservable<number>;
            selectedIdC49: KnockoutObservable<number>;
            texteditorC41: any;
            texteditorC42: any;
            texteditorC43: any;
            texteditorC44: any;
            texteditorC45: any;
            texteditorC46: any;
            texteditorC47: any;
            texteditorC51: any;
            //d
            itemListD8: KnockoutObservableArray<ItemModel>;
            selectedIdD8: KnockoutObservable<number>;
            valueD10: KnockoutObservable<string>;
            enableD11: KnockoutObservable<boolean>;
            texteditorD9: any;
            valueD10_1: KnockoutObservable<string>;
            enableD11_1: KnockoutObservable<boolean>;
            texteditorD12: any;
            selectedValueD13: KnockoutObservable<number>;
            itemListD15: KnockoutObservableArray<ItemModel>;
            selectedIdD15: KnockoutObservable<number>;
            enableD15: KnockoutObservable<boolean>;
            selectedIdD16: KnockoutObservable<number>;

            //e
            itemListE9: KnockoutObservableArray<ItemModel>;
            selectedIdE9: KnockoutObservable<number>;
            itemListE10: KnockoutObservableArray<ItemModel>;
            selectedIdE10: KnockoutObservable<number>;
            selectedValueE11: KnockoutObservable<number>;
            enableE11_5: KnockoutObservable<boolean>;
            checkedE11_5: KnockoutObservable<boolean>;
            itemListE12: KnockoutObservableArray<ItemModel>;
            selectedIdE12: KnockoutObservable<number>;
            texteditorE13: any;
            valueE14: KnockoutObservable<string>;
            enableE15: KnockoutObservable<boolean>;
            texteditorE16: any;
            valueE17: KnockoutObservable<string>;
            enableE18: KnockoutObservable<boolean>;

            //f
            selectedIdF10: KnockoutObservable<number>;
            selectedIdF11: KnockoutObservable<number>;
            selectedIdF12: KnockoutObservable<number>;
            selectedValueF13: KnockoutObservable<number>;
            checkedF13_1: KnockoutObservable<boolean>;
            enableF13_1: KnockoutObservable<boolean>;
            selectedIdF14: KnockoutObservable<number>;
            texteditorF15: any;
            valueF15_1: KnockoutObservable<string>;
            enableF15_2: KnockoutObservable<boolean>;
            texteditorF16: any;
            valueF16_1: KnockoutObservable<string>;
            enableF16_1: KnockoutObservable<boolean>;
            itemListF11: KnockoutObservableArray<ItemModel>;

            //g
            itemListG16: KnockoutObservableArray<ItemModel>;
            selectedIdG16: KnockoutObservable<number>;
            itemListG18: KnockoutObservableArray<ItemModel>;
            selectedIdG18: KnockoutObservable<number>;
            selectedIdG20: KnockoutObservable<number>;
            selectedIdG22: KnockoutObservable<number>;
            itemListG23: KnockoutObservableArray<ItemModel>;
            selectedIdG23: KnockoutObservable<number>;
            itemListG24: KnockoutObservableArray<ItemModel>;
            selectedIdG24: KnockoutObservable<number>;
            itemListG25: KnockoutObservableArray<ItemModel>;
            selectedIdG25: KnockoutObservable<number>;
            selectedIdG26: KnockoutObservable<number>;
            selectedIdG27: KnockoutObservable<number>;
            selectedIdG28: KnockoutObservable<number>;
            selectedIdG29: KnockoutObservable<number>;

            //h
            itemListH15: KnockoutObservableArray<ItemModel>;
            selectedIdH15: KnockoutObservable<number>;
            selectedIdH16: KnockoutObservable<number>;
            selectedIdH17: KnockoutObservable<number>;
            selectedIdH18: KnockoutObservable<number>;
            selectedIdH19: KnockoutObservable<number>;
            selectedIdH20: KnockoutObservable<number>;
            texteditorH22: any;
            texteditorH23_1: any;
            texteditorH24_1: any;
            texteditorH25_1: any;
            texteditorH26_1: any;
            texteditorH27_1: any;
            enableH21: KnockoutObservable<boolean>;
            enableH23: KnockoutObservable<boolean>;
            enableH24: KnockoutObservable<boolean>;
            enableH25: KnockoutObservable<boolean>;
            enableH26: KnockoutObservable<boolean>;
            enableH27: KnockoutObservable<boolean>;
            //j
            itemListJ18: KnockoutObservableArray<ItemModel>;
            selectedCodeJ18: KnockoutObservable<number>;
            itemListJ19: KnockoutObservableArray<ItemModel>;
            selectedIdJ19: KnockoutObservable<number>;
            itemListJ20: KnockoutObservableArray<ItemModel>;
            selectedIdJ20: KnockoutObservable<number>;
            selectedIdJ21: KnockoutObservable<number>;
            selectedIdJ22: KnockoutObservable<number>;
            selectedIdJ23: KnockoutObservable<number>;
            selectedIdJ24: KnockoutObservable<number>;
            selectedIdJ25: KnockoutObservable<number>;
            selectedIdJ26: KnockoutObservable<number>;
            selectedIdJ27: KnockoutObservable<number>;
            selectedIdJ28: KnockoutObservable<number>;
            texteditorJ29: any;
            valueJ30: KnockoutObservable<string>;
            enableJ31: KnockoutObservable<boolean>;
            texteditorJ32: any;
            valueJ30_1: KnockoutObservable<string>;
            enableJ31_1: KnockoutObservable<boolean>;
            //k
            itemListK12: KnockoutObservableArray<ItemModel>;
            selectedIdK12: KnockoutObservable<number>;
            itemListK13: KnockoutObservableArray<ItemModel>;
            selectedIdK13: KnockoutObservable<number>;
            itemListK14: KnockoutObservableArray<ItemModel>;
            selectedIdK14: KnockoutObservable<number>;
            selectedIdK15: KnockoutObservable<number>;
            itemListK16: KnockoutObservableArray<ItemModel>;
            selectedIdK16: KnockoutObservable<number>;
            texteditorK17: any;
            valueK18: KnockoutObservable<string>;
            enableK19: KnockoutObservable<boolean>;
            texteditorK20: any;
            valueK18_1: KnockoutObservable<string>;
            enableK19_1: KnockoutObservable<boolean>;
            selectedIdK21: KnockoutObservable<number>;
            itemListK22: KnockoutObservableArray<ItemModel>;
            selectedIdK22: KnockoutObservable<number>;

            //appliSet 
            baseDateFlg: KnockoutObservable<number>;
            advanceExcessMessDispAtr: KnockoutObservable<number>;
            hwAdvanceDispAtr: KnockoutObservable<number>;
            hwActualDispAtr: KnockoutObservable<number>;
            actualExcessMessDispAtr: KnockoutObservable<number>;
            otAdvanceDispAtr: KnockoutObservable<number>;
            otActualDispAtr: KnockoutObservable<number>;
            warningDateDispAtr: KnockoutObservable<number>;
            appReasonDispAtr: KnockoutObservable<number>;
            scheReflectFlg: KnockoutObservable<number>;
            priorityTimeReflectFlg: KnockoutObservable<number>;
            constructor() {
                let self = this;
                self.companyId = ko.observable('');
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: nts.uk.resource.getText('KAF022_2'), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: nts.uk.resource.getText('KAF022_3'), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-3', title: nts.uk.resource.getText('KAF022_4'), content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-4', title: nts.uk.resource.getText('KAF022_5'), content: '.tab-content-4', enable: ko.observable(true), visible: ko.observable(true) },

                    { id: 'tab-5', title: nts.uk.resource.getText('KAF022_6'), content: '.tab-content-5', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-6', title: nts.uk.resource.getText('KAF022_7'), content: '.tab-content-6', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-7', title: nts.uk.resource.getText('KAF022_8'), content: '.tab-content-7', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-8', title: nts.uk.resource.getText('KAF022_9'), content: '.tab-content-8', enable: ko.observable(true), visible: ko.observable(true) },

                    { id: 'tab-9', title: nts.uk.resource.getText('KAF022_10'), content: '.tab-content-9', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-10', title: nts.uk.resource.getText('KAF022_11'), content: '.tab-content-10', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-11', title: nts.uk.resource.getText('KAF022_12'), content: '.tab-content-11', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-12', title: nts.uk.resource.getText('KAF022_13'), content: '.tab-content-12', enable: ko.observable(true), visible: ko.observable(true) }
                ]);
                self.selectedTab = ko.observable('tab-1');
                //a4
                $("#fixed-table-a4").ntsFixedTable({});
                $("#fixed-table-a5").ntsFixedTable({});
                $("#fixed-table-a17").ntsFixedTable({});
                $("#fixed-table-a6").ntsFixedTable({});
                $("#fixed-table-a7").ntsFixedTable({});
                $("#fixed-table-a8").ntsFixedTable({});
                $("#fixed-table-a9").ntsFixedTable({});
                $("#fixed-table-a10").ntsFixedTable({});
                $("#fixed-table-a11").ntsFixedTable({});
                $("#fixed-table-a12").ntsFixedTable({});
                $("#fixed-table-a13").ntsFixedTable({});
                $("#fixed-table-a14").ntsFixedTable({});
                $("#fixed-table-a15").ntsFixedTable({});
                $("#fixed-table-a16").ntsFixedTable({});
                $("#fixed-table-b").ntsFixedTable({});
                $("#fixed-table-c").ntsFixedTable({});
                $("#fixed-table-d").ntsFixedTable({});
                $("#fixed-table-e").ntsFixedTable({});
                $("#fixed-table-f").ntsFixedTable({});
                $("#fixed-table-g").ntsFixedTable({});
                $("#fixed-table-h").ntsFixedTable({});
                $("#fixed-table-i").ntsFixedTable({});
                $("#fixed-table-j").ntsFixedTable({});
                $("#fixed-table-k").ntsFixedTable({});
                $("#fixed-table-l").ntsFixedTable({});
                self.enableA4_6 = ko.observable(false);

                self.itemListA4_7 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_321')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_322')),
                ]);
                self.itemListA4_8 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_323')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_324')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_325')),
                    new ItemModel(3, nts.uk.resource.getText('KAF022_326')),
                    new ItemModel(4, nts.uk.resource.getText('KAF022_327')),
                    new ItemModel(5, nts.uk.resource.getText('KAF022_328')),
                    new ItemModel(6, nts.uk.resource.getText('KAF022_329')),
                    new ItemModel(7, nts.uk.resource.getText('KAF022_330')),
                    new ItemModel(8, nts.uk.resource.getText('KAF022_331')),
                    new ItemModel(9, nts.uk.resource.getText('KAF022_332')),
                    new ItemModel(10, nts.uk.resource.getText('KAF022_333')),
                    new ItemModel(11, nts.uk.resource.getText('KAF022_334')),
                    new ItemModel(12, nts.uk.resource.getText('KAF022_335')),
                    new ItemModel(13, nts.uk.resource.getText('KAF022_336')),
                    new ItemModel(14, nts.uk.resource.getText('KAF022_337')),
                    new ItemModel(15, nts.uk.resource.getText('KAF022_338')),
                    new ItemModel(16, nts.uk.resource.getText('KAF022_339')),
                    new ItemModel(17, nts.uk.resource.getText('KAF022_340')),
                    new ItemModel(18, nts.uk.resource.getText('KAF022_341')),
                    new ItemModel(19, nts.uk.resource.getText('KAF022_342')),
                    new ItemModel(20, nts.uk.resource.getText('KAF022_343')),
                    new ItemModel(21, nts.uk.resource.getText('KAF022_344')),
                    new ItemModel(22, nts.uk.resource.getText('KAF022_345')),
                    new ItemModel(23, nts.uk.resource.getText('KAF022_346')),
                    new ItemModel(24, nts.uk.resource.getText('KAF022_347')),
                    new ItemModel(25, nts.uk.resource.getText('KAF022_348')),
                    new ItemModel(26, nts.uk.resource.getText('KAF022_349')),
                    new ItemModel(27, nts.uk.resource.getText('KAF022_350')),
                    new ItemModel(28, nts.uk.resource.getText('KAF022_351')),
                    new ItemModel(29, nts.uk.resource.getText('KAF022_352')),
                    new ItemModel(30, nts.uk.resource.getText('KAF022_353')),
                    new ItemModel(31, nts.uk.resource.getText('KAF022_354'))
                ]);
                self.selectedCodeA4_7 = ko.observable(0);
                self.selectedCodeA4_8 = ko.observable(0);
                self.isEnable = ko.observable(true);
                self.isEditable = ko.observable(false);
                self.dataA4Display = ko.observableArray([]);
                self.sizeArrayA4 = ko.observable(0);

                //a5
                self.itemListA5_14 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_36')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_37'))
                ]);
                self.selectedIdA5_14 = ko.observable(0);
                self.enableA5_14 = ko.observable(true);
                self.itemListA5_16 = ko.observableArray([
                    new ItemModel(1, '0'),
                    new ItemModel(2, '1'),
                    new ItemModel(3, '2'),
                    new ItemModel(4, '3'),
                    new ItemModel(5, '4'),
                    new ItemModel(6, '5'),
                    new ItemModel(7, '6'),
                ]);
                self.selectedCodeA5_16 = ko.observable(1);
                self.selectedIdA5_18 = ko.observable(0);
                self.selectedIdA5_19 = ko.observable(0);
                self.selectedIdA5_20 = ko.observable(0);
                self.selectedIdA5_21 = ko.observable(0);
                self.selectedIdA5_22 = ko.observable(0);
                self.selectedIdA5_23 = ko.observable(0);
                self.itemListA5_24 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_42')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_43')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_44'))
                ]);
                self.selectedIdA5_24 = ko.observable(0);
                self.selectedIdA5_25 = ko.observable(0);

                //a17
                self.itemListA17_4 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82')),
                ]);
                self.selectedIdA17_4 = ko.observable(0);
                self.itemListA17_5 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82')),
                ]);
                self.selectedIdA17_5 = ko.observable(0);
                //a6
                self.listDataA6 = ko.observableArray([]);

                //a7
                self.listDataA7 = ko.observableArray([]);
                self.enableA7_17 = ko.observable(false);
                self.enableA7_26 = ko.observable(false);
                self.enableA7_31 = ko.observable(false);
                self.enableA7_32 = ko.observable(false);
                self.enableA7_33 = ko.observable(false);
                self.enableA7_34 = ko.observable(false);
                self.enableA7_35 = ko.observable(false);
                self.enableA7_36 = ko.observable(false);
                self.enableA7_37 = ko.observable(false);
                self.enableA7_38 = ko.observable(false);
                self.enableA7_39 = ko.observable(false);
                self.enableA7_40 = ko.observable(false);
                self.enableA7_41 = ko.observable(false);

                self.selectedValueA7_18 = ko.observable(0);
                self.itemListA7_20 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_323')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_357')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_358')),
                    new ItemModel(3, nts.uk.resource.getText('KAF022_359')),
                    new ItemModel(4, nts.uk.resource.getText('KAF022_360')),
                    new ItemModel(5, nts.uk.resource.getText('KAF022_361')),
                    new ItemModel(6, nts.uk.resource.getText('KAF022_362')),
                    new ItemModel(7, nts.uk.resource.getText('KAF022_363')),
                ]);
                self.selectedCodeA7_20 = ko.observable(0);
                self.timeeditor = {
                    value: ko.observable(120),
                    constraint: 'SampleTimeDuration',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TimeEditorOption({
                        inputFormat: 'time'
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.enableA7_25 = ko.observable(false);
                self.selectedCodeA7_28 = ko.observable(0);
                self.enableA7_30 = ko.observable(false);
                self.enableA7_30_1 = ko.observable(false);
                self.enableA7_30_2 = ko.observable(false);
                self.enableA7_30_3 = ko.observable(false);
                self.enableA7_30_4 = ko.observable(false);
                self.enableA7_30_5 = ko.observable(false);
                self.enableA7_30_6 = ko.observable(false);
                self.enableA7_30_7 = ko.observable(false);
                self.enableA7_30_8 = ko.observable(false);
                self.enableA7_30_9 = ko.observable(false);
                self.enableA7_30_10 = ko.observable(false);
                self.enableA7_30_11 = ko.observable(false);
                self.selectedCodeA7_31 = ko.observable(0);
                self.selectedCodeA7_32 = ko.observable(0);
                self.selectedCodeA7_33 = ko.observable(0);
                self.selectedCodeA7_34 = ko.observable(0);
                self.selectedCodeA7_35 = ko.observable(0);
                self.selectedCodeA7_36 = ko.observable(0);
                self.selectedCodeA7_37 = ko.observable(0);
                self.selectedCodeA7_38 = ko.observable(0);
                self.selectedCodeA7_39 = ko.observable(0);
                self.selectedCodeA7_40 = ko.observable(0);
                self.selectedCodeA7_41 = ko.observable(0);
                //a8
                self.listDataA8 = ko.observableArray([]);
                self.enableA8_29 = ko.observable(false);
                self.enableA8_35 = ko.observable(false);
                self.enableA8_36 = ko.observable(false);
                self.enableA8_37 = ko.observable(false);
                self.enableA8_38 = ko.observable(false);
                self.enableA8_39 = ko.observable(false);
                self.enableA8_40 = ko.observable(false);
                self.enableA8_41 = ko.observable(false);
                self.enableA8_42 = ko.observable(false);
                self.enableA8_43 = ko.observable(false);
                self.enableA8_44 = ko.observable(false);
                self.enableA8_45 = ko.observable(false);
                self.enableA8_46 = ko.observable(false);
                self.enableA8_47 = ko.observable(false);
                self.enableA8_48 = ko.observable(false);
                self.enableA8_49 = ko.observable(false);
                self.enableA8_50 = ko.observable(false);
                self.enableA8_51 = ko.observable(false);
                self.enableA8_52 = ko.observable(false);
                self.enableA8_53 = ko.observable(false);
                self.enableA8_54 = ko.observable(false);

                self.enableA8_30 = ko.observable(false);
                self.enableA8_30_1 = ko.observable(false);
                self.enableA8_30_2 = ko.observable(false);
                self.enableA8_30_3 = ko.observable(false);
                self.enableA8_30_4 = ko.observable(false);
                self.enableA8_30_5 = ko.observable(false);
                self.enableA8_30_6 = ko.observable(false);
                self.enableA8_30_7 = ko.observable(false);
                self.enableA8_30_8 = ko.observable(false);
                self.enableA8_30_9 = ko.observable(false);
                self.enableA8_30_10 = ko.observable(false);
                self.enableA8_30_11 = ko.observable(false);
                self.enableA8_30_12 = ko.observable(false);
                self.enableA8_30_13 = ko.observable(false);
                self.enableA8_30_14 = ko.observable(false);
                self.enableA8_30_15 = ko.observable(false);
                self.enableA8_30_16 = ko.observable(false);
                self.enableA8_30_17 = ko.observable(false);
                self.enableA8_30_18 = ko.observable(false);
                self.enableA8_30_19 = ko.observable(false);
                self.enableA8_30_20 = ko.observable(false);

                self.enableA8_31 = ko.observable(false);
                self.enableA8_31_1 = ko.observable(false);
                self.enableA8_31_2 = ko.observable(false);
                self.enableA8_31_3 = ko.observable(false);
                self.enableA8_31_4 = ko.observable(false);
                self.enableA8_31_5 = ko.observable(false);
                self.enableA8_31_6 = ko.observable(false);
                self.enableA8_31_7 = ko.observable(false);
                self.enableA8_31_8 = ko.observable(false);
                self.enableA8_31_9 = ko.observable(false);
                self.enableA8_31_10 = ko.observable(false);
                self.enableA8_31_11 = ko.observable(false);
                self.enableA8_31_12 = ko.observable(false);
                self.enableA8_31_13 = ko.observable(false);
                self.enableA8_31_14 = ko.observable(false);
                self.enableA8_31_15 = ko.observable(false);
                self.enableA8_31_16 = ko.observable(false);
                self.enableA8_31_17 = ko.observable(false);
                self.enableA8_31_18 = ko.observable(false);
                self.enableA8_31_19 = ko.observable(false);
                self.enableA8_31_20 = ko.observable(false);

                self.enableA8_32 = ko.observable(false);
                self.enableA8_32_1 = ko.observable(false);
                self.enableA8_32_2 = ko.observable(false);
                self.enableA8_32_3 = ko.observable(false);
                self.enableA8_32_4 = ko.observable(false);
                self.enableA8_32_5 = ko.observable(false);
                self.enableA8_32_6 = ko.observable(false);
                self.enableA8_32_7 = ko.observable(false);
                self.enableA8_32_8 = ko.observable(false);
                self.enableA8_32_9 = ko.observable(false);
                self.enableA8_32_10 = ko.observable(false);
                self.enableA8_32_11 = ko.observable(false);
                self.enableA8_32_12 = ko.observable(false);
                self.enableA8_32_13 = ko.observable(false);
                self.enableA8_32_14 = ko.observable(false);
                self.enableA8_32_15 = ko.observable(false);
                self.enableA8_32_16 = ko.observable(false);
                self.enableA8_32_17 = ko.observable(false);
                self.enableA8_32_18 = ko.observable(false);
                self.enableA8_32_19 = ko.observable(false);
                self.enableA8_32_20 = ko.observable(false);

                self.enableA8_34 = ko.observable(false);
                self.enableA8_34_1 = ko.observable(false);
                self.enableA8_34_2 = ko.observable(false);
                self.enableA8_34_3 = ko.observable(false);
                self.enableA8_34_4 = ko.observable(false);
                self.enableA8_34_5 = ko.observable(false);
                self.enableA8_34_6 = ko.observable(false);
                self.enableA8_34_7 = ko.observable(false);
                self.enableA8_34_8 = ko.observable(false);
                self.enableA8_34_9 = ko.observable(false);
                self.enableA8_34_10 = ko.observable(false);
                self.enableA8_34_11 = ko.observable(false);
                self.enableA8_34_12 = ko.observable(false);
                self.enableA8_34_13 = ko.observable(false);
                self.enableA8_34_14 = ko.observable(false);
                self.enableA8_34_15 = ko.observable(false);
                self.enableA8_34_16 = ko.observable(false);
                self.enableA8_34_17 = ko.observable(false);
                self.enableA8_34_18 = ko.observable(false);
                self.enableA8_34_19 = ko.observable(false);
                self.enableA8_34_20 = ko.observable(false);
                let listPrePostInitialAtr = __viewContext.enums.PrePostInitialAtr;
                self.itemListA8_33 = ko.observableArray([]);
                _.forEach(listPrePostInitialAtr,(obj)=>{
                    self.itemListA8_33.push(new ItemModel(obj.value, obj.name));   
                }); 
                self.selectedCodeA8_33 = ko.observable(0);
                self.selectedCodeA8_33_1 = ko.observable(0);
                self.selectedCodeA8_33_2 = ko.observable(0);
                self.selectedCodeA8_33_3 = ko.observable(0);
                self.selectedCodeA8_33_4 = ko.observable(0);
                self.selectedCodeA8_33_5 = ko.observable(0);
                self.selectedCodeA8_33_6 = ko.observable(0);
                self.selectedCodeA8_33_7 = ko.observable(0);
                self.selectedCodeA8_33_8 = ko.observable(0);
                self.selectedCodeA8_33_9 = ko.observable(0);
                self.selectedCodeA8_33_10 = ko.observable(0);
                self.selectedCodeA8_33_11 = ko.observable(0);
                self.selectedCodeA8_33_12 = ko.observable(0);
                self.selectedCodeA8_33_13 = ko.observable(0);
                self.selectedCodeA8_33_14 = ko.observable(0);
                self.selectedCodeA8_33_15 = ko.observable(0);
                self.selectedCodeA8_33_16 = ko.observable(0);
                self.selectedCodeA8_33_17 = ko.observable(0);
                self.selectedCodeA8_33_18 = ko.observable(0);
                self.selectedCodeA8_33_19 = ko.observable(0);
                self.selectedCodeA8_33_20 = ko.observable(0);

                //a9 
                self.itemListA9_5 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);

                self.itemListA9_9 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_84')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_85'))
                ]);

                self.selectedIdA9_5 = ko.observable(0);
                self.selectedIdA9_8 = ko.observable(0);
                self.selectedIdA9_9 = ko.observable(0);

                //a10
                self.itemListA10_3 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdA10_3 = ko.observable(0);

                //a11
                self.itemListA11_8 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdA11_8 = ko.observable(0);
                self.selectedIdA11_9 = ko.observable(0);
                self.selectedIdA11_10 = ko.observable(0);
                self.selectedIdA11_11 = ko.observable(0);
                self.selectedIdA11_12 = ko.observable(0);
                self.selectedIdA11_13 = ko.observable(0);

                //a12
                self.itemListA12_5 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_36')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_37'))
                ]);
                self.selectedIdA12_5 = ko.observable(0);
                self.itemListA12_6 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_100')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_101'))
                ]);
                self.selectedIdA12_6 = ko.observable(0);
                self.itemListA12_7 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdA12_7 = ko.observable(0);

                //a13
                self.textEditorA13_4 = ko.observable('');
                self.listDataA13 = ko.observableArray([]);

                //a14
                self.itemListA14_3 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdA14_3 = ko.observable(0);

                //a15
                self.itemListA15_4 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.listDataA15 = ko.observableArray([]);
                //a16
                self.texteditorA16_7 = {
                    value: ko.observable(''),
                    constraint: 'Subject',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "468px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorA16_8 = {
                    value: ko.observable(''),
                    constraint: 'Content',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "468px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorA16_9 = {
                    value: ko.observable(''),
                    constraint: 'Subject',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "468px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorA16_10 = {
                    value: ko.observable(''),
                    constraint: 'Content',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "468px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorA16_11 = {
                    value: ko.observable(''),
                    constraint: 'Content',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "468px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };

                //b
                self.itemListB18 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_37')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_136')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_137'))
                ]);
                self.selectedIdB18 = ko.observable(0);
                self.itemListB19 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_139')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_140'))
                ]);
                self.selectedIdB19 = ko.observable(0);
                self.itemListB21 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdB21 = ko.observable(0);
                self.selectedIdB23 = ko.observable(0);
                self.selectedIdB25 = ko.observable(0);
                self.selectedIdB27 = ko.observable(0);
                self.selectedIdB29 = ko.observable(0);
                self.selectedIdB31 = ko.observable(0);
                self.selectedIdB32 = ko.observable(0);
                self.selectedIdB34 = ko.observable(0);
                self.selectedIdB35 = ko.observable(0);
                self.selectedIdB36 = ko.observable(0);

//                self.itemListB30 = ko.observableArray([
//                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
//                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
//                ]);
                 let listRest = __viewContext.enums.BreakReflect;
                self.itemListB30 = ko.observableArray([]);
                _.forEach(listRest,(a)=>{
                    self.itemListB30.push(new ItemModel(a.value, a.name));   
                }); 
                self.selectedCodeB30 = ko.observable(0);
                
//                self.itemListB33 = ko.observableArray([
//                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
//                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
//                ]);
                let listUnitAssignmentOvertime = __viewContext.enums.UnitAssignmentOvertime;
                self.itemListB33 = ko.observableArray([]);
                _.forEach(listUnitAssignmentOvertime,(b)=>{
                    self.itemListB33.push(new ItemModel(b.value, b.name));   
                }); 
                self.selectedCodeB33 = ko.observable(0);

                //i
                self.itemListI4 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdI4 = ko.observable(0);
                //c
                self.itemListC27 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_100')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_101')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_171')),
                ]);
                self.itemListC28 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.itemListC29 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_173')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_174')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_175'))
                ]);
                self.itemListC30 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_173')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_175'))
                ]);
                self.selectedIdC27 = ko.observable(0);
                self.selectedIdC28 = ko.observable(0);
                self.selectedIdC29 = ko.observable(0);
                self.selectedIdC30 = ko.observable(0);
                self.selectedIdC31 = ko.observable(0);
                self.selectedIdC32 = ko.observable(0);
                self.selectedIdC33 = ko.observable(0);
                self.selectedIdC34 = ko.observable(0);
                self.selectedIdC35 = ko.observable(0);
                self.selectedIdC36 = ko.observable(0);
                self.selectedIdC37 = ko.observable(0);
                self.selectedIdC38 = ko.observable(0);
                self.selectedIdC39 = ko.observable(0);
                self.selectedIdC40 = ko.observable(0);
                self.selectedIdC48 = ko.observable(0);
                self.selectedIdC49 = ko.observable(0);
                self.texteditorC41 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorC42 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorC43 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorC44 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorC45 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorC46 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorC47 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorC51 = {
                    value: ko.observable(''),
                    constraint: 'ObstacleName',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                //d
                self.itemListD8 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdD8 = ko.observable(0);
                self.valueD10 = ko.observable('');
                self.enableD11 = ko.observable(false);
                self.texteditorD9 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueD10_1 = ko.observable('');
                self.enableD11_1 = ko.observable(false);
                self.texteditorD12 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.selectedValueD13 = ko.observable(0);
                self.itemListD15 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdD15 = ko.observable(0);
                self.selectedIdD16 = ko.observable(0);
                self.enableD15 = ko.observable(true);

//                self.selectedValueD13.subscribe((value) => {
//                    if (value == 1) {
//                        self.enableD15(true);
//                    } else {
//                        self.enableD15(false);
//                    }
//                });
                //e
                self.itemListE9 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_195')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_196'))
                ]);
                self.selectedIdE9 = ko.observable(0);
                self.itemListE10 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_173')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_174')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_175'))
                ]);
                self.selectedIdE10 = ko.observable(0);
                self.selectedValueE11 = ko.observable(0);
                self.enableE11_5 = ko.observable(true);
                self.itemListE12 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_173')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_174')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_175'))
                ]);
                self.selectedIdE12 = ko.observable(0);
                self.texteditorE13 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueE14 = ko.observable('');
                self.enableE15 = ko.observable(false);
                self.texteditorE16 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueE17 = ko.observable('');
                self.enableE18 = ko.observable(false);
                self.checkedE11_5 = ko.observable(false);
//                self.selectedValueE11.subscribe((newValue) => {
//                    if (newValue == 2) {
//                        self.enableE11_5(true);
//                    } else {
//                        self.enableE11_5(false);
//                    }
//                })
                //f
                self.selectedIdF10 = ko.observable(0);
                self.selectedIdF11 = ko.observable(0);
                self.selectedIdF12 = ko.observable(0);
                self.selectedValueF13 = ko.observable(0);
                self.checkedF13_1 = ko.observable(false);
                self.enableF13_1 = ko.observable(true);
                self.selectedIdF14 = ko.observable(0);
                self.texteditorF15 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueF15_1 = ko.observable('');
                self.enableF15_2 = ko.observable(false);
                self.texteditorF16 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueF16_1 = ko.observable('');
                self.enableF16_1 = ko.observable(false);
                self.itemListF11 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
//                self.selectedValueF13.subscribe((newValue) => {
//                    if (newValue == 2) {
//                        self.enableF13_1(true);
//                    } else {
//                        self.enableF13_1(false);
//                    }
//                })
                //g
                self.itemListG16 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_221')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_222'))
                ]);
                self.selectedIdG16 = ko.observable(0);
                self.itemListG18 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdG18 = ko.observable(0);
                self.selectedIdG20 = ko.observable(0);
                self.selectedIdG22 = ko.observable(0);
                self.itemListG23 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_173')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_175'))
                ]);
                self.selectedIdG23 = ko.observable(0);
                self.itemListG24 = ko.observableArray([
                    new ItemModel(3, nts.uk.resource.getText('KAF022_198')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_199')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_200')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_201'))
                ]);
                self.selectedIdG24 = ko.observable(0);
                self.itemListG25 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_226')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_227'))
                ]);
                self.selectedIdG25 = ko.observable(0);
                self.selectedIdG26 = ko.observable(0);
                self.selectedIdG27 = ko.observable(0);
                self.selectedIdG28 = ko.observable(0);
                self.selectedIdG29 = ko.observable(0);

                //h
                self.itemListH15 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82'))
                ]);
                self.selectedIdH15 = ko.observable(0);
                self.selectedIdH16 = ko.observable(0);
                self.selectedIdH17 = ko.observable(0);
                self.selectedIdH18 = ko.observable(0);
                self.selectedIdH19 = ko.observable(0);
                self.selectedIdH20 = ko.observable(0);
                self.texteditorH22 = {
                    value: ko.observable(''),
                    constraint: 'NameWork',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorH23_1 = {
                    value: ko.observable(''),
                    constraint: 'NameWork',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorH24_1 = {
                    value: ko.observable(''),
                    constraint: 'NameWork',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorH25_1 = {
                    value: ko.observable(''),
                    constraint: 'NameWork',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorH26_1 = {
                    value: ko.observable(''),
                    constraint: 'NameWork',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.texteditorH27_1 = {
                    value: ko.observable(''),
                    constraint: 'NameWork',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.enableH21 = ko.observable(false);
                self.enableH23 = ko.observable(false);
                self.enableH24 = ko.observable(false);
                self.enableH25 = ko.observable(false);
                self.enableH26 = ko.observable(false);
                self.enableH27 = ko.observable(false);
                //j
                self.itemListJ18 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_226')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_227')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_226')),
                    new ItemModel(3, nts.uk.resource.getText('KAF022_227')),
                    new ItemModel(4, nts.uk.resource.getText('KAF022_226'))
                ]);
                self.selectedCodeJ18 = ko.observable(0);
                self.itemListJ19 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82')),
                ]);
                self.itemListJ20 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_36')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_37')),
                ]);
                self.selectedIdJ19 = ko.observable(0);
                self.selectedIdJ20 = ko.observable(0);
                self.selectedIdJ21 = ko.observable(0);
                self.selectedIdJ22 = ko.observable(0);
                self.selectedIdJ23 = ko.observable(0);
                self.selectedIdJ24 = ko.observable(0);
                self.selectedIdJ25 = ko.observable(0);
                self.selectedIdJ26 = ko.observable(0);
                self.selectedIdJ27 = ko.observable(0);
                self.selectedIdJ28 = ko.observable(0);
                self.texteditorJ29 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueJ30 = ko.observable('');
                self.enableJ31 = ko.observable(false);
                self.texteditorJ32 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueJ30_1 = ko.observable('');
                self.enableJ31_1 = ko.observable(false);
                //k
                self.itemListK12 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_100')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_101')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_270')),
                ]);
                self.selectedIdK12 = ko.observable(0);
                self.itemListK13 = ko.observableArray([
                    new ItemModel(1, nts.uk.resource.getText('KAF022_75')),
                    new ItemModel(0, nts.uk.resource.getText('KAF022_82')),
                ]);
                self.selectedIdK13 = ko.observable(0);
                self.itemListK14 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_272')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_273')),
                ]);
                self.selectedIdK14 = ko.observable(0);
                self.selectedIdK15 = ko.observable(0);
                self.itemListK16 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_173')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_175')),
                ]);
                self.selectedIdK16 = ko.observable(0);
                self.texteditorK17 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueK18 = ko.observable('');
                self.enableK19 = ko.observable(false);
                self.texteditorK20 = {
                    value: ko.observable(''),
                    constraint: 'Comment',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "",
                        width: "444px",
                        textalign: "left"
                    })),
                    required: ko.observable(false),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
                self.valueK18_1 = ko.observable('');
                self.enableK19_1 = ko.observable(false);
                self.selectedIdK21 = ko.observable(0);
                self.itemListK22 = ko.observableArray([
                    new ItemModel(0, nts.uk.resource.getText('KAF022_173')),
                    new ItemModel(1, nts.uk.resource.getText('KAF022_174')),
                    new ItemModel(2, nts.uk.resource.getText('KAF022_175'))
                ]);
                self.selectedIdK22 = ko.observable(0);
                //appliSet
                self.baseDateFlg = ko.observable(0);
                self.advanceExcessMessDispAtr = ko.observable(0);
                self.hwAdvanceDispAtr = ko.observable(0);
                self.hwActualDispAtr = ko.observable(0);
                self.actualExcessMessDispAtr = ko.observable(0);
                self.otAdvanceDispAtr = ko.observable(0);
                self.otActualDispAtr = ko.observable(0);
                self.warningDateDispAtr = ko.observable(0);
                self.appReasonDispAtr = ko.observable(0);
                self.scheReflectFlg = ko.observable(0);
                self.priorityTimeReflectFlg = ko.observable(0);

                self.start();
            }

            start(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                dfd.resolve();
                self.loadData();
                return dfd.promise();
            }
            loadData(): void {
                let self = this;
                nts.uk.ui.block.grayout();
                service.findAllData().done((data: any)=> {
                    self.initDataA4(data);
                    self.initDataA5(data);
                    self.initDataA5_24(data);
                    self.initDataA6(data);
                    self.initDataA7AndA8(data);
                    self.initDataA10(data);
                    self.initDataA13(data);
                    self.initDataA14(data);
                    self.initDataA16(data);
                    self.initDataA15();
                    self.initDataA17(data);
    
                    self.initDataB(data);
                    self.initDataC(data);
                    self.initDataD(data);
                    self.initDataF(data);
                    self.initDataE(data);
                    self.initDataG(data);
                    self.initDataH(data);
                    self.initDataI(data);
                    self.initDataJ(data);
                    self.initDataK(data);
                }).always(()=>{
                    nts.uk.ui.errors.clearAll();
                    nts.uk.ui.block.clear();
                    $("#a4_6").focus();
                });
                    
            }
            initDataA4(allData: any): void {
                let self = this;
                // init data A4
                self.dataA4Display([]);
                self.sizeArrayA4(0);
                let data = allData.allClosure;
                if (data) {
                    self.sizeArrayA4(data.length);
                    let ids = _.map(data, 'id');
                    let closureId = {
                        closureId:ids   
                    };
                    service.findApp(closureId).done((arr: any) => {
                        _.forEach(arr, (obj: any, index: number) => {
                            let name = obj.closureId + "." + _.find(data, ['id', obj.closureId]).name;
                            self.dataA4Display.push(new ItemA4(obj.closureId, name, obj.userAtr, obj.deadlineCriteria, obj.deadline));
                            if (self.dataA4Display().length == data.length) {
                                for (let i = data.length + 1; i <= 5; i++) {
                                    self.dataA4Display.push(new ItemA4(i, i + '.', 0, 0, 1));
                                }
                            }
                        });
                    });
                }
            }
            
            initDataA5(allData:any): void {
                let self = this;
                let data = allData.appliSet;
                if(data) {
                    self.companyId(data.companyId);
                    // reasonDisp
                    self.selectedIdA5_14(data.appReasonDispAtr);
                    // warnDateDisp
                    self.selectedCodeA5_16(data.warningDateDispAtr);
                    // overtimePre
                    self.selectedIdA5_18(data.otAdvanceDispAtr);
                    // hdPre
                    self.selectedIdA5_19(data.hwAdvanceDispAtr);
                    // msgAdvance
                    self.selectedIdA5_20(data.advanceExcessMessDispAtr);
                    // overtimePerfom
                    self.selectedIdA5_21(data.otActualDispAtr);
                    // hdPerform
                    self.selectedIdA5_22(data.hwActualDispAtr);
                    // msgExceeded
                    self.selectedIdA5_23(data.actualExcessMessDispAtr);
                }
            }
            
            initDataA5_24(allData: any): void{
                let self = this;
                let data = allData.appSet;
                if(data){
                    // scheduleCon
                    self.selectedIdA5_24(data.scheduleCon);
                    self.selectedIdA5_25(data.achiveCon);
                }
            }
            
            initDataA6(allData:any): void {
                let self = this;
                let listAppType = __viewContext.enums.ApplicationType;
                self.listDataA6([]);
                let data = allData.appName;
                if(data) {
                    _.forEach(listAppType, (appType) => {
                        let obj: any = _.find(data, ['appType', appType.value]);
                        if (obj) {
                            self.listDataA6.push(new ItemA6(self.companyId(), appType.name, obj.dispName, appType.value));
                        } else {
                            self.listDataA6.push(new ItemA6(self.companyId(), appType.name, '', appType.value));
                        }
                    });
                }
            }
            initDataA7AndA8(allData:any): void {
                let self = this;
                let listAppType = __viewContext.enums.ApplicationType;
                self.listDataA7([]);
                self.listDataA8([])
                let data = allData.appBf;
                if(data) {
                    _.forEach(listAppType, (appType: any) => {
                        let obj: any = _.find(data.beforeAfter, ['appType', appType.value]);
                        if (obj) {
                            self.listDataA7.push(new ItemA7(self.companyId(), appType.name, appType.value, obj.retrictPreUseFlg, obj.retrictPreMethodFlg,
                                obj.retrictPreDay, obj.retrictPreTimeDay, obj.retrictPostAllowFutureFlg));
                        } else {
                            self.listDataA7.push(new ItemA7(self.companyId(), appType.name, appType.value, 0, 0, 0, 0, 0));
                        }
                        let obj1: any = _.find(data.appType, ['appType', appType.value]);
                        if (obj1) {
                            self.listDataA8.push(new ItemA8(self.companyId(), appType.value, obj1.displayFixedReason, obj1.displayAppReason,
                                obj1.sendMailWhenRegister, obj1.sendMailWhenApproval, obj1.displayInitialSegment,
                                obj1.canClassificationChange, appType.name));
                        } else {
                            self.listDataA8.push(new ItemA8(self.companyId(), appType.value, 0, 0, 0, 0, 0, 0, appType.name));
                        }
                    });
                }
            }
            initDataA10(allData:any): void {
                let self = this;
                let data = allData.appCommon;
                if(data) {
                    self.selectedIdA10_3(data.showWkpNameBelong);
                }
            }
            initDataA13(allData:any): void {
                let self = this;
                let listAppType = __viewContext.enums.ApplicationType;
                self.listDataA13([]);
                let data = allData.proxy;
                let dataA13: Array<any> = [];
                if(data) {
                    self.listDataA13(_.map(data, 'appType'));
                    _.forEach(listAppType, (appType) => {
                        let obj: any = _.find(data, ['appType', appType.value]);
                        if (obj) {
                            dataA13.push(appType.name);
                        }
                    });
                }
                self.textEditorA13_4(dataA13.join(" + "));
            }
            initDataA14(allData:any): void {
                let self = this;
                let data = allData.jobAssign;
                if(data) {
                    self.selectedIdA14_3(data.isConcurrently ? 1 : 0);
                }
            }
            initDataA15(): void {
                let self = this;
                let date = {
                    baseDate: new Date().toISOString()
                };
                self.listDataA15([]);
                service.findJobId(date).done((data: any) => {
                    if (data) {
                        let jobIds = _.map(data, 'id');

                        let id = {
                            "jobtitleId": jobIds
                        };
                        service.findJobTitleSearchList(id).done(obj => {
                            _.forEach(data, element => {
                                let finder = _.find(obj, ['jobId', element.id]);
                                if (finder) {
                                    self.listDataA15.push(new ItemA15(element.id, element.name, finder.searchSetFlg));
                                } else {
                                    self.listDataA15.push(new ItemA15(element.id, element.name, 1));
                                }
                            });
                        });
                    }
                });
            }

            initDataA16(allData:any): void {
                let self = this;
                let dataMailHd = allData.mailHd;
                if(dataMailHd) {
                    self.texteditorA16_7.value(dataMailHd.subject);
                    self.texteditorA16_8.value(dataMailHd.content);
                }
                 let dataMailOt = allData.mailOt; 
                if(dataMailOt) {
                    self.texteditorA16_9.value(dataMailOt.subject);
                    self.texteditorA16_10.value(dataMailOt.content);
                }
                let dataAppTemp = allData.appTemp;
                if(dataAppTemp) {
                    self.texteditorA16_11.value(dataAppTemp.content);
                }
            }

            initDataA17(allData:any): void {
                let self = this;
                let dataAppro = allData.approvalSettingDto;
                    if (dataAppro) {
                        self.selectedIdA17_5(dataAppro.prinFlg);
                    }
                let dataAppSet = allData.appliSet;
                    if (dataAppSet) {
                        self.selectedIdA17_4(dataAppSet.appContentChangeFlg);
                        self.selectedIdA9_5(dataAppSet.attendentTimeReflectFlg);
                        self.selectedIdA11_8(dataAppSet.appActMonthConfirmFlg);
                        self.selectedIdA11_9(dataAppSet.appOvertimeNightFlg);
                        self.selectedIdA11_10(dataAppSet.appActLockFlg);
                        self.selectedIdA11_11(dataAppSet.appEndWorkFlg);
                        self.selectedIdA11_12(dataAppSet.requireAppReasonFlg);
                        self.selectedIdA11_13(dataAppSet.appActConfirmFlg);
                        self.selectedIdA12_5(dataAppSet.displayPrePostFlg);
                        self.selectedIdA12_6(dataAppSet.displaySearchTimeFlg);
                        self.selectedIdA12_7(dataAppSet.manualSendMailAtr);
                        self.baseDateFlg(dataAppSet.baseDateFlg);
                        self.advanceExcessMessDispAtr(dataAppSet.advanceExcessMessDispAtr);
                        self.hwAdvanceDispAtr(dataAppSet.hwAdvanceDispAtr);
                        self.hwActualDispAtr(dataAppSet.hwActualDispAtr);
                        self.actualExcessMessDispAtr(dataAppSet.actualExcessMessDispAtr);
                        self.otAdvanceDispAtr(dataAppSet.otAdvanceDispAtr);
                        self.otActualDispAtr(dataAppSet.otActualDispAtr);
                        self.warningDateDispAtr(dataAppSet.warningDateDispAtr);
                        self.appReasonDispAtr(dataAppSet.appReasonDispAtr);
                        self.selectedIdA9_8(dataAppSet.scheReflectFlg);
                        self.selectedIdA9_9(dataAppSet.priorityTimeReflectFlg);
                    }
            }
            initDataB(allData:any): void {
                let self = this;
                let data = allData.appOt;
                if(data) {
                    self.selectedIdB18(data.flexExcessUseSetAtr);
                    self.selectedIdB19(data.priorityStampSetAtr);
                    self.selectedIdB21(data.preTypeSiftReflectFlg);
                    self.selectedIdB23(data.preOvertimeReflectFlg);
                    self.selectedIdB25(data.postTypesiftReflectFlg);
                    self.selectedIdB27(data.postWorktimeReflectFlg);
                    self.selectedIdB29(data.postBreakReflectFlg);
                    self.selectedCodeB30(data.restAtr);
                    self.selectedIdB31(data.calendarDispAtr);
                    self.selectedIdB32(data.instructExcessOtAtr);
                    self.selectedCodeB33(data.unitAssignmentOvertime);
                    self.selectedIdB34(data.useOt);
                    self.selectedIdB35(data.earlyOverTimeUseAtr);
                    self.selectedIdB36(data.normalOvertimeUseAtr);
                }
            }
            initDataC(allData:any): void {
                let self = this;
                let data = allData.hdSet;
                if(data) {
                    self.selectedIdC27(data.wrkHours);
                    self.selectedIdC28(data.actualDisp);
                    self.selectedIdC29(data.appDateContra);
                    self.selectedIdC30(data.concheckOutLegal);
                    self.selectedIdC31(data.concheckDateRelease);
                    self.selectedIdC32(data.ckuperLimit);
                    self.selectedIdC33(data.regisNumYear);
                    self.selectedIdC34(data.regisShortLostHd);
                    self.selectedIdC35(data.regisShortReser);
                    self.selectedIdC36(data.regisLackPubHd);
                    self.selectedIdC37(data.regisInsuff);
                    self.selectedIdC38(data.useYear);
                    self.selectedIdC39(data.use60h);
                    self.selectedIdC40(data.useGener);
                    self.texteditorC41.value(data.yearHdName);
                    self.texteditorC42.value(data.obstacleName);
                    self.texteditorC43.value(data.absenteeism);
                    self.texteditorC44.value(data.specialVaca);
                    self.texteditorC45.value(data.yearResig);
                    self.texteditorC46.value(data.hdName);
                    self.texteditorC47.value(data.timeDigest);
                    self.selectedIdC48(data.changeWrkHour);
                    self.selectedIdC49(data.pridigCheck);
                    self.texteditorC51.value(data.furikyuName);
                }
            }
            initDataD(allData:any): void {
                let self = this;
                let data = allData.appChange;
                if(data) {
                    self.selectedIdD8(data.displayResultAtr);
                    self.valueD10(data.commentFontColor1);
                    self.enableD11(data.commentFontWeight1);
                    self.texteditorD9.value(data.commentContent1);
                    self.valueD10_1(data.commentFontColor2);
                    self.enableD11_1(data.commentFontWeight2);
                    self.texteditorD12.value(data.commentContent2);
                    self.selectedValueD13(data.workChangeTimeAtr);
                    self.selectedIdD15(data.initDisplayWorktime);
                    self.selectedIdD16(data.excludeHoliday);
                }
            }

            initDataF(allData:any): void {
                let self = this;
                let data = allData.goBack;
                if(data) {
                    self.selectedIdF10(data.workType);
                    self.selectedIdF11(data.performanceDisplayAtr);
                    self.selectedIdF12(data.contraditionCheckAtr);
                    self.selectedValueF13(data.workChangeFlg);
                    self.checkedF13_1(data.workChangeTimeAtr == 1 ? true : false);
                    self.selectedIdF14(data.lateLeaveEarlySetAtr);
                    self.texteditorF15.value(data.commentContent1);
                    self.valueF15_1(data.commentFontColor1);
                    self.enableF15_2(data.commentFontWeight1 == 1 ? true : false);
                    self.texteditorF16.value(data.commentContent2);
                    self.valueF16_1(data.commentFontColor2);
                    self.enableF16_1(data.commentFontWeight2 == 1 ? true : false);
                }
            }
            initDataE(allData:any): void {
                let self = this;
                let data = allData.tripReq;
                if(data) {
                    self.selectedIdE9(data.workType);
                    self.selectedIdE10(data.contractCheck);
                    self.selectedValueE11(data.workChange);
                    self.checkedE11_5(data.workChangeTime == 1 ? true : false);
                    self.selectedIdE12(data.lateLeave);
                    self.texteditorE13.value(data.comment1);
                    self.texteditorE16.value(data.comment2);
                    self.valueE14(data.color1);
                    self.enableE15(data.weight1);
                    self.valueE17(data.color2);
                    self.enableE18(data.weight2);
                }
            }
            initDataG(allData:any): void {
                let self = this;
                let data = allData.wdApp;
                if(data) {
                    self.selectedIdG16(data.typePaidLeave);
                    self.selectedIdG18(data.restTime);
                    self.selectedIdG20(data.workTime);
                    self.selectedIdG22(data.breakTime);
                    self.selectedIdG23(data.checkOut);
                    self.selectedIdG24(data.workChange);
                    self.selectedIdG25(data.timeInit);
                    self.selectedIdG26(data.checkHdTime);
                    self.selectedIdG27(data.prefixLeave);
                    self.selectedIdG28(data.directDivi);
                    self.selectedIdG29(data.bounSeg);
                }
            }
            initDataJ(allData:any): void {
                let self = this;
                let data = allData.stampReq;
                if(data) {
                    self.selectedCodeJ18(data.supFrameDispNO);
                    self.selectedIdJ19(data.resultDisp);
                    self.selectedIdJ20(data.stampAtr_Work_Disp);
                    self.selectedIdJ21(data.stampAtr_GoOut_Disp);
                    self.selectedIdJ22(data.stampAtr_Care_Disp);
                    self.selectedIdJ23(data.stampAtr_Sup_Disp);
                    self.selectedIdJ24(data.stampAtr_Child_Care_Disp);
                    self.selectedIdJ25(data.stampGoOutAtr_Private_Disp);
                    self.selectedIdJ26(data.stampGoOutAtr_Public_Disp);
                    self.selectedIdJ27(data.stampGoOutAtr_Compensation_Disp);
                    self.selectedIdJ28(data.stampGoOutAtr_Union_Disp);
                    self.texteditorJ29.value(data.topComment);
                    self.texteditorJ32.value(data.bottomComment);
                    self.valueJ30(data.topCommentFontColor);
                    self.valueJ30_1(data.bottomCommentFontColor);
                    self.enableJ31(data.topCommentFontWeight);
                    self.enableJ31_1(data.bottomCommentFontWeight);

                }
            }
            initDataH(allData:any): void {
                let self = this;
                let data = allData.timeHd;
                if(data) {
                    self.selectedIdH15(data.actualDisp);
                    self.selectedIdH16(data.checkOver);
                    self.selectedIdH17(data.checkDay);
                    self.selectedIdH18(data.useTimeYear);
                    self.selectedIdH19(data.use60h);
                    self.selectedIdH20(data.useTimeHd);
                    self.enableH21(data.useBefore == 1 ? true : false);
                    self.texteditorH22.value(data.nameBefore);
                    self.enableH23(data.useAfter == 1 ? true : false);
                    self.texteditorH23_1.value(data.nameAfter);
                    self.enableH24(data.useAttend2 == 1 ? true : false);
                    self.texteditorH24_1.value(data.nameBefore2);
                    self.enableH25(data.useAfter2 == 1 ? true : false);
                    self.texteditorH25_1.value(data.nameAfter2);
                    self.enableH26(data.usePrivate == 1 ? true : false);
                    self.texteditorH26_1.value(data.privateName);
                    self.enableH27(data.unionLeave == 1 ? true : false);
                    self.texteditorH27_1.value(data.unionName);
                }
            }
            initDataK(allData:any): void {
                let self = this;
                let data = allData.wdReq;
                if(data) {
                    self.selectedIdK12(data.deferredWorkTimeSelect);
                    self.selectedIdK13(data.simulAppliReq);
                    self.selectedIdK14(data.lettleSuperLeave);
                    self.selectedIdK15(data.useAtr);
                    self.selectedIdK16(data.checkUpLimitHalfDayHD);
                    self.texteditorK17.value(data.deferredComment);
                    self.valueK18(data.deferredLettleColor);
                    self.enableK19(data.deferredBold);
                    self.texteditorK20.value(data.pickUpComment);
                    self.valueK18_1(data.pickUpLettleColor);
                    self.enableK19_1(data.pickUpBold);
                    self.selectedIdK21(data.permissionDivision);
                    self.selectedIdK22(data.appliDateContrac);
                }
            }
            initDataI(allData:any): void {
                let self = this;
                let data = allData.lateEarly;
                if(data) {
                    self.selectedIdI4(data.showResult);
                }
            }
            save(): void {
                let self = this;
                self.saveDataAt();
            }
            saveDataAt(): void {
                if (nts.uk.ui.errors.hasError()){return;}
                nts.uk.ui.block.invisible();
                let self = this;
                let data: any = {};
                let dataA4 = [];
                for (let i = 0; i < self.sizeArrayA4(); i++) {
                    dataA4.push({
                        closureId: self.dataA4Display()[i].index,
                        userAtr: (self.dataA4Display()[i].a4_6() ? 1 : 0),
                        deadlineCriteria: self.dataA4Display()[i].a4_7(),
                        deadline: self.dataA4Display()[i].a4_8()
                    });
                }
                data.appDead = dataA4;  
                data.appSet = {
                    companyId: self.companyId(),
                    reasonDisp: self.selectedIdA5_14(),
                    warnDateDisp: self.selectedCodeA5_16(),
                    overtimePre: self.selectedIdA5_18(),
                    hdPre: self.selectedIdA5_19(),
                    msgAdvance: self.selectedIdA5_20(),
                    overtimePerfom: self.selectedIdA5_21(),
                    hdPerform: self.selectedIdA5_22(),
                    msgExceeded: self.selectedIdA5_23(),
                    scheduleCon: self.selectedIdA5_24(),
                    achiveCon: self.selectedIdA5_25(),
                     
                };
                data.appCommon = {
                    companyId: self.companyId(),
                    showWkpNameBelong: self.selectedIdA10_3(),
                     
                };
                data.proxy = {
                    companyId: self.companyId(),
                    appType: ko.toJS(self.listDataA13())
                    //todo A13_4
                };
                data.mailHd = {
                    companyId: self.companyId(),
                    subject: self.texteditorA16_7.value(),
                    content: self.texteditorA16_8.value()
                     
                };
                data.mailOt = {
                    companyId: self.companyId(),
                    subject: self.texteditorA16_9.value(),
                    content: self.texteditorA16_10.value()
                         
                };
                data.appTemp = {
                    companyId: self.companyId(),
                    content: self.texteditorA16_11.value()
                         
                };
                data.appliSet = {
                    companyId: self.companyId(),
                    baseDateFlg: self.baseDateFlg(),
                    advanceExcessMessDispAtr: self.advanceExcessMessDispAtr(),
                    hwAdvanceDispAtr: self.hwAdvanceDispAtr(),
                    hwActualDispAtr: self.hwActualDispAtr(),
                    actualExcessMessDispAtr: self.actualExcessMessDispAtr(),
                    otAdvanceDispAtr: self.otAdvanceDispAtr(),
                    otActualDispAtr: self.otActualDispAtr(),
                    warningDateDispAtr: self.warningDateDispAtr(),
                    appReasonDispAtr: self.appReasonDispAtr(),
                    scheReflectFlg: self.selectedIdA9_8(),
                    priorityTimeReflectFlg: self.selectedIdA9_9(),
                    appContentChangeFlg: self.selectedIdA17_4(),
                    attendentTimeReflectFlg: self.selectedIdA9_5(),
                    appActMonthConfirmFlg: self.selectedIdA11_8(),
                    appOvertimeNightFlg: self.selectedIdA11_9(),
                    appActLockFlg: self.selectedIdA11_10(),
                    appEndWorkFlg: self.selectedIdA11_11(),
                    requireAppReasonFlg: self.selectedIdA11_12(),
                    appActConfirmFlg: self.selectedIdA11_13(),
                    displayPrePostFlg: self.selectedIdA12_5(),
                    displaySearchTimeFlg: self.selectedIdA12_6(),
                    manualSendMailAtr: self.selectedIdA12_7(),
                    //todo wait -check 
                };
                data.appName = ko.toJS(self.listDataA6());
                data.stampReq = {
                    companyId: self.companyId(),
                    supFrameDispNO: self.selectedCodeJ18(),
                    resultDisp: self.selectedIdJ19(),
                    stampAtr_Work_Disp: self.selectedIdJ20(),
                    stampAtr_GoOut_Disp: self.selectedIdJ21(),
                    stampAtr_Care_Disp: self.selectedIdJ22(),
                    stampAtr_Sup_Disp: self.selectedIdJ23(),
                    stampAtr_Child_Care_Disp: self.selectedIdJ24(),
                    stampGoOutAtr_Private_Disp: self.selectedIdJ25(),
                    stampGoOutAtr_Public_Disp: self.selectedIdJ26(),
                    stampGoOutAtr_Compensation_Disp: self.selectedIdJ27(),
                    stampGoOutAtr_Union_Disp: self.selectedIdJ28(),
                    topComment: self.texteditorJ29.value(),
                    bottomComment: self.texteditorJ32.value(),
                    topCommentFontColor: self.valueJ30(),
                    topCommentFontWeight: self.enableJ31()? 1 : 0,
                    bottomCommentFontColor: self.valueJ30_1(),
                    bottomCommentFontWeight: self.enableJ31_1()? 1 : 0                 
                };
                data.goBack = {
                    companyId: self.companyId(),
                    workType: self.selectedIdF10(),
                    performanceDisplayAtr: self.selectedIdF11(),
                    contraditionCheckAtr: self.selectedIdF12(),
                    workChangeFlg: self.selectedValueF13(),
                    workChangeTimeAtr: (self.checkedF13_1() ? 1 : 0),
                    lateLeaveEarlySetAtr: self.selectedIdF14(),
                    commentContent1: self.texteditorF15.value(),
                    commentFontColor1: self.valueF15_1(),
                    commentFontWeight1: (self.enableF15_2() ? 1 : 0),
                    commentContent2: self.texteditorF16.value(),
                    commentFontColor2: self.valueF16_1(),
                    commentFontWeight2: (self.enableF16_1() ? 1 : 0),
                     
                };
                data.appOt = {
                    cid: self.companyId(),
                    flexExcessUseSetAtr: self.selectedIdB18(),
                    priorityStampSetAtr: self.selectedIdB19(),
                    preTypeSiftReflectFlg: self.selectedIdB21(),
                    preOvertimeReflectFlg: self.selectedIdB23(),
                    postTypesiftReflectFlg: self.selectedIdB25(),
                    postWorktimeReflectFlg: self.selectedIdB27(),
                    postBreakReflectFlg: self.selectedIdB29(),
                    restAtr: self.selectedCodeB30(),
                    calendarDispAtr: self.selectedIdB31(),
                    instructExcessOtAtr: self.selectedIdB32(),
                    unitAssignmentOvertime: self.selectedCodeB33(),
                    useOt: self.selectedIdB34(),
                    earlyOverTimeUseAtr: self.selectedIdB35(),
                    normalOvertimeUseAtr: self.selectedIdB36(),
                     
                };
                data.hdSet = {
                    companyId: self.companyId(),
                    wrkHours: self.selectedIdC27(),
                    actualDisp: self.selectedIdC28(),
                    appDateContra: self.selectedIdC29(),
                    concheckOutLegal: self.selectedIdC30(),
                    concheckDateRelease: self.selectedIdC31(),
                    ckuperLimit: self.selectedIdC32(),
                    regisNumYear: self.selectedIdC33(),
                    regisShortLostHd: self.selectedIdC34(),
                    regisShortReser: self.selectedIdC35(),
                    regisLackPubHd: self.selectedIdC36(),
                    regisInsuff: self.selectedIdC37(),
                    useYear: self.selectedIdC38(),
                    use60h: self.selectedIdC39(),
                    useGener: self.selectedIdC40(),
                    yearHdName: self.texteditorC41.value(),
                    obstacleName: self.texteditorC42.value(),
                    absenteeism: self.texteditorC43.value(),
                    specialVaca: self.texteditorC44.value(),
                    yearResig: self.texteditorC45.value(),
                    hdName: self.texteditorC46.value(),
                    timeDigest: self.texteditorC47.value(),
                    changeWrkHour: self.selectedIdC48(),
                    pridigCheck: self.selectedIdC49(),
                    furikyuName: self.texteditorC51.value()
                     
                };
                data.appChange = {
                    cid: self.companyId(),
                    displayResultAtr: self.selectedIdD8(),
                    commentFontColor1: self.valueD10(),
                    commentFontWeight1: self.enableD11() ? 1 : 0,
                    commentContent1: self.texteditorD9.value(),
                    commentFontColor2: self.valueD10_1(),
                    commentFontWeight2: self.enableD11_1() ? 1 : 0,
                    commentContent2: self.texteditorD12.value(),
                    workChangeTimeAtr: self.selectedValueD13(),
                    initDisplayWorktime: self.selectedIdD15(),
                    excludeHoliday: self.selectedIdD16()
                     
                };
                data.tripReq = {
                    companyId: self.companyId(),
                    workType: self.selectedIdE9(),
                    contractCheck: self.selectedIdE10(),
                    workChange: self.selectedValueE11(),
                    workChangeTime: self.checkedE11_5() ? 1 : 0,
                    lateLeave: self.selectedIdE12(),
                    comment1: self.texteditorE13.value(),
                    comment2: self.texteditorE16.value(),
                    color1:self.valueE14(),
                    weight1:self.enableE15()? 1 : 0,
                    color2:self.valueE17(),
                    weight2:self.enableE18()? 1 : 0
                };
                data.wdApp = {
                    companyId: self.companyId(),
                    typePaidLeave: self.selectedIdG16(),
                    restTime: self.selectedIdG18(),
                    workTime: self.selectedIdG20(),
                    breakTime: self.selectedIdG22(),
                    checkOut: self.selectedIdG23(),
                    workChange: self.selectedIdG24(),
                    timeInit: self.selectedIdG25(),
                    checkHdTime: self.selectedIdG26(),
                    prefixLeave: self.selectedIdG27(),
                    directDivi: self.selectedIdG28(),
                    bounSeg: self.selectedIdG29()
                     
                };
                data.timeHd = {
                    companyId: self.companyId(),
                    actualDisp: self.selectedIdH15(),
                    checkOver: self.selectedIdH16(),
                    checkDay: self.selectedIdH17(),
                    useTimeYear: self.selectedIdH18(),
                    use60h: self.selectedIdH19(),
                    useTimeHd: self.selectedIdH20(),
                    useBefore: (self.enableH21() ? 1 : 0),
                    nameBefore: self.texteditorH22.value(),
                    useAfter: (self.enableH23() ? 1 : 0),
                    nameAfter: self.texteditorH23_1.value(),
                    useAttend2: (self.enableH24() ? 1 : 0),
                    nameBefore2: self.texteditorH24_1.value(),
                    useAfter2: (self.enableH25() ? 1 : 0),
                    nameAfter2: self.texteditorH25_1.value(),
                    usePrivate: (self.enableH26() ? 1 : 0),
                    privateName: self.texteditorH26_1.value(),
                    unionLeave: (self.enableH27() ? 1 : 0),
                    unionName: self.texteditorH27_1.value()
                     
                };
                data.wdReq = {
                    companyId: self.companyId(),
                    deferredWorkTimeSelect: self.selectedIdK12(),
                    simulAppliReq: self.selectedIdK13(),
                    lettleSuperLeave: self.selectedIdK14(),
                    useAtr: self.selectedIdK15(),
                    checkUpLimitHalfDayHD: self.selectedIdK16(),
                    deferredComment: self.texteditorK17.value(),
                    deferredLettleColor: self.valueK18(),
                    deferredBold: self.enableK19() ? 1 : 0,
                    pickUpComment: self.texteditorK20.value(),
                    pickUpLettleColor: self.valueK18_1(),
                    pickUpBold: self.enableK19_1() ? 1 : 0,
                    permissionDivision: self.selectedIdK21(),
                    appliDateContrac: self.selectedIdK22(),
                                         
                };
                data.lateEarly = {
                    companyId: self.companyId(),
                    showResult: self.selectedIdI4()
                     
                };
                data.appBf = {
                    beforeAfter: _.map(ko.toJS(self.listDataA7()), (x: any) => {
                        x.retrictPostAllowFutureFlg = x.retrictPostAllowFutureFlg ? 1 : 0;
                        x.retrictPreUseFlg = x.retrictPreUseFlg ? 1 : 0;
                        return x;
                    }),
                    appType: _.map(ko.toJS(self.listDataA8()), (x: any) => {
                        x.displayFixedReason = x.displayFixedReason ? 1 : 0;
                        x.displayAppReason = x.displayAppReason ? 1 : 0;
                        x.sendMailWhenRegister = x.sendMailWhenRegister ? 1 : 0;
                        x.sendMailWhenApproval = x.sendMailWhenApproval ? 1 : 0;
                        x.canClassificationChange = x.canClassificationChange ? 1 : 0;
                        return x;
                    })
                };
                data.jobAssign = {
                    isConcurrently: self.selectedIdA14_3() ? 1 : 0
                };
                data.approvalSet = {
                    prinFlg: self.selectedIdA17_5()
                };
                data.jobSearch = ko.toJS(self.listDataA15());
                service.update(data).done(() => {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                        //Load data setting
                        self.loadData();
                    });
                }).always(()=>{
                    nts.uk.ui.block.clear();
                });
           
            }

        }

        class ItemModel {
            code: number;
            name: string;

            constructor(code: number, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        class ItemA4 {
            index: number;
            a4_5: KnockoutObservable<string>;
            a4_6: KnockoutObservable<boolean>;
            a4_7: KnockoutObservable<number>;
            a4_8: KnockoutObservable<number>;
            constructor(index: number, a4_5: string, a4_6: number, a4_7: number, a4_8: number) {
                this.index = index;
                this.a4_5 = ko.observable(nts.uk.resource.getText(a4_5));
                this.a4_6 = ko.observable(a4_6 == 1 ? true : false);
                this.a4_7 = ko.observable(a4_7);
                this.a4_8 = ko.observable(a4_8);
            }
        }
        class ItemA6 {
            companyId: KnockoutObservable<string>;
            appTypeName: KnockoutObservable<string>;
            dispName: KnockoutObservable<string>;
            appType: KnockoutObservable<number>;
            constructor(companyId: string, appTypeName: string, dispName: string, appType: number) {
                this.companyId = ko.observable(companyId);
                this.appTypeName = ko.observable(appTypeName);
                this.dispName = ko.observable(dispName);
                this.appType = ko.observable(appType);
            }
        }
        class ItemA7 {
            companyId: KnockoutObservable<string>;
            appType: KnockoutObservable<number>;
            retrictPreMethodFlg: KnockoutObservable<number>;
            retrictPreUseFlg: KnockoutObservable<boolean>;
            retrictPreDay: KnockoutObservable<number>;
            retrictPreTimeDay: KnockoutObservable<number>;
            retrictPostAllowFutureFlg: KnockoutObservable<boolean>;
            appTypeName: KnockoutObservable<string>;
            constructor(companyId: string, appTypeName: string, appType: number, retrictPreUseFlg: number, retrictPreMethodFlg: number,
                retrictPreDay: number, retrictPreTimeDay: number, retrictPostAllowFutureFlg: number) {
                this.companyId = ko.observable(companyId);
                this.appTypeName = ko.observable(appTypeName);
                this.appType = ko.observable(appType);
                this.retrictPreMethodFlg = ko.observable(retrictPreMethodFlg);
                this.retrictPreUseFlg = ko.observable(retrictPreUseFlg==1 ? true : false);
                this.retrictPreDay = ko.observable(retrictPreDay);
                this.retrictPreTimeDay = ko.observable(retrictPreTimeDay);
                this.retrictPostAllowFutureFlg = ko.observable(retrictPostAllowFutureFlg == 1 ? true : false);
            }
        }
        class ItemA8 {
            companyId: KnockoutObservable<string>;
            appType: KnockoutObservable<number>;
            displayFixedReason: KnockoutObservable<boolean>;
            displayAppReason: KnockoutObservable<boolean>;
            sendMailWhenRegister: KnockoutObservable<boolean>;
            sendMailWhenApproval: KnockoutObservable<boolean>;
            displayInitialSegment: KnockoutObservable<number>;
            canClassificationChange: KnockoutObservable<boolean>;
            appTypeName: KnockoutObservable<string>;
            constructor(companyId: string, appType: number, displayFixedReason: number, displayAppReason: number,
                sendMailWhenRegister: number, sendMailWhenApproval: number, displayInitialSegment: number,
                canClassificationChange: number, appTypeName: string) {
                this.companyId = ko.observable(companyId);
                this.appType = ko.observable(appType);
                this.displayFixedReason = ko.observable(displayFixedReason == 1 ? true : false);
                this.displayAppReason = ko.observable(displayAppReason == 1 ? true : false);
                this.sendMailWhenRegister = ko.observable(sendMailWhenRegister == 1 ? true : false);
                this.sendMailWhenApproval = ko.observable(sendMailWhenApproval == 1 ? true : false);
                this.displayInitialSegment = ko.observable(displayInitialSegment);
                this.canClassificationChange = ko.observable(canClassificationChange == 1 ? true : false);
                this.appTypeName = ko.observable(appTypeName);
            }
        }
        class ItemA15 {
            jobId: KnockoutObservable<string>;
            name: KnockoutObservable<string>;
            searchSetFlg: KnockoutObservable<number>;
            constructor(jobId: string, name: string, searchSetFlg: number) {
                this.jobId = ko.observable(jobId);
                this.name = ko.observable(name);
                this.searchSetFlg = ko.observable(searchSetFlg);
            }
        }

    }
}