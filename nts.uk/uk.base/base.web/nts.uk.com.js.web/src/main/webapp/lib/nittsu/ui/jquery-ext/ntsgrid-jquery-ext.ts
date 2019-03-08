/// <reference path="../../reference.ts"/>

interface JQuery {
    ntsGrid(options: any, ...params: Array<any>): any;
}

module nts.uk.ui.jqueryExtentions {

    export module ntsGrid {
        let storage;
        module dist {
            export let REMOTE: string = "Remote";
            
            /**
             * Query.
             */
            export function query(features: any) {
                storage = new Local();
                let store = feature.find(features, feature.STORAGE);
                if (!store) return;
                if (store.type === REMOTE) {
                    storage = new Remote(store.loadPath, store.savePath);
                }
            }
            
            export class Local {
                /**
                 * Get item.
                 */
                getItem(key: any) {
                    let dfd = $.Deferred();
                    dfd.resolve(uk.localStorage.getItem(key));
                    return dfd.promise();
                }
                
                /**
                 * Set item.
                 */
                setItemAsJson(key: any, value: any) {
                    let dfd = $.Deferred();
                    uk.localStorage.setItemAsJson(key, value);
                    dfd.resolve(true);
                    return dfd.promise();
                }
            }
            
            export class Remote {
                loadPath: string;
                savePath: string;
                constructor(loadPath: string, savePath: string) {
                    this.loadPath = loadPath;
                    this.savePath = savePath;
                }
                
                /**
                 * Get item.
                 */
                getItem(key: any) {
                    let dfd = $.Deferred();
                    request.ajax(this.loadPath, { value: key }).done(function(widths) {
                        dfd.resolve(util.optional.of(widths));
                    });
                    return dfd.promise();
                }
                
                /**
                 * Set item.
                 */
                setItemAsJson(key: any, value: any) {
                    let dfd = $.Deferred();
                    request.ajax(this.savePath, { key: key, columns: value }).done(function(res) {
                        dfd.resolve(res);
                    });
                    return dfd.promise();
                }
            }
        }
        
        $.fn.ntsGrid = function(options: any) {
            var self = this;
            var $self = $(self);
            
            if (typeof options === "string") {
                return functions.ntsAction($self, options, [].slice.call(arguments).slice(1));
            }
            
            if (options.ntsControls === undefined) {
                $self.igGrid(options);
                return;
            }
            
            dist.query(options.ntsFeatures);
            if (options.hidePrimaryKey) {
                _.forEach(options.columns, function(c) {
                    if (c.key === options.primaryKey) {
                        c.width = "1px";
                        if (columnSize.exists($self)) {
                            columnSize.save($self, c.key, 1);
                        }
                        feature.merge(options, feature.RESIZING, columnSize.createResizeOptions(c.key));
                        return false;
                    }
                });
            }
            let flatCols = validation.scanValidators($self, options.columns); 
            // Cell color
            let cellFormatter = $self.data(internal.CELL_FORMATTER);
            if (!cellFormatter) {
                cellFormatter = new color.CellFormatter($self, options.features, options.ntsFeatures, flatCols);
                $self.data(internal.CELL_FORMATTER, cellFormatter);
            }
            
            $self.addClass('compact-grid nts-grid');
            if ($self.closest(".nts-grid-wrapper").length === 0) {
                $self.wrap($("<div class='nts-grid-wrapper'/>"));
            }
            
            let columnControlTypes = {};
            let columnSpecialTypes = {};
            let bounceCombos = {};
            let cbHeaderColumns = [];
            let cbSelectionColumns = {};
            let formatColumn = function(column: any) {
                if (column.hidden) return column;
                if (column.showHeaderCheckbox) {
                    column.headerText = ntsControls.createHeaderCheckbox({ 
                        controlDef: {
                            options: { value: 1, text: column.headerText },
                            optionsValue: 'value',
                            optionsText: 'text'
                        }
                    }, column.key);
                    cbHeaderColumns.push(column.key);
                    cbSelectionColumns[column.key] = { 
                        selectAll: false, quantity: 0, 
                        onSelect: function(value) {
                            var fs = this;
                            let hiddenCount = fs.hiddenRows ? fs.hiddenRows.length : 0;
                            let disableCount = fs.disableRows ? fs.disableRows.size : 0;
                            if (value && ++fs.quantity === (options.dataSource.length - hiddenCount - disableCount)) {
                                fs.th.find(".nts-grid-header-control-" + column.key).find("input[type='checkbox']").prop("checked", true);
                                fs.selectAll = true;
                            } else if (!value && fs.quantity > 0) {
                                fs.quantity--;
                                if (fs.selectAll) {
                                    fs.th.find(".nts-grid-header-control-" + column.key).find("input[type='checkbox']").prop("checked", false);
                                    fs.selectAll = false;
                                }
                            }
                        }
                    };
                    
                    if (column.hiddenRows) {
                        cbSelectionColumns[column.key].hiddenRows = column.hiddenRows;
                    }
                }
                // Have column group
                if (!util.isNullOrUndefined(column.group)) {
                    let cols = _.map(column.group, formatColumn);
                    column.group = cols;
                    return column;  
                }
                // Special column types
                specialColumn.ifTrue(columnSpecialTypes, column, bounceCombos, flatCols);
                
                // Control types
                if (column.ntsControl === undefined) {
                    columnControlTypes[column.key] = ntsControls.TEXTBOX;
                    return cellFormatter.format(column);
                }
                if (column.ntsControl === ntsControls.LABEL) {
                    ntsControls.drawLabel($self, column, cellFormatter);
                    columnControlTypes[column.key] = ntsControls.LABEL;
                    return cellFormatter.format(column, true);
                }
                
                var controlDef = _.find(options.ntsControls, function(ctl: any) {
                    return ctl.name === column.ntsControl;
                });
                if (!util.isNullOrUndefined(controlDef)) columnControlTypes[column.key] = controlDef.controlType;
                else {
                    columnControlTypes[column.key] = ntsControls.TEXTBOX;
                    return cellFormatter.format(column);
                }
    
                // Format column
                column.formatter = function(value, rowObj) {
                    if (util.isNullOrUndefined(rowObj)) return value;
                    
                    var rowId = rowObj[$self.igGrid("option", "primaryKey")];
                    // Update
                    var update = (val) => {
                        if (!util.isNullOrUndefined($self.data("igGrid"))) {
                            updating.updateCell($self, rowId, column.key, column.dataType !== 'string' ? val : val.toString());
                            if (options.autoCommit === undefined || options.autoCommit === false) {
                                var updatedRow = $self.igGrid("rowById", rowId, false);
                                $self.igGrid("commit");
                                if (updatedRow !== undefined) $self.igGrid("virtualScrollTo", $(updatedRow).data("row-idx"));
                            }
                        }
                    };
                    // Delete row
                    var deleteRow = () => {
                        if ($self.data("igGrid") !== null) {
                            $self.data("ntsRowDeleting", true);
                            $self.data("igGridUpdating").deleteRow(rowId);
                        }
                    };
                    // Get control
                    var ntsControl = ntsControls.getControl(controlDef.controlType);
                    var $cell = internal.getCellById($self, rowId, column.key);
                    var isEnable;
                    if ($cell) {
                        isEnable = $cell.find("." + ntsControl.containerClass()).data("enable");
                    }
                    isEnable = isEnable !== undefined ? isEnable : controlDef.enable === undefined ? true : controlDef.enable;
                    var data: any = {
                        rowId: rowId,
                        columnKey: column.key,
                        controlDef: controlDef,
                        update: update,
                        deleteRow: deleteRow,
                        initValue: value,
                        rowObj: rowObj,
                        showHeaderCheckbox: column.showHeaderCheckbox,
                        enable: isEnable
                    };
                    if (!util.isNullOrUndefined(column.tabIndex)) {
                        data.tabIndex = column.tabIndex;
                    }
                    
                    let back;
                    if (back = bounceCombos[column.key]) {
                        data.bounce = back;
                    }
                    var controlCls = "nts-grid-control-" + column.key + "-" + rowId;
                    var $container = $("<div/>").append($("<div/>").addClass(controlCls).css("height", ntsControls.HEIGHT_CONTROL));
                    var $_self = $self;
                    setTimeout(function() {
                        var $self = $_self;   
                        let rowId = rowObj[$self.igGrid("option", "primaryKey")];
                        let $gridCell = internal.getCellById($self, rowId, column.key);
                        let gridCellChild;
                        if (!$gridCell || (gridCellChild = $gridCell.children()).length === 0) return;
                        if (gridCellChild[0].children.length === 0) {
                            if (controlDef.controlType !== ntsControls.CHECKBOX
                                || !column.hiddenRows || !column.hiddenRows.some(v => v === rowId)) {
                                let $control = ntsControl.draw(data);
                                let gridControl = $gridCell[0].querySelector("." + controlCls);
                                if (!gridControl) return;
                                gridControl.appendChild($control[0]);
                                if (controlDef.controlType === ntsControls.CHECKBOX && column.showHeaderCheckbox) {
                                    let cbSelectCols = $self.data(internal.CB_SELECTED) || {};
                                    let cbColConf = cbSelectCols[column.key]
                                    if (cbColConf) {
                                        $control.on("change", function() {
                                            cbColConf.onSelect($(this).find("input[type='checkbox']").is(":checked"));
                                        });
                                    }
                                }
                            }
                            ntsControl.$containedGrid = $self;
                            
                            // Cell state color
                            let c = {
                                id: rowId,
                                columnKey: column.key,
                                $element: $gridCell,
                                element: $gridCell[0]    
                            };
                            // Format cell
                            cellFormatter.style($self, c);
                            color.rememberDisabled($self, c);
                            color.markIfEdit($self, c);
                        }
                    }, 0);
    
                    return $container.html();
                };
                return column;
            }
            var columns = _.map(options.columns, formatColumn);
            
            options.columns = columns;
            updating.addFeature(options);
            options.autoCommit = true;
            options.tabIndex = -1;
            // Decorate editor border
            events.onCellClick($self);
            
            // Common settings
            settings.build($self, options);
            // Copy&Paste
            copyPaste.ifOn($self, options);
            events.afterRendered(options, cbSelectionColumns);
            columnSize.init($self, options.columns);
            ntsControls.bindCbHeaderColumns(options, cbHeaderColumns, cbSelectionColumns);
            
            // Group column key and its control type 
            $self.data(internal.CONTROL_TYPES, columnControlTypes);
            // Group column key and its special type
            $self.data(internal.SPECIAL_COL_TYPES, columnSpecialTypes);
            
            // Sheet
            sheet.load.setup($self, options);
            
            if (!onDemand.initial($self, options)) {
                if (!$self.data(internal.ORIG_DS)) {
                    $self.data(internal.ORIG_DS, _.cloneDeep(options.dataSource));
                }
                $self.igGrid(options);
            }
            // Window resize
            $(window).resize(function() {
                if (options.autoFitWindow) {
                    settings.setGridSize($self);
                }
                columnSize.load($self);
            });
            
            // Document click
            $(document).on(events.Handler.CLICK, function(evt) {
                if (!utils.isIgGrid($self) || !utils.isEditMode($self)) return;
                let $fixedBodyContainer: any = $self.igGrid("fixedBodyContainer");
                if (($fixedBodyContainer.length > 0 && utils.outsideGrid($fixedBodyContainer, evt.target) 
                    && utils.outsideGrid($self, evt.target))
                    || ($fixedBodyContainer.length === 0 && utils.outsideGrid($self, evt.target))) {
                    updating.endEdit($self);
                }
            });
        };
        
        module feature {
            export let UPDATING = "Updating";
            export let SELECTION = "Selection";
            export let RESIZING = "Resizing";
            export let COLUMN_FIX = "ColumnFixing";
            export let PAGING = "Paging";
            export let COPY_PASTE = "CopyPaste";
            export let CELL_EDIT = "CellEdit";
            export let CELL_COLOR = "CellColor";
            export let CELL_STATE = "CellState";
            export let ROW_STATE = "RowState";
            export let TEXT_COLOR = "TextColor";
            export let TEXT_STYLE = "TextStyle";
            export let HEADER_STYLES = "HeaderStyles";
            export let HIDING = "Hiding";
            export let SHEET = "Sheet";
            export let DEMAND_LOAD = "LoadOnDemand";
            export let STORAGE = "Storage";
            
            /**
             * Replace feature
             */
            export function replaceBy(options: any, featureName: string, newFeature: any) {
                let replaceId: number;
                _.forEach(options.features, function(feature: any, id: number) {
                    if (feature.name === featureName) {
                        replaceId = id;
                        return false;
                    }
                });
                options.features.splice(replaceId, 1, newFeature);
            }
            
            /**
             * Merge feature.
             */
            export function merge(options: any, featureName: string, feature: any) {
                let findId: number = -1;
                let obj;
                _.forEach(options.features, function(f: any, id: number) {
                    if (f.name === featureName) {
                        obj = f;
                        findId = id;
                        return false;
                    }
                });
                
                if (findId > -1) {
                    _.merge(obj, feature);
                    options.features.splice(findId, 1, obj);
                } else {
                    options.features.push(feature);
                }
            }
            
            /**
             * Check enable
             */ 
            export function isEnable(features: any, name: string) {
                return _.find(features, function(feature: any) {
                    return feature.name === name;
                }) !== undefined;
            }
            
            /**
             * Find feature
             */
            export function find(features: any, name: string) {
                return _.find(features, function(feature: any) {
                    return feature.name === name;
                });
            }
        }
        
        module updating {
            export let INPUT_CURR_SYM = "input-currency-symbol";
            export let CURR_SYM = "currency-symbol";
            
            /**
             * Add feature
             */
            export function addFeature(options: any) {
                let updateFeature = createUpdateOptions(options); 
                if (!feature.isEnable(options.features, feature.UPDATING)) {
                    options.features.push(updateFeature);
                } else {
                    feature.replaceBy(options, feature.UPDATING, createUpdateOptions(options));
                }
            }
            
            /**
             * Create update options
             */
            function createUpdateOptions(options: any) {
                let updateFeature: any = { name: feature.UPDATING, enableAddRow: false, enableDeleteRow: false, editMode: 'none' };
                if (feature.isEnable(options.ntsFeatures, feature.CELL_EDIT)) {
                    updateFeature.editMode = "cell";
                    updateFeature.editCellStarting = startEditCell;
                    updateFeature.editCellStarted = editStarted;
                    updateFeature.editCellEnding = beforeFinishEditCell;
                }
                return updateFeature;
            }
            
            /**
             * Check control
             */
            export function containsNtsControl($target: any) {
                let td = $target;
                if (!$target.prev().is("td")) td = $target.closest("td");
                return td.find("div[class*='nts-grid-control']").length > 0;
            } 
            
            /**
             * Edit cell
             */
            function startEditCell(evt: any, ui: any) {
                let selectedCell = selection.getSelectedCell($(evt.target));
                if (containsNtsControl($(evt.currentTarget)) || utils.isEnterKey(evt) || utils.isTabKey(evt)) {
                    if ($(evt.currentTarget).find("div[class*='nts-editor-container']").length > 0) return false;
                    if (util.isNullOrUndefined(selectedCell) || !utils.selectable($(evt.target))) return;
                    if (!evt.currentTarget.classList.contains("ui-iggrid-selectedcell")) {
                        $(evt.target).igGridSelection("selectCell", selectedCell.rowIndex, selectedCell.index,
                                        utils.isFixedColumnCell(selectedCell, utils.getVisibleColumnsMap($(evt.target))));
                    }
                    return false;
                } else if (utils.disabled($(evt.currentTarget))) return false;
                if (util.isNullOrUndefined(selectedCell) || !utils.selectable($(evt.target))) return;
                let $cell = $(selectedCell.element);
                if ($cell.hasClass(updating.CURR_SYM)) $cell.removeClass(updating.CURR_SYM);
                return true; 
            }
            
            /**
             * Edit started.
             */
            function editStarted(evt: any, ui: any) {
                let $grid = $(ui.owner.element);
                let valueType = validation.getValueType($grid, ui.columnKey);
                if (!evt.currentTarget) {
                    if (valueType === "TimeWithDay" || valueType === "Clock") {
                        let $editor = $(ui.editor.find("input")[0]);
                        $editor.css("text-align", "right");
                    } else if (valueType === "Currency") {
                        ui.editor.addClass(updating.INPUT_CURR_SYM);
                        let $editor = $(ui.editor.find("input")[0]);
                        $editor.css("text-align", "right");
                    }
                    return;
                }
                
                if (!util.isNullOrUndefined(ui.value) && !_.isEmpty(ui.value)) {
                    if (valueType === "TimeWithDay" || valueType === "Clock") {
                        let formatted;
                        try {
                            formatted = time.minutesBased.clock.dayattr.create(
                            time.minutesBased.clock.dayattr.parseString(ui.value).asMinutes).shortText;
                        } catch(e) { return; }
                        setTimeout(function() {
                            let $editor = $(ui.editor.find("input")[0]);
                            $editor.css("text-align", "right");
                            $editor.val(formatted).select();
                        }, 140);
                    } else if (valueType === "Currency") {
                        let groupSeparator = validation.getGroupSeparator($grid, ui.columnKey) || ",";
                        let value = text.replaceAll(ui.value, groupSeparator, "");
                        setTimeout(function() {
                            ui.editor.addClass(updating.INPUT_CURR_SYM);
                            let $editor = $(ui.editor.find("input")[0]);
                            let numb = Number(value);
                            $editor.val(isNaN(numb) ? value : numb).css("text-align", "right").select();
                        }, 140);
                    }
                } else if (valueType === "Currency") {
                    ui.editor.addClass(updating.INPUT_CURR_SYM);
                    let $editor = $(ui.editor.find("input")[0]);
                    $editor.css("text-align", "right");
                }
            }
            
            /**
             * Validate
             */
            export function onEditCell(evt: any, cell: any) {
                let $grid = fixedColumns.realGridOf($(evt.currentTarget));
                if (!utils.isEditMode($grid)) return;
                let validators: any =  $grid.data(validation.VALIDATORS);
                let fieldValidator = validators[cell.columnKey];
                if (util.isNullOrUndefined(fieldValidator)) return;
                
                let cellValue = $(cell.element).find("input:first").val();
                let result = fieldValidator.probe(cellValue);
                let $cellContainer = $(cell.element);
                errors.clear($grid, cell);
                if (!result.isValid) {
                    errors.set($grid, cell, result.errorMessage);
                }
            }
            
            /**
             * Trigger update
             */
            export function triggerCellUpdate(evt: any, cell: any) {
                var grid = evt.currentTarget;
                let $targetGrid = fixedColumns.realGridOf($(grid));
                
                if (utils.isEditMode($targetGrid) || utils.disabled($(cell.element))) return;
                if (utils.isAlphaNumeric(evt) || utils.isMinusSymbol(evt)
                    || utils.isDeleteKey(evt)) {
                    startEdit(evt, cell);
                }
            }
            
            /**
             * Start edit
             */
            function startEdit(evt: any, cell: any) {
                let $targetGrid = fixedColumns.realGridOf($(evt.currentTarget));
                if (!utils.updatable($targetGrid)) return;
                let $cell = $(cell.element);
                if ($cell.hasClass(updating.CURR_SYM)) $cell.removeClass(updating.CURR_SYM);
                utils.startEdit($targetGrid, cell);
                // Keep text contents if any, otherwise set input value
//                if ($(cell.element).text().trim() !== "") evt.preventDefault();
                if (!utils.isDeleteKey(evt)) {
                    setTimeout(function() {
                        let cellValue;
                        let char = evt.key === "Subtract" ? "-" : evt.key;
                        let $editor = $targetGrid.igGridUpdating("editorForCell", $(cell.element));
                        if (!util.isNullOrUndefined($editor.data("igTextEditor"))) {
                            $editor.igTextEditor("value", char);
                            let input = $editor.find("input")[0];
                            let len = input.value.length;
                            if ($.ig.util.isChrome || $.ig.util.isSafari) {
                                setTimeout(function() {
                                    input.setSelectionRange(len, len);
                                }, 110);
                            } else {
                                input.setSelectionRange(len, len);
                            }
                            cellValue = char;
                        } else if (!util.isNullOrUndefined($editor.data("igNumericEditor"))) {
                            cellValue = char;
                            if (!utils.isMinusSymbol(evt)) {
                                $editor.igNumericEditor("value", parseInt(cellValue));
                            } else {
                                cellValue = "-";
                                $editor.igNumericEditor("value", cellValue);
                            }
                            if ($.ig.util.isChrome || $.ig.util.isSafari) {
                                setTimeout(function() {
                                    let length = String($editor.igNumericEditor("value")).length;
                                    $editor.igNumericEditor("select", length, length); 
                                }, 110);
                            } else {
                                let length = String($editor.igNumericEditor("value")).length;
                                $editor.igNumericEditor("select", length, length); 
                            }
                        }
                        
                        // Validate
                        let validators: any =  $targetGrid.data(validation.VALIDATORS);
                        let fieldValidator = validators[cell.columnKey];
                        if (util.isNullOrUndefined(fieldValidator)) return;
                        
                        let result = fieldValidator.probe(cellValue);
                        let $cellContainer = $(cell.element);
                        errors.clear($targetGrid, cell);
                        if (!result.isValid) {
                            errors.set($targetGrid, cell, result.errorMessage);
                        }
                    }, 1);
                } else {
                    setTimeout(function() {
                        let $editor = $targetGrid.igGridUpdating("editorForCell", $(cell.element));
                        $editor.find("input").val("");
                    }, 1);
                }
                evt.preventDefault();
                evt.stopImmediatePropagation();
            }
            
            /**
             * Interrupt manipulations (e.g. cell navigation) on grid if errors occurred (setting needed).
             */
            function beforeFinishEditCell(evt: any, ui: any) {
                let $grid = $(evt.target);
                let selectedCell = selection.getSelectedCell($grid);
                let settings: any = $grid.data(internal.SETTINGS); 
                if (settings.preventEditInError
                    && utils.isEditMode($grid) && errors.any(selectedCell)) {
                    return false;
                }
                
                if (utils.isEditMode($grid) && (utils.isTabKey(evt) || utils.isEnterKey(evt) || evt.keyCode === undefined)) {
                    let gridUpdate: any = $grid.data("igGridUpdating");
                    let origValues = gridUpdate._originalValues;
                    if (!util.isNullOrUndefined(origValues)) {
                        _.forEach(Object.keys(origValues), function(colKey: any, idx: number) {
                            if (idx === 0) {
                                // Skip default update
                                gridUpdate._originalValues[colKey] = ui.value; 
                                return false;
                            }
                        });
                        _.defer(function() {
                            updating.updateCell($grid, selectedCell.id, selectedCell.columnKey, ui.value);
                        });
                    }
                }
                
                // Remove border color of editor
                let $editorContainer = $(selectedCell.element).find(errors.EDITOR_SELECTOR);
                if ($editorContainer.length > 0) $editorContainer.css(errors.NO_ERROR_STL);
                
                specialColumn.tryDo($grid, selectedCell, ui.value);
                if (ui.editor.hasClass(updating.INPUT_CURR_SYM)) {
                    $(selectedCell.element).addClass(updating.CURR_SYM);
                }
                return true;
            }
            
            /**
             * Update row and re-render all controls.
             * @Obsolete
             */
            export function _updateRow($grid: JQuery, rowId: any, visibleColumnsMap: any, updatedRowData: any) {
                if (util.isNullOrUndefined(updatedRowData) || Object.keys(updatedRowData).length === 0) return;
                $grid.igGridUpdating("updateRow", 
                            utils.parseIntIfNumber(rowId, $grid, visibleColumnsMap), updatedRowData);
            }
            
            /**
             * Update cell.
             */
            export function updateCell($grid: JQuery, rowId: any, columnKey: any, cellValue: any, allColumnsMap?: any, forceRender?: boolean) {
                let grid: any = $grid.data("igGrid");
                if (!utils.updatable($grid)) return;
                let gridUpdate: any = $grid.data("igGridUpdating");
                let origDs = $grid.data(internal.ORIG_DS);
                let autoCommit = grid.options.autoCommit;
                let columnsMap: any = allColumnsMap || utils.getColumnsMap($grid);
                let rId = utils.parseIntIfNumber(rowId, $grid, columnsMap);
                
                let valueType = validation.getValueType($grid, columnKey);
                if (!util.isNullOrUndefined(cellValue) && !_.isEmpty(cellValue) 
                    && (valueType === "TimeWithDay" || valueType === "Clock")) {
                    try {
                        cellValue = time.minutesBased.clock.dayattr.create(
                            time.minutesBased.clock.dayattr.parseString(String(cellValue)).asMinutes).shortText;
                    } catch(e) {}
                }
                
                let setting = $grid.data(internal.SETTINGS);
                let idx = setting.descriptor.keyIdxes[rId];
                if (util.isNullOrUndefined(idx)) return;
                let origData = origDs[idx]; //gridUpdate._getLatestValues(rId);
                grid.dataSource.setCellValue(rId, columnKey, cellValue, autoCommit);
                let isControl = utils.isNtsControl($grid, columnKey);
                if (!isControl || forceRender) renderCell($grid, rId, columnKey);
                if (isControl) {
                    $grid.trigger(events.Handler.CONTROL_CHANGE, [{ columnKey: columnKey, value: cellValue }]);
                }
                gridUpdate._notifyCellUpdated(rId);
                notifyUpdate($grid, rId, columnKey, cellValue, origData);
            }
            
            /**
             * Update row.
             */
            export function updateRow($grid: JQuery, rowId: any, updatedRowData: any, allColumnsMap?: any, forceRender?: boolean) {
                let grid: any = $grid.data("igGrid");
                if (!utils.updatable($grid)) return;
                let gridUpdate: any = $grid.data("igGridUpdating");
                let autoCommit = grid.options.autoCommit;
                let columnsMap: any = allColumnsMap || utils.getColumnsMap($grid);
                let rId = utils.parseIntIfNumber(rowId, $grid, columnsMap);
                let origDs = $grid.data(internal.ORIG_DS);
                let setting = $grid.data(internal.SETTINGS);
                let idx = setting.descriptor.keyIdxes[rId];
                if (util.isNullOrUndefined(idx)) return;
                let origData = origDs[idx]; //gridUpdate._getLatestValues(rId);
                grid.dataSource.updateRow(rId, $.extend({}, gridUpdate._getLatestValues(rId), updatedRowData), autoCommit);
                _.forEach(Object.keys(updatedRowData), function(key: any) {
                    notifyUpdate($grid, rId, key, updatedRowData[key], origData);
                    let isControl = utils.isNtsControl($grid, key);
                    if (isControl) {
                        $grid.trigger(events.Handler.CONTROL_CHANGE, [{ columnKey: key, value: updatedRowData[key] }]);
                    }
                    if (isControl && !forceRender) return;
                    let $vCell = renderCell($grid, rId, key, origData);
                    
                    // Validate
                    let validators: any =  $grid.data(validation.VALIDATORS);
                    let fieldValidator = validators[key];
                    if (util.isNullOrUndefined(fieldValidator)) return;
                    let cellValue = updatedRowData[key];
                    let result = fieldValidator.probe(String(cellValue));
                    let cell = { 
                        id: rowId,
                        columnKey: key,
                        element: $vCell
                    };
                    errors.clear($grid, cell);
                    if (!result.isValid) {
                        errors.set($grid, cell, result.errorMessage);
                    }
                });
                gridUpdate._notifyRowUpdated(rId, null);
            }
            
            /**
             * Notify update.
             */
            function notifyUpdate($grid: JQuery, rowId: any, columnKey: any, value: any, origData: any) {
                if (origData && (origData[columnKey] === value
                    || (util.isNullOrUndefined(origData[columnKey]) && _.isEmpty(value)))) {
                    let updatedCells = $grid.data(internal.UPDATED_CELLS);
                    if (updatedCells) {
                        _.remove(updatedCells, function(c, i) {
                            return c.rowId === rowId && c.columnKey === columnKey;
                        });
                    }
                    
                    let options = $grid.data(internal.GRID_OPTIONS);
                    if (!options || !options.getUserId || !options.userId) return;
                    
                    let id; 
                    if (util.isNullOrUndefined(id = origData[options.primaryKey])) {
                        let record = $grid.igGrid("findRecordByKey", rowId);
                        if (!record) return;
                        id = record[options.primaryKey];
                    }
                    let userId = options.getUserId(id);
                    let $cell = internal.getCellById($grid, rowId, columnKey);
                    
                    let cols;
                    if (userId === options.userId) {
                        $cell.removeClass(color.ManualEditTarget);
                        let targetEdits = $grid.data(internal.TARGET_EDITS);
                        if (targetEdits && (cols = targetEdits[rowId])) {
                            _.remove(cols, c => c === columnKey);
                            if (cols.length === 0) delete targetEdits[rowId]; 
                        }
                    } else {
                        $cell.removeClass(color.ManualEditOther);
                        let otherEdits = $grid.data(internal.OTHER_EDITS);
                        if (otherEdits && (cols = otherEdits[rowId])) {
                            _.remove(cols, c => c === columnKey);
                            if (cols.length === 0) delete otherEdits[rowId];
                        }
                    }
                    return;
                }
                
                let updatedCells = $grid.data(internal.UPDATED_CELLS);
                if (!updatedCells) {
                    $grid.data(internal.UPDATED_CELLS, []);
                    updatedCells = $grid.data(internal.UPDATED_CELLS);
                }
                let index = -1;
                let tCell = _.find(updatedCells, function(c, i) {
                    if (c.rowId === rowId && c.columnKey === columnKey) {
                        index = i;
                        return true;
                    }
                });
                if (tCell) updatedCells[index].value = value;
                else updatedCells.push({ rowId: rowId, columnKey: columnKey, value: value });
                
                let options = $grid.data(internal.GRID_OPTIONS);
                if (!options || !options.getUserId || !options.userId) return;
                
                let id;
                if (!origData || util.isNullOrUndefined(id = origData[options.primaryKey])) {
                    let record = $grid.igGrid("findRecordByKey", rowId);
                    if (!record) return;
                    id = record[options.primaryKey];
                }
                let userId = options.getUserId(id);
                let $cell = internal.getCellById($grid, rowId, columnKey);
                
                if (userId === options.userId) {
                    $cell.addClass(color.ManualEditTarget);
                    let targetEdits = $grid.data(internal.TARGET_EDITS);
                    if (!targetEdits) {
                        targetEdits = {};
                        targetEdits[rowId] = [ columnKey ];
                        $grid.data(internal.TARGET_EDITS, targetEdits);    
                        return;
                    }
                    if (!targetEdits[rowId]) {
                        targetEdits[rowId] = [ columnKey ];
                        return;
                    }
                    targetEdits[rowId].push(columnKey);
                } else {
                    $cell.addClass(color.ManualEditOther);
                    let otherEdits = $grid.data(internal.OTHER_EDITS);
                    if (!otherEdits) {
                        otherEdits = {};
                        otherEdits[rowId] = [ columnKey ];
                        $grid.data(internal.OTHER_EDITS, otherEdits);
                        return;
                    }
                    if (!otherEdits[rowId]) {
                        otherEdits[rowId] = [ columnKey ];
                        return;
                    }
                    otherEdits[rowId].push(columnKey);
                }
            }
            
            /**
             * Render cell
             */
            export function renderCell($grid: JQuery, rowId: any, columnKey: any, latestValues?: any, clearStates?: any) {
                let grid: any = $grid.data("igGrid");
                if (!utils.updatable($grid)) return;
                let gridUpdate: any = $grid.data("igGridUpdating");
                let rowData = gridUpdate._getLatestValues(rowId);
                let column: any =  _.find(utils.getVisibleColumns($grid), function(col: any) {
                    return col.key === columnKey;
                });
                let $cell = $grid.igGrid("cellById", rowId, columnKey);
                if (clearStates) {
                    [ color.Error, color.Alarm, color.ManualEditTarget, color.ManualEditOther,
                        color.Reflect, color.Calculation, color.Disable ].forEach(s => {
                            if ($cell.hasClass(s)) $cell.removeClass(s);
                    });
                }
                $cell.html(String(grid._renderCell(rowData[columnKey], column, rowData)));
                return $cell;
            }
            
            /**
             * End edit.
             */
            export function endEdit($grid: JQuery) {
                let selectedCell = selection.getSelectedCell($grid);
                let $selectedCell = $(selectedCell.element);
                
                let $editorContainer = $selectedCell.find(errors.EDITOR_SELECTOR);
                let value = $editorContainer.find("input")[0].value;
                let settings: any = $grid.data(internal.SETTINGS); 
                if (settings.preventEditInError
                    && utils.isEditMode($grid) && errors.any(selectedCell)) {
                    return;
                }
                
                if (utils.isEditMode($grid)) {
                    let gridUpdate: any = $grid.data("igGridUpdating");
                    let origValues = gridUpdate._originalValues;
                    if (!util.isNullOrUndefined(origValues)) {
                        _.forEach(Object.keys(origValues), function(colKey: any, idx: number) {
                            if (idx === 0) {
                                // Skip default update
                                gridUpdate._originalValues[colKey] = value; 
                                return false;
                            }
                        });
                        _.defer(function() {
                            updating.updateCell($grid, selectedCell.id, selectedCell.columnKey, value);
                        });
                    }
                }
                
                // Remove border color of editor
                if ($editorContainer.length > 0) $editorContainer.css(errors.NO_ERROR_STL);
                
                specialColumn.tryDo($grid, selectedCell, value);
                if ($editorContainer.find("span").hasClass(updating.INPUT_CURR_SYM)) {
                    $selectedCell.addClass(updating.CURR_SYM);
                }
                
                $grid.igGridUpdating("endEdit");
            }
        }
        
        module selection {
            
            /**
             * Add feature
             */
            export function addFeature(options: any) {
                let selection = { name: feature.SELECTION, mode: "cell", multipleSelection: true, wrapAround: false, cellSelectionChanged: selectCellChange };
                if (!feature.isEnable(options.features, feature.SELECTION)) {
                    options.features.push(selection);
                } else {
                    feature.replaceBy(options, feature.SELECTION, selection);
                }
            }
            
            /**
             * Select before
             */
            export function selectBefore($grid: JQuery, enterDirection?: string) {
                var enter = enterDirection || "right";
                if (enter === "right") selectPrev($grid);
                else selectAbove($grid);
            }
            
            /**
             * Select previous
             */
            export function selectPrev($grid: JQuery) {
                var selectedCell: any = getSelectedCell($grid);
                if (util.isNullOrUndefined(selectedCell)) return;
                clearSelection($grid);
                let visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                let isFixed = utils.isFixedColumnCell(selectedCell, visibleColumnsMap);
                if (selectedCell.index > 0) {
                    selectCell($grid, selectedCell.rowIndex, selectedCell.index - 1, isFixed);
                    let afterSelect = getSelectedCell($grid);
                    if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                        selectPrev($grid);
                    }
                } else if (selectedCell.index === 0) {
                    let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                    if (util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0) return;
                    let fixedColumns = utils.getFixedColumns(visibleColumnsMap);
                    let unfixedColumns = utils.getUnfixedColumns(visibleColumnsMap);
                    
                    if (isFixed || !utils.fixable($grid)) {
                        if (selectedCell.rowIndex > 0) {
                            selectCell($grid, selectedCell.rowIndex - 1, unfixedColumns.length - 1);
                        } else {
                            let dataSource = $grid.igGrid("option", "dataSource");
                            let sourceSize = dataSource.length;
                            $grid.igGrid("virtualScrollTo", sourceSize);
                            setTimeout(function() {
                                if (utils.pageable($grid)) {
                                    let pageSize = $grid.igGridPaging("pageSize");
                                    let pageIndex = $grid.igGridPaging("pageIndex");
                                    if (pageSize * (pageIndex + 1) > sourceSize) {
                                        selectCell($grid, sourceSize - pageSize * pageIndex - 1, unfixedColumns.length - 1);
                                    } else {
                                        selectCell($grid, pageSize - 1, unfixedColumns.length - 1);
                                    }
                                    return;
                                }
                                selectCell($grid, sourceSize - 1, unfixedColumns.length - 1);
                            }, 1);
                        }
                    } else if (utils.fixable($grid) && !isFixed) {
                        selectCell($grid, selectedCell.rowIndex, fixedColumns.length - 1, true);
                    }
                }
            }
            
            /**
             * Select above
             */
            export function selectAbove($grid: JQuery) {
                var selectedCell: any = getSelectedCell($grid);
                if (util.isNullOrUndefined(selectedCell)) return;
                clearSelection($grid);
                let isFixed = utils.isFixedColumnCell(selectedCell, utils.getVisibleColumnsMap($grid));
                let dataSource = $grid.igGrid("option", "dataSource");
                let sourceSize = dataSource.length;
                if (selectedCell.rowIndex > 0) {
                    selectCell($grid, selectedCell.rowIndex - 1, selectedCell.index, isFixed);
                } else if (selectedCell.rowIndex === 0) {
                    let visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                    let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                    if (util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0) return;
                    $grid.igGrid("virtualScrollTo", sourceSize);
                    if (utils.pageable($grid)) {
                        let pageSize = $grid.igGridPaging("pageSize");
                        let pageIndex = $grid.igGridPaging("pageIndex");
                        let lastIndex = pageSize - 1;
                        if (pageSize * (pageIndex + 1) > sourceSize) {
                            lastIndex = sourceSize - pageSize * pageIndex - 1;
                        }
                        setTimeout(function() {
                            if (selectedCell.index > 0) {
                            selectCell($grid, lastIndex, selectedCell.index - 1, columnsGroup[0].fixed);
                            } else if (selectedCell.index === 0) {
                                if (columnsGroup[0].fixed) {
                                    selectCell($grid, lastIndex, visibleColumnsMap["undefined"].length - 1);
                                    return;
                                } 
                                let noOfColTypes = Object.keys(visibleColumnsMap).length;
                                if (noOfColTypes === 2) {
                                    selectCell($grid, lastIndex, visibleColumnsMap["true"].length - 1, true);
                                } else {
                                    selectCell($grid, lastIndex, visibleColumnsMap["undefined"].length - 1);
                                }
                            }
                            let afterSelect = getSelectedCell($grid);
                            if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                                selectPrev($grid);
                                selectBelow($grid);
                            }
                        }, 1);
                        return;
                    }
                    setTimeout(function() {
                        if (selectedCell.index > 0) {
                            selectCell($grid, sourceSize - 1, selectedCell.index - 1, columnsGroup[0].fixed);
                        } else if (selectedCell.index === 0) {
                            if (columnsGroup[0].fixed) {
                                selectCell($grid, sourceSize - 1, visibleColumnsMap["undefined"].length - 1);
                                return;
                            } 
                            let noOfColTypes = Object.keys(visibleColumnsMap).length;
                            if (noOfColTypes === 2) {
                                selectCell($grid, sourceSize - 1, visibleColumnsMap["true"].length - 1, true);
                            } else {
                                selectCell($grid, sourceSize - 1, visibleColumnsMap["undefined"].length - 1);
                            }
                        }
                        let afterSelect = getSelectedCell($grid);
                        if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                            selectPrev($grid);
                            selectBelow($grid);
                        }
                    }, 1);
                }
            }
            
            /**
             * Select follow
             */
            export function selectFollow($grid: JQuery, enterDirection?: string) {
                var enter = enterDirection || "right";
                if (enter === "right") selectNext($grid);
                else selectBelow($grid);
            }
            
            /**
             * Select next
             */
            function selectNext($grid: JQuery) {
                var selectedCell: any = getSelectedCell($grid);
                if (util.isNullOrUndefined(selectedCell)) return;
                clearSelection($grid);
                let visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                let dataSource = $grid.igGrid("option", "dataSource");
                
                let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                if (util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0) return;
                if (selectedCell.index < columnsGroup.length - 1) { 
                    selectCell($grid, selectedCell.rowIndex, selectedCell.index + 1, columnsGroup[0].fixed); 
                } else if (selectedCell.index === columnsGroup.length - 1) {
                    if (columnsGroup[0].fixed) {
                        selectCell($grid, selectedCell.rowIndex, 0);
                    } else if (utils.pageable($grid)) {
                        let pageSize = $grid.igGridPaging("pageSize");
                        let pageIndex = $grid.igGridPaging("pageIndex");
                        if ((dataSource.length < pageSize * (pageIndex + 1)
                            && selectedCell.rowIndex < (dataSource.length - pageSize * pageIndex - 1))
                            || selectedCell.rowIndex < (pageSize - 1)) {
                            selectCell($grid, selectedCell.rowIndex + 1, 0, true);
                            let afterSelect: any = getSelectedCell($grid);
                            if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                                selectNext($grid);
                            }
                        } else {
                            $grid.igGrid("virtualScrollTo", "0px");
                            setTimeout(function() {
                                selectCell($grid, 0, 0, utils.fixable($grid) ? true : false);
                                let afterSelect: any = getSelectedCell($grid);
                                if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                                    selectNext($grid);
                                }
                            }, 1);
                        }
                    } else if (selectedCell.rowIndex < dataSource.length - 1) {
                        selectCell($grid, selectedCell.rowIndex + 1, 0, true);
                        let afterSelect: any = getSelectedCell($grid);
                        if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                            selectNext($grid);
                        }
                    } else {
                        $grid.igGrid("virtualScrollTo", "0px");
                        setTimeout(function() {
                            selectCell($grid, 0, 0, utils.fixable($grid) ? true : false);
                            let afterSelect: any = getSelectedCell($grid);
                            if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                                selectNext($grid);
                            }
                        }, 1);
                    }
                }
            }
            
            /**
             * Select below
             */
            function selectBelow($grid: JQuery) {
                var selectedCell: any = getSelectedCell($grid);
                if (util.isNullOrUndefined(selectedCell)) return;
                clearSelection($grid);
                let isFixed = utils.isFixedColumnCell(selectedCell, utils.getVisibleColumnsMap($grid));
                let dataSource = $grid.igGrid("option", "dataSource");
                let sourceSize = dataSource.length;
                if (utils.pageable($grid)) {
                    let pageSize = $grid.igGridPaging("pageSize");
                    let pageIndex = $grid.igGridPaging("pageIndex");
                    if ((pageSize * (pageIndex + 1) > sourceSize 
                        && selectedCell.rowIndex < (sourceSize - pageSize * pageIndex - 1))
                        || selectedCell.rowIndex < (pageSize - 1)) {
                        selectCell($grid, selectedCell.rowIndex + 1, selectedCell.index, isFixed);
                    } else {
                        let visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                        let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                        if (util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0) return;
                        $grid.igGrid("virtualScrollTo", "0px");
                        setTimeout(function() {
                            if (selectedCell.index < columnsGroup.length - 1) {
                                selectCell($grid, 0, selectedCell.index + 1, columnsGroup[0].fixed);
                            } else if (selectedCell.index === columnsGroup.length - 1) {
                                if (columnsGroup[0].fixed) {
                                    selectCell($grid, 0, 0);
                                } else {
                                    selectCell($grid, 0, 0, Object.keys(visibleColumnsMap).length === 2 ? true : undefined);
                                }
                            }
                            let afterSelect = getSelectedCell($grid);
                            if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                                selectNext($grid);
                            }
                        }, 1);
                    }
                    return;
                }
                if (selectedCell.rowIndex < sourceSize - 1) {
                    selectCell($grid, selectedCell.rowIndex + 1, selectedCell.index, isFixed);
                } else if (selectedCell.rowIndex === sourceSize - 1) {
                    let visibleColumnsMap = utils.getVisibleColumnsMap($grid);
                    let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                    if (util.isNullOrUndefined(columnsGroup) || columnsGroup.length === 0) return;
                    $grid.igGrid("virtualScrollTo", "0px");
                    setTimeout(function() {
                        if (selectedCell.index < columnsGroup.length - 1) {
                            selectCell($grid, 0, selectedCell.index + 1, columnsGroup[0].fixed);
                        } else if (selectedCell.index === columnsGroup.length - 1) {
                            if (columnsGroup[0].fixed) {
                                selectCell($grid, 0, 0);
                            } else {
                                selectCell($grid, 0, 0, Object.keys(visibleColumnsMap).length === 2 ? true : undefined);
                            }
                        }
                        let afterSelect = getSelectedCell($grid);
                        if (afterSelect && $(afterSelect.element).outerWidth() === 1) {
                            selectNext($grid);
                        }
                    }, 1);
                }
            }
            
            /**
             * Get selected cell
             */
            export function getSelectedCell($grid: JQuery) {
                if (!utils.selectable($grid)) { 
                    let $targetGrid = fixedColumns.realGridOf($grid); 
                    if (!util.isNullOrUndefined($targetGrid)) {
                        return $targetGrid.igGridSelection("selectedCells")[0] || $targetGrid.data(internal.SELECTED_CELL); 
                    }
                }
                return $grid.igGridSelection("selectedCells")[0] || $grid.data(internal.SELECTED_CELL);
            }
            
            /**
             * Get selected cells
             */
            export function getSelectedCells($grid: JQuery) {
                return utils.selectable($grid) ? $grid.igGridSelection("selectedCells") : undefined;
            }
            
            /**
             * Select cell
             */
            export function selectCell($grid: JQuery, rowIndex: number, columnIndex: number, isFixed?: boolean) {
                if (!utils.selectable($grid)) return;
                $grid.igGridSelection("selectCell", rowIndex, columnIndex, utils.fixable($grid) ? isFixed : undefined);
                
                // Fire cell selection change
                let ui: any = { owner: $grid.data("igGridSelection"),
                                selectedCells: $grid.igGridSelection("selectedCells") };
                let selectedCells = $grid.igGridSelection("selectedCells");
                if (selectedCells.length > 0) ui.cell = selectedCells[0];
                selectCellChange({ target: $grid[0] }, ui);
                let selectedCell: any = getSelectedCell($grid);
                let $element = $(selectedCell.element);
                let ntsCombo = $element.find(".nts-combo-container"); 
                if (ntsCombo.length > 0) {
                    ntsCombo.find("input").select();
                }
                let ntsSwitchs = $element.find(".nts-switch-container");
                if (ntsSwitchs.length > 0) {
                    ntsSwitchs.find("button").filter((i, b) => $(b).hasClass("selected")).focus();
                }
            }
            
            export function selectCellById($grid: JQuery, rowId: any, columnKey: string) {
                return;
            }
            
            function selectCellChange(evt: any, ui: any) {
                if (util.isNullOrUndefined(ui.cell)) return;
                $(evt.target).data(internal.SELECTED_CELL, ui.cell);
            }
            
            export function onCellNavigate(evt: any, enterDirection?: string) {
                var grid = evt.currentTarget;
                let $targetGrid = fixedColumns.realGridOf($(grid));
                
                if (utils.isTabKey(evt)) {
                    if (utils.isEditMode($targetGrid))
                        $targetGrid.igGridUpdating("endEdit");
                    
                    if (evt.shiftKey) {
                        selection.selectPrev($targetGrid);
                    } else {
                        selection.selectFollow($targetGrid);
                    }
                    evt.preventDefault();
                    return;
                }
                 
                if (utils.isEnterKey(evt)) {
                    if (evt.shiftKey) {
                        selection.selectBefore($targetGrid, enterDirection);
                    } else {
                        selection.selectFollow($targetGrid, enterDirection);
                    }
                    evt.stopImmediatePropagation();
                    return;
                }
            }
            
            /**
             * Clear selection
             */
            function clearSelection($grid) {
                if (utils.selectable($grid)) {
                    $grid.igGridSelection("clearSelection");
                    return;
                }
                let $targetGrid = fixedColumns.realGridOf($grid);
                if (!util.isNullOrUndefined($targetGrid) && utils.selectable($targetGrid))
                    $targetGrid.igGridSelection("clearSelection");
            }
            
            export class Direction {
                to: string;
                bind(evt: any) {
                    onCellNavigate(evt, this.to);
                }
            }
        }
        
        module columnSize {
            
            /**
             * Initialize
             */
            export function init($grid: JQuery, columns: any) {
                initValueExists($grid).done(function(res) {
                    if (res) return;
                    let columnWidths: {[ key: string ]: number } = {};
                    _.forEach(columns, function(col: any, index: number) {
                        flat(col, columnWidths);
                    });
                    saveAll($grid, columnWidths);
                });
            }
            
            /**
             * Flat.
             */
            function flat(col: any, columnWidths: any) {
                if (col.group) {
                    _.forEach(col.group, function(sCol) {
                        flat(sCol, columnWidths);
                    });
                    return;
                }
                columnWidths[col.key] = parseInt(col.width);
            }
            
            /**
             * Exists.
             */
            export function exists($grid: JQuery) {
                return uk.localStorage.getItem(getStorageKey($grid)).isPresent();
            }
            
            /**
             * Create resize options.
             */
            export function createResizeOptions(key: string) {
                let resizing = { name: feature.RESIZING };
                resizing.columnSettings = [{ columnKey: key, allowResizing: false, minimumWidth: 0 }];
                return resizing;
            }
            
            /**
             * Load data
             */
            export function load($grid: JQuery) {
                let storeKey = getStorageKey($grid);
                storage.getItem(storeKey).done(function(widths) {
                    widths.ifPresent((columns) => {
                        let widthColumns: any;
                        try {
                            widthColumns = JSON.parse(columns);
                        } catch(e) {
                            widthColumns = columns;
                        }
                        setWidths($grid, widthColumns);
                        
                        $grid.closest(".nts-grid-container").css("visibility", "visible");
                        return null;
                    });
                });
            }
            
            /**
             * Save data
             */
            export function save($grid: JQuery, columnKey: string, columnWidth: number) {
                let storeKey = getStorageKey($grid);
                if (storage instanceof dist.Local) {
                    let columnsWidth = uk.localStorage.getItem(storeKey);
                    let widths = {};
                    if (columnsWidth.isPresent()) {
                        widths = JSON.parse(columnsWidth.get());
                        widths[columnKey] = columnWidth;
                    } else {
                        widths[columnKey] = columnWidth;
                    }
                    uk.localStorage.setItemAsJson(storeKey, widths);
                } else if (storage instanceof dist.Remote) {
                    let width = {};
                    width[columnKey] = columnWidth;
                    storage.setItemAsJson(storeKey, width);
                }
            }
            
            /**
             * Save all data
             */
            function saveAll($grid: JQuery, widths: {[ key: string ]: number }) {
                let storeKey = getStorageKey($grid);
                storage.getItem(storeKey).done(function(columnWidths) {
                    if (!columnWidths.isPresent()) {
                        storage.setItemAsJson(storeKey, widths);
                    }
                });
            }
            
            function initValueExists($grid: JQuery) {
                let dfd = $.Deferred();
                let storeKey = getStorageKey($grid);
                storage.getItem(storeKey).done(function(columnWidths) {
                    dfd.resolve(columnWidths.isPresent());
                });
                return dfd.promise();
            }
            
            function getStorageKey($grid: JQuery) {
                return request.location.current.rawUrl + "/" + $grid.attr("id");
            }
            
            /**
             * Load column
             */
            export function loadOne($grid: JQuery, columnKey: string) {
                let storeKey = getStorageKey($grid);
                storage.getItem(storeKey).done(function(widths) {
                    widths.ifPresent((columns) => {
                        let widthColumns: any = JSON.parse(columns);
                        setWidth($grid, columnKey, widthColumns[columnKey]);
                        return null;
                    });
                });
            }
            
            /**
             * Load fixed columns
             */
            export function loadFixedColumns($grid: JQuery) {
                let storeKey = getStorageKey($grid);
                uk.localStorage.getItem(storeKey).ifPresent((columns) => {
                    let fixedColumns = utils.getVisibleFixedColumns($grid);
                    if (util.isNullOrUndefined(fixedColumns) || fixedColumns.length === 0) return;
                    let widthColumns: any = JSON.parse(columns);
                    _.forEach(fixedColumns, function(fixedCol) {
                        setWidth($grid, fixedCol.key, widthColumns[fixedCol.key]);
                    });
                    return null; 
                });
            }
            
            /**
             * Set width
             */
            function setWidth($grid: JQuery, columnKey: string, width: number, noCheck?: boolean) {
                if (noCheck !== true && util.isNullOrUndefined($grid.data("igGridResizing"))) return;
                try {
                    $grid.igGridResizing("resize", columnKey, width);
                } catch (e) {}
            }
            
            /**
             * Set widths
             */
            function setWidths($grid: JQuery, columns: {[ key: string ]: number}) {
                if (util.isNullOrUndefined($grid.data("igGridResizing"))
                    || util.isNullOrUndefined(columns)) return;
                let columnKeys = Object.keys(columns);
                _.forEach(columnKeys, function(key: any, index: number) {
                    setWidth($grid, key, columns[key], true);
                });
            }
        }

        module functions {
            export let ERRORS: string = "errors";
            export let UPDATE_ROW: string = "updateRow";
            export let SET_STATE: string = "setState";
            export let UPDATED_CELLS: string = "updatedCells";
            export let ENABLE_CONTROL: string = "enableNtsControlAt";
            export let ENABLE_ALL_CONTROLS: string = "enableNtsControls";
            export let DISABLE_CONTROL: string = "disableNtsControlAt";
            export let DISABLE_ALL_CONTROLS: string = "disableNtsControls";
            export let DIRECT_ENTER: string = "directEnter";
            export let CHECK_ALL: string = "checkAll";
            export let UNCHECK_ALL: string = "uncheckAll";
            export let HEADER_TEXT: string = "headerText";
            export let SELECTED_SHEET: string = "selectedSheet";
            export let CLEAR_ROW_STATES: string = "clearRowStates";
            export let RESET_ORIG_DS: string = "resetOrigDataSource";
            export let DESTROY: string = "destroy";
            
            /**
             * Actions
             */
            export function ntsAction($grid: JQuery, method: string, params: Array<any>) {
                switch (method) {
                    case UPDATE_ROW:
                        var autoCommit = $grid.data("igGrid") !== null && $grid.igGrid("option", "autoCommit") ? true : false;
                        updateRow($grid, params[0], params[1], autoCommit);
                        break;
                    case SET_STATE:
                        setState($grid, params[0], params[1], params[2]);
                        break;
                    case ENABLE_CONTROL:
                        enableNtsControlAt($grid, params[0], params[1], params[2]);
                        break;
                    case ENABLE_ALL_CONTROLS:
                        enableNtsControls($grid, params[0], params[1], params[2]);
                        break;
                    case DISABLE_CONTROL:
                        disableNtsControlAt($grid, params[0], params[1], params[2]);
                        break;
                    case DISABLE_ALL_CONTROLS:
                        disableNtsControls($grid, params[0], params[1], params[2]);
                        break;
                    case DIRECT_ENTER:
                        var direction: selection.Direction = $grid.data(internal.ENTER_DIRECT);
                        direction.to = params[0];
                        if (utils.fixable($grid)) {
                            let fixedTable = fixedColumns.getFixedTable($grid)
                            if (!util.isNullOrUndefined(fixedTable)) {
                                fixedTable.data(internal.ENTER_DIRECT).to = params[0];
                            }
                        }
                        break;
                    case CHECK_ALL:
                        checkAll($grid, params[0]);
                        break;
                    case UNCHECK_ALL:
                        uncheckAll($grid, params[0]);
                        break;
                    case HEADER_TEXT:
                        setHeaderText($grid, params[0], params[1], params[2]);
                        break;
                    case CLEAR_ROW_STATES:
                        clearStates($grid, params[0]);
                        break;
                    case RESET_ORIG_DS:
                        resetOrigDs($grid, params[0]);
                        break;
                    case DESTROY:
                        destroy($grid);
                        break;
                    case SELECTED_SHEET:
                        return getSelectedSheet($grid);
                    case UPDATED_CELLS:
                        return $grid.data(internal.UPDATED_CELLS);
                    case ERRORS:
                        return getErrors($grid);
                }
            }
            
            /**
             * Get errors.
             */
            function getErrors($grid: JQuery) {
                if (!$grid) return [];
                return $grid.data(internal.ERRORS);
            }
    
            /**
             * Update row
             */
            function updateRow($grid: JQuery, rowId: any, object: any, autoCommit: boolean) {
                let selectedSheet = getSelectedSheet($grid);
                if (selectedSheet) {
                    let grid = $grid.data("igGrid");
                    let options = grid.options;
                    Object.keys(object).forEach(function(k) {
                        if (!_.includes(selectedSheet.columns, k)) {
                            grid.dataSource.setCellValue(rowId, k, object[k], grid.options.autoCommit);
                            delete object[k];
                            if (!util.isNullOrUndefined(options.userId) && _.isFunction(options.getUserId)) {
                                let uId = options.getUserId(rowId);
                                if (uId === options.userId) {
                                    let targetEdits = $grid.data(internal.TARGET_EDITS);
                                    if (!targetEdits) {
                                        targetEdits = {};
                                        targetEdits[rowId] = [ k ];
                                        $grid.data(internal.TARGET_EDITS, targetEdits);    
                                        return;
                                    }
                                    if (!targetEdits[rowId]) {
                                        targetEdits[rowId] = [ k ];
                                        return;
                                    }
                                    targetEdits[rowId].push(k);
                                } else {
                                    let otherEdits = $grid.data(internal.OTHER_EDITS);
                                    if (!otherEdits) {
                                        otherEdits = {};
                                        otherEdits[rowId] = [ k ];
                                        $grid.data(internal.OTHER_EDITS, otherEdits);
                                        return;
                                    }
                                    if (!otherEdits[rowId]) {
                                        otherEdits[rowId] = [ k ];
                                        return;
                                    }
                                    otherEdits[rowId].push(k);
                                }
                            }
                        }
                    });
                }
                
                updating.updateRow($grid, rowId, object, undefined, true);
                if (!autoCommit) {
                    var updatedRow = $grid.igGrid("rowById", rowId, false);
                    $grid.igGrid("commit");
                    if (updatedRow !== undefined) $grid.igGrid("virtualScrollTo", $(updatedRow).data("row-idx"));
                }
            }
            
            /**
             * Set state.
             */
            function setState($grid: JQuery, rowId: any, key: any, states: any) {
                let cellFormatter = $grid.data(internal.CELL_FORMATTER);
                let cellStateFeatureDef = cellFormatter.cellStateFeatureDef; 
                if (cellFormatter.rowStates) {
                    let row = cellFormatter.rowStates[rowId];
                    if (row) {
                        let sts = row[key];
                        if (sts) {
                            if (sts[0][cellStateFeatureDef.state]) {
                                sts[0][cellStateFeatureDef.state] = states;
                            }
                        } else {
                            let cellState = {};
                            cellState[cellStateFeatureDef.rowId] = rowId;
                            cellState[cellStateFeatureDef.columnKey] = key;
                            cellState[cellStateFeatureDef.state] = states;
                            row[key] = [ cellState ];
                        }
                    } else {
                        cellFormatter.rowStates[rowId] = {};
                        let cellState = {};
                        cellState[cellStateFeatureDef.rowId] = rowId;
                        cellState[cellStateFeatureDef.columnKey] = key;
                        cellState[cellStateFeatureDef.state] = states;
                        cellFormatter.rowStates[rowId][key] = [ cellState ];
                    }
                } else {
                    cellFormatter.rowStates = {};
                    let cellState = {};
                    cellState[cellStateFeatureDef.rowId] = rowId;
                    cellState[cellStateFeatureDef.columnKey] = key;
                    cellState[cellStateFeatureDef.state] = states;
                    let colState = {};
                    colState[key] = [ cellState ];
                    cellFormatter.rowStates[rowId] = colState;
                }
                
                let selectedSheet = getSelectedSheet($grid);
                let features = $grid.igGrid("option", "features");
                let columns;
                if (selectedSheet) {
                    columns = selectedSheet.columns;
                }
                
                if (features) {
                    let colFixFt = feature.find(features, feature.COLUMN_FIX);
                    if (colFixFt) {
                        let fixedCols = _.filter(colFixFt.columnSettings, c => c.isFixed).map(c => c.columnKey);
                        if (selectedSheet) {
                            columns = _.concat(selectedSheet.columns, fixedCols);
                        }
                    }   
                }
                
                if (selectedSheet && !_.includes(columns, key)) {
                    let options = $grid.data(internal.GRID_OPTIONS);
                    let stateFt = feature.find(options.ntsFeatures, feature.CELL_STATE);
                    if (stateFt) {
                        let newState = {};
                        newState[stateFt.rowId] = rowId;
                        newState[stateFt.columnKey] = key;
                        newState[stateFt.state] = states;
                        stateFt.states.push(newState);
                    }
                    return;
                }
                updating.renderCell($grid, rowId, key, undefined, true);
            }

            /**
             * Disable controls
             */
            function disableNtsControls($grid: JQuery, columnKey: any, controlType: string, header?: boolean) {
                var ds = $grid.igGrid("option", "dataSource");
                var primaryKey = $grid.igGrid("option", "primaryKey");
                
                if (header && controlType === ntsControls.CHECKBOX) {
                    let setting = $grid.data(internal.SETTINGS);
                    if (setting && setting.descriptor && setting.descriptor.colIdxes) {
                        let key = setting.descriptor.colIdxes[columnKey];
                        
                        let cellElm;
                        let headerCells = setting.descriptor.headerCells;
                        if (!headerCells) {
                            cellElm = setting.descriptor.headerParent.find("th").filter(function() {
                                let c = $(this);
                                let id = c.attr("id");
                                if (!id) return false;
                                let parts = id.split("_");
                                id = parts[parts.length - 1];
                                return c.css("display") !== "none" && id === columnKey; 
                            });
                        } else {  
                            let cells = headerCells.filter(function(c) {
                                return c.css("display") !== "none";
                            });
                            cellElm = cells[key];
                        }
                        
                        if (cellElm) { 
                            let control = ntsControls.getControl(controlType);
                            if (control) {
                                control.disable(cellElm);
                            }
                        }
                    }
                }
                
                for (let i = 0; i < ds.length; i++) {
                    let id = ds[i][primaryKey];
                    disableNtsControlAt($grid, id, columnKey, controlType);
                    color.pushDisable($grid, { id: id, columnKey: columnKey });
                }
            }
            
            /**
             * Enable controls
             */
            function enableNtsControls($grid: JQuery, columnKey: any, controlType: string) {
                var ds = $grid.igGrid("option", "dataSource");
                var primaryKey = $grid.igGrid("option", "primaryKey");
                
                if (header && controlType === ntsControls.CHECKBOX) {
                    let setting = $grid.data(internal.SETTINGS);
                    if (setting && setting.descriptor && setting.descriptor.colIdxes) {
                        let key = setting.descriptor.colIdxes[columnKey];
                        
                        let cellElm;
                        let headerCells = setting.descriptor.headerCells;
                        if (!headerCells) {
                            cellElm = setting.descriptor.headerParent.find("th").filter(function() {
                                let c = $(this);
                                let id = c.attr("id");
                                if (!id) return false;
                                let parts = id.split("_");
                                id = parts[parts.length - 1];
                                return c.css("display") !== "none" && id === columnKey; 
                            });
                        } else {  
                            let cells = headerCells.filter(function(c) {
                                return c.css("display") !== "none";
                            });
                            cellElm = cells[key];
                        }
                        
                        if (cellElm) { 
                            let control = ntsControls.getControl(controlType);
                            if (control) {
                                control.enable(cellElm);
                            }
                        }
                    }
                }
                
                for (let i = 0; i < ds.length; i++) {
                    let id = ds[i][primaryKey];
                    enableNtsControlAt($grid, id, columnKey, controlType);
                    color.popDisable($grid, { id: id, columnKey: columnKey }); 
                }
            }
            
            /**
             * Disable control
             */
            function disableNtsControlAt($grid: JQuery, rowId: any, columnKey: any, controlType: string) {
                var cellContainer = $grid.igGrid("cellById", rowId, columnKey);
                var control = ntsControls.getControl(controlType);
                if (util.isNullOrUndefined(control)) return;
                let $cellContainer = $(cellContainer);
                control.disable($cellContainer);
                if (!$cellContainer.hasClass(color.Disable)) $cellContainer.addClass(color.Disable);
                color.pushDisable($grid, { id: rowId, columnKey: columnKey });
            }
    
            /**
             * Enable control
             */
            function enableNtsControlAt($grid: JQuery, rowId: any, columnKey: any, controlType: string) {
                var cellContainer = $grid.igGrid("cellById", rowId, columnKey);
                var control = ntsControls.getControl(controlType);
                if (util.isNullOrUndefined(control)) return;
                let $cellContainer = $(cellContainer);
                control.enable($cellContainer);
                $cellContainer.removeClass(color.Disable);
                color.popDisable($grid, { id: rowId, columnKey: columnKey });
            }
            
            /**
             * Check all
             */
            function checkAll($grid: JQuery, key: any) {
                let ds = $grid.igGrid("option", "dataSource");
                let primaryKey = $grid.igGrid("option", "primaryKey");
                if (utils.getControlType($grid, key) !== ntsControls.CHECKBOX) return;
                let cbSelect = $grid.data(internal.CB_SELECTED);
                let colCb = cbSelect[key];
                for (let i = 0; i < ds.length; i++) {
                    let id = ds[i][primaryKey];
                    if (colCb && colCb.disableRows
                        && colCb.disableRows.has(id))  
                        continue;
                    updating.updateCell($grid, id, key, true, undefined, true);
                }
            }
            
            /**
             * Uncheck all
             */
            function uncheckAll($grid: JQuery, key: any) {
                let ds = $grid.igGrid("option", "dataSource");
                let primaryKey = $grid.igGrid("option", "primaryKey");
                if (utils.getControlType($grid, key) !== ntsControls.CHECKBOX) return;
                let cbSelect = $grid.data(internal.CB_SELECTED);
                let colCb = cbSelect[key];
                for (let i = 0; i < ds.length; i++) {
                    let id = ds[i][primaryKey];
                    if (colCb && colCb.disableRows && colCb.disableRows.has(id)) continue;
                    updating.updateCell($grid, id, key, false, undefined, true);
                }
            }
            
            /**
             * Set header text.
             */
            function setHeaderText($grid: JQuery, key: any, text: any, group?: any) {
                if (!group) {
                    let setting = $grid.data(internal.SETTINGS);
                    if (!setting || !setting.descriptor || !setting.descriptor.colIdxes 
                        || !setting.descriptor.headerCells) return;
                    let colIdx = setting.descriptor.colIdxes[key];
                    let fixedColsLen = setting.descriptor.headerCells.length - Object.keys(setting.descriptor.colIdxes).length;
                    let headerCell = setting.descriptor.headerCells[colIdx + fixedColsLen];
                    if (headerCell) {
                        $(headerCell.find("span")[1]).html(text);
                    }
                    let options = $grid.data(internal.GRID_OPTIONS);
                    updateHeaderColumn(options.columns, key, text, group);
                    let sheetMng = $grid.data(internal.SHEETS);
                    if (sheetMng) {
                        Object.keys(sheetMng.sheetColumns).forEach(function(k) {
                            updateHeaderColumn(sheetMng.sheetColumns[k], key, text, group);
                        });
                    }
                    return;
                }
                let headersTable = $grid.igGrid("headersTable");
                headersTable.find("th").each(function() {
                    let $self = $(this);
                    let colspan = $self.attr("colspan");
                    if (util.isNullOrUndefined(colspan)) return;
                    let label = $self.attr("aria-label");
                    if (key === label.trim()) {
                        $self.attr("aria-label", text);
                        $self.children("span.ui-iggrid-headertext").text(text);
                        return false;
                    }
                });
                
                let options = $grid.data(internal.GRID_OPTIONS);
                updateHeaderColumn(options.columns, key, text, group);
                let sheetMng = $grid.data(internal.SHEETS);
                if (sheetMng) {
                    Object.keys(sheetMng.sheetColumns).forEach(function(k) {
                        updateHeaderColumn(sheetMng.sheetColumns[k], key, text, group);
                    });
                }
            }
            
            /**
             * Update header column.
             */
            function updateHeaderColumn(columns: Array<any>, key: any, text: any, group?: any) {
                let updated = false;
                _.forEach(columns, function(c, i) {
                    if (group && c.group && c.headerText === key) {
                        updated = true;
                        c.headerText = text;
                        return false;
                    }
                    
                    if (!group && c.group) {
                        updated = updateHeaderColumn(c.group, key, text, group);
                        if (updated) return false;
                    }
                    if (!group && !c.group && c.key === key) {
                        updated = true;
                        c.headerText = text;
                        return false;
                    }
                });
                return updated;
            }
            
            /**
             * Clear states.
             */
            function clearStates($grid: JQuery, arr: Array<any>) {
                if (arr && arr.constructor !== Array) {
                    return clearStates.apply(null, [ $grid, [ arr ]]);
                }
                
                let cellFormatter = $grid.data(internal.CELL_FORMATTER);
                if (!cellFormatter) return;
                
                arr.forEach(id => {
                    let disableRow;
                    if (cellFormatter.disableRows && (disableRow = cellFormatter.disableRows[id])
                        && disableRow[0].disable) {
                        delete cellFormatter.disableRows[id];
                    } else if (cellFormatter.rowStates && cellFormatter.rowStates[id]) {
                        delete cellFormatter.rowStates[id];
                    }
                    
                    clearRowStates($grid, id);
                });
            }
            
            /**
             * Clear row states.
             */
            function clearRowStates($grid: JQuery, id: any) {
                let $row = $grid.igGrid("rowById", id, false);
                removeClass($grid, $row, id);
                
                $row = $grid.igGrid("rowById", id, true);
                if ($row.length > 0) {
                    removeClass($grid, $row, id);
                }
            }
            
            /**
             * Remove class.
             */
            function removeClass($grid: JQuery, $row: JQuery, id: any) {
                let $cells = $row.find("td");
                [ color.Error, color.Alarm, color.ManualEditTarget, color.ManualEditOther,
                    color.Reflect, color.Calculation, color.Disable].forEach(s => {
                    $cells.each((i, td) => {
                        let $cell = $(td);
                        if ($cell.hasClass(s)) {
                            $cell.removeClass(s);
                            if (s === color.Disable) {
                                let described = $cell.attr("aria-describedby");
                                if (!described) return;
                                let key = described.split("_")[1];
                                let controlType = utils.getControlType($grid, key);
                                if (controlType && controlType !== ntsControls.TEXTBOX) {
                                    let control = ntsControls.getControl(controlType);
                                    if (util.isNullOrUndefined(control)) return;
                                    control.enable($cell);
                                    color.popDisable($grid, { id: id, columnKey: key });
                                }
                            }
                        }
                    });
                });
            }
            
            /**
             * Reset orig ds.
             */
            function resetOrigDs($grid: JQuery, ds: any) {
                $grid.data(internal.ORIG_DS, ds);
                $grid.data(internal.UPDATED_CELLS, null);
            }
            
            /**
             * Get selected sheet.
             */
            function getSelectedSheet($grid: JQuery) {
                let sheet = $grid.data(internal.SHEETS);
                if (!sheet || !sheet.currentSheet) return;
                return _.find(sheet.sheets, function(s) {
                    return s.name === sheet.currentSheet;
                });
            }
            
            /**
             * Destroy
             */
            function destroy($grid: JQuery) {
                let $container = $grid.closest(".nts-grid-container");
                if ($container.length === 0) {
                    $grid.igGrid("destroy");
                    $grid.off();
                    $grid.removeData();
                    return;
                }
                $container.find(".nts-grid-sheet-buttons").remove();
                $($grid.igGrid("container")).unwrap().unwrap();
                $grid.igGrid("destroy");
                $grid.off();
                $grid.removeData();
            }
        }
        
        module ntsControls {
            export let LABEL: string = 'Label';
            export let LINK_LABEL: string = 'LinkLabel';
            export let CHECKBOX: string = 'CheckBox';
            export let SWITCH_BUTTONS: string = 'SwitchButtons';
            export let COMBOBOX: string = 'ComboBox'; 
            export let BUTTON: string = 'Button';
            export let DELETE_BUTTON = 'DeleteButton';
            export let TEXTBOX = 'TextBox';
            export let TEXT_EDITOR = 'TextEditor';
            export let FLEX_IMAGE = 'FlexImage';
            export let IMAGE = 'Image';
            export let HEIGHT_CONTROL = "27px";
            
            export let COMBO_CLASS = "nts-combo-container";

            /**
             * Get control
             */
            export function getControl(name: string): NtsControlBase {
                switch (name) {
                    case CHECKBOX:
                        return new CheckBox();
                    case SWITCH_BUTTONS:
                        return new SwitchButtons();
                    case COMBOBOX:
                        return new ComboBox();
                    case BUTTON:
                        return new Button();
                    case DELETE_BUTTON:
                        return new DeleteButton();
                    case TEXT_EDITOR:
                        return new TextEditor();
                    case LINK_LABEL:
                        return new LinkLabel();
                    case FLEX_IMAGE:
                        return new FlexImage();
                    case IMAGE:
                        return new Image();
                }
            }
            
            /**
             * Draw label
             */
            export function drawLabel($grid: JQuery, column: any, cellFormatter: color.CellFormatter): void {
                column.formatter = function(value, rowObj) {
                    if (util.isNullOrUndefined(rowObj)) return value;
                    var $self = this;
                    var rowId = rowObj[$grid.igGrid("option", "primaryKey")];
                    var controlCls = "nts-grid-control-" + column.key + "-" + rowId;
                    var $container = $("<div/>").append($("<div/>").addClass(controlCls).css("height", HEIGHT_CONTROL));
                    setTimeout(function() {
                        let rId = rowObj[$grid.igGrid("option", "primaryKey")];
                        var $gridCell = internal.getCellById($grid, rId, column.key);
                        if ($gridCell && $($gridCell.children()[0]).children().length === 0) {
                            let action;
                            if (column.click && _.isFunction(column.click)) {
                                action = () => column.click(rowId, column.key);
                            }
                            $("." + controlCls).append(new Label(action).draw({ text: value }));
                            let cellElement = { 
                                id: rId,
                                columnKey: column.key,
                                $element: $gridCell
                            };
                            cellFormatter.style($grid, cellElement);
                            cellFormatter.setTextColor($grid, cellElement);
                            cellFormatter.setTextStyle($grid, cellElement);
                        }
                    }, 0);

                    return $container.html();
                };
            }
            
            /**
             * Create header checkbox.
             */
            export function createHeaderCheckbox(data: any, key: any) {
                let defaultOptions = {
                    update: $.noop,
                    initValue: false,
                    enable: true
                };
                let options = $.extend({}, defaultOptions, data);
                return new CheckBox().draw(options).addClass("nts-grid-header-control-" + key).prop("outerHTML");
            }
            
            /**
             * Bind cb header columns.
             */
            export function bindCbHeaderColumns(options: any, columns: Array<any>, selectionColumns: any) {
                options.headerCellRendered = function(evt, ui) {
                    let $grid = $(ui.owner.element);
                    let column = _.remove(columns, c => c === ui.columnKey);
                    if (!column || column.length === 0) return;
                    let columnConf = selectionColumns[column[0]];
                    if (columnConf) {
                        selectionColumns[column[0]].th = ui.th;
                    }
                    $(ui.th).find(".nts-grid-header-control-" + column[0]).find("input[type='checkbox']")
                    .on("change", function() {
                        let $cb = $(this);
                        let selected = $cb.is(":checked");
                        let cbSelectCols = $grid.data(internal.CB_SELECTED);
                        let cbSelectConf = cbSelectCols[column[0]];
                        if (!cbSelectConf) return;
                        
                        _.forEach(options.dataSource, function(r) {
                            if (!r) return;
                            let id = r[options.primaryKey];
                            if (cbSelectConf && (cbSelectConf.hiddenRows
                                && cbSelectConf.hiddenRows.some(v => v === id))
                                || (cbSelectConf.disableRows 
                                    && cbSelectConf.disableRows.has(id))) return;
                            updating.updateCell($grid, id, ui.columnKey, selected, undefined, true);
                        });
                        cbSelectConf.selectAll = selected;
                        if (selected) {
                            let hiddenCount = cbSelectConf.hiddenRows ? cbSelectConf.hiddenRows.length : 0;
                            let disableCount = cbSelectConf.disableRows ? cbSelectConf.disableRows.size : 0;
                            cbSelectConf.quantity = options.dataSource.length - hiddenCount - disableCount;
                            return;
                        }
                        cbSelectConf.quantity = 0;
                    });
                };
            }   
    
            abstract class NtsControlBase {
                $containedGrid: JQuery;
                readOnly: boolean = false;
                abstract containerClass(): string;
                // Draw control
                abstract draw(data: any): JQuery;
                // Enable control
                abstract enable($container: JQuery): void;
                // Disable control
                abstract disable($container: JQuery): void;
            }
    
            class CheckBox extends NtsControlBase {
                containerClass(): string {
                    return "nts-checkbox-container";
                }
    
                draw(data: any): JQuery {
                    var checkBoxText: string;
                    var setChecked = data.update;
                    var initValue = data.initValue;
                    var $wrapper = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                    $wrapper.addClass("ntsControl").on("click", (e) => {
                        if ($wrapper.data("readonly") === true) e.preventDefault();
                    });
    
                    var text = data.controlDef.options[data.controlDef.optionsText];
                    if (text) {
                        checkBoxText = text;
                    } else {
                        checkBoxText = $wrapper.text();
                        $wrapper.text('');
                    }
                    var $checkBoxLabel = $("<label class='ntsCheckBox'></label>");
                    var $checkBox = $('<input type="checkbox">').on("change", function() {
                        setChecked($(this).is(":checked"));
                    }).appendTo($checkBoxLabel);
                    var $box = $("<span class='box'></span>").appendTo($checkBoxLabel);
                    if (checkBoxText && checkBoxText.length > 0)
                        var label = $("<span class='label'></span>").html(checkBoxText).appendTo($checkBoxLabel);
                    $checkBoxLabel.appendTo($wrapper);
    
                    var checked = initValue !== undefined ? initValue : true;
                    $wrapper.data("readonly", this.readOnly);
                    var $checkBox = $wrapper.find("input[type='checkbox']");
    
                    if (checked === true) $checkBox.attr("checked", "checked");
                    else $checkBox.removeAttr("checked");
                    if (data.enable === true) $checkBox.removeAttr("disabled");
                    else $checkBox.attr("disabled", "disabled");
                    return $wrapper;
                }
    
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $wrapper.find("input[type='checkbox']").attr("disabled", "disabled");
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $wrapper.find("input[type='checkbox']").removeAttr("disabled");
                }
            }
    
            class SwitchButtons extends NtsControlBase {
                containerClass(): string {
                    return "nts-switch-container";
                }
    
                draw(data: any): JQuery {
                    var selectedCssClass = 'selected';
                    var options = data.controlDef.options;
                    var optionsValue = data.controlDef.optionsValue;
                    var optionsText = data.controlDef.optionsText;
                    var selectedValue = data.initValue;
                    var container = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                    container.on(events.Handler.KEY_UP, function(evt: any) {
                        let $buttons: any = container.find("button");
                        let index;
                        $buttons.each(function(i, elm) {
                            if (elm === document.activeElement) {
                                index = i;
                                return false;
                            }
                        });
                        
                        if (!util.isNullOrUndefined(index)) {
                            let arrowNav = false;
                            if (utils.isArrowLeft(evt)) {
                                index = index === 0 ? ($buttons.length - 1) : --index;
                                arrowNav = true;
                            }
                            if (utils.isArrowRight(evt)) {
                                index = index === $buttons.length - 1 ? 0 : ++index;
                                arrowNav = true;
                            }
                            
                            let $targetButton = $buttons.eq(index);
                            $targetButton.focus();
                            if (arrowNav) {
                                var selectedValue = $targetButton.data('swbtn');
                                $('button', container).removeClass(selectedCssClass);
                                $targetButton.addClass(selectedCssClass);
                                data.update(selectedValue);
                            }
                        }
                    });
    
                    let distinction = data.controlDef.distinction;
                    let switchOptions;
                    if (distinction && (switchOptions = distinction[data.rowId])) {
                        switchOptions = options.filter(function(o) {
                            return switchOptions.indexOf(o.value) > -1;
                        });
                    } else {
                        switchOptions = options;
                    }
                    
                    _.forEach(switchOptions, function(opt) {
                        var value = opt[optionsValue];
                        var text = opt[optionsText];
    
                        var btn = $('<button>').text(text).css("height", "26px")
                            .addClass('nts-switch-button')
                            .attr('data-swbtn', value)
                            .attr('tabindex', -1)
                            .on('click', function() {
                                var selectedValue = $(this).data('swbtn');
                                $('button', container).removeClass(selectedCssClass);
                                $(this).addClass(selectedCssClass);
                                data.update(selectedValue);
                            });
                        if (value === selectedValue) {
                            btn.addClass(selectedCssClass);
                        }
                        container.append(btn);
                    });
                    (data.enable === true) ? $('button', container).prop("disabled", false)
                        : $('button', container).prop("disabled", true);
                    return container;
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $('button', $wrapper).prop("disabled", false);
                }
    
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $('button', $wrapper).prop("disabled", true);
                }
    
            }
    
            class ComboBox extends NtsControlBase {
                containerClass(): string {
                    return "nts-combo-container";
                }
    
                draw(data: any): JQuery {
                    var self = this;
                    // Default values.
                    var distanceColumns = data.controlDef.spaceSize === "small" ? '  ' : '     ';
                    // Character used fill to the columns.
                    var fillCharacter = ' ';
                    var maxWidthCharacter = 15;
                    var container = $("<div/>").addClass(this.containerClass()).data("enable", data.enable);
                    var columns: Array<any> = data.controlDef.columns;
    
                    // Set attribute for multi column.
                    var itemTemplate: string = undefined;
                    var haveColumn = columns && columns.length > 0;
                    if (haveColumn) {
                        itemTemplate = '<div class="nts-combo-item">';
                        _.forEach(columns, function(item, i) {
                            // Set item template.
                            itemTemplate += '<div class="nts-column nts-combo-column-' + i + '">${' + item.prop + '}</div>';
                        });
                        itemTemplate += '</div>';
                    }
    
                    // Display full code name
                    if (data.controlDef.displayMode === "codeName") {
                        data.controlDef.options = data.controlDef.options.map((option) => {
                            var newOptionText: string = '';
                            if (haveColumn) {
                                _.forEach(columns, function(item, i) {
                                    var prop: string = option[item.prop];
                                    var length: number = item.length;
        
                                    if (i === columns.length - 1) {
                                        newOptionText += prop;
                                    } else {
                                        newOptionText += text.padRight(prop, fillCharacter, length) + distanceColumns;
                                    }
                                });
        
                            } else {
                                newOptionText = option[data.controlDef.optionsText];
                            }
                            option['nts-combo-label'] = newOptionText;
                            return option;
                        });
                    }
    
                    var comboMode: string = data.controlDef.editable ? 'editable' : 'dropdown';
                    container.igCombo({
                        dataSource: data.controlDef.options,
                        valueKey: data.controlDef.optionsValue,
                        textKey: data.controlDef.displayMode === 'codeName' 
                                    ? 'nts-combo-label' : data.controlDef.optionsText,
                        mode: comboMode,
                        disabled: !data.enable,
                        placeHolder: '',
                        enableClearButton: false,
                        initialSelectedItems: [
                            { value: data.initValue }
                        ],
                        itemTemplate: itemTemplate,
//                        selectionChanging: function(evt: any, ui: any) {
//                            var __self = self; 
//                            let $gridControl = $(evt.target).closest("div[class*=nts-grid-control]");
//                            if (util.isNullOrUndefined($gridControl)) return;
//                            let cls = $gridControl.attr("class");
//                            let classNameParts = cls.split("-");
//                            let rowId = classNameParts.pop();
//                            let columnKey = classNameParts.pop();
//                            let targetCell: any = __self.$containedGrid.igGrid("cellById", rowId, columnKey);
//                            let $comboContainer = $(targetCell).find("." + __self.containerClass());
//                            // Clear error if any
//                            let comboInput = $($comboContainer.find("input")[1]);
//                            comboInput.ntsError("clear");
//                            nts.uk.ui.errors.removeByElement(comboInput);
//                            comboInput.parent().removeClass("error");
//                        },
                        selectionChanged: function(evt: any, ui: any) {
                            var _self = self;
                            if (ui.items.length > 0) {
                                let selectedValue = ui.items[0].data[data.controlDef.optionsValue];
                                data.update(selectedValue);
                                
                                setTimeout(function() {
                                    var __self = _self; 
                                    let $gridControl = $(evt.target).closest("div[class*=nts-grid-control]");
                                    if (util.isNullOrUndefined($gridControl)) return;
                                    let cls = $gridControl.attr("class");
                                    let classNameParts = cls.split("-");
                                    let rowId = classNameParts.pop();
                                    let columnKey = classNameParts.pop();
                                    let targetCell: any = internal.getCellById(__self.$containedGrid, rowId, columnKey);
                                    if (!targetCell) return;
                                    let $comboContainer = targetCell.find("." + __self.containerClass());
                                    // Save selected item
                                    $comboContainer.data(internal.COMBO_SELECTED, selectedValue);
                                    
                                    if (data.bounce) {
                                        let bCell = internal.getCellById(__self.$containedGrid, rowId, data.bounce);
                                        let cell = { id: utils.parseIntIfNumber(rowId, __self.$containedGrid, utils.getColumnsMap(__self.$containedGrid)), 
                                                        columnKey: data.bounce, element: bCell ? bCell[0] : bCell };
                                        if (errors.any(cell)) {
                                            errors.clear(__self.$containedGrid, cell);
                                        }
                                        updating.updateCell(__self.$containedGrid, rowId, data.bounce, selectedValue);
                                    }
                                }, 0);
                            }
                        },
                        rendered: function() {
                            let tabIndex = !util.isNullOrUndefined(data.tabIndex) ? data.tabIndex : -1;
                            container.igCombo("option", "tabIndex", tabIndex);
                        }
                    });
                    
                    // Save init value
                    container.data(internal.COMBO_SELECTED, data.initValue);
    
                    // Set width for multi columns.
                    if (haveColumn) {
                        var totalWidth = 0;
                        var $dropDownOptions: any = container.igCombo("dropDown");
                        _.forEach(columns, function(item, i) {
                            var charLength: number = item.length;
                            var width = charLength * maxWidthCharacter + 10;
                            let $comboCol = $dropDownOptions.find('.nts-combo-column-' + i);
                            $comboCol.width(width);
                            if (i !== columns.length - 1) {
                                $comboCol.css("float", "left");
                            }
                            totalWidth += width + 10;
                        });
                        $dropDownOptions.find(".nts-combo-item").css({ minWidth: totalWidth });
                        container.css({ minWidth: totalWidth });
                    }
    
                    if (!util.isNullOrUndefined(data.controlDef.width)) {
                        container.igCombo("option", "width", data.controlDef.width);
                    }
                    container.data("columns", columns);
                    container.data("comboMode", comboMode);
                    return container;
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass());
                    $wrapper.data("enable", true);
                    $wrapper.igCombo("option", "disabled", false);
                }
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass());
                    $wrapper.data("enable", false);
                    $wrapper.igCombo("option", "disabled", true);
                }
            }
    
            class Button extends NtsControlBase {
                containerClass(): string {
                    return "nts-button-container";
                }
    
                draw(data: any): JQuery {
                    var $container = $("<div/>").addClass(this.containerClass());
                    var $button = $("<button/>").addClass("ntsButton").css("height", "25px").appendTo($container)
                        .text(data.controlDef.text || data.initValue).attr("tabindex", -1)
                        .data("enable", data.enable).on("click", $.proxy(data.controlDef.click, null, data.rowObj));
                    $button.prop("disabled", !data.enable);
                    return $container;
                }
    
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $wrapper.find(".ntsButton").prop("disabled", false);
                }
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $wrapper.find(".ntsButton").prop("disabled", true);
                }
            }
    
            class DeleteButton extends Button {
                draw(data: any): JQuery {
                    var btnContainer = super.draw(data);
                    var btn = btnContainer.find("button");
                    btn.off("click", data.controlDef.click);
                    btn.on("click", data.deleteRow);
                    return btn;
                }
            }
            
            class TextEditor extends NtsControlBase {
                containerClass(): string {
                    return "nts-editor-container";
                }
                
                draw(data: any): JQuery {
                    let self = this;
                    let constraint = data.controlDef.constraint;
                    let $container = $("<div/>").addClass(this.containerClass());
                    let $input = $("<input/>").addClass("nts-editor nts-input").css({ padding: "2px", width: "96%" })
                                    .attr("tabindex", -1).val(data.initValue);
                    if (constraint.valueType === "Time") $input.css("text-align", "right");
                    let $editor = $("<span/>").addClass("nts-editor-wrapper ntsControl").css("width", "100%").append($input).appendTo($container);
                    
                    let cell;
                    self.validate(data.controlDef, data.initValue).success(t => {
                        $input.val(t);
                        $input.data(internal.TXT_RAW, data.initValue);
                    }).terminate();
                    
                    let valueToDs = function(valueType, before, after) {
                        switch (valueType) {
                            case "Integer":
                            case "HalfInt":
                            case "String":
                                return before;
                            case "Time":
                                return after;
                        }
                    };
                    
                    $input.on(events.Handler.KEY_DOWN, function(evt) {
                        // TODO: Add check if error not occurred on this cell,
                        // depends on which border to set red.
                        if (utils.isEnterKey(evt)) {
                            let value = $input.val();
                            self.validate(data.controlDef, value).success(t => {
                                cell = self.cellBelongTo($input);
                                errors.clear(self.$containedGrid, cell);
                                let val = valueToDs(constraint.valueType, value, t);
                                $input.data(internal.TXT_RAW, val); 
                                data.update(val);
                            }).fail(errId => {
                                cell = self.cellBelongTo($input);
                                errors.set(self.$containedGrid, cell, resource.getMessage(errId));
                            }).terminate;
                        }
                    });
                    
                    $input.on(events.Handler.KEY_UP, function(evt) {
                        self.validate(data.controlDef, $input.val()).success(t => {
                            cell = self.cellBelongTo($input);
                            errors.clear(self.$containedGrid, cell);
                        }).fail(errId => {
                            cell = self.cellBelongTo($input);
                            errors.set(self.$containedGrid, cell, nts.uk.resource.getMessage(errId));
                        }).terminate();
                    });
                    $input.on(events.Handler.BLUR, function(evt) {
                        self.validate(data.controlDef, $input.val()).success(t => {
                            let value = $input.val();
                            cell = self.cellBelongTo($input);
                            errors.clear(self.$containedGrid, cell);
                            let val = valueToDs(constraint.valueType, value, t);
                            data.update(val);
                            $input.data(internal.TXT_RAW, val);
                            $input.val(t);
                        }).fail(errId => {
                            cell = self.cellBelongTo($input);
                            errors.set(self.$containedGrid, cell, nts.uk.resource.getMessage(errId));
                        }).terminate();
                    });
                    
                    $input.on(events.Handler.CLICK, function(evt) {
                        let rawValue = $input.data(internal.TXT_RAW);
                        if (!errors.any({ element: $input.closest("td")[0] }) 
                            && !util.isNullOrUndefined(rawValue)) $input.val(rawValue);
                    });
                    
                    return $container;
                }
                
                cellBelongTo($input: JQuery) {
                    let self = this;
                    let cell: any = {};
                    cell.element = $input.closest("td")[0];
                    let $gridControl = $input.closest("div[class*='nts-grid-control']");
                    if ($gridControl.length === 0) return;
                    let clazz = $gridControl.attr("class").split(" ")[0];
                    let pos = clazz.split("-");
                    cell.id = utils.parseIntIfNumber(pos.pop(), self.$containedGrid, utils.getColumnsMap(self.$containedGrid))
                    cell.columnKey = pos.pop();
                    return cell;
                }
                
                validate(controlDef: any, value: any) {
                    let constraint = controlDef.constraint;
                    if (constraint.required && (_.isEmpty(value) || _.isNull(value))) return validation.Result.invalid("FND_E_REQ_INPUT");
                    switch (constraint.valueType) {
                        case "Integer":
                            let valid = uk.ntsNumber.isNumber(value, false);
                            if (!valid) return validation.Result.invalid("FND_E_INTEGER");
                            let formatted = value;
                            if (constraint.format === "Number_Separated") {
                                formatted = uk.ntsNumber.formatNumber(value, { formatId: constraint.format });
                            }
                            return validation.Result.OK(formatted);
                        case "Time":
                            return validation.parseTime(value, constraint.format);
                        case "HalfInt": 
                            if (uk.ntsNumber.isHalfInt(value)) {
                                return validation.Result.OK(value);
                            }
                            return validation.Result.invalid("FND_E_HALFINT");
                        case "String":
                            return validation.Result.OK(value);
                    }
                }
                
                enable($container: JQuery): void {
                    let self = this;
                    let $wrapper = $container.find("." + self.containerClass());
                    $wrapper.find("input").prop("disabled", false);
                }
                disable($container: JQuery): void {
                    let self = this;
                    let $wrapper = $container.find("." + self.containerClass());
                    $wrapper.find("input").prop("disabled", true);
                }
            }
            
            class Label extends NtsControlBase {
                action: any;
                constructor(action?: any) {
                    super();
                    this.action = action;
                }
                
                containerClass(): string {
                    return "nts-label-container";
                }
                
                draw(data: any): JQuery {
                    let self = this;
                    var $container = $("<div/>").addClass(this.containerClass());
                    let $label = $("<label/>").addClass("ntsLabel").css({ padding: "3px 0px", display: "inline-block", width: "100%" }).text(data.text).appendTo($container);
                    if (self.action && _.isFunction(self.action)) {
                        $container.on(events.Handler.CLICK, function(evt) {
                            self.action();
                        });
                        $label.css({ cursor: "pointer" });
                    }
                    return $container;
                }
                
                enable($container: JQuery): void {
                    return;
                }
                disable($container: JQuery): void {
                    return;
                }
            }
            
            class LinkLabel extends NtsControlBase {
                containerClass(): string {
                    return "nts-link-container";
                }
                
                draw(data: any): JQuery {
                    return $('<div/>').addClass(this.containerClass()).append($("<a/>")
                                        .addClass("link-button").css({ backgroundColor: "inherit", color: "#0066CC" })
                                        .text(data.initValue).on("click", $.proxy(data.controlDef.click, null, data.rowId, data.columnKey)))
                                        .data("click", data.controlDef.click);
                }
                
                enable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $wrapper.find("a").css("color", "#0066CC").on("click", $wrapper.data("click"));
                }
                disable($container: JQuery): void {
                    var $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $wrapper.find("a").css("color", "#333").off("click");
                }
            }
            
            class FlexImage extends NtsControlBase {
                containerClass(): string {
                    return "nts-fleximage-container";
                }
                
                draw(data: any): JQuery {
                    let $container = $("<div/>").addClass(this.containerClass());
                    if (util.isNullOrUndefined(data.initValue) || _.isEmpty(data.initValue)) return $container;
                    let $image = $("<span/>").addClass(data.controlDef.source);
                    if (data.controlDef.click && _.isFunction(data.controlDef.click)) {
                        $container.on(events.Handler.CLICK, $.proxy(data.controlDef.click, null, data.columnKey, data.rowId))
                            .css({ cursor: "pointer" }).data(events.Handler.CLICK, data.controlDef.click);
                    }
                    return $container.append($image);
                }
                
                enable($container: JQuery): void {
                    let $wrapper = $container.find("." + this.containerClass()).data("enable", true);
                    $wrapper.on(events.Handler.CLICK, $wrapper.data(events.Handler.CLICK));
                }
                
                disable($container: JQuery): void {
                    let $wrapper = $container.find("." + this.containerClass()).data("enable", false);
                    $wrapper.off(events.Handler.CLICK);
                }
            }
            
            class Image extends NtsControlBase {
                containerClass(): string {
                   return "nts-image-container";
                }
                
                draw(data: any): JQuery {
                    return $("<div/>").addClass(this.containerClass()).append(
                        $("<span/>").addClass(data.controlDef.source));
                }
                enable($container: JQuery): void {
                }
                disable($container: JQuery): void {
                }
            }
            
            export module comboBox {
                
                export function getCopiedValue(cell: any, copiedText: string) {
                    let copiedValue;
                    let $comboBox = utils.comboBoxOfCell(cell);
                    if ($comboBox.length > 0) {
                        let items = $comboBox.igCombo("items");                   
                        let textKey = $comboBox.igCombo("option", "textKey");
                        let valueKey = $comboBox.igCombo("option", "valueKey");
                        _.forEach(items, function(item: any) {
                            if (item.data[textKey] === copiedText.trim()) {
                                copiedValue = item.data[valueKey];
                                return false;
                            }
                        });
                    }
                    return copiedValue;
                }
            }
        }
        
        module specialColumn {
            export let CODE: string = "code";
            export let COMBO_CODE: string = "comboCode";
            
            export function ifTrue(columnSpecialTypes: any, column: any, bounceCombos: any, flatCols: any) {
                if (util.isNullOrUndefined(column.ntsType)) return;
                if (column.ntsType === CODE) {
                    columnSpecialTypes[column.key] = { type: column.ntsType,
                                                       onChange: column.onChange };
                } else if (column.ntsType === COMBO_CODE) {
                    columnSpecialTypes[column.key] = { type: column.ntsType,
                                                       onChange: identity };
                    let index = _.findIndex(flatCols, function(o) {
                        return o.key === column.key;
                    });
                    let b;
                    if (index + 1 < flatCols.length && (b = flatCols[index + 1]) !== undefined) {
                        bounceCombos[b.key] = column.key;
                    }
                }
            }
            
            /**
             * Try column
             */
            export function tryDo($grid: JQuery, cell: any, pastedText: any, visibleColumnsMap?: any) {
                let columnTypes = $grid.data(internal.SPECIAL_COL_TYPES);
                let specialColumn;
                let columnKey = cell.columnKey;
                for (let key in columnTypes) {
                    if (key === columnKey) {
                        specialColumn = columnTypes[key];
                        break;
                    }
                }
                
                if (util.isNullOrUndefined(specialColumn)) return;
                visibleColumnsMap = !util.isNullOrUndefined(visibleColumnsMap) ? visibleColumnsMap : utils.getVisibleColumnsMap($grid);
                let isFixedColumn = utils.isFixedColumn(columnKey, visibleColumnsMap);
                let nextColumn = utils.nextColumnByKey(visibleColumnsMap, columnKey, isFixedColumn);
                if (util.isNullOrUndefined(nextColumn) || nextColumn.index === 0) return;
                
                let origDs = $grid.data(internal.ORIG_DS);
                let setting = $grid.data(internal.SETTINGS);
                let idx = setting.descriptor.keyIdxes[cell.id];
                let prevData;
                if (origDs && !util.isNullOrUndefined(idx) && (prevData = origDs[idx])) {
                    prevData = prevData[columnKey];
                }
                
                specialColumn.onChange(columnKey, cell.id, pastedText, prevData).done(function(res: any) {
                    let updatedRow = {};
                    let $gridRow = utils.rowAt(cell);
                    if (specialColumn.type === COMBO_CODE) {
                        let $nextCell = $grid.igGrid("cellById", $gridRow.data("id"), nextColumn.options.key);
                        let $comboContainer = $nextCell.find("." + ntsControls.COMBO_CLASS);
                        let ds = $comboContainer.igCombo("option", "dataSource");
                        let vKey = $comboContainer.igCombo("option", "valueKey");
                        if (util.isNullOrUndefined(ds)) return;
                        let valueExists;
                        _.forEach(ds._data, function(item: any) {
                            if (item[vKey].toString() === String(res.toString().trim())) {
                                valueExists = true;
                                return false;
                            } 
                        });
                        if (!valueExists) {
                            _.defer(() => {
                                updatedRow[columnKey] = "";
                                updating.updateRow($grid, $gridRow.data("id"), updatedRow, undefined, true);
                            });
                            return;
                        }
                    }
                    if (nextColumn.options.dataType === "number") {
                        updatedRow[nextColumn.options.key] = parseInt(res.toString().trim());
                    } else {
                        updatedRow[nextColumn.options.key] = String(res.toString().trim());
                    } 
                    updating.updateRow($grid, $gridRow.data("id"), updatedRow, undefined, true);
                }).fail(function(res: any) {
                    
                });
                return true;
            }
            
            function identity(key, id, value) {
                let dfd = $.Deferred();
                if (_.isNil(value) || value === "") {
                    dfd.resolve("-1");
                } else {
                    dfd.resolve(value);
                }
                return dfd.promise();  
            }
        }
        
        module copyPaste {
            
            enum CopyMode {
                SINGLE,
                MULTIPLE
            }
            
            enum PasteMode {
                NEW,
                UPDATE
            }
            
            export class Processor {
                $grid: JQuery;
                options: any;
                pasteInMode: PasteMode = PasteMode.UPDATE;
                copyMode: CopyMode;
                visibleColumnsMap: any;
            
                constructor(options?: any) {
                    this.options = options;
                }
                static addFeatures(options: any) {
                    selection.addFeature(options);
                    return new Processor(options);
                }
                
                /**
                 * $grid to handle copy paste
                 * $target to bind events to
                 */
                chainEvents($grid: JQuery, $target?: JQuery) {
                    var self = this;
                    self.$grid = $grid;
                    let target = !util.isNullOrUndefined($target) ? $target : $grid; 
                    events.Handler.pull(target).focusInWith(self).ctrlCxpWith(self);
                }
                
                /**
                 * Copy
                 */
                copyHandler(cut?: boolean) {
                    let selectedCells: Array<any> = selection.getSelectedCells(this.$grid);
                    let copiedData;
                    let checker = cut ? utils.isCuttableControls : utils.isCopiableControls;
                    nts.uk.ui.block.grayout();
                    if (selectedCells.length === 1) {
                        this.copyMode = CopyMode.SINGLE;
                        if (!checker(this.$grid, selectedCells[0].columnKey)) {
                            nts.uk.ui.block.clear();
                            return;
                        }
                        if (utils.isComboBox(this.$grid, selectedCells[0].columnKey)) {
                            let $comboBox = utils.comboBoxOfCell(selectedCells[0]);
                            if ($comboBox.length > 0) {
                                copiedData = $comboBox.igCombo("text");
                            }
                        } else {
                            let $cell = selectedCells[0].element;
                            let origVal = $cell.data(internal.CELL_ORIG_VAL);
                            copiedData = !util.isNullOrUndefined(origVal) ? origVal : $cell.text();
                        }
                    } else {
                        this.copyMode = CopyMode.MULTIPLE;
                        copiedData = this.converseStructure(selectedCells, cut);
                    }
                    $("#copyHelper").val(copiedData).select();
                    document.execCommand("copy");
                    nts.uk.ui.block.clear();
                    return selectedCells;
                }
                
                /**
                 * Converse structure
                 */
                converseStructure(cells: Array<any>, cut: boolean): string {
                    let self = this;
                    let maxRow = 0;
                    let minRow = 0;
                    let maxColumn = 0;
                    let minColumn = 0;
                    let structure = [];
                    let structData: string = "";
                    let $tdCell, origVal;
                    let checker = cut ? utils.isCuttableControls : utils.isCopiableControls;
                    _.forEach(cells, function(cell: any, index: number) {
                        let rowIndex = cell.rowIndex;
                        let columnIndex = utils.getDisplayColumnIndex(self.$grid, cell);
                        if (index === 0) {
                            minRow = maxRow = rowIndex;
                            minColumn = maxColumn = columnIndex;
                        }
                        if (rowIndex < minRow) minRow = rowIndex;
                        if (rowIndex > maxRow) maxRow = rowIndex;
                        if (columnIndex < minColumn) minColumn = columnIndex;
                        if (columnIndex > maxColumn) maxColumn = columnIndex;
                        if (util.isNullOrUndefined(structure[rowIndex])) {
                            structure[rowIndex] = {};
                        }
                        if (!checker(self.$grid, cell.columnKey)) return;
                        if (utils.isComboBox(self.$grid, cell.columnKey)) {
                            let $comboBox = utils.comboBoxOfCell(cell);
                            if ($comboBox.length > 0) {
                                structure[rowIndex][columnIndex] = $comboBox.igCombo("text");
                            }
                        } else {
                            $tdCell = cell.element;
                            origVal = $tdCell.data(internal.CELL_ORIG_VAL);
                            structure[rowIndex][columnIndex] = !util.isNullOrUndefined(origVal) ? origVal : $tdCell.text();
                        }
                    });
                    
                    for (var i = minRow; i <= maxRow; i++) {
                        for (var j = minColumn; j <= maxColumn; j++) {
                            if (util.isNullOrUndefined(structure[i]) || util.isNullOrUndefined(structure[i][j])) {
                                structData += "null";
                            } else {
                                structData += structure[i][j];
                            }
                            
                            if (j === maxColumn) structData += "\n";
                            else structData += "\t";
                        }
                    }
                    return structData;
                }
                
                /**
                 * Cut
                 */
                cutHandler() {
                    var self = this;
                    var selectedCells = this.copyHandler(true);
                    var cellsGroup = _.groupBy(selectedCells, "rowIndex");
                    _.forEach(Object.keys(cellsGroup), function(rowIdx: any) {
                        var $row = utils.rowAt(cellsGroup[rowIdx][0]);
                        var updatedRowData = {};
                        _.forEach(cellsGroup[rowIdx], function(cell: any) {
                            if (!utils.isCuttableControls(self.$grid, cell.columnKey)) return;
                            updatedRowData[cell.columnKey] = "";
                        });
                        updating.updateRow(self.$grid, $row.data("id"), updatedRowData);
                    });
                }
                
                /**
                 * Paste
                 */
                pasteHandler(evt: any) {
                    nts.uk.ui.block.grayout();
                    if (this.copyMode === CopyMode.SINGLE) {
                        this.pasteSingleCellHandler(evt);
                    } else {
                        this.pasteRangeHandler(evt);
                    }
                    nts.uk.ui.block.clear();
                }
                
                /**
                 * Paste single cell
                 */
                pasteSingleCellHandler(evt: any) {
                    let self = this;
                    let cbData = this.getClipboardContent(evt);
                    let selectedCells = selection.getSelectedCells(this.$grid);
                    let visibleColumnsMap = utils.getVisibleColumnsMap(self.$grid);
                    _.forEach(selectedCells, function(cell: any, index: number) {
                        if (!utils.isPastableControls(self.$grid, cell.columnKey)
                            || utils.isDisabled($(cell.element))) return;
                        
                        if (utils.isEditMode(self.$grid)) {
                            let editor = self.$grid.igGridUpdating("editorForCell", cell.element);
                            if (cell.element.has(editor).length > 0) {
                                let inputs = editor.find("input");
                                inputs[0].value = cbData;
                                inputs[1].value = cbData;
                                setTimeout(() => {
                                    inputs[0].focus();
                                }, 0);
                                return;
                            }
                        }
                        let rowIndex = cell.rowIndex;
                        let columnIndex = cell.index;
                        let $gridRow = utils.rowAt(cell);
                        let updatedRow = {};
                        let columnsGroup = utils.columnsGroupOfCell(cell, visibleColumnsMap);
                        let columnKey = columnsGroup[columnIndex].key;
                        
                        // When pasted cell is combox
                        if (utils.isComboBox(self.$grid, cell.columnKey)) {
                            let copiedValue = ntsControls.comboBox.getCopiedValue(cell, cbData);
                            if (!util.isNullOrUndefined(copiedValue)) {
                                updatedRow[columnKey] = columnsGroup[columnIndex].dataType === "number" 
                                    ? parseInt(copiedValue) : copiedValue;
                            } else {
                                // TODO: Handle if texts in item list not map pasted text.
                                let $combo = cell.element.find(".nts-combo-container")
                                let $comboInput = $($combo.find("input")[1]);
                                $comboInput.ntsError("set", "Pasted text not valid");
                                $combo.igCombo("text", "");
                                return;
                            }
                        } else {
                            setTimeout(function() {
                                specialColumn.tryDo(self.$grid, cell, cbData, visibleColumnsMap);
                            }, 1);
                            if (columnsGroup[columnIndex].dataType === "number") {
                                updatedRow[columnKey] = parseInt(cbData);
                            } else {
                                updatedRow[columnKey] = cbData;
                            }
                        }
                        updating.updateRow(self.$grid, $gridRow.data("id"), updatedRow);
                    });
                }
                
                /**
                 * Paste range
                 */
                pasteRangeHandler(evt: any) {
                    var cbData = this.getClipboardContent(evt);
                    if (utils.isEditMode(this.$grid)) {
                        cbData = this.processInEditMode(cbData);
                        this.updateInEditMode(cbData);
                    } else {
                        cbData = this.process(cbData);
                        this.pasteInMode === PasteMode.UPDATE ? this.updateWith(cbData) : this.addNew(cbData);
                    }
                }
                
                /**
                 * Get clipboard content
                 */
                getClipboardContent(evt: any) {
                    if (window.clipboardData) {
                        window.event.returnValue = false;
                        return window.clipboardData.getData("text");
                    } else {
                        return evt.originalEvent.clipboardData.getData("text/plain");
                    }
                }
                
                /**
                 * Process in edit mode
                 */
                private processInEditMode(data: string) {
                    if (util.isNullOrUndefined(data)) return;
                    return data.split("\n")[0];
                }
                
                /**
                 * Update in edit mode
                 */
                private updateInEditMode(data: string) {
                    let selectedCell = selection.getSelectedCell(this.$grid);
                    let rowIndex = selectedCell.rowIndex;
                    let columnIndex = selectedCell.index;
                    let visibleColumnsMap = utils.getVisibleColumnsMap(this.$grid);
                    let updateRow = {};
                    let columnsGroup = utils.columnsGroupOfCell(selectedCell, visibleColumnsMap);
                    let columnKey = columnsGroup[columnIndex].key;
                    updateRow[columnKey] = data;
                    let $gridRow = utils.rowAt(selectedCell);
                    updating.updateRow(this.$grid, $gridRow.data("id"), updateRow);
                }
                
                /**
                 * Process
                 */
                private process(data: string) {
                    var dataRows = _.map(data.split("\n"), function(row) {
                        return row.split("\t");
                    });
                    
                    var rowsCount = dataRows.length;
                    if ((dataRows[rowsCount - 1].length === 1 && dataRows[rowsCount - 1][0] === "")
                        || dataRows.length === 1 && dataRows[0].length === 1 
                            && (dataRows[0][0] === "" || dataRows[0][0] === "\r")) {
                        dataRows.pop();
                    }
                    return dataRows;
                }
                
                /**
                 * Update
                 */
                private updateWith(data: any) {
                    var self = this;
                    if (!utils.selectable(this.$grid) || !utils.updatable(this.$grid)) return;
                    var selectedCell: any = selection.getSelectedCell(this.$grid);
                    if (selectedCell === undefined) return;
                    selectedCell.element.focus();
                    
                    var visibleColumnsMap = utils.getVisibleColumnsMap(self.$grid);
                    var visibleColumns = utils.visibleColumnsFromMap(visibleColumnsMap);
                    var columnIndex = selectedCell.index;
                    var rowIndex = selectedCell.rowIndex;
//                    if (!this.pasteable(columnIndex + data[0].length - visibleColumns.length)) return;
                    
                    let targetCol = _.find(visibleColumns, function(column: any) {
                        return column.key === selectedCell.columnKey;
                    });
                    if (util.isNullOrUndefined(targetCol)) return;
                    
                    _.forEach(data, function(row: any, idx: number) {
                        var $gridRow;
                        if (idx === 0) $gridRow = utils.rowAt(selectedCell);
                        else $gridRow = utils.nextNRow(selectedCell, idx);
                        if (util.isNullOrUndefined($gridRow)) return;
                        var rowData = {};
                        let targetIndex = columnIndex;
                        let targetCell = selectedCell;
                        let targetColumn = targetCol;
                        
                        // Errors
                        let comboErrors = [];
                        for (var i = 0; i < row.length; i++) {
                            let nextColumn;
                            let columnKey = targetColumn.key;
                            let cellElement = self.$grid.igGrid("cellById", $gridRow.data("id"), columnKey);
                            if ((!util.isNullOrUndefined(row[i]) && row[i].trim() === "null")
                                || !utils.isPastableControls(self.$grid, columnKey)
                                || utils.isDisabled(cellElement)) {
                                // Go to next column
                                nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                                targetColumn = nextColumn.options;
                                targetIndex = nextColumn.index;
                                continue;
                            }
                            let columnsGroup = utils.columnsGroupOfColumn(targetColumn, visibleColumnsMap);
                            if (targetIndex > columnsGroup.length - 1) break;
                            
                            if (utils.isComboBox(self.$grid, columnKey)) {
                                let cellContent = row[i].trim();
                                let copiedValue = ntsControls.comboBox.getCopiedValue({ element: cellElement[0] }, cellContent);
                                if (!util.isNullOrUndefined(copiedValue)) {
                                    rowData[columnKey] = targetColumn.dataType === "number" ? parseInt(copiedValue) : copiedValue;
                                } else {
                                    // TODO: Handle if copied text not match any item in combobox list
                                    comboErrors.push({ cell: cellElement, content: cellContent });
                                    
                                    // Go to next column
                                    nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                                    targetColumn = nextColumn.options;
                                    targetIndex = nextColumn.index;
                                    continue;
                                }
                            } else {
                                let cell: any = {};
                                cell.columnKey = columnKey;
                                cell.element = cellElement;
                                cell.id = $gridRow.data("id");
                                cell.index = targetIndex;
                                cell.row = $gridRow;
                                cell.rowIndex = $gridRow.data("rowIdx");
                                (function(i) {
                                    setTimeout(function() {
                                        specialColumn.tryDo(self.$grid, cell, row[i].trim(), visibleColumnsMap);
                                    }, 1);
                                })(i);
                                
                                if (targetColumn.dataType === "number") {
                                    rowData[columnKey] = parseInt(row[i]);
                                } else {
                                    rowData[columnKey] = row[i];
                                }
                            }
                            // Go to next column
                            nextColumn = utils.nextColumn(visibleColumnsMap, targetIndex, targetColumn.fixed);
                            targetColumn = nextColumn.options;
                            targetIndex = nextColumn.index;
                        }
                        updating.updateRow(self.$grid, $gridRow.data("id"), rowData);    
                        _.forEach(comboErrors, function(combo: any) {
                            setTimeout(function() {
                                let $container = combo.cell.find(".nts-combo-container");
                                let $comboInput = $($container.find("input")[1]);
                                $comboInput.ntsError("set", "Pasted text not valid");
                                $container.igCombo("text", combo.content);
                            }, 0);
                        });
                    });
                }
                
                private addNew(data: any) {
                    var self = this;
//                    var visibleColumns = this.getVisibleColumns();
                    var visibleColumns = null;
                    if (!this.pasteable(data[0].length - visibleColumns.length)) return;
                    
                    _.forEach(data, function(row: any, idx: number) {
                        var rowData = {};
                        for (var i = 0; i < visibleColumns.length; i++) {
                            var columnKey = visibleColumns[i].key;
                            if (visibleColumns[i].dataType === "number") {
                                rowData[columnKey] = parseInt(row[i]);
                            } else {
                                rowData[columnKey] = row[i];
                            }
                        }
                        self.$grid.igGridUpdating("addRow", rowData);
                    });
                }
                
                private pasteable(excessColumns) {
                    if (excessColumns > 0) {
                        nts.uk.ui.dialog.alert("Copied table structure doesn't match.");
                        return false;
                    }
                    return true; 
                } 
            }
            
            export function ifOn($grid: JQuery, options: any) {
                if (options.ntsFeatures === undefined) return;
                _.forEach(options.ntsFeatures, function(f: any) {
                    if (f.name === feature.COPY_PASTE) {
                        Processor.addFeatures(options).chainEvents($grid);
                        return false;
                    }    
                });
            }   
        }
        
        module events {
            export class Handler {
                static KEY_DOWN: string = "keydown";
                static KEY_UP: string = "keyup";
                static FOCUS_IN: string = "focusin";
                static BLUR: string = "blur";
                static CLICK: string = "click";
                static MOUSE_DOWN: string = "mousedown";
                static SCROLL: string = "scroll";
                static GRID_EDIT_CELL_STARTED: string = "iggridupdatingeditcellstarted";
                static COLUMN_RESIZING: string = "iggridresizingcolumnresizing";
                static RECORDS: string = "iggridvirtualrecordsrender";
                static CELL_CLICK: string = "iggridcellclick";
                static PAGE_INDEX_CHANGE: string = "iggridpagingpageindexchanging";
                static PAGE_SIZE_CHANGE: string = "iggridpagingpagesizechanging";
                static CONTROL_CHANGE: string = "ntsgridcontrolvaluechanged";
                $grid: JQuery;
                options: any;
                preventEditInError: boolean;
                
                constructor($grid: JQuery, options: any) {
                    this.$grid = $grid;
                    this.options = options;
                    this.preventEditInError = !util.isNullOrUndefined(options) ? options.preventEditInError : undefined;
                }
                
                static pull($grid: JQuery, options?: any): Handler {
                    return new Handler($grid, options);
                }
                
                turnOn($mainGrid?: JQuery) {
                    if (feature.isEnable(this.options.ntsFeatures, feature.CELL_EDIT)) {
                        this.filter($mainGrid).onCellUpdate().onCellUpdateKeyUp();
                    }
                    if (!util.isNullOrUndefined(this.options.enter) 
                        && (utils.selectable(this.$grid) || utils.selectable($mainGrid))) {
                        this.onDirectEnter();
                    }
                    if (utils.selectable(this.$grid) || utils.selectable($mainGrid)) {
                        this.onSpacePress();
                    }
                    if (feature.isEnable(this.options.features, feature.RESIZING)) {
                        this.onColumnResizing();
                    }
                }
                
                /**
                 * Handle enter direction.
                 */
                onDirectEnter() {
                    // Enter direction
                    var direction: selection.Direction = new selection.Direction();
                    if (!direction.to) direction.to = this.options.enter;
                    this.$grid.on(Handler.KEY_DOWN, $.proxy(direction.bind, direction));
                    this.$grid.data(internal.ENTER_DIRECT, direction);
                    return this;
                }
                
                /**
                 * Handle cell edit.
                 */
                onCellUpdate() {
                    var self = this;
                    this.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (evt.ctrlKey) return;
                        let selectedCell: any = selection.getSelectedCell(self.$grid);
                        updating.triggerCellUpdate(evt, selectedCell);
                    });
                    return this;
                }
                
                /**
                 * Handle validation.
                 */
                onCellUpdateKeyUp() {
                    var self = this;
                    this.$grid.on(Handler.KEY_UP, function(evt: any) {
                        if (evt.ctrlKey) return;
                        let selectedCell: any = selection.getSelectedCell(self.$grid);
                        updating.onEditCell(evt, selectedCell);
                    });
                    return this;
                }
                
                /**
                 * Handle press space key on combobox.
                 */
                onSpacePress() {
                    var self = this;
                    self.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (!utils.isSpaceKey(evt)) return;
                        var selectedCell: any = selection.getSelectedCell(self.$grid);
                        if (util.isNullOrUndefined(selectedCell)) return;
                        var checkbox = $(selectedCell.element).find(".nts-checkbox-container");
                        if (checkbox.length > 0) {
                            checkbox.find("input[type='checkbox']").click();
                        }
                    });
                    return this;
                }
                
                /**
                 * Support copy paste.
                 */
                focusInWith(processor: copyPaste.Processor) {
                    this.$grid.on(Handler.FOCUS_IN, function(evt: any) {
                        if ($("#pasteHelper").length > 0 && $("#copyHelper").length > 0) return;
                        var pasteArea = $("<textarea id='pasteHelper'/>").css({ "opacity": 0, "overflow": "hidden" })
                                            .on("paste", $.proxy(processor.pasteHandler, processor));
                        var copyArea = $("<textarea id='copyHelper'/>").css({ "opacity": 0, "overflow": "hidden" });
                        $("<div/>").css({ "position": "fixed", "top": -10000, "left": -10000 })
                                    .appendTo($(document.body)).append(pasteArea).append(copyArea);
                    });
                    return this;
                }
                
                /**
                 * Copy, cut, paste events.
                 */
                ctrlCxpWith(processor: copyPaste.Processor) {
                    this.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (evt.ctrlKey && utils.isPasteKey(evt)) {
                            $("#pasteHelper").focus();
                        } else if (evt.ctrlKey && utils.isCopyKey(evt)) {
                            processor.copyHandler();
                        } else if (evt.ctrlKey && utils.isCutKey(evt)) {
//                            processor.cutHandler();   
                        }
                    });
                    return this;
                }
                
                /**
                 * Prevent forwarding events in particular cases.
                 */
                filter($target?: JQuery) {
                    var self = this;
                    let $mainGrid = !util.isNullOrUndefined($target) ? $target : self.$grid; 
                    
                    self.$grid.on(Handler.KEY_DOWN, function(evt: any) {
                        if (utils.isAlphaNumeric(evt) || utils.isMinusSymbol(evt) || utils.isDeleteKey(evt)) {
                            let cell = selection.getSelectedCell($mainGrid);
                            if (cell === undefined || updating.containsNtsControl($(evt.target)))  
                                evt.stopImmediatePropagation();
                            return;
                        }
                        
                        if (utils.isTabKey(evt) && utils.isErrorStatus($mainGrid)) {
                            evt.preventDefault();
                            evt.stopImmediatePropagation();
                        }
                    });
                    
                    if (this.preventEditInError) {
                        self.$grid[0].addEventListener(Handler.MOUSE_DOWN, function(evt: any) {
                            if (utils.isNotErrorCell($mainGrid, evt)) {
                                evt.preventDefault();
                                evt.stopImmediatePropagation();
                            }
                        }, true);
                        self.$grid[0].addEventListener(Handler.CLICK, function(evt: any) {
                            if (utils.isNotErrorCell($mainGrid, evt)) {
                                evt.preventDefault();
                                evt.stopImmediatePropagation();
                            }
                        }, true);
                    }
                    
                    return this;
                }
                
                onColumnResizing() {
                    var self = this;
                    // Not fired on fixed table but main grid (table)
                    self.$grid.on(Handler.COLUMN_RESIZING, function(evt: any, args: any) {
                        columnSize.save(self.$grid, args.columnKey, args.desiredWidth);
                    });
                    return this;
                }
            }
            
            /**
             * Post render process
             */
            export function afterRendered(options: any, cbSelectionColumns: any) {
                options.rendered = function(evt: any, ui: any) {
                    let $grid = $(evt.target);
                    events.Handler.pull($grid, options).turnOn();
                    let cbSelect = $grid.data(internal.CB_SELECTED);
                    if (cbSelect) {
                        _.merge(cbSelect, cbSelectionColumns);
                    } else {
                        $grid.data(internal.CB_SELECTED, cbSelectionColumns);
                    }
                    
                    // Bind events for fixed table part
                    let $fixedTbl = fixedColumns.getFixedTable($grid);
                    if ($fixedTbl.length > 0) {
                        if (feature.isEnable(options.ntsFeatures, feature.COPY_PASTE))
                            new copyPaste.Processor().chainEvents($grid, $fixedTbl);
                        events.Handler.pull($fixedTbl, options).turnOn($grid);
                    }
                    // Sheet scroll
                    let sheetConfig: any = $grid.data(internal.SHEETS);
                    sheet.onScroll($grid);
                    if (!util.isNullOrUndefined(sheetConfig) && !util.isNullOrUndefined(sheetConfig.currentPosition)) {
//                        let displayPos = sheetConfig.displayScrollTop;
                        $grid.igGrid("virtualScrollTo", sheetConfig.currentPosition);
//                        utils.getScrollContainer($grid).scrollTop(parseInt(sheetConfig.currentPosition));
//                        _.defer(function() {
//                            utils.getDisplayContainer($grid).scrollTop(displayPos);
//                        });
                    }
                    
                    // Set selected cell if any
                    let selectedCell = $grid.data(internal.SELECTED_CELL);
                    if (!util.isNullOrUndefined(selectedCell)) {
                        let fixedColumns = utils.getVisibleFixedColumns($grid);
                        if (_.find(fixedColumns, function(col) {
                                return col.key === selectedCell.columnKey;
                            }) !== undefined) {
                            setTimeout(function() {
                                selection.selectCell($grid, selectedCell.rowIndex, selectedCell.index, true);
                            }, 1);
                        }
                    }
                    // Mark errors
                    errors.mark($grid);
                    color.styleHeaders($grid, options);
                    if (options.autoFitWindow) {
                        // Resize grid
                        settings.setGridSize($grid);
                    }
                    // Load columns size
                    columnSize.load($grid);
                    utils.setChildrenTabIndex($grid, -1);
                };
            }
            
            /**
             * Cell click
             */
            export function onCellClick($grid: JQuery) {
                $grid.on(Handler.CELL_CLICK, function(evt: any, ui: any) {
                    if (!utils.isEditMode($grid) && errors.any({ element: ui.cellElement })) {
                        _.defer(function() {
                            let $editor = $(ui.cellElement).find(errors.EDITOR_SELECTOR);
                            if ($editor.length === 0) return;
                            $editor.css(errors.ERROR_STL);
                        });
                    }
                });
            }
        }
        
        module validation {
            export let VALIDATORS: string = "ntsValidators"; 
            let H_M_MAX: number = 60;
            
            export class ColumnFieldValidator {
                name: string;
                primitiveValue: string;
                options: any;
                constructor(name: string, primitiveValue: string, options: any) {
                    this.name = name;
                    this.primitiveValue = primitiveValue;
                    this.options = options;
                }
                
                probe(value: string) {
                    let valueType = this.primitiveValue ? ui.validation.getConstraint(this.primitiveValue).valueType
                                    : this.options.cDisplayType;
                    switch (valueType) {
                        case "String":
                            return new nts.uk.ui.validation.StringValidator(this.name, this.primitiveValue, this.options)
                                    .validate(value, this.options);
                        case "Integer":
                        case "Decimal":
                        case "HalfInt":
                            return new NumberValidator(this.name, valueType, this.primitiveValue, this.options) 
                                   .validate(value);
                        case "Currency":
                            let opts: any = new ui.option.CurrencyEditorOption();
                            opts.grouplength = this.options.groupLength | 3;
                            opts.decimallength = this.options.decimalLength | 2;
                            opts.currencyformat = this.options.currencyFormat ? this.options.currencyFormat : "JPY";
                            return new NumberValidator(this.name, valueType, this.primitiveValue, opts)
                                    .validate(value);
                        case "Time":
                            this.options.mode = "time";
                            return new nts.uk.ui.validation.TimeValidator(this.name, this.primitiveValue, this.options)
                                    .validate(value);
                        case "Clock":
                            // Don't merge with time type.
                            this.options.mode = "time";
                            return new nts.uk.ui.validation.TimeValidator(this.name, this.primitiveValue, this.options)
                                    .validate(value);
                        case "TimeWithDay":
                            this.options.timeWithDay = true;
                            let result = new ui.validation.TimeWithDayValidator(this.name, this.primitiveValue, this.options)
                                            .validate(value);
                            if (result.isValid) {
                                let formatter = new text.TimeWithDayFormatter(this.options);
                                result.parsedValue = formatter.format(result.parsedValue);
                            }
                            return result;
                    }
                }
            }
            
            class NumberValidator {
                name: string;
                displayType: string;
                primitiveValue: string;s
                options: any;
                constructor(name: string, displayType: string, primitiveValue?: string, options?: any) {
                    this.name = name;
                    this.displayType = displayType;
                    this.primitiveValue = primitiveValue;
                    this.options = options;
                }
                
                validate(text: string) {
                    let self = this;
                    if (self.primitiveValue) {
                        return new nts.uk.ui.validation.NumberValidator(self.name, self.primitiveValue, self.options).validate(text);
                    }
                    
                    if (self.displayType === "Currency") {
                        text = uk.text.replaceAll(text, self.options.groupseperator, "");
                    }
                    
                    let result = new ui.validation.ValidationResult();
                    if ((util.isNullOrUndefined(text) || text.length === 0)) {
                        if (self.options && self.options.required) {
                            result.fail(nts.uk.resource.getMessage('FND_E_REQ_INPUT', [ self.name ]), 'FND_E_REQ_INPUT');
                            return result;
                        }
                        if (!self.options || (self.options && !self.options.required)) {
                            result.success(text);
                            return result;
                        }
                    }
                    let message: any = {};
                    let isValid;
                    if (self.displayType === "HalfInt") {
                         isValid = ntsNumber.isHalfInt(text, message);
                    } else if (self.displayType === "Integer") {
                        isValid = ntsNumber.isNumber(text, false, self.options, message);
                    } else if (self.displayType === "Decimal" || self.displayType === "Currency") {
                        isValid = ntsNumber.isNumber(text, true, self.options, message);
                    }
                    
                    let min = 0, max = 999999999;
                    let value = parseFloat(text);
                    if (!util.isNullOrUndefined(self.options.min)) {
                        min = self.options.min;
                        if (value < min) isValid = false;
                    }
                    if (!util.isNullOrUndefined(self.options.max)) {
                        max = self.options.max;
                        if (value > max) isValid = false;
                    }
                    
                    if (!isValid) {
                        result.fail(resource.getMessage(message.id, [ self.name, min, max ]), message.id);
                        return result;
                    }
                    
                    let formatter = new uk.text.NumberFormatter({ option: self.options });
                    let formatted = formatter.format(text);
                    result.success(self.displayType === "Currency" ? formatted : value + "");
                    return result;
                }
            }
            
            export class Result {
                isValid: boolean;
                formatted: any;
                errorMessageId: string;
                onSuccess: any = $.noop;
                onFail: any = $.noop;
                constructor(isValid: boolean, formatted?: any, messageId?: string) {
                    this.isValid = isValid;
                    this.formatted = formatted;
                    this.errorMessageId = messageId;
                }
                
                static OK(formatted: any) {
                    return new Result(true, formatted);
                }
                
                static invalid(msgId: string) {
                    return new Result(false, null, msgId);
                }
                
                success(cnt: any) {
                    this.onSuccess = cnt;
                    return this;
                }
                
                fail(cnt: any) {
                    this.onFail = cnt;
                    return this;
                }
                
                terminate() {
                    let self = this;
                    if (self.isValid && self.onSuccess && _.isFunction(self.onSuccess)) {
                         self.onSuccess(self.formatted);
                    } else if (!self.isValid && self.onFail && _.isFunction(self.onFail)) {
                        self.onFail(self.errorMessageId);
                    }
                }
            }
            
            function getValidators(columnsDef: any) : { [columnKey: string]: ColumnFieldValidator } {
                var validators: any = {};
                _.forEach(columnsDef, function(def: any) {
                    if (def.constraint === undefined) return;
                    validators[def.key] = new ColumnFieldValidator(def.headerText, def.constraint.primitiveValue, def.constraint); 
                });
                return validators;
            }
            
            export function scanValidators($grid: JQuery, columnsDef: any) {
                let columns = utils.analyzeColumns(columnsDef);
                $grid.data(VALIDATORS, getValidators(columns));
                return columns;
            }
            
            export function parseTime(value: any, format?: any): Result {
                if (uk.ntsNumber.isNumber(value, false)) {
                    if (value <= H_M_MAX) return Result.OK(value);
                    let hh = Math.floor(value / 100);
                    let mm = value % 100;
                    if (mm >= H_M_MAX) return Result.invalid("NEED_MSG_INVALID_TIME_FORMAT");
                    return Result.OK(hh + ":" + mm.toLocaleString("en-US", { minimumIntegerDigits: 2, useGrouping: false }));
                }
                let formatRes = uk.time.applyFormat(format, value, undefined);
                if (!formatRes) return Result.invalid("NEED_MSG_INVALID_TIME_FORMAT");
                return Result.OK(formatRes);
            }
            
            export function getValueType($grid: JQuery, columnKey: any) {
                let validators: any = $grid.data(validation.VALIDATORS);
                if (!validators || !validators[columnKey]) return;
                let column = validators[columnKey];
                return column.primitiveValue ? ui.validation.getConstraint(column.primitiveValue).valueType
                                    : column.options.cDisplayType
            }
            
            export function getGroupSeparator($grid: JQuery, columnKey: any) {
                let validators: any = $grid.data(validation.VALIDATORS);
                if (!validators || !validators[columnKey]) return;
                return validators[columnKey].options.groupseperator;
            }
        }
        
        module errors {
            export let HAS_ERROR = "hasError";
            export let ERROR_STL = { "border-color": "#ff6666" };
            export let NO_ERROR_STL = { "border-color": "" };
            export let EDITOR_SELECTOR = "div.ui-igedit-container";
            
            export class GridCellError {
                grid: JQuery;
                rowId: any;
                columnKey: string;
                columnName: string;
                message: string;
                
                constructor(grid: JQuery, rowId: any, columnKey: any, message: string) {
                    this.grid = grid;
                    this.rowId = rowId;
                    this.columnKey = columnKey;
                    this.message = message;
                    this.setColumnName();   
                }
                
                setColumnName() {
                    let allCols = utils.getColumns(this.grid);
                    if (!allCols) return;
                    let col = allCols.filter(c => c.key === this.columnKey);
                    if (col.length > 0) {
                        this.columnName = col[0].headerText;
                    }
                }
                
                equals(err: GridCellError) {
                    if (!this.grid.is(err.grid)) return false;
                    if (this.rowId !== err.rowId) return false;
                    if (this.columnKey !== err.columnKey) return false;
                    return true;
                }
            }
            
            function addCellError($grid: JQuery, error: any) {
                let gridErrors: Array<any> = $grid.data(internal.ERRORS);
                if (!gridErrors) {
                    $grid.data(internal.ERRORS, [ error ]);
                    return;
                }
                
                if (gridErrors.some(function(e: GridCellError) {
                    return e.equals(error);
                })) return;
                gridErrors.push(error);
            }
        
            function removeCellError($grid: JQuery, rowId: any, key: any) {
                let gridErrors: Array<any> = $grid.data(internal.ERRORS);
                if (!gridErrors) return;
                _.remove(gridErrors, function(e) {
                    return $grid.is(e.grid) && rowId === e.rowId && key === e.columnKey;
                });
            }
            
            export function mark($grid: JQuery) {
                let errorsLog = $grid.data(internal.ERRORS_LOG);
                if (util.isNullOrUndefined(errorsLog)) return;
                let sheets: any = $grid.data(internal.SHEETS);
                let sheetErrors = errorsLog[sheets.currentSheet];
                if (util.isNullOrUndefined(sheetErrors)) return;
                _.forEach(sheetErrors, function(cell: any) {
                    let $cell = $grid.igGrid("cellById", cell.id, cell.columnKey);
                    decorate($cell);
                });
            }
            
            function decorate($cell: any) {
                $cell.addClass(HAS_ERROR);
                $cell.css(ERROR_STL);
                let $editor = $cell.find(EDITOR_SELECTOR);
                if ($editor.length > 0) $editor.css(ERROR_STL); 
            }
            
            export function set($grid: JQuery, cell: any, message: string) {
                if (!cell || !cell.element || any(cell)) return;
                let $cell = $(cell.element);
                decorate($cell);
                let errorDetails = createErrorInfos($grid, cell, message);
                let setting = $grid.data(internal.SETTINGS);
                if (setting.errorsOnPage) {
                    ui.errors.addCell(errorDetails);
                }
                addCellError($grid, errorDetails);
                addErrorInSheet($grid, cell);
            }
            
            function createErrorInfos($grid: JQuery, cell: any, message: string): any {
                let record: any = $grid.igGrid("findRecordByKey", cell.id);
                let setting: any = $grid.data(internal.SETTINGS);
                let error: any = new GridCellError($grid, cell.id, cell.columnKey, message);
                // Error column headers
                let headers;
                if (setting.errorsOnPage) {
                    let columns = ko.toJS(ui.errors.errorsViewModel().option().headers());
                    if (columns) {
                        headers = columns.filter(c => c.visible).map(c => c.name); 
                    }
                } else { 
                    headers = setting.errorColumns;
                }
                _.forEach(headers, function(header: any) {
                    if (util.isNullOrUndefined(record[header]) 
                        || !util.isNullOrUndefined(error[header])) return;
                    error[header] = record[header];
                });
                return error;
            } 
            
            export function clear($grid: JQuery, cell: any) {
                if (!cell || !cell.element || !any(cell)) return;
                let $cell = $(cell.element);
                $cell.removeClass(HAS_ERROR);
                $cell.css(NO_ERROR_STL);
                let $editor = $cell.find(EDITOR_SELECTOR);
                if ($editor.length > 0) $editor.css(NO_ERROR_STL);
                let setting = $grid.data(internal.SETTINGS);
                if (setting.errorsOnPage) {
                    ui.errors.removeCell($grid, cell.id, cell.columnKey);
                }
                removeCellError($grid, cell.id, cell.columnKey);
                removeErrorFromSheet($grid, cell);
            }
            
            export function any(cell: any) {
                return cell.element && $(cell.element).hasClass(HAS_ERROR);
            }
            
            function addErrorInSheet($grid: JQuery, cell: any) {
                let errorsLog = $grid.data(internal.ERRORS_LOG) || {};
                let sheets: any = $grid.data(internal.SHEETS);
                if (util.isNullOrUndefined(errorsLog[sheets.currentSheet])) {
                    errorsLog[sheets.currentSheet] = [];
                } 
                errorsLog[sheets.currentSheet].push(cell);
                $grid.data(internal.ERRORS_LOG, errorsLog);
            }
            
            function removeErrorFromSheet($grid: JQuery, cell: any) {
                let errorsLog = $grid.data(internal.ERRORS_LOG);
                if (util.isNullOrUndefined(errorsLog)) return;
                let sheets: any = $grid.data(internal.SHEETS);
                let sheetErrors = errorsLog[sheets.currentSheet];
                if (util.isNullOrUndefined(sheetErrors)) {
                    removeErrorBasedOtherSheet(sheets, errorsLog, cell);
                    return;
                }
                
                let cellErrorIdx;
                _.forEach(sheetErrors, function(errorCell: any, i: number) {
                    if (cellEquals(errorCell, cell)) {
                        cellErrorIdx = i;
                        return false;
                    }
                });
                if (!util.isNullOrUndefined(cellErrorIdx)) {
                    errorsLog[sheets.currentSheet].splice(cellErrorIdx, 1);
                } else {
                    removeErrorBasedOtherSheet(sheets, errorsLog, cell);
                }
            }
            
            function removeErrorBasedOtherSheet(sheets: any, errorsLog: any, cell: any) {
                let sheetsIn = sheets.columnsInSheetImme[cell.columnKey];
                if (sheetsIn && sheetsIn.size > 1) {
                    _.forEach(Array.from(sheetsIn), function(s, i) {
                        if (s !== sheets.currentSheet) {
                            let oErrs = errorsLog[s];
                            if (oErrs) {
                                _.remove(oErrs, e => cellEquals(e, cell));
                            }
                        } 
                    });
                }
            }
            
            export function markIfError($grid: JQuery, cell: any) {
                let errorsLog = $grid.data(internal.ERRORS_LOG);
                if (util.isNullOrUndefined(errorsLog)) return;
                let sheets: any = $grid.data(internal.SHEETS);
                let sheetErrors = errorsLog[sheets.currentSheet];
                if (util.isNullOrUndefined(sheetErrors)) {
                    markBasedOtherSheet(sheets, errorsLog, cell);
                    return;
                }
                
                let marked = false;
                _.forEach(sheetErrors, function(c: any) {
                    if (cellEquals(c, cell)) {
                        decorate($(cell.element));
                        marked = true;
                        return false;
                    }
                });
                
                if (!marked) {
                    markBasedOtherSheet(sheets, errorsLog, cell);
                }
            }
            
            function markBasedOtherSheet(sheets: any, errorsLog: any, cell: any) {
                let sheetsIn = sheets.columnsInSheetImme[cell.columnKey];
                if (sheetsIn && sheetsIn.size > 1) {
                    _.forEach(Array.from(sheetsIn), function(s, i) {
                        if (s !== sheets.currentSheet) {
                            let marked = false;
                            _.forEach(errorsLog[s], function(c) {
                                if (cellEquals(c, cell)) {
                                    decorate($(cell.element));
                                    marked = true;
                                    return false;
                                }
                            });
                            if (marked) return false;
                        }
                    });
                }
            }
            
            function cellEquals(one: any, other: any) {
                if (one.columnKey !== other.columnKey) return false;
                if (one.id !== other.id) return false;
//                if (one.rowIndex !== other.rowIndex) return false;
                return true;
            }
        }
        
        export module color {
            export let Error: string = "ntsgrid-error";
            export let Alarm: string = "ntsgrid-alarm";
            export let ManualEditTarget: string = "ntsgrid-manual-edit-target";
            export let ManualEditOther: string = "ntsgrid-manual-edit-other";
            export let Reflect: string = "ntsgrid-reflect";
            export let Calculation: string = "ntsgrid-calc";
            export let Disable: string = "ntsgrid-disable";
            
            export class CellFormatter {
                $grid: JQuery;
                // Cell
                cellStateFeatureDef: any;
                statesTable: Array<any>;
                rowStates: any;
                // Row
                rowDisableFeatureDef: any;
                disableRows: any;
                // Text color
                textColorFeatureDef: any;
                textColorsTable: any;
                textStyleFeatureDef: any;
                textStylesTable: any;
                
                constructor($grid, features, ntsFeatures, flatCols) {
                    this.$grid = $grid;
                    // Cell
                    this.cellStateFeatureDef = feature.find(ntsFeatures, feature.CELL_STATE);
                    this.setStatesTable(ntsFeatures);
                    // Row
                    this.rowDisableFeatureDef = feature.find(ntsFeatures, feature.ROW_STATE);
                    if (!util.isNullOrUndefined(this.rowDisableFeatureDef) 
                        && !util.isNullOrUndefined(this.rowDisableFeatureDef.rows)) {
                        this.disableRows = _.groupBy(this.rowDisableFeatureDef.rows, "rowId");
                        this.addDisableRows(features, ntsFeatures, flatCols);
                    }
                    // Text color
                    this.textColorFeatureDef = feature.find(ntsFeatures, feature.TEXT_COLOR);
                    this.setTextColorsTableMap(ntsFeatures);
                    
                    // Text style
                    this.textStyleFeatureDef = feature.find(ntsFeatures, feature.TEXT_STYLE);
                    this.setTextStylesTableMap();
                }
                
                /**
                 * Add disable rows.
                 */
                addDisableRows(features: any, ntsFeatures: any, flatCols: any) {
                    let self = this;
                    let sheetMng = self.$grid.data(internal.SHEETS);
                    let columns;
                    if (sheetMng) {
                        columns = sheetMng.sheetColumns[sheetMng.currentSheet];
                    } else {
                        let sheetFt = feature.find(ntsFeatures, feature.SHEET);
                        if (sheetFt) {
                            let sheetDf = sheetFt.sheets.filter(function(s) {
                                return s.name === sheetFt.initialDisplay; 
                            })[0];
                            
                            if (!sheetDf) return;
                            self.rowDisableFeatureDef.rows.forEach(function(i) {
                                sheetDf.columns.forEach(function(c) {
                                    self.addDisableState(i.rowId, c);
                                });
                            });
                            
                            let columnFixingFt = feature.find(features, feature.COLUMN_FIX);
                            if (columnFixingFt) {
                                self.rowDisableFeatureDef.rows.forEach(function(i) {
                                    columnFixingFt.columnSettings.forEach(function(c) {
                                        self.addDisableState(i.rowId, c.columnKey);
                                    });
                                });
                            }
                            return;
                        } else {
                            columns = flatCols;
                        }
                    }
                    
                    if (columns) {
                        let setCellDisable = function(cols: Array<any>) {
                            cols.forEach(function(c) {
                                if (c.group) {
                                    setCellDisable(c.group);  
                                    return;
                                }
                                self.rowDisableFeatureDef.rows.forEach(function(i) {
                                    self.addDisableState(i.rowId, c.key);   
                                });
                            });
                        };
                        setCellDisable(columns);
                    }
                }
                
                /**
                 * Set states table
                 */
                private setStatesTable(features: any) {
                    let self = this;
                    if (util.isNullOrUndefined(this.cellStateFeatureDef)) return;
                    let rowIdName = this.cellStateFeatureDef.rowId;
                    let columnKeyName = this.cellStateFeatureDef.columnKey;
                    let stateName = this.cellStateFeatureDef.state;
                    this.statesTable = this.cellStateFeatureDef.states;
                    this.rowStates = _.groupBy(this.statesTable, rowIdName);
                    
                    _.forEach(this.rowStates, (value, key) => {
                        this.rowStates[key] = _.groupBy(this.rowStates[key], (item) => {
                            if (item[stateName].indexOf(color.Disable) > -1) {
                                self.addDisableState(item[rowIdName], item[columnKeyName]);
                            }
                            return item[columnKeyName];
                        });
                    });
                }
                
                /**
                 * Set text colors
                 */
                private setTextColorsTableMap(features: any) {
                    if (util.isNullOrUndefined(this.textColorFeatureDef)) return;
                    let rowIdName = this.textColorFeatureDef.rowId;
                    let columnKeyName = this.textColorFeatureDef.columnKey;
                    let colorName = this.textColorFeatureDef.color;
                    let colorsTable = this.textColorFeatureDef.colorsTable;
                    this.textColorsTable = _.groupBy(colorsTable, rowIdName);
                    _.forEach(this.textColorsTable, (value, key) => {
                        this.textColorsTable[key] = _.groupBy(this.textColorsTable[key], (item) => {
                            return item[columnKeyName];
                        });
                    });
                }
                
                /**
                 * Set text styles.
                 */
                private setTextStylesTableMap() {
                    if (util.isNullOrUndefined(this.textStyleFeatureDef)) return;
                    let rowIdName = this.textStyleFeatureDef.rowId;
                    let columnKeyName = this.textStyleFeatureDef.columnKey;
                    let styleName = this.textStyleFeatureDef.style;
                    let stylesTable = this.textStyleFeatureDef.styles;
                    this.textStylesTable = _.groupBy(stylesTable, rowIdName);
                    _.forEach(this.textStylesTable, (value, key) => {
                        this.textStylesTable[key] = _.groupBy(this.textStylesTable[key], columnKeyName);
                    });
                }
                
                /**
                 * Format textbox.
                 */
                format(column: any, notTb?: boolean) {
                    var self = this;
                    if (util.isNullOrUndefined(this.cellStateFeatureDef) 
                        || column.formatter !== undefined) return column;
                    let rowIdName: string = this.cellStateFeatureDef.rowId;
                    let columnKeyName: string = this.cellStateFeatureDef.columnKey;
                    let stateName: string = this.cellStateFeatureDef.state;
                    let statesTable: any = this.cellStateFeatureDef.states;
                    
                    column.formatter = function(value, rowObj) {
                        if (uk.util.isNullOrUndefined(rowObj)) return value;
                        let origValue = value;
                        if (!notTb && column.constraint) {
                            let constraint = column.constraint;
                            let valueType = constraint.primitiveValue ? ui.validation.getConstraint(constraint.primitiveValue).valueType
                                        : constraint.cDisplayType;
                            if (!uk.util.isNullOrUndefined(value) && !_.isEmpty(value)) {
                                if (valueType === "TimeWithDay") {
                                    let minutes = time.minutesBased.clock.dayattr.parseString(value).asMinutes;
                                    let timeOpts = { timeWithDay: true };
                                    let formatter = new text.TimeWithDayFormatter(timeOpts);
                                    if (!util.isNullOrUndefined(minutes)) {
                                        try {
                                            value = formatter.format(minutes);
                                        } catch(e) {}
                                    }
                                } else if (valueType === "Clock") {
                                    let minutes = time.minutesBased.clock.dayattr.parseString(value).asMinutes;
                                    let timeOpts = { timeWithDay: false };
                                    let formatter = new text.TimeWithDayFormatter(timeOpts);
                                    if (!util.isNullOrUndefined(minutes)) {
                                        try {
                                            value = formatter.format(minutes);
                                        } catch(e) {}
                                    }
                                } else if (valueType === "Currency") { 
                                    let currencyOpts: any = new ui.option.CurrencyEditorOption();
                                    currencyOpts.grouplength = constraint.groupLength | 3;
                                    currencyOpts.decimallength = constraint.decimalLength | 2;
                                    currencyOpts.currencyformat = constraint.currencyFormat ? constraint.currencyFormat : "JPY";
                                    let groupSeparator = constraint.groupSeparator || ",";
                                    let rawValue = text.replaceAll(value, groupSeparator, "");
                                    let formatter = new uk.text.NumberFormatter({ option: currencyOpts });
                                    let numVal = Number(rawValue);
                                    if (!isNaN(numVal)) value = formatter.format(numVal);
                                    else value = rawValue;
                                }
                            }
                        }
                        var _self = self;
                        setTimeout(function() {
                            let $gridCell = internal.getCellById(self.$grid, rowObj[self.$grid.igGrid("option", "primaryKey")], column.key);
                            if (!$gridCell) return;
                            $gridCell.data(internal.CELL_ORIG_VAL, origValue);
                            let $tr = $gridCell.closest("tr");
                            let cell = {
                                columnKey: column.key,
                                element: $gridCell[0],
                                rowIndex: $tr.data("rowIdx"),
                                id: $tr.data("id")
                            };
                            // If cell has error, mark it
                            errors.markIfError(self.$grid, cell);
                            color.markIfEdit(self.$grid, cell);
                            
//                            let aColumn = _.find(_self.colorFeatureDef, function(col: any) {
//                                return col.key === column.key;
//                            });
//                            
//                            if (util.isNullOrUndefined(aColumn)) return;
//                            let cellColor = aColumn.map(aColumn.parse(value));
//                            $gridCell.css("background-color", cellColor);
                            
                            // Disable row
                            if (!util.isNullOrUndefined(self.disableRows)) {
                                let disableRow = self.disableRows[cell.id];
                                if (!util.isNullOrUndefined(disableRow) && disableRow.length > 0 && disableRow[0].disable) {
                                    $gridCell.addClass(color.Disable);
//                                    self.addDisableState(cell.id, cell.columnKey);
                                }
                            }
                            // Set cell states
                            if (!util.isNullOrUndefined(self.rowStates) && !util.isNullOrUndefined(rowIdName) 
                                && !util.isNullOrUndefined(columnKeyName) && !util.isNullOrUndefined(stateName)
                                && !util.isNullOrUndefined(self.rowStates[cell.id])) {
                                let cellState = self.rowStates[cell.id][column.key];
                                if (util.isNullOrUndefined(cellState) || cellState.length === 0) return;
                                _.forEach(cellState[0][stateName], function(stt: any) {
                                    $gridCell.addClass(stt);
//                                    if (stt === color.Disable) self.addDisableState(cell.id, cell.columnKey);
                                });
                            }
                        }, 0);
                        return value;
                    };
                    return column;
                }
                
                /**
                 * Add disable state.
                 */
                addDisableState(id: any, key: any) {
                    let self = this;
                    let cbSelect = self.$grid.data(internal.CB_SELECTED);
                    
                    if (!cbSelect) {
                        cbSelect = {};
                        self.$grid.data(internal.CB_SELECTED, cbSelect);
                    }
                    
                    let cbColConf = cbSelect[key];
                    if (!cbColConf) {
                        let ds = new Set();
                        ds.add(id);
                        cbSelect[key] = { disableRows: ds }; 
                        return;
                    }
                    
                    if (!cbColConf.disableRows) {
                        cbColConf.disableRows = new Set();
                    }
                    cbColConf.disableRows.add(id);
                }
                
                /**
                 * Style common controls.
                 */
                style($grid: JQuery, cell: any) {
                    let self = this;
                    if (util.isNullOrUndefined(this.cellStateFeatureDef)) return;
                    let rowIdName: string = this.cellStateFeatureDef.rowId;
                    let columnKeyName: string = this.cellStateFeatureDef.columnKey;
                    let stateName: string = this.cellStateFeatureDef.state;
                    let statesTable: any = this.cellStateFeatureDef.states;
                    
                    // Disable row
                    let controlType = utils.getControlType($grid, cell.columnKey);
                    if (!util.isNullOrUndefined(this.disableRows)) {
                        let disableRow = this.disableRows[cell.id];
                        if (!util.isNullOrUndefined(disableRow) && disableRow.length > 0 && disableRow[0].disable) {
                            cell.$element.addClass(color.Disable);
                            utils.disableNtsControl($grid, cell, controlType);
//                            self.addDisableState(cell.id, cell.columnKey);
                        }
                    }
                    // Set cell states
                    if (!util.isNullOrUndefined(self.rowStates) && !util.isNullOrUndefined(rowIdName) 
                        && !util.isNullOrUndefined(columnKeyName) && !util.isNullOrUndefined(stateName)
                        && !util.isNullOrUndefined(self.rowStates[cell.id])) {
                        let cellState = self.rowStates[cell.id][cell.columnKey];
                        if (util.isNullOrUndefined(cellState) || cellState.length === 0) return;
                        _.forEach(cellState[0][stateName], function(stt: any) {
                            if (stt === Disable && !cell.$element.hasClass(Disable)) {
                                utils.disableNtsControl($grid, cell, controlType);
//                                self.addDisableState(cell.id, cell.columnKey);
                            }
                            cell.$element.addClass(stt);
                        });
                    }
                }
                
                /**
                 * Set text color
                 */
                setTextColor($grid: JQuery, cell: any) {
                    if (util.isNullOrUndefined(this.textColorFeatureDef)) return;
                    let rowIdName: string = this.textColorFeatureDef.rowId;
                    let columnKeyName: string = this.textColorFeatureDef.columnKey;
                    let colorName: string = this.textColorFeatureDef.color;
                    let colorsTable: any = this.textColorFeatureDef.colorsTable;
                    
                    if (!util.isNullOrUndefined(colorsTable) && !util.isNullOrUndefined(rowIdName)
                        && !util.isNullOrUndefined(columnKeyName) && !util.isNullOrUndefined(colorName)
                        && !util.isNullOrUndefined(this.textColorsTable[cell.id])) {
                        let textColor = this.textColorsTable[cell.id][cell.columnKey];
                        if (util.isNullOrUndefined(textColor) || textColor.length === 0) return;
                        let txtColor = textColor[0][colorName];
                        if (txtColor.indexOf("#") === 0) {
                            cell.$element.css("color", txtColor);
                            return;
                        }
                        cell.$element.addClass(txtColor);
                    }
                }
                
                /**
                 * Set text style.
                 */
                setTextStyle($grid: JQuery, cell: any) {
                    if (util.isNullOrUndefined(this.textStyleFeatureDef)) return;
                    let rowIdName: string = this.textStyleFeatureDef.rowId;
                    let columnKeyName: string = this.textStyleFeatureDef.columnKey;
                    let styleName: string = this.textStyleFeatureDef.style;
                    let stylesTable: any = this.textStyleFeatureDef.styles;
                    
                    if (!util.isNullOrUndefined(stylesTable) && !util.isNullOrUndefined(rowIdName)
                        && !util.isNullOrUndefined(columnKeyName) && !util.isNullOrUndefined(styleName)
                        && !util.isNullOrUndefined(this.textStylesTable[cell.id])) {
                        let textStyle = this.textStylesTable[cell.id][cell.columnKey];
                        if (util.isNullOrUndefined(textStyle) || textStyle.length === 0) return;
                        let txtStyle = textStyle[0][styleName];
                        cell.$element.addClass(txtStyle);
                    }
                }
            }
            
            /**
             * Style headers
             */
            export function styleHeaders($grid: JQuery, options: any) {
                let headerStyles = feature.find(options.ntsFeatures, feature.HEADER_STYLES);
                if (util.isNullOrUndefined(headerStyles)) return;
                setHeadersColor($grid, headerStyles.columns);
            }
            
            /**
             * Set header color
             */
            function setHeadersColor($grid: JQuery, columns: Array<any>) {
                let headersTable: any = $grid.igGrid("headersTable");
                let fixedHeadersTable: any = $grid.igGrid("fixedHeadersTable");
                fixedHeadersTable.find("th").each(function() {
                    let $self = $(this);
                    let columnId = $self.attr("id");
                    if (util.isNullOrUndefined(columnId)) {
                        let owns = $self.attr("aria-owns");
                        if (!owns) return;
                        let key = owns.split(" ")[0].split("_")[1];
                        setBackground($self, key, columns);
                        return;
                    }
                    let key = columnId.split("_")[1];
                    setBackground($self, key, columns);
                });
                
                headersTable.find("th").each(function() {
                    let $self = $(this);
                    let columnId = $self.attr("id");
                    if (util.isNullOrUndefined(columnId)) {
                        let owns = $self.attr("aria-owns");
                        if (!owns) return;
                        let key = owns.split(" ")[0].split("_")[1];
                        setBackground($self, key, columns);
                        return;
                    }
                    let key = columnId.split("_")[1];
                    setBackground($self, key, columns);
                });
            }
            
            /**
             * Set background.
             */
            function setBackground($cell: JQuery, key: any, columns: Array<any>) {
                let targetColumn;
                _.forEach(columns, function(col: any) {
                    if (col.key === key) { 
                        targetColumn = col;
                        return false;
                    }
                });
                if (!util.isNullOrUndefined(targetColumn)) {
                    if (targetColumn.color.indexOf("#") === 0) {
                        $cell.css("background-color", targetColumn.color);
                        return;
                    }
                    $cell.addClass(targetColumn.color);
                }
            }
            
            /**
             * Remember disable
             */
            export function rememberDisabled($grid: JQuery, cell: any) {
                let settings = $grid.data(internal.SETTINGS);
                if (!settings) return;
                let disables = settings.disables;
                if (!disables) return;
                let controlType = utils.getControlType($grid, cell.columnKey);
                let row = disables[cell.id];
                if (!row) return;
                row.forEach(function(c, i) {
                    if (c === cell.columnKey) {
                        utils.disableNtsControl($grid, cell, controlType);
                        cell.$element.addClass(Disable);
                        return false;
                    }
                });
            }
            
            /**
             * Push disable
             */
            export function pushDisable($grid: JQuery, cell: any) {
                let settings = $grid.data(internal.SETTINGS);
                if (!settings) return;
                let disables = settings.disables;
                if (!disables) {
                    settings.disables = {};
                }
                if (!settings.disables[cell.id] || settings.disables[cell.id].size === 0) {
                    let dset = new Set();
                    dset.add(cell.columnKey);
                    settings.disables[cell.id] = dset;
                    return;
                }
                settings.disables[cell.id].add(cell.columnKey);
            }
            
            /**
             * Pop disable
             */
            export function popDisable($grid: JQuery, cell: any) {
                let settings = $grid.data(internal.SETTINGS);
                if (!settings) return;
                let disables = settings.disables;
                if (!disables || !disables[cell.id] || disables[cell.id].size === 0) return;
                disables[cell.id].delete(cell.columnKey);
            }
            
            /**
             * Mark if edit.
             */
            export function markIfEdit($grid: JQuery, cell: any) {
                let targetEdits = $grid.data(internal.TARGET_EDITS);
                let cols: Array<any>;
                if (!targetEdits || !(cols = targetEdits[cell.id])) {
                    markIfOtherEdit($grid, cell);
                    return;
                }
                if (cols.some(function(c) {
                    return c === cell.columnKey;
                })) {
                    cell.element.classList.add(ManualEditTarget);
                } else markIfOtherEdit($grid, cell);
            }
            
            /**
             * Mark if other edit.
             */
            function markIfOtherEdit($grid: JQuery, cell: any) {
                let otherEdits = $grid.data(internal.OTHER_EDITS);
                let cols: Array<any>;
                if (!otherEdits || !(cols = otherEdits[cell.id])) return;
                if (cols.some(function(c) {
                    return c === cell.columnKey;
                })) {
                    cell.element.classList.add(ManualEditOther);
                }
            }
        }
        
        module fixedColumns {
            
            /**
             * Get fixed table
             */
            export function getFixedTable($grid: JQuery): JQuery {
                return $("#" + $grid.attr("id") + "_fixed");
            }
            
            /**
             * Get real grid
             */
            export function realGridOf($grid: JQuery) {
                if (utils.isIgGrid($grid)) return $grid;
                let gridId = $grid.attr("id");
                if (util.isNullOrUndefined(gridId)) return; 
                let endIdx = gridId.indexOf("_fixed"); 
                if (endIdx !== -1) {
                    let referGrid = $("#" + gridId.substring(0, endIdx)); 
                    if (!util.isNullOrUndefined(referGrid) && utils.fixable(referGrid))
                        return referGrid; 
                }
            }
        }
        
        module sheet {
            let normalStyles = { backgroundColor: '', color: '' };
            let selectedStyles = { backgroundColor: '#00B050', color: '#fff' };
            
            export class Configurator {
                currentSheet: string;
                currentPosition: string;
                blockId: any;
                displayScrollTop: any;
                sheets: any;
                sheetColumns: any;
                columnsInSheetImme: any;
                columnsInSheet: any;
                
                constructor(currentSheet: string, sheets: any) {
                    this.currentSheet = currentSheet;
                    this.sheets = sheets;
                    this.columnsInSheetImme = {};
                    this.columnsInSheet = {};
                }
                
                /**
                 * Load
                 */
                static load($grid: JQuery, sheetFeature: any) {
                    let sheetConfig: any = $grid.data(internal.SHEETS);
                    if (util.isNullOrUndefined(sheetConfig)) {
                        let config = new Configurator(sheetFeature.initialDisplay, sheetFeature.sheets);
                        $grid.data(internal.SHEETS, config);
                    }
                }
            }
            
            export function onScroll($grid: JQuery) {
                let $scrollContainer = $("#" + $grid.attr("id") + "_scrollContainer");
                let $displayContainer = $("#" + $grid.attr("id") + "_displayContainer");
                if ($scrollContainer.length === 0 || $displayContainer.length === 0) return;
                
                let scrollListener = function(evt: any) {
                    let sheetConfig: any = $grid.data(internal.SHEETS);
                    if (util.isNullOrUndefined(sheetConfig)) return;
                    sheetConfig.currentPosition = $scrollContainer.scrollTop() + "px";
                    sheetConfig.displayScrollTop = $displayContainer.scrollTop();
                    sheetConfig.blockId = $grid.find("tbody tr:first").data("id");
                }
                $scrollContainer.on(events.Handler.SCROLL, scrollListener);  
            }
            
            /**
             * Unused
             */
            export function setup($grid: JQuery, options: any) {
                let sheetFeature = feature.find(options.ntsFeatures, feature.SHEET);
                if (util.isNullOrUndefined(sheetFeature)) return;
                let hidingFeature: any = { name: 'Hiding' };
                if (feature.isEnable(options.features, feature.HIDING)) {
                    feature.replaceBy(options, feature.HIDING, hidingFeature);
                } else {
                    options.features.push(hidingFeature);
                }
                
                Configurator.load($grid, sheetFeature);
                configButtons($grid, sheetFeature.sheets);
            }
            
            /**
             * Unused
             */
            function configButtons($grid: JQuery, sheets: any) {
                let gridWrapper = $("<div class='nts-grid-wrapper'/>");
                $grid.wrap($("<div class='nts-grid-container'/>").css("visibility", "hidden").wrap(gridWrapper));
                let gridContainer = $grid.closest(".nts-grid-container");
                let sheetButtonsWrapper = $("<div class='nts-grid-sheet-buttons'/>").appendTo(gridContainer);
                
                let sheetMng: any = $grid.data(internal.SHEETS);
                _.forEach(sheets, function(sheet: any) {
                    let btn = $("<button/>").addClass(sheet.name).text(sheet.text).appendTo(sheetButtonsWrapper);
                    if (sheetMng.currentSheet === sheet.name) btn.css(selectedStyles);
                    btn.on("click", function(evt: any) {
                        if (!utils.hidable($grid) || utils.isErrorStatus($grid)) return;
                        updateCurrentSheet($grid, sheet.name);
                        utils.showColumns($grid, sheet.columns);
                        hideOthers($grid);
                        
                        // Styles
                        sheetButtonsWrapper.find("button").css(normalStyles);
                        $(this).css(selectedStyles);
                    });
                });
            }
            
            export function hideOthers($grid: JQuery) {
                let sheetMng: any = $grid.data(internal.SHEETS);
                if (util.isNullOrUndefined(sheetMng)) return;
                let displayColumns;
                _.forEach(sheetMng.sheets, function(sheet: any) {
                    if (sheet.name !== sheetMng.currentSheet) {
                        utils.hideColumns($grid, sheet.columns);
                    } else {
                        displayColumns = sheet.columns;
                    }
                });
                
                // Resize displaying columns
                setTimeout(function() {
                    _.forEach(displayColumns, function(column: any) {
                         columnSize.loadOne($grid, column);
                    });
                }, 0);                
            }
            
            /**
             * Update current sheet
             */
            function updateCurrentSheet($grid: JQuery, name: string) {
                let sheetMng: any = $grid.data(internal.SHEETS);
                if (util.isNullOrUndefined(sheetMng)) return;
                sheetMng.currentSheet = name;
                $grid.data(internal.SHEETS, sheetMng);
            }
            
            export module load {
                
                /**
                 * Setup
                 */
                export function setup($grid: JQuery, options: any) {
                    let sheetFeature = feature.find(options.ntsFeatures, feature.SHEET);
                    if (util.isNullOrUndefined(sheetFeature)) {
                        let idxes = {};
                        utils.analyzeColumns(options.columns)
                            .filter(function (c) { return c.hidden !== true; })
                            .forEach(function(c, i) {
                                idxes[c.key] = i;
                            });
                        let setting = $grid.data(internal.SETTINGS);
                        if (!setting.descriptor) {
                            setting.descriptor = new settings.Descriptor();
                        } 
                        setting.descriptor.colIdxes = idxes;
                        if (util.isNullOrUndefined($grid.data(internal.GRID_OPTIONS))) { 
                            $grid.data(internal.GRID_OPTIONS, _.cloneDeep(options));
                        }
                        return;
                    }
                    Configurator.load($grid, sheetFeature);
                    configButtons($grid, sheetFeature.sheets);
                    if (!util.isNullOrUndefined($grid.data(internal.GRID_OPTIONS))) return; 
                    $grid.data(internal.GRID_OPTIONS, _.cloneDeep(options));
                    // Initial sheet
                    let sheetMng: any = $grid.data(internal.SHEETS);
                    let sheet: any = _.filter(sheetMng.sheets, function(sheet: any) {
                        return sheet.name === sheetMng.currentSheet;
                    });
                    
                    let columns;
                    if (!sheetMng.sheetColumns) {
                        sheetMng.sheetColumns = {};
                    }
                    columns = sheetMng.sheetColumns[sheet[0].name];
                    if (!columns) {
                        columns = getSheetColumns(options.columns, sheet[0], options.features, sheetMng);
                        sheetMng.sheetColumns[sheet[0].name] = columns.all;
                        let idxes = {};
                        utils.analyzeColumns(columns.unfixed)
                            .filter(function (c) { return c.hidden !== true; })
                            .forEach(function(c, i) {
                                idxes[c.key] = i;
                            });
                        let setting = $grid.data(internal.SETTINGS);
                        if (!setting.descriptor) {
                            setting.descriptor = new settings.Descriptor();
                            setting.descriptor.fixedColumns = columns.fixed;
                        }
                        setting.descriptor.colIdxes = idxes;
                        options.columns = columns.all; 
                    } else options.columns = columns;
                }
                
                /**
                 * Config buttons
                 */
                function configButtons($grid: JQuery, sheets: any) {
                    if ($grid.closest(".nts-grid-container").length > 0) return;
                    $grid.closest(".nts-grid-wrapper").wrap($("<div class='nts-grid-container'/>"));
                    let gridContainer = $grid.closest(".nts-grid-container");
                    let sheetButtonsWrapper = $("<div class='nts-grid-sheet-buttons'/>").appendTo(gridContainer);
                    
                    let sheetMng: any = $grid.data(internal.SHEETS);
                    _.forEach(sheets, function(sheet: any) {
                        let btn = $("<button/>").addClass(sheet.name).text(sheet.text).appendTo(sheetButtonsWrapper);
                        if (sheetMng.currentSheet === sheet.name) btn.css(selectedStyles);
                        btn.on("click", function(evt: any) {
                            if (utils.isErrorStatus($grid)) return;
                            updateCurrentSheet($grid, sheet.name);
                            let options = $grid.data(internal.GRID_OPTIONS);
                            let columns, clonedColumns;
                            if (!sheetMng.sheetColumns) {
                                sheetMng.sheetColumns = {};
                            }
                            
                            let settings = $grid.data(internal.SETTINGS);
                            columns = sheetMng.sheetColumns[sheet.name];
                            if (!columns) {
                                columns = getSheetColumns(options.columns, sheet, options.features, sheetMng);
                                sheetMng.sheetColumns[sheet.name] = columns.all;
                                let idxes = {};
                                utils.analyzeColumns(columns.unfixed)
                                    .filter(function (c) { return c.hidden !== true; })
                                    .forEach(function(c, i) {
                                        idxes[c.key] = i;
                                    });
                                settings.descriptor.colIdxes = idxes; 
                                clonedColumns = columns.all;
                            } else {
                                let idxes = {};
                                let fixedColumns = settings.descriptor.fixedColumns;
                                if (fixedColumns) {
                                    let unfixed = columns.slice(fixedColumns.length);
                                    utils.analyzeColumns(unfixed)
                                        .filter(function (c) { return c.hidden !== true; })
                                        .forEach(function(c, i) {
                                            idxes[c.key] = i;
                                        });
                                    settings.descriptor.colIdxes = idxes;
                                }
                                clonedColumns = columns;
                            }
                            let clonedOpts = _.cloneDeep(options);
                            clonedOpts.columns = clonedColumns;
                            clonedOpts.dataSource = $grid.igGrid("option", "dataSource");
                            $grid.igGrid("destroy");
                            $grid.off();
                            
                            let pagingFt = feature.find(clonedOpts.features, feature.PAGING);
                            if (pagingFt && settings) {
                                if (!util.isNullOrUndefined(settings.pageIndex)) {
                                    pagingFt.currentPageIndex = settings.pageIndex;
                                }
                                if (!util.isNullOrUndefined(settings.pageSize)) {
                                    pagingFt.pageSize = settings.pageSize;
                                }
                                feature.replaceBy(clonedOpts, feature.PAGING, pagingFt); 
                            }
                            $grid.ntsGrid(clonedOpts);
                            
                            // Styles
                            sheetButtonsWrapper.find("button").css(normalStyles);
                            $(this).css(selectedStyles);
                        });
                    });
                }
                
                /**
                 * Get sheet columns
                 */
                function getSheetColumns(allColumns: any, displaySheet: any, features: any, sheetMng: any) {
                    let fixedColumns = [];
                    let columns = [];
                    _.forEach(allColumns, function(column: any) {
                        let index;
                        if (column.group !== undefined && _.find(displaySheet.columns, function(col, i) {
                                if (col === column.group[0].key) {
                                    index = i;
                                    return true;
                                }
                            }) !== undefined) {
                            columns[index] = column;
                            
                            column.group.forEach(function(sc) {
                                if (!sheetMng.columnsInSheetImme[sc.key]) {
                                    let mSet = new Set();
                                    mSet.add(displaySheet.name);
                                    sheetMng.columnsInSheetImme[sc.key] = mSet;
                                } else {
                                    sheetMng.columnsInSheetImme[sc.key].add(displaySheet.name);
                                }
                            });
                            return;
                        }
                        
                        let belongToSheet = _.find(displaySheet.columns, function(col, i) {
                            if (col === column.key) {
                                index = i;
                                return true;
                            }
                        }) !== undefined;
                        if (belongToSheet) {
                            columns[index] = column;
                            if (!sheetMng.columnsInSheetImme[column.key]) {
                                let mSet = new Set();
                                mSet.add(displaySheet.name);
                                sheetMng.columnsInSheetImme[column.key] = mSet;
                            } else {
                                sheetMng.columnsInSheetImme[column.key].add(displaySheet.name);
                            }
                            return;
                        }
                        
                        let columnFixFeature = feature.find(features, feature.COLUMN_FIX);
                        if (!util.isNullOrUndefined(columnFixFeature)) {
                            let isFixed = _.find(columnFixFeature.columnSettings, function(s: any) {
                                return s.columnKey === column.key;
                            }) !== undefined;
                            if (isFixed) {
                                fixedColumns.push(column);
                                return;
                            }
                        }
                    });
                    
                    _.remove(columns, c => util.isNullOrUndefined(c));
                    return { fixed: fixedColumns,
                             unfixed: columns,
                             all: _.concat(fixedColumns, columns) };
                }
            }
        }
        
        module onDemand {
            
            export class Loader {
                allKeysPath: any;
                pageRecordsPath: any;
                keys: Array<any>;
                constructor(allKeysPath: any, pageRecordsPath: any) {
                    this.allKeysPath = allKeysPath;
                    this.pageRecordsPath = pageRecordsPath;
                }
            }
            
            export function hidePageSizeDD($grid: JQuery, options?: any) {
                if (options && !feature.find(options.ntsFeatures, feature.DEMAND_LOAD)) return; 
                let $gridContainer = $($grid.igGrid("container"));
                if ($gridContainer.length > 0) {
                    $gridContainer.find("div[class*='ui-iggrid-pagesizedropdowncontainer']").hide();
                }
            }
            
            /**
             * Load keys
             */
            export function loadKeys($grid: JQuery, path: any) {
                let dfd = $.Deferred();
                request.ajax(path).done(function(keys) {
                    let loader = $grid.data(internal.LOADER);
                    if (!loader.keys || loader.keys.length === 0) loader.keys = keys;
                    dfd.resolve(loader.keys);
                }).fail(function() {
                    dfd.reject();
                });
                return dfd.promise();
            }
            
            /**
             * Load data
             */
            export function loadLazy($grid: JQuery, path: any, keys: Array<any>, 
                startIndex: number, endIndex: number, dataSource: any, primaryKey: any) {
                let dfd = $.Deferred();
                request.ajax(path, keys).done(function(data) {
                    let origDs = $grid.data(internal.ORIG_DS);
                    if (!origDs) {
                        $grid.data(internal.ORIG_DS, []);
                        origDs = $grid.data(internal.ORIG_DS);
                    }
                    
                    let add = true;
                    if (origDs.length >= endIndex) {
                        add = false;
                    }
                    _.forEach(data, function(rData, index) {
                        for (let i = startIndex; i < endIndex; i++) {
                            if (dataSource[i] && dataSource[i][primaryKey] === rData[primaryKey]) {
                                rData = _.merge(rData, dataSource[i]);
                                rData.loaded = true;
                                dataSource.splice(i, 1, rData); 
                                if (add) origDs[i] = _.cloneDeep(rData);
                            }
                        }
                    });
                    dfd.resolve(dataSource);
                }).fail(function() {;
                    dfd.reject();
                });
                return dfd.promise();
            }
            
            /**
             * Initialize
             */
            export function initial($grid: JQuery, options: any) {
                if (!options) return false;
                let pagingFt = feature.find(options.features, feature.PAGING);
                if (!pagingFt) return false;
                bindPageChange($grid);
                
                let setting = $grid.data(internal.SETTINGS);
                if (util.isNullOrUndefined(setting.pageSize)) {
                    setting.pageSize = pagingFt.pageSize;
                }
                let demandLoadFt = feature.find(options.ntsFeatures, feature.DEMAND_LOAD);
                if (!demandLoadFt) return false;
                let pageSize = pagingFt.pageSize;
                let loader = $grid.data(internal.LOADER);
                if (!loader) {
                    $grid.data(internal.LOADER, new Loader(demandLoadFt.allKeysPath, demandLoadFt.pageRecordsPath));
                } else if (loader.keys) { // Switch sheet
                    pageSize = setting.pageSize;
                    return false;
                }
                
                let bindKeys = function(keys: Array<any>) {
                    let primaryKey = options.primaryKey;
                    let ds = keys.map((key, index) => {
                        let obj = {};
                        obj[primaryKey] = key;
                        obj["loaded"] = false;
                        return obj;
                    });
                    let firstRecordIndex = (pagingFt.currentPageIndex || 0) * pageSize;
                    let lastRecordIndex = firstRecordIndex + pageSize;
                    let firstPageItems = keys.slice(firstRecordIndex, lastRecordIndex);
                    loadLazy($grid, demandLoadFt.pageRecordsPath, firstPageItems, firstRecordIndex, lastRecordIndex,
                        ds, primaryKey).done(function(data) {
                        options.dataSource = options.dataSourceAdapter ? options.dataSourceAdapter(data) : data;
                        $grid.igGrid(options);
                    });  
                };
                
                if (options.recordKeys && options.recordKeys.constructor === Array) {
                    loader = $grid.data(internal.LOADER);
                    loader.keys = options.recordKeys;
                    bindKeys(options.recordKeys);
                    return true;
                }
                loadKeys($grid, demandLoadFt.allKeysPath).done(function(keys: Array<any>) {
                    bindKeys(keys);
                }).fail(function() {
                    
                });
                return true;
            }
            
            /**
             * Bind page change
             */
            function bindPageChange($grid: JQuery) {
                $grid.on(events.Handler.PAGE_INDEX_CHANGE, function(evt: any, ui: any) {
                    let newPageIndex = ui.newPageIndex;
                    let pageSize = ui.owner.pageSize();
                    let startIndex = newPageIndex * pageSize;
                    let endIndex = startIndex + pageSize;
                    let settings = $grid.data(internal.SETTINGS);
                    settings.pageChanged = true;
                    settings.pageIndex = ui.newPageIndex;
                    let loader = $grid.data(internal.LOADER);
                    if (!loader || !loader.keys) return;
                    let dataSource = $grid.igGrid("option", "dataSource");
                    let primaryKey = $grid.igGrid("option", "primaryKey");
                    let newKeys = loader.keys.slice(startIndex, endIndex);
                    for (let i = endIndex - 1; i >= startIndex; i--) {
                        if (dataSource[i] && dataSource[i].loaded) {
                            newKeys.splice(i - startIndex, 1);
                        }
                    }
                    if (newKeys.length === 0) return;
                    loadLazy($grid, loader.pageRecordsPath, newKeys, startIndex, endIndex, dataSource, primaryKey).done(function(data) {
                        let ds = settings.dataSourceAdapter ? settings.dataSourceAdapter(data) : data;
                        $grid.igGrid("option", "dataSource", ds);
                        ui.owner.pageIndex(ui.newPageIndex);
                    });
                    return false;
                });
                
                $grid.on(events.Handler.PAGE_SIZE_CHANGE, function(evt: any, ui: any) {
                    let setting = $grid.data(internal.SETTINGS);
                    setting.pageSize = ui.newPageSize;
                    setting.pageIndex = 0;
                    let loader = $grid.data(internal.LOADER);
                    if (!loader) return;
                    let currentPageIndex = 0;
                    let startIndex = currentPageIndex * ui.newPageSize;
                    let endIndex = startIndex + ui.newPageSize;
                    let newKeys = loader.keys.slice(startIndex, endIndex);
                    let dataSource = $grid.igGrid("option", "dataSource");
                    let primaryKey = $grid.igGrid("option", "primaryKey");
                    for (let i = endIndex - 1; i >= startIndex; i--) {
                        if (dataSource[i] && dataSource[i].loaded) {
                            newKeys.splice(i - startIndex, 1);
                        }
                    }
                    if (newKeys.length === 0) return;
                    loadLazy($grid, loader.pageRecordsPath, newKeys, startIndex, endIndex, dataSource, primaryKey).done(function(data) {
                        let ds = setting.dataSourceAdapter ? setting.dataSourceAdapter(data) : data;
                        $grid.igGrid("option", "dataSource", ds);
                        ui.owner.pageSize(ui.newPageSize);
                    });
                    return false;
                });
            }
        }
        
        module settings {
            
            export let USER_M: string = "M";
            export let USER_O: string = "O";
            
            export class Descriptor {
                startRow: number;
                rowCount: number;
                elements: Array<any>;
                keyIdxes: any;
                colIdxes: any;
                fixedColumns: Array<any>;
                fixedTable: JQuery;
                
                constructor(startRow?: number, rowCount?: number, elements?: Array<any>, keyIdxes?: any) {
                    this.startRow = startRow;
                    this.rowCount = rowCount;
                    this.elements = elements;
                    this.keyIdxes = keyIdxes;
                }
                
                update(startRow: number, rowCount: number, elements: Array<any>) {
                    this.startRow = startRow;
                    this.rowCount = rowCount;
                    this.elements = elements;
                }
                
                isFixedColumn(column) {
                    let index;
                    _.forEach(this.fixedColumns, function(c, i) {
                        if (c.key === column) {
                            index = i;
                            return false;
                        }
                    });
                    return index;
                }
            }
            
            /**
             * Build settings 
             */
            export function build($grid: JQuery, options: any) {
                let data: any = {};
                let rebuild;
                data.preventEditInError = options.preventEditInError;
                data.dataSourceAdapter = options.dataSourceAdapter;
                data.errorColumns = options.errorColumns;
                data.errorsOnPage = options.showErrorsOnPage;
                if (!$grid.data(internal.SETTINGS)) {
                    $grid.data(internal.SETTINGS, data);
                } else {
                    rebuild = true;
                }
                
                $grid.on(events.Handler.RECORDS, function(evt, arg) {
                    if (util.isNullOrUndefined(arg.owner._startRowIndex)) {
                        arg.owner._startRowIndex = 0;
                    }
                    let setting = $grid.data(internal.SETTINGS);
                    let owner = arg.owner;
                    let pageIndex = 0, pageSize = 0;
                    if (!util.isNullOrUndefined(setting.pageIndex)) {
                        pageIndex = setting.pageIndex;
                    }
                    if (!util.isNullOrUndefined(setting.pageSize)) {
                        pageSize = setting.pageSize;
                    }
                    let startRow = owner._startRowIndex + pageIndex * pageSize;
                    if (setting.pageChanged) { 
                        startRow = pageIndex * pageSize;
                        setTimeout(function() {
                            setting.pageChanged = false;
                        }, 0);
                    }
                    if (!setting.descriptor) {
                        let pk = owner.dataSource.settings.primaryKey;
                        let keyIdxes = {};
                        owner.dataSource._origDs.forEach(function(d, i) {
                            keyIdxes[d[pk]] = i; 
                        });
                        let descriptor = new Descriptor(startRow, owner._virtualRowCount, owner._virtualDom, keyIdxes);
                        setting.descriptor = descriptor;
                        setting.descriptor.fixedColumns = owner._fixedColumns;
                        setting.descriptor.fixedTable = owner._fixedTable;
                        setting.descriptor.headerCells = owner._headerCells;
                        setting.descriptor.headerParent = owner._headerParent;
                        return;
                    }
                    setting.descriptor.update(startRow, owner._virtualRowCount, owner._virtualDom); 
                    if (!setting.descriptor.keyIdxes || $grid.data("ntsRowDeleting")) {
                        let pk = owner.dataSource.settings.primaryKey;
                        let keyIdxes = {};
                        if (owner.dataSource._origDs) {
                            owner.dataSource._origDs.forEach(function(d, i) {
                                keyIdxes[d[pk]] = i; 
                            });
                        }
                        setting.descriptor.keyIdxes = keyIdxes;
                        setting.descriptor.fixedTable = owner._fixedTable;
                        setting.descriptor.headerCells = owner._headerCells;
                        setting.descriptor.headerParent = owner._headerParent;
                        $grid.data("ntsRowDeleting", false);
                    }
                    if (rebuild) {
                        setting.descriptor.fixedTable = owner._fixedTable;
                        setting.descriptor.headerCells = owner._headerCells;
                        setting.descriptor.headerParent = owner._headerParent;
                    }
                });
            }
            
            /**
             * Set grid size
             */
            export function setGridSize($grid: JQuery) {
                var height = window.innerHeight;
                var width = window.innerWidth;
                $grid.igGrid("option", "width", width - 240);
                $grid.igGrid("option", "height", height - 90);
            }
        }
        
        module internal {
            export let ORIG_DS = "ntsOrigDs";
            export let CONTROL_TYPES = "ntsControlTypesGroup";
            export let COMBO_SELECTED = "ntsComboSelection";
            export let CB_SELECTED = "ntsCheckboxSelection";
            export let UPDATED_CELLS = "ntsUpdatedCells";
            export let TARGET_EDITS = "ntsTargetEdits";
            export let OTHER_EDITS = "ntsOtherEdits"; 
            export let CELL_FORMATTER = "ntsCellFormatter";
            // Full columns options
            export let GRID_OPTIONS = "ntsGridOptions";
            export let SELECTED_CELL = "ntsSelectedCell";
            export let SHEETS: string = "ntsGridSheets";
            export let SPECIAL_COL_TYPES = "ntsSpecialColumnTypes";
            export let ENTER_DIRECT = "enter";
            export let SETTINGS = "ntsSettings";
            export let ERRORS = "ntsErrors";
            export let ERRORS_LOG = "ntsErrorsLog";
            export let LOADER = "ntsLoader";
            export let TXT_RAW = "rawText";
            export let CELL_ORIG_VAL = "_origValue";
            
            /**
             * Get cell by id.
             */
            export function getCellById($grid: JQuery, rowId: any, key: any) {
                let settings = $grid.data(SETTINGS);
                if (!settings || !settings.descriptor) return;
                let descriptor = settings.descriptor;
                if (!descriptor.keyIdxes || !descriptor.colIdxes) return;
                
                let idx = descriptor.keyIdxes[rowId];
                let colIdx = descriptor.colIdxes[key];
                if (util.isNullOrUndefined(colIdx)) {
                    let colIdx = descriptor.isFixedColumn(key);
                    if (!util.isNullOrUndefined(colIdx)) {
                        return descriptor.fixedTable.find("tr:eq(" + (idx - descriptor.startRow) + ") td:eq(" + colIdx + ")");    
                    }
                }
                if (!util.isNullOrUndefined(idx) && idx >= descriptor.startRow 
                    && idx <= descriptor.rowCount + descriptor.startRow - 1 && !util.isNullOrUndefined(colIdx)) {
                    return $(descriptor.elements[idx - descriptor.startRow][colIdx]);
                }
                
                return $grid.igGrid("cellById", rowId, key);
            }
        }
        
        module utils {
            
            export function isArrowKey(evt: any) {
                return evt.keyCode >= 37 && evt.keyCode <= 40;
            }
            export function isArrowLeft(evt: any) {
                return evt.keyCode === 37;
            }
            export function isArrowRight(evt: any) {
                return evt.keyCode === 39;
            }
            export function isAlphaNumeric(evt: any) {
                return (evt.keyCode >= 48 && evt.keyCode <= 90) 
                        || (evt.keyCode >= 96 && evt.keyCode <= 105);
            }
            export function isMinusSymbol(evt: any) {
                return evt.keyCode === 189 || evt.keyCode === 109;
            }
            export function isTabKey(evt: any) {
                return evt.keyCode === 9;
            }
            export function isEnterKey(evt: any) {
                return evt.keyCode === 13;
            }
            export function isSpaceKey(evt: any) {
                return evt.keyCode === 32;
            }
            export function isDeleteKey(evt: any) {
                return evt.keyCode === 46;
            }
            export function isPasteKey(evt: any) {
                return evt.keyCode === 86;
            }
            export function isCopyKey(evt: any) {
                return evt.keyCode === 67;
            }
            export function isCutKey(evt: any) {
                return evt.keyCode === 88;
            }
            
            /**
             * Is error
             */
            export function isErrorStatus($grid: JQuery) {
                let cell = selection.getSelectedCell($grid);
                return isEditMode($grid) && errors.any(cell);
            }
            
            /**
             * Only used in edit mode
             */
            export function isNotErrorCell($grid: JQuery, evt: any) {
                let cell = selection.getSelectedCell($grid);
                let $target = $(evt.target);
                let td = $target;
                if (!$target.prev().is("td")) td = $target.closest("td"); 
                return isEditMode($grid) && td.length > 0 && td[0] !== cell.element[0]
                        && errors.any(cell);
            }
            
            /**
             * Is edit mode
             */
            export function isEditMode($grid: JQuery) {
                return (updatable($grid) && $grid.igGridUpdating("isEditing"));
            }
            
            export function isIgGrid($grid: JQuery) {
                return $grid && !util.isNullOrUndefined($grid.data("igGrid"));
            }
            export function selectable($grid: JQuery) {
                return $grid && !util.isNullOrUndefined($grid.data("igGridSelection"));
            }
            export function updatable($grid: JQuery) {
                return $grid && !util.isNullOrUndefined($grid.data("igGridUpdating"));
            }
            export function fixable($grid: JQuery) {
                return $grid && !util.isNullOrUndefined($grid.data("igGridColumnFixing"));
            }
            export function hidable($grid: JQuery) {
                return $grid && !util.isNullOrUndefined($grid.data("igGridHiding"));
            }
            export function pageable($grid: JQuery) {
                return $grid && !util.isNullOrUndefined($grid.data("igGridPaging"));
            }
            export function disabled($cell: JQuery) {
                return $cell.hasClass(color.Disable);
            }
            
            /**
             * Data type of primary key
             */
            export function dataTypeOfPrimaryKey($grid: JQuery, columnsMap: any) : string {
                if (util.isNullOrUndefined(columnsMap)) return;
                let columns = columnsMap["undefined"];
                if (Object.keys(columnsMap).length > 1) {
                    columns = _.concat(columnsMap["true"], columnsMap["undefined"]);
                }
                let primaryKey = $grid.igGrid("option", "primaryKey");
                let keyColumn: Array<any> =  _.filter(columns, function(column: any) {
                    return column.key === primaryKey;
                });
                if (!util.isNullOrUndefined(keyColumn) && keyColumn.length > 0) return keyColumn[0].dataType;
                return;
            }
            
            /**
             * Parse number
             */
            export function parseIntIfNumber(value: any, $grid: JQuery, columnsMap: any) {
                if (dataTypeOfPrimaryKey($grid, columnsMap) === "number") {
                    return parseInt(value);
                }
                return value;
            }
            
            export function isCopiableControls($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                switch (columnControlTypes[columnKey]) {
                    case ntsControls.LINK_LABEL:
                    case ntsControls.TEXTBOX:
                    case ntsControls.LABEL:
                        return true;
                }
                return false;
            }
            
            export function isCuttableControls($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                switch (columnControlTypes[columnKey]) {
                    case ntsControls.TEXTBOX:
                        return true;
                }
                return false;
            }
            
            export function isPastableControls($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                switch (columnControlTypes[columnKey]) {
                    case ntsControls.LABEL:
                    case ntsControls.CHECKBOX:
                    case ntsControls.LINK_LABEL:
                    case ntsControls.COMBOBOX:
                    case ntsControls.FLEX_IMAGE:
                    case ntsControls.IMAGE:
                        return false;
                }
                return true;
            }
            
            export function isDisabled($cell: JQuery) {
                return $cell.hasClass(color.Disable);
            }
            
            export function isComboBox($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                if (columnControlTypes[columnKey] === ntsControls.COMBOBOX) return true;
                return false;
            }
            export function isNtsControl($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                switch (columnControlTypes[columnKey]) {
                    case ntsControls.LABEL:
                    case ntsControls.CHECKBOX:
                    case ntsControls.SWITCH_BUTTONS:
                    case ntsControls.COMBOBOX:
                    case ntsControls.BUTTON:
                    case ntsControls.DELETE_BUTTON:
                    case ntsControls.FLEX_IMAGE:
                    case ntsControls.IMAGE:
                    case ntsControls.TEXT_EDITOR:
                        return true;
                }
                return false;
            }
            
            /**
             * Get control type
             */
            export function getControlType($grid: JQuery, columnKey: string) {
                let columnControlTypes = $grid.data(internal.CONTROL_TYPES);
                if (util.isNullOrUndefined(columnControlTypes)) return;
                return columnControlTypes[columnKey];
            }
            
            export function comboBoxOfCell(cell: any) {
                return $(cell.element).find(".nts-combo-container");
            }
            
            export function getColumns($grid: JQuery) {
                if (isIgGrid($grid)) {
                    return $grid.igGrid("option", "columns");
                }
                let referGrid = fixedColumns.realGridOf($grid);
                if (!util.isNullOrUndefined(referGrid)) return referGrid.igGrid("option", "columns");
            }
            export function getColumnsMap($grid: JQuery) {
                let columns = getColumns($grid);
                return _.groupBy(columns, "fixed");
            }
            export function getVisibleColumns($grid: JQuery) {
                return _.filter(getColumns($grid), function(column: any) {
                    return column.hidden !== true;
                }); 
            }
            
            export function getVisibleColumnsMap($grid: JQuery) {
                let visibleColumns = getVisibleColumns($grid);
                return _.groupBy(visibleColumns, "fixed");
            }
            export function getVisibleFixedColumns($grid: JQuery) {
                return _.filter(getColumns($grid), function(column: any) {
                    return column.hidden !== true && column.fixed === true;
                });
            }
            
            export function isFixedColumn(columnKey: any, visibleColumnsMap: any) {
                return _.find(visibleColumnsMap["true"], function(column: any) {
                    return column.key === columnKey;
                }) !== undefined;
            }
            export function isFixedColumnCell(cell: any, visibleColumnsMap: any) {
                return _.find(visibleColumnsMap["true"], function(column: any) {
                    return column.key === cell.columnKey;
                }) !== undefined;
            }
            export function columnsGroupOfColumn(column: any, visibleColumnsMap: any) {
                return visibleColumnsMap[column.fixed ? "true" : "undefined" ];
            }
            export function columnsGroupOfCell(cell: any, visibleColumnsMap: any) {
                if (isFixedColumnCell(cell, visibleColumnsMap)) return visibleColumnsMap["true"];
                return visibleColumnsMap["undefined"];
            }
            export function visibleColumnsFromMap(visibleColumnsMap: any) {
                return _.concat(visibleColumnsMap["true"], visibleColumnsMap["undefined"]);
            }
            export function noOfVisibleColumns(visibleColumnsMap: any) {
                return visibleColumnsMap["true"].length + visibleColumnsMap["undefined"].length;
            }
            export function getFixedColumns(visibleColumnsMap: any) {
                return visibleColumnsMap["true"];
            }
            export function getUnfixedColumns(visibleColumnsMap: any) {
                return visibleColumnsMap["undefined"];
            }
            
            /**
             * Next column
             */
            export function nextColumn(visibleColumnsMap: any, columnIndex: number, isFixed: boolean) {
                if (util.isNullOrUndefined(visibleColumnsMap)) return;
                let nextCol: any = {};
                let mapKeyName = isFixed ? "true" : "undefined";
                let reverseKeyName = isFixed ? "undefined" : "true";
                if (columnIndex < visibleColumnsMap[mapKeyName].length - 1) {
                    return {
                                options: visibleColumnsMap[mapKeyName][columnIndex + 1],
                                index: columnIndex + 1
                           };
                } else if (columnIndex === visibleColumnsMap[mapKeyName].length - 1) {
                    return {
                                options: visibleColumnsMap[reverseKeyName][0],
                                index: 0
                           };
                }
            }
            
            /**
             * Next column by key
             */
            export function nextColumnByKey(visibleColumnsMap: any, columnKey: string, isFixed: boolean) {
                if (util.isNullOrUndefined(visibleColumnsMap)) return;
                let currentColumnIndex;
                let currentColumn;
                let fixedColumns = visibleColumnsMap["true"];
                let unfixedColumns = visibleColumnsMap["undefined"];
                
                if (isFixed && fixedColumns.length > 0) {
                    _.forEach(fixedColumns, function(col: any, index: number) {
                        if (col.key === columnKey) {
                            currentColumnIndex = index;
                            currentColumn = col;
                            return false;
                        }
                    });
                    if (util.isNullOrUndefined(currentColumn) || util.isNullOrUndefined(currentColumnIndex)) return;
                    if (currentColumnIndex === fixedColumns.length - 1) {
                        return {
                            options: unfixedColumns[0],
                            index: 0
                        };
                    }
                    return {
                        options: fixedColumns[currentColumnIndex + 1],
                        index: currentColumnIndex + 1
                    }
                } 
                
                if (!isFixed && unfixedColumns.length > 0) {
                    _.forEach(unfixedColumns, function(col: any, index: number) {
                        if (col.key === columnKey) {
                            currentColumnIndex = index;
                            currentColumn = col;
                            return false;
                        }
                    });
                    if (util.isNullOrUndefined(currentColumn) || util.isNullOrUndefined(currentColumnIndex)) return;
                    if (currentColumnIndex === unfixedColumns.length - 1) {  
                        return {
                            options: fixedColumns.length > 0 ? fixedColumns[0] : unfixedColumns[0],
                            index: 0  
                        };
                    }
                    return {
                        options: unfixedColumns[currentColumnIndex + 1],
                        index: currentColumnIndex + 1
                    }
                }
            }
            
            export function rowAt(cell: any) {
                if (util.isNullOrUndefined(cell)) return;
                return $(cell.element).closest("tr");
            }
            export function nextNRow(cell: any, noOfNext: number) {
                return $(cell.element).closest("tr").nextAll("tr:eq(" + (noOfNext - 1) + ")");
            }
            export function getDisplayColumnIndex($grid: JQuery, cell: any) {
                let columns = $grid.igGrid("option", "columns");
                for (let i = 0; i < columns.length; i++) {
                    if (columns[i].key === cell.columnKey)
                        return i;
                }
                return -1;
            }
            export function getDisplayContainer($grid: JQuery) {
                return $("#" + $grid.attr("id") + "_displayContainer");
            }
            export function getScrollContainer($grid: JQuery) {
                return $("#" + $grid.attr("id") + "_scrollContainer");
            }
            
            export function startEdit($grid: JQuery, cell: any) {
                let visibleColumns = getVisibleColumns($grid);
                for (let i = 0; i < visibleColumns.length; i++) {
                    if (visibleColumns[i].key === cell.columnKey) {
                        $grid.igGridUpdating("startEdit", cell.id, i);
                        break;
                    }
                }
            }
            
            export function hideColumns($grid: JQuery, columns: Array<any>) {
                $grid.igGridHiding("hideMultiColumns", columns);
            }
            export function showColumns($grid: JQuery, columns: Array<any>) {
                $grid.igGridHiding("showMultiColumns", columns);
            }
            
            export function disableNtsControl($grid: JQuery, cell: any, controlType: string) {
                var control = ntsControls.getControl(controlType);
                if (util.isNullOrUndefined(control)) return;
                control.disable(cell.$element);
                if (!cell.$element.hasClass(color.Disable)) cell.$element.addClass(color.Disable);
                color.pushDisable($grid, { id: cell.id, columnKey: cell.columnKey });
            }
            
            export function analyzeColumns(columns: any) {
                let flatCols = [];
                flatColumns(columns, flatCols);
                return flatCols;
            }
            
            function flatColumns(columns: any, flatCols: Array<any>) {
                _.forEach(columns, function(column) {
                    if (util.isNullOrUndefined(column.group)) {
                        flatCols.push(column);
                        return;
                    }
                    flatColumns(column.group, flatCols);
                });
            }
            
            export function setChildrenTabIndex($grid: JQuery, index: number) {
                let container = $grid.igGrid("container");
                $(container).attr("tabindex", 0);
                $(container).find("tr, th, td").attr("tabindex", index);
            }
            
            export function outsideGrid($grid: JQuery, target) {
                return !$grid.is(target) && $grid.has(target).length === 0;
            }
            
        }
    }
}