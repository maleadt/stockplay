	var d1 = [[1.2708504E+12, 11.1]];
	
		var dataa = eval("("+'{"data":[[1271335429000,138.4],[1271336037000,138.5],[1271336455000,138.5],[1271336515000,138.5],[1271336871000,138.45],[1271337173000,138.5],[1271337348000,138.5],[1271337495000,138.5],[1271337616000,138.8],[1271337740000,138.9],[1271337844000,138.95],[1271337983000,138.85],[1271338077000,139],[1271338129000,139],[1271338340000,138.85],[1271338450000,138.8],[1271338476000,139],[1271338689000,138.9]],"min":0,"max":12713388000,"name":"Testing"}'+")");


var primaryPlot = function() {};
var subPlot = function() {};
var plot = function() {};

$.extend(plot.prototype, {

	init: function(container, from, to) {
		this.containerName = '#'+container;
		this.data = this.getData(from, to);
		this.plotListeners = [];
		this._init(container, from, to);
		this.maxReferences = 3;
	},

	getData: function(from, to) {
		return this.getDummyData(from, to);
	},

	draw: function() {
		this.plot = $.plot(this.container, this.data, this.options);
		this.addTemporyEvents();
	},

	addTemporyEvents: function() {},

    getMinMax: function(from, to) {
    	var min, max, data;
		for (line in this.data) {
			data = this.data[line]['data'];
			for(i=0;i<data.length;i++)
		    	if (data[i][0] <= to && data[i][0] >= from) {
		        	if (max === undefined) {
	    	    		max = data[i][1];
	    	    		min = data[i][1];
	    	    	}
	        		if (data[i][1] > max)
		    			max =  data[i][1];
	        		if (data[i][1] < min)
		    			min =  data[i][1];
		    	}
		}
    	return [min,max];
    },

    setView: function(from, to) {
		this.options.setXRange(from, to);
		var range = this.getMinMax(from, to);
		this.options.setYRange(range[0], range[1]);
		var target;
		for (t in this.plotListeners) {
			target = this.plotListeners[t];
			target.setView(from, to);
			target.draw();
		}
    },

    addPlotListener: function(target) {
		this.plotListeners.push(target);
    }

});

$.extend(subPlot.prototype, {

	_init: function(container, from, to) {
		this.container = $(this.containerName);
		this.options = new BarOptions();
		this.draw();
		this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max); // dubbel tekenen vervangen door from en max functie
		this.draw();
	},

	getDummyData: function(from, to) {
		return [
			{ label: "Beursverloop Tomtom", data: [], color: 'lightblue', bars: { show: true }, id: 0 }
		];
	},

    setView: function(from, to) {
		this.options.setXRange(from, to);
		var range = this.getMinMax(from, to);
		this.options.setYRange(0, range[1]);
		var target;
		for (t in this.plotListeners) {
			target = this.plotListeners[t];
			target.setView(from, to);
			target.draw();
		}
    }	

});

$.extend(primaryPlot.prototype, {

	_init: function(container, from, to) {
		this.container = $(this.containerName+' div');
		this.options = new LineOptions();
		this.draw();
		this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max); // dubbel tekenen vervangen door from en max functie
		this.draw();
		this.addEvents();
		this.lastView = [this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max];
		this.history = new Array();
		this.noLines = 1;
		this.requestData(0, 'BE0003780948', this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max);
		this.draw();
		this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max);
		this.draw();
	},

	requestData: function(id, isin, from, to) {
		var line = this.data[id];
		line.label = "Beursverloop" + " " + dataa.name;
		line.data = dataa.data;
	},

	getDummyData: function(from, to) {
		return [
			{ label: "Beursverloop Tomtom", data: d1, color: 'lightblue', lines: { fill: true }, id: 0 }
		];
	},

	addLine: function(from, to, ref) {
		this.data.push({label: "Beursverloop Ordina", data: [], color: 'red', id: 1});
		this.draw();
		this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max); // dubbel tekenen vervangen door from en max functie
		this.draw();
	},
	
	addTemporyEvents: function() {
		$(this.containerName+' .legendLabel a').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			var href = event.srcElement.href;
			me.data.splice(href.substring(href.length-1),1);
			me.draw();
			$(me.containerName+' li.add').removeClass('disabled');
			me.noLines--;
			return false;
		});
	},

	pushHistory: function() {
		this.history.push(this.lastView);
		this.lastView = [this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max];
		$(this.containerName+' li.last, '+this.containerName+' li.reset').removeClass('disabled');
	},
	
    addEvents: function() {
    
		this.container.bind('plotzoom', {me: this}, function (event, plot) {
			var ranges = plot.getAxes().xaxis;
			var me = event.data.me;
			me.setView(ranges.min, ranges.max);
			me.draw();
			me.pushHistory();
		});

	    this.container.bind("plotselected", {me: this}, function (event, ranges) {
			var me = event.data.me;
			me.setView(ranges.xaxis.from, ranges.xaxis.to);
			me.draw();
			me.pushHistory();
		});

		this.container.bind('plotpan', {me: this}, function (event, plot) {
			var ranges = plot.getAxes().xaxis;
			var me = event.data.me;
			me.setView(ranges.min, ranges.max);
			me.draw();
			me.pushHistory();
		});
		
		$(this.containerName+' li.selection').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			me.options.setSelectionMode();
			me.draw();
			$(me.containerName+' .selection').hide();
			$(me.containerName+' .pan').show();
			me.pushHistory();
		});

		$(this.containerName+' li.pan').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			me.options.setPanningMode();
			me.draw();
			$(me.containerName+' .pan').hide();
			$(me.containerName+' .selection').show();
			me.pushHistory();
		});

		$(this.containerName+' li.zoomOut').bind('click', {me: this}, function(event) {
			event.data.me.plot.zoomOut();
		});

		$(this.containerName+' li.zoomIn').bind('click', {me: this}, function(event) {
			event.data.me.plot.zoom();
		});

		$(this.containerName+' li.reset').addClass('disabled').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			delete me.options;
			me.options = new LineOptions();
			me.data.length = 1;
			me.noLines = 1;
			me.draw();
			me.setView(me.plot.getAxes().xaxis.min, me.plot.getAxes().xaxis.max);
			// dubbel tekenen vervangen door from en max functie
			me.draw();
			me.history.length = 0;
			var ranges = me.plot.getAxes().xaxis;
			me.lastView = [ranges.min, ranges.max];
			$(me.containerName+' li.last, '+me.containerName+' li.reset').addClass('disabled');
			$(me.containerName+' li.add').removeClass('disabled');
		});

		$(this.containerName+' li.last').addClass('disabled').bind('click', {me: this}, function(event) {
			var me = event.data.me;
	   		if (me.history.length == 0)
	   			return;
	   		var range = me.history.pop();
			me.setView(range[0], range[1]);
			me.draw();
	   		if (me.history.length == 0) {
				var ranges = me.plot.getAxes().xaxis;
				me.lastView = [ranges.min, ranges.max];
				$(me.containerName+' li.last').addClass('disabled');
			}
		});

		$(this.containerName+' li.add').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			if (me.noLines == me.maxReferences)
				return;
			$(me.containerName).siblings('.overlay').show();
			$(me.containerName+' ul').hide();
		});

		$(this.containerName).bind('mouseover', {me: this}, function(event) {
			$(event.data.me.containerName+' .legendLabel a').show();
		});

		$(this.containerName).bind('mouseout', {me: this}, function(event) {
			$(event.data.me.containerName+' .legendLabel a').hide();
		});

		$(this.containerName).siblings('.overlay').children('p').children('.cancel').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			$(me.containerName).siblings('.overlay').hide();
			$(me.containerName+' ul').show();
			return false;
		});

		$(this.containerName).siblings('.overlay').children('p').children('.add').bind('click', {me: this}, function(event) {
			var me = event.data.me;
			if (me.noLines == me.maxReferences)
				return;
			me.addLine(1,1,1);
			me.noLines++;
			if (me.noLines == me.maxReferences)
				$(me.containerName+' li.add').addClass('disabled');
			$(me.containerName+' li.reset').removeClass('disabled');
			$(me.containerName).siblings('.overlay').hide();
			$(me.containerName+' ul').show();
			return false;
		});
		
		// Add translations
		$(this.containerName+' li.pan').text(messages.menuPan);
		$(this.containerName+' li.selection').text(messages.menuSelection);
		$(this.containerName+' li.last').text(messages.menuLast);
		$(this.containerName+' li.zoomIn').text(messages.menuZoomIn);
		$(this.containerName+' li.zoomOut').text(messages.menuZoomOut);
		$(this.containerName+' li.add').text(messages.menuAdd);
		$(this.containerName+' li.reset').text(messages.menuReset);
		$(this.containerName).siblings('.overlay').children('p').children('.add').text(messages.referenceAdd);
		$(this.containerName).siblings('.overlay').children('p').children('.cancel').text(messages.referenceCancel);
    }

});

var PrimaryPlot = function(container, from, to) {
	this.init(container, from, to);
}

var SubPlot = function(container, from, to) {
	this.init(container, from, to);
}

$.extend(true, PrimaryPlot, plot, primaryPlot);
$.extend(true, SubPlot, plot, subPlot);

$(function (){
	var prim = new PrimaryPlot('plotTest', 2, 11);
	var sec = new SubPlot('volumes', 2, 11);
	prim.addPlotListener(sec);
});