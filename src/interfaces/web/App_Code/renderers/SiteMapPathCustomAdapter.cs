using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;

/// <summary>
/// Rendert de SiteMapPath als een P tag.
/// </summary>
public class SiteMapPathCustomAdapter : System.Web.UI.WebControls.Adapters.WebControlAdapter
{

    protected override void RenderBeginTag(HtmlTextWriter writer)
    {
        Console.Write(Control);
        SiteMapPath path = Control as SiteMapPath;
        writer.WriteBeginTag("p");
        if (path.CssClass != "")
            writer.WriteAttribute("class", path.CssClass);
        writer.Write(HtmlTextWriter.TagRightChar);
    }

    protected override void RenderEndTag(HtmlTextWriter writer)
    {
        writer.WriteEndTag("p");
    }

    protected override void RenderContents(HtmlTextWriter writer)
    {
        SiteMapPath path = Control as SiteMapPath;
        SiteMapProvider Provider = ((SiteMapPath)(Control)).Provider;

        SiteMapNodeCollection collection = new SiteMapNodeCollection();
        SiteMapNode node = Provider.CurrentNode;
        while(node != null && node != Provider.RootNode)
        {
            collection.Add(node);
            node = node.ParentNode;
        }

        for (int i = collection.Count - 1; i > 0; i--)
        {
            RenderSiteMapNode(writer, path, collection[i]);
            writer.Write(" " + path.PathSeparator + " ");
        }
        if (collection.Count > 0)
        {
            RenderSiteMapNode(writer, path, collection[0]);
        }
    }

    private void RenderSiteMapNode(HtmlTextWriter writer, SiteMapPath path, SiteMapNode node)
    {
        writer.WriteBeginTag("a");
        if(node.Url != "")
            writer.WriteAttribute("href", node.Url);
        if (node.Description != "")
            writer.WriteAttribute("title", node.Description);
        writer.Write(HtmlTextWriter.TagRightChar);
        writer.Write(node.Title);
        writer.WriteEndTag("a");
    }
}
