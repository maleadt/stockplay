var primaryPlot = function() {};
var subPlot = function() {};
var plot = function() {};

$.extend(plot.prototype, {

	init: function(container, from, to) {
		this.containerName = '#'+container;
		this.container = $(this.containerName+' div');
		this.data = this.getData(from, to);
		this.options = new Options();
		this.draw();
		this.setView(this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max); // dubbel tekenen vervangen door from en max functie
		this.draw();
		this._init();
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
		    			max =  data[i][1]; // Zou het niet sneller zijn om enkel de index van het grooste element bij te houden?
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
    }

});

$.extend(subPlot.prototype, {

	_init: function(container, from, to) {
	}

});

$.extend(primaryPlot.prototype, {

	init: function(container, from, to) {
		this.addEvents();
		this.lastView = [this.plot.getAxes().xaxis.min, this.plot.getAxes().xaxis.max];
		this.history = new Array();
		this.noLines = 1;
	},

	getDummyData: function(from, to) {
		return [
			{ label: "Beursverloop Tomtom", data: d2, color: 'lightblue', lines: { fill: true }, id: 0 }
		];
	},

	addLine: function(from, to, ref) {
		this.data.push({label: "Beursverloop Ordina", data: d3, color: 'red', id: 1});
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
			me.options = new Options();
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
			if (me.noLines == 2)
				return;
			me.addLine(1,1,1);
			me.noLines++;
			if (me.noLines == 2)
				$(me.containerName+' li.add').addClass('disabled');
			$(me.containerName+' li.reset').removeClass('disabled');
		});

		$(this.containerName).bind('mouseover', {me: this}, function(event) {
			$(event.data.me.containerName+' .legendLabel a').show();
		});

		$(this.containerName).bind('mouseout', {me: this}, function(event) {
			$(event.data.me.containerName+' .legendLabel a').hide();
		});
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
	new PrimaryPlot('plotTest', 2, 11);
	new SubPlot('volumes', 2, 11);
	//new PrimaryPlot('plotTwee', 2, 11);
});