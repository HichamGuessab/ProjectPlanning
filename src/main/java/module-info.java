module main {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires org.mnode.ical4j.core;

    exports main;
    opens main to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
    exports entity;
    opens entity to com.fasterxml.jackson.databind;
    exports model;
    opens model to com.fasterxml.jackson.databind;
    exports service;
}