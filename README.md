# Journal App: Backend

Spring Boot REST API for a personal journal. Users write entries tagged with a mood, sign in with a password or Google, and get a weekly mood summary by email. Admins manage users and can view all entries. The API also serves weather data from an external API, cached in Redis.

**Frontend:** [journal-frontend](https://github.com/shashwat30515/journal-frontend) (React + TypeScript) · **Live demo: [live project]:https://journal-app-frontend-lake.vercel.app/login

![App screenshot](docs/screenshot-journal.png)

## Features

- JWT authentication (1-hour tokens, checked by a custom filter) and Google OAuth2 sign-in, linked by verified email
- Role-based access: `USER` and `ADMIN`
- Journal entries with mood: `Happy`, `Sad`, `Angry`, `Anxious`
- Admin API: list users, promote or demote admins, delete users, view all entries
- Weekly scheduled email with each user's most frequent mood from the last 7 days
- External weather API integration with Redis caching
- Separate `dev` and `prod` configuration profiles
- Docker support

## Architecture

<img width="8192" height="2315" alt="Spring Boot Journal Platform Architecture-2026-11-20-070543" src="https://github.com/user-attachments/assets/c56e0fc9-69fc-4437-aa31-31108239d7b0" />


### Request flow

1. The browser calls `/api/...`. Vercel (or nginx in Docker) forwards it to the backend.
2. `jwtfilter` reads `Authorization: Bearer <token>`, validates it with the JWT utilities, and loads the user.
3. Controllers call services. Services use MongoDB for data and Redis for cached results such as weather responses.
4. Once a week the scheduler reads users who have an email, counts their moods over the last 7 days, and sends the result by email.

### Google sign-in flow

1. The frontend sends the user to Google with the app's client id and redirect URI.
2. Google redirects back to the frontend with a `code`.
3. The frontend calls `GET /public/auth/google/callback?code=...`.
4. The backend exchanges the code, checks the token's audience and verified email, finds or creates the user by email, and returns a JWT.

## Project structure

```
src/main/java/com/cosmos/journalApp/
├── apiresponse/   weatherresponse (external API response model)
├── appcache/      appcache (configuration loaded once and kept in memory)
├── config/        corsconfig, redisconfig, SpringSecurity, UserSentimentCallback
├── controller/    Admincontroller, googleAuthcontroller, journalentrycontroller,
│                  publiccontroller, usercontroller
├── entity/        users, journalentry, configjournalappentity
├── enums/         Sentiment
├── filter/        jwtfilter
├── repository/    userrepository, userrepositoryimpl, journalentryrepository,
│                  configjournalrepo
├── schedular/     schedular (weekly mood email)
├── service/       userservice, service (journal), customuserdetailserviceimpl,
│                  emailservice, redisservice, weather
├── utils/         jwtutils
└── JournalAppApplication

src/main/resources/
├── application.yml          shared settings
├── application-dev.yml      local development
└── application-prod.yml     production
```

## API overview

| Area | Method and path | Access |
|---|---|---|
| Register | `POST /public/create-user` | public |
| Login | `POST /login` | public |
| Google login | `GET /public/auth/google/callback?code=` | public |
| Profile | `GET`, `PUT`, `DELETE /user` | user |
| Journal | `GET`, `POST /journal` | user |
| Entry by id | `GET`, `PUT`, `DELETE /journal/id/{id}` | owner |
| All users | `GET /admin/all-users-get` | admin |
| Create admin | `POST /admin/create-admin-user` | admin |
| Make or remove admin | `PUT /admin/make-admin/{id}`, `PUT /admin/remove-admin/{id}` | admin |
| Delete user | `DELETE /admin/delete-user/{id}` | admin |
| All entries | `GET /admin/all-journal-entries` | admin |

_Check each path against your controllers before publishing._

## Run locally

Requirements: Java 17, Maven, MongoDB, Redis (or Docker).

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

With Docker:

```bash
docker build -t journal-backend .
docker run -p 8080:8080 --env-file .env -e SPRING_PROFILES_ACTIVE=prod journal-backend
```

### Configuration (environment variables)

| Variable | Purpose |
|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` or `prod` |
| `SPRING_DATA_MONGODB_URI` | MongoDB connection |
| `SPRING_DATA_REDIS_HOST` | Redis host |
| `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID` | Google client id |
| `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET` | Google client secret |
| `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD` | SMTP login for the weekly email |
| Weather API key | key for the external weather API (name from your `application.yml`) |

Never commit real secrets. Keep them in environment variables or an ignored `.env` file.

## Tech stack

Java 17 · Spring Boot · Spring Security · JWT · OAuth2 · MongoDB · Redis · Maven · Docker
