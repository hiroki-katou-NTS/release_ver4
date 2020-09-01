/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.kmr001.b {

	const API = {
        GET_BENTO_RESERVATION: 'screen/at/record/reservation/bento-menu/getbentomenu',
        ADD_BENTO_RESERVATION: 'bento/bentomenusetting/add'
    };

	const PATH = {
	    REDIRECT: '/view/ccg/008/a/index.xhtml'
    };

	@bean()
	class ViewModel extends ko.ViewModel {
        enable: KnockoutObservable<boolean>;
        itemsReservationChange: KnockoutObservableArray<ReservationChange> = ko.observableArray([]);
        itemsReservationChangeDay: KnockoutObservableArray<TimePeriod> = ko.observableArray([]);
        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;
        model : KnockoutObservable<Reservation> = ko.observable(new Reservation(0,0,0,0,0,0,0,'',0,0,
            '',null,null,0));
        visibleContentChangeDeadline: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
        	super();
            const vm = this;

            //combo box B10_2
            vm.itemsReservationChange([
                {appId: 0, appName: vm.$i18n('KMR001_85')},
                {appId: 1, appName: vm.$i18n('KMR001_86')},
                {appId: 2, appName: vm.$i18n('KMR001_87')}
            ]);

            //combo box B10_3
            _.range(0, 31).map(item => vm.itemsReservationChangeDay.push({appId: item, appValue: (item+1).toString()}));
            vm.enable = ko.observable(true);
            console.log(vm.model);
        }

		created() {
			const vm = this;
			 _.extend(window, { vm });
		}
        mounted() {
            const vm = this;
            vm.$blockui("invisible");
            vm.$ajax(API.GET_BENTO_RESERVATION).done((data: Reservation) => {
                if(data) {
                    vm.$blockui("clear");
                    vm.model(new Reservation(Number(data.operationDistinction), Number(data.referenceTime),
                        data.contentChangeDeadline ? Number(data.contentChangeDeadline) : 0,
                        Number(data.contentChangeDeadlineDay), Number(data.orderDeadline),
                        Number(data.monthlyResults), Number(data.dailyResults), data.reservationFrameName1.toString(),
                        Number(data.reservationStartTime1), Number(data.reservationEndTime1), data.reservationFrameName2 ? data.reservationFrameName2.toString() : "",
                        data.reservationStartTime2 ? Number(data.reservationStartTime2) : null, data.reservationEndTime2 ? Number(data.reservationEndTime2) : null, Number(data.orderedData))
                    );
                    if(Number(data.contentChangeDeadline) == 1) {
                        vm.visibleContentChangeDeadline(true);
                    }
                }

            }).always(() => this.$blockui("clear"));
        }

        registerBentoReserveSetting() {
            const vm = this;
            $(".nts-input").trigger("validate");
            if (nts.uk.ui.errors.hasError()){
                return;
            }
            if(vm.model().reservationStartTime1() >= vm.model().reservationEndTime1()) {
                $('#end1').ntsError('set', {messageId:'Msg_849'});
                return;
            }
            if(vm.model().reservationStartTime2() != null && vm.model().reservationEndTime2() != null && vm.model().reservationStartTime2()  >= vm.model().reservationEndTime2()) {
                $('#end2').ntsError('set', {messageId:'Msg_849'});
                return;
            }

            vm.$blockui("invisible");
            const dataRegister = {
                operationDistinction : vm.model().operationDistinction(),
                referenceTime : vm.model().referenceTime(),
                dailyResults : vm.model().dailyResults(),
                monthlyResults : vm.model().monthlyResults(),
                contentChangeDeadline : vm.model().contentChangeDeadline(),
                contentChangeDeadlineDay : vm.model().contentChangeDeadlineDay(),
                orderedData : vm.model().orderedData(),
                orderDeadline : vm.model().orderDeadline(),
                name1 : vm.model().reservationFrameName1(),
                end1 : vm.model().reservationEndTime1(),
                start1 : vm.model().reservationStartTime1(),
                name2 : vm.model().reservationFrameName2(),
                end2 : vm.model().reservationEndTime2(),
                start2 : vm.model().reservationStartTime2()
            };
            vm.$ajax(API.ADD_BENTO_RESERVATION, dataRegister).done(() => {
                vm.$dialog.info({ messageId: "Msg_15" }).then(function () {
                    vm.$blockui("clear");
                });
            }).always(() => this.$blockui("clear"));
            
        }

	}

    interface ReservationChange{
        appId: number;
        appName: string;
    }

    interface TimePeriod{
        appId: number;
        appValue: string;
    }

    // class for kmr001 b
    class Reservation{
	    //B3_2
        operationDistinction: KnockoutObservable<number>;
        //B5_2
        referenceTime: KnockoutObservable<number>;
        //B10_2
        contentChangeDeadline: KnockoutObservable<number>;
        //B10_3
        contentChangeDeadlineDay: KnockoutObservable<number>;
        //B14_2
        orderDeadline: KnockoutObservable<number>;
        //B17_2
        monthlyResults: KnockoutObservable<number>;
        //B18_2
        dailyResults: KnockoutObservable<number>;
        //B19_3
        reservationFrameName1: KnockoutObservable<string>;
        //B19_5
        reservationStartTime1: KnockoutObservable<number>;
        //B19_7
        reservationEndTime1: KnockoutObservable<number>;
        //B20_3
        reservationFrameName2: KnockoutObservable<string>;
        //B20_5
        reservationStartTime2: KnockoutObservable<number>;
        //B20_7	
        reservationEndTime2: KnockoutObservable<number>;
        //B21_2	
        orderedData: KnockoutObservable<number>;
        
        constructor (operationDistinction: number, referenceTime: number, contentChangeDeadline: number, contentChangeDeadlineDay: number, orderDeadline: number, monthlyResults: number,
        dailyResults: number, reservationFrameName1: string, reservationStartTime1: number, reservationEndTime1: number, reservationFrameName2: string, reservationStartTime2: number,
        reservationEndTime2: number, orderedData: number ) {
            this.operationDistinction = ko.observable(operationDistinction),
            this.referenceTime = ko.observable(referenceTime),
            this.contentChangeDeadline = ko.observable(contentChangeDeadline),
            this.contentChangeDeadlineDay = ko.observable(contentChangeDeadlineDay),
            this.orderDeadline = ko.observable(orderDeadline),
            this.monthlyResults = ko.observable(monthlyResults),
            this.dailyResults = ko.observable(dailyResults),
            this.reservationFrameName1 = ko.observable(reservationFrameName1),
            this.reservationStartTime1 = ko.observable(reservationStartTime1),
            this.reservationEndTime1 = ko.observable(reservationEndTime1),
            this.reservationFrameName2 = ko.observable(reservationFrameName2),
            this.reservationStartTime2 = ko.observable(reservationStartTime2),
            this.reservationEndTime2 = ko.observable(reservationEndTime2),
            this.orderedData = ko.observable(orderedData),
            this.contentChangeDeadline.subscribe(data => {
                if(data == 1) {
                    nts.uk.ui._viewModel.content.visibleContentChangeDeadline(true);
                    return;
                }
                nts.uk.ui._viewModel.content.visibleContentChangeDeadline(false);
            });
            this.reservationEndTime1.subscribe(() => {
                $('#end1').ntsError('clear');
            });
            this.reservationEndTime2.subscribe(() => {
                    $('#end2').ntsError('clear');
            });
        };
    }

    class BentoReservation {

        //予約の運用区別
        operationClassification: KnockoutObservable<number>;

        //基準時間
        referenceTime: KnockoutObservable<number> = ko.observable(-1);

        //予約済みの内容変更期限内容
        changeDeadlineContents: KnockoutObservable<number> = ko.observable(-1);

        //予約済みの内容変更期限日数
        changeDeadlineDays: KnockoutObservable<number> = ko.observable(-1);

        //注文済み期限方法
        orderDeadline: KnockoutObservable<number> = ko.observable(-1);

        //月別実績の集計
        monthlyResults: KnockoutObservable<number> = ko.observable(-1);

        //日別実績の集計
        dailyResults: KnockoutObservable<number> = ko.observable(-1);

        //注文済みデータ
        orderedData: KnockoutObservable<number> = ko.observable(-1);

        reservationFrameName1: KnockoutObservable<string> = ko.observable("");

        reservationStartTime1: KnockoutObservable<number> = ko.observable(-1);

        reservationEndTime1: KnockoutObservable<number> = ko.observable(-1);

        reservationFrameName2: KnockoutObservable<string> = ko.observable("");

        reservationStartTime2: KnockoutObservable<number> = ko.observable(-1);

        reservationEndTime2: KnockoutObservable<number> = ko.observable(-1);

        constructor(operationClassification: KnockoutObservable<number>, referenceTime: KnockoutObservable<number>,
                    changeDeadlineContents: KnockoutObservable<number>, changeDeadlineDays: KnockoutObservable<number>,
                    orderDeadline: KnockoutObservable<number>, monthlyResults: KnockoutObservable<number>,
                    dailyResults: KnockoutObservable<number>, orderedData: KnockoutObservable<number>,
                    reservationFrameName1: KnockoutObservable<string>, reservationStartTime1: KnockoutObservable<number>,
                    reservationEndTime1: KnockoutObservable<number>, reservationFrameName2: KnockoutObservable<string>,
                    reservationStartTime2: KnockoutObservable<number>, reservationEndTime2: KnockoutObservable<number>,) {
            this.operationClassification = operationClassification;
            this.referenceTime = referenceTime;
            this.changeDeadlineContents = changeDeadlineContents;
            this.changeDeadlineDays = changeDeadlineDays;
            this.orderDeadline = orderDeadline;
            this.monthlyResults = monthlyResults;
            this.dailyResults = dailyResults;
            this.orderedData = orderedData;
            this.reservationFrameName1 = reservationFrameName1;
            this.reservationStartTime1 = reservationStartTime1;
            this.reservationEndTime1 = reservationEndTime1;
            this.reservationFrameName2 = reservationFrameName2;
            this.reservationStartTime2 = reservationStartTime2;
            this.reservationEndTime2 = reservationEndTime2;

        }
    }

}
