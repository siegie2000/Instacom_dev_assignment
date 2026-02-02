Instacom Dev Assignment

Overview



Instacom Dev Assignment is an Android application developed as part of a technical test completed between

Tuesday 27 January and Monday 2 February.



I named the project Instacom\_dev\_assignment to keep things simple and clear.



The main focus of this project was functionality, API interaction, and clean user flow rather than heavy UI design. The app implements a basic social feed where users can sign up, sign in, create posts, view feeds, edit or delete their own posts, and manage their account.



API Overview

Chosen API: mockapi.io



The application uses mockapi.io as its backend API.



Why mockapi.io was selected:



Easy and fast REST API setup



Supports full CRUD operations



No backend server configuration required



Ideal for development tests and prototypes



Resources used:



users – stores user account information



news – stores posts (feed items), linked to users using a userid field



App Features \& Flow

Authentication



Simple email and password sign up



Sign in using stored credentials



Session handling via SharedPreferences



Home Page



Displays a RecyclerView showing the full feed



A Floating Action Button (FAB) allows users to create posts easily



Creating a post opens a dialog, keeping the UI clean and minimal



Account Page



Displays a CardView containing:



Profile image



User name



Settings button



Below the profile card:



RecyclerView showing only the logged-in user’s posts



Each post has a menu button:



Edit → opens a dialog where the post text can be updated



Delete → deletes the post



Settings Page



To keep the experience simple, only two actions are available:



Sign out



Clears session data



Redirects to the sign-in screen



Delete account



Deletes all posts created by the user



Deletes the user account



User must sign up again after deletion



Design Choices



UI and design kept simple and basic



Focus placed on functionality and logic



Minimal screens and dialogs



Used Instacom green as the primary color



Avoided over-design to keep the app clean and easy to use



App Architecture



The app follows a basic MVVM-style structure with clear separation between authentication flow, UI screens, state management, and network operations.



Entry \& Navigation



sign\_in and sign\_up are simple Activities used for authentication.



Once authenticated, the user is navigated to MainActivity, which acts as the main host for the app.



MainActivity manages navigation between Fragments using bottom navigation.



UI Structure

ui/

&nbsp;├── home/

&nbsp;│   ├── HomeFragment

&nbsp;│   ├── HomeViewModel

&nbsp;│   ├── news\_repository

&nbsp;│   └── adapter\_feed

&nbsp;├── account/

&nbsp;│   ├── AccountFragment

&nbsp;│   ├── AccountViewModel

&nbsp;│   ├── account\_repository

&nbsp;│   └── your\_post\_adapter

&nbsp;├── notifications/

MainActivity

sign\_in

sign\_up



Key Concepts Used



Activities



sign\_in and sign\_up handle authentication



MainActivity hosts the main app navigation



Fragments for UI screens such as Home and Account



ViewModels for managing UI state and business logic



Repositories for API and network operations



LiveData for observing data changes



RecyclerView for displaying feeds



Dialogs \& AlertDialogs for creating, editing, and deleting posts



Libraries \& Dependencies



The project uses the following dependencies:



dependencies {

&nbsp;   implementation libs.picasso



&nbsp;   implementation libs.appcompat

&nbsp;   implementation libs.material

&nbsp;   implementation libs.constraintlayout

&nbsp;   implementation libs.lifecycle.livedata.ktx

&nbsp;   implementation libs.lifecycle.viewmodel.ktx

&nbsp;   implementation libs.navigation.fragment

&nbsp;   implementation libs.navigation.ui



&nbsp;   testImplementation libs.junit

&nbsp;   androidTestImplementation libs.ext.junit

&nbsp;   androidTestImplementation libs.espresso.core

}



Key libraries used:



Picasso – for loading profile images



ViewModel \& LiveData – for state management



RecyclerView – for displaying feeds



Material Components – for UI elements like FABs and dialogs



Setup \& Run Instructions



Clone the repository:



git clone https://github.com/<your-username>/Instacom\_dev\_assignment.git





Open the project in Android Studio



Let Gradle sync complete



Run the app on:



Android Emulator, or



Physical Android device



No additional backend setup is required — the app connects directly to mockapi.io



Screenshots \& Screen Recordings



All screenshots and screen recordings of the application can be found in the:



instacom screenshots/





This folder includes:



Sign up / Sign in screens



Home feed



Create post dialog



Account page



Edit \& delete post dialogs



Settings page

