using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace web.bo
{
    //Deze interface wordt geimplementeerd door de masterpage.
    //De DataAccess klassen roepen deze op indien de huidige sessie niet meer geldig is.
    public interface ISession
    {
        void handleSessionTimeout();
    }
}