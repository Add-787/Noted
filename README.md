# Noted
A simple todo application designed with offline capabilities integrated using Room and network calls performed with Retrofit. It also integrates Firebase analytics, performance monitoring and crashlytics for realtime analysis and debugging.

## Setup and Configuration Guide
You can get started on developing this application by following a few steps:
- Clone this repository
- Add this project on your firebase account.
- Download the google-services.json file and add it to the app module.

## Design
I have followed the following standard design practices for this project:
- Separation of ui and logic. The ui strictly only communicates with the viewmodel and is unaware of business logic.
- Integration of hilt for easier dependency injection.
- Usage of Jetpack compose for ui development.
- Interfaces created to abstract out the logic between network and local implementation.

## Material Design
<div align="center">
  <img src="https://github.com/Add-787/Noted/blob/main/assets/tasks-screen.jpg" width="320" height="600">
  <img src="https://github.com/Add-787/Noted/blob/main/assets/create-screen.jpg" width="320" height="600">
</div>

## Crash Detection
<div align="center">
  <img src="https://github.com/Add-787/Noted/blob/main/assets/ezgif-8f7099a57caddb.gif" width="320" height="600">
  <img src="https://github.com/Add-787/Noted/blob/main/assets/crashlytics.png" width="600" height="600">
</div>
- Crashlytics shows logs when a crash occurs in the app.

