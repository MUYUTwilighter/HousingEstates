# Housing Estate
A housing estate information platform.

## Features
1. Register and login, combined with email service
2. Search, post and remove estate information
3. Add/remove estate as favourite

## Interfaces
Use HTTP to communicate (default server port: 25565).
### Login & Register
- /login/id?uid=\<User ID\>&pwd=\<Password\> Login with user ID and password
- /login/email?email=\<Email\>&pwd=\<Password\> Login with user email and password
- /register/req?name