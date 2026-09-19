![Java CI](https://github.com/HK1947/restassured-api-framework/actions/workflows/ci.yml/badge.svg)

# Rest Assured API Framework

A production-ready REST API automation framework built with Java and Rest Assured, following industry best practices.

## 🏗️ Architecture

```
restassured-api-framework/
├── src/
│   ├── main/
│   │   ├── java/com/api/
│   │   │   ├── client/           # API client layer
│   │   │   ├── config/           # Configuration management
│   │   │   ├── models/           # POJO models
│   │   │   ├── specs/            # Request/Response specs
│   │   │   └── utils/            # Utilities
│   │   └── resources/
│   │       └── config/           # Environment configs
│   └── test/
│       ├── java/com/api/tests/   # Test classes
│       └── resources/
│           ├── schemas/          # JSON schemas
│           └── testng.xml        # TestNG configuration
├── pom.xml
└── README.md
```

## 🛠️ Tech Stack

- **Language**: Java 11+
- **Build Tool**: Maven
- **API Testing**: Rest Assured 5.x
- **Testing Framework**: TestNG
- **Serialization**: Jackson
- **Reporting**: Allure
- **Assertions**: AssertJ
- **Test Data**: JavaFaker

## 🎯 Key Features

### Design Patterns
- **Builder Pattern** - Request/Response specifications
- **Singleton Pattern** - Configuration management
- **Factory Pattern** - API client creation
- **POJO Pattern** - Type-safe request/response handling

### Framework Capabilities
- ✅ Reusable API client layer
- ✅ Request/Response specifications
- ✅ POJO models with Jackson
- ✅ JSON Schema validation
- ✅ Environment-based configuration
- ✅ Allure reporting integration
- ✅ Request/Response logging
- ✅ Test data generation
- ✅ Parallel test execution

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Allure CLI (optional, for reports)

### Installation

```bash
git clone <repository-url>
cd restassured-api-framework
mvn clean install -DskipTests
```

### Running Tests

```bash
# Run all tests
mvn test

# Run smoke tests
mvn test -Dgroups=smoke

# Run with specific environment
mvn test -Denv=qa

# Run and generate Allure report
mvn test allure:serve
```

## 📁 Key Components

### ApiClient
Reusable API client encapsulating HTTP operations.
```java
ApiClient client = new ApiClient();
Response response = client.get("/users/1");
Response response = client.post("/users", userObject);
```

### Request Specifications
```java
RequestSpecification spec = RequestSpecs.getBaseSpec();
RequestSpecification authSpec = RequestSpecs.getAuthSpec(token);
```

### Response Specifications
```java
response.then().spec(ResponseSpecs.success200());
response.then().spec(ResponseSpecs.created201());
```

### POJO Models
```java
User user = User.builder()
    .name("John Doe")
    .job("Developer")
    .build();
```

### Schema Validation
```java
SchemaValidator.validateSchema(response, "users-list-schema.json");
```

## 📝 Writing Tests

### Test Structure (AAA Pattern)
```java
@Test
public void testCreateUser() {
    // Arrange
    User newUser = TestDataGenerator.generateUser();

    // Act
    Response response = apiClient.post("/users", newUser);

    // Assert
    response.then().spec(ResponseSpecs.created201());
    assertThat(response.as(User.class).getName()).isEqualTo(newUser.getName());
}
```

## 📊 Allure Reporting

```bash
# Generate and open report
mvn allure:serve

# Generate report only
mvn allure:report
```

## 🔧 Configuration

### Environment Variables
| Variable | Description | Default |
|----------|-------------|---------|
| `env` | Environment (dev/qa/prod) | dev |

### Config Files
- `api-config.yaml` - Default settings
- `api-config-dev.yaml` - Development
- `api-config-qa.yaml` - QA environment

## 👨‍💻 Author

**Harsha Kumar**

## 📄 License

MIT License
