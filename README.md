# Amazon-Predict-Worth-or-Not-Worth-with-Random-forest

**Important Note for IntelliJ IDEA Users: After cloning the project, ensure to mark the src directory as a source root. Right-click on the src folder in the Project view, select "Mark Directory as" and then choose "Sources Root".**


### Main (`Main`)
The entry point of the application.
- `Main.java` - Starts the application(need preload training data and to train, which could take a few minutes because this is a large dataset.).

## Project Structure

Below is the structure of the project, which follows the MVC architecture:

### `src/`
The source directory containing all the code.

#### `Controller/`
Handles the application logic and user interaction.

- `DataPreprocessingController.java`
- `ErrorHandlingController.java`
- `InputValidationController.java`
- `PredictionController.java`
- `ProductController.java`
- `SearchController.java`

#### `Model_Classifier/`
Contains the data structures and algorithms for the application.

- `classifier/`
    - `DecisionTree.java`
    - `RandomForest.java`
- `Instance.java`
- `Product.java`
- `ProductModel.java`
- `RandomForestModel.java`
- `Split.java`
- `TreeNode.java`

#### `View/`
Responsible for all the user interface components.

- `MainView.java`
- `PredictionResultView.java`
- `ProductDetailView.java`


### Tests (`Unit_Test`)
Contains unit tests for the application.
- `Unit_Test.java` - Includes various unit tests to ensure application reliability.


