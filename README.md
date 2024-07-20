
**### YouTube android Application - Advanced Programming

## Overview

This project continues the development of an Android application inspired by YouTube. 
The application interacts with a Node.js server using MongoDB for user and video functionalities. 
When there is no connection to the server, the application operates locally using Room with SQLite for data storage.
Users can register, log in, view and add videos, and manage their profiles.

## Features

- **Registration Screen**: Sign up with a username, password, display name, and an optional profile picture.
- **Login Screen**: Log in using username and password.
- **Video List Screen**: Display a list of videos (similar to YouTube's homepage).
- **Video View Screen**: Play the selected video.
- **Add New Video Screen**: Allows users to add new videos.
- **Dark/Light Mode Toggle**: Switch between dark and light themes.
- **Personal Profile Page**: Accessed by a button in the lower bar.

## Running the Code
Server Setup:
1. Open your terminal in Visual Studio (or any other IDE). 
   Move to the desired directory in which you want to clone the project using the cd command, then write:
2. git clone https://github.com/TalFlint1/YouTube---Advanced-Programming---Web.git
3. move to main_ex2 branch
- git checkout main_ex2
4. run the script populateDB.js to set data and tables of mongoDB database:
- node populateDB.js
5. start the server:
- cd server
- node server.js
6. (Optional) Run both web and Android apps simultaneously:
- npm start

Android Application Setup
1. **Clone the Repository**
   Open your terminal in Android Studio
   Move to the desired directory in which you want to clone the project using cd command, then write:
2. git clone https://github.com/TalFlint1/YouTube---Advanced-Programming---Android.git
3. the branch is main_ex3 so make sure you are on this branch when you run the app
   you can get there by writing in terminal: 
- git checkout main_ex3
4. Connect an Android device or start an emulator.
   Click on the "Run" button in Android Studio.

## Instructions for using an Android Device instead of Emulator
To test the application on a real Android device instead of emulator, follow these steps:
1. Find your IP Address:
- open cmd on your computer
- type: ipconfig
- Look for the IPv4 Address under your active network connection
2. Modify the URL in RetrofitClient.java:
- Open the RetrofitClient.java file located in the com.example.youtube_android package.
- Replace 10.0.2.2 with the IPv4 address you noted from the cmd
- Example : if your IPv4 address is 192.168.1.5, the line should be changed to:
  "http://192.168.1.5:3001"
3. Modify the URL in network_security_config.xml:
- Open network_security_config.xml file located in res/xml folder
- Replace the dummy line with your actual IPv4 address
   dummy line : <domain includeSubdomains="true">192.168.1.5</domain>
4. Save the changes and rebuild the project.

## Development Process
- Task Management: Used Jira to plan and manage tasks, and assign tasks to divide the project between us.
- Version Control: Used Git with feature branches and pull requests for code review.
- UI Design: Followed Android's material design principles, using XML for layout design.
- Separation of Logic: Ensured a separation from design by implementing application logic in Java.

This Android application is our step towards a fully functional YouTube platform for Android use, 
now integrated with a dynamic backend server.**