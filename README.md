# IoT simulation using Boomi

## Objective
Build a process on top of Boomi integration that can serve as a simulator and testing tool for various purposes (mqtt/database/kafka/http, etc.)

> special thanks to Chris Cappetta https://github.com/ccappetta and Premjit Mishra from Boomi who have always helped get the better of the platform!


## Project inspiration & Boomi to the rescue!

The inspiration for this project came due to a need for IOT testing tool, which can feature as a MQTT publisher. The nature of immediate testing was to generate randonmess of incremental lat/long/temperature data and publishing it at a periodic interval to a MQTT broker. With this pursuit, I ended up paho and experimenting with mqtt cli tools. However, the level of complexity in building an automated data set and then automating the publishing was too high. 

<br />

![image](https://user-images.githubusercontent.com/39495790/120280155-f8535c00-c2d4-11eb-9cbd-1463b19bef43.png)

<br />

While that being the case, this project got all sorts of weird ideas to finally become a small end-to-end showcase. The simulator process can now be gracefully used for any kind of data set generation and diversify it's application to any target connector supported by Boomi.

Boomi Integration service provides a rich featurette of connectors and integration logic inclusive of custom scripting, etc. I decided to take advantage of Boomi Integration to build a simulation tool instead of leveraging docker/linux/windows tools.
This helped me further as my target processes that need to be tested via simulation were deployed on Boomi itself

<br />

![image](https://user-images.githubusercontent.com/39495790/120336889-6ff2ac80-c310-11eb-8524-fac45c1a5465.png)

![image](https://user-images.githubusercontent.com/39495790/120276911-f4253f80-c2d0-11eb-82c2-fdf89804ec54.png)

## Prequisities
* Basic working knowledge of Boomi Integration
* Docker host preferably with 4G or 8G RAM

## Creating the environment and installation token

The below steps outline creation of an environment and a token that will be used to install our atom on docker. Ensure that you copy your environment ID and the token in notepad for further use

![image](https://user-images.githubusercontent.com/39495790/120333370-3ff5da00-c30d-11eb-8b5c-e964e786edae.png)

## Install Atom on Docker

**Assumption:** You are logged into a ubuntu host with docker installed. We will create basic directories to host the atom installation files
```
sudo su
mkdir boomi_atom
cd boomi-atom
mkdir boomi_dir
```
Create a docker-compose.yml in the boomi_atom (parent directory) with the below content. **Replace** the environment ID and installation token with your values.
```
version: '3'
services:
  test-docker-1:
    image: boomi/atom:release
    privileged: true
    environment:
      - URL=https://platform.boomi.com
      - BOOMI_ATOMNAME=test-docker-1
      - BOOMI_CONTAINERNAME=test-docker-1
      - ATOM_LOCALHOSTID=test-docker-1
      - INSTALL_TOKEN=<paste-your-token-do-not-leave-space-after-equal-sign>
      - BOOMI_ENVIRONMENTID=<paste-your-environment-id-do-not-leave-space-after-equal-sign>
      - INSTALLATION_DIRECTORY=/var/boomi
    ports:
      - "9090:9090"
    volumes:
      - "./boomi_dir:/var/boomi:consistent"
```
Once done, issue the below command to initialize the container and atom installation
```
docker-compose up -d
```
Go back to your atomsphere account > manage and observe the atom operational in the specified environment

![image](https://user-images.githubusercontent.com/39495790/120277609-e45a2b00-c2d1-11eb-9d49-359c39202423.png)

Observe that we are changing the **heap size to 2G of memory**. You can skip it in case your system does not have enough memory. Once you update the memory, accept the restart and wait till the atom comes online.

![image](https://user-images.githubusercontent.com/39495790/120337081-a03a4b00-c310-11eb-9263-bbfd22702284.png)

Create a new folder in atomsphere build service which we will use to store all our processes.

## Building our MQTT simulator process

This process will serve as a simulator to generate mock data for IoT sensor. The output of this process will be sent to a MQTT broker via a connector. This how our Boomi simulator process will look like at the end.

![image](https://user-images.githubusercontent.com/39495790/120266415-77d63080-c2bf-11eb-8a91-67b571c222f9.png)

Create a new process component. We will use this process to simulate an iterative data-set for IOT data

![image](https://user-images.githubusercontent.com/39495790/120279052-a0682580-c2d3-11eb-842d-655352532b51.png)

### Seed message shape

We use the message shape to seed a message in a flatfile format. This will be iterated by the map shape and the function embedded in it.

![image](https://user-images.githubusercontent.com/39495790/120334650-6f591680-c30e-11eb-8b52-d77659c592ce.png)

You can copy the below in to the message shape. Alternatively if you change any of the column headers, the map definitions and function will need to be re-aligned.

```
latitude,longitude,temperature
100.0001,90.0001,30.00
```
<br />

### Flatfile profiles (input-output-profile for the map function)

Within the map, we will place a function to iterate seed data. The function will be supported via input and output profile that will be passed via the function.Create a csv file on your laptop with the following contents.

<br />

```
newLatitude,newLongitude,newTemperature,newDateTime
100.0000,90.0000,30.00,20210530 090053.989
```
<br />

Create a new flatfile profile and configure the options as seen below

![image](https://user-images.githubusercontent.com/39495790/120261574-ca125400-c2b5-11eb-9d65-8e8d2d2119d9.png)

<br />

Import the flatfile created on your laptop and edit the elements as seen below

<br />

![image](https://user-images.githubusercontent.com/39495790/120265764-4d37a800-c2be-11eb-9ea6-96e7dcd0a5d8.png)

<br />

![image](https://user-images.githubusercontent.com/39495790/120265782-558fe300-c2be-11eb-8207-b0e40acdaba6.png)

<br />

> Note the format for temperature has only two decimals instead of four for lat/long

<br />

![image](https://user-images.githubusercontent.com/39495790/120265835-6a6c7680-c2be-11eb-8484-c6caddd76808.png)

<br />

Ensure the datetime format is as seen below

<br />

![image](https://user-images.githubusercontent.com/39495790/120265851-71938480-c2be-11eb-83f8-a4267f40a436.png)

<br />

### Create the map

Create a new map and choose the flatfile profile created above as input and output profile. We are going to use the same profile for input and output as we have a loop back for iterative values

![image](https://user-images.githubusercontent.com/39495790/120223141-bf78a000-c25e-11eb-905f-59cc74eea3d2.png)

Add a function of the type custom scripting and select groovy 2.4

Copy paste the below in the script section

##### The magic of GROOVY!!!

This is the main function that runs inside of the map to incrementally generate data points. Thanks to Boomi., groovy custom scripts extend the low code platform in to a powerful beast..

![image](https://user-images.githubusercontent.com/39495790/120268011-b3bec500-c2c2-11eb-8556-491cbf6d96dc.png)

Copy paste the below script.

```
import java.util.Properties;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat
import java.io.InputStream;
int intValue = 5;
for ( int i = 0; i <= intValue; i++) {
Thread.sleep(5000);
Calendar cal = Calendar.getInstance();
DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss.SSS");
datetimeoutput = dateFormat.format(cal.getTime());
latitudeoutput = latitudeinput + Math.random();
longitudeoutput = longitudeinput + Math.random();
Random rnd = new Random();
temperatureoutput = temperatureinput + Math.random() -  Math.random() ;
}
```

Create the input and output parameters

> NOTE: The input parameters for latitudeinput, latitudeoutput, temperatureinput will of FLOAT type, else the function will not be able to parse decimal values

Copy/paste the above groovy script and save. Link the input values and output values as seen in the above figure and save

### Create a decision shape

![image](https://user-images.githubusercontent.com/39495790/120356141-a4bb2f80-c321-11eb-983d-9b017ca4265d.png)

The decision shapes helps to compare two values and then determine outcomes based on true/false. Herein we will try to match our latitude value with a static value of 110 such that whenever the loop hits that threshold, the process will complete. If this is not set, the Atom will get into an infinite loop.

For the first value, select profile element of newlatitude the flatfile profile created earlier (as shown below) and then on the seccond value, give a static value of 110 or whatever you feel comfortable. 

![image](https://user-images.githubusercontent.com/39495790/120356508-08455d00-c322-11eb-8fc9-011e3d9cf68c.png)

 ### Create a Branch
 
 Branch-1 will go the respective target connector
 Branch-2 will loop back to the start of the map (as seen in the diagram)
 
 ![image](https://user-images.githubusercontent.com/39495790/120223901-f4392700-c25f-11eb-9235-a03bb0c58278.png)

### Decision (true or false)

Link decision true to the branch and false to a stop shape (deselect "continue processing....")

### MQTT publisher

> Prerequiste: a ubuntu 18.04 machine with docker installed

```
sudo su
mkdir eclipse-mosquitto && cd eclipse-mosquitto && mkdir mosquitto && cd mosquitto
```
Create a config file named mosquitto.conf with the contents pasted from this file. 
```
vi mosquitto.conf
```
paste the contents of this file url into it

https://abc.com

Once done we will navigate back to parent directory and create our docker-compose manifest
```
cd ..
vi docker-compose.yml
```
and paste the below contents
```
version: '3'
services:
  mosquitto:
    image: eclipse-mosquitto:1.6.14
    hostname: mosquitto
    container_name: mosquitto
    expose:
      - "1883"
      - "9001"
    ports:
      - "1883:1883"
      - "9001:9001"
    volumes:
      - ./mosquitto/mosquitto.conf:/mosquitto/config/mosquitto.conf
```
Once done, we will start our eclipse-mosquitto mqtt broker
```
docker-compose up -d
```
### MQTT connector
Go back to the process in atomsphere and follow the below diagram to finish the MQTT connection and close it with a STOP shape
![image](https://user-images.githubusercontent.com/39495790/120282901-3bfb9500-c2d8-11eb-9d1d-34697a07c042.png)

### TEST and OBSERVE
Next we will run a test docker container for mosquitto subscriber. Open a separate console to the ubuntu machine where we installed the broker and run the below command
```
sudo su
docker run -it --rm efrecon/mqtt-client sub  -h 172.31.0.100 -t "#" -v
```
This enables a subscriber that listens to all topics. Go back to the Atomsphere and hit "TEST" button on the simulator process and let it execute
We can observe the mosquitto_sub incrementing the output as it listens to the topic. We can observe that the last line emits a rough value of 109 latitude, beyond which the process decision shape turns to failse (threshold set at 110) and terminates the process.

![image](https://user-images.githubusercontent.com/39495790/120355234-be0fac00-c320-11eb-9148-5caa808ec2a2.png)






