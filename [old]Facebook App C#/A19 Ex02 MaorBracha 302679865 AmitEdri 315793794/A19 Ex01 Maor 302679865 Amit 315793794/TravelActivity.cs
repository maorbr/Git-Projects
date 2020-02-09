using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class TravelActivity
    {
        public List<string> travelCategory { get; private set; }

        public TravelActivity()
        {
            initActivityCategories();
        }

        private void initActivityCategories()
        {
            travelCategory = new List<string>();
            travelCategory.Add("Hiking");
            travelCategory.Add("Surfing");
            travelCategory.Add("Running");
            travelCategory.Add("Walking");
            travelCategory.Add("Fishing");
            travelCategory.Add("Water Ski");
            travelCategory.Add("Snow Ski");
            travelCategory.Add("Snapling");
            travelCategory.Add("Diving");
        }
    }
}
