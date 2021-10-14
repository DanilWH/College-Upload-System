# For contributors

## Updating the project on the server

In order to turn your updates into reality (that is, to apply them on the server), do the following:
- `git commit` and `git push` your changes
- connect to the server through ssh (it's probably going to be `ssh administrator@192.168.0.94`).
- go to `~/IS-21/College-Upload-System` directory (`cd ~/IS-21/College-Upload-System`).
- now, pull your changes onto the server (`git pull origin master`)
- stop the Spring Boot Application service (`sudo systemctl stop college-upload-system.service`)
- update the jar file (`mvn clean package`)
- start the Spring Boot Application service (`sudo systemctl start college-upload-system.service`)

