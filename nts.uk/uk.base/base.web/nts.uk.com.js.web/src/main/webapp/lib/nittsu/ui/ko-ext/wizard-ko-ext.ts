/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {
    
    /**
     * Wizard binding handler
     */
    class WizardBindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data
            var data = valueAccessor();
            // Get step list
            var options: Array<any> = ko.unwrap(data.steps);
            var theme: string = ko.unwrap(data.theme);
            var cssClass: string = "nts-wizard " + "theme-" + theme;
            // Container
            var container = $(element);

            // Create steps
            for (var i = 0; i < options.length; i++) {
                var contentClass: string = ko.unwrap(options[i].content);
                var htmlStep = container.children('.steps').children(contentClass).html();
                var htmlContent = container.children('.contents').children(contentClass).html();
                container.append('<h1 class="' + contentClass + '">' + htmlStep + '</h1>');
                container.append('<div>' + htmlContent + '</div>');
            }
            var icon = container.find('.header .image').data('icon');

            // Remove html
            var header = container.children('.header');
            container.children('.header').remove();
            container.children('.steps').remove();
            container.children('.contents').remove();

            // Create wizard
            container.steps({
                headerTag: "h1",
                bodyTag: "div",
                transitionEffect: "slideLeft",
                stepsOrientation: "vertical",
                titleTemplate: '<div>#title#</div>',
                enablePagination: false,
                enableFinishButton: false,
                autoFocus: false,
                onStepChanged: function() {
                    // Remove old class.
                    container.children('.steps').children('ul').children('li').removeClass('step-current');
                    container.children('.steps').children('ul').children('li').removeClass('step-prev');
                    container.children('.steps').children('ul').children('li').removeClass('step-next');

                    // Add new class.
                    container.children('.steps').children('ul').children('.done').addClass('disabled');
                    container.children('.steps').children('ul').children('.current').addClass('step-current');
                    container.children('.steps').children('ul').children('.done').addClass('step-prev');
                    container.children('.steps').children('ul').children('.step-current').nextAll('li').not('.done').addClass('step-next');

                    return true;
                }
            }).data("length", options.length);

            // Add default class
            container.addClass(cssClass);
            container.children('.steps').children('ul').children('li').children('a').before('<div class="nts-steps"></div>');
            container.children('.steps').children('ul').children('li').children('a').addClass('nts-step-contents');
            //container.children('.steps').children('ul').children('.first').addClass('begin');
            container.children('.steps').children('ul').children('.last').addClass('end');
            container.children('.steps').children('ul').children('li').not('.begin').not('.end').children('.nts-steps').addClass('nts-steps-middle');
            container.find('.nts-steps-middle').append('<div class="nts-vertical-line"></div><div class="nts-bridge"><div class="nts-point"></div><div class="nts-horizontal-line"></div></div>')

            // Remove old class
            container.children('.steps').children('ul').children('li').removeClass('step-current');
            container.children('.steps').children('ul').children('li').removeClass('step-prev');
            container.children('.steps').children('ul').children('li').removeClass('step-next');

            // Add new class
            container.children('.steps').children('ul').children('.current').addClass('step-current');
            container.children('.steps').children('ul').children('.done').addClass('step-prev');
            container.children('.steps').children('ul').children('.step-current').nextAll('li').not('.done').addClass('step-next');

            // Remove content
            container.find('.actions').hide();

            // Add Header
            container.children('.steps').prepend(header);
            container.find('.header .image').attr('style', 'background-image: url("' + icon + '")');
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
        }
    }
    
    ko.bindingHandlers['ntsWizard'] = new WizardBindingHandler();
}