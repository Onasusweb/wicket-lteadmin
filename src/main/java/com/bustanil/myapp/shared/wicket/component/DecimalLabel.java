package com.bustanil.myapp.shared.wicket.component;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

public class DecimalLabel extends Label {

    public DecimalLabel(String id) {
        super(id);
    }

    public DecimalLabel(String id, Serializable label) {
        super(id, label);
    }

    public DecimalLabel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.addBehavior(new AttributeAppender("style", "text-align: right"));
    }
}
