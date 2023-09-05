# Pet App

Pet manager application (in developloment). This is my original application designed and created from scratch exclusively by my own. The purpose of this app is to help pet owners manage their pets. The app will be released on Play Store when it is finished.

## Features

- handy pets dashboard, pantry dashboard, 
- pet profile helps to:
   - manage your pet data (weight, dimensions, meals, water)
   - monitor your pet data (weight, height, lenght, circuit etc.) displayed with charts and lists
- manage meals:
    - set daily meals and gets remember notifications,
    - pantry supplies depends on pets meals demand
- Settings (allows to change unit and language),
- customize pet avatar using camera or gallery,
- modern UI, animations, user friendly interface
- data validation in forms

## Tech Stack

Application follows Google recommended modern app architecture, best practices, APIs.

**UI Layer:** 
  - UI elements were build using modern toolkit for building native UI which is **Jetpack Compose** with **Material Design 3**,
  - UI state is hoisted in **ViewModels** classes that holds data, expose it to the UI, and handle logic,
  - UDF pattern helps enforce separation of resposibility:
    - viewModels product UI state, contain necessary logic, handles user action and update the state,
    - UI components consume UI state and notify viewModels of user events
  - placed in ui package
    
**Data Layer:**
  - data layer is made of **repositories** each contain at the momemnt one **local data source** (in future also remote data source),
  - local data sources use:
    - SQLite with **Room** library (pets, pantry repositories),
    - **DataStore** (user settings repository),
  - repositories expose data using **Kotlin Coroutines** and **Kotlin Flows**
  - placed in data package

**Managing dependencies between components:**
  - this app use DI pattern and recommended **Hilt** library,
  - placed in di package

**Comunication between layers:**
  - **Kotlin Coroutines**,
  - **Kotlin Flows**

**Deffered tasks:**
  - schedules tasks with **WorkManager** (ex. sending daily melas notification),
  - placed in work package

## Demo
### Pet dashboard
![pet_dashboard_demo](https://github.com/legalad/PetApp/assets/109519711/7d945780-4070-4a62-9dc0-1417a6d614a6)
### Sample data dashboard
![data_dashboard_demo](https://github.com/legalad/PetApp/assets/109519711/88e029c7-a4b0-483b-9668-5fc5bf5eab49)


## Screenshots
### General
![pet_profile](https://github.com/legalad/PetApp/assets/109519711/39165688-3f76-4cdc-9cfe-de16f2d03348)
### Forms
![valid_form](https://github.com/legalad/PetApp/assets/109519711/df9da25a-7bc9-4d81-9199-b684613eff8d)
![invalid_form](https://github.com/legalad/PetApp/assets/109519711/f1b49b1a-d09f-4564-ac1b-0b1db73c3021)
![blank_form](https://github.com/legalad/PetApp/assets/109519711/4ad72bff-6b1f-4134-b9aa-0f973c790d17)

### Other
![notification_meals](https://github.com/legalad/PetApp/assets/109519711/af930540-b35e-40ba-b752-7b73dd392d58)



## Roadmap

- New features (ex. pet documents, gallery, etc.)
- Provide working with different screen sizes (especially tablets)
- Integrate with external API (currently app ony use local data source but future features demands remote data source)

## Authors

- [@legalad](https://www.github.com/legalad)
