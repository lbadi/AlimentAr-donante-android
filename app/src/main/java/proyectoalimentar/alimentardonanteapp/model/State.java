package proyectoalimentar.alimentardonanteapp.model;


import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;

public enum State {
    OPEN(AlimentarApp.getContext().getString(R.string.open)),
    ACTIVE(AlimentarApp.getContext().getString(R.string.active)),
    FINISHED(AlimentarApp.getContext().getString(R.string.finished)),
    CANCELLED(AlimentarApp.getContext().getString(R.string.cancelled));

    private String messageToDisplay;

    State(String messageToDisplay){
        this.messageToDisplay = messageToDisplay;
    }

    public String getMessageToDisplay() {
        return messageToDisplay;
    }

    @Override
    public String toString() {
        return messageToDisplay;
    }
}
