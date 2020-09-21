/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.ui.at.ksu002.a {

	const template = `
		<div class="cf" data-bind="ntsDatePicker: { 
			value: yearMonth,
			dateFormat: 'yearmonth' ,
		 	valueFormat: 'YYYYMM',
	  		fiscalMonthsMode: true,
			defaultClass: 'round-orange',
			showJumpButtons: true  }">
		</div>
		
		<div class="title-label">
			<span data-bind="i18n: 'KSU002_23'"></span>
			<span data-bind="i18n: 'KSU002_7'"></span>
		</div>
		
        <div data-bind="ntsComboBox: {
            width: '200px',
            name: $component.$i18n('KSU002_22'),
            value: ko.observable(''),
            options: ko.observableArray([]),
            optionsValue: 'id',
            optionsText: 'title',
            editable: false,
            selectFirstIfNull: true,
            columns: [
                { prop: 'title', length: 10 },
            ]}"></div>

		<div class="title-label">
			<span data-bind="i18n: 'KSU002_6'"></span>
			<span data-bind="i18n: 'KSU002_7'"></span>
		</div>

		<div class="cf" data-bind="ntsSwitchButton: {
			name: $i18n('KSU002_6'),
			value: ko.observable(1),
			options: [
				{ code: 1, name: $i18n('KSU002_8') },
				{ code: 2, name: $i18n('KSU002_9') }
			],
			optionsText: 'name',
			optionsValue: 'code' }"></div>
					
		<style type="text/css" rel="stylesheet">
            .title-date {
				margin: 5px 0;
				border: 1px solid #cccccc;
				background-color: #edfac2;
				border-radius: 5px;
				padding: 6px;
				display: inline-block;
			}
			.title-date>div {
				float: left;
				display: block;
			}
			.title-date>div.title-label {
				padding: 0 10px;
				line-height: 32px;
			}
			.title-date .nts-switch-button {
				min-width: 60px;
				min-height: 32px;
			}
			.title-date .nts-datepicker-wrapper>input,
			.title-date .nts-datepicker-wrapper>button {
				vertical-align: top;
			}
			.title-date .nts-datepicker-wrapper>input {
				height: 20px;
			}
			.title-date .nts-datepicker-wrapper>button {
				height: 29px;
			}
			.title-date .nts-datepicker-wrapper.arrow-bottom:before,
			.title-date .nts-datepicker-wrapper.arrow-bottom:after {
				left: 45px;
			}
        </style>
        <style type="text/css" rel="stylesheet" data-bind="html: $component.style"></style>
    `;

	interface Params {
		
	}
	
	const COMPONENT_NAME = 'title-date';
	
    @handler({
        bindingName: COMPONENT_NAME,
        validatable: true,
        virtual: false
    })
    export class TitleDateComponentBindingHandler implements KnockoutBindingHandler {
        init(element: any, valueAccessor: () => any, allBindingsAccessor: KnockoutAllBindingsAccessor, viewModel: any, bindingContext: KnockoutBindingContext): void | { controlsDescendantBindings: boolean; } {
            const name = COMPONENT_NAME;
            const baseDate = valueAccessor();
            const mode = allBindingsAccessor.get('mode');

            element.classList.add('cf');
            element.classList.add('title-date');

            ko.applyBindingsToNode(element, { component: { name: name, params: { mode: mode, baseDate: baseDate } } }, bindingContext);

            return { controlsDescendantBindings: true };
        }
    }

	@component({
		name: COMPONENT_NAME,
		template
	})
	export class TitleDateComponent extends ko.ViewModel {
		public yearMonth: KnockoutObservable<string> = ko.observable(moment().format('YYYYMM'));
		
		constructor(private params: Params) {
			super();
		}

		mounted() {
		}
	}
}
