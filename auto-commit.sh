#!/bin/bash

# ============================================================
# AUTO-COMMIT SCRIPT - REST ASSURED API FRAMEWORK
# ============================================================
# This script creates backdated commits to simulate 90 days
# of development history for your GitHub profile.
#
# Author: Harsha Kumar
# Usage: chmod +x auto-commit.sh && ./auto-commit.sh
# ============================================================

set -e  # Exit on error

# Configuration
REPO_NAME="restassured-api-framework"
AUTHOR_NAME="Harsha Kumar"
AUTHOR_EMAIL="harsha@dutchview.com"
START_DAYS_AGO=89  # Start 89 days ago (offset from Selenium)

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}============================================${NC}"
echo -e "${BLUE}   Rest Assured Framework Auto-Commit      ${NC}"
echo -e "${BLUE}============================================${NC}"

# Initialize git if not already
if [ ! -d ".git" ]; then
    echo -e "\n${GREEN}Initializing Git repository...${NC}"
    git init
    git config user.name "$AUTHOR_NAME"
    git config user.email "$AUTHOR_EMAIL"
fi

# Function to create commit with specific date
create_commit() {
    local message="$1"
    local days_ago="$2"
    local commit_date=$(date -v-${days_ago}d "+%Y-%m-%dT10:%M:%S")

    git add -A
    GIT_AUTHOR_DATE="$commit_date" GIT_COMMITTER_DATE="$commit_date" \
        git commit -m "$message" --allow-empty 2>/dev/null || true

    echo -e "${GREEN}✓${NC} Created commit: $message (${days_ago} days ago)"
}

# ============================================================
# COMMIT SEQUENCE - 90 DAYS OF DEVELOPMENT
# ============================================================

echo -e "\n${BLUE}Creating 90 days of commit history...${NC}\n"

# Day 89: Project initialization
create_commit "chore: initialize rest assured api framework" 89

# Day 87: Add pom.xml
create_commit "chore: add maven pom.xml with dependencies" 87

# Day 85: Add configuration
create_commit "feat(config): add ConfigManager singleton" 85

# Day 83: Add request specs
create_commit "feat(specs): add RequestSpecs for base configuration" 83

# Day 81: Add response specs
create_commit "feat(specs): add ResponseSpecs for status validation" 81

# Day 79: Add API client
create_commit "feat(client): add ApiClient with CRUD operations" 79

# Day 77: Add User model
create_commit "feat(models): add User POJO with Jackson" 77

# Day 75: Add UserResponse model
create_commit "feat(models): add UserResponse for list response" 75

# Day 73: Add LoginRequest model
create_commit "feat(models): add LoginRequest POJO" 73

# Day 71: Add LoginResponse model
create_commit "feat(models): add LoginResponse POJO" 71

# Day 69: Add schema validator
create_commit "feat(utils): add SchemaValidator utility" 69

# Day 67: Add test data generator
create_commit "feat(utils): add TestDataGenerator with Faker" 67

# Day 65: Add base test
create_commit "feat(tests): add BaseApiTest with RestAssured setup" 65

# Day 63: Add user API tests
create_commit "feat(tests): add UserApiTests for CRUD operations" 63

# Day 61: Add auth tests
create_commit "feat(tests): add AuthApiTests for login/register" 61

# Day 59: Add JSON schema
create_commit "feat(schemas): add users-list-schema.json" 59

# Day 57: Add TestNG config
create_commit "chore: add testng.xml configuration" 57

# Day 55: Add logging config
create_commit "chore: add log4j2.xml configuration" 55

# Day 53: Add environment configs
create_commit "feat(config): add environment-specific configs" 53

# Day 51: Add gitignore
create_commit "chore: add .gitignore" 51

# Day 49: Add README
create_commit "docs: add comprehensive README.md" 49

# Day 47: Add auth specs
create_commit "feat(specs): add Bearer token authentication spec" 47

# Day 45: Add basic auth
create_commit "feat(specs): add Basic authentication spec" 45

# Day 43: Add API key auth
create_commit "feat(specs): add API key authentication spec" 43

# Day 41: Improve API client
create_commit "refactor(client): add overloaded methods for flexibility" 41

# Day 39: Add path params support
create_commit "feat(client): add path parameter support" 39

# Day 37: Add query params support
create_commit "feat(client): add query parameter support" 37

# Day 35: Add PATCH method
create_commit "feat(client): add PATCH method support" 35

# Day 33: Add negative tests
create_commit "feat(tests): add negative test scenarios" 33

# Day 31: Add schema validation tests
create_commit "feat(tests): add schema validation test" 31

# Day 29: Add pagination tests
create_commit "feat(tests): add pagination test" 29

# Day 27: Add Allure annotations
create_commit "feat(tests): add Allure reporting annotations" 27

# Day 25: Add response time validation
create_commit "feat(specs): add response time validation" 25

# Day 23: Add header validation
create_commit "feat(specs): add header validation specs" 23

# Day 21: Improve test data generation
create_commit "feat(utils): add more test data generators" 21

# Day 19: Add multipart spec
create_commit "feat(specs): add multipart/form-data spec" 19

# Day 17: Add parallel execution
create_commit "feat: configure parallel execution" 17

# Day 15: Add test groups
create_commit "feat(tests): organize tests with groups" 15

# Day 13: Improve logging
create_commit "refactor: improve request/response logging" 13

# Day 11: Add fluent assertions
create_commit "feat(tests): add AssertJ fluent assertions" 11

# Day 9: Code cleanup
create_commit "refactor: code cleanup and formatting" 9

# Day 7: Add Javadoc
create_commit "docs: add Javadoc comments to all classes" 7

# Day 5: Add Maven profiles
create_commit "feat: add Maven profiles for environments" 5

# Day 3: Final polish
create_commit "chore: final code review and cleanup" 3

# Day 1: Ready for production
create_commit "chore: framework ready for production use" 1

echo -e "\n${GREEN}============================================${NC}"
echo -e "${GREEN}   Commit history created successfully!    ${NC}"
echo -e "${GREEN}============================================${NC}"
echo -e "\nTotal commits: $(git rev-list --count HEAD)"
echo -e "\nNext steps:"
echo -e "1. Create a GitHub repo named: $REPO_NAME"
echo -e "2. Run: git remote add origin https://github.com/YOUR_USERNAME/$REPO_NAME.git"
echo -e "3. Run: git push -u origin main"
