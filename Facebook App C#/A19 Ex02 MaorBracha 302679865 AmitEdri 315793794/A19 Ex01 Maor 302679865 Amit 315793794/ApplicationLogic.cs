using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using FacebookWrapper;
using FacebookWrapper.ObjectModel;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class ApplicationLogic
    {
        private LoggedUserDataSingleton m_ManagerUserData;

        public User LoggedInUser
        {
            get;
            set;
        }

        public LoginResult LoginResult
        {
            get;
            set;
        }

        public string AppId
        {
            get;
            set;
        }

        public ApplicationLogic()
        {
            AppId = "2208649642728812"; /*2208649642728812";*/
        }

        public void LoginAndInit()
        {
            m_ManagerUserData = LoggedUserDataSingleton.getInstance();
            LoginResult = FacebookWrapper.FacebookService.Login(AppId, "public_profile", "user_events", "user_events"); ///need to add here all the reviewed permissions
            LoggedInUser = LoginResult.LoggedInUser;
            m_ManagerUserData.UpdateUserData(LoggedInUser);
        }
    }
}
