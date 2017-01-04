﻿/*!@license
* Infragistics.Web.ClientUI Video Player localization resources 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

/*global jQuery */
(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.VideoPlayer) {
	    $.ig.VideoPlayer = {};

	    $.extend($.ig.VideoPlayer, {

		    locale: {
			    liveStream: "ライブ ビデオ",
			    live: "ライブ",
			    paused: "一時停止",
			    playing: "再生している",
			    play: '再生',
			    volume: "音量",
			    unsupportedVideoSource: "現在のビデオ ソースにはブラウザーでサポートされる書式が含まれていません。",
			    missingVideoSource: "互換性のあるビデオ ソースがありません。",
			    progressLabelLongFormat: "$currentTime$ / $duration$",
			    progressLabelShortFormat: "$currentTime$",
			    enterFullscreen: "全画面表示の開始",
			    exitFullscreen: "全画面表示の終了",
			    skipTo: "指定の位置に移動",
			    unsupportedBrowser: "現在のブラウザーは HTML5 ビデオをサポートしません。<br/>以下のバージョンにアップグレードしてください。",
			    currentBrowser: "現在のブラウザー: {0}",
			    ie9: "Microsoft Internet Explorer 9+",
			    chrome8: "Google Chrome 8+",
			    firefox36: "Mozilla Firefox 3.6+",
			    safari5: "Apple Safari 5+",
			    opera11: "Opera 11+",
			    ieDownload: "http://www.microsoft.com/japan/windows/internet-explorer/default.aspx",
			    operaDownload: "http://www.opera.com/download/",
			    chromeDownload: "http://www.google.co.jp/chrome/intl/ja/landing_ff.html?hl=ja",
			    firefoxDownload: "http://www.mozilla.com/",
			    safariDownload: "http://www.apple.com/jp/safari/download/",
			    buffering: '読み込んでいます',
			    adMessage: '広告: ビデオは $duration$ 秒後に再開します。', // Use also $timeString$ for MM:SS format.
			    adMessageLong: '広告: ビデオは $duration$ 後に再開します。',
			    adMessageNoDuration: '広告: ビデオはコマーシャルの後に再開します。',
			    adNewWindowTip: '広告: クリックすると、広告コンテンツを新しいウィンドウで開きます。',
			    nonDivException: 'Infragistics HTML5 ビデオ プレーヤーは DIV タグのみにインスタンス化できます。',
			    relatedVideos: '関連ビデオ',
			    replayButton: '再生',
			    replayTooltip: 'クリックすると、前回再生したビデオを再生します'
		    }
	    });

    }
})(jQuery);

/*!@license
 * Infragistics.Web.ClientUI jQuery HTML5 Video Player 16.1.20161.2145
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *  jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *  infragistics.util.js
 *  infragistics.ui.shared.js
 */

/*global jQuery, window */
if (typeof jQuery !== "function") {
	throw new Error("jQuery is undefined");
}

(function ($) {

	$.widget("ui.igVideoPlayer", {
		_const: {
			VOLUME_MAX: 1.0,
			VOLUME_MIN: 0.0,
			VIDEO_STATE_TIMEOUT: 200,
			SCROLL_TIMEOUT: 100,
			COMMERCIAL_SEEK_DELTA: 2,
			SLIDE_SEEK_TIMEOUT: 500,
			HREF: "#"
		},

		_id: function (id) {
			return this.element[ 0 ].id + id;
		},

		options: {
			/* type="array" Get or set a list of video sources to choose from. Best coded/format is automatically detected by the control. Supported types are depending on the browser and could be one of the following mov, mp4, webm, ogg. */
			sources: [],
			/* type="number" Get or set the width of the control. By default null will stretch the control to fit data, if no other widths are defined. */
			width: null,
			/* type="number" Get or set the height of the control. By default null will stretch the control to fit data, if no other heights are defined. */
			height: null,
			/* type="string" Get or set a URL to an image to show, when no video data is available. */
			posterUrl: "",
			/* type="bool" Get or set whether to preload load initial data for duration of video. If true it may start buffering the video, but this highly depends on the specific browser implementation. */
			preload: false,
			/* type="bool" Get or set whether the video should start playing immediately after the control is loaded. */
			autoplay: false,
			/* type="bool" Get or set whether player controls will auto hide when video is not hovered. This is applicable only when Infragistics playback controls are used. */
			autohide: true,
			/* type="number" Get or set volume slider auto hide delay. This is applicable only when Infragistics playback controls are used. */
			volumeAutohideDelay: 1000,
			/* type="number" Get or set the center big button hide delay. */
			centerButtonHideDelay: 1200,
			/* type="bool" Get or set whether the video to start again after it has ended. */
			loop: false,
			/* type="bool" Get or set whether if you want to use the built in browser controls. By default player uses Infragistics playback controls. Note that you may have different look and feel across different browsers if you use the built in browser controls. */
			browserControls: false,
			/* type="bool" Get or set whether the video player to be in full screen or not. This is not a pure full screen, because browsers do not allow that. It just sets 100% width and height to the control. */
			fullscreen: false,
			/* type="number" Get or set the video volume. It can be between 0.0 and 1.0. */
			volume: 0.5,
			/* type="bool" Get or set whether the video volume is muted. */
			muted: false,
			/* type="string" Get or set video title. */
			title: "",
			/* type="bool" Get or set whether the control seek tool tip will be shown when hovering the video progress bar. */
			showSeekTime: true,
			/* type="string" Get or set the format of the video progress label. You should use ${currentTime} to represent current playback position and ${duration} to represent video duration. */
			progressLabelFormat: "${currentTime} / ${duration}",
			/* type="array" Get or set an array of bookmarks that will be displayed in the video player control. */
			bookmarks: [{
				/* type="number" Get or set where the bookmark will be positioned. Should be between 0 and movie duration in seconds. */
				time: 0,
				/* type="string" Get or set bookmark title. It is shown as tooltip on hover. */
				title: "",
				/* type="bool" Get or set whether the bookmark is disabled or not. */
				disabled: false
			}],
			/* type="array" Get or set an array of related videos that will be displayed when video playback has ended. */
			relatedVideos: [{
				/* type="string" Get or set the URL of the related video image. */
				imageUrl: "",
				/* type="string" Get or set the title of the video. */
				title: "",
				/* type="number" Get or set the width of the related video image. */
				width: null,
				/* type="number" Get or set the height of the related video image. */
				height: null,
				/* type="string" Get or set a link to a page that will play the related video. It will be opened in a new window. If there are sources also, the link property has a priority. */
				link: "",
				/* type="array" Get or set the sources of the related video. */
				sources: [],
				/* type="string" Get or set custom CSS class to be applied on the related video element. */
				css: null
			}],
			/* type="array" Get or set an array of banner objects that will show the banners when the video clip is played. */
			banners: [{
				/* type="string" Get or set the banner image url. */
				imageUrl: "",
				/* type="array" Get or set an array of numbers. Each number specifies on which second in the movie the banner will pop. */
				times: [],
				/* type="bool" Get or set whether the user will be able to close the banner or not. */
				closeBanner: true,
				/* type="bool" Get or set whether to apply animation effects when showing or hiding the banner. If set to true, the animation is played for banner.duration in milliseconds. */
				animate: true,
				/* type="bool" Get or set whether the banner is visible or not. */
				visible: true,
				/* type="number" Get or set the banner animation duration. */
				duration: 1000,
				/* type="bool" Get or set whether to automatically hide the banner. If set to true, the banner is hidden after banner.hidedelay in milliseconds. */
				autohide: false,
				/* type="number" Get or set the banner autohide delay in milliseconds. It is taken into account only if the banner.autohide option is set to true. */
				hidedelay: 10000,
				/* type="string" Get or set the banner link that will open in new window. */
				link: null,
				/* type="number" Get or set the banner width */
				width: null,
				/* type="number" Get or set the banner height */
				height: null,
				/* type="string" Get or set the banner specific css class, that will be applied on the banner grid. */
				css: null
			}],
			/* type="object" Get or set an array of commercials objects that will be displayed when the video is playing. */
			commercials: {
				/* type="array" Get or set an array of linked commercial objects. A linked commercial is a separate video file that will be played in the specified position of the original movie clip by the startTime property. This feature is useful if you have frequently changing outside commercial sources. */
				linkedCommercials: [{
					/* type="array" Get or set the sources of the linked commercial video. */
					sources: [],
					/* type="number" Get or set the second in the video at which the linked commercial should play. */
					startTime: 10,
					/* type="string" Get or set the link to open on linked commercial click. */
					link: "",
					/* type="string" Get or set the tooltip for the linked commercial bookmark. */
					title: ""
				}],
				/* type="array" Get or set an array of embedded commercials objects. An embedded commercial is an ad that is contained in the original video file. It is suitable when you want to mark some sections of the video as commercials. */
				embeddedCommercials: [{
					/* type="number" Get or set the start second of the embedded commercial. */
					startTime: 20,
					/* type="number" Get or set the end second of the embedded commercial. */
					endTime: 30,
					/* type="string" Get or set the sponsored link of the embedded commercial. */
					link: "",
					/* type="string" Get or set the tooltip for the bookmark of the embedded commercial. */
					title: ""
				}],
				/* type="bool" Get or set whether the commercials will play againg during the repetitive video plays. */
				alwaysPlayCommercials: false,
				/* type="bool" Get or set whether to show commercial locations or not. */
				showBookmarks: true,
				/* type="object" Customizes the ad message settings of the commercial. Ad message shows the duration of the commercial and pops up when the commercial starts playing.  */
				adMessage: {
					/* type="bool" Get or set whether to apply an animation effect when showing or hiding the ad message. If set to true, the animation is played for commercial.adMessage.animationDuration in milliseconds. */
					animate: true,
					/* type="bool" Get or set the ad message auto hide of the commercial. */
					autoHide: true,
					/* type="number" Get or set the ad message hide delay. */
					hideDelay: 20000,
					/* type="number" Get or set the ad message animation duration of the commercial. */
					animationDuration: 1000
				}
			}
		},

		events: {
			/* Occurs when video has ended.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			Use ui.duration to get the video duration in seconds.
			*/
			ended: "ended",
			/* cancel="false" Occurs when video gets playing.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			Use ui.duration to get the video duration in seconds.
			*/
			playing: "playing",
			/* Occurs when video is paused.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			Use ui.duration to get the video duration in seconds.
			*/
			paused: "paused",
			/* cancel="false" Occurs when a chunk of data is buffered.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			Use ui.buffered to get buffered percentage.
			*/
			buffering: "buffering",
			/* Occurs when video has advanced the playback position.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			Use ui.currentTime to get current position in the video at which the event was fired.
			Use ui.duration to get the video duration in seconds.
			*/
			progress: "progress",
			/* cancel="false" Occurs when igVideoPlayer is waiting for data from the server.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			Use ui.currentTime to get current position in the video at which the event was fired.
			Use ui.duration to get the video duration in seconds.
			*/
			waiting: "waiting",
			/* Occurs when the bookmark is hit.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			Use ui.bookmark to get the bookmark object from the bookmarks array.
			Use ui.bookmarkElement to get the html element in the DOM.
			*/
			bookmarkHit: "bookmarkHit",
			/* Occurs when the bookmark is clicked.
			Function takes arguments evt and ui.
			Use ui.bookmark to get the bookmark object from the bookmarks array.
			Use ui.bookmarkElement to get the html element in the DOM.
			*/
			bookmarkClick: "bookmarkClick",
			/* cancel="true" Occurs when igVideoPlayer enters full screen mode.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			*/
			enterFullScreen: "enterFullScreen",
			/* cancel="true" Occurs when igVideoPlayer exits full screen mode.
			Function takes arguments evt and ui.
			Use ui.source to get the url of the playing video.
			*/
			exitFullScreen: "exitFullScreen",
			/* cancel="true" Occurs when related video is clicked.
			Function takes arguments evt and ui.
			Use ui.relatedVideo to get the relatedVideo object from the relatedVideos array.
			Use ui.relatedVideoElement to get the relatedVideo html element in the DOM.
			*/
			relatedVideoClick: "relatedVideoClick",
			/* Defines the name of the player banner visible event. Fired when the banner has been displayed.
			Function takes arguments evt and ui.
			Use ui.index to get the banner index in the banners array.
			Use ui.banner to get the banner object from the banners array.
			Use ui.bannerElement to get the banner html element in the DOM.
			*/
			bannerVisible: "bannerVisible",
			/* Occurs when the banner is hidden.
			Function takes arguments evt and ui.
			Use ui.index to get the banner index in the banners array.
			Use ui.banner to get the banner object from the banners array.
			Use ui.bannerElement to get the banner html element in the DOM.
			*/
			bannerHidden: "bannerHidden",
			/* Occurs when the banner is clicked.
			Function takes arguments evt and ui.
			Use ui.bannerElement to get the banner html element in the DOM.
			*/
			bannerClick: "bannerClick",
			/* Occurs when browser does not support HTML 5 video. */
			browserNotSupported: "browserNotSupported"
		},

		css: {
			/* Get or set the widget base CSS classes. */
			"baseClasses": "ui-widget ui-igplayer",
			/* Get or set the player controls CSS classes. */
			"controlsClass": "ui-widget-header ui-igplayer-controls " +
			"ui-igplayer-grid ui-igplayer-autohide ui-corner-all",
			/* Get or set the controls hide CSS class. Applied on controls auto hide. */
			"controlsHideClass": "ui-igplayer-controls-hide",
			/* Get or set the class applied on main video element. */
			"videoClass": "ui-igplayer-video",
			/* Get or set the class applied on a span element, shown when the player is opened in a non HTML5 compatible browser. */
			"unsupportedBrowserClass": "ui-html5-non-html5-supported-message ui-helper-clearfix",
			/* Get or set the css class applied on the main video when the control is activated. */
			"activeVideoClass": "ui-igplayer-active-video",
			/* Get or set the css class applied on the play button of the playback controls. */
			"playButtonClass": "ui-igplayer-playbutton",
			/* Get or set the css class applied on the slider widget of the playback controls. */
			"progressBarClass": "ui-igplayer-progressbar ui-corner-all",
			/* Get or set the css class applied on the full screen button of the playback controls. */
			"fullScreenClass": "ui-igplayer-fullscreen-button",
			/* Get or set the css class applied on the full screen button icon. */
			"fullScreenIconClass": "ui-icon ui-igbutton-icon ui-icon-arrow-4-diag",
			/* Get or set the css class applied on the volume control button of the playback controls. */
			"volumeControlClass": "ui-igplayer-volumecontrol",
			/* Get or set the css class applied on the volume slider of the playback controls. */
			"volumeSliderClass": "ui-igplayer-volumeslider",
			/* Get or set the css class applied on the progress label of the playback controls. */
			"progressLabelClass": "ui-igplayer-progresslabel",
			/* Get or set the css class applied on the bookmark container DIV. */
			"bookmarkContainerClass": "ui-widget ui-igplayer-bookmark-container",
			/* Get or set the css class applied on the bookmark header container. */
			"bookmarkContainerHeaderClass": "ui-widget-header ui-igplayer-bookmark-header",
			/* Get or set the css class applied on the bookmark list in the bookmark area. */
			"bookmarkListClass": "ui-widget-content ui-igplayer-bookmark-list",
			/* Get or set the css class applied on the bookmark list item when the item in the bookmark area is disabled. */
			"bookmarkItemDisabledClass": "ui-igplayer-bookmark-item-disabled",
			/* Get or set the css class applied on the bookmark list item showing the time in the bookmark area. */
			"bookmarkItemTimeClass": "ui-igplayer-bookmark-item-time",
			/* Get or set the css class applied on the bookmark list item showing the title in the bookmark area. */
			"bookmarkItemTitleClass": "ui-igplayer-bookmark-item-title",
			/* Get or set the css class showing the active bookmark item in the bookmark area. */
			"bookmarkActiveItemClass": "ui-igplayer-bookmark-active-item",
			/* Get or set the css class applied on the related videos container class. */
			"relatedVideosContainerClass": "ui-igplayer-related-video-container",
			/* Get or set the css class applied on the related videos outer most container. */
			"relatedVideosOuterContainerClass": "ui-igplayer-related-video-outer-container",
			/* Get or set the css class applied on a related video element. */
			"relatedVideoClass": "ui-igplayer-related-video",
			/* Get or set the css class applied on the first related video element. */
			"relatedVideoFirstClass": "ui-igplayer-related-video-first",
			/* Get or set the css class applied on the last related video element. */
			"relatedVideoLastClass": "ui-igplayer-related-video-last",
			/* Get or set the css class applied on a related video hover. */
			"relatedVideoHoverClass": "ui-igplayer-related-video-hover",
			/* Get or set the css class applied on the left scroll button. */
			"relatedVideoScrollLeftClass": "ui-igplayer-related-scroller-left",
			/* Get or set the class applied on the left scroll button icon. */
			"relatedVideoScrollLeftIconClass": "ui-icon-triangle-1-w",
			/* Get or set the css class applied on the right scroll button of the related videos. */
			"relatedVideoScrollRightClass": "ui-igplayer-related-scroller-right",
			/* Get or set the css class applied on the right icon of the scroll button. */
			"relatedVideoScrollRightIconClass": "ui-icon-triangle-1-e",
			/* Get or set the css class applied on the related videos list. */
			"relatedVideoScrollListClass": "ui-igplayer-related-list",
			/* Get or set the css class applied on the related videos header area. */
			"relatedVideosHeaderClass": "ui-igplayer-related-video-header",
			/* Get or set the css class applied on the related videos bottom area containing the replay and fullscreen buttons. */
			"relatedVideoBarClass": "ui-igplayer-related-video-bar",
			/* Get or set the css class applied on the replay button in the related videos area. */
			"relatedVideoReplayClass": "ui-igplayer-related-video-replay",
			/* Get or set the css class applied on the replay button icon. */
			"relatedVideoReplayIconClass": "ui-icon-arrowrefresh-1-s",
			/* Get or set the css class applied on the banner container div. */
			"bannerContainerClass": "ui-igplayer-banner-container",
			/* Get or set the css class applied on the banner container div. */
			"bannerClass": "ui-igplayer-banner ui-corner-all",
			/* Get or set the css class applied on the banner close button. */
			"bannerCloseClass": "ui-igplayer-banner-close",
			/* Get or set the css class applied on the banner close icon. */
			"bannerCloseIconClass": "ui-icon-close",
			/* Get or set the css class applied on the resume video message container. */
			"adMsgContainerClass": "ui-igplayer-ad-msg-container",
			/* Get or set the css class applied on the resume video message span. */
			"adMsgClass": "ui-igplayer-ad-msg",
			/* Get or set the css class applied on the resume video message close button. */
			"adMsgCloseClass": "ui-igplayer-ad-msg-close",
			/* Get or set the css class applied on the resume video message close button icon. */
			"adMsgCloseIconClass": "ui-icon-close",
			/* Get or set the css class applied on the linked commercial bookmark. */
			"linkedBookmarkClass": "ui-igplayer-linked-bookmark",
			/* Get or set the css class applied on an embedded commercial bookmark. */
			"adBookmarkClass": "ui-igplayer-ad-bookmark",
			/* Get or set the css class applied on the anchor element shown when video sources are unsupported. */
			"unsupportedVideoSourceClass": "ui-igplayer-not-supported-video-source",
			/* Get or set the css class applied on the anchor element showing the unsupported video source icon. */
			"unsupportedVideoSourceIconClass": "ui-igplayer-not-supported-video-source-icon",
			/* Get or set the css class applied on the center play button. */
			"centerPlayButtonClass": "ui-igplayer-centerplaybutton-play",
			/* Get or set the css class applied on the center pause button. */
			"centerPauseButtonClass": "ui-igplayer-centerplaybutton-pause",
			/* Get or set the css class applied on the center play button icon. */
			"centerPlayButtonIconClass": "ui-igplayer-centerplaybutton-icon",
			/* Get or set the css class applied on the buffering indicator. */
			"waitingIndicatorClass": "ui-igplayer-waiting",
			/* Get or set the css class applied on the buffering indicator icon. */
			"waitingIndicatorIconClass": "ui-igplayer-waiting-icon",
			/* Get or set the css class applied on the seek tooltip. */
			"seekTooltipClass": "ui-igplayer-seektooltip"
		},

		widget: function () {
			/*
			Returns the element on which the widget was instantiated
			*/
			return this.element;
		},

		_createWidget: function () {
			/* !Strip dummy objects from options, because they are defined for documentation purposes only! */
			var o = this.options;
			o.bookmarks = [];
			o.relatedVideos = [];
			o.banners = [];
			o.commercials.linkedCommercials = [];
			o.commercials.embeddedCommercials = [];
			$.Widget.prototype._createWidget.apply(this, arguments);
		},

		_create: function () {
			/* create the video element */

			// Under mobile devices always browser controls are rendered, except when Modernizr is undefined
			if ($.ig.util.isTouch) {
				this.options.browserControls = true;
			}

			if (this.element.is("div")) {
				this.container = this.element;
			} else if (this.element.is("video")) {
				this.container = $("<div></div>").attr("id", this._id("_container")).insertBefore(this.element);
				this.element.appendTo(this.container);
			} else {
				throw new Error($.ig.VideoPlayer.locale.nonDivException);
			}

			this._oldWidth = this.element.css("width");
			this._oldHeight = this.element.css("height");

			this._renderControl();
		},

		_renderControl: function () {
			var o = this.options,
				video,
				css = this.css;

			this._prevReadyState = 0;
			this._bookmarksRendered = false;

			if (this.supportsVideo()) {
				this._renderBanners();
			}

			this.currentVideo = video = this._createMainVideoElement().addClass(css.videoClass);

			if (o.disabled) {
				this.container.addClass("ui-state-disabled");
			}

			if (o.width) {
				video.css("width", o.width);
				this.container.css("width", o.width);
			}

			if (o.height) {
				video.css("height", o.height);
				this.container.css("height", o.height);
			}

			if (this.supportsVideo()) {
				this._baseZIndex = parseInt(this.container.css("z-index"), 10) || 0;
				this._attachEvents(video);
				this._renderSources(o.sources, video);
				this._analyzeSource(video);
				setTimeout($.proxy(this._onVideoStateChange, this), this._const.VIDEO_STATE_TIMEOUT);

				this._createBigPlayButton();
				this._createWaitingIndicator();
				this._renderEmbeddedCommercials();
				this._renderCommercials();
				video[ 0 ].muted = o.muted;
				if (o.muted && $.ig.util.isWebKit) {
					video[ 0 ].volume = 0;
				} else if (o.volume >= this._const.VOLUME_MIN && o.volume <= this._const.VOLUME_MAX) {
					video[ 0 ].volume = o.volume;
				}

				if (!o.browserControls) {
					this._renderControls();
				}
				this._lastPausedState = video[ 0 ].paused;

				if (o.fullscreen) {
					this._setOption("fullscreen", true, true);
				}

				this._renderRelatedVideos();
			} else {
				this.currentVideo.hide();

				//this._renderUnsupportedBrowser();
				$.ig.util._renderUnsupportedBrowser(this, $.ig.VideoPlayer.locale);
			}
			/* if rendered, move it to be always last element in the widget */

			// K.D. June 8th, 2011 Bug #78401 Preventing default action on click
			$("#" + this._id("_detectError")).appendTo(this.container).bind({
				click: function (event) {
					event.preventDefault();
				}
			});

			this.container.addClass(css.baseClasses);
		},

		_createVideoElement: function (id) {
			var video = $("<video></video>").attr("id", id);
			return video;
		},

		_saveExistingVideoProperties: function (video) {

			this._oldAutoPlay = this._getVideoProperty(video, "autoplay");
			this._oldPreload = this._getVideoProperty(video, "preload");
			this._oldLoop = this._getVideoProperty(video, "loop");
			this._oldPoster = this._getVideoProperty(video, "poster");
			this._oldControls = this._getVideoProperty(video, "controls");
			this._oldSrc = this._getVideoProperty(video, "src");
			this._oldMuted = video[ 0 ].muted;
			this._oldVolume = video[ 0 ].volume;
			this._removeVideoProperty(video, "autoplay");
			this._removeVideoProperty(video, "preload");
			this._removeVideoProperty(video, "loop");
			this._removeVideoProperty(video, "poster");
			this._removeVideoProperty(video, "controls");
			this._removeVideoProperty(video, "src");
		},

		_restoreExistingVideoProperties: function (video) {
			this._setVideoProperty(video, "autoplay", this._oldAutoPlay);
			this._setVideoProperty(video, "preload", this._oldPreload);
			this._setVideoProperty(video, "loop", this._oldLoop);
			this._setVideoProperty(video, "poster", this._oldPoster);
			this._setVideoProperty(video, "controls", this._oldControls);
			this._setVideoProperty(video, "src", this._oldSrc);
			video[ 0 ].muted = this._oldMuted;
			video[ 0 ].volume = this._oldVolume;
		},

		_createMainVideoElement: function () {
			var o = this.options,
				isVideoExisting = this.element.is("video"),
				video = (isVideoExisting ? this.element : this._createVideoElement(this._id("_video")));

			if (isVideoExisting) {
				this._saveExistingVideoProperties(video);
			}

			if (o.autoplay) {
				this._setVideoProperty(video, "autoplay", true);
			}
			this._setVideoProperty(video, "preload", "metadata");
			if (o.preload) {
				this._setVideoProperty(video, "preload", "auto");
			}

			if (o.browserControls) {
				this._setVideoProperty(video, "controls", true);
			}

			if (o.loop) {
				this._setVideoProperty(video, "loop", true);
			}

			if (o.posterUrl.length > 0) {
				this._setVideoProperty(video, "poster", o.posterUrl);
			}
			return video.appendTo(this.container);
		},

		_setVideoProperty: function (video, prop, value) {
			if (video.prop) {
				video.prop(prop, value);
			} else {
				video.attr(prop, value);
			}
		},

		_getVideoProperty: function (video, prop) {
			if (video.prop) {
				return video.prop(prop);
			} else {
				return video.attr(prop);
			}
		},

		_removeVideoProperty: function (video, prop) {
			if (video.prop) {
				return video.removeProp(prop);
			} else {
				return video.removeAttr(prop);
			}
		},

		_renderUnsupportedBrowser: function () {
			if (this._trigger(this.events.browserNotSupported)) {
				var container =
				$("<div></div>").addClass(this.css.unsupportedBrowserClass).appendTo(this.container),
				 ul, browserUnsupported;

				// K.D. June 7th, 2011 Bug #76981 Currently only the version is shown and as this is important for
				// IE showing the name of the browser before the version as well
				if ($.ig.util.isIE) {
					browserUnsupported = "Internet Explorer " + $.ig.util.browserVersion;
				} else if ($.ig.util.isOpera) {
					browserUnsupported = "Opera " + $.ig.util.browserVersion;
				} else if ($.ig.util.isWebKit) {
					browserUnsupported = "Webkit " + $.ig.util.browserVersion;
				} else if ($.ig.util.isFF) {
					browserUnsupported = "Mozilla Firefox " + $.ig.util.browserVersion;
				} else {
					browserUnsupported = $.ig.util.browserVersion;
				}

				$("<div></div>").addClass("ui-igvideoplayer-current-browser-label")
								.html($.ig.VideoPlayer.locale.currentBrowser.replace("{0}", browserUnsupported))
								.appendTo(container);
				$("<div></div>").addClass("ui-igvideoplayer-non-html5-text")
								.html($.ig.VideoPlayer.locale.unsupportedBrowser).appendTo(container);
				ul = $("<ul></ul>").addClass("ui-igplayer-browsers-list").appendTo(container);
				$("<a></a>").attr("href", $.ig.VideoPlayer.locale.chromeDownload).attr("target", "_blank")
							.addClass("ui-igplayer-chrome-icon")
							.html($.ig.VideoPlayer.locale.chrome8).appendTo($("<li></li>")
							.addClass("ui-corner-all").appendTo(ul));
				$("<a></a>").attr("href", $.ig.VideoPlayer.locale.firefoxDownload).attr("target", "_blank")
							.addClass("ui-igplayer-firefox-icon")
							.html($.ig.VideoPlayer.locale.firefox36).appendTo($("<li></li>")
							.addClass("ui-corner-all").appendTo(ul));
				$("<a></a>").attr("href", $.ig.VideoPlayer.locale.operaDownload).attr("target", "_blank")
							.addClass("ui-igplayer-Opera-icon")
							.html($.ig.VideoPlayer.locale.opera11).appendTo($("<li></li>")
							.addClass("ui-corner-all").appendTo(ul));
				$("<a></a>").attr("href", $.ig.VideoPlayer.locale.safariDownload).attr("target", "_blank")
							.addClass("ui-igplayer-safari-icon")
							.html($.ig.VideoPlayer.locale.safari5).appendTo($("<li></li>")
							.addClass("ui-corner-all").appendTo(ul));
				$("<a></a>").attr("href", $.ig.VideoPlayer.locale.ieDownload).attr("target", "_blank")
							.addClass("ui-igplayer-ie-icon")
							.html($.ig.VideoPlayer.locale.ie9).appendTo($("<li></li>")
							.addClass("ui-corner-all").appendTo(ul));
				this.container.addClass("ui-igvideoplayer-non-html5");
			}
		},

		_renderRelatedVideoBar: function (container) {
			var css = this.css,
				control = this,
				relatedVideoBar = $("<div></div>").attr("id", this._id("_rv_bar"))
												.addClass(css.relatedVideoBarClass)
												.appendTo(container);
			/* render related video full screen button */
			$("<a></a>").attr("id", this._id("_rv_bar_fs")).appendTo(
				$("<div></div>").css({
					"position": "absolute",
					"top": 0,
					"left": 0,
					"text-align": "right",
					"width": "100%",
					"height": "100%"
				}).appendTo(relatedVideoBar)
			).igButton({
				link: { href: this._const.HREF },
				css: { buttonLabelClass: css.fullScreenIconClass }
			}).attr("title", $.ig.VideoPlayer.locale.enterFullscreen).addClass(css.fullScreenClass).bind({
				click: function (event) {
					event.preventDefault();
					control._setOption("fullscreen", !control.options.fullscreen);
				}
			});
			/* render related video replay button */
			$("<a></a>").attr("id", this._id("_rv_bar_replay")).appendTo(relatedVideoBar).igButton({
				onlyIcons: false,
				labelText: $.ig.VideoPlayer.locale.replayButton,
				icons: { primary: css.relatedVideoReplayIconClass },
				link: { href: this._const.HREF }
			}).attr("title", $.ig.VideoPlayer.locale.replayTooltip).bind({
				click: function (event) {
					event.preventDefault();
					if (!$(this).igButton("option", "disabled")) {
						control.togglePlay();
					}
				}
			}).addClass(css.relatedVideoReplayClass);
		},

		_renderScrollButtons: function (container) {
			var css = this.css,
				ul;
			$("<a></a>").attr("id", this._id("_rv_left")).appendTo(container).igButton({
				onlyIcons: true,
				icons: { primary: css.relatedVideoScrollLeftIconClass },
				link: { href: this._const.HREF }
			}).addClass(css.relatedVideoScrollLeftClass).css("position", "absolute").hide();

			ul = $("<ul></ul>").addClass(css.relatedVideoScrollListClass).appendTo(container);

			$("<a></a>").attr("id", this._id("_rv_right")).appendTo(container).igButton({
				onlyIcons: true,
				icons: { primary: css.relatedVideoScrollRightIconClass },
				link: { href: this._const.HREF }
			}).addClass(css.relatedVideoScrollRightClass).css("position", "absolute");

			return ul;
		},

		_renderRelatedVideos: function () {
			var o = this.options,
				related = o.relatedVideos || [],
				len = related.length,
				i = 0,
				css = this.css,
				container,
				scrollContainer,
				ulContainer,
				ulContainerWidth = 10, // In order to fully display the last related video, there is 10px margin from the parent element.
				currentVideo,
				control = this;

			if (len > 0) {
				container = $("<div></div>").attr("id",  this._id("_rvcc"))
											.addClass(css.relatedVideosOuterContainerClass)
											.appendTo(this.container).hide();
				/* render header div */
				$("<div><div>").html($.ig.VideoPlayer.locale.relatedVideos)
					.addClass(css.relatedVideosHeaderClass).appendTo(container);
				/* render scroll container with buttons and ul */
				scrollContainer = $("<div></div>").attr("id", this._id("_rvc"))
												.appendTo(container)
												.addClass(css.relatedVideosContainerClass);
				ulContainer = this._renderScrollButtons(scrollContainer);
				this._renderRelatedVideoBar(container);

				this._rvUlWidth = 0;
				this._rvImagesToLoad = 0;
				this._rvImagesLoaded = 0;

				for (i; i < len; i++) {
					if (related[ i ].imageUrl && related[ i ].imageUrl !== "") {
						currentVideo = this._renderRelatedVideo(related[ i ], ulContainer, i === 0, i === len - 1);
						this._rvImagesToLoad++;
						ulContainerWidth += currentVideo.outerWidth(true);
					}
				}

				// N.A. 10/31/2013 Bug #156185: Set width of the related videos in order to show all of them.
				if (ulContainerWidth > container.width() && ulContainerWidth > ulContainer.width()) {
					ulContainer.width(ulContainerWidth);
				}

				this.relatedScrollers = $("a", scrollContainer).bind({
					mouseover: function () {
						if (!control._isScrolling) {
							control._isScrolling = true;
							control._leftDirection = $(this).hasClass(control.css.relatedVideoScrollLeftClass);
							control._rvDoScroll();
						}
					},
					mouseout: function () {
						control._rvStopScroll();
					}
				});

				this.relatedVideoElements = $("." + this.css.relatedVideoClass.split(" ")[ 0 ], ulContainer)
					.each(function (i) {
						$(this).data("related-index", i);
					})
					.bind({
						mouseover: function () {
							$(this).addClass(control.css.relatedVideoHoverClass).children().filter("div").show();
						},
						mouseout: function () {
							$(this).removeClass(control.css.relatedVideoHoverClass).children().filter("div").hide();
						},
						click: function (event) {
							var relatedVideo = control.options.relatedVideos[ $(this).data("related-index") ];
							if (control._relatedVideoClick(relatedVideo, $(this), event)) {
								if (relatedVideo.link && relatedVideo.link.length > 0) {
									window.open(relatedVideo.link, "_blank");
								} else if (relatedVideo.sources) {
									control._renderSources(relatedVideo.sources, control.currentVideo);
									control._analyzeSource(control.currentVideo, true);
									control.play();
								}
							}
						}
					});
			}
		},

		_rvScrollOnce: function () {
			var container = $("#" + this._id("_rvc")),
				ul = $("ul", container),
				currentLeftStr = ul[ 0 ].style.left,
				currentLeft = parseInt((currentLeftStr === "" || currentLeftStr === "auto" ?
										"0px"
										: currentLeftStr), 10),
				pixelsToScroll = 10,
				newLeft = this._leftDirection ? currentLeft + pixelsToScroll : currentLeft - pixelsToScroll,
				left = $("#" + this._id("_rv_left")),
				right = $("#" + this._id("_rv_right")),
				ulWidth = ul.width(),
				scrollContainerWidth = container.width(),
				continueScrolling = true;
			ul[ 0 ].style.left = newLeft + "px";

			if (currentLeft === 0 && newLeft < 0) {
				left.show();
			}

			if (newLeft >= 0 && currentLeft !== 0 && left[ 0 ].style.display !== "none") {
				left.hide();
				continueScrolling = false;
			}

			if (scrollContainerWidth - newLeft >= ulWidth && right[ 0 ].style.display !== "none") {

				// hide the right button
				continueScrolling = false;
				right.hide();
			} else if (right[ 0 ].style.display === "none") {

				// show the right button
				right.show();
			}
			return continueScrolling;
		},

		_rvDoScroll: function () {
			var continueScrolling = this._isScrolling && this._rvScrollOnce();
			if (continueScrolling) {
				this._scrollingTimoutId =
				setTimeout($.proxy(this._rvDoScroll, this), this._const.SCROLL_TIMEOUT);
			} else {
				this._rvStopScroll();
			}
		},

		_rvStopScroll: function () {
			if (this._isScrolling) {
				clearTimeout(this._scrollingTimoutId);
				this._isScrolling = false;
			}
		},

		_relatedVideoClick: function (relatedVideo, relatedElement, event) {
			var args = {
				relatedVideo: relatedVideo,
				relatedVideoElement: relatedElement
			};
			return this._trigger(this.events.relatedVideoClick, event, args);
		},

		_renderRelatedVideo: function (related, container, isFirst, isLast) {
			var rv = $("<li></li>").appendTo(container).addClass(this.css.relatedVideoClass),
				relatedImg = $("<img/>").appendTo(rv),
				self = this;

			if (isFirst || isLast) {
				rv.addClass(isFirst ? this.css.relatedVideoFirstClass : this.css.relatedVideoLastClass);
			}

			if (related.imageUrl && related.imageUrl !== "") {
				relatedImg.attr("src", related.imageUrl);
			}

			if (related.title && related.title !== "") {
				relatedImg.attr("title", related.title);
			}

			if (related.css && related.css !== "") {
				relatedImg.addClass(related.css);
			}

			if (related.width && related.width !== "") {
				relatedImg.css("width", related.width);
			}

			if (related.height && related.height !== "") {
				relatedImg.css("height", related.height);
			}

			$("<div></div>").appendTo(relatedImg.parent()).css("position", "absolute").hide();

			relatedImg.bind({
				load: function () {
					self._imageLoaded(this);
				},
				error: function () {
					self._rvImagesLoaded++;
					self._onImagesLoaded();
				},
				readystatechange: function () {
					self._imageLoaded(this);
				}
			});
			return rv;
		},

		_imageLoaded: function (img) {
			var rs = img.readyState,
				width = parseInt(img.style.width, 10),
				height = parseInt(img.style.height, 10),
				spanSize;
			if (!rs || (new RegExp("loaded|complete")).test(rs)) {
				this._rvUlWidth += (width + parseInt($(img).parent().css("marginRight"), 10) +
								parseInt($(img).parent().css("marginLeft"), 10));
				this._rvImagesLoaded++;

				// calculate the best play icon to be put over the image.
				if (width > 24 && height > 24) {
					if (width > 50 && height > 50) {
						spanSize = 48;
					} else if (width > 34 && height > 34) {
						spanSize = 32;
					} else {
						spanSize = 24;
					}
					$(img).parent().children().filter("div")
						.addClass("ui-igplayer-related-video-hover-x" + spanSize)
						.css({ "width": spanSize,
							  "height": spanSize,
							  "top": (height / 2) - (spanSize / 2),
							  "left": (width / 2) - (spanSize / 2) });
				}
			}
			this._onImagesLoaded();
		},

		_onImagesLoaded: function () {
			if (this._rvImagesLoaded === this._rvImagesToLoad) {
				var container = $("#" + this._id("_rvc"));
				$("ul", container).css("width", this._rvUlWidth + 4);
				container.parent().show();
				if (this._rvUlWidth < container.width()) {
					this.relatedScrollers.hide();
				} else {
					$(this.relatedScrollers[ 1 ]).show();
				}
				container.parent().hide();
				delete this._rvUlWidth;
				delete this._rvImagesLoaded;
				delete this._rvImagesToLoad;
			}
		},

		_renderEmbeddedCommercials: function () {
			var ec = this.options.commercials.embeddedCommercials || [],
				i = 0,
				len = ec.length,
				filtered = [],
				val;

			/* filter only the ranges that are good. */
			for (i; i < len; i++) {
				val = ec[ i ];
				if (val && val.startTime && val.endTime && val.startTime < val.endTime) {
					filtered.push(val);
				}
			}

			ec = filtered;
			len = ec.length;

			if (ec && len > 0) {
				this._embeddedCommercialsShow = $.extend(true, [], ec);
				this._embeddedCommercialIndex = 0;
				this._renderAdMessage();
			}
		},

		_renderCommercials: function () {
			var o = this.options,
				com = o.commercials.linkedCommercials || [],
				control = this,
				video;
			if (com.length > 0) {
				video = this._createVideoElement(this._id("_com_video")).attr("preload", "auto")
				.appendTo(this.container).addClass(this.css.videoClass)
				.bind({
					ended: function () {
						var slider = $("#" + control._id("_ctrls_s")),
							bookmarks = slider.data("igSlider").bookmarks;
						control._hideWaitingIndicator();
						control._commercialIndex++;
						control._commercialsShow.shift();
						control._commercialPlaying = false;
						control.currentVideo = control._getMainVideo();
						control.currentVideo.show();
						$(this).hide();
						slider.igSlider("option", "disabled", false);
						if (bookmarks) {
							bookmarks.show();
						}
						control.currentVideo[ 0 ].muted = this.muted;
						control.currentVideo[ 0 ].play();
						control.currentVideo.addClass(control.css.videoClass);
						control.controls[ control._activeControlsIndex ].css("z-index", "");
						$("#" + control._id("_seek_tooltip")).css("z-index", "");
						$("#" + control._id("_play")).css("z-index", "");
					},
					waiting: function (event) {
						control._onVideoWaiting(event);
						control._waiting(this.currentSrc, this.currentTime, this.duration, event);
					},
					timeupdate: function (event) {
						control._changeCurrentTime(event);
					},
					playing: function (event) {
						control._hideWaitingIndicator();
						$("span", $("#" + control._id("_ctrls_play"))).addClass("ui-icon-pause")
																	.removeClass("ui-icon-play");
						control._playing(this.currentSrc, this.duration, event);
					},
					pause: function (event) {
						$("span", $("#" + control._id("_ctrls_play"))).addClass("ui-icon-play")
																	.removeClass("ui-icon-pause");
						control._paused(this.currentSrc, this.duration, event);
					},
					click: function (event) {
						event.preventDefault();
						var link = control.options.commercials.linkedCommercials[ control._commercialIndex ].link;
						if (link && link.length > 0) {
							window.open(link, "_blank");
						}
					}
				}).hide();
				if (video.prop && video.prop("preload") !== "auto") {
					video.prop("preload", "auto");
				}
				this._buildCommercialsShow();
				this._renderAdMessage();
			}
		},

		_renderAdClose: function (container) {
			var control = this;

			$("<a></a>").attr("id", this._id("_ad_msg_close")).appendTo(container).igButton({
				onlyIcons: true,
				icons: { primary: this.css.adMsgCloseIconClass },
				link: { href: this._const.HREF }
			}).addClass(this.css.adMsgCloseClass).bind("click", function (event) {
				event.preventDefault();
				event.stopPropagation();
				control.hideAdMessage();
			});
		},

		_renderAdMessage: function () {
			if ($("#" + this._id("_ad_msg_c")).length === 0) {
				var css = this.css,
					container;

				container = $("<div></div>").attr("id", this._id("_ad_msg_c"))
					.addClass(css.adMsgContainerClass).prependTo(this.container).bind({
						mouseover: function () {
							$(this).addClass("ui-state-hover");
						},
						mouseout: function () {
							$(this).removeClass("ui-state-hover");
						}
					}).hide();

				$("<span></span>").attr("id", this._id("_ad_msg")).appendTo(container)
					.addClass(css.adMsgClass).html($.ig.VideoPlayer.locale.adMessage);

				this._renderAdClose(container);
			}
		},

		_showAdMessage: function (seconds) {
			var adMsgContainer = $("#" + this._id("_ad_msg_c")),
				msgOpt = this.options.commercials.adMessage;

			this._updateAdMessage(seconds, true);
			adMsgContainer.css("width", this.container.css("width"));

			if (msgOpt.animate) {
				adMsgContainer.slideToggle(msgOpt.animationDuration);
			} else {
				adMsgContainer.show();
			}

			if (msgOpt.autoHide) {
				this._adHideTimeout = setTimeout($.proxy(this.hideAdMessage, this), msgOpt.hideDelay);
			}
		},

		_updateAdMessage: function (seconds, force) {
			var msg = $("#" + this._id("_ad_msg"));
			if (msg.is(":visible") || force) {
				if (isNaN(seconds)) {
					msg.html($.ig.VideoPlayer.locale.adMessageNoDuration);
				} else {
					seconds = parseInt(seconds, 10);
					if (seconds > 60) {
						msg.html($.ig.VideoPlayer.locale.adMessageLong
							.replace("$duration$", this._toTimeString(seconds)));
					} else {
						msg.html($.ig.VideoPlayer.locale.adMessage.replace("$duration$", seconds));
					}
				}
				this._isHiddenAdMessage = false;
			} else if (!this._isHiddenAdMessage) {
				this._showAdMessage(seconds);
			}
		},

		hideAdMessage: function () {
			/* Hide the add message if shown. */
			clearTimeout(this._adHideTimeout);
			var msg = $("#" + this._id("_ad_msg_c")),
				msgOpt = this.options.commercials.adMessage;
			if (msg.is(":visible")) {
				if (msgOpt.animate) {
					msg.slideToggle(msgOpt.animationDuration);
				} else {
					msg.hide();
				}
				this._isHiddenAdMessage = true;
			}
		},

		_buildCommercialsShow: function () {
			var com = this.options.commercials.linkedCommercials || [],
				len = com.length,
				i = 0;
			this._commercialsShow = [];
			this._commercialIndex = 0;
			for (i; i < len; i++) {
				this._commercialsShow.push(com[ i ].startTime);
			}
			this._commercialsShow = $.extend(true, [], this._commercialsShow);
		},

		playCommercial: function (commercial) {
			/* Play a linked commercial for this video.
			   paramType="object" Specify the linked commercial to play.
			*/
			if (!commercial || !commercial.sources) {
				return;
			}

			var comVideo = $("#" + this._id("_com_video")),
				videoOffset = this.currentVideo.offset(),
				slider = $("#" + this._id("_ctrls_s")),
				bookmarks = slider.data("igSlider").bookmarks,
				bookmarkTitle = $("#" + this._id("_ctrls_s_tooltip"));

			this._commercialPlaying = true;
			$("source", comVideo).remove();
			this._renderSources(commercial.sources, comVideo);
			this._analyzeSource(comVideo);
			this.currentVideo[ 0 ].pause();
			this.currentVideo.removeClass(this.css.videoClass);
			this.controls[ this._activeControlsIndex ].css("z-index", this._baseZIndex + 5000);
			$("#" + this._id("_seek_tooltip")).css("z-index", this._baseZIndex + 5000);
			$("#" + this._id("_play")).css("z-index", this._baseZIndex + 5000);

			slider.igSlider("option", "disabled", true);
			if (bookmarks && bookmarkTitle) {
				bookmarks.hide();
				bookmarkTitle.hide();
			}

			comVideo[ 0 ].muted = this.currentVideo[ 0 ].muted;
			comVideo[ 0 ].play();

			if (commercial.link && commercial.link.length > 0) {
				comVideo.attr("title", $.ig.VideoPlayer.locale.adNewWindowTip);
			} else {
				comVideo.removeAttr("title");
			}

			comVideo.css("top", videoOffset.top)
				.css("left", videoOffset.left)
				.css("width", this.currentVideo.css("width"))
				.css("height", this.currentVideo.css("height")).show();
			this.currentVideo.hide();
			this.currentVideo = comVideo;
		},

		_renderBanners: function () {
			var banners = this.options.banners || [],
				len = banners.length,
				i = 0,
				parent = $("<div></div>").appendTo(this.container).addClass(this.css.bannerContainerClass),
				banner;

			this._bannerShow = [];

			for (i; i < len; i++) {
				banner = banners[ i ];
				if (banner.imageUrl && banner.imageUrl.length > 0) {
					this._renderBannerContainer(banner, this._id("_banner_grid" + i), parent)
						.data("banner-index", i);
				}
				this._bannerShow.push($.extend(true, [], banner.times));
			}
		},

		_buildBannersShow: function () {
			var banners = this.options.banners || [],
				len = banners.length,
				i = 0;

			if (len > 0) {
				this._bannerShow = [];
			}

			for (i; i < len; i++) {
				this._bannerShow.push($.extend(true, [], banners[ i ].times));
			}
		},

		_renderBannerClose: function (container, id) {
			var css = this.css,
				control = this;

			$("<a></a>").attr("id", id + "_banner_close").appendTo(
				$("<div></div>").css({ "position": "absolute",
										"top": 0,
										"left": 0,
										"text-align": "right",
										"width": "100%",
										"height": "100%" }).appendTo(container)
			).addClass(css.bannerCloseClass).igButton({
				onlyIcons: true,
				icons: { primary: css.bannerCloseIconClass },
				link: { href: this._const.HREF }
			}).bind("click", function (event) {
				event.preventDefault();
				event.stopPropagation();
				control.hideBanner($(this).parent().parent().data("banner-index"));
			});
		},

		_renderBannerContainer: function (banner, id, parent) {
			var css = this.css,
				control = this,
				container = $("<div></div>").attr("id", id).appendTo(parent),
				img = $("<img></img>").attr("id", id + "_img").attr("src", banner.imageUrl).appendTo(container);

			this.banner = container.addClass(css.bannerClass);

			if (!banner.visible) {
				container.hide();
			}

			if (banner.closeBanner) {
				this._renderBannerClose(container, id);
			}

			if (banner.width) {
				container.css("width", banner.width);
				img.attr("width", parseInt(banner.width, 10));
			}

			if (banner.height) {
				container.css("height", banner.height);
				img.attr("height", parseInt(banner.height, 10));
			}

			if (banner.css) {
				container.addClass(banner.css);
			}

			container.bind("click", function (event) {
				var noCancel = true,
					args = {

						// K.D. June 3rd, 2011 Bug #77526 The banner option is no longer available and thus
						// removing the obsolete value from the click event arguments
						// S.S. January 3rd, 2012 Bug #97226 The best way to return the banner is by using the event's target. With
						// the previous code the bannerElement always returned the last banner regardless of the one clicked.
						bannerElement: $(event.currentTarget)
					};

				noCancel = control._trigger(control.events.bannerClick, event, args);

				if (banner.link && banner.link.length > 0 && noCancel) {
					window.open(banner.link, "_blank");
				}
			});
			return container;
		},

		showBanner: function (index) {
			/* Shows the ad banner, if there is such.
			   paramType="number" Specify the index of the banner from the banners array.
			*/
			if (this.options.banners && index >= 0 && index < this.options.banners.length) {
				var banner = this.options.banners[ index ],
					id = this._id("_banner_grid" + index),
					container = $("#" + id);
				if (banner.animate) {
					container.fadeIn(banner.duration);
				} else {
					container.show();
				}
				this._bannerVisible(index, container);

				if (banner.autohide) {
					this._bannerHideTimeout =
					setTimeout(this._createDelegate(this, this.hideBanner, [ index ]), banner.hidedelay);
				}
			}
		},

		_createDelegate: function (instance, method, args) {
			return function () {
				return method.apply(instance, args);
			};
		},

		hideBanner: function (index) {
			/* Hide the ad banner, if there is such.
			   paramType="number" Specify the index of the banner from the banners array.
			*/
			if (this.options.banners && index >= 0 && index < this.options.banners.length) {

				// Hides the ad banner, if there is such.
				clearTimeout(this._bannerHideTimeout);
				var banner = this.options.banners[ index ],
					container = $("#" + this._id("_banner_grid" + index));
				if (banner.animate) {
					container.fadeOut(banner.duration);
				} else {
					container.hide();
				}
				this._bannerHidden(index, container);
			}
		},

		_renderSources: function (sources, video) {
			var control = this;
			$(sources).each(function () {
				control._renderSource(this, video);
			});
		},

		_renderSource: function (source, video) {
			var qmIndex = source.indexOf("?"),
				extStartIndex = source.lastIndexOf(".") + 1,
				extEndIndex = qmIndex > -1 ? qmIndex : source.length,
				extension = source.substring(extStartIndex, extEndIndex);
			$("<source></source>").attr("src", source)
				.attr("type", ("video/" + source.substring(source.lastIndexOf(".") + 1, source.length))
				.replace("/ogv", "/ogg"))
				.appendTo(video);
			video.data(extension, source);
		},

		_getMainVideo: function () {
			return this.element.is("video") ? this.element : $("#" + this._id("_video"));
		},

		_setOption: function (key, value, force) {
			var video = this.currentVideo,
				videoElem = video[ 0 ],
				mainVideo = this._getMainVideo(),
				videoOffset = video.offset(),
				options = this.options,
				noCancel = true,
				fsVideoH = 0;

			if (this.options[ key ] === value) {
				return;
			}

			switch (key) {
			case "disabled":
				if (value) {
					this.container.addClass("ui-state-disabled");
				} else {
					this.container.removeClass("ui-state-disabled");
				}
				break;
			case "sources":
				$("source", mainVideo).remove();
				this._renderSources(value, mainVideo);
				this._analyzeSource(mainVideo);
				break;
			case "width":
				if (value !== options.width) {
					video.css("width", value);
					this.container.css("width", value);
					if (!options.browserControls) {
						$("#" + this._id("_ctrls")).parent().css("width", value);
						$("#" + this._id("_title_ctrls")).parent().css("width", value);
					}
					options.width = value;
				}
				break;
			case "height":
				if (value !== options.height) {
					video.css("height", value);
					this.container.css("height", value);
					options.height = value;
				}
				break;
			case "posterUrl":
				if (value !== options.posterUrl) {
					this._setVideoProperty(video, "poster", value);
					options.posterUrl = value;
				}
				break;
			case "loop":
				if (value !== options.loop) {
					videoElem.loop = value;
					options.loop = value;
				}
				break;
			case "browserControls":

				// Under mobile devices always browser controls are rendered
				if ($.ig.util.isTouch) {
					return;
				}
				if (value !== options.browserControls) {
					if (!value) {
						videoElem.controls = false;

						// S.S. June 24th, 2011 Bug #74601 Before rendering our controls we need to be sure the correct
						// volume is set for the widget to display in the volume slider.
						this._ensureVolume();
						this._renderControls();

						// K.D. November 22nd, 2011 Bug #80594 The buffered area is rendered with a large delay when
						// switching from browser to our controls, so forcing the rendering
						this._detectBuffered();
					} else {
						videoElem.controls = true;
						this._destroyControls();
					}
					options.browserControls = value;
				}
				break;
			case "autohide":
				if (!options.browserControls) {
					if (value) {
						this._onControlMouseOut(); // hide the controls.
					} else {
						this._onControlMouseOver(); // show the controls.
					}
				}
				options.autohide = value;
				break;
			case "fullscreen":
				if (value !== options.fullscreen || force) {
					if (value) {
						noCancel = this._enterFullScreen(videoElem.currentSrc);
						if (noCancel) {
							this.container.data("style.position", this.container.css("position"));
							this.container.data("document.scrollTop", $(document).scrollTop());
							this.container.data("document.scrollLeft", $(document).scrollLeft());
							this.container.data("style.left", this.container.css("left"));
							this.container.data("style.top", this.container.css("top"));
							this.container.css("z-index", this._baseZIndex + 10000);
							$(document).scrollTop(0);
							$(document).scrollLeft(0);
							this.container.css("position", "fixed")
								.css("height", "100%")
								.css("width", "100%")
								.css("left", "0px")
								.css("top", "0px");
							$("#" + this._id("_ctrls")).parent().css("width", "100%");

							if (this._commercialPlaying) {
								video.css("top", 0).css("left", 0);
								mainVideo.css("height", "100%").css("width", "100%");
							}
							video.css("height", "100%").css("width", "100%");
							if (!options.browserControls) {
								$("span", $("#" + this._id("_ctrls_fs_btn")))
									.removeClass("ui-icon-arrow-4-diag")
									.addClass("ui-icon-closethick");
							}

							if ($("#" + this._id("_rv_bar_fs")).is(":visible")) {
								$("span", $("#" + this._id("_rv_bar_fs")))
									.removeClass("ui-icon-arrow-4-diag")
									.addClass("ui-icon-closethick");
							}

							if (options.bookmarks && options.bookmarks.length > 0) {
								$("#" + this._id("_bookmarks")).hide();
							}
						}
					} else {
						noCancel = this._exitFullScreen(videoElem.currentSrc);
						if (noCancel) {
							this.container.css("position", this.container.data("style.position"))
								.css("left", this.container.data("style.left"))
								.css("top", this.container.data("style.top"));
							this.container.css("z-index", this._baseZIndex);
							$(document).scrollTop(this.container.data("document.scrollTop"));
							$(document).scrollLeft(this.container.data("document.scrollLeft"));
							if (options.height) {
								if (options.bookmarks && options.bookmarks.length > 0) {
									fsVideoH = parseInt(options.height, 10) -
												parseInt($("#" + this._id("_bookmarks")).css("height"), 10);
									if (this._commercialPlaying) {
										mainVideo.css("height", fsVideoH);
									}
									video.css("height", fsVideoH);
								} else {
									if (this._commercialPlaying) {
										mainVideo.css("height", options.height);
									}
									video.css("height", options.height);
								}
								this.container.css("height", options.height);
							} else {
								if (this._commercialPlaying) {
									mainVideo.css("height", "");
								}
								video.css("height", "");
								this.container.css("height", "");
							}

							if (options.width) {
								if (this._commercialPlaying) {
									mainVideo.css("width", options.width);
								}
								video.css("width", options.width);
								$("#" + this._id("_ctrls")).parent().css("width", options.width);
								this.container.css("width", options.width);
							} else {
								if (this._commercialPlaying) {
									mainVideo.css("width", "");
								}
								video.css("width", "");
								$("#" + this._id("_ctrls")).parent().css("width", "");
								this.container.css("width", "");
							}

							if (this._commercialPlaying) {
								video.css("top", videoOffset.top).css("left", videoOffset.left);
							}

							if (!options.browserControls) {
								$("span", $("#" + this._id("_ctrls_fs_btn")))
									.removeClass("ui-icon-closethick")
									.addClass("ui-icon-arrow-4-diag");
							}

							if ($("#" + this._id("_rv_bar_fs")).is(":visible")) {
								$("span", $("#" + this._id("_rv_bar_fs")))
									.removeClass("ui-icon-closethick")
									.addClass("ui-icon-arrow-4-diag");
							}

							if (options.bookmarks && options.bookmarks.length > 0) {
								$("#" + this._id("_bookmarks")).show();
							}
						}
					}

					if (!noCancel) {
						return; /* event was cancelled! */
					}

					if ($("#" + this._id("_waiting")).is(":visible")) {

						// recalculate the position of waiting indicator.
						this._showWaitingIndicator();
					}

					if ($("#" + this._id("_play")).is(":visible")) {
						this._showCenterPlayButton();
					}

					if ($("#" + this._id("_ad_msg_c")).is(":visible")) {
						$("#" + this._id("_ad_msg_c")).css("width", this.container.css("width"));
					}

					if ($("#" + this._id("_ctrls_vs")).is(":visible")) {
						this._hideVolumeSlider();
					}

					if ($("#" + this._id("_detectError")).is(":visible")) {
						this._showUnsupportedVideoSourceMsg();
					}

					$("body").toggleClass("ui-igplayer-full-screen-mode");

					if (!options.browserControls) {
						$("#" + this._id("_ctrls_fs_btn"))
						.attr("title", value ?
									$.ig.VideoPlayer.locale.exitFullscreen :
									$.ig.VideoPlayer.locale.enterFullscreen);
					}
					if ($("#" + this._id("_rv_bar_fs")).is(":visible")) {
						$("#" + this._id("_rv_bar_fs"))
						.attr("title", value ?
									$.ig.VideoPlayer.locale.exitFullscreen :
									$.ig.VideoPlayer.locale.enterFullscreen);
					}
					options.fullscreen = value;
				}
				break;
			case "volume":
				if (value !== options.volume && value >= this._const.VOLUME_MIN &&
					value <= this._const.VOLUME_MAX) {
					options.volume = videoElem.volume = value;

					if (value > 0 && videoElem.muted) {
						options.muted = videoElem.muted = false;
					} else if (value === 0 && !videoElem.muted) {
						options.muted = videoElem.muted = true;
					}

					if (!options.browserControls) {
						this._adjustVolumeButton(value);
						$("#" + this._id("_ctrls_vs")).igSlider("option", "value", value * 100);
					}
				}
				break;
			case "muted":
				if (value !== options.muted) {
					options.muted = videoElem.muted = value;
					$("#" + this._id("_ctrls_vs"))
					.igSlider("option", "value", (value ?
													0 :
													(options.volume !== 0 ? options.volume : 0.3) * 100));
					if (options.volume === 0) {
						options.volume = 0.3;
					}
					if (!value && videoElem.volume === 0) {
						videoElem.volume = options.volume;
					}
					if (!options.browserControls) {
						this._adjustVolumeButton($("#" + this._id("_ctrls_vs")).igSlider("option", "value"));
					}
				}
				break;
			case "title":
				$("#" + this._id("_title_ctrls_t")).html(value);
				break;
			case "progressLabelFormat":
				options.progressLabelFormat = value;
				this._updateProgressLabel();
				break;
			case "alwaysPlayCommercials":
				if (value !== options.commercials.alwaysPlayCommercials) {
					options.commercials.alwaysPlayCommercials = value;
					if (value) {
						this.resetCommercialsShow();
					}
				}
				break;
			case "bookmarks":
				this._destroyBookmarks();
				options.bookmarks = value;
				this._renderBookmarks();
				break;
			case "relatedVideos":
				this._destroyRelatedVideos();
				options.relatedVideos = value;
				this._renderRelatedVideos();
				break;
			default:
				break;
			}
			$.Widget.prototype._setOption.apply(this, arguments);
		},

		_onVideoStateChange: function () {
			var video = this.currentVideo,
				videoElem = video[ 0 ],
				o = this.options,
				prevReadyState = this._prevReadyState,
				readyState = videoElem.readyState;

			/*if (prevReadyState === videoElem.HAVE_NOTHING &&
			   readyState === videoElem.HAVE_METADATA) {
				loaded metadata
			} else if (prevReadyState === videoElem.HAVE_METADATA &&
					  readyState >= videoElem.HAVE_CURRENT_DATA) {
				loaded data
			} else */
			if (prevReadyState >= videoElem.HAVE_FUTURE_DATA && readyState <= videoElem.HAVE_CURRENT_DATA) {
				/* waiting for data */
				if (!videoElem.ended) {
					this._showWaitingIndicator();
				}
			} else if (prevReadyState <= videoElem.HAVE_CURRENT_DATA &&
				readyState === videoElem.HAVE_FUTURE_DATA) {
				/* can continue play(canplay) */
				this._hideWaitingIndicator();
			} else if (readyState === videoElem.HAVE_ENOUGH_DATA) {
				if (prevReadyState <= videoElem.HAVE_CURRENT_DATA) {
					/* canplay for first time. */
					this._hideWaitingIndicator();
				} else {

					// K.D. April 5th, 2012 Bug #108111 In Webkit the state changed continues firing after the video ends
					// The result is that the related videos are rendered and then removed immediately.
					return;
				}
			}

			this._prevReadyState = readyState;

			this._detectBuffered();

			/* if (videoElem.controls !== this._lastControlsState) {
				this._setOption("browserControls", videoElem.controls); TODO - react to video context menu!
			}*/
			/* Handle the case when play is invoked by browser context menu! */
			if (this._lastPausedState !== videoElem.paused) {
				if (this._lastPausedState) {
					this._showCenterPlayButton().removeClass(this.css.centerPauseButtonClass)
					.addClass(this.css.centerPlayButtonClass);
				} else {
					this._showCenterPlayButton().removeClass(this.css.centerPlayButtonClass)
					.addClass(this.css.centerPauseButtonClass);
				}
				setTimeout($.proxy(this._hideCenterPlayButton, this), o.centerButtonHideDelay);
				this._prepareForPlay();
				this._lastPausedState = videoElem.paused;
			}

			this._setOption("muted", videoElem.muted);

			this._refreshDuration();

			this._onVideoStateChangeId =
			setTimeout($.proxy(this._onVideoStateChange, this), this._const.VIDEO_STATE_TIMEOUT);
		},

		_updateTitleControlsTimeString: function (timeString, title) {
			if (this._activeControlsIndex === 1 && timeString) {
				/* if we are in inital mode update the duration */
				if (!title) {
					title = timeString;
				}
				$("span.ui-igplayer-playbutton-text", $("#" + this._id("_title_ctrls")))
				.attr("title", title).html(timeString);
			}
		},

		_analyzeSource: function (video, doNotShowError) {
			var detected = false;

			if (this.supportsH264BaselineVideo() && video.data("mp4") !== undefined) {
				video.attr("src", video.data("mp4"));
				detected = true;

			// N.A. Aug 22th, 2013 Bug #145499 VideoPlayer is not playing WebM format
			} else if (this.supportsWebmVideo() && video.data("webm") !== undefined) {
				video.attr("src", video.data("webm"));
				detected = true;
			} else if (this.supportsOggTheoraVideo()) {
				if (video.data("ogv") !== undefined) {
					video.attr("src", video.data("ogv"));
					detected = true;
				} else if (video.data("ogg") !== undefined) {
					video.attr("src", video.data("ogg"));
					detected = true;
				}
			}

			video.data("sourceDetected", detected);

			if (!detected && !doNotShowError) {
				this._showUnsupportedVideoSourceMsg();
				if (!this.options.browserControls) {
					/* disable controls that invoke play */
					if (this._activeControlsIndex === 0) {
						$("#" + this._id("_ctrls_play")).igButton("option", "disabled", true);
					}/* else if (this.container.data("activeControlsIndex") === 1) {
						title controls
					}*/
				}
			} else {
				this._hideUnsupportedVideoSourceMsg();
				/* enable controls that invoke play */
				if (!this.options.browserControls) {
					if (this._activeControlsIndex === 0) {
						/* normal controls */
						$("#" + this._id("_ctrls_play")).igButton("option", "disabled", false);
					}/* else if (this.container.data("activeControlsIndex") === 1) {
						title controls
					}*/
				}
			}
		},

		resetCommercialsShow: function () {
			/* Resets the commercials, to be shown again. */
			this._buildCommercialsShow();
			this._embeddedCommercialsShow = $.extend(true, [], this.options.commercials.embeddedCommercials);
			this._embeddedCommercialIndex = 0;
		},

		_attachVideoEvents: function (video) {
			var control = this;
			this._videoEvents = {
				error: function (event) {
					control._onVideoError(event);
				},
				progress: function (event) {
					control._onVideoLoading(event);
				},
				waiting: function (event) {
					control._onVideoWaiting(event);
					control._waiting(this.currentSrc, this.currentTime, this.duration, event);
				},
				seeking: function () {
					control._showWaitingIndicator();
				},
				seeked: function () {
					control._hideWaitingIndicator();
				},
				timeupdate: function (event) {
					control._changeCurrentTime(event);
				},
				ended: function (event) {
					/* Display big play button in the center when ended. */
					control._showCenterPlayButton().removeClass("ui-igplayer-centerplaybutton-pause")
					.addClass("ui-igplayer-centerplaybutton-play");
					control._hideWaitingIndicator();
					/* Update controls play button. */
					if (!control.options.browserControls) {
						$("span", $("#" + control._id("_ctrls_play")))
						.addClass("ui-icon-play")
						.removeClass("ui-icon-pause");
					}
					$("#" + control._id("_ctrls_pb")).igProgressBar("option", "value", 0);
					if (control.options.relatedVideos.length > 0) {
						control._onControlMouseOut(null);
						$("#" + control._id("_rvcc")).show();
					}
					control._buildBannersShow();
					if (control.options.commercials.alwaysPlayCommercials) {
						control.resetCommercialsShow();
					}
					control._ended(this.currentSrc, this.duration, event);
				},
				playing: function (event) {
					control._hideWaitingIndicator();
					$("span", $("#" + control._id("_ctrls_play")))
					    .addClass("ui-icon-pause")
					    .removeClass("ui-icon-play");
					control._playing(this.currentSrc, this.duration, event);
				},
				pause: function (event) {
					$("span", $("#" + control._id("_ctrls_play")))
					    .addClass("ui-icon-play")
					    .removeClass("ui-icon-pause");
					control._paused(this.currentSrc, this.duration, event);
				},
				click: function (event) {
					var o = control.options;
					if (!o.browserControls) {
						event.preventDefault();
					}
					if (event.button === 0 && !o.disabled) {
						/* 18-FEB-2011 66594 Limitation: Do not toggle video when browser controls are used.
						 * The problem is that clicking on play button of the browser controls fires click for the video
						 * and we cannot determine the actual target of the click. This causes the state of the video to not change.
						 */
						control._onVideoClick(!o.browserControls);
					}
				},
				dblclick: function (event) {
					event.preventDefault();
					if (event.button === 0 && !control.options.disabled) {
						control._onVideoDblClick();
					}
				},
				selectstart: function () {
					return false;
				},

				// N.A. July 18th, 2013 Bug #140860 In Chrome meta data loading is slower, that's we ensure it's loaded before requesting video duration property.
				loadedmetadata: function () {
					control._refreshDuration();
				}
			};
			video.bind(this._videoEvents);
		},

		_onVideoClick: function (allowPlay) {
			if (this._embeddedCommercialPlaying) {
				var link = this.options.commercials.embeddedCommercials[ this._embeddedCommercialIndex ].link;
				if (link && link.length > 0) {
					window.open(link, "_blank");
				}
			} else if (allowPlay) {
				this.togglePlay();
			}
		},

		_onVideoDblClick: function () {
			var o = this.options;
			this._setOption("fullscreen", !o.fullscreen);
			if (!o.browserControls) {
				this._toggleCenterPlayButton();
			}
		},

		_attachEvents: function (video) {
			var control = this;

			this._attachVideoEvents(video);

			this._generalEvts = {
				keydown: function (event) {
					if (event.keyCode === $.ui.keyCode.ESCAPE) {
						if (control.options.fullscreen) {
							control._setOption("fullscreen", false);
							event.preventDefault();
							event.stopPropagation();
						}
					} else if (event.keyCode === $.ui.keyCode.SPACE) {
						event.preventDefault();
						event.stopPropagation();
						control.togglePlay();
					}
				}
			};

			this._documentEvts = {
				contextmenu: function (e) {
					if ($(e.target).is("video") && control.options.disabled) {
						e.preventDefault();
						e.stopPropagation();
					}
				}
			};

			$(document).bind(this._documentEvts);

			if (!this.options.browserControls) {
				this._controlsEvts = {
					mouseover: function (event) {
						control._onControlMouseOver(event);
					},
					mouseout: function (event) {
						control._onControlMouseOut(event);
					}
				};
				this.container.bind(this._controlsEvts);
			}
			this.container.bind(this._generalEvts);
		},

		_handleKbNavigation: function (event) {
			if (event.keyCode === $.ui.keyCode.HOME) {
				$(this.controlButtons[ 0 ]).focus();
				event.stopPropagation();
				event.preventDefault();
			} else if (event.keyCode === $.ui.keyCode.END) {
				$(this.controlButtons[ this.controlButtons.length - 1 ]).focus();
				event.stopPropagation();
				event.preventDefault();
			}
		},

		_handleBlurKb: function () {
			this.currentVideo.removeClass(this.css.activeVideoClass);
		},

		_handleFocusKb: function () {
			this.currentVideo.addClass(this.css.activeVideoClass);
		},

		_onVideoLoading: function (event) {
			this._detectBuffered(event);
		},

		_detectBuffered: function (event) {
			var videoElem = this.currentVideo[ 0 ],
				buffered = videoElem.buffered,
				duration = parseInt(videoElem.duration, 10),
				len = buffered ? buffered.length : 0,
				firstBuffered = len > 0 ? parseInt(buffered.start(len - 1), 10) : 0,
				lastBuffered = len > 0 ? parseInt(buffered.end(len - 1), 10) : 0,
				bufferSize = (lastBuffered / duration) * 100;

			// K.D. May 31st, 2011 Bug #77359 Buffered is fired constantly because the previously buffered value
			// is never kept and checked against
			if (len > 0 && this._previouslyBuffered !== bufferSize) {

				// S.S. November 24th, 2011 Bug #75937 When the video starts again with loop the new buffered area is before the last one
				// so if the value is not reset the old one is not refreshed
				if (this._previousBuffer && lastBuffered < parseInt(this._previousBuffer.end(0), 10)) {
					$("#" + this._id("_ctrls_pb")).igProgressBar("option", "value", firstBuffered);
				}
				$("#" + this._id("_ctrls_pb"))
					.igProgressBar("option", "endValue", (lastBuffered / duration) * 100);
				this._buffering(videoElem.currentSrc, bufferSize, event);
				this._previouslyBuffered = bufferSize;
				this._previousBuffer = buffered;
			}
		},

		_getActiveControls: function () {
			var ai = this._activeControlsIndex;
			if (ai !== undefined && this.controls && ai >= 0 && ai < this.controls.length) {
				return this.controls[ ai ].parent();
			}
			return null;
		},

		_onControlMouseOver: function () {
			var controlsDiv = this._getActiveControls(),
				o = this.options;
			if (controlsDiv && !o.browserControls && o.autohide &&
				 !$("#" + this._id("_rvcc")).is(":visible")) {
				controlsDiv.show().removeClass(this.css.controlsHideClass);
			}
		},

		_onControlMouseOut: function () {
			var o = this.options,
				controlsDiv = this._getActiveControls();
			if (controlsDiv && !o.browserControls && o.autohide) {
				controlsDiv.addClass(this.css.controlsHideClass).hide();
			}
		},

		_onVideoError: function (e) {
			var error = e.target.error;
			switch (error.code) {
			case error.MEDIA_ERR_ABORTED:
				/* user aborted the plying. */
				break;
			case error.MEDIA_ERR_NETWORK:
				/* try again to play the video. */
				this.play();
				break;
			case error.MEDIA_ERR_DECODE:
			case error.MEDIA_ERR_SRC_NOT_SUPPORTED:
				/* run the analyze algorithm again. */
				this._showUnsupportedVideoSourceMsg();
				break;
			default:
				break;
			}
		},

		_changeCurrentTime: function (event) {
			var videoElem = this.currentVideo[ 0 ],
				rawDuration = videoElem.duration,
				currentTime;

			if (rawDuration === Infinity) {
				/* live stream */
				this._updateProgressLabel();
			} else if (!isNaN(rawDuration)) {
				currentTime = parseInt(videoElem.currentTime, 10);
				if (!this._userSliding) {
					$("#" + this._id("_ctrls_s")).igSlider("option", "value", (currentTime / rawDuration) * 100);
				}
				this._updateProgressLabel();

				if (this._embeddedCommercialPlaying) {
					this._updateAdMessage(this.options.commercials
						.embeddedCommercials[ this._embeddedCommercialIndex ].endTime - currentTime);
					this._checkEmbeddedCommercialShow(currentTime);
				} else if (!this._commercialPlaying) {
					this._checkBannerShow(currentTime);
					this._checkCommercialsShow(currentTime);
					this._checkEmbeddedCommercialShow(currentTime);
				} else {
					this._updateAdMessage(rawDuration - currentTime);
				}
			}
			this._progress(videoElem.currentSrc, currentTime, rawDuration, event);
		},

		_checkBannerShow: function (currentTime) {
			var show = this._bannerShow || [],
				len = show.length,
				i = 0;
			for (i; i < len; i++) {
				if (show[ i ].length > 0 && currentTime >= show[ i ][ 0 ]) {
					show[ i ].shift();
					this.showBanner(i);
				}
			}
		},

		_checkCommercialsShow: function (currentTime) {
			if (this._commercialsShow && this._commercialsShow.length > 0 &&
					currentTime >= this._commercialsShow[ 0 ] && !this._commercialPlaying) {
				this._commercialPlaying = true;
				if (currentTime - this._commercialsShow[ 0 ] > this._const.COMMERCIAL_SEEK_DELTA) {
					this.currentTime(this._commercialsShow[ 0 ]);
				}
				this.playCommercial(this.options.commercials.linkedCommercials[ this._commercialIndex ]);
			}
		},

		_checkEmbeddedCommercialShow: function (currentTime) {
			if (this._embeddedCommercialsShow && this._embeddedCommercialsShow.length > 0 &&
					!this.currentVideo[ 0 ].seeking) {
				var currentCommercial = this._embeddedCommercialsShow[ 0 ];
				if (currentTime >= currentCommercial.startTime && !this._embeddedCommercialPlaying) {
					this._embeddedCommercialPlaying = true;
					if (currentTime - currentCommercial.startTime > this._const.COMMERCIAL_SEEK_DELTA) {
						this.currentTime(currentCommercial.startTime);
					}
					$("#" + this._id("_ctrls_s")).igSlider("option", "disabled", true);
					this._showAdMessage(currentCommercial.endTime - currentCommercial.startTime);
					if (currentCommercial.link && currentCommercial.link.length > 0) {
						this.currentVideo.attr("title", $.ig.VideoPlayer.locale.adNewWindowTip);
					}
				} else if (currentTime >= currentCommercial.endTime && this._embeddedCommercialPlaying) {
					this._embeddedCommercialsShow.shift();
					this._embeddedCommercialIndex++;
					this._embeddedCommercialPlaying = false;
					this.hideAdMessage();
					$("#" + this._id("_ctrls_s")).igSlider("option", "disabled", false);
					if (currentCommercial.link && currentCommercial.link.length > 0) {
						this.currentVideo.removeAttr("title");
					}
				}
			}
		},

		_updateProgressLabel: function () {
			var videoElem = this.currentVideo[ 0 ],
				progressLabel = $("#" + this._id("_ctrls_pl")),
				rawDuration = videoElem.duration,
				duration = parseInt(rawDuration, 10),
				currentTime = parseInt(videoElem.currentTime, 10),
				progressString = this.options.progressLabelFormat;
			progressString = progressString
				.replace("${currentTime}", this._toTimeString(currentTime))
					.replace("${duration}", (rawDuration === Infinity ?
															$.ig.VideoPlayer.locale.liveStream :
															this._toTimeString(duration)));
			progressLabel.html(progressString);
		},

		_toTimeString: function (val) {
			/* val is assumed to be always seconds, because video.currentTime is always in seconds. */
			if (typeof val === "number") {
				var secs = parseInt(val % 60, 10),
					mins = parseInt(val / 60, 10),
					hours = parseInt(mins / 60, 10),
					days = parseInt(hours / 24, 10),
					timeFormat = "{0}d {1}:{2}:{3}";
				mins = parseInt(mins % 60, 10);
				hours = parseInt(hours % 60, 10);
				timeFormat = timeFormat.replace("{3}", (secs < 10 ? "0" + secs : secs)).replace("{2}", mins);
				if (hours > 0) {
					timeFormat = timeFormat.replace("{1}", hours);
					if (days > 0) {
						timeFormat = timeFormat.replace("{0}", days);
					} else {
						timeFormat = timeFormat.replace("{0}d ", "");
					}
				} else {
					timeFormat = timeFormat.replace("{0}d {1}:", "");
				}
				return timeFormat;
			}
			return "NaN";
		},

		_onVideoWaiting: function () {
			this._showWaitingIndicator();
		},

		_createUnsupportedVideoSourceMsg: function () {
			var css = this.css;
			return this._createButton(this._id("_detectError"),
									  css.unsupportedVideoSourceClass,
									  css.unsupportedVideoSourceIconClass,
									  $.ig.VideoPlayer.locale.missingVideoSource)
										.attr("title", $.ig.VideoPlayer.locale.unsupportedVideoSource);
		},

		_showUnsupportedVideoSourceMsg: function () {
			var error = $("#" + this._id("_detectError"));

			if (error.length === 0) {
				error = this._createUnsupportedVideoSourceMsg();
			}

			return error.show();
		},

		_hideUnsupportedVideoSourceMsg: function () {
			$("#" + this._id("_detectError")).hide();
		},

		_createBigPlayButton: function () {
			var control = this, css = this.css;

			this._createButton(this._id("_play"),
							   css.centerPlayButtonClass,
							   css.centerPlayButtonIconClass,
							   "").bind({
				click: function (event) {
					event.preventDefault();
					if (event.button === 0) {
						control._onVideoClick(true);
					}
				},
				dblclick: function (event) {
					event.preventDefault();
					if (event.button === 0) {
						control._onVideoDblClick();
					}
				}
			});
		},

		_createButton: function (buttonId, buttonClass, spanClass, text) {
			$("<span></span>").html(text).addClass(spanClass)
				.appendTo($("<a></a>").attr("id", buttonId)
				.attr("href", this._const.HREF).attr("tabIndex", -1)
				.addClass("ui-state-default").addClass(buttonClass)
				.appendTo(this.container));
			return $("#" + buttonId).bind({
				mouseover: function () {
					$(this).addClass("ui-state-hover");
				},
				mouseout: function () {
					$(this).removeClass("ui-state-hover");
				}
			}).hide();
		},

		_showCenterPlayButton: function () {
			var video = this.currentVideo,
				button = $("#" + this._id("_play")),

				// K.D. May 31st, 2011 Bug #74395 The center play button is absolutely positioned inside the video container
				// and thus when positioning it we shouldn't include the video offset in the calculation
				videoCenterTop  = Math.floor(video.height() / 2),
				videoCenterLeft = Math.floor(video.width() / 2),
				top = videoCenterTop - Math.floor(button.innerHeight() / 2),
				left = videoCenterLeft - Math.floor(button.innerWidth() / 2);
			button.css("position", "absolute").css("left", left).css("top", top);
			return button.show();
		},

		_toggleCenterPlayButton: function () {
			var videoElem = this.currentVideo[ 0 ],
				o = this.options,
				css = this.css;
			if (videoElem.paused || videoElem.ended) {
				/* from paused state to playing state. */
				this._showCenterPlayButton()
					.removeClass(css.centerPauseButtonClass)
					.addClass(css.centerPlayButtonClass);
			} else {
				/* from playing state to paused state. */
				this._showCenterPlayButton()
					.removeClass(css.centerPlayButtonClass)
					.addClass(css.centerPauseButtonClass);
			}
			setTimeout($.proxy(this._hideCenterPlayButton, this), o.centerButtonHideDelay);
		},

		_hideCenterPlayButton: function () {
			return $("#" + this._id("_play")).hide();
		},

		_createWaitingIndicator: function () {
			var css = this.css;
			this._createButton(this._id("_waiting"),
							   css.waitingIndicatorClass,
							   css.waitingIndicatorIconClass,
							   $.ig.VideoPlayer.locale.buffering);
		},

		_createSeekTimeToolTip: function () {
			$("<div></div>").attr("id", this._id("_seek_tooltip")).hide().igTooltip({
				text: "00:00",
				arrowLocation: "bottom"
			}).appendTo(this.container).addClass(this.css.seekTooltipClass);
		},

		_showSeekTimeToolTip: function (x, y, seconds) {
			var toolTip = $("#" + this._id("_seek_tooltip"));

			toolTip.css("top", (y - toolTip.outerHeight()))
				.css("left", (x - (toolTip.width() / 2)))
				.igTooltip("option", "text", this._toTimeString(seconds))
				.show();
		},

		_showWaitingIndicator: function () {
			if ($("#" + this._id("_detectError")).is(":visible")) {
				return;
			}
			var video = this.currentVideo,
				button = $("#" + this._id("_waiting")),

				// K.D. May 31st, 2011 Bug #74395 The waiting indicator is absolutely positioned inside the video container
				// and thus when positioning it we shouldn't include the video offset in the calculation (same as big central button)
				videoCenterTop  = Math.floor(video.height() / 2),
				videoCenterLeft = Math.floor(video.width() / 2),
				top = videoCenterTop - Math.floor(button.innerHeight() / 2),
				left = videoCenterLeft - Math.floor(button.innerWidth() / 2);
			button.css("position", "absolute").css("left", left).css("top", top).show();
		},

		_hideWaitingIndicator: function () {
			$("#" + this._id("_waiting")).hide();
		},

		_renderTitleControls: function () {
			var title = this.options.title,
				control = this,
				controlsDiv,
				html = '<div id="' + this._id("_title_ctrls") +
						'" class="ui-widget-header ui-igplayer-controls ' +
						'ui-igplayer-grid ui-igplayer-title-controls ui-corner-all">';
			html += '<div class="ui-igplayer-row">';
			html += '<div class="ui-igplayer-container ui-igplayer-cell ui-igplayer-playback-alone">';
			html += '		<a id="' + this._id("_title_ctrls_play") +
							'" class="ui-button ui-igplayer-playbutton ui-igplayer-playback-with-time ' +
							'ui-state-default ui-corner-all ui-priority-primary" href="' +
							this._const.HREF + '">';
			html += '			<span title="' + $.ig.VideoPlayer.locale.play +
								'" class="ui-icon ui-icon-play ui-igplayer-playbutton-icon"></span>';
			html += '			<span title="00:00" class="ui-button-text ui-igplayer-playbutton-text">00:00</span>';
			html += "		</a>";
			html += "</div>";
			html += '<div id="' + this._id("_title_ctrls_t") +
					'" class="ui-igplayer-container ui-igplayer-cell ui-igplayer-video-title">';
			if (title && title.length > 0) {
				html += title;
			}
			html += "</div></div></div>";

			controlsDiv = $(html).appendTo(this.container)
								.wrap('<div style="position:absolute; width: 100%;"></div>');
			if (this.options.width) {
				controlsDiv.parent().css("width", this.options.width);
			}

			$("#" + this._id("_title_ctrls_play")).bind({
				click: function (event) {
					event.preventDefault();
					if (event.button === 0 && !control.options.disabled) {
						control.togglePlay();
					}
				}
			});

			return controlsDiv;
		},

		_hideTitleControls: function () {
			if (this._activeControlsIndex !== 0) {
				this._activeControlsIndex = 0;
				$("#" + this._id("_title_ctrls"))
					.addClass(this.css.controlsHideClass)
					.parent()
					.remove();
				$("#" + this._id("_ctrls")).parent().show();
			}
		},

		_convertBookmarks: function (prop, bookmarks, duration, css) {
			/* Converts the list of bookmarks to slider bookmarks. */
			var len = bookmarks.length,
				newBookmarks = [],
				i = 0,
				mark,
				newMark,
				value;
			for (i; i < len; i++) {
				mark = bookmarks[ i ];
				if (mark[ prop ] > 0 && mark[ prop ] < duration) {
					value = parseInt((mark[ prop ] / duration) * 100, 10);
					newMark = {
						value: value,
						secondsValue: mark[ prop ],
						displayTime: this._toTimeString(mark[ prop ]),
						index: i,
						title: mark.title,
						disabled: mark.disabled
					};

					if (css && css.length > 0) {
						newMark.css = css;
					}

					newBookmarks.push(newMark);
				}
			}
			return newBookmarks;
		},

		_renderBookmarks: function () {
			var o = this.options,
				bookmarks = o.bookmarks || [],
				com = o.commercials,
				ads = com.linkedCommercials || [],
				eads = com.embeddedCommercials || [],
				duration = this.currentVideo[ 0 ].duration,
				newBookmarks = [],
				newAdBookmarks = [],
				newEAdBookmarks = [];

			if (com.showBookmarks) {
				if (ads.length > 0) {
					newAdBookmarks =
						this._convertBookmarks("startTime", ads, duration, this.css.linkedBookmarkClass);
				}

				if (eads.length > 0) {
					newEAdBookmarks =
						this._convertBookmarks("startTime", eads, duration, this.css.adBookmarkClass);
				}
			}

			if (bookmarks.length > 0) {
				/* transform the video bookmarks value to slider value in range 0 to 100. */
				newBookmarks = this._convertBookmarks("time", bookmarks, duration);

				if (newBookmarks.length > 0) {
					this._renderBookmarkArea(newBookmarks);
				}
			}

			/* newBookmarks is destroyed to contain the merge result! */
			if (newAdBookmarks.length > 0) {
				$.merge(newBookmarks, newAdBookmarks);
			}
			if (newEAdBookmarks.length > 0) {
				$.merge(newBookmarks, newEAdBookmarks);
			}

			if (newBookmarks.length > 0) {
				$("#" + this._id("_ctrls_s")).igSlider("option", "bookmarks", newBookmarks);
			}

			this._bookmarksRendered = true;
		},

		_renderBookmarkArea: function (bookmarks) {
			if (bookmarks && bookmarks.length > 0) {
				var css = this.css,
					bookmarkStr = '<li class="ui-corner-all $class$"><span class="' +
							css.bookmarkItemTimeClass + '">$time$</span><span class="' +
							css.bookmarkItemTitleClass + '" title="$titleAttr$">$title$</span></li>',
					bookmarkAreaStr = '<div id="$id$" class="$bookmarkContainerClass$">' +
									'<div class="$headerClass$">$headerText$</div>' +
									'<ul class="$bookmarkListClass$">$list$</ul></div>',
					bookmarkList = "",
					bookmarkArea,
					mark,
					i = 0,
					len = bookmarks.length,
					control = this;

				for (i; i < len; i++) {
					mark = bookmarks[ i ];
					bookmarkList += bookmarkStr.replace("$time$", mark.displayTime)
						.replace("$titleAttr$", mark.title)
						.replace("$title$", mark.title)
						.replace("$class$", mark.disabled ? css.bookmarkItemDisabledClass : "");
				}

				bookmarkAreaStr = bookmarkAreaStr.replace("$id$", this._id("_bookmarks"))
					.replace("$bookmarkContainerClass$", css.bookmarkContainerClass)
					.replace("$headerClass$", css.bookmarkContainerHeaderClass)
					.replace("$bookmarkListClass$", css.bookmarkListClass)
					.replace("$headerText$", $.ig.VideoPlayer.locale.skipTo)
					.replace("$list$", bookmarkList);

				bookmarkArea = $(bookmarkAreaStr).appendTo(this.container);

				this.bookmarkElements = $("li", $("#" + this._id("_bookmarks")));

				this.bookmarkElements.each(function (i) {
					$(this).data("bookmark-index", bookmarks[ i ].index); /* the index according to options.bookmarks. */
					$(this).data("bookmark-sec-value", bookmarks[ i ].secondsValue);
				});

				this.bookmarkElements.bind({
					click: function (event) {
						var index = $(this).data("bookmark-index"),
							mark = control.options.bookmarks[ index ];
						event.stopPropagation();
						event.preventDefault();
						if (!mark.disabled) {
							if (control.paused()) {
								control.togglePlay();
							}
							control.currentTime($(this).data("bookmark-sec-value"));
							control._changeActiveBookmark(index);

							// K.D. June 1st, 2011 Bookmark use should be unified and not referred from the slider
							// because in the case of browser controls the slider doesn't exist and the bookmark
							// pins are not available as object.
							control._bookmarkClick(mark, bookmarks[ index ], event);
						}
					}
				});
				if (this.options.height) {
					this.currentVideo.css("height", this.container.height() - bookmarkArea.height());
					this._resizeBookmarkAreaTimeoutId = setTimeout($.proxy(this._onPlayerResize, this), 500);
				}
			}
		},

		_onPlayerResize: function () {
			var o = this.options,
				containerH = this.container.height(),
				containerW = this.container.width(),
				bookmarkArea = $("#" + this._id("_bookmarks"));
			if ((o.height || o.width) &&
				(this._oldContainerHeight !== containerH ||
				this._oldContainerWidth !== containerW)) {

				// control has been resized, we have to resize the bookmark are and video container.
				this.currentVideo.css("height", containerH - bookmarkArea.height());
				this._oldContainerHeight = containerH;
				this._oldContainerWidth = containerW;
			}
			this._resizeBookmarkAreaTimeoutId = setTimeout($.proxy(this._onPlayerResize, this), 250);
		},

		// S.S. June 24th, 2011 Bug #74601 Before rendering our controls we need to be sure the correct
		// volume is set for the widget to display in the volume slider.
		_ensureVolume: function () {
			this.options.volume = this.currentVideo[ 0 ].volume;
		},

		_renderControls: function () {
			$("body").addClass("ui-igplayer-normal-screen-mode");

			if (this.options.showSeekTime) {
				this._createSeekTimeToolTip();
			}

			var videoElem = this.currentVideo[ 0 ],
				control = this,
				controlsDiv,
				controlsArray = [],
				autoPlay = this.options.autoplay,
				href = this._const.HREF,
				html = '<div id="' + this._id("_ctrls") + '" class="$controlsClass$">',
				cachedTime;
			html += '			<div class="ui-igplayer-row">';
			html += '				<div class="ui-igplayer-container ui-igplayer-cell ui-igplayer-playback">';
			html += '					<a id="$playButtonId$" class="$playButtonClass$" href="' + href + '"></a>';
			html += "				</div>";

			html += '				<div class="ui-igplayer-container ui-igplayer-cell ui-igplayer-progress-bar">';
			html += '					<div id="$progressBarId$" class="$progressBarClass$"></div>';
			html += "				</div>";

			html += '				<div class="ui-igplayer-container ui-igplayer-extra">';
			html += '					<div class="ui-igplayer-grid">';
			html += '						<div class="ui-igplayer-row">';
			html += '							<div class="ui-igplayer-cell">';
			html += '								<a id="$progressLabelId$" class="$progressLabelClass$" href="' +
													href + '"></a>';
			html += "							</div>";

			html += '							<div class="ui-igplayer-cell">';
			html += "								<div>";
			html += '									<div id="$volumeSliderId$" class="$volumeSliderClass$"></div>';
			html += "									<div>";
			html += '										<a id="$volumeControlId$" class="$volumeControlClass$" href="' +
															href + '"></a>';
			html += "									</div>";
			html += "								</div>";
			html += "							</div>";
			html += '							<div class="ui-igplayer-cell">';
			html += '								<a id="$fullScreenId$" class="$fullScreenClass$" href="' + href + '"></a>';
			html += "							</div>";
			html += "						</div>";
			html += "					</div>";
			html += "				</div>";
			html += "			</div>";
			html += "		</div>";

			html = html.replace("$playButtonClass$", this.css.playButtonClass)
						.replace("$progressBarClass$", this.css.progressBarClass)
						.replace("$fullScreenClass$", this.css.fullScreenClass)
						.replace("$controlsClass$", this.css.controlsClass)
						.replace("$volumeControlClass$", this.css.volumeControlClass)
						.replace("$volumeSliderClass$", this.css.volumeSliderClass)
						.replace("$progressLabelClass$", this.css.progressLabelClass);

			html = html.replace("$playButtonId$", this._id("_ctrls_play"))
						.replace("$progressBarId$", this._id("_ctrls_s"))
						.replace("$fullScreenId$", this._id("_ctrls_fs_btn"))
						.replace("$volumeControlId$", this._id("_ctrls_vc_btn"))
						.replace("$volumeSliderId$", this._id("_ctrls_vs"))
						.replace("$progressLabelId$", this._id("_ctrls_pl"));

			controlsDiv = $(html).appendTo(this.container)
								.wrap('<div style="position:absolute; width: 100%;"></div>');
			if (this.options.width) {
				controlsDiv.parent().css("width", this.options.width);
			}

			controlsArray.push(controlsDiv);

			if (!autoPlay && videoElem.currentTime === 0) {
				controlsDiv.parent().hide();
				this._activeControlsIndex = 1;
				controlsArray.push(this._renderTitleControls());
			} else {
				this._activeControlsIndex = 0;
			}

			this.controls = $(controlsArray);

			/* bind events */
			$("#" + this._id("_ctrls_play")).bind({
				click: function (event) {
					event.preventDefault();
					if (!$(this).igButton("option", "disabled")) {
						control.togglePlay();
					}
				}
			}).igButton({ link: { href: this._const.HREF },
						  css: { buttonLabelClass: "ui-icon " +
						  (autoPlay ? "ui-icon-pause " : "ui-icon-play ") + "ui-igplayer-playbutton-icon " }
						}).addClass("ui-priority-primary")
				.attr("title", (autoPlay ? $.ig.VideoPlayer.locale.playing : $.ig.VideoPlayer.locale.paused));

			$("#" + this._id("_ctrls_fs_btn")).bind({
				click: function (event) {
					event.preventDefault();
					control._setOption("fullscreen", !control.options.fullscreen);
				}
			}).igButton({ link: { href: this._const.HREF },
						  css: { buttonLabelClass: this.css.fullScreenIconClass }
						}).attr("title", $.ig.VideoPlayer.locale.enterFullscreen);

			$('<div id="' + this._id("_ctrls_pb") + '"></div>').appendTo(
				$("#" + this._id("_ctrls_s")).igSlider({
					slide: function (event, ui) {
						if (event.originalEvent === undefined) {
							return false;
						}
						clearTimeout(control._slideTimeout);
						control._slideTimeout =
							setTimeout(control._createDelegate(control, control._slide, [ ui ]),
										control._const.SLIDE_SEEK_TIMEOUT);
					},
					start: function () {
						control._userSliding = true;
					},
					stop: function () {
						control._userSliding = false;
					},
					bookmarkhit: function (event, ui) {
						var index = ui.bookmark.index,
							elems = control.bookmarkElements;
						if (elems && elems.length > 0) {
							control._changeActiveBookmark(index);
							control._bookmarkHit(control.currentVideo[ 0 ].currentSrc,
												 control.options.bookmarks[ index ],
												 elems[ index ], event);
						}
					},
					bookmarkclick: function (event, ui) {
						var mark;
						if ($(ui.bookmarkElement).hasClass("ui-igplayer-linked-bookmark")) {
							mark = control.options.commercials.linkedCommercials[ ui.bookmark.index ];
						} else if ($(ui.bookmarkElement).hasClass("ui-igplayer-ad-bookmark")) {
							mark = control.options.commercials.embeddedCommercials[ ui.bookmark.index ];
						} else {
							mark = control.options.bookmarks[ ui.bookmark.index ];
							control._changeActiveBookmark(ui.bookmark.index);
						}
						if (mark.time) {
							cachedTime = mark.time;
						} else {
							cachedTime = mark.startTime;
						}
						control.currentTime(cachedTime);
						control._bookmarkClick(mark, ui.bookmarkElement, event);
						return false;
					},
					min: 0,
					max: 100,
					animate: true
				}).css("display", "block")
			).igProgressBar({
				range: true,
				endValue: 0
			});

			$("#" + this._id("_ctrls_pb")).bind({
				mousemove: function (e) {
					if (control.options.showSeekTime && this.offsetWidth > 0) {
						var relativeX = e.pageX - this.offsetLeft - $(this).offset().left,
							percentLocation = parseInt((relativeX / this.offsetWidth) * 100, 10),
							duration = videoElem.duration,
							seconds,
							controlOffset = control.container.offset();
						if (!isNaN(duration) && duration !== Infinity && !control._commercialPlaying) {
							seconds = parseInt((percentLocation * duration) / 100, 10);
							control._showSeekTimeToolTip(e.pageX - controlOffset.left,
														e.pageY - controlOffset.top, seconds);
						}
					}
				},
				mouseout: function () {
					$("#" + control._id("_seek_tooltip")).hide();
				}
			});

			$("#" + this._id("_ctrls_pl")).bind({
				click: function (event) {
					event.preventDefault();
				}
			});

			$("#" + this._id("_ctrls_vc_btn")).bind({
				mouseover: function () {
					control._showVolumeSlider();
				},
				mouseout: function (event) {
					if (!jQuery.contains(event.currentTarget, event.relatedTarget) &&
						event.currentTarget !== event.relatedTarget) {
						control._volumeSliderTimeoutId =
							setTimeout($.proxy(control._hideVolumeSlider, control),
										control.options.volumeAutohideDelay);
					}
				},
				click: function (event) {
					event.preventDefault();
					control._setOption("muted", !control.options.muted);
				}
			}).igButton({ link:{ href: this._const.HREF },
						css:{ buttonLabelClass: "ui-icon " +
							(this.options.muted ?
								"ui-icon-volume-off" :
								"ui-icon-volume-on") +
							" ui-igbutton-icon" }
						  }).attr("title", $.ig.VideoPlayer.locale.volume);

			$("#" + this._id("_ctrls_vs")).hide().bind({
				mouseover: function () {
					control._volumeSliderMouseOut = false;
					clearTimeout(control._volumeSliderTimeoutId);
				},
				mouseout: function () {
					if (!control._userSlidingVolume) {
						control._volumeSliderTimeoutId =
							setTimeout($.proxy(control._hideVolumeSlider, control),
									control.options.volumeAutohideDelay);
					}
					control._volumeSliderMouseOut = true;
				}
			}).igSlider({
				slide: function (event, ui) {
					if (event.originalEvent === undefined) {
						return; /* event was triggered programatically by changing slider.value. */
					}
					control._setOption("volume", parseFloat(parseInt(ui.value, 10) / 100));
				},
				start: function () {
					control._userSlidingVolume = true;
				},
				stop: function () {
					control._userSlidingVolume = false;
				},
				min: 0,
				max: 100,
				step: 10,
				orientation: "vertical",
				value: (this.options.muted ? 0 : this.options.volume * 100)
			});
			$("#" + this._id("_ctrls_vs")).data("igSlider").handle.attr("tabIndex", -1).bind({
				blur: function () {
					control._volumeSliderTimeoutId =
						setTimeout($.proxy(control._hideVolumeSlider, control), control.options.volumeAutohideDelay);
				}
			});

			this.controlButtons = $([ document.getElementById(this._id("_ctrls_play")),
									 $("a.ui-state-default", $("#" + this._id("_ctrls_s")))[ 0 ],
									 document.getElementById(this._id("_ctrls_vc_btn")),
									 document.getElementById(this._id("_ctrls_fs_btn"))
									]);
			this.controlButtons.each(function (i) {
				$(this).data("index.control-button", i);
			});
			this.controlButtons.bind({
				keydown: function (event) {
					control._handleKbNavigation(event);
				},
				focus: function (event) {
					control._handleFocusKb(event);
				},
				blur: function (event) {
					control._handleBlurKb(event);
				}
			});
			$("#" + this._id("_ctrls_vc_btn")).bind({
				focus: function () {
					control._showVolumeSlider();
				},
				blur: function () {
					if (!control._transferFocusFromVolumeButtonToVolumeSlider) {
						control._hideVolumeSlider();
					}
					control._transferFocusFromVolumeButtonToVolumeSlider = false;
				},
				keydown: function (event) {
					if (event.keyCode === $.ui.keyCode.TAB) {
						event.preventDefault();
						event.stopPropagation();
						control._transferFocusFromVolumeButtonToVolumeSlider = true;
						$("#" + control._id("_ctrls_vs")).data("igSlider").handle.focus();
					}
				}
			});
			$("#" + control._id("_ctrls_vs")).data("igSlider").handle.bind({
				keydown: function (event) {
					if (event.keyCode === $.ui.keyCode.TAB) {
						event.preventDefault();
						event.stopPropagation();
						$("#" + control._id("_ctrls_fs_btn")).focus();
					}
				}
			});
		},

		_slide: function (ui) {
			var value = ui.value,
				rawDuration = this.currentVideo[ 0 ].duration,
				currentTime = rawDuration * (value / 100);
			if (!isNaN(rawDuration) && rawDuration !== Infinity) {
				this.currentTime(currentTime);
			}
			this._userSliding = false;
		},

		_changeActiveBookmark: function (index) {
			var activeBookmarkIndex = this._activeBookmarkIndex,
				bkElems = this.bookmarkElements;

			if (bkElems && bkElems.length > 0 && index !== activeBookmarkIndex) {
				if (activeBookmarkIndex >= 0 && activeBookmarkIndex < bkElems.length) {
					$(bkElems[ activeBookmarkIndex ]).removeClass(this.css.bookmarkActiveItemClass);
				}
				if (index >= 0 && index < bkElems.length) {
					$(bkElems[ index ]).addClass(this.css.bookmarkActiveItemClass);
					this._activeBookmarkIndex = index;
				}
			}
		},

		_adjustVolumeButton: function (value) {
			var volumeButton = $("#" + this._id("_ctrls_vc_btn"));
			if (value === 0) {
				$("span", volumeButton)
					.removeClass("ui-icon-volume-on")
					.addClass("ui-icon-volume-off");
			} else {
				$("span", volumeButton)
					.removeClass("ui-icon-volume-off")
					.addClass("ui-icon-volume-on");
			}
		},

		_hideVolumeSlider: function () {
			var slider = $("#" + this._id("_ctrls_vs"));
			slider.data("igSlider").handle.attr("tabIndex", -1);
			slider.hide();
		},

		_showVolumeSlider: function () {
			clearTimeout(this._volumeSliderTimeoutId);
			var slider = $("#" + this._id("_ctrls_vs"));
			slider.data("igSlider").handle.removeAttr("tabIndex");
			slider.show();
		},

		_ended: function (source, duration, event) {
			/* fired when a video source has ended */
			var args = {
					source: source,
					duration: duration
				};
			return this._trigger(this.events.ended, event, args);
		},

		_playing: function (source, duration, event) {
			/* fired when a video source has resumed playing (after a pause) */
			var args = {
					source: source,
					duration: duration,
					currentTime: this.currentVideo[ 0 ].currentTime
				};
			return this._trigger(this.events.playing, event, args);
		},

		_paused: function (source, duration, event) {
			/* fired when video source has been paused. */
			var args = {
					source: source,
					duration: duration,
					currentTime: this.currentVideo[ 0 ].currentTime
				};
			return this._trigger(this.events.paused, event, args);
		},

		_buffering: function (source, bufferedPercent, event) {
			/* fired when a video source has more buffered data. */
			var args = {
					source: source,
					buffered: bufferedPercent
				};
			return this._trigger(this.events.buffering, event, args);
		},

		_progress: function (source, currentTime, duration, event) {
			/* fired when video source has changed to a new position. */
			var args = {
					source: source,
					currentTime: currentTime,
					duration: duration
				};
			return this._trigger(this.events.progress, event, args);
		},

		_waiting: function (source, currentTime, duration, event) {
			/* fired when the video source is waiting for data, to be download from the server. */
			var args = {
					source: source,
					currentTime: currentTime,
					duration: duration
				};
			return this._trigger(this.events.waiting, event, args);
		},

		_bookmarkHit: function (source, bookmark, bookmarkElement, event) {
			/* fired when a bookmark was hit by the playback. */
			var args = {
					source: source,
					bookmark: bookmark,
					bookmarkElement: bookmarkElement
				};
			return this._trigger(this.events.bookmarkHit, event, args);
		},

		_bookmarkClick: function (bookmark, bookmarkElement, event) {
			/* fired when a bookmark has been clicked by the user. */
			var args = {
					bookmark: bookmark,
					bookmarkElement: bookmarkElement
				};
			return this._trigger(this.events.bookmarkClick, event, args);
		},

		_enterFullScreen: function (source) {
			/* fired when the control is entering full screen */
			var args = {
					source: source
				};
			return this._trigger(this.events.enterFullScreen, null, args);
		},

		_exitFullScreen: function (source) {
			/* fired when the control is exitting full screen */
			var args = {
					source: source
				};
			return this._trigger(this.events.exitFullScreen, null, args);
		},

		_bannerVisible: function (index, element) {
			/* fired when the control banner is shown */
			var args = {
					index: index,
					banner: this.options.banners[ index ],
					bannerElement: element
				};
			return this._trigger(this.events.bannerVisible, null, args);
		},

		_bannerHidden: function (index, element) {
			/* fired when the control banner is hidden */
			var args = {
					index: index,
					banner: this.options.banners[ index ],
					bannerElement: element
				};
			return this._trigger(this.events.bannerHidden, null, args);
		},

		_prepareForPlay: function () {
			var o = this.options,
				video = this.currentVideo;
			if ($("source", video).length === 0) {
				this._renderSources(o.sources, video);
			}
			if (!video.data("sourceDetected")) {
				this._analyzeSource(video);
			}
			this._hideTitleControls();
			if (o.relatedVideos && o.relatedVideos.length > 0) {
				$("#" + this._id("_rvcc")).hide();
			}
		},

		togglePlay: function () {
			/* Toggle control play state. If video is playing it will pause, if video is paused it will play. */
			var videoElem = this.currentVideo[ 0 ];

			this._prepareForPlay();
			this._toggleCenterPlayButton();

			if (videoElem.paused || videoElem.ended) {

				// K.D. June 3rd, 2011 Bug #75771 The title of the play/pause button has to be updated
				if (!this.options.browserControls) {
					$("#" + this._id("_ctrls_play")).attr("title", $.ig.VideoPlayer.locale.playing);
				}
				videoElem.play();
			} else {

				// K.D. June 3rd, 2011 Bug #75771 The title of the play/pause button has to be updated
				if (!this.options.browserControls) {
					$("#" + this._id("_ctrls_play")).attr("title", $.ig.VideoPlayer.locale.paused);
				}
				videoElem.pause();
			}
		},

		play: function () {
			/* Start playing current loaded video if any. */
			var videoElem = this.currentVideo[ 0 ];
			if (videoElem.paused || videoElem.ended) {
				this._prepareForPlay();
				this._toggleCenterPlayButton();

				// K.D. June 3rd, 2011 Bug #75771 The title of the play/pause button has to be updated
				if (!this.options.browserControls) {
					$("#" + this._id("_ctrls_play")).attr("title", $.ig.VideoPlayer.locale.playing);
				}
				videoElem.play();
			}
		},

		pause: function () {
			/* Pause the currently playing video if any. */
			var videoElem = this.currentVideo[ 0 ];
			if (!videoElem.paused) {
				this._toggleCenterPlayButton();

				// K.D. June 3rd, 2011 Bug #75771 The title of the play/pause button has to be updated
				if (!this.options.browserControls) {
					$("#" + this._id("_ctrls_play")).attr("title", $.ig.VideoPlayer.locale.paused);
				}
				videoElem.pause();
			}
		},

		currentTime: function (val) {
			/* Get or set the current time of the playing video.
			   paramType="number" Specify the playback position in seconds to navigate to.
			   returnType="number" Returns the current playback position.
			*/
			var videoElem = this.currentVideo[ 0 ],
				rawDuration = videoElem.duration;
			if (val !== null && val !== undefined) {
			    try {
			        // A.M. May 9, 2016 #218835 "currentTime method does not work as setter in IE"
					if ($.ig.util.isIE) {
					    $("video").bind("canplay", function () { this.currentTime = val; });
					}
					    videoElem.currentTime = val;
					    $("#" + this._id("_ctrls_pb"))
						    .igProgressBar("option", "value", (val / rawDuration).toFixed(2) * 100);
					    this._checkCommercialsShow(val);
					    this._checkEmbeddedCommercialShow(val);
				} catch (ex) {}

				if (videoElem.paused && !isNaN(rawDuration)) {
					/* adjust slider position because, timeupdate will not fire. */
					$("#" + this._id("_ctrls_s"))
						.igSlider("option", "value", (val / rawDuration).toFixed(2) * 100);
				}
			}
			return parseInt(videoElem.currentTime, 10);
		},

		screenshot: function (scaleFactor) {
			/* Get a screenshot of the current video frame. It returns a canvas object that you can position and show on the page. This depends on the browser support for canvas.
			   paramType="number" min="0.0" max="1.0" optional="true" Specify scale factor between 0 and 1.
			   returnType="canvasType" Returns HTML5 canvas element that represents the captured screenshot. */
			if (!scaleFactor || $.type(scaleFactor) !== "number") {
				scaleFactor = 1;
			}
			var videoElem = this.currentVideo[ 0 ],
				w = videoElem.videoWidth * scaleFactor,
				h = videoElem.videoHeight * scaleFactor,
				canvas = document.createElement("canvas"),
				ctx;
			if (canvas.getContext) {
				canvas.width  = w;
				canvas.height = h;
				ctx = canvas.getContext("2d");
				ctx.drawImage(videoElem, 0, 0, w, h);
			}
			return canvas;
		},

		supportsVideo: function () {
			/* Get whether the current browser supports video tag.
			   returnType="bool" */
			return !!document.createElement("video").canPlayType;
		},

		supportsH264BaselineVideo: function () {
			/* Get whether the current browser supports H.264 codec.
			   returnType="bool" */
			if (!this.supportsVideo()) {
				return false;
			}
			var v = document.createElement("video");
			return v.canPlayType('video/mp4; codecs="avc1.42E01E, mp4a.40.2"') !== "";
		},

		supportsOggTheoraVideo: function () {
			/* Get whether the current browser supports Theora codec.
			   returnType="bool" */
			if (!this.supportsVideo()) {
				return false;
			}
			var v = document.createElement("video");
			return v.canPlayType('video/ogg; codecs="theora, vorbis"') !== "";
		},

		supportsWebmVideo: function () {
			/* Get whether the current browser supports WEBM codec.
			   returnType="bool" */
			if (!this.supportsVideo()) {
				return false;
			}
			var v = document.createElement("video");
			return v.canPlayType('video/webm; codecs="vp8, vorbis"') !== "";
		},

		paused: function () {
			/* Get whether the current played video is paused.
			   returnType="bool" */
			return this.currentVideo[ 0 ].paused;
		},

		ended: function () {
			/* Get whether the current played video has ended.
			   returnType="bool" */
			return this._getMainVideo()[ 0 ].ended;
		},

		duration: function () {
			/* Get the current duration of the played video. It may be NaN if duration is still not loaded or the video is a live stream.
			returnType="number" */
			this._refreshDuration();
			return this._duration;
		},

		_refreshDuration: function () {
			var duration = this.currentVideo[ 0 ].duration;
			if (duration !== this._duration) {
				this._duration = duration;
				if (duration === Infinity) {
					this._updateTitleControlsTimeString($.ig.VideoPlayer.locale.live,
														$.ig.VideoPlayer.locale.liveStream);
					$("#" + this._id("_ctrls_s")).data("igSlider").handle.hide();
					$("#" + this._id("_ctrls_s")).igSlider("option", "disabled", true);
				} else if (!isNaN(duration)) {
					if (this._commercialPlaying) {
						this._showAdMessage(parseInt(duration, 10));
					} else {
						this._updateTitleControlsTimeString(this._toTimeString(duration));

						// K.D. October 27th, 2011 Bug #83033 There is an issue with Firefox 3.6 with the duration
						// being 0 and updated later to the duration of the video so adding a check for duration
						// being greater than 0
						if (!this._bookmarksRendered && duration > 0) {
							this._renderBookmarks();
						}
					}
				}
			}
		},

		seeking: function () {
			/* Get whether the player is seeking to find the new playback position specified.
			returnType="bool"  */
			return this.currentVideo[ 0 ].seeking;
		},

		destroy: function () {
			/* Destroys the widget. */
			var css = this.css;
			/* Clear any player specific settings from the element. */
			clearTimeout(this._scrollingTimoutId);
			clearTimeout(this._volumeSliderTimeoutId);
			clearTimeout(this._onVideoStateChangeId);
			clearTimeout(this._resizeBookmarkAreaTimeoutId);

			if (this._oldWidth) {
				this.element.css("width", this._oldWidth);
			}
			if (this._oldHeight) {
				this.element.css("height", this._oldHeight);
			}
			this.container.unbind(this._generalEvts);
			$(document).unbind(this._documentEvts);
			if (this._controlsEvts) {
				this.container.unbind(this._controlsEvts);
			}
			this.container.removeClass(css.baseClasses);

			$("#" + this._id("_ctrls_s")).igSlider("destroy");
			$("#" + this._id("_ctrls_pb")).igProgressBar("destroy");
			$("#" + this._id("_ctrls_vs")).igSlider("destroy");
			$("#" + this._id("_seek_tooltip")).igTooltip("destroy");

			if (this._commercialsShow) {
				delete this._commercialsShow;
				delete this._commercialIndex;
				delete this._commercialPlaying;
			}

			if (this._embeddedCommercialsShow) {
				delete this._embeddedCommercialPlaying;
				delete this._embeddedCommercialIndex;
				delete this._embeddedCommercialsShow;
			}

			delete this.bookmarkElements;
			delete this.relatedVideoElements;
			delete this.controlButtons;
			delete this._bannerShow;
			delete this.banner;

			// Will recursively call, unbind(), removeData() and remove() on all children!
			if (this.element.is("video")) {
				this.element.unbind(this._videoEvents);
				this.element.removeClass(css.videoClass);
				this._restoreExistingVideoProperties(this.element);
				$(":not(video)", this.container).remove();
				this.element.unwrap();
			} else {
				this.container.children().remove();
			}

			$.Widget.prototype.destroy.apply(this, arguments);
		},

		_destroyBookmarks: function () {
			if (this.bookmarkElements && this.bookmarkElements.length > 0) {
				this.bookmarkElements.unbind();
				this.bookmarkElements.removeData();
				delete this.bookmarkElements;
				$("#" + this._id("_bookmarks")).remove();
				$("#" + this._id("_ctrls_s")).igSlider("clearBookmarks");
				this._bookmarksRendered = false;
			}
		},

		_destroyRelatedVideos: function () {
			var related = this.options.relatedVideos;
			if (related && related.length > 0) {
				$("#" + this._id("_rvcc")).remove();
				delete this.relatedVideoElements;
				related = [];
			}
		},

		_destroyControls: function () {
			var controls = this.options.browserControls;
			if (!controls) {
				/* destroy child widgets */
				$("#" + this._id("_ctrls_play")).igButton("destroy");
				$("#" + this._id("_ctrls_vc_btn")).igButton("destroy");
				$("#" + this._id("_ctrls_fs_btn")).igButton("destroy");
				$("#" + this._id("_ctrls_pb")).igProgressBar("destroy");
				$("#" + this._id("_ctrls_s")).igSlider("destroy");
				$("#" + this._id("_ctrls_vs")).igSlider("destroy");
				/* destroy controls, calls unbind and removeData on all children recursively. */
				this.controls.each(function () {
					$(this).parent().remove();
				});
				delete this.controls;
				delete this.controlButtons;
				delete this._activeControlsIndex;
			}
		}
	});
	$.extend($.ui.igVideoPlayer, { version: "16.1.20161.2145" });
}(jQuery));



