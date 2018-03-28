package com.vaadin.demo.stockdata;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(response -> {
            Document document = response.getDocument();

            Element head = document.head();
            head.appendChild(createMeta(document, "viewport",
                    "width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes"));
            head.appendChild(createMeta(document,
                    "apple-mobile-web-app-capable", "yes"));
            head.appendChild(createMeta(document,
                    "apple-mobile-web-app-status-bar-style", "black"));

            document.body().attr("theme", "dark");
        });
    }

    private Element createMeta(Document document, String name, String content) {
        Element meta = document.createElement("meta");
        meta.attr("name", name);
        meta.attr("content", content);
        return meta;
    }

}