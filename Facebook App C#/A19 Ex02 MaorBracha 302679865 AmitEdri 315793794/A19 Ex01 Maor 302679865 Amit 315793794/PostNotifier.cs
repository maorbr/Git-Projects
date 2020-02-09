using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class PostNotifier
    {
        public event Action<string> UpdatingReplyPostID;

        public PostNotifier()
        {
        }

        public void onUpdateReplyPostID(string i_ReplyID)
        {
            if (this.UpdatingReplyPostID != null)
            {
                this.UpdatingReplyPostID(i_ReplyID);
            }
        }
    }
}
