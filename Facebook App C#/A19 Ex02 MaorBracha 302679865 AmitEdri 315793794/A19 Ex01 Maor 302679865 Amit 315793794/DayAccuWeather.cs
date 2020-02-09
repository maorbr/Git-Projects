using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class DayAccuWeather
    {
        public DateTime DateAndTime { get; private set; }

        public float Temperture { get; private set; }

        public string Description { get; private set; }

        public DayAccuWeather(XmlNode i_XmlDoc)
        {
            XmlNode temp_node = i_XmlDoc;
            XmlAttribute temp_value = temp_node.Attributes["from"];
            string dateTime = temp_value.Value;

            dateTime = dateTime.Replace("T", " ");

            DateAndTime = DateTime.Parse(dateTime);
            temp_node = i_XmlDoc.SelectSingleNode("temperature");
            temp_value = temp_node.Attributes["value"];
            Temperture = float.Parse(temp_value.Value);
            temp_node = i_XmlDoc.SelectSingleNode("symbol");
            temp_value = temp_node.Attributes["name"];
            Description = temp_value.Value;
        }

        public override string ToString()
        {
            return " " + DateAndTime.ToString() + " - " + Temperture.ToString() + " - " + Description + ".";
        }
    }
}