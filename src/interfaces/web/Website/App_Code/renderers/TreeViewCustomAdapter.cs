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
/// Summary description for TreeViewCustomAdapter
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

        foreach (TreeNode node in tree.Nodes)
        {
            writer.WriteBeginTag("li");
            writer.Write(HtmlTextWriter.TagRightChar);
            if (node.NavigateUrl != null)
            {
                writer.WriteBeginTag("a");
                writer.WriteAttribute("href", node.NavigateUrl);
                writer.Write(HtmlTextWriter.TagRightChar);
                writer.Write(node.Text);
                writer.WriteEndTag("a");
            }
            writer.WriteEndTag("li");

            if (node.ChildNodes.Count != 0)
            {
                writer.WriteBeginTag("ul");
                writer.Write(HtmlTextWriter.TagRightChar);

                foreach (TreeNode subnode in node.ChildNodes)
                {
                    writer.WriteBeginTag("li");
                    writer.Write(HtmlTextWriter.TagRightChar);
                    if (node.NavigateUrl != null)
                    {
                        writer.WriteBeginTag("a");
                        writer.WriteAttribute("href", subnode.NavigateUrl);
                        writer.Write(">");
                        writer.Write(subnode.Text);
                        writer.WriteEndTag("a");
                    }
                    writer.WriteEndTag("li");
                }
                writer.WriteEndTag("ul");
            }
        }
    }
}
