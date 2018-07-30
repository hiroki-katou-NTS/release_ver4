/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    import count = nts.uk.text.countHalf;

    const WoC = 9,
        MINWIDTH = 'auto',
        TAB_INDEX = 'tabindex',
        KEYPRESS = 'keypress',
        KEYDOWN = 'keydown',
        FOCUS = 'focus',
        VALIDATE = 'validate',
        OPENDDL = 'openDropDown',
        CLOSEDDL = 'closeDropDown',
        OPENED = 'dropDownOpened',
        IGCOMB = 'igCombo',
        OPTION = 'option',
        ENABLE = 'enable',
        EDITABLE = 'editable',
        DROPDOWN = 'dropdown',
        COMBOROW = 'nts-combo-item',
        COMBOCOL = 'nts-column nts-combo-column',
        DATA = '_nts_data',
        CHANGED = '_nts_changed',
        SHOWVALUE = '_nts_show',
        NAME = '_nts_name',
        CWIDTH = '_nts_col_width',
        VALUE = '_nts_value',
        REQUIRED = '_nts_required';


    export class ComboBoxBindingHandler implements KnockoutBindingHandler {
        init = (element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext) => {
            let template = '',
                $element = $(element),
                accessor = valueAccessor(),
                // dataSource of igCombo
                options: Array<any> = ko.unwrap(accessor.options),
                // enable or disable
                enable: boolean = _.has(accessor, 'enable') ? ko.unwrap(accessor.enable) : true,
                // mode of dropdown
                editable: boolean = _.has(accessor, 'editable') ? ko.unwrap(accessor.editable) : false,
                // require or no
                required: boolean = _.has(accessor, 'required') ? ko.unwrap(accessor.required) : false,
                // textKey
                optionsText: string = _.has(accessor, 'optionsText') ? ko.unwrap(accessor.optionsText) : null,
                // valueKey
                optionsValue: string = _.has(accessor, 'optionsValue') ? ko.unwrap(accessor.optionsValue) : null,
                // columns
                columns: Array<any> = _.has(accessor, 'columns') ? ko.unwrap(accessor.columns) : [{ prop: optionsText }],
                visibleItemsCount = _.has(accessor, 'visibleItemsCount') ? ko.unwrap(accessor.visibleItemsCount) : 5,
                dropDownAttachedToBody: boolean = _.has(accessor, 'dropDownAttachedToBody') ? ko.unwrap(accessor.dropDownAttachedToBody) : false,
                $show = $('<div>', {
                    'class': 'nts-toggle-dropdown',
                    'style': 'padding-left: 2px; color: #000; height: 29px',
                    click: (evt) => {
                        if ($element.data(IGCOMB)) {
                            if ($element.igCombo(OPENED)) {
                                $element.igCombo(CLOSEDDL);
                                evt.stopPropagation();
                            } else {
                                $element.igCombo(OPENDDL, () => { }, true, true);
                                evt.stopPropagation();
                            }
                        }
                    }
                });

            // filter valid options
            options = _(options)
                .filter(x => _.isObject(x))
                .value();

            // fix show dropdown in igGrid
            if (!!$element.closest(".ui-iggrid").length) {
                dropDownAttachedToBody = true;
            }

            // generate template if has columns
            if (_.isArray(columns)) {
                template = `<div class='${COMBOROW}'>${_.map(columns, (c, i) => `<div data-ntsclass='${c.toggle || ''}' class='${COMBOCOL}-${i} ${c.prop.toLowerCase()} ${c.toggle || ''}'>\$\{${c.prop}\}&nbsp;</div>`).join('')}</div>`;
            }

            if (!$element.attr('tabindex')) {
                $element.attr('tabindex', 0);
            }

            $element
                // delegate event for change template (on old filter box)
                .on(SHOWVALUE, (evt) => {
                    let data = $element.data(DATA),
                        cws = data[CWIDTH],
                        ks = _.keys(cws);

                    let option = _.find(data[DATA], t => t[optionsValue] == data[VALUE]),
                        _template = template;

                    if (option) {
                        _.each(_.keys(option), k => {
                            _template = _template.replace(`\$\{${k}\}`, option[k]);
                        });

                        $show.html(_template);

                        _.each(ks, k => {
                            $show.find(`.${k.toLowerCase()}:not(:last-child)`)
                                .css('width', `${cws[k] * WoC}px`);

                            $show.find(`.${k.toLowerCase()}`)
                                .css('height', '31px')
                                .css('line-height', '27px')
                                .find('.nts-column:last-child').css('margin-right', 0);;
                        });
                    } else {
                        $show.empty();
                    }
                })
                // define event changed for save default data
                .on(CHANGED, (evt, key, value = undefined) => {
                    let data = $element.data(DATA) || {};
                    {
                        data[key] = value;
                        $element.data(DATA, data);
                    }
                })
                // define event validate for check require
                .on(VALIDATE, (evt, ui) => {
                    let data = $element.data(DATA),
                        value = data[VALUE];

                    if ((ui ? data[CHANGED] : true) && data[ENABLE] && data[REQUIRED] && (_.isEmpty(String(value).trim()) || _.isNil(value))) {
                        $element
                            .addClass('error')
                            .ntsError("set", resource.getMessage("FND_E_REQ_SELECT", [data[NAME]]), "FND_E_REQ_SELECT");
                    } else {
                        $element
                            .removeClass('error')
                            .ntsError("clear");
                    }
                })
                // delegate open or close event on enter key
                .on(KEYDOWN, (evt, ui) => {
                    if ($element.data(IGCOMB)) {
                        if ([13].indexOf(evt.which || evt.keyCode) > -1) {
                            // fire click of igcombo-button
                            $element
                                .find('.ui-igcombo-button')
                                .trigger('click');
                        } else if ([32, 38, 40].indexOf(evt.which || evt.keyCode) > -1) {
                            if (!$element.igCombo(OPENED)) {
                                // fire click of igcombo-button
                                $element
                                    .find('.ui-igcombo-button')
                                    .trigger('click');
                            }
                        }
                    }
                })
                .igCombo({
                    loadOnDemandSettings: {
                        enabled: true,
                        pageSize: 15
                    },
                    dataSource: options,
                    placeHolder: '',
                    textKey: 'nts_' + optionsText,
                    valueKey: optionsValue,
                    mode: editable ? EDITABLE : DROPDOWN,
                    disabled: !ko.toJS(enable),
                    enableClearButton: false,
                    itemTemplate: template,
                    dropDownWidth: "auto",
                    tabIndex: $element.attr('tabindex') || 0,
                    visibleItemsCount: visibleItemsCount,
                    dropDownAttachedToBody: dropDownAttachedToBody,
                    rendered: function(evt, ui) {
                        $element
                            .find('.ui-igcombo')
                            .css('background', '#f6f6f6')
                            .find('.ui-igcombo-fieldholder').hide();

                        $element
                            .find('.ui-igcombo-hidden-field')
                            .parent()
                            .append($show)
                            .css('overflow', 'hidden');
                    },
                    itemsRendered: (evt, ui) => {
                        let data = $element.data(DATA) || {},
                            cws = data[CWIDTH] || [],
                            ks = _.keys(cws);

                        // calc new size of template columns
                        _.each(ks, k => {
                            $("[class*=ui-igcombo-orientation]")
                                .find(`.${k.toLowerCase()}:not(:last-child)`)
                                .css('width', `${cws[k] * WoC}px`);
                        });
                    },
                    selectionChanged: (evt, ui) => {
                        if (!_.size(ui.items)) {
                            $element.trigger(CHANGED, [VALUE, null]);
                        } else {
                            let value = ui.items[0]["data"][optionsValue];

                            $element.trigger(CHANGED, [VALUE, value]);
                        }
                    },
                    dropDownClosed: (evt, ui) => {
                        // check flag changed for validate
                        $element.trigger(CHANGED, [CHANGED, true]);

                        setTimeout(() => {
                            let data = $element.data(DATA);

                            // select first if !select and !editable
                            if (!data[EDITABLE] && _.isNil(data[VALUE])) {
                                $element.trigger(CHANGED, [VALUE, $element.igCombo('value')]);
                                //reload data
                                data = $element.data(DATA);
                            }

                            // set value on select
                            accessor.value(data[VALUE]);

                            // validate if required
                            $element
                                .trigger(VALIDATE, [true])
                                .trigger(SHOWVALUE)
                                .focus();
                        }, 10);
                    },
                    dropDownOpening: (evt, ui) => {
                        let data = $element.data(DATA),
                            cws = data[CWIDTH],
                            ks = _.keys(cws);

                        // move searchbox to list
                        $element
                            .find('.ui-igcombo-fieldholder')
                            .prependTo(ui.list);

                        // show searchbox if editable
                        let $input = ui.list
                            .find('.ui-igcombo-fieldholder')
                            .css('height', !!data[EDITABLE] ? '' : '0px')
                            .css('padding', !!data[EDITABLE] ? '3px' : '')
                            .css('background-color', !!data[EDITABLE] ? '#f6f6f6' : '')
                            .show()
                            .find('input')
                            .css('width', '0px')
                            .css('height', !!data[EDITABLE] ? '29px' : '0px')
                            .css('text-indent', !!data[EDITABLE] ? '0' : '-99999px')
                            .css('border', !!data[EDITABLE] ? '1px solid #ccc' : 'none');

                        if (!$input.data('_nts_bind')) {
                            $input
                                .on(KEYDOWN, (evt, ui) => {
                                    if ([13].indexOf(evt.which || evt.keyCode) > -1) {
                                        if ($element.data(IGCOMB)) {
                                            // fire click of igcombo-button
                                            $element
                                                .find('.ui-igcombo-button')
                                                .trigger('click');
                                        }
                                    }
                                })
                                .data('_nts_bind', true)
                                .attr('tabindex', -1);
                        }

                        // calc new size of template columns
                        _.each(ks, k => {
                            $(ui.list).find(`.${k.toLowerCase()}${_.size(ks) == 1 ? '' : ':not(:last-child)'}`)
                                .css('width', `${cws[k] * WoC}px`);
                        });

                        // fix min width of dropdown = $element.width();
                        $(ui.list)
                            .css('min-width', $element.width() + 'px')
                            .find('.nts-column:last-child')
                            .css('margin-right', 0);

                        setTimeout(() => {
                            $input.css('width', ($(ui.list).width() - 6) + 'px')
                        }, 25);
                    }
                })
                .trigger(CHANGED, [DATA, options])
                .trigger(CHANGED, [TAB_INDEX, $element.attr(TAB_INDEX) || 0])
                .addClass('ntsControl')
                .on('blur', () => { $element.css('box-shadow', ''); })
                .on('focus', () => {
                    $element
                        .css('outline', 'none')
                        .css('box-shadow', '0 0 1px 1px #0096f2');
                });
        }

        update = (element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext) => {
            let ss = new Date().getTime(),
                $element = $(element),
                accessor = valueAccessor(),
                width = _.has(accessor, 'width') ? ko.unwrap(accessor.width) : undefined,
                name: string = ko.unwrap(accessor.name),
                value: any = ko.unwrap(accessor.value),
                // dataSource of igCombo
                options: Array<any> = ko.unwrap(accessor.options),
                // init default selection
                selectFirstIfNull = !(ko.unwrap(accessor.selectFirstIfNull) === false), // default: true
                // enable or disable
                enable: boolean = _.has(accessor, 'enable') ? ko.unwrap(accessor.enable) : true,
                // mode of dropdown
                editable: boolean = _.has(accessor, 'editable') ? ko.unwrap(accessor.editable) : false,
                // require or no
                required: boolean = _.has(accessor, 'required') ? ko.unwrap(accessor.required) : false,
                // textKey
                optionsText: string = _.has(accessor, 'optionsText') ? ko.unwrap(accessor.optionsText) : null,
                // valueKey
                optionsValue: string = _.has(accessor, 'optionsValue') ? ko.unwrap(accessor.optionsValue) : null,
                // columns
                columns: Array<any> = _.has(accessor, 'columns') ? ko.unwrap(accessor.columns) : [{ prop: optionsText }];

            // filter valid options
            options = _(options)
                .filter(x => _.isObject(x))
                .value();

            let props = columns.map(c => c.prop),
                // list key value
                vkl = _(options)
                    .map(m => {
                        if (!!m) {
                            return _(m)
                                .keys(m)
                                .map(t => ({
                                    k: t,
                                    w: _.max([count(_.trim(m[t])), (_.find(columns, c => c.prop == t) || {}).length || 0])
                                }))
                                .filter(m => props.indexOf(m.k) > -1)
                                .keyBy('k')
                                .mapValues('w')
                                .value();
                        }

                        return undefined;
                    }).filter(f => !!f).value(),
                cws = _(props)
                    .map(p => ({ k: p, v: _.maxBy(vkl, p) }))
                    .map(m => ({ k: m.k, v: (m.v || {})[m.k] || 0 }))
                    .keyBy('k')
                    .mapValues('v')
                    .value();

            // map new options width nts_[optionsText]
            // (show new prop on filter box)
            options = _(options)
                .map(m => {
                    let c = {},
                        k = ko.toJS(m),
                        t = k[optionsText],
                        v = k[optionsValue],
                        n = _.omit(k, [optionsValue]),
                        nt = _.map(props, p => k[p]).join(' ').trim();

                    c[optionsValue] = !_.isNil(v) ? v : '';
                    c['nts_' + optionsText] = nt || t || ' ';

                    return _.extend(n, c);
                })
                .value();

            // check value has exist in option
            let vio = _.find(options, f => f[optionsValue] == value);

            if (!vio) {
                if (selectFirstIfNull) {
                    vio = _.head(options);

                    if (!vio) {
                        value = undefined;
                    } else {
                        value = vio[optionsValue];
                    }
                } else {
                    value = undefined;
                }
                accessor.value(value);
            }

            // check flag changed for validate
            if (_.has($element.data(DATA), VALUE)) {
                $element.trigger(CHANGED, [CHANGED, true]);
            }

            // save change value
            $element
                .trigger(CHANGED, [CWIDTH, cws])
                .trigger(CHANGED, [NAME, name])
                .trigger(CHANGED, [VALUE, value])
                .trigger(CHANGED, [ENABLE, enable])
                .trigger(CHANGED, [EDITABLE, editable])
                .trigger(CHANGED, [REQUIRED, required]);

            // if igCombo has init
            if ($element.data("igCombo")) {
                let data = $element.data(DATA),
                    olds = data[DATA];

                // change dataSource if changed
                if (!_.isEqual(olds, options)) {
                    $element.igCombo(OPTION, "dataSource", options);
                }

                $element
                    // enable or disable 
                    .igCombo(OPTION, "disabled", !enable)
                    // set new value
                    .igCombo("value", value);

                if (!enable) {
                    $element.removeAttr(TAB_INDEX);
                } else {
                    $element.attr(TAB_INDEX, data[TAB_INDEX]);
                }

                // validate if has dataOptions
                $element
                    .trigger(VALIDATE, [true]);

                if (_.isNil(value)) {
                    $element
                        .igCombo("deselectAll");
                }

                // set width of container
                if (width) {
                    if (width != MINWIDTH) {
                        $element.igCombo("option", "width", width);
                    } else { // auto width
                        $element
                            .igCombo("option", "width", (_.sum(_.map(cws, c => c)) * WoC + 60) + 'px');
                    }
                }
            }

            // set new dataSource to data;
            $element
                .trigger(CHANGED, [DATA, options])
                .trigger(SHOWVALUE);
        }
    }

    ko.bindingHandlers['ntsComboBox'] = new ComboBoxBindingHandler();
}
