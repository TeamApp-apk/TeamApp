
# TeamApp
An application that helps people find sports partners.

![app_logo](https://github.com/user-attachments/assets/37bcd1ee-3308-4f74-8f29-3ebb81ac2b61)
## Overview
**TeamApp** is a mobile application designed to connect sports enthusiasts with potential partners based on their location, interests, and activities. The app aims to make it easier for users to find others to join them in sports and fitness activities.

## Features
+ **User Profiles**: Each user creates a profile, including details such as their name, age, gender and custom avatar. The app focuses on sports, so users aren't allowed to upload their own photos. This decision comes from the observation that, historically, allowing photo uploads often shifts the app's focus towards resembling dating platforms, especially when users can create Facebook-style profiles.
+ **Location-based Matching**: The app uses location services to show users nearby sport events. All location services are built upon TomTom's Maps API, which is just as reliable as google maps, but at a lower cost.
+ **Activity Creation**: Users can create their own sport events. They can choose the details of the event: discipline, location, maximum number of participants, time, gender (whether the creator is looking for a men/woman/mixed team),  description.
+ **Chat functionality**: Every event has it's own group chat, so participants can communicate and talk about details.
+ **Authentication**: Users can sign up using an email or social account, then set up their profile with details.
+ **Advanced search**: Users can filter all their searches based on their preferences 
## Architecture 
+ **Frontend**: The app, primarily built with Kotlin, features a clean and intuitive user interface crafted using Jetpack Compose. The code adheres to Android development best practices for optimal performance and maintainability.
+ **Backend**: Backend heavily relies on cloud : **Firebase** is used to handle all backend operations, such as:
  + **Authentication**: User login and registration.
  + **Cloud Firestore**: Storing user data, Manages user profiles, event details, interactions in real-time and chat .
  + **Cloud Messaging**: Sends notifications to users about new messages, invitations to events, or other updates.
  + **Firebase Functions**: Backend logic for processing data, managing users, and syncing data.
## Technical Stack
+ **Languages**: Kotlin for the android app, firebase hadnles backend services and database management.
+ **Frameworks**:
  + **Jetpack Compose** for building user interface.
  + **Firebase SDK** for authentication, database etc.
  + **TomTom Maps SDK** for handling all location features.
  
## Future Enhancements
  + Deploying app on Google Play Store.
  + Developing a system that ensures participants do not declare attendance at an event and then fail to show up.
  + Introduce challenges where users can compete or collaborate with others, earning badges and rewards.
  + Adding skill level to search/create filters.

Currently project is on hold due to the demanding semester.
  
  

