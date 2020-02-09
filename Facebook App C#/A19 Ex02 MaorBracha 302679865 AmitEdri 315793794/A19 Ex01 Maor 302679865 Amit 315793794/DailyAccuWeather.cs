using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Serialization;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class DailyAccuWeather
    {
        public string Location { get; private set; }

        [XmlElement("forecast")]
        public List<DayAccuWeather> Forecast { get; private set; }

        private readonly string r_DesiredWeatherTime = "09:00:00";

        public DailyAccuWeather(XmlNode i_XmlNode)
        {
            XmlNode temp_node = i_XmlNode.SelectSingleNode("location");
            XmlAttribute temp_value;

            Location = temp_node.SelectSingleNode("name").InnerText;
            Forecast = new List<DayAccuWeather>();
            temp_node = i_XmlNode.SelectSingleNode("forecast");

            foreach (XmlNode item in temp_node)
            {
                temp_value = item.Attributes["from"];

                if (temp_value.Value.Contains(r_DesiredWeatherTime))
                {
                    Forecast.Add(new DayAccuWeather(item));
                }
            }
        }
    }
}
