using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace A19_Ex01_Maor_302679865_Amit_315793794
{
    public class SortByStrategy<T>
    {
        public Func<T, T, bool> compareToMethod { get; set; }

        public SortByStrategy(Func<T, T, bool> i_CompareToMethod)
        {
            compareToMethod = i_CompareToMethod;
        }

        public List<T> SortingAlgo(List<T> i_UnsortedList)
        {
            List<T> sortedList = new List<T>();
            sortedList = i_UnsortedList;

            for (int i = 0; i < sortedList.Count; i++)
            {
                for (int j = 0; j < sortedList.Count - 1; j++)
                {
                    if (compareToMethod.Invoke(sortedList[j], sortedList[j + 1]))
                    {
                        T temp = sortedList[j];
                        sortedList[j] = sortedList[j + 1];
                        sortedList[j + 1] = temp;
                    }
                }
            }

            return sortedList;
        }
    }
}
