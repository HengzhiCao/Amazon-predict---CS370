# Amazon-Predict-Worth-or-Not-Worth-with-Random-forest

## Project Structure

Below is the structure of the project, which follows the MVC architecture:

### `src/MVC/java`
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

#### `Unit_Test/`
Includes unit tests for the application components.

- `Unit_Test.java`

#### `View/`
Responsible for all the user interface components.

- `MainView.java`
- `PredictionResultView.java`
- `ProductDetailView.java`

### Main (`Main`)
The entry point of the application.
- `Main.java` - Starts the application.

### Tests (`Unit_Test`)
Contains unit tests for the application.
- `Unit_Test.java` - Includes various unit tests to ensure application reliability.
### Main (`Main`)

