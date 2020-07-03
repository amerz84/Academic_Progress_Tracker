# Android App for WGU C196 Student Tracker

### Developed using Android Studio 3.6

## About

This is an Android mobile application developed for students attending WGU to track their academic progress. It uses the Room architecture to create a persistent SQLite database. The student information is arranged in a logical hierarchy, as follows:

- Each student is enrolled in at least one term
- Each term consists of at least one course
- Each course has at least one exam (assessment), one instructor (mentor), and zero or more notes associated with the course.


## Features

- Sharing based on device capability - email, SMS, etc.
- Device notifications/alerts - no need to be actively using application
- Full Insert/Update/Delete capability

## Instructions for Use
*Note: App needs to be installed on either an emulator or an actual Android device using Ice Cream Sandwich or more recent (minimum SDK version 15)*

1. Download .apk file from \app\release directory
2. Install on device/emulator (if using Android Studio, file can be dragged and dropped onto running emulator screen)
3. Run application
4. On the initial screen, click on the hamburger menu and select "Add Sample Data"
5. Navigate through and interact with program as desired
