module nts.uk.ui.chart {
    
    export let warning = [], pDiv = document.createElement("div");
    export class Ruler {
        
        chartArea: HTMLElement;
        definedType: any = {};
        gcChart: any = {};
        lineLock: any = {};
        slideTrigger: any;
        snatchInterval: number;
        dragInsert: boolean = false;
        
        constructor(chartArea: HTMLElement) {
            if (_.isNil(chartArea)) {
                warning.push(new Warn("chartArea is undefined."));
            }
            this.chartArea = chartArea;
        }
        
        addType(options: any) {
            let self = this;
            if (_.isNil(options.name)) {
                warning.push(new Warn("Set type name"));
                return;
            }
            
            self.definedType[options.name] = new DefinedType(options);
            if (options.locked) {
                self.lineLock[options.lineNo] = true;
            }
        }
        
        addChart(options?: any) {
            let self = this, chart = new GanttChart(options);
            if (chart.locked) {
                self.lineLock[chart.lineNo] = true;
            }
            
            if ((self.gcChart[options.lineNo] || {})[options.id]) {
                warning.push(new Warn("Chart id existed"));
                return;
            }
            
            if (chart.newChart()) return;
            if (_.isNil(self.gcChart[options.lineNo])) {
                self.gcChart[options.lineNo] = {};
            }
            
            self.gcChart[options.lineNo][options.id] = chart;
            let show = true;
            if (!_.isNil(options.parent)) {
                let parent = self.gcChart[options.lineNo][options.parent];
                if (parent) {
                    parent.children.push(chart);
                    if (chart.end <= parent.start || chart.start >= parent.end) show = false;
                    else if (parent.start > chart.start) {
                        chart.html.style.left = `${chart.origin[0] + parent.start * chart.unitToPx}px`;
                        chart.html.style.width = `${(chart.end - parent.start) * chart.unitToPx - 1}px`;
                    } else if (parent.end < chart.end) {
                        chart.html.style.width = `${(parent.end - chart.start) * chart.unitToPx - 1}px`;
                    }
                }
            }
            
            if (chart.title) {
                let titleTag = pDiv.cloneNode(true);
                titleTag.className = "chart-name";
                titleTag.textContent = chart.title;
                chart.html.appendChild(titleTag);
            }
            
            if (show) {
                self.chartArea.appendChild(chart.html);
            }
            
            let docMove = () => {
                event.preventDefault();
                if (_.keys(self.slideTrigger).length === 0) return;
                let diff = event.pageX - self.slideTrigger.pageX, nearestLine, parentChart;
                if (!_.isNil(chart.parent)) {
                    parentChart = self.gcChart[chart.lineNo][chart.parent];
                }
                
                if (self.slideTrigger.holdPos === HOLD_POS.BODY) {
                    if (!chart.canSlide) return;
                    nearestLine = Math.round(self.slideTrigger.start + diff / chart.unitToPx);
                    if (nearestLine % self._getSnatchInterval(chart) !== 0) return;
                    let step = nearestLine - self.slideTrigger.start, 
                        pDec = { left: nearestLine * chart.unitToPx, start: nearestLine, end: self.slideTrigger.end + step };
                    if (chart.limitStartMin > pDec.start || chart.limitStartMax < pDec.start 
                        || chart.limitEndMin > pDec.end || chart.limitEndMax < pDec.end) return;
                    let lineCharts = self.gcChart[chart.lineNo];
                    if (_(lineCharts).keys().find(k => {
                        let sameLineChart: GanttChart = lineCharts[k];
                        return (sameLineChart.id !== chart.id && _.isNil(sameLineChart.parent) 
                                && !sameLineChart.bePassedThrough
                                && ((nearestLine > chart.start && pDec.end > sameLineChart.start && pDec.start < sameLineChart.start)
                                || (nearestLine < chart.start && pDec.start < sameLineChart.end && pDec.end > sameLineChart.end)));
                    })) return;
                    
                    if (parentChart && ((diff > 0 && pDec.end > parentChart.end) || (diff < 0 && pDec.start < parentChart.start))) return;
                    
                    if (parentChart && _.find(parentChart.children, (child: GanttChart) => {
                        return child.id !== chart.id && !child.bePassedThrough 
                            && ((chart.start >= child.end && pDec.start < child.end) || (chart.end <= child.start && pDec.end > child.start));
                    })) return;
                    
                    _.forEach(chart.children, (child: GanttChart) => {
                        let childSlide;
                        if (child.followParent) {
                            childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                            if (!childSlide) return;
                            child.reposition({ start: childSlide.start + step, end: childSlide.end + step, left: childSlide.left + step * child.unitToPx });
                        } else if (diff > 0 && child.start < pDec.start && !child.pin) {
                            childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                            if (!childSlide) return;
                            child.reposition({ width: childSlide.length + (childSlide.start - pDec.start) * child.unitToPx, left: pDec.start * child.unitToPx, start: pDec.start });
                        } else if (diff < 0 && child.end > pDec.end && !child.pin) {
                            childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                            if (!childSlide) return;
                            child.reposition({ width: childSlide.length + (pDec.end - childSlide.end) * child.unitToPx, end: pDec.end });
                        }
                    });
                    
                    chart.reposition(pDec);
                } else if (self.slideTrigger.holdPos === HOLD_POS.START) {
                    if (chart.fixed === CHART_FIXED.START || chart.fixed === CHART_FIXED.BOTH) return;
                    nearestLine = Math.round(self.slideTrigger.start + diff / chart.unitToPx);
                    if (nearestLine % self._getSnatchInterval(chart) !== 0 || nearestLine === chart.start) return;
                    let pDec = { width: self.slideTrigger.length + (self.slideTrigger.start - nearestLine) * chart.unitToPx, left: nearestLine * chart.unitToPx, start: nearestLine };
                    if (chart.limitStartMin > pDec.start || chart.limitStartMax < pDec.start) return;
                    if (pDec.start + self._getSnatchInterval(chart) > chart.end
                        || (parentChart && !self.slideTrigger.overlap && pDec.start < parentChart.start)) return;
                    self.slideTrigger.ltr = nearestLine > chart.start;
                    let lineCharts = self.gcChart[chart.lineNo];
                    if (_(lineCharts).keys().find(k => {
                        let sameLineChart: GanttChart = lineCharts[k];
                        return (sameLineChart.id !== chart.id && _.isNil(sameLineChart.parent) 
                                && !sameLineChart.bePassedThrough
                                && (nearestLine < chart.start && pDec.start < sameLineChart.end && chart.end > sameLineChart.end));
                    })) return;
                    
                    _.forEach(chart.children, (child: GanttChart) => {
                        let childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                        if (!childSlide) return;
                        if (child.pin) {
                            if (child.rollup) {
                                if (nearestLine >= child.start && nearestLine < child.end) {
                                    let newWidth = (Math.min(childSlide.end, chart.end) - childSlide.start) * child.unitToPx - 1 + (childSlide.start - pDec.start) * child.unitToPx;
                                    child.reposition({ width: newWidth, left: pDec.start * child.unitToPx });
                                    if (!self.chartArea.contains(child.html)) {
                                        self.chartArea.appendChild(child.html);
                                    }
                                    
                                    if (!_.find(self.slideTrigger.edgeCharts, c => c.id === child.id)) {
                                        self.slideTrigger.edgeCharts.push(child);
                                    }
                                } else if (nearestLine < child.start) {
                                    if (!self.chartArea.contains(child.html)) {
                                        if (self.slideTrigger.start <= child.end) return;
                                        self.chartArea.appendChild(child.html);
                                    }
                                    
                                    let maxWidth = (Math.min(child.end, chart.end) - child.start) * child.unitToPx - 1,
                                        currentWidth = parseFloat(child.html.style.width);
                                    if (currentWidth !== maxWidth) {
                                        child.reposition({ width: maxWidth, left: parseFloat(child.html.style.left) - parseFloat(maxWidth - currentWidth) });
                                    }
                                    
                                    if (self.slideTrigger.edgeCharts.length > 0) {
                                        _.remove(self.slideTrigger.edgeCharts, c => c.id === child.id);
                                    }
                                } else {
                                    child.reposition({ width: 0 });
                                    if (self.slideTrigger.edgeCharts.length > 0) {
                                        _.remove(self.slideTrigger.edgeCharts, c => c.id === child.id);
                                    }
                                }
                            }                               
                        }  
                            
//                            child.reposition({ width: childSlide.length + (childSlide.start - pDec.start) * child.unitToPx, left: pDec.start * child.unitToPx, start: pDec.start });
                         
                    });
                    
                    if (self.slideTrigger.overlap) {
                        parentChart.reposition({ width: self.slideTrigger.overlap.parentLength + (self.slideTrigger.start - nearestLine) * parentChart.unitToPx, left: pDec.left, start: pDec.start });
                    }
                    
                    chart.reposition(pDec);
                } else {
                    if (chart.fixed === CHART_FIXED.END || chart.fixed === CHART_FIXED.BOTH) return;
                    nearestLine = Math.round(self.slideTrigger.end + diff / chart.unitToPx);
                    if (nearestLine % self._getSnatchInterval(chart) !== 0 || nearestLine === chart.end) return;
                    let pDec = { width: self.slideTrigger.length + (nearestLine - self.slideTrigger.end) * chart.unitToPx, end: nearestLine };
                    if (chart.limitEndMax < pDec.end || chart.limitEndMin > pDec.end) return;
                    if (chart.start + self._getSnatchInterval(chart) > pDec.end
                        || (parentChart && !self.slideTrigger.overlap && pDec.end > parentChart.end)) return;
                    self.slideTrigger.ltr = nearestLine > chart.end;
                    let lineCharts = self.gcChart[chart.lineNo];
                    if (_(lineCharts).keys().find(k => {
                        let sameLineChart: GanttChart = lineCharts[k];
                        return (sameLineChart.id !== chart.id && _.isNil(sameLineChart.parent) 
                                && !sameLineChart.bePassedThrough
                                && (nearestLine > chart.end && pDec.end > sameLineChart.start && chart.start < sameLineChart.start));
                    })) return;
                    
                    _.forEach(chart.children, (child: GanttChart) => {
                        let childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                        if (!childSlide) return;
                        if (child.pin) {
                            if (child.rollup) {
                                if (nearestLine > child.start && nearestLine <= child.end) {
                                    let newWidth = (childSlide.end - Math.max(childSlide.start, chart.start)) * child.unitToPx - 1 + (pDec.end - childSlide.end) * child.unitToPx;
                                    child.reposition({ width: newWidth });
                                    if (!self.chartArea.contains(child.html)) {
                                        self.chartArea.appendChild(child.html);
                                    }
                                    
                                    if (!_.find(self.slideTrigger.edgeCharts, c => c.id === child.id)) {
                                        self.slideTrigger.edgeCharts.push(child);
                                    }
                                } else if (nearestLine > child.end) {
                                    if (!self.chartArea.contains(child.html)) {
                                        if (self.slideTrigger.end > child.start) return;
                                        self.chartArea.appendChild(child.html);
                                    }
                                    
                                    let maxWidth = (child.end - Math.max(child.start, chart.start)) * child.unitToPx - 1,
                                        currentWidth = parseFloat(child.html.style.width);
                                    if (currentWidth !== maxWidth) {
                                        child.reposition({ width: maxWidth });
                                    }
                                    
                                    if (self.slideTrigger.edgeCharts.length > 0) {
                                        _.remove(self.slideTrigger.edgeCharts, c => c.id === child.id);
                                    }
                                } else {
                                    child.reposition({ width: 0 });
                                    if (self.slideTrigger.edgeCharts.length > 0) {
                                        _.remove(self.slideTrigger.edgeCharts, c => c.id === child.id);
                                    }
                                }
                            }                               
                        }
//                            child.reposition({ width: childSlide.length + (pDec.end - childSlide.end) * child.unitToPx, end: pDec.end });
                    });
                    
                    if (self.slideTrigger.overlap) {
                        parentChart.reposition({ width: self.slideTrigger.overlap.parentLength + (nearestLine - self.slideTrigger.end) * parentChart.unitToPx, end: pDec.end });
                    }
                    
                    chart.reposition(pDec);
                }
            };
            
            let docUp = () => {
                _.forEach(chart.children, (child: GanttChart) => {
                    if (child.pin && child.rollup && child.roundEdge) {
                        if (self.slideTrigger.holdPos === HOLD_POS.START
                            && chart.start > child.start && chart.start <= child.end) {
                            let delta = (child.end - chart.start) * chart.unitToPx,
                                chartWidth = parseFloat(chart.html.style.width) - delta,
                                chartLeft = parseFloat(chart.html.style.left) + delta;
                            chart.reposition({ width: chartWidth, left: chartLeft, start: child.end });
                            child.reposition({ width: 0 });
                            if (self.slideTrigger.edgeCharts.length > 0) {
                                self.slideTrigger.edgeCharts.forEach((c: GanttChart) => {
                                    if (c.id === child.id) return;
                                    c.reposition({ width: parseFloat(c.html.style.width) - delta, left: parseFloat(c.html.style.left) + delta });
                                });
                            }
                            
                            return false;
                        } else if (self.slideTrigger.holdPos === HOLD_POS.END
                            && chart.end > child.start && chart.end < child.end) {
                            let delta = (chart.end - child.start) * chart.unitToPx,
                                chartWidth = parseFloat(chart.html.style.width) - delta;
                            chart.reposition({ width: chartWidth, end: child.start });
                            child.reposition({ width: 0 });
                            if (self.slideTrigger.edgeCharts.length > 0) {
                                self.slideTrigger.edgeCharts.forEach((c: GanttChart) => {
                                    if (c.id === child.id) return;
                                    c.reposition({ width: parseFloat(c.html.style.width) - delta });  
                                });
                            }
                            
                            return false;
                        }
                    }
                });
                
                document.removeEventListener("mousemove", docMove);
                document.removeEventListener("mouseup", docUp);
                
                let e = document.createEvent('CustomEvent');
                if (self.slideTrigger.holdPos === HOLD_POS.BODY) {
                    if (_.isFunction(chart.dropFinished)) {
                        chart.dropFinished(chart.start, chart.end);
                    }
                    
                    e.initCustomEvent("gcdrop", true, true, [ chart.start, chart.end ]);
                } else {
                    if (_.isFunction(chart.resizeFinished)) {
                        chart.resizeFinished(chart.start, chart.end, self.slideTrigger.holdPos === HOLD_POS.START);
                    }
                    
                    e.initCustomEvent("gcresize", true, true, [ chart.start, chart.end, self.slideTrigger.holdPos === HOLD_POS.START ]);   
                }
                
                self.slideTrigger = {};
                chart.html.dispatchEvent(e);
            };
            
            chart.html.addEventListener("mousedown", () => {
                let holdPos = self.getHoldPos(chart);
                if (chart.pin) {
//                    if (chart.rollup) {
                        if (!_.isNil(chart.parent)) {
                            let parentChart = self.gcChart[chart.lineNo][chart.parent];
                            if (holdPos === HOLD_POS.START && parentChart.start >= chart.start) {
                                let pevt = new support.ChartEvent("mousedown");
                                pevt.pageX = event.pageX;
                                pevt.pageY = event.pageY;
                                pevt.offsetX = event.offsetX;
                                parentChart.html.dispatchEvent(pevt);
                                return;
                            } else if (holdPos === HOLD_POS.END && parentChart.end <= chart.end) {
                                let pevt = new support.ChartEvent("mousedown");
                                pevt.pageX = event.pageX;
                                pevt.pageY = event.pageY;
                                pevt.offsetX = parseFloat(parentChart.html.style.width);
                                parentChart.html.dispatchEvent(pevt);
                                return;
                            }
                        }
//                    }
                }
                
                if (holdPos === HOLD_POS.OUT) return;
                self.slideTrigger = { 
                    pageX: event.pageX,
                    holdPos: holdPos,
                    length: parseFloat(chart.html.style.width),
                    start: chart.start,
                    end: chart.end,
                    children: _.map(chart.children, c => ({ id: c.id, start: c.start, end: c.end, length: parseFloat(c.html.style.width), left: parseFloat(c.html.style.left) })),
                    edgeCharts: []
                };
                
                if (!_.isNil(chart.parent)) {
                    let parentChart = self.gcChart[chart.lineNo][chart.parent];
                    if ((holdPos === HOLD_POS.START && parentChart.start === chart.start)
                        || (holdPos === HOLD_POS.END && parentChart.end === chart.end)) {
                        self.slideTrigger.overlap = { parentLength: parseFloat(parentChart.html.style.width) };
                    }
                }
                
                document.addEventListener("mousemove", docMove);
                document.addEventListener("mouseup", docUp);
            });
            
            chart.html.addEventListener("mousemove", () => {
                if (self.dragInsert) {
                    chart.html.style.cursor = "";
                    return;
                }
                
                let holdPos = self.getHoldPos(chart);
                if (holdPos === HOLD_POS.START) {
                    if (chart.fixed !== CHART_FIXED.START && chart.fixed !== CHART_FIXED.BOTH) {
                        chart.cursor = "col-resize";
                    } else if (chart.pin /*&& chart.rollup*/ && !_.isNil(chart.parent)) {
                        let parentChart = self.gcChart[chart.lineNo][chart.parent];
                        if (parentChart.start >= chart.start
                            && parentChart.fixed !== CHART_FIXED.START && parentChart.fixed !== CHART_FIXED.BOTH) {
                            chart.cursor = "col-resize";
                        }
                    }
                } else if (holdPos === HOLD_POS.END) {
                    if (chart.fixed !== CHART_FIXED.END && chart.fixed !== CHART_FIXED.BOTH) {
                        chart.cursor = "col-resize";
                    } else if (chart.pin /*&& chart.rollup*/ && !_.isNil(chart.parent)) {
                        let parentChart = self.gcChart[chart.lineNo][chart.parent];
                        if (parentChart.end <= chart.end
                            && parentChart.fixed !== CHART_FIXED.END && parentChart.fixed !== CHART_FIXED.BOTH) {
                            chart.cursor = "col-resize";
                        }
                    }
                } else if (holdPos === HOLD_POS.BODY && chart.canSlide) {
                    chart.cursor = "e-resize";
                } else chart.cursor = "";
                
                chart.html.style.cursor = chart.cursor;
            });
            
            return chart.html;
        }
        
        getHoldPos(chart: GanttChart) {
            let self = this;
            if (self.lineLock[chart.lineNo] /*|| chart.fixed === CHART_FIXED.BOTH*/) return HOLD_POS.OUT;
            let parentChart;
            if (chart.parent) {
                parentChart = self.gcChart[chart.lineNo][chart.parent];
            }
            
            if (chart.fixed === CHART_FIXED.BOTH && parentChart 
                && chart.start > parentChart.start && chart.end < parentChart.end
                && (event.offsetX < chart.drawerSize || parseFloat(chart.html.style.width) - chart.drawerSize < event.offsetX)) {
                return HOLD_POS.BODY;
            } else if (chart.fixed !== CHART_FIXED.START && event.offsetX < chart.drawerSize) {
                return HOLD_POS.START;
            } else if (chart.fixed !== CHART_FIXED.END
                && parseFloat(chart.html.style.width)/*(chart.end - chart.start) * chart.unitToPx*/ - chart.drawerSize < event.offsetX) {
                return HOLD_POS.END;
            } else {
                return HOLD_POS.BODY;
            }
        }
        
        addChartWithType(typeName: string, options?: any) {
            let self = this,
                chartType = self.definedType[typeName];
            if (_.isNil(options)) {
                options = {};
            }
            
            if (chartType) {
                _.forEach(_.keys(chartType), key => {
                    let assignedKey = key === "name" ? "definedType" : key; 
                    if (_.isNil(options[assignedKey]) && !_.isNil(chartType[key])) {
                        options[assignedKey] = chartType[key];
                    }
                });
            }
            
            return self.addChart(options);
        }
        
        setLock(lines: Array<any>, lock: any) {
            let self = this;
            _.forEach(lines, line => self.lineLock[line] = lock); 
        }
        
        setSnatchInterval(interval: number) {
            this.snatchInterval = interval;
        }
        
        setDragInsert(insert: boolean) {
            this.dragInsert = insert;
        }
        
        replaceAt(lineNo: number, charts: Array<{ type: string, options: any }>, id?: any) {
            if (_.isNil(lineNo) || _.isNil(charts) || charts.length === 0) return;
            let self = this;
            let lineChart = self.gcChart[lineNo];
            if (!lineChart) return;
            if (!_.isNil(id)) {
                let removed = _(lineChart).keys().map(c => lineChart[c]).find(c => c.id === id);
                if (removed) {
                    if (removed.html.parentNode) removed.html.parentNode.removeChild(removed.html);
                    let chart = charts[0];
                    if (_.has(chart, "type")) {
                        self.addChartWithType(chart.type, chart.options)
                    } else self.addChart(chart.options);
                }
                
                return;
            }
            
            _(lineChart).keys().map(c => lineChart[c]).forEach(c => {
                if (c.html.parentNode) c.html.parentNode.removeChild(c.html)
            });
            
            self.gcChart[lineNo] = {};
            charts.forEach(c => _.has(c, "type") ? self.addChartWithType(c.type, c.options) : self.addChart(c.options));
        }
        
        move(lineNo: number, id: any, start: any) {
            let self = this;
            if (_.isNil(lineNo) || _.isNil(id) || _.isNil(start)) return;
            let chart = (self.gcChart[lineNo] || {})[id];
            if (_.isNil(chart)) return;
            self.slideTrigger = {
                length: parseFloat(chart.html.style.width),
                start: chart.start,
                end: chart.end,
                children: _.map(chart.children, c => ({ id: c.id, start: c.start, end: c.end, length: parseFloat(c.html.style.width), left: parseFloat(c.html.style.left) }))
            };
            
            let diff = start - chart.start;
            let pDec = { left: start * chart.unitToPx, start: start, end: chart.end + start - chart.start };
            if (chart.limitStartMin > pDec.start || chart.limitStartMax < pDec.start 
                || chart.limitEndMin > pDec.end || chart.limitEndMax < pDec.end) return;
            let parentChart = (self.gcChart[lineNo] || {})[chart.parent],
                step = start - chart.start;
            if (parentChart && ((step > 0 && pDec.end > parentChart.end) || (step < 0 && pDec.start < parentChart.start))) return;
            
            _.forEach(chart.children, (child: GanttChart) => {
                let childSlide;
                if (child.followParent) {
                    childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                    if (!childSlide) return;
                    child.reposition({ start: childSlide.start + step, end: childSlide.end + step, left: childSlide.left + step * child.unitToPx });
                } else if (diff > 0 && child.start < pDec.start) {
                    childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                    if (!childSlide) return;
                    child.reposition({ width: childSlide.length + (childSlide.start - pDec.start) * child.unitToPx, left: pDec.start * child.unitToPx, start: pDec.start });
                } else if (diff < 0 && child.end > pDec.end) {
                    childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                    if (!childSlide) return;
                    child.reposition({ width: childSlide.length + (pDec.end - childSlide.end) * child.unitToPx, end: pDec.end });
                }
            });
            
            chart.reposition(pDec);
            self.slideTrigger = {};
        }
        
        extend(lineNo: number, id: any, start: any, end?: any) {
            let self = this;
            if (_.isNil(lineNo) || _.isNil(id) || (_.isNil(start) && _.isNil(end))) return;
            let chart = (self.gcChart[lineNo] || {})[id];
            if (_.isNil(chart)) return;
            let parentChart;
            if (!_.isNil(chart.parent)) {
                parentChart = (self.gcChart[lineNo] || {})[chart.parent];
            }
            
            if (!_.isNil(start) && start !== chart.start) {
                self.slideTrigger = {
                    length: parseFloat(chart.html.style.width),
                    start: chart.start,
                    end: chart.end,
                    children: _.map(chart.children, c => ({ id: c.id, start: c.start, end: c.end, length: parseFloat(c.html.style.width), left: parseFloat(c.html.style.left) }))
                };
                
                //if (start % self._getSnatchInterval(chart) !== 0) return;
                let pDec = { width: self.slideTrigger.length + (self.slideTrigger.start - start) * chart.unitToPx, left: start * chart.unitToPx, start: start };
                if (chart.limitStartMin > pDec.start || chart.limitStartMax < pDec.start) return;
                if (pDec.start + self._getSnatchInterval(chart) > chart.end
                    || (parentChart && !self.slideTrigger.overlap && pDec.start < parentChart.start)) return;
                self.slideTrigger.ltr = start > chart.start;
                    
                _.forEach(chart.children, (child: GanttChart) => {
                    let childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                    if (!childSlide) return;
                    if (child.pin) {
                        if (child.rollup) {
                            if (start >= child.start && start < child.end) {
                                let newWidth = (Math.min(childSlide.end, chart.end) - childSlide.start) * child.unitToPx - 1 + (childSlide.start - pDec.start) * child.unitToPx;
                                child.reposition({ width: newWidth, left: pDec.start * child.unitToPx });
                                if (!self.chartArea.contains(child.html)) {
                                    self.chartArea.appendChild(child.html);
                                }
                            } else if (start < child.start) {
                                if (!self.chartArea.contains(child.html) && child.end > chart.end) return;
                                let maxWidth = (Math.min(child.end, chart.end) - child.start) * child.unitToPx - 1,
                                    currentWidth = parseFloat(child.html.style.width);
                                if (currentWidth !== maxWidth) {
                                    child.reposition({ width: maxWidth, left: parseFloat(child.html.style.left) - parseFloat(maxWidth - currentWidth) });
                                }
                                
                                if (!self.chartArea.contains(child.html)) {
                                    self.chartArea.appendChild(child.html);
                                }   
                            } else {
                                child.reposition({ width: 0 });
                            }
                        }                               
                    }
                });
                
                chart.reposition(pDec);
            }
            
            if (!_.isNil(end) && end !== chart.end) {
                self.slideTrigger = {
                    length: parseFloat(chart.html.style.width),
                    start: chart.start,
                    end: chart.end,
                    children: _.map(chart.children, c => ({ id: c.id, start: c.start, end: c.end, length: parseFloat(c.html.style.width), left: parseFloat(c.html.style.left) }))
                };
                
                //if (end % self._getSnatchInterval(chart) !== 0) return;
                let pDec = { width: self.slideTrigger.length + (end - self.slideTrigger.end) * chart.unitToPx, end: end };
                if (chart.limitEndMax < pDec.end || chart.limitEndMin > pDec.end) return;
                if (chart.start + self._getSnatchInterval(chart) > pDec.end
                    || (parentChart && !self.slideTrigger.overlap && pDec.end > parentChart.end)) return;
                self.slideTrigger.ltr = end > chart.end;
                
                _.forEach(chart.children, (child: GanttChart) => {
                    let childSlide = _.find(self.slideTrigger.children, c => c.id === child.id);
                    if (!childSlide) return;
                    if (child.pin) {
                        if (child.rollup) {
                            if (end > child.start && end <= child.end) {
                                let newWidth = (childSlide.end - Math.max(childSlide.start, chart.start)) * child.unitToPx - 1 + (pDec.end - childSlide.end) * child.unitToPx;
                                child.reposition({ width: newWidth });
                                if (!self.chartArea.contains(child.html)) {
                                    self.chartArea.appendChild(child.html);
                                }
                            } else if (end > child.end) {
                                if (child.start < chart.start) return;
                                let maxWidth = (child.end - Math.max(child.start, chart.start)) * child.unitToPx - 1,
                                    currentWidth = parseFloat(child.html.style.width);
                                if (currentWidth !== maxWidth) {
                                    child.reposition({ width: maxWidth });
                                }
                                
                                if (!self.chartArea.contains(child.html)) {
                                    self.chartArea.appendChild(child.html);
                                }
                            } else {
                                child.reposition({ width: 0 });
                            }
                        }                               
                    }
                });
                
                chart.reposition(pDec);
            }
            
            self.slideTrigger = {};
        }
        
        private _getSnatchInterval(chart: GanttChart) {
            let self = this;
            if (!_.isNil(self.snatchInterval)) return self.snatchInterval;
            return chart.snatchInterval;
        }
    }
    
    class DefinedType {
        
        name: string;
        parent: string;
        title: string;
        lineNo: number;
        color: string;
        followParent: boolean;
        canSlide: boolean;
        cursor: string;
        limitStartMin: number;
        limitStartMax: number;
        limitEndMin: number;
        limitEndMax: number;
        fixed: CHART_FIXED;
        unitToPx: number;
        locked: boolean;
        chartWidth: number;
        lineWidth: number;
        snatchInterval: number;
        drawerSize: number;
        bePassedThrough: boolean;
        pin: boolean;
        rollup: boolean;
        roundEdge: boolean;
        resizeFinished: any;
        dropFinished: any;
        
        constructor(options) {
            this.name = options.name;
            this.parent = options.parent;
            this.title = options.title;
            this.lineNo = options.lineNo;
            this.color = options.color;
            this.followParent = options.followParent;
            this.canSlide = options.canSlide;
            this.cursor = options.cursor;
            this.limitStartMin = options.limitStartMin;
            this.limitStartMax = options.limitStartMax;
            this.limitEndMin = options.limitEndMin;
            this.limitEndMax = options.limitEndMax;
            this.unitToPx = options.unitToPx;
            this.fixed = options.fixed;
            this.locked = options.locked;
            this.chartWidth = options.chartWidth;
            this.lineWidth = options.lineWidth;
            this.snatchInterval = options.snatchInterval;
            this.drawerSize = options.drawerSize;
            this.bePassedThrough = options.bePassedThrough;
            this.pin = options.pin;
            this.rollup = options.rollup;
            this.roundEdge = options.roundEdge;
            this.resizeFinished = options.resizeFinished;
            this.dropFinished = options.dropFinished;
        }
    }
    
    class GanttChart {
        
        lineNo: number;
        id: string;
        parent: string;
        children: Array<GanttChart> = [];
        title: string;
        definedType: string;
        maxArea: number = 1000;
        start: number;
        end: number;
        zIndex: number = 1000;
        color: string = "#b8f441";
        origin: Array<any> = [0, 0];
        chartWidth: number = 15;
        lineWidth: number = 20;
        unitToPx: number = 10;
        snatchInterval: number = 1;
        canSlide: boolean = false;
        limitStartMin: number = 0;
        limitStartMax: number;
        limitEndMin: number = 0;
        limitEndMax: number;
        followParent: boolean = false;
        fixed: CHART_FIXED = CHART_FIXED.NONE;
        drawerSize: number = 3;
        cursor: string;
        bePassedThrough: boolean = true;
        locked: boolean = false;
        rollup: boolean = false;
        pin: boolean = false;
        roundEdge: boolean = false;
        html: HTMLElement;
        resizeFinished: any;
        dropFinished: any;
        
        constructor(options: any) {
            let self = this;
            if (!_.keys(options).length) return;
            self.limitStartMax = options.limitStartMax || options.maxArea || self.maxArea;
            self.limitEndMax = options.limitEndMax || options.maxArea || self.maxArea;
            $.extend(self, options);
        }
        
        newChart() {
            
            if (_.isNil(this.id)) {
                warning.push(new Warn("Not set id"));
                return 1;
            }
            
            if (_.isNil(this.lineNo)) {
                warning.push(new Warn("Not set lineNo"));
                return 1;
            }
            
            if (this.limitStartMin > this.start || this.limitStartMax < this.start) {
                warning.push(new Warn(`${this.lineNo}-${this.id} start is out of range.`));
                return 1;
            }
            
            if (this.limitEndMin > this.end || this.limitEndMax < this.end) {
                warning.push(new Warn(`${this.lineNo}-${this.id} end is out of range.`));
            }
            
            let self = this,
                posTop = self.origin[1] + self.lineNo * self.lineWidth + Math.floor((self.lineWidth - self.chartWidth) / 2),
                posLeft = self.origin[0] + self.start * self.unitToPx,
                chart = document.createElement("div");
            chart.setAttribute("id", `${self.lineNo}-${self.id}`);
            chart.className = "nts-ganttchart";
            chart.style.cssText = `; position: absolute; top: ${posTop}px; left: ${posLeft}px; z-index: ${self.zIndex}; 
                overflow: hidden; white-space: nowrap; width: ${(self.end - self.start) * self.unitToPx - 1}px; height: ${self.chartWidth}px;
                background-color: ${self.color}; cursor: ${self.cursor}; border: 1px solid #AAB7B8; `;
            
            self.html = chart;
            self.html.addEventListener("selectstart", () => { return false; });
        }
        
        reposition(style: any) {
            let self = this;
            
//            if ((_.has(style, "start") && style.start < self.limitStart)
//                || (_.has(style, "end") && style.end > self.limitEnd)) return;
            
            if (_.has(style, "start")) {
                self.start = style.start;    
            }
            
            if (_.has(style, "end")) {
                self.end = style.end;
            }
            
            if (_.has(style, "top")) {
                self.html.style.top = `${style.top}px`;
            }
            
            if (_.has(style, "left")) {
                self.html.style.left = `${style.left}px`;
            }
            
            if (_.has(style, "width")) {
                if (style.width <= 0) {
                    if (self.html.parentNode) {
                        self.html.parentNode.removeChild(self.html);
                    }
                } else {
                    self.html.style.width = `${style.width}px`;
                }
            }
            
        }
    }
    
    module support {
        
        export function ChartEvent(name: string, params?: any) {
            let evt;
            params = params || { bubbles: false, cancelable: false, detail: null };
            try {
                evt = new Event(name, params);
            } catch(e) {
                evt = document.createEvent("CustomEvent");
                evt.initCustomEvent(name, params.bubbles, params.cancelable, params.detail);
            }
            
            return evt;
        }
        
        export function nodeInsertedObserver(elm: HTMLElement, cb: any) {
            let MutationObserver = window.MutationObserver || window.WebKitMutationObserver;
            if (MutationObserver) {
                let observer = new MutationObserver((mutations) => {
                    let insertedNodes = [];
                    mutations.forEach(r => r.addedNodes.length && insertedNodes.push(...r.addedNodes));
                    if (_.isFunction(cb)) {
                        cb(insertedNodes);
                    }
                });
                
                observer.observe(elm, { childList: true, subtree: true });
                return;
            }
            
            elm.addEventListener("DOMNodeInserted", cb);
        }
    }   
    
    class Warn {
        message: string;
        constructor(msg: string) {
            this.message = msg;
        }
    }
    
    enum CHART_FIXED {
        NONE = "None",
        START = "Start",
        END = "End",
        BOTH = "Both"
    } 
    
    enum HOLD_POS {
        START = "Start",
        END = "End",
        BODY = "Body",
        OUT = "Out"
    }
}