var foo = new Date;
var d1 = [[foo.getTime() - (1000 * 60 * 60 * 24 * 7), 10], [foo.getTime(), 10]];

var primaryPlot = function() { };
var subPlot = function() { };
var plot = function() { };

$.extend(plot.prototype, {

    init: function(container, from, to, isin) {
        this.containerName = '#' + container;
        this.data = this.getData(from, to);
        this.plotListeners = [];
        this.codes = [];
		this.maxReferences = 3;
        this._init(container, from, to, isin);
    },

    getData: function(from, to) {
        return this.getDummyData(from, to);
    },

    draw: function() {
        this.plot = $.plot(this.container, this.data, this.options);
        this.addTemporyEvents();
    },

    addTemporyEvents: function() { },

    getStats: function(from, to) {
        var min, max, data, noPoints = 0;
        var output = [];
        for (line in this.data) {
            data = this.data[line]['data'];
            for (i = 0; i < data.length; i++)
                if (data[i][0] <= to && data[i][0] >= from) {
                if (max === undefined) {
                    max = data[i][1];
                    min = data[i][1];
                }
                if (data[i][1] > max)
                    max = data[i][1];
                if (data[i][1] < min)
                    min = data[i][1];
                noPoints++;
            }
        }
        output.min = min;
        output.max = max;
        output.noPoints = noPoints;
        return output;
    },

    setView: function(from, to) {
        if (from < this.rangeMin)
            from = this.rangeMin;
        if (to > this.rangeMax)
            to = this.rangeMax;
        this.options.setXRange(from, to);
        var range = this.getStats(from, to);
        this.options.setYRange(range.min, range.max);
        var target;
        for (t in this.plotListeners) {
            target = this.plotListeners[t];
            target.setView(from, to);
            target.requestData();
            target.draw();
        }
    },

    addPlotListener: function(target) {
        this.plotListeners.push(target);
    }

});

$.extend(subPlot.prototype, {

    _init: function(container, from, to, isin) {
        this.container = $(this.containerName);
        this.options = new BarOptions();
        this.draw();
        this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max); // dubbel tekenen vervangen door from en max functie
        this.draw();
        this.codes.push(isin);
    },

    getDummyData: function(from, to) {
        return [
			{ label: '', data: d1, color: 'lightblue', bars: { show: true }, id: 0 }
		];
    },

    requestDataFromService: function(id, isin, from, to) {
        var _this = this;
        var diff = (to - from) / 2;
        from -= diff;
        to += diff;
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: "WebService.asmx/getVolumes",
            data: "{'isin':'" + isin + "','from':" + from + ",'to':" + to + "}",
            dataType: "json",
            success: function(json) {
                var line = _this.data[id];
                var data = eval("(" + json.d + ")");
                var xaxis = _this.plot.getAxes().xaxis;
                line.label = '';
                line.data = data.data;
                _this.draw();
                _this.setView(xaxis.min, xaxis.max);
                _this.draw();
            }
        });
    },

    requestData: function() {
        for (nr in this.codes)
            this.requestDataFromService(nr, this.codes[nr], this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max);
    },

    setView: function(from, to) {
        this.options.setXRange(from, to);
        var range = this.getStats(from, to);
        this.options.setYRange(0, range.max);
        var target;
        for (t in this.plotListeners) {
            target = this.plotListeners[t];
            target.setView(from, to);
            target.draw();
        }
    }

});

$.extend(primaryPlot.prototype, {

    _init: function (container, from, to, isin) {
        this.container = $(this.containerName + ' div');
        this.options = new LineOptions();
        this.draw();
        this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max); // dubbel tekenen vervangen door from en max functie
        this.draw();
        this.addEvents();
        this.lastView = [this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max];
        this.history = new Array();
        this.noLines = 1;
        this.requestDataFromService(0, isin, this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max);
        this.codes.push(isin);
        this.draw();
        this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max);
        this.draw();
    },

    getDummyData: function (from, to) {
        return [
			{ label: messages.loading, data: d1, color: 'lightblue', lines: { fill: true }, id: 0 }
		];
    },

    addLine: function (from, to, ref) {
        var color = 'red';
        if (this.noLines == 2)
            color = 'yellow';
        this.data.push({ label: messages.loading, data: [], color: color, id: this.noLines });
        this.requestDataFromService(this.noLines, ref, this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max);
        this.codes.push(ref);
    },

    addTemporyEvents: function () {
        $(this.containerName + ' .legendLabel a').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            //var href = event.srcElement.href;
            var href = event.target.href;
            me.data.splice(href.substring(href.length - 1), 1);
            me.draw();
            $(me.containerName + ' li.add').removeClass('disabled');
            me.noLines--;
            return false;
        });
    },

    pushHistory: function () {
        this.history.push(this.lastView);
        this.lastView = [this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max];
        $(this.containerName + ' li.last, ' + this.containerName + ' li.reset').removeClass('disabled');
    },

    requestDataFromService: function (id, isin, from, to) {
        var _this = this;
        var diff = (to - from) / 2;
        from -= diff;
        to += diff;
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: "WebService.asmx/getData",
            data: "{'isin':'" + isin + "','from':" + from + ",'to':" + to + "}",
            dataType: "json",
            success: function (json) {
                var line = _this.data[id];
                var data = eval("(" + json.d + ")");
                var xaxis = _this.plot.getAxes().xaxis;
                line.label = messages.legendString + ' ' + data.name;
                line.data = data.data;
                _this.draw();
                _this.setView(xaxis.min, xaxis.max);
                _this.draw();
                if (_this.rangeMin === undefined || _this.rangeMin > data.min)
                    _this.rangeMin = data.min;
                if (_this.rangeMax === undefined || _this.rangeMax < data.max)
                    _this.rangeMax = data.max;
            }
        });
    },

    requestData: function () {
        for (nr in this.codes)
            this.requestDataFromService(nr, this.codes[nr], this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max);
    },

    addEvents: function () {

        this.container.bind('plotzoom', { me: this }, function (event, plot) {
            var ranges = plot.getAxes().xaxis;
            var me = event.data.me;
            // controle op maximum inzoomen
            //var range = me.getStats(ranges.min, ranges.max);
            //if (range.noPoints < 10)
            //    return false;
            me.setView(ranges.min, ranges.max);
            me.draw();
            me.requestData();
            me.pushHistory();
        });

        this.container.bind("plotselected", { me: this }, function (event, ranges) {
            var me = event.data.me;
            me.setView(ranges.xaxis.from, ranges.xaxis.to);
            me.draw();
            me.requestData();
            me.pushHistory();
        });

        this.container.bind('plotpan', { me: this }, function (event, plot) {
            var ranges = plot.getAxes().xaxis;
            var me = event.data.me;
            me.setView(ranges.min, ranges.max);
            me.draw();
            me.requestData();
            me.pushHistory();
        });

        $(this.containerName + ' li.selection').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            me.options.setSelectionMode();
            me.draw();
            me.requestData();
            $(me.containerName + ' .selection').hide();
            $(me.containerName + ' .pan').show();
            me.pushHistory();
        });

        $(this.containerName + ' li.pan').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            me.options.setPanningMode();
            me.draw();
            me.requestData();
            $(me.containerName + ' .pan').hide();
            $(me.containerName + ' .selection').show();
            me.pushHistory();
        });

        $(this.containerName + ' li.zoomOut').bind('click', { me: this }, function (event) {
            event.data.me.plot.zoomOut();
        });

        $(this.containerName + ' li.zoomIn').bind('click', { me: this }, function (event) {
            event.data.me.plot.zoom();
        });

        $(this.containerName + ' li.reset').addClass('disabled').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            delete me.options;
            me.options = new LineOptions();
            me.data.length = 1;
            me.noLines = 1;
            me.codes.lenght = 0;
            me.draw();
            me.setView(me.plot.getAxes().xaxis.min, me.plot.getAxes().xaxis.max);
            // dubbel tekenen vervangen door from en max functie
            me.draw();
            me.requestData();
            me.history.length = 0;
            var ranges = me.plot.getAxes().xaxis;
            me.lastView = [ranges.min, ranges.max];
            $(me.containerName + ' li.last, ' + me.containerName + ' li.reset').addClass('disabled');
            $(me.containerName + ' li.add').removeClass('disabled');
        });

        $(this.containerName + ' li.last').addClass('disabled').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            if (me.history.length == 0)
                return;
            var range = me.history.pop();
            me.setView(range[0], range[1]);
            me.draw();
            me.requestData();
            if (me.history.length == 0) {
                var ranges = me.plot.getAxes().xaxis;
                me.lastView = [ranges.min, ranges.max];
                $(me.containerName + ' li.last').addClass('disabled');
            }
        });

        $(this.containerName + ' li.add').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            if (me.noLines == me.maxReferences)
                return;
            me.referenceMenuActivated = true;
            $(me.containerName).parent().children('.subPlot').hide();
            $(me.containerName).siblings('.overlay').show();
            $(me.containerName + ' ul').hide();
        });

        $(this.containerName).parent().bind('mouseover', { me: this }, function (event) {
            var me = event.data.me;
            $(me.containerName + ' .legendLabel a').show();
            if (me.referenceMenuActivated)
                return;
            $(me.containerName + ' ul').show();
        });

        $(this.containerName).parent().bind('mouseout', { me: this }, function (event) {
            var me = event.data.me;
            $(me.containerName + ' .legendLabel a').hide();
            if (me.referenceMenuActivated)
                return;
            $(me.containerName + ' ul').hide();
        });

        $(this.containerName).siblings('.overlay').children('p').children('.cancel').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            $(me.containerName).siblings('.overlay').hide();
            $(me.containerName + ' ul').show();
            me.referenceMenuActivated = false;
            $(me.containerName).parent().children('.subPlot').show();
            return false;
        });

        $(this.containerName).siblings('.overlay').children('p').children('.add').bind('click', { me: this }, function (event) {
            var me = event.data.me;
            if (me.noLines == me.maxReferences)
                return;
            me.addLine(1, 1, $('#code').val());
            me.noLines++;
            if (me.noLines == me.maxReferences)
                $(me.containerName + ' li.add').addClass('disabled');
            $(me.containerName + ' li.reset').removeClass('disabled');
            $(me.containerName).siblings('.overlay').hide();
            $(me.containerName + ' ul').show();
            $(me.containerName).parent().children('.subPlot').show();
            me.referenceMenuActivated = false;
            return false;
        });

        // Add translations
        $(this.containerName + ' li.pan').text(messages.menuPan);
        $(this.containerName + ' li.selection').text(messages.menuSelection);
        $(this.containerName + ' li.last').text(messages.menuLast);
        $(this.containerName + ' li.zoomIn').text(messages.menuZoomIn);
        $(this.containerName + ' li.zoomOut').text(messages.menuZoomOut);
        $(this.containerName + ' li.add').text(messages.menuAdd);
        $(this.containerName + ' li.reset').text(messages.menuReset);
        $(this.containerName).siblings('.overlay').children('p').children('.add').text(messages.referenceAdd);
        $(this.containerName).siblings('.overlay').children('p').children('.cancel').text(messages.referenceCancel);

    }

});

var PrimaryPlot = function(container, from, to, isin) {
    this.init(container, from, to, isin);
}

var SubPlot = function(container, from, to, isin) {
    this.init(container, from, to, isin);
}

$.extend(true, PrimaryPlot.prototype, plot.prototype, primaryPlot.prototype);
$.extend(true, SubPlot.prototype, plot.prototype, subPlot.prototype);