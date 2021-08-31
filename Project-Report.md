<h1 align = "center">My Expenses</h1>
<h4 align = "right">CREATED BY - Rachit Agrawal</h4>

<h2>Idea Behind the App</h2>
<p>The aim of the app is to help users maintain a track of the amount of money they spend each day effectively and easily in their smart phones. The idea behind the app was motivated by the amount of money a student spends in his college days. A college student generally has a very tight budget and he has to manage various things with it. This app was designed to help a student maintain his/her daily expenses and also analyse the amount of money spent over a certain period of time.</p>

<h2> Overview of the App </h2>
![navigation](https://user-images.githubusercontent.com/34854802/131551506-af13797a-b5f5-4732-bd08-7f0f332fbe65.jpeg)
<p>The app consists of two sections "Daily Expenses" and "Analysis" which can be selected through the button on the top left corner of the app. By default the app is opened with the "Daily Expenses" section. The features of each section are described in detail below.</p>
<h3>Daily Expenses</h3>
![daily](https://user-images.githubusercontent.com/34854802/131551844-f4ba7f77-7242-44b4-b168-fdbb6c326bc8.jpeg)
![add](https://user-images.githubusercontent.com/34854802/131551970-b885d577-d53f-46cd-a2b7-16741431e267.jpeg)
<p>This section shows the expenses of a user on a specific date. The various options available to the user are as follows:</p>
  <ul>
    <li>The "SELECT DATE" option enables the user to select the date for which the expenses are shown.</li>
    <li>The "TOTAL" shows the total amount of money spent on a particular day.</li>
    <li>A particular expense can be deleted by swiping it either left or right.</li>
    <li>A particular expense can be clicked to edit it</li>
    <li>The "DELETE ALL" button deletes all the expenses on that day.</li>
    <li>The "Plus" button on the bottom right corner enables the user to add a new expense to the app.</li>
  </ul>

<h3>Analysis</h3>
![graph](https://user-images.githubusercontent.com/34854802/131551913-6d75d7c4-1ef6-424d-8a4c-8adefb96b5c5.jpeg)
<p>This section shows the graph of the total amount of money spent each day by a user during a specific time period as selected by the user. The various options available to the user are as follows:</p>
  <ul>
    <li>The "Start Date" and "End date" option enables the user to select the time period.</li>
    <li>A point on the graph can be clicked to display the total amount of money spent on that date.</li>
  </ul>


<h2>Implementation</h2>
<p> The app is built using Android Studio. Navigation Drawer is used to create the navigation menu to select either of the two options "Daily Expenses" or "Analysis". Each of these options are implemented using fragments and intents are used to transfer data between various activities and fragments. A <strong>MVVM (Model-View-ViewModel)</strong> model is used in this application. Room is used to store the details of each expense in a table in a SQLite database. Following the MVVM model, an Entity, a Database, a Dao, a Repository and a ViewModel are used. To display the various expenses Recycler View is used. To plot the "Analysis" graph, GraphView is used. 

<h2>Challenges Faced During Implementation</h2>
<p>There were quite a few challenges faced during the implementation of the app. Retreiving data from the room database and displaying it in recycler view and passing it among various activities and fragments played a major role in the app. Storing and performing operations on the date on which an expense is added was a bit challenging. Plotting the graph using GraphView, and the data from the room database, was also a challenging task.</p>

<h2>Learnings from the Project</h2>
<p>I learnt a great deal while making this app. First of all I was able to learn the basic concepts of android development such as activities, fragments, inputs, outputs, lifecycle of activities and fragments, etc. and the working of an android application itself. I learnt implementing the MVVM model and using the room database. I learnt using GraphView to plot graphs. Moreover, I understood the importance of making efficient applications without any memory leaks and with least resource utilisation, for example, executing some tasks on a parallel thread instead of executing them on main thread using AsyncTasks. After making this application I was able to understand about the working of android applications and am yet to discover its vastness.</p>
