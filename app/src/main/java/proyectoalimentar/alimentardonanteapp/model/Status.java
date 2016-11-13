package proyectoalimentar.alimentardonanteapp.model;


import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;

public enum Status {
    OPEN(AlimentarApp.getContext().getString(R.string.open), "open"),
    ACTIVE(AlimentarApp.getContext().getString(R.string.active), "active"),
    FINISHED(AlimentarApp.getContext().getString(R.string.finished), "finished"),
    CANCELLED(AlimentarApp.getContext().getString(R.string.cancelled), "cancelled"),
    UNKOWN("Indefinida" , "unkown");

    private String messageToDisplay;
    private String jsonValue;

    Status(String messageToDisplay, String jsonValue){
        this.messageToDisplay = messageToDisplay;
        this.jsonValue = jsonValue;
    }

    public String getMessageToDisplay() {
        return messageToDisplay;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    @Override
    public String toString() {
        return getMessageToDisplay();
    }
}
