# Desafio API - Test Automation Suite

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![REST Assured](https://img.shields.io/badge/REST--Assured-5.4.0-4A90D9?style=flat-square)
![JUnit5](https://img.shields.io/badge/JUnit-5.10.2-25A162?style=flat-square&logo=junit5&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=flat-square&logo=apachemaven&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)

Automation test suite for the [ServeRest](https://serverest.dev) REST API, covering user management endpoints with positive and negative scenarios.

---

## 🗂 Project Structure

```
src/
└── test/
    ├── java/
    │   └── com/nicmsaraiva/
    │       ├── api/
    │       │   ├── base/
    │       │   │   └── BaseTest.java
    │       │   └── users/
    │       │       ├── CreateUserTest.java
    │       │       ├── DeleteUserTest.java
    │       │       ├── GetUserTest.java
    │       │       └── UpdateUserTest.java
    │       ├── config/
    │       │   └── APIConfig.java
    │       ├── enums/
    │       │   └── Endpoints.java
    │       └── utils/
    │           ├── TestDataGenerator.java
    │           └── JsonBuilder.java
    └── resources/
        ├── config.properties
        ├── payloads/
        │   ├── create-user.json
        │   ├── login.json
        │   └── update-user.json
```

---

## ⚙️ Prerequisites

- Java 17+
- Maven 3.x

---

## 🔧 Environment Setup

**1. Clone the repository:**
```bash
git clone https://github.com/nicmsaraiva/desafio-api-c4.git
cd desafio-api-c4
```

**2. Configure the environment:**

Edit `src/test/resources/config.properties`:
```properties
base.uri=https://serverest.dev
```

**3. Install dependencies:**
```bash
mvn clean install -DskipTests
```

---

## ▶️ Running the Tests

**Run all tests:**
```bash
mvn test
```

**Run a specific test class:**
```bash
mvn test -Dtest=CreateUserTest
```

**Run a specific test method:**
```bash
mvn test -Dtest=CreateUserTest#shouldCreateUserSuccessfully
```

**Generate HTML report:**
```bash
mvn site -DskipTests
```
Report will be available at `target/site/surefire-report.html`.

---

## 🧪 Test Cases

### 👤 Users — `CreateUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `shouldCreateUserSuccessfully` | Create user with valid payload | 201 |
| `shouldReturn400WhenEmailIsMissing` | Create user without email field | 400 |
| `shouldReturn400WhenEmailIsInvalid` | Create user with malformed email | 400 |
| `shouldReturn400WhenPasswordIsMissing` | Create user without password field | 400 |
| `shouldReturn400WhenNameIsMissing` | Create user without name field | 400 |
| `shouldReturn400WhenAdministradorIsMissing` | Create user without administrador field | 400 |
| `shouldReturn400WhenNameIsInteger` | Create user with integer as name | 400 |
| `shouldReturn400WhenEmailIsInteger` | Create user with integer as email | 400 |
| `shouldReturn400WhenPasswordIsInteger` | Create user with integer as password | 400 |
| `shouldReturn400WhenAdministradorIsInteger` | Create user with integer as administrador | 400 |
| `shouldReturn400WhenEmailIsAlreadyInUse` | Create user with duplicate email | 400 |
| `shouldReturnCreatedUserId` | Verify response includes generated _id | 201 |
| `shouldPersistUserDataAfterCreation` | GET after POST confirms data was saved | 200 |
| `shouldCreateUserWithAdministradorFalse` | Create non-admin user | 201 |
| `shouldReturn400WhenBodyIsEmpty` | Send empty JSON body | 400 |

---

### 🔍 Users — `GetUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `shouldReturnAllUsers` | List all users | 200 |
| `shouldReturnUsersFilteredByName` | List users filtered by name | 200 |
| `shouldReturnUsersFilteredByEmail` | List users filtered by email | 200 |
| `shouldReturnUsersFilteredByAdministrador` | List users filtered by administrador flag | 200 |
| `shouldReturnUsersFilteredByMultipleParams` | List users filtered by name, email and administrador | 200 |
| `shouldReturnEmptyWhenAdministradorFilterHasNoMatch` | Combined filters with no results | 200 |
| `shouldReturnEmptyListWhenNoUsersMatch` | List users with filter that returns no results | 200 |
| `shouldReturnUserById` | Get user by valid id | 200 |
| `shouldReturnCorrectFieldsForCreatedUser` | Verify all fields match created user data | 200 |
| `shouldReturn400WhenIdDoesNotExist` | Get user by non-existent id | 400 |
| `shouldReturn400WhenIdHasInvalidLength` | Get user by id with wrong length | 400 |
| `shouldReturnUserListWhenIdIsEmpty` | GET with empty id returns user list | 200 |
| `shouldIgnoreUnknownQueryParam` | Unknown query param does not break the request | 200 |

---

### ✏️ Users — `UpdateUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `shouldUpdateUserSuccessfully` | Update user with valid payload | 200 |
| `shouldCreateUserWhenIdDoesNotExist` | PUT with non-existent id creates new user | 201 |
| `shouldReturn400WhenEmailIsAlreadyInUse` | Update user with already used email | 400 |
| `shouldReturn400WhenEmailIsMissing` | Update user without email field | 400 |
| `shouldReturn400WhenEmailIsInvalid` | Update user with malformed email | 400 |
| `shouldReturn400WhenNameIsMissing` | Update user without name field | 400 |
| `shouldReturn400WhenPasswordIsMissing` | Update user without password field | 400 |
| `shouldReturn400WhenAdministradorIsMissing` | Update user without administrador field | 400 |
| `shouldReturn400WhenNameIsInteger` | Update user with integer as name | 400 |
| `shouldReturn400WhenEmailIsInteger` | Update user with integer as email | 400 |
| `shouldReturn400WhenPasswordIsInteger` | Update user with integer as password | 400 |
| `shouldReturn400WhenAdministradorIsInteger` | Update user with integer as administrador | 400 |
| `shouldKeepUpdatedDataAfterUpdate` | GET after PUT confirms data was updated | 200 |

---

### 🗑️ Users — `DeleteUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `shouldDeleteUserSuccessfully` | Delete existing user | 200 |
| `shouldReturn200WithNoRecordWhenIdIsInvalid` | Delete user with non-existent id | 200 |
| `shouldReturn200WithNoRecordWhenUserIsAlreadyDeleted` | Delete user that was already deleted | 200 |
| `shouldNotFindUserAfterDeletion` | GET after DELETE confirms user was removed | 400 |
| `shouldReturn200WhenIdHasSpecialCharacters` | Delete with special characters in id | 200 |

---

## 🚀 CI/CD

This project uses **GitHub Actions** to run tests automatically on every push or pull request to `master` and `develop` branches.

**Pipeline steps:**
1. Checkout repository
2. Set up JDK 17
3. Cache Maven dependencies
4. Create config.properties
5. Run tests
6. Generate Surefire HTML report
7. Upload report as artifact

**Accessing the report:**

After each pipeline execution, go to `Actions` tab → select the run → download the `surefire-report` artifact → open `surefire-report.html`.

---

## 🏗️ Architecture Decisions

### `ApiConfig`
Centralizes environment configuration by loading `config.properties` (with caching) and setting `RestAssured.baseURI`. Uses try-with-resources for safe stream handling.

### `BaseTest`
Base class for all test classes. Holds a shared `RequestSpecification` (`requestSpec`) with content type, OAuth2 authentication, and conditional logging (`log().ifValidationFails()`), eliminating repeated setup across tests. Exposes `getAuthToken()` (with caching) using Java's native `HttpClient` — intentionally decoupled from REST Assured to keep infrastructure concerns separate from test assertions. Provides `createUser()` overloads and `deleteUser()` for test data management.

### `JsonBuilder`
Reads a JSON fixture file and allows field-level mutations via `with()` and `without()` methods. Uses a shared `ObjectMapper` instance. Keeps tests focused on what changes per scenario without rebuilding the full payload each time.

### `TestDataGenerator`
Utility class for dynamic test data generation — emails (UUID-based to avoid conflicts), passwords, and alphanumeric strings using `ThreadLocalRandom`.

### `Endpoints`
Enum that centralizes all API route paths. A single place to update if routes change.

---

## 🔗 API Reference

Full API documentation: [https://serverest.dev](https://serverest.dev)

---

## 👨‍💻 Author

**Nicolas Saraiva** — Senior QA Engineer
[LinkedIn](https://www.linkedin.com/in/nicmsaraiva/) · [GitHub](https://github.com/nicmsaraiva)
