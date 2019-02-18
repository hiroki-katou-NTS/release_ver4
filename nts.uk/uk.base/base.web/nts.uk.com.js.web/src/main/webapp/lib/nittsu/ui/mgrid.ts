module nts.uk.ui.mgrid {
    
    const MGRID = "mgrid";
    const FIXED = "mgrid-fixed";
    const FREE = "mgrid-free";
    const HEADER = "mgrid-header";
    const BODY = "mgrid-body";
    const Default = "default";
    const SheetDef = "_sheetDef";
    const DISTANCE = 1;
    const BODY_ROW_HEIGHT = 29;
    const SUM_HEIGHT = 27;
    let defaultOptions = { columns: [], features: [] };
    let _scrollWidth, _maxFixedWidth = 0, _maxFreeWidth, _columnsMap = {}, _dataSource, _secColumn = {},
        _hasFixed, _validators = {}, _mDesc, _mEditor, _cloud, _hr, _direction, _errors = [], 
        _errorColumns, _errorsOnPage, _$grid, _pk, _pkType, _summaries, _objId, _getObjId, _hasSum, _pageSize, _currentPage, _currentSheet, _start, _end, 
        _headerHeight, _zeroHidden, _paging = false, _sheeting = false, _copie = false, _mafollicle = {}, _vessel = () => _mafollicle[_currentPage][_currentSheet], 
        _cstifle = () => _mafollicle[SheetDef][_currentSheet].columns, _specialColumn = {}, _specialLinkColumn = {}, _histoire = [], _flexFitWidth,
        _copieer, _collerer, _fixedHiddenColumns = [], _hiddenColumns = [], _fixedColumns, _selected = {}, _dirties = {}, _headerWrappers, _bodyWrappers, _sumWrappers,
        _fixedControlMap = {}, _cellStates, _features, _leftAlign, _header, _rid = {}, _remainWidth = 240, _remainHeight = 190,
        _prtDiv = document.createElement("div"), _prtCell = document.createElement("td");
    
    export class MGrid {
        
        $container: HTMLElement;
        headers: Array<any>;
        bodies: Array<any>;
        fixedHeader: any = { containerClass: FIXED };
        fixedBody: any = { containerClass: FIXED };
        header: any = { containerClass: FREE };
        body: any = { containerClass: FREE };
        fixedSummaries: any = { containerClass: FIXED + "-summaries" };
        summaries: any = { containerClass: FREE + "-summaries" }; 
        
        constructor($container: HTMLElement, options: any) {
            let self = this;
            self.$container = $container;
            _$grid = $($container);
            _pk = options.primaryKey;
            _pkType = options.primaryKeyDataType;
            _features = options.features;
            _objId = options.userId;
            _getObjId = options.getUserId;
            _errorColumns = options.errorColumns;
            _errorsOnPage = options.errorsOnPage;
            _headerHeight = options.headerHeight;
            _zeroHidden = options.hideZero;
            _.assignIn(self, _.cloneDeep(options));  
            self.makeDefault();  
        }
        
        /**
         * MakeDefault.
         */
        makeDefault() {
            let self = this;
            self.$container.tabIndex = -1;
            self.fixedHeader = _.assignIn(self.fixedHeader, _.cloneDeep(defaultOptions), { ntsControls: self.ntsControls });
            self.fixedBody = _.assignIn(self.fixedBody, _.cloneDeep(defaultOptions));
            self.header = _.assignIn(self.header, _.cloneDeep(defaultOptions), { ntsControls: self.ntsControls });
            self.body = _.assignIn(self.body, _.cloneDeep(defaultOptions));
            self.compreOptions();
            if (self.enter) {
                _$grid.data("enterDirect", self.enter);
            }
            if (!_.isNil(self.subWidth)) {
                _remainWidth = parseFloat(self.subWidth);
            }
            if (!_.isNil(self.subHeight)) {
                _remainHeight = parseFloat(self.subHeight);
            }
            _$grid.mGrid({});
        }
        
        /**
         * CompreOptions.
         */
        compreOptions() {
            let self = this;
            if (self.features) {
                let columnFixFt = tn.find(self.features, tn.COLUMN_FIX);
                let colParts;
                
                let pageFt = tn.find(self.features, tn.PAGING);
                if (pageFt) {
                    _paging = true;
                    _pageSize = pageFt.pageSize;
                    _currentPage = pageFt.currentPageIndex;
                    let remainder, pageSources, size = self.dataSource.length; 
                    let noPage = Math.floor(size / _pageSize);
                    for (let i = 0; i < noPage; i++) {
                        let s = i * _pageSize;
                        let src = _.slice(self.dataSource, s, s + _pageSize);
                        _mafollicle[i] = { dataSource: src, origDs: _.cloneDeep(src) };
                    }
                    
                    if ((remainder = size % _pageSize) !== 0) {
                        let s = _pageSize * noPage;
                        let src = _.slice(self.dataSource, s, s + _pageSize);
                        _mafollicle[noPage] = { dataSource: src, origDs: _.cloneDeep(src) };
                    }
                    
                    if (_.keys(_mafollicle).length === 0) {
                        _mafollicle[0] = { dataSource: [], origDs: [] };
                    }
                } else {
                    _currentPage = Default;
                    _mafollicle[_currentPage] = { dataSource: self.dataSource, origDs: _.cloneDeep(self.dataSource) };
                }
                
                let sheetFt = tn.find(self.features, tn.SHEET);
                let headerStyles, savingFt = tn.find(self.features, tn.WIDTH_SAVE);
                let sheetDef = {};
                if (sheetFt) {
                    _sheeting = true;
                    _currentSheet = sheetFt.initialDisplay;
                    _.forEach(sheetFt.sheets, s => {
                        let sheetCols = [];
                        _.forEach(s.columns, c => {
                            let sc = _.find(self.columns, col => {
                                if (col.group) {
                                    _.forEach(col.group, gc => {
                                        if (_.isNil(_secColumn[gc.key])) {
                                            _secColumn[gc.key] = gc;
                                        }
                                    });
                                    return col.group[0].key === c;
                                } else return col.key === c;
                            });
                            
                            if (sc && !sc.group && !_secColumn[sc.key]) {
                                _secColumn[sc.key] = sc;
                            }
                            if (sc) sheetCols.push(sc);
                        });
                        
                        sheetDef[s.name] = { columns: sheetCols, text: s.text };
                    });
                } else {
                    _currentSheet = Default;
                    sheetDef[Default] = { columns: self.columns, text: "Sheet" };
                }
                
                _mafollicle[SheetDef] = sheetDef;
                
                if (columnFixFt && columnFixFt.columnSettings) {
                    let fixedColumns = _.filter(columnFixFt.columnSettings, c => c.isFixed);
                    if (_sheeting) {
                        let fixedCols = _.filter(self.columns, c => {
                            return _.some(fixedColumns, f => f.columnKey === c.key);
                        });
                        
                        colParts = [ fixedCols, _cstifle() ];
                    } else {
                        colParts = _.partition(self.columns, c => { 
                            return _.some(fixedColumns, f => f.columnKey === c.key);
                        });
                    }
                    
                    _.forEach(colParts, (c, i) => kt.turfSurf(c, !i));
                    _fixedColumns = colParts[0];
                    self.fixedHeader.columns = colParts[0];
                    self.fixedHeader.height = self.headerHeight;
                    self.fixedBody.columns = colParts[0];
                    self.header.columns = colParts[1];
                    self.header.height = self.headerHeight;
                    self.body.columns = colParts[1];
                    _hasFixed = true;
                    headerStyles = tn.find(self.features, tn.HEADER_STYLE);
                    if (headerStyles) {
                        let styleParts = _.partition(headerStyles.columns, c => {
                            return _.some(fixedColumns, f => f.columnKey === c.key);
                        });
                        
                        self.fixedHeader.features.push({ name: tn.HEADER_STYLE, columns: styleParts[0] });
                        self.header.features.push({ name: tn.HEADER_STYLE, columns: styleParts[1] });
                    }
                } else {
                    self.header.columns = self.columns;
                    self.body.columns = self.columns;
                    kt.turfSurf(self.columns);
                    colParts = [ self.columns ];
                    headerStyles = tn.find(self.features, tn.HEADER_STYLE);
                    if (headerStyles) {
                        self.header.features.push({ name: tn.HEADER_STYLE, columns: headerStyles.columns });
                    }
                }
                
                let summaries = tn.find(self.features, tn.SUMMARIES);
                if (summaries) {
                    _summaries = {};
                    if (colParts.length > 1) {
                        self.fixedSummaries.columns = colParts[0];
                        self.fixedSummaries.height = SUM_HEIGHT + "px";
                        self.summaries.columns = colParts[1];
                        self.summaries.height = SUM_HEIGHT + "px";
                    } else {
                        self.summaries.columns = colParts[0];
                        self.summaries.height = SUM_HEIGHT + "px";
                    }
                    
                    _.forEach(summaries.columnSettings, s => {
                        let sum = { calculator: s.summaryCalculator, formatter: s.formatter };
                        if (s.summaryCalculator === "Time") {
                            sum[_currentPage] = moment.duration("0:00");
                        } else if (s.summaryCalculator === "Number") {
                            sum[_currentPage] = 0;
                        }
                        
                        _summaries[s.columnKey] = sum;
                    });
                }
                
                if (tn.isEnable(self.features, tn.COPY)) _copie = true;
            }
        }
        
        /**
         * Create.
         */
        create() {
            let self = this;
            let left: string = "0px";
            let top: string = "0px";
            let start = performance.now();
            self.headers = _.filter([ self.fixedHeader, self.header ], h => {
                return h && h.columns;
            });
            
            self.bodies = _.filter([ self.fixedBody, self.body ], b => {
                return b && b.columns;
            });
            
            self.$container.classList.add(MGRID);
            $.data(self.$container, MGRID, self);
            let pTable = $.data(self.$container, MGRID); 
            pTable.owner = { headers: [], bodies: [] };
            
            let scrollWidth = ti.getScrollWidth();
            let headerWrappers = [], bodyWrappers = [], sumWrappers = [], headerColGroup = [], 
                bodyColGroup = [], sumColGroup = [], $fixedHeaderTbl, painters = [], controlMap = {};
            let $frag = document.createDocumentFragment();
            let freeWrapperWidth;
            self.headers.forEach((headPart, i) => {
                if (!_.isNil(self.headers[i]) && headPart.columns.length > 0) {
                    headPart.overflow = "hidden";
                    if (headPart.containerClass === FREE) {
                        freeWrapperWidth = parseFloat(self.width) - _maxFixedWidth;
                        headPart.width = freeWrapperWidth + "px";
                    }
                    headPart.isHeader = true;
                    let $headerWrapper = v.createWrapper("0px", left, headPart);
                    pTable.owner.headers.push($headerWrapper); 
                    $headerWrapper.classList.add(HEADER);
//                    self.$container.appendChild($headerWrapper);
                    $frag.appendChild($headerWrapper);
                    let tablePart = v.process($headerWrapper, headPart);
                    let $tbl = tablePart.$table;
                    painters.push(tablePart.painter);
                    
                    if (headPart.containerClass === FIXED) {
                        left = (parseInt(left) + _maxFixedWidth + DISTANCE) + "px";
                        $fixedHeaderTbl = $tbl;
                        _fixedControlMap = tablePart.controlMap;
                    } else {
                        if ($fixedHeaderTbl) $fixedHeaderTbl.style.height = self.headerHeight;
                        $tbl.style.height = self.headerHeight;
                        top = (parseFloat(self.headerHeight) + DISTANCE) + "px";
                        _mafollicle[_currentPage][_currentSheet] = {};
                        _vessel().$hGroup = $tbl.querySelector("colgroup");
                        _vessel().$hBody = $tbl.querySelector("tbody");
                        _mafollicle[SheetDef][_currentSheet].hColArr = tablePart.cols;
                    }
                    headerWrappers.push($headerWrapper);
                    headerColGroup.push(tablePart.cols);
                    _.assignIn(controlMap, tablePart.controlMap);
                }
                
                if (i === self.headers.length - 1) {
                    _header = headPart;
                    _mafollicle[SheetDef][_currentSheet].levelStruct = headPart.levelStruct; 
                }
            });
            
            _headerWrappers = headerWrappers;
            let bodyHeight = parseFloat(self.height) - parseFloat(self.headerHeight);
            self.bodies.forEach((bodyPart, i) => {
                let $bodyWrapper: HTMLElement, alignLeft = 0;
                if (!_.isNil(bodyPart) && bodyPart.columns.length > 0) {
                    bodyPart.rowHeight = BODY_ROW_HEIGHT + "px";
                    
                    if (bodyPart.containerClass === FIXED) {
                        bodyPart.height = bodyHeight + "px";
                        bodyPart.width = _maxFixedWidth + "px"; 
                    } else {
                        bodyPart.height = bodyHeight + "px";
                        bodyPart.width = (parseFloat(self.headers[i].width) + scrollWidth) + "px";
                        alignLeft = left;
                    }
                    
                    $bodyWrapper = v.createWrapper(top, alignLeft, bodyPart);
                    pTable.owner.bodies.push($bodyWrapper);
                    bodyWrappers.push($bodyWrapper);
                    $frag.appendChild($bodyWrapper);
                    
                    if (bodyPart.containerClass === FREE && !_.isNil($bodyWrapper)) {
                        bodyPart.overflow = "scroll";
                        tc.syncDoubDirVerticalScrolls(bodyWrappers);
                        if (!self.summaries || !self.summaries.columns) {
                            tc.syncHorizontalScroll(headerWrappers[i], $bodyWrapper);
                        }
                        tc.bindVertWheel($bodyWrapper, true);
                    } else {
                        bodyPart.overflowX = "scroll";
                        tc.bindVertWheel($bodyWrapper);   
                    }
                    
                    let result = v.table($bodyWrapper, bodyPart);
                    if (bodyPart.containerClass === FREE) {
                        _vessel().$bGroup = result.$table.querySelector("colgroup");
                        _mafollicle[SheetDef][_currentSheet].bColArr = result.cols;
                    }
                    bodyColGroup.push(result.cols);
                }
            });
            
            _hasSum = !_.isNil(self.summaries.columns);
            let artifactOptions = { primaryKey: self.primaryKey, controlMap: controlMap, features: self.features, hasSum: _hasSum };
            _dataSource = _mafollicle[_currentPage].dataSource;
            _mafollicle[SheetDef][_currentSheet].controlMap = controlMap;
            _mafollicle[SheetDef][_currentSheet].painters = painters;
            _mafollicle[SheetDef][_currentSheet].maxWidth = _maxFreeWidth;
            if (!_.isNil(self.maxRows) && self.maxRows >= 31) {
                artifactOptions.noBlocRangee = self.maxRows;
                artifactOptions.noGrappeBloc = 2;
            } 
            v.construe(self.$container, bodyWrappers, artifactOptions);
            _bodyWrappers = bodyWrappers;
            let dWrapper = _hasFixed ? bodyWrappers[1] : bodyWrappers[0];
            _vessel().$bBody = dWrapper.querySelector("tbody");
            
            top = parseFloat(self.height) + DISTANCE - scrollWidth - SUM_HEIGHT;
            ti.calcTotal();
            [ self.fixedSummaries, self.summaries ].filter(s => s && s.columns).forEach((sumPart, i) => {
                if (!sumPart.columns || sumPart.columns.length === 0) return;
                let alignLeft = i === 0 ? 0 : left;
                if (sumPart.containerClass === FREE + "-summaries") {
                    sumPart.width = self.headers[i].width;
                }
                
                let $sumDiv = v.createWrapper(top + "px", alignLeft, sumPart);
                $frag.appendChild($sumDiv);
                let $tbl = document.createElement("table");
                $sumDiv.appendChild($tbl);
                let $tbody = document.createElement("tbody");
                $tbl.appendChild($tbody);
                let $colGroup = document.createElement("colgroup");
                $tbl.insertBefore($colGroup, $tbody);
                let ptr, cols = [], $tr = document.createElement("tr");
                $tr.style.height = "27px";
                $tbody.appendChild($tr);
                
                if (sumPart.containerClass === FREE + "-summaries") {
                    _.forEach(bodyColGroup[bodyColGroup.length > 1 ? 1 : 0], c => {
                        let col = c.cloneNode(true);
                        $colGroup.appendChild(col);
                        cols.push(col);
                    });
                    
                    if (painters.length > 1) ptr = painters[1];
                    else ptr = painters[0];
                    tc.syncDoubDirHorizontalScrolls([ headerWrappers[i], bodyWrappers[i], $sumDiv ]);
                    _mafollicle[SheetDef][_currentSheet].sumColArr = cols;
                } else {
                    _.forEach(bodyColGroup[0], c => {
                        let col = c.cloneNode(true);
                        $colGroup.appendChild(col);
                        cols.push(col);
                    });
                    
                    ptr = painters[0];
                }
                
                sumColGroup.push(cols);
                _.forEach(ptr.columns, c => {
                    let sum = _summaries[c.key]; 
                    let $td = _prtCell.cloneNode();
                    if (!ptr.visibleColumnsMap[c.key]) {
                        $td.style.display = "none";
                    }
                    $tr.appendChild($td);
                    
                    if (!sum) return;
                    if (sum.calculator === "Time") {
                        $td.textContent = ti.momentToString(sum[_currentPage]);
                        sum[_currentSheet] = $td;
                    } else if (sum.calculator === "Number") {
                        if (sum.formatter === "Currency") {
                            $td.textContent = ti.asCurrency(sum[_currentPage]);
                        } else $td.textContent = sum[_currentPage];
                        sum[_currentSheet] = $td;
                    } else {
                        $td.textContent = sum.calculator;
                    }
                });
                
                _vessel().$sumGroup = $colGroup;
                _vessel().$sumBody = $tbody;
                sumWrappers.push($sumDiv);
            });
            
            _sumWrappers = sumWrappers;
            let btmw = Math.min(parseFloat(self.width), _maxFixedWidth + _maxFreeWidth);
            gp.imiPages($frag, top, btmw + "px");
            gp.imiSheets($frag, _paging ? top + gp.PAGE_HEIGHT : top, btmw + "px");
            _leftAlign = left;
            
            let sizeUi = { headerWrappers: headerWrappers, bodyWrappers: bodyWrappers,
                            sumWrappers: sumWrappers, headerColGroup: headerColGroup,
                            bodyColGroup: bodyColGroup, sumColGroup: sumColGroup };
            let freeAdjuster = new kt.ColumnAdjuster([ _maxFixedWidth, freeWrapperWidth ], self.headerHeight, sizeUi, self.float);
            kt._adjuster = freeAdjuster;
            freeAdjuster.handle();
            su.binding(self.$container, self.autoFitWindow, self.minRows, self.maxRows);
            lch.checkUp(self.$container);
            
            self.$container.appendChild($frag);
            kt.screenLargeur(self.minRows, self.maxRows);
            console.log(performance.now() - start);
        }
    }
    
    module tn {
        export const COLUMN_FIX = "ColumnFixing";
        export const SUMMARIES = "Summaries";
        export const PAGING = "Paging";
        export const SHEET = "Sheet";
        export const RESIZING = "Resizing";
        export const HEADER_STYLE = "HeaderStyles";
        export const CELL_STYLE = "CellStyles";
        export const COPY = "Copy";
        export const WIDTH_SAVE = "WidthSaving";
        
        /**
         * Is enable.
         */
        export function isEnable(features: any, name: string) {
            return _.find(features, function(feature: any) {
                return feature.name === name;
            }) !== undefined;
        }
        
        /**
         * Find.
         */
        export function find(features: any, name: string) {
            return _.find(features, function(feature: any) {
                return feature.name === name;
            });
        }
    }
    
    module v {
        export const CELL_CLS = "mcell";
        export const DATA = "md";
        export const INIT_MAN_EDIT = "init-man-edit";
        
        /**
         * Process.
         */
        export function process($container: HTMLElement, options: any, isUpdate?: boolean) {
            let levelStruct = synthesizeHeaders(options);
            options.levelStruct = levelStruct;
            
            if (isUpdate && !_.isNil($container.style.maxWidth) && !_.isEmpty($container.style.maxWidth)) {
                let maxWidth = calcWidth(options.columns);
                if (!options.isHeader && options.overflow === "scroll") {
                    $container.style.maxWidth = (maxWidth + ti.getScrollWidth()) + "px";
                } else {
                    $container.style.maxWidth = maxWidth + "px";
                }
            }
            
            if (options.isHeader) {
                if (Object.keys(levelStruct).length > 1) {
                    return groupHeader($container, options, isUpdate);
                }
            } else {
                options.float = options.float === false ? false : true;
            }
            
            let result = table($container, options, isUpdate);
            let painter = paint($container, options);
            
            return { $table: result.$table, cols: result.cols, controlMap: result.controlMap, painter: painter };
        }
        
        /**
         * Group header.
         */
        function groupHeader($container: HTMLElement, options: any, isUpdate: boolean) {
            let $table = selector.create("table").html("<tbody></tbody>").addClass(options.containerClass + "-table")
                        .css({ position: "relative", "table-layout": "fixed", width: "100%", 
//                                "border-collapse": "separate", 
                                "user-select": "none" }).getSingle();
            $container.appendChild($table);
            let $tbody = $table.getElementsByTagName("tbody")[0];
            if (!isUpdate) {
                $container.style.height = options.height;
                $container.style.width = options.width;
            }
            if (!util.isNullOrUndefined(options.overflow)) $container.style.overflow = options.overflow;
            else if (!util.isNullOrUndefined(options.overflowX) && !util.isNullOrUndefined(options.overflowY)) {
                $container.style.overflowX = options.overflowX;
                $container.style.overflowY = options.overflowY;
            }
            let $colGroup = document.createElement("colgroup"); 
            $table.insertBefore($colGroup, $tbody);
            let colGroup = generateColGroup($colGroup, options.columns, options.ntsControls);
            let painter: GroupHeaderPainter = new GroupHeaderPainter(options);
            painter.rows($tbody);
            return { $table: $table, cols: colGroup.cols, controlMap: colGroup.controlMap, painter: painter };
        }
        
        /**
         * Generate column group.
         */
        function generateColGroup($colGroup: HTMLElement, columns: any, ntsControls?: Array<any>) {
            let cols = [], controlMap = {};
            _.forEach(columns, function(col: any) {
                if (!_.isNil(col.group)) {
                    let colGroup = generateColGroup($colGroup, col.group, ntsControls);
                    if (colGroup) {
                        colGroup.cols.forEach(c => cols.push(c));
                        _.assignIn(controlMap, colGroup.controlMap);
                    }
                    return;
                }
                let $col = document.createElement("col");
                $col.style.width = col.width;
                $colGroup.appendChild($col);
                cols.push($col);
                
                if (ntsControls && col.ntsControl) {
                    let foundControl = _.find(ntsControls, c => c.name === col.ntsControl);
                    if (foundControl) controlMap[col.key] = foundControl;
                }
                
                if (col.hidden === true) $col.style.display = "none";
            });
            
            return { cols: cols, controlMap: controlMap };
        }
        
        /**
         * Table.
         */
        export function table($container: HTMLElement, options: any, isUpdate?: boolean) {
            let $table = document.createElement("table");
            $table.innerHTML = "<tbody></tbody>";
            $table.className = options.containerClass + "-table";
            $container.appendChild($table);
            let $tbody = $table.getElementsByTagName("tbody")[0];
            if (!isUpdate) {
                $container.style.height = options.height;
                $container.style.width = options.width;
            }
            if (!_.isNil(options.overflow)) $container.style.overflow = options.overflow;
            else if (!_.isNil(options.overflowX)) {
                $container.style.overflowX = options.overflowX;
            } else if (!_.isNil(options.overflowY)) {
                $container.style.overflowY = options.overflowY;
            }
            let $colGroup = document.createElement("colgroup");
            $table.insertBefore($colGroup, $tbody);
            let colDef = generateColGroup($colGroup, options.columns, options.ntsControls);

            return { $table: $table, cols: colDef.cols, controlMap: colDef.controlMap };
        }
        
        /**
         * Paint.
         */
        export function paint($container: HTMLElement, options: any) {
            let dataSource;
            if (!_.isNil(options.dataSource)) {
                 dataSource = options.dataSource;   
            } else {
                let item = {};
                _.forEach(options.columns, function(col: any) {
                    item[col.key] = col.headerText;
                });
                dataSource = [item]; 
            }
            
            return normal($container, dataSource, options);
        }
        
        /**
         * Normal.
         */
        export function normal($container: HTMLElement, dataSource: Array<any>, options: any) {
            let painter: Painter = new Painter($container, options);
            $.data($container, lo.CANON, { _origDs: _.cloneDeep(dataSource), dataSource: dataSource, primaryKey: options.primaryKey, painter: painter });
            let $tbody = $container.querySelector("tbody");
            _.forEach(dataSource, function(item: any, index: number) {
                let $tr = painter.row(item, undefined, index);
                $tbody.appendChild($tr);
            });
            return painter;
        }
        
        /**
         * Construe.
         */
        export function construe($container: HTMLElement, containers: Array<HTMLElement>, options: any, single?: any) {
            if (options.features) {
                let cellStyleFt = tn.find(options.features, tn.CELL_STYLE);    
                if (cellStyleFt) {
                    if (_cellStates) options.states = _cellStates;
                    else {
                        [ "states" ].forEach(ft => {
                            if (cellStyleFt[ft]) {
                                let typeFt = _.groupBy(cellStyleFt[ft], "rowId");
                                _.forEach(typeFt, (value, key) => {
                                    typeFt[key] = _.groupBy(typeFt[key], (item) => {
                                        return item.columnKey;
                                    });
                                });
                                
                                _cellStates = typeFt;
                                options[ft] = _cellStates; 
                            }
                        });
                    }
                }
            }
            
            if (!_cloud) _cloud = new aho.Platrer(containers, options);
            let res = single ? _cloud.renderSideRows(true) : _cloud.renderRows(true);
            if (!res) return;
            let start = res.start, end = res.end, cursor;
            if (_.isNil(_mDesc)) {
                _mDesc = {};
                $.data($container, lo.DESC, _mDesc);
                if (!_.isNil(res.fixedColIdxes)) {
                    _mDesc.fixedColIdxes = res.fixedColIdxes;
                }
                
                if (_.isNil(_mDesc.fixedRows)) { 
                    _mDesc.fixedRows = [];
                    _mDesc.fixedRowElements = [];
                }
                _mDesc.rows = [];
                _mDesc.rowElements = [];
            }
               
            if (!_.isNil(res.colIdxes) && (_.isNil(_mDesc.colIdxes) || _mDesc.colIdxes.length === 0)) {
                _mDesc.colIdxes = res.colIdxes;
            }
            
            for (let i = start; i <= end; i++) {
                cursor = i - start;
                if (!_mDesc.fixedRows[i] && res.fixedRows[cursor]) {
                    _mDesc.fixedRows[i] = res.fixedRows[cursor];
                    _mDesc.fixedRowElements[i] = res.fixedRowElements[cursor];
                }
                _mDesc.rows[i] = res.rows[cursor];
                _mDesc.rowElements[i] = res.rowElements[cursor];
            }
            
            if (!_vessel()) {
                _mafollicle[_currentPage][_currentSheet] = {};
            }
            _vessel().desc = _mDesc;
            _vessel().errors = _errors;
            _vessel().dirties = _dirties;
            _vessel().zeroHidden = _zeroHidden;
            _vessel().selected = _selected;
            _vessel().histoire = _histoire;
            
            if (!_.isNil(_currentPage)) {
                let openRange = _pageSize * _currentPage;
                let closeRange = _pageSize * (_currentPage + 1) - 1; 
            }
        }
        
        /**
         * Synthesize headers.
         */
        export function synthesizeHeaders(options: any) {
            let level: any = {};
            peelStruct(options.columns, level, 0);
            let rowCount = Object.keys(level).length; 
            if (rowCount > 1) {
                _.forEach(Object.keys(level), function(key: any) {
                    _.forEach(level[key], function(col: any) {
                        if (util.isNullOrUndefined(col.colspan)) {
                            col.rowspan = rowCount - parseInt(key);
                        }
                    });
                });
            }
            return level;
        }
        
        /**
         * Peel struct.
         */
        function peelStruct(columns: Array<any>, level: any, currentLevel: number, parent: any) {
            let colspan = 0, noGroup = 0;
            
            _.forEach(columns, function(col: any) {
                let clonedCol = _.clone(col);
                let colCount = 0;
                if (!_.isNil(col.group)) {
                    colCount = col.group.length;
                    noGroup++;
                    let ret: any = peelStruct(col.group, level, currentLevel + 1, col.headerText);
                    if (!util.isNullOrUndefined(ret)) {
                        colCount += ret;
                    }
                    clonedCol.colspan = colCount;
                }
                
                if (_.isNil(level[currentLevel])) {
                    level[currentLevel] = [];
                }
                
                level[currentLevel].push(clonedCol);
                colspan += colCount;
                
                if (col.constraint) {
                    let validator = new hpl.ColumnFieldValidator(parent, col.headerText, col.constraint.primitiveValue, col.constraint);
                    _validators[col.key] = validator;
                }
                
                let linkType = col.ntsType; 
                if (linkType) {
                    let parts = linkType.split("_");
                    if (!parts || parts.length !== 2) return; 
                    if (parts[0] === "comboCode") {
                        _specialColumn[col.key] = parts[1];
                        _specialColumn[parts[1]] = col.key;
                    } else {
                        _specialLinkColumn[col.key] = { column: parts[1], changed: col.onChange };
                    }
                }
                
            });
            
            return colspan !== 0 ? (colspan - noGroup) : undefined;
        }
        
        /**
         * Get constraint name.
         */
        export function getConstraintName(key: string) {
            let column = _columnsMap[key];
            if (!column) return;
            let constraint = column.constraint;
            return constraint.primitiveValue ? ui.validation.getConstraint(constraint.primitiveValue).valueType
                        : constraint.cDisplayType;
        }
        
        abstract class Conditional {
            options: any;
            columnsMap: {[ key: string ]: any };
            columns: Array<any>;
            visibleColumns: Array<any>;
            hiddenColumns: Array<any>;
            visibleColumnsMap: {[ key: string ]: any };
            hiddenColumnsMap: {[ key: string ]: any };
            constructor(options: any) {
                this.options = options;
                let clsColumns = ti.classifyColumns(options);
                this.columns = clsColumns.columns;
                this.visibleColumns = clsColumns.visibleColumns;
                this.hiddenColumns = clsColumns.hiddenColumns;
                this.visibleColumnsMap = ti.getColumnsMap(this.visibleColumns);
                this.hiddenColumnsMap = ti.getColumnsMap(this.hiddenColumns);
                _.assignIn(_columnsMap, this.visibleColumnsMap); 
            }
        }
        
        export class Painter extends Conditional {
            $container: HTMLElement;
            styles: any;
            
            constructor($container: HTMLElement, options: any) {
                super(options);
                this.$container = $container;
                
                if (options.features) {
                    let headerStyle = tn.find(options.features, tn.HEADER_STYLE);
                    if (headerStyle) {
                        this.styles = _.groupBy(headerStyle.columns, "key");
                    }
                }
                
                if (!_.isNil(options.levelStruct)) {
                    this.columnsMap = ti.columnsMapFromStruct(options.levelStruct);
                } else {
                    this.columnsMap = _.groupBy(options.columns, "key");
                }
            }
            
            /**
             * BubColumn.
             */
            bubColumn(name: any, i: any) {
                let self = this;
                let col = _.remove(self.hiddenColumns, c => c.key === name);
                if (!col || col.length === 0) return;
                self.visibleColumns.push(col[0]);
                if (self.hiddenColumnsMap.hasOwnProperty(name)) delete self.hiddenColumnsMap[name];
                self.visibleColumnsMap[name] = col;
                _.forEach(_.keys(_mafollicle), k => {
                    if (k === SheetDef || k === String(_currentPage)) return;
                    let maf = _mafollicle[k];
                    _.forEach(_.keys(maf), s => { 
                        if (maf[s].hasOwnProperty("desc")) {
                            _.forEach(maf[s].desc.fixedRows, r => {
                                let a = r[i];
                                if (a && a.style.display === "none") {
                                    a.style.display = "";
                                }
                            });
                            return false;
                        }
                    });
                });
            }
            
            /**
             * UnbubColumn.
             */
            unbubColumn(name: any, i: any) {
                let self = this;
                let col = _.remove(self.visibleColumns, c => c.key === name);
                if (!col || col.length === 0) return;
                self.hiddenColumns.push(col[0]);
                if (self.visibleColumnsMap.hasOwnProperty(name)) delete self.visibleColumnsMap[name];
                self.hiddenColumnsMap[name] = col;
                _.forEach(_.keys(_mafollicle), k => {
                    if (k === SheetDef || k === String(_currentPage)) return;
                    let maf = _mafollicle[k];
                    _.forEach(_.keys(maf), s => { 
                        if (maf[s].hasOwnProperty("desc")) {
                            _.forEach(maf[s].desc.fixedRows, r => {
                                let a = r[i];
                                if (a && a.style.display !== "none") {
                                    a.style.display = "none";
                                }
                            });
                            return false;
                        }
                    });
                });
            }
            
            /**
             * Cell.
             */
            cell(rData: any, rowIdx: number, key: string): HTMLElement {
                let self = this;
                let cData = rData[key]; 
                let data = cData;
                let column: any = self.columnsMap[key];
                if (util.isNullOrUndefined(column)) return;
                let ws = column.css && column.css.whiteSpace ? column.css.whiteSpace : "nowrap";
                let td = document.createElement("td");
                $.data(td, lo.VIEW, rowIdx + "-" + key);
                
                let tdStyle = "";
                tdStyle += "; border-width: 1px; overflow: hidden; ";
                if (self.options.isHeader) {
                    tdStyle += "word-break: break-all; vertical-align: top;";
                } else {
                    tdStyle += "white-space: " + ws + ";"; // position: relative;";
                }
                
                if (!self.visibleColumnsMap[key]) {
                    tdStyle += "; display: none;";
                    if (self.$container.classList.contains(FIXED)) _fixedHiddenColumns.push(key);
                    else _hiddenColumns.push(key);
                }
                let hStyle;
                if (self.styles && (hStyle = self.styles[key])) {
                    _.forEach(hStyle[0].colors, c => {
                        if (c.indexOf('#') === 0) {
                            tdStyle += "; background-color: " + c + ";";
                        } else td.classList.add(c);
                    });
                }
                
                td.innerHTML = data;
                td.style.cssText += tdStyle;
                
                return td;
            }
            
            /**
             * Row.
             */
            row(data: any, config: any, rowIdx: number): HTMLElement {
                let self = this;
                let tr: any = document.createElement("tr");
                if (config) {
                    tr.style.height = parseFloat(config.css.height) + "px";
                }
                
                _.forEach(Object.keys(data), function(key: any, index: number) {
                    if (!self.visibleColumnsMap[key] && !self.hiddenColumnsMap[key]) return;
                    let cell = self.cell(data, rowIdx, key);
                    tr.appendChild(cell);
                });
                
                return tr;
            }
        }
        
        export class GroupHeaderPainter extends Conditional {
            levelStruct: any;
            styles: any;
            
            constructor(options: any) {
                super(options);
                this.levelStruct = options.levelStruct;
                this.columnsMap = ti.columnsMapFromStruct(this.levelStruct);
                if (options.features) {
                    let styleFt = tn.find(options.features, tn.HEADER_STYLE);
                    if (styleFt) {
                        this.styles = _.groupBy(styleFt.columns, "key");
                    }
                } 
            }
            
            /**
             * Cell.
             */
            cell(text: any, rowIdx: number, cell: any): HTMLElement {
                let self = this;
                let $td = document.createElement("td");
                $.data($td, lo.VIEW, rowIdx + "-" + cell.key);
                let tdStyle = "; border-width: 1px; overflow: hidden; word-break: break-all; vertical-align: top; border-collapse: collapse;";
                if (!_.isNil(cell.rowspan) && cell.rowspan > 1) $td.setAttribute("rowspan", cell.rowspan);
                if (!_.isNil(cell.colspan) && cell.colspan > 1) $td.setAttribute("colspan", cell.colspan);
                else if (_.isNil(cell.colspan) && !self.visibleColumnsMap[cell.key]) {
                    tdStyle += "; display: none;";
                    if (self.options.containerClass === FIXED) _fixedHiddenColumns.push(cell.key);
                    else _hiddenColumns.push(cell.key);
                }
                
                let column = self.columnsMap[cell.key];
                let hStyle;
                if (self.styles && ((hStyle = self.styles[cell.key])
                    || (cell.group && (hStyle = self.styles[cell.group[0].key])))) {
                    _.forEach(hStyle[0].colors, c => {
                        if (c.indexOf('#') === 0) {
                            tdStyle += "; background-color: " + c + ";";
                        } else $td.classList.add(c);
                    });
                }
                
                $td.innerHTML = text;
                $td.style.cssText += tdStyle;
                return $td;
            }
            
            /**
             * Rows.
             */
            rows($tbody: HTMLElement) {
                let self = this;
                _.forEach(Object.keys(self.levelStruct), function(rowIdx: any) {
                    
                    let $tr = document.createElement("tr");
                    let oneLevel = self.levelStruct[rowIdx];
                    _.forEach(oneLevel, function(cell: any) {
                        if (!self.visibleColumnsMap[cell.key] && !self.hiddenColumnsMap[cell.key]
                            && _.isNil(cell.colspan)) return;
                        let $cell = self.cell(cell.headerText, rowIdx, cell);
                        $tr.appendChild($cell);
                    });
                    
                    $tbody.appendChild($tr);
                });
            }
        }
        
        export class ConcurrentPainter {
            painter: any;
            painters: Array<Painter>;
            controlMap: any;
            primaryKey: string;
            columns: Array<string> = [];
            states: any;
            protoCell: HTMLElement;
            protoRow: HTMLElement;
            
            constructor(ui: any) {
                this.revive();
                this.primaryKey = ui.primaryKey;
                this.states = ui.states;
                this.protoRow = document.createElement("tr");
                this.protoCell = document.createElement("td");
            }
            
            /**
             * Revive.
             */
            revive() {
                this.painters = _mafollicle[SheetDef][_currentSheet].painters;
                this.columns = [];
                _.forEach(this.painters, p => _.forEach(p.columns, c => {
                    this.columns.push(c.key);
                }));
                
                this.controlMap = _mafollicle[SheetDef][_currentSheet].controlMap;
            }
            
            /**
             * Cell.
             */
            cell(rData: any, rowIdx: number, key: string, fixed: boolean) {
                let self = this;
                let cData = rData[key]; 
                let data = cData, columnsMap, visibleColumnsMap, paint;
                if (fixed) {
                    paint = self.painters[0];
                } else if (self.painters.length > 1) {
                    paint = self.painters[1];
                } else {
                    paint = self.painters[0];
                }
                
                columnsMap = paint.columnsMap;
                visibleColumnsMap = paint.visibleColumnsMap;
                
                let column: any = columnsMap[key];
                if (_.isNil(column)) return;
                let ws = column.css && column.css.whiteSpace ? column.css.whiteSpace : "nowrap";
//                let td = document.createElement("td");
                let td = self.protoCell.cloneNode(true);
                td.classList.add(CELL_CLS);
                td.tabIndex = -1;
                $.data(td, lo.VIEW, rowIdx + "-" + key);
                
                let tdStyle = "";
                tdStyle += "; border-width: 1px; overflow: hidden; white-space: " 
                            + ws + "; padding: 0px 2px; ";//position: relative;";
                
                let col = visibleColumnsMap[key];
                if (!col) tdStyle += "; display: none;";
                else if (col[0].columnCssClass === hpl.CURRENCY_CLS || col[0].columnCssClass === "halign-right") {
                    td.classList.add(col[0].columnCssClass);
                }
                let controlDef = self.controlMap[key];
                
                let id = rData[self.primaryKey];
                let rState, cState;
                if (self.states && (rState = self.states[id]) && (cState = rState[key])) {
                    _.forEach(cState, s => {
                        _.forEach(s.state, st => {
                            if (st.indexOf('#') === 0) {
                                tdStyle += "; color: " + cState + ";";
                            } else if (st === color.ManualEditTarget || st === color.ManualEditOther) {
                                td.classList.add(st);
                                if (!s.suivant) $.data(td, INIT_MAN_EDIT, st);
                            } else td.classList.add(st);
                        });
                    });
                    rState = null;
                    cState = null;
                }
                
                if (column.ntsControl === dkn.LABEL) {
                    td.classList.add(dkn.LABEL_CLS);
                    td.innerHTML = data;
                    $.data(td, DATA, data);
                    dkn.controlType[key] = dkn.LABEL;
                } else if (controlDef) {
                    let ui: any = {
                        rowIdx: rowIdx,
                        rowId: id,
                        columnKey: key,
                        controlDef: controlDef,
                        update: (v, i, r) => {
                            su.wedgeCell(_$grid[0], { rowIdx: (_.isNil(i) ? rowIdx : i), columnKey: key }, v, r)
                        },
                        deleteRow: su.deleteRow,
                        initValue: data,
                        rowObj: rData,
                        enable: !td.classList.contains(color.Disable)
                    };
                    
                    let res, control = dkn.getControl(controlDef.controlType);
                    if (control) {
                        if (controlDef.controlType === dkn.CHECKBOX && ui.enable) {
                            let origVal = _mafollicle[_currentPage].origDs[rowIdx][key];
                            if (dkn.allCheck[key] === true) {
                                ui.initValue = true;
                                res = su.wedgeCell(_$grid[0], { rowIdx: rowIdx, columnKey: key }, true);
                                if (res) td.classList.add(res);
                            } else if (dkn.allCheck[key] === false) {
                                ui.initValue = false;
                                res = su.wedgeCell(_$grid[0], { rowIdx: rowIdx, columnKey: key }, false);
                                if (res) td.classList.add(res);
                            }
                        }
                        
                        let $control = control(ui);
                        if (controlDef.controlType !== dkn.COMBOBOX) {
                            td.appendChild($control);
                        } else {
                            td.innerHTML = $control.name;
                            $.data(td, "code", $control.code);
                        }
                    }
                    $.data(td, DATA, data);
                } else if (_zeroHidden && ti.isZero(data, key)) {
                    td.textContent = "";
                    dkn.textBox(key);
                    let formatted = su.format(column, data);
                    let disFormat = su.formatSave(column, data); 
                    $.data(td, DATA, disFormat);
                } else {
                    let formatted = su.format(column, data);
                    td.innerHTML = formatted;
                    dkn.textBox(key);
                    let disFormat = su.formatSave(column, data);
                    $.data(td, DATA, disFormat);
                }
                td.style.cssText += tdStyle;
                
//                let sum = _summaries[key];
//                if (sum) {
//                    switch (sum.calculator) {
//                        case "Time":
//                            sum.total.add(moment.duration(data));
//                            break;
//                        case "Number": 
//                            sum.total += (!_.isNil(data) ? parseFloat(data) : 0);
//                            break;
//                    } 
//                }
                
                return td;
            }
            
            /**
             * Row.
             */
            row(data: any, config: any, rowIdx: number) {
                let self = this;
                let fixedColumns, fixedCount = 0;
                if (rowIdx === 0 && self.painters.length > 1) {
                    fixedColumns = self.painters[0].columns;
                    fixedCount = fixedColumns.length;
                }
                
                let fixedVColumnsMap, dVColumnsMap, hiddenFixed, hiddenDV;
                if (self.painters.length > 1) {
                    fixedVColumnsMap = self.painters[0].visibleColumnsMap;
                    hiddenFixed = self.painters[0].hiddenColumnsMap;
                    dVColumnsMap = self.painters[1].visibleColumnsMap; 
                    hiddenDV = self.painters[1].hiddenColumnsMap;
                } else {
                    dVColumnsMap = self.painters[0].visibleColumnsMap;
                    hiddenDV = self.painters[0].hiddenColumnsMap;
                }
                
                let fixedColIdxes = {}, colIdxes = {},
                    fixedElements = [], elements = [],
                    fixedTr, tr: any = self.protoRow.cloneNode(true); //document.createElement("tr");
                if (fixedVColumnsMap && _.keys(fixedVColumnsMap).length > 0) {
//                    fixedTr = document.createElement("tr");
                    fixedTr = self.protoRow.cloneNode(true);
                }
                
                if (config) {
                    if (fixedTr) {
                        fixedTr.style.height = parseFloat(config.css.height) + "px";
                    }
                    tr.style.height = parseFloat(config.css.height) + "px";
                }
                
                _.forEach(self.columns, function(key: any, index: number) {
                    let cell;
                    if (dVColumnsMap[key] || hiddenDV[key]) {
                        cell = self.cell(data, rowIdx, key);
                        tr.appendChild(cell);
                        elements.push(cell);
                        if (rowIdx === 0) colIdxes[key] = index - fixedCount;
                    } else if (fixedVColumnsMap[key] || hiddenFixed[key]) {
                        cell = self.cell(data, rowIdx, key, true);
                        fixedTr.appendChild(cell);
                        fixedElements.push(cell);
                        if (rowIdx === 0) fixedColIdxes[key] = index;
                    }
                });
                
                if (fixedTr) {
                    fixedTr.addXEventListener(ssk.MOUSE_OVER, evt => {
                        self.hoover(evt);
                    });
                    
                    fixedTr.addXEventListener(ssk.MOUSE_OUT, evt => {
                        self.hoover(evt, true);
                    });
                }
                
                tr.addXEventListener(ssk.MOUSE_OVER, evt => {
                    self.hoover(evt);
                });
                
                tr.addXEventListener(ssk.MOUSE_OUT, evt => {
                    self.hoover(evt, true);
                });
                
                let ret = { fixedRow: fixedTr, row: tr, fixedElements: fixedElements, elements: elements }; 
                if (rowIdx === 0) {
                    ret.fixedColIdxes = fixedColIdxes;
                    ret.colIdxes = colIdxes;
                }
                
                return ret;
            }
            
            /**
             * Hoover.
             */
            hoover(evt: any, out?: boolean) {
                let $tCell = evt.target;
                if (!selector.is($tCell, "td." + CELL_CLS)) return;
                let coord = ti.getCellCoord($tCell);
                if (!coord) return;
                let elms;
                if (_mDesc.fixedRows && (elms = _mDesc.fixedRows[coord.rowIdx])) {
                    _.forEach(elms, c => {
                        if (!c.classList.contains(color.HOVER) && !out) {
                            c.classList.add(color.HOVER);
                            _hr = coord.rowIdx;
                        } else if (c.classList.contains(color.HOVER) && out) {
                            c.classList.remove(color.HOVER);
                        }
                    });
                }
                
                if (_mDesc.rows && (elms = _mDesc.rows[coord.rowIdx])) {
                    _.forEach(elms, c => {
                        if (!c.classList.contains(color.HOVER) && !out) {
                            c.classList.add(color.HOVER);
                            _hr = coord.rowIdx;
                        } else if (c.classList.contains(color.HOVER) && out) {
                            c.classList.remove(color.HOVER);
                        }
                    });
                }
            }
        }
        
        export class SidePainter {
            columnsMap: any;
            columns: Array<any>;
            visibleColumns: Array<any>;
            visibleColumnsMap: any;
            controlMap: any;
            primaryKey: string;
            states: any;
            protoCell: HTMLElement;
            protoRow: HTMLElement;
            
            constructor(ui: any) {
                this.revive();
                this.primaryKey = ui.primaryKey;
                this.states = ui.states;
                this.protoRow = document.createElement("tr");
                this.protoCell = document.createElement("td");
            }
            
            /**
             * Revive.
             */
            revive() {
                let colCls = ti.classifyColumns({ columns: _cstifle() });
                this.columns = colCls.columns;
                this.visibleColumns = colCls.visibleColumns;
                this.visibleColumnsMap = ti.getColumnsMap(this.visibleColumns);
                this.controlMap = _mafollicle[SheetDef][_currentSheet].controlMap;
                let levelStruct = _mafollicle[SheetDef][_currentSheet].levelStruct;
                
                if (!_.isNil(levelStruct)) {
                    this.columnsMap = ti.columnsMapFromStruct(levelStruct);
                } else {
                    this.columnsMap = _.groupBy(_cstifle(), "key");
                }
            }
            
            /**
             * Cell.
             */
            cell(rData: any, rowIdx: number, key: string, fixed: boolean) {
                let self = this;
                let cData = rData[key]; 
                let data = cData;
                
                let column = self.columnsMap[key];
                if (_.isNil(column)) return;
                let ws = column.css && column.css.whiteSpace ? column.css.whiteSpace : "nowrap";
//                let td = document.createElement("td");
                let td = self.protoCell.cloneNode(true);
                td.classList.add(CELL_CLS);
                td.tabIndex = -1;
                $.data(td, lo.VIEW, rowIdx + "-" + key);
                
                let tdStyle = "";
                tdStyle += "; border-width: 1px; overflow: hidden; white-space: " 
                            + ws + "; padding: 0px 2px;"; // position: relative";
                
                let col = self.visibleColumnsMap[key];
                if (!col) tdStyle += "; display: none;";
                else if (col[0].columnCssClass === hpl.CURRENCY_CLS || col[0].columnCssClass === "halign-right") {
                    td.classList.add(col[0].columnCssClass);
                }
                let controlDef = self.controlMap[key];
                
                let id = rData[self.primaryKey];
                let rState, cState;
                if (self.states && (rState = self.states[id]) && (cState = rState[key])) {
                    _.forEach(cState, s => {
                        _.forEach(s.state, st => {
                            if (st.indexOf('#') === 0) {
                                tdStyle += "; color: " + cState + ";";
                            } else if (st === color.ManualEditTarget || st === color.ManualEditOther) {
                                td.classList.add(st);
                                if (!s.suivant) $.data(td, INIT_MAN_EDIT, st);
                            } else td.classList.add(st);
                        });
                    });
                    rState = null;
                    cState = null;
                }
                
                if (column.ntsControl === dkn.LABEL) {
                    td.classList.add(dkn.LABEL_CLS);
                    td.innerHTML = data;
                    $.data(td, DATA, data);
                    dkn.controlType[key] = dkn.LABEL;
                } else if (controlDef) {
                    let ui: any = {
                        rowIdx: rowIdx,
                        rowId: id,
                        columnKey: key,
                        controlDef: controlDef,
                        update: (v, i, r) => {
                            su.wedgeCell(_$grid[0], { rowIdx: (_.isNil(i) ? rowIdx : i), columnKey: key }, v, r)
                        },   
                        deleteRow: su.deleteRow,
                        initValue: data,
                        rowObj: rData,
                        enable: !td.classList.contains(color.Disable)
                    };
                    
                    let control = dkn.getControl(controlDef.controlType);
                    if (control) {
                        let $control = control(ui);
                        if (controlDef.controlType !== dkn.COMBOBOX) {
                            td.appendChild($control);
                        } else {
                            td.innerHTML = $control.name;
                            $.data(td, "code", $control.code);
                        }
                    }
                    $.data(td, DATA, data);
                } else if (_zeroHidden && ti.isZero(data, key)) {
                    td.textContent = "";
                    dkn.textBox(key);
                    let formatted = su.format(column, data);
                    let disFormat = su.formatSave(column, data);
                    $.data(td, DATA, disFormat);
                } else {
                    let formatted = su.format(column, data);
                    td.innerHTML = formatted;
                    dkn.textBox(key);
                    let disFormat = su.formatSave(column, data);
                    $.data(td, DATA, disFormat);
                }
                td.style.cssText += tdStyle;
                
//                let sum = _summaries[key];
//                if (sum) {
//                    switch (sum.calculator) {
//                        case "Time":
//                            sum.total.add(moment.duration(data));
//                            break;
//                        case "Number": 
//                            sum.total += (!_.isNil(data) ? parseFloat(data) : 0);
//                            break;
//                    } 
//                }
                
                return td;
            }
            
            /**
             * Row.
             */
            row(data: any, config: any, rowIdx: number) {
                let self = this; 
                let colIdxes = {}, elements = [], 
                    tr: any = self.protoRow.cloneNode(true); //document.createElement("tr");
                
                if (config) {
                    tr.style.height = parseFloat(config.css.height) + "px";
                }
                
                _.forEach(self.columns, function(col: any, index: number) {
                    let cell, key = col.key;
                    cell = self.cell(data, rowIdx, key);
                    tr.appendChild(cell);
                    elements.push(cell);
                    if (rowIdx === 0) colIdxes[key] = index;
                });
                
                tr.addXEventListener(ssk.MOUSE_OVER, evt => {
                    self.hoover(evt);
                });
                
                tr.addXEventListener(ssk.MOUSE_OUT, evt => {
                    self.hoover(evt, true);
                });
                
                let ret = { row: tr, elements: elements }; 
                if (rowIdx === 0) {
                    ret.colIdxes = colIdxes;
                }
                
                return ret;
            }
            
            /**
             * Hoover.
             */
            hoover(evt: any, out?: boolean) {
                let $tCell = evt.target;
                if (!selector.is($tCell, "td." + CELL_CLS)) return;
                let coord = ti.getCellCoord($tCell);
                if (!coord) return;
                let elms;
                if (_mDesc.fixedRows && (elms = _mDesc.fixedRows[coord.rowIdx])) {
                    _.forEach(elms, c => {
                        if (!c.classList.contains(color.HOVER) && !out) {
                            c.classList.add(color.HOVER);
                            _hr = coord.rowIdx;
                        } else if (c.classList.contains(color.HOVER) && out) {
                            c.classList.remove(color.HOVER);
                        }
                    });
                }
                
                if (_mDesc.rows && (elms = _mDesc.rows[coord.rowIdx])) {
                    _.forEach(elms, c => {
                        if (!c.classList.contains(color.HOVER) && !out) {
                            c.classList.add(color.HOVER);
                            _hr = coord.rowIdx;
                        } else if (c.classList.contains(color.HOVER) && out) {
                            c.classList.remove(color.HOVER);
                        }
                    });
                }
            }
        }
        
        /**
         * Extra.
         */
        export function extra(className: string, height: number) {
            let element = document.createElement("tr");
            element.style.height = height + "px";
            ti.addClass(element, "extable-" + className);
            return element;
        }
        
        /**
         * Wrapper styles.
         */
        export function wrapperStyles(top: string, left: string, width: string, maxWidth?: string, height: string) {
            let style: any = { 
                position: "absolute",
                overflow: "hidden",
                top: top,
                left: left,
                width: width,
                border: "solid 1px #CCC"
            };
            
            if (maxWidth) {
                style.maxWidth = maxWidth;
                if (parseFloat(maxWidth) < parseFloat(width)) style.width = maxWidth;
            }
            
            if (height) {
                style.height = height;
            }
            
            return style;   
        }
        
        /**
         * Calc width.
         */
        export function calcWidth(columns: Array<any>) {
            let width = 0;
            columns.forEach(function(c, i) {
                if (c.hidden === true) return;
                if (c.group) {
                    width += calcWidth(c.group);
                    return;
                }
                width += parseFloat(c.width);
            });
            return width;
        }
        
        /**
         * Create wrapper.
         */
        export function createWrapper(top: string, left: string, options: any, newOpt?: any) {
            let style, width, maxWidth;
            if (options.containerClass === FREE) {
                if (!_maxFreeWidth || newOpt) {
                    _maxFreeWidth = calcWidth(options.columns);
                } 
                maxWidth = options.isHeader ? _maxFreeWidth : _maxFreeWidth + ti.getScrollWidth();
                style = wrapperStyles(top, left, options.width, maxWidth + "px", options.height);
                style["background-color"] = "#F3F3F3"; 
                style["padding-right"] = "1px";
            } else if (options.containerClass === FIXED) {
                if (!_maxFixedWidth || newOpt) {
                    _maxFixedWidth = calcWidth(options.columns);
                }
                style = wrapperStyles(top, left, _maxFixedWidth + "px", undefined, options.height);
                style["background-color"] = "#F3F3F3";
                style["padding-right"] = "1px";
            } else if (options.containerClass === gp.PAGING_CLS || options.containerClass === gp.SHEET_CLS) {
                style = wrapperStyles(top, left, options.width, undefined, options.height);
                style["background-color"] = "#E9E9E9";
                style["border"] = "1px solid #dddddd";
                style["color"] = "#333333";
            } else {
                width = options.containerClass === FIXED + "-summaries" ? _maxFixedWidth + "px" : options.width;
                maxWidth = options.containerClass !== FIXED + "-summaries" ? _maxFreeWidth + "px" : undefined;
                style = wrapperStyles(top, left, width, maxWidth, options.height);
                style["z-index"] = 1; 
                style["background-color"] = "#F6F6F6";
            }
            
            return selector.create("div").data(lo.MPART, options.containerClass)
                        .addClass(options.containerClass)
                        .css(style).getSingle();
        }
    }
    
    module aho {
        export const TOP_SPACE = "top-space";
        export const BOTTOM_SPACE = "bottom-space";
        export const NULL = null;
        
        export class Platrer {
            $fixedContainer: HTMLElement;
            $container: HTMLElement;
            options: any;
            rowsOfBlock: number;
            blocksOfCluster: number;
            rowHeight: number;
            blockHeight: number;
            clusterHeight: number;
            primaryKey: string;
            topOffset: number;
            bottomOffset: number;
            currentCluster: number;
            startIndex: number;
            endIndex: number;
            hasSum: boolean;
            painter: v.ConcurrentPainter;
            sidePainter: v.SidePainter;
            
            constructor(containers: Array<any>, options: any) {
                if (containers && containers.length > 1) {
                    this.$fixedContainer = containers[0];
                    this.$container = containers[1];
                } else this.$container = containers[0];
                this.options = options;
                this.primaryKey = options.primaryKey;
                this.rowsOfBlock = options.noBlocRangee || 30;
                this.blocksOfCluster = options.noGrappeBloc || 3;
                this.rowHeight = parseInt(BODY_ROW_HEIGHT);
                this.blockHeight = this.rowsOfBlock * this.rowHeight;
                this.clusterHeight = this.blockHeight * this.blocksOfCluster;
                this.hasSum = options.hasSum;
                let ui = { 
                            primaryKey: this.primaryKey, 
                            states: options.states,
                            levelStruct: options.levelStruct
                        };
                this.painter = new v.ConcurrentPainter(ui);
                this.sidePainter = new v.SidePainter(ui);
                this.onScroll();
            }
            
            /**
             * Get cluster no.
             */
            getClusterNo() {
                return Math.floor(this.$container.scrollTop / (this.clusterHeight - this.blockHeight));
            }
            
            /**
             * Render rows.
             */
            renderRows(manual?: boolean) {
                let self = this;
                let clusterNo = self.getClusterNo();
                if (manual) self.currentCluster = clusterNo;
                if (_dataSource.length < self.rowsOfBlock) {
                    self.topOffset = 0;
                    self.bottomOffset = 0;
                }
                
                let rowsOfCluster = self.blocksOfCluster * self.rowsOfBlock;
                let startRowIdx = _start = self.startIndex = Math.max((rowsOfCluster - self.rowsOfBlock) * clusterNo, 0);
                let endRowIdx = self.endIndex = startRowIdx + rowsOfCluster;
                _end = endRowIdx - 1;
                self.topOffset = Math.max(startRowIdx * self.rowHeight, 0);
                self.bottomOffset = Math.max((_dataSource.length - endRowIdx) * self.rowHeight, 0);
                let rowConfig = { css: { height: self.rowHeight } };
                
                let containerElm = self.$container;
                let fixedTbody, tbody = document.createElement("tbody");
                let topSpace = v.extra(TOP_SPACE, self.topOffset);
                
                if (self.$fixedContainer) {
                    fixedTbody = document.createElement("tbody");
                    fixedTbody.appendChild(topSpace.cloneNode(true));
                }
                
                tbody.appendChild(topSpace);
                let res = {}, fixedRows = [], rows = [], fixedRowElements = [], rowElements = [], min, max;
                for (let i = startRowIdx; i < endRowIdx; i++) {
                    if (_.isNil(_dataSource[i])) continue;
                    let rElm;
                    
                    if (_mDesc && _mDesc.rowElements && (rElm = _mDesc.rowElements[i])) {
                        tbody.appendChild(rElm);
                        if (self.$fixedContainer) {
                            fixedTbody.appendChild(_mDesc.fixedRowElements[i]);
                        }
                        continue;
                    }
                    
                    if (_.isNil(min)) min = i;
                    max = Math.max(_.isNil(min) ? startRowIdx : min, i);
                    let rowElms = self.painter.row(_dataSource[i], rowConfig, i);
                    tbody.appendChild(rowElms.row);
                    rowElements.push(rowElms.row);
                    
                    if (self.$fixedContainer) {
                        if (_mDesc && _mDesc.fixedRowElements && (rElm = _mDesc.fixedRowElements[i])) {
                            fixedTbody.appendChild(rElm);
                        } else fixedTbody.appendChild(rowElms.fixedRow);
                        fixedRowElements.push(rowElms.fixedRow);
                    }
                    
                    // Assure equilibrium
                    fixedRows.push(rowElms.fixedElements);
                    rows.push(rowElms.elements);
                    if (i === 0) {
                        res.fixedColIdxes = rowElms.fixedColIdxes;
                        res.colIdxes = rowElms.colIdxes;
                    }
                } 
                
                let bottomSpace = v.extra(BOTTOM_SPACE, self.hasSum ? self.bottomOffset + SUM_HEIGHT + 2 : self.bottomOffset);
                tbody.appendChild(bottomSpace);
                containerElm.querySelector("table").replaceChild(tbody, containerElm.getElementsByTagName("tbody")[0]);
                
                if (self.$fixedContainer) {
                    fixedTbody.appendChild(bottomSpace.cloneNode(true));
                    self.$fixedContainer.querySelector("table").replaceChild(fixedTbody, self.$fixedContainer.getElementsByTagName("tbody")[0]);
                }
                
                if (rows.length === 0) return;
                
                res.fixedRows = fixedRows;
                res.rows = rows;
                res.fixedRowElements = fixedRowElements;
                res.rowElements = rowElements;
                res.start = !_.isNil(min) ? min : startRowIdx;
                res.end = max;
                
                setTimeout(() => {
                    ssk.trigger(self.$container, ssk.RENDERED);
                }, 0);
                
                return res;
            }
            
            /**
             * OnScroll.
             */
            onScroll() {
                let self = this;
                self.$container.removeXEventListener(ssk.SCROLL_EVT + ".detail");
                self.$container.addXEventListener(ssk.SCROLL_EVT + ".detail", function() {
                    let inClusterNo = self.getClusterNo();
                    if (self.currentCluster !== inClusterNo) {
                        self.currentCluster = inClusterNo;
                        let res = self.renderRows();
                        let hCols;
                        if (!_.isNil(_hr) && (hCols = _mDesc.rows[_hr])) {
                            _.forEach(hCols, c => {    
                                if (c.classList.contains(color.HOVER)) {
                                    c.classList.remove(color.HOVER);
                                }
                            });
                        }
                        
                        if (!_.isNil(_hr) && (hCols = _mDesc.fixedRows[_hr])) {
                            _.forEach(hCols, c => {
                                if (c.classList.contains(color.HOVER)) {
                                    c.classList.remove(color.HOVER);
                                }
                            });
                        }
                        
                        if (!res) return;
                        let start = res.start, end = res.end, cursor;
                        for (let i = start; i <= end; i++) {
                            cursor = i - start;
                            if (!_mDesc.fixedRows[i]) {
                                _mDesc.fixedRows[i] = res.fixedRows[cursor];
                                _mDesc.fixedRowElements[i] = res.fixedRowElements[cursor];
                            }
                            
                            _mDesc.rows[i] = res.rows[cursor];
                            _mDesc.rowElements[i] = res.rowElements[cursor];
                        }
                    }
                });
            }
            
            /**
             * RenderSideRows.
             */
            renderSideRows(manual?: boolean) {
                let self = this;
                let clusterNo = self.getClusterNo();
                if (manual) self.currentCluster = clusterNo;
                if (_dataSource.length < self.rowsOfBlock) {
                    self.topOffset = 0;
                    self.bottomOffset = 0;
                }
                
                let rowsOfCluster = self.blocksOfCluster * self.rowsOfBlock;
                let startRowIdx = _start = self.startIndex = Math.max((rowsOfCluster - self.rowsOfBlock) * clusterNo, 0);
                let endRowIdx = self.endIndex = startRowIdx + rowsOfCluster;
                _end = endRowIdx - 1;
                self.topOffset = Math.max(startRowIdx * self.rowHeight, 0);
                self.bottomOffset = Math.max((_dataSource.length - endRowIdx) * self.rowHeight, 0);
                let rowConfig = { css: { height: self.rowHeight } };
                
                let containerElm = self.$container;
                let tbody = document.createElement("tbody");
                let topSpace = v.extra(TOP_SPACE, self.topOffset);
                
                tbody.appendChild(topSpace);
                let res = {}, rows = [], rowElements = [], min, max;
                for (let i = startRowIdx; i < endRowIdx; i++) {
                    if (_.isNil(_dataSource[i])) continue;
                    let rElm;
                    
                    if (_mDesc && _mDesc.rowElements && (rElm = _mDesc.rowElements[i])) {
                        tbody.appendChild(rElm);
                        continue;
                    }
                    
                    if (_.isNil(min)) min = i;
                    max = Math.max(_.isNil(min) ? startRowIdx : min, i);
                    let rowElms = self.sidePainter.row(_dataSource[i], rowConfig, i);
                    tbody.appendChild(rowElms.row);
                    rowElements.push(rowElms.row);
                    rows.push(rowElms.elements);
                    
                    if (i === 0) {
                        res.colIdxes = rowElms.colIdxes;
                    }
                } 
                
                let bottomSpace = v.extra(BOTTOM_SPACE, self.hasSum ? self.bottomOffset + SUM_HEIGHT + 2 : self.bottomOffset);
                tbody.appendChild(bottomSpace);
                containerElm.querySelector("table").replaceChild(tbody, containerElm.getElementsByTagName("tbody")[0]);
                
                if (rows.length === 0) return;
                
                res.rows = rows;
                res.rowElements = rowElements;
                res.start = !_.isNil(min) ? min : startRowIdx;
                res.end = max;
                
                setTimeout(() => {
                    ssk.trigger(self.$container, ssk.RENDERED);
                }, 0);
                
                return res;
            }
        }
    }
    
    module tc {
        export let SCROLL_SYNCING = "scroll-syncing";
        export let VERT_SCROLL_SYNCING = "vert-scroll-syncing";
        
        /**
         * Bind vertWheel.
         */
        export function bindVertWheel($container: HTMLElement, showY?: boolean, abnorm?: boolean) {
            let $_container = $($container);
            $container.addXEventListener(ssk.MOUSE_WHEEL, function(event: any) {
                let delta = event.deltaY;
                let direction = delta < 0 ? -1 : 1;
                let value = $_container.scrollTop();
//                $container.stop().animate({ scrollTop: value }, 10);
                let os = ti.isIE() ? 25 : 50;
                if (!abnorm && ((direction < 0 && value === 0)
                    || (direction > 0 && $container.scrollHeight - value + ti.getScrollWidth() === $_container.height()))) {
                    let $contents = document.getElementById("contents-area");
                    if ($contents) {
                        $contents.scrollTop += direction * os;
                    }
                }
                
                $_container.scrollTop(value + direction * os);
                
                if (_mEditor && _mEditor.type === dkn.COMBOBOX) {
                    let cbx = dkn.controlType[_mEditor.columnKey];
                    let $combo = cbx.my.querySelector("." + dkn.CBX_CLS);
                    if (cbx.dropdown && cbx.dropdown.style.top !== "-99999px") {
                        dkn.closeDD(cbx.dropdown);
                        $combo.classList.remove(dkn.CBX_ACTIVE_CLS);
                    }
                }  
                
                event.preventDefault();
                event.stopImmediatePropagation();
            });
            
            if (!showY && $container.style.overflowY !== "hidden") {
                $container.style.overflowY = "hidden";
            }
        }
        
        /**
         * Unbind vertWheel.
         */
        export function unbindVertWheel($container: HTMLElement) {
            $container.removeXEventListener(ssk.MOUSE_WHEEL);
            $container.style.overflowY = "scroll";
        }
        
        /**
         * Sync scrolls.
         */
        export function syncDoubDirHorizontalScrolls(wrappers: Array<HTMLElement>) {
            _.forEach(wrappers, function($main: HTMLElement, index: number) {
                if (!$main) return;
                $main.addXEventListener(ssk.SCROLL_EVT, function() {
                    _.forEach(wrappers, function($depend: HTMLElement, i: number) {
                        if (i === index || !$depend) return;
                        let mainSyncing = $.data($main, SCROLL_SYNCING);
                        if (!mainSyncing) {
                            $.data($depend, SCROLL_SYNCING, true);
                            $depend.scrollLeft = $main.scrollLeft;
                        }
                    });
                    $.data($main, SCROLL_SYNCING, false);
                });
            });
        }
        
        /**
         * Sync scrolls.
         */
        export function syncDoubDirVerticalScrolls(wrappers: Array<HTMLElement>) {
            _.forEach(wrappers, function($main: HTMLElement, index: number) {
                    $main.addXEventListener(ssk.SCROLL_EVT, function(event: any) {
                        _.forEach(wrappers, function($depend: HTMLElement, i: number) {
                            if (i === index) return;
                            let mainSyncing = $.data($main, VERT_SCROLL_SYNCING);
                            if (!mainSyncing) {
                                $.data($depend, VERT_SCROLL_SYNCING, true);
                                $depend.scrollTop = $main.scrollTop;
                            }
                        });
                        $.data($main, VERT_SCROLL_SYNCING, false);
                    });
            });
        }
        
        /**
         * Sync scroll.
         */
        export function syncHorizontalScroll($headerWrap: HTMLElement, $bodyWrap: HTMLElement) {
            $bodyWrap.addXEventListener(ssk.SCROLL_EVT, function() {
                $headerWrap.scrollLeft = $bodyWrap.scrollLeft;
            });
        }
        
        /**
         * Sync scroll.
         */
        export function syncVerticalScroll($pivotBody: HTMLElement, bodyWraps: Array<HTMLElement>) {
            $pivotBody.addXEventListener(ssk.SCROLL_EVT, function() {
                _.forEach(bodyWraps, function(body: HTMLElement) {
                    body.scrollTop = $pivotBody.scrollTop;
                });
            });
        }
        
        /**
         * VisualJumpTo.
         */
        export function visualJumpTo($grid: HTMLElement, index: any) {
            if (index >= _cloud.startIndex && index < _cloud.endIndex) return;
            let tbl = $grid.querySelector("." + FREE + ":not(.mgrid-header)");
            tbl.scrollTop = index * BODY_ROW_HEIGHT;
            return true;
        }
    }
    
    module kt {
        export const AGENCY = "mgrid-agency";
        export const FIXED_LINE = "mgrid-fixed-line";
        export const LINE = "mgrid-line";
        export const RESIZE_COL = "resizeColumn";
        export const RESIZE_NO = "resizeNo";
        export const AREA_AGENCY = "mgrid-area-agency";
        export const RESIZE_AREA = "resize-area";
        export const AREA_LINE = "mgrid-area-line";
        export const STAY_CLS = "mgrid-stay";
        
        export let _adjuster;
        export let _widths = {};
        export let _columnWidths = {};
        
        export class ColumnAdjuster {
            headerWrappers: Array<HTMLElement>;
            bodyWrappers: Array<HTMLElement>;
            sumWrappers: Array<HTMLElement>;
            headerColGroup: Array<HTMLElemenet> = [];
            bodyColGroup: Array<HTMLElement> = [];
            sumColGroup: Array<HTMLElement> = [];
            widths: Array<string>;
            height: string;
            unshiftRight: any;
            dir: any
            
            $fixedAgency: HTMLElement;
            $agency: HTMLElement;
            fixedLines: Array<HTMLElement> = [];
            lines: Array<HTMLElement> = []; 
            $ownerDoc: HTMLElement;
            actionDetails: any;
            
            constructor(widths: Array<string>, height: any, sizeUi: any, unshift?: any) {
                this.headerWrappers = sizeUi.headerWrappers;
                this.bodyWrappers = sizeUi.bodyWrappers;
                this.sumWrappers = sizeUi.sumWrappers;
                this.unshiftRight = unshift;
                _.forEach(sizeUi.headerColGroup, g => {
                    if (g) {
                        let vCols = g.filter(c => c.style.display !== "none");
                        this.headerColGroup.push(vCols);
                    }
                });
                
                _.forEach(sizeUi.bodyColGroup, g => {
                    if (g) {
                        let vCols = g.filter(c => c.style.display !== "none");
                        this.bodyColGroup.push(vCols);
                    }
                });
                
                _.forEach(sizeUi.sumColGroup, g => {
                    if (g) {
                        let vCols = g.filter(c => c.style.display !== "none");
                        this.sumColGroup.push(vCols);
                    }
                });
                this.widths = widths;
                this.height = height;
                this.$ownerDoc = this.headerWrappers[0].ownerDocument;
                if (widths.length > 1) {
                    _widths._fixed = parseFloat(widths[0]);
                    _widths._unfixed = parseFloat(widths[1]); 
                } else _widths._unfixed = parseFloat(widths[0]);
            }
            
            /**
             * Nostal.
             */
            nostal(headerColGroup: any, bodyColGroup: any, sumColGroup: any, fixed?: any) {
                let i = _hasFixed && !fixed ? 1 : 0;
                this.headerColGroup[i] = headerColGroup.filter(c => c.style.display !== "none");
                this.bodyColGroup[i] = bodyColGroup.filter(c => c.style.display !== "none");
                this.sumColGroup[i] = sumColGroup.filter(c => c.style.display !== "none");
                this.widths = [ kt._widths._fixed, kt._widths._unfixed ];
                let agency;
                if (_hasFixed) {
                    agency = this.headerWrappers[0].querySelector("." + AGENCY);
                    if (agency) ti.remove(agency);
                }
                
                agency = this.headerWrappers[1].querySelector("." + AGENCY);
                if (agency) ti.remove(agency);
                this.fixedLines = [];
                this.lines = [];
            }
            
            /**
             * Handle.
             */
            handle() {
                let self = this;
                if (self.headerColGroup.length > 1) {
                    self.$fixedAgency = document.createElement("div"); 
                    self.$fixedAgency.className = AGENCY;
                    self.$fixedAgency.style.cssText = "; position: relative; width: " + self.widths[0];
                    let $fixedHeaderTable = self.headerWrappers[0].querySelector("table");
                    $fixedHeaderTable.insertAdjacentElement("beforebegin", self.$fixedAgency);
                    let left = 0, hiddenCount = 0;
                    
                    _.forEach(self.headerColGroup[0], ($targetCol, i) => {
                        if ($targetCol.style.display === "none") {
                            hiddenCount++;
                            return;
                        }
                        let $line = document.createElement("div");
                        $line.className = FIXED_LINE;
                        $.data($line, RESIZE_COL, $targetCol); 
                        $.data($line, RESIZE_NO, i - hiddenCount);
                        self.$fixedAgency.appendChild($line);
                        
                        left += (i === self.headerColGroup[0].length ? DISTANCE : 0) + parseFloat($targetCol.style.width);
                        $line.style.left = left + "px";
                        $line.style.height = self.height;
                        self.fixedLines.push($line);
                    });
                    
                    self.fixedHiddenCount = hiddenCount;
                    left = 0;
                    self.$agency = document.createElement("div");
                    self.$agency.className = AGENCY;
                    self.$agency.style.cssText = "; position: relative; width: " + self.widths[1];
                    let $headerTable = self.headerWrappers[1].querySelector("table");
                    $headerTable.insertAdjacentElement("beforebegin", self.$agency);
                    hiddenCount = 0;
                    _.forEach(self.headerColGroup[1], ($targetCol, i) => {
                        if (i === self.headerColGroup[1].length - 1) return;
                        if ($targetCol.style.display === "none") {
                            hiddenCount++;
                            return;
                        }
                        let $line = document.createElement("div");
                        $line.className = LINE;
                        $.data($line, RESIZE_COL, $targetCol);
                        $.data($line, RESIZE_NO, i - hiddenCount);
                        self.$agency.appendChild($line);
                        left += parseFloat($targetCol.style.width);
                        $line.style.left = left + "px";
                        $line.style.height = self.height;
                        self.lines.push($line);
                    });
                    
                    self.hiddenCount = hiddenCount;
                    self.$fixedAgency.removeXEventListener(ssk.MOUSE_DOWN);
                    self.$fixedAgency.addXEventListener(ssk.MOUSE_DOWN, self.cursorDown.bind(self));  
                    self.$agency.removeXEventListener(ssk.MOUSE_DOWN);
                    self.$agency.addXEventListener(ssk.MOUSE_DOWN, self.cursorDown.bind(self));
                } else {
                    let left = 0, hiddenCount = 0;
                    self.$agency = document.createElement("div");
                    self.$agency.className = AGENCY;
                    self.$agency.style.cssText = "; position: relative; width: " + self.widths[0];
                    let $headerTable = self.headerWrappers[0].querySelector("table");
                    $headerTable.insertAdjacentElement("beforebegin", self.$agency);
                    _.forEach(self.headerColGroup[0], ($targetCol, i) => {
                        if (i === self.headerColGroup[0].length - 1) return;
                        if ($targetCol.style.display === "none") {
                            hiddenCount++;
                            return;
                        }
                        
                        let $line = document.createElement("div");
                        $line.className = LINE;
                        $.data($line, RESIZE_COL, $targetCol);
                        $.data($line, RESIZE_NO, i - hiddenCount);
                        self.$agency.appendChild($line);
                        left += parseFloat($targetCol.style.width);
                        $line.style.left = left + "px";
                        $line.style.height = self.height;
                        self.lines.push($line);
                    });
                    
                    self.hiddenCount = hiddenCount;  
                    self.$agency.removeXEventListener(ssk.MOUSE_DOWN);
                    self.$agency.addXEventListener(ssk.MOUSE_DOWN, self.cursorDown.bind(self));
                }
            }
            
            /**
             * Cursor down.
             */
            cursorDown(event: any, trg?: any) {
                let self = this;
                if (self.actionDetails) {
                    self.unshiftRight ? self.cursorUp(event) : self.cursorUpShift(event);
                }
                let $targetGrip = event.target;
                if (!selector.is($targetGrip, "." + LINE)
                    && !selector.is($targetGrip, "." + FIXED_LINE)) return;
                let gripIndex = $.data($targetGrip, RESIZE_NO);
                let $leftCol = $.data($targetGrip, RESIZE_COL);
                let headerGroup, isFixed = false;
                if ($targetGrip.classList.contains(FIXED_LINE)) {
                    headerGroup = self.headerColGroup[0];
                    isFixed = true;
                } else if (self.headerColGroup.length > 1) {
                    headerGroup = self.headerColGroup[1];
                } else {
                    headerGroup = self.headerColGroup[0];
                }
                
                let breakArea, wrapperLeft, wrapperRight, maxWrapperRight, leftAlign;
                if (isFixed && self.headerColGroup.length > 1 && gripIndex === self.headerColGroup[0].length - 1) {
                    breakArea = true;
                }
                
                if (self.headerWrappers.length > 1) {
                    wrapperLeft = self.headerWrappers[0].style.width;
                    wrapperRight = self.headerWrappers[1].style.width;
                    maxWrapperRight = self.headerWrappers[1].style.maxWidth;
                    leftAlign = self.headerWrappers[1].style.left;
                }   
                
                let $rightCol = headerGroup[gripIndex + 1];
                let leftWidth = $leftCol.style.width;
                let rightWidth;
                if ($rightCol) rightWidth = $rightCol.style.width;
                self.actionDetails = {
                    $targetGrip: $targetGrip,
                    gripIndex: gripIndex,
                    $leftCol: $leftCol,
                    $rightCol: $rightCol,
                    xCoord: getCursorX(event),
                    isFixed: isFixed,
                    breakArea: breakArea,
                    leftAlign: parseFloat(leftAlign),
                    widths: {
                        left : parseFloat(leftWidth),
                        right: rightWidth ? parseFloat(rightWidth) : undefined,
                        wrapperLeft: parseFloat(wrapperLeft),
                        wrapperRight: parseFloat(wrapperRight),
                        maxWrapperRight: parseFloat(maxWrapperRight)
                    },
                    changedWidths: {
                        left: parseFloat(leftWidth),
                        right: rightWidth ? parseFloat(rightWidth) : undefined
                    }
                };
                
                self.$ownerDoc.addXEventListener(ssk.MOUSE_MOVE, self.unshiftRight ? self.cursorMove.bind(self) : self.cursorMoveShift.bind(self));
                self.$ownerDoc.addXEventListener(ssk.MOUSE_UP, self.unshiftRight ? self.cursorUp.bind(self) : self.cursorUpShift.bind(self));
                if (!trg) {
                    event.preventDefault();
                    event.stopPropagation();
                }
            }
            
            /**
             * Cursor move shift.
             */
            cursorMoveShift(event: any) {
                let self = this;
                if (!self.actionDetails) return;
                let evt, distance = getCursorX(event) - self.actionDetails.xCoord;
                if (distance === 0) return;
                else if (distance > 0) {
                    if (_.isNil(self.dir)) {
                        self.dir = 1;   
                    } else if (self.dir === -1) {
                        evt = { target: self.actionDetails.$targetGrip };
                        self.cursorUpShift(event);
                        evt.pageX = event.pageX;
                        self.cursorDown(evt, true);
                    }
                } else if (_.isNil(self.dir)) {
                    self.dir = -1;
                } else if (self.dir === 1) {
                    evt = { target: self.actionDetails.$targetGrip };
                    self.cursorUpShift(event);
                    evt.pageX = event.pageX;
                    self.cursorDown(evt, true);
                }
                
                let leftWidth, leftAreaWidth, rightAreaWidth, leftAlign;
                leftWidth = self.actionDetails.widths.left + distance;
                
                if (leftWidth <= 20) return;
                if (self.actionDetails.breakArea || self.actionDetails.isFixed) {
                    leftAreaWidth = self.actionDetails.widths.wrapperLeft + distance;
                    _maxFixedWidth = leftAreaWidth;
                    rightAreaWidth = self.actionDetails.widths.wrapperRight - distance;
                    leftAlign = self.actionDetails.leftAlign + distance;
                    let $header = _$grid[0].querySelector("." + FREE + "." + HEADER);
                    let sWrap = _$grid[0].querySelector("." + gp.SHEET_CLS);
                    let pWrap = _$grid[0].querySelector("." + gp.PAGING_CLS);
                    let btmw = (Math.min(parseFloat($header.style.width), parseFloat($header.style.maxWidth)) 
                        + _maxFixedWidth + ti.getScrollWidth()) + "px";
                    if (sWrap) sWrap.style.width = btmw;
                    if (pWrap) pWrap.style.width = btmw;
                }
                
                self.actionDetails.changedWidths.left = leftWidth;
                let bodyGroup, sumGroup;
                if (self.actionDetails.isFixed) {
                    bodyGroup = self.bodyColGroup[0];
                    if (self.sumWrappers.length > 0) sumGroup = self.sumColGroup[0];
                } else { 
                    let i = self.bodyColGroup.length > 1 ? 1 : 0;
                    bodyGroup = self.bodyColGroup[i];
                    self.bodyWrappers[i].style.maxWidth = (self.actionDetails.widths.maxWrapperRight + distance + ti.getScrollWidth()) + "px";
                    self.headerWrappers[i].style.maxWidth = (self.actionDetails.widths.maxWrapperRight + distance) + "px";
                    if (self.sumWrappers.length > 0) {
                        sumGroup = self.sumColGroup[i];
                        self.sumWrappers[i].style.maxWidth = (self.actionDetails.widths.maxWrapperRight + distance) + "px";
                    }
                }
                
                if (self.actionDetails.$leftCol) {
                    self.setWidth(self.actionDetails.$leftCol, leftWidth);
                    let $contentLeftCol = bodyGroup[self.actionDetails.gripIndex];
                    self.setWidth($contentLeftCol, leftWidth);
                    if (self.sumWrappers.length > 0) {
                        let $sumLeftCol = sumGroup[self.actionDetails.gripIndex];
                        self.setWidth($sumLeftCol, leftWidth);
                    }
                    
                    if (leftAreaWidth) {
                        self.setWidth(self.headerWrappers[0], leftAreaWidth);
                        self.setWidth(self.bodyWrappers[0], leftAreaWidth);
                        if (self.sumWrappers.length > 0) self.setWidth(self.sumWrappers[0], leftAreaWidth);
                        _widths._fixed = leftAreaWidth;
                    }
                }
                
                if (rightAreaWidth) {
                    self.setWidth(self.headerWrappers[1], rightAreaWidth);
                    self.setWidth(self.bodyWrappers[1], rightAreaWidth + ti.getScrollWidth());
                    self.headerWrappers[1].style.left = leftAlign + "px";
                    self.bodyWrappers[1].style.left = leftAlign + "px";
                    if (self.sumWrappers.length > 0) {
                        self.setWidth(self.sumWrappers[1], rightAreaWidth);
                        self.sumWrappers[1].style.left = leftAlign + "px";
                    }
                    _widths._unfixed = rightAreaWidth;
                }
                
                let i = self.bodyWrappers.length > 1 ? 1 : 0;
                if (!self.actionDetails.isFixed && distance < 0) {
                    let width = parseFloat(self.bodyWrappers[i].style.width),
                        maxWidth = parseFloat(self.bodyWrappers[i].style.maxWidth);
                    
                    if (maxWidth < width) {
                        let pageDiv = _$grid[0].querySelector("." + gp.PAGING_CLS),
                            sheetDiv = _$grid[0].querySelector("." + gp.SHEET_CLS),
                            btw = _maxFixedWidth + maxWidth;
                        if (pageDiv) {
                            self.setWidth(pageDiv, btw);
                        }
                        if (sheetDiv) {
                            self.setWidth(sheetDiv, btw);
                        }
                        _widths._unfixed = maxWidth - ti.getScrollWidth();
                    }
                }
 
                if (_hasFixed && distance > 0 && !self.actionDetails.isFixed) {
                    let width = parseFloat(self.bodyWrappers[1].style.width),
                        maxWidth = parseFloat(self.bodyWrappers[1].style.maxWidth),
                        pageDiv = _$grid[0].querySelector("." + gp.PAGING_CLS),
                        sheetDiv = _$grid[0].querySelector("." + gp.SHEET_CLS),
                        ws = Math.min(maxWidth, width),
                        btw = _maxFixedWidth + ws;
                    if (pageDiv && parseFloat(pageDiv.style.width) !== btw) {
                        self.setWidth(pageDiv, btw);
                    }
                    if (sheetDiv && parseFloat(sheetDiv.style.width) !== btw) {
                        self.setWidth(sheetDiv, btw);
                    }
                    _widths._unfixed = ws;
                } 
            }
            
            /**
             * Cursor up shift.
             */
            cursorUpShift(event: any) {
                let self = this;
                self.$ownerDoc.removeXEventListener(ssk.MOUSE_MOVE);
                self.$ownerDoc.removeXEventListener(ssk.MOUSE_UP);
                self.syncLines();
                let leftCol, tidx = self.actionDetails.gripIndex;
                if (!_vessel() || !_vessel().desc) {
                    self.actionDetails = null;
                    return;
                }
                 
                if (self.actionDetails.isFixed) {
                    _.forEach(_fixedHiddenColumns, c => {
                        let idx = _vessel().desc.fixedColIdxes[c];
                        if (parseFloat(idx) <= self.actionDetails.gripIndex) {
                            tidx++;
                        }
                    });
                    
                    _.forEach(_.keys(_vessel().desc.fixedColIdxes), k => {
                        let i = parseFloat(_vessel().desc.fixedColIdxes[k]);
                        if (i === tidx) {
                            leftCol = k;
                            if (self.actionDetails.breakArea || leftCol) return false;
                            return;
                        }
                    });
                    
                    replenLargeur(leftCol, self.actionDetails.changedWidths.left, "reparer");
                } else {
                    _.forEach(_hiddenColumns, c => {
                        let idx = _vessel().desc.colIdxes[c];
                        if (parseFloat(idx) <= self.actionDetails.gripIndex) {
                            tidx++;
                        }
                    });
                    
                    _.forEach(_.keys(_vessel().desc.colIdxes), k => {
                        let i = parseFloat(_vessel().desc.colIdxes[k]);
                        if (i === tidx) {
                            leftCol = k;
                            return false;
                        }
                    });
                    
                    replenLargeur(leftCol, self.actionDetails.changedWidths.left);
                }
                self.actionDetails = null;
                self.dir = null;
            }
            
            /**
             * Cursor move.
             */
            cursorMove(event: any) {
                let self = this;
                if (!self.actionDetails) return;
                let distance = getCursorX(event) - self.actionDetails.xCoord;
                if (distance === 0) return;
                let leftWidth, rightWidth, leftAreaWidth, rightAreaWidth, leftAlign;
                leftWidth = self.actionDetails.widths.left + distance;
                rightWidth = self.actionDetails.widths.right - distance;
                
                if (leftWidth <= 20 || rightWidth <= 20) return;
                if (self.actionDetails.breakArea) {
                    leftAreaWidth = self.actionDetails.widths.wrapperLeft + distance;
                    _maxFixedWidth = leftAreaWidth;
                    rightAreaWidth = self.actionDetails.widths.wrapperRight - distance;
                    leftAlign = self.actionDetails.leftAlign + distance;
                    let $header = _$grid[0].querySelector("." + FREE + "." + HEADER);
                    let sWrap = _$grid[0].querySelector("." + gp.SHEET_CLS);
                    let pWrap = _$grid[0].querySelector("." + gp.PAGING_CLS);
                    let btmw = (Math.min(parseFloat($header.style.width), parseFloat($header.style.maxWidth)) 
                        + _maxFixedWidth + ti.getScrollWidth()) + "px";
                    if (sWrap) sWrap.style.width = btmw;
                    if (pWrap) pWrap.style.width = btmw;
                }
                
                self.actionDetails.changedWidths.left = leftWidth;
                self.actionDetails.changedWidths.right = rightWidth;
                let bodyGroup, sumGroup;
                if (self.actionDetails.isFixed) {
                    bodyGroup = self.bodyColGroup[0];
                    if (self.sumWrappers.length > 0) sumGroup = self.sumColGroup[0];
                } else { 
                    bodyGroup = self.bodyColGroup[1];
                    if (self.sumWrappers.length > 0) sumGroup = self.sumColGroup[1];
                }
                
                if (self.actionDetails.$leftCol) {
                    self.setWidth(self.actionDetails.$leftCol, leftWidth);
                    let $contentLeftCol = bodyGroup[self.actionDetails.gripIndex];
                    self.setWidth($contentLeftCol, leftWidth);
                    if (self.sumWrappers.length > 0) {
                        let $sumLeftCol = sumGroup[self.actionDetails.gripIndex];
                        self.setWidth($sumLeftCol, leftWidth);
                    }
                    
                    if (leftAreaWidth) {
                        self.setWidth(self.headerWrappers[0], leftAreaWidth);
                        self.setWidth(self.bodyWrappers[0], leftAreaWidth);
                        if (self.sumWrappers.length > 0) self.setWidth(self.sumWrappers[0], leftAreaWidth);
                        _widths._fixed = leftAreaWidth;
                    }
                }
                
                if (self.actionDetails.$rightCol) {
                    self.setWidth(self.actionDetails.$rightCol, rightWidth);
                    let $contentRightCol = bodyGroup[self.actionDetails.gripIndex + 1];
                    self.setWidth($contentRightCol, rightWidth);
                    if (self.sumWrappers.length > 0) {
                        let $sumRightCol = sumGroup[self.actionDetails.gripIndex + 1];
                        self.setWidth($sumRightCol, rightWidth);
                    }
                }
                
                if (rightAreaWidth) {
                    self.setWidth(self.headerWrappers[1], rightAreaWidth);
                    self.setWidth(self.bodyWrappers[1], rightAreaWidth + ti.getScrollWidth());
                    self.headerWrappers[1].style.left = leftAlign + "px";
                    self.bodyWrappers[1].style.left = leftAlign + "px";
                    if (self.sumWrappers.length > 0) {
                        self.setWidth(self.sumWrappers[1], rightAreaWidth);
                        self.sumWrappers[1].style.left = leftAlign + "px";
                    }
                    _widths._unfixed = rightAreaWidth;
                }
            }
            
            /**
             * Cursor up.
             */
            cursorUp(event: any) {
                let self = this;
                self.$ownerDoc.removeXEventListener(ssk.MOUSE_MOVE);
                self.$ownerDoc.removeXEventListener(ssk.MOUSE_UP);
                self.syncLines();
                let leftCol, rightCol, tidx = self.actionDetails.gripIndex;
                if (!_vessel() || !_vessel().desc) {
                    self.actionDetails = null;
                    return;
                }
                 
                if (self.actionDetails.isFixed) {
                    _.forEach(_fixedHiddenColumns, c => {
                        let idx = _vessel().desc.fixedColIdxes[c];
                        if (parseFloat(idx) <= self.actionDetails.gripIndex) {
                            tidx++;
                        }
                    });
                    
                    _.forEach(_.keys(_vessel().desc.fixedColIdxes), k => {
                        let i = parseFloat(_vessel().desc.fixedColIdxes[k]);
                        if (i === tidx) {
                            leftCol = k;
                            if (self.actionDetails.breakArea || (leftCol && rightCol)) return false;
                            return;
                        }
                        
                        if (!self.actionDetails.breakArea && i === tidx + 1) {
                            rightCol = k;
                            if (leftCol && rightCol) return false;
                        }
                    });
                    
                    replenLargeur(leftCol, self.actionDetails.changedWidths.left, "reparer");
                    if (!self.actionDetails.breakArea) {
                        replenLargeur(rightCol, self.actionDetails.changedWidths.right, "reparer");
                    }
                } else {
                    _.forEach(_.keys(_vessel().desc.colIdxes), k => {
                        let i = parseFloat(_vessel().desc.colIdxes[k]);
                        if (i === self.actionDetails.gripIndex) {
                            leftCol = k;
                        } else if (i === self.actionDetails.gripIndex + 1) {
                            rightCol = k;
                        }
                        
                        if (leftCol && rightCol) return false;
                    });
                    
                    replenLargeur(leftCol, self.actionDetails.changedWidths.left);
                    if (rightCol) {
                        replenLargeur(rightCol, self.actionDetails.changedWidths.right);
                    }
                }
                self.actionDetails = null;
            }
            
            /**
             * Set width.
             */
            setWidth($col: HTMLElement, width: any) {
                $col.style.width = parseFloat(width) + "px";
            }
            
            /**
             * Sync lines.
             */
            syncLines() {
                let i, self = this; 
                if (self.actionDetails.isFixed) i = 0;
                else if (self.headerWrappers.length > 1) i = 1;
                else i = 0;
                self.$agency.style.width = self.headerWrappers[i].style.width;
                let left = 0, group = self.headerColGroup[i];
                _.forEach(group, function($td: HTMLElement, index: number) {
                    if ($td.style.display === "none" || (!self.actionDetails.isFixed && index === group.length - 1)) return;
                    left += parseFloat($td.style.width);
                    if (index < self.actionDetails.gripIndex) return;
                    if (self.unshiftRight && index > self.actionDetails.gripIndex) return false;
                    let lineArr = self.actionDetails.isFixed ? self.fixedLines : self.lines;
                    let div = lineArr[index];
                    div.style.left = left + "px";
                });
            }
        }
        
        /**
         * Get cursorX.
         */
        function getCursorX(event: any) {
            return event.pageX;
        }
        
        /**
         * ReplenLargeur.
         */
        export function replenLargeur(column: any, width: any, sht?: any) {
            let storeKey = getStoreKey(), wdec = uk.localStorage.getItem(storeKey);
            if (!wdec.isPresent()) return;
            wdec = JSON.parse(wdec.get()); 
            if (!wdec) return;
            wdec[_.isNil(sht) ? _currentSheet : sht][column] = parseFloat(width);
            uk.localStorage.setItemAsJson(storeKey, wdec);
        }
        
        /**
         * TurfSurf.
         */
        export function turfSurf(cols: Array<any>, reparer: any) {
            let newly, size, key = reparer ? "reparer" : _currentSheet, storeKey = getStoreKey(), 
                wdef = uk.localStorage.getItem(storeKey);
            
            if (!wdef.isPresent()) {
                wdef = {};
                wdef[key] = {};
                _.forEach(cols, c => {
                    wdef[key][c.key] = parseFloat(c.width); 
                });
                
                uk.localStorage.setItemAsJson(storeKey, wdef);
                return;
            }
            
            let setLargeur = function(columns, data) {
                _.forEach(columns, c => {
                    if (c.group) {
                        setLargeur(c.group, data);
                        return;
                    }
                    
                    let largeur = data[c.key];
                    if (!_.isNil(largeur)) {
                        c.width = parseFloat(largeur) + "px";
                    } else {
                        data[c.key] = parseFloat(c.width);
                        newly = true;
                    }
                });
            };
            
            let setNewLargeur = function(columns, data) {
                _.forEach(columns, c => {
                    if (c.group) {
                        setNewLargeur(c.group, data);
                    } else {
                        data[c.key] = parseFloat(c.width);
                    }
                });
            };
            
            wdef = JSON.parse(wdef.get());
            if ((size = wdef[key])) {
                setLargeur(cols, size);
                if (newly) {
                    uk.localStorage.setItemAsJson(storeKey, wdef);
                }
            } else {
                wdef[key] = {};
                setNewLargeur(cols, wdef[key]);
                uk.localStorage.setItemAsJson(storeKey, wdef);
            }
        }
        
        /**
         * ScreenLargeur.
         */
        export function screenLargeur(noRowsMin: any, noRowsMax: any) {
            if (!_headerWrappers || _headerWrappers.length === 0) return;
            let width, height = window.innerHeight - _remainHeight - parseFloat(_headerHeight), btmw;
            let pageDiv = _$grid[0].querySelector("." + gp.PAGING_CLS);
            let sheetDiv = _$grid[0].querySelector("." + gp.SHEET_CLS);
            if (_headerWrappers.length > 1) {
                width = window.innerWidth - _remainWidth - _maxFixedWidth;
                _flexFitWidth = Math.min(width + ti.getScrollWidth(), parseFloat(_bodyWrappers[1].style.maxWidth));
                btmw = _maxFixedWidth + _flexFitWidth + 2;
                _headerWrappers[1].style.width = width + "px";
                _bodyWrappers[1].style.width = (width + ti.getScrollWidth()) + "px";
                height -= ((pageDiv ? gp.PAGE_HEIGHT : 0) + (sheetDiv ? gp.SHEET_HEIGHT : 0));
                if (!_.isNil(noRowsMin) && !_.isNil(noRowsMax)) {
                    noRowsMin = parseFloat(noRowsMin);
                    noRowsMax = parseFloat(noRowsMax);
                    let size = _dataSource.length,
                        no = Math.min(Math.max(size, noRowsMin), noRowsMax);
                    height = no * BODY_ROW_HEIGHT + 19;
                }
                let vari = height - parseFloat(_bodyWrappers[0].style.height);
                if (_sumWrappers && _sumWrappers.length > 1) {
                    _sumWrappers[1].style.width = width + "px";
                    height += SUM_HEIGHT;
                    vari += SUM_HEIGHT;
                    _sumWrappers[0].style.top = (parseFloat(_sumWrappers[0].style.top) + vari) + "px";
                    _sumWrappers[1].style.top = (parseFloat(_sumWrappers[1].style.top) + vari) + "px";
                }
                if (pageDiv) {
                    pageDiv.style.width = btmw + "px";
                    pageDiv.style.top = (parseFloat(pageDiv.style.top) + vari) + "px";
                }
                if (sheetDiv) {
                    sheetDiv.style.width = btmw + "px";
                    sheetDiv.style.top = (parseFloat(sheetDiv.style.top) + vari) + "px";
                    let sheetBtn = sheetDiv.querySelector(".mgrid-sheet-buttonlist");
                    let scrollbar = sheetDiv.querySelector(".mgrid-sheet-scrollbar");
                    if (sheetBtn.offsetHeight <= gp.SHEET_HEIGHT) {
                        scrollbar.classList.add("ui-state-disabled");
                    } else scrollbar.classList.remove("ui-state-disabled"); 
                }
                _bodyWrappers[0].style.height = height + "px";
                _bodyWrappers[1].style.height = height + "px";
                return;
            }
            
            width = window.innerWidth - _remainWidth;
            btmw = Math.min(width + ti.getScrollWidth(), parseFloat(_bodyWrappers[0].style.maxWidth));
            _flexFitWidth = btmw;
            _headerWrappers[0].style.width = width + "px";
            _bodyWrappers[0].style.width = (width + ti.getScrollWidth()) + "px";
            height -= ((pageDiv ? gp.PAGE_HEIGHT : 0) + (sheetDiv ? gp.SHEET_HEIGHT : 0));
            if (!_.isNil(noRowsMin) && !_.isNil(noRowsMax)) {
                noRowsMin = parseFloat(noRowsMin);
                noRowsMax = parseFloat(noRowsMax);
                let size = _dataSource.length,
                    no = Math.min(Math.max(size, noRowsMin), noRowsMax);
                height = no * BODY_ROW_HEIGHT + 19;
            }
            let vari = height - parseFloat(_bodyWrappers[0].style.height);
            if (_sumWrappers && _sumWrappers.length > 0) {
                _sumWrappers[0].style.width = width + "px";
                height += SUM_HEIGHT;
                vari += SUM_HEIGHT;
                _sumWrappers[0].style.top = (parseFloat(_sumWrappers[0].style.top) + vari) + "px";
            }
            if (pageDiv) {
                pageDiv.style.width = btmw + "px";
                pageDiv.style.top = (parseFloat(pageDiv.style.top) + vari) + "px";
            }
            if (sheetDiv) {
                sheetDiv.style.width = btmw + "px";
                sheetDiv.style.top = (parseFloat(sheetDiv.style.top) + vari) + "px";
            }
            _bodyWrappers[0].style.height = height + "px";
        }
        
        /**
         * Get storeKey.
         */
        function getStoreKey() {
            return request.location.current.rawUrl + "/" + _$grid.attr("id");
        }
    }
    
    module lo {
        export const MPART = "mPart";
        export const VIEW = "mView";
        export const LAST_SELECT = "mLastSelect";
        export const DESC = "mDescription";
        export const CBX_SELECTED = "selectedValue";
        export const CBX_SELECTED_TD = "code";
        export const CBX_ITEM_VALUE = "value";
        
        $.widget("md.mGrid", {
            options: {},
            _create: function() {    
            },
            directEnter: function(direct) {
                this.element.data("enterDirect", direct);
            },
            dataSource: function() {
                return _.cloneDeep(_dataSource);
            },
            errors: function() {
                return _.cloneDeep(_errors);
            },
            disableNtsControlAt: function(id, key, $cell) {
                if (!$cell) {
                    let idx = _.findIndex(_dataSource, r => r[_pk] === id);
                    if (_.isNil(idx)) return;
                    $cell = lch.cellAt(_$grid[0], idx, key);
                }
                if (_.isNil($cell) || $cell.classList.contains(color.Disable)) return;
                $cell.classList.add(color.Disable);
                switch (dkn.controlType[key]) {
                    case dkn.LINK_LABEL:
                        let link = $cell.querySelector(".mlink-button");
                        if (link) {
                            link.removeXEventListener(ssk.CLICK_EVT);
                            link.style.color = "#333";
                            link.style.cursor = "default";
                        }
                        break;
                    case dkn.BUTTON:
                    case dkn.DELETE_BUTTON:
                        let btn = $cell.querySelector(".mbutton");
                        if (btn) btn.disabled = true;
                        break;
                    case dkn.FLEX_IMAGE:
                        let img = $cell.querySelector("span");
                        if (img) {
                            img.removeXEventListener(ssk.CLICK_EVT);
                            img.style.cursor = "default";
                        }
                        break;
                    case dkn.CHECKBOX:
                        let check = $cell.querySelector("input");
                        if (check) {
                            check.setAttribute("disabled", "disabled");
                        }
                        break;
                }
                
                color.pushState(id, key, color.Disable);
            },
            enableNtsControlAt: function(id, key, $cell) {
                if (!$cell) {
                    let idx = _.findIndex(_dataSource, r => r[_pk] === id);
                    if (_.isNil(idx)) return;
                    $cell = lch.cellAt(_$grid[0], idx, key);
                }
                if (_.isNil($cell) || !$cell.classList.contains(color.Disable)) return;
                $cell.classList.remove(color.Disable);
                switch (dkn.controlType[key]) {
                    case dkn.LINK_LABEL:
                        let link = $cell.querySelector(".mlink-button");
                        if (link) {
                            link.addXEventListener(ssk.CLICK_EVT, $.data(link, ssk.CLICK_EVT));
                            link.style.color = "#0066CC";
                            link.style.cursor = "pointer";
                        }
                        break;
                    case dkn.BUTTON:
                    case dkn.DELETE_BUTTON:
                        let btn = $cell.querySelector(".mbutton");
                        if (btn) {
                            btn.disabled = false;
                            let hdl = $.data(btn, ssk.CLICK_EVT);
                            if (hdl) {
                                btn.removeXEventListener(ssk.CLICK_EVT);
                                btn.addXEventListener(ssk.CLICK_EVT, hdl);
                            }
                        }
                        break;
                    case dkn.FLEX_IMAGE:
                        let img = $cell.querySelector("span");
                        if (img) {
                            img.addXEventListener(ssk.CLICK_EVT, $.data(img, ssk.CLICK_EVT));
                            img.style.cursor = "pointer";
                        }
                        break;
                    case dkn.CHECKBOX:
                        let check = $cell.querySelector("input");
                        if (check) {
                            check.removeAttribute("disabled");
                        }
                        break;
                }
                
                color.popState(id, key, color.Disable);
            },
            setState: function(id, key, states) {
                let self = this;
                let idx = _.findIndex(_dataSource, r => r[_pk] === id);
                if (_.isNil(idx)) return;
                let $cell = lch.cellAt(_$grid[0], idx, key);
                let ftPrint = false, 
                    setShtCellState = function($c) {
                    let disabled;
                    _.forEach(states, s => {
                        if (s === color.Disable) {
                            self.disableNtsControlAt(id, key, $c);
                            disabled = true;
                            return;
                        }
                        if (!$c.classList.contains(s))
                            $c.classList.add(s);
                    });
                    
                    if (disabled) _.remove(states, s => s === color.Disable);
                    color.pushState(id, key, states);  
                    if (!ftPrint) ftPrint = true;   
                };
                
                if ($cell) {
                    setShtCellState($cell);
                }
                _.forEach(_.keys(_mafollicle[SheetDef]), s => {
                    if (s === _currentSheet) return;
                    let tidx, maf = _mafollicle[_currentPage][s];
                    if (maf && maf.desc && maf.desc.fixedColIdxes 
                        && !_.isNil(tidx = maf.desc.fixedColIdxes[key])) {
                        $cell = maf.desc.fixedRows[idx][tidx];
                        if ($cell) setShtCellState($cell);
                    } else if (maf && maf.desc && maf.desc.colIdxes
                        && !_.isNil(tidx = maf.desc.colIdxes[key])) {
                        $cell = maf.desc.rows[idx][tidx];
                        if ($cell) setShtCellState($cell);
                    } 
                });
                
                if (!ftPrint) color.pushState(id, key, states); 
            },
            clearState: function(idArr: Array<any>) {
                let self = this;
                
                let cleanOthShtCellElm = function(id, c) {
                    let coord = ti.getCellCoord(c);
                    color.ALL.forEach(s => {
                        if (c.classList.contains(s)) {
                            if (s === color.Disable) {
                                self.enableNtsControlAt(id, coord.columnKey, c);
                            }
                            c.classList.remove(s);    
                        }
                    });
                    
                    color.popState(id, coord.columnKey, color.ALL); 
                };
                
                let clean = function(id) {
                    let idx = _.findIndex(_dataSource, r => r[_pk] === id); 
                    let row = lch.rowAt(_$grid[0], idx);
                    _.forEach(row, c => {
                        cleanOthShtCellElm(id, c);
                    });
                    
                    _.forEach(_.keys(_mafollicle[SheetDef]), s => {
                        if (s === _currentSheet) return;
                        let maf = _mafollicle[_currentPage][s];
                        if (maf && maf.desc) {
                            let othShtRow = lch.rowAt(_$grid[0], idx, maf.desc); 
                            _.forEach(othShtRow, c => {
                                cleanOthShtCellElm(id, c);
                            }); 
                        } else cleanOthSht(id, _mafollicle[SheetDef][s].columns);
                    });
                };
                
                let cleanOthSht = function(id, cols) {
                    _.forEach(cols, c => {
                        if (c.group) {
                            cleanOthSht(id, c.group);
                            return;
                        }
                        color.popState(id, c.key, color.ALL);
                    });
                };
                
                if (idArr && !_.isArray(idArr)) {
                    clean(idArr);
                    return;
                }
                
                _.forEach(idArr, id => {
                    clean(id);
                });
            },
            hideZero: function(val) {
                if (changeZero(val)) {
                    _zeroHidden = val;
                    if (_vessel()) _vessel().zeroHidden = val;
                }
            },
            updatedCells: function(all) {
                let arr = [];
                let toNumber = false, column = _columnsMap[_pk];
                if ((column && _.toLower(column[0].dataType) === "number")
                    || _.toLower(_pkType) === "number") {
                    toNumber = true;
                }
                
                _.forEach(Object.keys(_dirties), r => {
                    _.forEach(Object.keys(_dirties[r]), c => {
                        arr.push({ rowId: (toNumber ? parseFloat(r) : r), columnKey: c, value: _dirties[r][c] });
                    });
                });
                
                if (all) {
                    _.forEach(_.keys(_mafollicle[SheetDef]), k => {
                        if (k === _currentSheet) return;
                        let maf = _mafollicle[_currentPage][k];
                        if (!maf || !maf.dirties) return;
                        _.forEach(_.keys(maf.dirties), r => {
                            _.forEach(_.keys(maf.dirties[r]), c => {
                                arr.push({ rowId: (toNumber ? parseFloat(r) : r), columnKey: c, value: maf.dirties[r][c] });
                            });
                        });
                    });    
                }
                
                return arr;
            },
            showColumn: function(col) {
                if (!_vessel() || !_vessel().desc) return;
                let $col, i = _vessel().desc.fixedColIdxes[col];
                if (_.isNil(i)) return;
                let hCols, bCols, sCols, header = _$grid[0].querySelector("." + FIXED + "." + HEADER);
                if (header) {
                    hCols = header.querySelectorAll("col");
                    $col = hCols[i];
                    if ($col && $col.style.display === "none") {
                        $col.style.display = "";
                    }
                    
                    let headerCols = header.querySelectorAll("td");
                    $col = headerCols[i];
                    if ($col && $col.style.display === "none") {
                        $col.style.display = "";
                    }
                    _.remove(_fixedHiddenColumns, c => c === col);
                }
                
                let body = _$grid[0].querySelector("." + FIXED + ":not(." + HEADER + ")");
                if (body) {
                    bCols = body.querySelectorAll("col");
                    $col = bCols[i];
                    if ($col && $col.style.display === "none") {
                        $col.style.display = "";
                    }
                    _.forEach(_vessel().desc.fixedRows, r => {
                        let a = r[i];
                        if (a && a.style.display === "none") {
                            a.style.display = "";
                        }
                    });
                    
                    let colWidth = parseFloat($col.style.width);
                    _maxFixedWidth += colWidth;
                    kt._widths._fixed = _maxFixedWidth;
                    _.forEach(_.slice(_$grid[0].querySelectorAll("." + FREE)), t => {
                        if (!t) return;
                        let width = parseFloat(t.style.width),
                            left = parseFloat(t.style.left);
                        t.style.width = (width - colWidth) + "px";
                        t.style.left = (left + colWidth) + "px";
                    });
                    
                    let sum = _$grid[0].querySelector("." + FIXED + "-summaries");
                    if (sum) {
                        sCols = sum.querySelectorAll("col");
                        $col = sCols[i];
                        if ($col && $col.style.display === "none") {
                            $col.style.display = "";
                        }
                        
                        let cols = sum.querySelectorAll("td");
                        $col = cols[i];
                        if ($col && $col.style.display === "none") {
                            $col.style.display = "";
                        }
                        let dSum = _$grid[0].querySelector("." + FREE + "-summaries");
                        if (dSum) {
                            let width = parseFloat(dSum.style.width),
                                left = parseFloat(dSum.style.left);
                            dSum.style.width = (width - colWidth) + "px";
                            dSum.style.left = (left + colWidth) + "px";
                        }
                        sum.style.width = _maxFixedWidth + "px";
                    }
                    header.style.width = _maxFixedWidth + "px";
                    body.style.width = _maxFixedWidth + "px";
                    kt._adjuster.nostal(_.slice(hCols), _.slice(bCols), _.slice(sCols), true);
                    kt._adjuster.handle();
                    _cloud.painter.painters[0].bubColumn(col, i);
                }
            },
            hideColumn: function(col) {
                if (!_vessel() || !_vessel().desc) return;
                let $col, i = _vessel().desc.fixedColIdxes[col];
                if (_.isNil(i)) return;
                let hCols, bCols, sCols, header = _$grid[0].querySelector("." + FIXED + "." + HEADER);
                if (header) {
                    hCols = header.querySelectorAll("col");
                    $col = hCols[i];
                    if ($col && $col.style.display !== "none") {
                        $col.style.display = "none";
                    }
                    
                    let headerCols = header.querySelectorAll("td");
                    $col = headerCols[i];
                    if ($col && $col.style.display !== "none") {
                        $col.style.display = "none";
                    }
                    _fixedHiddenColumns.add(col);
                }
                
                let body = _$grid[0].querySelector("." + FIXED + ":not(." + HEADER + ")");
                if (body) {
                    bCols = body.querySelectorAll("col");
                    $col = bCols[i];
                    if ($col && $col.style.display !== "none") {
                        $col.style.display = "none";
                    }
                    _.forEach(_vessel().desc.fixedRows, r => {
                        let a = r[i];
                        if (a && a.style.display !== "none") {
                            a.style.display = "none";
                        }
                    });
                    
                    let colWidth = parseFloat($col.style.width);
                    _maxFixedWidth -= colWidth;
                    kt._widths._fixed = _maxFixedWidth;
                    _.forEach(_.slice(_$grid[0].querySelectorAll("." + FREE)), t => {
                        if (!t) return;
                        let width = parseFloat(t.style.width),
                            left = parseFloat(t.style.left);
                        t.style.width = (width + colWidth) + "px";
                        t.style.left = (left - colWidth) + "px";
                    });
                    
                    let sum = _$grid[0].querySelector("." + FIXED + "-summaries");
                    if (sum) {
                        sCols = sum.querySelectorAll("col");
                        $col = sCols[i];
                        if ($col && $col.style.display !== "none") {
                            $col.style.display = "none";
                        }
                        
                        let cols = sum.querySelectorAll("td");
                        $col = cols[i];
                        if ($col && $col.style.display !== "none") {
                            $col.style.display = "none";
                        }
                        let dSum = _$grid[0].querySelector("." + FREE + "-summaries");
                        if (dSum) {
                            let width = parseFloat(dSum.style.width),
                                left = parseFloat(dSum.style.left);
                            dSum.style.width = (width + colWidth) + "px";
                            dSum.style.left = (left - colWidth) + "px";
                        }
                        sum.style.width = _maxFixedWidth + "px";
                    }
                    header.style.width = _maxFixedWidth + "px";
                    body.style.width = _maxFixedWidth + "px";
                    kt._adjuster.nostal(_.slice(hCols), _.slice(bCols), _.slice(sCols), true);
                    kt._adjuster.handle();
                    _cloud.painter.painters[0].unbubColumn(col, i);
                }
            },
            updateCell: function(id, key, val, reset, ackDis) {
                let idx = _.findIndex(_dataSource, r => r[_pk] === id);
                if (_.isNil(idx)) return;
                let $cell = lch.cellAt(_$grid[0], idx, key);
                if (_.isNil($cell)) {
                    if (dkn.controlType[key] === dkn.TEXTBOX) {
                        let col = _columnsMap[key];
                        if (!col || col.length === 0) return;
                        su.wedgeCell(_$grid[0], { rowIdx: idx, columnKey: key }, su.formatSave(col[0], val), reset);
                    } else su.wedgeCell(_$grid[0], { rowIdx: idx, columnKey: key }, val, reset);
                    return idx;
                }
                if ((!ackDis && $cell.classList.contains(color.Disable))) return idx;
                if (dkn.controlType[key] === dkn.TEXTBOX) {
                    let col = _columnsMap[key];
                    if (!col || col.length === 0) return;
                    val = String(val);
                    let formatted = su.format(col[0], val);
                    $cell.innerHTML = formatted;
                    let disFormat = su.formatSave(col[0], val);
                    su.wedgeCell(_$grid[0], { rowIdx: idx, columnKey: key }, disFormat, reset);
                    $.data($cell, v.DATA, disFormat);
                } else if (dkn.controlType[key] === dkn.CHECKBOX) {
                    let check = $cell.querySelector("input[type='checkbox']");
                    if (!check) return;
                    if (val) { //&& check.getAttribute("checked") !== "checked") {
                        check.setAttribute("checked", "checked");
                        check.checked = true;
                        let evt = document.createEvent("HTMLEvents");
                        evt.initEvent("change", false, true);
                        evt.resetValue = reset;
                        evt.checked = val;
                        check.dispatchEvent(evt);
                    } else if (!val) { // && check.getAttribute("checked") === "checked") {
                        check.removeAttribute("checked");
                        check.checked = false;
                        let evt = document.createEvent("HTMLEvents");
                        evt.initEvent("change", false, true);
                        evt.resetValue = reset;
                        evt.checked = val;
                        check.dispatchEvent(evt);
                    }
                } else if (dkn.controlType[key] === dkn.LINK_LABEL) {
                    let link = $cell.querySelector("a");
                    link.innerHTML = val;
                    su.wedgeCell(_$grid[0], { rowIdx: idx, columnKey: key }, val, reset);
                } else if (dkn.controlType[key] === dkn.FLEX_IMAGE) {
                    let $image;
                    if (!_.isNil(val) && val !== "") {
                        let controlDef, controlMap = _mafollicle[SheetDef][_currentSheet].controlMap;
                        if (!controlMap || !(controlDef = controlMap[key])) return;
                        $image = document.createElement("span");
                        $image.className = controlDef.source;
                        if (controlDef.click && _.isFunction(controlDef.click)) {
                            let clickHandle = controlDef.click.bind(null, key, id);
                            $image.addXEventListener(ssk.CLICK_EVT, clickHandle);
                            $image.style.cursor = "pointer";
                            $.data($image, ssk.CLICK_EVT, clickHandle);
                        }
                        
                        $cell.innerHTML = "";
                        $cell.appendChild($image);
                    } else {
                        $image = $cell.querySelector("span");
                        if ($image) ti.remove($image);
                    }
                    su.wedgeCell(_$grid[0], { rowIdx: idx, columnKey: key }, val, reset);
                } else if (dkn.controlType[key] === dkn.LABEL) {
                    $cell.innerHTML = val;
                    su.wedgeCell(_$grid[0], { rowIdx: idx, columnKey: key }, val, reset);
                    $.data($cell, v.DATA, val);
                } else {
                    let cbx = dkn.controlType[key];
                    if (_.isObject(cbx) && cbx.type === dkn.COMBOBOX) {
                        let sel = _.find(cbx.options, o => o.code === val);
                        if (sel) { 
                            su.wedgeCell(_$grid[0], { rowIdx: idx, columnKey: key }, val, reset);
                            $.data($cell, lo.CBX_SELECTED_TD, val);
                            $cell.textContent = sel ? sel.name : ""; 
                        }
                    }
                }
                
                return idx;
            },
            checkAll: function(key, fixed) {
                let idxes, rows; 
                if (fixed) {
                    idxes = _vessel().desc.fixedColIdxes;
                } else {
                    idxes = _vessel().desc.colIdxes;
                }
                
                let i = idxes[key];
                if (_.isNil(i)) return;
                _.forEach(_.keys(_mafollicle), k => {
                    if (k === SheetDef) return;
                    let st = _mafollicle[k][_currentSheet];
                    if (!st) return;
                    _.forEach(st.desc[fixed ? "fixedRows" : "rows"], r => {
                        if (!r) return;
                        let cell = r[i];
                        if (cell) {
                            let check = cell.querySelector("input[type='checkbox']");
                            if (!cell.classList.contains(color.Disable) && check 
                                && check.getAttribute("checked") !== "checked") {
                                check.setAttribute("checked", "checked");
                                check.checked = true;
                                let evt = document.createEvent("HTMLEvents");
                                evt.initEvent("change", false, true);
                                evt.checked = true;
                                check.dispatchEvent(evt);
                            }
                        }
                    });
                });
                
                dkn.allCheck[key] = true;
            },
            uncheckAll: function(key, fixed) {
                let idxes, rows; 
                if (fixed) {
                    idxes = _vessel().desc.fixedColIdxes;
                } else {
                    idxes = _vessel().desc.colIdxes;
                }
                
                let i = idxes[key];
                if (_.isNil(i)) return;
                _.forEach(_.keys(_mafollicle), k => {
                    if (k === SheetDef) return;
                    let st = _mafollicle[k][_currentSheet];
                    if (!st) return;
                    _.forEach(st.desc[fixed ? "fixedRows" : "rows"], r => {
                        if (!r) return;
                        let cell = r[i];
                        if (cell) {
                            let check = cell.querySelector("input[type='checkbox']");
                            if (!cell.classList.contains(color.Disable) && check 
                                && check.getAttribute("checked") === "checked") {
                                check.removeAttribute("checked");
                                check.checked = false;
                                let evt = document.createEvent("HTMLEvents");
                                evt.initEvent("change", false, true);
                                evt.checked = false;
                                check.dispatchEvent(evt);
                            }
                        }
                    });
                });
                
                dkn.allCheck[key] = false;
            },
            headerText: function(key, text, parent) {
                let rename = function(cols) {
                    let ret;
                    _.forEach(cols, c => {
                        if (parent) {
                            if (c.group && c.headerText === key) {
                                c.headerText = text;
                                ret = c;
                                return false;
                            }
                            return;
                        }
                        
                        if (c.group && (ret = rename(c.group, key, text))) {
                            return false;    
                        }
                        
                        if (c.key === key) {
                            c.headerText = text;
                            ret = c;
                            return false;
                        }
                    });
                    
                    return ret;
                }
                
                let found;
                _.forEach(_.keys(_mafollicle[SheetDef]), k => {
                    let cols = _mafollicle[SheetDef][k].columns;
                    if (cols) {
                        found = rename(cols);
                    }
                });
                
                let colspan, tdList = _$grid[0].querySelectorAll("." + HEADER + "." + FREE + " td");
                let replace = function(td) {
                    let done, coord = ti.getCellCoord(td);
                    colspan = td.getAttribute("colspan");
                    if (!parent && coord && coord.columnKey === key) {
                        td.innerHTML = text;
                        done = true;
                    } else if (parent && !_.isNil(colspan) && td.textContent === key) {
                        td.innerHTML = text;
                        done = true;
                    }
                    
                    return done;
                };
                
                _.forEach(tdList, td => {
                    if (replace(td)) return false;
                });
                
                _.forEach(_.keys(_mafollicle), k => {
                    if (k === SheetDef) return;
                    _.forEach(_.keys(_mafollicle[k]), s => {
                        if (s === _currentSheet) return;
                        let body;
                        if ((body = _mafollicle[k][s].$hBody)) {
                            _.forEach(body.querySelectorAll("td"), td => {
                                if (replace(td)) return false;
                            });
                        }
                    });
                });
            },
            getCellValue: function(id, key) {
                let idx = _.findIndex(_dataSource, r => r[_pk] === id);
                if (_.isNil(idx)) return;
                return _dataSource[idx][key];
            },
            selectedSheet: function() {
                return _currentSheet;
            },
            selectedPage: function() {
                return _currentPage;
            },
            columnWidth: function(col, stt) {
                let item = uk.localStorage.getItem(request.location.current.rawUrl + "/" + _$grid.attr("id"));  
                if (item.isPresent() && col) {
                    let obj = JSON.parse(item.get()); 
                    let w = obj[stt ? "reparer" : _currentSheet][col]; 
                    return !_.isNil(w) ? w : -1;
                }
                return -1;
            },
            destroy: function() {
                _maxFixedWidth = 0; _maxFreeWidth = null; _columnsMap = {}; _dataSource = null; _secColumn = {};
                _hasFixed = null; _validators = {}; _mDesc = null; _mEditor = null; _cloud = null;
                _hr = null; _direction = null; _errors = []; _errorColumns = null; _errorsOnPage = null;
                _$grid = null; _pk = null; _pkType = null; _summaries = null; _objId = null; _getObjId = null; dkn.allCheck = {};
                _hasSum = null; _pageSize = null; _currentPage = null; _currentSheet = null; _start = null; _end = null; 
                _headerHeight = null; _zeroHidden = null; _paging = false; _sheeting = false; _copie = false; _mafollicle = {}; 
                _specialColumn = {}; _specialLinkColumn = {}; _fixedHiddenColumns = []; _hiddenColumns = []; _fixedColumns = null; _selected = {}; _dirties = {}; 
                _rid = {}, _headerWrappers = null; _bodyWrappers = null; _sumWrappers = null; _fixedControlMap = {}; _cellStates = null; _features = null; 
                _leftAlign = null; _header = null; _flexFitWidth = null; this.element.html(""); this.element.removeData(); _histoire = [];
                this.element[0].parentNode.replaceChild(this.element[0].cloneNode(), this.element[0]);
            }  
        });
        
        /**
         * Change zero.
         */
        export function changeZero(hide: any) {
            let ves, desc, realVal;
            if (_zeroHidden === hide) return false;
            if ((ves = _vessel()) && (desc = ves.desc)) {
                _.forEach(desc.rows, r => {
                    _.forEach(r, c => {
                        let key = ti.getCellCoord(c).columnKey;
                        let control = dkn.controlType[key];
                        if (control !== dkn.TEXTBOX) return;
                        let content = $.data(c, v.DATA);
                        if (hide && ti.isZero(content, key)) {
                            c.textContent = "";
                        } else if (!hide && c.textContent === "" && content !== "") {
                            let format = su.format(_columnsMap[key][0], content);
                            c.textContent = format;
                        }
                    });
                });
            }
            
            return true;
        }
    }
    
    module su {
        
        export const EDITOR = "meditor";
        export let _copieMode;
        export let afterCollertar;
        
        /**
         * Binding.
         */
        export function binding($grid: HTMLElement, fitWindow: any, noRowsMin: any, noRowsMax: any) {
            
            $grid.addXEventListener(ssk.MOUSE_DOWN, evt => {
                let $tCell = evt.target;
                if (!$tCell || !selector.is($tCell, "." + v.CELL_CLS)
                    || $tCell.classList.contains(color.Disable)
                    || $tCell.classList.contains(dkn.LABEL_CLS)) return;
                
                let coord = ti.getCellCoord($tCell);
                let control = dkn.controlType[coord.columnKey];
                let cEditor = _mEditor;
                
                if (!control || ti.isEqual(coord, cEditor, [ "rowIdx", "columnKey" ])
                    || (control === dkn.TEXTBOX && !$tCell.classList.contains(lch.CELL_SELECTED_CLS))) return;
                let $editor = dkn.controlType[dkn.TEXTBOX].my;
                let $input = $editor.querySelector("input.medit");
                let cType = {};
                if (control === dkn.TEXTBOX && $tCell.classList.contains(lch.CELL_SELECTED_CLS)) {
                    endEdit($grid);
                    if ($tCell.classList.contains(hpl.CURRENCY_CLS)) {
                        $tCell.classList.remove(hpl.CURRENCY_CLS);
                        $editor.classList.add(hpl.CURRENCY_CLS);
                    }
                    
                    $tCell.textContent = "";
                    $tCell.classList.add(dkn.CONTROL_CLS);
                    $tCell.appendChild($editor);
                    let data = $.data($tCell, v.DATA);
                    $input.value = !_.isNil(data) ? data : "";
                    cType.type = dkn.TEXTBOX;
                    setTimeout(() => {
                        $input.select();
                    }, 0);
                    
                    let coord = ti.getCellCoord($tCell);
                    $input.style.imeMode = "inactive";
                    if (coord) {
                        let column = _columnsMap[coord.columnKey];
                        if (column && column[0].japanese) {
                            $input.style.imeMode = "active";
                        }
                    }
                } else if (control.type === dkn.COMBOBOX && !$tCell.querySelector(".mcombo-wrapper")) {
                    endEdit($grid);
                    $tCell.textContent = "";
                    $tCell.classList.add(dkn.CONTROL_CLS);
                    let $combo = control.my.querySelector("." + dkn.CBX_CLS);
                    let $comboValue = control.my.querySelector(".mcombo-value");
                    let items = control.dropdown.querySelectorAll(".mcombo-listitem");
                    let code = $.data($tCell, lo.CBX_SELECTED_TD);
                    _.forEach(items, i => {
                        let value = $.data(i, lo.CBX_ITEM_VALUE);
                        
                        if (i.classList.contains("selecteditem")) {
                            i.classList.remove("selecteditem");
                        }
                        
                        if (code === value) {
                            let $item = i.querySelector(".mcombo-item");
                            if ($item) {
                                $comboValue.innerHTML = "";
                                $comboValue.appendChild($item.cloneNode(true));
                                i.classList.add("selecteditem");
                            }
                        }
                    });
                    
                    $tCell.appendChild(control.my);
                    $.data(control.my, lo.CBX_SELECTED, code);
                    dkn.openDD(control.dropdown, control.my);
                    $combo.classList.add(dkn.CBX_ACTIVE_CLS);
                    cType.type = dkn.COMBOBOX;
                } else if (control === dkn.FLEX_IMAGE || control === dkn.CHECKBOX
                        || control === dkn.LINK_LABEL || control === dkn.SWITCH_BUTTONS) {
                    endEdit($grid);
                }
                
                _mEditor = _.assignIn(coord, cType);
                evt.stopPropagation();
            });
            
            document.addXEventListener(ssk.MOUSE_DOWN, evt => {
                if (!evt.target) return;
                if (!selector.is(evt.target, "input.medit")
                    && !selector.is(evt.target, "div[class*='mcombo']")) {
                    endEdit($grid);
                }
            });
            
            $grid.addXEventListener(ssk.KEY_DOWN, evt => {
                let $grid = evt.currentTarget, $tCell = evt.target;
                if (!$grid) return;
                if (ti.isEnterKey(evt)) {
                    let direct = $.data($grid, "enterDirect");
                    if (evt.shiftKey) {
                        lch.selectPrev($grid, direct);
                    } else {
                        lch.selectNext($grid, direct);        
                    }
                } else if (ti.isTabKey(evt)) {
                    evt.preventDefault();
                    if (evt.shiftKey) {
                        lch.selectPrev($grid);
                    } else {
                        lch.selectNext($grid);
                    }
                } else if (ti.isArrowLeft(evt)) {
                    evt.preventDefault();
                    lch.selectPrev($grid);
                } else if (ti.isArrowRight(evt)) {
                    evt.preventDefault();
                    lch.selectNext($grid);
                } else if (ti.isArrowUp(evt)) {
                    evt.preventDefault();
                    lch.selectPrev($grid, "below");
                } else if (ti.isArrowDown(evt)) {
                    evt.preventDefault();
                    lch.selectNext($grid, "below");
                }
                
                // Get input
                if (!evt.ctrlKey && ti.isAlphaNumeric(evt) || ti.isMinusSymbol(evt) || ti.isDeleteKey(evt)
                    || evt.keyCode === 113 || ti.isSpaceKey(evt)) {
                    if (!$tCell || !selector.is($tCell, "." + v.CELL_CLS)
                        || $tCell.classList.contains(color.Disable)
                        || $tCell.classList.contains(dkn.LABEL_CLS)) return;
                    let coord = ti.getCellCoord($tCell);
                    let control = dkn.controlType[coord.columnKey];
                    let cEditor = _mEditor;
                    
                    if (control === dkn.CHECKBOX && ti.isSpaceKey(evt)) {
                        let check = $tCell.querySelector("input[type='checkbox']");
                        if (!check) return;
                        let checked;
                        if (check.getAttribute("checked") === "checked") {
                            check.removeAttribute("checked");
                            check.checked = checked = false;
                        } else {
                            check.setAttribute("checked", "checked");
                            check.checked = checked = true;
                        }
                        let changeEvt = document.createEvent("HTMLEvents");
                        changeEvt.initEvent("change", false, true);
                        changeEvt.checked = checked;
                        check.dispatchEvent(changeEvt);
                        evt.preventDefault();
                        return;
                    }
                    
                    if (!control || ti.isEqual(coord, cEditor, [ "rowIdx", "columnKey" ])
                        || (control === dkn.TEXTBOX && !$tCell.classList.contains(lch.CELL_SELECTED_CLS))) return;
                    let $editor = dkn.controlType[dkn.TEXTBOX].my;
                    let $input = $editor.querySelector("input.medit");
                    let cType = {};
                    if (control === dkn.TEXTBOX && $tCell.classList.contains(lch.CELL_SELECTED_CLS)) {
                        endEdit($grid);
                        if ($tCell.classList.contains(hpl.CURRENCY_CLS)) {
                            $tCell.classList.remove(hpl.CURRENCY_CLS);
                            $editor.classList.add(hpl.CURRENCY_CLS);
                        }
                        
                        $tCell.textContent = "";
                        $tCell.classList.add(dkn.CONTROL_CLS);
                        $tCell.appendChild($editor);
//                        $input.value = evt.key;
                        if (ti.isDeleteKey(evt) && cEditor === null) {
                            $input.value = "";
                        } else if (evt.keyCode === 113) {
                            let data = $.data($tCell, v.DATA);
                            $input.value = !_.isNil(data) ? data : "";
                            $input.select();
                        }
                        
                        cType.type = dkn.TEXTBOX;
                        let coord = ti.getCellCoord($tCell);
                        $input.style.imeMode = "inactive";
                        if (coord) {
                            let column = _columnsMap[coord.columnKey];
                            if (column && column[0].japanese) {
                                $input.style.imeMode = "active";
                            }
                        }
                        
                        $input.focus();
                    }
                    
                    _mEditor = _.assignIn(coord, cType);
                }
                
                if (!_copie) return;
                if (evt.ctrlKey && evt.keyCode === 86 && _collerer) {
                    afterCollertar = document.activeElement;
                    _collerer.focus();
                } else if (evt.ctrlKey && evt.keyCode === 67) {
                    copieData();
                } else if (evt.ctrlKey && evt.keyCode === 88) {
                    copieData(true);
                } else if (evt.ctrlKey && evt.keyCode === 90) {
                    annuler();
                }
            });
            
            if (_copie) {
                $grid.addXEventListener(ssk.FOCUS_IN, evt => {
                    if (_collerer && _copieer) return;
                    _collerer = document.createElement("textarea");
                    _collerer.setAttribute("id", "mgrid-collerer");
                    _collerer.style.opacity = "0";
                    _collerer.style.overflow = "hidden";
                    _collerer.addXEventListener(ssk.PASTE, collerData.bind(null));
                    _copieer = document.createElement("textarea"); 
                    _copieer.setAttribute("id", "mgrid-copieer");
                    _copieer.style.opacity = "0";
                    _copieer.style.overflow = "hidden";
                    let $div = document.createElement("div");
                    $div.style.position = "fixed";
                    $div.style.top = "-10000px";
                    $div.style.left = "-10000px";
                    document.body.appendChild($div);
                    $div.appendChild(_collerer);
                    $div.appendChild(_copieer);
                });
            }
            
            if (fitWindow) {
                window.addXEventListener(ssk.RESIZE, evt => {
                    kt.screenLargeur(noRowsMin, noRowsMax);
                });
            }
        }
        
        /**
         * EndEdit.
         */
        export function endEdit($grid: HTMLElement) {
            let editor = _mEditor;
            if (!editor) return;
            let $bCell = lch.cellAt($grid, editor.rowIdx, editor.columnKey);
            if (editor.type === dkn.TEXTBOX) {
                let $editor = dkn.controlType[dkn.TEXTBOX].my;
                let $input = $editor.querySelector("input.medit");
                let inputVal = $input.value;
                if ($bCell) {
                    let spl = {}, column = _columnsMap[editor.columnKey];
                    if (!column) return;
                    let failed = khl.any({ element: $bCell }), 
                        formatted = failed ? inputVal : (_zeroHidden && ti.isZero(inputVal, editor.columnKey) ? "" : format(column[0], inputVal, spl));
                    $bCell.textContent = formatted;
                    let disFormat = inputVal === "" || failed ? inputVal : (spl.padded ? formatted : formatSave(column[0], inputVal));
                    wedgeCell($grid, editor, disFormat);
                    $.data($bCell, v.DATA, disFormat);
                    
                    if ($editor.classList.contains(hpl.CURRENCY_CLS)) {
                        $editor.classList.remove(hpl.CURRENCY_CLS);
                        $bCell.classList.add(hpl.CURRENCY_CLS);
                    }
                    $input.value = "";
                    
                    let inputRidd = function() {
                        if ($bCell.classList.contains(khl.ERROR_CLS)) return;
                        let ridd = column[0].inputProcess; 
                        if (ridd) {
                            let rData = _dataSource[parseFloat(editor.rowIdx)];
                            let rId;
                            if (rData) rId = rData[_pk];
                            ridd(rId, editor.columnKey, disFormat).done((sData) => {
                                _.forEach(sData, sd => {
                                    let res = _$grid.mGrid("updateCell", sd.id, sd.item, sd.value);
                                    if (!_.isNil(res) && res >= 0) {
                                        let sht = _.filter(_.keys(_mafollicle[SheetDef]), k => {
                                            if (k === _currentSheet) return;
                                            let sCols = _mafollicle[SheetDef][k].columns;
                                            return _.find(sCols, c => c.key === sd.item);
                                        });
                                        
                                        _.forEach(sht, s => {
                                            wedgeShtCell(res, sd.item, sd.value, s); 
                                        });
                                    }
                                });
                            });
                        }
                    };
                    
                    let sCol = _specialColumn[editor.columnKey];
                    if (sCol) {
                        let cbx = dkn.controlType[sCol];
                        if (_.toLower(column[0].dataType) === "number") {
                            inputVal = parseFloat(inputVal);
                        }
                        wedgeCell($grid, { rowIdx: editor.rowIdx, columnKey: sCol }, inputVal);
                        let selectedOpt = _.find(cbx.options, o => o.code === inputVal); 
                        if (!_.isNil(selectedOpt)) {
                            let $cbxCell = lch.cellAt($grid, editor.rowIdx, sCol);
                            $cbxCell.textContent = selectedOpt ? selectedOpt.name : "";
                            $.data($cbxCell, lo.CBX_SELECTED_TD, inputVal);
                        }
                    } else if ((sCol = _specialLinkColumn[editor.columnKey]) && sCol.changed) {
                        let data = _mafollicle[_currentPage].origDs[editor.rowIdx];
                        sCol.changed(editor.columnKey, data[_pk], formatted, data[editor.columnKey]).done(res => {
                            let $linkCell = lch.cellAt($grid, editor.rowIdx, sCol.column);
                            if ($linkCell) {
                                $linkCell.querySelector("a").textContent = res;
                                wedgeCell($grid, { rowIdx: editor.rowIdx, columnKey: sCol.column }, res);
                            }
                            inputRidd();
                        });
                    } else inputRidd();
                }
            } else if (editor.type === dkn.COMBOBOX) {
                let cbx = dkn.controlType[editor.columnKey];
                let bSelectedValue = $.data(cbx.my, lo.CBX_SELECTED);
                let $combo = cbx.my.querySelector("." + dkn.CBX_CLS);
                wedgeCell($grid, editor, bSelectedValue);
                let selectedOpt = _.find(cbx.options, o => o.code === bSelectedValue); 
                $bCell.textContent = selectedOpt ? selectedOpt.name : ""; 
                if (cbx.dropdown && cbx.dropdown.style.top !== "-99999px") {
                    dkn.closeDD(cbx.dropdown);
                    $combo.classList.remove(dkn.CBX_ACTIVE_CLS);
                }
            }
            
            $bCell.classList.remove(dkn.CONTROL_CLS);
            _mEditor = null;
        }
        
        /**
         * WedgeCell.
         */
        export function wedgeCell($grid: HTMLElement, coord: any, cellValue: any, reset?: any) {
            let res, valueType = hpl.getValueType($grid, coord.columnKey);
            if (!_.isNil(cellValue) && !_.isEmpty(cellValue)) { 
                if (valueType === "TimeWithDay" || valueType === "Clock") {
                    try {
                        cellValue = time.minutesBased.clock.dayattr.create(
                            time.minutesBased.clock.dayattr.parseString(String(cellValue)).asMinutes).shortText;
                    } catch(e) {}
                } else if (valueType === "Time") {
                    cellValue = nts.uk.time.minutesBased.duration.parseString(String(cellValue)).format();
                }
            }
            
            let rData = _dataSource[coord.rowIdx];
            if (_.isNil(rData)) return;
            let id = rData[_pk];
            
            let origDs = _mafollicle[_currentPage].origDs;
            if (!origDs) return;
            let column = _columnsMap[coord.columnKey];
            
            if (column && _.toLower(column[0].dataType) === "number") {
                cellValue = parseFloat(cellValue);
            }
           
            if (reset) {
                origDs[coord.rowIdx][coord.columnKey] = cellValue;
            }
            let $cell, sumDone, origVal = origDs[coord.rowIdx][coord.columnKey];
            
            let transe = function(sheet, zeroHidden, dirties, desc, main) {
                let colour, before, after, total, calcCell = lch.cellAt(_$grid[0], coord.rowIdx, coord.columnKey, desc),
                    sum = _summaries[coord.columnKey]; 
                if (sum && sum.calculator === "Time" && calcCell) {
                    if (!sumDone) {
                        after = moment.duration(cellValue);
                        before = moment.duration($.data(calcCell, v.DATA));
                        let diff = after.subtract(before);
                        sum[_currentPage].add(diff);
                        sumDone = true;
                    }
                    sum[sheet].textContent = ti.momentToString(sum[_currentPage]);
                } else if (sum && sum.calculator === "Number" && calcCell) {
                    if (!sumDone) {
                        after = parseFloat(cellValue);
                        before = parseFloat($.data(calcCell, v.DATA));
                        total = sum[_currentPage] + ((isNaN(after) ? 0 : after) - (isNaN(before) ? 0 : before));
                        sum[_currentPage] = total;
                        sumDone = true;
                    }
                    sum[sheet].textContent = sum.formatter === "Currency" ? ti.asCurrency(sum[_currentPage]) : sum[_currentPage];
                }
                
                if (zeroHidden && ti.isZero(origVal, coord.columnKey)
                    && (cellValue === "" || _.isNil(cellValue) || ti.isZero(cellValue, coord.columnKey))) {
                    $cell = lch.cellAt($grid, coord.rowIdx, coord.columnKey, desc);
                    if (!$cell) {
                        if (!_.isNil(dirties[id]) && !_.isNil(dirties[id][coord.columnKey])) {
                            delete dirties[id][coord.columnKey];
                        }
                        return { c: calcCell };
                    }
                    
                    $cell.classList.remove(color.ManualEditTarget);
                    $cell.classList.remove(color.ManualEditOther);
                    if (main) {
                        color.popState(id, coord.columnKey, [ color.ManualEditTarget, color.ManualEditOther ]);
                    }
                    
                    let initManEdit = $.data($cell, v.INIT_MAN_EDIT);
                    if (initManEdit) {
                        $cell.classList.add(initManEdit);
                        if (main) color.pushState(id, coord.columnKey, [ initManEdit ]);
                    }
                    if (!_.isNil(dirties[id]) && !_.isNil(dirties[id][coord.columnKey])) {
                        delete dirties[id][coord.columnKey];
                    }
                } else {
                    if (cellValue === origVal) {
                        $cell = lch.cellAt($grid, coord.rowIdx, coord.columnKey, desc);
                        if (!$cell) {
                            if (!_.isNil(dirties[id]) && !_.isNil(dirties[id][coord.columnKey])) {
                                delete dirties[id][coord.columnKey];
                            }
                            return { c: calcCell };
                        }
                        $cell.classList.remove(color.ManualEditTarget);
                        $cell.classList.remove(color.ManualEditOther);
                        if (main) {
                            color.popState(id, coord.columnKey, [ color.ManualEditTarget, color.ManualEditOther ]);
                        }
                        
                        rData[coord.columnKey] = cellValue;
                        let initManEdit = $.data($cell, v.INIT_MAN_EDIT);
                        if (initManEdit) {
                            $cell.classList.add(initManEdit);
                            if (main) {
                                color.pushState(id, coord.columnKey, [ initManEdit ]);
                            }
                        }
                        if (!_.isNil(dirties[id]) && !_.isNil(dirties[id][coord.columnKey])) {
                            delete dirties[id][coord.columnKey];
                        }
                        return { c: calcCell };
                    }
                    
                    if (!dirties[id]) {
                        dirties[id] = {};
                        dirties[id][coord.columnKey] = cellValue;
                    } else {
                        dirties[id][coord.columnKey] = cellValue;
                    }
                    rData[coord.columnKey] = cellValue;
                    if (!_.isNil(_objId) && !_.isNil(_getObjId) && _.isFunction(_getObjId)) {
                        let cId = _getObjId(id);
                        let $cell = lch.cellAt($grid, coord.rowIdx, coord.columnKey, desc);
                        if (cId === _objId) {
                            colour = color.ManualEditTarget;
                        } else {
                            colour = color.ManualEditOther;
                        }
                        
                        if (main) color.pushState(id, coord.columnKey, [ colour ], true);
                        if (!$cell) return { colour: colour };
                        $cell.classList.add(colour);
                        return { c: calcCell, colour: colour };
                    }
                }
            };
            
            let some = function(arr) {
                let exist = false;
                _.forEach(arr, c => {
                    if (c.group) {
                        exist = some(c.group);
                        if (exist) return false;
                    }
                    
                    if (c.key === coord.columnKey) {
                        exist = true;
                        return false;
                    }
                });
                
                return exist;
            };
            
            let osht = function(inoth) {
                _.forEach(_.keys(_mafollicle[SheetDef]), s => {
                    if (s === _currentSheet || !some(_mafollicle[SheetDef][s].columns)) return;
                    let t, formatted, disFormat, maf = _mafollicle[_currentPage][s];
                    if (maf && maf.desc) {
                        t = transe(s, maf.zeroHidden, maf.dirties, maf.desc);
                        if (!t || !t.c || _.find(_fixedColumns, fc => fc.key === coord.columnKey)) return;
                        let control = dkn.controlType[coord.columnKey];
                        if (control === dkn.LINK_LABEL) {
                            let link = t.c.querySelector("a");
                            link.innerHTML = cellValue;
                        } else if (_.isObject(control) && control.type === dkn.COMBOBOX) {
                            let sel = _.find(control.options, o => o.code === cellValue);
                            if (sel) { 
                                $.data(t.c, lo.CBX_SELECTED_TD, cellValue);
                                t.c.textContent = sel.name; 
                            }
                        } else {
                            formatted = !_.isNil(column) ? format(column[0], cellValue) : cellValue;
                            t.c.textContent = formatted;
                            disFormat = cellValue === "" || _.isNil(column) ? cellValue : formatSave(column[0], cellValue);
                            $.data(t.c, v.DATA, disFormat);
                        }
                        
                        if (t.colour) t.c.classList.add(t.colour);
                    }
                    
                    if (maf && maf.zeroHidden && ti.isZero(origVal, coord.columnKey)
                        && (cellValue === "" || _.isNil(cellValue) || ti.isZero(cellValue, coord.columnKey))
                        && !_.isNil(maf.dirties[id]) && !_.isNil(maf.dirties[id][coord.columnKey])) {
                        delete maf.dirties[id][coord.columnKey];
                    } else if (maf && cellValue === origVal
                        && !_.isNil(maf.dirties[id]) && !_.isNil(maf.dirties[id][coord.columnKey])) {
                        delete maf.dirties[id][coord.columnKey];
                    } else if (cellValue !== origVal) {
                        if (!maf) {
                            _mafollicle[_currentPage][s] = { dirties: {} };
                            maf = _mafollicle[_currentPage][s];
                        }
                        
                        if (!maf.dirties[id]) maf.dirties[id] = {};
                        maf.dirties[id][coord.columnKey] = cellValue;
                    } else if (inoth && cellValue === origVal) {
                        rData[coord.columnKey] = cellValue;
                    }
                });
            };
            
            if (!column) {
                osht(true);
                return;
            }
            
            res = transe(_currentSheet, _zeroHidden, _dirties, null, true);
            osht();
            
            return res ? res.colour : null;
        }
        
        /**
         * WedgeShtCell.
         */
        export function wedgeShtCell(rowIdx: any, key: any, value, sht: any) {
            let rd = _dataSource[rowIdx];
            if (!rd) return;
            let stt, id = rd[_pk];
            if (_cellStates[id] && (stt = _cellStates[id][key]) && (stt = stt[0].state)
                && _.find(stt, s => s === color.Disable)) return;
             
            let maf = _mafollicle[_currentPage][sht];
            if (maf && maf.desc) {
                let i = maf.desc.colIdxes[key];
                if (_.isNil(i)) return;
                let c = maf.desc.rows[rowIdx][i];
                if (!c) return;
                let retCol = _columnsMap[key];
                if (!retCol) return;
                let formatted = format(retCol[0], value);
                c.textContent = formatted;
                let disFormat = formatSave(retCol[0], value);
                wedgePrelimShtCell(c, rowIdx, key, value, sht);
                $.data(c, v.DATA, disFormat);
            } else {
                wedgePrelimShtCell(null, rowIdx, key, value, sht);
            }
        }
        
        /**
         * WedgePrelimShtCell.
         */
        export function wedgePrelimShtCell($cell: HTMLElement, rowIdx: any, key: any, value: any, sht: any, reset?: any) {
            let res, valueType = hpl.getValueType(_$grid[0], key);
            if (!_.isNil(value) && !_.isEmpty(value)) { 
                if (valueType === "TimeWithDay" || valueType === "Clock") {
                    try {
                        value = time.minutesBased.clock.dayattr.create(
                            time.minutesBased.clock.dayattr.parseString(String(value)).asMinutes).shortText;
                    } catch(e) {}
                } else if (valueType === "Time") {
                    value = nts.uk.time.minutesBased.duration.parseString(String(value)).format();
                }
            }
            
            let rData = _dataSource[rowIdx];
            if (_.isNil(rData)) return;
            let id = rData[_pk];
            
            let origDs = _mafollicle[_currentPage].origDs;
            if (!origDs) return;
            if (reset) {
                origDs[rowIdx][key] = value;
            }
            let origVal = origDs[rowIdx][key];
            let column = _columnsMap[key];
            if (column && _.toLower(column[0].dataType) === "number") {
                value = parseFloat(value);
            }
            
            let before, after, total, sum = _summaries[key],
                ohsht = _mafollicle[_currentPage][sht]; 
            if (sum && sum.calculator === "Time") {
                after = moment.duration(value);
                before = moment.duration(rData[key]);
                let diff = after.subtract(before);
                sum[_currentPage].add(diff);
                if (sum[sht]) sum[sht].textContent = ti.momentToString(sum[_currentPage]);
            } else if (sum && sum.calculator === "Number") {
                after = parseFloat(value);
                before = parseFloat(rData[key]);
                total = sum[_currentPage] + ((isNaN(after) ? 0 : after) - (isNaN(before) ? 0 : before));
                sum[_currentPage] = total;
                if (sum[sht]) {
                    sum[sht].textContent = sum.formatter === "Currency" ? ti.asCurrency(sum[_currentPage]) : sum[_currentPage];
                }
            }
            
            if (_zeroHidden && ti.isZero(origVal, key)
                && (value === "" || _.isNil(value) || parseFloat(value) === 0)) {
                if (ohsht && !_.isNil(ohsht.dirties[id]) && !_.isNil(ohsht.dirties[id][key])) {
                    delete dirties[id][coord.columnKey];
                }
                
                color.popState(id, key, [ color.ManualEditTarget, color.ManualEditOther ]);
                if (!$cell) return;
                $cell.classList.remove(color.ManualEditTarget);
                $cell.classList.remove(color.ManualEditOther);
            } else {
                if (value === origVal) {
                    if (ohsht && !_.isNil(ohsht.dirties[id]) && !_.isNil(ohsht.dirties[id][key])) {
                        delete ohsht.dirties[id][key];
                    }
                    
                    color.popState(id, key, [ color.ManualEditTarget, color.ManualEditOther ]);
                    if (!$cell) return;
                    $cell.classList.remove(color.ManualEditTarget);
                    $cell.classList.remove(color.ManualEditOther);
                    return;
                }
                
                let dirties;
                if (!ohsht) {
                    _mafollicle[_currentPage][sht] = { dirties: {} };
                    dirties = _mafollicle[_currentPage][sht].dirties;
                    dirties[id] = {};
                } else if (!ohsht.dirties) {
                    _mafollicle[_currentPage][sht].dirties = {};
                    dirties = _mafollicle[_currentPage][sht].dirties;
                    dirties[id] = {};
                } else if (!ohsht.dirties[id]) {
                    _mafollicle[_currentPage][sht].dirties[id] = {};
                    dirties = _mafollicle[_currentPage][sht].dirties;
                } else dirties = _mafollicle[_currentPage][sht].dirties;
                
                dirties[id][key] = value;
                rData[key] = value;
                if (!_.isNil(_objId) && !_.isNil(_getObjId) && _.isFunction(_getObjId)) {
                    let cId = _getObjId(id);
                    if (cId === _objId) {
                        res = color.ManualEditTarget;
                    } else {
                        res = color.ManualEditOther;
                    }
                    
                    color.pushState(id, key, res, true);
                    if (!$cell) return res;
                    $cell.classList.add(res);
                }
            }
        }
        
        /**
         * Format.
         */
        export function format(column: any, value: any, spl?: any) {
            if (util.isNullOrEmpty(_.trim(value))) return value;
            if (column.constraint) {
                let contrainte, valueType, constraint = column.constraint;
                if (constraint.primitiveValue) {
                    contrainte = ui.validation.getConstraint(constraint.primitiveValue);
                    valueType = contrainte.valueType;
                } else valueType = constraint.cDisplayType;
                
                if (!_.isNil(value) && value !== "") {
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
                    } else if (valueType === "Time") {
                        value = uk.time.minutesBased.duration.parseString(value).format();
                    } else if (valueType === "Currency") { 
                        let currencyOpts: any = new ui.option.CurrencyEditorOption();
                        currencyOpts.grouplength = constraint.groupLength | 3;
                        currencyOpts.decimallength = _.isNil(constraint.decimalLength) ? 0 : constraint.decimalLength;
                        currencyOpts.currencyformat = constraint.currencyFormat ? constraint.currencyFormat : "JPY";
                        let groupSeparator = constraint.groupSeparator || ",";
                        let rawValue = text.replaceAll(value, groupSeparator, "");
                        let formatter = new uk.text.NumberFormatter({ option: currencyOpts });
                        let numVal = Number(rawValue);
                        if (!isNaN(numVal)) value = formatter.format(numVal);
                        else value = rawValue;
                    } else if (valueType === "String" && contrainte && contrainte.maxLength && contrainte.isZeroPadded) {
                        value = uk.text.padLeft(value, '0', parseInt(contrainte.maxLength));
                        if (spl) spl.padded = true;
                    }
                }
            }
            
            return value;
        }
        
        /**
         * Format save.
         */
        export function formatSave(column: any, value: any) {
            if (column.constraint && !util.isNullOrEmpty(value)) {
                let parsed, constraint = column.constraint;
                let valueType = constraint.primitiveValue ? ui.validation.getConstraint(constraint.primitiveValue).valueType
                            : constraint.cDisplayType;
                if (!_.isNil(value) && value !== "") {
                    if (valueType === "Time") {
                        parsed = uk.time.minutesBased.duration.parseString(value);
                        if (parsed.success) value = parsed.format();
                    } else if (valueType === "TimeWithDay" || valueType === "Clock") {
                        let minutes = time.minutesBased.clock.dayattr.parseString(String(value)).asMinutes;
                        if (_.isNil(minutes)) return value;
                        try {
                            value = time.minutesBased.clock.dayattr.create(minutes).shortText;
                        } catch (e) {}   
                    }
                }
            }
            
            return value;
        }
        
        /**
         * CollerData.
         */
        export function collerData(evt: any) {
            let data;
            let key, keys = _.keys(_selected);
            if (keys.length !== 1 || _selected[keys[0]].length !== 1) return; 
            key = _selected[keys[0]][0];
            let target = lch.cellAt(_$grid[0], keys[0], key);
            if (!target) return; 
            if (window.clipboardData) {
                window.event.returnValue = false;
                data = window.clipboardData.getData("text");
            } else {
                data = evt.clipboardData.getData("text/plain");
            }
            
            if (_mEditor && _mEditor.type === dkn.TEXTBOX) {
                let $editor = dkn.controlType[dkn.TEXTBOX].my;
                let $input = $editor.querySelector("input.medit");
                $input.value = data;
                return;
            }
            
            if (afterCollertar) setTimeout(() => afterCollertar.focus({ preventScroll: true }), 1);
            let formatted, disFormat, coord = ti.getCellCoord(target), col = _columnsMap[coord.columnKey];
            
            let inputRidd = function($t, rowIdx, columnKey, dFormat) {
                if ($t.classList.contains(khl.ERROR_CLS)) return;
                let ridd = _columnsMap[columnKey][0].inputProcess; 
                if (ridd) {
                    let rData = _dataSource[rowIdx];
                    let rId;
                    if (rData) rId = rData[_pk];
                    ridd(rId, columnKey, dFormat).done((sData) => {
                        _.forEach(sData, sd => {
                            let res = _$grid.mGrid("updateCell", sd.id, sd.item, sd.value);
                            if (!_.isNil(res) && res >= 0) {
                                let sht = _.filter(_.keys(_mafollicle[SheetDef]), k => {
                                    if (k === _currentSheet) return;
                                    let sCols = _mafollicle[SheetDef][k].columns;
                                    return _.find(sCols, c => c.key === sd.item);
                                });
                                
                                _.forEach(sht, s => {
                                    wedgeShtCell(res, sd.item, sd.value, s); 
                                });
                            }
                        });
                    });
                }
            };
            
            let collerRidd = function(rowIdx, columnKey, value) {
                let sCol = _specialColumn[columnKey];
                if (sCol) {
                    let cbx = dkn.controlType[sCol];
                    wedgeCell(_$grid[0], { rowIdx: rowIdx, columnKey: sCol }, value);
                    let selectedOpt = _.find(cbx.options, o => o.code === value); 
                    if (!_.isNil(selectedOpt)) {
                        let $cbxCell = lch.cellAt(_$grid[0], rowIdx, sCol);
                        $cbxCell.textContent = selectedOpt ? selectedOpt.name : "";
                        $.data($cbxCell, lo.CBX_SELECTED_TD, value);
                    }
                } else if ((sCol = _specialLinkColumn[columnKey]) && sCol.changed) {
                    let data = _mafollicle[_currentPage].origDs[rowIdx];
                    sCol.changed(columnKey, data[_pk], value, data[columnKey]).done(res => {
                        let $linkCell = lch.cellAt(_$grid[0], rowIdx, sCol.column);
                        if ($linkCell) {
                            $linkCell.querySelector("a").textContent = res;
                            wedgeCell(_$grid[0], { rowIdx: rowIdx, columnKey: sCol.column }, res);
                        }
                        
                        let $t = lch.cellAt(_$grid[0], rowIdx, columnKey);
                        inputRidd($t, rowIdx, columnKey, value);
                    });
                } else {
                    let $t = lch.cellAt(_$grid[0], rowIdx, columnKey); 
                    inputRidd($t, rowIdx, columnKey, value);
                }
            };
            
            if (_copieMode === 0) {
                if (dkn.controlType[coord.columnKey] !== dkn.TEXTBOX || target.classList.contains(color.Disable)
                    || !col || col.length === 0) return;
                let validator = _validators[coord.columnKey];
                if (validator) {
                    let result = validator.probe(data); 
                    let cell = { id: _dataSource[coord.rowIdx][_pk], index: coord.rowIdx, columnKey: coord.columnKey, element: target };
                    khl.clear(cell);
                    
                    if (!result.isValid) {
                        khl.set(cell, result.errorMessage);
                    }
                }
                
                formatted = su.format(col[0], data);
                target.innerHTML = formatted;
                disFormat = su.formatSave(col[0], data);
                _histoire.push({ tx: util.randomId(), o: [{ coord: coord, value: _dataSource[coord.rowIdx][coord.columnKey] }]});
                su.wedgeCell(_$grid[0], coord, disFormat);
                $.data(target, v.DATA, disFormat);
                collerRidd(coord.rowIdx, coord.columnKey, disFormat);
                return;
            }
                
            let dataRows = _.map(data.split("\n"), function(row) {
                return row.split("\t");
            });
            
            let rowsCount = dataRows.length;
            if ((dataRows[rowsCount - 1].length === 1 && dataRows[rowsCount - 1][0] === "")
                || (dataRows.length === 1 && dataRows[0].length === 1 
                    && (dataRows[0][0] === "" || dataRows[0][0] === "\r"))) {
                dataRows.pop();
            }
            
            let cArr, e, pointCoord, pointCol, cPoint, rPoint = keys[0], 
                fixedCount = _.keys(_mDesc.fixedColIdxes).length, eIdx = _mDesc.fixedColIdxes[key];
            if (_.isNil(eIdx)) {
                eIdx = _mDesc.colIdxes[key];
                if (!_.isNil(eIdx)) eIdx += fixedCount;
            }
            
            if (_.isNil(eIdx)) return;
            let sess = { tx: util.randomId(), o: [] };
            _.forEach(dataRows, r => {
                cArr = lch.rowAt(_$grid[0], rPoint++);
                cPoint = eIdx;
                if (!cArr) return;
                
                _.forEach(r, (c, i) => {
                    e = cArr[cPoint++];
                    if (e.style.display === "none" && cArr.length > cPoint + 1) e = cArr[cPoint++];
                    if (!e) return false;
                    c = _.trim(c);
                    if (c === "null") return;
                    pointCoord = ti.getCellCoord(e);
                    pointCol = _columnsMap[pointCoord.columnKey];
                    if (dkn.controlType[pointCoord.columnKey] !== dkn.TEXTBOX || e.classList.contains(color.Disable)
                        || !pointCol || pointCol.length === 0) {
                        return;
                    }
                    
                    let validator = _validators[pointCoord.columnKey];
                    if (validator) {
                        let result = validator.probe(c); 
                        let cell = { id: _dataSource[pointCoord.rowIdx][_pk], index: pointCoord.rowIdx, columnKey: pointCoord.columnKey, element: e };
                        khl.clear(cell);
                        
                        if (!result.isValid) {
                            khl.set(cell, result.errorMessage);
                        }
                    }
                    
                    formatted = su.format(pointCol[0], c);
                    e.innerHTML = formatted;
                    disFormat = su.formatSave(pointCol[0], c);
                    sess.o.push({ coord: pointCoord, value: _dataSource[pointCoord.rowIdx][pointCoord.columnKey] });
                    su.wedgeCell(_$grid[0], pointCoord, disFormat);
                    $.data(e, v.DATA, disFormat);
                    collerRidd(pointCoord.rowIdx, pointCoord.columnKey, disFormat);
                });
            });
            
            _histoire.push(sess);
        }
        
        /**
         * CopieData.
         */
        export function copieData(coupe?: any) {
            let keys = Object.keys(_selected);
            if (!_selected || keys.length === 0) return;
            let coord, key, struct = "", ds = _dataSource, sess, onetar;
            if (coupe) {
                sess = { tx: util.randomId(), o: [] };
            }
            if (keys.length === 1 && _selected[keys[0]].length === 1) {
                _copieMode = 0;
                key = _selected[keys[0]][0];
                let struct = ds[parseFloat(keys[0])][key];
                if (coupe) {
                    let cell = lch.cellAt(_$grid[0], keys[0], key);
                    if (cell) {
                        coord = ti.getCellCoord(cell);
                        cell.innerHTML = "";
                        sess.o.push({ coord: coord, value: _dataSource[coord.rowIdx][coord.columnKey] });
                        su.wedgeCell(_$grid[0], coord, "");
                        $.data(cell, v.DATA, "");
                        _histoire.push(sess);
                    }
                }
                
                if (_copieer) {
                    onetar = document.activeElement;
                    _copieer.value = struct;
                    _copieer.select();
                    document.execCommand("copy");
                    onetar.focus({ preventScroll: true });
                }
                return;
            }
            
            let sortedKeys = keys.sort((o, t) => o - t);
            let fixedCount = 0, colIdx, elms, e, desc = _mDesc, min, max, value;
            _copieMode = 1;
            if (desc.fixedColIdxes) {
                fixedCount = Object.keys(desc.fixedColIdxes).length;
            }
            
            _.forEach(_.keys(_selected), r => {
                let idxArr = _.map(_selected[r], c => {
                    let idx, isFixed = true;
                    if (desc.fixedColIdxes) {
                        idx = desc.fixedColIdxes[c];
                    }
                    
                    if (_.isNil(idx)) {
                        idx = desc.colIdxes[c];
                        isFixed = false;
                    }
                    
                    if (coupe) {
                        sess.o.push({ coord: { rowIdx: r, columnKey: c }, value: _dataSource[r][c] });
                    }
                    return isFixed ? idx : idx + fixedCount;
                });
                
                let minVal = _.min(idxArr);
                let maxVal = _.max(idxArr);
                if (_.isNil(min) || min > minVal) min = minVal; 
                if (_.isNil(max) || max < maxVal) max = maxVal;
            });
            
            if (coupe && sess.o.length > 0) {
                _histoire.push(sess);
            }
            
            for (let i = parseFloat(sortedKeys[0]); i <= parseFloat(sortedKeys[sortedKeys.length - 1]); i++) {
                elms = lch.rowAt(_$grid[0], i);
                for (let c = min; c <= max; c++) {
                    e = elms[c];
                    if (!e || e.style.display === "none") return;
                    coord = ti.getCellCoord(e);
                    value = ds[i][coord.columnKey];
                    if (_.isNil(value) || value === "" || !e.classList.contains(lch.CELL_SELECTED_CLS)
                        || e.classList.contains(color.Disable)) {
                        struct += "null"; 
                    } else { 
                        struct += value;
                        if (coupe) {
                            e.innerHTML = "";
                            su.wedgeCell(_$grid[0], coord, "");
                            $.data(e, v.DATA, "");
                        }
                    }
                    
                    if (c === max) struct += "\n";
                    else struct += "\t";
                } 
            }
            
            if (_copieer) {
                onetar = document.activeElement;
                _copieer.value = struct;
                _copieer.select();
                document.execCommand("copy");
                onetar.focus({ preventScroll: true });
            }
        }
        
        /**
         * Annuler.
         */
        export function annuler() {
            if (!_histoire || _histoire.length === 0) return;
            let sess = _histoire.pop();
            if (sess) {
                let afterAnnulerRidd = function(el, c, column, disFormat) {
                    if (el.classList.contains(khl.ERROR_CLS)) return;
                    let ridd = column[0].inputProcess; 
                    if (ridd) {
                        let rData = _dataSource[parseFloat(c.rowIdx)];
                        let rId;
                        if (rData) rId = rData[_pk];
                        ridd(rId, c.columnKey, disFormat).done((sData) => {
                            _.forEach(sData, sd => {
                                let res = _$grid.mGrid("updateCell", sd.id, sd.item, sd.value);
                                if (!_.isNil(res) && res >= 0) {
                                    let sht = _.filter(_.keys(_mafollicle[SheetDef]), k => {
                                        if (k === _currentSheet) return;
                                        let sCols = _mafollicle[SheetDef][k].columns;
                                        return _.find(sCols, c => c.key === sd.item);
                                    });
                                    
                                    _.forEach(sht, s => {
                                        wedgeShtCell(res, sd.item, sd.value, s); 
                                    });
                                }
                            });
                        });
                    }
                };
                
                sess.o.forEach(c => {
                    let failed, spl = {},
                        el = lch.cellAt(_$grid[0], c.coord.rowIdx, c.coord.columnKey),
                        column = _columnsMap[c.coord.columnKey], 
                        formatted = failed ? c.value : (_zeroHidden && ti.isZero(c.value, c.coord.columnKey) ? "" : format(column[0], c.value, spl));
                    let validator = _validators[c.coord.columnKey];
                    if (validator) {
                        let result = validator.probe(c.value); 
                        let cell = { id: _dataSource[c.coord.rowIdx][_pk], index: c.coord.rowIdx, columnKey: c.coord.columnKey, element: el };
                        khl.clear(cell);
                        
                        if (!result.isValid) {
                            khl.set(cell, result.errorMessage);
                            failed = true;
                        }
                    }
                    
                    el.textContent = formatted;
                    let disFormat = c.value === "" || failed ? c.value : (spl.padded ? formatted : formatSave(column[0], c.value));
                    wedgeCell(_$grid[0], c.coord, disFormat);
                    $.data(el, v.DATA, disFormat);
                    
                    let sCol = _specialColumn[c.coord.columnKey];
                    if (sCol) {
                        let cbx = dkn.controlType[sCol];
                        wedgeCell(_$grid[0], { rowIdx: c.coord.rowIdx, columnKey: sCol }, c.value);
                        let selectedOpt = _.find(cbx.options, o => o.code === c.value); 
                        if (!_.isNil(selectedOpt)) {
                            let $cbxCell = lch.cellAt(_$grid[0], c.coord.rowIdx, sCol);
                            $cbxCell.textContent = selectedOpt ? selectedOpt.name : "";
                            $.data($cbxCell, lo.CBX_SELECTED_TD, c.value);
                        }
                    } else if ((sCol = _specialLinkColumn[c.coord.columnKey]) && sCol.changed) {
                        let data = _mafollicle[_currentPage].origDs[c.coord.rowIdx];
                        sCol.changed(c.coord.columnKey, data[_pk], formatted, data[c.coord.columnKey]).done(res => {
                            let $linkCell = lch.cellAt(_$grid[0], c.coord.rowIdx, sCol.column);
                            if ($linkCell) {
                                $linkCell.querySelector("a").textContent = res;
                                wedgeCell(_$grid[0], { rowIdx: c.coord.rowIdx, columnKey: sCol.column }, res);
                            }
                            
                            afterAnnulerRidd(el, c.coord, column, disFormat);
                        });
                    } else afterAnnulerRidd(el, c.coord, column, disFormat);
                });
            }
        }
    }
    
    module ssk {
        export let SCROLL_EVT = "scroll";
        export let CLICK_EVT = "click";
        export let MOUSE_DOWN = "mousedown";
        export let MOUSE_MOVE = "mousemove";
        export let MOUSE_UP = "mouseup";
        export let MOUSE_OVER = "mouseover";
        export let MOUSE_ENTER = "mouseenter";
        export let MOUSE_OUT = "mouseout";
        export let MOUSE_LEAVE = "mouseleave";
        export let FOCUS_IN = "focusin";
        export let PASTE = "paste";
        export let MOUSE_WHEEL = "wheel";
        export let RESIZE = "resize";
        export let KEY_DOWN = "keydown";
        export let KEY_UP = "keyup";
        export let CM = "contextmenu";
        export let AREA_RESIZE_STARTED = "extablearearesizestarted";
        export let AREA_RESIZE = "extablearearesize";
        export let AREA_RESIZE_END = "extablearearesizeend";
        export let BODY_HEIGHT_CHANGED = "extablebodyheightchanged";
        export let OCCUPY_UPDATE = "extableoccupyupdate";
        export let START_EDIT = "extablestartedit";
        export let STOP_EDIT = "extablestopedit";
        export let CELL_UPDATED = "extablecellupdated";
        export let ROW_UPDATED = "extablerowupdated";
        export let POPUP_SHOWN = "xpopupshown";
        export let POPUP_INPUT_END = "xpopupinputend";
        export let ROUND_RETREAT = "extablecellretreat";
        export let CHECK_ALL = "extableselectallrows";
        export let CHECK_ROW = "extableselectrow";
        export let MOUSEIN_COLUMN = "extablemouseincolumn";
        export let MOUSEOUT_COLUMN = "extablemousoutcolumn";
        export let RENDERED = "extablerowsrendered";
        export let COMPLETED = "extablecompleted";
        
        window.addXEventListener = document.addXEventListener = Element.prototype.addXEventListener = addEventListener;
        window.removeXEventListener = document.removeXEventListener = Element.prototype.removeXEventListener = removeEventListener;
        
        /**
         * Trigger.
         */
        export function trigger($target: HTMLElement, eventName: string, args?: any) {
            let event;
            if (window.CustomEvent) {
                event = new CustomEvent(eventName, { detail: args });
            } else {
                event = document.createEvent('CustomEvent');
                event.initCustomEvent(eventName, true, true, args);
            }
            $target.dispatchEvent(event);
        }
        
        /**
         * Add event listener.
         */
        function addEventListener(event, cb, opts) {
            let self = this;
            if (!self.ns) self.ns = {};
            if (!self.ns[event]) self.ns[event] = [ cb ];
            else self.ns[event].push(cb);
            self.addEventListener(event.split(".")[0], cb, opts);  
        };
        
        /**
         * Remove event listener.
         */
        function removeEventListener(event, cb?) {
            let self = this;
            if (!self.ns) return;
            if (cb) {
                let keys = Object.keys(self.ns).filter(function(k) {
                    return (k === event || k === event.split(".")[0])
                            && self.ns[k].indexOf(cb) > -1;
                });
                
                let key;
                if (keys.length > 0) {
                    key = keys[0];
                    self.ns[key].splice(self.ns[key].indexOf(cb), 1);
                    if (self.ns[key].length === 0) delete self.ns[key];
                }
                self.removeEventListener(event.split(".")[0], cb);
                return;
            }
            
            if (!self.ns[event]) return;
            self.ns[event].forEach(function(e) {
                self.removeEventListener(event.split(".")[0], e); 
            });
            delete self.ns[event];
        }
    }
    
    export module gp {
        export const PAGING_CLS = "mgrid-paging";
        export const SHEET_CLS = "mgrid-sheet";
        export const PAGE_HEIGHT = 44;
        export const SHEET_HEIGHT = 30;
        export let $sheetArea;
        
        /**
         * ImiPages.
         */
        export function imiPages($container: HTMLElement, top: any, width: any) {
            if (!_paging) return;
            let $pageArea = v.createWrapper(top + ti.getScrollWidth() + SUM_HEIGHT + "px", 0, 
                { width: parseFloat(width) + ti.getScrollWidth() + "px", height: PAGE_HEIGHT + "px", containerClass: PAGING_CLS });
            $container.appendChild($pageArea);
            let $recDesc = document.createElement("span");
            $recDesc.classList.add("mgrid-pagerecordlabel");
            $recDesc.textContent = "100 レコード";
            $pageArea.appendChild($recDesc);
            let $gridPaging = _prtDiv.cloneNode();
            $gridPaging.classList.add("mgrid-paging-nav");
            $pageArea.appendChild($gridPaging);
            let $firstPage = _prtDiv.cloneNode();
            $firstPage.classList.add("mgrid-firstpage");
            $firstPage.classList.add("mgrid-paging-item");
            $firstPage.classList.add("ui-state-default");
            $gridPaging.appendChild($firstPage);
            let $arrowStopImg = document.createElement("span");
            $arrowStopImg.classList.add("mgrid-pageimg");
            $arrowStopImg.classList.add("ui-icon");
            $arrowStopImg.classList.add("ui-icon-arrowstop-1-w");
            $firstPage.appendChild($arrowStopImg);
            
            let $buttons = document.createElement("ul");
            $buttons.classList.add("mgrid-page-buttonlist");
            $gridPaging.appendChild($buttons);
            
            let pageList = _.filter(Object.keys(_mafollicle), p => p !== SheetDef).sort((p1, p2) => parseFloat(p1) - parseFloat(p2));
            $firstPage.addXEventListener(ssk.CLICK_EVT, evt => {
                if (parseInt(pageList[0]) === _currentPage) return;
                let btns = $buttons.querySelectorAll("li");
                _.forEach(btns, li => {
                    if (li.classList.contains("ui-state-active")) {
                        li.classList.remove("ui-state-active");
                    }
                });
                
                lungeto(0);
                btns[0].classList.add("ui-state-active");
            });
            
            _.forEach(pageList, p => {
                if (p !== SheetDef) {
                    let $pageBtn = document.createElement("li");
                    $pageBtn.classList.add("mgrid-page-button");
                    $pageBtn.classList.add("ui-state-default");
                    $pageBtn.textContent = parseInt(p) + 1;
                    if (parseInt(p) === _currentPage) $pageBtn.classList.add("ui-state-active");
                    $pageBtn.addXEventListener(ssk.CLICK_EVT, evt => {
                        if ($pageBtn.classList.contains("ui-state-active")) return;
                        lungeto(parseInt(p));
                        _.forEach($buttons.querySelectorAll("li"), li => {
                            if (li.classList.contains("ui-state-active")) {
                                li.classList.remove("ui-state-active");
                            }
                        });
                        
                        $pageBtn.classList.add("ui-state-active");
                    });
                    
                    $buttons.appendChild($pageBtn);
                }
            });
            
            let $lastPage = _prtDiv.cloneNode();
            $lastPage.classList.add("mgrid-lastpage");
            $lastPage.classList.add("mgrid-paging-item");
            $lastPage.classList.add("ui-state-default");
            $gridPaging.appendChild($lastPage);
            $lastPage.addXEventListener(ssk.CLICK_EVT, evt => {
                if (pageList.length - 1 === _currentPage) return;
                let btns = $buttons.querySelectorAll("li");
                _.forEach(btns, li => {
                    if (li.classList.contains("ui-state-active")) {
                        li.classList.remove("ui-state-active");
                    }
                });
                
                lungeto(pageList.length - 1);
                btns[btns.length - 1].classList.add("ui-state-active");
            });
            
            let $arrowStopEImg = document.createElement("span");
            $arrowStopEImg.classList.add("mgrid-pageimg");
            $arrowStopEImg.classList.add("ui-icon");
            $arrowStopEImg.classList.add("ui-icon-arrowstop-1-e");
            $lastPage.appendChild($arrowStopEImg);
        }
        
        /**
         * ImiSheets.
         */
        export function imiSheets($container: HTMLElement, top: any, width: any) {
            if (!_sheeting) return;
            $sheetArea = v.createWrapper(top + ti.getScrollWidth() + SUM_HEIGHT + "px", 0, 
                { width: parseFloat(width) + ti.getScrollWidth() + "px", height: SHEET_HEIGHT + "px", containerClass: SHEET_CLS });
            $container.appendChild($sheetArea);
            let $scrollBar = document.createElement("ul");
            $scrollBar.classList.add("mgrid-sheet-scrollbar");
            $sheetArea.appendChild($scrollBar);
            let $up = document.createElement("li");
            $up.className = "ui-icon-triangle-1-n ui-icon";
            $scrollBar.appendChild($up);
            let $down = document.createElement("li");
            $down.className = "ui-icon-triangle-1-s ui-icon";
            $scrollBar.appendChild($down);
            let $gridSheet = _prtDiv.cloneNode();
            $gridSheet.classList.add("mgrid-sheet-nav");
            $sheetArea.appendChild($gridSheet);
            let $buttons = document.createElement("ul");
            $buttons.classList.add("mgrid-sheet-buttonlist");
            $gridSheet.appendChild($buttons);
            
            _.forEach(Object.keys(_mafollicle[SheetDef]), s => {
                let $btn = document.createElement("li");
                $btn.classList.add("mgrid-sheet-button");
                $btn.classList.add("ui-state-default");
                $btn.textContent = _mafollicle[SheetDef][s].text;
                if (s === _currentSheet) $btn.classList.add("ui-state-active");
                $btn.addXEventListener(ssk.CLICK_EVT, evt => {
                    if ($btn.classList.contains("ui-state-active")
                        || !_dataSource || _dataSource.length === 0) return;
                    hopto(s);
                    _.forEach($buttons.querySelectorAll("li"), li => {
                        if (li.classList.contains("ui-state-active")) {
                            li.classList.remove("ui-state-active");
                        }
                    });
                    
                    $btn.classList.add("ui-state-active");
                });
                
                $buttons.appendChild($btn);
            });
            
            let sheetNav = $($gridSheet);
            $up.addXEventListener(ssk.MOUSE_DOWN, evt => {
                sheetNav.scrollTop(sheetNav.scrollTop() - SHEET_HEIGHT);
            });
            
            $down.addXEventListener(ssk.MOUSE_DOWN, evt => {
                sheetNav.scrollTop(sheetNav.scrollTop() + SHEET_HEIGHT);
            });
        }
        
        /**
         * Lungeto.
         */
        export function lungeto(index: any) {
            let sheetDef = _mafollicle[SheetDef][_currentSheet];
//            _mafollicle[_currentPage].dataSource = _.cloneDeep(_dataSource);
            _currentPage = index;
            _dataSource = _mafollicle[_currentPage].dataSource;
            _hr = null;
            lch.clearAll(_$grid[0]);
            
            _.filter(_bodyWrappers, w => w.classList.contains(FREE))[0].scrollTop = 0;
            
            if (!_vessel()) {
                _mafollicle[_currentPage][_currentSheet] = { errors: [], desc: {}, dirties: {}, zeroHidden: _zeroHidden, selected: {}, histoire: [] };
            }
            
            _mDesc = _vessel().desc;
            _errors = _vessel().errors;
            _dirties = _vessel().dirties;
            _selected = _vessel().selected;
            _histoire = _vessel().histoire;
            let sum, res = _cloud.renderRows(true);
            ti.calcTotal();
            _.forEach(_.keys(_summaries), k => {
                sum = _summaries[k];
                if (!sum[_currentSheet]) return;
                if (sum.calculator === "Number") {
                    sum[_currentSheet].textContent = sum.formatter === "Currency" ? ti.asCurrency(sum[_currentPage]) : sum[_currentPage]; 
                } else if (sum.calculator === "Time") {
                    sum[_currentSheet].textContent = ti.momentToString(sum[_currentPage]);
                }
            });
            
            if (!res) {
                let tmp = _zeroHidden;
                _zeroHidden = _vessel().zeroHidden;
                _vessel().zeroHidden = tmp;
                if (lo.changeZero(_vessel().zeroHidden)) _zeroHidden = _vessel().zeroHidden;
                return;
            }
            
            let start = res.start, end = res.end, cursor;
            if (_.isNil(_mDesc) || Object.keys(_mDesc).length === 0) {
                $.data(_$grid[0], lo.DESC, _mDesc);
                if (!_.isNil(res.fixedColIdxes) || !_.isNil(res.colIdxes)) {
                    _mDesc.fixedColIdxes = res.fixedColIdxes;
                    _mDesc.colIdxes = res.colIdxes;
                } 
                
                _mDesc.fixedRows = [];
                _mDesc.rows = [];
                _mDesc.fixedRowElements = [];
                _mDesc.rowElements = [];
            }
            
            for (let i = start; i <= end; i++) {
                cursor = i - start;
                if (!_mDesc.fixedRows[i]) {
                    _mDesc.fixedRows[i] = res.fixedRows[cursor];
                    _mDesc.fixedRowElements[i] = res.fixedRowElements[cursor];
                }
                _mDesc.rows[i] = res.rows[cursor];
                _mDesc.rowElements[i] = res.rowElements[cursor];
            }
            
            let tmp = _zeroHidden;
            _zeroHidden = _vessel().zeroHidden;
            _vessel().zeroHidden = tmp;
            if (lo.changeZero(_vessel().zeroHidden)) _zeroHidden = _vessel().zeroHidden;
        }
        
        /**
         * Hopto.
         */
        export function hopto(place: any) {
            let bfPainter;
            if (_currentSheet === place) return;
            if (_hasFixed) {
                bfPainter = _.cloneDeep(_mafollicle[SheetDef][_currentSheet].painters[0]);
                if (_summaries) {
                    _.forEach(_fixedColumns, c => {    
                        let sum = _summaries[c.key];
                        if (!sum || !sum[_currentSheet]) return;
                        sum[place] = sum[_currentSheet];    
                    });
                }
            }
            _currentSheet = place;
            
            if (!_vessel()) {
                let desc = { 
                        fixedColIdxes: _.cloneDeep(_mDesc.fixedColIdxes), 
                        fixedRows: _.cloneDeep(_mDesc.fixedRows),
                        fixedRowElements: _.cloneDeep(_mDesc.fixedRowElements),
                        colIdxes: [],
                        rows: [],
                        rowElements: []
                };
                
                let dirties = {}, selected = {};
                if (_selected) {
                    _.forEach(_.keys(_selected), r => {
                        let selectArr = _.filter(_selected[r], c => _.some(_fixedColumns, fc => fc.key === c));
                        if (selectArr.length > 0) {
                            selected[r] = selectArr;
                        }
                    });
                }
                
                if (_dirties) {
                    _.forEach(_.keys(_dirties), r => {
                        let cols = {};
                        _.forEach(_.keys(_dirties[r]), c => {
                            if (_.some(_fixedColumns, fc => fc.key === c)) {
                                cols[c] = _dirties[r][c];
                            }
                        });
                        
                        if (_.keys(cols).length > 0) {
                            dirties[r] = cols;
                        }
                    });
                }
                _mafollicle[_currentPage][_currentSheet] = { desc: desc, errors: [], dirties: dirties, zeroHidden: _zeroHidden, selected: selected, histoire: [] };
            } else if (!_vessel().desc && _vessel().dirties) {
                let desc = { 
                        fixedColIdxes: _.cloneDeep(_mDesc.fixedColIdxes), 
                        fixedRows: _.cloneDeep(_mDesc.fixedRows),
                        fixedRowElements: _.cloneDeep(_mDesc.fixedRowElements),
                        colIdxes: [],
                        rows: [],
                        rowElements: []
                };
                
                let selected = {};
                if (_selected) {
                    _.forEach(_.keys(_selected), r => {
                        let selectArr = _.filter(_selected[r], c => _.some(_fixedColumns, fc => fc.key === c));
                        if (selectArr.length > 0) {
                            selected[r] = selectArr;
                        }
                    });
                }
                
                if (_dirties) {
                    _.forEach(_.keys(_dirties), r => {
                        let cols = {};
                        _.forEach(_.keys(_dirties[r]), c => {
                            if (_.some(_fixedColumns, fc => fc.key === c)) {
                                cols[c] = _dirties[r][c];
                            }
                        });
                        
                        if (_.keys(cols).length > 0) {
                            _vessel().dirties[r] = cols;
                        }
                    });
                }
                
                _vessel().desc = desc;
                _vessel().selected = selected;
                _vessel().zeroHidden = _zeroHidden;
                _vessel().errors = [];
                _vessel().histoire = [];
            } else {
                _.forEach(_.keys(_selected), r => {
                    _.forEach(_selected[r], c => {
                        if (_.some(_fixedColumns, fc => fc.key === c)) {
                            if (!_vessel().selected[r]) {
                                _vessel().selected[r] = [ c ];
                            } else {
                                _vessel().selected[r].push(c);
                            }
                        }
                    });
                });
                
                _.forEach(_.keys(_dirties), r => {
                    _.forEach(_.keys(_dirties[r]), c => {
                        if (_.some(_fixedColumns, fc => fc.key === c)) {
                            if (!_vessel().dirties[r]) {
                                _vessel().dirties[r] = {};
                            }
                            _vessel().dirties[r][c] = _dirties[r][c];
                        }
                    });
                });
            }
            
            _mDesc = _vessel().desc;
            _errors = _vessel().errors;
            _dirties = _vessel().dirties;
            _selected = _vessel().selected;
            _histoire = _vessel().histoire;
            let $header = _$grid[0].querySelector("." + FREE + "." + HEADER);
            let $headerTbl = $header.querySelector("table");
            let bhGroup = $header.querySelector("colgroup");
            let bhBody = $header.querySelector("tbody");
            let dTable = _bodyWrappers[1].querySelector("table");
            let bbGroup = dTable.querySelector("colgroup");
            let sumWrap = _$grid[0].querySelector("." + FREE + "-summaries");
            let sumTbl = sumWrap.querySelector("table");
            let bSumGroup = sumWrap.querySelector("colgroup");
            let bSumBody = sumWrap.querySelector("tbody");
            let sWrap = _$grid[0].querySelector("." + gp.SHEET_CLS);
            let pWrap = _$grid[0].querySelector("." + gp.PAGING_CLS);
            
            if (!_vessel().$hGroup) {
                kt.turfSurf(_cstifle());
                _header.columns = _cstifle();
                let $wrapper = v.createWrapper("0px", _leftAlign, _header, true);
                _mafollicle[SheetDef][_currentSheet].maxWidth = _maxFreeWidth;
                $header.style.maxWidth = _maxFreeWidth + "px";
                let bw = (_maxFreeWidth + ti.getScrollWidth()) + "px";
                _bodyWrappers[1].style.maxWidth = bw;
                let btmw = (Math.min(parseFloat($header.style.width), parseFloat($header.style.maxWidth)) + _maxFixedWidth + ti.getScrollWidth() + 2) + "px";
                if (sumWrap) {
                    sumWrap.style.maxWidth = _maxFreeWidth + "px";
                    sumWrap.style.width = $header.style.width;
                }
                
                if (sWrap) {
                    sWrap.style.width = btmw;
                }
                
                if (pWrap) {
                    pWrap.style.width = btmw;
                }
                $wrapper.classList.add(HEADER);
                let table = v.process($wrapper, _header);
                table.$table.style.height = _header.height;
                let hGroup = $wrapper.querySelector("colgroup");
                let hBody = $wrapper.querySelector("tbody"); 
                $headerTbl.replaceChild(hGroup, bhGroup);
                $headerTbl.replaceChild(hBody, bhBody);
                _vessel().$hGroup = hGroup;
                _vessel().$hBody = hBody;
                _mafollicle[SheetDef][_currentSheet].hColArr = table.cols; 
                let artifactOptions = { primaryKey: _pk, controlMap: table.controlMap,  
                    columns: _header.columns, features: _features, hasSum: _hasSum };
                _mafollicle[SheetDef][_currentSheet].controlMap = _.assignIn(_fixedControlMap, table.controlMap);
                let painters = _hasFixed ? [ bfPainter, table.painter ] : [ table.painter ];
                _mafollicle[SheetDef][_currentSheet].painters = painters;
                let colGroup = document.createElement("colgroup");
                let bodyGroupArr = [];
                _.forEach(table.cols, c => {
                    let col = c.cloneNode();
                    colGroup.appendChild(col);
                    bodyGroupArr.push(col);
                });
                _vessel().$bGroup = colGroup;
                _mafollicle[SheetDef][_currentSheet].bColArr = bodyGroupArr;
                dTable.replaceChild(colGroup, bbGroup);
                _mafollicle[SheetDef][_currentSheet].levelStruct = _.cloneDeep(_header.levelStruct); 
                _cloud.painter.revive();
                _cloud.sidePainter.revive();
                v.construe(_$grid[0], _bodyWrappers, artifactOptions, true);
                _vessel().$bBody = dTable.querySelector("tbody");
                
                let $sumBody = document.createElement("tbody");
                let sumGroupArr = [], $sumGroup = document.createElement("colgroup");
                let $tr = document.createElement("tr");
                $tr.style.height = "27px";
                $sumBody.appendChild($tr);
                
                _.forEach(table.cols, c => {
                    let col = c.cloneNode(true);
                    $sumGroup.appendChild(col);
                    sumGroupArr.push(col);
                });
                
                _.forEach(table.painter.visibleColumns, c => {
                    if (c.hidden) return;
                    let sum = _summaries[c.key]; 
                    let $td = _prtCell.cloneNode();
                    $tr.appendChild($td);
                    
                    if (!sum) return;
                    if (sum.calculator === "Time") {
                        $td.textContent = ti.momentToString(sum[_currentPage]);
                        sum[_currentSheet] = $td;
                    } else if (sum.calculator === "Number") {
                        $td.textContent = sum.formatter === "Currency" ? ti.asCurrency(sum[_currentPage]) : sum[_currentPage];
                        sum[_currentSheet] = $td;
                    } else {
                        $td.textContent = sum.calculator;
                    }
                });
                
                sumTbl.replaceChild($sumGroup, bSumGroup);
                sumTbl.replaceChild($sumBody, bSumBody);
                _vessel().$sumGroup = $sumGroup;
                _vessel().$sumBody = $sumBody;
                _mafollicle[SheetDef][_currentSheet].sumColArr = sumGroupArr;
                
                kt._adjuster.nostal(table.cols, bodyGroupArr, sumGroupArr);
                kt._adjuster.handle(); 
                if (lo.changeZero(_vessel().zeroHidden)) _vessel().zeroHidden = _zeroHidden;         
                return;
            }
            
            _maxFreeWidth = _mafollicle[SheetDef][_currentSheet].maxWidth;
            $header.style.maxWidth = _maxFreeWidth + "px";
            let bw = (_maxFreeWidth + ti.getScrollWidth()) + "px";
            _bodyWrappers[1].style.maxWidth = bw;
            let btmw = (Math.min(parseFloat($header.style.width), parseFloat($header.style.maxWidth)) + _maxFixedWidth + ti.getScrollWidth() + 2) + "px";
            if (sumWrap) {
                sumWrap.style.maxWidth = _maxFreeWidth + "px";
                sumWrap.style.width = $header.style.width;
            }
            
            if (sWrap) {
                sWrap.style.width = btmw;
            }
            
            if (pWrap) {
                pWrap.style.width = btmw;
            }
            _cloud.painter.revive();
            _cloud.sidePainter.revive();
            $headerTbl.replaceChild(_vessel().$hGroup, bhGroup);
            $headerTbl.replaceChild(_vessel().$hBody, bhBody);
            dTable.replaceChild(_vessel().$bGroup, bbGroup);
//            dTable.replaceChild(_vessel().$bBody, dTable.querySelector("tbody"));
            _cloud.renderSideRows(true);
            sumTbl.replaceChild(_vessel().$sumGroup, bSumGroup);
            sumTbl.replaceChild(_vessel().$sumBody, bSumBody);
            kt._adjuster.nostal(_mafollicle[SheetDef][_currentSheet].hColArr, 
                _mafollicle[SheetDef][_currentSheet].bColArr, _mafollicle[SheetDef][_currentSheet].sumColArr);
            kt._adjuster.handle();
            if (lo.changeZero(_vessel().zeroHidden)) _vessel().zeroHidden = _zeroHidden;
        }
    }
    
    export module dkn {
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
        export let controlType = {};
        export let allCheck = {};
        
        export const CONTROL_CLS = "nts-control";
        export const LABEL_CLS = "mlabel";
        export const CBX_CLS = "mcombo";
        export const CBX_ACTIVE_CLS = "mcombo-state-active";

        /**
         * Get control.
         */
        export function getControl(name: string): NtsControlBase {
            switch (name) {
                case TEXTBOX:
                    return textBox();
                case CHECKBOX:
                    return checkBox;
//                case SWITCH_BUTTONS:
//                    return new SwitchButtons;
                case COMBOBOX:
                    return comboBox;
                case BUTTON:
                    return button;
                case DELETE_BUTTON:
                    return deleteButton;
//                case TEXT_EDITOR:
//                    return new TextEditor;
                case LINK_LABEL:
                    return linkLabel;
                case FLEX_IMAGE:
                    return flexImage;
                case IMAGE:
                    return image;
            }
        }
        
        /**
         * Textbox.
         */
        export function textBox(key: string) {
            let control = controlType[TEXTBOX];
            controlType[key] = TEXTBOX;
            if (control) {
                return;
            }
            let $editContainer = document.createElement("div"); 
            $editContainer.classList.add("medit-container");
            $editContainer.style.height = (BODY_ROW_HEIGHT - 3) + "px";
            let $editor = document.createElement("input");
            $editor.classList.add("medit");
            $editContainer.appendChild($editor);
            controlType[TEXTBOX] = { my: $editContainer, type: TEXTBOX };
            $editor.addXEventListener(ssk.KEY_DOWN, evt => {
                if (ti.isEnterKey(evt) || ti.isTabKey(evt)) {
                    let grid = ti.closest($editor, "." + MGRID);
                    su.endEdit(grid);
                }
                
                if (ti.isArrowLeft(evt) || ti.isArrowRight(evt) || ti.isArrowUp(evt) || ti.isArrowDown(evt)) {
                    evt.stopPropagation();
                }
            });
            
            $editor.addXEventListener(ssk.KEY_UP, evt => {
                let $td = ti.closest($editor, "td." + v.CELL_CLS);
                if ($td) {
                    let coord = ti.getCellCoord($td);
                    let validator = _validators[coord.columnKey];
                    if (!validator) return;
                    let result = validator.probe($editor.value); 
                    let cell = { id: _dataSource[coord.rowIdx][_pk], index: coord.rowIdx, columnKey: coord.columnKey, element: $td };
                    khl.clear(cell);
                    
                    if (!result.isValid) {
                        khl.set(cell, result.errorMessage);
                    }
                }
            });
        }

        /**
         * Checkbox.
         */
        function checkBox(data: any): HTMLElement {
            let checkBoxText: string;
            let setChecked = data.update;
            let initValue = data.initValue;
            let $wrapper = document.createDocumentFragment();
            let text = data.controlDef.options[data.controlDef.optionsText];
            checkBoxText = text;
            
            let $checkBoxLabel = document.createElement("label");
            $checkBoxLabel.classList.add("ntsCheckBox");
            let $checkBox = document.createElement("input");
            $checkBox.setAttribute("type", "checkbox");
            $checkBox.addXEventListener("change", function(evt) {
                let checked = $checkBox.checked || evt.checked ? true : false;
                if (checked) {
                    $checkBox.setAttribute("checked", "checked");
                } else {
                    $checkBox.removeAttribute("checked");
                }
                setChecked(checked, null, evt.resetValue);
            });
            $checkBoxLabel.appendChild($checkBox);
            
            let $box = document.createElement("span");
            $box.classList.add("box");
            $checkBoxLabel.appendChild($box);
            
            if (checkBoxText && checkBoxText.length > 0) {
                let span = document.createElement("span");
                span.classList.add("label");
                span.innerHTML = checkBoxText;
                $checkBoxLabel.appendChild(span);
            }
            
            $wrapper.appendChild($checkBoxLabel);
            let checked = initValue !== undefined ? initValue : true;

            if (checked === true) $checkBox.setAttribute("checked", "checked");
            else $checkBox.removeAttribute("checked");
            if (data.enable === true) $checkBox.removeAttribute("disabled");
            else $checkBox.setAttribute("disabled", "disabled");
            if (!controlType[data.columnKey]) {
                controlType[data.columnKey] = CHECKBOX;
            }
            
            return $wrapper;
        }
        
        /**
         * Combobox.
         */
        export function comboBox(data: any): HTMLElement {
            let result = "", control = controlType[data.columnKey];
            if (control) {
                _.forEach(data.controlDef.options, i => {
                    let val = i[data.controlDef.optionsValue];
                    if (val === data.initValue) {
                        result = { code: val, name: i[data.controlDef.optionsText] };
//                        result = createItem(_prtDiv, val, i[data.controlDef.optionsText]);
                        return false;
                    }
                });
                
                return result;
            }
            
            let comboDiv = document.createElement("div");
            let $comboWrapper = comboDiv.cloneNode();
            $comboWrapper.classList.add("mcombo-wrapper");
            if (!data.controlDef.width) {
                $comboWrapper.style.width = "100%";
//                $comboWrapper.style.top = "-1px";
            } else {
                $comboWrapper.style.width = data.controlDef.width;
            }
            let $combo = comboDiv.cloneNode();
            $combo.classList.add(CBX_CLS);
            $combo.classList.add("ui-state-default");
            $comboWrapper.appendChild($combo);
            
            let $comboBtn = comboDiv.cloneNode();
            $comboBtn.classList.add("mcombo-button");
            $comboBtn.classList.add("ui-state-default");
            let $comboBtnIcon = comboDiv.cloneNode();
            $comboBtnIcon.classList.add("mcombo-buttonicon");
            $comboBtnIcon.classList.add("ui-icon-triangle-1-s");
            $comboBtnIcon.classList.add("ui-icon");
            $comboBtn.appendChild($comboBtnIcon);
            
            let $comboValue = comboDiv.cloneNode();
            $comboValue.classList.add("mcombo-value");
            $combo.appendChild($comboBtn);
            $combo.appendChild($comboValue);
            
            let $comboDropdown = comboDiv.cloneNode();
            $comboDropdown.classList.add("mcombo-dropdown");
            document.body.appendChild($comboDropdown);
            
            $combo.addXEventListener(ssk.CLICK_EVT, function(evt) {
                if ($comboDropdown.style.height === "" || $comboDropdown.style.height === "0px") {
                    openDD($comboDropdown, $comboWrapper);
                    $combo.classList.add(CBX_ACTIVE_CLS);
                } else {
                    closeDD($comboDropdown);
                    $combo.classList.remove(CBX_ACTIVE_CLS);
                }
            });
            
            let $comboList = comboDiv.cloneNode();
            $comboList.classList.add("mcombo-list");
            $comboDropdown.appendChild($comboList);
            let $itemHolder = document.createElement("ul");
            $itemHolder.classList.add("mcombo-listitemholder");
            $comboList.appendChild($itemHolder);
            
            let val, textContent, maxHeight = 0, itemList = [], controlDef = data.controlDef;
            _.forEach(data.controlDef.options, i => {
                let $item = document.createElement("li");
                $item.classList.add("mcombo-listitem");
                $item.classList.add("ui-state-default");
                val = i[controlDef.optionsValue];
                $.data($item, "value", val);
//                textContent = text.padRight(val, ' ', 6) + i[controlDef.optionsText];
//                $item.textContent = textContent;
                // Create columns
                let $comboItem = createItem(comboDiv, val, i[controlDef.optionsText], $item);
                
                if (data.initValue === val) {
                    let initItem = $comboItem.cloneNode(true);
                    $comboValue.appendChild(initItem);
                    $item.classList.add("selecteditem");
                    result = { code: val, name: i[controlDef.optionsText] };
                }
                
                $item.addXEventListener(ssk.CLICK_EVT, evt => {
                    $comboValue.innerHTML = "";
                    $comboValue.appendChild($comboItem.cloneNode(true));
                    _.forEach(itemList, i => {
                        if (i.classList.contains("selecteditem")) {
                            i.classList.remove("selecteditem");
                        } 
                    });
                    
                    let value = $.data($item, lo.CBX_ITEM_VALUE);
                    $.data($comboWrapper, lo.CBX_SELECTED, value); 
                    $item.classList.add("selecteditem");
                    let $cbxCell = ti.closest($comboWrapper, "." + v.CELL_CLS);
                    if ($cbxCell) {
                        $.data($cbxCell, lo.CBX_SELECTED_TD, value);
                    }
                    closeDD($comboDropdown);
                    $combo.classList.remove(CBX_ACTIVE_CLS);
                    let coord = ti.getCellCoord($cbxCell);
                    data.update(value, coord.rowIdx);
                    let sCol = _specialColumn[data.columnKey];
                    if (sCol) {
                        let $cCell = lch.cellAt(_$grid[0], coord.rowIdx, sCol);
                        if ($cCell) {
                            let column = _columnsMap[sCol];
                            let formatted = su.format(column[0], value); 
                            $cCell.textContent = formatted;
                            su.wedgeCell(_$grid[0], { rowIdx: coord.rowIdx, columnKey: sCol },  value);
                            $.data($cCell, v.DATA, value);
                            khl.clear({ id: _dataSource[coord.rowIdx][_pk], columnKey: sCol, element: $cCell });
                        }
                    }
                });
                $itemHolder.appendChild($item);
                itemList.add($item);
                maxHeight += 26;
            });
            
            $comboDropdown.style.maxHeight = (maxHeight + 2) + "px";
            controlType[data.columnKey] = { my: $comboWrapper, dropdown: $comboDropdown, options: data.controlDef.options, type: COMBOBOX };
            return result;
        }
        
        /**
         * OpenDD.
         */
        export function openDD($dd, $w) {
            let offset = selector.offset($w);
            $dd.style.top = (offset.top + BODY_ROW_HEIGHT - 2) + "px";
            $dd.style.left = offset.left + "px";
            $dd.style.width = $w.offsetWidth + "px";
            $dd.style.height = $dd.style.maxHeight;
        };
        
        /**
         * CloseDD.
         */
        export function closeDD($dd) {
            $dd.style.height = "0px";
//            setTimeout(() => {
                $dd.style.top = "-99999px";
                $dd.style.left = "-99999px";
//            }, 120);
        }
        
        /**
         * Create item.
         */
        function createItem(comboDiv: HTMLElement, code: any, name: any, $item?: HTMLElement) {
            let $comboItem = comboDiv.cloneNode();
            $comboItem.classList.add("mcombo-item");
            if ($item) $item.appendChild($comboItem);
            let $itemCode = comboDiv.cloneNode();
            $itemCode.classList.add("mcombo-item-column");
            $itemCode.style.width = "25%";
            $itemCode.style.float = "left";
            $itemCode.textContent = code;
            let $itemName = comboDiv.cloneNode();
            $itemName.classList.add("mcombo-item-column");
            $itemName.style.width = "75%";
            $itemName.textContent = name;
            $comboItem.appendChild($itemCode);
            $comboItem.appendChild($itemName);
            return $comboItem;
        }

        /**
         * Button.
         */
        function button(data: any): HTMLElement {
            let $container = document.createDocumentFragment();
            let $button = document.createElement("button");
            $button.classList.add("mbutton");
            $container.appendChild($button);
            $button.innerHTML = data.controlDef.text || data.initValue;
            $button.setAttribute("tabindex", -1)
            $.data($button, "enable", data.enable);
            let clickHandle = data.controlDef.click.bind(null, data.rowObj);
            $.data($button, ssk.CLICK_EVT, clickHandle);
            if (data.enable) $button.addXEventListener("click", clickHandle);
            $button.disabled = !data.enable;
            if (!controlType[data.columnKey]) {
                controlType[data.columnKey] = BUTTON;
            }
            
            return $container;
        }
        
        /**
         * Delete button.
         */
        function deleteButton(data: any): HTMLElement {
            let btnContainer = button(data);
            let btn = btnContainer.querySelector("button");
            btn.removeXEventListener("click", data.controlDef.click);
            btn.addXEventListener("click", data.deleteRow);
            return btnContainer;
        }
            
        /**
         * LinkLabel.
         */
        function linkLabel(data: any): HTMLElement {
            let $container = document.createDocumentFragment();
            let $link = document.createElement("a");
            $link.classList.add("mlink-button");
            $link.textContent = data.initValue;
            let clickHandle = data.controlDef.click.bind(null, data.rowId, data.columnKey);
            
            if (data.enable) {
                $link.addXEventListener("click", clickHandle);
            } else $link.style.color = "#333";
            $.data($link, "click", clickHandle);
            $container.appendChild($link);
            
            if (!controlType[data.columnKey]) {
                controlType[data.columnKey] = LINK_LABEL;
            }
            
            return $container;
        }
            
        /**
         * FlexImage.
         */
        function flexImage(data: any): HTMLElement {
            let $container = document.createDocumentFragment();
            if (_.isNil(data.initValue) || _.isEmpty(data.initValue)) {
                if (!controlType[data.columnKey]) {
                    controlType[data.columnKey] = FLEX_IMAGE;
                }
                return $container;
            }
            let $image = document.createElement("span");
            $image.className = data.controlDef.source;
            
            if (data.controlDef.click && _.isFunction(data.controlDef.click)) {
                let clickHandle = data.controlDef.click.bind(null, data.columnKey, data.rowId);
                if (data.enable) {
                    $image.addXEventListener(ssk.CLICK_EVT, clickHandle);
                    $image.style.cursor = "pointer";
                }
                $.data($image, ssk.CLICK_EVT, clickHandle);
            }
            
            $container.appendChild($image);
            if (!controlType[data.columnKey]) {
                controlType[data.columnKey] = FLEX_IMAGE;
            }
            
            return $container;
        }
            
        /**
         * Image.
         */
        function image(data: any): HTMLElement {
            let $container = document.createDocumentFragment();
            let $span = document.createElement("span");
            $span.classList.add(data.controlDef.source);
            $container.appendChild($span);
            
            if (!controlType[data.columnKey]) {
                controlType[data.columnKey] = IMAGE;
            }
            
            return $container;
        }
    }
    
    module lch {
        
        export const CELL_SELECTED_CLS = "cell-selected";
        
        export class Cell {
            rowIndex: any;
            columnKey: any;
            value: any;
            
            constructor(rowIdx: any, columnKey: any, value: any) {
                this.rowIndex = rowIdx;
                this.columnKey = columnKey;
                this.value = value;
            }
        }
        
        /**
         * Checkup.
         */
        export function checkUp($grid: HTMLElement) {
            let isSelecting;
            
            $grid.addXEventListener(ssk.MOUSE_DOWN, function(evt: any) {
                let $target = evt.target;
                if (!selector.is($target, ".mcell")) return;
                isSelecting = true;
                
                window.addXEventListener(ssk.MOUSE_UP + ".block", function(evt: any) {
                    isSelecting = false;
                    $grid.onselectstart = null;
                    window.removeXEventListener(ssk.MOUSE_UP + ".block");
                });
                
                if (evt.shiftKey) {
                    selectRange($grid, $target);
                    $grid.onselectstart = function() {
                        return false;
                    };
                    return;
                }
                
                if (!evt.ctrlKey) {
                    clearAll($grid);
                }
                selectCell($grid, $target);
            });
            
            $grid.addXEventListener(ssk.MOUSE_UP, function(evt: any) {
                isSelecting = false;
                $grid.onselectstart = null;
            });
            
            $grid.addXEventListener(ssk.MOUSE_MOVE, function(evt: any) {
                if (isSelecting) {
                    selectRange($grid, evt.target);
                }
            });
        }
        
        /**
         * Select range.
         */
        function selectRange($grid: HTMLElement, $cell: HTMLElement) {
            if (_.isNil($cell) || !selector.is($cell, "td.mcell")) return;
            let lastSelected = $.data($grid, lo.LAST_SELECT);
            if (!lastSelected) { 
                selectCell($grid, $cell);
                return;
            }
            
            clearAll($grid);
            let toCoord = ti.getCellCoord($cell); 
            let minRowIdx = Math.min(lastSelected.rowIdx, toCoord.rowIdx);
            let maxRowIdx = Math.max(lastSelected.rowIdx, toCoord.rowIdx);
            for (let i = minRowIdx; i < maxRowIdx + 1; i++) { 
                cellInRange($grid, i, lastSelected.columnKey, toCoord.columnKey);
            }
        }
        
        /**
         * Mark cell.
         */
        export function markCell($cell: HTMLElement) {
            if ($cell && selector.is($cell, "td.mcell")) {
                $cell.classList.add(CELL_SELECTED_CLS);
                return true;
            }
            return false;
        }
        
        /**
         * Select cell.
         */
        export function selectCell($grid: HTMLElement, $cell: HTMLElement, notLast?: boolean) {
            if (!markCell($cell)) return;
            let coord = ti.getCellCoord($cell);
            addSelect($grid, coord.rowIdx, coord.columnKey, notLast);
            
            if (ti.isChrome() && (!_fixedColumns || !_.some(_fixedColumns, c => c.key === coord.columnKey))) {
                if (!_bodyWrappers || _bodyWrappers.length === 0) return;
                $grid.focus({ preventScroll: true });
                let wrapper = _bodyWrappers[_bodyWrappers.length > 1 ? 1 : 0];
                let offsetLeft = $cell.offsetLeft, left = offsetLeft + $cell.offsetWidth,
                    scrollLeft = wrapper.scrollLeft, width = scrollLeft  + parseFloat(wrapper.style.width); 
//                let scroll = function() {
//                    wrapper.addXEventListener(ssk.SCROLL_EVT + ".select", e => {
//                        setTimeout(() => {
//                            $cell.focus();
//                        }, 100);
//                        wrapper.removeXEventListener(ssk.SCROLL_EVT + ".select");
//                    });
//                };
                
                if (left > width) {
//                    scroll();
                    wrapper.scrollLeft += (left - width + 100);
                } else if (offsetLeft < scrollLeft) {
//                    scroll();
                    wrapper.scrollLeft -= (scrollLeft - offsetLeft + 100)
                } else {
                    $cell.focus({ preventScroll: true });
                }
            } else $cell.focus();
        }
        
        /**
         * Add select.
         */
        export function addSelect($grid: HTMLElement, rowIdx: any, columnKey: any, notLast?: boolean) {
            let selectedCells = _selected;
            if (!notLast) $.data($grid, lo.LAST_SELECT, { rowIdx: rowIdx, columnKey: columnKey });
            if (!selectedCells) {
                selectedCells = {};
                selectedCells[rowIdx] = [ columnKey ];
                _selected = selectedCells;
                return;
            }
            
            if (!selectedCells[rowIdx]) {
                selectedCells[rowIdx] = [ columnKey ];
                return;
            }
            
            if (_.find(selectedCells[rowIdx], function(key) {
                return key === columnKey;
            }) === undefined) {
                selectedCells[rowIdx].push(columnKey);
            }
        }
        
        /**
         * Clear.
         */
        export function clear($grid: HTMLElement, rowIdx: any, columnKey: any) {
            let selectedCells = _selected;
            if (!selectedCells) return;
            let row = selectedCells[rowIdx];
            if (!row || row.length === 0) return;
            let colIdx;
            _.forEach(row, function(key: any, index: number) {
                if (key === columnKey) {
                    colIdx = index;
                    return false;
                }
            });
            
            if (_.isNil(colIdx)) return;
            row.splice(colIdx, 1);
            let selectedCell = cellAt($grid, rowIdx, columnKey);
            if (selectedCell === null) return;
            if (selectedCell) {
                ti.removeClass(selectedCell, CELL_SELECTED_CLS);
            }
        }
        
        /**
         * Clear all.
         */
        export function clearAll($grid: HTMLElement) {
            let selectedCells = _selected;
            if (!selectedCells) return;
            _.forEach(Object.keys(selectedCells), function(rowIdx: any, index: number) {
                _.forEach(selectedCells[rowIdx], function(col: any, i: number) {
                    let $cell = cellAt($grid, rowIdx, col);
                    if ($cell) {
                        ti.removeClass($cell, CELL_SELECTED_CLS);
                    }
                });
            });
            
            if (!_selected) return;
            _.forEach(Object.keys(_selected), p => {
                if (_selected.hasOwnProperty(p)) delete _selected[p];
            });
        }
        
        /**
         * Cell at.
         */
        export function cellAt($grid: HTMLElement, rowIdx: any, columnKey: any, desc?: any) {
            let rowArr = rowAt($grid, rowIdx, desc);
            return getCellInRow(rowArr, columnKey);
        }
        
        /**
         * Row at.
         */
        export function rowAt($grid: HTMLElement, rowIdx: any, desc?: any) : Array<HTMLElement> {
            if (!desc) desc = _mDesc;
            let fixed, row;
            if (!desc || !desc.rows || !(row = desc.rows[rowIdx])) {
                return null;
            }
            
            if (desc.fixedRows && (fixed = desc.fixedRows[rowIdx])) {
                row = _.concat(fixed, row);
            } 
            return row;
        }
        
        /**
         * Cell in range.
         */
        export function cellInRange($grid: HTMLElement, rowIdx: any, startKey: any, endKey: any) {
            let range = [];
            let rowArr = rowAt($grid, rowIdx);
            let desc = _mDesc;
            if (_.isNil(rowArr) || _.isNil(desc)) return;
            let start, end, fixedCount = 0;
            if (desc.fixedColIdxes) {
                start = desc.fixedColIdxes[startKey];
                end = desc.fixedColIdxes[endKey];
                fixedCount = Object.keys(desc.fixedColIdxes).length;
            }
            
            if (_.isNil(start)) {
                start = desc.colIdxes[startKey] + fixedCount;
            }
            
            if (_.isNil(end)) {
                end = desc.colIdxes[endKey] + fixedCount;
            }
            
            if (_.isNil(start) || _.isNil(end)) return;
            let min = Math.min(start, end);
            let max = Math.max(start, end);
            _.forEach(rowArr, (c, index) => {
                if (c.style.display !== "none" && index >= min && index <= max) {
                    ti.addClass(c, CELL_SELECTED_CLS);
                    let coord = ti.getCellCoord(c);
                    
                    if (coord) {
                        addSelect($grid, rowIdx, coord.columnKey, true);
                        range.push(c);
                    }
                } else if (index > max) return;
            });
            
            return range;
        }
        
        /**
         * Get cell in row.
         */
        export function getCellInRow(rowArr: any, columnKey: any) {
            if (_.isNil(rowArr)) return null;
            
            return _.find(rowArr, c => {
                if (c.style.display === "none") return false;
                let coord = ti.getCellCoord(c);    
                if (coord.columnKey === columnKey) return true;
            });
        }
        
        /**
         * Get selected cells.
         */
        export function getSelectedCells($grid: HTMLElement) {   
            let selectedCells = _selected;
            let desc = _mDesc;
            let dataSource = _dataSource;
            let cells = [];
            let arr = _.sortBy(_.keys(selectedCells), r => parseFloat(r));
            _.forEach(arr, function(rowIdx: any) {
                _.forEach(selectedCells[rowIdx], function(colKey: any) {
                    cells.push(new Cell(rowIdx, colKey, dataSource[rowIdx][colKey]));
                });
            });
            return cells;
        }
        
        /**
         * Select next.
         */
        export function selectNext($grid: HTMLElement, direct?: boolean) {
            let selectedCells = _selected;
            let keys = Object.keys(selectedCells);
            if (!selectedCells || keys.length === 0) return;
            let sortedKeys = keys.sort((o, t) => o - t);
            let cell, nCell, colElms, key, fixedCount = 0, dCount = 0, colIdx, desc = _mDesc, ds = _dataSource, tRowIdx = parseFloat(sortedKeys[0]);
            if (_.isNil(tRowIdx)) return;
            
            if (desc.fixedColIdxes) {
                fixedCount = Object.keys(desc.fixedColIdxes).length;
            }
            
            colIdx = _.min(_.map(selectedCells[tRowIdx], c => {
                let idx, isFixed = true;
                if (desc.fixedColIdxes) {
                    idx = desc.fixedColIdxes[c];
                }
                
                if (_.isNil(idx)) {
                    idx = desc.colIdxes[c];
                    isFixed = false;
                }
                
                return isFixed ? idx : idx + fixedCount;
            }));
            
            dCount = Object.keys(desc.colIdxes).length;
            
            if (direct === "below") {
                if (ds && parseInt(tRowIdx) === ds.length - 1) {
                    tRowIdx = 0;
                    if (fixedCount + dCount === colIdx + 1) {
                        colIdx = 0;
                    } else colIdx++;
                } else {
                    tRowIdx++;
                }
            } else if (fixedCount + dCount === colIdx + 1) {
                colIdx = 0;
                if (ds && parseInt(tRowIdx) === ds.length - 1) {
                    tRowIdx = 0;
                } else {
                    tRowIdx++;
                }
            } else {
                colIdx++;
            }
                
            colElms = rowAt($grid, tRowIdx);
            nCell = colElms[colIdx];
            clearAll($grid);
            
            if (tc.visualJumpTo($grid, tRowIdx)) {
                setTimeout(() => {
                    selectCell($grid, nCell), 1
                    if (nCell.style.display === "none") selectNext($grid, direct);
                });
                return;
            }
            
            selectCell($grid, nCell);
            if (nCell.style.display === "none") selectNext($grid, direct);
        }
        
        /**
         * Select prev.
         */
        export function selectPrev($grid: HTMLElement, direct?: boolean) {
            let selectedCells = _selected;
            let keys = Object.keys(selectedCells);
            if (!selectedCells || keys.length === 0) return;
            let sortedKeys = keys.sort((o, t) => o - t);
            let cell, nCell, colElms, key, fixedCount = 0, dCount = 0, colIdx, desc = _mDesc, ds = _dataSource, tRowIdx = parseFloat(sortedKeys[0]);
            if (_.isNil(tRowIdx)) return;
            
            if (desc.fixedColIdxes) {
                fixedCount = Object.keys(desc.fixedColIdxes).length;
            }
            
            colIdx = _.min(_.map(selectedCells[tRowIdx], c => {
                let idx, isFixed = true;
                if (desc.fixedColIdxes) {
                    idx = desc.fixedColIdxes[c];
                }
                
                if (_.isNil(idx)) {
                    idx = desc.colIdxes[c];
                    isFixed = false;
                }
                
                return isFixed ? idx : idx + fixedCount;
            }));
            
            dCount = Object.keys(desc.colIdxes).length;
            
            if (direct === "below") {
                if (tRowIdx === 0) {
                    tRowIdx = ds.length - 1;
                    if (colIdx === 0) {
                        colIdx = fixedCount + dCount - 1;
                    } else colIdx--;
                } else {
                    tRowIdx--;
                }
            } else if (colIdx === 0) {
                colIdx = fixedCount + dCount - 1;
                if (tRowIdx === 0) {
                    tRowIdx = ds.length - 1;
                } else {
                    tRowIdx--;
                }
            } else {
                colIdx--;
            }
                
            colElms = rowAt($grid, tRowIdx);
            nCell = colElms[colIdx];
            clearAll($grid);
            
            if (tc.visualJumpTo($grid, tRowIdx)) {
                setTimeout(() => selectCell($grid, nCell), 1);
                if (nCell.style.display === "none") selectPrev($grid, direct);
                return;
            }
            
            selectCell($grid, nCell);
            if (nCell.style.display === "none") selectPrev($grid, direct);
        }
    }
    
    module hpl {
        export const VALIDATORS = "mValidators"; 
        export const CURRENCY_CLS = "currency-symbol";
        let H_M_MAX: number = 60;
        
        export class ColumnFieldValidator {
            parentName: string;
            name: string;
            primitiveValue: string;
            options: any;
            constructor(parentName: string, name: string, primitiveValue: string, options: any) {
                this.parentName = parentName;
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
                        return new NumberValidator(this.name, valueType, this.primitiveValue, this.options, this.parentName) 
                               .validate(value);
                    case "Currency":
                        let opts: any = new ui.option.CurrencyEditorOption();
                        opts.grouplength = this.options.groupLength | 3;
                        opts.decimallength = _.isNil(this.options.decimalLength) ? 0 : this.options.decimalLength;
                        opts.currencyformat = this.options.currencyFormat ? this.options.currencyFormat : "JPY";
                        if (!_.isNil(this.options.min)) opts.min = this.options.min;
                        if (!_.isNil(this.options.max)) opts.max = this.options.max;
                        if (!_.isNil(this.options.required)) opts.required = this.options.required;
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
                    case "StandardTimeWithDay":
                        this.options.timeWithDay = true;
                        let result = new TimeWithDayValidator(this.name, this.primitiveValue, this.options)
                                        .validate(value);
                        if (result.isValid) {
                            let formatter = new text.TimeWithDayFormatter(this.options);
                            result.parsedValue = formatter.format(result.parsedValue);
                        }
                        return result;
                    case "TimeWithDay":
                        this.options.timeWithDay = true;
                        let result = new nts.uk.ui.validation.TimeWithDayValidator(this.name, this.primitiveValue, this.options)
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
            parentName: string;
            name: string;
            displayType: string;
            primitiveValue: string;
            options: any;
            constructor(name: string, displayType: string, primitiveValue?: string, options?: any, parentName?: any) {
                this.parentName = parentName;
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
                        result.fail(nts.uk.resource.getMessage('MsgB_1', [ self.name ]), 'MsgB_1');
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
                } else if (self.displayType === "Decimal") {
                    isValid = ntsNumber.isNumber(text, true, self.options, message);
                } else if (self.displayType === "Currency") {
                    isValid = ntsNumber.isNumber(text, false, self.options, message);
                    if (_.indexOf(text, ".") > -1) isValid = false;
                }
                
                let min = 0, max = 999999999;
                let value = parseFloat(text);
                
                if (self.options.values && !_.some(self.options.values, v => v === value)) {
                    result.fail(resource.getMessage("Msg_1443", [ self.parentName ]), "Msg_1443");
                    return result;
                }
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
        
        let MAX_VALUE = time.minutesBased.duration.parseString("71:59"),
            MIN_VALUE = time.minutesBased.duration.parseString("-12:00");
        
        class TimeWithDayValidator {
            name: string;
            constraint: any;
            required: boolean; 
            constructor(name: string, primitiveValueName: string, option?: any) {
                this.name = name;
                this.constraint = ui.validation.getConstraint(primitiveValueName);
                if (_.isNil(this.constraint)) {
                    this.constraint = {};
                    if (option && !_.isNil(option.min)) {
                        this.constraint.min = option.min;
                    }
                    if (option && !_.isNil(option.max)) {
                        this.constraint.max = option.max;
                    }
                }
                this.required = (option && option.required) ? option.required : false;
            }
    
            validate(inputText: string): any {
                let self = this;
                let result = new ui.validation.ValidationResult();
                
                if (util.isNullOrEmpty(inputText)) {
                    if (this.required) {
                        result.fail(nts.uk.resource.getMessage('MsgB_1', [ this.name ]), 'MsgB_1');
                    } else result.success("");
                    return result;
                }
                
                let minValue, maxValue, minParsed, maxParsed, parsedValue;
                if (!_.isNil(self.constraint.min)) { 
                    minParsed = time.minutesBased.duration.parseString(self.constraint.min);
                    if (minParsed.success) {
                        minValue = minParsed.toValue();
                    }
                } else minValue = MIN_VALUE.toValue();
                
                if (!_.isNil(self.constraint.max)) {
                    maxParsed = time.minutesBased.duration.parseString(self.constraint.max);
                    if (maxParsed.success) {
                        maxValue = maxParsed.toValue();
                    }      
                } else maxValue = MAX_VALUE.toValue();
                
                let parsed = time.minutesBased.duration.parseString(inputText);
                if (!parsed.success || (parsedValue = parsed.toValue()) !== Math.round(parsedValue) 
                    || parsedValue < minValue || parsedValue > maxValue) {
                    result.fail(nts.uk.resource.getMessage("FND_E_CLOCK", [ self.name, minParsed.format(), maxParsed.format() ]), "FND_E_CLOCK");
                } else {
                    result.success(parsedValue);
                }
                
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
        
        /**
         * Parse time.
         */
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
        
        /**
         * Get value type.
         */
        export function getValueType(columnKey: any) {
            if (!_validators || !_validators[columnKey]) return;
            let column = _validators[columnKey];
            return column.primitiveValue ? ui.validation.getConstraint(column.primitiveValue).valueType
                                : column.options.cDisplayType
        }
        
        /**
         * Get group separator.
         */
        export function getGroupSeparator(columnKey: any) {
            if (!_validators || !_validators[columnKey]) return;
            return _validators[columnKey].options.groupseperator;
        }
    }
    
    module khl {
        export let ERROR_CLS = "merror";
        
        export class GridCellError {
            grid: JQuery = _$grid;
            index: any;
            rowId: any;
            columnKey: string;
            columnName: string;
            message: string;
            
            constructor(index: any, rowId: any, columnKey: any, message: string) {
                this.index = index;
                this.rowId = rowId;
                this.columnKey = columnKey;
                this.message = message;
                let col = _columnsMap[this.columnKey];
                if (col) this.columnName = col[0].headerText; 
            }
            
            equals(err: GridCellError) {
                if (this.index !== err.index) return false;
                if (this.rowId !== err.rowId) return false;
                if (this.columnKey !== err.columnKey) return false;
                return true;
            }
        }
        
        /**
         * Add error.
         */
        function addCellError(error: any) {
            if (_errors.some(function(e: GridCellError) {
                return e.equals(error);
            })) return;
            
            _errors.push(error);
        }
    
        /**
         * Remove error.
         */
        function removeCellError(rowId: any, key: any) {
            _.remove(_errors, function(e) {
                return rowId === e.rowId && key === e.columnKey;
            });
        }
        
        /**
         * Set.
         */
        export function set(cell: any, message: string) {
            if (!cell || !cell.element || any(cell)) return;
            let $cell = cell.element;
            $cell.classList.add(ERROR_CLS);
            let errorDetails = createErrorInfos(cell, message);
            if (_errorsOnPage) {
                ui.errors.addCell(errorDetails);
            }
            addCellError(errorDetails);
        }
        
        /**
         * Create error infos.
         */
        function createErrorInfos(cell: any, message: string): any {
            let record: any = _dataSource[cell.index];
            let error: any = new GridCellError(cell.index, cell.id, cell.columnKey, message);
            // Error column headers
            let headers;
            if (_errorsOnPage) {
                let columns = ko.toJS(ui.errors.errorsViewModel().option().headers());
                if (columns) {
                    headers = columns.filter(c => c.visible).map(c => c.name); 
                }
            } else { 
                headers = _errorColumns;
            }
            _.forEach(headers, function(header: any) {
                if (_.isNil(record[header]) 
                    || !_.isNil(error[header])) return;
                error[header] = record[header];
            });
            return error;
        } 
        
        /**
         * Clear.
         */
        export function clear(cell: any) {
            if (!cell || !cell.element || !any(cell)) return;
            let $cell = cell.element;
            $cell.classList.remove(ERROR_CLS);
            if (_errorsOnPage) {
                ui.errors.removeCell(_$grid, cell.id, cell.columnKey);
            }
            removeCellError(cell.id, cell.columnKey);
        }
        
        /**
         * Any.
         */
        export function any(cell: any) {
            return cell.element && cell.element.classList.contains(ERROR_CLS);
        }
        
        /**
         * Cell equals.
         */
        function cellEquals(one: any, other: any) {
            if (one.columnKey !== other.columnKey) return false;
            if (one.id !== other.id) return false;
            return true;
        }
    }
    
    module selector {
        
        /**
         * Find.
         */
        export function find(p: HTMLElement, sel: any) {
            return new Manipulator().addNodes(p.querySelectorAll(sel));
        }
        
        /**
         * Create.
         */
        export function create(str: any) {
            return new Manipulator().addElement(document.createElement(str));
        }
        
        /**
         * Is.
         */
        export function is(el, sel) {
            let matches = el.matches || el.matchesSelector || el.msMatchesSelector || el.mozMatchesSelector || el.webkitMatchesSelector || el.oMatchesSelector;
            if (matches) return matches.call(el, sel);
            return $(el).is(sel);
        }
        
        /**
         * Index.
         */
        export function index(el) {
            return Array.prototype.slice.call(el.parentNode.children).indexOf(el);
        }
        
        /**
         * Query all.
         */
        export function queryAll(el, sel): Array<HTMLElement> {
            return Array.prototype.slice.call(el.querySelectorAll(sel));
        }
        
        /**
         * Offset.
         */
        export function offset(el) {
            let rect = el.getBoundingClientRect();
            return {
              top: rect.top + document.body.scrollTop,
              left: rect.left + document.body.scrollLeft
            }
        }
        
        /**
         * Class siblings.
         */
        export function classSiblings(node: any, clazz: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            let results = [];
            for (let i = 0; i < children.length; i++) {
                if (children[i] === node) continue;
                let classList = children[i].classList;
                for (let j = 0; j < classList.length; j++) {
                    if (classList.item(j) === clazz) {
                        results.push(children[i]);
                        break;
                    }
                }
            }
            
            return results;
        }
        
        /**
         * Sibling lt.
         */
        export function siblingsLt(el, index) {
            let parent = el.parentNode;
            if (!parent) return;
            let children = parent.children;
            let results = [];
            for (let i = 0; i < children.length; i++) {
                if (i < index) {
                    if (children[i] !== el) results.push(children[i]);
                } else return results;
            }
        }
        
        export class Manipulator {
            elements: Array<HTMLElement>;
                
            addNodes(nodes: NodeList) {
                if (!nodes || nodes.length === 0) return;
                this.elements = Array.prototype.slice.call(self.elements);
                return this;
            }
            
            addElements(elements: Array<HTMLElement>) {
                this.elements = elements;
                return this;
            }
            
            addElement(element: HTMLElement) {
                if (!this.elements) this.elements = [];
                this.elements.push(element);
                return this;
            }
            
            html(str) {
                this.elements.forEach(function(e) {
                    e.innerHTML = str;
                });
                return this;
            }
            
            width(w) {
                let self = this;
                this.elements.forEach(function(e) {
                    e.style.width = parseInt(w) + "px";
                });
                return this;
            }
            
            height(h) {
                let self = this;
                this.elements.forEach(function(e) {
                    e.style.height = parseInt(h) + "px";
                });
                return this;
            }
            
            data(name: any, value: any) {
                this.elements.forEach(function(e) {
                    $.data(e, name, value);
                });
                return this;
            }
            
            addClass(clazz: any) {
                this.elements.forEach(function(e) {
                    e.classList.add(clazz);
                });
                return this;
            }
            
            css(style: any) {
                let text = "; ";
                Object.keys(style).forEach(function(k) {
                    if (k === "maxWidth") {
                        text += ("max-width: " + style[k] + "; ");
                        return;
                    }
                    text += k + ": " + style[k] + "; ";
                });
                
                this.elements.forEach(function(e) {
                    e.style.cssText = text;
                });
                
                return this;
            }
            
            getSingle() {
                return this.elements[0];
            }
            
            get() {
                return this.elements;
            }
        }
    }
    
    export module color {
        export const Error = "mgrid-error";
        export const Alarm = "mgrid-alarm";
        export const ManualEditTarget = "mgrid-manual-edit-target";
        export const ManualEditOther = "mgrid-manual-edit-other";
        export const Reflect = "mgrid-reflect";
        export const Calculation = "mgrid-calc";
        export const Disable = "mgrid-disable";
        export const HOVER = "ui-state-hover";
        export const ALL = [ Error, Alarm, ManualEditTarget, ManualEditOther, Reflect, Calculation, Disable ];
        
        /**
         * Push state.
         */
        export function pushState(id: any, key: any, state: any, suivant?: any) {
            if (!_cellStates[id]) {
                _cellStates[id] = {};
                _cellStates[id][key] = [{ rowId: id, columnKey: key, state: _.concat([], state), suivant: suivant }]
                return;
            }
            
            if (!_cellStates[id][key]) {
                _cellStates[id][key] = [{ rowId: id, columnKey: key, state: _.concat([], state), suivant: suivant }];
                return;
            }
            
            if (_.isArray(state)) {
                _.forEach(state, s => {
                    _cellStates[id][key][0].state.push(s);
                });
            } else _cellStates[id][key][0].state.push(state);
            
            if (suivant) {
                _cellStates[id][key][0].suivant = suivant;
            }
        }
        
        /**
         * Pop state.
         */
        export function popState(id: any, key: any, states: any) {
            if (!states) return;
            if (!_cellStates[id] || !_cellStates[id][key]) return;
            _.remove(_cellStates[id][key][0].state, s => {
                if (_.isArray(states)) {
                    return _.some(states, state => state === s); 
                } else return s === states;
            });
        }
    }
    
     module ti {
        
        /**
         * Is IE.
         */
        export function isIE() {
            return window.navigator.userAgent.indexOf("MSIE") > -1 || window.navigator.userAgent.match(/trident/i);
        }
        
        /**
         * Is Chrome.
         */
        export function isChrome() {
            return window.chrome;
        }
        
        /**
         * Is Edge.
         */
        export function isEdge() {
            return window.navigator.userAgent.indexOf("Edge") > -1;
        }
         
        export function isArrowKey(evt: any) {
            return evt.keyCode >= 37 && evt.keyCode <= 40;
        }
         
        export function isArrowLeft(evt: any) {
            return evt.keyCode === 37;
        }
        
        export function isArrowRight(evt: any) {
            return evt.keyCode === 39;
        }
         
        export function isArrowUp(evt: any) {
            return evt.keyCode === 38;
        }
         
        export function isArrowDown(evt: any) {
            return evt.keyCode === 40;
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
         
        export function isZero(value: any, name: any) {
            let col = _secColumn[name];
            if (col && ((col.constraint && col.constraint.cDisplayType === "TimeWithDay")
                || !col.grant)) return false;
            return Number(value) === 0 || value === "0:00" || value === "00:00";
        }
         
        export function isTableCell(obj: any) {
            return obj.constructor === HTMLTableCellElement 
                || obj.constructor === HTMLTableDataCellElement;
        }
         
        /**
         * Equal.
         */
        export function isEqual(one: any, two: any, fields?: Array<any>) {
            if (_.isObject(one) && _.isObject(two)) {
                return (fields && fields.length > 0) 
                        ? _.isEqual(_.omitBy(one, (d, p) => fields.every(f => f !== p)),
                                    _.omitBy(two, (d, p) => fields.every(f => f !== p)))
                        : _.isEqual(_.omit(one, _.isFunction), _.omit(two, _.isFunction));
            }
            return _.isEqual(one, two);
        }
        
        /**
         * Get scroll width.
         */
        export function getScrollWidth() {
            if (_scrollWidth) return _scrollWidth;
            let $outer = document.body.appendChild(selector.create("div").css({ visibility: 'hidden', width: "100px", overflow: 'scroll' }).getSingle());
            let $inner = selector.create("div").css({ width: '100%' }).getSingle();
            $outer.appendChild($inner);
            let widthWithScroll = $inner.offsetWidth;
            $outer.parentNode.removeChild($outer);
            _scrollWidth = 100 - widthWithScroll;
            return _scrollWidth;
        }
         
        /**
         * First sibling.
         */
        export function firstSibling(node: any, clazz: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            for (let i = 0; i < children.length; i++) {
                if (node !== children[i] && children[i].classList.contains(clazz)) {
                    return children[i];
                }
            }
        }
        
        /**
         * Class siblings.
         */
        export function classSiblings(node: any, partialClass: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            let results = [];
            for (let i = 0; i < children.length; i++) {
                if (children[i] === node) continue;
                let classList = children[i].classList;
                for (let j = 0; j < classList.length; j++) {
                    if (classList.item(j).indexOf(partialClass) >= 0) {
                        results.push(children[i]);
                    }
                }
            }
            
            return results;
        }
        
        /**
         * Consume siblings.
         */
        export function consumeSiblings(node: any, op: any) {
            let parent = node.parentElement;
            if (!parent) return;
            let children = parent.children;
            for (let i = 0; i < children.length; i++) {
                if (node !== children[i]) {
                    op(children[i]);
                }
            }
        }
        
        /**
         * Closest.
         */
        export function closest(el, selector) {
            let matches;
            ['matches', 'webkitMatchesSelector', 'mozMatchesSelector', 'msMatchesSelector', 'oMatchesSelector'].some(function(fn) {
                if (typeof document.body[fn] === 'function') {
                    matches = fn;
                    return true;
                }
                return false;
            });
        
            let parent;
            while (el) {
                parent = el.parentElement;
                if (parent && parent[matches](selector)) {
                    return parent;
                }
                el = parent;
            }
        }
         
        /**
         * Add class.
         */
        export function addClass1n(node, clazz) {
            if (node && node.constructor !== HTMLCollection) {
                let children = node.querySelectorAll("." + v.CHILD_CELL_CLS); 
                if (children.length > 0) addClass(children, clazz);
                else addClass(node, clazz);
                return;
            }
            
            for (let i = 0; i < node.length; i++) {
                let children = node[i].querySelectorAll("." + v.CHILD_CELL_CLS);
                if (children.length > 0) addClass(children, clazz);
                else addClass(node[i], clazz);
            }
        }
        
        /**
         * Remove class.
         */
        export function removeClass1n(node, clazz) {
            if (node && node.constructor !== HTMLCollection) {
                let children = node.querySelectorAll("." + v.CHILD_CELL_CLS);
                if (children.length > 0) removeClass(children, clazz);
                else removeClass(node, clazz);
                return;
            }
            
            for (let i = 0; i < node.length; i++) {
                let children = node[i].querySelectorAll("." + v.CHILD_CELL_CLS);
                if (children.length > 0) removeClass(children, clazz);
                else removeClass(node[i], clazz);
            }
        }
        
        /**
         * Add class.
         */
        export function addClass(node, clazz) {
            if (node && node.constructor !== HTMLCollection && node.constructor !== NodeList) {
                node.classList.add(clazz);
                return;
            }
            
            for (let i = 0; i < node.length; i++) {
                if (!node[i].classList.contains(clazz)) {
                    node[i].classList.add(clazz);
                }
            }
        }
        
        /**
         * Remove class.
         */
        export function removeClass(node, clazz) {
            if (node && node.constructor !== HTMLCollection && node.constructor !== NodeList) {
                node.classList.remove(clazz);
                return;
            }
            
            for (let i = 0; i < node.length; i++) {
                if (node[i].classList.contains(clazz)) {
                    node[i].classList.remove(clazz);
                }
            }
        }
        
        /**
         * Remove.
         */
        export function remove(node) {
            if (isIE() && node && node.parentNode) {
                node.parentNode.removeChild(node);
                return;
            }
            
            node.remove();
        }
         
        /**
         * Classify columns.
         */
        export function classifyColumns(options: any) {
            let visibleColumns = [];
            let hiddenColumns = [];
            let columns = filterColumns(options.columns, visibleColumns, hiddenColumns);
            
            return {
                        visibleColumns: visibleColumns,
                        hiddenColumns: hiddenColumns,
                        columns: columns
                   };
        }
         
        /**
         * Get columns map.
         */
        export function getColumnsMap(columns: any) {
            return _.groupBy(columns, "key");
        }
         
        /**
         * Filter columns.
         */
        function filterColumns(columns: Array<any>, visibleColumns: Array<any>, hiddenColumns: Array<any>) {
            let cols = [];
            _.forEach(columns, function(col: any) {
                if (!_.isNil(col.hidden) && col.hidden === true) {
                    hiddenColumns.push(col);
                    cols.push(col);
                    return;
                }
                
                if (!util.isNullOrUndefined(col.group) && col.group.length > 0) { 
                    cols = _.concat(cols, filterColumns(col.group, visibleColumns, hiddenColumns));
                } else {
                    visibleColumns.push(col);
                    cols.push(col);
                }
            });
            
            return cols;
        }
         
        /**
         * Columns map.
         */ 
        export function columnsMapFromStruct(levelStruct: any) {
            let map = {};
            _.forEach(Object.keys(levelStruct), function(nth: any) {
                _.forEach(levelStruct[nth], function(col: any) {
                    if (!util.isNullOrUndefined(col.key)) {
                        map[col.key] = col;
                    }
                });
            });
            return map; 
        }
         
         /**
          * Calc total.
          */
         export function calcTotal() {
            _.forEach(_.keys(_summaries), k => {
                let sum = _summaries[k];
                if ((sum.calculator === "Time" && sum[_currentPage] && sum[_currentPage].asHours() > 0)
                    || (sum.calculator === "Number" && sum[_currentPage] > 0)) return;
                
                _.forEach(_dataSource, d => {
                    switch (sum.calculator) {
                        case "Time":
                            if (_.isNil(sum[_currentPage])) {
                                sum[_currentPage] = moment.duration("0:00");
                            }
                            sum[_currentPage].add(moment.duration(d[k]));
                            break;
                        case "Number": 
                            if (_.isNil(sum[_currentPage])) {
                                sum[_currentPage] = 0;
                            }
                            sum[_currentPage] += (!_.isNil(d[k]) && d[k] !== "" ? parseFloat(d[k]) : 0);
                            break;
                    }
                });     
            });
         }
         
         /**
          * Moment to string.
          */
         export function momentToString(total) {
            let minus = "", time = total.asHours();
            if (time < 0) {
                time = Math.abs(time);
                minus = "-";    
            }
             
            let hour = Math.floor(time),
                minute = (time - hour) * 60,
                roundMin = Math.round(minute),
                minuteStr = roundMin < 10 ? ("0" + roundMin) : String(roundMin);
            return minus + hour + ":" + minuteStr;
         }
         
         /**
          * As currency.
          */
         export function asCurrency(value) {
            let currencyOpts: any = new ui.option.CurrencyEditorOption();
            currencyOpts.grouplength = 3;
            currencyOpts.decimallength = 0;
            currencyOpts.currencyformat = "JPY";
            let formatter = new uk.text.NumberFormatter({ option: currencyOpts });
            if (!isNaN(value)) return formatter.format(value);
            return value;
         }
         
         /**
          * Get cell coord.
          */
         export function getCellCoord($cell: HTMLElement) {
            if (!$cell) return;
            let $td = $cell;
            if (selector.is($cell, "div")) {
                $td = closest($cell, "td");
            }
             
            let view = $.data($td, lo.VIEW);
            if (!view) return;
            let coord = view.split("-");
            if (util.isNullOrUndefined(coord[0]) || util.isNullOrUndefined(coord[1])) return;
             
            return {
                rowIdx: parseFloat(coord[0]),
                columnKey: coord[1]
            };
        }
     }
}