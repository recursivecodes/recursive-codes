package codes.recursive

import java.beans.PropertyEditorSupport
import java.text.SimpleDateFormat

class DateConverter extends PropertyEditorSupport {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    def value
    String getAsText() {
        return formatter.format(value)
    }
    void setAsText(String text) {
       value = text
    }
}