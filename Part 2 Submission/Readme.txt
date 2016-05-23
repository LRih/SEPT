Tutor: Dale (12:30 PM Friday 051.06.022)

--------------------------------------------


Richard Liu s3168087

Percentage:
25%

Contribution description:
Graphing classes
Logging class
Factory method
New JUnit tests
Component diagram


--------------------------------------------


Quan Do s3443163

Percentage:
25%

Contribution description:
Incorporating new UI


--------------------------------------------


Brenton Kelly s3544115

Percentage:
25%

Contribution description:
New LeanTesting system/acceptance tests


--------------------------------------------


Marcel Nowosiak s3539505

Percentage:
25%

Contribution description:


--------------------------------------------


============================================
Location of new code
============================================
Data/ForecastFactory.java
Data/ForecastIOUtils.java
Data/OpenWeatherMapUtils.java
Model/Forecast.java
Utils/ForecastWorker.java
Utils/Log.java
Utils/TextUtils.java
View/ForecastChart.java
View/FrameLoading.java
View/LineChart.java
View/StationChart.java
View/StationForecast.java
View/Style.java


============================================
JUnit tests
============================================
Unit tests are located in Tests.java


============================================
Factory method implementation
============================================
We have implemented the factory method in this assignment to select between two
different forecast sources. Ihe class is located in "Data/ForecastFactory.java".

The motivation is it does not burden the client with how the object required is
created or even the type. This delegates the responsibility to another class which
follows the single responsibility principle.

In ForecastFactory, there is a method for getting the forecasts. The caller only
needs to provide the source and the factory takes care of the rest (i.e. how to
obtain the forecast data and create the required object representations). The
factory checks the specified source and calls different routines depending on the
source.
