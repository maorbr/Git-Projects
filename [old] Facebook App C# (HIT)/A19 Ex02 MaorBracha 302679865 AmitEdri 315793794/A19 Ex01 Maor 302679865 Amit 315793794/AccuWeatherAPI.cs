using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Net;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public static class AccuWeatherAPI
    {
        private const string k_APIKey = "6de1afd5d9a1b1916aa1fc3af7c2b80f";

        private static string s_CurrentURL;

        private static XmlDocument s_XMLDocument;

        public static DailyAccuWeather GetCityAccuWeather(string i_City)
        {
            DailyAccuWeather forecast;

            s_CurrentURL = setCurrentURL(i_City);
            s_XMLDocument = getXmlDocument(s_CurrentURL);

            XmlNode xmlNode = s_XMLDocument.SelectSingleNode("weatherdata");
            forecast = new DailyAccuWeather(xmlNode);

            return forecast;
        }

        private static string setCurrentURL(string i_City)
        {
            return "http://api.openweathermap.org/data/2.5/forecast?q=" + i_City + "&mode=xml&units=metric&APPID=" + k_APIKey;
        }

        private static XmlDocument getXmlDocument(string i_CurrentURL)
        {
            XmlDocument xmlDocument;

            using (WebClient webClient = new WebClient())
            {
                string xmlContent = webClient.DownloadString(i_CurrentURL);
                xmlDocument = new XmlDocument();

                xmlDocument.LoadXml(xmlContent);
            }

            return xmlDocument;
        }
    }
}
