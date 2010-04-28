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
/// Rendert de treeview als een UL met LI tags.
/// </summary>
public class TreeViewCustomAdapter : System.Web.UI.WebControls.Adapters.WebControlAdapter
{
    protected override void RenderBeginTag(HtmlTextWriter writer)
    {  
        TreeView tree = Control as TreeView;
        writer.WriteBeginTag("ul");
        if(tree.CssClass != null)
            writer.WriteAttribute("class", tree.CssClass);
        writer.Write(HtmlTextWriter.TagRightChar);
    }

    protected override void RenderEndTag(HtmlTextWriter writer)
    {
        writer.WriteEndTag("ul");
    }

    protected override void RenderContents(HtmlTextWriter writer)
    {
        TreeView tree = Control as TreeView;
        RenderNodes(writer, tree.Nodes);
    }

    private void RenderNodes(HtmlTextWriter writer, TreeNodeCollection children)
    {
        writer.Indent++;
        foreach (TreeNode subnode in children)
        {
            writer.WriteLine();
            writer.WriteBeginTag("li");
            writer.Write(HtmlTextWriter.TagRightChar);

            if (subnode.NavigateUrl != null)
            {
                writer.WriteBeginTag("a");
                if(subnode.NavigateUrl != "")
                    writer.WriteAttribute("href", subnode.NavigateUrl);
                if(subnode.ToolTip != "")
                    writer.WriteAttribute("title", subnode.ToolTip);
                writer.Write(">");
                writer.Write(subnode.Text);
                writer.WriteEndTag("a");
            }

            writer.WriteEndTag("li");

            if (subnode.ChildNodes.Count != 0)
            {
                writer.WriteLine();
                writer.WriteBeginTag("ul");
                writer.WriteAttribute("class", "TreeViewSubmenu");
                writer.Write(HtmlTextWriter.TagRightChar);
                RenderNodes(writer, subnode.ChildNodes);
                writer.WriteEndTag("ul");
            }
        }
        writer.Indent--;
        writer.WriteLine();
    }
}