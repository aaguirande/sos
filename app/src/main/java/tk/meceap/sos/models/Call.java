package tk.meceap.sos.models;

public class Call {
    String callId, location, distanceValue, distanceText, durationText, durationValue;

    public Call() {
    }

    public Call(String callId, String location,
                String distanceValue, String distanceText,
                String durationValue, String durationText) {
        this.callId = callId;
        this.location = location;
        this.distanceValue = distanceValue;
        this.distanceText = distanceText;
        this.durationText = durationText;
        this.durationValue = durationValue;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(String distanceValue) {
        this.distanceValue = distanceValue;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(String durationValue) {
        this.durationValue = durationValue;
    }

    @Override
    public String toString() {
        return "Call{" +
                "callId='" + callId + '\'' +
                ", location='" + location + '\'' +
                ", distanceValue='" + distanceValue + '\'' +
                ", distanceText='" + distanceText + '\'' +
                ", durationText='" + durationText + '\'' +
                ", durationValue='" + durationValue + '\'' +
                '}';
    }
}
