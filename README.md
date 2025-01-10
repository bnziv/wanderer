# Wanderer

Wanderer is an Android app designed to help users explore nearby places or search for specific locations by area. With  features like filtering by place type, viewing detailed information, and bookmarking favorite spots, Wanderer makes traveling or simply exploring easier and more convenient. The app provides information on nearby places using the Google Places API and local weather information using the OpenWeatherMap API and enables account management with Google Firestore.


## Features

- Use the Google Places API to explore nearby attractions, restaurants, and more.
- Look up places in specific locations and filter your search by place type (e.g., restaurants, parks, museums).
- View descriptions, contact details, opening hours, and reviews for each place.
- Save places to your account for quick access.
- Check the weather at your current location using the OpenWeatherMap API.
- Create/login to your account and manage your bookmarks with Google Firestore.


## Screenshots

<div style="display: flex; flex-wrap: wrap; gap: 10px; justify-content: center;">

  <img src="https://i.imgur.com/WtdJaAe.png" alt="Screenshot 1" width="30%">
  <img src="https://i.imgur.com/Bd2HvMZ.png" alt="Screenshot 2" width="30%">
  <img src="https://i.imgur.com/EXKCQxk.png" alt="Screenshot 3" width="30%">
  <img src="https://i.imgur.com/1weY6aI.png" alt="Screenshot 4" width="30%">
  <img src="https://i.imgur.com/3PL243I.png" alt="Screenshot 5" width="30%">
  <img src="https://i.imgur.com/Z0Xsg7C.png" alt="Screenshot 6" width="30%">

</div>


## How to Use

### Prequisites

 - Android Studio
 - Google Places API key
 - OpenWeatherMap API key
 - Firebase project set up for Firestore

### Setup

1. Clone the repository:
    ```
    git clone https://github.com/bnziv/wanderer.git
    cd wanderer
    ```
2. Open the project in Android Studio.
3. Add your API keys:
   - Create a file named `apikey.properties` in the `app/` directory.
   - Add the following lines to the file:
     ```
     GOOGLE_PLACES_API_KEY=your_google_places_key
     WEATHER_API_KEY=your_openweathermap_key
     ```
4. Add your Firebase configuration to the `google-services.json` file in the `app/` directory.
5. Build and run the app.


## License

Wanderer is released under the [MIT License](https://opensource.org/licenses/MIT).
