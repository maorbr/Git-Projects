using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using FacebookWrapper;
using FacebookWrapper.ObjectModel;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public partial class MainForm : Form
    {
        private string m_YourInterestedIn = null;
        private bool m_IsFirstFetch = true;
        private LoggedUserDataSingleton m_UserData = LoggedUserDataSingleton.getInstance();
        private TravelActivityFacade m_MyTravel = null;
        private PostNotifier m_PostNotifier;

        public MainForm()
        {
            InitializeComponent();
            m_PostNotifier = new PostNotifier();
            m_PostNotifier.UpdatingReplyPostID += this.showReplyPostIdOrUpdatingReplyPostID;
        }

        protected override void OnShown(EventArgs e)
        {
            base.OnShown(e);
            
            new Thread(populateUIFromFacebookDataTab1).Start(); ///tab control1

            new Thread(populateUIFromFacebookDataTab2).Start(); ///tab control2

            new Thread(populateUIFromFacebookDataTab3).Start(); ///tab control3
        }

        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);
        }

        /// tab control1 -----------------------------------------------------------------------------------------------------------------------------------
        private void listBoxFriends_SelectedIndexChanged(object sender, EventArgs e)
        {
            displaySelectedFriend();
        }

        private void listBoxEvents_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (listBoxEvents.SelectedItems.Count == 1)
            {
                Event selectedEvent = listBoxEvents.SelectedItem as Event;
                pictureBoxEvent.LoadAsync(selectedEvent.PictureNormalURL);
            }
        }

        private void listBoxCheckins_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (listBoxCheckins.SelectedItems.Count == 1)
            {
                Checkin selectedCheckin = listBoxCheckins.SelectedItem as Checkin;
            }
        }

        private void listBoxAlbums_SelectedIndexChanged(object sender, EventArgs e)
        {
            displaySelectedAlbums();
        }

        private void buttonLinkPosts_Click(object sender, EventArgs e)
        {
            fetchPosts();
        }

        private void buttonFetchFriends_Click(object sender, EventArgs e)
        {
            fetchFriends();
        }

        private void buttonFetchEvents_Click(object sender, EventArgs e)
        {
            fetchEvents();
        }

        private void buttonFetchCheckins_Click(object sender, EventArgs e)
        {
            fetchCheckins();
        }

        private void buttonFetchAlbums_Click(object sender, EventArgs e)
        {
            fetchAlbums();
        }

        private void populateUIFromFacebookDataTab1()
        {
            new Thread(fetchPosts).Start();
            new Thread(fetchFriends).Start();
            new Thread(fetchCheckins).Start();
            new Thread(fetchAlbums).Start();
            new Thread(fetchAlbums).Start();  
            ////new Thread(fetchEvents).Start();

            PictureBoxLogin.LoadAsync(m_UserData.PictureNormalURL);
            labelLoggedIn.Invoke(new Action(() => labelLoggedIn.Text = "Logged in as " + m_UserData.Name));
            labelLoggedIn.Invoke(new Action(() => labelBirthday.Text = m_UserData.BirthdayDate));
            labelLoggedIn.Invoke(new Action(() => labelEmail.Text = m_UserData.Email));
            ////labelLoggedIn.Invoke(new Action(() => labelCity.Text = m_UserData.Hometown.ToString()));
            ////labelLoggedIn.Invoke(new Action(() => labelGender.Text = m_UserData.Gender.ToString()));
            ////labelLoggedIn.Invoke(new Action(() => labelRelationship.Text = m_UserData.RelationshipStatus.ToString()));
            ////labelLoggedIn.Invoke(new Action(() => labelEducations.Text = m_UserData.Educations.ToString()));

            m_IsFirstFetch = false;       
        }

        private void fetchAlbums()
        {
            if (!listBoxAlbums.InvokeRequired)
            {
               albumBindingSource.DataSource = m_UserData.AlbumList;
            }
            else
            {
                listBoxAlbums.Invoke(new Action(() => albumBindingSource.DataSource = m_UserData.AlbumList));
            }

            if (m_UserData.AlbumList.Count == 0 && !m_IsFirstFetch)
            {
                MessageBox.Show("No Albums to retrieve :(");
            }
        }

        private void fetchPosts()
        {
            if (listBoxPosts.Items.Count == 0)
            {
                ThePostsIterator fetchPostsIterator = new ThePostsIterator(post => post.Message != null);

                listBoxPosts.Invoke(new Action(() => listBoxPosts.DisplayMember = "Message"));

                foreach (Post post in fetchPostsIterator)
                {
                    listBoxPosts.Invoke(new Action(() => listBoxPosts.Items.Add(post)));
                }

                if (m_UserData.PostList.Count == 0 && !m_IsFirstFetch)
                {
                    MessageBox.Show("No Posts to retrieve :(");
                }
            }
        }

        private void fetchFriends()
        {
            listBoxFriends.Invoke(new Action(() => friendListBindingSource.DataSource = m_UserData.FriendList));

            if (m_UserData.FriendList.Count == 0 && !m_IsFirstFetch)
            {
                MessageBox.Show("No Friends to retrieve :(");
            }
            else
            {
                SortByComboBox.Invoke(new Action(() => SortByComboBox.Enabled = true));
            }
        }

        private void displaySelectedFriend()
        {
            if (listBoxFriends.SelectedItems.Count == 1)
            {
                User selectedFriend = listBoxFriends.SelectedItem as User;
                if (selectedFriend.PictureNormalURL != null)
                {
                    pictureBoxFriend.LoadAsync(selectedFriend.PictureNormalURL);
                }
                else
                {
                    PictureBoxLogin.Image = PictureBoxLogin.ErrorImage;
                }
            }
        }

        private void fetchEvents()
        {
            listBoxEvents.Invoke(new Action(() => listBoxEvents.Items.Clear()));
            listBoxEvents.Invoke(new Action(() => listBoxEvents.DisplayMember = "Name"));

            foreach (Event fbEvent in m_UserData.EventList)
            {
                listBoxEvents.Invoke(new Action(() => listBoxEvents.Items.Add(fbEvent)));
            }

            if (m_UserData.EventList.Count == 0 && !m_IsFirstFetch)
            {
                MessageBox.Show("No Events to retrieve !");
            }
        }

        private void fetchCheckins()
        {
            foreach (Checkin checkin in m_UserData.CheckinList)
            {
                listBoxCheckins.Invoke(new Action(() => listBoxCheckins.Items.Add(checkin.Place.Name)));
            }

            if (m_UserData.CheckinList.Count == 0 && !m_IsFirstFetch)
            {
                MessageBox.Show("No Checkins to retrieve !");
            }
        }

        private void displaySelectedAlbums()
        {
            if (listBoxAlbums.SelectedItems.Count == 1)
            {
                Album selectedAlbum = listBoxAlbums.SelectedItem as Album;
                if (selectedAlbum.PictureAlbumURL != null)
                {
                    pictureBoxAlbums.LoadAsync(selectedAlbum.PictureAlbumURL);
                }
                else
                {
                    pictureBoxAlbums.Image = pictureBoxAlbums.ErrorImage;
                }
            }
        }

        private void buttonPost_Click(object sender, EventArgs e)
        {
            this.PostStatus(this.textBoxPost.Text);
            textBoxPost.Invoke(new Action(() => textBoxPost.Clear()));
        }

        private void SortByComboBox_SelectedIndexChanged(object sender, EventArgs e)
        {
           this.orderByFriends(SortByComboBox.SelectedItem.ToString());
        }

        public void orderByFriends(string i_SortBy)
        {
            switch (i_SortBy)
            {
                case "First Name":
                    {
                        SortByStrategy<User> SortByObj = new SortByStrategy<User>((friend1, friend2) => friend1.FirstName.CompareTo(friend2.FirstName) == 1 ? true : false);
                        friendListBindingSource.DataSource = SortByObj.SortingAlgo(listBoxFriends.Items.Cast<User>().ToList());
                        break;
                    }

                case "Last Name":
                    {
                        SortByStrategy<User> sorter = new SortByStrategy<User>((friend1, friend2) => friend1.LastName.CompareTo(friend2.LastName) == 1 ? true : false);
                        friendListBindingSource.DataSource = sorter.SortingAlgo(listBoxFriends.Items.Cast<User>().ToList());
                        break;
                    }
            }
        }

        public void PostStatus(string i_Status)
        {
            var replyPostID = m_UserData.PostStatus(i_Status);
            m_PostNotifier.onUpdateReplyPostID(replyPostID);
        }

        private void showReplyPostIdOrUpdatingReplyPostID(string i_ReplyPostID)
        {
            if (i_ReplyPostID != "ERROR")
            {
                MessageBox.Show(string.Format("Status Post succeed (ID: {0})", i_ReplyPostID));
            }
            else
            {
                MessageBox.Show(string.Format("Status Post NOT succeed (ID: {0})", i_ReplyPostID));
            }
        }

        /// tab control2 -----------------------------------------------------------------------------------------------------------------------------------
        public void populateUIFromFacebookDataTab2()
        {
            pictureBoxYou.Load(m_UserData.PictureLargeURL);
        }

        private void buttonSearch_Click(object sender, EventArgs e)
        {
            searchIdealMatch();
        }

        private void comboBoxGender_SelectedIndexChanged(object sender, EventArgs e)
        {
            try
            {
                m_YourInterestedIn = (string)comboBoxGender.SelectedValue;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void searchIdealMatch()
        {
            string yourGender = m_UserData.Gender.ToString();
            string yourInterestedIn = null;
            string idealMatchGender, idealMatchInterestedIn = null;
            User idealMatchUser = null;
            MatchingResultAdapter matchAdapter;

            if (m_UserData.InterestedIn != null)
            {
                yourInterestedIn = m_UserData.InterestedIn.ToString();
            }

            foreach (User friend in m_UserData.FriendList)
            {
                idealMatchGender = friend.Gender.ToString();
                if (friend.InterestedIn != null)
                {
                    idealMatchInterestedIn = friend.InterestedIn.ToString();
                }

                if (friend.RelationshipStatus.ToString() == "Single")
                {
                    if (yourInterestedIn == idealMatchGender && yourGender == idealMatchInterestedIn)
                    {
                        if (m_UserData.Languages.ToString() == friend.Languages.ToString())
                        {
                            if (m_UserData.Hometown.ToString() == friend.Hometown.ToString())
                            {
                                idealMatchUser = friend;
                                pictureBoxHim.Load(idealMatchUser.PictureLargeURL);
                                labelFriend.Text = idealMatchUser.Name;
                                matchAdapter = new MatchingResultAdapter(m_UserData.Name, idealMatchUser.Name, idealMatchUser.Hometown.ToString());
                                MessageBox.Show("Search Found, " + matchAdapter.StringResult);
                                break;
                            }
                        }
                    }
                }
            }

            if (idealMatchUser == null)
            {
                labelFriend.Text = "Not Found!";
                MessageBox.Show("Search not Found!", "Ideal Love Match");
            }
        }

        /// tab control3 -----------------------------------------------------------------------------------------------------------------------------------
        private void populateUIFromFacebookDataTab3()
        {
            m_MyTravel = new TravelActivityFacade();
            m_MyTravel.CreateTravel();
            loadCategoryComboBox();
        }

        private void loadCategoryComboBox()
        {
            foreach (string activity in m_MyTravel.GetTravelCategory())
            {
                comboBoxActivity.Items.Add(activity);
            }
        }

        private void buttonShareActivity_Click(object sender, EventArgs e)
        {
            string statusId = m_UserData.PostStatus(textBoxActivityPost.Text);
            MessageBox.Show("ID: " + statusId + "Activity posted");
            textBoxActivityPost.Text = string.Empty;
        }

        private void buttonGetForecast_Click(object sender, EventArgs e)
        {
            try
            { 
                foreach (DayAccuWeather item in m_MyTravel.GetDailyWeather(textBoxCity.Text))
                {
                    listBoxWeather.Items.Add(m_MyTravel.GetTravelLocation() + " - " + item.ToString());
                }
            }
            catch (Exception)
            {
                MessageBox.Show("City not found", "Note!");
            }
        }

        private void comboBoxActivity_SelectedIndexChanged(object sender, EventArgs e)
        {
            ComboBox comboBox = sender as ComboBox;
            textBoxActivityPost.Text = string.Empty;
            textBoxActivityPost.Text = textBoxActivityPost.Text + comboBox.SelectedItem.ToString() + " | ";
        }

        private void listBoxFriendsJoin_DoubleClick(object sender, EventArgs e)
        {
            ListBox listBox = sender as ListBox;

            if (!textBoxActivityPost.Text.Contains(listBox.SelectedItem.ToString()))
            {
                textBoxActivityPost.Text = textBoxActivityPost.Text + (listBox.SelectedItem as User).Name + " / ";
            }
        }

        private void listBoxWeather_DoubleClick(object sender, EventArgs e)
        {
            textBoxActivityPost.Text = textBoxActivityPost.Text + Environment.NewLine + "at " + (sender as ListBox).SelectedItem.ToString();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string myTravel = "My Travel: \nTime and Location: " + listBoxWeather.SelectedItem.ToString() + "\nActivity: " + comboBoxActivity.SelectedItem.ToString() + "\nFriend join with me: " + (listBoxFriendsJoin.SelectedItem as User).Name;
            labelFinalPost.Text = myTravel;
            textBoxActivityPost.Text = myTravel;
        }
    }
}