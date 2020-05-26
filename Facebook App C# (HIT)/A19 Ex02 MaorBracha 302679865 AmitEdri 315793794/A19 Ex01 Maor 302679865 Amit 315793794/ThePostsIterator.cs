using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using FacebookWrapper.ObjectModel;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class ThePostsIterator : IEnumerable<Post>
    {
        private readonly ICollection<Post> m_PostList;

        public Func<Post, bool> Condition { get; set; }

        public ThePostsIterator(Func<Post, bool> i_Condition)
        {
            Condition = i_Condition;
            m_PostList = LoggedUserDataSingleton.getInstance().PostList;
        }

        public IEnumerator<Post> GetEnumerator()
        {
            foreach (Post post in m_PostList)
            {
                if (Condition.Invoke(post))
                {
                    yield return post;
                }
            }
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return this.GetEnumerator();
        }
    }
}
