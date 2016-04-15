using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Text.RegularExpressions;

namespace scrapingWeatherEx
{
    class Program
    {
        static Dictionary<string, string[]> urls = new Dictionary<string, string[]>();
        static List<State> states = new List<State>();

        static void Main(string[] args)
        {
            // urls
            urls.Add("Victoria", new string[] { "http://www.bom.gov.au/vic/observations/vicall.shtml" });
            urls.Add("New South Wales", new string[] { "http://www.bom.gov.au/nsw/observations/nswall.shtml" });
            urls.Add("Tasmania", new string[] { "http://www.bom.gov.au/tas/observations/tasall.shtml" });
            urls.Add("Western Australia", new string[] { "http://www.bom.gov.au/wa/observations/waall.shtml" });
            urls.Add("South Australia", new string[] { "http://www.bom.gov.au/sa/observations/saall.shtml" });
            urls.Add("Northern Territory", new string[] { "http://www.bom.gov.au/nt/observations/ntall.shtml" });
            urls.Add("Queensland", new string[] { "http://www.bom.gov.au/qld/observations/qldall.shtml" });
            urls.Add("Antarctica", new string[] { "http://www.bom.gov.au/ant/observations/antall.shtml" });

            foreach (string key in urls.Keys)
            {
                List<Station> stations = new List<Station>();

                foreach (string url in urls[key])
                {
                    string source = Get(url);

                    MatchCollection matches = Regex.Matches(source, "<a href=\"/products/(.+?)\">(.+?)</a></th>");

                    foreach (Match m in matches)
                    {
                        Console.WriteLine(m.Groups[2].Value);
                        string stationSource = Get("http://www.bom.gov.au/products/" + m.Groups[1].Value);
                        string json = "http://www.bom.gov.au" + Regex.Match(stationSource, "<a href=\"(.+?\\.json)\">").Groups[1].Value;
                        string historicalId = Regex.Match(stationSource, "/climate/dwo/(.+?)\\.latest\\.shtml").Groups[1].Value;

                        stations.Add(new Station(m.Groups[2].Value, json, historicalId));
                    }
                }

                states.Add(new State(key, stations.ToArray()));
            }

            File.WriteAllText(AppDomain.CurrentDomain.BaseDirectory + @"\stations.json", JsonConvert.SerializeObject(states));
        }

        static string Get(string url)
        {
            WebRequest request = WebRequest.Create(url);

            using (WebResponse response = request.GetResponse())
            using (StreamReader reader = new StreamReader(response.GetResponseStream()))
                return reader.ReadToEnd();
        }
    }

    class State
    {
        public string state;
        public Station[] stations;

        public State(string state, Station[] stations)
        {
            this.state = state;
            this.stations = stations;
        }
    }

    class Station
    {
        public string city;
        public string url;
        public string historicalId;

        public Station(string city, string url, string historicalId)
        {
            this.city = city;
            this.url = url;
            this.historicalId = historicalId;
        }
    }
}
