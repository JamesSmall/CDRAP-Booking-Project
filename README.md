# CDRAP-Booking-Project
the project from the capstone class. this contains the java code, a request can be made for a blank spreadsheet that this application uses.

you will need to import the project using gradle

the project will require a copy of the following spreadsheet in your google drive. https://docs.google.com/spreadsheets/d/1HjWbWYVw1_2jDMp_ZCwh1Ykhrk-9SyKIJQnLuZcshuM/edit?usp=sharing

the application will ask for a login to a google drive (this is seperatefrom app credientials below)

the application will require credientials in order to run at all!

Using google docs https://developers.google.com/identity/protocols/OAuth2 <-you will need a gmail account, and than you will need to create an authentication file for this application The file must be named 'client_id.json' and placed into the resources directory inside eclipse (or other gradle using compiler) Please name the app "BookingApp" inside google console //that is the internal name of the application

place the credentials with the name 'client_id.json' into the resource directoy, the application WILL NOT run without it.

to import, download as zip, extract the file, than use import in eclipse and and import as IDE (other platforms that support gradle) and import gradle
