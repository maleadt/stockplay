using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using StockPlay;

namespace StockPlay.implXMLRPC {
	public class Plot
	{
	    public Plot() {
	        this.data = new List<List<double>>();
	    }    
	
	    public List<List<Double>> data { get; set; }
	    public double min { get; set; }
	    public double max { get; set; }
	    public string name { get; set; }
	}
}