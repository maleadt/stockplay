var Flot = function(container, from, to) {
	this.init(container, from, to);
}

$.extend(Flot.prototype, {

	// object variables
	container: '',
	data: {},
	options: null,

	init: function(container, from, to) {
		this.container = $(container);
		this.data = this.getData(from, to);
		this.options = new Options();
		this.draw();
	},

	getData: function(from, to) {
		return this.getDummyData(from, to);
	},
	
	getDummyData: function(from, to) {
		//alert("data opvragen");
		var d = [];
		for (var i = 0; i <= 250; ++i) {
			var x = from + i * (to - from) / 500;
			d.push([x*10000000, (Math.sin(x+4)+2)*25]);
		}

		return [
			{ label: "Beursverloop Tomtom", data: d, color: "lightblue" }
		];
	},
	
	draw: function() {
		$.plot(this.container, this.data, this.options);
	},
	
    getMax: function(from, to) {
    	// ToDo min en max voor ALLE zichtbare lijnen berekenen
    	var max = null;
 		for(i=0;i<startData[0]['data'].length;i++)
        	if (startData[0]['data'][i][0] <= to && startData[0]['data'][i][0] >= from) {
	        	if (max == null)
	        		max = startData[0]['data'][i][1];
	        	if (startData[0]['data'][i][1] > max )
    	    		max =  startData[0]['data'][i][1];
    	    }
    	return max;
    },

    getMin: function(from, to) {
    	// ToDo min en max voor ALLE zichtbare lijnen berekenen
    	var min = null;
 		for(i=0;i<startData[0]['data'].length;i++)
        	if (startData[0]['data'][i][0] <= to && startData[0]['data'][i][0] >= from) {
	        	if (min == null)
	        		min = startData[0]['data'][i][1];
	        	if (startData[0]['data'][i][1] < min )
	        		min =  startData[0]['data'][i][1];
	       	}
    	return min;
    },
    
});


$(function (){
	var widget1 = new Flot('#plotTest div', 2, 11);
});