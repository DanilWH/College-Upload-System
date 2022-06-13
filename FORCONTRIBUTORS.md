# For contributors

## Updating the project on the server

In order to turn your updates into reality (that is, to apply them on the server), do the following:
- `git commit` and `git push` your changes
- connect to the server through ssh (`ssh administrator@192.168.0.94` for local connection, `ssh administrator@88.205.135.82 -p 7073` for global connection e.g. from home).
- stop the Spring Boot Application service (`sudo systemctl stop college-upload-system.service`)
- go to `~/IS-21/College-Upload-System` directory (`cd ~/IS-21/College-Upload-System`).
- now, pull your changes onto the server (`git pull origin master`)
- update the jar file (`mvn clean package`)
- start the Spring Boot Application service (`sudo systemctl start college-upload-system.service`)

## Project parameters

First name size: min - 2, max - 50;  
Last name size: min - 2, max - 50;  
Father name size: min - 4, max - 50;  
Login size: min - 1, max - 60;  
Password size: min - 8, max - 30;
