package CameraFormatComparisonPKG;

public class FormatCalculations {
    private Format inputFormat, outputFormat;
    private static FormatList formatList = new FormatList();
    private final double FEET_TO_METER_SCALAR = 3.28084;
    private final double FEET_TO_MILLIMETER_SCALAR = 304.799990246;
    private final double MILLIMETER_TO_METER_SCALAR = 1000.0;
    private final double RADIANS_TO_DEGREES_SCALAR = 57.2957795131; //Decimal approximation of 180/pi
    private double inputFormatWidth;
    private double inputFormatHeight;
    private double outputFormatWidth;
    private double outputFormatHeight;
    private double inputFocalLengthInMM; //Units in millimeters
    private double inputApertureValue;
    private double outputApertureValue;
    private double focalDistanceInFeet;
    private double circleOfConfusion;
    private double nearDepthOfFieldInFeet, nearDepthOfFieldInMeters, farDepthOfFieldInFeet, farDepthOfFieldInMeters,
            totalDepthOfFieldInFeet, totalDepthOfFieldInMeters;
    private double horizontalAngleOfView, verticalAngleOfView, diagonalAngleOfView;
    private double horizontalFieldOfViewFeet, verticalFieldOfViewFeet, diagonalFieldOfViewFeet,
            horizontalFieldOfViewMeters, verticalFieldOfViewMeters, diagonalFieldOfViewMeters;


/*
Method: FormatCalculations()
Description: A constructor that takes in all of the information from the user-entry areas of the UI and stores all of
             the values necessary to populate the remaining fields in the UI to compare two formats.
Input:
    inputFormatListIndex - An integer that represents the input format within a position of an ArrayList stored and
                           maintained in the Format List class.
    outputFormatListIndex - An integer that represents the output format within a position of an ArrayList stored and
                           maintained in the Format List class.
    inputFocalLength - The focal length value of the input lens that the program is trying to match to a different format
    inputAV - The Aperture Value (AV) setting that the input lens is set to. This influences the depth of field and, along
              with the focal length, will determine the aperture value of the output lens.
    focusDistanceMeasurementInFeet - The distance from the image plane to the subject.
Output: Creates an object
Returns: No return type (N/A for a constructor)
*/

    FormatCalculations(int inputFormatListIndex, int outputFormatListIndex, double inputFocalLength,
                       double inputAV, double focusDistanceMeasurementInFeet){
        this.inputFormat = formatList.getFormat(inputFormatListIndex);
        this.inputFormatWidth = inputFormat.getWidth();
        this.inputFormatHeight = inputFormat.getHeight();
        this.outputFormat = formatList.getFormat(outputFormatListIndex);
        this.outputFormatWidth = outputFormat.getWidth();
        this.outputFormatHeight = outputFormat.getHeight();
        this.inputFocalLengthInMM = inputFocalLength;
        this.inputApertureValue = inputAV;
        this.circleOfConfusion = formatList.getFormat(inputFormatListIndex).calculateCircleOfConfusion();
        this.focalDistanceInFeet = focusDistanceMeasurementInFeet;
        this.nearDepthOfFieldInFeet = calculateNearDepthOfFieldLimit(focusDistanceMeasurementInFeet, inputFocalLength,
                inputAV, formatList.getFormat(inputFormatListIndex).calculateCircleOfConfusion()) / FEET_TO_MILLIMETER_SCALAR;
        this.nearDepthOfFieldInMeters = calculateNearDepthOfFieldLimit(focusDistanceMeasurementInFeet, inputFocalLength,
                inputAV, formatList.getFormat(inputFormatListIndex).calculateCircleOfConfusion()) / MILLIMETER_TO_METER_SCALAR;
        this.farDepthOfFieldInFeet = calculateFarDepthOfFieldLimit(focusDistanceMeasurementInFeet, inputFocalLength,
                inputAV, formatList.getFormat(inputFormatListIndex).calculateCircleOfConfusion()) / FEET_TO_MILLIMETER_SCALAR;
        this.farDepthOfFieldInMeters = calculateFarDepthOfFieldLimit(focusDistanceMeasurementInFeet, inputFocalLength,
                inputAV, formatList.getFormat(inputFormatListIndex).calculateCircleOfConfusion()) / MILLIMETER_TO_METER_SCALAR;
        this.totalDepthOfFieldInFeet = calculateTotalDepthOfFieldInMeters(nearDepthOfFieldInFeet, farDepthOfFieldInFeet);
        this.totalDepthOfFieldInMeters = calculateTotalDepthOfFieldInMeters(nearDepthOfFieldInMeters, farDepthOfFieldInMeters);
        this.horizontalAngleOfView = calculateAngleOfViewInDegrees(inputFormatWidth, inputFocalLengthInMM);
        this.verticalAngleOfView = calculateAngleOfViewInDegrees(inputFormatHeight, inputFocalLengthInMM);
        this.diagonalAngleOfView = calculateAngleOfViewInDegrees(calculateDiagonal(inputFormatWidth, inputFormatHeight),
                inputFocalLengthInMM);
        this.horizontalFieldOfViewFeet = calculateFieldOfView(focusDistanceMeasurementInFeet, horizontalAngleOfView);
        this.verticalFieldOfViewFeet = calculateFieldOfView(focusDistanceMeasurementInFeet, verticalAngleOfView);
        this.diagonalFieldOfViewFeet = calculateFieldOfView(focusDistanceMeasurementInFeet, diagonalAngleOfView);
        this.horizontalFieldOfViewMeters = feetToMetersConversion(calculateFieldOfView(focusDistanceMeasurementInFeet,
                horizontalAngleOfView));
        this.verticalFieldOfViewMeters = feetToMetersConversion(calculateFieldOfView(focusDistanceMeasurementInFeet,
                verticalAngleOfView));
        this.diagonalFieldOfViewMeters = feetToMetersConversion(calculateFieldOfView(focusDistanceMeasurementInFeet,
                diagonalAngleOfView));
        this.outputApertureValue = calculateEquivalentApertureValue(inputFormatWidth, outputFormatWidth,
                inputApertureValue);
    }

/*
Method: metersToFeetConversion()
Description: Converts the units of a length measurement from meters to feet
Input: double valueInMeters - the length measurement in metric units
Output: None
Returns: double valueInFeet - the length measurement in imperial units
*/

    private double metersToFeetConversion(double valueInMeters){
        double valueInFeet;
        valueInFeet = valueInMeters * FEET_TO_METER_SCALAR;
        return valueInFeet;
    }

/*
Method: feetToMetersConversion()
Description: Converts the units of a length measurement from meters to feet
Input: double valueInFeet - the length measurement in imperial units
Output: None
Returns: double valueInMeters - the length measurement in metric units
*/

    private double feetToMetersConversion(double valueInFeet){
        double valueInMeters;
        valueInMeters = valueInFeet / FEET_TO_METER_SCALAR;
        return valueInMeters;
    }

/*
Method: calculateHyperFocalDistance()
Description: Calculates the minimum focus distance setting required for everything from that distance to infinity to be
            in acceptable focus. This value is required for the depth of field formulas used in other methods.
Input: double focalLengthInMM - The focal length of the lens
       double apertureValue - The aperture value of that lens
       double circleOfConfusion - A measurement required to determine acceptable sharpness. Only used in depth of field
                                  formulas.
Output: None
Returns: double hyperfocalDistanceValue - units in millimeters
*/

    private double calculateHyperFocalDistance(double focalLengthInMM, double apertureValue, double circleOfConfusion){
        double hyperfocalDistanceValue;
        hyperfocalDistanceValue =
                ((focalLengthInMM * focalLengthInMM) / (apertureValue * circleOfConfusion)) + focalLengthInMM;
        return hyperfocalDistanceValue;
    }

/*
Method: calculateNearDepthOfFieldLimit()
Description: Calculates the closest distance from the image plane where objects appear acceptably in-focus
Input: double focalDistanceInFeet - Subject distance from the lens
       double focalLengthInMM - The focal length of the lens
       double apertureValue - The aperture value of that lens
       double circleOfConfusion - A measurement required to determine acceptable sharpness. Required for this formula.
Output: None
Returns: double nearDepthOfFieldLimitInMM - units in millimeters
*/

    private double calculateNearDepthOfFieldLimit(double focalDistanceInFeet, double focalLengthInMM,
                                                  double apertureValue, double circleOfConfusion){
        double nearDepthOfFieldLimitInMM;
        double focalDistanceInMM = focalDistanceInFeet * FEET_TO_MILLIMETER_SCALAR;
        double hyperFocalDistanceInMM = calculateHyperFocalDistance(focalLengthInMM, apertureValue, circleOfConfusion);

        nearDepthOfFieldLimitInMM = ((focalDistanceInMM * (hyperFocalDistanceInMM - focalLengthInMM)) / (
                (hyperFocalDistanceInMM + focalDistanceInMM) - (2 * focalLengthInMM)));

        return nearDepthOfFieldLimitInMM;
    }

/*
Method: calculateFarDepthOfFieldLimit()
Description: Calculates the farthest distance from the image plane where objects still appear acceptably in-focus
Input: double focalDistanceInFeet - Subject distance from the lens
       double focalLengthInMM - The focal length of the lens
       double apertureValue - The aperture value of that lens
       double circleOfConfusion - A measurement required to determine acceptable sharpness. Required for this formula.
Output: None
Returns: double farDepthOfFieldLimitInMM - units in millimeters
*/

    private double calculateFarDepthOfFieldLimit(double focalDistanceInFeet, double focalLengthInMM,
                                                  double apertureValue, double circleOfConfusion){
        double farDepthOfFieldLimitInMM;
        double focalDistanceInMM = focalDistanceInFeet * FEET_TO_MILLIMETER_SCALAR;
        double hyperFocalDistanceInMM = calculateHyperFocalDistance(focalLengthInMM, apertureValue, circleOfConfusion);

        farDepthOfFieldLimitInMM = ((focalDistanceInMM * (hyperFocalDistanceInMM - focalLengthInMM)) / (
                (hyperFocalDistanceInMM - focalDistanceInMM)));

        return farDepthOfFieldLimitInMM;
    }

/*
Method: calculateTotalDepthOfFieldInMeters()
Description: Calculates the farthest distance from the image plane where objects still appear acceptably in-focus
Input: double nearDepthOfField - Closest distance from the image plane where objects appear acceptably in-focus
       double farDepthOfField - Farthest distance from the image plane where objects still appear acceptably in-focus
Output: None
Returns: double totalDepthOfField - units in millimeters
*/

    private double calculateTotalDepthOfFieldInMeters(double nearDepthOfField, double farDepthOfField){
        double totalDepthOfField = farDepthOfField - nearDepthOfField;
        return totalDepthOfField;
    }

/*
Method: radiansToDegrees()
Description: Converts the value of an angle from radians to degrees
Input: double angleInRadians - The value of an angle in metric units (radians)
Output: None
Returns: double angleInDegrees - The value of an angle in non-metric units
*/

    private double radiansToDegrees(double angleInRadians){
        double angleInDegrees;
        angleInDegrees = angleInRadians * RADIANS_TO_DEGREES_SCALAR;
        return angleInDegrees;
    }

/*
Method: calculateAngleOfViewInDegrees()
Description: Calculates the angle of view of a specific lens on a specific camera format. This is one of several methods
             that exist, but this is designed for contemporary lenses that focus internally. For a bellows-focus camera
             such as an 8x10 View Camera or the Mamiya RB67 camera in this application, another formula is required to
             account for the movement of the lens in relation to the subject it's photographing.
Input: double frameDimensionInMM - Either the width, height, OR diagonal of the format
       double focalLengthInMM - The focal length of the internal-focus lens
Output: None
Returns: double angleOfView - The value of an angle in degrees
*/

    private double calculateAngleOfViewInDegrees(double frameDimensionInMM, double focalLengthInMM){
        double angleOfView;
        angleOfView = 2.0 * (Math.atan((frameDimensionInMM / (2.0 * focalLengthInMM))));
        angleOfView = radiansToDegrees(angleOfView);
        return angleOfView;
    }

/*
Method: calculateDiagonal()
Description: Converts the value of an angle from radians to degrees
Input: double width - One side of a rectangle
       double height - One adjacent side of that rectangle
Output: None
Returns: double diagonal - The length of the diagonal line formed between the vertices of the two sides inputted
*/

    private double calculateDiagonal(double width, double height){
        double diagonal;
        diagonal = Math.sqrt(Math.pow(width, 2.0) + Math.pow(height, 2.0));
        return diagonal;
    }

/*
Method: calculateFieldOfView()
Description: Calculates the field of view at a certain distance from the image plane with a specific angle of view. This
             is important in planning because it answers questions such as, "how big does the green/blue screen need to
             be to fill the space behind the actor on a certain lens" or "what lens should we use if the camera is
             shooting a car interior (limited distance between subject and image plane) and we want to show the entire
             back seat where two goons flank our kidnapped hero".
Input: double focalDistanceInFeet - Distance from the subject to the plane in the camera where the image is projected
       double angleOfViewInDegrees - Stored in the FormatCalculations object in degrees, but Java's Math library requires
                                     radians. Units are converted within the method.
Output: None
Returns: double fieldOfViewInFeet - The view (width, height, or diagonal) in imperial units. The formula accepts any
                                    units. Since the distance value is stored in the FormatCalculations object in feet
                                    and the UI's slider input uses feet, this method returns feet. Units can be converted
                                    elsewhere as needed.
*/

        private double calculateFieldOfView(double focalDistanceInFeet, double angleOfViewInDegrees){
            double fieldOfViewInFeet;
            double angleOfViewInRadians = angleOfViewInDegrees / RADIANS_TO_DEGREES_SCALAR;

            fieldOfViewInFeet = ((2.0 * focalDistanceInFeet) *
                    (Math.tan(angleOfViewInRadians / 2.0)));

            return fieldOfViewInFeet;
        }

/*
Method: calculateFieldOfView()
Description: Calculates the focal length of the lens necessary to match the framing of the output (desired) camera
             format with that of the input (starting) format
Input: double horizontalAngleOfViewInDegrees - The horizontal angle that the camera "sees". Stored as degrees in the
                                               FormatCalculations object and converted to radians for the Java Math library
       double horizontalFormatDimension - The width of the output (desired) camera format
Output: None
Returns: double equivalentFocalLength - The focal length of the lens necessary to match the framing of the output
                                        (desired) camera format with that of the input (starting) format
*/
    public double calculateEquivalentLens(double horizontalAngleOfViewInDegrees, double horizontalFormatDimension){
            double equivalentFocalLength;
            double horizontalAngleOfViewInRadians = horizontalAngleOfViewInDegrees / RADIANS_TO_DEGREES_SCALAR;
            equivalentFocalLength = (horizontalFormatDimension /
                    (2.0 * (Math.tan(horizontalAngleOfViewInRadians / 2.0))));

            return equivalentFocalLength;
    }

/*
Method: calculateEquivalentApertureValue()
Description: Calculates the focal length of the lens necessary to match the framing of the output (desired) camera
             format with that of the input (starting) format
Input: double inputFormatWidth - The horizontal measurement of the input format in millimeters
       double outputFormatWidth - The horizontal measurement of the output format in millimeters
       double inputApertureValue - The aperture value entered into the UI by the user
Output: None
Returns: double equivalentApertureValue - The aperture value of the lens necessary to match the depth of field of the
                                          output (desired) camera format with that of the input (starting) aperture value
*/

    private double calculateEquivalentApertureValue(double inputFormatWidth, double outputFormatWidth,
                                                    double inputApertureValue){
        double equivalentApertureValue;
        equivalentApertureValue = ((outputFormatWidth / inputFormatWidth) * inputApertureValue);
        return equivalentApertureValue;
    }

    public double getNearDepthOfFieldInFeet() {
        return nearDepthOfFieldInFeet;
    }

    public double getNearDepthOfFieldInMeters() {
        return nearDepthOfFieldInMeters;
    }

    public double getFarDepthOfFieldInFeet() {
        return farDepthOfFieldInFeet;
    }

    public double getFarDepthOfFieldInMeters() {
        return farDepthOfFieldInMeters;
    }

    public double getTotalDepthOfFieldInFeet() {
        return totalDepthOfFieldInFeet;
    }

    public double getTotalDepthOfFieldInMeters() {
        return totalDepthOfFieldInMeters;
    }

    public double getHorizontalAngleOfView() {
        return horizontalAngleOfView;
    }

    public double getVerticalAngleOfView() {
        return verticalAngleOfView;
    }

    public double getDiagonalAngleOfView() {
        return diagonalAngleOfView;
    }

    public double getHorizontalFieldOfViewFeet() {
        return horizontalFieldOfViewFeet;
    }

    public double getVerticalFieldOfViewFeet() {
        return verticalFieldOfViewFeet;
    }

    public double getDiagonalFieldOfViewFeet() {
        return diagonalFieldOfViewFeet;
    }

    public double getHorizontalFieldOfViewMeters() {
        return horizontalFieldOfViewMeters;
    }

    public double getVerticalFieldOfViewMeters() {
        return verticalFieldOfViewMeters;
    }

    public double getDiagonalFieldOfViewMeters() {
        return diagonalFieldOfViewMeters;
    }

    public double getOutputFormatWidth() {
        return outputFormatWidth;
    }

    public double getOutputApertureValue() {
        return outputApertureValue;
    }


}
