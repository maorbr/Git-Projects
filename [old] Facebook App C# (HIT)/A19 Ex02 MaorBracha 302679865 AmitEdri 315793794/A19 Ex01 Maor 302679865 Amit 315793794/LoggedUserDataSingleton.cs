using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using FacebookWrapper;
using FacebookWrapper.ObjectModel;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class LoggedUserDataSingleton
    {
        private User m_User = null;

        private static LoggedUserDataSingleton s_Instance { get; set; }

        public string PictureNormalURL { get; set; }

        public string PictureLargeURL { get; set; }

        public string BirthdayDate { get; set; }

        public string Email { get; set; }

        public string FirstName { get; set; }

        public string Gender { get; set; }

        public string LastName { get; set; }

        public string RelationshipStatus { get; set; }

        public string InterestedIn { get; set; }

        public string Religion { get; set; }

        public string Name { get; set; }

        public string Educations { get; set; }

        public City Hometown { get; set; }

        public Page[] Languages { get; set; }

        public FacebookObjectCollection<Post> PostList { get; set; }

        public FacebookObjectCollection<Album> AlbumList { get; set; }

        public FacebookObjectCollection<User> FriendList { get; set; }

        public FacebookObjectCollection<Event> EventList { get; set; }

        public FacebookObjectCollection<Checkin> CheckinList { get; set; }

        private LoggedUserDataSingleton()
        {
        }

        public static LoggedUserDataSingleton getInstance()
        {
            if (s_Instance == null)
            {
                s_Instance = new LoggedUserDataSingleton();
            }

            return s_Instance;
        }

        public string PostStatus(string i_StatusString)
        {
            Status StatusToPost = null;

            if (i_StatusString != string.Empty)
            {
                try
                {
                    StatusToPost = m_User.PostStatus(i_StatusString);
                    return StatusToPost.Id;  
                }
                catch (Facebook.FacebookOAuthException exception)
                {
                    MessageBox.Show(string.Format("Post operation did not succeed: {0}", exception.Message), "ERROR WHILE POSTING!");
                    return "ERROR";
                }                 
            }
            else
            {
                throw new ArgumentException("Status can't be empty field");
            }
        }

        public void UpdateUserData(User i_User)
        {
            m_User = i_User;
            s_Instance.PictureNormalURL = m_User.PictureNormalURL;
            s_Instance.PictureLargeURL = m_User.PictureLargeURL;
            s_Instance.BirthdayDate = m_User.Birthday;
            s_Instance.Email = m_User.Email;
            s_Instance.Gender = m_User.Gender.ToString();
            s_Instance.RelationshipStatus = m_User.RelationshipStatus.ToString(); 
            s_Instance.Hometown = m_User.Hometown;
            s_Instance.Languages = m_User.Languages;
            s_Instance.Name = m_User.Name;
            s_Instance.PostList = m_User.Posts;
            s_Instance.AlbumList = m_User.Albums;
            s_Instance.FriendList = m_User.Friends;
            s_Instance.CheckinList = m_User.Checkins;

            ////if (m_User.InterestedIn.ToString() != null)
            ////{
            ////    s_Instance.InterestedIn = m_User.InterestedIn.ToString();
            ////}
            ////
            ////if (m_User.Educations.ToString() != null)
            ////{
            ////    s_Instance.Educations = m_User.Educations.ToString();             
            ////}
            ////
            ////if (m_User.Events != null)
            ////{
            ////    s_Instance.EventList = m_User.Events;
            ////}
        }
    }
}
