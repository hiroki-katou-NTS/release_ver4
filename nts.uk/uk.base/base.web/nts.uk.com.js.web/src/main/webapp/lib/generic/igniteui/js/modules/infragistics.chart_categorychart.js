/*!@license
* Infragistics.Web.ClientUI infragistics.chart_categorychart.js 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
* Depends:
*     jquery-1.4.4.js
*     jquery.ui.core.js
*     jquery.ui.widget.js
*     infragistics.util.js
*/

(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["AbstractEnumerable:a", 
"Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Func$1:ba", 
"MulticastDelegate:bb", 
"IntPtr:bc", 
"AbstractEnumerator:bd", 
"Array:bo", 
"GenericEnumerable$1:cj", 
"GenericEnumerator$1:ck"]);


} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Array:c4", 
"Func$1:ib", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["IProvidesViewport:a", 
"Void:b", 
"ValueType:c", 
"Object:d", 
"Type:e", 
"Boolean:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"Series:be", 
"Control:bf", 
"FrameworkElement:bg", 
"UIElement:bh", 
"DependencyObject:bi", 
"Dictionary:bj", 
"DependencyProperty:bk", 
"PropertyMetadata:bl", 
"PropertyChangedCallback:bm", 
"MulticastDelegate:bn", 
"IntPtr:bo", 
"DependencyPropertyChangedEventArgs:bp", 
"DependencyPropertiesCollection:bq", 
"UnsetValue:br", 
"Script:bs", 
"Binding:bt", 
"PropertyPath:bu", 
"Transform:bv", 
"Visibility:bw", 
"Style:bx", 
"Thickness:by", 
"HorizontalAlignment:bz", 
"VerticalAlignment:b0", 
"INotifyPropertyChanged:b1", 
"PropertyChangedEventHandler:b2", 
"PropertyChangedEventArgs:b3", 
"SeriesView:b4", 
"ISchedulableRender:b5", 
"SeriesViewer:b6", 
"SeriesViewerView:b7", 
"CanvasRenderScheduler:b8", 
"List$1:b9", 
"IList$1:ca", 
"ICollection$1:cb", 
"IArray:cc", 
"IArrayList:cd", 
"Array:ce", 
"CompareCallback:cf", 
"Func$3:cg", 
"Action$1:ch", 
"Comparer$1:ci", 
"IComparer:cj", 
"IComparer$1:ck", 
"DefaultComparer$1:cl", 
"Comparison$1:cm", 
"ReadOnlyCollection$1:cn", 
"Predicate$1:co", 
"NotImplementedException:cp", 
"Callback:cq", 
"window:cr", 
"RenderingContext:cs", 
"IRenderer:ct", 
"Rectangle:cu", 
"Shape:cv", 
"Brush:cw", 
"Color:cx", 
"ArgumentException:cy", 
"DoubleCollection:cz", 
"Path:c0", 
"Geometry:c1", 
"GeometryType:c2", 
"TextBlock:c3", 
"Polygon:c4", 
"PointCollection:c5", 
"Polyline:c6", 
"DataTemplateRenderInfo:c7", 
"DataTemplatePassInfo:c8", 
"ContentControl:c9", 
"DataTemplate:da", 
"DataTemplateRenderHandler:db", 
"DataTemplateMeasureHandler:dc", 
"DataTemplateMeasureInfo:dd", 
"DataTemplatePassHandler:de", 
"Line:df", 
"FontInfo:dg", 
"XamOverviewPlusDetailPane:dh", 
"XamOverviewPlusDetailPaneView:di", 
"XamOverviewPlusDetailPaneViewManager:dj", 
"JQueryObject:dk", 
"Element:dl", 
"ElementAttributeCollection:dm", 
"ElementCollection:dn", 
"WebStyle:dp", 
"ElementNodeType:dq", 
"Document:dr", 
"EventListener:ds", 
"IElementEventHandler:dt", 
"ElementEventHandler:du", 
"ElementAttribute:dv", 
"JQueryPosition:dw", 
"JQueryCallback:dx", 
"JQueryEvent:dy", 
"JQueryUICallback:dz", 
"EventProxy:d0", 
"ModifierKeys:d1", 
"Func$2:d2", 
"MouseWheelHandler:d3", 
"Delegate:d4", 
"Interlocked:d5", 
"GestureHandler:d6", 
"ZoomGestureHandler:d7", 
"FlingGestureHandler:d8", 
"ContactHandler:d9", 
"TouchHandler:ea", 
"MouseOverHandler:eb", 
"MouseHandler:ec", 
"KeyHandler:ed", 
"Key:ee", 
"JQuery:ef", 
"JQueryDeferred:eg", 
"JQueryPromise:eh", 
"Action:ei", 
"CanvasViewRenderer:ej", 
"CanvasContext2D:ek", 
"CanvasContext:el", 
"TextMetrics:em", 
"ImageData:en", 
"CanvasElement:eo", 
"Gradient:ep", 
"LinearGradientBrush:eq", 
"GradientStop:er", 
"GeometryGroup:es", 
"GeometryCollection:et", 
"FillRule:eu", 
"PathGeometry:ev", 
"PathFigureCollection:ew", 
"LineGeometry:ex", 
"RectangleGeometry:ey", 
"EllipseGeometry:ez", 
"ArcSegment:e0", 
"PathSegment:e1", 
"PathSegmentType:e2", 
"SweepDirection:e3", 
"PathFigure:e4", 
"PathSegmentCollection:e5", 
"LineSegment:e6", 
"PolyLineSegment:e7", 
"BezierSegment:e8", 
"PolyBezierSegment:e9", 
"GeometryUtil:fa", 
"Tuple$2:fb", 
"TransformGroup:fc", 
"TransformCollection:fd", 
"TranslateTransform:fe", 
"RotateTransform:ff", 
"ScaleTransform:fg", 
"DivElement:fh", 
"BaseDOMEventProxy:fi", 
"DOMEventProxy:fj", 
"MSGesture:fk", 
"MouseEventArgs:fl", 
"EventArgs:fm", 
"DoubleAnimator:fn", 
"EasingFunctionHandler:fo", 
"ImageElement:fp", 
"RectUtil:fq", 
"MathUtil:fr", 
"RuntimeHelpers:fs", 
"RuntimeFieldHandle:ft", 
"PropertyChangedEventArgs$1:fu", 
"InteractionState:fv", 
"OverviewPlusDetailPaneMode:fw", 
"IOverviewPlusDetailControl:fx", 
"EventHandler$1:fy", 
"ArgumentNullException:fz", 
"OverviewPlusDetailViewportHost:f0", 
"XamDataChart:f1", 
"GridMode:f2", 
"BrushCollection:f3", 
"ObservableCollection$1:f4", 
"INotifyCollectionChanged:f5", 
"NotifyCollectionChangedEventHandler:f6", 
"NotifyCollectionChangedEventArgs:f7", 
"NotifyCollectionChangedAction:f8", 
"InterpolationMode:f9", 
"Random:ga", 
"ColorUtil:gb", 
"AxisCollection:gc", 
"XamDataChartView:gd", 
"SeriesViewerViewManager:ge", 
"AxisTitlePosition:gf", 
"PointerTooltipStyle:gg", 
"Dictionary$2:gh", 
"IDictionary$2:gi", 
"IDictionary:gj", 
"KeyValuePair$2:gk", 
"Enumerable:gl", 
"Thread:gm", 
"ThreadStart:gn", 
"IOrderedEnumerable$1:go", 
"SortedList$1:gp", 
"IEqualityComparer$1:gq", 
"EqualityComparer$1:gr", 
"IEqualityComparer:gs", 
"DefaultEqualityComparer$1:gt", 
"InvalidOperationException:gu", 
"CanvasGestureDOMEventProxy:gv", 
"TouchPointInfo:gw", 
"DOMExecutionContext:gx", 
"IExecutionContext:gy", 
"ExecutionContextExecuteCallback:gz", 
"TouchGestureRecognizer:g0", 
"TouchGestureState:g1", 
"TouchVelocityTracker:g2", 
"TouchHistoryItem:g3", 
"TouchVelocityReading:g4", 
"TouchGestureEventHandler:g5", 
"TouchGestureEventArgs:g6", 
"CancelableTouchGestureEventHandler:g7", 
"CssHelper:g8", 
"CssGradientUtil:g9", 
"FontUtil:ha", 
"TileZoomTile:hb", 
"TileZoomTileInfo:hc", 
"TileZoomTileCache:hd", 
"TileZoomManager:he", 
"RectChangedEventHandler:hf", 
"RectChangedEventArgs:hg", 
"Debug:hh", 
"TileZoomInfo:hi", 
"LinkedList$1:hj", 
"LinkedListNode$1:hk", 
"RenderSurface:hl", 
"FragmentBase:hm", 
"HorizontalAnchoredCategorySeries:hn", 
"AnchoredCategorySeries:ho", 
"CategorySeries:hp", 
"MarkerSeries:hq", 
"MarkerSeriesView:hr", 
"Marker:hs", 
"DataContext:ht", 
"MarkerTemplates:hu", 
"HashPool$2:hv", 
"IHashPool$2:hw", 
"IPool$1:hx", 
"Func$1:hy", 
"Pool$1:hz", 
"IIndexedPool$1:h0", 
"MarkerType:h1", 
"SeriesVisualData:h2", 
"PrimitiveVisualDataList:h3", 
"IVisualData:h4", 
"PrimitiveVisualData:h5", 
"PrimitiveAppearanceData:h6", 
"BrushAppearanceData:h7", 
"StringBuilder:h8", 
"Environment:h9", 
"AppearanceHelper:ia", 
"LinearGradientBrushAppearanceData:ib", 
"GradientStopAppearanceData:ic", 
"SolidBrushAppearanceData:id", 
"GeometryData:ie", 
"GetPointsSettings:ig", 
"EllipseGeometryData:ih", 
"RectangleGeometryData:ii", 
"LineGeometryData:ij", 
"PathGeometryData:ik", 
"PathFigureData:il", 
"SegmentData:im", 
"LineSegmentData:io", 
"PolylineSegmentData:ip", 
"ArcSegmentData:iq", 
"PolyBezierSegmentData:ir", 
"BezierSegmentData:is", 
"LabelAppearanceData:it", 
"ShapeTags:iu", 
"PointerTooltipVisualDataList:iv", 
"MarkerVisualDataList:iw", 
"MarkerVisualData:ix", 
"PointerTooltipVisualData:iy", 
"RectangleVisualData:iz", 
"PolygonVisualData:i0", 
"PolyLineVisualData:i1", 
"IFastItemsSource:i2", 
"IFastItemColumn$1:i3", 
"IFastItemColumnPropertyName:i4", 
"FastItemsSourceEventArgs:i5", 
"FastItemsSourceEventAction:i6", 
"IHasCategoryModePreference:i7", 
"IHasCategoryAxis:i8", 
"CategoryAxisBase:i9", 
"Axis:ja", 
"AxisView:jb", 
"StackedSeriesBase:jc", 
"IIsCategoryBased:jd", 
"CategoryMode:je", 
"ICategoryScaler:jf", 
"IScaler:jg", 
"ScalerParams:jh", 
"IBucketizer:ji", 
"IDetectsCollisions:jj", 
"StackedSeriesView:jk", 
"CategorySeriesView:jl", 
"ISupportsMarkers:jm", 
"CategoryBucketCalculator:jn", 
"ISortingAxis:jo", 
"CategoryFrame:jp", 
"Frame:jq", 
"BrushUtil:jr", 
"Canvas:js", 
"Panel:jt", 
"UIElementCollection:ju", 
"StackedBucketCalculator:jv", 
"StackedSeriesManager:jw", 
"StackedSeriesCollection:jx", 
"StackedFragmentSeries:jy", 
"PenLineCap:jz", 
"PropertyUpdatedEventHandler:j0", 
"PropertyUpdatedEventArgs:j1", 
"StackedAreaSeries:j2", 
"HorizontalStackedSeriesBase:j3", 
"NumericYAxis:j4", 
"StraightNumericAxisBase:j5", 
"NumericAxisBase:j6", 
"NumericAxisBaseView:j7", 
"NumericAxisRenderer:j8", 
"AxisRendererBase:j9", 
"ShouldRenderHandler:ka", 
"ScaleValueHandler:kb", 
"AxisRenderingParametersBase:kc", 
"RangeInfo:kd", 
"TickmarkValues:ke", 
"TickmarkValuesInitializationParameters:kf", 
"Func$4:kg", 
"GetGroupCenterHandler:kh", 
"GetUnscaledGroupCenterHandler:ki", 
"PathRenderingInfo:kj", 
"RenderStripHandler:kk", 
"RenderLineHandler:kl", 
"ShouldRenderLinesHandler:km", 
"ShouldRenderContentHandler:kn", 
"RenderAxisLineHandler:ko", 
"DetermineCrossingValueHandler:kp", 
"ShouldRenderLabelHandler:kq", 
"GetLabelLocationHandler:kr", 
"LabelPosition:ks", 
"TransformToLabelValueHandler:kt", 
"AxisLabelManager:ku", 
"AxisLabelPanelBase:kv", 
"AxisLabelPanelBaseView:kw", 
"AxisLabelSettings:kx", 
"AxisLabelsLocation:ky", 
"TitleSettings:kz", 
"GetLabelForItemHandler:k0", 
"CreateRenderingParamsHandler:k1", 
"SnapMajorValueHandler:k2", 
"AdjustMajorValueHandler:k3", 
"CategoryAxisRenderingParameters:k4", 
"LogarithmicTickmarkValues:k5", 
"LogarithmicNumericSnapper:k6", 
"Snapper:k7", 
"LinearTickmarkValues:k8", 
"LinearNumericSnapper:k9", 
"AxisRangeChangedEventArgs:la", 
"AxisRange:lb", 
"AutoRangeCalculator:lc", 
"NumericRadiusAxis:ld", 
"NumericRadiusAxisView:le", 
"NumericAngleAxis:lf", 
"IAngleScaler:lg", 
"NumericAngleAxisView:lh", 
"PolarAxisRenderingManager:li", 
"ViewportUtils:lj", 
"PolarAxisRenderingParameters:lk", 
"NumericAxisRenderingParameters:ll", 
"IPolarRadialRenderingParameters:lm", 
"RadialAxisRenderingParameters:ln", 
"AxisOrientation:lo", 
"AngleAxisLabelPanel:lp", 
"AngleAxisLabelPanelView:lq", 
"Extensions:lr", 
"CategoryAngleAxis:ls", 
"CategoryAngleAxisView:lt", 
"CategoryAxisBaseView:lu", 
"CategoryAxisRenderer:lv", 
"LinearCategorySnapper:lw", 
"CategoryTickmarkValues:lx", 
"RadialAxisLabelPanel:ly", 
"HorizontalAxisLabelPanelBase:lz", 
"HorizontalAxisLabelPanelBaseView:l0", 
"RadialAxisLabelPanelView:l1", 
"NumericScaler:l2", 
"StraightNumericAxisBaseView:l3", 
"NumericScaleMode:l4", 
"LogarithmicScaler:l5", 
"NumericXAxis:l6", 
"NumericXAxisView:l7", 
"HorizontalSmartAxisLabelPanel:l8", 
"AxisExtentType:l9", 
"SmartAxisLabelDisplayType:ma", 
"HorizontalSmartAxisLabelPanelView:mb", 
"FontMappingInfo:mc", 
"CategoryDateTimeXAxis:md", 
"CategoryDateTimeXAxisView:me", 
"TimeAxisDisplayType:mf", 
"FastItemDateTimeColumn:mg", 
"IFastItemColumnInternal:mh", 
"FastItemColumn:mi", 
"FastReflectionHelper:mj", 
"HorizontalAxisLabelPanel:mk", 
"CoercionInfo:ml", 
"SortedListView$1:mm", 
"ArrayUtil:mn", 
"HorizontalLogarithmicScaler:mo", 
"HorizontalLinearScaler:mp", 
"LinearScaler:mq", 
"NumericYAxisView:mr", 
"VerticalAxisLabelPanel:ms", 
"VerticalAxisLabelPanelView:mt", 
"VerticalLogarithmicScaler:mu", 
"VerticalLinearScaler:mv", 
"CategoryFramePreparerBase:mw", 
"FramePreparer:mx", 
"ISupportsErrorBars:my", 
"DefaultSupportsMarkers:mz", 
"DefaultProvidesViewport:m0", 
"DefaultSupportsErrorBars:m1", 
"PreparationParams:m2", 
"CategoryYAxis:m3", 
"CategoryYAxisView:m4", 
"SyncSettings:m5", 
"ValuesHolder:m6", 
"LineSeries:m7", 
"LineSeriesView:m8", 
"AnchoredCategorySeriesView:m9", 
"CategoryTrendLineManagerBase:na", 
"TrendLineManagerBase$1:nb", 
"TrendLineType:nc", 
"Clipper:nd", 
"EdgeClipper:ne", 
"LeftClipper:nf", 
"BottomClipper:ng", 
"RightClipper:nh", 
"TopClipper:ni", 
"TrendResolutionParams:nj", 
"Flattener:nk", 
"Stack$1:nl", 
"ReverseArrayEnumerator$1:nm", 
"SpiralTodo:nn", 
"FlattenerSettings:no", 
"IPreparesCategoryTrendline:np", 
"SortingTrendLineManager:nq", 
"TrendFitCalculator:nr", 
"LeastSquaresFit:ns", 
"Numeric:nt", 
"TrendAverageCalculator:nu", 
"CategoryTrendLineManager:nv", 
"AnchoredCategoryBucketCalculator:nw", 
"UnknownValuePlotting:nx", 
"CategoryLineRasterizer:ny", 
"Action$5:nz", 
"PathVisualData:n0", 
"CategorySeriesRenderManager:n1", 
"AssigningCategoryStyleEventArgs:n2", 
"AssigningCategoryStyleEventArgsBase:n3", 
"GetCategoryItemsHandler:n4", 
"HighlightingInfo:n5", 
"HighlightingState:n6", 
"AssigningCategoryMarkerStyleEventArgs:n7", 
"HighlightingManager:n8", 
"SplineSeriesBase:n9", 
"SplineSeriesBaseView:oa", 
"SplineType:ob", 
"CollisionAvoider:oc", 
"SafeSortedReadOnlyDoubleCollection:od", 
"SafeReadOnlyDoubleCollection:oe", 
"SafeEnumerable:of", 
"AreaSeries:og", 
"AreaSeriesView:oh", 
"LegendTemplates:oi", 
"PieChartBase:oj", 
"PieChartBaseView:ok", 
"PieChartViewManager:ol", 
"PieChartVisualData:om", 
"PieSliceVisualDataList:on", 
"PieSliceVisualData:oo", 
"PieSliceDataContext:op", 
"Slice:oq", 
"SliceView:or", 
"PieLabel:os", 
"LabelsPosition:ot", 
"MouseButtonEventArgs:ou", 
"FastItemsSource:ov", 
"ColumnReference:ow", 
"FastItemObjectColumn:ox", 
"FastItemIntColumn:oy", 
"LeaderLineType:oz", 
"OthersCategoryType:o0", 
"IndexCollection:o1", 
"LegendBase:o2", 
"LegendBaseView:o3", 
"LegendBaseViewManager:o4", 
"GradientData:o5", 
"GradientStopData:o6", 
"DataChartLegendMouseButtonEventArgs:o7", 
"DataChartMouseButtonEventArgs:o8", 
"ChartLegendMouseEventArgs:o9", 
"ChartMouseEventArgs:pa", 
"DataChartLegendMouseButtonEventHandler:pb", 
"DataChartLegendMouseEventHandler:pc", 
"LegendVisualData:pd", 
"LegendVisualDataList:pe", 
"LegendItemVisualData:pf", 
"FunnelSliceDataContext:pg", 
"PieChartFormatLabelHandler:ph", 
"LabelClickEventHandler:pi", 
"LabelClickEventArgs:pj", 
"SliceClickEventHandler:pk", 
"SliceClickEventArgs:pl", 
"ItemLegend:pm", 
"ItemLegendView:pn", 
"LegendItemInfo:po", 
"BubbleSeries:pp", 
"ScatterBase:pq", 
"ScatterBaseView:pr", 
"MarkerManagerBase:ps", 
"OwnedPoint:pt", 
"MarkerManagerBucket:pu", 
"ScatterTrendLineManager:pv", 
"NumericMarkerManager:pw", 
"CollisionAvoidanceType:px", 
"SmartPlacer:py", 
"ISmartPlaceable:pz", 
"SmartPosition:p0", 
"SmartPlaceableWrapper$1:p1", 
"ScatterAxisInfoCache:p2", 
"ScatterErrorBarSettings:p3", 
"ErrorBarSettingsBase:p4", 
"EnableErrorBars:p5", 
"ErrorBarCalculatorReference:p6", 
"IErrorBarCalculator:p7", 
"ErrorBarCalculatorType:p8", 
"ScatterFrame:p9", 
"ScatterFrameBase$1:qa", 
"DictInterpolator$3:qb", 
"Action$6:qc", 
"SeriesHitTestMode:qd", 
"SyncLink:qe", 
"IFastItemsSourceProvider:qf", 
"ChartCollection:qg", 
"FastItemsSourceReference:qh", 
"SyncManager:qi", 
"SyncLinkManager:qj", 
"ErrorBarsHelper:qk", 
"BubbleSeriesView:ql", 
"BubbleMarkerManager:qm", 
"SizeScale:qn", 
"BrushScale:qo", 
"ScaleLegend:qp", 
"ScaleLegendView:qq", 
"CustomPaletteBrushScale:qr", 
"BrushSelectionMode:qs", 
"ValueBrushScale:qt", 
"RingSeriesBase:qu", 
"XamDoughnutChart:qv", 
"RingCollection:qw", 
"Ring:qx", 
"RingControl:qy", 
"RingControlView:qz", 
"Arc:q0", 
"ArcView:q1", 
"ArcItem:q2", 
"SliceItem:q3", 
"RingSeriesBaseView:q4", 
"Nullable$1:q5", 
"RingSeriesCollection:q6", 
"SliceCollection:q7", 
"XamDoughnutChartView:q8", 
"Action$2:q9", 
"DoughnutChartVisualData:ra", 
"RingSeriesVisualDataList:rb", 
"RingSeriesVisualData:rc", 
"RingVisualDataList:rd", 
"RingVisualData:re", 
"ArcVisualDataList:rf", 
"ArcVisualData:rg", 
"SliceVisualDataList:rh", 
"SliceVisualData:ri", 
"DoughnutChartLabelVisualData:rj", 
"HoleDimensionsChangedEventHandler:rk", 
"HoleDimensionsChangedEventArgs:rl", 
"XamFunnelChart:rm", 
"IItemProvider:rn", 
"MessageHandler:ro", 
"MessageHandlerEventHandler:rp", 
"Message:rq", 
"ServiceProvider:rr", 
"MessageChannel:rs", 
"MessageEventHandler:rt", 
"Queue$1:ru", 
"XamFunnelConnector:rv", 
"XamFunnelController:rw", 
"SliceInfoList:rx", 
"SliceInfo:ry", 
"SliceAppearance:rz", 
"PointList:r0", 
"FunnelSliceVisualData:r1", 
"PointData:r2", 
"SliceInfoUnaryComparison:r3", 
"Bezier:r4", 
"BezierPoint:r5", 
"BezierOp:r6", 
"BezierPointComparison:r7", 
"DoubleColumn:r8", 
"ObjectColumn:r9", 
"XamFunnelView:sa", 
"IOuterLabelWidthDecider:sb", 
"IFunnelLabelSizeDecider:sc", 
"MouseLeaveMessage:sd", 
"InteractionMessage:se", 
"MouseMoveMessage:sf", 
"MouseButtonMessage:sg", 
"MouseButtonAction:sh", 
"MouseButtonType:si", 
"SetAreaSizeMessage:sj", 
"RenderingMessage:sk", 
"RenderSliceMessage:sl", 
"RenderOuterLabelMessage:sm", 
"TooltipValueChangedMessage:sn", 
"TooltipUpdateMessage:so", 
"FunnelDataContext:sp", 
"PropertyChangedMessage:sq", 
"ConfigurationMessage:sr", 
"ClearMessage:ss", 
"ClearTooltipMessage:st", 
"ContainerSizeChangedMessage:su", 
"ViewportChangedMessage:sv", 
"ViewPropertyChangedMessage:sw", 
"OuterLabelAlignment:sx", 
"FunnelSliceDisplay:sy", 
"SliceSelectionManager:sz", 
"DataUpdatedMessage:s0", 
"ItemsSourceAction:s1", 
"FunnelFrame:s2", 
"UserSelectedItemsChangedMessage:s3", 
"LabelSizeChangedMessage:s4", 
"FrameRenderCompleteMessage:s5", 
"IntColumn:s6", 
"IntColumnComparison:s7", 
"Convert:s8", 
"SelectedItemsChangedMessage:s9", 
"ModelUpdateMessage:ta", 
"SliceClickedMessage:tb", 
"FunnelSliceClickedEventHandler:tc", 
"FunnelSliceClickedEventArgs:td", 
"FunnelChartVisualData:te", 
"FunnelSliceVisualDataList:tf", 
"RingSeries:tg", 
"WaterfallSeries:th", 
"WaterfallSeriesView:ti", 
"CategoryTransitionInMode:tj", 
"FinancialSeries:tk", 
"FinancialSeriesView:tl", 
"FinancialBucketCalculator:tm", 
"CategoryTransitionSourceFramePreparer:tn", 
"TransitionInSpeedType:to", 
"FinancialCalculationDataSource:tp", 
"CalculatedColumn:tq", 
"FinancialEventArgs:tr", 
"FinancialCalculationSupportingCalculations:ts", 
"ColumnSupportingCalculation:tt", 
"SupportingCalculation$1:tu", 
"SupportingCalculationStrategy:tv", 
"DataSourceSupportingCalculation:tw", 
"ProvideColumnValuesStrategy:tx", 
"AssigningCategoryStyleEventHandler:ty", 
"FinancialValueList:tz", 
"CategoryXAxis:t0", 
"CategoryXAxisView:t1", 
"FinancialEventHandler:t2", 
"StepLineSeries:t3", 
"StepLineSeriesView:t4", 
"StepAreaSeries:t5", 
"StepAreaSeriesView:t6", 
"RangeAreaSeries:t7", 
"HorizontalRangeCategorySeries:t8", 
"RangeCategorySeries:t9", 
"IHasHighLowValueCategory:ua", 
"RangeCategorySeriesView:ub", 
"RangeCategoryBucketCalculator:uc", 
"RangeCategoryFramePreparer:ud", 
"IHasCategoryTrendline:ue", 
"IHasTrendline:uf", 
"DefaultCategoryTrendlineHost:ug", 
"DefaultCategoryTrendlinePreparer:uh", 
"DefaultHighLowValueProvider:ui", 
"HighLowValuesHolder:uj", 
"CategoryMarkerManager:uk", 
"RangeValueList:ul", 
"RangeAreaSeriesView:um", 
"LineFragment:un", 
"LineFragmentView:uo", 
"LineFragmentBucketCalculator:up", 
"IStacked100Series:uq", 
"AreaFragment:ur", 
"AreaFragmentView:us", 
"AreaFragmentBucketCalculator:ut", 
"StackedSplineAreaSeries:uu", 
"SplineAreaFragment:uv", 
"SplineFragmentBase:uw", 
"SplineAreaFragmentView:ux", 
"StackedColumnSeries:uy", 
"StackedColumnSeriesView:uz", 
"StackedColumnBucketCalculator:u0", 
"ColumnFragment:u1", 
"ColumnFragmentView:u2", 
"StackedBarSeries:u3", 
"VerticalStackedSeriesBase:u4", 
"IBarSeries:u5", 
"StackedBarSeriesView:u6", 
"StackedBarBucketCalculator:u7", 
"BarFragment:u8", 
"StackedLineSeries:u9", 
"StackedSplineSeries:va", 
"SplineFragment:vb", 
"SplineFragmentView:vc", 
"SplineFragmentBucketCalculator:vd", 
"StackedSeriesFramePreparer:ve", 
"CategoryFramePreparer:vf", 
"IHasSingleValueCategory:vg", 
"DefaultSingleValueProvider:vh", 
"SingleValuesHolder:vi", 
"StackedSeriesCreatedEventHandler:vj", 
"StackedSeriesCreatedEventArgs:vk", 
"StackedSeriesVisualData:vl", 
"SeriesVisualDataList:vm", 
"AxisComponentsForView:vn", 
"AxisComponentsFromView:vo", 
"AxisFormatLabelHandler:vp", 
"VisualExportHelper:vq", 
"ContentInfo:vr", 
"LabelFontHeuristics:vs", 
"AxisRangeChangedEventHandler:vt", 
"ChartContentManager:vu", 
"ChartContentType:vv", 
"RenderRequestedEventArgs:vw", 
"ChartTitleVisualData:vx", 
"VisualDataSerializer:vy", 
"AxisVisualData:vz", 
"AxisLabelVisualDataList:v0", 
"AxisLabelVisualData:v1", 
"AssigningCategoryMarkerStyleEventHandler:v2", 
"SeriesComponentsForView:v3", 
"CategorySeriesMarkerCollisionAvoidance:v4", 
"NonCollisionAvoider:v5", 
"DataChartAxisRangeChangedEventHandler:v6", 
"ChartAxisRangeChangedEventArgs:v7", 
"ChartVisualData:v8", 
"AxisVisualDataList:v9", 
"RadialBase:wa", 
"RadialBaseView:wb", 
"RadialBucketCalculator:wc", 
"SeriesRenderer$2:wd", 
"SeriesRenderingArguments:we", 
"RadialFrame:wf", 
"RadialAxes:wg", 
"PolarBase:wh", 
"PolarBaseView:wi", 
"PolarTrendLineManager:wj", 
"PolarLinePlanner:wk", 
"AngleRadiusPair:wl", 
"PolarAxisInfoCache:wm", 
"PolarFrame:wn", 
"PolarAxes:wo", 
"SeriesCollection:wp", 
"SeriesViewerComponentsFromView:wq", 
"SeriesViewerSurfaceViewer:wr", 
"LabelPanelArranger:ws", 
"LabelPanelsArrangeState:wt", 
"ChartHitTestMode:wu", 
"WindowResponse:wv", 
"ViewerSurfaceUsage:ww", 
"SeriesViewerComponentsForView:wx", 
"DataChartCursorEventHandler:wy", 
"ChartCursorEventArgs:wz", 
"DataChartMouseButtonEventHandler:w0", 
"DataChartMouseEventHandler:w1", 
"AnnotationLayer:w2", 
"AnnotationLayerView:w3", 
"RefreshCompletedEventHandler:w4", 
"SeriesComponentsFromView:w5", 
"EasingFunctions:w6", 
"PolygonUtil:w7", 
"TrendCalculators:w8", 
"BarFramePreparer:yi", 
"BarTrendFitCalculator:yj", 
"BarTrendLineManager:yk", 
"VerticalAnchoredCategorySeries:yl", 
"BarSeries:ym", 
"BarSeriesView:yn", 
"BarBucketCalculator:yo", 
"PointSeries:yp", 
"PointSeriesView:yq", 
"ColumnSeries:y3", 
"ColumnSeriesView:y4", 
"ConsolidatedItemsPosition:y5", 
"SplineAreaSeries:y8", 
"SplineAreaSeriesView:y9", 
"SplineSeries:za", 
"SplineSeriesView:zb", 
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('ConsolidatedItemsPosition', 'Enum', {
	init: function (v) {
		this._v = v;
	},
	$getName: function (v) {
		switch (v) {
			case 0: return "Minimum";
			case 1: return "Maximum";
			case 2: return "Median";
			case 3: return "RelativeMinimum";
			case 4: return "RelativeMaximum";
			default: return v.toString();
		}
	},
	$value: function () {
		return this._v;
	},
	$type: new $.ig.Type('ConsolidatedItemsPosition', $.ig.Enum.prototype.$type)
}, true);

$.ig.util.defType('BarFramePreparer', 'CategoryFramePreparer', {
	init: function (initNumber, host) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.BarFramePreparer.prototype.init1.call(this, 1, host, $.ig.util.cast($.ig.ISupportsMarkers.prototype.$type, host), $.ig.util.cast($.ig.IProvidesViewport.prototype.$type, host), $.ig.util.cast($.ig.ISupportsErrorBars.prototype.$type, host), $.ig.util.cast($.ig.IBucketizer.prototype.$type, host));
	},
	init1: function (initNumber, host, markersHost, viewportHost, errorBarsHost, bucketizingHost) {
		$.ig.CategoryFramePreparer.prototype.init1.call(this, 1, host, markersHost, viewportHost, errorBarsHost, bucketizingHost);
		this.trendlineHost(new $.ig.DefaultCategoryTrendlineHost());
		if ($.ig.util.cast($.ig.IHasCategoryTrendline.prototype.$type, host) !== null) {
			this.trendlineHost($.ig.util.cast($.ig.IHasCategoryTrendline.prototype.$type, host));
		}
		this.valuesProvider(new $.ig.DefaultSingleValueProvider());
		if ($.ig.util.cast($.ig.IHasSingleValueCategory.prototype.$type, host) !== null) {
			this.valuesProvider($.ig.util.cast($.ig.IHasSingleValueCategory.prototype.$type, host));
		}
	},
	prepareMarker: function (frame, bucket, collisionAvoider, itemIndex, markerCount, markerBucket) {
		var x = bucket[1];
		var y = bucket[0];
		var markerRect = new $.ig.Rect(0, x - 5, y - 5, 11, 11);
		if (!$.ig.util.isNaN(x) && !$.ig.util.isNaN(y) && !Number.isInfinity(x) && !Number.isInfinity(y) && collisionAvoider.tryAdd(markerRect)) {
			frame._markers.add({ __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			this.markersHost().updateMarkerTemplate(markerCount, itemIndex, markerBucket);
			return true;
		}
		return false;
	}
	,
	prepareTrendline: function (p, h, offset) {
		if (this.trendlineHost().trendLineType() == $.ig.TrendLineType.prototype.none || this.trendlineHost().trendlinePreparer() == null || this.trendlineHost().trendLinePeriod() < 1) {
			return;
		}
		var effectiveViewportRect = p.effectiveViewportRect();
		var sParams = new $.ig.ScalerParams(0, p.windowRect(), p.viewportRect(), p.scaler().isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, p.windowRect(), p.viewportRect(), p.yScaler().isInverted(), effectiveViewportRect);
		var values = (h).values();
		if (p.sortingScaler() != null && p.sortingScaler().sortedIndices() != null) {
			values = new $.ig.SafeSortedReadOnlyDoubleCollection(0, values, p.sortingScaler().sortedIndices());
		}
		var trendResolutionParams = (function () {
			var $ret = new $.ig.TrendResolutionParams();
			$ret.bucketSize(p.bucketSize());
			$ret.firstBucket(p.firstBucket());
			$ret.lastBucket(p.lastBucket());
			$ret.offset(offset);
			$ret.resolution(p.resolution());
			$ret.viewport(p.viewportRect());
			return $ret;
		}());
		if (this.trendlineHost().trendLineType() != $.ig.TrendLineType.prototype.none) {
			this.trendlineHost().trendlinePreparer().prepareLine(p.frame()._trend, this.trendlineHost().trendLineType(), values, this.trendlineHost().trendLinePeriod(), function (x) { return p.yScaler().getScaledValue(x, yParams); }, function (y) { return p.scaler().getScaledValue(y, sParams); }, trendResolutionParams);
		}
	}
	,
	$type: new $.ig.Type('BarFramePreparer', $.ig.CategoryFramePreparer.prototype.$type)
}, true);

$.ig.util.defType('BarTrendFitCalculator', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	calculateFit: function (trend, trendLineType, trendResolutionParams, trendCoefficients, count, GetUnscaledX, GetUnscaledY, GetScaledXValue, GetScaledYValue, ymin, ymax) {
		if (trendCoefficients == null) {
			switch (trendLineType) {
				case $.ig.TrendLineType.prototype.linearFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.linearFit(count, GetUnscaledY, GetUnscaledX);
					break;
				case $.ig.TrendLineType.prototype.quadraticFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.quadraticFit(count, GetUnscaledY, GetUnscaledX);
					break;
				case $.ig.TrendLineType.prototype.cubicFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.cubicFit(count, GetUnscaledY, GetUnscaledX);
					break;
				case $.ig.TrendLineType.prototype.quarticFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.quarticFit(count, GetUnscaledY, GetUnscaledX);
					break;
				case $.ig.TrendLineType.prototype.quinticFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.quinticFit(count, GetUnscaledY, GetUnscaledX);
					break;
				case $.ig.TrendLineType.prototype.exponentialFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.exponentialFit(count, GetUnscaledY, GetUnscaledX);
					break;
				case $.ig.TrendLineType.prototype.logarithmicFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.logarithmicFit(count, GetUnscaledY, GetUnscaledX);
					break;
				case $.ig.TrendLineType.prototype.powerLawFit:
					trendCoefficients = $.ig.LeastSquaresFit.prototype.powerLawFit(count, GetUnscaledY, GetUnscaledX);
					break;
				default: throw new $.ig.NotImplementedException(0);
			}
		}
		if (trendCoefficients == null) {
			return null;
		}
		for (var i = 0; i < trendResolutionParams.viewport().height(); i += 2) {
			var p = i / (trendResolutionParams.viewport().height() - 1);
			var yi = ymin + p * (ymax - ymin);
			var xi = NaN;
			switch (trendLineType) {
				case $.ig.TrendLineType.prototype.linearFit:
					xi = $.ig.LeastSquaresFit.prototype.linearEvaluate(trendCoefficients, yi);
					break;
				case $.ig.TrendLineType.prototype.quadraticFit:
					xi = $.ig.LeastSquaresFit.prototype.quadraticEvaluate(trendCoefficients, yi);
					break;
				case $.ig.TrendLineType.prototype.cubicFit:
					xi = $.ig.LeastSquaresFit.prototype.cubicEvaluate(trendCoefficients, yi);
					break;
				case $.ig.TrendLineType.prototype.quarticFit:
					xi = $.ig.LeastSquaresFit.prototype.quarticEvaluate(trendCoefficients, yi);
					break;
				case $.ig.TrendLineType.prototype.quinticFit:
					xi = $.ig.LeastSquaresFit.prototype.quinticEvaluate(trendCoefficients, yi);
					break;
				case $.ig.TrendLineType.prototype.exponentialFit:
					xi = $.ig.LeastSquaresFit.prototype.exponentialEvaluate(trendCoefficients, yi);
					break;
				case $.ig.TrendLineType.prototype.logarithmicFit:
					xi = $.ig.LeastSquaresFit.prototype.logarithmicEvaluate(trendCoefficients, yi);
					break;
				case $.ig.TrendLineType.prototype.powerLawFit:
					xi = $.ig.LeastSquaresFit.prototype.powerLawEvaluate(trendCoefficients, yi);
					break;
				default: throw new $.ig.NotImplementedException(0);
			}
			xi = GetScaledXValue(xi);
			yi = GetScaledYValue(yi);
			if (!$.ig.util.isNaN(xi) && !Number.isInfinity(xi)) {
				trend.add({ __x: xi, __y: yi + trendResolutionParams.offset(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			}
		}
		return trendCoefficients;
	}
	,
	$type: new $.ig.Type('BarTrendFitCalculator', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('BarTrendLineManager', 'CategoryTrendLineManager', {
	init: function () {
		$.ig.CategoryTrendLineManager.prototype.init.call(this);
	},
	prepareLine: function (flattenedPoints, trendLineType, valueColumn, period, GetScaledXValue, GetScaledYValue, trendResolutionParams) {
		var ymin = trendResolutionParams.firstBucket() * trendResolutionParams.bucketSize();
		var ymax = trendResolutionParams.lastBucket() * trendResolutionParams.bucketSize();
		var trend = new $.ig.List$1($.ig.Point.prototype.$type, 0);
		if (trendLineType == $.ig.TrendLineType.prototype.none) {
			this.trendCoefficients(null);
			this.trendColumn().clear();
			return;
		}
		if (this.isFit(trendLineType)) {
			this.trendColumn().clear();
			this.trendCoefficients($.ig.BarTrendFitCalculator.prototype.calculateFit(trend, trendLineType, trendResolutionParams, this.trendCoefficients(), valueColumn.count(), function (i) { return valueColumn.item(i); }, function (i) { return i + 1; }, GetScaledXValue, function (x) { return GetScaledYValue(x - 1); }, ymin + 1, ymax + 1));
		}
		if (this.isAverage(trendLineType)) {
			this.trendCoefficients(null);
			$.ig.TrendAverageCalculator.prototype.calculateSingleValueAverage(trendLineType, this.trendColumn(), valueColumn, period);
			for (var i = trendResolutionParams.firstBucket(); i <= trendResolutionParams.lastBucket(); i += 1) {
				var itemIndex = i * trendResolutionParams.bucketSize();
				if (itemIndex >= 0 && itemIndex < this.trendColumn().count()) {
					var xi = GetScaledXValue(this.trendColumn().__inner[itemIndex]);
					var yi = GetScaledYValue(itemIndex);
					trend.add({ __x: xi, __y: yi + trendResolutionParams.offset(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
				}
			}
		}
		this.flattenTrendLine(trend, trendResolutionParams, flattenedPoints);
	}
	,
	$type: new $.ig.Type('BarTrendLineManager', $.ig.CategoryTrendLineManager.prototype.$type)
}, true);

$.ig.util.defType('VerticalAnchoredCategorySeries', 'AnchoredCategorySeries', {
	init: function () {
		$.ig.AnchoredCategorySeries.prototype.init.call(this);
	},
	xAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.VerticalAnchoredCategorySeries.prototype.xAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.VerticalAnchoredCategorySeries.prototype.xAxisProperty);
		}
	}
	,
	yAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.VerticalAnchoredCategorySeries.prototype.yAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.VerticalAnchoredCategorySeries.prototype.yAxisProperty);
		}
	}
	,
	isVertical: function () {
		return true;
	}
	,
	getCategoryAxis: function () {
		return this.yAxis();
	}
	,
	getValueAxis: function () {
		return this.xAxis();
	}
	,
	getOffsetValue: function () {
		return this.framePreparer().getOffset(this.getCategoryAxis(), this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	getCategoryWidth: function () {
		return this.yAxis().getCategorySize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	getNextOrExactIndex: function (world, skipUnknowns) {
		return this.getNextOrExactIndexHelper(world, skipUnknowns, this.getCategoryAxis(), this.getExactUnsortedItemIndex.runOn(this), this.valueColumn());
	}
	,
	getPreviousOrExactIndex: function (world, skipUnknowns) {
		return this.getPreviousOrExactIndexHelper(world, skipUnknowns, this.getCategoryAxis(), this.getExactUnsortedItemIndex.runOn(this), this.valueColumn());
	}
	,
	getDistanceToIndex: function (world, index, axis, p, offset) {
		if (this.valueColumn() == null) {
			return Number.POSITIVE_INFINITY;
		}
		return this.getDistanceToIndexHelper(world, index, this.yAxis(), p, offset, this.valueColumn().count(), this.getExactUnsortedItemIndex.runOn(this));
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.computational;
		} else {
			return $.ig.AnchoredCategorySeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	getSeriesValueMarkerBoundingBox: function (world) {
		if (!this.hasMarkers() || !this.shouldDisplayMarkers()) {
			return $.ig.Rect.prototype.empty();
		}
		var markers = this._currentFrame._markers;
		var pos = this.fromWorldPosition(world);
		var index = -1;
		var isInverted = this.getCategoryAxis().isInverted();
		var maxPosition = this.viewport().bottom() * 2;
		if (isInverted) {
			index = $.ig.ArrayUtil.prototype.binarySearch$11($.ig.Point.prototype.$type, markers, function (item) {
				return (maxPosition - pos.__y) < (maxPosition - item.__y) ? -1 : ((maxPosition - pos.__y) > (maxPosition - item.__y) ? 1 : 0);
			});
		} else {
			index = $.ig.ArrayUtil.prototype.binarySearch$11($.ig.Point.prototype.$type, markers, function (item) {
				return pos.__y < item.__y ? -1 : (pos.__y > item.__y ? 1 : 0);
			});
		}
		if (index < 0) {
			index = ~index;
		}
		if (index < 0) {
			index = 0;
		}
		if (index > markers.count() - 1) {
			index = markers.count() - 1;
		}
		var prevIndex = index;
		var nextIndex = index;
		if (prevIndex > 0) {
			prevIndex--;
		}
		if (nextIndex < markers.count() - 1) {
			nextIndex++;
		}
		var prevY = markers.__inner[prevIndex].__y;
		var currY = markers.__inner[index].__y;
		var nextY = markers.__inner[nextIndex].__y;
		var distPrev = Math.abs(prevY - pos.__y);
		var distCurr = Math.abs(currY - pos.__y);
		var distNext = Math.abs(nextY - pos.__y);
		var prevBox = this.getMarkerBoundingBox(prevIndex);
		var currBox = this.getMarkerBoundingBox(index);
		var nextBox = this.getMarkerBoundingBox(nextIndex);
		var prevContains = prevBox.containsPoint(pos);
		var currContains = currBox.containsPoint(pos);
		var nextContains = nextBox.containsPoint(pos);
		if (distCurr <= distPrev && distCurr <= distNext) {
			if (!isInverted && nextContains) {
				return nextBox;
			}
			if (isInverted && prevContains) {
				return prevBox;
			}
			return currBox;
		}
		if (distPrev <= distCurr && distPrev <= distNext) {
			if (!isInverted && currContains) {
				return currBox;
			}
			return this.getMarkerBoundingBox(prevIndex);
		}
		if (distNext <= distCurr && distNext <= distPrev) {
			if (isInverted && currContains) {
				return currBox;
			}
			return this.getMarkerBoundingBox(nextIndex);
		}
		return $.ig.Rect.prototype.empty();
	}
	,
	getMarkerBoundingBox: function (index) {
		return this.anchoredView().getMarkerBoundingBox(index);
	}
	,
	testHit: function (position, isFinger) {
		if (this.testHighLowStrokeOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	getSeriesValue: function (world, useInterpolation, skipUnknowns) {
		if (this.seriesViewer() == null) {
			return NaN;
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var categoryAxis = this.getCategoryAxis();
		var yParams = new $.ig.ScalerParams(0, this.seriesViewer().actualWindowRect(), this.view().viewport(), categoryAxis.isInverted(), effectiveViewportRect);
		var offset = this.framePreparer().getOffset(categoryAxis, this.seriesViewer().actualWindowRect(), this.view().viewport(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.valueColumn(), world, categoryAxis, yParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesValuePosition: function (world, useInterpolation, skipUnknowns) {
		var categoryAxis = this.getCategoryAxis();
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.framePreparer().getOffset(categoryAxis, this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.getValueAxis(), categoryAxis, null, null, null);
	}
	,
	getXAxis: function () {
		return this.xAxis();
	}
	,
	getYAxis: function () {
		return this.yAxis();
	}
	,
	setXAxis: function (xAxis) {
		this.xAxis($.ig.util.cast($.ig.NumericXAxis.prototype.$type, xAxis));
	}
	,
	setYAxis: function (yAxis) {
		this.yAxis($.ig.util.cast($.ig.CategoryYAxis.prototype.$type, yAxis));
	}
	,
	updateNumericAxisRange: function () {
		return this.xAxis() != null && this.xAxis().updateRange();
	}
	,
	getCategoryItems: function (orderedStartIndex, orderedEndIndex) {
		return this.getCategoryItemsHelper(orderedStartIndex, orderedEndIndex, this.yAxis());
	}
	,
	getExactItemIndex: function (world) {
		var windowRect = this.seriesViewer() != null ? this.seriesViewer().actualWindowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = this.view().viewport();
		var contentViewport = this.getContentViewport(this.view());
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var categoryAxis = this.getCategoryAxis();
		var rowIndex = -1;
		if (categoryAxis != null && !windowRect.isEmpty() && !viewportRect.isEmpty()) {
			var p = new $.ig.ScalerParams(0, windowRect, viewportRect, categoryAxis.isInverted(), effectiveViewportRect);
			var top = categoryAxis.getUnscaledValue(contentViewport.top(), p);
			var bottom = categoryAxis.getUnscaledValue(contentViewport.bottom(), p);
			var windowY = (world.__y - windowRect.top()) / windowRect.height();
			var bucket = top + (windowY * (bottom - top));
			if (categoryAxis.categoryMode() != $.ig.CategoryMode.prototype.mode0) {
				bucket -= 0.5;
			}
			rowIndex = bucket;
		}
		return rowIndex;
	}
	,
	getExactUnsortedItemIndex: function (world) {
		return this.getExactUnsortedItemIndexHelper(world, this.getCategoryAxis());
	}
	,
	getItemIndex: function (world) {
		return $.ig.truncate(Math.round(this.getExactItemIndex(world)));
	}
	,
	getItem: function (world) {
		var index = this.getItemIndex(world);
		return index >= 0 && this.fastItemsSource() != null && index < this.fastItemsSource().count() ? this.fastItemsSource().item(index) : null;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.AnchoredCategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.VerticalAnchoredCategorySeries.prototype.xAxisPropertyName:
				if (oldValue != newValue) {
					this.deregisterForAxis($.ig.util.cast($.ig.Axis.prototype.$type, oldValue));
					this.registerForAxis($.ig.util.cast($.ig.Axis.prototype.$type, newValue));
					this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
					this.notifyThumbnailAppearanceChanged();
				}
				break;
			case $.ig.VerticalAnchoredCategorySeries.prototype.yAxisPropertyName:
				if (oldValue != newValue) {
					this.deregisterForAxis($.ig.util.cast($.ig.Axis.prototype.$type, oldValue));
					this.registerForAxis($.ig.util.cast($.ig.Axis.prototype.$type, newValue));
					this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
					this.updateNumericAxisRange();
					this.renderSeries(false);
					this.notifyThumbnailAppearanceChanged();
				}
				break;
		}
	}
	,
	canUseAsYAxis: function (axis) {
		if ($.ig.util.cast($.ig.CategoryYAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	canUseAsXAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericXAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	$type: new $.ig.Type('VerticalAnchoredCategorySeries', $.ig.AnchoredCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('BarSeries', 'VerticalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.BarSeriesView(this);
	}
	,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.fromZero;
	}
	,
	onViewCreated: function (view) {
		$.ig.VerticalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.barView(view);
	}
	,
	_barView: null,
	barView: function (value) {
		if (arguments.length === 1) {
			this._barView = value;
			return value;
		} else {
			return this._barView;
		}
	}
	,
	hasIndividualElements: function () {
		return true;
	}
	,
	getSeriesValueBoundingBox: function (world) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
		var pos = this.fromWorldPosition(world);
		var previousIndex = this.getPreviousOrExactIndex(world, true);
		var nextIndex = this.getNextOrExactIndex(world, true);
		var prevBucket = $.ig.truncate(Math.floor(previousIndex / this.getBucketSize(this.view())));
		var nextBucket = $.ig.truncate(Math.floor(nextIndex / this.getBucketSize(this.view())));
		var prevBucketIndex = prevBucket - this.getFirstBucket(this.view());
		var nextBucketIndex = nextBucket - this.getFirstBucket(this.view());
		if (prevBucketIndex < 0) {
			prevBucketIndex = 0;
		}
		if (prevBucketIndex > this._currentFrame._buckets.count() - 1) {
			prevBucketIndex = this._currentFrame._buckets.count() - 1;
		}
		if (nextBucketIndex < 0) {
			nextBucketIndex = 0;
		}
		if (nextBucketIndex > this._currentFrame._buckets.count() - 1) {
			nextBucketIndex = this._currentFrame._buckets.count() - 1;
		}
		var prevBucketValue = this._currentFrame._buckets.__inner[prevBucketIndex];
		var nextBucketValue = this._currentFrame._buckets.__inner[nextBucketIndex];
		var prevDist = Math.abs(pos.__y - prevBucketValue[0]);
		var nextDist = Math.abs(pos.__y - nextBucketValue[0]);
		var zero = this.getWorldZeroValue(this.categoryView());
		var groupWidth = this.yAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		if (prevDist < nextDist) {
			var top = prevBucketValue[0] - 0.5 * groupWidth;
			var right = prevBucketValue[1];
			var left = zero;
			return new $.ig.Rect(0, Math.min(left, right), top, Math.max(left, right) - Math.min(left, right), groupWidth);
		} else {
			var top1 = nextBucketValue[0] - 0.5 * groupWidth;
			var right1 = nextBucketValue[1];
			var left1 = zero;
			return new $.ig.Rect(0, Math.min(left1, right1), top1, Math.max(left1, right1) - Math.min(left1, right1), groupWidth);
		}
	}
	,
	testHit: function (position, isFinger) {
		if (this.testShapesOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	init: function () {
		$.ig.VerticalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.BarSeries.prototype.$type);
		this.framePreparer(new $.ig.BarFramePreparer(1, this, this.barView(), this, this, this.barView().bucketCalculator()));
	},
	getFramePreparer: function (view) {
		var categoryView = $.ig.util.cast($.ig.CategorySeriesView.prototype.$type, view);
		if (categoryView != null && categoryView.isThumbnailView()) {
			if (categoryView.isAlternateView()) {
				return new $.ig.BarFramePreparer(1, this, $.ig.util.cast($.ig.ISupportsMarkers.prototype.$type, categoryView), categoryView, this, categoryView.bucketCalculator());
			} else {
				return new $.ig.BarFramePreparer(1, this, $.ig.util.cast($.ig.ISupportsMarkers.prototype.$type, categoryView), this.seriesViewer().view().overviewPlusDetailViewportHost(), this, categoryView.bucketCalculator());
			}
		} else {
			return this.framePreparer();
		}
	}
	,
	onApplyTemplate: function () {
		$.ig.VerticalAnchoredCategorySeries.prototype.onApplyTemplate.call(this);
	}
	,
	radiusX: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BarSeries.prototype.radiusXProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BarSeries.prototype.radiusXProperty);
		}
	}
	,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.BarSeries.prototype.radiusYProperty, value);
			return value;
		} else {
			return this.getValue($.ig.BarSeries.prototype.radiusYProperty);
		}
	}
	,
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode2;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.VerticalAnchoredCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var barView = $.ig.util.cast($.ig.BarSeriesView.prototype.$type, view);
		if (wipeClean && barView != null && barView.columns() != null) {
			barView.columns().count(0);
		}
	}
	,
	getMode2Index: function () {
		var result = 0;
		var en = this.seriesViewer().series().getEnumerator();
		while (en.moveNext()) {
			var currentSeries = en.current();
			if (currentSeries == this) {
				return result;
			}
			var currentCategorySeries = $.ig.util.cast($.ig.IBarSeries.prototype.$type, currentSeries);
			if (currentCategorySeries != null && currentCategorySeries.yAxis() == this.yAxis() && currentCategorySeries.getPreferredCategoryMode() == $.ig.CategoryMode.prototype.mode2) {
				result++;
			}
		}
		$.ig.Debug.prototype.assert1(false, "CategorySeries.GetMode2Index failed to find series");
		return -1;
	}
	,
	getWorldZeroValue: function (view) {
		var value = 0;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		if (!windowRect.isEmpty() && !viewportRect.isEmpty() && this.xAxis() != null) {
			var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
			value = this.xAxis().getScaledValue(this.xAxis().referenceValue(), xParams);
		}
		return value;
	}
	,
	getRange: function (axis) {
		if (this.valueColumn() == null || this.valueColumn().count() == 0) {
			return null;
		}
		if (axis == this.yAxis()) {
			return new $.ig.AxisRange(0, this.valueColumn().count() - 1);
		}
		if (axis == this.xAxis()) {
			return new $.ig.AxisRange(this.valueColumn().minimum(), this.valueColumn().maximum());
		}
		return null;
	}
	,
	scrollIntoView: function (item) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var unitRect = new $.ig.Rect(0, 0, 0, 1, 1);
		var index = !windowRect.isEmpty() && !viewportRect.isEmpty() && this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		var effectiveViewportRect = this.getEffectiveViewportForUnitViewport(this.view());
		var xParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.xAxis().isInverted(), effectiveViewportRect);
		var yParams = new $.ig.ScalerParams(0, unitRect, unitRect, this.yAxis().isInverted(), effectiveViewportRect);
		var cy = this.yAxis() != null ? this.yAxis().getScaledValue(index, yParams) : NaN;
		var offset = this.yAxis() != null ? this.framePreparer().getOffset(this.yAxis(), unitRect, unitRect, effectiveViewportRect) : 0;
		cy += offset;
		var cx = this.xAxis() != null && this.valueColumn() != null && index < this.valueColumn().count() ? this.xAxis().getScaledValue(this.valueColumn().item(index), xParams) : NaN;
		if (!$.ig.util.isNaN(cx)) {
			if (cx < windowRect.left() + 0.1 * windowRect.width()) {
				cx = cx + 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (cx > windowRect.right() - 0.1 * windowRect.width()) {
				cx = cx - 0.4 * windowRect.width();
				windowRect.x(cx - 0.5 * windowRect.width());
			}
		}
		if (!$.ig.util.isNaN(cy)) {
			if (cy < windowRect.top() + 0.1 * windowRect.height()) {
				cy = cy + 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
			if (cy > windowRect.bottom() - 0.1 * windowRect.height()) {
				cy = cy - 0.4 * windowRect.height();
				windowRect.y(cy - 0.5 * windowRect.height());
			}
		}
		if (this.syncLink() != null) {
			this.syncLink().windowNotify(this.seriesViewer(), windowRect);
		}
		return index >= 0;
	}
	,
	getItemSpan: function () {
		return this.yAxis().getGroupSize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	renderFrame: function (frame, view) {
		$.ig.VerticalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
		var buckets = frame._buckets;
		if (!view.hasSurface()) {
			return;
		}
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
		var yAxis = this.yAxis();
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.yAxis().isInverted(), effectiveViewportRect);
		var xscale = this.xAxis();
		var zero = xscale.getScaledValue(xscale.referenceValue(), xParams);
		var groupWidth = this.yAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		var barView = $.ig.util.cast($.ig.BarSeriesView.prototype.$type, view);
		if ($.ig.util.isNaN(groupWidth) || Number.isInfinity(groupWidth) || $.ig.util.isNaN(zero)) {
			barView.columns().count(0);
			return;
		}
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.yAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		this._renderManager._initialRenderRadiusX = this.radiusX();
		this._renderManager._initialRenderRadiusY = this.radiusY();
		this._renderManager._actualRenderRadiusX = this.radiusX();
		this._renderManager._actualRenderRadiusY = this.radiusY();
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		var isSorting = this.xAxis().isSorting();
		var valueCount = this.valueColumn().count();
		for (var i = 0; i < buckets.count(); ++i) {
			var top = buckets.__inner[i][0] - 0.5 * groupWidth;
			var right = buckets.__inner[i][1];
			var left = zero;
			left = Math.max(left, -100);
			right = Math.min(right, viewportRect.right() + 100);
			var column = barView.columns().item(i);
			column.height(groupWidth);
			column.width(Math.abs(right - left));
			if (areStylesOverriden) {
				this.performCategoryStyleOverride(buckets, i, valueCount, yAxis, yParams, view.isThumbnailView());
			}
			this._renderManager.setCategoryShapeAppearance(column, false, false, false, false);
			column.radiusX(this._renderManager._actualRenderRadiusX);
			column.radiusY(this._renderManager._actualRenderRadiusY);
			barView.setColumnPosition(column, Math.min(right, left), top);
		}
		barView.columns().count(buckets.count());
		view.updateFrameVersion(frame);
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.reset:
			case $.ig.FastItemsSourceEventAction.prototype.insert:
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				this.anchoredView().bucketCalculator().calculateBuckets(this.resolution());
				break;
		}
		this.anchoredView().trendLineManager().dataUpdated(action, position, count, propertyName);
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.reset:
				if (this.xAxis() != null && !this.xAxis().updateRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.insert:
				if (this.xAxis() != null && !this.xAxis().updateRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				if (this.xAxis() != null && !this.xAxis().updateRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.replace:
				if (this.valueMemberPath() != null && this.anchoredView().bucketCalculator()._bucketSize > 0) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.change:
				if (propertyName == this.valueMemberPath()) {
					if (this.xAxis() != null && !this.xAxis().updateRange()) {
						this.renderSeries(true);
					}
				}
				break;
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.VerticalAnchoredCategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.VerticalAnchoredCategorySeries.prototype.xAxisPropertyName:
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				this.anchoredView().bucketCalculator().calculateBuckets(this.resolution());
				if (this.xAxis() != null && this.xAxis().updateRange()) {
					this.renderSeries(false);
				}
				break;
			case $.ig.VerticalAnchoredCategorySeries.prototype.yAxisPropertyName:
				if (oldValue != null) {
					(oldValue).deregisterSeries(this);
				}
				if (newValue != null) {
					(newValue).registerSeries(this);
				}
				this.anchoredView().trendLineManager($.ig.CategoryTrendLineManagerBase.prototype.selectManager(this.anchoredView().trendLineManager(), this.yAxis(), this.rootCanvas(), this));
				this.anchoredView().bucketCalculator().calculateBuckets(this.resolution());
				this.renderSeries(false);
				break;
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				if (this.xAxis() != null && !this.xAxis().updateRange()) {
					this.anchoredView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.AnchoredCategorySeries.prototype.valueColumnPropertyName:
				if (this.xAxis() != null && !this.xAxis().updateRange()) {
					this.anchoredView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.Series.prototype.seriesViewerPropertyName:
				if (oldValue != null && newValue == null) {
					this.deregisterForAxis(this.xAxis());
					this.deregisterForAxis(this.yAxis());
				}
				if (oldValue == null && newValue != null) {
					this.registerForAxis(this.xAxis());
					this.registerForAxis(this.yAxis());
				}
				this.anchoredView().bucketCalculator().calculateBuckets(this.resolution());
				this.renderSeries(false);
				break;
		}
	}
	,
	getPreferredCategoryMode: function () {
		return this.preferredCategoryMode(this.yAxis());
	}
	,
	currentCategoryMode: function () {
		return this.preferredCategoryMode(this.yAxis());
	}
	,
	scaler: function () {
		return this.yAxis();
	}
	,
	yScaler: function () {
		return this.xAxis();
	}
	,
	$type: new $.ig.Type('BarSeries', $.ig.VerticalAnchoredCategorySeries.prototype.$type, [$.ig.IIsCategoryBased.prototype.$type, $.ig.IBarSeries.prototype.$type])
}, true);

$.ig.util.defType('BarBucketCalculator', 'CategoryBucketCalculator', {
	init: function (view) {
		$.ig.CategoryBucketCalculator.prototype.init.call(this, view);
		this.barView(view);
	},
	_barView: null,
	barView: function (value) {
		if (arguments.length === 1) {
			this._barView = value;
			return value;
		} else {
			return this._barView;
		}
	}
	,
	calculateBuckets: function (resolution) {
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var fastItemsSource = this.view().categoryModel().fastItemsSource();
		if (windowRect.isEmpty() || viewportRect.isEmpty() || this.barView().barModel().yAxis() == null || fastItemsSource == null || fastItemsSource.count() == 0) {
			this._bucketSize = 0;
			return;
		}
		var barSeries = $.ig.util.cast($.ig.BarSeries.prototype.$type, this.view().categoryModel());
		var contentViewport = barSeries.getContentViewport(this.view());
		var effectiveViewportRect = barSeries.getEffectiveViewport1(this.view());
		var p = new $.ig.ScalerParams(0, windowRect, viewportRect, barSeries.yAxis().isInverted(), effectiveViewportRect);
		var y0 = Math.floor(barSeries.yAxis().getUnscaledValue1(contentViewport.top(), p, $.ig.CategoryMode.prototype.mode0));
		var y1 = Math.ceil(barSeries.yAxis().getUnscaledValue1(contentViewport.bottom(), p, $.ig.CategoryMode.prototype.mode0));
		if (!barSeries.yAxis().isInverted()) {
			y1 = Math.ceil(barSeries.yAxis().getUnscaledValue1(contentViewport.top(), p, $.ig.CategoryMode.prototype.mode0));
			y0 = Math.floor(barSeries.yAxis().getUnscaledValue1(contentViewport.bottom(), p, $.ig.CategoryMode.prototype.mode0));
		}
		var c = Math.floor((y1 - y0 + 1) * resolution / contentViewport.height());
		this._bucketSize = $.ig.truncate(Math.max(1, c));
		this._firstBucket = $.ig.truncate(Math.max(0, Math.floor(y0 / this._bucketSize) - 1));
		this._lastBucket = $.ig.truncate(Math.ceil(y1 / this._bucketSize));
	}
	,
	getBucket: function (bucket) {
		var column = this.__values;
		var count = this.__count;
		var i0 = Math.min(bucket * this._bucketSize, count - 1);
		var i1 = Math.min(i0 + this._bucketSize - 1, count - 1);
		var min = NaN;
		var max = NaN;
		for (var i = i0; i <= i1; ++i) {
			var y = column[i];
			if (!$.ig.util.isNaN(min)) {
				if (!$.ig.util.isNaN(y)) {
					min = Math.min(min, y);
					max = Math.max(max, y);
				}
			} else {
				min = y;
				max = y;
			}
		}
		if (!$.ig.util.isNaN(min)) {
			return [ (0.5 * (i0 + i1)), min, max ];
		}
		return [ (0.5 * (i0 + i1)), NaN, NaN ];
	}
	,
	__values: null,
	__count: 0,
	cacheValues: function () {
		this.__count = this.barView().barModel().valueColumn().count();
		this.__values = this.barView().barModel().valueColumn().asArray();
	}
	,
	unCacheValues: function () {
		this.__values = null;
	}
	,
	$type: new $.ig.Type('BarBucketCalculator', $.ig.CategoryBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('PointSeries', 'HorizontalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.PointSeriesView(this);
	}
	,
	_pointView: null,
	pointView: function (value) {
		if (arguments.length === 1) {
			this._pointView = value;
			return value;
		} else {
			return this._pointView;
		}
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.pointView(view);
	}
	,
	init: function () {
		$.ig.HorizontalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.PointSeries.prototype.$type);
	},
	renderFrame: function (frame, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
	}
	,
	testHit: function (position, isFinger) {
		var snapPosition = this.getSeriesValuePosition(this.toWorldPosition(position), true, false);
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
	}
	,
	$type: new $.ig.Type('PointSeries', $.ig.HorizontalAnchoredCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('AreaSeries', 'HorizontalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.AreaSeriesView(this);
	}
	,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.fromZero;
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.areaView(view);
	}
	,
	_areaView: null,
	areaView: function (value) {
		if (arguments.length === 1) {
			this._areaView = value;
			return value;
		} else {
			return this._areaView;
		}
	}
	,
	init: function () {
		$.ig.HorizontalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.AreaSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode0;
	}
	,
	testHit: function (position, isFinger) {
		var snapPosition = this.getSeriesValuePosition(this.toWorldPosition(position), true, false);
		if (this.testAreaOver(position, snapPosition, isFinger)) {
			return true;
		}
		if (this.testHighLowStrokeOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var areaView = view;
		areaView.clearAreaLines();
	}
	,
	renderFrame: function (frame, view) {
		var $self = this;
		$.ig.HorizontalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
		var bucketSize = view.bucketCalculator()._bucketSize;
		var areaView = $.ig.util.cast($.ig.AreaSeriesView.prototype.$type, view);
		var buckets = frame._buckets;
		var effectiveViewportRect = this.getEffectiveViewport1(areaView);
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.cachedXAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, this.valueColumn().count(), this.cachedXAxis(), xParams, view.isThumbnailView());
		}
		var line0 = areaView.line0();
		var line1 = areaView.line1();
		var polygon0 = areaView.polygon0();
		var polygon1 = areaView.polygon1();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(polygon0, false, true, false, false);
		this._renderManager.setCategoryShapeAppearance(polygon1, false, true, false, false);
		if (view.checkFrameDirty(frame)) {
			areaView.rasterizeArea1(buckets.count(), buckets, true, bucketSize, this.resolution(), function (p0, l0, p01, l1, f) { $self.terminatePolygon(p0, frame._buckets.count(), view); }, this.unknownValuePlotting(), buckets.count() == 1 ? null : this.getLineClipper1(buckets, buckets.count() - 1, view.viewport(), view.windowRect(), true));
			view.updateFrameVersion(frame);
		}
		var yAxis = this.getYAxis();
		polygon0.__opacity = this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
		polygon1.__opacity = 0.5 * this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
	}
	,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.AreaSeries.prototype.unknownValuePlottingProperty, $.ig.UnknownValuePlotting.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.AreaSeries.prototype.unknownValuePlottingProperty));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.AreaSeries.prototype._unknownValuePlottingPropertyName:
				this.framePreparer().skipUnknowns(this.unknownValuePlotting() == $.ig.UnknownValuePlotting.prototype.linearInterpolate);
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	$type: new $.ig.Type('AreaSeries', $.ig.HorizontalAnchoredCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('ColumnSeries', 'HorizontalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.ColumnSeriesView(this);
	}
	,
	_columnView: null,
	columnView: function (value) {
		if (arguments.length === 1) {
			this._columnView = value;
			return value;
		} else {
			return this._columnView;
		}
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.columnView(view);
	}
	,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.fromZero;
	}
	,
	init: function () {
		this.__consolidatedColumnVerticalPosition = $.ig.ConsolidatedItemsPosition.prototype.minimum;
		$.ig.HorizontalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.ColumnSeries.prototype.$type);
	},
	radiusX: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ColumnSeries.prototype.radiusXProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ColumnSeries.prototype.radiusXProperty);
		}
	}
	,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.ColumnSeries.prototype.radiusYProperty, value);
			return value;
		} else {
			return this.getValue($.ig.ColumnSeries.prototype.radiusYProperty);
		}
	}
	,
	hasIndividualElements: function () {
		return true;
	}
	,
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode2;
	}
	,
	getSeriesValueBoundingBox: function (world) {
		var pos = this.fromWorldPosition(world);
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.cachedYAxis().isInverted(), effectiveViewportRect);
		var matching = this.getMatchingBuckets(this.getCategoryAxis(), this._currentFrame._buckets, this.getBucketSize(this.view()), this.getFirstBucket(this.view()), world, true);
		if (matching == null) {
			return $.ig.Rect.prototype.empty();
		}
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var prevDist = Math.abs(pos.__x - prevBucketValue[0]);
		var nextDist = Math.abs(pos.__x - nextBucketValue[0]);
		var zero = this.yAxis().getScaledValue(this.yAxis().referenceValue(), yParams);
		var groupWidth = this.cachedXAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		if (prevDist < nextDist) {
			var left = prevBucketValue[0] - 0.5 * groupWidth;
			var top = this.getBucketValue(prevBucketValue);
			var bottom = zero;
			return new $.ig.Rect(0, left, Math.min(top, bottom), groupWidth, Math.max(top, bottom) - Math.min(top, bottom));
		} else {
			var left1 = nextBucketValue[0] - 0.5 * groupWidth;
			var top1 = this.getBucketValue(nextBucketValue);
			var bottom1 = zero;
			return new $.ig.Rect(0, left1, Math.min(top1, bottom1), groupWidth, Math.max(top1, bottom1) - Math.min(top1, bottom1));
		}
	}
	,
	testHit: function (position, isFinger) {
		if (this.testShapesOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var columnView = view;
		if (wipeClean && columnView.columns() != null) {
			columnView.columns().count(0);
		}
	}
	,
	getItemSpan: function () {
		return this.cachedXAxis().getGroupSize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	renderFrame: function (frame, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
		var buckets = frame._buckets;
		if (!view.ready()) {
			return;
		}
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.cachedYAxis().isInverted(), effectiveViewportRect);
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.cachedXAxis().isInverted(), effectiveViewportRect);
		var yscale = this.cachedYAxis();
		var zero = yscale.getScaledValue(yscale.referenceValue(), yParams);
		var groupWidth = this.cachedXAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		var xAxis = this.cachedXAxis();
		var columnView = view;
		if ($.ig.util.isNaN(groupWidth) || Number.isInfinity(groupWidth)) {
			columnView.columns().count(0);
			return;
		}
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		this._renderManager._initialRenderRadiusX = this.radiusX();
		this._renderManager._initialRenderRadiusY = this.radiusY();
		this._renderManager._actualRenderRadiusX = this.radiusX();
		this._renderManager._actualRenderRadiusY = this.radiusY();
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		var isSorting = this.cachedXAxis().isSorting();
		var valueCount = this.valueColumn().count();
		var bucketSize = this.getBucketSize(view);
		var bucketCount = 0;
		for (var i = 0; i < buckets.count(); ++i) {
			var left = buckets.__inner[i][0] - 0.5 * groupWidth;
			var top = this.getBucketValue(buckets.__inner[i]);
			var bottom = zero;
			top = Math.max(top, -100);
			bottom = Math.min(bottom, viewportRect.bottom() + 100);
			var height = Math.abs(bottom - top);
			if (Number.isInfinity(height)) {
				continue;
			}
			var column = columnView.columns().item(bucketCount);
			bucketCount++;
			column.width(groupWidth);
			column.height(height);
			if (areStylesOverriden) {
				this.performCategoryStyleOverride(buckets, i, valueCount, xAxis, xParams, view.isThumbnailView());
			}
			if (column.dataContext() != null) {
				var dc = column.dataContext();
				if (bucketSize == 1) {
					var columnIndex = this._renderManager.getBucketBounds(valueCount, i)[0];
					if (columnIndex >= 0 && columnIndex < this.fastItemsSource().count()) {
						dc.item(this.fastItemsSource().item(columnIndex));
					}
				} else {
					dc.item(null);
				}
			}
			this._renderManager.setCategoryShapeAppearance(column, false, false, false, false);
			column.radiusX(this._renderManager._actualRenderRadiusX);
			column.radiusY(this._renderManager._actualRenderRadiusY);
			columnView.positionRectangle(column, left, Math.min(bottom, top));
		}
		columnView.columns().count(bucketCount);
		view.updateFrameVersion(frame);
	}
	,
	__consolidatedColumnVerticalPosition: 0,
	consolidatedColumnVerticalPosition: function (value) {
		if (arguments.length === 1) {
			if (this.consolidatedColumnVerticalPosition() != value) {
				var oldValue = this.consolidatedColumnVerticalPosition();
				this.__consolidatedColumnVerticalPosition = value;
				this.raisePropertyChanged($.ig.ColumnSeries.prototype._consolidatedColumnVerticalPositionPropertyName, $.ig.ConsolidatedItemsPosition.prototype.getBox(oldValue), $.ig.ConsolidatedItemsPosition.prototype.getBox(this.consolidatedColumnVerticalPosition()));
			}
			return value;
		} else {
			return this.__consolidatedColumnVerticalPosition;
		}
	}
	,
	getBucketValue: function (bucket) {
		if (bucket == null || bucket.length < 3) {
			return NaN;
		}
		switch (this.consolidatedColumnVerticalPosition()) {
			case $.ig.ConsolidatedItemsPosition.prototype.minimum: return bucket[1];
			case $.ig.ConsolidatedItemsPosition.prototype.maximum: return bucket[2];
			case $.ig.ConsolidatedItemsPosition.prototype.median: return (bucket[1] + bucket[2]) / 2;
			case $.ig.ConsolidatedItemsPosition.prototype.relativeMinimum:
				var median = (bucket[1] + bucket[2]) / 2;
				if (this.yAxis() == null) {
					return median;
				}
				var dataMedian = this.yAxis().getUnscaledValue(median, new $.ig.ScalerParams(1, this.seriesViewer().windowRect(), this.seriesViewer().viewportRect(), this.yAxis().isInverted()));
				if (dataMedian < this.yAxis().referenceValue()) {
					return bucket[2];
				} else {
					return bucket[1];
				}
			case $.ig.ConsolidatedItemsPosition.prototype.relativeMaximum:
				var alsoTheMedian = (bucket[1] + bucket[2]) / 2;
				var alsoDataMedian = this.yAxis().getUnscaledValue(alsoTheMedian, new $.ig.ScalerParams(1, this.seriesViewer().windowRect(), this.seriesViewer().viewportRect(), this.yAxis().isInverted()));
				if (this.yAxis() == null) {
					return alsoTheMedian;
				}
				if (alsoDataMedian < this.yAxis().referenceValue()) {
					return bucket[1];
				} else {
					return bucket[2];
				}
			default:
				$.ig.Debug.prototype.assert1(false, "unexpected ConsolidatedItemsPosition.");
				return (bucket[1] + bucket[2]) / 2;
		}
	}
	,
	$type: new $.ig.Type('ColumnSeries', $.ig.HorizontalAnchoredCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('LineSeries', 'HorizontalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.LineSeriesView(this);
	}
	,
	_lineView: null,
	lineView: function (value) {
		if (arguments.length === 1) {
			this._lineView = value;
			return value;
		} else {
			return this._lineView;
		}
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.lineView(view);
	}
	,
	init: function () {
		$.ig.HorizontalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.LineSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var lineView = view;
		lineView.clearLine();
	}
	,
	renderFrame: function (frame, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
		var bucketSize = view.bucketCalculator()._bucketSize;
		var lineView = $.ig.util.cast($.ig.LineSeriesView.prototype.$type, view);
		var buckets = frame._buckets;
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.cachedXAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, this.valueColumn().count(), this.cachedXAxis(), xParams, view.isThumbnailView());
		}
		var line0 = lineView.line0();
		var line1 = lineView.line1();
		var fillArea = lineView.fillArea();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(fillArea, false, true, false, false);
		fillArea.__opacity = 0.75 * this._renderManager._actualRenderOpacity;
		if (view.checkFrameDirty(frame)) {
			lineView.rasterizeLine(buckets.count(), buckets, true, this.unknownValuePlotting(), this.getLineClipper(buckets, buckets.count() - 1, view.viewport(), view.windowRect()), bucketSize, this.resolution());
			view.updateFrameVersion(frame);
		}
	}
	,
	unknownValuePlotting: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.LineSeries.prototype.unknownValuePlottingProperty, $.ig.UnknownValuePlotting.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.LineSeries.prototype.unknownValuePlottingProperty));
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.LineSeries.prototype._unknownValuePlottingPropertyName:
				this.framePreparer().skipUnknowns(this.unknownValuePlotting() == $.ig.UnknownValuePlotting.prototype.linearInterpolate);
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	$type: new $.ig.Type('LineSeries', $.ig.HorizontalAnchoredCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('SplineAreaSeries', 'SplineSeriesBase', {
	createView: function () {
		return new $.ig.SplineAreaSeriesView(this);
	}
	,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.fromZero;
	}
	,
	onViewCreated: function (view) {
		$.ig.SplineSeriesBase.prototype.onViewCreated.call(this, view);
		this.splineAreaView(view);
	}
	,
	_splineAreaView: null,
	splineAreaView: function (value) {
		if (arguments.length === 1) {
			this._splineAreaView = value;
			return value;
		} else {
			return this._splineAreaView;
		}
	}
	,
	init: function () {
		$.ig.SplineSeriesBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.SplineAreaSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.SplineSeriesBase.prototype.clearRendering.call(this, wipeClean, view);
		var splineAreaView = view;
		splineAreaView.clearSplineArea();
	}
	,
	testHit: function (position, isFinger) {
		var snapPosition = this.getSeriesValuePosition(this.toWorldPosition(position), true, false);
		if (this.testAreaOver(position, snapPosition, isFinger)) {
			return true;
		}
		if (this.testHighLowStrokeOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	renderFrame: function (frame, view) {
		var $self = this;
		$.ig.SplineSeriesBase.prototype.renderFrame.call(this, frame, view);
		var bucketSize = this.categoryView().bucketCalculator()._bucketSize;
		var splineAreaView = $.ig.util.cast($.ig.SplineAreaSeriesView.prototype.$type, view);
		var buckets = frame._buckets;
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.cachedXAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, this.valueColumn().count(), this.cachedXAxis(), xParams, view.isThumbnailView());
		}
		var line0 = splineAreaView.line0();
		var line1 = splineAreaView.line1();
		var polygon0 = splineAreaView.polygon0();
		var polygon1 = splineAreaView.polygon1();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(polygon0, false, true, false, false);
		this._renderManager.setCategoryShapeAppearance(polygon1, false, true, false, false);
		polygon0.__opacity = this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
		polygon1.__opacity = 0.5 * this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
		if (view.checkFrameDirty(frame)) {
			splineAreaView.rasterizeSplineArea1(frame._buckets.count(), buckets, true, bucketSize, this.resolution(), function (p0, l0, p1, l1, f) { $self.terminatePolygon(p0, frame._buckets.count(), view); }, $.ig.UnknownValuePlotting.prototype.dontPlot, frame._buckets.count() == 1 ? null : this.getLineClipper1(buckets, buckets.count() - 1, view.viewport(), view.windowRect(), true));
			view.updateFrameVersion(frame);
		}
		var yAxis = this.getYAxis();
	}
	,
	$type: new $.ig.Type('SplineAreaSeries', $.ig.SplineSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('SplineSeries', 'SplineSeriesBase', {
	createView: function () {
		return new $.ig.SplineSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.SplineSeriesBase.prototype.onViewCreated.call(this, view);
		this.splineView(view);
	}
	,
	_splineView: null,
	splineView: function (value) {
		if (arguments.length === 1) {
			this._splineView = value;
			return value;
		} else {
			return this._splineView;
		}
	}
	,
	init: function () {
		$.ig.SplineSeriesBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.SplineSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.SplineSeriesBase.prototype.clearRendering.call(this, wipeClean, view);
		var splineView = view;
		splineView.clearSplineLines();
	}
	,
	renderFrame: function (frame, view) {
		$.ig.SplineSeriesBase.prototype.renderFrame.call(this, frame, view);
		var categoryView = view;
		var bucketSize = categoryView.bucketCalculator()._bucketSize;
		var splineView = $.ig.util.cast($.ig.SplineSeriesView.prototype.$type, view);
		var buckets = frame._buckets;
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.cachedXAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, this.valueColumn().count(), this.cachedXAxis(), xParams, view.isThumbnailView());
		}
		var line0 = splineView.line0();
		var line1 = splineView.line1();
		var fillArea = splineView.fillArea();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(fillArea, false, true, false, false);
		fillArea.__opacity = 0.75 * this._renderManager._actualRenderOpacity;
		if (view.checkFrameDirty(frame)) {
			splineView.rasterizeSpline(buckets.count(), buckets, true, $.ig.UnknownValuePlotting.prototype.dontPlot, this.getLineClipper(buckets, buckets.count() - 1, view.viewport(), view.windowRect()), bucketSize, this.resolution());
			view.updateFrameVersion(frame);
		}
	}
	,
	$type: new $.ig.Type('SplineSeries', $.ig.SplineSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('StepAreaSeries', 'HorizontalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.StepAreaSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.stepAreaView(view);
	}
	,
	_stepAreaView: null,
	stepAreaView: function (value) {
		if (arguments.length === 1) {
			this._stepAreaView = value;
			return value;
		} else {
			return this._stepAreaView;
		}
	}
	,
	init: function () {
		$.ig.HorizontalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.StepAreaSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode1;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var stepAreaView = view;
		stepAreaView.clearStepArea();
	}
	,
	getXValue: function (i, fromEnd, frame, width, isSorting) {
		if (fromEnd) {
			if (isSorting) {
				if ((i & 1) == 0 || (($.ig.intDivide(i, 2)) + 1) >= frame._buckets.count()) {
					return frame._buckets.__inner[($.ig.intDivide(i, 2))][0];
				}
				return frame._buckets.__inner[($.ig.intDivide(i, 2)) + 1][0];
			}
			return frame._buckets.__inner[($.ig.intDivide(i, 2))][0] + ((i & 1) == 1 ? -width : width);
		}
		if (isSorting) {
			if ((i & 1) == 0 || (($.ig.intDivide(i, 2)) + 1) >= frame._buckets.count()) {
				return frame._buckets.__inner[($.ig.intDivide(i, 2))][0];
			}
			return frame._buckets.__inner[($.ig.intDivide(i, 2)) + 1][0];
		}
		return frame._buckets.__inner[($.ig.intDivide(i, 2))][0] + ((i & 1) == 0 ? -width : width);
	}
	,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.fromZero;
	}
	,
	getInterpolatedSeriesValue: function (p, column, prevItem, nextItem, unsortedPrevItem, unsortedNextItem, offset, isSorting) {
		var prevValue = NaN;
		if (prevItem >= 0 && prevItem < column.count()) {
			prevValue = column.item(prevItem);
		}
		var nextValue = NaN;
		if (nextItem >= 0 && nextItem < column.count()) {
			nextValue = column.item(nextItem);
		}
		if (unsortedNextItem == 0) {
			return nextValue;
		}
		if (unsortedPrevItem == column.count() - 1) {
			return prevValue;
		}
		if ($.ig.util.isNaN(nextValue) && offset != 0 && p <= 0.5 && !isSorting) {
			return prevValue;
		}
		if ($.ig.util.isNaN(prevValue) && offset != 0 && p >= 0.5 && !isSorting) {
			return nextValue;
		}
		if (p > 0.5) {
			return nextValue;
		}
		return prevValue;
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.colorEncoded;
		} else {
			return $.ig.HorizontalAnchoredCategorySeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	renderFrame: function (frame, view) {
		var $self = this;
		$.ig.HorizontalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var isSorting = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.cachedXAxis()) != null;
		var width = this.cachedXAxis() != null ? 0.5 * this.cachedXAxis().getCategorySize(windowRect, viewportRect, effectiveViewportRect) : 0;
		if (this.cachedXAxis() != null && this.cachedXAxis().isInverted()) {
			width = -width;
		}
		var count = frame._buckets.count() * 2;
		var buckets = new $.ig.List$1(Array, 2, count);
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		for (var i = 0; i < count; i++) {
			var bucket = new Array(4);
			bucket[0] = this.getXValue(i, false, frame, width, isSorting);
			bucket[1] = frame._buckets.__inner[($.ig.intDivide(i, 2))][1];
			bucket[2] = this.getXValue(i, true, frame, width, isSorting);
			bucket[3] = frame._buckets.__inner[($.ig.intDivide(i, 2))][2];
			buckets.add(bucket);
		}
		var bucketSize = this.categoryView().bucketCalculator()._bucketSize;
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.cachedXAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, this.valueColumn().count(), this.cachedXAxis(), xParams, view.isThumbnailView());
		}
		var stepAreaView = $.ig.util.cast($.ig.StepAreaSeriesView.prototype.$type, view);
		var line0 = stepAreaView.polyline0();
		var line1 = stepAreaView.polyline1();
		var polygon0 = stepAreaView.polygon0();
		var polygon1 = stepAreaView.polygon1();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(polygon0, false, true, false, false);
		this._renderManager.setCategoryShapeAppearance(polygon1, false, true, false, false);
		if (view.checkFrameDirty(frame)) {
			stepAreaView.rasterizeStepArea1(count, buckets, false, bucketSize, this.resolution(), function (p0, l0, p01, l1, f) { $self.terminatePolygon(p0, 2 * frame._buckets.count(), view); }, $.ig.UnknownValuePlotting.prototype.dontPlot, this.getLineClipper1(buckets, buckets.count() - 1, view.viewport(), view.windowRect(), true));
			view.updateFrameVersion(frame);
		}
		var yAxis = this.getYAxis();
		stepAreaView.polygon0().__opacity = this.actualAreaFillOpacity();
		stepAreaView.polygon1().__opacity = 0.5 * this.actualAreaFillOpacity();
	}
	,
	currentCategoryMode: function () {
		if (this.cachedXAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.cachedXAxis()) !== null) {
			this.cachedXAxis().categoryMode($.ig.CategoryMode.prototype.mode0);
			return $.ig.CategoryMode.prototype.mode0;
		}
		return $.ig.CategoryMode.prototype.mode1;
	}
	,
	$type: new $.ig.Type('StepAreaSeries', $.ig.HorizontalAnchoredCategorySeries.prototype.$type, [$.ig.IIsCategoryBased.prototype.$type])
}, true);

$.ig.util.defType('StepLineSeries', 'HorizontalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.StepLineSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.stepView(view);
	}
	,
	_stepView: null,
	stepView: function (value) {
		if (arguments.length === 1) {
			this._stepView = value;
			return value;
		} else {
			return this._stepView;
		}
	}
	,
	init: function () {
		$.ig.HorizontalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.StepLineSeries.prototype.$type);
	},
	onApplyTemplate: function () {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onApplyTemplate.call(this);
	}
	,
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode1;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var stepView = view;
		stepView.clearStepLine();
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.colorEncoded;
		} else {
			return $.ig.HorizontalAnchoredCategorySeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	getInterpolatedSeriesValue: function (p, column, prevItem, nextItem, unsortedPrevItem, unsortedNextItem, offset, isSorting) {
		var prevValue = NaN;
		if (prevItem >= 0 && prevItem < column.count()) {
			prevValue = column.item(prevItem);
		}
		var nextValue = NaN;
		if (nextItem >= 0 && nextItem < column.count()) {
			nextValue = column.item(nextItem);
		}
		if (unsortedNextItem == 0) {
			return nextValue;
		}
		if (unsortedPrevItem == column.count() - 1) {
			return prevValue;
		}
		if ($.ig.util.isNaN(nextValue) && offset != 0 && p <= 0.5 && !isSorting) {
			return prevValue;
		}
		if ($.ig.util.isNaN(prevValue) && offset != 0 && p >= 0.5 && !isSorting) {
			return nextValue;
		}
		if (p > 0.5) {
			return nextValue;
		}
		return prevValue;
	}
	,
	getXValue: function (i, fromEnd, frame, width, xInverted, isSorting) {
		var even;
		if (fromEnd) {
			if (isSorting) {
				even = (i & 1) == 0;
				if (even || ($.ig.intDivide(i, 2)) + 1 < 0) {
					return frame._buckets.__inner[($.ig.intDivide(i, 2))][0];
				}
				if (frame._buckets.count() == ($.ig.intDivide(i, 2)) + 1) {
					return frame._buckets.__inner[($.ig.intDivide(i, 2))][0];
				}
				return frame._buckets.__inner[($.ig.intDivide(i, 2)) + 1][0];
			}
			even = (i & 1) == 0;
			return (frame._buckets.__inner[($.ig.intDivide(i, 2))][0] + (even ? width : -width));
		}
		if (isSorting) {
			even = (i & 1) == 0;
			if (even || (($.ig.intDivide(i, 2)) + 1) >= frame._buckets.count()) {
				return frame._buckets.__inner[($.ig.intDivide(i, 2))][0];
			}
			if (frame._buckets.count() == ($.ig.intDivide(i, 2)) + 1) {
				return frame._buckets.__inner[($.ig.intDivide(i, 2))][0];
			}
			return (frame._buckets.__inner[($.ig.intDivide(i, 2)) + 1][0]);
		}
		even = (i & 1) == 0;
		return (frame._buckets.__inner[($.ig.intDivide(i, 2))][0] + (even ? -width : width));
	}
	,
	renderFrame: function (frame, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var isSorting = $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.cachedXAxis()) != null;
		var xIsInverted = this.cachedXAxis().isInverted();
		var width = 0;
		if (this.cachedXAxis() != null) {
			width = 0.5 * this.cachedXAxis().getCategorySize(windowRect, viewportRect, effectiveViewportRect);
		}
		var buckets = frame._buckets;
		var count = 2 * buckets.count();
		var procesedBuckets = new $.ig.List$1(Array, 2, count);
		if (!this.cachedXAxis().isInverted()) {
			for (var i = 0; i < count; i++) {
				var values = new Array(4);
				values[0] = this.getXValue(i, false, frame, width, xIsInverted, isSorting);
				values[1] = frame._buckets.__inner[($.ig.intDivide(i, 2))][1];
				values[2] = this.getXValue(i, true, frame, width, xIsInverted, isSorting);
				values[3] = frame._buckets.__inner[($.ig.intDivide(i, 2))][2];
				procesedBuckets.add(values);
			}
		} else {
			for (var i1 = 0; i1 < count; i1++) {
				var values1 = new Array(4);
				values1[0] = this.getXValue(i1, true, frame, width, xIsInverted, isSorting);
				values1[1] = frame._buckets.__inner[($.ig.intDivide(i1, 2))][1];
				values1[2] = this.getXValue(i1, true, frame, width, xIsInverted, isSorting);
				values1[3] = frame._buckets.__inner[($.ig.intDivide(i1, 2))][2];
				procesedBuckets.add(values1);
			}
		}
		var stepLineView = $.ig.util.cast($.ig.StepLineSeriesView.prototype.$type, view);
		var bucketSize = stepLineView.bucketCalculator()._bucketSize;
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.cachedXAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, this.valueColumn().count(), this.cachedXAxis(), xParams, view.isThumbnailView());
		}
		var line0 = stepLineView.line0();
		var line1 = stepLineView.line1();
		var fillArea = stepLineView.fillArea();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, false);
		this._renderManager.setCategoryShapeAppearance(fillArea, false, true, false, false);
		fillArea.__opacity = 0.75 * this._renderManager._actualRenderOpacity;
		if (view.checkFrameDirty(frame)) {
			stepLineView.rasterizeStepLine(count, procesedBuckets, false, $.ig.UnknownValuePlotting.prototype.dontPlot, this.getLineClipper(procesedBuckets, count - 1, view.viewport(), view.windowRect()), bucketSize, this.resolution());
			view.updateFrameVersion(frame);
		}
	}
	,
	currentCategoryMode: function () {
		if (this.cachedXAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.cachedXAxis()) !== null) {
			this.cachedXAxis().categoryMode($.ig.CategoryMode.prototype.mode0);
			return $.ig.CategoryMode.prototype.mode0;
		}
		return $.ig.CategoryMode.prototype.mode1;
	}
	,
	$type: new $.ig.Type('StepLineSeries', $.ig.HorizontalAnchoredCategorySeries.prototype.$type, [$.ig.IIsCategoryBased.prototype.$type])
}, true);

$.ig.util.defType('WaterfallSeries', 'HorizontalAnchoredCategorySeries', {
	createView: function () {
		return new $.ig.WaterfallSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.onViewCreated.call(this, view);
		this.waterfallView(view);
	}
	,
	_waterfallView: null,
	waterfallView: function (value) {
		if (arguments.length === 1) {
			this._waterfallView = value;
			return value;
		} else {
			return this._waterfallView;
		}
	}
	,
	init: function () {
		$.ig.HorizontalAnchoredCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.WaterfallSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode2;
	}
	,
	negativeBrush: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.WaterfallSeries.prototype.negativeBrushProperty, value);
			return value;
		} else {
			return this.getValue($.ig.WaterfallSeries.prototype.negativeBrushProperty);
		}
	}
	,
	radiusX: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.WaterfallSeries.prototype.radiusXProperty, value);
			return value;
		} else {
			return this.getValue($.ig.WaterfallSeries.prototype.radiusXProperty);
		}
	}
	,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.WaterfallSeries.prototype.radiusYProperty, value);
			return value;
		} else {
			return this.getValue($.ig.WaterfallSeries.prototype.radiusYProperty);
		}
	}
	,
	hasIndividualElements: function () {
		return true;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.WaterfallSeries.prototype.radiusYPropertyName:
			case $.ig.WaterfallSeries.prototype.radiusXPropertyName:
			case $.ig.WaterfallSeries.prototype.negativeBrushPropertyName:
				this.renderSeries(false);
				break;
		}
	}
	,
	testHit: function (position, isFinger) {
		if (this.testShapesOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	getSeriesValueBoundingBox: function (world) {
		var $self = this;
		var columns = this.waterfallView().columns();
		var pos = this.fromWorldPosition(world);
		var index = -1;
		var isInverted = this.xAxis().isInverted();
		var maxPosition = this.viewport().right() * 2;
		if (isInverted) {
			index = $.ig.ArrayUtil.prototype.binarySearch$1($.ig.Rectangle.prototype.$type, columns, function (item) {
				var box = $self.waterfallView().getColumnBoundingBoxFromColumn(item);
				return (maxPosition - pos.__x) < (maxPosition - (box.left() + box.width() / 2)) ? -1 : ((maxPosition - pos.__x) > (maxPosition - (box.left() + box.width() / 2)) ? 1 : 0);
			});
		} else {
			index = $.ig.ArrayUtil.prototype.binarySearch$1($.ig.Rectangle.prototype.$type, columns, function (item) {
				var box = $self.waterfallView().getColumnBoundingBoxFromColumn(item);
				return pos.__x < (box.left() + box.width() / 2) ? -1 : (pos.__x > (box.left() + box.width() / 2) ? 1 : 0);
			});
		}
		if (index < 0) {
			index = ~index;
			index--;
		}
		if (index < 0) {
			index = 0;
		}
		if (index > columns.count() - 1) {
			index = columns.count() - 1;
		}
		var prevIndex = index;
		var nextIndex = index;
		if (prevIndex < 0) {
			prevIndex--;
		}
		if (nextIndex < columns.count() - 1) {
			nextIndex++;
		}
		var prevBox = this.getColumnBoundingBox(prevIndex);
		var currBox = this.getColumnBoundingBox(index);
		var nextBox = this.getColumnBoundingBox(nextIndex);
		var prevX = prevBox.left() + prevBox.width() / 2;
		var currX = currBox.left() + currBox.width() / 2;
		var nextX = nextBox.left() + nextBox.width() / 2;
		var distPrev = Math.abs(prevX - pos.__x);
		var distCurr = Math.abs(currX - pos.__x);
		var distNext = Math.abs(nextX - pos.__x);
		var prevContains = prevBox.containsPoint(pos);
		var currContains = currBox.containsPoint(pos);
		var nextContains = nextBox.containsPoint(pos);
		if (distCurr <= distPrev && distCurr <= distNext) {
			if (!isInverted && nextContains) {
				return nextBox;
			}
			if (isInverted && prevContains) {
				return prevBox;
			}
			return currBox;
		}
		if (distPrev <= distCurr && distPrev <= distNext) {
			if (!isInverted && currContains) {
				return currBox;
			}
			return prevBox;
		}
		if (distNext <= distCurr && distNext <= distPrev) {
			if (isInverted && currContains) {
				return currBox;
			}
			return nextBox;
		}
		return $.ig.Rect.prototype.empty();
	}
	,
	getColumnBoundingBox: function (index) {
		return this.waterfallView().getColumnBoundingBox(index);
	}
	,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.fromZero;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalAnchoredCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		this.waterfallView().columns().count(0);
	}
	,
	getItemSpan: function () {
		return this.cachedXAxis().getGroupSize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	renderFrame: function (frame, view) {
		var waterfallView = view;
		$.ig.HorizontalAnchoredCategorySeries.prototype.renderFrame.call(this, frame, view);
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var groupWidth = this.cachedXAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		var radiusX = this.radiusX();
		var radiusY = this.radiusY();
		var rectangleGeometry;
		var rect;
		var left;
		var zeroVal = this.getWorldZeroValue(view);
		var lastKnownValue = NaN;
		var columns = waterfallView.columns();
		var currentRectangle;
		var columnCount = 0;
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.cachedXAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		this._renderManager._initialRenderRadiusX = this.radiusX();
		this._renderManager._initialRenderRadiusY = this.radiusY();
		this._renderManager._actualRenderRadiusX = this.radiusX();
		this._renderManager._actualRenderRadiusY = this.radiusY();
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		var isSorting = this.cachedXAxis().isSorting();
		var valueCount = this.valueColumn().count();
		var buckets = frame._buckets;
		var xAxis = this.cachedXAxis();
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.cachedXAxis().isInverted(), effectiveViewportRect);
		var positiveBrush = this.actualBrush();
		var negativeBrush = this.negativeBrush();
		if (frame._buckets.count() > 0) {
			left = frame._buckets.__inner[0][0] - 0.5 * groupWidth;
			var currentValue = frame._buckets.__inner[0][1];
			if (!$.ig.util.isNaN(currentValue)) {
				if (currentValue > zeroVal) {
					currentRectangle = columns.item(columnCount);
					columnCount++;
					waterfallView.positionRectangle(currentRectangle, left, zeroVal);
					currentRectangle.width(groupWidth);
					currentRectangle.height(Math.abs(zeroVal - currentValue));
					this._renderManager._initialRenderFill = negativeBrush;
					this._renderManager._actualRenderFill = negativeBrush;
					this._renderManager._actualNegativeShape = true;
					if (areStylesOverriden) {
						this.performCategoryStyleOverride(buckets, 0, valueCount, xAxis, xParams, view.isThumbnailView());
					}
					this._renderManager.setCategoryShapeAppearance(currentRectangle, false, false, false, false);
					currentRectangle.radiusX(this._renderManager._actualRenderRadiusX);
					currentRectangle.radiusY(this._renderManager._actualRenderRadiusY);
				} else {
					currentRectangle = columns.item(columnCount);
					columnCount++;
					waterfallView.positionRectangle(currentRectangle, left, currentValue);
					currentRectangle.width(groupWidth);
					currentRectangle.height(Math.abs(currentValue - zeroVal));
					this._renderManager._initialRenderFill = positiveBrush;
					this._renderManager._actualRenderFill = positiveBrush;
					this._renderManager._actualNegativeShape = false;
					if (areStylesOverriden) {
						this.performCategoryStyleOverride(buckets, 0, valueCount, xAxis, xParams, view.isThumbnailView());
					}
					this._renderManager.setCategoryShapeAppearance(currentRectangle, false, false, false, false);
					currentRectangle.radiusX(this._renderManager._actualRenderRadiusX);
					currentRectangle.radiusY(this._renderManager._actualRenderRadiusY);
				}
				lastKnownValue = currentValue;
			} else {
				lastKnownValue = zeroVal;
			}
		}
		for (var i = 1; i < frame._buckets.count(); ++i) {
			var bucket1 = frame._buckets.__inner[i];
			left = frame._buckets.__inner[i][0] - 0.5 * groupWidth;
			var currentValue1 = bucket1[1];
			if (!$.ig.util.isNaN(currentValue1)) {
				currentRectangle = columns.item(columnCount);
				columnCount++;
				waterfallView.positionRectangle(currentRectangle, left, Math.min(lastKnownValue, currentValue1));
				currentRectangle.width(groupWidth);
				currentRectangle.height(Math.abs(lastKnownValue - currentValue1));
				this._renderManager._initialRenderFill = positiveBrush;
				this._renderManager._actualRenderFill = positiveBrush;
				this._renderManager._actualNegativeShape = false;
				if (lastKnownValue > currentValue1) {
					if (areStylesOverriden) {
						this.performCategoryStyleOverride(buckets, i, valueCount, xAxis, xParams, view.isThumbnailView());
					}
				} else {
					this._renderManager._initialRenderFill = negativeBrush;
					this._renderManager._actualRenderFill = negativeBrush;
					this._renderManager._actualNegativeShape = true;
					if (areStylesOverriden) {
						this.performCategoryStyleOverride(buckets, i, valueCount, xAxis, xParams, view.isThumbnailView());
					}
				}
				this._renderManager.setCategoryShapeAppearance(currentRectangle, false, false, false, false);
				currentRectangle.radiusX(this._renderManager._actualRenderRadiusX);
				currentRectangle.radiusY(this._renderManager._actualRenderRadiusY);
				lastKnownValue = currentValue1;
			}
		}
		columns.count(columnCount);
		view.updateFrameVersion(frame);
	}
	,
	$type: new $.ig.Type('WaterfallSeries', $.ig.HorizontalAnchoredCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('Legend', 'LegendBase', {
	createView: function () {
		return new $.ig.LegendView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.LegendBase.prototype.onViewCreated.call(this, view);
		this.legendView(view);
	}
	,
	_legendView: null,
	legendView: function (value) {
		if (arguments.length === 1) {
			this._legendView = value;
			return value;
		} else {
			return this._legendView;
		}
	}
	,
	init: function () {
		var $self = this;
		$.ig.LegendBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.Legend.prototype.$type);
		var $t = this.children();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, function (o, e) {
			if (e.oldItems() != null) {
				var en = e.oldItems().getEnumerator();
				while (en.moveNext()) {
					var item = en.current();
					$self.legendView().removeItemVisual(item);
				}
			}
			if (e.newItems() != null) {
				var en1 = e.newItems().getEnumerator();
				while (en1.moveNext()) {
					var item1 = en1.current();
					$self.legendView().addItemVisual(item1);
				}
			}
		});
	},
	getSeriesIndex: function (series) {
		if ($.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null || $.ig.util.cast($.ig.SplineFragmentBase.prototype.$type, series) !== null) {
			var parentSeries = $.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null ? (series).parentSeries() : (series).parentSeries();
			if (parentSeries.reverseLegendOrder()) {
				return -1 + parentSeries.index() + parentSeries.actualSeries().count() - parentSeries.stackedSeriesManager().seriesVisual().indexOf($.ig.util.cast($.ig.AnchoredCategorySeries.prototype.$type, series));
			}
		}
		return series.index();
	}
	,
	addChildInOrder: function (legendItem, series) {
		if ($.ig.util.cast($.ig.StackedSeriesBase.prototype.$type, series) !== null) {
			return;
		}
		if (!series.isUsableInLegend()) {
			return;
		}
		var index = 0;
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var item = en.current();
			var itemChart;
			var itemSeries;
			var itemItem;
			var $ret = this.view().fetchLegendEnvironment(item, itemChart, itemSeries, itemItem);
			itemChart = $ret.p1;
			itemSeries = $ret.p2;
			itemItem = $ret.p3;
			if (series.seriesViewer() != null && itemChart != null && (this.getSortOrder(series.seriesViewer()) < this.getSortOrder(itemChart) || (this.getSortOrder(series.seriesViewer()) == -1 && this.getSortOrder(itemChart) == -1 && series.seriesViewer().getHashCode() < itemChart.getHashCode()))) {
				break;
			}
			if (series.seriesViewer() != null && itemChart != null && series.seriesViewer() == itemChart && itemSeries != null) {
				var indexOfSeries = this.getSeriesIndex(series);
				var indexOfItemSeries = this.getSeriesIndex(itemSeries);
				var sortOrderSeries = this.getSortOrder(series);
				var sortOrderItemSeries = this.getSortOrder(itemSeries);
				if ($.ig.util.cast($.ig.BarSeries.prototype.$type, itemSeries) !== null) {
					if (sortOrderItemSeries == -1 && sortOrderSeries == -1) {
						index = 0;
						break;
					}
					if (sortOrderSeries < sortOrderItemSeries || sortOrderItemSeries == -1) {
						break;
					}
				}
				if (indexOfSeries <= indexOfItemSeries) {
					break;
				}
			}
			index++;
		}
		this.children().insert(index, legendItem);
		var info = new $.ig.LegendItemInfo();
		info.legendItem(legendItem);
		info.series(series);
		var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, legendItem);
		if (contentControl != null && contentControl.content() != null) {
			var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
			if (context != null) {
				info.dataContext(context);
				info.text(context.itemLabel());
			}
		}
	}
	,
	getSortOrder: function (target) {
		return -1;
	}
	,
	$type: new $.ig.Type('Legend', $.ig.LegendBase.prototype.$type)
}, true);

$.ig.util.defType('AreaSeriesView', 'AnchoredCategorySeriesView', {
	_areaModel: null,
	areaModel: function (value) {
		if (arguments.length === 1) {
			this._areaModel = value;
			return value;
		} else {
			return this._areaModel;
		}
	}
	,
	init: function (model) {
		this._polygon0 = new $.ig.Path();
		this._polyline0 = new $.ig.Path();
		this._polygon1 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		this.__hitPolygon0 = new $.ig.Path();
		this.__hitPolyline0 = new $.ig.Path();
		this.__hitPolygon1 = new $.ig.Path();
		this.__hitPolyline1 = new $.ig.Path();
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.areaModel(model);
	},
	onInit: function () {
		$.ig.AnchoredCategorySeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	line0: function () {
		return this._polyline0;
	}
	,
	line1: function () {
		return this._polyline1;
	}
	,
	polygon0: function () {
		return this._polygon0;
	}
	,
	polygon1: function () {
		return this._polygon1;
	}
	,
	_polygon0: null,
	_polyline0: null,
	_polygon1: null,
	_polyline1: null,
	clearAreaLines: function () {
		this._polygon0.data(null);
		this._polygon1.data(null);
		this._polyline0.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	rasterizeArea: function (count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting) {
		this.rasterizeArea1(count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, null);
	}
	,
	rasterizeArea1: function (count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, clipper) {
		this.anchoredModel().lineRasterizer().isSortingAxis($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.categoryModel().getXAxis()) !== null ? true : false);
		this.anchoredModel().lineRasterizer().rasterizePolygonPaths1(this._polygon0, this._polyline0, this._polygon1, this._polyline1, count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, clipper);
		this.makeDirty();
	}
	,
	__hitPolygon0: null,
	__hitPolyline0: null,
	__hitPolygon1: null,
	__hitPolyline1: null,
	setupHitAppearanceOverride: function () {
		$.ig.AnchoredCategorySeriesView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolygon0.data(this._polygon0.data());
		this.__hitPolyline0.data(this._polyline0.data());
		this.__hitPolygon1.data(this._polygon1.data());
		this.__hitPolyline1.data(this._polyline1.data());
		var hitBrush = this.getHitBrush();
		this.__hitPolygon0.__fill = hitBrush;
		this.__hitPolygon0.__opacity = 1;
		this.__hitPolygon1.__fill = hitBrush;
		this.__hitPolygon1.__opacity = 1;
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (isHitContext) {
			context.renderPath(this.__hitPolygon0);
			context.renderPath(this.__hitPolygon1);
			context.renderPath(this.__hitPolyline0);
			context.renderPath(this.__hitPolyline1);
		} else {
			context.renderPath(this._polygon0);
			context.renderPath(this._polygon1);
			context.renderPath(this._polyline0);
			context.renderPath(this._polyline1);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		upperShape.tags().add("Main");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon1);
		translucent.tags().add("Translucent");
		var fill = new $.ig.PathVisualData(1, "fillShape", this._polygon0);
		fill.tags().add("Fill");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
		svd.shapes().add(fill);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(5);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(-3);
	}
	,
	$type: new $.ig.Type('AreaSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('BarSeriesView', 'AnchoredCategorySeriesView', {
	_barModel: null,
	barModel: function (value) {
		if (arguments.length === 1) {
			this._barModel = value;
			return value;
		} else {
			return this._barModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		this.__hitItem = new $.ig.Rectangle();
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.barModel(model);
		this.columns((function () {
			var $ret = new $.ig.Pool$1($.ig.Rectangle.prototype.$type);
			$ret.create($self.columnCreate.runOn($self));
			$ret.activate($self.columnActivate.runOn($self));
			$ret.disactivate($self.columnDisactivate.runOn($self));
			$ret.destroy($self.columnDestroy.runOn($self));
			return $ret;
		}()));
		this.trendLineManager(new $.ig.BarTrendLineManager());
	},
	_columns: null,
	columns: function (value) {
		if (arguments.length === 1) {
			this._columns = value;
			return value;
		} else {
			return this._columns;
		}
	}
	,
	onInit: function () {
		$.ig.AnchoredCategorySeriesView.prototype.onInit.call(this);
		this.visibleColumns(new $.ig.List$1($.ig.Rectangle.prototype.$type, 0));
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	columnCreate: function () {
		var column = new $.ig.Rectangle();
		this.visibleColumns().add(column);
		column.__visibility = $.ig.Visibility.prototype.collapsed;
		return column;
	}
	,
	_visibleColumns: null,
	visibleColumns: function (value) {
		if (arguments.length === 1) {
			this._visibleColumns = value;
			return value;
		} else {
			return this._visibleColumns;
		}
	}
	,
	columnActivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	columnDisactivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	columnDestroy: function (column) {
		this.visibleColumns().remove(column);
	}
	,
	setColumnPosition: function (column, x, y) {
		if (!this.isDirty()) {
			this.makeDirty();
		}
		column.canvasTop(y);
		column.canvasLeft(x);
	}
	,
	createBucketCalculator: function () {
		return new $.ig.BarBucketCalculator(this);
	}
	,
	setupItemHitAppearanceOverride: function (item, index) {
		$.ig.AnchoredCategorySeriesView.prototype.setupItemHitAppearanceOverride.call(this, item, index);
		var rect = item;
		var brush = this.getHitBrush1(index);
		rect.__fill = brush;
		rect.__stroke = brush;
		rect.strokeThickness(this.model().thickness());
	}
	,
	getItem: function (index) {
		return this.visibleColumns().__inner[index];
	}
	,
	__hitItem: null,
	getHitItem: function (index) {
		var item = this.visibleColumns().__inner[index];
		this.__hitItem.canvasLeft(item.canvasLeft());
		this.__hitItem.canvasTop(item.canvasTop());
		this.__hitItem.width(item.width());
		this.__hitItem.height(item.height());
		var hitBrush = this.getHitBrush1(index);
		this.__hitItem.__fill = hitBrush;
		this.__hitItem.__stroke = hitBrush;
		this.__hitItem.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		return this.__hitItem;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			for (var i = 0; i < this.visibleColumns().count(); i++) {
				var column = this.getCurrentItem(i, isHitContext);
				this.setupItemAppearance(column, i, isHitContext);
				context.renderRectangle(column);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var i = 0;
		var toSort = new $.ig.List$1($.ig.Rectangle.prototype.$type, 0);
		var en = this.columns().active().getEnumerator();
		while (en.moveNext()) {
			var column = en.current();
			toSort.add(column);
		}
		toSort.sort2(function (c1, c2) {
			if (c1.canvasTop() > c2.canvasTop()) {
				return -1;
			} else if (c1.canvasTop() < c2.canvasTop()) {
				return 1;
			} else {
				return 0;
			}
		});
		var en1 = toSort.getEnumerator();
		while (en1.moveNext()) {
			var column1 = en1.current();
			var rvd = new $.ig.RectangleVisualData(1, "column" + i, column1);
			rvd.tags().add("Main");
			rvd.tags().add("Fill");
			svd.shapes().add(rvd);
		}
		i++;
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(5);
		this.model().shadowOffsetX(5);
		this.model().shadowOffsetY(-5);
	}
	,
	$type: new $.ig.Type('BarSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('ColumnSeriesView', 'AnchoredCategorySeriesView', {
	onInit: function () {
		$.ig.AnchoredCategorySeriesView.prototype.onInit.call(this);
		this.visibleColumns(new $.ig.List$1($.ig.Rectangle.prototype.$type, 0));
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_columnModel: null,
	columnModel: function (value) {
		if (arguments.length === 1) {
			this._columnModel = value;
			return value;
		} else {
			return this._columnModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		this.__hitItem = new $.ig.Rectangle();
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.columnModel(model);
		this.columns((function () {
			var $ret = new $.ig.Pool$1($.ig.Rectangle.prototype.$type);
			$ret.create($self.columnCreate.runOn($self));
			$ret.activate($self.columnActivate.runOn($self));
			$ret.disactivate($self.columnDisactivate.runOn($self));
			$ret.destroy($self.columnDestroy.runOn($self));
			return $ret;
		}()));
	},
	columnCreate: function () {
		var column = new $.ig.Rectangle();
		this.visibleColumns().add(column);
		column.__visibility = $.ig.Visibility.prototype.collapsed;
		return column;
	}
	,
	_visibleColumns: null,
	visibleColumns: function (value) {
		if (arguments.length === 1) {
			this._visibleColumns = value;
			return value;
		} else {
			return this._visibleColumns;
		}
	}
	,
	columnActivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	columnDisactivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	columnDestroy: function (column) {
		this.visibleColumns().remove(column);
	}
	,
	positionRectangle: function (column, left, top) {
		if (!this.isDirty()) {
			this.makeDirty();
		}
		column.canvasTop(top);
		column.canvasLeft(left);
	}
	,
	getItem: function (index) {
		return this.visibleColumns().__inner[index];
	}
	,
	__hitItem: null,
	getHitItem: function (index) {
		var item = this.visibleColumns().__inner[index];
		this.__hitItem.__visibility = item.__visibility;
		this.__hitItem.canvasLeft(item.canvasLeft());
		this.__hitItem.canvasTop(item.canvasTop());
		this.__hitItem.width(item.width());
		this.__hitItem.height(item.height());
		var hitBrush = this.getHitBrush1(index);
		this.__hitItem.__fill = hitBrush;
		this.__hitItem.__stroke = hitBrush;
		this.__hitItem.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		return this.__hitItem;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		var doGradients = false;
		if (context.shouldRender()) {
			for (var i = 0; i < this.visibleColumns().count(); i++) {
				var column = this.getCurrentItem(i, isHitContext);
				this.setupItemAppearance(column, i, isHitContext);
				context.renderRectangle(column);
			}
		}
	}
	,
	_columns: null,
	columns: function (value) {
		if (arguments.length === 1) {
			this._columns = value;
			return value;
		} else {
			return this._columns;
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var i = 0;
		var toSort = new $.ig.List$1($.ig.Rectangle.prototype.$type, 0);
		var en = this.columns().active().getEnumerator();
		while (en.moveNext()) {
			var column = en.current();
			toSort.add(column);
		}
		toSort.sort2(function (c1, c2) {
			if (c1.canvasLeft() < c2.canvasLeft()) {
				return -1;
			} else if (c1.canvasLeft() > c2.canvasLeft()) {
				return 1;
			} else {
				return 0;
			}
		});
		var en1 = toSort.getEnumerator();
		while (en1.moveNext()) {
			var column1 = en1.current();
			var rvd = new $.ig.RectangleVisualData(1, "column" + i, column1);
			rvd.tags().add("Main");
			rvd.tags().add("Fill");
			svd.shapes().add(rvd);
		}
		i++;
	}
	,
	$type: new $.ig.Type('ColumnSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('LineSeriesView', 'AnchoredCategorySeriesView', {
	_lineModel: null,
	lineModel: function (value) {
		if (arguments.length === 1) {
			this._lineModel = value;
			return value;
		} else {
			return this._lineModel;
		}
	}
	,
	init: function (model) {
		this._polyline0 = new $.ig.Path();
		this._polygon01 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		this.__hitPolyline1 = new $.ig.Path();
		this.__hitPolyline0 = new $.ig.Path();
		this.__hitPolygon01 = new $.ig.Path();
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.lineModel(model);
	},
	_polyline0: null,
	_polygon01: null,
	_polyline1: null,
	line0: function () {
		return this._polyline0;
	}
	,
	line1: function () {
		return this._polyline1;
	}
	,
	fillArea: function () {
		return this._polygon01;
	}
	,
	clearLine: function () {
		this._polygon01.data(null);
		this._polyline0.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	rasterizeLine: function (bucketCount, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution) {
		this.lineModel().lineRasterizer().isSortingAxis($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.categoryModel().getXAxis()) !== null ? true : false);
		this.lineModel().lineRasterizer().rasterizePolylinePaths(this._polyline0, this._polygon01, this._polyline1, bucketCount, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution);
		this.makeDirty();
	}
	,
	setupAppearanceOverride: function () {
		$.ig.AnchoredCategorySeriesView.prototype.setupAppearanceOverride.call(this);
	}
	,
	__hitPolyline1: null,
	__hitPolyline0: null,
	__hitPolygon01: null,
	setupHitAppearanceOverride: function () {
		$.ig.AnchoredCategorySeriesView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolyline0.data(this._polyline0.data());
		this.__hitPolyline1.data(this._polyline1.data());
		this.__hitPolygon01.data(this._polygon01.data());
		var hitBrush = this.getHitBrush();
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolygon01.__fill = hitBrush;
		this.__hitPolygon01.__opacity = 1;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (isHitContext) {
				context.renderPath(this.__hitPolygon01);
				context.renderPath(this.__hitPolyline0);
				context.renderPath(this.__hitPolyline1);
			} else {
				context.renderPath(this._polygon01);
				context.renderPath(this._polyline0);
				context.renderPath(this._polyline1);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		lowerShape.tags().add("Main");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon01);
		translucent.tags().add("Translucent");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(4);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('LineSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('PointSeriesView', 'AnchoredCategorySeriesView', {
	_pointModel: null,
	pointModel: function (value) {
		if (arguments.length === 1) {
			this._pointModel = value;
			return value;
		} else {
			return this._pointModel;
		}
	}
	,
	init: function (model) {
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.pointModel(model);
	},
	onInit: function () {
		$.ig.AnchoredCategorySeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.markerModel().markerType($.ig.MarkerType.prototype.automatic);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.pointBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(2);
		this.model().shadowOffsetY(2);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('PointSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('SplineAreaSeriesView', 'SplineSeriesBaseView', {
	_splineAreaModel: null,
	splineAreaModel: function (value) {
		if (arguments.length === 1) {
			this._splineAreaModel = value;
			return value;
		} else {
			return this._splineAreaModel;
		}
	}
	,
	init: function (model) {
		this._polygon0 = new $.ig.Path();
		this._polyline0 = new $.ig.Path();
		this._polygon1 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		this.__hitPolygon0 = new $.ig.Path();
		this.__hitPolyline0 = new $.ig.Path();
		this.__hitPolygon1 = new $.ig.Path();
		this.__hitPolyline1 = new $.ig.Path();
		$.ig.SplineSeriesBaseView.prototype.init.call(this, model);
		this.splineAreaModel(model);
	},
	onInit: function () {
		$.ig.SplineSeriesBaseView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_polygon0: null,
	_polyline0: null,
	_polygon1: null,
	_polyline1: null,
	line0: function () {
		return this._polyline0;
	}
	,
	line1: function () {
		return this._polyline1;
	}
	,
	polygon0: function () {
		return this._polygon0;
	}
	,
	polygon1: function () {
		return this._polygon1;
	}
	,
	clearSplineArea: function () {
		this._polygon0.data(null);
		this._polygon1.data(null);
		this._polyline0.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	rasterizeSplineArea: function (count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting) {
		this.rasterizeSplineArea1(count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, null);
	}
	,
	rasterizeSplineArea1: function (count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, clipper) {
		this.anchoredModel().lineRasterizer().isSortingAxis($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.categoryModel().getXAxis()) !== null ? true : false);
		this.anchoredModel().lineRasterizer().rasterizePolygonPaths1(this._polygon0, this._polyline0, this._polygon1, this._polyline1, count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, clipper);
		this.makeDirty();
	}
	,
	__hitPolygon0: null,
	__hitPolyline0: null,
	__hitPolygon1: null,
	__hitPolyline1: null,
	setupHitAppearanceOverride: function () {
		$.ig.SplineSeriesBaseView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolygon0.data(this._polygon0.data());
		this.__hitPolyline0.data(this._polyline0.data());
		this.__hitPolygon1.data(this._polygon1.data());
		this.__hitPolyline1.data(this._polyline1.data());
		var hitBrush = this.getHitBrush();
		this.__hitPolygon0.__fill = hitBrush;
		this.__hitPolygon0.__opacity = 1;
		this.__hitPolygon1.__fill = hitBrush;
		this.__hitPolygon1.__opacity = 1;
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.SplineSeriesBaseView.prototype.renderOverride.call(this, context, isHitContext);
		if (isHitContext) {
			context.renderPath(this.__hitPolygon0);
			context.renderPath(this.__hitPolygon1);
			context.renderPath(this.__hitPolyline0);
			context.renderPath(this.__hitPolyline1);
		} else {
			context.renderPath(this._polygon0);
			context.renderPath(this._polygon1);
			context.renderPath(this._polyline0);
			context.renderPath(this._polyline1);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.SplineSeriesBaseView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		upperShape.tags().add("Main");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon1);
		translucent.tags().add("Translucent");
		var fill = new $.ig.PathVisualData(1, "fillShape", this._polygon0);
		fill.tags().add("Fill");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
		svd.shapes().add(fill);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(5);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(-3);
	}
	,
	$type: new $.ig.Type('SplineAreaSeriesView', $.ig.SplineSeriesBaseView.prototype.$type)
}, true);

$.ig.util.defType('SplineSeriesView', 'SplineSeriesBaseView', {
	_splineModel: null,
	splineModel: function (value) {
		if (arguments.length === 1) {
			this._splineModel = value;
			return value;
		} else {
			return this._splineModel;
		}
	}
	,
	init: function (model) {
		this._polyline0 = new $.ig.Path();
		this._polygon01 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		this.__hitPolyline1 = new $.ig.Path();
		this.__hitPolyline0 = new $.ig.Path();
		this.__hitPolygon01 = new $.ig.Path();
		$.ig.SplineSeriesBaseView.prototype.init.call(this, model);
		this.splineModel(model);
	},
	_polyline0: null,
	_polygon01: null,
	_polyline1: null,
	line0: function () {
		return this._polyline0;
	}
	,
	line1: function () {
		return this._polyline1;
	}
	,
	fillArea: function () {
		return this._polygon01;
	}
	,
	clearSplineLines: function () {
		this._polygon01.data(null);
		this._polyline0.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	rasterizeSpline: function (count, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution) {
		this.anchoredModel().lineRasterizer().isSortingAxis($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.categoryModel().getXAxis()) !== null ? true : false);
		this.anchoredModel().lineRasterizer().rasterizePolylinePaths(this._polyline0, this._polygon01, this._polyline1, count, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution);
		this.makeDirty();
	}
	,
	__hitPolyline1: null,
	__hitPolyline0: null,
	__hitPolygon01: null,
	setupHitAppearanceOverride: function () {
		$.ig.SplineSeriesBaseView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolyline0.data(this._polyline0.data());
		this.__hitPolyline1.data(this._polyline1.data());
		this.__hitPolygon01.data(this._polygon01.data());
		var hitBrush = this.getHitBrush();
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolygon01.__fill = hitBrush;
		this.__hitPolygon01.__opacity = 1;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.SplineSeriesBaseView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (isHitContext) {
				context.renderPath(this.__hitPolygon01);
				context.renderPath(this.__hitPolyline0);
				context.renderPath(this.__hitPolyline1);
			} else {
				context.renderPath(this._polygon01);
				context.renderPath(this._polyline0);
				context.renderPath(this._polyline1);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.SplineSeriesBaseView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		lowerShape.tags().add("Main");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon01);
		translucent.tags().add("Translucent");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
	}
	,
	$type: new $.ig.Type('SplineSeriesView', $.ig.SplineSeriesBaseView.prototype.$type)
}, true);

$.ig.util.defType('StepAreaSeriesView', 'AnchoredCategorySeriesView', {
	_stepAreaModel: null,
	stepAreaModel: function (value) {
		if (arguments.length === 1) {
			this._stepAreaModel = value;
			return value;
		} else {
			return this._stepAreaModel;
		}
	}
	,
	init: function (model) {
		this._polygon0 = new $.ig.Path();
		this._polyline0 = new $.ig.Path();
		this._polygon1 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		this.__hitPolygon0 = new $.ig.Path();
		this.__hitPolyline0 = new $.ig.Path();
		this.__hitPolygon1 = new $.ig.Path();
		this.__hitPolyline1 = new $.ig.Path();
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.stepAreaModel(model);
	},
	onInit: function () {
		$.ig.AnchoredCategorySeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_polygon0: null,
	_polyline0: null,
	_polygon1: null,
	_polyline1: null,
	polygon0: function () {
		return this._polygon0;
	}
	,
	polyline0: function () {
		return this._polyline0;
	}
	,
	polygon1: function () {
		return this._polygon1;
	}
	,
	polyline1: function () {
		return this._polyline1;
	}
	,
	clearStepArea: function () {
		this._polygon0.data(null);
		this._polygon1.data(null);
		this._polyline0.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	rasterizeStepArea: function (count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting) {
		this.rasterizeStepArea(count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting);
	}
	,
	rasterizeStepArea1: function (count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, clipper) {
		this.anchoredModel().lineRasterizer().isSortingAxis($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.categoryModel().getXAxis()) !== null ? true : false);
		this.anchoredModel().lineRasterizer().rasterizePolygonPaths1(this._polygon0, this._polyline0, this._polygon1, this._polyline1, count, buckets, useX0AsX1, bucketSize, resolution, terminatePolygon, unknownValuePlotting, clipper);
		this.makeDirty();
	}
	,
	__hitPolygon0: null,
	__hitPolyline0: null,
	__hitPolygon1: null,
	__hitPolyline1: null,
	setupHitAppearanceOverride: function () {
		$.ig.AnchoredCategorySeriesView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolygon0.data(this._polygon0.data());
		this.__hitPolyline0.data(this._polyline0.data());
		this.__hitPolygon1.data(this._polygon1.data());
		this.__hitPolyline1.data(this._polyline1.data());
		var hitBrush = this.getHitBrush();
		this.__hitPolygon0.__fill = hitBrush;
		this.__hitPolygon0.__opacity = 1;
		this.__hitPolygon1.__fill = hitBrush;
		this.__hitPolygon1.__opacity = 1;
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (isHitContext) {
			context.renderPath(this.__hitPolygon0);
			context.renderPath(this.__hitPolygon1);
			context.renderPath(this.__hitPolyline0);
			context.renderPath(this.__hitPolyline1);
		} else {
			context.renderPath(this._polygon0);
			context.renderPath(this._polygon1);
			context.renderPath(this._polyline0);
			context.renderPath(this._polyline1);
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		upperShape.tags().add("Main");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon1);
		translucent.tags().add("Translucent");
		var fill = new $.ig.PathVisualData(1, "fillShape", this._polygon0);
		fill.tags().add("Fill");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
		svd.shapes().add(fill);
	}
	,
	$type: new $.ig.Type('StepAreaSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('StepLineSeriesView', 'AnchoredCategorySeriesView', {
	_stepModel: null,
	stepModel: function (value) {
		if (arguments.length === 1) {
			this._stepModel = value;
			return value;
		} else {
			return this._stepModel;
		}
	}
	,
	init: function (model) {
		this._polyline0 = new $.ig.Path();
		this._polygon01 = new $.ig.Path();
		this._polyline1 = new $.ig.Path();
		this.__hitPolyline1 = new $.ig.Path();
		this.__hitPolyline0 = new $.ig.Path();
		this.__hitPolygon01 = new $.ig.Path();
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.stepModel(model);
	},
	_polyline0: null,
	_polygon01: null,
	_polyline1: null,
	line0: function () {
		return this._polyline0;
	}
	,
	line1: function () {
		return this._polyline1;
	}
	,
	fillArea: function () {
		return this._polygon01;
	}
	,
	clearStepLine: function () {
		this._polygon01.data(null);
		this._polyline0.data(null);
		this._polyline1.data(null);
		this.makeDirty();
	}
	,
	rasterizeStepLine: function (count, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution) {
		this.anchoredModel().lineRasterizer().isSortingAxis($.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.categoryModel().getXAxis()) !== null ? true : false);
		this.anchoredModel().lineRasterizer().rasterizePolylinePaths(this._polyline0, this._polygon01, this._polyline1, count, buckets, useX0AsX1, unknownValuePlotting, clipper, bucketSize, resolution);
		this.makeDirty();
	}
	,
	__hitPolyline1: null,
	__hitPolyline0: null,
	__hitPolygon01: null,
	setupHitAppearanceOverride: function () {
		$.ig.AnchoredCategorySeriesView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolyline0.data(this._polyline0.data());
		this.__hitPolyline1.data(this._polyline1.data());
		this.__hitPolygon01.data(this._polygon01.data());
		var hitBrush = this.getHitBrush();
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolygon01.__fill = hitBrush;
		this.__hitPolygon01.__opacity = 1;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (isHitContext) {
				context.renderPath(this.__hitPolygon01);
				context.renderPath(this.__hitPolyline0);
				context.renderPath(this.__hitPolyline1);
			} else {
				context.renderPath(this._polygon01);
				context.renderPath(this._polyline0);
				context.renderPath(this._polyline1);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PathVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		lowerShape.tags().add("Main");
		var upperShape = new $.ig.PathVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		var translucent = new $.ig.PathVisualData(1, "translucentShape", this._polygon01);
		translucent.tags().add("Translucent");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(translucent);
	}
	,
	applyDropShadowDefaultSettings: function () {
		var color = new $.ig.Color();
		color.colorString("rgba(95,95,95,0.5)");
		this.model().shadowColor(color);
		this.model().shadowBlur(3);
		this.model().shadowOffsetX(1);
		this.model().shadowOffsetY(4);
		this.model().useSingleShadow(false);
	}
	,
	$type: new $.ig.Type('StepLineSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('WaterfallSeriesView', 'AnchoredCategorySeriesView', {
	onInit: function () {
		$.ig.AnchoredCategorySeriesView.prototype.onInit.call(this);
		this.visibleColumns(new $.ig.List$1($.ig.Rectangle.prototype.$type, 0));
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.waterfallModel().negativeBrush((function () {
				var $ret = new $.ig.Brush();
				$ret.fill("#415460");
				return $ret;
			}()));
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.positiveNegativeBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_waterfallModel: null,
	waterfallModel: function (value) {
		if (arguments.length === 1) {
			this._waterfallModel = value;
			return value;
		} else {
			return this._waterfallModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		this.__hitItem = new $.ig.Rectangle();
		$.ig.AnchoredCategorySeriesView.prototype.init.call(this, model);
		this.waterfallModel(model);
		this.columns((function () {
			var $ret = new $.ig.Pool$1($.ig.Rectangle.prototype.$type);
			$ret.create($self.columnCreate.runOn($self));
			$ret.activate($self.columnActivate.runOn($self));
			$ret.disactivate($self.columnDisactivate.runOn($self));
			$ret.destroy($self.columnDestroy.runOn($self));
			return $ret;
		}()));
	},
	columnCreate: function () {
		var column = new $.ig.Rectangle();
		this.visibleColumns().add(column);
		column.__visibility = $.ig.Visibility.prototype.collapsed;
		return column;
	}
	,
	_visibleColumns: null,
	visibleColumns: function (value) {
		if (arguments.length === 1) {
			this._visibleColumns = value;
			return value;
		} else {
			return this._visibleColumns;
		}
	}
	,
	columnActivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	columnDisactivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	columnDestroy: function (column) {
		this.visibleColumns().remove(column);
	}
	,
	positionRectangle: function (column, left, top) {
		var dirty = false;
		if (column.canvasTop() != top) {
			dirty = true;
			column.canvasTop(top);
		}
		if (column.canvasLeft() != left) {
			dirty = true;
			column.canvasLeft(left);
		}
		if (dirty) {
			this.makeDirty();
		}
	}
	,
	getItem: function (index) {
		return this.visibleColumns().__inner[index];
	}
	,
	__hitItem: null,
	getHitItem: function (index) {
		var item = this.visibleColumns().__inner[index];
		this.__hitItem.__visibility = item.__visibility;
		this.__hitItem.canvasLeft(item.canvasLeft());
		this.__hitItem.canvasTop(item.canvasTop());
		this.__hitItem.width(item.width());
		this.__hitItem.height(item.height());
		var hitBrush = this.getHitBrush1(index);
		this.__hitItem.__fill = hitBrush;
		this.__hitItem.__stroke = hitBrush;
		this.__hitItem.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		return this.__hitItem;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.AnchoredCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		var doGradients = false;
		if (context.shouldRender()) {
			for (var i = 0; i < this.visibleColumns().count(); i++) {
				var column = this.getCurrentItem(i, isHitContext);
				this.setupItemAppearance(column, i, isHitContext);
				context.renderRectangle(column);
			}
		}
	}
	,
	_columns: null,
	columns: function (value) {
		if (arguments.length === 1) {
			this._columns = value;
			return value;
		} else {
			return this._columns;
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.AnchoredCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var i = 0;
		var toSort = new $.ig.List$1($.ig.Rectangle.prototype.$type, 0);
		var en = this.columns().active().getEnumerator();
		while (en.moveNext()) {
			var column = en.current();
			toSort.add(column);
		}
		toSort.sort2(function (c1, c2) {
			if (c1.canvasLeft() < c2.canvasLeft()) {
				return -1;
			} else if (c1.canvasLeft() > c2.canvasLeft()) {
				return 1;
			} else {
				return 0;
			}
		});
		var en1 = toSort.getEnumerator();
		while (en1.moveNext()) {
			var column1 = en1.current();
			var rvd = new $.ig.RectangleVisualData(1, "column" + i, column1);
			rvd.tags().add("Main");
			svd.shapes().add(rvd);
			if (column1.__fill == this.model().actualBrush()) {
				rvd.tags().add("Positive");
			} else {
				rvd.tags().add("Negative");
			}
		}
		i++;
	}
	,
	getColumnBoundingBox: function (index) {
		var rect = this.columns().item(index);
		return new $.ig.Rect(0, rect.canvasLeft(), rect.canvasTop(), rect.width(), rect.height());
	}
	,
	getColumnBoundingBoxFromColumn: function (rect) {
		return new $.ig.Rect(0, rect.canvasLeft(), rect.canvasTop(), rect.width(), rect.height());
	}
	,
	$type: new $.ig.Type('WaterfallSeriesView', $.ig.AnchoredCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('LegendView', 'LegendBaseView', {
	init: function (model) {
		$.ig.LegendBaseView.prototype.init.call(this, model);
		this.legendModel(model);
	},
	_legendModel: null,
	legendModel: function (value) {
		if (arguments.length === 1) {
			this._legendModel = value;
			return value;
		} else {
			return this._legendModel;
		}
	}
	,
	onInit: function () {
		$.ig.LegendBaseView.prototype.onInit.call(this);
	}
	,
	$type: new $.ig.Type('LegendView', $.ig.LegendBaseView.prototype.$type)
}, true);

$.ig.ConsolidatedItemsPosition.prototype.minimum = 0;
$.ig.ConsolidatedItemsPosition.prototype.maximum = 1;
$.ig.ConsolidatedItemsPosition.prototype.median = 2;
$.ig.ConsolidatedItemsPosition.prototype.relativeMinimum = 3;
$.ig.ConsolidatedItemsPosition.prototype.relativeMaximum = 4;

$.ig.VerticalAnchoredCategorySeries.prototype.xAxisPropertyName = "XAxis";
$.ig.VerticalAnchoredCategorySeries.prototype.yAxisPropertyName = "YAxis";
$.ig.VerticalAnchoredCategorySeries.prototype.xAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.VerticalAnchoredCategorySeries.prototype.xAxisPropertyName, $.ig.NumericXAxis.prototype.$type, $.ig.VerticalAnchoredCategorySeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.VerticalAnchoredCategorySeries.prototype.xAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.VerticalAnchoredCategorySeries.prototype.yAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.VerticalAnchoredCategorySeries.prototype.yAxisPropertyName, $.ig.CategoryYAxis.prototype.$type, $.ig.VerticalAnchoredCategorySeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.VerticalAnchoredCategorySeries.prototype.yAxisPropertyName, e.oldValue(), e.newValue());
}));

$.ig.BarSeries.prototype.radiusXPropertyName = "RadiusX";
$.ig.BarSeries.prototype.radiusYPropertyName = "RadiusY";
$.ig.BarSeries.prototype.radiusXProperty = $.ig.DependencyProperty.prototype.register($.ig.BarSeries.prototype.radiusXPropertyName, Number, $.ig.BarSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BarSeries.prototype.radiusXPropertyName, e.oldValue(), e.newValue());
}));
$.ig.BarSeries.prototype.radiusYProperty = $.ig.DependencyProperty.prototype.register($.ig.BarSeries.prototype.radiusYPropertyName, Number, $.ig.BarSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.BarSeries.prototype.radiusYPropertyName, e.oldValue(), e.newValue());
}));

$.ig.AreaSeries.prototype._unknownValuePlottingPropertyName = "UnknownValuePlotting";
$.ig.AreaSeries.prototype.unknownValuePlottingProperty = $.ig.DependencyProperty.prototype.register($.ig.AreaSeries.prototype._unknownValuePlottingPropertyName, $.ig.UnknownValuePlotting.prototype.$type, $.ig.AreaSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.UnknownValuePlotting.prototype.getBox($.ig.UnknownValuePlotting.prototype.dontPlot), function (sender, e) {
	(sender).raisePropertyChanged($.ig.AreaSeries.prototype._unknownValuePlottingPropertyName, e.oldValue(), e.newValue());
}));

$.ig.ColumnSeries.prototype.radiusXPropertyName = "RadiusX";
$.ig.ColumnSeries.prototype.radiusYPropertyName = "RadiusY";
$.ig.ColumnSeries.prototype._consolidatedColumnVerticalPositionPropertyName = "ConsolidatedColumnVerticalPosition";
$.ig.ColumnSeries.prototype.radiusXProperty = $.ig.DependencyProperty.prototype.register($.ig.ColumnSeries.prototype.radiusXPropertyName, Number, $.ig.ColumnSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ColumnSeries.prototype.radiusXPropertyName, e.oldValue(), e.newValue());
}));
$.ig.ColumnSeries.prototype.radiusYProperty = $.ig.DependencyProperty.prototype.register($.ig.ColumnSeries.prototype.radiusYPropertyName, Number, $.ig.ColumnSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.ColumnSeries.prototype.radiusYPropertyName, e.oldValue(), e.newValue());
}));

$.ig.LineSeries.prototype._unknownValuePlottingPropertyName = "UnknownValuePlotting";
$.ig.LineSeries.prototype.unknownValuePlottingProperty = $.ig.DependencyProperty.prototype.register($.ig.LineSeries.prototype._unknownValuePlottingPropertyName, $.ig.UnknownValuePlotting.prototype.$type, $.ig.LineSeries.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.UnknownValuePlotting.prototype.getBox($.ig.UnknownValuePlotting.prototype.dontPlot), function (sender, e) {
	(sender).raisePropertyChanged($.ig.LineSeries.prototype._unknownValuePlottingPropertyName, e.oldValue(), e.newValue());
}));

$.ig.WaterfallSeries.prototype.negativeBrushPropertyName = "NegativeBrush";
$.ig.WaterfallSeries.prototype.radiusXPropertyName = "RadiusX";
$.ig.WaterfallSeries.prototype.radiusYPropertyName = "RadiusY";
$.ig.WaterfallSeries.prototype.negativeBrushProperty = $.ig.DependencyProperty.prototype.register($.ig.WaterfallSeries.prototype.negativeBrushPropertyName, $.ig.Brush.prototype.$type, $.ig.WaterfallSeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.WaterfallSeries.prototype.negativeBrushPropertyName, e.oldValue(), e.newValue());
}));
$.ig.WaterfallSeries.prototype.radiusXProperty = $.ig.DependencyProperty.prototype.register($.ig.WaterfallSeries.prototype.radiusXPropertyName, Number, $.ig.WaterfallSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.WaterfallSeries.prototype.radiusXPropertyName, e.oldValue(), e.newValue());
}));
$.ig.WaterfallSeries.prototype.radiusYProperty = $.ig.DependencyProperty.prototype.register($.ig.WaterfallSeries.prototype.radiusYPropertyName, Number, $.ig.WaterfallSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.WaterfallSeries.prototype.radiusYPropertyName, e.oldValue(), e.newValue());
}));

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

} (jQuery));


