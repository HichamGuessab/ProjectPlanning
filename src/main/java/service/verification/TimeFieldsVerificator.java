package service.verification;

public class TimeFieldsVerificator {
    public static String getErrorMessage(String startTime, String endTime) {
        String errorMessage = isStringCorrectTimeFormat(endTime);
        if(!errorMessage.isEmpty()) {
            return errorMessage;
        }
        errorMessage = isStringCorrectTimeFormat(startTime);
        if(!errorMessage.isEmpty()) {
            return errorMessage;
        }

        String[] startTimeParts = startTime.split(":");
        String[] endTimeParts = endTime.split(":");

        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);
        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);
        if(startHour > endHour) {
            return "L'heure de début doit être avant l'heure de fin";
        }
        if(startHour == endHour && startMinute >= endMinute) {
            return "L'heure de début doit être avant l'heure de fin";
        }
        if(startHour < 8 || endHour > 20 || (endHour == 20 && endMinute > 0)) {
            return "Les événements doivent être entre 8h et 20h";
        }
        return "";
    }

    private static String isStringCorrectTimeFormat(String time) {
        if(!time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            return "Le temps doit être au format HH:MM";
        }
        String[] timeParts = time.split(":");
        int minutes = Integer.parseInt(timeParts[1]);
        if(minutes % 30 != 0) {
            return "Les minutes doivent être 00 ou 30";
        }
        return "";
    }
}
