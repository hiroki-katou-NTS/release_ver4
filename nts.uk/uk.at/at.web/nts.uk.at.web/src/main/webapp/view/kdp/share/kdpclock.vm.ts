/// <reference path="../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kdp.share {
	const template = `
	<div class="panel" id="stamp-date"
		data-bind="style: {
			'color': ko.toJS(settings).textColor,
			'background-color': ko.toJS(settings).backGroundColor
		}">
    	<span id="stamp-date-text" data-bind="date: time, format: 'YYYY年M月D日(ddd)'"></span>
    </div>
    <div class="panel" id="stamp-time"
		data-bind="style: {
			'color': ko.toJS(settings).textColor,
			'background-color': ko.toJS(settings).backGroundColor
		}">
        <span id="stamp-time-text" data-bind="date: time, format: 'HH:mm'"></span>
    </div>
	<div class="button-group" data-bind="if: !!events">
		<div data-bind="if: !!ko.unwrap(events.setting.show)">
			<button class="btn-setting" data-bind="icon: 5, click: events.setting.click"></button>
		</div>
		<div data-bind="if: !!ko.unwrap(events.company.show)">
			<button class="btn-company proceed small" data-bind="i18n: 'KDP003_2', click: events.company.click"></button>
		</div>
	</div>
`;

	@component({
		name: 'stamp-clock',
		template
	})
	export class StampClock extends ko.ViewModel {
		time: KnockoutObservable<Date> = ko.observable(new Date());
		settings: KnockoutObservable<StampColor> = ko.observable({
			textColor: 'rgb(255, 255, 255)',
			backGroundColor: 'rgb(0, 51, 204)'
		});

		events!: ClickEvent;

		created(params?: StampClocParam) {
			const vm = this;

			if (params) {
				const { setting, events } = ko.toJS(params);
				const { textColor, backGroundColor } = setting || { textColor: 'rgb(255, 255, 255)', backGroundColor: 'rgb(0, 51, 204)' };

				// convert setting event to binding object
				if (_.isFunction(events.setting)) {
					const click = events.setting;
					
					events.setting = {
						click,
						show: true
					};
				}
				
				// convert company event to binding object
				if (_.isFunction(events.company)) {
					const click = events.company;
					
					events.company = {
						click,
						show: true
					};
				}

				vm.events = events;
				vm.settings({ textColor, backGroundColor });
			}

			setInterval(() => vm.time(vm.$date.now()), 100);
		}

		mounted() {
			const vm = this;

			$(vm.$el).attr('id', 'stamp-header');
		}
	}

	export interface StampClocParam {
		events?: ClickEvent;
		setting?: StampColor;
	}

	export interface ClickEvent {
		setting: () => void | {
			show: KnockoutObservable<boolean>;
			click: () => void;
		};
		company: () => void | {
			show: KnockoutObservable<boolean>;
			click: () => void;
		};
	}

	export interface StampColor {
		textColor: string;
		backGroundColor: string;
	}
}