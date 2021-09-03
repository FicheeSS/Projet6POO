package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.ApplicationEvent;
import ui.Main;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MessageViewController implements Initializable {
    private final ObservableList<MessageObject> items = FXCollections.observableArrayList();
    @FXML
    public ListView<MessageObject> MessageView = new ListView<MessageObject>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main.getAppEventDisp().addListener((ApplicationEvent.events event, Object... params) -> {
            if (event.equals(ApplicationEvent.events.MESSAGE)) {
                items.add(new MessageObject((String) params[0], (ApplicationEvent.messageTypes) params[1]));
            }
        });
        MessageView.setItems(items);
        MessageView.setCellFactory(param -> new ListCell<MessageObject>() {
            private final ImageView imageView = new ImageView();

            @Override
            public void updateItem(MessageObject message, boolean empty) {
                super.updateItem(message, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    switch (message.getMessageTypes()) {
                        case STOCK -> imageView.setImage(new Image(Objects.requireNonNull(MessageViewController.class.getResourceAsStream("./img/stock.png"))));
                        case BIRTHDAY -> imageView.setImage((new Image(Objects.requireNonNull(MessageViewController.class.getResourceAsStream("./img/birthday.png")))));
                        case LIVRAISON -> imageView.setImage(new Image(Objects.requireNonNull(MessageViewController.class.getResourceAsStream("./img/livraison.png"))));
                        case SUCCES -> imageView.setImage(new Image(Objects.requireNonNull(MessageViewController.class.getResourceAsStream("./img/succes.png"))));
                    }
                    imageView.setPreserveRatio(true);
                    imageView.setFitHeight(64);
                    setText(message.getText());
                    setGraphic(imageView);
                }
            }
        });
    }

    private class MessageObject {
        private final String text;
        private final ApplicationEvent.messageTypes messageTypes;

        public MessageObject(String text, ApplicationEvent.messageTypes messageTypes) {
            this.messageTypes = messageTypes;
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public ApplicationEvent.messageTypes getMessageTypes() {
            return messageTypes;
        }
    }
}
