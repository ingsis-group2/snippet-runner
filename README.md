# Snippet Runner

## Setup

1. Create `gradle.properties` following the example below:
   ```properties
   # gradle.properties
   gpr.user=<GITHUB_USERNAME>
   gpr.key=<GITHUB_TOKEN>
   ```

   Replace `<GITHUB_USERNAME>` and `<GITHUB_TOKEN>` with your GitHub username and a personal access token that has the necessary permissions.

2. Run the following command to start the application:
   ```shell
   docker compose up
   ```

## Endpoints

The Snippet Runner exposes the following endpoints:

1. **Execute Snippet**
    - **URL:** `/execute`
    - **Method:** POST
    - **Description:** Executes a code snippet with provided inputs.
    - **Request Body:**
      ```json
      {
        "content": "<code snippet>",
        "version": "<version>",
        "inputs": ["input1", "input2", "..."]
      }
      ```
    - **Response:** Returns the output and any errors from executing the snippet.

2. **Format Code**
    - **URL:** `/format`
    - **Method:** POST
    - **Description:** Formats a code snippet based on specified formatting rules.
    - **Request Body:**
      ```json
      {
        "content": "<code snippet>",
        "version": "<version>",
        "formatRules": {
          "colonBefore": false,
          "colonAfter": false,
          "assignationBefore": false,
          "assignationAfter": false,
          "printJump": 1,
          "ifIndentation": 2
        }
      }
      ```
    - **Response:** Returns the formatted code and any errors encountered during formatting.

3. **Lint Code**
    - **URL:** `/lint`
    - **Method:** POST
    - **Description:** Lints a code snippet based on specified linting rules.
    - **Request Body:**
      ```json
      {
        "content": "<code snippet>",
        "version": "<version>",
        "lintRules": {
          "enablePrintExpressions": false,
          "caseConvention": "CAMEL_CASE"
        }
      }
      ```
    - **Response:** Returns a list of violations found during linting and any errors encountered.

