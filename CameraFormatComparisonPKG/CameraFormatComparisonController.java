package CameraFormatComparisonPKG;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.util.Duration;
import java.text.DecimalFormat;

public class CameraFormatComparisonController {
    @FXML ComboBox<String> inputFormatList, outputFormatList;
    @FXML Label inputFormatReq, inputDimensions, inputFocalLengthReq, inputApertureValueReq, distanceReadout;
    @FXML Label outputFormatReq, outputDimensions, outputFocalLength, outputApertureValue, hAOV, hFOVFeet, hFOVMeters;
    @FXML Label vAOV, vFOVFeet, vFOVMeters, dAOV, dFOVFeet, dFOVMeters, totalDOFDisplay, nearDOFDisplay, farDOFDisplay;
    @FXML Label imaxLensDisplay, fullFrameLensDisplay, s35LensDisplay, s16LensDisplay, s8LensDisplay, iPhoneLensDisplay;
    @FXML TextField inputFocalLength, inputApertureValue;
    @FXML Slider distanceSlider;
    @FXML Button calculateButton, resetButton;

    private double focalLength;
    private double apertureValue;
    private int inputSelectionIndex;
    private int outputSelectionIndex;
    private double initialDistanceSlider;
    private final int SMALLEST_APERTURE_VALUE = 90;     //A diaphragm smaller than this is virtually impossible.
                                                        //The size and value of the aperture are inversely proportional
    private final int LONGEST_FOCAL_LENGTH = 10000;     //A 10,000mm lens is absurd. Anything above 200mm is specialty
    private double inputDistanceValue;
    private Format format;
    private FormatList formatList = new FormatList();



    //Store values of label prompt text to reset label values
    private String initialInputFormatPromptText, initialOutputFormatPromptText, initialInputFocalLength,
            initialOutputFocalLength, initialInputApertureValue, initialOutputApertureValue, initialDistanceValue,
            initialTotalDOFDisplay, initialNearDOFDisplay, initialFarDOFDisplay, initialHAOV, initialVAOV, initialDAOV,
            initialHFOVFeet, initialVFOVFeet, initialDFOVFeet, initialHFOVMeters, initialVFOVMeters, initialDFOVMeters,
            initialImaxLensDisplay, initialFullFrameLensDisplay, initialS35LensDisplay, initialS16LensDisplay,
            initialS8LensDisplay, initialIPhoneLensDisplay;


    public void initialize() {
        //Store values for required input fields
        storeRequiredInputValues();

        //Synchronize distance slider thumb value with distanceReadout display value
        updateDistanceSlider();

        //Fill the "inputFormatList" combobox with names of formats defined within the FormatList class
        generateComboBoxListValues(inputFormatList);

        //Fill the "outputFormatList" combobox with names of formats defined within the FormatList class
        generateComboBoxListValues(outputFormatList);
    }


/*
Method: storeRequiredInputValues()
Description: Store the default input combobox, textfield, and slider values that the UI launches with. Only applies to
             controls that the user specifies in the usage of the application.
Input: None
Output: Declares values for class-level strings.
Returns: Nothing (void)
*/
    private void storeRequiredInputValues(){
        initialInputFormatPromptText = inputDimensions.getText();
        initialOutputFormatPromptText = outputDimensions.getText();
        initialInputFocalLength = inputFocalLength.getText();
        initialOutputFocalLength = outputFocalLength.getText();
        initialInputApertureValue = inputApertureValue.getText();
        initialOutputApertureValue = outputApertureValue.getText();
        initialDistanceValue = distanceReadout.getText();
        initialTotalDOFDisplay = totalDOFDisplay.getText();
        initialNearDOFDisplay = nearDOFDisplay.getText();
        initialFarDOFDisplay = farDOFDisplay.getText();
        initialHAOV = hAOV.getText();
        initialVAOV = vAOV.getText();
        initialDAOV = dAOV.getText();
        initialHFOVFeet = hFOVFeet.getText();
        initialVFOVFeet = vFOVFeet.getText();
        initialDFOVFeet = dFOVFeet.getText();
        initialHFOVMeters = hFOVMeters.getText();
        initialVFOVMeters = vFOVMeters.getText();
        initialDFOVMeters = dFOVMeters.getText();
        initialImaxLensDisplay = imaxLensDisplay.getText();
        initialFullFrameLensDisplay = fullFrameLensDisplay.getText();
        initialS35LensDisplay = s35LensDisplay.getText();
        initialS16LensDisplay = s16LensDisplay.getText();
        initialS8LensDisplay = s8LensDisplay.getText();
        initialIPhoneLensDisplay = iPhoneLensDisplay.getText();
        initialDistanceSlider = distanceSlider.getValue();
    }


/*
Method: updateDistanceSlider()
Description: Update the value of the distance slider display dynamically as long as the program runs. Value is generated
             by the slider control. Distance value is formatted with decimalFormat, defined below as "#0.00 feet". With
             the exception of the first digit, this pattern will always display numbers, even if the value of that
             number is 0. The first digit will disappear if the value is 0. the String inputDistance is stored at the
             class level. A timeline animation is used to continuously update the distanceReadout text at a frequency of
             once per 10 milliseconds.
Input: None
Output: Updates the distanceReadout label
Returns: Nothing (void)
*/
    private void updateDistanceSlider(){
        Timeline timelineAnimation = new Timeline(
                new KeyFrame(Duration.millis(10),
                        e -> {
                            String pattern = "#0.00 feet";
                            DecimalFormat decimalFormat = new DecimalFormat(pattern);
                            String inputDistance = decimalFormat.format(distanceSlider.getValue());
                            distanceReadout.setText(inputDistance);
                        }
                )
        );

        //Repeat animation for as long as the program runs
        timelineAnimation.setCycleCount(Timeline.INDEFINITE);
        timelineAnimation.play();
    }

/*
Method: generateComboBoxListValues()
Description: Fill the format list ComboBoxes with names of formats defined within the FormatList class
Input: ComboBox of type String
Output: See description
Returns: Nothing (void)
*/
    private void generateComboBoxListValues(ComboBox<String> formatDropDownList){
        for (int i = 0; i < formatList.getCount(); i++){
            format = formatList.getFormat(i);
            formatDropDownList.getItems().add(format.getDesc());
        }
    }

/*
Method: validate()
Description: Gathers and validates the information from across the user-accessible form. Validates from the bottom-up,
             right-to-left (reverse order) to place focus on the highest-level invalid item first, according to Latin
             script.
Issue: Invalid hex character entries for Focal Length and Aperture Value accepted. For example "3d" is considered valid
Input: None
Output: Updates several labels and (potentially) changes visibility on "Required" labels
Returns: Nothing (void)
*/
    private void validate(){
        //Make all "Required" warning labels invisible
        inputFormatReq.setVisible(false);
        outputFormatReq.setVisible(false);
        inputFocalLengthReq.setVisible(false);
        inputApertureValueReq.setVisible(false);

        //Check for valid, non-negative, aperture value
        //Aperture values cannot be negative for a converging lens
        try {
            apertureValue = Double.parseDouble(inputApertureValue.getText());
            if (apertureValue <= 0 || apertureValue > SMALLEST_APERTURE_VALUE){
                throw new NumberFormatException();
            }
            inputApertureValueReq.setVisible(false);
        } catch (NullPointerException e){
            //Make associated "Required" warning label visible
            inputApertureValueReq.setVisible(true);
            inputApertureValue.setText(initialInputApertureValue);
            inputApertureValue.requestFocus();
        } catch (NumberFormatException e) {
            //Make associated "Required" warning label visible
            inputApertureValueReq.setVisible(true);
            inputApertureValue.setText(initialInputApertureValue);
            inputApertureValue.requestFocus();
        }

        //Check for valid, non-negative, focal length value
        //Focal lengths cannot be negative for a converging lens
        try {
            focalLength = Double.parseDouble(inputFocalLength.getText());

            if (focalLength <= 0 || focalLength > LONGEST_FOCAL_LENGTH){
                throw new NumberFormatException();
            }
            inputFocalLengthReq.setVisible(false);
        } catch (NullPointerException e){
            //Make associated "Required" warning label visible
            inputFocalLengthReq.setVisible(true);
            inputFocalLength.setText(initialInputFocalLength);
            inputFocalLength.requestFocus();
        } catch (NumberFormatException e) {
            //Make associated "Required" warning label visible
            inputFocalLengthReq.setVisible(true);
            inputFocalLength.setText(initialInputFocalLength);
            inputFocalLength.requestFocus();
        }

        //The outputDimensions label is updated to match the width and height of the selected output format
        try {
            //getSelectedIndex() returns -1 if no item is selected or if currently selected item is not in the list
            outputSelectionIndex = outputFormatList.getSelectionModel().getSelectedIndex();
            if (outputSelectionIndex < 0){
                throw new Exception();
            }
            outputDimensions.setText(formatList.getFormat(outputSelectionIndex).getDimensions());
        } catch (Exception e) {
            //Make associated "Required" warning label visible
            outputFormatReq.setVisible(true);
            outputFormatList.requestFocus();
        }

        //The inputDimensions label is updated to match the width and height of the selected input format
        try {
            //getSelectedIndex() returns -1 if no item is selected or if currently selected item is not in the list
            inputSelectionIndex = inputFormatList.getSelectionModel().getSelectedIndex();
            if (inputSelectionIndex < 0){
                throw new Exception();
            }
            inputDimensions.setText(formatList.getFormat(inputSelectionIndex).getDimensions());
        } catch (Exception e) {
            //Make associated "Required" warning label visible
            inputFormatReq.setVisible(true);
            inputFormatList.requestFocus();
        }
    }

/*
Method: calculatePressed()
Description: When the "Calculate" button is pressed, this method calls validate() method, then calls methods calculate()
             method, then calls update() method.
Input: ActionEvent from button press
Output: Updates all labels and (potentially) changes visibility on "Required" labels
Returns: Nothing (void)
*/

    public void calculatePressed(ActionEvent actionEvent) {
        //Validate form input
        validate();

        //Collect values and pass them to FormatCalculations
        FormatCalculations matchTheseFormats = new FormatCalculations(
                inputSelectionIndex, outputSelectionIndex, focalLength, apertureValue, distanceSlider.getValue());


        //Calculate Values for User Selected Format


        //Update all display labels with the appropriate values

            //Depths of Field
            String labelPattern = "###0.00";
            DecimalFormat labelFormat = new DecimalFormat(labelPattern);

                //Far Depth of Field Label
                String farDoFBeforeComma = labelFormat.format(matchTheseFormats.getFarDepthOfFieldInFeet());
                String farDoFAfterComma = labelFormat.format(matchTheseFormats.getFarDepthOfFieldInMeters());
                farDOFDisplay.setText(farDoFBeforeComma + " feet, " + farDoFAfterComma + " meters");

                //Near Depth of Field Label
                String nearDoFBeforeComma = labelFormat.format(matchTheseFormats.getNearDepthOfFieldInFeet());
                String nearDoFAfterComma = labelFormat.format(matchTheseFormats.getNearDepthOfFieldInMeters());
                nearDOFDisplay.setText(nearDoFBeforeComma + " feet, " + nearDoFAfterComma + " meters");

                //Total Depth of Field Label
                String totalDoFBeforeComma = labelFormat.format(matchTheseFormats.getTotalDepthOfFieldInFeet());
                String totalDoFAfterComma = labelFormat.format(matchTheseFormats.getTotalDepthOfFieldInMeters());
                totalDOFDisplay.setText(totalDoFBeforeComma + " feet, " + totalDoFAfterComma + " meters");

            //Angles of View (input format)
                //Horizontal Angle of View
                String horizontalAngleOfView = labelFormat.format(matchTheseFormats.getHorizontalAngleOfView());
                hAOV.setText(horizontalAngleOfView);

                //Vertical Angle of View
                String verticalAngleOfView = labelFormat.format(matchTheseFormats.getVerticalAngleOfView());
                vAOV.setText(verticalAngleOfView);

                //Diagonal Angle of View
                String diagonalAngleOfView = labelFormat.format(matchTheseFormats.getDiagonalAngleOfView());
                dAOV.setText(diagonalAngleOfView);

            //Fields of View (input format)
                //Horizontal Field of View (Imperial)
                String horizontalFieldOfViewFeet = labelFormat.format(matchTheseFormats.getHorizontalFieldOfViewFeet());
                hFOVFeet.setText(horizontalFieldOfViewFeet);

                //Vertical Field of View (Imperial)
                String verticalFieldOfViewFeet = labelFormat.format(matchTheseFormats.getVerticalFieldOfViewFeet());
                vFOVFeet.setText(verticalFieldOfViewFeet);

                //Diagonal Field of View (Imperial)
                String diagonalFieldOfViewFeet = labelFormat.format(matchTheseFormats.getDiagonalFieldOfViewFeet());
                dFOVFeet.setText(diagonalFieldOfViewFeet);

                //Horizontal Field of View (Metric)
                String horizontalFieldOfViewMeters = labelFormat.format(matchTheseFormats.getHorizontalFieldOfViewMeters());
                hFOVMeters.setText(horizontalFieldOfViewMeters);

                //Vertical Field of View (Metric)
                String verticalFieldOfViewMeters = labelFormat.format(matchTheseFormats.getVerticalFieldOfViewMeters());
                vFOVMeters.setText(verticalFieldOfViewMeters);

                //Diagonal Field of ViewMeters (Metric)
                String diagonalFieldOfViewMeters = labelFormat.format(matchTheseFormats.getDiagonalFieldOfViewMeters());
                dFOVMeters.setText(diagonalFieldOfViewMeters);

            //Output Labels
                //Output ComboBox Format Equivalent Lens
                double outputComboBoxFocalLengthValue = matchTheseFormats.calculateEquivalentLens(
                        matchTheseFormats.getHorizontalAngleOfView(), matchTheseFormats.getOutputFormatWidth());
                String outputComboBoxFocalLengthString = labelFormat.format(outputComboBoxFocalLengthValue);
                outputFocalLength.setText(outputComboBoxFocalLengthString + "mm");
                outputFocalLength.setOpacity(1.0);

                //Output ComboBox Format Equivalent Aperture Value
                String apertureLabelPattern = "f/##0.0";
                DecimalFormat apertureLabelFormat = new DecimalFormat(apertureLabelPattern);
                String outputComboBoxAperture = apertureLabelFormat.format(matchTheseFormats.getOutputApertureValue());
                outputApertureValue.setText(outputComboBoxAperture);
                outputApertureValue.setOpacity(1.0);

                //Format List Labels
                    //IMAX (15-Perforation) Film; width of format is 70.41mm
                    double topLeftLabelFocalLengthValue = matchTheseFormats.calculateEquivalentLens(
                            matchTheseFormats.getHorizontalAngleOfView(), 70.41);
                    String topLeftLabelFocalLengthString = labelFormat.format(topLeftLabelFocalLengthValue);
                    imaxLensDisplay.setText(topLeftLabelFocalLengthString + "mm");

                    //Full-Frame; width of format is 36.0mm
                    double topCenterLabelFocalLengthValue = matchTheseFormats.calculateEquivalentLens(
                            matchTheseFormats.getHorizontalAngleOfView(), 36.0);
                    String topCenterLabelFocalLengthString = labelFormat.format(topCenterLabelFocalLengthValue);
                    fullFrameLensDisplay.setText(topCenterLabelFocalLengthString + "mm");

                    //Super-35mm (4-Perforation) Film; width of format is 24.89mm
                    double topRightLabelFocalLengthValue = matchTheseFormats.calculateEquivalentLens(
                            matchTheseFormats.getHorizontalAngleOfView(), 24.89);
                    String topRightLabelFocalLengthString = labelFormat.format(topRightLabelFocalLengthValue);
                    s35LensDisplay.setText(topRightLabelFocalLengthString + "mm");

                    //Super-16mm Film; width of format is 12.52mm
                    double bottomLeftLabelFocalLengthValue = matchTheseFormats.calculateEquivalentLens(
                            matchTheseFormats.getHorizontalAngleOfView(), 12.52);
                    String bottomLeftLabelFocalLengthString = labelFormat.format(bottomLeftLabelFocalLengthValue);
                    s16LensDisplay.setText(bottomLeftLabelFocalLengthString + "mm");

                    //Super-8mm Film; width of format is 5.79mm
                    double bottomCenterLabelFocalLengthValue = matchTheseFormats.calculateEquivalentLens(
                            matchTheseFormats.getHorizontalAngleOfView(), 5.79);
                    String bottomCenterLabelFocalLengthString = labelFormat.format(bottomCenterLabelFocalLengthValue);
                    s8LensDisplay.setText(bottomCenterLabelFocalLengthString + "mm");

                    //iPhone X-Series Sensor; width of format is 5.6mm
                    double bottomRightLabelFocalLengthValue = matchTheseFormats.calculateEquivalentLens(
                            matchTheseFormats.getHorizontalAngleOfView(), 5.6);
                    String bottomRightLabelFocalLengthString = labelFormat.format(bottomRightLabelFocalLengthValue);
                    iPhoneLensDisplay.setText(bottomRightLabelFocalLengthString + "mm");
    }

    public void resetPressed(ActionEvent actionEvent) {
        inputDimensions.setText(initialInputFormatPromptText);
        outputDimensions.setText(initialOutputFormatPromptText);
        inputFocalLength.setText(initialInputFocalLength);
        outputFocalLength.setText(initialOutputFocalLength);
        inputApertureValue.setText(initialInputApertureValue);
        outputApertureValue.setText(initialOutputApertureValue);
        totalDOFDisplay.setText(initialTotalDOFDisplay);
        nearDOFDisplay.setText(initialNearDOFDisplay);
        farDOFDisplay.setText(initialFarDOFDisplay);
        hAOV.setText(initialHAOV);
        vAOV.setText(initialVAOV);
        dAOV.setText(initialDAOV);
        hFOVFeet.setText(initialHFOVFeet);
        vFOVFeet.setText(initialVFOVFeet);
        dFOVFeet.setText(initialDFOVFeet);
        hFOVMeters.setText(initialHFOVMeters);
        vFOVMeters.setText(initialVFOVMeters);
        dFOVMeters.setText(initialDFOVMeters);
        imaxLensDisplay.setText(initialImaxLensDisplay);
        fullFrameLensDisplay.setText(initialFullFrameLensDisplay);
        s35LensDisplay.setText(initialS35LensDisplay);
        s16LensDisplay.setText(initialS16LensDisplay);
        s8LensDisplay.setText(initialS8LensDisplay);
        iPhoneLensDisplay.setText(initialIPhoneLensDisplay);
        distanceSlider.setValue(initialDistanceSlider);
        distanceReadout.setText(initialDistanceValue);
        inputFormatList.setValue(null);
        outputFormatList.setValue(null);
    }
}
