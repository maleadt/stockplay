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
            SiteMapNode overview = SiteMap.Provider.FindSiteMapNode("~/SecuritiesOverview.aspx");
            //Een bestaande node kan je niet aanpassen, dus nemen we een kopie
            SiteMapNode node = new SiteMapNode(overview.Provider, overview.Key, overview.Url, overview.Title, overview.Description);
            
            string title = "Security Detail";
            string query = "SecurityDetail.aspx";
            string description = "Detail of security";
            if (context.Request.Params["param"] != null) {
                title += " (" + context.Request.Params["param"] + ")";
                query += "?param=" + context.Request.Params["param"];
                description += " '" + context.Request.Params["param"] + "' ";
            }
            SiteMapNode nodeSecurity = new SiteMapNode(SiteMap.Provider, query, query, title, description);
            
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
