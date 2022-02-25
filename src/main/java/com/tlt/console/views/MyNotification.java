package com.tlt.console.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MyNotification {

    public void success(String message){
        Icon icon = VaadinIcon.CHECK_CIRCLE.create();
        Div info = new Div(new Text(message));

        HorizontalLayout layout = new HorizontalLayout(
                icon, info);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.add(layout);
        notification.setDuration(2000);
        notification.open();
    }

    public void error(String error){
        Icon icon = VaadinIcon.WARNING.create();
        Div info = new Div(new Text(error));

        HorizontalLayout layout = new HorizontalLayout(
                icon, info);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.add(layout);
        notification.setDuration(2000);
        notification.open();
    }

}
