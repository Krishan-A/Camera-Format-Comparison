package CameraFormatComparisonPKG;

import java.util.ArrayList;

public class FormatList {

    private ArrayList<Format> formatList;

    FormatList(){
        Format f;

        formatList = new ArrayList<Format>();

        f = new Format("Mamiya RB67 (6x8 Back)", 76.0, 56.0);
        formatList.add(f);
        f = new Format("Pentax 67", 70.0, 56.0);
        formatList.add(f);
        f = new Format("Mamiya RB67 (6x7 Back)", 69.5, 56.0);
        formatList.add(f);

        //Motion Picture Film IMAX Capture
        f = new Format("IMAX Film (15-Perf)", 70.41, 52.63);
        formatList.add(f);
        f = new Format("Alexa 65 (Open Gate 6.5k)", 54.12, 25.58);
        formatList.add(f);
        f = new Format("65mm Film (5-Perf)", 52.63, 23.01);
        formatList.add(f);

        //Motion Picture Film VistaVision
        f = new Format("VistaVision Film (8-Perf)", 37.7, 25.0);
        formatList.add(f);
        f = new Format("Alexa LF (Open Gate 4.5k)",  36.7, 25.54);
        formatList.add(f);

        //Still Film Full-Frame a.k.a. 135
        f = new Format("Full-Frame (135 Film)", 36.0, 24.0);
        formatList.add(f);

        //Super-35mm Formats
        f = new Format("Alexa XT/SXT/Mini (Open Gate 3.4k)", 28.25, 18.17);
        formatList.add(f);
        f = new Format("Alexa Mini (16:9 UHD)", 26.4, 14.85);
        formatList.add(f);
        f = new Format("Super-35mm Film (4-Perf)", 24.89, 18.67);
        formatList.add(f);
        f = new Format("Super-35mm Film (3-Perf)", 24.89, 13.89);
        formatList.add(f);
        f = new Format("Super-35mm Film (2-Perf)", 24.89, 9.35);
        formatList.add(f);
        f = new Format("Alexa Classic (16:9 2k)", 23.66, 13.32);
        formatList.add(f);

        //Other 35mm Formats
        f = new Format("Academy 35mm Film", 22.0, 15.99);
        formatList.add(f);
        f = new Format("Canon Film APS-C", 25.1, 16.7);
        formatList.add(f);
        f = new Format("Canon Digital APS-C (\"Crop Sensor\")", 22.3, 14.9);
        formatList.add(f);

        //Smaller than 35mm Formats
        f = new Format("Micro Four Thirds", 17.3, 13.0);
        formatList.add(f);
        f = new Format("Super 16mm Film", 12.52, 7.41);
        formatList.add(f);
        f = new Format("16mm Film", 10.26, 7.49);
        formatList.add(f);
        f = new Format("Super 8mm Film", 5.79, 4.01);
        formatList.add(f);
        f = new Format("8mm Film", 4.5, 3.3);
        formatList.add(f);
        f = new Format("iPhone X-Series", 5.6, 4.2);
        formatList.add(f);
    }
    public int getCount() {
        return this.formatList.size();
    }

    public Format getFormat(int index) {

        return this.formatList.get(index);
    }
}
