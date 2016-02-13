package com.bustanil.myapp.shared.wicket.component;

import com.bustanil.myapp.shared.wicket.converter.IDBigDecimalConverter;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.resource.JQueryPluginResourceReference;
import org.apache.wicket.util.convert.IConverter;

import java.math.BigDecimal;

import static de.agilecoders.wicket.jquery.JQuery.$;

public class DecimalTextField extends TextField<BigDecimal> {

    public DecimalTextField(String id) {
        super(id);
    }

    public DecimalTextField(String id, Class<BigDecimal> type) {
        super(id, type);
    }

    public DecimalTextField(String id, IModel<BigDecimal> model) {
        super(id, model);
    }

    public DecimalTextField(String id, IModel<BigDecimal> model, Class<BigDecimal> type) {
        super(id, model, type);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        tag.addBehavior(new AttributeAppender("style", "text-align: right"));
        super.onComponentTag(tag);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(
                new JQueryPluginResourceReference(DecimalTextField.class, "autoNumeric-min.js"), "autonumericjs"));

        response.render($(this).chain(new AutoNumericFunction('.', ',', 2)).asDomReadyScript());

    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>) new IDBigDecimalConverter();
    }
}
