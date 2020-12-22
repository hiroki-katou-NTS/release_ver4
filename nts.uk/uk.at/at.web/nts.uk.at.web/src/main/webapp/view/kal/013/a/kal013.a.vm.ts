/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />
module nts.uk.at.view.kal013.a {

    import common = nts.uk.at.view.kal013.common;
    import tab = nts.uk.at.view.kal013.a.tab;

    const PATH_API = {
        getEnumAlarmCategory: "at/function/alarm/get/enum/alarm/category",
        changeCategory: "alarmworkplace/checkCondition/getByCategory",
        changeAlarmCode: "alarmworkplace/checkCondition/getByCategoryItemCD",
        register: "alarmworkplace/checkCondition/register"
    };

    @bean()
    class ViewModel extends ko.ViewModel {

        //categories
        categoryList: KnockoutObservableArray<common.Category> = ko.observableArray(common.workplaceCategory());
        selectedCategoryCode: KnockoutObservable<number> = ko.observable(common.WorkplaceCategory.MASTER_CHECK_BASIC);
        selectedCategory: KnockoutObservable<common.CategoryPattern> = ko.observable(null);
        //Alarm list
        selectedAlarmCode: KnockoutObservable<string> = ko.observable(null);
        alarmListItems: KnockoutObservableArray<common.Alarm> = ko.observableArray([]);
        currentCode: KnockoutObservable<string> = ko.observable(null);
        currentName: KnockoutObservable<string> = ko.observable(null);

        // List Fixed Items
        fixedItems: KnockoutObservableArray<common.AlarmDto> = ko.observableArray([]);
        // List Conditions
        conditions: KnockoutObservableArray<any> = ko.observableArray([]);
        // List Optional Items
        optionalItems: KnockoutObservableArray<any> = ko.observableArray([]);
        // Actual List
        actualFixedItems: KnockoutObservableArray<common.AlarmDto> = ko.observableArray([]);

        //tab panel
        tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
        selectedTab: KnockoutObservable<string> = ko.observable(null); //active
        //grid list
        selectedAll: KnockoutObservable<boolean> = ko.observable(false);
        alarmArrangeList: KnockoutObservableArray<common.AlarmDto> = ko.observableArray([]);
        //switch button
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: KnockoutObservable<number> = ko.observable(1);

        workplaceCategory: any = {};

        uniqueConditions: KnockoutObservable<tab.UniqueCondition>;
        checkConditions: KnockoutObservable<tab.CheckCondition>;

        constructor(params: any) {
            super();
            const vm = this;

            vm.workplaceCategory = common.WorkplaceCategory;

            vm.getAlarmChecklist();

            vm.tabSelections();

            vm.selectedCategoryCode.subscribe((newCode: any) => {
                if (!newCode) return;
                vm.getSelectedCategory(newCode);

                //reload with new category
                vm.getAlarmChecklist(newCode);
                vm.showHiddenTabByCategory(newCode);
            });

            vm.selectedTab.subscribe((newTab) => {
                const vm = this;
                let category: any = vm.selectedCategoryCode();

                switch (category) {
                    case vm.workplaceCategory.SCHEDULE_DAILY:
                        let hasClicked: any = $("#fixedTableCCDT").attr('data-clicked');
                        if (typeof hasClicked == 'undefined' || hasClicked === 'false') {
                            $("#fixedTableCCDT")
                                .attr('data-clicked', 'true')
                                .ntsFixedTable({height: 370});
                        }
                        break;
                }
            });

            //show tabs
            vm.uniqueConditions = ko.observable(new tab.UniqueCondition(vm.selectedCategoryCode()));
            vm.checkConditions = ko.observable(null);
        }

        created(params: any) {
            const vm = this;
        }

        mounted() {
            const vm = this;
            $("#fixedTable").ntsFixedTable({height: 350});
            vm.changeCategory();

            vm.selectedCategoryCode.subscribe(value => {
                vm.changeCategory();
            });

            vm.selectedAlarmCode.subscribe(value => {
                if (value) {
                    vm.changeAlarmCode();
                }
            });

            vm.actualFixedItems.subscribe(value => {
                vm.uniqueConditions().alarmListItem(value);
                vm.uniqueConditions().selectedAll(false);
            })
        }

        switchNewMode() {
            const vm = this;

            vm.$errors("clear");
            vm.currentCode(null);
            vm.currentName(null);
            vm.selectedAlarmCode(null);
            vm.actualFixedItems(vm.fixedItems());
            vm.actualFixedItems.valueHasMutated();
        }

        copyAlarm() {
            const vm = this;

            vm.$errors("clear");
            vm.currentCode(null);
            vm.currentName(null);
            vm.selectedAlarmCode(null);
        }

        findItemSelected(code: any, seachArr: Array<any>): any {
            const vm = this;
            if (!_.isArray(seachArr)) return null;
            let found = _.find(seachArr, ['code', code]);
            return (!_.isNil(found)) ? found : null;
        }

        changeCategory() {
            const vm = this;

            vm.$blockui("grayout");
            return vm.$ajax(PATH_API.changeCategory + "/" + vm.selectedCategoryCode()).done((res: any) => {
                if (res) {
                    let categoryItem = _.map(res.alarmWkpCheckCdt, function (i: any) {
                        return new common.Alarm(i.code, i.name);
                    });
                    let fixedItems = _.map(res.fixedItems, function (i: any) {
                        return new common.AlarmDto(
                            false,
                            i.no,
                            i.classification,
                            i.name,
                            i.message
                        );
                    });

                    vm.fixedItems(fixedItems);

                    if (categoryItem.length) {
                        vm.alarmListItems(categoryItem);
                        vm.selectedAlarmCode("");
                        vm.selectedAlarmCode(categoryItem[0].code);
                    } else {
                        vm.actualFixedItems(fixedItems);
                    }

                }
            }).fail((err) => {
                vm.$dialog.error(err);
            }).always(() => vm.$blockui("clear"));

        }

        changeAlarmCode() {
            const vm = this;

            let params = {
                category: vm.selectedCategoryCode(),
                code: vm.selectedAlarmCode()
            };
            let selectedItems = _.find(vm.alarmListItems(), i => i.code == vm.selectedAlarmCode());

            if (selectedItems) {
                vm.currentCode(selectedItems.code);
                vm.currentName(selectedItems.name);
            }

            vm.$blockui("grayout");
            vm.$ajax(PATH_API.changeAlarmCode, params).done((res: any) => {
                if (res) {
                    let fixed = _.cloneDeep(vm.fixedItems());
                    let actual = _.map(fixed, function (i: common.AlarmDto) {
                        if (res.conditions && res.conditions.length) {
                            let con = res.conditions;
                            let itemHasSameNo = _.find(con, (x: FixedExtractionConditionDto) => x.no == i.no());
                            if (itemHasSameNo) {
                                i.message(itemHasSameNo.displayMessage);
                                i.isChecked(itemHasSameNo.useAtr);
                            }
                        }
                        return i;
                    });

                    vm.actualFixedItems(actual);

                    if (res.optionalItems && res.optionalItems) {

                    }

                }


            }).fail((err) => {
                vm.$dialog.error(err);
            }).always(() => vm.$blockui("clear"));

        }

        openScreenB() {
            const vm = this;
            //vm.$window.storage('');
            vm.$window.modal('/view/kal/013/d/index.xhtml').then(() => {
            });
        }

        /**
         * Registration of alarm list check conditions (by workplace)
         */
        registerAlarmListByWorkplace() {
            const vm = this;

            let alarmCheck = {
                category: vm.selectedCategory(),
                code: vm.currentCode(),
                name: vm.currentName()
            };

            let alarmCheckCon = _.map(ko.toJS(vm.actualFixedItems()), function (i: common.AlarmDto) {
               return {
                   no: i.no,
                   message: i.message,
                   check: i.isChecked
               };
            });

            let param = {
                alarmCheck,
                alarmCheckCon
            };

            vm.$blockui("grayout");
            vm.$ajax(PATH_API.register, param).done((res) => {
                vm.$dialog.info({messageId: "Msg_15"});
            }).fail((err) => {
                vm.$dialog.error(err);
            }).always(() => vm.$blockui("clear"));
        }

        /**
         * Delete of alarm list check conditions (by workplace)
         */
        deleteAlarmListByWorkplace() {
            const vm = this;
            const param = {
                categoryID: 1
            };


        }

        /**
         * Gets alarm checklist / アラームチェックリスト
         */
        getAlarmChecklist(code?: number) {
            const vm = this;

        }

        /**
         * Gets selected category
         * @param categoryCode
         * @returns  CategoryPattern
         */
        getSelectedCategory(categoryCode: number) {
            const vm = this;
            if (categoryCode < 0) return;

            let fountCategory = vm.findItemSelected(categoryCode, vm.categoryList());
            if (!_.isNil(fountCategory)) {
                vm.selectedCategory(new common.CategoryPattern(fountCategory.code, fountCategory.name));
            } else {
                vm.selectedCategory(new common.CategoryPattern('', ''));
            }
        }

        /**
         * Tabs selections
         */
        tabSelections() {
            const vm = this;

            vm.tabs = ko.observableArray([
                {
                    id: 'tab-1',
                    title: vm.$i18n('KAL013_15'),
                    content: '.tab-content-1',
                    enable: ko.observable(true),
                    visible: ko.observable(true)
                },
                {
                    id: 'tab-2',
                    title: vm.$i18n('KAL013_15'),
                    content: '.tab-content-2',
                    enable: ko.observable(true),
                    visible: ko.observable(false)
                }
            ]);

            vm.selectedTab('tab-1');
        }

        showHiddenTabByCategory(Category: any) {
            const vm = this;

            //hidden all tab
            _.forEach(vm.tabs(), (tab: any, index) => {
                tab.visible(index === 0);
            });
            /*
            MASTER_CHECK_BASIC = 0,// マスタチェック(基本)
            MASTER_CHECK_WORKPLACE = 1,// マスタチェック(職場)
            MASTER_CHECK_DAILY = 2,// マスタチェック(日次)
            SCHEDULE_DAILY = 3, // "スケジュール／日次",
            MONTHLY = 4,// 月次
            APPLICATION_APPROVAL = 5, //"申請承認"/
            */

            //vm.checkConditions = ko.observable(null);

            switch (Category) {

                case vm.workplaceCategory.APPLICATION_APPROVAL:
                case vm.workplaceCategory.MASTER_CHECK_WORKPLACE:
                case vm.workplaceCategory.MASTER_CHECK_BASIC:
                case vm.workplaceCategory.MASTER_CHECK_DAILY:
                    break;

                case vm.workplaceCategory.SCHEDULE_DAILY:
                    vm.tabs()[1].visible(true);
                    vm.checkConditions(new tab.CheckCondition(true));
                    break;

                case vm.workplaceCategory.MONTHLY:
                    vm.tabs()[1].visible(true);
                    vm.checkConditions(new tab.CheckCondition(true));
                    break;
            }
        }

        //check by tabs
    }

    interface FixedExtractionConditionDto {
        // ID
        id: string;
        // No
        no: number;
        // 使用区分
        useAtr: boolean;
        // 表示するメッセージ
        displayMessage: string;
    }

    interface OptionalItemDto {
        checkItem: number;
        no: number;
        conditionName: string;
        message: string;
        useAtr: boolean;
    }

    interface FixedExtractionItemDto {
        // ID
        id: string;
        // NO
        no: number;
        // 区分
        classification: number;
        // 名称
        name: string;
        // 表示メッセージ
        message: string;
    }

    interface AlarmWkpCheckCdtDto {
        // カテゴリ
        category: number;
        // アラームチェック条件コード
        code: string;
        // 名称
        name: string;
    }
}