﻿<%@ Application Language="C#" %>
<%@ Import Namespace="log4net" %>
<%@ Import Namespace="log4net.Config" %>
<%@ Import Namespace="System.Collections.Generic" %>

<script runat="server">

    void Application_Start(object sender, EventArgs e) 
    {
        // Code that runs on application startup
        XmlConfigurator.Configure(); //Loggin automatisch configureren met Web.Config
        
        ILog sysLog = LogManager.GetLogger("Application");
        sysLog.Info("Website startup");

        SiteMap.SiteMapResolve += new SiteMapResolveEventHandler(HandleUnknownSiteMapNode);
    }

    //Voegt paginas toe die niet in de SiteMap staan (bijvoorbeeld voor SecurityDetails.aspx)
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

            string title = (string) HttpContext.GetGlobalResourceObject("Global", "SecurityDetailTitle");
            string query = "SecurityDetail.aspx";
            string description = (string)HttpContext.GetGlobalResourceObject("Global", "SecurityDetailDescription");
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
        ILog sysLog = LogManager.GetLogger("Application");
        sysLog.Info("Website is going down...");
    }
        
    void Application_Error(object sender, EventArgs e) 
    { 
        // Code that runs when an unhandled error occurs

        System.Threading.Thread.CurrentThread.CurrentUICulture = new System.Globalization.CultureInfo("en-US");

        Exception ex = Server.GetLastError();

        //Niet opgevangen excepties doorspelen aan de logger
        Exception lastException = Server.GetLastError().GetBaseException();

        ILog sysLog = LogManager.GetLogger("Application");
        sysLog.Fatal("Fatal error occured in ASP.NET website", lastException);
    }

    void Session_Start(object sender, EventArgs e) 
    {
        // Code that runs when a new session is started
        string sessionId = Session.SessionID; //Fix voor een exceptie die gegooid wordt
                                              //indien sessies verlopen
        
        
        if (!HttpContext.Current.User.Identity.Name.Equals(""))
        {
            //controleren of sessie bestaat en nog niet verlopen is
            if (Session["userID"] == null || Session["sessionID"] == null )
            {
                FormsAuthentication.SignOut();
                Response.Redirect(Request.Url.ToString());
            }
        }
    }

    void Session_End(object sender, EventArgs e) 
    {
        // Code that runs when a session ends. 
        // Note: The Session_End event is raised only when the sessionstate mode
        // is set to InProc in the Web.config file. If session mode is set to StateServer 
        // or SQLServer, the event is not raised.
    }
</script>
