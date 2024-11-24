module ca.mcjammers.codejam14 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires javafx.web;
    requires html2pdf;
    requires org.slf4j;
    requires layout;
    requires kernel;
    requires io;


    opens ca.mcjammers.codejam14 to javafx.fxml;
    exports ca.mcjammers.codejam14;
}