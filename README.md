### Prerequisite:
- have docker and docker-compose install
- cd ./docker-db
- docker-compose up -d --build
  - this will run sample database in docker
- you can now just run **BaseProjectApplication** as spring application normally

### Note:
- this sample code is not yet completed, but you can look at some sample code:


### Structure:
- **base** : this is all base class that can have similar code to other similar company project (can be build into jar/sdk to use)
- |_ auth
- |_ config
- |_ constant
- |_ exception
- |_ interceptor
- |_ security
- |_ utils
- **common** : this is all common class that can be used outside of company project or anywhere is possible (can be build into jar/sdk to use)
- |_ base
- |_ exception
- |_ phonecode
- |_ utils
- **core** : this is main project code for business
- |_ config
- |_ constants
- |_ controller
- |_ mapper
- |_ model
- |_ repository
- |_ request
- |_ response
- |_ security
- |_ service
