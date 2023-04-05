# Weather-App
It is a mobile application that describes:
+ Weather for specific location chosen by user,
+ Add his/her favorite locations,
+ Also can set alerts for specific locations,
  + There are two types of alerts:
      + Alarm (notification with sound),
      + Notification.

# How to use
+ First the app will request your location,
+ Second it will make you choose the way you prefer to get the location (GPS, Map),
+ Finaly you will get your picked location, current weather temperature, weather temperature for 24 hours of the day, 7 days of the week.

# Implementation
+ Android studio is used to create the application, Room for local storage for retrieving data of home (either it was from GPS or from Map) and favorite locations, Shared Preference for keeping data of locations (either home location or favourite locations)
+ There are four main screens:
  + Home screen: it shows weather forecast for user's already picked location
  + Favorite screen: it holds the favorite locations picked by user, and by selecting one of those locations, it navigates to page like home to display weather forecast of this location
  + Alert screen: in this you can set alerts for different countries, you can pick the starting date and time, ending date and time and the location you want, you can also set it as alarm (notification with sound of alarm) or notification
  + Setting screen: in this screen you can pick:
    + Location (GPS, Map)
    + Language (English, Arabic)
    + Temperature (Celsius, Kelvin, Fahrenheit)
    + Wind speed (Meter/Sec, Mile/Hour)

# Navigation through the application:
## Splash screen
![1-removebg-preview](https://user-images.githubusercontent.com/92337458/229962752-07a2f561-0f6f-43e8-85ee-f89a239d6895.png)

## Entry screen
![2-removebg-preview](https://user-images.githubusercontent.com/92337458/229962820-500d2088-cac6-483d-9d77-068d96bffec0.png)
![3-removebg-preview](https://user-images.githubusercontent.com/92337458/229962893-900342a4-8d78-4177-95f2-77748f8ae191.png)

## Home screen
![4-removebg-preview](https://user-images.githubusercontent.com/92337458/229962946-58f264c2-2ede-45e3-8d5e-cbd1f32249f0.png)

## Favorite screen
![5-removebg-preview](https://user-images.githubusercontent.com/92337458/229963154-e95000f6-d877-4110-98cc-fa31a124966a.png)
![6-removebg-preview](https://user-images.githubusercontent.com/92337458/229963266-223e7ecd-afd0-4746-a5cf-dc68f3c75109.png)

## Alert screen
![7-removebg-preview](https://user-images.githubusercontent.com/92337458/229963321-609d13be-b68c-42ee-a90e-e1f47116dd5d.png)
![8-removebg-preview](https://user-images.githubusercontent.com/92337458/229963357-462ffaa1-9e16-42a6-8128-fd8556276506.png)
![9-removebg-preview](https://user-images.githubusercontent.com/92337458/229963404-ccf202c9-348d-4aac-84fc-73599075713c.png)
![10-removebg-preview](https://user-images.githubusercontent.com/92337458/229963437-7d289058-790a-4bf2-887c-e11904f66e70.png)

## Setting screen
![11-removebg-preview](https://user-images.githubusercontent.com/92337458/229963476-7d435081-f9f3-43d9-b37e-0cf5a20b2cd2.png)
