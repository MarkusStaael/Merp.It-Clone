module org.example.firstyearproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.junit.jupiter.api;
    requires java.desktop;
    requires java.sql;


    opens org.example.firstyearproject to javafx.fxml;
    exports org.example.firstyearproject;
    exports org.example.firstyearproject.DataTypes;
    opens org.example.firstyearproject.DataTypes to javafx.fxml;
    exports org.example.firstyearproject.MapObjects;
    opens org.example.firstyearproject.MapObjects to javafx.fxml;
    exports org.example.firstyearproject.Algorithms;
    opens org.example.firstyearproject.Algorithms to javafx.fxml;
    exports org.example.firstyearproject.Misc;
    opens org.example.firstyearproject.Misc to javafx.fxml;
}