using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using FacebookWrapper;
using FacebookWrapper.ObjectModel;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public partial class LoginForm : Form
    {
        public ApplicationLogic AppLogic { get; set; }

        public LoginForm()
        {
            InitializeComponent();
            AppLogic = new ApplicationLogic();
        }

        private void loginBtn_Click(object sender, EventArgs e)
        {
            AppLogic.LoginAndInit();
            openMainForm();
        }

        private void openMainForm()
        {
            MainForm mainForm = new MainForm();
            this.Hide();
            mainForm.ShowDialog();
            Close();
        }
    }
}
