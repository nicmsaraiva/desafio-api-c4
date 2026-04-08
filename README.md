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
    │       │   ├── auth/
    │       │   │   └── LoginTest.java
    │       │   ├── base/
    │       │   │   └── BaseTest.java
    │       │   └── users/
    │       │       ├── CreateUserTest.java
    │       │       ├── DeleteUserTest.java
    │       │       ├── GetUserTest.java
    │       │       └── UpdateUserTest.java
    │       ├── config/
    │       │   └── ApiConfig.java
    │       ├── enums/
    │       │   └── Endpoints.java
    │       └── utils/
    │           ├── Generator.java
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
mvn test -Dtest=CreateUserTest#createUserTestWithSuccess_thenReturnStatus200
```

**Generate HTML report:**
```bash
mvn test surefire-report:report site -DgenerateReports=false
```
Report will be available at `target/site/surefire-report.html`.

---

## 🧪 Test Cases

### 🔐 Auth — `LoginTest`

| Test | Description | Expected Status |
|---|---|---|
| `loginTestWithSuccess` | Login with valid credentials | 200 |

---

### 👤 Users — `CreateUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `createUserTestWithSuccess` | Create user with valid payload | 201 |
| `createUserWithoutEmail` | Create user without email field | 400 |
| `createUserWithInvalidEmail` | Create user with malformed email | 400 |
| `createUserWithoutPassword` | Create user without password field | 400 |
| `createUserWithoutName` | Create user without name field | 400 |
| `createUserWithoutAdministrador` | Create user without administrador field | 400 |
| `createUserWithIntegerName` | Create user with integer as name | 400 |
| `createUserWithIntegerEmail` | Create user with integer as email | 400 |
| `createUserWithIntegerPassword` | Create user with integer as password | 400 |
| `createUserWithIntegerAdministrador` | Create user with integer as administrador | 400 |

---

### 🔍 Users — `GetUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `getAllUsersWithSuccess` | List all users | 200 |
| `getUsersFilteredByName` | List users filtered by name | 200 |
| `getUsersFilteredByAdministrador` | List users filtered by administrador flag | 200 |
| `getUsersWithNoResults` | List users with filter that returns no results | 200 |
| `getUserByIdWithSuccess` | Get user by valid id | 200 |
| `getUserByInvalidId` | Get user by non-existent id | 400 |

---

### ✏️ Users — `UpdateUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `updateUserWithSuccess` | Update user with valid payload | 200 |
| `updateUserWithNonExistentId` | PUT with non-existent id creates new user | 201 |
| `updateUserWithDuplicateEmail` | Update user with already used email | 400 |
| `updateUserWithoutToken` | Update user without auth token | 401 |
| `updateUserWithInvalidToken` | Update user with invalid token | 401 |
| `updateUserWithoutEmail` | Update user without email field | 400 |
| `updateUserWithoutName` | Update user without name field | 400 |
| `updateUserWithoutPassword` | Update user without password field | 400 |

---

### 🗑️ Users — `DeleteUserTest`

| Test | Description | Expected Status |
|---|---|---|
| `deleteUserWithSuccess` | Delete existing user | 200 |
| `deleteUserWithInvalidId` | Delete user with non-existent id | 400 |
| `deleteUserAlreadyDeleted` | Delete user that was already deleted | 400 |
| `deleteUserWithoutToken` | Delete user without auth token | 401 |
| `deleteUserWithInvalidToken` | Delete user with invalid token | 401 |

---

## 🚀 CI/CD

This project uses **GitHub Actions** to run tests automatically on every push or pull request to `master` and `develop` branches.

**Pipeline steps:**
1. Checkout repository
2. Set up JDK 17
3. Run tests
4. Generate Surefire HTML report
5. Upload report as artifact

**Accessing the report:**

After each pipeline execution, go to `Actions` tab → select the run → download the `surefire-report` artifact.

## 🏗️ Architecture Decisions

### `ApiConfig`
Centralizes environment configuration by loading `config.properties` and setting `RestAssured.baseURI`. Called explicitly in `BaseTest.setup()`.

### `BaseTest`
Base class for all test classes. Holds the `@BeforeAll setup()` that configures the environment and exposes the `getAuthToken()` method using Java's native `HttpClient` — intentionally decoupled from REST Assured to keep infrastructure concerns separate from test assertions.

### `JsonBuilder`
Reads a JSON fixture file and allows field-level mutations via `with()` and `without()` methods. Keeps tests focused on what changes per scenario without rebuilding the full payload each time.

### `Generator`
Utility class for dynamic test data generation — emails (UUID-based to avoid conflicts), passwords, and alphanumeric strings of configurable length.

### `Endpoints`
Enum that centralizes all API route paths. A single place to update if routes change.

---

## 🔗 API Reference

Full API documentation: [https://serverest.dev](https://serverest.dev)

---

## 👨‍💻 Author

**Nicolas Saraiva** — Senior QA Engineer  
[LinkedIn](https://www.linkedin.com/in/nicmsaraiva/) · [GitHub](https://github.com/nicmsaraiva)
