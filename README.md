# What is this project?
Demo video player app featuring pre-loading (with optional percentage), caching and playing videos in a recycler view using Google ExoVideoPlayer.

# Main Features
1. Kotlin
2. MVVM
3. Flow
4. DataStore
5. Hilt
6. Hilt constructor injection
7. Dagger
8. Live Data
9. Coroutines
10. Navigation Components
11. View binding
12. Data binding
13. Google exo video player
14. Google exo video player Caching
15. Pre loading by optional percent of load (10% default)
16. Service
17. Room Persistence
18. Room caching
19. Retrofit2
20. Constraint Layout
21. Recycler view Using DiffUtil
22. Screen Rotation
23. Generic API response
24. Toolbar
25. Recyclre view
26. Recycler view scroll listener
27. Network monitor

# PreLoad:
### To test preload you need to folow below instructions: <br />
   **First:** Open the application with Internet connected. <br />
   **Second:** Watch the first video (DO NOT SCROLL), when you are watching, all the videos will be pre-loaded in service within 10% of their length (Exo has a minimum length). <br />
   **Third:** Disconnect from the Internet and scroll through the list. You will see all the videos will play for at least 10% <br />
   
   # Video Caching:
   Connect to the Internet and open the application, watch videos until the end, disconnect from the Internet, close the application, and reopen it. You will see that    all the videos will play without the Internet



# Architecture: MVVM
![a](https://user-images.githubusercontent.com/8142223/128475126-08940086-b459-4486-b8eb-2f95932a7260.png)

# List of Screens (Video List, Setting)
1![1](https://user-images.githubusercontent.com/8142223/170933200-720c136e-d424-49b8-9361-ecf823bd77c3.jpg)
![2](https://user-images.githubusercontent.com/8142223/170933213-ca898073-2df7-4a35-95ef-237016842a38.jpg)
![3](https://user-images.githubusercontent.com/8142223/170933226-84e47199-f310-45a9-ad9e-4de6d4721095.jpg)


# List of APIs
GET -> https://dl.dropboxusercontent.com/s/11hex4wrxwzd3pv/videos.json

# GitHub Repository
https://github.com/hajhosseinico/SmartVideoPlayer
