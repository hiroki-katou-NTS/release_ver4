module nts.uk.at.kal014.c {

    import common=nts.uk.at.kal014.common;
    import StartSpecify = nts.uk.at.kal014.a.StartSpecify;
    import EndSpecify = nts.uk.at.kal014.a.EndSpecify;
    import PreviousClassification = nts.uk.at.kal014.a.PreviousClassification;

    @bean()
    export class Kal014CViewModel extends ko.ViewModel {
        modalDTO: ModalDto = new ModalDto();
        strComboMonth: KnockoutObservableArray<any>;
        endComboMonth: KnockoutObservableArray<any>;
        strComboDay: KnockoutObservableArray<any>;
        endComboDay: KnockoutObservableArray<any>;
        strSelected: KnockoutObservable<number> = ko.observable(0);
        endSelected: KnockoutObservable<number> = ko.observable(0);
        workPalceCategory: any;
        dateSpecify: KnockoutObservableArray<any>;
        monthSpecify: KnockoutObservableArray<any>;
        isDayStart : KnockoutObservable<boolean> = ko.observable(true);
        isDayEnd : KnockoutObservable<boolean> = ko.observable(true);
        isMonthStart : KnockoutObservable<boolean> = ko.observable(true);
        isMonthEnd : KnockoutObservable<boolean> = ko.observable(true);

        constructor(params: any) {
            super();
            const vm = this;
            vm.workPalceCategory = common.WORKPLACE_CATAGORY;

            vm.strComboMonth = ko.observableArray(__viewContext.enums.SpecifiedMonth);
            vm.endComboMonth = ko.observableArray(__viewContext.enums.SpecifiedMonth);
            vm.strComboDay = ko.observableArray(__viewContext.enums.PreviousClassification);
            vm.endComboDay = ko.observableArray(__viewContext.enums.PreviousClassification);
            vm.dateSpecify = ko.observableArray([
                {value: StartSpecify.DAYS, name: vm.$i18n("KAL014_44")},
                {value: StartSpecify.MONTH, name: ""}
            ]);
            vm.monthSpecify = ko.observableArray([
                {value: EndSpecify.DAYS, name: vm.$i18n("KAL014_49")},
                {value: EndSpecify.MONTH, name: ""}
            ]);

            vm.initModalData(params);
        }

        created() {
            const vm = this;
            vm.strSelected.subscribe((value: number)=>{
                vm.isDayStart(value == StartSpecify.DAYS);
                vm.isMonthStart(value == StartSpecify.MONTH);

                if (value == StartSpecify.MONTH){
                    vm.modalDTO.clearDayStart();
                } else{
                    vm.modalDTO.clearMonthStart()
                }
            })
            vm.endSelected.subscribe((value: number)=>{
                vm.isDayEnd(value == EndSpecify.DAYS);
                vm.isMonthEnd(value == EndSpecify.MONTH);
                if (value == EndSpecify.MONTH){
                    vm.modalDTO.clearDayEnd();
                } else{
                    vm.modalDTO.clearMonthStart()
                }
            })
        }


        /**
         * This function is responsible to initialized modal data
         *
         * @return boolean
         **/
        initModalData(modalData: any) {
            const vm = this;
            vm.strSelected(modalData.extractionDaily.strSpecify)
            vm.endSelected(modalData.extractionDaily.endSpecify)
            vm.modalDTO.categoryId(modalData.alarmCategory);
            vm.modalDTO.categoryName(modalData.alarmCtgName);
            vm.modalDTO.startMonth(modalData.extractionDaily.strMonth);
            vm.modalDTO.endMonth(modalData.extractionDaily.endMonth);
            vm.modalDTO.numberOfDayFromStart(modalData.extractionDaily.strDay);
            vm.modalDTO.numberOfDayFromEnd(modalData.extractionDaily.endDay);
            vm.modalDTO.beforeAndAfterStart(modalData.extractionDaily.strPreviousDay);
            vm.modalDTO.beforeAndAfterEnd(modalData.extractionDaily.endPreviousDay);
        }

        /**
         * This function is responsible to close the modal
         *
         * @return type void         *
         * */
        cancel_Dialog(): any {
            const vm = this;
            vm.$window.close({
            });
        }

        /**
         * This function is responsible to perform modal data action
         *
         * @return type void         *
         * */
        decide(): any {
            const vm = this;
            let checkResult = vm.checkPeriod();
            if (!_.isNil(checkResult)) {
                vm.$dialog.error({messageId: checkResult}).done(()=>{
                    return;
                })

            } else {
                let shareData = {
                    alarmCategory: vm.modalDTO.categoryId(),
                    alarmCategoryName: vm.modalDTO.categoryName(),
                    //Start date
                    strSpecify: vm.strSelected(),
                    strMonth: vm.modalDTO.startMonth(),
                    strDay: vm.modalDTO.numberOfDayFromStart(),
                    strPreviousDay: vm.modalDTO.beforeAndAfterStart(),
                    strPreviousMonth: vm.endSelected() == StartSpecify.MONTH ? PreviousClassification.BEFORE : null,
                    //End date
                    endSpecify: vm.endSelected(),
                    endMonth: vm.modalDTO.endMonth(),
                    endDay: vm.modalDTO.numberOfDayFromEnd(),
                    endPreviousDay:  vm.modalDTO.beforeAndAfterEnd(),
                    endPreviousMonth: vm.endSelected() == EndSpecify.MONTH ? PreviousClassification.BEFORE : null
                }
                vm.$window.close({
                    shareData
                });
            }

        }

        /**
         * This function is responsible to error validation check[補足資料２]
         *
         * @return type void               *
         * */
        checkPeriod(): string {
            var vm = this;
                // (a）開始区分＝「当日」　AND　終了区分＝「当日」
            if (vm.strSelected() === StartSpecify.DAYS
                && vm.endSelected() === EndSpecify.DAYS) {
                /**
                 * ①開始の前後区分＝「後」　AND　終了の前後区分＝「前」
                 */
                if (vm.modalDTO.beforeAndAfterStart() === PreviousClassification.AHEAD
                    && vm.modalDTO.beforeAndAfterEnd() === PreviousClassification.BEFORE) {
                    return "Msg_812";
                }
                /**
                 * ②開始の前後区分＝「前」　AND　終了の前後区分＝「前」 　AND　開始の日数　＜　終了の日数
                 */
                else if (vm.modalDTO.beforeAndAfterStart() === PreviousClassification.BEFORE
                        && vm.modalDTO.beforeAndAfterEnd() === PreviousClassification.BEFORE
                        && vm.modalDTO.numberOfDayFromStart() < vm.modalDTO.numberOfDayFromEnd()) {
                    return "Msg_812";
                }
                /**
                 * ③開始の前後区分＝「後」　AND　終了の前後区分＝「後」
                 */
                else if (vm.modalDTO.beforeAndAfterStart() === PreviousClassification.AHEAD
                    && vm.modalDTO.beforeAndAfterEnd() === PreviousClassification.AHEAD
                    && vm.modalDTO.numberOfDayFromStart() > vm.modalDTO.numberOfDayFromEnd()) {
                    return "Msg_812";
                    // ④開始の前後区分＝「前」　AND　終了の前後区分＝「後」
                } else if (vm.modalDTO.beforeAndAfterStart() === PreviousClassification.BEFORE
                    && vm.modalDTO.beforeAndAfterEnd() === PreviousClassification.AHEAD) {
                    return null
                }
            }
            /**
             * (b）開始区分＝「当日」　AND　終了区分＝「締め終了日」
             AND　終了の月数＝当月
             */
            else if ((vm.strSelected() === StartSpecify.DAYS
                    && vm.endSelected() === StartSpecify.MONTH) && vm.modalDTO.endMonth() === 0) {
                return "Msg_813";
            }
            /**
             * (c）開始区分＝「締め開始日」　AND　終了区分＝「締め終了日」
             AND　開始の月数　＞　終了の月数
             */
            else if ((vm.strSelected() === StartSpecify.MONTH && vm.endSelected() === StartSpecify.MONTH)
                && vm.modalDTO.startMonth() < vm.modalDTO.endMonth()) {
                return "Msg_812";
            }

            return null;
        }
    }

    class ModalDto {
        categoryId: KnockoutObservable<any>;
        categoryName: KnockoutObservable<string>;
        startMonth: KnockoutObservable<any>;
        endMonth: KnockoutObservable<any>;
        numberOfDayFromStart:  KnockoutObservable<any>;
        numberOfDayFromEnd: KnockoutObservable<any>;
        beforeAndAfterStart: KnockoutObservable<any>;
        beforeAndAfterEnd: KnockoutObservable<any>;

        constructor() {
            this.categoryId = ko.observable('');
            this.categoryName = ko.observable('');
            this.startMonth = ko.observable('');
            this.endMonth = ko.observable(null);
            this.numberOfDayFromStart = ko.observable('');
            this.numberOfDayFromEnd = ko.observable('');
            this.beforeAndAfterStart = ko.observable('');
            this.beforeAndAfterEnd = ko.observable('');
        }

        clearDayStart(){
            this.numberOfDayFromStart = ko.observable(null);
            this.beforeAndAfterStart = ko.observable(null);
        }

        clearDayEnd(){
            this.numberOfDayFromEnd = ko.observable(null);
            this.beforeAndAfterEnd = ko.observable(null);
        }

        clearMonthStart(){
            this.startMonth = ko.observable(null);
        }

        clearMonthEnd(){
            this.endMonth = ko.observable(null);
        }
    }

    class BoxModel {
        id: number;
        name: string;

        constructor(id: number, name: string) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }
}