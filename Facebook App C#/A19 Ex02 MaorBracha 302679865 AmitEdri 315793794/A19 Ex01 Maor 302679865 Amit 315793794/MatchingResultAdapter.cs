using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class MatchingResultAdapter
    {
        private string m_NameOfUser;

        private string m_NameOfMatchedPerson;

        private string m_Hometown;

        public string StringResult { get; set; }

        public MatchingResultAdapter(string i_nameOfUser, string i_nameOfMatch, string i_hometown)
        {
            m_NameOfUser = i_nameOfUser;
            m_NameOfMatchedPerson = i_nameOfMatch;
            m_Hometown = i_hometown;
            createStringResult();
        }

        private void createStringResult()
        {
            StringResult = "Congratulations!! " + m_NameOfUser + "we have found your ideal love match, his name:" + m_NameOfMatchedPerson + " and he live in " + m_Hometown;
        }
    }
}
