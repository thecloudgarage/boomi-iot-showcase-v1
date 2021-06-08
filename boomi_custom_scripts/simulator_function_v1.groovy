import java.util.Properties;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat
import java.io.InputStream;
int intValue = 2;
for ( int i = 0; i <= intValue; i++) {
Thread.sleep(2000);
gpsterminalidoutput = gpsterminalidinput;
latitudeoutput = latitudeinput + Math.random();
longitudeoutput = longitudeinput + Math.random();
temperatureoutput = temperatureinput + Math.random() -  Math.random();
fueloutput = fuelinput -  Math.random();
Random rnd = new Random();
speedoutput = speedinput + rnd.nextInt(100);
Calendar cal = Calendar.getInstance();
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
timestampoutput = dateFormat.format(cal.getTime());
}
