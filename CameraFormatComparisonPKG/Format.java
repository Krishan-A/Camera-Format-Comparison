package CameraFormatComparisonPKG;

public class Format {

    private String description;
    private double widthMM;   //Units in millimeters
    private double heightMM;  //Units in millimeters

    Format(String description, double widthMM, double heightMM) {
        this.description = description;
        this.widthMM = widthMM;
        this.heightMM = heightMM;
    }

    public String getDesc() {
        return this.description;
    }

    public String getDimensions() { return String.valueOf(widthMM) + "mm x " + String.valueOf(heightMM) + "mm";}

    public double getWidth() {
        return this.widthMM;
    }

    public double getHeight(){
        return this.heightMM;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public void setWidth(double wMM) {
        this.widthMM = wMM;
    }

    public void setHeight(double hMM) {
        this.heightMM = hMM;
    }

    public double calculateDiagonal(){
        double diagonal;
        diagonal = Math.sqrt((this.widthMM * this.widthMM) + (this.heightMM * this.heightMM));
        return diagonal;
    }

    public double calculateCircleOfConfusion(){
        double circleOfConfusion;
        circleOfConfusion = this.calculateDiagonal()/1500.0;
        return circleOfConfusion;
    }

    public boolean isEqual(Format format) {
        if (this.description.equalsIgnoreCase(format.description)) {
            return true;
        } else {
            return false;
        }
    }
}
