<%@ Application Language="C#" %>

<script runat="server">

    void Application_Start(object sender, EventArgs e) 
    {
        // Code that runs on application startup
        SiteMap.SiteMapResolve += new SiteMapResolveEventHandler(HandleUnknownSiteMapNode);
    }

    //Verwerkt de event die wordt opgeroepen als een pagina niet in de SiteMap staat (bijvoorbeeld voor SecurityDetails.aspx)
    //Zie: http://msdn.microsoft.com/en-us/magazine/cc163598.aspx
    public static SiteMapNode HandleUnknownSiteMapNode(object sender, SiteMapResolveEventArgs e)
    {
        HttpContext context = HttpContext.Current;

        //Sitemap node toevoegen voor de securitydetail pagina
        if (context.Request.Path.ToLower().Contains("securitydetail.aspx"))
        {
            SiteMapNode node = new SiteMapNode(SiteMap.Provider, "SecurityOverview.aspx", "SecurityOverview.aspx", "Security Overview");
            
            string title = "Security Detail";
            string query = "SecurityDetail.aspx";
            if (context.Request.Params["param"] != null) {
                title += " (" + context.Request.Params["param"] + ")";
                query += "?param=" + context.Request.Params["param"];
            }
            SiteMapNode nodeSecurity = new SiteMapNode(SiteMap.Provider, query, query, title);
            
            node.ParentNode = SiteMap.Provider.RootNode;
            nodeSecurity.ParentNode = node;
            return nodeSecurity;
        }

        return null; //Andere pagina's die niet in de sitemap zitten negeren we
    }
    
    void Application_End(object sender, EventArgs e) 
    {
        //  Code that runs on application shutdown

    }
        
    void Application_Error(object sender, EventArgs e) 
    { 
        // Code that runs when an unhandled error occurs

    }

    void Session_Start(object sender, EventArgs e) 
    {
        // Code that runs when a new session is started

    }

    void Session_End(object sender, EventArgs e) 
    {
        // Code that runs when a session ends. 
        // Note: The Session_End event is raised only when the sessionstate mode
        // is set to InProc in the Web.config file. If session mode is set to StateServer 
        // or SQLServer, the event is not raised.

    }
       
</script>
