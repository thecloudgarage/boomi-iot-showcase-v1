import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Properties;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat
import java.io.InputStream;
import groovy.time.*;

// gpsterminalidinput
// latitudeinput
// longitudeinput
// temperatureinput
// fuelinput

// gpsterminalidoutput
// latitudeoutput
// longitudeoutput
// incrementaldistance
// temperatureoutput
// speed
// fueldiff
// fueltotal

int intValue = 0;
for ( int i = 0; i <= intValue; i++) {

// Set initial time & Induce delay for every loop
sleepTimer = 6000;
Thread.sleep(sleepTimer);

// Relay the GPS terminal id as-is
gpsterminalidoutput = gpsterminalidinput;

// Generate the next latitude longitude for the loop
latitudeoutput = latitudeinput + (Math.random() / 1000);
longitudeoutput = longitudeinput + (Math.random() / 1000);

// Calculate distance travelled based on old and new values of lat,long
  
// The math module contains a function
// named toRadians which converts from
// degrees to radians.
lon1 = Math.toRadians(longitudeinput);
lon2 = Math.toRadians(longitudeoutput);
lat1 = Math.toRadians(latitudeinput);
lat2 = Math.toRadians(latitudeoutput);
 
// Haversine formula
double dlon = lon2 - lon1;
double dlat = lat2 - lat1;
double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2),2);
double c = 2 * Math.asin(Math.sqrt(a));

// Radius of earth in kilometers. Use 3956
// for miles
double r = 6371;

// calculate the result
incrementaldistance = c * r;

// Generate random incremental or decremental temperature values
temperatureoutput = temperatureinput + Math.random() -  Math.random();

// Calculate time difference in hours
double timediffHours = sleepTimer / 3600000;

// Finally calculate speed
speedoutput = incrementaldistance / timediffHours;
 
// Calculate fuel

// Assume a varying fuel efficiency
double fuelefficiency = 10.00 + Math.random() -  Math.random();

// Calculate amount of fuel required for the incremental distance
fueldiffoutput = incrementaldistance / fuelefficiency;

// Calculate total fuel consumed
if (fueldiffinput != null) {
fueltotaloutput = fueldiffinput +  fueldiffoutput;
}
else {
fueltotaloutput = fueldiffoutput;
};

// Set incremental timestamp
Calendar cal = Calendar.getInstance();
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
timestampoutput = dateFormat.format(cal.getTime());
}
