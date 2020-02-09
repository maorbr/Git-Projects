using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class TravelActivityFacade
    {
        private DailyAccuWeather m_DailyWeather;
        private TravelActivity m_TravelActivity;

        public TravelActivityFacade()
        {
        }

        public string GetTravelLocation()
        {
            return m_DailyWeather.Location;
        }

        public List<DayAccuWeather> GetDailyWeather(string i_City)
        {
            m_DailyWeather = AccuWeatherAPI.GetCityAccuWeather(i_City);

            return m_DailyWeather.Forecast;
        }

        public void CreateTravel()
        {
            m_TravelActivity = new TravelActivity();
        }

        public List<string> GetTravelCategory()
        {
            return m_TravelActivity.travelCategory;
        }
    }
}
